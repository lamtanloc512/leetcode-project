package com.practice.leetcode.backtracking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Subsets problem.
 */
class SubsetsSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Backtracking (Quay lui)
   * 
   * Ý tưởng: Với mỗi phần tử, ta có 2 lựa chọn: CHỌN hoặc KHÔNG CHỌN.
   * 
   * Cây quyết định cho nums = [1,2,3]:
   * 
   * []
   * / \
   * [1] []
   * / \ / \
   * [1,2] [1] [2] []
   * / \ / \ / \ / \
   * [1,2,3][1,2][1,3][1] [2,3][2] [3] []
   * 
   * Các nút lá là tất cả subsets: [], [1], [2], [3], [1,2], [1,3], [2,3], [1,2,3]
   * 
   * Thuật toán:
   * 1. Bắt đầu với tập rỗng
   * 2. Với mỗi vị trí index từ 0 đến n-1:
   * a. CHỌN phần tử: thêm nums[index] vào current, đệ quy với index+1
   * b. BACKTRACK: loại bỏ phần tử vừa thêm
   * c. KHÔNG CHỌN: đệ quy với index+1 (đã tự động bỏ qua)
   * 3. Khi index == n: đã xét hết, thêm current vào result
   * 
   * Cách triển khai (dùng start index):
   * - backtrack(start) xét tất cả phần tử từ start đến n-1
   * - Mỗi vị trí đều có thể bắt đầu subset mới
   * 
   * Time: O(n × 2^n) - có 2^n subsets, mỗi subset copy O(n)
   * Space: O(n) - độ sâu đệ quy
   */
  static class ImperativeSolution {
    public List<List<Integer>> subsets(int[] nums) {
      List<List<Integer>> result = new ArrayList<>();
      backtrack(nums, 0, new ArrayList<>(), result);
      return result;
    }

    private void backtrack(int[] nums, int start, List<Integer> current,
        List<List<Integer>> result) {
      // Thêm subset hiện tại (mọi trạng thái đều là subset hợp lệ)
      result.add(new ArrayList<>(current));

      // Thử thêm từng phần tử từ start
      for (int i = start; i < nums.length; i++) {
        current.add(nums[i]); // CHỌN
        backtrack(nums, i + 1, current, result); // Đệ quy
        current.remove(current.size() - 1); // BACKTRACK (bỏ chọn)
      }
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  /**
   * THUẬT TOÁN 2: Iterative (Lặp)
   * 
   * Ý tưởng: Xây dựng dần bằng cách thêm phần tử mới vào tất cả subset hiện có.
   * 
   * Ví dụ: nums = [1,2,3]
   * 
   * Bắt đầu: [[]]
   * Thêm 1: [[], [1]]
   * Thêm 2: [[], [1], [2], [1,2]]
   * Thêm 3: [[], [1], [2], [1,2], [3], [1,3], [2,3], [1,2,3]]
   * 
   * Với mỗi phần tử mới, tạo bản sao của TẤT CẢ subset và thêm phần tử đó.
   * 
   * Time: O(n × 2^n)
   * Space: O(2^n) cho kết quả
   */
  static class FunctionalSolution {
    public List<List<Integer>> subsets(int[] nums) {
      List<List<Integer>> result = new ArrayList<>();
      result.add(new ArrayList<>()); // Bắt đầu với subset rỗng

      for (int num : nums) {
        // Tạo subset mới bằng cách thêm num vào mỗi subset hiện có
        List<List<Integer>> newSubsets = new ArrayList<>();
        for (List<Integer> subset : result) {
          List<Integer> newSubset = new ArrayList<>(subset);
          newSubset.add(num);
          newSubsets.add(newSubset);
        }
        result.addAll(newSubsets);
      }

      return result;
    }
  }

  // ========== TESTS ==========
  private final ImperativeSolution imp = new ImperativeSolution();
  private final FunctionalSolution func = new FunctionalSolution();

  @Test
  @DisplayName("Subsets - generate power set")
  void testSubsets() {
    List<List<Integer>> result1 = imp.subsets(new int[] { 1, 2, 3 });
    assertThat(result1).hasSize(8); // 2^3 = 8 subsets

    List<List<Integer>> result2 = func.subsets(new int[] { 1, 2, 3 });
    assertThat(result2).hasSize(8);
  }
}
