package com.practice.leetcode.prefixsum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║              PREFIX SUM / PRODUCT / DIFFERENCE PATTERN GUIDE               ║
 * ╚════════════════════════════════════════════════════════════════════════════╝
 * 
 * Tài liệu này bao gồm:
 * 1. Template cho mỗi pattern
 * 2. Cách nhận biết khi nào dùng
 * 3. Các bài tập thực hành từ dễ đến khó
 */
public class PrefixSumPatternGuide {

  // ╔══════════════════════════════════════════════════════════════════════════╗
  // ║                    PATTERN 1: PREFIX SUM (Tổng tiền tố)                  ║
  // ╚══════════════════════════════════════════════════════════════════════════╝
  /**
   * 🎯 KHI NÀO DÙNG PREFIX SUM?
   * 
   * 1. Cần tính TỔNG khoảng [i, j] nhiều lần
   * 2. Cần đếm số phần tử thỏa điều kiện (kết hợp với counting)
   * 3. Tìm subarray có tổng bằng K
   * 4. Giá trị nằm trong khoảng nhỏ → dùng Counting + Prefix Sum
   * 
   * ⚡ Complexity: O(n) tiền xử lý, O(1) mỗi query
   * 
   * 📝 TEMPLATE:
   * 
   * // 1. Tạo prefix sum array (size n+1)
   * int[] prefix = new int[n + 1];
   * for (int i = 0; i < n; i++) {
   *     prefix[i + 1] = prefix[i] + nums[i];
   * }
   * 
   * // 2. Query tổng [i, j] (inclusive)
   * int sum = prefix[j + 1] - prefix[i];
   */
  @Nested
  @DisplayName("Pattern 1: Prefix Sum")
  class PrefixSumPattern {

    // ========================================================================
    // BÀI 1.1: Range Sum Query (LeetCode 303) - EASY
    // ========================================================================
    /**
     * Bài toán: Cho mảng nums, trả lời nhiều query sumRange(i, j)
     * 
     * Input: nums = [-2, 0, 3, -5, 2, -1]
     * sumRange(0, 2) = -2 + 0 + 3 = 1
     * sumRange(2, 5) = 3 + (-5) + 2 + (-1) = -1
     */
    class NumArray {
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

    @Test
    @DisplayName("1.1 Range Sum Query")
    void testRangeSumQuery() {
      NumArray arr = new NumArray(new int[] { -2, 0, 3, -5, 2, -1 });
      assertThat(arr.sumRange(0, 2)).isEqualTo(1);
      assertThat(arr.sumRange(2, 5)).isEqualTo(-1);
      assertThat(arr.sumRange(0, 5)).isEqualTo(-3);
    }

    // ========================================================================
    // BÀI 1.2: Subarray Sum Equals K (LeetCode 560) - MEDIUM
    // ========================================================================
    /**
     * Bài toán: Đếm số subarray có tổng = k
     * 
     * Input: nums = [1, 1, 1], k = 2
     * Output: 2 (subarray [1,1] xuất hiện 2 lần)
     * 
     * 💡 Ý TƯỞNG:
     * - Nếu prefix[j] - prefix[i] = k → subarray [i, j-1] có tổng k
     * - Tương đương: prefix[i] = prefix[j] - k
     * - Dùng HashMap đếm số lần xuất hiện của mỗi prefix sum
     * 
     * 🔑 KEY INSIGHT:
     * Với mỗi prefix[j], đếm có bao nhiêu prefix[i] = prefix[j] - k
     */
    public int subarraySum(int[] nums, int k) {
      Map<Integer, Integer> prefixCount = new HashMap<>();
      prefixCount.put(0, 1); // Empty prefix có sum = 0

      int count = 0;
      int currentSum = 0;

      for (int num : nums) {
        currentSum += num;

        // Có bao nhiêu prefix sum = currentSum - k?
        count += prefixCount.getOrDefault(currentSum - k, 0);

        // Lưu prefix sum hiện tại
        prefixCount.merge(currentSum, 1, Integer::sum);
      }

      return count;
    }

    @Test
    @DisplayName("1.2 Subarray Sum Equals K")
    void testSubarraySum() {
      assertThat(subarraySum(new int[] { 1, 1, 1 }, 2)).isEqualTo(2);
      assertThat(subarraySum(new int[] { 1, 2, 3 }, 3)).isEqualTo(2); // [1,2] và [3]
      assertThat(subarraySum(new int[] { 1, -1, 0 }, 0)).isEqualTo(3); // [1,-1], [-1,0], [1,-1,0]
    }

    // ========================================================================
    // BÀI 1.3: Contiguous Array (LeetCode 525) - MEDIUM
    // ========================================================================
    /**
     * Bài toán: Tìm subarray dài nhất có số 0 và 1 bằng nhau
     * 
     * Input: nums = [0, 1, 0]
     * Output: 2 ([0, 1] hoặc [1, 0])
     * 
     * 💡 TRICK: Thay 0 bằng -1, tìm subarray có tổng = 0
     * [0, 1, 0] → [-1, 1, -1]
     * Prefix: [0, -1, 0, -1]
     * prefix[0] = prefix[2] = 0 → subarray [0,1] có tổng 0
     */
    public int findMaxLength(int[] nums) {
      Map<Integer, Integer> firstIndex = new HashMap<>();
      firstIndex.put(0, -1); // Sum 0 xảy ra trước index 0

      int maxLen = 0;
      int sum = 0;

      for (int i = 0; i < nums.length; i++) {
        sum += (nums[i] == 0 ? -1 : 1);

        if (firstIndex.containsKey(sum)) {
          maxLen = Math.max(maxLen, i - firstIndex.get(sum));
        } else {
          firstIndex.put(sum, i);
        }
      }

      return maxLen;
    }

    @Test
    @DisplayName("1.3 Contiguous Array")
    void testContiguousArray() {
      assertThat(findMaxLength(new int[] { 0, 1 })).isEqualTo(2);
      assertThat(findMaxLength(new int[] { 0, 1, 0 })).isEqualTo(2);
      assertThat(findMaxLength(new int[] { 0, 1, 0, 1 })).isEqualTo(4);
    }
  }

  // ╔══════════════════════════════════════════════════════════════════════════╗
  // ║                  PATTERN 2: PREFIX PRODUCT (Tích tiền tố)                ║
  // ╚══════════════════════════════════════════════════════════════════════════╝
  /**
   * 🎯 KHI NÀO DÙNG PREFIX PRODUCT?
   * 
   * 1. Cần tính tích loại trừ phần tử hiện tại
   * 2. Cần tỉ lệ/chia các phần tử
   * 
   * ⚠️ LƯU Ý: Phải xử lý số 0 cẩn thận!
   * 
   * 📝 TEMPLATE:
   * 
   * // Prefix product từ trái
   * int[] leftProduct = new int[n];
   * leftProduct[0] = 1;
   * for (int i = 1; i < n; i++) {
   *     leftProduct[i] = leftProduct[i-1] * nums[i-1];
   * }
   * 
   * // Suffix product từ phải
   * int[] rightProduct = new int[n];
   * rightProduct[n-1] = 1;
   * for (int i = n-2; i >= 0; i--) {
   *     rightProduct[i] = rightProduct[i+1] * nums[i+1];
   * }
   * 
   * // Kết quả = trái × phải
   * result[i] = leftProduct[i] * rightProduct[i];
   */
  @Nested
  @DisplayName("Pattern 2: Prefix Product")
  class PrefixProductPattern {

    // ========================================================================
    // BÀI 2.1: Product of Array Except Self (LeetCode 238) - MEDIUM
    // ========================================================================
    /**
     * Bài toán: Với mỗi i, tính tích tất cả phần tử NGOẠI TRỪ nums[i]
     * KHÔNG được dùng phép chia!
     * 
     * Input: nums = [1, 2, 3, 4]
     * Output: [24, 12, 8, 6]
     * - result[0] = 2×3×4 = 24
     * - result[1] = 1×3×4 = 12
     * - result[2] = 1×2×4 = 8
     * - result[3] = 1×2×3 = 6
     * 
     * 💡 Ý TƯỞNG:
     * result[i] = (tích bên trái) × (tích bên phải)
     * 
     * leftProduct:  [1,  1,  2,  6 ]  (tích từ đầu đến i-1)
     * rightProduct: [24, 12, 4,  1 ]  (tích từ i+1 đến cuối)
     * result:       [24, 12, 8,  6 ]
     */
    public int[] productExceptSelf(int[] nums) {
      int n = nums.length;
      int[] result = new int[n];

      // Pass 1: Tính prefix product từ trái
      result[0] = 1;
      for (int i = 1; i < n; i++) {
        result[i] = result[i - 1] * nums[i - 1];
      }

      // Pass 2: Nhân với suffix product từ phải
      int rightProduct = 1;
      for (int i = n - 1; i >= 0; i--) {
        result[i] *= rightProduct;
        rightProduct *= nums[i];
      }

      return result;
    }

    @Test
    @DisplayName("2.1 Product of Array Except Self")
    void testProductExceptSelf() {
      assertThat(productExceptSelf(new int[] { 1, 2, 3, 4 }))
          .containsExactly(24, 12, 8, 6);
      assertThat(productExceptSelf(new int[] { -1, 1, 0, -3, 3 }))
          .containsExactly(0, 0, 9, 0, 0);
    }

    // ========================================================================
    // BÀI 2.2: Trapping Rain Water (LeetCode 42) - HARD
    // ========================================================================
    /**
     * Bài toán: Tính lượng nước mưa có thể đọng lại
     * 
     * Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
     * Output: 6
     * 
     *             █
     *     █ ~ ~ ~ █ █ ~ █
     * ~ ~ █ █ ~ █ █ █ █ █ █ ~
     * ─────────────────────────
     * 
     * 💡 Ý TƯỞNG:
     * Nước tại i = min(maxLeft, maxRight) - height[i]
     * 
     * Dùng prefix max từ trái, suffix max từ phải
     */
    public int trap(int[] height) {
      int n = height.length;
      if (n == 0)
        return 0;

      // Prefix max từ trái
      int[] leftMax = new int[n];
      leftMax[0] = height[0];
      for (int i = 1; i < n; i++) {
        leftMax[i] = Math.max(leftMax[i - 1], height[i]);
      }

      // Suffix max từ phải
      int[] rightMax = new int[n];
      rightMax[n - 1] = height[n - 1];
      for (int i = n - 2; i >= 0; i--) {
        rightMax[i] = Math.max(rightMax[i + 1], height[i]);
      }

      // Tính nước
      int water = 0;
      for (int i = 0; i < n; i++) {
        water += Math.min(leftMax[i], rightMax[i]) - height[i];
      }

      return water;
    }

    @Test
    @DisplayName("2.2 Trapping Rain Water")
    void testTrap() {
      assertThat(trap(new int[] { 0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1 }))
          .isEqualTo(6);
      assertThat(trap(new int[] { 4, 2, 0, 3, 2, 5 }))
          .isEqualTo(9);
    }
  }

  // ╔══════════════════════════════════════════════════════════════════════════╗
  // ║               PATTERN 3: DIFFERENCE ARRAY (Mảng hiệu)                    ║
  // ╚══════════════════════════════════════════════════════════════════════════╝
  /**
   * 🎯 KHI NÀO DÙNG DIFFERENCE ARRAY?
   * 
   * 1. Cần CỘNG một giá trị vào KHOẢNG [i, j] nhiều lần
   * 2. Bài toán có interval với start/end
   * 3. Lên xe xuống xe, booking room, flight...
   * 
   * 📝 TEMPLATE:
   * 
   * // Thêm value vào khoảng [start, end]
   * diff[start] += value;
   * diff[end + 1] -= value;  // Hoàn tác sau khoảng
   * 
   * // Khôi phục mảng gốc bằng prefix sum
   * for (int i = 1; i < n; i++) {
   *     diff[i] += diff[i - 1];
   * }
   * 
   * 💡 NGUYÊN LÝ:
   * diff là "đạo hàm" của mảng gốc
   * prefix sum là "tích phân" để khôi phục
   */
  @Nested
  @DisplayName("Pattern 3: Difference Array")
  class DifferenceArrayPattern {

    // ========================================================================
    // BÀI 3.1: Car Pooling (LeetCode 1094) - MEDIUM
    // ========================================================================
    /**
     * Bài toán: Xe có capacity chỗ. trips[i] = [passengers, from, to]
     * Hỏi có chở được hết không?
     * 
     * Input: trips = [[2,1,5], [3,3,7]], capacity = 4
     * Output: false (tại điểm 3, có 2+3=5 > 4)
     */
    public boolean carPooling(int[][] trips, int capacity) {
      int[] diff = new int[1001]; // to <= 1000

      for (int[] trip : trips) {
        int passengers = trip[0];
        int from = trip[1];
        int to = trip[2];

        diff[from] += passengers; // Lên xe
        diff[to] -= passengers; // Xuống xe (không đi đến to)
      }

      // Prefix sum để tính số người trên xe
      int current = 0;
      for (int change : diff) {
        current += change;
        if (current > capacity)
          return false;
      }

      return true;
    }

    @Test
    @DisplayName("3.1 Car Pooling")
    void testCarPooling() {
      assertThat(carPooling(new int[][] { { 2, 1, 5 }, { 3, 3, 7 } }, 4)).isFalse();
      assertThat(carPooling(new int[][] { { 2, 1, 5 }, { 3, 3, 7 } }, 5)).isTrue();
      assertThat(carPooling(new int[][] { { 2, 1, 5 }, { 3, 5, 7 } }, 3)).isTrue();
    }

    // ========================================================================
    // BÀI 3.2: Range Addition (LeetCode 370) - MEDIUM
    // ========================================================================
    /**
     * Bài toán: Mảng length=n khởi tạo 0.
     * Thực hiện nhiều operations [start, end, inc]: cộng inc vào [start, end]
     * Trả về mảng cuối cùng.
     * 
     * Input: length = 5, updates = [[1,3,2], [2,4,3], [0,2,-2]]
     * 
     * [0, 0, 0, 0, 0] → +2 [1,3] → [0, 2, 2, 2, 0]
     *                  → +3 [2,4] → [0, 2, 5, 5, 3]
     *                  → -2 [0,2] → [-2, 0, 3, 5, 3]
     */
    public int[] getModifiedArray(int length, int[][] updates) {
      int[] diff = new int[length];

      for (int[] update : updates) {
        int start = update[0];
        int end = update[1];
        int inc = update[2];

        diff[start] += inc;
        if (end + 1 < length) {
          diff[end + 1] -= inc;
        }
      }

      // Prefix sum để khôi phục
      for (int i = 1; i < length; i++) {
        diff[i] += diff[i - 1];
      }

      return diff;
    }

    @Test
    @DisplayName("3.2 Range Addition")
    void testRangeAddition() {
      int[][] updates = { { 1, 3, 2 }, { 2, 4, 3 }, { 0, 2, -2 } };
      assertThat(getModifiedArray(5, updates))
          .containsExactly(-2, 0, 3, 5, 3);
    }

    // ========================================================================
    // BÀI 3.3: Corporate Flight Bookings (LeetCode 1109) - MEDIUM
    // ========================================================================
    /**
     * Bài toán: Có n chuyến bay. bookings[i] = [first, last, seats]
     * Trả về số ghế đã book của mỗi chuyến.
     * 
     * Input: bookings = [[1,2,10], [2,3,20], [2,5,25]], n = 5
     * Output: [10, 55, 45, 25, 25]
     * 
     * Flight 1: 10 seats
     * Flight 2: 10 + 20 + 25 = 55 seats
     * ...
     */
    public int[] corpFlightBookings(int[][] bookings, int n) {
      int[] diff = new int[n + 2]; // +2 để tránh out of bounds

      for (int[] booking : bookings) {
        int first = booking[0];
        int last = booking[1];
        int seats = booking[2];

        diff[first] += seats;
        diff[last + 1] -= seats;
      }

      // Prefix sum
      int[] result = new int[n];
      int current = 0;
      for (int i = 1; i <= n; i++) {
        current += diff[i];
        result[i - 1] = current;
      }

      return result;
    }

    @Test
    @DisplayName("3.3 Corporate Flight Bookings")
    void testFlightBookings() {
      int[][] bookings = { { 1, 2, 10 }, { 2, 3, 20 }, { 2, 5, 25 } };
      assertThat(corpFlightBookings(bookings, 5))
          .containsExactly(10, 55, 45, 25, 25);
    }
  }

  // ╔══════════════════════════════════════════════════════════════════════════╗
  // ║                    PATTERN 4: 2D PREFIX SUM                              ║
  // ╚══════════════════════════════════════════════════════════════════════════╝
  /**
   * 🎯 KHI NÀO DÙNG 2D PREFIX SUM?
   * 
   * 1. Tính tổng hình chữ nhật trong matrix
   * 2. Đếm phần tử trong vùng 2D
   * 
   * 📝 TEMPLATE:
   * 
   * // Xây dựng 2D prefix sum
   * prefix[i][j] = tổng từ (0,0) đến (i-1, j-1)
   * 
   * prefix[i][j] = prefix[i-1][j] + prefix[i][j-1]
   *              - prefix[i-1][j-1] + matrix[i-1][j-1]
   * 
   * // Query (r1, c1) → (r2, c2)
   * sum = prefix[r2+1][c2+1] - prefix[r1][c2+1]
   *     - prefix[r2+1][c1] + prefix[r1][c1]
   * 
   * 💡 HÌNH ẢNH HÓA:
   *    ┌────────┬────────┐
   *    │   A    │   B    │
   *    ├────────┼────────┤
   *    │   C    │ WANTED │
   *    └────────┴────────┘
   *    WANTED = Total - A - C + (phần A∩C bị trừ 2 lần)
   */
  @Nested
  @DisplayName("Pattern 4: 2D Prefix Sum")
  class TwoDPrefixSumPattern {

    // ========================================================================
    // BÀI 4.1: Range Sum Query 2D (LeetCode 304) - MEDIUM
    // ========================================================================
    class NumMatrix {
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

      public int sumRegion(int r1, int c1, int r2, int c2) {
        return prefix[r2 + 1][c2 + 1] - prefix[r1][c2 + 1]
            - prefix[r2 + 1][c1] + prefix[r1][c1];
      }
    }

    @Test
    @DisplayName("4.1 Range Sum Query 2D")
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
    }
  }

  // ╔══════════════════════════════════════════════════════════════════════════╗
  // ║                         TÓM TẮT & CHEAT SHEET                            ║
  // ╚══════════════════════════════════════════════════════════════════════════╝
  /*
   * ┌─────────────────┬──────────────────────────────────────────────────────┐
   * │    Pattern      │              Dấu hiệu nhận biết                      │
   * ├─────────────────┼──────────────────────────────────────────────────────┤
   * │ Prefix Sum      │ "Tổng từ i đến j", "đếm số phần tử", "subarray sum"  │
   * ├─────────────────┼──────────────────────────────────────────────────────┤
   * │ Prefix Product  │ "Tích ngoại trừ", "max trái/phải", không dùng chia   │
   * ├─────────────────┼──────────────────────────────────────────────────────┤
   * │ Difference Array│ "Cộng vào khoảng [i,j]", interval, booking, xe buýt  │
   * ├─────────────────┼──────────────────────────────────────────────────────┤
   * │ 2D Prefix Sum   │ "Tổng hình chữ nhật", matrix, grid                   │
   * └─────────────────┴──────────────────────────────────────────────────────┘
   * 
   * 🔑 KEY RELATIONSHIPS:
   * - Difference Array ←→ Prefix Sum (đạo hàm ←→ tích phân)
   * - prefix[j] - prefix[i] = sum(i, j-1)
   * - Counting + Prefix Sum = Đếm số phần tử thỏa điều kiện
   * 
   * 💪 PRACTICE ORDER:
   * 1. Range Sum Query (303) - Hiểu cơ bản
   * 2. Smaller Numbers Than Current (1365) - Counting + Prefix
   * 3. Subarray Sum Equals K (560) - HashMap + Prefix
   * 4. Product Except Self (238) - Prefix/Suffix product
   * 5. Car Pooling (1094) - Difference array
   * 6. Trapping Rain Water (42) - Prefix max
   * 7. Range Sum Query 2D (304) - 2D extension
   */
}
