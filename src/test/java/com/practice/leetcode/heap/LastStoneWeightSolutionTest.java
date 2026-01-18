package com.practice.leetcode.heap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Last Stone Weight problem.
 */
class LastStoneWeightSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Max heap simulation.
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public int lastStoneWeight(int[] stones) {
      PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

      for (int stone : stones) {
        maxHeap.offer(stone);
      }

      while (maxHeap.size() > 1) {
        int y = maxHeap.poll();
        int x = maxHeap.poll();

        if (y != x) {
          maxHeap.offer(y - x);
        }
      }

      return maxHeap.isEmpty() ? 0 : maxHeap.peek();
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Recursive simulation with sorted list.
     * Time Complexity: O(n^2 log n)
     * Space Complexity: O(n)
     */
    public int lastStoneWeight(int[] stones) {
      List<Integer> stoneList = new ArrayList<>();
      for (int stone : stones) {
        stoneList.add(stone);
      }

      while (stoneList.size() > 1) {
        stoneList.sort(Collections.reverseOrder());
        int y = stoneList.remove(0);
        int x = stoneList.remove(0);

        if (y != x) {
          stoneList.add(y - x);
        }
      }

      return stoneList.isEmpty() ? 0 : stoneList.get(0);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [2,7,4,1,8,1] -> 1")
    void testExample1() {
      assertThat(imperativeSolution.lastStoneWeight(new int[] { 2, 7, 4, 1, 8, 1 })).isEqualTo(1);
    }

    @Test
    @DisplayName("Example 2: [1] -> 1")
    void testExample2() {
      assertThat(imperativeSolution.lastStoneWeight(new int[] { 1 })).isEqualTo(1);
    }

    @Test
    @DisplayName("Two equal stones")
    void testTwoEqual() {
      assertThat(imperativeSolution.lastStoneWeight(new int[] { 5, 5 })).isEqualTo(0);
    }

    @Test
    @DisplayName("All same weight")
    void testAllSame() {
      assertThat(imperativeSolution.lastStoneWeight(new int[] { 2, 2, 2, 2 })).isEqualTo(0);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [2,7,4,1,8,1] -> 1")
    void testExample1() {
      assertThat(functionalSolution.lastStoneWeight(new int[] { 2, 7, 4, 1, 8, 1 })).isEqualTo(1);
    }

    @Test
    @DisplayName("Example 2: [1] -> 1")
    void testExample2() {
      assertThat(functionalSolution.lastStoneWeight(new int[] { 1 })).isEqualTo(1);
    }

    @Test
    @DisplayName("Two equal stones")
    void testTwoEqual() {
      assertThat(functionalSolution.lastStoneWeight(new int[] { 5, 5 })).isEqualTo(0);
    }

    @Test
    @DisplayName("All same weight")
    void testAllSame() {
      assertThat(functionalSolution.lastStoneWeight(new int[] { 2, 2, 2, 2 })).isEqualTo(0);
    }
  }
}
