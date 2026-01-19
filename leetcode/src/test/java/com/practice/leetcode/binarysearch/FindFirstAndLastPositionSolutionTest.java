package com.practice.leetcode.binarysearch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Find First and Last Position problem.
 */
class FindFirstAndLastPositionSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Binary Search 2 lần (tìm Lower Bound và Upper Bound)
   * 
   * Ý tưởng:
   * - Lần 1: Tìm vị trí đầu tiên của target (lower bound)
   * - Lần 2: Tìm vị trí cuối cùng của target (upper bound - 1)
   * 
   * Lower Bound: Vị trí nhỏ nhất mà nums[pos] >= target
   * Upper Bound: Vị trí nhỏ nhất mà nums[pos] > target
   * 
   * Ví dụ: nums = [5,7,7,8,8,10], target = 8
   * 
   * Tìm Lower Bound (first 8):
   * - Tìm vị trí đầu tiên >= 8 → index 3
   * 
   * Tìm Upper Bound - 1 (last 8):
   * - Tìm vị trí đầu tiên > 8 → index 5
   * - Trừ 1 → index 4
   * 
   * Kết quả: [3, 4]
   * 
   * Time: O(log n) - 2 lần binary search
   * Space: O(1)
   */
  static class ImperativeSolution {
    public int[] searchRange(int[] nums, int target) {
      int first = findFirst(nums, target);

      if (first == -1) {
        return new int[] { -1, -1 };
      }

      int last = findLast(nums, target);
      return new int[] { first, last };
    }

    // Tìm vị trí đầu tiên của target
    private int findFirst(int[] nums, int target) {
      int left = 0, right = nums.length - 1;
      int result = -1;

      while (left <= right) {
        int mid = left + (right - left) / 2;

        if (nums[mid] == target) {
          result = mid;
          right = mid - 1; // Tiếp tục tìm bên trái
        } else if (nums[mid] < target) {
          left = mid + 1;
        } else {
          right = mid - 1;
        }
      }

      return result;
    }

    // Tìm vị trí cuối cùng của target
    private int findLast(int[] nums, int target) {
      int left = 0, right = nums.length - 1;
      int result = -1;

      while (left <= right) {
        int mid = left + (right - left) / 2;

        if (nums[mid] == target) {
          result = mid;
          left = mid + 1; // Tiếp tục tìm bên phải
        } else if (nums[mid] < target) {
          left = mid + 1;
        } else {
          right = mid - 1;
        }
      }

      return result;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  /**
   * Cách tiếp cận dùng tìm bound chung.
   */
  static class FunctionalSolution {
    public int[] searchRange(int[] nums, int target) {
      int first = findBound(nums, target, true);
      if (first == -1)
        return new int[] { -1, -1 };
      int last = findBound(nums, target, false);
      return new int[] { first, last };
    }

    private int findBound(int[] nums, int target, boolean findFirst) {
      int left = 0, right = nums.length - 1;
      int result = -1;

      while (left <= right) {
        int mid = left + (right - left) / 2;

        if (nums[mid] == target) {
          result = mid;
          if (findFirst)
            right = mid - 1;
          else
            left = mid + 1;
        } else if (nums[mid] < target) {
          left = mid + 1;
        } else {
          right = mid - 1;
        }
      }

      return result;
    }
  }

  // ========== TESTS ==========
  private final ImperativeSolution imp = new ImperativeSolution();
  private final FunctionalSolution func = new FunctionalSolution();

  @Test
  @DisplayName("Find first and last position of target")
  void testSearchRange() {
    assertThat(imp.searchRange(new int[] { 5, 7, 7, 8, 8, 10 }, 8)).containsExactly(3, 4);
    assertThat(imp.searchRange(new int[] { 5, 7, 7, 8, 8, 10 }, 6)).containsExactly(-1, -1);
    assertThat(func.searchRange(new int[] { 5, 7, 7, 8, 8, 10 }, 8)).containsExactly(3, 4);
  }
}
