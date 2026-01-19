package com.practice.leetcode.dp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Coin Change problem.
 */
class CoinChangeSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Dynamic Programming - Unbounded Knapsack
   * 
   * Định nghĩa:
   * dp[i] = Số đồng xu TỐI THIỂU để tạo số tiền i
   * 
   * Ý tưởng:
   * - Với mỗi số tiền i, thử dùng từng loại coin
   * - Nếu dùng coin, ta cần dp[i - coin] + 1 đồng
   * - Lấy min của tất cả các cách
   * 
   * Công thức:
   * dp[i] = min(dp[i], dp[i - coin] + 1) với mỗi coin <= i
   * 
   * Base case:
   * dp[0] = 0 (không cần đồng xu nào để tạo 0)
   * dp[i] = ∞ (ban đầu, chưa có cách nào)
   * 
   * Ví dụ: coins = [1,2,5], amount = 11
   * 
   * dp[0] = 0
   * dp[1] = dp[0]+1 = 1 (1 xu: 1)
   * dp[2] = min(dp[1]+1, dp[0]+1) = 1 (1 xu: 2)
   * dp[3] = min(dp[2]+1, dp[1]+1) = 2 (2 xu: 2+1)
   * dp[5] = min(dp[4]+1, dp[3]+1, dp[0]+1) = 1 (1 xu: 5)
   * ...
   * dp[11] = 3 (5+5+1)
   * 
   * Time: O(amount × coins.length)
   * Space: O(amount)
   */
  static class ImperativeSolution {
    public int coinChange(int[] coins, int amount) {
      int[] dp = new int[amount + 1];
      Arrays.fill(dp, amount + 1); // Dùng amount+1 thay vì MAX_VALUE để tránh overflow
      dp[0] = 0;

      for (int i = 1; i <= amount; i++) {
        for (int coin : coins) {
          if (coin <= i) {
            dp[i] = Math.min(dp[i], dp[i - coin] + 1);
          }
        }
      }

      return dp[amount] > amount ? -1 : dp[amount];
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  /**
   * Cách tiếp cận tương tự nhưng dùng stream cho inner loop.
   */
  static class FunctionalSolution {
    public int coinChange(int[] coins, int amount) {
      int[] dp = new int[amount + 1];
      Arrays.fill(dp, amount + 1);
      dp[0] = 0;

      for (int i = 1; i <= amount; i++) {
        final int current = i;
        dp[i] = Arrays.stream(coins)
            .filter(coin -> coin <= current)
            .map(coin -> dp[current - coin] + 1)
            .min()
            .orElse(dp[i]);

        dp[i] = Math.min(dp[i], amount + 1);
      }

      return dp[amount] > amount ? -1 : dp[amount];
    }
  }

  // ========== TESTS ==========
  private final ImperativeSolution imp = new ImperativeSolution();
  private final FunctionalSolution func = new FunctionalSolution();

  @Test
  @DisplayName("Coin Change - minimum coins")
  void testCoinChange() {
    assertThat(imp.coinChange(new int[] { 1, 2, 5 }, 11)).isEqualTo(3);
    assertThat(imp.coinChange(new int[] { 2 }, 3)).isEqualTo(-1);
    assertThat(func.coinChange(new int[] { 1, 2, 5 }, 11)).isEqualTo(3);
  }
}
