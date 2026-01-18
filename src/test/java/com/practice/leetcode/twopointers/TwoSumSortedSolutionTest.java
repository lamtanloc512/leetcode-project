package com.practice.leetcode.twopointers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Two Sum II problem.
 */
class TwoSumSortedSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Two pointers from both ends.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int[] twoSum(int[] numbers, int target) {
      int left = 0, right = numbers.length - 1;

      while (left < right) {
        int sum = numbers[left] + numbers[right];
        if (sum == target) {
          return new int[] { left + 1, right + 1 }; // 1-indexed
        } else if (sum < target) {
          left++;
        } else {
          right--;
        }
      }

      return new int[] { -1, -1 };
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Two pointers with recursive helper.
     * Time Complexity: O(n)
     * Space Complexity: O(1) iterative equivalent
     */
    public int[] twoSum(int[] numbers, int target) {
      return findPair(numbers, target, 0, numbers.length - 1);
    }

    private int[] findPair(int[] nums, int target, int left, int right) {
      if (left >= right)
        return new int[] { -1, -1 };

      int sum = nums[left] + nums[right];
      if (sum == target)
        return new int[] { left + 1, right + 1 };
      return sum < target
          ? findPair(nums, target, left + 1, right)
          : findPair(nums, target, left, right - 1);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [2,7,11,15], target=9 -> [1,2]")
    void testExample1() {
      assertThat(imperativeSolution.twoSum(new int[] { 2, 7, 11, 15 }, 9))
          .containsExactly(1, 2);
    }

    @Test
    @DisplayName("Example 2: [2,3,4], target=6 -> [1,3]")
    void testExample2() {
      assertThat(imperativeSolution.twoSum(new int[] { 2, 3, 4 }, 6))
          .containsExactly(1, 3);
    }

    @Test
    @DisplayName("Example 3: [-1,0], target=-1 -> [1,2]")
    void testExample3() {
      assertThat(imperativeSolution.twoSum(new int[] { -1, 0 }, -1))
          .containsExactly(1, 2);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [2,7,11,15], target=9 -> [1,2]")
    void testExample1() {
      assertThat(functionalSolution.twoSum(new int[] { 2, 7, 11, 15 }, 9))
          .containsExactly(1, 2);
    }

    @Test
    @DisplayName("Example 2: [2,3,4], target=6 -> [1,3]")
    void testExample2() {
      assertThat(functionalSolution.twoSum(new int[] { 2, 3, 4 }, 6))
          .containsExactly(1, 3);
    }

    @Test
    @DisplayName("Example 3: [-1,0], target=-1 -> [1,2]")
    void testExample3() {
      assertThat(functionalSolution.twoSum(new int[] { -1, 0 }, -1))
          .containsExactly(1, 2);
    }
  }
}
