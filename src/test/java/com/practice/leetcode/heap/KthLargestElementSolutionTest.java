package com.practice.leetcode.heap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.PriorityQueue;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Kth Largest Element in an Array problem.
 */
class KthLargestElementSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Min heap of size k approach.
     * Time Complexity: O(n log k)
     * Space Complexity: O(k)
     */
    public int findKthLargest(int[] nums, int k) {
      PriorityQueue<Integer> minHeap = new PriorityQueue<>();

      for (int num : nums) {
        minHeap.offer(num);
        if (minHeap.size() > k) {
          minHeap.poll();
        }
      }

      return minHeap.peek();
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Stream sorted approach.
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public int findKthLargest(int[] nums, int k) {
      return Arrays.stream(nums)
          .boxed()
          .sorted((a, b) -> b - a)
          .skip(k - 1)
          .findFirst()
          .orElseThrow(() -> new RuntimeException("findKthLargest: array is empty"));
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [3,2,1,5,6,4], k=2 -> 5")
    void testExample1() {
      assertThat(imperativeSolution.findKthLargest(new int[] { 3, 2, 1, 5, 6, 4 }, 2)).isEqualTo(5);
    }

    @Test
    @DisplayName("Example 2: [3,2,3,1,2,4,5,5,6], k=4 -> 4")
    void testExample2() {
      assertThat(imperativeSolution.findKthLargest(new int[] { 3, 2, 3, 1, 2, 4, 5, 5, 6 }, 4)).isEqualTo(4);
    }

    @Test
    @DisplayName("Single element")
    void testSingleElement() {
      assertThat(imperativeSolution.findKthLargest(new int[] { 1 }, 1)).isEqualTo(1);
    }

    @Test
    @DisplayName("All same elements")
    void testAllSame() {
      assertThat(imperativeSolution.findKthLargest(new int[] { 5, 5, 5, 5 }, 2)).isEqualTo(5);
    }

    @Test
    @DisplayName("Negative numbers")
    void testNegative() {
      assertThat(imperativeSolution.findKthLargest(new int[] { -1, -2, -3, -4 }, 2)).isEqualTo(-2);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [3,2,1,5,6,4], k=2 -> 5")
    void testExample1() {
      assertThat(functionalSolution.findKthLargest(new int[] { 3, 2, 1, 5, 6, 4 }, 2)).isEqualTo(5);
    }

    @Test
    @DisplayName("Example 2: [3,2,3,1,2,4,5,5,6], k=4 -> 4")
    void testExample2() {
      assertThat(functionalSolution.findKthLargest(new int[] { 3, 2, 3, 1, 2, 4, 5, 5, 6 }, 4)).isEqualTo(4);
    }

    @Test
    @DisplayName("Single element")
    void testSingleElement() {
      assertThat(functionalSolution.findKthLargest(new int[] { 1 }, 1)).isEqualTo(1);
    }

    @Test
    @DisplayName("All same elements")
    void testAllSame() {
      assertThat(functionalSolution.findKthLargest(new int[] { 5, 5, 5, 5 }, 2)).isEqualTo(5);
    }

    @Test
    @DisplayName("Negative numbers")
    void testNegative() {
      assertThat(functionalSolution.findKthLargest(new int[] { -1, -2, -3, -4 }, 2)).isEqualTo(-2);
    }
  }
}
