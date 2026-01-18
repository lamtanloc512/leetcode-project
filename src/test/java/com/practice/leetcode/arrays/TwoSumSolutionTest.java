package com.practice.leetcode.arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Two Sum problem.
 */
class TwoSumSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: HashMap lưu phần bù (complement)
   *
   * Ý tưởng: Với mỗi số nums[i], ta tìm xem (target - nums[i]) có tồn tại không.
   * Thay vì duyệt O(n²), ta dùng HashMap để lookup O(1).
   *
   * Cách hoạt động:
   * 1. Duyệt qua mảng, với mỗi phần tử nums[i]:
   * - Tính complement = target - nums[i]
   * - Nếu complement đã có trong map → Tìm thấy cặp!
   * - Nếu không, lưu nums[i] và index vào map
   *
   * Ví dụ: nums = [2,7,11,15], target = 9
   *
   * i=0: complement = 9-2 = 7, map trống → Lưu {2:0}
   * i=1: complement = 9-7 = 2, map có 2 → Return [0,1]
   *
   * Time: O(n) - duyệt 1 lần
   * Space: O(n) - HashMap lưu tối đa n phần tử
   */
  static class ImperativeSolution {
    public int[] twoSum(int[] nums, int target) {
      Map<Integer, Integer> numToIndex = new HashMap<>();

      for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];

        if (numToIndex.containsKey(complement)) {
          return new int[] { numToIndex.get(complement), i };
        }

        numToIndex.put(nums[i], i);
      }

      return new int[] {}; // No solution found
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Uses IntStream with a HashMap for functional approach.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] twoSum(int[] nums, int target) {
      Map<Integer, Integer> numToIndex = new HashMap<>();

      return IntStream.range(0, nums.length)
          .boxed()
          .flatMap(i -> {
            int complement = target - nums[i];
            if (numToIndex.containsKey(complement)) {
              return java.util.stream.Stream.of(
                  new int[] { numToIndex.get(complement), i });
            }
            numToIndex.put(nums[i], i);
            return java.util.stream.Stream.empty();
          })
          .findFirst()
          .orElse(new int[] {});
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [2,7,11,15], target=9 -> [0,1]")
    void testExample1() {
      int[] nums = { 2, 7, 11, 15 };
      int[] result = imperativeSolution.twoSum(nums, 9);
      assertThat(result).containsExactly(0, 1);
    }

    @Test
    @DisplayName("Example 2: [3,2,4], target=6 -> [1,2]")
    void testExample2() {
      int[] nums = { 3, 2, 4 };
      int[] result = imperativeSolution.twoSum(nums, 6);
      assertThat(result).containsExactly(1, 2);
    }

    @Test
    @DisplayName("Example 3: [3,3], target=6 -> [0,1]")
    void testExample3() {
      int[] nums = { 3, 3 };
      int[] result = imperativeSolution.twoSum(nums, 6);
      assertThat(result).containsExactly(0, 1);
    }

    @Test
    @DisplayName("Negative numbers")
    void testNegativeNumbers() {
      int[] nums = { -1, -2, -3, -4, -5 };
      int[] result = imperativeSolution.twoSum(nums, -8);
      assertThat(result).containsExactly(2, 4);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [2,7,11,15], target=9 -> [0,1]")
    void testExample1() {
      int[] nums = { 2, 7, 11, 15 };
      int[] result = functionalSolution.twoSum(nums, 9);
      assertThat(result).containsExactly(0, 1);
    }

    @Test
    @DisplayName("Example 2: [3,2,4], target=6 -> [1,2]")
    void testExample2() {
      int[] nums = { 3, 2, 4 };
      int[] result = functionalSolution.twoSum(nums, 6);
      assertThat(result).containsExactly(1, 2);
    }

    @Test
    @DisplayName("Example 3: [3,3], target=6 -> [0,1]")
    void testExample3() {
      int[] nums = { 3, 3 };
      int[] result = functionalSolution.twoSum(nums, 6);
      assertThat(result).containsExactly(0, 1);
    }

    @Test
    @DisplayName("Negative numbers")
    void testNegativeNumbers() {
      int[] nums = { -1, -2, -3, -4, -5 };
      int[] result = functionalSolution.twoSum(nums, -8);
      assertThat(result).containsExactly(2, 4);
    }
  }
}
