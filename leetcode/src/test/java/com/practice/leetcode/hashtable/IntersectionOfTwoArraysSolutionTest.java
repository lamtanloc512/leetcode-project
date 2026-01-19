package com.practice.leetcode.hashtable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Intersection of Two Arrays II problem.
 */
class IntersectionOfTwoArraysSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * HashMap frequency counting approach.
     * Time Complexity: O(n + m)
     * Space Complexity: O(min(n, m))
     */
    public int[] intersect(int[] nums1, int[] nums2) {
      if (nums1.length > nums2.length) {
        return intersect(nums2, nums1);
      }

      Map<Integer, Integer> countMap = new HashMap<>();
      for (int num : nums1) {
        countMap.merge(num, 1, Integer::sum);
      }

      List<Integer> result = new ArrayList<>();
      for (int num : nums2) {
        if (countMap.getOrDefault(num, 0) > 0) {
          result.add(num);
          countMap.merge(num, -1, Integer::sum);
        }
      }

      return result.stream().mapToInt(Integer::intValue).toArray();
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Stream-based approach with mutable frequency map.
     * Time Complexity: O(n + m)
     * Space Complexity: O(n)
     */
    public int[] intersect(int[] nums1, int[] nums2) {
      Map<Integer, Long> freq1 = Arrays.stream(nums1)
          .boxed()
          .collect(Collectors.groupingBy(
              Function.identity(),
              Collectors.counting()));

      Map<Integer, Integer> used = new HashMap<>();

      return Arrays.stream(nums2)
          .filter(num -> {
            long available = freq1.getOrDefault(num, 0L);
            int alreadyUsed = used.getOrDefault(num, 0);
            if (alreadyUsed < available) {
              used.merge(num, 1, Integer::sum);
              return true;
            }
            return false;
          })
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
    @DisplayName("Example 1: [1,2,2,1], [2,2] -> [2,2]")
    void testExample1() {
      int[] result = imperativeSolution.intersect(new int[] { 1, 2, 2, 1 }, new int[] { 2, 2 });
      Arrays.sort(result);
      assertThat(result).containsExactly(2, 2);
    }

    @Test
    @DisplayName("Example 2: [4,9,5], [9,4,9,8,4] -> [4,9] or [9,4]")
    void testExample2() {
      int[] result = imperativeSolution.intersect(new int[] { 4, 9, 5 }, new int[] { 9, 4, 9, 8, 4 });
      Arrays.sort(result);
      assertThat(result).containsExactly(4, 9);
    }

    @Test
    @DisplayName("No intersection")
    void testNoIntersection() {
      int[] result = imperativeSolution.intersect(new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 });
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Identical arrays")
    void testIdentical() {
      int[] result = imperativeSolution.intersect(new int[] { 1, 2, 3 }, new int[] { 1, 2, 3 });
      Arrays.sort(result);
      assertThat(result).containsExactly(1, 2, 3);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [1,2,2,1], [2,2] -> [2,2]")
    void testExample1() {
      int[] result = functionalSolution.intersect(new int[] { 1, 2, 2, 1 }, new int[] { 2, 2 });
      Arrays.sort(result);
      assertThat(result).containsExactly(2, 2);
    }

    @Test
    @DisplayName("Example 2: [4,9,5], [9,4,9,8,4] -> [4,9] or [9,4]")
    void testExample2() {
      int[] result = functionalSolution.intersect(new int[] { 4, 9, 5 }, new int[] { 9, 4, 9, 8, 4 });
      Arrays.sort(result);
      assertThat(result).containsExactly(4, 9);
    }

    @Test
    @DisplayName("No intersection")
    void testNoIntersection() {
      int[] result = functionalSolution.intersect(new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 });
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Identical arrays")
    void testIdentical() {
      int[] result = functionalSolution.intersect(new int[] { 1, 2, 3 }, new int[] { 1, 2, 3 });
      Arrays.sort(result);
      assertThat(result).containsExactly(1, 2, 3);
    }
  }
}
