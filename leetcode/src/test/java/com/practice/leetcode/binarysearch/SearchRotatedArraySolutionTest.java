package com.practice.leetcode.binarysearch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Search in Rotated Sorted Array problem.
 */
class SearchRotatedArraySolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Binary Search trên mảng xoay
   * 
   * Ý tưởng chính: Sau mỗi phép chia đôi, ít nhất một nửa mảng vẫn được sắp xếp.
   * Ta xác định nửa nào được sắp xếp rồi quyết định tìm kiếm bên nào.
   * 
   * Cách xác định nửa nào sorted:
   * - Nếu nums[left] <= nums[mid] → Nửa trái sorted
   * - Ngược lại → Nửa phải sorted
   * 
   * Thuật toán:
   * 1. Tính mid
   * 2. Kiểm tra nums[mid] == target → return
   * 3. Xác định nửa nào sorted:
   * a. Nếu nửa trái sorted (nums[left] <= nums[mid]):
   * - Nếu target trong range [left, mid) → tìm bên trái
   * - Ngược lại → tìm bên phải
   * b. Nếu nửa phải sorted:
   * - Nếu target trong range (mid, right] → tìm bên phải
   * - Ngược lại → tìm bên trái
   * 
   * Ví dụ: nums = [4,5,6,7,0,1,2], target = 0
   * 
   * Bước 1: left=0, right=6, mid=3
   * nums[3]=7, nums[0]=4 <= 7 → Nửa trái [4,5,6,7] sorted
   * target=0 không trong [4,7] → Tìm bên phải, left=4
   * 
   * Bước 2: left=4, right=6, mid=5
   * nums[5]=1, nums[4]=0 <= 1 → Nửa trái [0,1] sorted
   * target=0 trong [0,1] → Tìm bên trái, right=4
   * 
   * Bước 3: left=4, right=4, mid=4
   * nums[4]=0 == target → return 4
   * 
   * Time: O(log n)
   * Space: O(1)
   */
  static class ImperativeSolution {
    public int search(int[] nums, int target) {
      int left = 0, right = nums.length - 1;

      while (left <= right) {
        int mid = left + (right - left) / 2;

        if (nums[mid] == target) {
          return mid;
        }

        // Xác định nửa nào được sắp xếp
        if (nums[left] <= nums[mid]) {
          // Nửa trái sorted
          if (target >= nums[left] && target < nums[mid]) {
            right = mid - 1; // Target bên trái
          } else {
            left = mid + 1; // Target bên phải
          }
        } else {
          // Nửa phải sorted
          if (target > nums[mid] && target <= nums[right]) {
            left = mid + 1; // Target bên phải
          } else {
            right = mid - 1; // Target bên trái
          }
        }
      }

      return -1;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  /**
   * Đệ quy với cùng logic.
   */
  static class FunctionalSolution {
    public int search(int[] nums, int target) {
      return searchHelper(nums, target, 0, nums.length - 1);
    }

    private int searchHelper(int[] nums, int target, int left, int right) {
      if (left > right)
        return -1;

      int mid = left + (right - left) / 2;
      if (nums[mid] == target)
        return mid;

      // Nửa trái sorted
      if (nums[left] <= nums[mid]) {
        if (target >= nums[left] && target < nums[mid]) {
          return searchHelper(nums, target, left, mid - 1);
        } else {
          return searchHelper(nums, target, mid + 1, right);
        }
      }
      // Nửa phải sorted
      else {
        if (target > nums[mid] && target <= nums[right]) {
          return searchHelper(nums, target, mid + 1, right);
        } else {
          return searchHelper(nums, target, left, mid - 1);
        }
      }
    }
  }

  // ========== TESTS ==========
  private final ImperativeSolution imp = new ImperativeSolution();
  private final FunctionalSolution func = new FunctionalSolution();

  @Test
  @DisplayName("Search in rotated sorted array")
  void testSearch() {
    assertThat(imp.search(new int[] { 4, 5, 6, 7, 0, 1, 2 }, 0)).isEqualTo(4);
    assertThat(imp.search(new int[] { 4, 5, 6, 7, 0, 1, 2 }, 3)).isEqualTo(-1);
    assertThat(func.search(new int[] { 4, 5, 6, 7, 0, 1, 2 }, 0)).isEqualTo(4);
  }
}
