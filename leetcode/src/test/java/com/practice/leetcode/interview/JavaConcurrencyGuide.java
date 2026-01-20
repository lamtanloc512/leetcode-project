package com.practice.leetcode.interview;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║              JAVA CONCURRENCY & MULTITHREADING GUIDE                      ║
 * ╠═══════════════════════════════════════════════════════════════════════════╣
 * ║ Deep dive into java.util.concurrent, async patterns, và HTTP handlers     ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class JavaConcurrencyGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // PACKAGES OVERVIEW
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ JAVA CONCURRENCY PACKAGES                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ java.lang:                                                                  │
   * │   Thread, Runnable, ThreadLocal, ThreadGroup                                │
   * │                                                                             │
   * │ java.util.concurrent:                                                       │
   * │   ExecutorService, ThreadPoolExecutor, ForkJoinPool, ScheduledExecutor      │
   * │   Future, CompletableFuture, Callable                                       │
   * │   ConcurrentHashMap, CopyOnWriteArrayList, BlockingQueue                    │
   * │   CountDownLatch, CyclicBarrier, Semaphore, Phaser, Exchanger               │
   * │                                                                             │
   * │ java.util.concurrent.atomic:                                                │
   * │   AtomicInteger, AtomicLong, AtomicReference, AtomicBoolean                 │
   * │   LongAdder, LongAccumulator, DoubleAdder                                   │
   * │                                                                             │
   * │ java.util.concurrent.locks:                                                 │
   * │   ReentrantLock, ReentrantReadWriteLock, StampedLock                        │
   * │   Condition, LockSupport                                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // THREAD CREATION & LIFECYCLE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 3 WAYS TO CREATE THREADS                                                    │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. EXTEND THREAD (ít dùng - không flexible):                                │
   * │                                                                             │
   * │ class MyThread extends Thread {                                             │
   * │     @Override                                                               │
   * │     public void run() {                                                     │
   * │         System.out.println("Thread: " + getName());                         │
   * │     }                                                                       │
   * │ }                                                                           │
   * │ new MyThread().start();  // start() không phải run()!                       │
   * │                                                                             │
   * │ 2. IMPLEMENT RUNNABLE (preferred):                                          │
   * │                                                                             │
   * │ Runnable task = () -> System.out.println("Hello");                          │
   * │ new Thread(task, "worker-1").start();                                       │
   * │                                                                             │
   * │ 3. IMPLEMENT CALLABLE (return value + exception):                           │
   * │                                                                             │
   * │ Callable<Integer> task = () -> {                                            │
   * │     return heavyComputation();                                              │
   * │ };                                                                          │
   * │ Future<Integer> future = executor.submit(task);                             │
   * │ Integer result = future.get();  // Blocking!                                │
   * │                                                                             │
   * │ ┌────────────────┬──────────────────────────────────────────────────────┐   │
   * │ │ Runnable       │ void run() - no return, no checked exception         │   │
   * │ │ Callable<V>    │ V call() throws Exception - return + exception       │   │
   * │ └────────────────┴──────────────────────────────────────────────────────┘   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ THREAD STATES (Thread.State enum)                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │  NEW ──start()──▶ RUNNABLE ◀──────────────────────────────────────┐         │
   * │                      │                                            │         │
   * │                      │ synchronized                               │         │
   * │                      ▼                                            │         │
   * │                   BLOCKED ──got lock──────────────────────────────┤         │
   * │                      │                                            │         │
   * │                      │ wait()/join()                              │         │
   * │                      ▼                                            │         │
   * │                   WAITING ──notify()/join done────────────────────┤         │
   * │                      │                                            │         │
   * │                      │ sleep(ms)/wait(ms)                         │         │
   * │                      ▼                                            │         │
   * │                TIMED_WAITING ──timeout/notify()───────────────────┘         │
   * │                      │                                                      │
   * │                      │ run() completed/exception                            │
   * │                      ▼                                                      │
   * │                 TERMINATED                                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ THREAD INTERRUPTION (Cách dừng thread đúng cách)                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ⚠️ NEVER USE: thread.stop() - deprecated, unsafe, có thể corrupt data!      │
   * │                                                                             │
   * │ ✓ ĐÚNG CÁCH: Dùng interrupt flag                                            │
   * │                                                                             │
   * │ class Worker implements Runnable {                                          │
   * │     @Override                                                               │
   * │     public void run() {                                                     │
   * │         while (!Thread.currentThread().isInterrupted()) {                   │
   * │             try {                                                           │
   * │                 doWork();                                                   │
   * │                 Thread.sleep(1000);                                         │
   * │             } catch (InterruptedException e) {                              │
   * │                 // sleep() throws InterruptedException khi bị interrupt     │
   * │                 Thread.currentThread().interrupt(); // Restore flag!        │
   * │                 break;  // Exit gracefully                                  │
   * │             }                                                               │
   * │         }                                                                   │
   * │         cleanup();  // Cleanup resources                                    │
   * │     }                                                                       │
   * │ }                                                                           │
   * │                                                                             │
   * │ // Để stop thread:                                                          │
   * │ workerThread.interrupt();                                                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // EXECUTORSERVICE & THREAD POOLS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ WHY THREAD POOLS?                                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PROBLEMS với raw Thread:                                                    │
   * │ • Thread creation expensive (~1MB stack memory, OS overhead)                │
   * │ • Không control được số lượng → OOM, context switching overhead             │
   * │ • Khó manage lifecycle                                                      │
   * │                                                                             │
   * │ SOLUTION: Thread Pool - reuse threads                                       │
   * │                                                                             │
   * │ ┌─────────────────────────────────────────────────────────────────────────┐ │
   * │ │  Task Queue: [T1] [T2] [T3] [T4] [T5] ...                               │ │
   * │ │                    │                                                    │ │
   * │ │                    ▼                                                    │ │
   * │ │  ┌─────────────────────────────────────────────────────────────────┐    │ │
   * │ │  │              Thread Pool (fixed size)                           │    │ │
   * │ │  │  [Worker-1: busy] [Worker-2: busy] [Worker-3: idle] [Worker-4]  │    │ │
   * │ │  └─────────────────────────────────────────────────────────────────┘    │ │
   * │ └─────────────────────────────────────────────────────────────────────────┘ │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ EXECUTOR TYPES (Executors factory)                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. FIXED THREAD POOL:                                                       │
   * │ ExecutorService exec = Executors.newFixedThreadPool(4);                     │
   * │ // 4 threads cố định, unbounded queue                                       │
   * │ // Use: CPU-bound tasks, workload ổn định                                   │
   * │                                                                             │
   * │ 2. CACHED THREAD POOL:                                                      │
   * │ ExecutorService exec = Executors.newCachedThreadPool();                     │
   * │ // Tạo thread mới khi cần, reuse idle threads (60s timeout)                 │
   * │ // ⚠️ DANGER: Có thể tạo vô hạn threads!                                    │
   * │ // Use: Nhiều short-lived tasks, load thấp                                  │
   * │                                                                             │
   * │ 3. SINGLE THREAD EXECUTOR:                                                  │
   * │ ExecutorService exec = Executors.newSingleThreadExecutor();                 │
   * │ // 1 thread duy nhất, đảm bảo thứ tự FIFO                                   │
   * │ // Use: Sequential processing, event logging                                │
   * │                                                                             │
   * │ 4. SCHEDULED THREAD POOL:                                                   │
   * │ ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);   │
   * │ scheduler.schedule(task, 5, TimeUnit.SECONDS);  // Run sau 5s               │
   * │ scheduler.scheduleAtFixedRate(task, 0, 1, SECONDS);  // Mỗi 1s              │
   * │ scheduler.scheduleWithFixedDelay(task, 0, 1, SECONDS);  // Delay 1s sau khi done│
   * │                                                                             │
   * │ 5. WORK STEALING POOL (Java 8+):                                            │
   * │ ExecutorService exec = Executors.newWorkStealingPool();                     │
   * │ // ForkJoinPool, parallelism = CPU cores                                    │
   * │ // Use: Recursive/divide-conquer tasks                                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ THREADPOOLEXECUTOR (Full control - PRODUCTION RECOMMENDED)                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // Executors.newFixedThreadPool() dùng unbounded queue → memory leak!       │
   * │ // Production: Luôn dùng bounded queue                                      │
   * │                                                                             │
   * │ ThreadPoolExecutor executor = new ThreadPoolExecutor(                       │
   * │     4,                              // corePoolSize: threads luôn giữ       │
   * │     8,                              // maxPoolSize: tối đa khi queue đầy    │
   * │     60L, TimeUnit.SECONDS,          // keepAlive: idle timeout              │
   * │     new ArrayBlockingQueue<>(100),  // ⚠️ BOUNDED QUEUE!                    │
   * │     new ThreadFactoryBuilder()      // Custom thread names                  │
   * │         .setNameFormat("worker-%d")                                         │
   * │         .build(),                                                           │
   * │     new ThreadPoolExecutor.CallerRunsPolicy()  // Rejection handler         │
   * │ );                                                                          │
   * │                                                                             │
   * │ REJECTION POLICIES (Khi queue đầy + max threads đạt):                       │
   * │ ┌──────────────────────────┬────────────────────────────────────────────┐   │
   * │ │ AbortPolicy (default)    │ Throw RejectedExecutionException           │   │
   * │ │ CallerRunsPolicy         │ Caller thread chạy task (backpressure)     │   │
   * │ │ DiscardPolicy            │ Bỏ task mới (silent)                       │   │
   * │ │ DiscardOldestPolicy      │ Bỏ task cũ nhất trong queue                │   │
   * │ └──────────────────────────┴────────────────────────────────────────────┘   │
   * │                                                                             │
   * │ FORMULA cho pool size:                                                      │
   * │ • CPU-bound: threads = CPU cores                                            │
   * │ • I/O-bound: threads = CPU cores × (1 + wait_time/compute_time)             │
   * │ • Example: 4 cores, wait=100ms, compute=10ms → 4 × 11 = 44 threads          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ EXECUTOR SHUTDOWN (QUAN TRỌNG!)                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // ⚠️ Executors tạo non-daemon threads → app không exit nếu không shutdown  │
   * │                                                                             │
   * │ // Pattern chuẩn:                                                           │
   * │ executor.shutdown();  // Không nhận task mới, chờ running tasks             │
   * │ try {                                                                       │
   * │     if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {                 │
   * │         executor.shutdownNow();  // Cancel running tasks                    │
   * │         if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {             │
   * │             log.error("Pool did not terminate");                            │
   * │         }                                                                   │
   * │     }                                                                       │
   * │ } catch (InterruptedException e) {                                          │
   * │     executor.shutdownNow();                                                 │
   * │     Thread.currentThread().interrupt();                                     │
   * │ }                                                                           │
   * │                                                                             │
   * │ // Spring Boot: Dùng @Bean với destroyMethod                                │
   * │ @Bean(destroyMethod = "shutdown")                                           │
   * │ public ExecutorService taskExecutor() { ... }                               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // FUTURE & COMPLETABLEFUTURE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ FUTURE (Java 5) - Basic async result                                        │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ExecutorService executor = Executors.newFixedThreadPool(2);                 │
   * │                                                                             │
   * │ Future<String> future = executor.submit(() -> {                             │
   * │     Thread.sleep(1000);                                                     │
   * │     return "Result";                                                        │
   * │ });                                                                         │
   * │                                                                             │
   * │ // BLOCKING operations:                                                     │
   * │ String result = future.get();                    // Block vô hạn            │
   * │ String result = future.get(5, TimeUnit.SECONDS); // Block với timeout       │
   * │                                                                             │
   * │ // Check status:                                                            │
   * │ future.isDone();      // Đã hoàn thành?                                     │
   * │ future.isCancelled(); // Đã bị cancel?                                      │
   * │ future.cancel(true);  // Cancel (true = interrupt)                          │
   * │                                                                             │
   * │ ⚠️ LIMITATIONS:                                                             │
   * │ • get() blocks → không thể compose                                          │
   * │ • Không có callback/chaining                                                │
   * │ • Không có exception handling tốt                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ COMPLETABLEFUTURE (Java 8) - Modern async                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // KHỞI TẠO:                                                                │
   * │ CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {        │
   * │     return fetchData();  // Runs on ForkJoinPool.commonPool()               │
   * │ });                                                                         │
   * │                                                                             │
   * │ // Với custom executor:                                                     │
   * │ CompletableFuture<String> f2 = CompletableFuture.supplyAsync(               │
   * │     () -> fetchData(),                                                      │
   * │     myExecutor                                                              │
   * │ );                                                                          │
   * │                                                                             │
   * │ // No return value:                                                         │
   * │ CompletableFuture<Void> f3 = CompletableFuture.runAsync(() -> {             │
   * │     sendNotification();                                                     │
   * │ });                                                                         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ COMPLETABLEFUTURE CHAINING                                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // TRANSFORM: thenApply (như map)                                           │
   * │ CompletableFuture<Integer> lengthFuture = fetchDataAsync()                  │
   * │     .thenApply(data -> data.length());  // String → Integer                 │
   * │                                                                             │
   * │ // CONSUME: thenAccept (no return)                                          │
   * │ fetchDataAsync()                                                            │
   * │     .thenAccept(data -> log.info("Got: {}", data));                         │
   * │                                                                             │
   * │ // RUN: thenRun (ignore result)                                             │
   * │ fetchDataAsync()                                                            │
   * │     .thenRun(() -> log.info("Completed!"));                                 │
   * │                                                                             │
   * │ // FLATMAP: thenCompose (chain async calls)                                 │
   * │ CompletableFuture<Order> orderFuture = getUserAsync(userId)                 │
   * │     .thenCompose(user -> getOrderAsync(user.getId()));  // Future → Future  │
   * │                                                                             │
   * │ // ASYNC variants: runs on different thread                                 │
   * │ .thenApplyAsync(data -> process(data))                                      │
   * │ .thenApplyAsync(data -> process(data), myExecutor)                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ COMBINING MULTIPLE FUTURES                                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ CompletableFuture<User> userF = getUserAsync(id);                           │
   * │ CompletableFuture<List<Order>> ordersF = getOrdersAsync(id);                │
   * │ CompletableFuture<Profile> profileF = getProfileAsync(id);                  │
   * │                                                                             │
   * │ // COMBINE TWO: thenCombine                                                 │
   * │ CompletableFuture<UserProfile> combined = userF.thenCombine(ordersF,        │
   * │     (user, orders) -> new UserProfile(user, orders)                         │
   * │ );                                                                          │
   * │                                                                             │
   * │ // WAIT ALL: allOf (returns Void)                                           │
   * │ CompletableFuture<Void> all = CompletableFuture.allOf(userF, ordersF);      │
   * │ all.thenRun(() -> {                                                         │
   * │     User user = userF.join();      // Already completed, non-blocking       │
   * │     List<Order> orders = ordersF.join();                                    │
   * │     process(user, orders);                                                  │
   * │ });                                                                         │
   * │                                                                             │
   * │ // WAIT ANY: anyOf (first to complete)                                      │
   * │ CompletableFuture<Object> any = CompletableFuture.anyOf(f1, f2, f3);        │
   * │                                                                             │
   * │ // PATTERN: Collect all results                                             │
   * │ List<CompletableFuture<Data>> futures = ids.stream()                        │
   * │     .map(id -> fetchAsync(id))                                              │
   * │     .collect(toList());                                                     │
   * │                                                                             │
   * │ CompletableFuture<List<Data>> allResults = CompletableFuture                │
   * │     .allOf(futures.toArray(new CompletableFuture[0]))                       │
   * │     .thenApply(v -> futures.stream()                                        │
   * │         .map(CompletableFuture::join)                                       │
   * │         .collect(toList()));                                                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ ERROR HANDLING                                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // EXCEPTIONALLY: recover from error                                        │
   * │ fetchDataAsync()                                                            │
   * │     .exceptionally(ex -> {                                                  │
   * │         log.error("Failed", ex);                                            │
   * │         return "default";  // Fallback value                                │
   * │     });                                                                     │
   * │                                                                             │
   * │ // HANDLE: handle cả success và failure                                     │
   * │ fetchDataAsync()                                                            │
   * │     .handle((result, ex) -> {                                               │
   * │         if (ex != null) {                                                   │
   * │             return handleError(ex);                                         │
   * │         }                                                                   │
   * │         return processResult(result);                                       │
   * │     });                                                                     │
   * │                                                                             │
   * │ // WHENCOMPLETE: side-effect (logging, cleanup)                             │
   * │ fetchDataAsync()                                                            │
   * │     .whenComplete((result, ex) -> {                                         │
   * │         if (ex != null) log.error("Error", ex);                             │
   * │         else log.info("Success: {}", result);                               │
   * │         // Không thay đổi result                                            │
   * │     });                                                                     │
   * │                                                                             │
   * │ // TIMEOUT (Java 9+, workaround Java 8):                                    │
   * │ future.orTimeout(5, TimeUnit.SECONDS);           // Java 9+                 │
   * │ future.completeOnTimeout("default", 5, SECONDS); // Java 9+                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // ASYNC IN HTTP REST API HANDLERS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SERVLET/SPRING MVC THREADING MODEL                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │  Client Request                                                             │
   * │       │                                                                     │
   * │       ▼                                                                     │
   * │  ┌─────────────────────────────────────────────────────────────────────┐    │
   * │  │  Tomcat Thread Pool (default: 200 threads)                          │    │
   * │  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐                    │    │
   * │  │  │Thread-1 │ │Thread-2 │ │Thread-3 │ │Thread-N │                    │    │
   * │  │  │ (busy)  │ │ (busy)  │ │ (idle)  │ │ (idle)  │                    │    │
   * │  │  └────┬────┘ └─────────┘ └─────────┘ └─────────┘                    │    │
   * │  └───────│─────────────────────────────────────────────────────────────┘    │
   * │          │                                                                  │
   * │          ▼                                                                  │
   * │    @GetMapping("/users/{id}")                                               │
   * │    public User getUser(@PathVariable Long id) {                             │
   * │        return userService.findById(id);  // Thread blocked!                 │
   * │    }                                                                        │
   * │                                                                             │
   * │ ⚠️ PROBLEM: Thread bị block khi gọi DB/external API                         │
   * │    → 200 concurrent requests = hết thread!                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN 1: @Async - Fire and Forget                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // Configuration:                                                           │
   * │ @Configuration                                                              │
   * │ @EnableAsync                                                                │
   * │ public class AsyncConfig {                                                  │
   * │     @Bean("asyncExecutor")                                                  │
   * │     public Executor asyncExecutor() {                                       │
   * │         ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();     │
   * │         executor.setCorePoolSize(4);                                        │
   * │         executor.setMaxPoolSize(10);                                        │
   * │         executor.setQueueCapacity(100);                                     │
   * │         executor.setThreadNamePrefix("async-");                             │
   * │         executor.setRejectedExecutionHandler(new CallerRunsPolicy());       │
   * │         executor.initialize();                                              │
   * │         return executor;                                                    │
   * │     }                                                                       │
   * │ }                                                                           │
   * │                                                                             │
   * │ // Service:                                                                 │
   * │ @Service                                                                    │
   * │ public class NotificationService {                                          │
   * │     @Async("asyncExecutor")                                                 │
   * │     public void sendEmailAsync(String to, String message) {                 │
   * │         // Runs on async thread, không block caller                         │
   * │         emailClient.send(to, message);                                      │
   * │     }                                                                       │
   * │                                                                             │
   * │     @Async("asyncExecutor")                                                 │
   * │     public CompletableFuture<Boolean> sendEmailWithResult(String to) {      │
   * │         boolean sent = emailClient.send(to, "Hello");                       │
   * │         return CompletableFuture.completedFuture(sent);                     │
   * │     }                                                                       │
   * │ }                                                                           │
   * │                                                                             │
   * │ // Controller:                                                              │
   * │ @PostMapping("/orders")                                                     │
   * │ public ResponseEntity<Order> createOrder(@RequestBody OrderRequest req) {   │
   * │     Order order = orderService.create(req);                                 │
   * │     notificationService.sendEmailAsync(req.getEmail(), "Order created");    │
   * │     return ResponseEntity.ok(order);  // Return immediately                 │
   * │ }                                                                           │
   * │                                                                             │
   * │ ⚠️ CAVEATS:                                                                 │
   * │ • @Async trên cùng class không work (proxy issue)                           │
   * │ • Method phải public                                                        │
   * │ • Không catch được exception nếu void                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN 2: DeferredResult - Release Servlet Thread                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ @GetMapping("/orders/{id}")                                                 │
   * │ public DeferredResult<Order> getOrder(@PathVariable Long id) {              │
   * │     DeferredResult<Order> deferredResult = new DeferredResult<>(5000L);     │
   * │                                                                             │
   * │     // Set timeout handler                                                  │
   * │     deferredResult.onTimeout(() ->                                          │
   * │         deferredResult.setErrorResult(                                      │
   * │             ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build()       │
   * │         )                                                                   │
   * │     );                                                                      │
   * │                                                                             │
   * │     // Process on different thread                                          │
   * │     CompletableFuture.supplyAsync(                                          │
   * │         () -> orderService.findById(id),                                    │
   * │         asyncExecutor                                                       │
   * │     ).whenComplete((order, ex) -> {                                         │
   * │         if (ex != null) {                                                   │
   * │             deferredResult.setErrorResult(ex);                              │
   * │         } else {                                                            │
   * │             deferredResult.setResult(order);  // Complete response          │
   * │         }                                                                   │
   * │     });                                                                     │
   * │                                                                             │
   * │     return deferredResult;  // Servlet thread released immediately!         │
   * │ }                                                                           │
   * │                                                                             │
   * │ FLOW:                                                                       │
   * │ 1. Request arrives → Servlet thread handles                                 │
   * │ 2. Return DeferredResult → Servlet thread released                          │
   * │ 3. Async work completes → setResult() called                                │
   * │ 4. Another thread sends response                                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN 3: CompletableFuture as Return Type                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ @GetMapping("/users/{id}")                                                  │
   * │ public CompletableFuture<User> getUser(@PathVariable Long id) {             │
   * │     return CompletableFuture.supplyAsync(                                   │
   * │         () -> userService.findById(id),                                     │
   * │         asyncExecutor                                                       │
   * │     );                                                                      │
   * │ }                                                                           │
   * │                                                                             │
   * │ // Spring MVC tự động handle như DeferredResult                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN 4: Parallel API Calls (COMMON INTERVIEW QUESTION!)                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // ❌ SEQUENTIAL: 300ms + 200ms + 100ms = 600ms total                       │
   * │ public UserDashboard getDashboardSequential(Long userId) {                  │
   * │     User user = userService.getUser(userId);        // 300ms                │
   * │     List<Order> orders = orderService.getOrders(userId);  // 200ms          │
   * │     Profile profile = profileService.getProfile(userId);  // 100ms          │
   * │     return new UserDashboard(user, orders, profile);                        │
   * │ }                                                                           │
   * │                                                                             │
   * │ // ✓ PARALLEL: max(300ms, 200ms, 100ms) = 300ms total                       │
   * │ public UserDashboard getDashboardParallel(Long userId) {                    │
   * │     CompletableFuture<User> userF = CompletableFuture.supplyAsync(          │
   * │         () -> userService.getUser(userId), executor                         │
   * │     );                                                                      │
   * │     CompletableFuture<List<Order>> ordersF = CompletableFuture.supplyAsync( │
   * │         () -> orderService.getOrders(userId), executor                      │
   * │     );                                                                      │
   * │     CompletableFuture<Profile> profileF = CompletableFuture.supplyAsync(    │
   * │         () -> profileService.getProfile(userId), executor                   │
   * │     );                                                                      │
   * │                                                                             │
   * │     // Wait all to complete                                                 │
   * │     CompletableFuture.allOf(userF, ordersF, profileF).join();               │
   * │                                                                             │
   * │     return new UserDashboard(                                               │
   * │         userF.join(),                                                       │
   * │         ordersF.join(),                                                     │
   * │         profileF.join()                                                     │
   * │     );                                                                      │
   * │ }                                                                           │
   * │                                                                             │
   * │ // ✓ WITH TIMEOUT và ERROR HANDLING:                                        │
   * │ public UserDashboard getDashboardSafe(Long userId) {                        │
   * │     CompletableFuture<User> userF = CompletableFuture                       │
   * │         .supplyAsync(() -> userService.getUser(userId), executor)           │
   * │         .exceptionally(ex -> {                                              │
   * │             log.warn("Failed to get user", ex);                             │
   * │             return null;  // Fallback                                       │
   * │         });                                                                 │
   * │                                                                             │
   * │     CompletableFuture<List<Order>> ordersF = CompletableFuture              │
   * │         .supplyAsync(() -> orderService.getOrders(userId), executor)        │
   * │         .exceptionally(ex -> Collections.emptyList());                      │
   * │                                                                             │
   * │     try {                                                                   │
   * │         CompletableFuture.allOf(userF, ordersF)                             │
   * │             .get(3, TimeUnit.SECONDS);  // Timeout!                         │
   * │         return new UserDashboard(userF.join(), ordersF.join());             │
   * │     } catch (TimeoutException e) {                                          │
   * │         throw new ResponseStatusException(                                  │
   * │             HttpStatus.GATEWAY_TIMEOUT, "Timeout fetching data"             │
   * │         );                                                                  │
   * │     }                                                                       │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN 5: Streaming Response (Large data)                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ @GetMapping("/reports/export")                                              │
   * │ public StreamingResponseBody exportReport() {                               │
   * │     return outputStream -> {                                                │
   * │         // Write data in chunks - memory efficient                          │
   * │         reportService.streamReportData(row -> {                             │
   * │             try {                                                           │
   * │                 outputStream.write(formatRow(row).getBytes());              │
   * │                 outputStream.flush();                                       │
   * │             } catch (IOException e) {                                       │
   * │                 throw new RuntimeException(e);                              │
   * │             }                                                               │
   * │         });                                                                 │
   * │     };                                                                      │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SYNCHRONIZATION & LOCKS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SYNCHRONIZED KEYWORD                                                        │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. METHOD-LEVEL (lock on 'this'):                                           │
   * │ public synchronized void deposit(int amount) {                              │
   * │     balance += amount;                                                      │
   * │ }                                                                           │
   * │                                                                             │
   * │ 2. BLOCK-LEVEL (fine-grained control):                                      │
   * │ private final Object lock = new Object();                                   │
   * │ public void deposit(int amount) {                                           │
   * │     // Non-critical section                                                 │
   * │     synchronized (lock) {                                                   │
   * │         balance += amount;  // Critical section                             │
   * │     }                                                                       │
   * │ }                                                                           │
   * │                                                                             │
   * │ 3. STATIC METHOD (lock on Class object):                                    │
   * │ public static synchronized void staticMethod() {                            │
   * │     // Lock on MyClass.class                                                │
   * │ }                                                                           │
   * │                                                                             │
   * │ ⚠️ BAD PRACTICES:                                                           │
   * │ synchronized (new Object()) { }  // New object mỗi lần = no lock!           │
   * │ synchronized ("lock") { }        // String interning issues                 │
   * │ synchronized (Integer.valueOf(1)) { } // Cached boxed primitives            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ REENTRANTLOCK vs SYNCHRONIZED                                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌──────────────────┬──────────────────────┬─────────────────────────────┐   │
   * │ │ Feature          │ synchronized         │ ReentrantLock               │   │
   * │ ├──────────────────┼──────────────────────┼─────────────────────────────┤   │
   * │ │ Unlock           │ Automatic            │ Manual (finally!)           │   │
   * │ │ Scope            │ Block/method only    │ Can span methods            │   │
   * │ │ Try lock         │ ❌                   │ ✓ tryLock()                 │   │
   * │ │ Timeout          │ ❌                   │ ✓ tryLock(time, unit)       │   │
   * │ │ Fairness         │ ❌                   │ ✓ new ReentrantLock(true)   │   │
   * │ │ Interruptible    │ ❌                   │ ✓ lockInterruptibly()       │   │
   * │ │ Conditions       │ 1 (wait/notify)      │ Multiple newCondition()     │   │
   * │ │ Performance      │ Similar (Java 6+)    │ Similar                     │   │
   * │ └──────────────────┴──────────────────────┴─────────────────────────────┘   │
   * │                                                                             │
   * │ // BASIC USAGE:                                                             │
   * │ private final ReentrantLock lock = new ReentrantLock();                     │
   * │                                                                             │
   * │ public void transfer(Account from, Account to, int amount) {                │
   * │     lock.lock();                                                            │
   * │     try {                                                                   │
   * │         from.withdraw(amount);                                              │
   * │         to.deposit(amount);                                                 │
   * │     } finally {                                                             │
   * │         lock.unlock();  // MUST unlock in finally!                          │
   * │     }                                                                       │
   * │ }                                                                           │
   * │                                                                             │
   * │ // TRY LOCK WITH TIMEOUT (avoid deadlock):                                  │
   * │ public boolean tryTransfer(Account from, Account to, int amount) {          │
   * │     try {                                                                   │
   * │         if (lock.tryLock(1, TimeUnit.SECONDS)) {                            │
   * │             try {                                                           │
   * │                 from.withdraw(amount);                                      │
   * │                 to.deposit(amount);                                         │
   * │                 return true;                                                │
   * │             } finally {                                                     │
   * │                 lock.unlock();                                              │
   * │             }                                                               │
   * │         }                                                                   │
   * │         return false;  // Could not acquire lock                            │
   * │     } catch (InterruptedException e) {                                      │
   * │         Thread.currentThread().interrupt();                                 │
   * │         return false;                                                       │
   * │     }                                                                       │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ READWRITELOCK - Read-Heavy Workloads                                        │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // Multiple readers OR single writer                                        │
   * │ private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(); │
   * │ private final Lock readLock = rwLock.readLock();                            │
   * │ private final Lock writeLock = rwLock.writeLock();                          │
   * │ private Map<String, Object> cache = new HashMap<>();                        │
   * │                                                                             │
   * │ public Object get(String key) {                                             │
   * │     readLock.lock();                                                        │
   * │     try {                                                                   │
   * │         return cache.get(key);                                              │
   * │     } finally {                                                             │
   * │         readLock.unlock();                                                  │
   * │     }                                                                       │
   * │ }                                                                           │
   * │                                                                             │
   * │ public void put(String key, Object value) {                                 │
   * │     writeLock.lock();                                                       │
   * │     try {                                                                   │
   * │         cache.put(key, value);                                              │
   * │     } finally {                                                             │
   * │         writeLock.unlock();                                                 │
   * │     }                                                                       │
   * │ }                                                                           │
   * │                                                                             │
   * │ USE CASE: Config cache, in-memory DB, read-heavy caches                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CONCURRENT COLLECTIONS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ THREAD-SAFE COLLECTIONS                                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ ┌────────────────────┬─────────────────────────┬─────────────────────────┐  │
   * │ │ Non-Thread-Safe    │ Thread-Safe Alternative │ Notes                   │  │
   * │ ├────────────────────┼─────────────────────────┼─────────────────────────┤  │
   * │ │ HashMap            │ ConcurrentHashMap       │ Segment locking         │  │
   * │ │ ArrayList          │ CopyOnWriteArrayList    │ Read-heavy only         │  │
   * │ │ LinkedList (queue) │ ConcurrentLinkedQueue   │ Non-blocking            │  │
   * │ │ PriorityQueue      │ PriorityBlockingQueue   │ Blocking                │  │
   * │ └────────────────────┴─────────────────────────┴─────────────────────────┘  │
   * │                                                                             │
   * │ // CONCURRENTHASHMAP ATOMIC:                                                │
   * │ map.putIfAbsent("key", 1);                                                  │
   * │ map.computeIfAbsent("key", k -> compute(k));                                │
   * │ map.merge("key", 1, Integer::sum);                                          │
   * │                                                                             │
   * │ // BLOCKINGQUEUE:                                                           │
   * │ queue.put(task);   // Blocks if full                                        │
   * │ queue.take();      // Blocks if empty                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // ATOMICS & THREADLOCAL
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ ATOMIC (Lock-free CAS)           │ THREADLOCAL (Per-thread storage)         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ AtomicInteger counter = ...;     │ ThreadLocal<User> ctx = ...;             │
   * │ counter.incrementAndGet();       │ ctx.set(user);                           │
   * │ counter.compareAndSet(old,new);  │ ctx.get();                               │
   * │                                  │ ctx.remove(); // MUST in thread pool!    │
   * │ // High contention: LongAdder    │                                          │
   * │ LongAdder adder = ...;           │ // Memory leak if forgot remove()!       │
   * │ adder.increment();               │ // Always use try-finally                │
   * │ adder.sum();                     │                                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SYNC UTILITIES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ CountDownLatch(n)   │ Wait for n tasks, one-time use                        │
   * │                     │ latch.countDown(); latch.await();                     │
   * ├─────────────────────┼───────────────────────────────────────────────────────┤
   * │ CyclicBarrier(n)    │ All parties meet, reusable auto-reset                 │
   * │                     │ barrier.await();                                      │
   * ├─────────────────────┼───────────────────────────────────────────────────────┤
   * │ Semaphore(n)        │ Limit concurrent access (n permits)                   │
   * │                     │ sem.acquire(); try{...} finally{sem.release();}       │
   * └─────────────────────┴───────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // COMMON CONCURRENCY BUGS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 1. Shared mutable state in singleton bean                                   │
   * │    ❌ private Order current; // All requests share!                          │
   * │    ✓ Pass as parameter OR use ThreadLocal                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 2. ThreadLocal memory leak                                                  │
   * │    ❌ Forgot remove() → data persists in thread pool                         │
   * │    ✓ Always try-finally with remove()                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 3. Blocking in ForkJoinPool.commonPool()                                    │
   * │    ❌ supplyAsync(() -> blockingIO())                                         │
   * │    ✓ supplyAsync(() -> blockingIO(), ioExecutor)                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 4. Double-checked locking without volatile                                  │
   * │    ❌ private static Singleton instance;                                     │
   * │    ✓ private static volatile Singleton instance;                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 5. Forgot ExecutorService.shutdown()                                        │
   * │    ❌ App never terminates (non-daemon threads)                              │
   * │    ✓ @PreDestroy shutdown() or @Bean(destroyMethod="shutdown")              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

}


