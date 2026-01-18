package com.practice.leetcode.binarysearch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Binary Search problem.
 */
class BinarySearchSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN BINARY SEARCH (Tìm kiếm nhị phân):
   * 
   * Ý tưởng: Chia đôi vùng tìm kiếm sau mỗi bước so sánh.
   * 
   * Cách hoạt động:
   * 1. Duy trì 2 con trỏ: left (đầu mảng) và right (cuối mảng)
   * 2. Tính mid = (left + right) / 2
   * 3. So sánh nums[mid] với target:
   * - Nếu nums[mid] == target → Tìm thấy, trả về mid
   * - Nếu nums[mid] < target → Target nằm bên phải, left = mid + 1
   * - Nếu nums[mid] > target → Target nằm bên trái, right = mid - 1
   * 4. Lặp lại cho đến khi left > right (không tìm thấy)
   * 
   * Ví dụ: nums = [-1,0,3,5,9,12], target = 9
   * 
   * Bước 1: left=0, right=5, mid=2, nums[2]=3 < 9 → left=3
   * Bước 2: left=3, right=5, mid=4, nums[4]=9 == 9 → return 4
   * 
   * Time: O(log n) - Mỗi bước loại bỏ nửa mảng
   * Space: O(1) - Chỉ dùng biến con trỏ
   */
  static class ImperativeSolution {
    public int search(int[] nums, int target) {
      int left = 0;
      int right = nums.length - 1;

      while (left <= right) {
        int mid = left + (right - left) / 2; // Tránh overflow

        if (nums[mid] == target) {
          return mid;
        } else if (nums[mid] < target) {
          left = mid + 1; // Tìm bên phải
        } else {
          right = mid - 1; // Tìm bên trái
        }
      }

      return -1; // Không tìm thấy
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  /**
   * Sử dụng Java Arrays.binarySearch() - wrapper function.
   * Hoặc cách đệ quy (tail recursion).
   */
  static class FunctionalSolution {
    public int search(int[] nums, int target) {
      return binarySearchRecursive(nums, target, 0, nums.length - 1);
    }

    /**
     * Đệ quy: Giống imperative nhưng dùng đệ quy thay vòng lặp.
     * Tail recursion - có thể tối ưu bởi compiler.
     */
    private int binarySearchRecursive(int[] nums, int target, int left, int right) {
      if (left > right)
        return -1;

      int mid = left + (right - left) / 2;

      if (nums[mid] == target)
        return mid;

      return nums[mid] < target
          ? binarySearchRecursive(nums, target, mid + 1, right)
          : binarySearchRecursive(nums, target, left, mid - 1);
    }
  }

  // ========== TESTS ==========
  private final ImperativeSolution imp = new ImperativeSolution();
  private final FunctionalSolution func = new FunctionalSolution();

  @Test
  @DisplayName("Target exists in middle")
  void testTargetFound() {
    assertThat(imp.search(new int[] { -1, 0, 3, 5, 9, 12 }, 9)).isEqualTo(4);
    assertThat(func.search(new int[] { -1, 0, 3, 5, 9, 12 }, 9)).isEqualTo(4);
  }

  @Test
  @DisplayName("Target not found")
  void testTargetNotFound() {
    assertThat(imp.search(new int[] { -1, 0, 3, 5, 9, 12 }, 2)).isEqualTo(-1);
    assertThat(func.search(new int[] { -1, 0, 3, 5, 9, 12 }, 2)).isEqualTo(-1);
  }
}
