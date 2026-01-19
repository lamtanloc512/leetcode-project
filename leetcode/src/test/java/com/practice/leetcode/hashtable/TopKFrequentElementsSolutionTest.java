package com.practice.leetcode.hashtable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Top K Frequent Elements problem.
 */
class TopKFrequentElementsSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Bucket sort approach.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    @SuppressWarnings("unchecked")
    public int[] topKFrequent(int[] nums, int k) {
      Map<Integer, Integer> count = new HashMap<>();
      for (int num : nums) {
        count.merge(num, 1, Integer::sum);
      }

      // Buckets indexed by frequency
      List<Integer>[] buckets = new List[nums.length + 1];
      for (int i = 0; i < buckets.length; i++) {
        buckets[i] = new ArrayList<>();
      }

      for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
        buckets[entry.getValue()].add(entry.getKey());
      }

      int[] result = new int[k];
      int index = 0;

      for (int freq = buckets.length - 1; freq >= 0 && index < k; freq--) {
        for (int num : buckets[freq]) {
          if (index < k) {
            result[index++] = num;
          }
        }
      }

      return result;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Stream-based with sorting by frequency.
     * Time Complexity: O(n log n) due to sorting
     * Space Complexity: O(n)
     */
    public int[] topKFrequent(int[] nums, int k) {
      Map<Integer, Long> frequency = Arrays.stream(nums)
          .boxed()
          .collect(Collectors.groupingBy(
              Function.identity(),
              Collectors.counting()));

      return frequency.entrySet().stream()
          .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
          .limit(k)
          .mapToInt(Map.Entry::getKey)
          .toArray();
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [1,1,1,2,2,3], k=2 -> [1,2]")
    void testExample1() {
      int[] result = imperativeSolution.topKFrequent(new int[] { 1, 1, 1, 2, 2, 3 }, 2);
      Arrays.sort(result);
      assertThat(result).containsExactly(1, 2);
    }

    @Test
    @DisplayName("Example 2: [1], k=1 -> [1]")
    void testExample2() {
      int[] result = imperativeSolution.topKFrequent(new int[] { 1 }, 1);
      assertThat(result).containsExactly(1);
    }

    @Test
    @DisplayName("All same frequency")
    void testSameFrequency() {
      int[] result = imperativeSolution.topKFrequent(new int[] { 1, 2, 3 }, 2);
      assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Negative numbers")
    void testNegativeNumbers() {
      int[] result = imperativeSolution.topKFrequent(new int[] { -1, -1, 2, 2, 2 }, 1);
      assertThat(result).containsExactly(2);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [1,1,1,2,2,3], k=2 -> [1,2]")
    void testExample1() {
      int[] result = functionalSolution.topKFrequent(new int[] { 1, 1, 1, 2, 2, 3 }, 2);
      Arrays.sort(result);
      assertThat(result).containsExactly(1, 2);
    }

    @Test
    @DisplayName("Example 2: [1], k=1 -> [1]")
    void testExample2() {
      int[] result = functionalSolution.topKFrequent(new int[] { 1 }, 1);
      assertThat(result).containsExactly(1);
    }

    @Test
    @DisplayName("All same frequency")
    void testSameFrequency() {
      int[] result = functionalSolution.topKFrequent(new int[] { 1, 2, 3 }, 2);
      assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Negative numbers")
    void testNegativeNumbers() {
      int[] result = functionalSolution.topKFrequent(new int[] { -1, -1, 2, 2, 2 }, 1);
      assertThat(result).containsExactly(2);
    }
  }
}
