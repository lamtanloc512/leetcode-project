package com.practice.leetcode.slidingwindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Solutions and tests for Maximum Average Subarray I problem.
 */
class MaxAverageSubarraySolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Fixed-size sliding window.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public double findMaxAverage(int[] nums, int k) {
      // Initial window sum
      int sum = 0;
      for (int i = 0; i < k; i++) {
        sum += nums[i];
      }

      int maxSum = sum;

      // Slide the window
      for (int i = k; i < nums.length; i++) {
        sum = sum + nums[i] - nums[i - k];
        maxSum = Math.max(maxSum, sum);
      }

      return (double) maxSum / k;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Stream-based sliding window.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public double findMaxAverage(int[] nums, int k) {
      int initialSum = IntStream.range(0, k).map(i -> nums[i]).sum();

      int[] state = { initialSum, initialSum }; // [currentSum, maxSum]

      IntStream.range(k, nums.length).forEach(i -> {
        state[0] = state[0] + nums[i] - nums[i - k];
        state[1] = Math.max(state[1], state[0]);
      });

      return (double) state[1] / k;
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [1,12,-5,-6,50,3], k=4 -> 12.75")
    void testExample1() {
      assertThat(imperativeSolution.findMaxAverage(new int[] { 1, 12, -5, -6, 50, 3 }, 4))
          .isCloseTo(12.75, within(0.00001));
    }

    @Test
    @DisplayName("Example 2: [5], k=1 -> 5.0")
    void testExample2() {
      assertThat(imperativeSolution.findMaxAverage(new int[] { 5 }, 1))
          .isCloseTo(5.0, within(0.00001));
    }

    @Test
    @DisplayName("All negatives")
    void testAllNegatives() {
      assertThat(imperativeSolution.findMaxAverage(new int[] { -1, -2, -3 }, 2))
          .isCloseTo(-1.5, within(0.00001));
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [1,12,-5,-6,50,3], k=4 -> 12.75")
    void testExample1() {
      assertThat(functionalSolution.findMaxAverage(new int[] { 1, 12, -5, -6, 50, 3 }, 4))
          .isCloseTo(12.75, within(0.00001));
    }

    @Test
    @DisplayName("Example 2: [5], k=1 -> 5.0")
    void testExample2() {
      assertThat(functionalSolution.findMaxAverage(new int[] { 5 }, 1))
          .isCloseTo(5.0, within(0.00001));
    }

    @Test
    @DisplayName("All negatives")
    void testAllNegatives() {
      assertThat(functionalSolution.findMaxAverage(new int[] { -1, -2, -3 }, 2))
          .isCloseTo(-1.5, within(0.00001));
    }
  }
}
