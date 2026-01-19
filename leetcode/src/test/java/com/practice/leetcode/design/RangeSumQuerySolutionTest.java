package com.practice.leetcode.design;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RANGE SUM QUERY - Truy vấn tổng khoảng
 * 
 * Bài toán: Cho mảng nums, trả lời nhiều query sumRange(i, j).
 * 
 * Đây là bài cơ bản về Prefix Sum - kỹ thuật cực kỳ quan trọng!
 */
class RangeSumQuerySolutionTest {

  /**
   * THUẬT TOÁN: Prefix Sum (Mảng tổng tiền tố)
   * 
   * Ý tưởng:
   * - Tiền xử lý: prefix[i] = nums[0] + nums[1] + ... + nums[i-1]
   * - Query: sumRange(i, j) = prefix[j+1] - prefix[i]
   * 
   * Tại sao công thức này đúng?
   * prefix[j+1] = nums[0] + nums[1] + ... + nums[j]
   * prefix[i] = nums[0] + nums[1] + ... + nums[i-1]
   * Hiệu = nums[i] + nums[i+1] + ... + nums[j]
   * 
   * Ví dụ: nums = [-2, 0, 3, -5, 2, -1]
   * 
   * prefix = [0, -2, -2, 1, -4, -2, -3]
   * 0 1 2 3 4 5 6
   * 
   * sumRange(0, 2) = prefix[3] - prefix[0] = 1 - 0 = 1
   * = nums[0] + nums[1] + nums[2] = -2 + 0 + 3 = 1 ✓
   * 
   * sumRange(2, 5) = prefix[6] - prefix[2] = -3 - (-2) = -1
   * = 3 + (-5) + 2 + (-1) = -1 ✓
   * 
   * sumRange(0, 5) = prefix[6] - prefix[0] = -3 - 0 = -3
   * = -2 + 0 + 3 + (-5) + 2 + (-1) = -3 ✓
   * 
   * Time: O(n) cho khởi tạo, O(1) cho mỗi query
   * Space: O(n)
   */
  static class NumArray {
    private int[] prefix;

    public NumArray(int[] nums) {
      prefix = new int[nums.length + 1];
      for (int i = 0; i < nums.length; i++) {
        prefix[i + 1] = prefix[i] + nums[i];
      }
    }

    public int sumRange(int left, int right) {
      return prefix[right + 1] - prefix[left];
    }
  }

  /**
   * PHIÊN BẢN 2D: Range Sum Query 2D
   * 
   * Bài toán: Cho matrix, tính tổng hình chữ nhật [row1, col1] -> [row2, col2]
   * 
   * Ý tưởng tương tự: prefix[i][j] = tổng từ (0,0) đến (i-1, j-1)
   * 
   * Công thức tính tổng hình chữ nhật:
   * sum = prefix[r2+1][c2+1] - prefix[r1][c2+1] - prefix[r2+1][c1] +
   * prefix[r1][c1]
   * 
   * (Trừ đi 2 phần không cần, cộng lại phần bị trừ 2 lần)
   */
  static class NumMatrix {
    private int[][] prefix;

    public NumMatrix(int[][] matrix) {
      int m = matrix.length, n = matrix[0].length;
      prefix = new int[m + 1][n + 1];

      for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
          prefix[i][j] = prefix[i - 1][j] + prefix[i][j - 1]
              - prefix[i - 1][j - 1] + matrix[i - 1][j - 1];
        }
      }
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
      return prefix[row2 + 1][col2 + 1] - prefix[row1][col2 + 1]
          - prefix[row2 + 1][col1] + prefix[row1][col1];
    }
  }

  // ========== TESTS ==========

  @Test
  @DisplayName("1D Range Sum Query")
  void testNumArray() {
    NumArray arr = new NumArray(new int[] { -2, 0, 3, -5, 2, -1 });

    assertThat(arr.sumRange(0, 2)).isEqualTo(1);
    assertThat(arr.sumRange(2, 5)).isEqualTo(-1);
    assertThat(arr.sumRange(0, 5)).isEqualTo(-3);
  }

  @Test
  @DisplayName("2D Range Sum Query")
  void testNumMatrix() {
    int[][] matrix = {
        { 3, 0, 1, 4, 2 },
        { 5, 6, 3, 2, 1 },
        { 1, 2, 0, 1, 5 },
        { 4, 1, 0, 1, 7 },
        { 1, 0, 3, 0, 5 }
    };
    NumMatrix mat = new NumMatrix(matrix);

    assertThat(mat.sumRegion(2, 1, 4, 3)).isEqualTo(8);
    assertThat(mat.sumRegion(1, 1, 2, 2)).isEqualTo(11);
    assertThat(mat.sumRegion(1, 2, 2, 4)).isEqualTo(12);
  }
}
