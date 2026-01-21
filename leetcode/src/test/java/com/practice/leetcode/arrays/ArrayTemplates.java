package com.practice.leetcode.arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ ARRAY TEMPLATES                                                           ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class ArrayTemplates {

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 1: PREFIX SUM (1D)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Range Sum Query, Subarray Sum Equals K
  public void solvePrefixSum(int[] nums) {
    int n = nums.length;
    int[] prefix = new int[n + 1];

    // Build Prefix Array
    for (int i = 0; i < n; i++) {
      prefix[i + 1] = prefix[i] + nums[i];
    }

    // Query sum(i, j) = prefix[j+1] - prefix[i]
    // Ví dụ: Sum từ index 1 đến 3 -> prefix[4] - prefix[1]
  }

  // Prefix Sum + HashMap (Subarray Sum equals K)
  public int subarraySum(int[] nums, int k) {
    int count = 0, sum = 0;
    Map<Integer, Integer> map = new HashMap<>(); // map<prefixSum, frequency>
    map.put(0, 1); // Base case: sum 0 xuất hiện 1 lần

    for (int num : nums) {
      sum += num;
      if (map.containsKey(sum - k)) {
        count += map.get(sum - k);
      }
      map.put(sum, map.getOrDefault(sum, 0) + 1);
    }
    return count;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 2: SLIDING WINDOW (FIXED SIZE)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Max Sum Subarray of size K
  public int solveFixedSlidingWindow(int[] nums, int k) {
    int currentSum = 0;
    int maxSum = Integer.MIN_VALUE;

    // Init window
    for (int i = 0; i < nums.length; i++) {
      currentSum += nums[i];

      // Khi cửa sổ đủ k phần tử
      if (i >= k - 1) {
        maxSum = Math.max(maxSum, currentSum);
        // Trượt: loại bỏ phần tử đầu tiên của cửa sổ cũ
        currentSum -= nums[i - (k - 1)];
      }
    }
    return maxSum;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 3: SLIDING WINDOW (DYNAMIC SIZE)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Longest Subarray with condition, Min Subarray Len
  public int solveDynamicSlidingWindow(int[] nums, int target) {
    int left = 0, currentSum = 0;
    int minLength = Integer.MAX_VALUE;

    for (int right = 0; right < nums.length; right++) {
      // 1. Expand window
      currentSum += nums[right];

      // 2. Shrink window (khi vi phạm đk hoặc tìm tối ưu)
      while (currentSum >= target) {
        minLength = Math.min(minLength, right - left + 1);
        currentSum -= nums[left];
        left++;
      }
    }
    return minLength == Integer.MAX_VALUE ? 0 : minLength;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 4: 2D MATRIX TRAVERSAL
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Island problems, Grid paths
  public void solveMatrixDFS(char[][] grid) {
    int rows = grid.length;
    int cols = grid[0].length;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (grid[i][j] == '1') { // Found land
          dfs(grid, i, j);
        }
      }
    }
  }

  private void dfs(char[][] grid, int r, int c) {
    int rows = grid.length;
    int cols = grid[0].length;

    // Boundary check + Visited check
    if (r < 0 || c < 0 || r >= rows || c >= cols || grid[r][c] == '0') {
      return;
    }

    grid[r][c] = '0'; // Mark visited

    // Visit 4 directions
    dfs(grid, r + 1, c);
    dfs(grid, r - 1, c);
    dfs(grid, r, c + 1);
    dfs(grid, r, c - 1);
  }
}
