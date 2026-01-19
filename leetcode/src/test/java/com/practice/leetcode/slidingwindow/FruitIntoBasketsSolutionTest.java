package com.practice.leetcode.slidingwindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Fruit Into Baskets problem.
 */
class FruitIntoBasketsSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Sliding window with at most 2 distinct types.
     * Time Complexity: O(n)
     * Space Complexity: O(1) - at most 3 entries in map
     */
    public int totalFruit(int[] fruits) {
      Map<Integer, Integer> basket = new HashMap<>();
      int left = 0;
      int maxFruits = 0;

      for (int right = 0; right < fruits.length; right++) {
        basket.merge(fruits[right], 1, Integer::sum);

        while (basket.size() > 2) {
          int leftFruit = fruits[left];
          basket.merge(leftFruit, -1, Integer::sum);
          if (basket.get(leftFruit) == 0) {
            basket.remove(leftFruit);
          }
          left++;
        }

        maxFruits = Math.max(maxFruits, right - left + 1);
      }

      return maxFruits;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Same sliding window approach.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int totalFruit(int[] fruits) {
      Map<Integer, Integer> basket = new HashMap<>();
      int[] state = { 0, 0 }; // [left, maxFruits]

      for (int right = 0; right < fruits.length; right++) {
        basket.merge(fruits[right], 1, Integer::sum);

        while (basket.size() > 2) {
          int leftFruit = fruits[state[0]];
          basket.compute(leftFruit, (k, v) -> v == 1 ? null : v - 1);
          state[0]++;
        }

        state[1] = Math.max(state[1], right - state[0] + 1);
      }

      return state[1];
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [1,2,1] -> 3")
    void testExample1() {
      assertThat(imperativeSolution.totalFruit(new int[] { 1, 2, 1 })).isEqualTo(3);
    }

    @Test
    @DisplayName("Example 2: [0,1,2,2] -> 3")
    void testExample2() {
      assertThat(imperativeSolution.totalFruit(new int[] { 0, 1, 2, 2 })).isEqualTo(3);
    }

    @Test
    @DisplayName("Example 3: [1,2,3,2,2] -> 4")
    void testExample3() {
      assertThat(imperativeSolution.totalFruit(new int[] { 1, 2, 3, 2, 2 })).isEqualTo(4);
    }

    @Test
    @DisplayName("Single type")
    void testSingleType() {
      assertThat(imperativeSolution.totalFruit(new int[] { 1, 1, 1, 1 })).isEqualTo(4);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [1,2,1] -> 3")
    void testExample1() {
      assertThat(functionalSolution.totalFruit(new int[] { 1, 2, 1 })).isEqualTo(3);
    }

    @Test
    @DisplayName("Example 2: [0,1,2,2] -> 3")
    void testExample2() {
      assertThat(functionalSolution.totalFruit(new int[] { 0, 1, 2, 2 })).isEqualTo(3);
    }

    @Test
    @DisplayName("Example 3: [1,2,3,2,2] -> 4")
    void testExample3() {
      assertThat(functionalSolution.totalFruit(new int[] { 1, 2, 3, 2, 2 })).isEqualTo(4);
    }

    @Test
    @DisplayName("Single type")
    void testSingleType() {
      assertThat(functionalSolution.totalFruit(new int[] { 1, 1, 1, 1 })).isEqualTo(4);
    }
  }
}
