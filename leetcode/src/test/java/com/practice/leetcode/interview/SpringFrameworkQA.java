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

  // ═══════════════════════════════════════════════════════════════════════════
  // PROXY DEEP DIVE (Advanced)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Spring dùng Proxy như thế nào? JDK vs CGLIB?                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Spring dùng proxy để implement AOP (Transactions, Security, Caching)     │
   * │                                                                             │
   * │ 2 LOẠI PROXY:                                                               │
   * │                                                                             │
   * │ 1. JDK DYNAMIC PROXY                                                        │
   * │    - Chỉ proxy INTERFACE (implements)                                       │
   * │    - Dùng java.lang.reflect.Proxy                                           │
   * │    - Faster creation, slightly slower invocation                            │
   * │                                                                             │
   * │    interface UserService { void save(); }                                   │
   * │    class UserServiceImpl implements UserService { ... }                     │
   * │    → Proxy implements UserService, delegates to UserServiceImpl             │
   * │                                                                             │
   * │ 2. CGLIB PROXY                                                              │
   * │    - Proxy CLASS (extends - tạo subclass)                                   │
   * │    - Dùng bytecode generation                                               │
   * │    - Slower creation, faster invocation                                     │
   * │    - KHÔNG proxy được final class/methods!                                  │
   * │                                                                             │
   * │    class UserService { void save(); }                                       │
   * │    → CGLIB tạo: class UserService$$EnhancerByCGLIB extends UserService      │
   * │                                                                             │
   * │ SPRING BOOT DEFAULT: CGLIB (proxyTargetClass=true)                          │
   * │                                                                             │
   * │ KIỂM TRA PROXY:                                                             │
   * │ userService.getClass().getName()                                            │
   * │ → "com.example.UserService$$EnhancerBySpringCGLIB$$abc123"                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Tại sao self-invocation không work với @Transactional?                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Vì proxy chỉ intercept external calls!                                   │
   * │                                                                             │
   * │ @Service                                                                    │
   * │ class OrderService {                                                        │
   * │     public void process() {                                                 │
   * │         // BUG! saveOrder() được gọi trực tiếp, bypass proxy!               │
   * │         saveOrder();  // "this" là target, không phải proxy                 │
   * │     }                                                                       │
   * │                                                                             │
   * │     @Transactional                                                          │
   * │     public void saveOrder() {                                               │
   * │         // KHÔNG có transaction!                                            │
   * │     }                                                                       │
   * │ }                                                                           │
   * │                                                                             │
   * │ GIẢI PHÁP:                                                                  │
   * │                                                                             │
   * │ 1. Inject self (ApplicationContext)                                         │
   * │    @Autowired ApplicationContext ctx;                                       │
   * │    public void process() {                                                  │
   * │        ctx.getBean(OrderService.class).saveOrder(); // Qua proxy            │
   * │    }                                                                        │
   * │                                                                             │
   * │ 2. Tách ra service khác                                                     │
   * │    orderService.process() → orderTransactionService.saveOrder()             │
   * │                                                                             │
   * │ 3. AopContext.currentProxy() (cần enable exposeProxy)                       │
   * │    ((OrderService) AopContext.currentProxy()).saveOrder();                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // @TRANSACTIONAL DEEP DIVE (Gotchas)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Những lỗi phổ biến với @Transactional?                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. PRIVATE/PROTECTED METHODS                                                │
   * │    @Transactional                                                           │
   * │    private void save() { }  // ❌ KHÔNG WORK! Phải public                   │
   * │                                                                             │
   * │ 2. CHECKED EXCEPTION                                                        │
   * │    @Transactional                                                           │
   * │    void save() throws IOException { throw new IOException(); }              │
   * │    // ❌ KHÔNG rollback! Default chỉ rollback RuntimeException              │
   * │    // ✓ Fix: @Transactional(rollbackFor = Exception.class)                  │
   * │                                                                             │
   * │ 3. CATCH EXCEPTION TRONG METHOD                                             │
   * │    @Transactional                                                           │
   * │    void save() {                                                            │
   * │        try {                                                                │
   * │            repo.save(entity);                                               │
   * │        } catch (Exception e) {                                              │
   * │            log.error("Error", e);  // ❌ KHÔNG rollback, exception bị nuốt! │
   * │        }                                                                    │
   * │    }                                                                        │
   * │                                                                             │
   * │ 4. READONLY TRANSACTION VẪN CÓ THỂ WRITE                                    │
   * │    @Transactional(readOnly = true)                                          │
   * │    void update() {                                                          │
   * │        repo.save(entity);  // Still works! (database dependent)             │
   * │    }                                                                        │
   * │    // readOnly là hint cho optimization, không phải enforcement             │
   * │                                                                             │
   * │ 5. TRANSACTION TRONG THREAD MỚI                                             │
   * │    @Transactional                                                           │
   * │    void process() {                                                         │
   * │        new Thread(() -> {                                                   │
   * │            repo.save(entity);  // ❌ KHÔNG có TX! TX bound to original thread│
   * │        }).start();                                                          │
   * │    }                                                                        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: @Transactional internal flow?                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. TransactionInterceptor intercepts method call                            │
   * │ 2. Check TransactionAttributeSource for TX metadata                         │
   * │ 3. PlatformTransactionManager.getTransaction()                              │
   * │    - Check existing TX (ThreadLocal)                                        │
   * │    - Apply propagation rules                                                │
   * │ 4. DataSource.getConnection()                                               │
   * │ 5. connection.setAutoCommit(false)                                          │
   * │ 6. Store connection in TransactionSynchronizationManager (ThreadLocal)      │
   * │ 7. Execute target method                                                    │
   * │ 8. On success: commit() | On exception: rollback()                          │
   * │ 9. Release resources, clear ThreadLocal                                     │
   * │                                                                             │
   * │ THREADLOCAL BINDING:                                                        │
   * │ TransactionSynchronizationManager lưu TX context per thread                 │
   * │ → Vì sao @Async cần @Transactional riêng                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // JPA / HIBERNATE DEEP DIVE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: N+1 Query Problem?                                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ @Entity class Order {                                                       │
   * │     @ManyToOne(fetch = FetchType.LAZY)                                      │
   * │     private Customer customer;                                              │
   * │ }                                                                           │
   * │                                                                             │
   * │ List<Order> orders = orderRepo.findAll();  // 1 query                       │
   * │ for (Order o : orders) {                                                    │
   * │     o.getCustomer().getName();  // N queries! (mỗi order 1 query)           │
   * │ }                                                                           │
   * │                                                                             │
   * │ SOLUTIONS:                                                                  │
   * │                                                                             │
   * │ 1. JOIN FETCH (JPQL)                                                        │
   * │    @Query("SELECT o FROM Order o JOIN FETCH o.customer")                    │
   * │    List<Order> findAllWithCustomer();                                       │
   * │                                                                             │
   * │ 2. EntityGraph                                                              │
   * │    @EntityGraph(attributePaths = {"customer"})                              │
   * │    List<Order> findAll();                                                   │
   * │                                                                             │
   * │ 3. Batch Fetching                                                           │
   * │    @BatchSize(size = 25)  // Fetch 25 customers at a time                   │
   * │    private Customer customer;                                               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: LazyInitializationException?                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ CAUSE: Access lazy-loaded entity OUTSIDE transaction/session               │
   * │                                                                             │
   * │ @Transactional                                                              │
   * │ Order order = orderRepo.findById(1);                                        │
   * │ // TX ends here...                                                          │
   * │                                                                             │
   * │ order.getCustomer().getName();  // 💥 LazyInitializationException!          │
   * │ // Session đã đóng, không thể load customer                                 │
   * │                                                                             │
   * │ SOLUTIONS:                                                                  │
   * │ 1. Fetch trong transaction (JOIN FETCH, EntityGraph)                        │
   * │ 2. DTO projection (query chỉ data cần)                                      │
   * │ 3. Open Session In View (ANTI-PATTERN, tránh dùng!)                         │
   * │    spring.jpa.open-in-view=true (default, giữ session qua view layer)       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Hibernate Dirty Checking?                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ @Transactional                                                              │
   * │ void update(Long id) {                                                      │
   * │     User user = userRepo.findById(id);                                      │
   * │     user.setName("New Name");                                               │
   * │     // KHÔNG cần gọi save()! Hibernate tự detect changes                    │
   * │ }                                                                           │
   * │ // Khi TX commit → Hibernate compare với snapshot → AUTO UPDATE             │
   * │                                                                             │
   * │ ENTITY STATES:                                                              │
   * │ - Transient: new, chưa persist                                              │
   * │ - Managed: trong persistence context, tracked                               │
   * │ - Detached: đã persist, nhưng session đóng                                  │
   * │ - Removed: marked for deletion                                              │
   * │                                                                             │
   * │ PERFORMANCE TIP:                                                            │
   * │ @Transactional(readOnly = true) → Disable dirty checking                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: First-level vs Second-level Cache?                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ FIRST-LEVEL CACHE (Session Cache):                                          │
   * │ - Per session/transaction                                                   │
   * │ - Always enabled                                                            │
   * │ - findById(1) → findById(1) trong cùng TX = 1 query                         │
   * │                                                                             │
   * │ SECOND-LEVEL CACHE (SessionFactory Cache):                                  │
   * │ - Shared across sessions                                                    │
   * │ - Need explicit enable + provider (Ehcache, Redis)                          │
   * │ - @Cacheable on entity                                                      │
   * │                                                                             │
   * │ QUERY CACHE:                                                                │
   * │ - Cache query results                                                       │
   * │ - Invalidated khi entity change                                             │
   * │ - @QueryHint(name = "org.hibernate.cacheable", value = "true")              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SPRING DATA MAGIC
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Spring Data JPA tạo implementation như thế nào?                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ interface UserRepository extends JpaRepository<User, Long> {                │
   * │     List<User> findByEmailAndStatus(String email, String status);           │
   * │ }                                                                           │
   * │ // KHÔNG có implementation class!                                           │
   * │                                                                             │
   * │ FLOW:                                                                       │
   * │ 1. JpaRepositoryFactoryBean scan @Repository interfaces                     │
   * │ 2. Parse method name → Query tree                                           │
   * │    findByEmailAndStatus → SELECT * WHERE email=? AND status=?               │
   * │ 3. Tạo Proxy class at runtime                                               │
   * │ 4. SimpleJpaRepository là base implementation                               │
   * │                                                                             │
   * │ QUERY DERIVATION KEYWORDS:                                                  │
   * │ - findBy, getBy, queryBy, readBy, countBy, existsBy, deleteBy               │
   * │ - And, Or, Between, LessThan, GreaterThan                                   │
   * │ - Like, NotLike, StartingWith, EndingWith, Containing                       │
   * │ - OrderBy, IsNull, IsNotNull, In, NotIn                                     │
   * │ - First, Top, Distinct                                                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Projection trong Spring Data?                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. INTERFACE PROJECTION                                                     │
   * │    interface UserView {                                                     │
   * │        String getName();                                                    │
   * │        String getEmail();                                                   │
   * │    }                                                                        │
   * │    List<UserView> findByStatus(String status);                              │
   * │    // SELECT name, email FROM users WHERE status=?                          │
   * │                                                                             │
   * │ 2. CLASS PROJECTION (DTO)                                                   │
   * │    @Query("SELECT new com.example.UserDTO(u.name, u.email) FROM User u")    │
   * │    List<UserDTO> findAllDTOs();                                             │
   * │                                                                             │
   * │ 3. DYNAMIC PROJECTION                                                       │
   * │    <T> List<T> findByStatus(String status, Class<T> type);                  │
   * │    repo.findByStatus("active", UserView.class);                             │
   * │    repo.findByStatus("active", User.class);                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SPRING AOT (Ahead Of Time) & GraalVM
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Spring AOT là gì? Tại sao cần?                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ TRADITIONAL SPRING (JIT - Just In Time):                                    │
   * │ - Startup: Scan classpath, create beans, generate proxies                   │
   * │ - Uses reflection heavily                                                   │
   * │ - Slow startup, high memory                                                 │
   * │                                                                             │
   * │ SPRING AOT (Ahead Of Time):                                                 │
   * │ - Compile time: Pre-compute bean definitions, generate code                 │
   * │ - Produce optimized bytecode                                                │
   * │ - Faster startup, lower memory                                              │
   * │                                                                             │
   * │ GRAALVM NATIVE IMAGE:                                                       │
   * │ - Compile to native executable (no JVM!)                                    │
   * │ - ~10-100x faster startup                                                   │
   * │ - ~5x lower memory                                                          │
   * │ - But: Longer build time, no dynamic class loading                          │
   * │                                                                             │
   * │ BUILD:                                                                      │
   * │ ./mvnw spring-boot:build-image -Pnative                                     │
   * │ ./gradlew bootBuildImage --native                                           │
   * │                                                                             │
   * │ LIMITATIONS:                                                                │
   * │ - No runtime bytecode generation (CGLIB issues)                             │
   * │ - Reflection cần declare in reflect-config.json                             │
   * │ - Dynamic proxies cần register                                              │
   * │ - Classpath scanning limited                                                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: AOT Processing làm gì?                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. BEAN DEFINITIONS                                                         │
   * │    - Pre-compute BeanDefinition thay vì runtime scan                        │
   * │    - Generate: BeanDefinitionRegistrar code                                 │
   * │                                                                             │
   * │ 2. PROXY CLASSES                                                            │
   * │    - Generate proxy classes at build time                                   │
   * │    - No CGLIB at runtime                                                    │
   * │                                                                             │
   * │ 3. REFLECTION HINTS                                                         │
   * │    - Generate reflect-config.json for GraalVM                               │
   * │    - @RegisterReflectionForBinding                                          │
   * │                                                                             │
   * │ 4. RESOURCE HINTS                                                           │
   * │    - Static resources to include                                            │
   * │    - resource-config.json                                                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // RPC / gRPC WITH SPRING
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: REST vs gRPC vs GraphQL?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌─────────────┬─────────────┬─────────────┬─────────────┐                   │
   * │ │             │ REST        │ gRPC        │ GraphQL     │                   │
   * │ ├─────────────┼─────────────┼─────────────┼─────────────┤                   │
   * │ │ Protocol    │ HTTP/1.1    │ HTTP/2      │ HTTP        │                   │
   * │ │ Format      │ JSON        │ Protobuf    │ JSON        │                   │
   * │ │ Contract    │ OpenAPI     │ .proto      │ Schema      │                   │
   * │ │ Streaming   │ Limited     │ Full        │ Subscription│                   │
   * │ │ Performance │ Good        │ Excellent   │ Varies      │                   │
   * │ │ Browser     │ ✓           │ ✗ (grpc-web)│ ✓           │                   │
   * │ └─────────────┴─────────────┴─────────────┴─────────────┘                   │
   * │                                                                             │
   * │ gRPC USE CASES:                                                             │
   * │ - Microservices internal communication                                      │
   * │ - Low latency requirements                                                  │
   * │ - Streaming (bidirectional)                                                 │
   * │ - Polyglot environments                                                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: gRPC với Spring Boot?                                                    │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. DEFINE PROTO                                                             │
   * │    service UserService {                                                    │
   * │        rpc GetUser (UserRequest) returns (UserResponse);                    │
   * │        rpc StreamUsers (Empty) returns (stream UserResponse);               │
   * │    }                                                                        │
   * │                                                                             │
   * │ 2. IMPLEMENT SERVICE                                                        │
   * │    @GrpcService                                                             │
   * │    class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {      │
   * │        @Override                                                            │
   * │        public void getUser(UserRequest req,                                 │
   * │                           StreamObserver<UserResponse> response) {          │
   * │            UserResponse user = UserResponse.newBuilder()                    │
   * │                .setId(req.getId())                                          │
   * │                .setName("John")                                             │
   * │                .build();                                                    │
   * │            response.onNext(user);                                           │
   * │            response.onCompleted();                                          │
   * │        }                                                                    │
   * │    }                                                                        │
   * │                                                                             │
   * │ 3. CLIENT                                                                   │
   * │    @GrpcClient("user-service")                                              │
   * │    private UserServiceBlockingStub userStub;                                │
   * │                                                                             │
   * │    UserResponse user = userStub.getUser(                                    │
   * │        UserRequest.newBuilder().setId(1).build()                            │
   * │    );                                                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SPRING HIDDEN GEMS & GOTCHAS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: @Async hoạt động thế nào?                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ @EnableAsync  // Enable async processing                                    │
   * │                                                                             │
   * │ @Async                                                                      │
   * │ public CompletableFuture<String> processAsync() {                           │
   * │     // Runs in separate thread                                              │
   * │     return CompletableFuture.completedFuture("done");                       │
   * │ }                                                                           │
   * │                                                                             │
   * │ GOTCHAS:                                                                    │
   * │ - Cần @EnableAsync                                                          │
   * │ - Phải public method                                                        │
   * │ - Self-invocation KHÔNG work (same as @Transactional)                       │
   * │ - Return void hoặc Future (không thể return value trực tiếp)                │
   * │ - @Transactional KHÔNG propagate (TX bound to original thread)              │
   * │                                                                             │
   * │ CUSTOM EXECUTOR:                                                            │
   * │ @Async("customExecutor")                                                    │
   * │ public void process() { }                                                   │
   * │                                                                             │
   * │ @Bean("customExecutor")                                                     │
   * │ Executor executor() {                                                       │
   * │     ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();               │
   * │     ex.setCorePoolSize(10);                                                 │
   * │     ex.setMaxPoolSize(50);                                                  │
   * │     ex.setQueueCapacity(100);                                               │
   * │     return ex;                                                              │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: @EventListener và ApplicationEvent?                                      │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // Define event                                                             │
   * │ class OrderCreatedEvent extends ApplicationEvent {                          │
   * │     private final Order order;                                              │
   * │     // constructor                                                          │
   * │ }                                                                           │
   * │                                                                             │
   * │ // Publish event                                                            │
   * │ @Autowired ApplicationEventPublisher publisher;                             │
   * │ publisher.publishEvent(new OrderCreatedEvent(order));                       │
   * │                                                                             │
   * │ // Listen to event                                                          │
   * │ @EventListener                                                              │
   * │ void handleOrderCreated(OrderCreatedEvent event) {                          │
   * │     sendNotification(event.getOrder());                                     │
   * │ }                                                                           │
   * │                                                                             │
   * │ // Async listener                                                           │
   * │ @Async                                                                      │
   * │ @EventListener                                                              │
   * │ void asyncHandle(OrderCreatedEvent event) { }                               │
   * │                                                                             │
   * │ // Transactional listener (after commit)                                    │
   * │ @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)          │
   * │ void afterCommit(OrderCreatedEvent event) {                                 │
   * │     // Only runs if TX committed successfully                               │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Circular Dependency và cách xử lý?                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ @Service A { @Autowired B b; }                                              │
   * │ @Service B { @Autowired A a; }                                              │
   * │ // 💥 BeanCurrentlyInCreationException                                      │
   * │                                                                             │
   * │ Spring Boot 2.6+ KHÔNG cho phép circular dependencies by default            │
   * │                                                                             │
   * │ SOLUTIONS:                                                                  │
   * │                                                                             │
   * │ 1. Redesign (BEST) - Tách responsibilities                                  │
   * │                                                                             │
   * │ 2. @Lazy                                                                    │
   * │    @Autowired @Lazy B b;  // Inject proxy, resolve later                    │
   * │                                                                             │
   * │ 3. Setter injection                                                         │
   * │    private B b;                                                             │
   * │    @Autowired void setB(B b) { this.b = b; }                                │
   * │                                                                             │
   * │ 4. ObjectFactory                                                            │
   * │    @Autowired ObjectFactory<B> bFactory;                                    │
   * │    void use() { bFactory.getObject().method(); }                            │
   * │                                                                             │
   * │ 5. spring.main.allow-circular-references=true (AVOID!)                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: SpEL (Spring Expression Language)?                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // Property access                                                          │
   * │ @Value("${app.name}")                                                       │
   * │ String appName;                                                             │
   * │                                                                             │
   * │ // Default value                                                            │
   * │ @Value("${app.timeout:30}")                                                 │
   * │ int timeout;                                                                │
   * │                                                                             │
   * │ // SpEL expression                                                          │
   * │ @Value("#{systemProperties['user.name']}")                                  │
   * │ String userName;                                                            │
   * │                                                                             │
   * │ // Bean reference                                                           │
   * │ @Value("#{userService.defaultUser}")                                        │
   * │ User defaultUser;                                                           │
   * │                                                                             │
   * │ // Conditional                                                              │
   * │ @Value("#{${app.production} ? 'prod' : 'dev'}")                             │
   * │ String env;                                                                 │
   * │                                                                             │
   * │ // In annotations                                                           │
   * │ @Cacheable(key = "#user.id")                                                │
   * │ @PreAuthorize("hasRole('ADMIN') or #user.id == authentication.principal.id")│
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
