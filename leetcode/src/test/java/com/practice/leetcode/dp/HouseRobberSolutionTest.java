package com.practice.leetcode.dp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for House Robber problem.
 */
class HouseRobberSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Dynamic Programming với quyết định cướp/bỏ
   * 
   * Bài toán: Cướp các nhà không liền kề để tối đa hóa tiền.
   * 
   * Định nghĩa:
   * dp[i] = Số tiền tối đa có thể cướp từ nhà 0 đến nhà i
   * 
   * Tại mỗi nhà i, có 2 lựa chọn:
   * 1. CƯỚP nhà i: Lấy nums[i] + dp[i-2] (bỏ nhà i-1)
   * 2. BỎ QUA nhà i: Giữ nguyên dp[i-1]
   * 
   * Công thức:
   * dp[i] = max(dp[i-1], dp[i-2] + nums[i])
   * 
   * Ví dụ: nums = [2, 7, 9, 3, 1]
   * 
   * i=0: dp[0] = 2 (chỉ có 1 nhà)
   * i=1: dp[1] = max(2, 7) = 7 (chọn nhà 1)
   * i=2: dp[2] = max(7, 2+9) = 11 (cướp nhà 0,2)
   * i=3: dp[3] = max(11, 7+3) = 11 (vẫn giữ nhà 0,2)
   * i=4: dp[4] = max(11, 11+1) = 12 (cướp nhà 0,2,4)
   * 
   * Kết quả: 12 (cướp nhà 0, 2, 4 → 2+9+1=12)
   * 
   * Tối ưu không gian: Chỉ cần 2 biến prev1 và prev2
   * 
   * Time: O(n)
   * Space: O(1)
   */
  static class ImperativeSolution {
    public int rob(int[] nums) {
      if (nums.length == 1)
        return nums[0];

      int prev2 = 0; // dp[i-2]
      int prev1 = nums[0]; // dp[i-1]

      for (int i = 1; i < nums.length; i++) {
        int current = Math.max(prev1, prev2 + nums[i]);
        prev2 = prev1;
        prev1 = current;
      }

      return prev1;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  /**
   * Dùng reduce với state là (prev2, prev1).
   */
  static class FunctionalSolution {
    public int rob(int[] nums) {
      if (nums.length == 1)
        return nums[0];

      int[] state = { 0, nums[0] }; // [prev2, prev1]

      for (int i = 1; i < nums.length; i++) {
        int current = Math.max(state[1], state[0] + nums[i]);
        state[0] = state[1];
        state[1] = current;
      }

      return state[1];
    }
  }

  // ========== TESTS ==========
  private final ImperativeSolution imp = new ImperativeSolution();
  private final FunctionalSolution func = new FunctionalSolution();

  @Test
  @DisplayName("House Robber - maximize non-adjacent sum")
  void testRob() {
    assertThat(imp.rob(new int[] { 1, 2, 3, 1 })).isEqualTo(4);
    assertThat(imp.rob(new int[] { 2, 7, 9, 3, 1 })).isEqualTo(12);
    assertThat(func.rob(new int[] { 2, 7, 9, 3, 1 })).isEqualTo(12);
  }
}
