package com.practice.leetcode.arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Best Time to Buy and Sell Stock problem.
 */
class BestTimeToBuyAndSellStockSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Track minimum price and maximum profit while iterating.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int maxProfit(int[] prices) {
      if (prices == null || prices.length < 2) {
        return 0;
      }

      int minPrice = prices[0];
      int maxProfit = 0;

      for (int i = 1; i < prices.length; i++) {
        if (prices[i] < minPrice) {
          minPrice = prices[i];
        } else {
          maxProfit = Math.max(maxProfit, prices[i] - minPrice);
        }
      }

      return maxProfit;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Uses reduce with a state holder to track min and max profit.
     * Time Complexity: O(n)
     * Space Complexity: O(1) - aside from stream overhead
     */
    public int maxProfit(int[] prices) {
      if (prices == null || prices.length < 2) {
        return 0;
      }

      // State: [minPrice, maxProfit]
      int[] result = IntStream.of(prices)
          .boxed()
          .reduce(
              new int[] { Integer.MAX_VALUE, 0 },
              (state, price) -> new int[] {
                  Math.min(state[0], price),
                  Math.max(state[1], price - state[0])
              },
              (a, b) -> a // combiner not used in sequential stream
          );

      return result[1];
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [7,1,5,3,6,4] -> 5")
    void testExample1() {
      int[] prices = { 7, 1, 5, 3, 6, 4 };
      assertThat(imperativeSolution.maxProfit(prices)).isEqualTo(5);
    }

    @Test
    @DisplayName("Example 2: [7,6,4,3,1] -> 0 (no profit possible)")
    void testExample2() {
      int[] prices = { 7, 6, 4, 3, 1 };
      assertThat(imperativeSolution.maxProfit(prices)).isEqualTo(0);
    }

    @Test
    @DisplayName("Single element array")
    void testSingleElement() {
      int[] prices = { 5 };
      assertThat(imperativeSolution.maxProfit(prices)).isEqualTo(0);
    }

    @Test
    @DisplayName("Two elements with profit")
    void testTwoElementsWithProfit() {
      int[] prices = { 1, 5 };
      assertThat(imperativeSolution.maxProfit(prices)).isEqualTo(4);
    }

    @Test
    @DisplayName("All same prices")
    void testAllSamePrices() {
      int[] prices = { 5, 5, 5, 5 };
      assertThat(imperativeSolution.maxProfit(prices)).isEqualTo(0);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [7,1,5,3,6,4] -> 5")
    void testExample1() {
      int[] prices = { 7, 1, 5, 3, 6, 4 };
      assertThat(functionalSolution.maxProfit(prices)).isEqualTo(5);
    }

    @Test
    @DisplayName("Example 2: [7,6,4,3,1] -> 0 (no profit possible)")
    void testExample2() {
      int[] prices = { 7, 6, 4, 3, 1 };
      assertThat(functionalSolution.maxProfit(prices)).isEqualTo(0);
    }

    @Test
    @DisplayName("Single element array")
    void testSingleElement() {
      int[] prices = { 5 };
      assertThat(functionalSolution.maxProfit(prices)).isEqualTo(0);
    }

    @Test
    @DisplayName("Two elements with profit")
    void testTwoElementsWithProfit() {
      int[] prices = { 1, 5 };
      assertThat(functionalSolution.maxProfit(prices)).isEqualTo(4);
    }

    @Test
    @DisplayName("All same prices")
    void testAllSamePrices() {
      int[] prices = { 5, 5, 5, 5 };
      assertThat(functionalSolution.maxProfit(prices)).isEqualTo(0);
    }
  }
}
