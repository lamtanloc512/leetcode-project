package com.practice.leetcode.javacollections;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                     JAVA COLLECTIONS FRAMEWORK                            ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │                          COLLECTIONS HIERARCHY                              │
 * ├─────────────────────────────────────────────────────────────────────────────┤
 * │                                                                             │
 * │                            Iterable<E>                                      │
 * │                                 │                                           │
 * │                           Collection<E>                                     │
 * │              ┌──────────────────┼──────────────────┐                        │
 * │              │                  │                  │                        │
 * │           List<E>            Set<E>            Queue<E>                     │
 * │              │                  │                  │                        │
 * │     ┌────────┼────────┐   ┌─────┼─────┐     ┌──────┼──────┐                 │
 * │     │        │        │   │     │     │     │      │      │                 │
 * │ ArrayList LinkedList  │ HashSet│ TreeSet  Deque       PriorityQ             │
 * │          Vector       │   LinkedHashSet     │                               │
 * │                       │                 ArrayDeque                          │
 * │                                                                             │
 * │                              Map<K,V>                                       │
 * │              ┌──────────────────┼──────────────────┐                        │
 * │              │                  │                  │                        │
 * │          HashMap          LinkedHashMap        TreeMap                      │
 * │              │                                                              │
 * │         ConcurrentHashMap                                                   │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │                           CHỌN COLLECTION NÀO?                              │
 * ├─────────────────────────────────────────────────────────────────────────────┤
 * │                                                                             │
 * │ ┌─────────────────┬────────────────────────────────────────────────────┐    │
 * │ │ Requirement     │ Collection                                        │    │
 * │ ├─────────────────┼────────────────────────────────────────────────────┤    │
 * │ │ Index access    │ ArrayList                                         │    │
 * │ │ Fast add/remove │ LinkedList                                        │    │
 * │ │ Unique elements │ HashSet (unordered) / TreeSet (sorted)            │    │
 * │ │ Key-Value pairs │ HashMap (unordered) / TreeMap (sorted)            │    │
 * │ │ Insert order    │ LinkedHashSet / LinkedHashMap                     │    │
 * │ │ FIFO queue      │ ArrayDeque                                        │    │
 * │ │ Priority queue  │ PriorityQueue                                     │    │
 * │ │ Stack (LIFO)    │ ArrayDeque (NOT Stack class!)                     │    │
 * │ │ Thread-safe     │ ConcurrentHashMap, CopyOnWriteArrayList           │    │
 * │ └─────────────────┴────────────────────────────────────────────────────┘    │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │                         TIME COMPLEXITY SUMMARY                             │
 * ├─────────────────────────────────────────────────────────────────────────────┤
 * │                                                                             │
 * │ ┌────────────────┬─────────┬─────────┬─────────┬───────────┬──────────┐     │
 * │ │                │ add     │ remove  │ get     │ contains  │ Ordered  │     │
 * │ ├────────────────┼─────────┼─────────┼─────────┼───────────┼──────────┤     │
 * │ │ ArrayList      │ O(1)*   │ O(n)    │ O(1)    │ O(n)      │ Yes      │     │
 * │ │ LinkedList     │ O(1)    │ O(1)**  │ O(n)    │ O(n)      │ Yes      │     │
 * │ │ HashSet        │ O(1)    │ O(1)    │ -       │ O(1)      │ No       │     │
 * │ │ TreeSet        │ O(logn) │ O(logn) │ -       │ O(logn)   │ Sorted   │     │
 * │ │ HashMap        │ O(1)    │ O(1)    │ O(1)    │ O(1)      │ No       │     │
 * │ │ TreeMap        │ O(logn) │ O(logn) │ O(logn) │ O(logn)   │ Sorted   │     │
 * │ │ ArrayDeque     │ O(1)    │ O(1)    │ O(n)    │ O(n)      │ Yes      │     │
 * │ │ PriorityQueue  │ O(logn) │ O(logn) │ O(1)*** │ O(n)      │ Heap     │     │
 * │ └────────────────┴─────────┴─────────┴─────────┴───────────┴──────────┘     │
 * │                                                                             │
 * │ * amortized, ** if have reference, *** peek only                            │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class CollectionsOverview {
  // See other files in this package for detailed guides:
  // - ListGuide.java
  // - SetGuide.java
  // - MapGuide.java
  // - QueueDequeGuide.java
}
