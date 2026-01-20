package com.practice.leetcode.interview;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                         JAVA CORE INTERVIEW Q&A                            ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class JavaCoreQA {

  // ═══════════════════════════════════════════════════════════════════════════
  // OOP CONCEPTS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: 4 nguyên tắc OOP là gì?                                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │ 1. ENCAPSULATION (Đóng gói)                                                 │
   * │    - Gộp data + methods vào 1 class                                         │
   * │    - Dùng private fields + public getters/setters                           │
   * │    - Ẩn implementation details                                              │
   * │                                                                             │
   * │ 2. INHERITANCE (Kế thừa)                                                    │
   * │    - Class con kế thừa từ class cha (extends)                               │
   * │    - Tái sử dụng code                                                       │
   * │    - Java chỉ hỗ trợ single inheritance                                     │
   * │                                                                             │
   * │ 3. POLYMORPHISM (Đa hình)                                                   │
   * │    - Compile-time: Method Overloading (cùng tên, khác params)               │
   * │    - Runtime: Method Overriding (class con override method cha)             │
   * │                                                                             │
   * │ 4. ABSTRACTION (Trừu tượng)                                                 │
   * │    - Ẩn complexity, chỉ show interface                                      │
   * │    - Dùng abstract class hoặc interface                                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Abstract class vs Interface?                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌─────────────────────┬─────────────────────┬─────────────────────────────┐ │
   * │ │                     │ Abstract Class      │ Interface                   │ │
   * │ ├─────────────────────┼─────────────────────┼─────────────────────────────┤ │
   * │ │ Inheritance         │ extends (1 class)   │ implements (nhiều)          │ │
   * │ │ Constructor         │ Có                  │ Không                       │ │
   * │ │ Fields              │ Có (mọi loại)       │ Chỉ public static final     │ │
   * │ │ Methods             │ abstract + concrete │ abstract + default (Java 8) │ │
   * │ │ Access modifiers    │ Mọi loại            │ Chỉ public                  │ │
   * │ └─────────────────────┴─────────────────────┴─────────────────────────────┘ │
   * │                                                                             │
   * │ KHI NÀO DÙNG:                                                               │
   * │ - Abstract class: Khi có shared code/state giữa các subclass                │
   * │ - Interface: Khi định nghĩa contract, multiple inheritance                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // COLLECTIONS FRAMEWORK
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: ArrayList vs LinkedList?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌────────────────┬─────────────────────────┬────────────────────────────┐   │
   * │ │                │ ArrayList               │ LinkedList                 │   │
   * │ ├────────────────┼─────────────────────────┼────────────────────────────┤   │
   * │ │ Structure      │ Dynamic array           │ Doubly linked list         │   │
   * │ │ get(index)     │ O(1)                    │ O(n)                       │   │
   * │ │ add(end)       │ O(1) amortized          │ O(1)                       │   │
   * │ │ add(middle)    │ O(n)                    │ O(1) nếu có node           │   │
   * │ │ remove         │ O(n)                    │ O(1) nếu có node           │   │
   * │ │ Memory         │ Ít hơn                  │ Nhiều hơn (pointers)       │   │
   * │ └────────────────┴─────────────────────────┴────────────────────────────┘   │
   * │                                                                             │
   * │ USE CASE:                                                                   │
   * │ - ArrayList: Random access thường xuyên, ít insert/delete                   │
   * │ - LinkedList: Insert/delete thường xuyên ở đầu/giữa                         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: HashMap vs HashTable vs ConcurrentHashMap?                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌────────────────┬───────────────┬───────────────┬────────────────────────┐ │
   * │ │                │ HashMap       │ HashTable     │ ConcurrentHashMap      │ │
   * │ ├────────────────┼───────────────┼───────────────┼────────────────────────┤ │
   * │ │ Thread-safe    │ ❌            │ ✓ (sync)      │ ✓ (segment locks)      │ │
   * │ │ Null key/value │ ✓             │ ❌            │ ❌                     │ │
   * │ │ Performance    │ Fastest       │ Slowest       │ Fast + thread-safe     │ │
   * │ │ Legacy         │ ❌            │ ✓             │ ❌                     │ │
   * │ └────────────────┴───────────────┴───────────────┴────────────────────────┘ │
   * │                                                                             │
   * │ BEST PRACTICE: Dùng ConcurrentHashMap cho multi-threaded, HashMap cho rest  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: HashSet vs TreeSet vs LinkedHashSet?                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌────────────────┬───────────────┬───────────────┬────────────────────────┐ │
   * │ │                │ HashSet       │ TreeSet       │ LinkedHashSet          │ │
   * │ ├────────────────┼───────────────┼───────────────┼────────────────────────┤ │
   * │ │ Order          │ Không         │ Sorted        │ Insertion order        │ │
   * │ │ add/remove/    │ O(1)          │ O(log n)      │ O(1)                   │ │
   * │ │ contains       │               │               │                        │ │
   * │ │ Null           │ ✓ (1 null)    │ ❌            │ ✓ (1 null)             │ │
   * │ │ Backing        │ HashMap       │ TreeMap       │ LinkedHashMap          │ │
   * │ └────────────────┴───────────────┴───────────────┴────────────────────────┘ │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // MULTITHREADING & CONCURRENCY
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Cách tạo Thread trong Java?                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: 3 cách:                                                                  │
   * │                                                                             │
   * │ 1. EXTENDS THREAD:                                                          │
   * │    class MyThread extends Thread {                                          │
   * │        public void run() { ... }                                            │
   * │    }                                                                        │
   * │    new MyThread().start();                                                  │
   * │                                                                             │
   * │ 2. IMPLEMENTS RUNNABLE (Recommended):                                       │
   * │    class MyRunnable implements Runnable {                                   │
   * │        public void run() { ... }                                            │
   * │    }                                                                        │
   * │    new Thread(new MyRunnable()).start();                                    │
   * │                                                                             │
   * │ 3. IMPLEMENTS CALLABLE (có return value):                                   │
   * │    Callable<Integer> task = () -> { return 42; };                           │
   * │    ExecutorService executor = Executors.newSingleThreadExecutor();          │
   * │    Future<Integer> future = executor.submit(task);                          │
   * │    Integer result = future.get();                                           │
   * │                                                                             │
   * │ WHY RUNNABLE > THREAD?                                                      │
   * │ - Java không hỗ trợ multiple inheritance                                    │
   * │ - Runnable có thể dùng với ExecutorService                                  │
   * │ - Separation of concerns                                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: synchronized vs volatile vs Lock?                                        │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ SYNCHRONIZED:                                                               │
   * │ - Đảm bảo mutual exclusion (chỉ 1 thread vào critical section)              │
   * │ - Có thể dùng với method hoặc block                                         │
   * │ - Tự động acquire/release lock                                              │
   * │                                                                             │
   * │ VOLATILE:                                                                   │
   * │ - Đảm bảo visibility (tất cả threads thấy giá trị mới nhất)                 │
   * │ - KHÔNG đảm bảo atomicity (count++ vẫn không thread-safe)                   │
   * │ - Dùng cho flag, singleton double-check locking                             │
   * │                                                                             │
   * │ LOCK (ReentrantLock):                                                       │
   * │ - Linh hoạt hơn synchronized                                                │
   * │ - Có tryLock(), lockInterruptibly()                                         │
   * │ - Có thể fair lock (FIFO)                                                   │
   * │ - PHẢI manually unlock trong finally block                                  │
   * │                                                                             │
   * │ ┌───────────────────┬─────────────────────────────────────────────────────┐ │
   * │ │ Use case          │ Choice                                              │ │
   * │ ├───────────────────┼─────────────────────────────────────────────────────┤ │
   * │ │ Simple mutex      │ synchronized                                        │ │
   * │ │ Flag/status       │ volatile                                            │ │
   * │ │ Try lock, timeout │ ReentrantLock                                       │ │
   * │ │ Read-heavy        │ ReadWriteLock                                       │ │
   * │ │ Counter           │ AtomicInteger                                       │ │
   * │ └───────────────────┴─────────────────────────────────────────────────────┘ │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Thread Pool là gì? Các loại ThreadPool?                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Thread pool quản lý và tái sử dụng threads                               │
   * │                                                                             │
   * │ ┌────────────────────────────┬─────────────────────────────────────────────┐│
   * │ │ Type                       │ Use case                                    ││
   * │ ├────────────────────────────┼─────────────────────────────────────────────┤│
   * │ │ newFixedThreadPool(n)      │ Số thread cố định, task queue unlimited     ││
   * │ │                            │ Dùng cho CPU-bound tasks                    ││
   * │ ├────────────────────────────┼─────────────────────────────────────────────┤│
   * │ │ newCachedThreadPool()      │ Tạo thread mới khi cần, reuse nếu có        ││
   * │ │                            │ Dùng cho short-lived async tasks            ││
   * │ ├────────────────────────────┼─────────────────────────────────────────────┤│
   * │ │ newSingleThreadExecutor()  │ 1 thread, task thực hiện tuần tự            ││
   * │ │                            │ Dùng khi cần order guarantee                ││
   * │ ├────────────────────────────┼─────────────────────────────────────────────┤│
   * │ │ newScheduledThreadPool(n)  │ Scheduled/periodic tasks                    ││
   * │ └────────────────────────────┴─────────────────────────────────────────────┘│
   * │                                                                             │
   * │ BEST PRACTICE:                                                              │
   * │ - CPU-bound: threads = number of cores                                      │
   * │ - I/O-bound: threads = cores × (1 + wait_time/compute_time)                 │
   * │ - Luôn shutdown() executor khi xong                                         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // MEMORY MANAGEMENT
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Heap vs Stack?                                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌────────────────┬─────────────────────────┬────────────────────────────┐   │
   * │ │                │ Stack                   │ Heap                       │   │
   * │ ├────────────────┼─────────────────────────┼────────────────────────────┤   │
   * │ │ Lưu gì         │ Primitives, references  │ Objects                    │   │
   * │ │ Scope          │ Method scope            │ Global                     │   │
   * │ │ Size           │ Nhỏ (512KB - 1MB)       │ Lớn (configurable)         │   │
   * │ │ Speed          │ Nhanh                   │ Chậm hơn                   │   │
   * │ │ Thread         │ Mỗi thread có stack     │ Shared giữa threads        │   │
   * │ │ Management     │ LIFO, tự động           │ GC                         │   │
   * │ │ Error          │ StackOverflowError      │ OutOfMemoryError           │   │
   * │ └────────────────┴─────────────────────────┴────────────────────────────┘   │
   * │                                                                             │
   * │ EXAMPLE:                                                                    │
   * │   int x = 10;              // x ở Stack, 10 ở Stack                         │
   * │   String s = new String(); // s (reference) ở Stack, object ở Heap          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Garbage Collection hoạt động như nào?                                    │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ HEAP STRUCTURE:                                                             │
   * │ ┌─────────────────────────────────────────────────────────────────────────┐ │
   * │ │      Young Generation        │         Old Generation                   │ │
   * │ │ ┌───────────┬────────────────┤                                          │ │
   * │ │ │   Eden    │  Survivor (S0, S1)                                        │ │
   * │ │ └───────────┴────────────────┴──────────────────────────────────────────┤ │
   * │ └─────────────────────────────────────────────────────────────────────────┘ │
   * │                                                                             │
   * │ PROCESS:                                                                    │
   * │ 1. New objects → Eden                                                       │
   * │ 2. Eden full → Minor GC → survivors move to S0/S1                           │
   * │ 3. Objects survive nhiều lần → Old Gen                                      │
   * │ 4. Old Gen full → Major GC (Full GC) - SLOW!                                │
   * │                                                                             │
   * │ GC ALGORITHMS:                                                              │
   * │ - Serial GC: Single thread, small apps                                      │
   * │ - Parallel GC: Multiple threads, throughput focus                           │
   * │ - G1 GC: Low latency, large heaps (default Java 9+)                         │
   * │ - ZGC: Ultra-low latency (< 10ms pause)                                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // JAVA 8+ FEATURES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Lambda expression là gì?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Cách viết ngắn gọn cho anonymous class với 1 method                      │
   * │                                                                             │
   * │ TRƯỚC (Anonymous class):                                                    │
   * │   Runnable r = new Runnable() {                                             │
   * │       @Override                                                             │
   * │       public void run() { System.out.println("Hello"); }                    │
   * │   };                                                                        │
   * │                                                                             │
   * │ SAU (Lambda):                                                               │
   * │   Runnable r = () -> System.out.println("Hello");                           │
   * │                                                                             │
   * │ REQUIREMENT: Chỉ dùng với Functional Interface (1 abstract method)          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Stream API là gì? Khi nào dùng?                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: API để xử lý collections theo style functional                           │
   * │                                                                             │
   * │ EXAMPLE:                                                                    │
   * │   List<String> names = Arrays.asList("Alice", "Bob", "Charlie");            │
   * │                                                                             │
   * │   // Filter + transform + collect                                           │
   * │   List<String> result = names.stream()                                      │
   * │       .filter(n -> n.length() > 3)    // Intermediate                       │
   * │       .map(String::toUpperCase)       // Intermediate                       │
   * │       .collect(Collectors.toList());  // Terminal                           │
   * │                                                                             │
   * │ OPERATIONS:                                                                 │
   * │ - Intermediate: filter, map, flatMap, sorted, distinct, limit               │
   * │ - Terminal: collect, forEach, reduce, count, findFirst, anyMatch            │
   * │                                                                             │
   * │ PARALLEL STREAM:                                                            │
   * │   list.parallelStream()  // Multi-threaded processing                       │
   * │                                                                             │
   * │ CAUTION:                                                                    │
   * │ - Streams are lazily evaluated (không chạy đến khi có terminal op)          │
   * │ - Streams không reusable (chỉ dùng 1 lần)                                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Optional là gì? Tại sao dùng?                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Container có thể chứa value hoặc empty, thay thế null                    │
   * │                                                                             │
   * │ TRƯỚC:                                                                      │
   * │   User user = findUser(id);                                                 │
   * │   if (user != null) {                                                       │
   * │       Address addr = user.getAddress();                                     │
   * │       if (addr != null) { ... }  // Null check hell!                        │
   * │   }                                                                         │
   * │                                                                             │
   * │ SAU:                                                                        │
   * │   Optional<User> user = findUser(id);                                       │
   * │   String city = user                                                        │
   * │       .map(User::getAddress)                                                │
   * │       .map(Address::getCity)                                                │
   * │       .orElse("Unknown");                                                   │
   * │                                                                             │
   * │ KEY METHODS:                                                                │
   * │ - of(value), ofNullable(value), empty()                                     │
   * │ - isPresent(), isEmpty() (Java 11)                                          │
   * │ - get(), orElse(), orElseGet(), orElseThrow()                               │
   * │ - map(), flatMap(), filter()                                                │
   * │                                                                             │
   * │ BEST PRACTICES:                                                             │
   * │ - Dùng cho return type, KHÔNG dùng cho parameters hay fields                │
   * │ - KHÔNG dùng get() mà không check isPresent()                               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // STRING & IMMUTABILITY
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: String Pool là gì?                                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Vùng nhớ đặc biệt trong Heap lưu String literals để tái sử dụng          │
   * │                                                                             │
   * │ EXAMPLE:                                                                    │
   * │   String s1 = "Hello";        // Tạo trong pool                             │
   * │   String s2 = "Hello";        // Reuse từ pool                              │
   * │   String s3 = new String("Hello"); // Tạo mới trong Heap, KHÔNG pool        │
   * │                                                                             │
   * │   s1 == s2;    // TRUE (cùng reference)                                     │
   * │   s1 == s3;    // FALSE (khác reference)                                    │
   * │   s1.equals(s3); // TRUE (cùng content)                                     │
   * │                                                                             │
   * │   s3.intern(); // Đưa vào pool, return reference trong pool                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: String vs StringBuilder vs StringBuffer?                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌──────────────┬────────────────┬────────────────┬────────────────────────┐ │
   * │ │              │ String         │ StringBuilder  │ StringBuffer           │ │
   * │ ├──────────────┼────────────────┼────────────────┼────────────────────────┤ │
   * │ │ Mutability   │ Immutable      │ Mutable        │ Mutable                │ │
   * │ │ Thread-safe  │ ✓ (immutable)  │ ❌             │ ✓ (synchronized)       │ │
   * │ │ Performance  │ Slowest*       │ Fastest        │ Slower than SB         │ │
   * │ └──────────────┴────────────────┴────────────────┴────────────────────────┘ │
   * │                                                                             │
   * │ * String chậm khi concatenation vì tạo object mới mỗi lần                   │
   * │                                                                             │
   * │ USE CASE:                                                                   │
   * │ - String: Ít thay đổi, literals                                             │
   * │ - StringBuilder: Loop concatenation, single thread                          │
   * │ - StringBuffer: Multi-threaded (hiếm dùng, prefer StringBuilder + sync)     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // EQUALS & HASHCODE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: == vs equals()?                                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │ - == : So sánh REFERENCE (có phải cùng object không?)                       │
   * │ - equals(): So sánh CONTENT (giá trị có bằng nhau không?)                   │
   * │                                                                             │
   * │ EXAMPLE:                                                                    │
   * │   Integer a = new Integer(100);                                             │
   * │   Integer b = new Integer(100);                                             │
   * │   a == b;      // FALSE (khác object)                                       │
   * │   a.equals(b); // TRUE (cùng value)                                         │
   * │                                                                             │
   * │   Integer c = 100;  // Autoboxing, từ cache (-128 to 127)                   │
   * │   Integer d = 100;                                                          │
   * │   c == d;      // TRUE! (cùng cached object)                                │
   * │                                                                             │
   * │   Integer e = 200;                                                          │
   * │   Integer f = 200;                                                          │
   * │   e == f;      // FALSE (ngoài cache range)                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Tại sao phải override cả equals() và hashCode()?                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: CONTRACT:                                                                │
   * │ - Nếu a.equals(b) == true → a.hashCode() == b.hashCode()                    │
   * │ - Ngược lại KHÔNG bắt buộc (collision OK)                                   │
   * │                                                                             │
   * │ NẾU CHỈ OVERRIDE EQUALS:                                                    │
   * │   class Person {                                                            │
   * │       String name;                                                          │
   * │       @Override boolean equals(Object o) { ... }                            │
   * │       // Không override hashCode!                                           │
   * │   }                                                                         │
   * │                                                                             │
   * │   Set<Person> set = new HashSet<>();                                        │
   * │   set.add(new Person("Alice"));                                             │
   * │   set.contains(new Person("Alice")); // FALSE! (different hashCode)         │
   * │                                                                             │
   * │ → HashMap, HashSet sẽ không hoạt động đúng!                                 │
   * │                                                                             │
   * │ BEST PRACTICE:                                                              │
   * │ - Dùng IDE generate hoặc Lombok @EqualsAndHashCode                          │
   * │ - Dùng Objects.hash(field1, field2, ...)                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // EXCEPTION HANDLING
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Checked vs Unchecked Exception?                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ EXCEPTION HIERARCHY:                                                        │
   * │                    Throwable                                                │
   * │                   /         \                                               │
   * │              Error          Exception                                       │
   * │      (Unchecked)           /         \                                      │
   * │                  RuntimeException   IOException, SQLException...            │
   * │                   (Unchecked)            (Checked)                          │
   * │                                                                             │
   * │ ┌────────────────┬─────────────────────────┬────────────────────────────┐   │
   * │ │                │ Checked                 │ Unchecked                  │   │
   * │ ├────────────────┼─────────────────────────┼────────────────────────────┤   │
   * │ │ Compile time   │ Bắt buộc handle         │ Không bắt buộc             │   │
   * │ │ Extends        │ Exception               │ RuntimeException           │   │
   * │ │ Ví dụ          │ IOException, SQL        │ NullPointer, ArrayIndex    │   │
   * │ │ Use case       │ Recoverable errors      │ Programming bugs           │   │
   * │ └────────────────┴─────────────────────────┴────────────────────────────┘   │
   * │                                                                             │
   * │ BEST PRACTICES:                                                             │
   * │ - Prefer unchecked cho business logic errors                                │
   * │ - Không catch Exception/Throwable (quá chung)                               │
   * │ - Luôn log exception trước khi swallow                                      │
   * │ - Dùng try-with-resources cho AutoCloseable                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CACHING IN JAVA
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Các loại Reference trong Java (cho caching)?                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌──────────────────┬───────────────────────────────────────────────────────┐│
   * │ │ Reference Type   │ GC behavior                                           ││
   * │ ├──────────────────┼───────────────────────────────────────────────────────┤│
   * │ │ Strong           │ KHÔNG bao giờ GC nếu còn reachable                    ││
   * │ │ Object obj = new │ Bình thường, khi obj = null mới GC                    ││
   * │ ├──────────────────┼───────────────────────────────────────────────────────┤│
   * │ │ WeakReference    │ GC BẤT CỨ LÚC NÀO (next GC cycle)                     ││
   * │ │                  │ Dù memory vẫn đủ                                      ││
   * │ │                  │ Use: WeakHashMap, ThreadLocal cleanup                 ││
   * │ ├──────────────────┼───────────────────────────────────────────────────────┤│
   * │ │ SoftReference    │ GC KHI SẮP OOM (memory pressure)                      ││
   * │ │                  │ Giữ lâu hơn Weak nếu memory đủ                        ││
   * │ │                  │ Use: Memory-sensitive caches                          ││
   * │ ├──────────────────┼───────────────────────────────────────────────────────┤│
   * │ │ PhantomReference │ KHÔNG access được object                              ││
   * │ │                  │ Chỉ biết object đã được GC                            ││
   * │ │                  │ Use: Cleanup resources, finalization replacement      ││
   * │ └──────────────────┴───────────────────────────────────────────────────────┘│
   * │                                                                             │
   * │ EXAMPLE:                                                                    │
   * │   // Strong                                                                 │
   * │   Object obj = new Object();                                                │
   * │                                                                             │
   * │   // Weak - sẽ bị GC sớm                                                    │
   * │   WeakReference<Object> weak = new WeakReference<>(obj);                    │
   * │   obj = null;  // Không còn strong reference                                │
   * │   // weak.get() có thể trả về null bất cứ lúc nào                           │
   * │                                                                             │
   * │   // Soft - giữ lâu hơn, GC khi memory thấp                                 │
   * │   SoftReference<byte[]> cache = new SoftReference<>(new byte[1024*1024]);   │
   * │   // cache.get() trả về null khi memory pressure                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: WeakHashMap là gì? Khi nào dùng?                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ DEFINITION: Map với KEYS là WeakReferences                                  │
   * │ → Entry tự động bị remove khi KEY không còn strong reference                │
   * │                                                                             │
   * │ EXAMPLE:                                                                    │
   * │   WeakHashMap<Object, String> cache = new WeakHashMap<>();                  │
   * │   Object key = new Object();                                                │
   * │   cache.put(key, "value");                                                  │
   * │                                                                             │
   * │   cache.get(key);  // "value"                                               │
   * │   key = null;      // Key không còn strong reference                        │
   * │   System.gc();     // Trigger GC                                            │
   * │   cache.size();    // 0! Entry đã bị remove                                 │
   * │                                                                             │
   * │ USE CASES:                                                                  │
   * │ - Metadata cache: attach data đến objects mà không giữ object alive         │
   * │ - Listener registry: auto-cleanup khi object bị GC                          │
   * │ - ClassLoader-scoped caches                                                 │
   * │                                                                             │
   * │ ⚠️ CAUTION với String keys:                                                 │
   * │   cache.put("literal", "value");  // "literal" ở String pool → KHÔNG GC!    │
   * │   cache.put(new String("key"), "value");  // OK, có thể GC                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Cách implement LRU Cache trong Java?                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. LINKEDHASHMAP (Simplest)                                                 │
   * │ ───────────────────────────                                                 │
   * │   class LRUCache<K, V> extends LinkedHashMap<K, V> {                        │
   * │       private final int capacity;                                           │
   * │                                                                             │
   * │       public LRUCache(int capacity) {                                       │
   * │           super(capacity, 0.75f, true);  // accessOrder = true!             │
   * │           this.capacity = capacity;                                         │
   * │       }                                                                     │
   * │                                                                             │
   * │       @Override                                                             │
   * │       protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {         │
   * │           return size() > capacity;  // Remove oldest khi vượt capacity     │
   * │       }                                                                     │
   * │   }                                                                         │
   * │                                                                             │
   * │ 2. HASHMAP + DOUBLY LINKED LIST (LeetCode style)                            │
   * │ ───────────────────────────────────────────────────                         │
   * │   class LRUCache {                                                          │
   * │       Map<Integer, Node> map = new HashMap<>();                             │
   * │       Node head, tail;  // Doubly linked list                               │
   * │       int capacity;                                                         │
   * │                                                                             │
   * │       public int get(int key) {                                             │
   * │           Node node = map.get(key);                                         │
   * │           if (node == null) return -1;                                      │
   * │           moveToHead(node);  // Mark as recently used                       │
   * │           return node.value;                                                │
   * │       }                                                                     │
   * │                                                                             │
   * │       public void put(int key, int value) {                                 │
   * │           if (map.containsKey(key)) {                                       │
   * │               Node node = map.get(key);                                     │
   * │               node.value = value;                                           │
   * │               moveToHead(node);                                             │
   * │           } else {                                                          │
   * │               Node node = new Node(key, value);                             │
   * │               map.put(key, node);                                           │
   * │               addToHead(node);                                              │
   * │               if (map.size() > capacity) {                                  │
   * │                   Node removed = removeTail();  // Evict LRU                │
   * │                   map.remove(removed.key);                                  │
   * │               }                                                             │
   * │           }                                                                 │
   * │       }                                                                     │
   * │   }                                                                         │
   * │                                                                             │
   * │ Complexity: O(1) cho cả get và put                                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Guava Cache vs Caffeine?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. GUAVA CACHE                                                              │
   * │ ──────────────                                                              │
   * │   LoadingCache<Key, Value> cache = CacheBuilder.newBuilder()                │
   * │       .maximumSize(1000)                     // Max entries                 │
   * │       .expireAfterWrite(10, TimeUnit.MINUTES)// TTL                         │
   * │       .expireAfterAccess(5, TimeUnit.MINUTES)// Idle timeout                │
   * │       .concurrencyLevel(4)                   // Concurrent writes           │
   * │       .recordStats()                         // Enable statistics           │
   * │       .build(new CacheLoader<Key, Value>() {                                │
   * │           public Value load(Key key) {                                      │
   * │               return loadFromDB(key);        // Auto-load on miss           │
   * │           }                                                                 │
   * │       });                                                                   │
   * │                                                                             │
   * │   Value v = cache.get(key);        // Auto-load nếu miss                    │
   * │   cache.put(key, value);           // Manual put                            │
   * │   cache.invalidate(key);           // Remove                                │
   * │   cache.stats();                   // Hit rate, load time, etc.             │
   * │                                                                             │
   * │ 2. CAFFEINE (Java 8+, faster than Guava)                                    │
   * │ ─────────────────────────────────────────                                   │
   * │   LoadingCache<Key, Value> cache = Caffeine.newBuilder()                    │
   * │       .maximumSize(10_000)                                                  │
   * │       .expireAfterWrite(Duration.ofMinutes(5))                              │
   * │       .refreshAfterWrite(Duration.ofMinutes(1)) // Async refresh            │
   * │       .recordStats()                                                        │
   * │       .build(key -> loadFromDB(key));                                       │
   * │                                                                             │
   * │ ┌────────────────┬─────────────────────────┬────────────────────────────┐   │
   * │ │                │ Guava Cache             │ Caffeine                   │   │
   * │ ├────────────────┼─────────────────────────┼────────────────────────────┤   │
   * │ │ Performance    │ Good                    │ Better (W-TinyLFU)         │   │
   * │ │ Algorithm      │ LRU                     │ W-TinyLFU (frequency+age)  │   │
   * │ │ Async          │ Limited                 │ Full support               │   │
   * │ │ Java version   │ 6+                      │ 8+                         │   │
   * │ │ Maintenance    │ Maintenance mode        │ Active                     │   │
   * │ └────────────────┴─────────────────────────┴────────────────────────────┘   │
   * │                                                                             │
   * │ RECOMMENDATION: Dùng Caffeine cho projects mới                              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Cache Patterns?                                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. CACHE-ASIDE (Lazy Loading)                                               │
   * │ ──────────────────────────────                                              │
   * │   Value get(Key key) {                                                      │
   * │       Value v = cache.get(key);                                             │
   * │       if (v == null) {                                                      │
   * │           v = database.get(key);    // Load from DB                         │
   * │           cache.put(key, v);        // Put vào cache                        │
   * │       }                                                                     │
   * │       return v;                                                             │
   * │   }                                                                         │
   * │                                                                             │
   * │   void update(Key key, Value value) {                                       │
   * │       database.update(key, value);                                          │
   * │       cache.invalidate(key);        // Invalidate, KHÔNG update!            │
   * │   }                                                                         │
   * │                                                                             │
   * │ ✓ Simple, application controls cache                                        │
   * │ ✗ Cache miss → slow first request                                           │
   * │                                                                             │
   * │ 2. READ-THROUGH                                                             │
   * │ ─────────────────                                                           │
   * │   Cache tự động load từ DB khi miss                                         │
   * │   → Dùng LoadingCache (Guava/Caffeine)                                      │
   * │                                                                             │
   * │ 3. WRITE-THROUGH                                                            │
   * │ ──────────────────                                                          │
   * │   Write vào cache → Cache sync write vào DB                                 │
   * │   ✓ Data consistency                                                        │
   * │   ✗ Write latency cao                                                       │
   * │                                                                             │
   * │ 4. WRITE-BEHIND (Write-Back)                                                │
   * │ ─────────────────────────────                                               │
   * │   Write vào cache → Async write vào DB (batch/delay)                        │
   * │   ✓ Fast write                                                              │
   * │   ✗ Risk data loss nếu crash                                                │
   * │                                                                             │
   * │ 5. REFRESH-AHEAD                                                            │
   * │ ──────────────────                                                          │
   * │   Proactively refresh cache trước khi expire                                │
   * │   → Caffeine refreshAfterWrite()                                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Cache Eviction Policies?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌────────────────┬─────────────────────────────────────────────────────────┐│
   * │ │ Policy         │ Description                                            ││
   * │ ├────────────────┼─────────────────────────────────────────────────────────┤│
   * │ │ LRU            │ Least Recently Used - evict oldest accessed             ││
   * │ │                │ Simple, nhưng không tính frequency                      ││
   * │ ├────────────────┼─────────────────────────────────────────────────────────┤│
   * │ │ LFU            │ Least Frequently Used - evict ít access nhất            ││
   * │ │                │ Tốt cho hot data, nhưng slow adapt                      ││
   * │ ├────────────────┼─────────────────────────────────────────────────────────┤│
   * │ │ FIFO           │ First In First Out - evict oldest entry                 ││
   * │ │                │ Simple, nhưng không tốt cho temporal locality           ││
   * │ ├────────────────┼─────────────────────────────────────────────────────────┤│
   * │ │ TTL            │ Time To Live - evict sau X time                         ││
   * │ │                │ Dùng cho data có expiry                                 ││
   * │ ├────────────────┼─────────────────────────────────────────────────────────┤│
   * │ │ W-TinyLFU      │ Window TinyLFU (Caffeine)                               ││
   * │ │                │ Hybrid: frequency + recency, best overall               ││
   * │ └────────────────┴─────────────────────────────────────────────────────────┘│
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Thread-safe caching?                                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ❌ KHÔNG THREAD-SAFE:                                                       │
   * │   HashMap<K, V> cache = new HashMap<>();                                    │
   * │   if (!cache.containsKey(key)) {  // Race condition!                        │
   * │       cache.put(key, loadValue()); // Có thể load nhiều lần                 │
   * │   }                                                                         │
   * │                                                                             │
   * │ ✓ FIX 1: ConcurrentHashMap.computeIfAbsent                                  │
   * │   ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();                │
   * │   V value = cache.computeIfAbsent(key, k -> loadValue());                   │
   * │                                                                             │
   * │ ✓ FIX 2: Guava/Caffeine LoadingCache                                        │
   * │   // Thread-safe by design, chỉ 1 thread load mỗi key                       │
   * │   cache.get(key);  // Block nếu đang load                                   │
   * │                                                                             │
   * │ ✓ FIX 3: Double-checked locking (cho lazy singleton cache)                  │
   * │   private volatile Map<K, V> cache;                                         │
   * │                                                                             │
   * │   Map<K, V> getCache() {                                                    │
   * │       if (cache == null) {                                                  │
   * │           synchronized (this) {                                             │
   * │               if (cache == null) {                                          │
   * │                   cache = loadCache();                                      │
   * │               }                                                             │
   * │           }                                                                 │
   * │       }                                                                     │
   * │       return cache;                                                         │
   * │   }                                                                         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ COMMON CACHE PROBLEMS                                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. CACHE STAMPEDE (Thundering Herd)                                         │
   * │    Cache expire → 1000 requests hit DB cùng lúc                             │
   * │    FIX: Lock, probabilistic early expiry, background refresh                │
   * │                                                                             │
   * │ 2. CACHE PENETRATION                                                        │
   * │    Query key không tồn tại → luôn miss, hit DB                              │
   * │    FIX: Cache null/empty values, Bloom filter                               │
   * │                                                                             │
   * │ 3. CACHE BREAKDOWN                                                          │
   * │    Hot key expire → massive DB load                                         │
   * │    FIX: Never expire hot keys, mutex lock on load                           │
   * │                                                                             │
   * │ 4. CACHE INCONSISTENCY                                                      │
   * │    Data thay đổi nhưng cache còn stale                                      │
   * │    FIX: Short TTL, invalidation, versioning                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SORTING IN JAVA
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Sorting Algorithms Comparison                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌────────────────┬─────────┬─────────┬─────────┬───────────┬──────────────┐ │
   * │ │ Algorithm      │ Best    │ Average │ Worst   │ Space     │ Stable?      │ │
   * │ ├────────────────┼─────────┼─────────┼─────────┼───────────┼──────────────┤ │
   * │ │ Bubble Sort    │ O(n)    │ O(n²)   │ O(n²)   │ O(1)      │ ✓ Yes        │ │
   * │ │ Selection Sort │ O(n²)   │ O(n²)   │ O(n²)   │ O(1)      │ ✗ No         │ │
   * │ │ Insertion Sort │ O(n)    │ O(n²)   │ O(n²)   │ O(1)      │ ✓ Yes        │ │
   * │ │ Merge Sort     │ O(nlogn)│ O(nlogn)│ O(nlogn)│ O(n)      │ ✓ Yes        │ │
   * │ │ Quick Sort     │ O(nlogn)│ O(nlogn)│ O(n²)   │ O(logn)   │ ✗ No         │ │
   * │ │ Heap Sort      │ O(nlogn)│ O(nlogn)│ O(nlogn)│ O(1)      │ ✗ No         │ │
   * │ │ Tim Sort       │ O(n)    │ O(nlogn)│ O(nlogn)│ O(n)      │ ✓ Yes        │ │
   * │ │ Counting Sort  │ O(n+k)  │ O(n+k)  │ O(n+k)  │ O(k)      │ ✓ Yes        │ │
   * │ │ Radix Sort     │ O(nk)   │ O(nk)   │ O(nk)   │ O(n+k)    │ ✓ Yes        │ │
   * │ │ Bucket Sort    │ O(n+k)  │ O(n+k)  │ O(n²)   │ O(n)      │ ✓ Yes        │ │
   * │ └────────────────┴─────────┴─────────┴─────────┴───────────┴──────────────┘ │
   * │                                                                             │
   * │ STABLE = Giữ nguyên thứ tự của các phần tử bằng nhau                        │
   * │                                                                             │
   * │ KHI NÀO DÙNG:                                                               │
   * │ - Insertion Sort: Small arrays, nearly sorted                               │
   * │ - Merge Sort: Linked lists, stable required, external sort                  │
   * │ - Quick Sort: General purpose, in-place (Java Arrays.sort primitives)       │
   * │ - Tim Sort: Real-world data (Java Arrays.sort objects)                      │
   * │ - Counting/Radix: Known range integers                                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Java built-in sorting?                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. ARRAYS.SORT (cho arrays)                                                 │
   * │ ───────────────────────────                                                 │
   * │   int[] arr = {5, 2, 8, 1};                                                 │
   * │   Arrays.sort(arr);                    // [1, 2, 5, 8]                      │
   * │   Arrays.sort(arr, 1, 3);              // Sort range [1, 3)                 │
   * │                                                                             │
   * │   // Descending (primitives - phải box)                                     │
   * │   Integer[] arr2 = {5, 2, 8, 1};                                            │
   * │   Arrays.sort(arr2, Collections.reverseOrder());                            │
   * │                                                                             │
   * │   // Custom comparator                                                      │
   * │   Arrays.sort(arr2, (a, b) -> b - a);  // Descending                        │
   * │                                                                             │
   * │ 2. COLLECTIONS.SORT (cho Lists)                                             │
   * │ ─────────────────────────────────                                           │
   * │   List<Integer> list = Arrays.asList(5, 2, 8, 1);                           │
   * │   Collections.sort(list);              // [1, 2, 5, 8]                      │
   * │   Collections.sort(list, Collections.reverseOrder());                       │
   * │                                                                             │
   * │   // Hoặc dùng List.sort() (Java 8+)                                        │
   * │   list.sort(Comparator.naturalOrder());                                     │
   * │   list.sort(Comparator.reverseOrder());                                     │
   * │                                                                             │
   * │ 3. STREAM.SORTED                                                            │
   * │ ──────────────────                                                          │
   * │   list.stream()                                                             │
   * │       .sorted()                        // Natural order                     │
   * │       .sorted(Comparator.reverseOrder())                                    │
   * │       .collect(Collectors.toList());                                        │
   * │                                                                             │
   * │ ALGORITHM USED:                                                             │
   * │ - Primitives: Dual-Pivot Quicksort (unstable)                               │
   * │ - Objects: TimSort (stable)                                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Comparable vs Comparator?                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌──────────────────┬─────────────────────────┬─────────────────────────────┐│
   * │ │                  │ Comparable              │ Comparator                  ││
   * │ ├──────────────────┼─────────────────────────┼─────────────────────────────┤│
   * │ │ Package          │ java.lang               │ java.util                   ││
   * │ │ Method           │ compareTo(T o)          │ compare(T o1, T o2)         ││
   * │ │ Usage            │ Natural ordering        │ Custom ordering             ││
   * │ │ Implementation   │ Trong class             │ Bên ngoài class             ││
   * │ │ Số orderings     │ 1 (natural)             │ Nhiều                       ││
   * │ └──────────────────┴─────────────────────────┴─────────────────────────────┘│
   * │                                                                             │
   * │ COMPARABLE:                                                                 │
   * │   class Person implements Comparable<Person> {                              │
   * │       String name;                                                          │
   * │       int age;                                                              │
   * │                                                                             │
   * │       @Override                                                             │
   * │       public int compareTo(Person other) {                                  │
   * │           return this.name.compareTo(other.name);  // Natural: by name      │
   * │       }                                                                     │
   * │   }                                                                         │
   * │                                                                             │
   * │   Collections.sort(personList);  // Dùng natural ordering                   │
   * │                                                                             │
   * │ COMPARATOR:                                                                 │
   * │   // Sort by age                                                            │
   * │   Comparator<Person> byAge = (p1, p2) -> p1.age - p2.age;                   │
   * │   Collections.sort(personList, byAge);                                      │
   * │                                                                             │
   * │   // Hoặc dùng method reference                                             │
   * │   Comparator<Person> byAge2 = Comparator.comparingInt(p -> p.age);          │
   * │                                                                             │
   * │ RETURN VALUE:                                                               │
   * │ - Negative: this < other (hoặc o1 < o2)                                     │
   * │ - Zero: this == other                                                       │
   * │ - Positive: this > other                                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Comparator chaining (Java 8+)?                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // Sort by multiple fields                                                  │
   * │ Comparator<Person> comparator = Comparator                                  │
   * │     .comparing(Person::getLastName)         // Primary: lastName            │
   * │     .thenComparing(Person::getFirstName)    // Secondary: firstName         │
   * │     .thenComparingInt(Person::getAge);      // Tertiary: age                │
   * │                                                                             │
   * │ // Null handling                                                            │
   * │ Comparator<Person> nullSafe = Comparator                                    │
   * │     .comparing(Person::getName, Comparator.nullsFirst(String::compareTo));  │
   * │                                                                             │
   * │ // Reverse                                                                  │
   * │ Comparator<Person> descAge = Comparator                                     │
   * │     .comparingInt(Person::getAge)                                           │
   * │     .reversed();                                                            │
   * │                                                                             │
   * │ COMMON PATTERNS:                                                            │
   * │   Comparator.comparing(Function)           // Extract key to compare        │
   * │   Comparator.comparingInt(ToIntFunction)   // Avoid boxing                  │
   * │   Comparator.comparingLong(ToLongFunction)                                  │
   * │   Comparator.comparingDouble(ToDoubleFunction)                              │
   * │   .thenComparing(...)                      // Chain                         │
   * │   .reversed()                              // Reverse order                 │
   * │   .nullsFirst(comparator)                  // Nulls first                   │
   * │   .nullsLast(comparator)                   // Nulls last                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ COMMON SORTING PATTERNS (LeetCode)                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. SORT 2D ARRAY                                                            │
   * │ ──────────────────                                                          │
   * │   int[][] intervals = {{1,3}, {2,6}, {8,10}};                               │
   * │                                                                             │
   * │   // By first element                                                       │
   * │   Arrays.sort(intervals, (a, b) -> a[0] - b[0]);                            │
   * │                                                                             │
   * │   // By first, then second                                                  │
   * │   Arrays.sort(intervals, (a, b) -> {                                        │
   * │       if (a[0] != b[0]) return a[0] - b[0];                                 │
   * │       return a[1] - b[1];                                                   │
   * │   });                                                                       │
   * │                                                                             │
   * │   // Dùng Comparator.comparing                                              │
   * │   Arrays.sort(intervals, Comparator                                         │
   * │       .comparingInt((int[] a) -> a[0])                                      │
   * │       .thenComparingInt(a -> a[1]));                                        │
   * │                                                                             │
   * │ 2. SORT MAP BY VALUE                                                        │
   * │ ─────────────────────                                                       │
   * │   Map<String, Integer> map = new HashMap<>();                               │
   * │                                                                             │
   * │   // Sort by value descending                                               │
   * │   List<Map.Entry<String, Integer>> sorted = map.entrySet().stream()         │
   * │       .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())     │
   * │       .collect(Collectors.toList());                                        │
   * │                                                                             │
   * │   // Top K frequent                                                         │
   * │   List<String> topK = map.entrySet().stream()                               │
   * │       .sorted((a, b) -> b.getValue() - a.getValue())                        │
   * │       .limit(k)                                                             │
   * │       .map(Map.Entry::getKey)                                               │
   * │       .collect(Collectors.toList());                                        │
   * │                                                                             │
   * │ 3. CUSTOM SORT ORDER                                                        │
   * │ ─────────────────────                                                       │
   * │   // Sort strings by custom order                                           │
   * │   String order = "cba";  // c < b < a                                       │
   * │   Map<Character, Integer> orderMap = new HashMap<>();                       │
   * │   for (int i = 0; i < order.length(); i++) {                                │
   * │       orderMap.put(order.charAt(i), i);                                     │
   * │   }                                                                         │
   * │   Arrays.sort(arr, (s1, s2) ->                                              │
   * │       orderMap.get(s1.charAt(0)) - orderMap.get(s2.charAt(0)));             │
   * │                                                                             │
   * │ 4. PARTIAL SORT (Top K)                                                     │
   * │ ────────────────────────                                                    │
   * │   // Dùng PriorityQueue thay vì full sort                                   │
   * │   PriorityQueue<Integer> minHeap = new PriorityQueue<>();                   │
   * │   for (int num : arr) {                                                     │
   * │       minHeap.offer(num);                                                   │
   * │       if (minHeap.size() > k) minHeap.poll();                               │
   * │   }                                                                         │
   * │   // minHeap chứa k largest elements                                        │
   * │   // Time: O(n log k) thay vì O(n log n)                                    │
   * │                                                                             │
   * │ 5. SORT BY FREQUENCY                                                        │
   * │ ─────────────────────                                                       │
   * │   Map<Integer, Integer> freq = new HashMap<>();                             │
   * │   for (int num : nums) {                                                    │
   * │       freq.merge(num, 1, Integer::sum);                                     │
   * │   }                                                                         │
   * │   Arrays.sort(boxedNums, (a, b) -> {                                        │
   * │       if (!freq.get(a).equals(freq.get(b))) {                               │
   * │           return freq.get(a) - freq.get(b);  // By freq ascending           │
   * │       }                                                                     │
   * │       return b - a;  // By value descending                                 │
   * │   });                                                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ ⚠️ COMMON MISTAKES                                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. INTEGER OVERFLOW trong comparator                                        │
   * │ ─────────────────────────────────────                                       │
   * │ ❌ SAI:                                                                     │
   * │   Arrays.sort(arr, (a, b) -> a - b);  // Overflow nếu a hoặc b quá lớn!     │
   * │                                                                             │
   * │   int a = Integer.MAX_VALUE;                                                │
   * │   int b = -1;                                                               │
   * │   a - b = overflow!  // Âm thay vì dương                                    │
   * │                                                                             │
   * │ ✓ ĐÚNG:                                                                     │
   * │   Arrays.sort(arr, (a, b) -> Integer.compare(a, b));                        │
   * │   Arrays.sort(arr, Comparator.naturalOrder());                              │
   * │                                                                             │
   * │ 2. SORT PRIMITIVES descending                                               │
   * │ ──────────────────────────────                                              │
   * │ ❌ KHÔNG WORK:                                                              │
   * │   int[] arr = {5, 2, 8};                                                    │
   * │   Arrays.sort(arr, Collections.reverseOrder());  // Compile error!          │
   * │                                                                             │
   * │ ✓ FIX 1: Box to Integer[]                                                   │
   * │   Integer[] boxed = Arrays.stream(arr).boxed().toArray(Integer[]::new);     │
   * │   Arrays.sort(boxed, Collections.reverseOrder());                           │
   * │                                                                             │
   * │ ✓ FIX 2: Sort ascending then reverse                                        │
   * │   Arrays.sort(arr);                                                         │
   * │   for (int i = 0, j = arr.length - 1; i < j; i++, j--) {                    │
   * │       int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;                    │
   * │   }                                                                         │
   * │                                                                             │
   * │ 3. COMPARATOR không consistent                                              │
   * │ ────────────────────────────────                                            │
   * │ ❌ SAI:                                                                     │
   * │   // Nếu a == b, phải return 0!                                             │
   * │   Comparator<Integer> bad = (a, b) -> a < b ? -1 : 1;  // Không có 0!       │
   * │                                                                             │
   * │ ✓ ĐÚNG:                                                                     │
   * │   Comparator<Integer> good = (a, b) -> a < b ? -1 : (a > b ? 1 : 0);        │
   * │   Comparator<Integer> better = Integer::compare;                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // JAVA CONCURRENCY & MULTITHREADING
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ⚠️ Nội dung này đã được tách ra file riêng để nghiên cứu sâu hơn:
   * @see JavaConcurrencyGuide
   *
   * Topics covered:
   * - Thread Creation & Lifecycle
   * - ExecutorService & Thread Pools (5 types)
   * - ThreadPoolExecutor (production config)
   * - Future & CompletableFuture (chaining, combining, error handling)
   * - Async in HTTP REST API Handlers (5 patterns)
   * - Synchronization & Locks (synchronized, ReentrantLock, ReadWriteLock)
   * - Concurrent Collections (ConcurrentHashMap, BlockingQueue)
   * - Atomic Classes (AtomicInteger, LongAdder)
   * - ThreadLocal (per-thread storage, memory leak warning)
   * - Synchronization Utilities (CountDownLatch, CyclicBarrier, Semaphore)
   * - Common Concurrency Bugs & Fixes
   */
}
