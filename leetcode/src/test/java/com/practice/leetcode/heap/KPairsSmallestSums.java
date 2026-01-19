package com.practice.leetcode.heap;

import java.util.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║              373. Find K Pairs with Smallest Sums (Medium)                ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * Given: nums1, nums2 sorted, find k pairs (u,v) with smallest sums
 *
 * Example:
 * nums1 = [1,7,11], nums2 = [2,4,6], k = 3
 * Output: [[1,2],[1,4],[1,6]]
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * APPROACH: Min Heap + Smart Enumeration
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * KEY INSIGHT:
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ Nếu brute force tạo tất cả pairs: O(n*m) → quá chậm                         │
 * │                                                                             │
 * │ OBSERVATION:                                                                │
 * │ - nums1, nums2 đã sorted                                                    │
 * │ - pair(i,j) có sum = nums1[i] + nums2[j]                                    │
 * │ - pair(0,0) ALWAYS có smallest sum                                          │
 * │ - Sau pair(i,j), candidates tiếp theo là: (i+1,j) hoặc (i,j+1)              │
 * │                                                                             │
 * │ Giống như merge K sorted lists!                                             │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * VISUALIZATION:
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ nums1 = [1, 7, 11]                                                          │
 * │ nums2 = [2, 4, 6]                                                           │
 * │                                                                             │
 * │ Sum Matrix:                                                                 │
 * │          j=0   j=1   j=2                                                    │
 * │         [2]   [4]   [6]  ← nums2                                            │
 * │ i=0 [1]  3     5     7                                                      │
 * │ i=1 [7]  9    11    13                                                      │
 * │ i=2 [11] 13   15    17                                                      │
 * │     ↑                                                                       │
 * │   nums1                                                                     │
 * │                                                                             │
 * │ Mỗi row và column đều sorted!                                               │
 * │                                                                             │
 * │ Start: (0,0) = 3                                                            │
 * │ Pop 3 → add (1,0)=9, (0,1)=5                                                │
 * │ Pop 5 → add (1,1)=11, (0,2)=7   (nhưng (1,0) đã có)                          │
 * │ Pop 7 → add (1,2)=13            (nhưng (0,3) out of bounds)                  │
 * │ ...                                                                         │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * OPTIMIZED APPROACH:
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ Thay vì add cả (i+1,j) và (i,j+1), chỉ cần:                                 │
 * │                                                                             │
 * │ 1. Init heap với tất cả (i, 0) cho i từ 0 đến min(k, nums1.length)          │
 * │    → Đây là column đầu tiên                                                 │
 * │                                                                             │
 * │ 2. Pop min, add (i, j+1) vào heap                                           │
 * │    → Chỉ cần di chuyển sang phải trong mỗi row                              │
 * │                                                                             │
 * │ Không cần visited set!                                                      │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * STEP BY STEP:
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ nums1 = [1, 7, 11], nums2 = [2, 4, 6], k = 3                                │
 * │                                                                             │
 * │ Init heap: [(1+2, 0, 0), (7+2, 1, 0), (11+2, 2, 0)]                          │
 * │           = [(3, 0, 0), (9, 1, 0), (13, 2, 0)]                               │
 * │                                                                             │
 * │ Pop (3, 0, 0) → result: [[1,2]]                                             │
 * │ Add (0, 1): (1+4, 0, 1) = (5, 0, 1)                                          │
 * │ Heap: [(5, 0, 1), (9, 1, 0), (13, 2, 0)]                                     │
 * │                                                                             │
 * │ Pop (5, 0, 1) → result: [[1,2], [1,4]]                                       │
 * │ Add (0, 2): (1+6, 0, 2) = (7, 0, 2)                                          │
 * │ Heap: [(7, 0, 2), (9, 1, 0), (13, 2, 0)]                                     │
 * │                                                                             │
 * │ Pop (7, 0, 2) → result: [[1,2], [1,4], [1,6]] ✓ k=3 reached!                 │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * COMPLEXITY:
 * Time: O(k * log(min(k, n)))  - k extractions from heap of size min(k,n)
 * Space: O(min(k, n))          - heap size
 */
public class KPairsSmallestSums {

  /**
   * SOLUTION: Optimized Min Heap
   * - Chỉ init column đầu tiên
   * - Mỗi lần pop, add cell bên phải
   */
  public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
    List<List<Integer>> result = new ArrayList<>();

    if (nums1.length == 0 || nums2.length == 0 || k == 0) {
      return result;
    }

    // Min heap: [sum, index in nums1, index in nums2]
    // Java 8 compatible: so sánh theo sum
    PriorityQueue<int[]> minHeap = new PriorityQueue<>(
        (a, b) -> (a[0] - b[0])
    );

    // Init: Add first column (all pairs with nums2[0])
    // Chỉ cần add min(k, nums1.length) pairs vì không cần nhiều hơn k
    for (int i = 0; i < Math.min(k, nums1.length); i++) {
      // [sum, i, j]
      minHeap.offer(new int[]{nums1[i] + nums2[0], i, 0});
    }

    // Extract k smallest pairs
    while (!minHeap.isEmpty() && result.size() < k) {
      int[] current = minHeap.poll();
      int i = current[1];  // index in nums1
      int j = current[2];  // index in nums2

      // Add this pair to result
      result.add(Arrays.asList(nums1[i], nums2[j]));

      // Add next pair in this row: (i, j+1)
      boolean hasNextInRow = j + 1 < nums2.length;
      if (hasNextInRow) {
        minHeap.offer(new int[]{nums1[i] + nums2[j + 1], i, j + 1});
      }
    }

    return result;
  }

  /**
   * ALTERNATIVE: General approach with visited set
   * - Slower but more intuitive
   * - Có thể expand cả (i+1,j) và (i,j+1)
   */
  public List<List<Integer>> kSmallestPairsWithVisited(int[] nums1, int[] nums2, int k) {
    List<List<Integer>> result = new ArrayList<>();

    if (nums1.length == 0 || nums2.length == 0 || k == 0) {
      return result;
    }

    PriorityQueue<int[]> minHeap = new PriorityQueue<>(
        (a, b) -> (a[0] - b[0])
    );

    // Use visited set to avoid duplicates
    Set<String> visited = new HashSet<>();

    // Start with (0, 0)
    minHeap.offer(new int[]{nums1[0] + nums2[0], 0, 0});
    visited.add("0,0");

    while (!minHeap.isEmpty() && result.size() < k) {
      int[] current = minHeap.poll();
      int i = current[1];
      int j = current[2];

      result.add(Arrays.asList(nums1[i], nums2[j]));

      // Add (i+1, j) if not visited
      if (i + 1 < nums1.length && !visited.contains((i + 1) + "," + j)) {
        minHeap.offer(new int[]{nums1[i + 1] + nums2[j], i + 1, j});
        visited.add((i + 1) + "," + j);
      }

      // Add (i, j+1) if not visited
      if (j + 1 < nums2.length && !visited.contains(i + "," + (j + 1))) {
        minHeap.offer(new int[]{nums1[i] + nums2[j + 1], i, j + 1});
        visited.add(i + "," + (j + 1));
      }
    }

    return result;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEST
  // ═══════════════════════════════════════════════════════════════════════════
  public static void main(String[] args) {
    KPairsSmallestSums solution = new KPairsSmallestSums();

    // Test case 1
    int[] nums1 = {1, 7, 11};
    int[] nums2 = {2, 4, 6};
    int k = 3;
    System.out.println("Test 1: " + solution.kSmallestPairs(nums1, nums2, k));
    // Expected: [[1,2], [1,4], [1,6]]

    // Test case 2
    int[] nums1b = {1, 1, 2};
    int[] nums2b = {1, 2, 3};
    int kb = 2;
    System.out.println("Test 2: " + solution.kSmallestPairs(nums1b, nums2b, kb));
    // Expected: [[1,1], [1,1]]

    // Test case 3
    int[] nums1c = {1, 2};
    int[] nums2c = {3};
    int kc = 3;
    System.out.println("Test 3: " + solution.kSmallestPairs(nums1c, nums2c, kc));
    // Expected: [[1,3], [2,3]]
  }
}

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * SIMILAR PROBLEMS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Merge K Sorted Lists (LeetCode 23)
 *    - Same pattern: min heap để merge
 *
 * 2. Kth Smallest Element in Sorted Matrix (LeetCode 378)
 *    - Matrix sorted row & column
 *    - Same heap approach
 *
 * 3. Find K-th Smallest Pair Distance (LeetCode 719)
 *    - Binary search + two pointers
 *
 * 4. Ugly Number II (LeetCode 264)
 *    - Multiple pointers / heap approach
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * KEY TAKEAWAYS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Sorted arrays → explore incrementally
 * 2. Min heap cho "K smallest" problems
 * 3. Trick: Init với 1 dimension, expand other dimension
 * 4. Avoid visited set bằng cách chỉ expand một hướng
 * 5. Similar to BFS trên matrix với priority
 */
