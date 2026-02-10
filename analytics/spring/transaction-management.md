# Quản lý Giao dịch trong Spring (Transaction Management)

Tài liệu này giải thích chi tiết mọi khía cạnh của transaction trong Spring: nó là gì, cách dùng, khi nào dùng, trade-off, xử lý exception, và các usecase thực tế.

---

## Mục lục

1. [Tại sao Spring có module ORM/JPA riêng?](#1-tại-sao-spring-có-module-ormjpa-riêng)
2. [@Transactional là gì?](#2-transactional-là-gì)
3. [Các thuộc tính của @Transactional](#3-các-thuộc-tính-của-transactional)
4. [Propagation – Các mức lan truyền](#4-propagation--các-mức-lan-truyền)
5. [Isolation – Các mức cô lập](#5-isolation--các-mức-cô-lập)
6. [Xử lý Exception và Rollback](#6-xử-lý-exception-và-rollback)
7. [Optimistic Locking vs Pessimistic Locking](#7-optimistic-locking-vs-pessimistic-locking)
8. [Self-invocation Problem](#8-self-invocation-problem)
9. [Usecase thực tế: Chuyển tiền ngân hàng](#9-usecase-thực-tế-chuyển-tiền-ngân-hàng)
10. [Usecase: Đặt hàng + gọi API thanh toán](#10-usecase-đặt-hàng--gọi-api-thanh-toán)
11. [Tổng kết Trade-off](#11-tổng-kết-trade-off)

---

## 1. Tại sao Spring có module ORM/JPA riêng?

**Câu hỏi:** Hibernate đã là ORM mạnh, sao Spring còn cần `spring-orm`, `spring-jpa`?

**Trả lời:**

- **Abstraction layer:** Spring cung cấp `PlatformTransactionManager` interface chung cho mọi công nghệ (JDBC, JPA, Hibernate, JTA). Bạn có thể đổi từ Hibernate sang EclipseLink mà không cần sửa code service.
- **Declarative transaction:** `@Transactional` là annotation của Spring, không phải Hibernate. Spring tạo proxy, quản lý `EntityManager`, bind vào thread, commit/rollback tự động.
- **Tích hợp tốt hơn:** Spring quản lý lifecycle của `EntityManagerFactory`, inject `EntityManager` qua `@PersistenceContext`, và đồng bộ với `JdbcTemplate` trong cùng transaction.
- **Exception translation:** Spring chuyển đổi Hibernate exception (`HibernateException`) thành `DataAccessException` hierarchy, giúp code không phụ thuộc vào vendor cụ thể.

```java
// Không cần biết Hibernate hay EclipseLink
@Autowired
private PlatformTransactionManager transactionManager; // Có thể là JpaTransactionManager, HibernateTransactionManager, DataSourceTransactionManager...
```

---

## 2. @Transactional là gì?

`@Transactional` là annotation đánh dấu một method (hoặc class) cần chạy trong transaction. Spring tạo **proxy** bao quanh bean, và khi method được gọi:

1. Proxy bắt lời gọi
2. `TransactionInterceptor` kiểm tra metadata
3. `PlatformTransactionManager` mở transaction
4. Method thực thi
5. Nếu thành công → commit; nếu exception → rollback

```java
@Service
public class AccountService {

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Account from = accountRepository.findById(fromId).orElseThrow();
        Account to = accountRepository.findById(toId).orElseThrow();
        
        from.debit(amount);   // Trừ tiền
        to.credit(amount);    // Cộng tiền
        
        accountRepository.save(from);
        accountRepository.save(to);
        // Nếu bất kỳ dòng nào ném exception → rollback cả hai
    }
}
```

---

## 3. Các thuộc tính của @Transactional

| Thuộc tính | Mô tả | Giá trị mặc định |
|------------|-------|------------------|
| `propagation` | Cách xử lý khi đã có transaction | `REQUIRED` |
| `isolation` | Mức cô lập dữ liệu | `DEFAULT` (của DB) |
| `timeout` | Thời gian tối đa (giây) | `-1` (không giới hạn) |
| `readOnly` | Đánh dấu chỉ đọc (tối ưu performance) | `false` |
| `rollbackFor` | Exception nào sẽ rollback | Unchecked exceptions |
| `noRollbackFor` | Exception nào KHÔNG rollback | Rỗng |
| `transactionManager` | Tên bean TransactionManager | Mặc định |

```java
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ,
    timeout = 30,
    readOnly = false,
    rollbackFor = { BusinessException.class },
    noRollbackFor = { NotificationException.class }
)
public void complexOperation() { ... }
```

---

## 4. Propagation – Các mức lan truyền

### 4.1. REQUIRED (mặc định)

**Là gì:** Nếu đã có transaction → dùng luôn. Nếu chưa → tạo mới.

**Khi nào dùng:** Hầu hết các trường hợp. Service gọi repository, tất cả chạy trong cùng transaction.

```java
@Transactional(propagation = Propagation.REQUIRED)
public void methodA() {
    methodB(); // Cùng transaction với methodA
}

@Transactional(propagation = Propagation.REQUIRED)
public void methodB() { ... }
```

### 4.2. REQUIRES_NEW

**Là gì:** Luôn tạo transaction mới. Nếu đang có transaction → suspend (tạm dừng) nó.

**Khi nào dùng:** Khi cần ghi log audit độc lập, dù business logic fail thì log vẫn được lưu.

**Trade-off:** Tốn thêm connection, có thể deadlock nếu không cẩn thận.

```java
@Transactional
public void placeOrder(Order order) {
    orderRepository.save(order);
    auditService.logAction("ORDER_PLACED", order.getId()); // REQUIRES_NEW
    // Nếu sau đó ném exception → order rollback, nhưng audit log vẫn commit
}

@Service
public class AuditService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(String action, Long entityId) {
        auditRepository.save(new AuditLog(action, entityId));
    }
}
```

### 4.3. NESTED

**Là gì:** Tạo savepoint trong transaction hiện tại. Nếu nested transaction fail → rollback về savepoint, transaction cha vẫn tiếp tục.

**Khi nào dùng:** Xử lý batch, muốn skip item lỗi mà không rollback toàn bộ.

**Trade-off:** Không phải mọi DB đều hỗ trợ savepoint (MySQL InnoDB có, một số NoSQL không).

```java
@Transactional
public void processBatch(List<Item> items) {
    for (Item item : items) {
        try {
            processItem(item); // NESTED
        } catch (Exception e) {
            log.warn("Skip item {}", item.getId());
            // Chỉ rollback item này, tiếp tục các item khác
        }
    }
}

@Transactional(propagation = Propagation.NESTED)
public void processItem(Item item) { ... }
```

### 4.4. SUPPORTS, NOT_SUPPORTED, MANDATORY, NEVER

| Propagation | Có TX sẵn | Không có TX |
|-------------|-----------|-------------|
| SUPPORTS | Dùng luôn | Không tạo, chạy không TX |
| NOT_SUPPORTED | Suspend TX | Chạy không TX |
| MANDATORY | Dùng luôn | Ném exception |
| NEVER | Ném exception | Chạy không TX |

---

## 5. Isolation – Các mức cô lập

### 5.1. Các vấn đề về đồng thời (concurrency anomalies)

| Vấn đề | Mô tả |
|--------|-------|
| **Dirty Read** | Đọc dữ liệu chưa commit từ transaction khác |
| **Non-repeatable Read** | Đọc cùng row 2 lần, giá trị khác nhau (bị update) |
| **Phantom Read** | Query 2 lần, số row khác nhau (bị insert/delete) |

### 5.2. Các mức isolation

| Isolation | Dirty Read | Non-repeatable | Phantom |
|-----------|------------|----------------|---------|
| READ_UNCOMMITTED | Có thể | Có thể | Có thể |
| READ_COMMITTED | Không | Có thể | Có thể |
| REPEATABLE_READ | Không | Không | Có thể |
| SERIALIZABLE | Không | Không | Không |

### 5.3. Khi nào dùng

```java
// Báo cáo tài chính - cần chính xác tuyệt đối
@Transactional(isolation = Isolation.SERIALIZABLE)
public FinancialReport generateMonthlyReport() { ... }

// Đọc danh sách sản phẩm - không cần quá nghiêm ngặt
@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
public List<Product> getProducts() { ... }
```

**Trade-off:**
- `SERIALIZABLE`: An toàn nhất nhưng **chậm nhất**, dễ bị lock contention.
- `READ_COMMITTED`: Cân bằng, phù hợp hầu hết usecase (mặc định của PostgreSQL).
- `REPEATABLE_READ`: Mặc định của MySQL InnoDB, tốt cho OLTP.

---

## 6. Xử lý Exception và Rollback

### 6.1. Quy tắc mặc định

- **Unchecked exception** (`RuntimeException`, `Error`) → **Rollback**
- **Checked exception** → **Commit** (KHÔNG rollback)

```java
@Transactional
public void process() {
    repository.save(entity);
    throw new RuntimeException("Lỗi!"); // → Rollback
}

@Transactional
public void process() throws IOException {
    repository.save(entity);
    throw new IOException("Lỗi file!"); // → KHÔNG rollback (checked)
}
```

### 6.2. Tùy chỉnh rollback

```java
// Rollback cả checked exception
@Transactional(rollbackFor = Exception.class)
public void process() throws BusinessException { ... }

// Rollback cho exception cụ thể
@Transactional(rollbackFor = { PaymentFailedException.class, InsufficientFundsException.class })
public void transfer() { ... }

// KHÔNG rollback cho exception cụ thể
@Transactional(noRollbackFor = EmailNotSentException.class)
public void createUserAndNotify() {
    userRepository.save(user);
    emailService.send(user); // Nếu email fail → vẫn commit user
}
```

### 6.3. Rollback thủ công

```java
@Transactional
public void process() {
    try {
        doSomething();
    } catch (Exception e) {
        // Đánh dấu rollback mà không ném exception
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        log.error("Lỗi, đã rollback", e);
    }
}
```

---

## 7. Optimistic Locking vs Pessimistic Locking

### 7.1. Optimistic Locking (Khóa lạc quan)

**Là gì:** Giả định xung đột hiếm khi xảy ra. Không lock row khi đọc, chỉ kiểm tra version khi ghi.

**Cách dùng:** Thêm `@Version` field vào entity.

```java
@Entity
public class Product {
    @Id
    private Long id;
    
    @Version
    private Long version; // Tự động tăng mỗi lần update
    
    private Integer stock;
}

// Khi update, JPA tự sinh:
// UPDATE product SET stock = ?, version = ? WHERE id = ? AND version = ?
// Nếu version không khớp → OptimisticLockException
```

**Khi nào dùng:**
- Hệ thống **đọc nhiều, ghi ít**
- Xung đột hiếm khi xảy ra
- Chấp nhận retry khi conflict

**Trade-off:**
- Ưu: Không lock DB, throughput cao
- Nhược: Phải xử lý `OptimisticLockException` và retry

```java
@Service
public class ProductService {
    
    @Transactional
    @Retryable(value = OptimisticLockException.class, maxAttempts = 3)
    public void updateStock(Long productId, int delta) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.setStock(product.getStock() + delta);
        productRepository.save(product);
    }
}
```

### 7.2. Pessimistic Locking (Khóa bi quan)

**Là gì:** Lock row ngay khi đọc, ngăn transaction khác truy cập.

**Cách dùng:** Dùng `@Lock` annotation hoặc query hint.

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // PESSIMISTIC_WRITE: SELECT ... FOR UPDATE
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);
    
    // PESSIMISTIC_READ: SELECT ... FOR SHARE (cho phép đọc, chặn ghi)
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Product> findByIdForShare(Long id);
}

@Transactional
public void reserveStock(Long productId, int quantity) {
    Product product = productRepository.findByIdWithLock(productId); // Lock row
    if (product.getStock() < quantity) {
        throw new InsufficientStockException();
    }
    product.setStock(product.getStock() - quantity);
    // Khi commit → release lock
}
```

**Khi nào dùng:**
- Hệ thống **ghi nhiều, xung đột cao**
- Cần đảm bảo consistency tuyệt đối
- Transaction ngắn

**Trade-off:**
- Ưu: Không cần retry, consistency cao
- Nhược: Giảm throughput, có thể deadlock, giữ lock lâu → bottleneck

### 7.3. So sánh

| Tiêu chí | Optimistic | Pessimistic |
|----------|------------|-------------|
| Lock timing | Khi commit | Khi đọc |
| Concurrency | Cao | Thấp |
| Deadlock risk | Không | Có |
| Retry cần thiết | Có | Không |
| Phù hợp | Read-heavy | Write-heavy |

---

## 8. Self-invocation Problem

**Vấn đề:** Khi method A gọi method B **trong cùng class**, proxy không được kích hoạt → `@Transactional` trên method B bị bỏ qua.

```java
@Service
public class OrderService {
    
    @Transactional
    public void processOrder(Order order) {
        saveOrder(order);
        updateInventory(order); // ❌ KHÔNG có transaction mới!
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateInventory(Order order) {
        // Mong đợi transaction mới, nhưng thực tế chạy trong transaction của processOrder
    }
}
```

**Giải thích:**

```
Caller → Proxy.processOrder() → this.updateInventory()
              ↓                        ↓
        [Proxy bắt]            [Gọi trực tiếp, bypass proxy!]
```

**Giải pháp:**

```java
// Cách 1: Inject self
@Service
public class OrderService {
    @Autowired
    private OrderService self; // Inject proxy của chính mình
    
    @Transactional
    public void processOrder(Order order) {
        self.updateInventory(order); // ✅ Đi qua proxy
    }
}

// Cách 2: Tách ra service khác
@Service
public class InventoryService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateInventory(Order order) { ... }
}

// Cách 3: Dùng AspectJ mode (compile-time weaving thay vì proxy)
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
```

---

## 9. Usecase thực tế: Chuyển tiền ngân hàng

### 9.1. Yêu cầu
- Trừ tiền tài khoản A
- Cộng tiền tài khoản B
- Cả hai phải thành công hoặc cả hai thất bại (Atomicity)
- Không ai được thấy trạng thái trung gian (Isolation)

### 9.2. Implementation

```java
@Service
@RequiredArgsConstructor
public class TransferService {
    
    private final AccountRepository accountRepository;
    private final TransferLogRepository logRepository;
    
    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        rollbackFor = Exception.class
    )
    public TransferResult transfer(Long fromId, Long toId, BigDecimal amount) {
        // 1. Validate
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền phải > 0");
        }
        
        // 2. Lock accounts theo thứ tự ID để tránh deadlock
        Long firstId = Math.min(fromId, toId);
        Long secondId = Math.max(fromId, toId);
        
        Account first = accountRepository.findByIdWithLock(firstId)
            .orElseThrow(() -> new AccountNotFoundException(firstId));
        Account second = accountRepository.findByIdWithLock(secondId)
            .orElseThrow(() -> new AccountNotFoundException(secondId));
        
        Account from = fromId.equals(firstId) ? first : second;
        Account to = toId.equals(firstId) ? first : second;
        
        // 3. Kiểm tra số dư
        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(fromId, amount);
        }
        
        // 4. Thực hiện chuyển tiền
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        
        // 5. Lưu log
        TransferLog log = new TransferLog(fromId, toId, amount, LocalDateTime.now());
        logRepository.save(log);
        
        return new TransferResult(log.getId(), "SUCCESS");
    }
}
```

### 9.3. Giải thích

- **`REPEATABLE_READ`**: Đảm bảo số dư không đổi trong suốt transaction.
- **Lock theo thứ tự ID**: Tránh deadlock khi 2 transaction chuyển tiền ngược chiều.
- **`rollbackFor = Exception.class`**: Rollback cả checked exception.

---

## 10. Usecase: Đặt hàng + gọi API thanh toán

### 10.1. Vấn đề

Khi gọi API bên ngoài trong `@Transactional`:
- API thành công → commit DB
- API thất bại → rollback DB ✅
- DB commit → API thất bại sau đó → **KHÔNG rollback được** (đã commit!) ❌

### 10.2. Giải pháp 1: API trước, DB sau

```java
@Transactional
public Order placeOrder(OrderRequest request) {
    // 1. Validate và chuẩn bị order (chưa lưu)
    Order order = new Order(request);
    
    // 2. Gọi API thanh toán TRƯỚC
    PaymentResult payment = paymentClient.charge(request.getPaymentInfo());
    if (!payment.isSuccess()) {
        throw new PaymentFailedException(payment.getMessage());
    }
    
    // 3. Lưu order với payment info
    order.setPaymentId(payment.getId());
    order.setStatus(OrderStatus.PAID);
    orderRepository.save(order);
    
    return order;
}
```

**Nhược điểm:** Nếu DB fail sau khi API success → tiền đã trừ nhưng order không có.

### 10.3. Giải pháp 2: Outbox Pattern

```java
@Transactional
public Order placeOrder(OrderRequest request) {
    // 1. Lưu order với status PENDING
    Order order = new Order(request);
    order.setStatus(OrderStatus.PENDING);
    orderRepository.save(order);
    
    // 2. Lưu event vào outbox (cùng transaction)
    OutboxEvent event = new OutboxEvent(
        "PAYMENT_REQUEST",
        objectMapper.writeValueAsString(new PaymentRequest(order))
    );
    outboxRepository.save(event);
    
    return order;
    // Commit cả order và event
}

// Scheduler riêng xử lý outbox
@Scheduled(fixedDelay = 1000)
@Transactional
public void processOutbox() {
    List<OutboxEvent> events = outboxRepository.findPendingEvents();
    for (OutboxEvent event : events) {
        try {
            PaymentResult result = paymentClient.charge(event.getPayload());
            orderRepository.updateStatus(event.getOrderId(), 
                result.isSuccess() ? OrderStatus.PAID : OrderStatus.FAILED);
            event.setProcessed(true);
        } catch (Exception e) {
            event.incrementRetryCount();
        }
        outboxRepository.save(event);
    }
}
```

### 10.4. Giải pháp 3: Saga Pattern với Compensating Transaction

```java
public Order placeOrderSaga(OrderRequest request) {
    Order order = null;
    PaymentResult payment = null;
    
    try {
        // Step 1: Lưu order
        order = orderService.createOrder(request);
        
        // Step 2: Gọi payment
        payment = paymentService.charge(order);
        
        // Step 3: Update order status
        orderService.confirmOrder(order.getId(), payment.getId());
        
        return order;
        
    } catch (Exception e) {
        // Compensate: Hoàn tiền nếu đã charge
        if (payment != null && payment.isSuccess()) {
            paymentService.refund(payment.getId());
        }
        // Compensate: Hủy order nếu đã tạo
        if (order != null) {
            orderService.cancelOrder(order.getId());
        }
        throw e;
    }
}
```

---

## 11. Tổng kết Trade-off

| Tình huống | Recommendation |
|------------|----------------|
| CRUD đơn giản | `@Transactional` mặc định là đủ |
| Báo cáo tài chính | `isolation = SERIALIZABLE`, `readOnly = true` |
| Audit log độc lập | `propagation = REQUIRES_NEW` |
| Batch processing | `propagation = NESTED` hoặc chunk transaction |
| High concurrency, read-heavy | Optimistic locking (`@Version`) |
| High contention, write-heavy | Pessimistic locking (`@Lock`) |
| Gọi external API | Outbox pattern hoặc Saga |
| Self-invocation | Inject self hoặc tách service |

### Checklist cho developer

- [ ] Luôn đặt `@Transactional` ở **service layer**, không phải repository
- [ ] Hiểu rõ exception nào cần rollback → dùng `rollbackFor`
- [ ] Tránh gọi method internal trong cùng class (self-invocation)
- [ ] Sử dụng `readOnly = true` cho query thuần đọc
- [ ] Lock theo thứ tự cố định để tránh deadlock
- [ ] Không gọi external API giữa transaction nếu có thể
- [ ] Test rollback scenario bằng cách ném exception cố ý
