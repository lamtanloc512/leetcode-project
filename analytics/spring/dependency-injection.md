# Dependency Injection trong Spring (Tiêm phụ thuộc)

Tài liệu này giải thích chi tiết cơ chế DI của Spring: nó là gì, các loại injection, cách hoạt động bên trong, và best practices.

---

## Mục lục

1. [Dependency Injection là gì?](#1-dependency-injection-là-gì)
2. [Các loại Injection](#2-các-loại-injection)
3. [Cơ chế hoạt động bên trong](#3-cơ-chế-hoạt-động-bên-trong)
4. [@Qualifier và @Primary](#4-qualifier-và-primary)
5. [Xử lý Circular Dependency](#5-xử-lý-circular-dependency)
6. [Best Practices](#6-best-practices)

---

## 1. Dependency Injection là gì?

**Định nghĩa:** Dependency Injection (DI) là pattern mà dependencies (các đối tượng mà class cần) được "tiêm" vào từ bên ngoài thay vì class tự tạo.

**Không có DI:**

```java
public class OrderService {
    // Tự tạo dependency → tight coupling
    private final PaymentService paymentService = new PaymentService();
    private final InventoryService inventoryService = new InventoryService();
}
```

**Có DI:**

```java
@Service
public class OrderService {
    // Dependencies được inject từ bên ngoài → loose coupling
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    
    public OrderService(PaymentService paymentService, InventoryService inventoryService) {
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
    }
}
```

**Lợi ích:**
- **Testable:** Dễ dàng mock dependencies trong unit test
- **Loose coupling:** Class không cần biết cách tạo dependency
- **Flexible:** Có thể thay đổi implementation mà không sửa code

---

## 2. Các loại Injection

### 2.1. Constructor Injection (KHUYẾN NGHỊ)

**Là gì:** Dependencies được truyền qua constructor.

```java
@Service
public class OrderService {
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    
    // Từ Spring 4.3+, @Autowired không bắt buộc nếu chỉ có 1 constructor
    public OrderService(PaymentService paymentService, 
                        InventoryService inventoryService) {
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
    }
}
```

**Ưu điểm:**
- Fields có thể `final` → immutable
- Dễ test với mock
- Fail-fast: thiếu dependency → lỗi ngay lúc startup
- Dependencies rõ ràng trong constructor

**Khi nào dùng:** Mọi lúc có thể. Đây là cách được khuyến nghị.

### 2.2. Setter Injection

**Là gì:** Dependencies được inject qua setter method.

```java
@Service
public class ReportService {
    private EmailService emailService;
    
    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
```

**Ưu điểm:**
- Cho phép optional dependencies
- Có thể thay đổi dependency sau khi tạo object

**Nhược điểm:**
- Không thể `final`
- Object có thể ở trạng thái không hoàn chỉnh

**Khi nào dùng:** 
- Optional dependencies
- Giải quyết circular dependency

### 2.3. Field Injection (KHÔNG KHUYẾN NGHỊ)

**Là gì:** Dependencies được inject trực tiếp vào field.

```java
@Service
public class NotificationService {
    @Autowired
    private EmailService emailService; // ❌ Không khuyến nghị
    
    @Autowired
    private SmsService smsService; // ❌ Không khuyến nghị
}
```

**Nhược điểm:**
- Không thể `final`
- Khó test (cần reflection hoặc framework hỗ trợ)
- Dependencies bị ẩn (không thấy trong constructor)
- Có thể null nếu không inject đúng

**Khi nào dùng:** Chỉ trong test class hoặc code cũ không refactor được.

### 2.4. So sánh

| Tiêu chí | Constructor | Setter | Field |
|----------|-------------|--------|-------|
| Immutable (final) | ✅ | ❌ | ❌ |
| Dễ test | ✅ | ⚠️ | ❌ |
| Dependencies rõ ràng | ✅ | ⚠️ | ❌ |
| Optional dependency | ❌ | ✅ | ⚠️ |
| Circular dependency | ❌ | ✅ | ✅ |

---

## 3. Cơ chế hoạt động bên trong

### 3.1. AutowiredAnnotationBeanPostProcessor

Đây là class chính xử lý `@Autowired`, `@Value`, và `@Inject`:

```
spring-beans/.../AutowiredAnnotationBeanPostProcessor.java
```

**Luồng xử lý:**

1. **`postProcessMergedBeanDefinition`**: Khi bean definition được merge, processor quét class để tìm tất cả fields/methods/constructors có `@Autowired`.

2. **`findInjectionMetadata`**: Tạo `InjectionMetadata` chứa danh sách các `InjectedElement` (field hoặc method cần inject).

3. **`determineCandidateConstructors`**: Xác định constructor nào sẽ dùng để tạo bean:
   - Nếu có 1 constructor duy nhất → dùng nó
   - Nếu có nhiều constructor, chọn cái có `@Autowired`
   - Nếu nhiều `@Autowired(required=false)` → chọn cái có nhiều dependencies nhất mà Spring có thể satisfy

4. **`postProcessProperties`**: Sau khi bean được tạo, inject các field và gọi setter methods.

```java
// Pseudocode của quá trình
public class AutowiredAnnotationBeanPostProcessor {
    
    // Cache để tránh scan lại
    private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>();
    
    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, 
                                                 Class<?> beanType, String beanName) {
        InjectionMetadata metadata = findInjectionMetadata(beanName, beanType);
        metadata.checkConfigMembers(beanDefinition);
    }
    
    private InjectionMetadata findInjectionMetadata(String beanName, Class<?> clazz) {
        // Check cache first
        InjectionMetadata metadata = injectionMetadataCache.get(beanName);
        if (metadata == null) {
            metadata = buildAutowiringMetadata(clazz); // Scan class
            injectionMetadataCache.put(beanName, metadata);
        }
        return metadata;
    }
}
```

### 3.2. QualifierAnnotationAutowireCandidateResolver

Khi có nhiều bean cùng type, class này quyết định bean nào sẽ được inject:

```java
// Từ spring-beans/.../QualifierAnnotationAutowireCandidateResolver.java

@Override
public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
    // 1. Kiểm tra bean có được phép autowire không
    if (!super.isAutowireCandidate(bdHolder, descriptor)) {
        return false;
    }
    
    // 2. Kiểm tra @Qualifier annotations
    Boolean checked = checkQualifiers(bdHolder, descriptor.getAnnotations());
    
    // 3. Nếu có qualifier và match → true
    // Nếu có qualifier nhưng không match → false
    // Nếu không có qualifier → dựa vào defaultCandidate
    return (checked == Boolean.TRUE || 
            (checked == null && bd.isDefaultCandidate()));
}
```

**Thứ tự ưu tiên khi resolve:**

1. **@Qualifier match**: Bean có cùng qualifier value
2. **Bean name match**: Nếu field name trùng với bean name
3. **@Primary**: Bean được đánh dấu là primary
4. **Single candidate**: Nếu chỉ có 1 bean duy nhất

---

## 4. @Qualifier và @Primary

### 4.1. Vấn đề: Nhiều bean cùng type

```java
public interface NotificationService { ... }

@Service
public class EmailNotificationService implements NotificationService { ... }

@Service
public class SmsNotificationService implements NotificationService { ... }

@Service
public class OrderService {
    @Autowired
    private NotificationService notificationService; // ❌ Lỗi! Có 2 candidates
}
```

### 4.2. Giải pháp 1: @Qualifier

```java
@Service
@Qualifier("email")
public class EmailNotificationService implements NotificationService { ... }

@Service
@Qualifier("sms")
public class SmsNotificationService implements NotificationService { ... }

@Service
public class OrderService {
    @Autowired
    @Qualifier("email") // Chỉ định rõ muốn bean nào
    private NotificationService notificationService;
}
```

### 4.3. Giải pháp 2: @Primary

```java
@Service
@Primary // Đây là default khi không specify qualifier
public class EmailNotificationService implements NotificationService { ... }

@Service
public class SmsNotificationService implements NotificationService { ... }

@Service
public class OrderService {
    @Autowired
    private NotificationService notificationService; // → EmailNotificationService
}
```

### 4.4. Giải pháp 3: Inject tất cả (List/Map)

```java
@Service
public class NotificationFacade {
    
    private final List<NotificationService> services; // Inject tất cả implementations
    
    public NotificationFacade(List<NotificationService> services) {
        this.services = services;
    }
    
    public void notifyAll(String message) {
        services.forEach(s -> s.send(message));
    }
}

// Hoặc dùng Map với bean name làm key
@Service
public class NotificationFacade {
    
    private final Map<String, NotificationService> services;
    
    public NotificationFacade(Map<String, NotificationService> services) {
        this.services = services; // {"emailNotificationService": ..., "smsNotificationService": ...}
    }
}
```

### 4.5. Custom Qualifier Annotation

```java
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface NotificationType {
    String value();
}

@Service
@NotificationType("email")
public class EmailNotificationService implements NotificationService { ... }

@Service
public class OrderService {
    @Autowired
    @NotificationType("email")
    private NotificationService notificationService;
}
```

---

## 5. Xử lý Circular Dependency

### 5.1. Vấn đề

```java
@Service
public class ServiceA {
    @Autowired
    private ServiceB serviceB; // A cần B
}

@Service
public class ServiceB {
    @Autowired
    private ServiceA serviceA; // B cần A → Circular!
}
```

**Lỗi với constructor injection:**

```
BeanCurrentlyInCreationException: Error creating bean with name 'serviceA': 
Requested bean is currently in creation: Is there an unresolvable circular reference?
```

### 5.2. Giải pháp

**Cách 1: Setter injection (một trong hai)**

```java
@Service
public class ServiceA {
    private ServiceB serviceB;
    
    @Autowired
    public void setServiceB(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}
```

**Cách 2: @Lazy**

```java
@Service
public class ServiceA {
    private final ServiceB serviceB;
    
    public ServiceA(@Lazy ServiceB serviceB) { // Tạo proxy, inject thực sau
        this.serviceB = serviceB;
    }
}
```

**Cách 3: ObjectProvider (Lazy lookup)**

```java
@Service
public class ServiceA {
    private final ObjectProvider<ServiceB> serviceBProvider;
    
    public ServiceA(ObjectProvider<ServiceB> serviceBProvider) {
        this.serviceBProvider = serviceBProvider;
    }
    
    public void doSomething() {
        ServiceB serviceB = serviceBProvider.getObject(); // Lấy khi cần
    }
}
```

**Cách 4: Refactor (tốt nhất)**

Circular dependency thường là dấu hiệu thiết kế xấu. Tách logic chung ra service thứ 3:

```java
@Service
public class CommonService {
    // Logic dùng chung
}

@Service
public class ServiceA {
    private final CommonService commonService;
}

@Service
public class ServiceB {
    private final CommonService commonService;
}
```

---

## 6. Best Practices

### 6.1. Luôn dùng Constructor Injection

```java
// ✅ Tốt
@Service
@RequiredArgsConstructor // Lombok generate constructor
public class OrderService {
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
}

// ❌ Tránh
@Service
public class OrderService {
    @Autowired
    private PaymentService paymentService;
}
```

### 6.2. Inject interface, không phải implementation

```java
// ✅ Tốt - loose coupling
private final PaymentService paymentService;

// ❌ Tránh - tight coupling
private final StripePaymentService paymentService;
```

### 6.3. Đặt tên bean rõ ràng với @Qualifier

```java
// Config class
@Configuration
public class DataSourceConfig {
    
    @Bean
    @Qualifier("primary")
    public DataSource primaryDataSource() { ... }
    
    @Bean
    @Qualifier("readonly")
    public DataSource readonlyDataSource() { ... }
}

// Usage
@Repository
public class UserRepository {
    public UserRepository(@Qualifier("primary") DataSource dataSource) { ... }
}
```

### 6.4. Sử dụng @ConfigurationProperties cho config

```java
@ConfigurationProperties(prefix = "app.payment")
@Validated
public class PaymentProperties {
    @NotBlank
    private String apiKey;
    
    @Min(1000)
    private int timeout = 5000;
    
    // getters, setters
}

@Service
public class PaymentService {
    private final PaymentProperties properties;
    
    public PaymentService(PaymentProperties properties) {
        this.properties = properties;
    }
}
```

### 6.5. Checklist

- [ ] Sử dụng constructor injection cho tất cả required dependencies
- [ ] Đánh dấu fields là `final`
- [ ] Inject interface thay vì implementation cụ thể
- [ ] Dùng `@Qualifier` khi có nhiều beans cùng type
- [ ] Refactor circular dependencies thay vì dùng workaround
- [ ] Test với mock objects
