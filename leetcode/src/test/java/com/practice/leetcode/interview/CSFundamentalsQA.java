package com.practice.leetcode.interview;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                   CS FUNDAMENTALS INTERVIEW Q&A                            ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class CSFundamentalsQA {

  // ═══════════════════════════════════════════════════════════════════════════
  // BIG O NOTATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Big O notation là gì? Các độ phức tạp phổ biến?                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Cách đo độ phức tạp thuật toán khi input tăng                            │
   * │                                                                             │
   * │ ┌──────────────────┬───────────────────────────────────────────────────┐    │
   * │ │ Complexity       │ Example                                           │    │
   * │ ├──────────────────┼───────────────────────────────────────────────────┤    │
   * │ │ O(1)             │ Array access, HashMap get                         │    │
   * │ │ O(log n)         │ Binary search, balanced tree operations           │    │
   * │ │ O(n)             │ Linear search, single loop                        │    │
   * │ │ O(n log n)       │ Merge sort, Quick sort (average)                  │    │
   * │ │ O(n²)            │ Bubble sort, nested loops                         │    │
   * │ │ O(2^n)           │ Recursive Fibonacci, subsets                      │    │
   * │ │ O(n!)            │ Permutations                                      │    │
   * │ └──────────────────┴───────────────────────────────────────────────────┘    │
   * │                                                                             │
   * │ TÍNH BIG O:                                                                 │
   * │ - Bỏ constants: O(2n) → O(n)                                                │
   * │ - Giữ term lớn nhất: O(n² + n) → O(n²)                                      │
   * │ - Nested loops nhân: O(n) × O(m) = O(nm)                                    │
   * │ - Sequential cộng: O(n) + O(m) = O(n + m)                                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Time vs Space complexity?                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │ - TIME: Số operations khi input tăng                                        │
   * │ - SPACE: Bộ nhớ sử dụng thêm (không tính input)                             │
   * │                                                                             │
   * │ TRADE-OFF:                                                                  │
   * │ - Dùng thêm memory để giảm time (caching, memoization)                      │
   * │ - Dùng thêm time để giảm memory (in-place algorithms)                       │
   * │                                                                             │
   * │ EXAMPLE - Fibonacci:                                                        │
   * │ - Recursive: O(2^n) time, O(n) space (call stack)                           │
   * │ - Memoization: O(n) time, O(n) space (cache)                                │
   * │ - Iterative: O(n) time, O(1) space                                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DATA STRUCTURES COMPARISON
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Array vs LinkedList?                                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌────────────────┬───────────────────────┬─────────────────────────────┐    │
   * │ │ Operation      │ Array                 │ LinkedList                  │    │
   * │ ├────────────────┼───────────────────────┼─────────────────────────────┤    │
   * │ │ Access [i]     │ O(1)                  │ O(n)                        │    │
   * │ │ Search         │ O(n), O(log n) sorted │ O(n)                        │    │
   * │ │ Insert (begin) │ O(n)                  │ O(1)                        │    │
   * │ │ Insert (end)   │ O(1) amortized        │ O(1) if tail pointer        │    │
   * │ │ Insert (mid)   │ O(n)                  │ O(1) if have node           │    │
   * │ │ Delete         │ O(n)                  │ O(1) if have node           │    │
   * │ │ Memory         │ Contiguous            │ Scattered + pointers        │    │
   * │ └────────────────┴───────────────────────┴─────────────────────────────┘    │
   * │                                                                             │
   * │ USE ARRAY: Random access, cache-friendly, known size                        │
   * │ USE LINKED: Frequent insert/delete, unknown size, queue/stack               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Stack vs Queue?                                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌──────────────────┬───────────────────┬─────────────────────────────────┐  │
   * │ │                  │ Stack             │ Queue                           │  │
   * │ ├──────────────────┼───────────────────┼─────────────────────────────────┤  │
   * │ │ Principle        │ LIFO              │ FIFO                            │  │
   * │ │ Insert           │ push() - top      │ enqueue() - rear                │  │
   * │ │ Remove           │ pop() - top       │ dequeue() - front               │  │
   * │ │ Peek             │ peek() - top      │ peek() - front                  │  │
   * │ └──────────────────┴───────────────────┴─────────────────────────────────┘  │
   * │                                                                             │
   * │ USE STACK: Undo, recursion, expression parsing, DFS                         │
   * │ USE QUEUE: BFS, scheduling, buffering, message queues                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Hash Table collision resolution?                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ 1. CHAINING (Separate Chaining):                                            │
   * │    - Mỗi bucket là linked list                                              │
   * │    - Collision → thêm vào list                                              │
   * │    - Pro: Đơn giản, không cần resize thường xuyên                           │
   * │    - Con: Extra memory cho pointers                                         │
   * │                                                                             │
   * │ 2. OPEN ADDRESSING:                                                         │
   * │    - Linear Probing: hash+1, hash+2, ...                                    │
   * │    - Quadratic Probing: hash+1², hash+2², ...                               │
   * │    - Double Hashing: hash + i*hash2                                         │
   * │    - Pro: Better cache locality                                             │
   * │    - Con: Clustering vấn đề, phải resize                                    │
   * │                                                                             │
   * │ JAVA HASHMAP: Chaining với linked list, chuyển sang Red-Black Tree          │
   * │               khi bucket có > 8 entries (Java 8+)                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Binary Tree vs BST vs AVL vs Red-Black Tree?                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌─────────────────┬────────────────────────────────────────────────────┐    │
   * │ │ Type            │ Property                                          │    │
   * │ ├─────────────────┼────────────────────────────────────────────────────┤    │
   * │ │ Binary Tree     │ Max 2 children per node                           │    │
   * │ │ BST             │ Left < Node < Right                               │    │
   * │ │ AVL             │ BST + height balanced (diff ≤ 1)                  │    │
   * │ │ Red-Black       │ BST + color rules (less strict than AVL)          │    │
   * │ └─────────────────┴────────────────────────────────────────────────────┘    │
   * │                                                                             │
   * │ OPERATIONS O(log n) - balanced BST:                                         │
   * │ - Insert, Delete, Search                                                    │
   * │                                                                             │
   * │ AVL vs RED-BLACK:                                                           │
   * │ - AVL: Strictly balanced → faster search, slower insert/delete              │
   * │ - Red-Black: Less strict → faster insert/delete, used in TreeMap/TreeSet   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SORTING ALGORITHMS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: So sánh các thuật toán Sorting?                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌────────────────┬──────────┬──────────┬──────────┬─────────┬────────────┐  │
   * │ │ Algorithm      │ Best     │ Average  │ Worst    │ Space   │ Stable     │  │
   * │ ├────────────────┼──────────┼──────────┼──────────┼─────────┼────────────┤  │
   * │ │ Bubble Sort    │ O(n)     │ O(n²)    │ O(n²)    │ O(1)    │ Yes        │  │
   * │ │ Selection Sort │ O(n²)    │ O(n²)    │ O(n²)    │ O(1)    │ No         │  │
   * │ │ Insertion Sort │ O(n)     │ O(n²)    │ O(n²)    │ O(1)    │ Yes        │  │
   * │ │ Merge Sort     │ O(nlogn) │ O(nlogn) │ O(nlogn) │ O(n)    │ Yes        │  │
   * │ │ Quick Sort     │ O(nlogn) │ O(nlogn) │ O(n²)    │ O(logn) │ No         │  │
   * │ │ Heap Sort      │ O(nlogn) │ O(nlogn) │ O(nlogn) │ O(1)    │ No         │  │
   * │ │ Counting Sort  │ O(n+k)   │ O(n+k)   │ O(n+k)   │ O(k)    │ Yes        │  │
   * │ │ Radix Sort     │ O(nk)    │ O(nk)    │ O(nk)    │ O(n+k)  │ Yes        │  │
   * │ └────────────────┴──────────┴──────────┴──────────┴─────────┴────────────┘  │
   * │                                                                             │
   * │ STABLE: Giữ nguyên thứ tự tương đối của các phần tử bằng nhau               │
   * │                                                                             │
   * │ JAVA USES:                                                                  │
   * │ - Arrays.sort(primitives): Dual-Pivot QuickSort                             │
   * │ - Arrays.sort(Objects): TimSort (Merge + Insertion)                         │
   * │ - Collections.sort(): TimSort                                               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // OPERATING SYSTEM CONCEPTS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Process vs Thread?                                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌─────────────────┬─────────────────────┬───────────────────────────────┐   │
   * │ │                 │ Process             │ Thread                        │   │
   * │ ├─────────────────┼─────────────────────┼───────────────────────────────┤   │
   * │ │ Definition      │ Instance của program│ Unit of execution in process  │   │
   * │ │ Memory          │ Separate space      │ Shared within process         │   │
   * │ │ Communication   │ IPC (pipes, sockets)│ Shared memory                 │   │
   * │ │ Creation cost   │ Expensive           │ Lightweight                   │   │
   * │ │ Context switch  │ Slow                │ Fast                          │   │
   * │ │ Crash impact    │ Isolated            │ Affects all threads           │   │
   * │ └─────────────────┴─────────────────────┴───────────────────────────────┘   │
   * │                                                                             │
   * │ THREAD STATES: New → Runnable → Running → Blocked/Waiting → Terminated      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Deadlock là gì? Cách tránh?                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Khi 2+ threads chờ nhau mãi mãi                                          │
   * │                                                                             │
   * │ 4 ĐIỀU KIỆN XẢY RA DEADLOCK (Coffman conditions):                           │
   * │ 1. Mutual Exclusion: Resource chỉ 1 thread dùng 1 lúc                       │
   * │ 2. Hold and Wait: Thread giữ resource và chờ resource khác                  │
   * │ 3. No Preemption: Không thể cưỡng chiếm resource                            │
   * │ 4. Circular Wait: A chờ B, B chờ C, C chờ A                                 │
   * │                                                                             │
   * │ CÁCH TRÁNH:                                                                 │
   * │ - Lock ordering: Luôn acquire locks theo thứ tự cố định                     │
   * │ - Lock timeout: Dùng tryLock() với timeout                                  │
   * │ - Deadlock detection: Monitor và kill threads                               │
   * │ - Một lock duy nhất: Coarse-grained locking                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Virtual Memory là gì?                                                    │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Trừu tượng hóa physical memory, mỗi process có address space riêng       │
   * │                                                                             │
   * │ CONCEPTS:                                                                   │
   * │ - Page: Fixed-size block trong virtual memory                               │
   * │ - Frame: Fixed-size block trong physical memory                             │
   * │ - Page Table: Maps virtual pages → physical frames                          │
   * │ - Page Fault: Page không trong RAM → load từ disk                           │
   * │                                                                             │
   * │ PAGE REPLACEMENT ALGORITHMS:                                                │
   * │ - FIFO: First In First Out                                                  │
   * │ - LRU: Least Recently Used (phổ biến nhất)                                  │
   * │ - LFU: Least Frequently Used                                                │
   * │ - Optimal: Replace page used furthest in future (theoretical)               │
   * │                                                                             │
   * │ THRASHING: Quá nhiều page faults → hệ thống chậm                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DATABASE FUNDAMENTALS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: ACID properties là gì?                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ A - ATOMICITY: All or nothing                                               │
   * │     Transaction thành công hoàn toàn hoặc rollback toàn bộ                  │
   * │                                                                             │
   * │ C - CONSISTENCY: Valid state → Valid state                                  │
   * │     Database luôn ở trạng thái hợp lệ (constraints satisfied)               │
   * │                                                                             │
   * │ I - ISOLATION: Transactions độc lập                                         │
   * │     Concurrent transactions không thấy intermediate states                  │
   * │                                                                             │
   * │ D - DURABILITY: Committed data persists                                     │
   * │     Sau commit, data không mất kể cả khi crash                              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: SQL vs NoSQL?                                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌───────────────────┬─────────────────────┬─────────────────────────────┐   │
   * │ │                   │ SQL                 │ NoSQL                       │   │
   * │ ├───────────────────┼─────────────────────┼─────────────────────────────┤   │
   * │ │ Schema            │ Fixed               │ Flexible                    │   │
   * │ │ Scaling           │ Vertical            │ Horizontal                  │   │
   * │ │ ACID              │ Strong              │ BASE (eventual)             │   │
   * │ │ Joins             │ Supported           │ Limited/None                │   │
   * │ │ Query             │ SQL                 │ Varies                      │   │
   * │ │ Examples          │ MySQL, PostgreSQL   │ MongoDB, Cassandra, Redis   │   │
   * │ └───────────────────┴─────────────────────┴─────────────────────────────┘   │
   * │                                                                             │
   * │ NOSQL TYPES:                                                                │
   * │ - Document: MongoDB (JSON-like)                                             │
   * │ - Key-Value: Redis, DynamoDB                                                │
   * │ - Column-Family: Cassandra, HBase                                           │
   * │ - Graph: Neo4j                                                              │
   * │                                                                             │
   * │ USE SQL: Complex queries, transactions, relationships                       │
   * │ USE NOSQL: Massive scale, flexible schema, high write throughput            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Index là gì? Khi nào dùng?                                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Data structure để tăng tốc query (thường là B-Tree hoặc Hash)            │
   * │                                                                             │
   * │ PROS:                                                                       │
   * │ - SELECT, WHERE, JOIN nhanh hơn                                             │
   * │ - ORDER BY, GROUP BY nhanh hơn                                              │
   * │                                                                             │
   * │ CONS:                                                                       │
   * │ - Chậm INSERT, UPDATE, DELETE (phải update index)                           │
   * │ - Tốn storage                                                               │
   * │                                                                             │
   * │ BEST PRACTICES:                                                             │
   * │ - Index columns trong WHERE, JOIN                                           │
   * │ - Composite index cho multiple columns query                                │
   * │ - Không index low cardinality columns (boolean, gender)                     │
   * │ - Không over-index (mỗi table nên < 5 indexes)                              │
   * │                                                                             │
   * │ INDEX TYPES:                                                                │
   * │ - B-Tree: Range queries, sorting (default)                                  │
   * │ - Hash: Exact match only                                                    │
   * │ - Full-text: Text search                                                    │
   * │ - Composite: Multiple columns                                               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Transaction Isolation Levels?                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌─────────────────────┬───────────┬───────────┬──────────────┬───────────┐  │
   * │ │ Level               │ Dirty Read│ Non-Rep   │ Phantom Read │ Perf      │  │
   * │ ├─────────────────────┼───────────┼───────────┼──────────────┼───────────┤  │
   * │ │ READ UNCOMMITTED    │ ✓         │ ✓         │ ✓            │ Fastest   │  │
   * │ │ READ COMMITTED      │ ✗         │ ✓         │ ✓            │ Fast      │  │
   * │ │ REPEATABLE READ     │ ✗         │ ✗         │ ✓            │ Slower    │  │
   * │ │ SERIALIZABLE        │ ✗         │ ✗         │ ✗            │ Slowest   │  │
   * │ └─────────────────────┴───────────┴───────────┴──────────────┴───────────┘  │
   * │                                                                             │
   * │ PROBLEMS:                                                                   │
   * │ - Dirty Read: Đọc uncommitted data                                          │
   * │ - Non-Repeatable Read: Đọc 2 lần kết quả khác nhau                          │
   * │ - Phantom Read: New rows appear trong cùng transaction                      │
   * │                                                                             │
   * │ DEFAULT: Most databases use READ COMMITTED                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
