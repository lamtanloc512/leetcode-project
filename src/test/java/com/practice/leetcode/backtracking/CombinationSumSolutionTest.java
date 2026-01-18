package com.practice.leetcode.backtracking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Combination Sum problem.
 */
class CombinationSumSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Backtracking với phần tử có thể dùng lại
   * 
   * Khác với Subsets: Mỗi phần tử có thể được chọn NHIỀU LẦN.
   * 
   * Ý tưởng:
   * - Duyệt qua từng ứng viên
   * - Với mỗi ứng viên, CÓ THỂ chọn lại (không tăng index)
   * - Dừng khi tổng = target hoặc > target
   * 
   * Cây quyết định cho candidates = [2,3,6,7], target = 7:
   * 
   * [] (target=7)
   * / | | \
   * [2] [3] [6] [7] ✓
   * (rem=5) (rem=4) (rem=1) (rem=0)
   * / | \ / \ |
   * [2,2] [2,3] [2,6] [3,3] [3,6] X
   * (rem=3)(rem=2) X (rem=1) X
   * / \ |
   * [2,2,2] [2,2,3] [2,3,3]
   * (rem=1) (rem=0)✓ X
   * |
   * [2,2,2,2]
   * X (rem<0)
   * 
   * Kết quả: [[2,2,3], [7]]
   * 
   * Tránh trùng lặp:
   * - Chỉ xét từ index hiện tại trở đi (không quay lại)
   * - [2,3] và [3,2] chỉ được tính 1 lần là [2,3]
   * 
   * Time: O(n^(target/min)) trong worst case
   * Space: O(target/min) - độ sâu đệ quy
   */
  static class ImperativeSolution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
      List<List<Integer>> result = new ArrayList<>();
      backtrack(candidates, target, 0, new ArrayList<>(), result);
      return result;
    }

    private void backtrack(int[] candidates, int remaining, int start,
        List<Integer> current, List<List<Integer>> result) {
      // Base cases
      if (remaining == 0) {
        // Tìm thấy tổ hợp hợp lệ
        result.add(new ArrayList<>(current));
        return;
      }
      if (remaining < 0) {
        // Tổng vượt quá target
        return;
      }

      // Thử từng ứng viên từ start (tránh trùng)
      for (int i = start; i < candidates.length; i++) {
        current.add(candidates[i]);
        // i (không phải i+1) vì có thể dùng lại cùng phần tử
        backtrack(candidates, remaining - candidates[i], i, current, result);
        current.remove(current.size() - 1); // BACKTRACK
      }
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  /**
   * Cùng ý tưởng, cấu trúc code khác.
   */
  static class FunctionalSolution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
      List<List<Integer>> result = new ArrayList<>();
      Arrays.sort(candidates); // Sắp xếp để tối ưu pruning
      backtrack(candidates, target, 0, new ArrayList<>(), result);
      return result;
    }

    private void backtrack(int[] candidates, int remaining, int start,
        List<Integer> current, List<List<Integer>> result) {
      if (remaining == 0) {
        result.add(new ArrayList<>(current));
        return;
      }

      for (int i = start; i < candidates.length; i++) {
        // Pruning: Nếu phần tử hiện tại > remaining, các phần tử sau cũng vậy
        if (candidates[i] > remaining)
          break;

        current.add(candidates[i]);
        backtrack(candidates, remaining - candidates[i], i, current, result);
        current.remove(current.size() - 1);
      }
    }
  }

  // ========== TESTS ==========
  private final ImperativeSolution imp = new ImperativeSolution();
  private final FunctionalSolution func = new FunctionalSolution();

  @Test
  @DisplayName("Combination Sum - find all unique combinations")
  void testCombinationSum() {
    List<List<Integer>> result1 = imp.combinationSum(new int[] { 2, 3, 6, 7 }, 7);
    assertThat(result1).hasSize(2);

    List<List<Integer>> result2 = func.combinationSum(new int[] { 2, 3, 5 }, 8);
    assertThat(result2).hasSize(3); // [[2,2,2,2], [2,3,3], [3,5]]
  }
}
