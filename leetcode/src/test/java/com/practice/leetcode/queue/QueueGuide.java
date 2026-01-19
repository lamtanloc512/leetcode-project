package com.practice.leetcode.queue;

import java.util.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                              QUEUE GUIDE                                  ║
 * ║                             (Hàng đợi)                                    ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ QUEUE là gì?                                                                │
 * │                                                                             │
 * │ - FIFO: First In, First Out (vào trước, ra trước)                           │
 * │ - Operations: offer()/add(), poll()/remove(), peek() - đều O(1)             │
 * │                                                                             │
 * │ Visualize:                                                                  │
 * │   offer(1) → offer(2) → offer(3) → poll()=1 → poll()=2                      │
 * │                                                                             │
 * │   front ← [1] ← [2] ← [3] ← back                                            │
 * │   ──────────────────────────────→ ra front, vào back                        │
 * │                                                                             │
 * │ Java Implementations:                                                       │
 * │ - LinkedList<E>: Doubly linked list, null allowed                           │
 * │ - ArrayDeque<E>: Resizable array, faster, no null (RECOMMENDED)             │
 * │ - PriorityQueue<E>: Heap-based, sorted order                                │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class QueueGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 1: SIMULATION (Mô phỏng)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Dùng cho: Simulate real-world queue scenarios                               │
   * │ - Task scheduling, Round-robin processes                                    │
   * │ - People waiting in line                                                    │
   * │                                                                             │
   * │ Template:                                                                   │
   * │ Queue<Item> queue = new LinkedList<>();                                     │
   * │ // Initialize queue                                                         │
   * │ while (!queue.isEmpty()) {                                                  │
   * │     Item current = queue.poll();                                            │
   * │     process(current);                                                       │
   * │     if (needsMoreProcessing(current)) {                                     │
   * │         queue.offer(current);  // Go back to end                            │
   * │     }                                                                       │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ EXAMPLE: TIME NEEDED TO BUY TICKETS (LeetCode 2073)                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ tickets = [2,3,2], k = 2 → Answer: 6                                        │
   * │                                                                             │
   * │ Mỗi người mua 1 vé/lượt, rồi về cuối hàng. Tính thời gian người k mua xong. │
   * │                                                                             │
   * │ SOLUTION 1: SIMULATION (dễ hiểu, O(sum(tickets)))                           │
   * │ - Simulate từng lượt mua vé                                                 │
   * │ - Khi người k mua xong → return time                                        │
   * │                                                                             │
   * │ SOLUTION 2: MATH (optimal, O(n))                                            │
   * │ - Người đứng TRƯỚC k: mua min(tickets[i], tickets[k]) vé                    │
   * │ - Người đứng SAU k: mua min(tickets[i], tickets[k] - 1) vé                  │
   * │ - Bản thân k: mua tickets[k] vé                                             │
   * │                                                                             │
   * │ Vì sao? Trước khi k mua xong, người đứng trước k sẽ được mua                │
   * │ cùng số lượt với k. Người đứng sau sẽ ít hơn 1 lượt.                        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
  int timeRequiredToBuy_Simulation(int[] tickets, int k) {
    Queue<int[]> queue = new LinkedList<>();  // {index, ticketsLeft}
    for (int i = 0; i < tickets.length; i++) {
      queue.offer(new int[]{i, tickets[i]});
    }

    int time = 0;
    while (!queue.isEmpty()) {
      int[] person = queue.poll();
      person[1]--;  // Buy 1 ticket
      time++;

      if (person[0] == k && person[1] == 0) {
        return time;  // Person k is done!
      }

      if (person[1] > 0) {
        queue.offer(person);  // Go back to end of line
      }
    }
    return time;
  }

  // MATH solution - O(n)
  int timeRequiredToBuy_Math(int[] tickets, int k) {
    int time = 0;
    int target = tickets[k];

    for (int i = 0; i < tickets.length; i++) {
      if (i <= k) {
        // Người đứng trước hoặc bằng k: mua tối đa = target lượt
        time += Math.min(tickets[i], target);
      } else {
        // Người đứng sau k: mua tối đa = target - 1 lượt
        time += Math.min(tickets[i], target - 1);
      }
    }
    return time;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 2: BFS (Breadth-First Search)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Dùng cho: Shortest path, Level-order traversal, Multi-source BFS            │
   * │                                                                             │
   * │ Template:                                                                   │
   * │ Queue<Node> queue = new LinkedList<>();                                     │
   * │ Set<Node> visited = new HashSet<>();                                        │
   * │ queue.offer(start);                                                         │
   * │ visited.add(start);                                                         │
   * │                                                                             │
   * │ int level = 0;                                                              │
   * │ while (!queue.isEmpty()) {                                                  │
   * │     int size = queue.size();  // Process level by level                     │
   * │     for (int i = 0; i < size; i++) {                                        │
   * │         Node current = queue.poll();                                        │
   * │         if (isTarget(current)) return level;                                │
   * │                                                                             │
   * │         for (Node neighbor : getNeighbors(current)) {                       │
   * │             if (!visited.contains(neighbor)) {                              │
   * │                 visited.add(neighbor);                                      │
   * │                 queue.offer(neighbor);                                      │
   * │             }                                                               │
   * │         }                                                                   │
   * │     }                                                                       │
   * │     level++;                                                                │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 3: SLIDING WINDOW / DEQUE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Dùng cho: Sliding Window Maximum/Minimum                                    │
   * │                                                                             │
   * │ Deque (Double-ended queue) = có thể add/remove từ cả 2 đầu                  │
   * │                                                                             │
   * │ Template - SLIDING WINDOW MAXIMUM:                                          │
   * │ Deque<Integer> deque = new ArrayDeque<>();  // Lưu INDEX                    │
   * │                                                                             │
   * │ for (int i = 0; i < n; i++) {                                               │
   * │     // 1. Remove elements out of window (từ front)                          │
   * │     while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {             │
   * │         deque.pollFirst();                                                  │
   * │     }                                                                       │
   * │                                                                             │
   * │     // 2. Maintain monotonic decreasing (từ back)                           │
   * │     while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {          │
   * │         deque.pollLast();                                                   │
   * │     }                                                                       │
   * │                                                                             │
   * │     deque.offerLast(i);                                                     │
   * │                                                                             │
   * │     // 3. Get result (front is always max)                                  │
   * │     if (i >= k - 1) {                                                       │
   * │         result[i - k + 1] = nums[deque.peekFirst()];                        │
   * │     }                                                                       │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
  int[] maxSlidingWindow(int[] nums, int k) {
    if (nums == null || k <= 0) return new int[0];
    int n = nums.length;
    int[] result = new int[n - k + 1];
    Deque<Integer> deque = new ArrayDeque<>();  // stores indices

    for (int i = 0; i < n; i++) {
      // Remove indices out of current window
      while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
        deque.pollFirst();
      }

      // Remove smaller elements (maintain decreasing order)
      while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
        deque.pollLast();
      }

      deque.offerLast(i);

      // Window is complete, record result
      if (i >= k - 1) {
        result[i - k + 1] = nums[deque.peekFirst()];
      }
    }
    return result;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 4: CIRCULAR QUEUE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Dùng cho: Fixed-size buffer, Ring buffer                                    │
   * │                                                                             │
   * │ KEY INSIGHT:                                                                │
   * │ - Dùng array với 2 pointers: front và rear                                  │
   * │ - Wrap around: (index + 1) % capacity                                       │
   * │                                                                             │
   * │ Visualize (capacity = 4):                                                   │
   * │     [1] [2] [3] [ ]                                                         │
   * │      ↑           ↑                                                          │
   * │    front       rear                                                         │
   * │                                                                             │
   * │ Empty: front == rear && count == 0                                          │
   * │ Full: count == capacity                                                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // RELATED PROBLEMS CHEAT SHEET
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SIMULATION                                                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 2073. Time Needed to Buy Tickets         (Easy)   ← Current problem        │
   * │  933. Number of Recent Calls             (Easy)   - Count calls in window  │
   * │  622. Design Circular Queue              (Medium) - Implement circular Q   │
   * │  641. Design Circular Deque              (Medium) - Implement circular DQ  │
   * │  346. Moving Average from Data Stream    (Easy)   - Sliding window average │
   * │ 1823. Find the Winner of Circular Game   (Medium) - Josephus problem       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ BFS                                                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │  102. Binary Tree Level Order Traversal  (Medium) - Classic BFS             │
   * │  200. Number of Islands                  (Medium) - Grid BFS                │
   * │  752. Open the Lock                      (Medium) - State BFS               │
   * │  127. Word Ladder                        (Hard)   - Transform BFS           │
   * │  994. Rotting Oranges                    (Medium) - Multi-source BFS        │
   * │  286. Walls and Gates                    (Medium) - Multi-source BFS        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SLIDING WINDOW DEQUE (Monotonic Deque)                                      │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │  239. Sliding Window Maximum             (Hard)   - Max in window           │
   * │ 1438. Longest Subarray with Diff ≤ Limit (Medium) - Min-Max deque           │
   * │  862. Shortest Subarray with Sum ≥ K     (Hard)   - Monotonic deque + PS    │
   * │ 1696. Jump Game VI                       (Medium) - DP + deque optimization │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ PRIORITY QUEUE (Heap-based queue)                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │  215. Kth Largest Element                (Medium) - Quick select / heap     │
   * │  347. Top K Frequent Elements            (Medium) - Freq + heap             │
   * │  295. Find Median from Data Stream       (Hard)   - Two heaps               │
   * │  373. Find K Pairs with Smallest Sums    (Medium) - Heap + 2D search        │
   * │ 1046. Last Stone Weight                  (Easy)   - Simulation + heap       │
   * │  767. Reorganize String                  (Medium) - Greedy + heap           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
