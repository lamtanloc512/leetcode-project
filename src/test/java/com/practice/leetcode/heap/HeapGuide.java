package com.practice.leetcode.heap;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ HEAP / PRIORITY QUEUE GUIDE ║
 * ║ (Đống / Hàng đợi ưu tiên) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ HEAP là gì? │
 * │ │
 * │ - Cấu trúc dữ liệu cây binary đặc biệt │
 * │ - MIN HEAP: Parent <= Children (root là MIN) │
 * │ - MAX HEAP: Parent >= Children (root là MAX) │
 * │ │
 * │ Operations: │
 * │ - offer()/add(): O(log n) │
 * │ - poll(): O(log n) - lấy và xóa root │
 * │ - peek(): O(1) - xem root │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * JAVA PRIORITY QUEUE
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * // Min Heap (default)
 * PriorityQueue<Integer> minHeap = new PriorityQueue<>();
 *
 * // Max Heap
 * PriorityQueue<Integer> maxHeap = new
 * PriorityQueue<>(Collections.reverseOrder());
 * // hoặc
 * PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
 *
 * // Custom comparison
 * PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
 */
public class HeapGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 1: TOP K ELEMENTS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán: Tìm K phần tử lớn nhất/nhỏ nhất
   *
   * Trick QUAN TRỌNG:
   * - Tìm K LỚN NHẤT → Dùng MIN HEAP size K
   * - Tìm K NHỎ NHẤT → Dùng MAX HEAP size K
   *
   * Tại sao? Giữ heap size = K, phần tử không đủ tốt sẽ bị loại
   *
   * Time: O(n log k) - tốt hơn O(n log n) khi k << n
   */
  int findKthLargest(int[] nums, int k) {
    // Min heap size k - root là phần tử nhỏ nhất trong top k
    java.util.PriorityQueue<Integer> minHeap = new java.util.PriorityQueue<>();

    for (int num : nums) {
      minHeap.offer(num);

      if (minHeap.size() > k) {
        minHeap.poll(); // Loại phần tử nhỏ nhất
      }
    }

    return minHeap.peek(); // Kth largest = smallest trong top k
  }

  // Top K Frequent Elements
  int[] topKFrequent(int[] nums, int k) {
    // Đếm frequency
    java.util.Map<Integer, Integer> freq = new java.util.HashMap<>();
    for (int num : nums) {
      freq.merge(num, 1, Integer::sum);
    }

    // Min heap theo frequency
    java.util.PriorityQueue<Integer> minHeap = new java.util.PriorityQueue<>((a, b) -> freq.get(a) - freq.get(b));

    for (int num : freq.keySet()) {
      minHeap.offer(num);
      if (minHeap.size() > k) {
        minHeap.poll();
      }
    }

    return minHeap.stream().mapToInt(x -> x).toArray();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 2: MERGE K SORTED
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán: Merge k sorted lists/arrays
   *
   * Ý tưởng: Heap lưu head của mỗi list, luôn lấy min
   *
   * Time: O(n log k) với n = tổng số elements
   */
  // Giả sử ListNode đã định nghĩa
  static class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
      this.val = val;
    }
  }

  ListNode mergeKLists(ListNode[] lists) {
    if (lists == null || lists.length == 0)
      return null;

    // Min heap theo node.val
    java.util.PriorityQueue<ListNode> minHeap = new java.util.PriorityQueue<>((a, b) -> a.val - b.val);

    // Thêm head của mỗi list
    for (ListNode head : lists) {
      if (head != null) {
        minHeap.offer(head);
      }
    }

    ListNode dummy = new ListNode(0);
    ListNode tail = dummy;

    while (!minHeap.isEmpty()) {
      ListNode node = minHeap.poll();
      tail.next = node;
      tail = tail.next;

      if (node.next != null) {
        minHeap.offer(node.next);
      }
    }

    return dummy.next;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 3: TWO HEAPS (Median)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán: Find Median from Data Stream
   *
   * Ý tưởng: Chia stream thành 2 nửa
   * - maxHeap: chứa nửa NHỎ (root = max của nửa nhỏ)
   * - minHeap: chứa nửa LỚN (root = min của nửa lớn)
   *
   * Median = maxHeap.peek() hoặc avg của 2 roots
   *
   * Invariant: |maxHeap.size() - minHeap.size()| <= 1
   */
  static class MedianFinder {
    private java.util.PriorityQueue<Integer> maxHeap; // Nửa nhỏ
    private java.util.PriorityQueue<Integer> minHeap; // Nửa lớn

    MedianFinder() {
      maxHeap = new java.util.PriorityQueue<>((a, b) -> b - a);
      minHeap = new java.util.PriorityQueue<>();
    }

    void addNum(int num) {
      // Luôn thêm vào maxHeap trước
      maxHeap.offer(num);

      // Chuyển max của maxHeap sang minHeap
      minHeap.offer(maxHeap.poll());

      // Balance: maxHeap có thể lớn hơn 1 phần tử
      if (minHeap.size() > maxHeap.size()) {
        maxHeap.offer(minHeap.poll());
      }
    }

    double findMedian() {
      if (maxHeap.size() > minHeap.size()) {
        return maxHeap.peek();
      }
      return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 4: SCHEDULING / SIMULATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Dùng cho: Task scheduling, simulation theo thời gian
   *
   * Ví dụ: Last Stone Weight
   * - Mỗi turn, lấy 2 đá nặng nhất
   * - Đập vào nhau, còn lại = |diff|
   */
  int lastStoneWeight(int[] stones) {
    // Max heap
    java.util.PriorityQueue<Integer> maxHeap = new java.util.PriorityQueue<>((a, b) -> b - a);

    for (int stone : stones) {
      maxHeap.offer(stone);
    }

    while (maxHeap.size() > 1) {
      int first = maxHeap.poll();
      int second = maxHeap.poll();

      if (first != second) {
        maxHeap.offer(first - second);
      }
    }

    return maxHeap.isEmpty() ? 0 : maxHeap.peek();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BẢNG CHỌN HEAP
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌──────────────────────────────────────────────────────────────────────────┐
   * │ Loại bài │ Heap Type │ Size │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Kth largest │ Min Heap │ K │
   * │ Kth smallest │ Max Heap │ K │
   * │ Top K frequent │ Min Heap (freq) │ K │
   * │ Merge K sorted │ Min Heap │ K │
   * │ Running median │ 2 Heaps │ n/2 each │
   * │ Task scheduler │ Max Heap │ varies │
   * │ K closest points │ Max Heap (dist) │ K │
   * └──────────────────────────────────────────────────────────────────────────┘
   *
   *
   * CÁC BÀI THỰC HÀNH
   *
   * EASY:
   * - Last Stone Weight (1046)
   * - Kth Largest Element in Stream (703)
   *
   * MEDIUM:
   * - Kth Largest Element in Array (215)
   * - Top K Frequent Elements (347)
   * - K Closest Points to Origin (973)
   * - Task Scheduler (621)
   *
   * HARD:
   * - Merge K Sorted Lists (23)
   * - Find Median from Data Stream (295)
   * - Sliding Window Median (480)
   */
}
