package com.practice.leetcode.arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Contains Duplicate problem.
 */
class ContainsDuplicateSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Uses a HashSet to track seen elements.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean containsDuplicate(int[] nums) {
      Set<Integer> seen = new HashSet<>();

      for (int num : nums) {
        if (seen.contains(num)) {
          return true;
        }
        seen.add(num);
      }

      return false;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Compares distinct count with original array length.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean containsDuplicate(int[] nums) {
      return Arrays.stream(nums)
          .distinct()
          .count() != nums.length;
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [1,2,3,1] -> true")
    void testExample1() {
      int[] nums = { 1, 2, 3, 1 };
      assertThat(imperativeSolution.containsDuplicate(nums)).isTrue();
    }

    @Test
    @DisplayName("Example 2: [1,2,3,4] -> false")
    void testExample2() {
      int[] nums = { 1, 2, 3, 4 };
      assertThat(imperativeSolution.containsDuplicate(nums)).isFalse();
    }

    @Test
    @DisplayName("Example 3: [1,1,1,3,3,4,3,2,4,2] -> true")
    void testExample3() {
      int[] nums = { 1, 1, 1, 3, 3, 4, 3, 2, 4, 2 };
      assertThat(imperativeSolution.containsDuplicate(nums)).isTrue();
    }

    @Test
    @DisplayName("Single element")
    void testSingleElement() {
      int[] nums = { 1 };
      assertThat(imperativeSolution.containsDuplicate(nums)).isFalse();
    }

    @Test
    @DisplayName("Two same elements")
    void testTwoSameElements() {
      int[] nums = { 1, 1 };
      assertThat(imperativeSolution.containsDuplicate(nums)).isTrue();
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [1,2,3,1] -> true")
    void testExample1() {
      int[] nums = { 1, 2, 3, 1 };
      assertThat(functionalSolution.containsDuplicate(nums)).isTrue();
    }

    @Test
    @DisplayName("Example 2: [1,2,3,4] -> false")
    void testExample2() {
      int[] nums = { 1, 2, 3, 4 };
      assertThat(functionalSolution.containsDuplicate(nums)).isFalse();
    }

    @Test
    @DisplayName("Example 3: [1,1,1,3,3,4,3,2,4,2] -> true")
    void testExample3() {
      int[] nums = { 1, 1, 1, 3, 3, 4, 3, 2, 4, 2 };
      assertThat(functionalSolution.containsDuplicate(nums)).isTrue();
    }

    @Test
    @DisplayName("Single element")
    void testSingleElement() {
      int[] nums = { 1 };
      assertThat(functionalSolution.containsDuplicate(nums)).isFalse();
    }

    @Test
    @DisplayName("Two same elements")
    void testTwoSameElements() {
      int[] nums = { 1, 1 };
      assertThat(functionalSolution.containsDuplicate(nums)).isTrue();
    }
  }
}
