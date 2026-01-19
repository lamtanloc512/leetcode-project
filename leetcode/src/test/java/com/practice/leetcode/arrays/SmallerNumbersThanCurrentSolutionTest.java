package com.practice.leetcode.arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ============================================================================
 * HOW MANY NUMBERS ARE SMALLER THAN THE CURRENT NUMBER
 * LeetCode 1365 - Easy
 * ============================================================================
 * 
 * Bài toán: Cho mảng nums, với mỗi nums[i] đếm có bao nhiêu số NHỎ HƠN nó.
 * 
 * Ví dụ:
 * Input:  nums = [8, 1, 2, 2, 3]
 * Output: [4, 0, 1, 1, 3]
 * Giải thích:
 * - 8: có 4 số nhỏ hơn (1, 2, 2, 3)
 * - 1: có 0 số nhỏ hơn
 * - 2: có 1 số nhỏ hơn (1)
 * - 3: có 3 số nhỏ hơn (1, 2, 2)
 * 
 * ============================================================================
 * 🎯 CÁCH NHẬN BIẾT BÀI DÙNG PREFIX SUM / COUNTING SORT
 * ============================================================================
 * 
 * Dấu hiệu nhận biết:
 * 1. Cần ĐẾM số phần tử thỏa điều kiện (nhỏ hơn, lớn hơn, trong khoảng)
 * 2. Giá trị phần tử nằm trong KHOẢNG NHỎ (0-100, 0-1000)
 *    → Hint: "0 <= nums[i] <= 100" → Dùng counting!
 * 3. Cần trả lời NHIỀU TRUY VẤN cùng lúc
 * 4. Brute force O(n²) → Có thể tối ưu O(n)
 * 
 * Câu hỏi tự hỏi:
 * - "Nếu biết có bao nhiêu số = X, tôi suy ra được gì?"
 * - "Có thể tiền xử lý để trả lời nhanh không?"
 * 
 * ============================================================================
 * 💡 THUẬT TOÁN: COUNTING + PREFIX SUM
 * ============================================================================
 * 
 * Bước 1: ĐẾM tần suất mỗi giá trị (Counting)
 * Bước 2: TÍNH PREFIX SUM để biết có bao nhiêu số < X
 * Bước 3: TRẢ LỜI từng phần tử
 * 
 * Ví dụ chi tiết: nums = [8, 1, 2, 2, 3]
 * 
 * Bước 1 - Đếm:
 * count[1] = 1 (có 1 số 1)
 * count[2] = 2 (có 2 số 2)
 * count[3] = 1 (có 1 số 3)
 * count[8] = 1 (có 1 số 8)
 * 
 * Bước 2 - Prefix Sum:
 * "Có bao nhiêu số < X?" = Tổng count[0] + count[1] + ... + count[X-1]
 * 
 * prefix[0] = 0                     (0 số < 0)
 * prefix[1] = 0                     (0 số < 1)
 * prefix[2] = 0 + 1 = 1             (1 số < 2, đó là số 1)
 * prefix[3] = 0 + 1 + 2 = 3         (3 số < 3, đó là 1, 2, 2)
 * prefix[4] = 0 + 1 + 2 + 1 = 4     (4 số < 4)
 * ...
 * prefix[8] = 4                     (4 số < 8)
 * 
 * Bước 3 - Trả lời:
 * nums[0] = 8 → prefix[8] = 4 ✓
 * nums[1] = 1 → prefix[1] = 0 ✓
 * nums[2] = 2 → prefix[2] = 1 ✓
 * nums[3] = 2 → prefix[2] = 1 ✓
 * nums[4] = 3 → prefix[3] = 3 ✓
 * 
 * ============================================================================
 * ⚡ COMPLEXITY
 * ============================================================================
 * Time:  O(n + k) với k = max value (ở đây k = 101)
 * Space: O(k)
 * 
 * So với brute force O(n²), tối ưu đáng kể khi n lớn!
 */
class SmallerNumbersThanCurrentSolutionTest {

  // ========================================================================
  // SOLUTION 1: COUNTING + PREFIX SUM (Optimal)
  // ========================================================================
  static class CountingSolution {
    public int[] smallerNumbersThanCurrent(int[] nums) {
      // Bước 1: Đếm tần suất (nums[i] trong [0, 100])
      int[] count = new int[101];
      for (int num : nums) {
        count[num]++;
      }

      // Bước 2: Tính prefix sum
      // count[i] sẽ chứa số lượng phần tử < i
      int total = 0;
      for (int i = 0; i < count.length; i++) {
        int current = count[i];
        count[i] = total;  // Số phần tử nhỏ hơn i
        total += current;  // Cộng dồn
      }

      // Bước 3: Trả lời
      int[] result = new int[nums.length];
      for (int i = 0; i < nums.length; i++) {
        result[i] = count[nums[i]];
      }

      return result;
    }
  }

  // ========================================================================
  // SOLUTION 2: BRUTE FORCE O(n²) - Để so sánh
  // ========================================================================
  static class BruteForceSolution {
    public int[] smallerNumbersThanCurrent(int[] nums) {
      int[] result = new int[nums.length];

      for (int i = 0; i < nums.length; i++) {
        int count = 0;
        for (int j = 0; j < nums.length; j++) {
          if (nums[j] < nums[i]) {
            count++;
          }
        }
        result[i] = count;
      }

      return result;
    }
  }

  // ========================================================================
  // SOLUTION 3: SORTING - Cách khác
  // ========================================================================
  static class SortingSolution {
    public int[] smallerNumbersThanCurrent(int[] nums) {
      int n = nums.length;
      int[][] pairs = new int[n][2]; // [value, originalIndex]

      for (int i = 0; i < n; i++) {
        pairs[i][0] = nums[i];
        pairs[i][1] = i;
      }

      // Sort theo value
      Arrays.sort(pairs, (a, b) -> a[0] - b[0]);

      int[] result = new int[n];
      for (int i = 0; i < n; i++) {
        // Nếu giá trị giống phần tử trước, copy kết quả
        if (i > 0 && pairs[i][0] == pairs[i - 1][0]) {
          result[pairs[i][1]] = result[pairs[i - 1][1]];
        } else {
          result[pairs[i][1]] = i; // Index trong sorted = số phần tử nhỏ hơn
        }
      }

      return result;
    }
  }

  // ========================================================================
  // 📝 SO SÁNH CÁC APPROACH
  // ========================================================================
  /*
   * | Approach     | Time      | Space   | Khi nào dùng?              |
   * |--------------|-----------|---------|----------------------------|
   * | Counting     | O(n + k)  | O(k)    | Giá trị nhỏ, biết trước k  |
   * | Sorting      | O(n logn) | O(n)    | Giá trị lớn, không giới hạn|
   * | Brute Force  | O(n²)     | O(1)    | Interview practice only!   |
   * 
   * Với bài này: nums[i] ∈ [0, 100] → Counting là optimal!
   */

  // ========================================================================
  // 🔗 BÀI TẬP LIÊN QUAN
  // ========================================================================
  /*
   * Các bài tương tự dùng Counting + Prefix Sum:
   * 
   * 1. Count of Smaller Numbers After Self (Hard)
   *    - Đếm số nhỏ hơn BÊN PHẢI (dùng Merge Sort / BIT)
   * 
   * 2. Range Sum Query
   *    - Tính tổng khoảng [i, j] với prefix sum
   * 
   * 3. Product of Array Except Self
   *    - Dùng prefix product và suffix product
   * 
   * 4. Find All Numbers Disappeared in an Array
   *    - Đếm để tìm số thiếu
   * 
   * 5. Relative Ranks
   *    - Gán rank dựa trên sorting/counting
   */

  // ========================================================================
  // TESTS
  // ========================================================================
  private final CountingSolution solution = new CountingSolution();

  @Test
  @DisplayName("Example 1: nums = [8,1,2,2,3]")
  void testExample1() {
    int[] result = solution.smallerNumbersThanCurrent(new int[] { 8, 1, 2, 2, 3 });
    assertThat(result).containsExactly(4, 0, 1, 1, 3);
  }

  @Test
  @DisplayName("Example 2: nums = [6,5,4,8]")
  void testExample2() {
    int[] result = solution.smallerNumbersThanCurrent(new int[] { 6, 5, 4, 8 });
    assertThat(result).containsExactly(2, 1, 0, 3);
  }

  @Test
  @DisplayName("Example 3: nums = [7,7,7,7]")
  void testAllEqual() {
    int[] result = solution.smallerNumbersThanCurrent(new int[] { 7, 7, 7, 7 });
    assertThat(result).containsExactly(0, 0, 0, 0);
  }

  @Test
  @DisplayName("Verify all solutions give same result")
  void testAllSolutions() {
    int[] nums = { 8, 1, 2, 2, 3 };

    int[] expected = { 4, 0, 1, 1, 3 };

    assertThat(new CountingSolution().smallerNumbersThanCurrent(nums.clone()))
        .containsExactly(expected);
    assertThat(new BruteForceSolution().smallerNumbersThanCurrent(nums.clone()))
        .containsExactly(expected);
    assertThat(new SortingSolution().smallerNumbersThanCurrent(nums.clone()))
        .containsExactly(expected);
  }
}
