package com.practice.leetcode.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * ====================================================================
 * JAVA INTERVIEW DEEP DIVE - PRACTICAL CODE EXAMPLES
 * ====================================================================
 * 
 * Comprehensive code examples demonstrating core Java concepts
 * commonly asked in technical interviews.
 * 
 * @author Interview Preparation Guide
 */
public class JavaInterviewDeepDive {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Java Interview Deep Dive ===\n");
        
        // Uncomment to run specific demos
        // HashMapDemo.run();
        // EqualsHashCodeDemo.run();
        // PassByValueDemo.run();
        // ThreadingDemo.run();
        // ThreadSafeCollectionsDemo.run();
        // CompletableFutureDemo.run();
        // StreamParallelDemo.run();
        // ReentrantLockDemo.run();
    }

    // ================================================================
    // 1. HASHMAP INTERNALS
    // ================================================================
    
    /**
     * Demonstrates how HashMap works internally.
     * 
     * Key concepts:
     * - Hash calculation: hash = key.hashCode() ^ (key.hashCode() >>> 16)
     * - Index calculation: index = hash & (n - 1), where n = table.length
     * - Collision handling: LinkedList (size < 8) → Red-Black Tree (size >= 8)
     * - Load factor: 0.75 (default) - triggers resize when exceeded
     * - Resize: Doubles capacity, rehashes all entries
     */
    static class HashMapDemo {
        
        public static void run() {
            System.out.println("=== HashMap Internals Demo ===\n");
            
            // 1. Default HashMap behavior
            Map<String, Integer> map = new HashMap<>();
            
            // Initial capacity: 16, Load factor: 0.75
            // Threshold = 16 * 0.75 = 12
            // When size > 12, resize to 32
            
            for (int i = 0; i < 20; i++) {
                map.put("key" + i, i);
            }
            
            System.out.println("Map size: " + map.size());
            
            // 2. Custom initial capacity for known size
            // Avoid resizing by setting capacity = expectedSize / 0.75 + 1
            int expectedSize = 1000000;
            Map<String, Integer> optimizedMap = new HashMap<>(
                (int) (expectedSize / 0.75) + 1
            );
            
            // 3. Demonstrate hash collision
            demonstrateCollision();
            
            // 4. Custom key object - importance of equals/hashCode
            demonstrateCustomKey();
        }
        
        private static void demonstrateCollision() {
            System.out.println("\n--- Hash Collision Demo ---");
            
            // These strings have the same hashCode in Java
            String s1 = "Aa";
            String s2 = "BB";
            
            System.out.println("'Aa'.hashCode() = " + s1.hashCode());
            System.out.println("'BB'.hashCode() = " + s2.hashCode());
            System.out.println("Same hashCode: " + (s1.hashCode() == s2.hashCode()));
            
            Map<String, String> map = new HashMap<>();
            map.put(s1, "value1");
            map.put(s2, "value2");
            
            // Both stored in same bucket but as separate entries
            System.out.println("map.get('Aa') = " + map.get(s1));
            System.out.println("map.get('BB') = " + map.get(s2));
        }
        
        private static void demonstrateCustomKey() {
            System.out.println("\n--- Custom Key Demo ---");
            
            Map<Employee, String> employeeMap = new HashMap<>();
            
            Employee emp1 = new Employee(1L, "John");
            Employee emp2 = new Employee(1L, "John Updated"); // Same ID
            
            employeeMap.put(emp1, "Department A");
            
            // Can we find with a "different" object that's logically equal?
            System.out.println("Find emp2 (same ID): " + employeeMap.get(emp2));
            
            // Works because Employee implements equals/hashCode based on ID
        }
    }
    
    /**
     * Employee class demonstrating proper equals() and hashCode() implementation.
     * 
     * CONTRACT:
     * 1. If a.equals(b) == true, then a.hashCode() == b.hashCode() (REQUIRED)
     * 2. If a.hashCode() == b.hashCode(), a.equals(b) can be true or false
     * 3. equals() must be reflexive, symmetric, transitive, consistent
     */
    static class Employee {
        private final Long id;          // Business key
        private String name;            // Not used in equals/hashCode
        private String department;      // Not used in equals/hashCode
        
        public Employee(Long id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public Long getId() { return id; }
        public String getName() { return name; }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Employee employee = (Employee) o;
            return Objects.equals(id, employee.id); // Only compare business key
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(id); // MUST use same fields as equals()
        }
        
        @Override
        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "'}";
        }
    }

    // ================================================================
    // 2. EQUALS VS HASHCODE
    // ================================================================
    
    static class EqualsHashCodeDemo {
        
        public static void run() {
            System.out.println("\n=== equals() vs hashCode() Demo ===\n");
            
            // Demonstrate the contract
            demonstrateContract();
            
            // Demonstrate what happens when you violate the contract
            demonstrateViolation();
        }
        
        private static void demonstrateContract() {
            System.out.println("--- Correct Implementation ---");
            
            Employee emp1 = new Employee(1L, "John");
            Employee emp2 = new Employee(1L, "John");
            Employee emp3 = new Employee(2L, "Jane");
            
            // Reflexive: a.equals(a) == true
            System.out.println("Reflexive: emp1.equals(emp1) = " + emp1.equals(emp1));
            
            // Symmetric: a.equals(b) == b.equals(a)
            System.out.println("Symmetric: emp1.equals(emp2) = " + emp1.equals(emp2));
            System.out.println("Symmetric: emp2.equals(emp1) = " + emp2.equals(emp1));
            
            // Contract: equals true → hashCode must be same
            System.out.println("emp1.hashCode() = " + emp1.hashCode());
            System.out.println("emp2.hashCode() = " + emp2.hashCode());
            System.out.println("HashCodes equal: " + (emp1.hashCode() == emp2.hashCode()));
            
            // Different objects, different hashCodes (typically)
            System.out.println("emp3.hashCode() = " + emp3.hashCode());
        }
        
        private static void demonstrateViolation() {
            System.out.println("\n--- What happens with BAD implementation ---");
            
            // Bad class - overrides equals but not hashCode
            class BadKey {
                private final int value;
                
                BadKey(int value) {
                    this.value = value;
                }
                
                @Override
                public boolean equals(Object o) {
                    if (this == o) return true;
                    if (o == null || getClass() != o.getClass()) return false;
                    BadKey badKey = (BadKey) o;
                    return value == badKey.value;
                }
                
                // Missing hashCode override! Uses Object.hashCode() - memory address
            }
            
            Map<BadKey, String> map = new HashMap<>();
            BadKey key1 = new BadKey(1);
            map.put(key1, "value1");
            
            BadKey key2 = new BadKey(1); // Logically equal
            System.out.println("key1.equals(key2) = " + key1.equals(key2));
            System.out.println("key1.hashCode() = " + key1.hashCode());
            System.out.println("key2.hashCode() = " + key2.hashCode());
            
            // BUG: Cannot find the value!
            System.out.println("map.get(key2) = " + map.get(key2)); // null!
            System.out.println("Object 'lost' in HashMap because hashCode different!");
        }
    }

    // ================================================================
    // 3. PASS BY VALUE VS REFERENCE
    // ================================================================
    
    /**
     * Java is ALWAYS pass-by-value!
     * 
     * For primitives: The actual value is copied
     * For objects: The reference (memory address) VALUE is copied
     * 
     * This means:
     * - You can modify the object's internal state
     * - You CANNOT make the original reference point to a different object
     */
    static class PassByValueDemo {
        
        public static void run() {
            System.out.println("\n=== Pass by Value Demo ===\n");
            
            // Primitive: actual value copied
            int x = 10;
            modifyPrimitive(x);
            System.out.println("After modifyPrimitive: x = " + x); // Still 10
            
            // Object: reference value copied
            StringBuilder sb = new StringBuilder("Hello");
            modifyObject(sb);
            System.out.println("After modifyObject: sb = " + sb); // "Hello World"
            
            // Reassigning reference doesn't affect original
            replaceObject(sb);
            System.out.println("After replaceObject: sb = " + sb); // Still "Hello World"
            
            // String immutability demo
            stringDemo();
            
            // StringBuilder mutability demo
            stringBuilderDemo();
        }
        
        private static void modifyPrimitive(int value) {
            value = 20; // Only modifies local copy
        }
        
        private static void modifyObject(StringBuilder sb) {
            sb.append(" World"); // Modifies the actual object
        }
        
        private static void replaceObject(StringBuilder sb) {
            sb = new StringBuilder("Replaced"); // Only changes local reference
        }
        
        private static void stringDemo() {
            System.out.println("\n--- String Immutability ---");
            
            String s1 = "Hello";
            String s2 = s1;
            
            s1 = s1 + " World"; // Creates NEW String object
            
            System.out.println("s1 = " + s1); // "Hello World"
            System.out.println("s2 = " + s2); // "Hello" - unchanged!
            
            // String Pool demonstration
            String s3 = "Hello";
            String s4 = "Hello";
            String s5 = new String("Hello");
            
            System.out.println("s3 == s4: " + (s3 == s4));        // true - same pool object
            System.out.println("s3 == s5: " + (s3 == s5));        // false - different objects
            System.out.println("s3.equals(s5): " + s3.equals(s5)); // true - same content
        }
        
        private static void stringBuilderDemo() {
            System.out.println("\n--- StringBuilder Performance ---");
            
            int iterations = 10000;
            
            // BAD: String concatenation in loop - O(n²)
            long start = System.currentTimeMillis();
            String result = "";
            for (int i = 0; i < iterations; i++) {
                result += i; // Creates new String each time!
            }
            long stringTime = System.currentTimeMillis() - start;
            
            // GOOD: StringBuilder - O(n)
            start = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < iterations; i++) {
                sb.append(i); // Modifies in place
            }
            String result2 = sb.toString();
            long sbTime = System.currentTimeMillis() - start;
            
            System.out.println("String concat time: " + stringTime + "ms");
            System.out.println("StringBuilder time: " + sbTime + "ms");
            System.out.println("StringBuilder is " + (stringTime / Math.max(1, sbTime)) + "x faster");
        }
    }

    // ================================================================
    // 4. MULTI-THREADING
    // ================================================================
    
    static class ThreadingDemo {
        
        public static void run() throws Exception {
            System.out.println("\n=== Multi-Threading Demo ===\n");
            
            // Basic thread creation
            basicThreadDemo();
            
            // Thread states
            threadStatesDemo();
            
            // Synchronization
            synchronizationDemo();
            
            // ThreadLocal
            threadLocalDemo();
        }
        
        private static void basicThreadDemo() throws InterruptedException, ExecutionException {
            System.out.println("--- Basic Thread Creation ---");
            
            // Method 1: Extend Thread
            Thread t1 = new Thread() {
                @Override
                public void run() {
                    System.out.println("Thread via extends Thread");
                }
            };
            
            // Method 2: Runnable
            Thread t2 = new Thread(() -> System.out.println("Thread via Runnable"));
            
            // Method 3: Callable with ExecutorService (returns value)
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(() -> "Result from Callable");
            
            t1.start();
            t2.start();
            System.out.println("Callable result: " + future.get());
            
            executor.shutdown();
        }
        
        private static void threadStatesDemo() throws InterruptedException {
            System.out.println("\n--- Thread States ---");
            
            Object lock = new Object();
            
            Thread thread = new Thread(() -> {
                try {
                    synchronized (lock) {
                        lock.wait(100); // WAITING -> TIMED_WAITING
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            System.out.println("Before start: " + thread.getState()); // NEW
            thread.start();
            System.out.println("After start: " + thread.getState());  // RUNNABLE
            
            Thread.sleep(50);
            System.out.println("During wait: " + thread.getState());  // TIMED_WAITING
            
            thread.join();
            System.out.println("After completion: " + thread.getState()); // TERMINATED
        }
        
        private static void synchronizationDemo() throws InterruptedException {
            System.out.println("\n--- Synchronization Demo ---");
            
            // Without synchronization - race condition
            Counter unsafeCounter = new Counter();
            CountDownLatch latch1 = new CountDownLatch(100);
            
            for (int i = 0; i < 100; i++) {
                new Thread(() -> {
                    for (int j = 0; j < 1000; j++) {
                        unsafeCounter.incrementUnsafe();
                    }
                    latch1.countDown();
                }).start();
            }
            latch1.await();
            System.out.println("Unsafe counter (expected 100000): " + unsafeCounter.getCount());
            
            // With synchronization - correct
            Counter safeCounter = new Counter();
            CountDownLatch latch2 = new CountDownLatch(100);
            
            for (int i = 0; i < 100; i++) {
                new Thread(() -> {
                    for (int j = 0; j < 1000; j++) {
                        safeCounter.incrementSafe();
                    }
                    latch2.countDown();
                }).start();
            }
            latch2.await();
            System.out.println("Safe counter (expected 100000): " + safeCounter.getCount());
        }
        
        static class Counter {
            private int count = 0;
            
            // NOT thread-safe: read-modify-write is not atomic
            public void incrementUnsafe() {
                count++; // count = count + 1 (3 operations!)
            }
            
            // Thread-safe: synchronized block
            public synchronized void incrementSafe() {
                count++;
            }
            
            public int getCount() { return count; }
        }
        
        private static void threadLocalDemo() {
            System.out.println("\n--- ThreadLocal Demo ---");
            
            // Each thread has its own copy
            ThreadLocal<String> userContext = new ThreadLocal<>();
            
            Thread t1 = new Thread(() -> {
                userContext.set("User-A");
                System.out.println("Thread 1: " + userContext.get());
                userContext.remove(); // Clean up to avoid memory leak!
            });
            
            Thread t2 = new Thread(() -> {
                userContext.set("User-B");
                System.out.println("Thread 2: " + userContext.get());
                userContext.remove();
            });
            
            t1.start();
            t2.start();
            
            // Main thread has no value
            System.out.println("Main thread: " + userContext.get()); // null
        }
    }

    // ================================================================
    // 5. THREAD-SAFE COLLECTIONS
    // ================================================================
    
    static class ThreadSafeCollectionsDemo {
        
        public static void run() throws Exception {
            System.out.println("\n=== Thread-Safe Collections Demo ===\n");
            
            concurrentHashMapDemo();
            copyOnWriteArrayListDemo();
        }
        
        private static void concurrentHashMapDemo() throws InterruptedException {
            System.out.println("--- ConcurrentHashMap Demo ---");
            
            // ConcurrentHashMap: segment/bucket-level locking
            ConcurrentHashMap<String, AtomicInteger> map = new ConcurrentHashMap<>();
            
            // Atomic operations
            map.computeIfAbsent("counter", k -> new AtomicInteger(0));
            
            CountDownLatch latch = new CountDownLatch(10);
            for (int i = 0; i < 10; i++) {
                new Thread(() -> {
                    for (int j = 0; j < 1000; j++) {
                        // Thread-safe increment
                        map.get("counter").incrementAndGet();
                    }
                    latch.countDown();
                }).start();
            }
            latch.await();
            
            System.out.println("Counter value (expected 10000): " + map.get("counter").get());
            
            // Bulk operations (weakly consistent)
            map.forEach((k, v) -> System.out.println(k + " = " + v));
        }
        
        private static void copyOnWriteArrayListDemo() {
            System.out.println("\n--- CopyOnWriteArrayList Demo ---");
            
            // CopyOnWriteArrayList: creates new array on every write
            // Best for: read-heavy, write-rarely scenarios
            CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
            
            list.add("item1");
            list.add("item2");
            
            // Safe iteration - won't throw ConcurrentModificationException
            for (String item : list) {
                list.add("item3"); // Modifies a copy
                System.out.println("Iterating: " + item);
            }
            
            System.out.println("Final list: " + list);
        }
    }

    // ================================================================
    // 6. REENTRANT LOCK
    // ================================================================
    
    static class ReentrantLockDemo {
        
        public static void run() throws Exception {
            System.out.println("\n=== ReentrantLock Demo ===\n");
            
            basicLockDemo();
            fairnessDemo();
            conditionDemo();
        }
        
        private static void basicLockDemo() {
            System.out.println("--- Basic ReentrantLock ---");
            
            ReentrantLock lock = new ReentrantLock();
            
            // Proper usage with try-finally
            lock.lock();
            try {
                System.out.println("Lock acquired, doing work...");
                // Critical section
            } finally {
                lock.unlock(); // ALWAYS unlock in finally!
            }
            
            // tryLock - non-blocking attempt
            if (lock.tryLock()) {
                try {
                    System.out.println("tryLock succeeded");
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("Could not acquire lock");
            }
            
            // tryLock with timeout
            try {
                if (lock.tryLock(1, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Acquired lock within timeout");
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        private static void fairnessDemo() {
            System.out.println("\n--- Fair vs Unfair Lock ---");
            
            // Unfair lock (default) - faster, but can cause starvation
            ReentrantLock unfairLock = new ReentrantLock(); // fair = false
            
            // Fair lock - guarantees FIFO ordering, slightly slower
            ReentrantLock fairLock = new ReentrantLock(true);
            
            System.out.println("Unfair lock fairness: " + unfairLock.isFair());
            System.out.println("Fair lock fairness: " + fairLock.isFair());
        }
        
        private static void conditionDemo() throws InterruptedException {
            System.out.println("\n--- Condition Variables ---");
            
            // Producer-Consumer with Condition
            ReentrantLock lock = new ReentrantLock();
            Condition notEmpty = lock.newCondition();
            Condition notFull = lock.newCondition();
            
            Queue<Integer> queue = new LinkedList<>();
            int capacity = 5;
            
            // Producer
            Runnable producer = () -> {
                for (int i = 0; i < 10; i++) {
                    lock.lock();
                    try {
                        while (queue.size() == capacity) {
                            notFull.await(); // Wait until not full
                        }
                        queue.offer(i);
                        System.out.println("Produced: " + i);
                        notEmpty.signal(); // Signal consumer
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        lock.unlock();
                    }
                }
            };
            
            // Consumer
            Runnable consumer = () -> {
                for (int i = 0; i < 10; i++) {
                    lock.lock();
                    try {
                        while (queue.isEmpty()) {
                            notEmpty.await(); // Wait until not empty
                        }
                        int value = queue.poll();
                        System.out.println("Consumed: " + value);
                        notFull.signal(); // Signal producer
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        lock.unlock();
                    }
                }
            };
            
            Thread producerThread = new Thread(producer);
            Thread consumerThread = new Thread(consumer);
            
            producerThread.start();
            consumerThread.start();
            
            producerThread.join();
            consumerThread.join();
        }
    }

    // ================================================================
    // 7. VIRTUAL THREADS (Java 21+)
    // ================================================================
    
    /**
     * Virtual Threads are lightweight threads managed by JVM, not OS.
     * 
     * Benefits:
     * - ~1KB stack (vs ~1MB for platform threads)
     * - Can create millions of virtual threads
     * - Blocking I/O unmounts from carrier thread
     * - Best for I/O-bound tasks
     * 
     * Note: Requires Java 21+. Uncomment to run if available.
     */
    static class VirtualThreadDemo {
        
        public static void run() throws Exception {
            System.out.println("\n=== Virtual Threads Demo (Java 21+) ===\n");
            
            // Uncomment if running on Java 21+
            /*
            // Create single virtual thread
            Thread vThread = Thread.ofVirtual().start(() -> {
                System.out.println("Running in virtual thread: " + Thread.currentThread());
            });
            vThread.join();
            
            // Virtual thread per task executor
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                List<Future<String>> futures = new ArrayList<>();
                
                for (int i = 0; i < 10000; i++) {
                    final int taskId = i;
                    futures.add(executor.submit(() -> {
                        Thread.sleep(100); // Blocking doesn't waste platform thread!
                        return "Task " + taskId + " completed";
                    }));
                }
                
                // All 10,000 tasks run concurrently with minimal threads
                for (Future<String> future : futures) {
                    future.get();
                }
            }
            
            System.out.println("Completed 10,000 virtual thread tasks");
            */
            
            System.out.println("Virtual threads require Java 21+");
        }
    }

    // ================================================================
    // 8. COMPLETABLE FUTURE
    // ================================================================
    
    static class CompletableFutureDemo {
        
        public static void run() throws Exception {
            System.out.println("\n=== CompletableFuture Demo ===\n");
            
            basicOperations();
            chainingOperations();
            combiningFutures();
            exceptionHandling();
        }
        
        private static void basicOperations() throws Exception {
            System.out.println("--- Basic Operations ---");
            
            // supplyAsync - returns a value
            CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
                sleep(100);
                return "Result from supplyAsync";
            });
            
            // runAsync - no return value
            CompletableFuture<Void> cf2 = CompletableFuture.runAsync(() -> {
                sleep(100);
                System.out.println("runAsync completed");
            });
            
            System.out.println("cf1 result: " + cf1.get());
            cf2.join();
        }
        
        private static void chainingOperations() throws Exception {
            System.out.println("\n--- Chaining Operations ---");
            
            String result = CompletableFuture
                .supplyAsync(() -> "Hello")
                .thenApply(s -> s + " World")        // Transform: String -> String
                .thenApply(String::toUpperCase)       // Transform: String -> String
                .thenApplyAsync(s -> s + "!")         // Async transform
                .get();
            
            System.out.println("Chained result: " + result);
            
            // thenAccept - consume result (no return)
            CompletableFuture.supplyAsync(() -> "Data")
                .thenAccept(data -> System.out.println("Consumed: " + data))
                .join();
            
            // thenRun - run action after completion (no access to result)
            CompletableFuture.supplyAsync(() -> "Ignored")
                .thenRun(() -> System.out.println("Task completed"))
                .join();
        }
        
        private static void combiningFutures() throws Exception {
            System.out.println("\n--- Combining Futures ---");
            
            CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
                sleep(100);
                return "API1";
            });
            
            CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
                sleep(200);
                return "API2";
            });
            
            CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> {
                sleep(150);
                return "API3";
            });
            
            // thenCombine - combine two futures
            String combined = cf1.thenCombine(cf2, (r1, r2) -> r1 + " + " + r2).get();
            System.out.println("thenCombine result: " + combined);
            
            // allOf - wait for all (returns Void)
            CompletableFuture<Void> allOf = CompletableFuture.allOf(cf1, cf2, cf3);
            allOf.join();
            
            // Get results after allOf
            List<String> results = Stream.of(cf1, cf2, cf3)
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
            System.out.println("allOf results: " + results);
            
            // anyOf - complete when any one completes
            cf1 = CompletableFuture.supplyAsync(() -> { sleep(100); return "Fast"; });
            cf2 = CompletableFuture.supplyAsync(() -> { sleep(200); return "Slow"; });
            
            Object first = CompletableFuture.anyOf(cf1, cf2).join();
            System.out.println("anyOf first result: " + first);
        }
        
        private static void exceptionHandling() throws Exception {
            System.out.println("\n--- Exception Handling ---");
            
            // exceptionally - handle exception, return fallback
            String result = CompletableFuture
                .<String>supplyAsync(() -> {
                    throw new RuntimeException("API failed");
                })
                .exceptionally(ex -> {
                    System.out.println("Exception caught: " + ex.getMessage());
                    return "Fallback value";
                })
                .get();
            System.out.println("Result with exception handling: " + result);
            
            // handle - handle both success and failure
            String result2 = CompletableFuture
                .supplyAsync(() -> "Success")
                .handle((value, ex) -> {
                    if (ex != null) {
                        return "Error: " + ex.getMessage();
                    }
                    return "Handled: " + value;
                })
                .get();
            System.out.println("Handle result: " + result2);
            
            // whenComplete - side effects only, doesn't transform result
            CompletableFuture.supplyAsync(() -> "Data")
                .whenComplete((value, ex) -> {
                    if (ex != null) {
                        System.out.println("Failed with: " + ex);
                    } else {
                        System.out.println("Completed with: " + value);
                    }
                })
                .join();
        }
        
        private static void sleep(long ms) {
            try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    // ================================================================
    // 9. STREAM PARALLEL
    // ================================================================
    
    static class StreamParallelDemo {
        
        public static void run() {
            System.out.println("\n=== Stream Parallel Demo ===\n");
            
            // Good use case: Large data, CPU-intensive operations
            List<Integer> largeList = IntStream.range(0, 1000000)
                .boxed()
                .collect(Collectors.toList());
            
            // Sequential
            long start = System.currentTimeMillis();
            long sum1 = largeList.stream()
                .mapToLong(i -> heavyComputation(i))
                .sum();
            long seqTime = System.currentTimeMillis() - start;
            
            // Parallel
            start = System.currentTimeMillis();
            long sum2 = largeList.parallelStream()
                .mapToLong(i -> heavyComputation(i))
                .sum();
            long parTime = System.currentTimeMillis() - start;
            
            System.out.println("Sequential time: " + seqTime + "ms");
            System.out.println("Parallel time: " + parTime + "ms");
            System.out.println("Speedup: " + (float) seqTime / parTime + "x");
            
            // Don't use parallel for:
            demonstrateBadUseCases();
            
            // Custom ForkJoinPool to avoid common pool starvation
            customForkJoinPoolDemo(largeList);
        }
        
        private static long heavyComputation(int i) {
            // Simulating CPU-intensive work
            return (long) Math.pow(i, 2);
        }
        
        private static void demonstrateBadUseCases() {
            System.out.println("\n--- When NOT to use parallel() ---");
            
            // 1. Small collections - overhead > benefit
            List<Integer> smallList = Arrays.asList(1, 2, 3, 4, 5);
            // smallList.parallelStream()... // Don't do this
            
            // 2. LinkedList - poor splitting performance
            LinkedList<Integer> linkedList = new LinkedList<>();
            // linkedList.parallelStream()... // O(n) to split
            
            // 3. I/O operations - blocks threads
            // list.parallelStream().forEach(item -> httpClient.call(item)); // Bad!
            
            // 4. Stateful operations
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            List<Integer> results = new ArrayList<>(); // Shared mutable state!
            // numbers.parallelStream().forEach(results::add); // Thread-unsafe!
            
            // Correct way for stateful:
            List<Integer> safeResults = numbers.parallelStream()
                .collect(Collectors.toList()); // Thread-safe collector
            
            System.out.println("Safe parallel collect: " + safeResults);
        }
        
        private static void customForkJoinPoolDemo(List<Integer> list) {
            System.out.println("\n--- Custom ForkJoinPool ---");
            
            // Avoid polluting common ForkJoinPool
            ForkJoinPool customPool = new ForkJoinPool(4);
            
            try {
                List<Integer> result = customPool.submit(() ->
                    list.parallelStream()
                        .filter(i -> i % 2 == 0)
                        .collect(Collectors.toList())
                ).join();
                
                System.out.println("Processed " + result.size() + " items with custom pool");
            } finally {
                customPool.shutdown();
            }
        }
    }

    // ================================================================
    // 10. GARBAGE COLLECTION DEMO
    // ================================================================
    
    /**
     * Demonstrates GC concepts through code.
     * 
     * An object becomes eligible for GC when it's unreachable from GC roots:
     * - Local variables in active threads
     * - Static fields
     * - JNI references
     * - Active threads themselves
     */
    static class GarbageCollectionDemo {
        
        public static void run() {
            System.out.println("\n=== Garbage Collection Demo ===\n");
            
            // When is object eligible for GC?
            demonstrateEligibility();
            
            // Finalization (deprecated in Java 9+)
            demonstrateFinalization();
            
            // Memory leak examples
            demonstrateMemoryLeaks();
        }
        
        private static void demonstrateEligibility() {
            System.out.println("--- GC Eligibility ---");
            
            // Case 1: Nulling reference
            Object obj1 = new Object();
            obj1 = null; // Now eligible for GC
            
            // Case 2: Reassignment
            Object obj2 = new Object(); // First object
            obj2 = new Object(); // First object now eligible
            
            // Case 3: Out of scope
            { 
                Object obj3 = new Object();
            } // obj3 eligible after this block
            
            // Case 4: Island of isolation
            class Node {
                Node next;
            }
            Node n1 = new Node();
            Node n2 = new Node();
            n1.next = n2;
            n2.next = n1;
            
            n1 = null;
            n2 = null;
            // Both nodes are now eligible despite circular reference
            
            System.out.println("Objects marked for GC (no guarantees when collected)");
        }
        
        private static void demonstrateFinalization() {
            System.out.println("\n--- Finalization (Deprecated) ---");
            
            // finalize() is deprecated in Java 9+
            // Use try-with-resources and Cleaner API instead
            
            class Resource implements AutoCloseable {
                @Override
                public void close() {
                    System.out.println("Resource closed properly");
                }
            }
            
            // Proper resource management
            try (Resource resource = new Resource()) {
                // Use resource
            } catch (Exception e) {
                // Handle exception
            }
        }
        
        private static void demonstrateMemoryLeaks() {
            System.out.println("\n--- Common Memory Leak Patterns ---");
            
            // 1. Static collections that grow unbounded
            // static List<Object> cache = new ArrayList<>(); // Dangerous!
            
            // 2. ThreadLocal not removed
            ThreadLocal<byte[]> threadLocal = new ThreadLocal<>();
            threadLocal.set(new byte[1024 * 1024]); // 1MB
            // threadLocal.remove(); // MUST call this, especially in thread pools!
            threadLocal.remove();
            
            // 3. Listeners/callbacks not deregistered
            // eventSource.addEventListener(listener); // Must remove when done
            
            // 4. Inner class holding reference to outer class
            class Outer {
                byte[] data = new byte[1024 * 1024];
                
                class Inner {
                    // Inner implicitly holds reference to Outer
                    // Even if Outer is nulled, Inner keeps it alive
                }
            }
            
            System.out.println("Be mindful of these leak patterns!");
        }
    }

    // ================================================================
    // 11. DESIGN PATTERNS
    // ================================================================
    
    /**
     * Common design patterns frequently asked in interviews.
     */
    static class DesignPatternsDemo {
        
        // ----- BUILDER PATTERN -----
        
        /**
         * Builder Pattern: Construct complex objects step by step.
         * Use when: Many constructor parameters, optional fields, immutable objects.
         */
        static class Order {
            private final Long id;
            private final String customerName;
            private final List<String> items;
            private final double total;
            private final String status;
            
            private Order(Builder builder) {
                this.id = builder.id;
                this.customerName = builder.customerName;
                this.items = builder.items;
                this.total = builder.total;
                this.status = builder.status;
            }
            
            public static Builder builder() {
                return new Builder();
            }
            
            static class Builder {
                private Long id;
                private String customerName;
                private List<String> items = new ArrayList<>();
                private double total;
                private String status = "PENDING";
                
                public Builder id(Long id) { this.id = id; return this; }
                public Builder customerName(String name) { this.customerName = name; return this; }
                public Builder items(List<String> items) { this.items = items; return this; }
                public Builder addItem(String item) { this.items.add(item); return this; }
                public Builder total(double total) { this.total = total; return this; }
                public Builder status(String status) { this.status = status; return this; }
                
                public Order build() {
                    // Validation can go here
                    return new Order(this);
                }
            }
            
            @Override
            public String toString() {
                return "Order{id=" + id + ", customer=" + customerName + 
                       ", items=" + items + ", total=" + total + ", status=" + status + "}";
            }
        }
        
        // ----- STRATEGY PATTERN -----
        
        /**
         * Strategy Pattern: Define a family of algorithms, encapsulate each one.
         * Use when: Multiple ways to perform an operation, avoid long if-else chains.
         */
        interface PaymentStrategy {
            void pay(double amount);
        }
        
        static class CreditCardPayment implements PaymentStrategy {
            private String cardNumber;
            
            CreditCardPayment(String cardNumber) {
                this.cardNumber = cardNumber;
            }
            
            @Override
            public void pay(double amount) {
                System.out.println("Paid $" + amount + " with Credit Card: " + cardNumber);
            }
        }
        
        static class PayPalPayment implements PaymentStrategy {
            private String email;
            
            PayPalPayment(String email) {
                this.email = email;
            }
            
            @Override
            public void pay(double amount) {
                System.out.println("Paid $" + amount + " with PayPal: " + email);
            }
        }
        
        static class ShoppingCart {
            private PaymentStrategy paymentStrategy;
            
            public void setPaymentStrategy(PaymentStrategy strategy) {
                this.paymentStrategy = strategy;
            }
            
            public void checkout(double amount) {
                paymentStrategy.pay(amount);
            }
        }
        
        // ----- SINGLETON PATTERN -----
        
        /**
         * Singleton Pattern: Ensure only one instance exists.
         * Modern approach: Use enum (thread-safe, serialization-safe).
         */
        enum DatabaseConnection {
            INSTANCE;
            
            private String url = "jdbc:mysql://localhost:3306/db";
            
            public void connect() {
                System.out.println("Connected to: " + url);
            }
        }
        
        // ----- FACTORY PATTERN -----
        
        /**
         * Factory Pattern: Create objects without specifying exact class.
         */
        interface Notification {
            void send(String message);
        }
        
        static class EmailNotification implements Notification {
            public void send(String message) {
                System.out.println("Email: " + message);
            }
        }
        
        static class SMSNotification implements Notification {
            public void send(String message) {
                System.out.println("SMS: " + message);
            }
        }
        
        static class NotificationFactory {
            public static Notification create(String type) {
                switch (type.toLowerCase()) {
                    case "email":
                        return new EmailNotification();
                    case "sms":
                        return new SMSNotification();
                    default:
                        throw new IllegalArgumentException("Unknown type: " + type);
                }
            }
        }
        
        public static void run() {
            System.out.println("\n=== Design Patterns Demo ===\n");
            
            // Builder
            System.out.println("--- Builder Pattern ---");
            Order order = Order.builder()
                .id(1L)
                .customerName("John Doe")
                .addItem("Laptop")
                .addItem("Mouse")
                .total(1299.99)
                .build();
            System.out.println(order);
            
            // Strategy
            System.out.println("\n--- Strategy Pattern ---");
            ShoppingCart cart = new ShoppingCart();
            cart.setPaymentStrategy(new CreditCardPayment("1234-5678"));
            cart.checkout(100.0);
            
            cart.setPaymentStrategy(new PayPalPayment("john@example.com"));
            cart.checkout(50.0);
            
            // Singleton
            System.out.println("\n--- Singleton Pattern ---");
            DatabaseConnection.INSTANCE.connect();
            
            // Factory
            System.out.println("\n--- Factory Pattern ---");
            Notification email = NotificationFactory.create("email");
            email.send("Hello via email!");
            
            Notification sms = NotificationFactory.create("sms");
            sms.send("Hello via SMS!");
        }
    }

    // ================================================================
    // 12. SPRING-LIKE PROXY DEMONSTRATION
    // ================================================================
    
    /**
     * Demonstrates how Spring's @Transactional and @Async work using proxies.
     * This is a simplified version to understand the concept.
     */
    static class ProxyDemo {
        
        interface UserService {
            void createUser(String name);
            void updateUser(String name);
        }
        
        static class UserServiceImpl implements UserService {
            @Override
            public void createUser(String name) {
                System.out.println("Creating user: " + name);
            }
            
            @Override
            public void updateUser(String name) {
                System.out.println("Updating user: " + name);
            }
        }
        
        /**
         * JDK Dynamic Proxy - wraps interface implementations.
         * This is similar to how Spring creates proxies for @Transactional.
         */
        static UserService createTransactionalProxy(UserService target) {
            return (UserService) java.lang.reflect.Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class[]{UserService.class},
                (proxy, method, args) -> {
                    System.out.println("[TX] BEGIN TRANSACTION");
                    try {
                        Object result = method.invoke(target, args);
                        System.out.println("[TX] COMMIT TRANSACTION");
                        return result;
                    } catch (Exception e) {
                        System.out.println("[TX] ROLLBACK TRANSACTION");
                        throw e;
                    }
                }
            );
        }
        
        public static void run() {
            System.out.println("\n=== Proxy Demo (Spring-like) ===\n");
            
            // Without proxy - no transaction
            System.out.println("--- Without Proxy ---");
            UserService service = new UserServiceImpl();
            service.createUser("John");
            
            // With proxy - transparent transaction management
            System.out.println("\n--- With Transactional Proxy ---");
            UserService proxiedService = createTransactionalProxy(service);
            proxiedService.createUser("Jane");
            
            // IMPORTANT: Self-invocation problem demonstration
            System.out.println("\n--- Self-Invocation Problem ---");
            System.out.println("When method A calls method B within same class,");
            System.out.println("the call to B bypasses the proxy!");
            System.out.println("This is why @Transactional self-invocation doesn't work.");
        }
    }
}
