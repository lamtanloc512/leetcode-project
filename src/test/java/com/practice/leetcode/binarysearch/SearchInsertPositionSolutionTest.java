package com.practice.leetcode.binarysearch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Search Insert Position problem.
 */
class SearchInsertPositionSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Binary Search tìm Lower Bound
   * 
   * Ý tưởng: Tìm vị trí nhỏ nhất mà nums[pos] >= target
   * 
   * Khác Binary Search thông thường:
   * - Không return ngay khi tìm thấy
   * - Khi kết thúc, left chính là vị trí chèn
   * 
   * Cách hoạt động:
   * 1. Nếu nums[mid] >= target: right = mid (có thể là đáp án)
   * 2. Nếu nums[mid] < target: left = mid + 1 (chắc chắn không phải)
   * 3. Khi left == right, đó là vị trí chèn
   * 
   * Ví dụ: nums = [1,3,5,6], target = 2
   * 
   * Bước 1: left=0, right=4, mid=2, nums[2]=5 >= 2 → right=2
   * Bước 2: left=0, right=2, mid=1, nums[1]=3 >= 2 → right=1
   * Bước 3: left=0, right=1, mid=0, nums[0]=1 < 2 → left=1
   * Kết thúc: left=1 → Chèn tại vị trí 1
   * 
   * Time: O(log n)
   * Space: O(1)
   */
  static class ImperativeSolution {
    public int searchInsert(int[] nums, int target) {
      int left = 0;
      int right = nums.length;

      while (left < right) {
        int mid = left + (right - left) / 2;

        if (nums[mid] >= target) {
          right = mid;
        } else {
          left = mid + 1;
        }
      }

      return left;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  /**
   * Đệ quy với cùng logic lower bound.
   */
  static class FunctionalSolution {
    public int searchInsert(int[] nums, int target) {
      return findLowerBound(nums, target, 0, nums.length);
    }

    private int findLowerBound(int[] nums, int target, int left, int right) {
      if (left >= right)
        return left;

      int mid = left + (right - left) / 2;

      return nums[mid] >= target
          ? findLowerBound(nums, target, left, mid)
          : findLowerBound(nums, target, mid + 1, right);
    }
  }

  // ========== TESTS ==========
  private final ImperativeSolution imp = new ImperativeSolution();
  private final FunctionalSolution func = new FunctionalSolution();

  @Test
  @DisplayName("Target found and insert position scenarios")
  void testSearchInsert() {
    // Target exists
    assertThat(imp.searchInsert(new int[] { 1, 3, 5, 6 }, 5)).isEqualTo(2);
    // Insert in middle
    assertThat(imp.searchInsert(new int[] { 1, 3, 5, 6 }, 2)).isEqualTo(1);
    // Insert at end
    assertThat(imp.searchInsert(new int[] { 1, 3, 5, 6 }, 7)).isEqualTo(4);

    assertThat(func.searchInsert(new int[] { 1, 3, 5, 6 }, 5)).isEqualTo(2);
  }
}
