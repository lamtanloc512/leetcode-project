package com.practice.leetcode.slidingwindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Minimum Size Subarray Sum problem.
 */
class MinSizeSubarraySumSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Variable-size sliding window.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int minSubArrayLen(int target, int[] nums) {
      int minLen = Integer.MAX_VALUE;
      int left = 0;
      int sum = 0;

      for (int right = 0; right < nums.length; right++) {
        sum += nums[right];

        while (sum >= target) {
          minLen = Math.min(minLen, right - left + 1);
          sum -= nums[left++];
        }
      }

      return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Same sliding window with record state.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int minSubArrayLen(int target, int[] nums) {
      int[] state = { 0, 0, Integer.MAX_VALUE }; // [left, sum, minLen]

      for (int right = 0; right < nums.length; right++) {
        state[1] += nums[right];

        while (state[1] >= target) {
          state[2] = Math.min(state[2], right - state[0] + 1);
          state[1] -= nums[state[0]++];
        }
      }

      return state[2] == Integer.MAX_VALUE ? 0 : state[2];
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: target=7, [2,3,1,2,4,3] -> 2")
    void testExample1() {
      assertThat(imperativeSolution.minSubArrayLen(7, new int[] { 2, 3, 1, 2, 4, 3 }))
          .isEqualTo(2);
    }

    @Test
    @DisplayName("Example 2: target=4, [1,4,4] -> 1")
    void testExample2() {
      assertThat(imperativeSolution.minSubArrayLen(4, new int[] { 1, 4, 4 }))
          .isEqualTo(1);
    }

    @Test
    @DisplayName("Example 3: target=11, [1,1,1,1,1,1,1,1] -> 0")
    void testExample3() {
      assertThat(imperativeSolution.minSubArrayLen(11, new int[] { 1, 1, 1, 1, 1, 1, 1, 1 }))
          .isEqualTo(0);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: target=7, [2,3,1,2,4,3] -> 2")
    void testExample1() {
      assertThat(functionalSolution.minSubArrayLen(7, new int[] { 2, 3, 1, 2, 4, 3 }))
          .isEqualTo(2);
    }

    @Test
    @DisplayName("Example 2: target=4, [1,4,4] -> 1")
    void testExample2() {
      assertThat(functionalSolution.minSubArrayLen(4, new int[] { 1, 4, 4 }))
          .isEqualTo(1);
    }

    @Test
    @DisplayName("Example 3: target=11, [1,1,1,1,1,1,1,1] -> 0")
    void testExample3() {
      assertThat(functionalSolution.minSubArrayLen(11, new int[] { 1, 1, 1, 1, 1, 1, 1, 1 }))
          .isEqualTo(0);
    }
  }
}
