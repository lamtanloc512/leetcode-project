package com.practice.leetcode.heap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for K Closest Points to Origin problem.
 */
class KClosestPointsSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Max heap of size k based on distance.
     * Time Complexity: O(n log k)
     * Space Complexity: O(k)
     */
    public int[][] kClosest(int[][] points, int k) {
      // Max heap - keep k closest points
      PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
          (a, b) -> distance(b) - distance(a));

      for (int[] point : points) {
        maxHeap.offer(point);
        if (maxHeap.size() > k) {
          maxHeap.poll();
        }
      }

      int[][] result = new int[k][2];
      int i = 0;
      while (!maxHeap.isEmpty()) {
        result[i++] = maxHeap.poll();
      }

      return result;
    }

    private int distance(int[] point) {
      return point[0] * point[0] + point[1] * point[1];
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Stream sorted by distance.
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public int[][] kClosest(int[][] points, int k) {
      return Arrays.stream(points)
          .sorted(Comparator.comparingInt(p -> p[0] * p[0] + p[1] * p[1]))
          .limit(k)
          .toArray(int[][]::new);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [[1,3],[-2,2]], k=1 -> [[-2,2]]")
    void testExample1() {
      int[][] result = imperativeSolution.kClosest(new int[][] { { 1, 3 }, { -2, 2 } }, 1);
      assertThat(result).hasDimensions(1, 2);
      assertThat(result[0]).containsExactly(-2, 2);
    }

    @Test
    @DisplayName("Example 2: [[3,3],[5,-1],[-2,4]], k=2")
    void testExample2() {
      int[][] result = imperativeSolution.kClosest(new int[][] { { 3, 3 }, { 5, -1 }, { -2, 4 } }, 2);
      assertThat(result).hasDimensions(2, 2);
    }

    @Test
    @DisplayName("Single point")
    void testSinglePoint() {
      int[][] result = imperativeSolution.kClosest(new int[][] { { 1, 1 } }, 1);
      assertThat(result).hasDimensions(1, 2);
      assertThat(result[0]).containsExactly(1, 1);
    }

    @Test
    @DisplayName("Origin point")
    void testOrigin() {
      int[][] result = imperativeSolution.kClosest(new int[][] { { 0, 0 }, { 1, 1 } }, 1);
      assertThat(result).hasDimensions(1, 2);
      assertThat(result[0]).containsExactly(0, 0);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [[1,3],[-2,2]], k=1 -> [[-2,2]]")
    void testExample1() {
      int[][] result = functionalSolution.kClosest(new int[][] { { 1, 3 }, { -2, 2 } }, 1);
      assertThat(result).hasDimensions(1, 2);
      assertThat(result[0]).containsExactly(-2, 2);
    }

    @Test
    @DisplayName("Example 2: [[3,3],[5,-1],[-2,4]], k=2")
    void testExample2() {
      int[][] result = functionalSolution.kClosest(new int[][] { { 3, 3 }, { 5, -1 }, { -2, 4 } }, 2);
      assertThat(result).hasDimensions(2, 2);
    }

    @Test
    @DisplayName("Single point")
    void testSinglePoint() {
      int[][] result = functionalSolution.kClosest(new int[][] { { 1, 1 } }, 1);
      assertThat(result).hasDimensions(1, 2);
      assertThat(result[0]).containsExactly(1, 1);
    }

    @Test
    @DisplayName("Origin point")
    void testOrigin() {
      int[][] result = functionalSolution.kClosest(new int[][] { { 0, 0 }, { 1, 1 } }, 1);
      assertThat(result).hasDimensions(1, 2);
      assertThat(result[0]).containsExactly(0, 0);
    }
  }
}
