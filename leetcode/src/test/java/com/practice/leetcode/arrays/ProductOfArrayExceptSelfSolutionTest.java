package com.practice.leetcode.arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Product of Array Except Self problem.
 */
class ProductOfArrayExceptSelfSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Prefix-Suffix Product (Tích tiền tố và hậu tố)
   *
   * Bài toán: Tính tích tất cả phần tử TRỪ phần tử hiện tại, KHÔNG dùng phép
   * chia.
   *
   * Ý tưởng: result[i] = (tích các phần tử bên trái) × (tích các phần tử bên
   * phải)
   *
   * Ví dụ: nums = [1, 2, 3, 4]
   *
   * Prefix (tích từ trái):
   * prefix[0] = 1 (không có phần tử bên trái)
   * prefix[1] = 1 = nums[0]
   * prefix[2] = 1×2 = nums[0]×nums[1]
   * prefix[3] = 1×2×3 = nums[0]×nums[1]×nums[2]
   * → [1, 1, 2, 6]
   *
   * Suffix (tích từ phải):
   * suffix[3] = 1 (không có phần tử bên phải)
   * suffix[2] = 4 = nums[3]
   * suffix[1] = 4×3 = nums[3]×nums[2]
   * suffix[0] = 4×3×2 = nums[3]×nums[2]×nums[1]
   * → [24, 12, 4, 1]
   *
   * Kết quả: prefix[i] × suffix[i]
   * → [1×24, 1×12, 2×4, 6×1] = [24, 12, 8, 6]
   *
   * Tối ưu: Dùng 1 pass cho prefix lưu vào result, 1 pass cho suffix nhân trực
   * tiếp.
   *
   * Time: O(n) - 2 lần duyệt
   * Space: O(1) - không tính output array
   */
  static class ImperativeSolution {
    public int[] productExceptSelf(int[] nums) {
      int n = nums.length;
      int[] result = new int[n];

      // First pass: calculate prefix products
      result[0] = 1;
      for (int i = 1; i < n; i++) {
        result[i] = result[i - 1] * nums[i - 1];
      }

      // Second pass: multiply by suffix products
      int suffix = 1;
      for (int i = n - 1; i >= 0; i--) {
        result[i] *= suffix;
        suffix *= nums[i];
      }

      return result;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Uses streams to compute prefix and suffix products.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] productExceptSelf(int[] nums) {
      int n = nums.length;

      // Calculate prefix products
      int[] prefix = new int[n];
      prefix[0] = 1;
      IntStream.range(1, n)
          .forEach(i -> prefix[i] = prefix[i - 1] * nums[i - 1]);

      // Calculate suffix products
      int[] suffix = new int[n];
      suffix[n - 1] = 1;
      for (int i = n - 2; i >= 0; i--) {
        suffix[i] = suffix[i + 1] * nums[i + 1];
      }

      // Combine prefix and suffix
      return IntStream.range(0, n)
          .map(i -> prefix[i] * suffix[i])
          .toArray();
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [1,2,3,4] -> [24,12,8,6]")
    void testExample1() {
      int[] nums = { 1, 2, 3, 4 };
      assertThat(imperativeSolution.productExceptSelf(nums))
          .containsExactly(24, 12, 8, 6);
    }

    @Test
    @DisplayName("Example 2: [-1,1,0,-3,3] -> [0,0,9,0,0]")
    void testExample2() {
      int[] nums = { -1, 1, 0, -3, 3 };
      assertThat(imperativeSolution.productExceptSelf(nums))
          .containsExactly(0, 0, 9, 0, 0);
    }

    @Test
    @DisplayName("Two elements: [2,3] -> [3,2]")
    void testTwoElements() {
      int[] nums = { 2, 3 };
      assertThat(imperativeSolution.productExceptSelf(nums))
          .containsExactly(3, 2);
    }

    @Test
    @DisplayName("Array with ones: [1,1,1,1] -> [1,1,1,1]")
    void testAllOnes() {
      int[] nums = { 1, 1, 1, 1 };
      assertThat(imperativeSolution.productExceptSelf(nums))
          .containsExactly(1, 1, 1, 1);
    }

    @Test
    @DisplayName("Negative numbers: [-2,3,-4] -> [-12,8,-6]")
    void testNegativeNumbers() {
      int[] nums = { -2, 3, -4 };
      assertThat(imperativeSolution.productExceptSelf(nums))
          .containsExactly(-12, 8, -6);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [1,2,3,4] -> [24,12,8,6]")
    void testExample1() {
      int[] nums = { 1, 2, 3, 4 };
      assertThat(functionalSolution.productExceptSelf(nums))
          .containsExactly(24, 12, 8, 6);
    }

    @Test
    @DisplayName("Example 2: [-1,1,0,-3,3] -> [0,0,9,0,0]")
    void testExample2() {
      int[] nums = { -1, 1, 0, -3, 3 };
      assertThat(functionalSolution.productExceptSelf(nums))
          .containsExactly(0, 0, 9, 0, 0);
    }

    @Test
    @DisplayName("Two elements: [2,3] -> [3,2]")
    void testTwoElements() {
      int[] nums = { 2, 3 };
      assertThat(functionalSolution.productExceptSelf(nums))
          .containsExactly(3, 2);
    }

    @Test
    @DisplayName("Array with ones: [1,1,1,1] -> [1,1,1,1]")
    void testAllOnes() {
      int[] nums = { 1, 1, 1, 1 };
      assertThat(functionalSolution.productExceptSelf(nums))
          .containsExactly(1, 1, 1, 1);
    }

    @Test
    @DisplayName("Negative numbers: [-2,3,-4] -> [-12,8,-6]")
    void testNegativeNumbers() {
      int[] nums = { -2, 3, -4 };
      assertThat(functionalSolution.productExceptSelf(nums))
          .containsExactly(-12, 8, -6);
    }
  }
}
