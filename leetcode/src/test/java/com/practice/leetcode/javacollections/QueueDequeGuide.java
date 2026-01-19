package com.practice.leetcode.javacollections;

import java.util.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                        QUEUE & DEQUE GUIDE                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ Queue  - FIFO (First In, First Out)                                         │
 * │ Deque  - Double-Ended Queue (cả 2 đầu)                                      │
 * │ PriorityQueue - Heap-based, sorted order                                    │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ IMPLEMENTATIONS:                                                            │
 * │ ArrayDeque   - Best for Stack/Queue (faster than LinkedList!)               │
 * │ LinkedList   - Also implements Queue/Deque                                  │
 * │ PriorityQueue- Heap, O(log n) add/remove                                    │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class QueueDequeGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // QUEUE OPERATIONS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ Queue có 2 set methods - throws exception vs returns special value        │
   * │                                                                           │
   * │ Operation   │ Throws Exception  │ Returns null/false                      │
   * │ ────────────┼───────────────────┼──────────────────                       │
   * │ Insert      │ add(e)            │ offer(e)         ← PREFER               │
   * │ Remove      │ remove()          │ poll()           ← PREFER               │
   * │ Examine     │ element()         │ peek()           ← PREFER               │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  void queueOperations() {
    Queue<String> queue = new ArrayDeque<>();

    // ADD (to tail)
    queue.offer("first");    // returns true/false - PREFER
    queue.add("second");     // throws exception if full

    // REMOVE (from head)
    String removed = queue.poll();    // returns null if empty - PREFER
    // String removed2 = queue.remove(); // throws exception if empty

    // PEEK (head without removing)
    String head = queue.peek();       // returns null if empty - PREFER
    // String head2 = queue.element();  // throws exception if empty

    // SIZE & EMPTY
    int size = queue.size();
    boolean empty = queue.isEmpty();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // DEQUE OPERATIONS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ Deque = Stack + Queue                                                     │
   * │                                                                           │
   * │ First (head)       │ addFirst()  offerFirst() │ removeFirst() pollFirst() │
   * │ Last (tail)        │ addLast()   offerLast()  │ removeLast()  pollLast()  │
   * │                                                                           │
   * │ As Stack (LIFO): push() = addFirst(), pop() = removeFirst()               │
   * │ As Queue (FIFO): offer() = offerLast(), poll() = pollFirst()              │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  void dequeOperations() {
    Deque<String> deque = new ArrayDeque<>();

    // ADD
    deque.offerFirst("head");    // Add to head
    deque.offerLast("tail");     // Add to tail
    deque.offer("tail2");        // Same as offerLast

    // REMOVE
    String first = deque.pollFirst();  // Remove from head
    String last = deque.pollLast();    // Remove from tail
    String poll = deque.poll();        // Same as pollFirst

    // PEEK
    String peekFirst = deque.peekFirst();
    String peekLast = deque.peekLast();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // USE AS STACK (LIFO)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ĐỂ IMPLEMENT STACK, DÙNG ArrayDeque, KHÔNG DÙNG Stack class!
   * Stack class là legacy, synchronized, chậm.
   */
  void useAsStack() {
    Deque<Integer> stack = new ArrayDeque<>();

    // PUSH (add to top)
    stack.push(1);
    stack.push(2);
    stack.push(3);

    // POP (remove from top)
    int top = stack.pop();     // 3
    int next = stack.pop();    // 2

    // PEEK (see top without removing)
    int peek = stack.peek();   // 1

    // Common pattern: check before pop
    if (!stack.isEmpty()) {
      int value = stack.pop();
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PRIORITY QUEUE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * PriorityQueue = Heap implementation
   * - Min heap by default (smallest first)
   * - O(log n) add/remove, O(1) peek
   */
  void priorityQueueOperations() {
    // 1. Min heap (default) - smallest first
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    minHeap.offer(3);
    minHeap.offer(1);
    minHeap.offer(2);
    // peek() = 1, poll order: 1, 2, 3

    // 2. Max heap - largest first
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    // Or: new PriorityQueue<>(Comparator.reverseOrder());
    maxHeap.offer(3);
    maxHeap.offer(1);
    maxHeap.offer(2);
    // peek() = 3, poll order: 3, 2, 1

    // 3. Custom comparator
    PriorityQueue<String> byLength = new PriorityQueue<>(
        Comparator.comparingInt(String::length)
    );

    // 4. With initial capacity
    PriorityQueue<Integer> pq = new PriorityQueue<>(100);

    // OPERATIONS
    pq.offer(5);                    // O(log n)
    int min = pq.peek();            // O(1), không remove
    int removed = pq.poll();        // O(log n), remove và return
    boolean has = pq.contains(3);   // O(n)!
    pq.remove(3);                   // O(n)! - tìm và remove
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // COMMON PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Top K Elements                                                   │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  int[] topKLargest(int[] nums, int k) {
    // Min heap of size k (keeps k largest)
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    for (int num : nums) {
      minHeap.offer(num);
      if (minHeap.size() > k) {
        minHeap.poll();  // Remove smallest
      }
    }

    // Result
    int[] result = new int[k];
    for (int i = k - 1; i >= 0; i--) {
      result[i] = minHeap.poll();
    }
    return result;
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: K Smallest Elements                                              │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  int[] topKSmallest(int[] nums, int k) {
    // Max heap of size k (keeps k smallest)
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

    for (int num : nums) {
      maxHeap.offer(num);
      if (maxHeap.size() > k) {
        maxHeap.poll();  // Remove largest
      }
    }

    int[] result = new int[k];
    for (int i = k - 1; i >= 0; i--) {
      result[i] = maxHeap.poll();
    }
    return result;
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Merge K Sorted Lists                                             │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  int[] mergeKSortedArrays(int[][] arrays) {
    PriorityQueue<int[]> pq = new PriorityQueue<>(
        Comparator.comparingInt(a -> a[0])  // Compare by value
    );
    // Each element: {value, arrayIndex, elementIndex}

    int total = 0;
    for (int i = 0; i < arrays.length; i++) {
      if (arrays[i].length > 0) {
        pq.offer(new int[]{arrays[i][0], i, 0});
        total += arrays[i].length;
      }
    }

    int[] result = new int[total];
    int idx = 0;

    while (!pq.isEmpty()) {
      int[] curr = pq.poll();
      result[idx++] = curr[0];

      int arrIdx = curr[1];
      int elemIdx = curr[2];

      if (elemIdx + 1 < arrays[arrIdx].length) {
        pq.offer(new int[]{arrays[arrIdx][elemIdx + 1], arrIdx, elemIdx + 1});
      }
    }

    return result;
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: BFS with Queue                                                   │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  void bfsTemplate() {
    Queue<Integer> queue = new ArrayDeque<>();
    Set<Integer> visited = new HashSet<>();

    // queue.offer(start);
    // visited.add(start);

    while (!queue.isEmpty()) {
      int size = queue.size();  // Process level by level
      for (int i = 0; i < size; i++) {
        int current = queue.poll();
        // process(current);

        // for (int neighbor : getNeighbors(current)) {
        //   if (!visited.contains(neighbor)) {
        //     visited.add(neighbor);
        //     queue.offer(neighbor);
        //   }
        // }
      }
      // level++;
    }
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Sliding Window using Deque                                       │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  int[] maxSlidingWindow(int[] nums, int k) {
    Deque<Integer> deque = new ArrayDeque<>();  // Stores indices
    int[] result = new int[nums.length - k + 1];

    for (int i = 0; i < nums.length; i++) {
      // Remove indices out of window
      while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
        deque.pollFirst();
      }

      // Maintain decreasing order (remove smaller elements)
      while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
        deque.pollLast();
      }

      deque.offerLast(i);

      // Record result when window is complete
      if (i >= k - 1) {
        result[i - k + 1] = nums[deque.peekFirst()];
      }
    }
    return result;
  }
}
