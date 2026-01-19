package com.practice.leetcode.backtracking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Permutations problem.
 */
class PermutationsSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Backtracking với Swap
   * 
   * Ý tưởng: Với mỗi vị trí, thử đặt tất cả các phần tử có thể vào đó.
   * 
   * Cây quyết định cho nums = [1,2,3]:
   * 
   * [1,2,3]
   * / | \
   * [1,2,3] [2,1,3] [3,2,1]
   * swap(0,0) swap(0,1) swap(0,2)
   * / \ / \ / \
   * [1,2,3] [1,3,2] [2,1,3] [2,3,1] [3,2,1] [3,1,2]
   * swap(1,1) swap(1,2) ...
   * 
   * Thuật toán:
   * 1. Với vị trí index, thử swap với tất cả vị trí từ index đến n-1
   * 2. Sau mỗi swap, đệ quy xuống vị trí tiếp theo
   * 3. BACKTRACK: swap lại để khôi phục trạng thái ban đầu
   * 4. Khi index == n: đã hoàn thành 1 hoán vị
   * 
   * Tại sao swap hoạt động?
   * - swap(index, i) đặt phần tử thứ i vào vị trí index
   * - Các lần đệ quy tiếp theo sẽ hoán vị phần còn lại
   * 
   * Time: O(n × n!) - có n! hoán vị, mỗi cái copy O(n)
   * Space: O(n) - độ sâu đệ quy
   */
  static class ImperativeSolution {
    public List<List<Integer>> permute(int[] nums) {
      List<List<Integer>> result = new ArrayList<>();
      backtrack(nums, 0, result);
      return result;
    }

    private void backtrack(int[] nums, int index, List<List<Integer>> result) {
      if (index == nums.length) {
        // Hoàn thành một hoán vị - thêm vào kết quả
        List<Integer> permutation = new ArrayList<>();
        for (int num : nums)
          permutation.add(num);
        result.add(permutation);
        return;
      }

      for (int i = index; i < nums.length; i++) {
        swap(nums, index, i); // CHỌN: đặt nums[i] vào vị trí index
        backtrack(nums, index + 1, result);
        swap(nums, index, i); // BACKTRACK: hoàn tác
      }
    }

    private void swap(int[] nums, int i, int j) {
      int temp = nums[i];
      nums[i] = nums[j];
      nums[j] = temp;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  /**
   * THUẬT TOÁN 2: Backtracking với Used Array
   * 
   * Ý tưởng khác: Dùng mảng boolean đánh dấu phần tử đã dùng.
   * 
   * - Xây dựng hoán vị từng phần tử một
   * - Với mỗi vị trí, chọn phần tử chưa dùng
   * - Đánh dấu đã dùng, đệ quy, bỏ đánh dấu
   * 
   * Ưu điểm: Dễ hiểu hơn, không thay đổi mảng gốc
   */
  static class FunctionalSolution {
    public List<List<Integer>> permute(int[] nums) {
      List<List<Integer>> result = new ArrayList<>();
      boolean[] used = new boolean[nums.length];
      backtrack(nums, used, new ArrayList<>(), result);
      return result;
    }

    private void backtrack(int[] nums, boolean[] used,
        List<Integer> current, List<List<Integer>> result) {
      if (current.size() == nums.length) {
        result.add(new ArrayList<>(current));
        return;
      }

      for (int i = 0; i < nums.length; i++) {
        if (!used[i]) {
          used[i] = true; // Đánh dấu
          current.add(nums[i]); // CHỌN
          backtrack(nums, used, current, result);
          current.remove(current.size() - 1); // BACKTRACK
          used[i] = false; // Bỏ đánh dấu
        }
      }
    }
  }

  // ========== TESTS ==========
  private final ImperativeSolution imp = new ImperativeSolution();
  private final FunctionalSolution func = new FunctionalSolution();

  @Test
  @DisplayName("Permutations - generate all permutations")
  void testPermutations() {
    List<List<Integer>> result1 = imp.permute(new int[] { 1, 2, 3 });
    assertThat(result1).hasSize(6); // 3! = 6

    List<List<Integer>> result2 = func.permute(new int[] { 1, 2, 3 });
    assertThat(result2).hasSize(6);
  }
}
