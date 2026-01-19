package com.practice.leetcode.dp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Longest Increasing Subsequence problem.
 */
class LongestIncreasingSubsequenceSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN 1: Dynamic Programming O(n²)
   * 
   * Định nghĩa:
   * dp[i] = Độ dài LIS kết thúc tại index i
   * 
   * Ý tưởng:
   * - Với mỗi vị trí i, xét tất cả vị trí j < i
   * - Nếu nums[j] < nums[i], ta có thể mở rộng LIS kết thúc tại j
   * - dp[i] = max(dp[j] + 1) với mọi j < i mà nums[j] < nums[i]
   * 
   * Ví dụ: nums = [10,9,2,5,3,7,101,18]
   * 
   * i=0: dp[0]=1 (LIS=[10])
   * i=1: dp[1]=1 (9 không thể mở rộng từ 10)
   * i=2: dp[2]=1 (2 không thể mở rộng)
   * i=3: dp[3]=2 (2<5, mở rộng từ 2: [2,5])
   * i=4: dp[4]=2 (2<3: [2,3])
   * i=5: dp[5]=3 (5<7 hoặc 3<7: [2,5,7] hoặc [2,3,7])
   * i=6: dp[6]=4 ([2,5,7,101])
   * i=7: dp[7]=4 ([2,5,7,18])
   * 
   * Time: O(n²)
   * Space: O(n)
   */
  static class ImperativeSolution {
    public int lengthOfLIS(int[] nums) {
      int n = nums.length;
      int[] dp = new int[n];
      Arrays.fill(dp, 1);

      int maxLen = 1;

      for (int i = 1; i < n; i++) {
        for (int j = 0; j < i; j++) {
          if (nums[j] < nums[i]) {
            dp[i] = Math.max(dp[i], dp[j] + 1);
          }
        }
        maxLen = Math.max(maxLen, dp[i]);
      }

      return maxLen;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  /**
   * THUẬT TOÁN 2: Binary Search O(n log n)
   * 
   * Ý tưởng: Duy trì mảng "tails" chứa phần tử nhỏ nhất kết thúc LIS có độ dài i.
   * 
   * Với mỗi phần tử num:
   * - Tìm vị trí đầu tiên trong tails >= num (binary search)
   * - Nếu tìm thấy: thay thế (để giữ giá trị nhỏ hơn)
   * - Nếu không: thêm vào cuối (mở rộng LIS)
   * 
   * Ví dụ: nums = [10,9,2,5,3,7,101,18]
   * 
   * num=10: tails=[10]
   * num=9: tails=[9] (9<10, thay thế)
   * num=2: tails=[2] (2<9, thay thế)
   * num=5: tails=[2,5] (5>2, thêm vào)
   * num=3: tails=[2,3] (3<5 nhưng >2, thay thế 5)
   * num=7: tails=[2,3,7]
   * num=101: tails=[2,3,7,101]
   * num=18: tails=[2,3,7,18] (18<101, thay thế)
   * 
   * Độ dài tails = 4 = LIS length
   * 
   * Lưu ý: tails KHÔNG phải là LIS thực tế, chỉ là công cụ đếm!
   * 
   * Time: O(n log n)
   * Space: O(n)
   */
  static class FunctionalSolution {
    public int lengthOfLIS(int[] nums) {
      List<Integer> tails = new ArrayList<>();

      for (int num : nums) {
        int pos = binarySearch(tails, num);

        if (pos == tails.size()) {
          tails.add(num);
        } else {
          tails.set(pos, num);
        }
      }

      return tails.size();
    }

    // Tìm vị trí đầu tiên >= target
    private int binarySearch(List<Integer> list, int target) {
      int left = 0, right = list.size();
      while (left < right) {
        int mid = left + (right - left) / 2;
        if (list.get(mid) >= target) {
          right = mid;
        } else {
          left = mid + 1;
        }
      }
      return left;
    }
  }

  // ========== TESTS ==========
  private final ImperativeSolution imp = new ImperativeSolution();
  private final FunctionalSolution func = new FunctionalSolution();

  @Test
  @DisplayName("LIS - find longest increasing subsequence length")
  void testLIS() {
    assertThat(imp.lengthOfLIS(new int[] { 10, 9, 2, 5, 3, 7, 101, 18 })).isEqualTo(4);
    assertThat(imp.lengthOfLIS(new int[] { 0, 1, 0, 3, 2, 3 })).isEqualTo(4);
    assertThat(func.lengthOfLIS(new int[] { 10, 9, 2, 5, 3, 7, 101, 18 })).isEqualTo(4);
  }
}
