package com.practice.leetcode.interview;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                    SPRING FRAMEWORK INTERVIEW Q&A                          ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class SpringFrameworkQA {

  // ═══════════════════════════════════════════════════════════════════════════
  // IoC & DEPENDENCY INJECTION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: IoC (Inversion of Control) là gì?                                        │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Thay vì code tạo dependencies, Framework quản lý và inject vào           │
   * │                                                                             │
   * │ TRƯỚC (No IoC):                                                             │
   * │   class UserService {                                                       │
   * │       private UserRepository repo = new UserRepositoryImpl();  // Tự tạo!   │
   * │   }                                                                         │
   * │                                                                             │
   * │ SAU (IoC):                                                                  │
   * │   @Service                                                                  │
   * │   class UserService {                                                       │
   * │       private final UserRepository repo;  // Framework inject               │
   * │       public UserService(UserRepository repo) { this.repo = repo; }         │
   * │   }                                                                         │
   * │                                                                             │
   * │ LỢI ÍCH:                                                                    │
   * │ - Loose coupling                                                            │
   * │ - Dễ test (mock dependencies)                                               │
   * │ - Dễ thay đổi implementation                                                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: 3 loại Dependency Injection?                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ 1. CONSTRUCTOR INJECTION (Recommended):                                     │
   * │    @Service                                                                 │
   * │    class UserService {                                                      │
   * │        private final UserRepository repo;                                   │
   * │        public UserService(UserRepository repo) {                            │
   * │            this.repo = repo;                                                │
   * │        }                                                                    │
   * │    }                                                                        │
   * │    ✓ Immutable, ✓ Required deps, ✓ Dễ test                                  │
   * │                                                                             │
   * │ 2. SETTER INJECTION:                                                        │
   * │    @Service                                                                 │
   * │    class UserService {                                                      │
   * │        private UserRepository repo;                                         │
   * │        @Autowired                                                           │
   * │        public void setRepo(UserRepository repo) { this.repo = repo; }       │
   * │    }                                                                        │
   * │    ✓ Optional deps, ✗ Mutable                                               │
   * │                                                                             │
   * │ 3. FIELD INJECTION (Avoid!):                                                │
   * │    @Service                                                                 │
   * │    class UserService {                                                      │
   * │        @Autowired                                                           │
   * │        private UserRepository repo;                                         │
   * │    }                                                                        │
   * │    ✗ Khó test, ✗ Hidden deps, ✗ Reflection                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // BEAN LIFECYCLE & SCOPES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Bean Lifecycle trong Spring?                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ 1. Instantiation (new)                                                      │
   * │ 2. Populate Properties (DI)                                                 │
   * │ 3. BeanNameAware.setBeanName()                                              │
   * │ 4. BeanFactoryAware.setBeanFactory()                                        │
   * │ 5. ApplicationContextAware.setApplicationContext()                          │
   * │ 6. BeanPostProcessor.postProcessBeforeInitialization()                      │
   * │ 7. @PostConstruct method                                                    │
   * │ 8. InitializingBean.afterPropertiesSet()                                    │
   * │ 9. Custom init-method                                                       │
   * │ 10. BeanPostProcessor.postProcessAfterInitialization()                      │
   * │ 11. Bean is ready                                                           │
   * │ 12. @PreDestroy method (on shutdown)                                        │
   * │ 13. DisposableBean.destroy()                                                │
   * │ 14. Custom destroy-method                                                   │
   * │                                                                             │
   * │ COMMON USE:                                                                 │
   * │ @PostConstruct - Initialize resources                                       │
   * │ @PreDestroy - Cleanup resources                                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Bean Scopes trong Spring?                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ ┌───────────────┬───────────────────────────────────────────────────────┐   │
   * │ │ Scope         │ Description                                           │   │
   * │ ├───────────────┼───────────────────────────────────────────────────────┤   │
   * │ │ singleton     │ 1 instance per container (DEFAULT)                    │   │
   * │ │ prototype     │ New instance mỗi lần inject                           │   │
   * │ │ request       │ 1 instance per HTTP request (web)                     │   │
   * │ │ session       │ 1 instance per HTTP session (web)                     │   │
   * │ │ application   │ 1 instance per ServletContext (web)                   │   │
   * │ └───────────────┴───────────────────────────────────────────────────────┘   │
   * │                                                                             │
   * │ EXAMPLE:                                                                    │
   * │ @Scope("prototype")                                                         │
   * │ @Component                                                                  │
   * │ class PrototypeBean { ... }                                                 │
   * │                                                                             │
   * │ WARNING: Inject prototype vào singleton sẽ không work như mong đợi!         │
   * │ → Dùng ObjectFactory<T> hoặc @Lookup                                        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SPRING MVC
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Spring MVC Request Flow?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │   Client → DispatcherServlet → HandlerMapping → Controller                  │
   * │              ↓                                      ↓                       │
   * │          ViewResolver ← DispatcherServlet ← ModelAndView                    │
   * │              ↓                                                              │
   * │            View → Client                                                    │
   * │                                                                             │
   * │ CHI TIẾT:                                                                   │
   * │ 1. Request đến DispatcherServlet (Front Controller)                         │
   * │ 2. DispatcherServlet hỏi HandlerMapping tìm Controller                      │
   * │ 3. Controller xử lý, return ModelAndView                                    │
   * │ 4. ViewResolver resolve view name → View                                    │
   * │ 5. View render với Model data                                               │
   * │ 6. Response về client                                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: @Controller vs @RestController?                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ @Controller:                                                                │
   * │ - Return view name (HTML page)                                              │
   * │ - Cần @ResponseBody để return JSON                                          │
   * │                                                                             │
   * │ @RestController = @Controller + @ResponseBody:                              │
   * │ - Return data trực tiếp (JSON/XML)                                          │
   * │ - Dùng cho REST APIs                                                        │
   * │                                                                             │
   * │ EXAMPLE:                                                                    │
   * │ @RestController                                                             │
   * │ @RequestMapping("/api/users")                                               │
   * │ class UserController {                                                      │
   * │     @GetMapping("/{id}")                                                    │
   * │     User getUser(@PathVariable Long id) { return userService.get(id); }     │
   * │                                                                             │
   * │     @PostMapping                                                            │
   * │     User create(@RequestBody User user) { return userService.save(user); }  │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // @TRANSACTIONAL
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: @Transactional hoạt động như nào?                                        │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Spring tạo proxy để wrap method với transaction logic                    │
   * │                                                                             │
   * │ PROXY MECHANISM:                                                            │
   * │   Caller → Proxy → [BEGIN TX] → Target Method → [COMMIT/ROLLBACK] → Return  │
   * │                                                                             │
   * │ IMPORTANT GOTCHAS:                                                          │
   * │ 1. Chỉ work trên PUBLIC methods                                             │
   * │ 2. Self-invocation không work! (method gọi method trong cùng class)         │
   * │ 3. Default chỉ rollback RuntimeException                                    │
   * │                                                                             │
   * │ EXAMPLE:                                                                    │
   * │ @Transactional                                                              │
   * │ public void transfer(Long from, Long to, BigDecimal amount) {               │
   * │     accountRepo.withdraw(from, amount);                                     │
   * │     accountRepo.deposit(to, amount);  // Exception → cả 2 rollback          │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Transaction Propagation types?                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ ┌─────────────────────┬─────────────────────────────────────────────────┐   │
   * │ │ Propagation         │ Behavior                                        │   │
   * │ ├─────────────────────┼─────────────────────────────────────────────────┤   │
   * │ │ REQUIRED (default)  │ Join existing TX, or create new                 │   │
   * │ │ REQUIRES_NEW        │ Always create new TX (suspend existing)         │   │
   * │ │ NESTED              │ Nested TX with savepoint                        │   │
   * │ │ MANDATORY           │ Must have existing TX, else exception           │   │
   * │ │ SUPPORTS            │ Use TX if exists, else run without              │   │
   * │ │ NOT_SUPPORTED       │ Run without TX (suspend existing)               │   │
   * │ │ NEVER               │ Must NOT have TX, else exception                │   │
   * │ └─────────────────────┴─────────────────────────────────────────────────┘   │
   * │                                                                             │
   * │ COMMON USE CASE:                                                            │
   * │ - REQUIRES_NEW: Audit logging (luôn commit dù main tx fail)                 │
   * │ - NESTED: Partial rollback với savepoint                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Transaction Isolation levels?                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: (Same as database isolation)                                             │
   * │                                                                             │
   * │ @Transactional(isolation = Isolation.READ_COMMITTED)                        │
   * │                                                                             │
   * │ - DEFAULT: Use database default                                             │
   * │ - READ_UNCOMMITTED: Dirty reads possible                                    │
   * │ - READ_COMMITTED: Most databases default                                    │
   * │ - REPEATABLE_READ: MySQL default                                            │
   * │ - SERIALIZABLE: Strictest, slowest                                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SPRING BOOT
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Spring Boot Auto-configuration hoạt động như nào?                        │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ @SpringBootApplication = @Configuration + @EnableAutoConfiguration          │
   * │                        + @ComponentScan                                     │
   * │                                                                             │
   * │ FLOW:                                                                       │
   * │ 1. Scan META-INF/spring.factories (hoặc spring/org.springframework.boot.    │
   * │    autoconfigure.AutoConfiguration.imports trong Spring Boot 3)             │
   * │ 2. Load các @Configuration classes                                          │
   * │ 3. @Conditional annotations quyết định có enable không                      │
   * │                                                                             │
   * │ COMMON CONDITIONS:                                                          │
   * │ @ConditionalOnClass - Class có trong classpath                              │
   * │ @ConditionalOnMissingBean - Bean chưa được define                           │
   * │ @ConditionalOnProperty - Property có giá trị                                │
   * │                                                                             │
   * │ EXAMPLE: DataSourceAutoConfiguration                                        │
   * │ @ConditionalOnClass(DataSource.class)  // Có dependency                     │
   * │ @ConditionalOnMissingBean(DataSource.class)  // User chưa define            │
   * │ → Auto-configure DataSource từ application.properties                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: @ConfigurationProperties là gì?                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Type-safe configuration binding từ properties/yaml                       │
   * │                                                                             │
   * │ application.yml:                                                            │
   * │   app:                                                                      │
   * │     name: MyApp                                                             │
   * │     max-connections: 100                                                    │
   * │                                                                             │
   * │ @ConfigurationProperties(prefix = "app")                                    │
   * │ @Component                                                                  │
   * │ class AppConfig {                                                           │
   * │     private String name;                                                    │
   * │     private int maxConnections;  // Tự động bind max-connections            │
   * │     // getters, setters                                                     │
   * │ }                                                                           │
   * │                                                                             │
   * │ BENEFITS:                                                                   │
   * │ - Type-safe (compile-time check)                                            │
   * │ - IDE auto-completion                                                       │
   * │ - Validation với @Validated                                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // AOP (ASPECT ORIENTED PROGRAMMING)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: AOP là gì? Khi nào dùng?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Tách cross-cutting concerns ra khỏi business logic                       │
   * │                                                                             │
   * │ CROSS-CUTTING CONCERNS:                                                     │
   * │ - Logging                                                                   │
   * │ - Security                                                                  │
   * │ - Transaction                                                               │
   * │ - Caching                                                                   │
   * │ - Exception handling                                                        │
   * │                                                                             │
   * │ KEY CONCEPTS:                                                               │
   * │ - Aspect: Module chứa cross-cutting logic                                   │
   * │ - Join Point: Điểm có thể apply aspect (method call, exception)             │
   * │ - Pointcut: Expression chọn join points                                     │
   * │ - Advice: Action tại join point (Before, After, Around)                     │
   * │ - Weaving: Apply aspects vào target objects                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Các loại Advice trong AOP?                                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ @Before - Chạy trước method                                                 │
   * │ @After - Chạy sau method (finally)                                          │
   * │ @AfterReturning - Sau method return thành công                              │
   * │ @AfterThrowing - Sau method throw exception                                 │
   * │ @Around - Wrap method (full control)                                        │
   * │                                                                             │
   * │ EXAMPLE:                                                                    │
   * │ @Aspect                                                                     │
   * │ @Component                                                                  │
   * │ class LoggingAspect {                                                       │
   * │                                                                             │
   * │     @Around("execution(* com.example.service.*.*(..))")                     │
   * │     public Object logExecutionTime(ProceedingJoinPoint joinPoint)           │
   * │             throws Throwable {                                              │
   * │         long start = System.currentTimeMillis();                            │
   * │         Object result = joinPoint.proceed();                                │
   * │         long duration = System.currentTimeMillis() - start;                 │
   * │         log.info("{} executed in {}ms",                                     │
   * │             joinPoint.getSignature(), duration);                            │
   * │         return result;                                                      │
   * │     }                                                                       │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // COMMON ANNOTATIONS CHEAT SHEET
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ STEREOTYPE ANNOTATIONS                                                      │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ @Component - Generic component                                              │
   * │ @Service - Business logic layer                                             │
   * │ @Repository - Data access layer (+ exception translation)                   │
   * │ @Controller - Web layer (MVC)                                               │
   * │ @RestController - REST API (= @Controller + @ResponseBody)                  │
   * │ @Configuration - Java-based configuration                                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ WEB ANNOTATIONS                                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ @RequestMapping - Map URL to handler                                        │
   * │ @GetMapping, @PostMapping, @PutMapping, @DeleteMapping                      │
   * │ @PathVariable - URL path variable                                           │
   * │ @RequestParam - Query parameter                                             │
   * │ @RequestBody - Request body (JSON → Object)                                 │
   * │ @ResponseBody - Return type (Object → JSON)                                 │
   * │ @RequestHeader - HTTP header                                                │
   * │ @CookieValue - Cookie value                                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ DI ANNOTATIONS                                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ @Autowired - Inject dependency                                              │
   * │ @Qualifier - Specify which bean to inject                                   │
   * │ @Primary - Default bean when multiple candidates                            │
   * │ @Value - Inject property value                                              │
   * │ @Lazy - Lazy initialization                                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
