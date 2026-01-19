package com.practice.leetcode.dp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Climbing Stairs problem.
 */
class ClimbingStairsSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Dynamic Programming (Fibonacci biến thể)
   * 
   * Phân tích bài toán:
   * - Để lên bậc thứ n, ta có thể:
   * + Bước 1 bậc từ bậc (n-1), HOẶC
   * + Bước 2 bậc từ bậc (n-2)
   * 
   * Công thức truy hồi:
   * f(n) = f(n-1) + f(n-2)
   * 
   * Base cases:
   * f(1) = 1 (chỉ có 1 cách: bước 1 bậc)
   * f(2) = 2 (2 cách: 1+1 hoặc 2)
   * 
   * Đây chính là dãy Fibonacci!
   * 
   * Ví dụ: n = 5
   * 
   * n: 1 2 3 4 5
   * f(n): 1 2 3 5 8
   * 
   * f(3) = f(2) + f(1) = 2 + 1 = 3
   * f(4) = f(3) + f(2) = 3 + 2 = 5
   * f(5) = f(4) + f(3) = 5 + 3 = 8
   * 
   * Tối ưu không gian:
   * - Chỉ cần lưu 2 giá trị trước đó (không cần array)
   * 
   * Time: O(n)
   * Space: O(1) - chỉ dùng 2 biến
   */
  static class ImperativeSolution {
    public int climbStairs(int n) {
      if (n <= 2)
        return n;

      int prev2 = 1; // f(n-2)
      int prev1 = 2; // f(n-1)

      for (int i = 3; i <= n; i++) {
        int current = prev1 + prev2;
        prev2 = prev1;
        prev1 = current;
      }

      return prev1;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  /**
   * Dùng reduce để tính Fibonacci.
   * Mỗi bước: (a, b) → (b, a+b)
   */
  static class FunctionalSolution {
    public int climbStairs(int n) {
      if (n <= 2)
        return n;

      // [prev2, prev1] → reduce qua n-2 bước
      int[] result = IntStream.range(2, n)
          .boxed()
          .reduce(
              new int[] { 1, 2 }, // Initial: f(1), f(2)
              (state, i) -> new int[] { state[1], state[0] + state[1] },
              (a, b) -> a);

      return result[1];
    }
  }

  // ========== TESTS ==========
  private final ImperativeSolution imp = new ImperativeSolution();
  private final FunctionalSolution func = new FunctionalSolution();

  @Test
  @DisplayName("Climbing stairs - Fibonacci pattern")
  void testClimbStairs() {
    assertThat(imp.climbStairs(2)).isEqualTo(2);
    assertThat(imp.climbStairs(3)).isEqualTo(3);
    assertThat(imp.climbStairs(5)).isEqualTo(8);
    assertThat(func.climbStairs(5)).isEqualTo(8);
  }
}
