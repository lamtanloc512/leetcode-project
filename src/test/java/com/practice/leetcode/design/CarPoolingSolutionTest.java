package com.practice.leetcode.design;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CAR POOLING - Ghép xe đi chung
 * 
 * Bài toán: Có 1 xe với capacity chỗ ngồi. Cho danh sách trips[i] =
 * [passengers, from, to]
 * - passengers: số hành khách
 * - from: điểm đón
 * - to: điểm trả
 * 
 * Hỏi: Xe có thể chở hết được không?
 * 
 * Ví dụ thực tế:
 * - Xe buýt đưa đón
 * - Uber Pool / Grab Share
 */
class CarPoolingSolutionTest {

  /**
   * THUẬT TOÁN: Difference Array (Mảng hiệu)
   * 
   * Ý tưởng:
   * - Tạo mảng đếm thay đổi số người tại mỗi điểm
   * - Tại điểm from: +passengers (lên xe)
   * - Tại điểm to: -passengers (xuống xe)
   * - Duyệt qua, nếu tổng > capacity → return false
   * 
   * Ví dụ: capacity=4, trips=[[2,1,5], [3,3,7]]
   * 
   * Mảng thay đổi (diff):
   * - Trip [2,1,5]: diff[1] += 2, diff[5] -= 2
   * - Trip [3,3,7]: diff[3] += 3, diff[7] -= 3
   * 
   * diff = [0, +2, 0, +3, 0, -2, 0, -3]
   * 
   * Tính prefix sum (số người trên xe):
   * Điểm 0: 0
   * Điểm 1: 0 + 2 = 2
   * Điểm 2: 2 + 0 = 2
   * Điểm 3: 2 + 3 = 5 > capacity=4 → FALSE!
   * 
   * Kết quả: Không thể chở hết
   * 
   * Time: O(n + maxLocation)
   * Space: O(maxLocation)
   */
  static class Solution {
    public boolean carPooling(int[][] trips, int capacity) {
      // Tìm điểm xa nhất
      int maxLoc = 0;
      for (int[] trip : trips) {
        maxLoc = Math.max(maxLoc, trip[2]);
      }

      // Mảng đếm thay đổi số người
      int[] diff = new int[maxLoc + 1];

      for (int[] trip : trips) {
        int passengers = trip[0];
        int from = trip[1];
        int to = trip[2];

        diff[from] += passengers; // Lên xe
        diff[to] -= passengers; // Xuống xe
      }

      // Tính prefix sum và kiểm tra
      int currentPassengers = 0;
      for (int change : diff) {
        currentPassengers += change;
        if (currentPassengers > capacity) {
          return false;
        }
      }

      return true;
    }
  }

  /**
   * CÁCH 2: TreeMap (Tối ưu khi điểm thưa)
   * 
   * Dùng TreeMap khi số điểm ít nhưng giá trị lớn
   */
  static class TreeMapSolution {
    public boolean carPooling(int[][] trips, int capacity) {
      TreeMap<Integer, Integer> timeline = new TreeMap<>();

      for (int[] trip : trips) {
        int passengers = trip[0];
        int from = trip[1];
        int to = trip[2];

        timeline.merge(from, passengers, Integer::sum);
        timeline.merge(to, -passengers, Integer::sum);
      }

      int currentPassengers = 0;
      for (int change : timeline.values()) {
        currentPassengers += change;
        if (currentPassengers > capacity) {
          return false;
        }
      }

      return true;
    }
  }

  // ========== TESTS ==========
  private final Solution solution = new Solution();
  private final TreeMapSolution treemapSolution = new TreeMapSolution();

  @Test
  @DisplayName("Car pooling feasibility")
  void testCarPooling() {
    // Không đủ chỗ
    assertThat(solution.carPooling(new int[][] { { 2, 1, 5 }, { 3, 3, 7 } }, 4)).isFalse();

    // Đủ chỗ
    assertThat(solution.carPooling(new int[][] { { 2, 1, 5 }, { 3, 3, 7 } }, 5)).isTrue();

    // TreeMap solution
    assertThat(treemapSolution.carPooling(new int[][] { { 2, 1, 5 }, { 3, 5, 7 } }, 3)).isTrue();
  }
}
