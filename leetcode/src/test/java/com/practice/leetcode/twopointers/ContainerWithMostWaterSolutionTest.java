package com.practice.leetcode.twopointers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Container With Most Water problem.
 */
class ContainerWithMostWaterSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Two pointers from both ends, move the shorter one.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int maxArea(int[] height) {
      int left = 0, right = height.length - 1;
      int maxArea = 0;

      while (left < right) {
        int area = (right - left) * Math.min(height[left], height[right]);
        maxArea = Math.max(maxArea, area);

        if (height[left] < height[right]) {
          left++;
        } else {
          right--;
        }
      }

      return maxArea;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Recursive two pointers approach.
     * Time Complexity: O(n)
     * Space Complexity: O(n) due to recursion
     */
    public int maxArea(int[] height) {
      return maxAreaHelper(height, 0, height.length - 1, 0);
    }

    private int maxAreaHelper(int[] h, int left, int right, int maxSoFar) {
      if (left >= right)
        return maxSoFar;

      int area = (right - left) * Math.min(h[left], h[right]);
      int newMax = Math.max(maxSoFar, area);

      return h[left] < h[right]
          ? maxAreaHelper(h, left + 1, right, newMax)
          : maxAreaHelper(h, left, right - 1, newMax);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [1,8,6,2,5,4,8,3,7] -> 49")
    void testExample1() {
      assertThat(imperativeSolution.maxArea(new int[] { 1, 8, 6, 2, 5, 4, 8, 3, 7 }))
          .isEqualTo(49);
    }

    @Test
    @DisplayName("Example 2: [1,1] -> 1")
    void testExample2() {
      assertThat(imperativeSolution.maxArea(new int[] { 1, 1 })).isEqualTo(1);
    }

    @Test
    @DisplayName("Decreasing heights")
    void testDecreasing() {
      assertThat(imperativeSolution.maxArea(new int[] { 5, 4, 3, 2, 1 })).isEqualTo(6);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [1,8,6,2,5,4,8,3,7] -> 49")
    void testExample1() {
      assertThat(functionalSolution.maxArea(new int[] { 1, 8, 6, 2, 5, 4, 8, 3, 7 }))
          .isEqualTo(49);
    }

    @Test
    @DisplayName("Example 2: [1,1] -> 1")
    void testExample2() {
      assertThat(functionalSolution.maxArea(new int[] { 1, 1 })).isEqualTo(1);
    }

    @Test
    @DisplayName("Decreasing heights")
    void testDecreasing() {
      assertThat(functionalSolution.maxArea(new int[] { 5, 4, 3, 2, 1 })).isEqualTo(6);
    }
  }
}
