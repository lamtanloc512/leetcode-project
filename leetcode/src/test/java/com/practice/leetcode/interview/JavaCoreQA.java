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
   * │ │ newCachedThreadPool()      │ Tạo thread mới khi cần, reuse nếu có       ││
   * │ │                            │ Dùng cho short-lived async tasks            ││
   * │ ├────────────────────────────┼─────────────────────────────────────────────┤│
   * │ │ newSingleThreadExecutor()  │ 1 thread, task thực hiện tuần tự            ││
   * │ │                            │ Dùng khi cần order guarantee                ││
   * │ ├────────────────────────────┼─────────────────────────────────────────────┤│
   * │ │ newScheduledThreadPool(n)  │ Scheduled/periodic tasks                   ││
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
   * │   String s = new String(); // s (reference) ở Stack, object ở Heap         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Garbage Collection hoạt động như nào?                                    │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ HEAP STRUCTURE:                                                             │
   * │ ┌─────────────────────────────────────────────────────────────────────────┐ │
   * │ │      Young Generation        │         Old Generation                  │ │
   * │ │ ┌───────────┬────────────────┤                                         │ │
   * │ │ │   Eden    │  Survivor (S0, S1)                                       │ │
   * │ │ └───────────┴────────────────┴─────────────────────────────────────────┤ │
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
}
