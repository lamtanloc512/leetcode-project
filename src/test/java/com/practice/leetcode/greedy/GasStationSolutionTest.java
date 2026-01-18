package com.practice.leetcode.greedy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * GAS STATION - Trạm xăng
 * 
 * Bài toán: Có n trạm xăng trên đường vòng.
 * - gas[i]: lượng xăng tại trạm i
 * - cost[i]: chi phí xăng để đi từ trạm i đến trạm i+1
 * 
 * Tìm trạm xuất phát để có thể đi hết 1 vòng, hoặc -1 nếu không thể.
 */
class GasStationSolutionTest {

  /**
   * THUẬT TOÁN: Greedy - Một lần duyệt
   * 
   * Quan sát quan trọng:
   * 1. Nếu tổng gas >= tổng cost → LUÔN có lời giải
   * 2. Nếu từ trạm A không đến được trạm B, thì các trạm giữa A và B
   * cũng không đến được B → Bắt đầu từ B+1
   * 
   * Ví dụ: gas = [1,2,3,4,5], cost = [3,4,5,1,2]
   * 
   * Tổng gas = 15, Tổng cost = 15 → Có lời giải
   * 
   * i=0: tank = 0 + 1 - 3 = -2 < 0 → Reset! start = 1
   * i=1: tank = 0 + 2 - 4 = -2 < 0 → Reset! start = 2
   * i=2: tank = 0 + 3 - 5 = -2 < 0 → Reset! start = 3
   * i=3: tank = 0 + 4 - 1 = 3 >= 0 ✓
   * i=4: tank = 3 + 5 - 2 = 6 >= 0 ✓
   * 
   * Kiểm tra: Xuất phát từ 3:
   * 3 → 4: tank = 4 - 1 = 3
   * 4 → 0: tank = 3 + 5 - 2 = 6
   * 0 → 1: tank = 6 + 1 - 3 = 4
   * 1 → 2: tank = 4 + 2 - 4 = 2
   * 2 → 3: tank = 2 + 3 - 5 = 0 → Về đích!
   * 
   * Tại sao chỉ cần 1 lần duyệt?
   * - Nếu tổng gas >= tổng cost → Đảm bảo có đáp án
   * - Điểm start cuối cùng không bị reset là đáp án duy nhất
   * 
   * Time: O(n)
   * Space: O(1)
   */
  static class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
      int totalGas = 0;
      int totalCost = 0;
      int tank = 0;
      int start = 0;

      for (int i = 0; i < gas.length; i++) {
        totalGas += gas[i];
        totalCost += cost[i];
        tank += gas[i] - cost[i];

        if (tank < 0) {
          // Không thể đến được từ start → Reset
          start = i + 1;
          tank = 0;
        }
      }

      // Kiểm tra có đủ xăng tổng hay không
      return totalGas >= totalCost ? start : -1;
    }
  }

  // ========== TESTS ==========
  private final Solution solution = new Solution();

  @Test
  @DisplayName("Find starting gas station")
  void testGasStation() {
    assertThat(solution.canCompleteCircuit(
        new int[] { 1, 2, 3, 4, 5 },
        new int[] { 3, 4, 5, 1, 2 })).isEqualTo(3);

    assertThat(solution.canCompleteCircuit(
        new int[] { 2, 3, 4 },
        new int[] { 3, 4, 3 })).isEqualTo(-1);
  }
}
