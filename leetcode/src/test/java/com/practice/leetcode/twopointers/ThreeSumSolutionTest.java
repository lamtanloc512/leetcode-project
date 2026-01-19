package com.practice.leetcode.twopointers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for 3Sum problem.
 */
class ThreeSumSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Sort + two pointers for each fixed first element.
     * Time Complexity: O(n²)
     * Space Complexity: O(1) excluding output
     */
    public List<List<Integer>> threeSum(int[] nums) {
      List<List<Integer>> result = new ArrayList<>();
      Arrays.sort(nums);

      for (int i = 0; i < nums.length - 2; i++) {
        // Skip duplicates for first element
        if (i > 0 && nums[i] == nums[i - 1])
          continue;

        int left = i + 1, right = nums.length - 1;

        while (left < right) {
          int sum = nums[i] + nums[left] + nums[right];

          if (sum == 0) {
            result.add(Arrays.asList(nums[i], nums[left], nums[right]));

            // Skip duplicates
            while (left < right && nums[left] == nums[left + 1])
              left++;
            while (left < right && nums[right] == nums[right - 1])
              right--;

            left++;
            right--;
          } else if (sum < 0) {
            left++;
          } else {
            right--;
          }
        }
      }

      return result;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Same logic with stream-based duplicate handling.
     * Time Complexity: O(n²)
     * Space Complexity: O(n)
     */
    public List<List<Integer>> threeSum(int[] nums) {
      Arrays.sort(nums);
      Set<List<Integer>> resultSet = new HashSet<>();

      IntStream.range(0, nums.length - 2)
          .filter(i -> i == 0 || nums[i] != nums[i - 1])
          .forEach(i -> {
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
              int sum = nums[i] + nums[left] + nums[right];
              if (sum == 0) {
                resultSet.add(Arrays.asList(nums[i], nums[left++], nums[right--]));
              } else if (sum < 0) {
                left++;
              } else {
                right--;
              }
            }
          });

      return new ArrayList<>(resultSet);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [-1,0,1,2,-1,-4]")
    void testExample1() {
      List<List<Integer>> result = imperativeSolution.threeSum(new int[] { -1, 0, 1, 2, -1, -4 });
      assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Example 2: [0,1,1] -> []")
    void testExample2() {
      List<List<Integer>> result = imperativeSolution.threeSum(new int[] { 0, 1, 1 });
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Example 3: [0,0,0] -> [[0,0,0]]")
    void testExample3() {
      List<List<Integer>> result = imperativeSolution.threeSum(new int[] { 0, 0, 0 });
      assertThat(result).hasSize(1);
      assertThat(result.get(0)).containsExactly(0, 0, 0);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [-1,0,1,2,-1,-4]")
    void testExample1() {
      List<List<Integer>> result = functionalSolution.threeSum(new int[] { -1, 0, 1, 2, -1, -4 });
      assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Example 2: [0,1,1] -> []")
    void testExample2() {
      List<List<Integer>> result = functionalSolution.threeSum(new int[] { 0, 1, 1 });
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Example 3: [0,0,0] -> [[0,0,0]]")
    void testExample3() {
      List<List<Integer>> result = functionalSolution.threeSum(new int[] { 0, 0, 0 });
      assertThat(result).hasSize(1);
      assertThat(result.get(0)).containsExactly(0, 0, 0);
    }
  }
}
