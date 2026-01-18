package com.practice.leetcode.greedy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MINIMUM NUMBER OF PLATFORMS - Số sân ga tối thiểu
 * 
 * Bài toán: Cho arrivals[] và departures[] của các chuyến tàu.
 * Tìm số sân ga TỐI THIỂU để không có 2 tàu đỗ cùng 1 sân ga.
 * 
 * Đây là bài kinh điển trong phỏng vấn - tương tự Meeting Rooms II!
 */
class MinimumPlatformsSolutionTest {

  /**
   * THUẬT TOÁN: Merge 2 Sorted Arrays
   * 
   * Giống Meeting Rooms II nhưng dùng 2 con trỏ.
   * 
   * Ý tưởng:
   * 1. Sort arrivals và departures riêng
   * 2. Duyệt song song, nếu arrival <= departure → Tàu mới đến trước khi tàu cũ
   * đi
   * 3. Đếm số tàu đang đỗ đồng thời
   * 
   * Ví dụ:
   * arrivals = [9:00, 9:40, 9:50, 11:00, 15:00, 18:00]
   * departures = [9:10, 12:00, 11:20, 11:30, 19:00, 20:00]
   * 
   * Sort: arr = [9:00, 9:40, 9:50, 11:00, 15:00, 18:00]
   * dep = [9:10, 11:20, 11:30, 12:00, 19:00, 20:00]
   * 
   * i=0,j=0: 9:00 <= 9:10 → Tàu đến, platforms=1
   * i=1,j=0: 9:40 > 9:10 → Tàu đi, platforms=0, j++
   * i=1,j=1: 9:40 <= 11:20 → Tàu đến, platforms=1
   * i=2,j=1: 9:50 <= 11:20 → Tàu đến, platforms=2
   * i=3,j=1: 11:00 <= 11:20 → Tàu đến, platforms=3 ← MAX
   * i=4,j=1: 15:00 > 11:20 → Tàu đi, platforms=2
   * ...
   * 
   * Kết quả: 3 sân ga
   * 
   * Time: O(n log n)
   * Space: O(1) nếu cho phép modify input
   */
  static class Solution {
    public int findPlatform(int[] arr, int[] dep) {
      Arrays.sort(arr);
      Arrays.sort(dep);

      int n = arr.length;
      int platforms = 0;
      int maxPlatforms = 0;

      int i = 0; // arrival pointer
      int j = 0; // departure pointer

      while (i < n) {
        if (arr[i] <= dep[j]) {
          // Tàu mới đến trước/cùng lúc tàu cũ đi
          platforms++;
          i++;
          maxPlatforms = Math.max(maxPlatforms, platforms);
        } else {
          // Tàu cũ đi trước
          platforms--;
          j++;
        }
      }

      return maxPlatforms;
    }
  }

  /**
   * CÁCH 2: Dùng Map đếm sự kiện (phù hợp khi thời gian là string/complex)
   */
  static class MapSolution {
    public int findPlatform(int[] arr, int[] dep) {
      TreeMap<Integer, Integer> timeline = new TreeMap<>();

      for (int time : arr) {
        timeline.merge(time, 1, Integer::sum); // +1 tàu đến
      }
      for (int time : dep) {
        // +1 để departure xử lý sau arrival cùng thời điểm
        timeline.merge(time + 1, -1, Integer::sum); // -1 tàu đi
      }

      int current = 0;
      int max = 0;
      for (int change : timeline.values()) {
        current += change;
        max = Math.max(max, current);
      }

      return max;
    }
  }

  // ========== TESTS ==========
  private final Solution solution = new Solution();
  private final MapSolution mapSolution = new MapSolution();

  @Test
  @DisplayName("Minimum platforms for trains")
  void testPlatforms() {
    int[] arr = { 900, 940, 950, 1100, 1500, 1800 };
    int[] dep = { 910, 1200, 1120, 1130, 1900, 2000 };

    assertThat(solution.findPlatform(arr.clone(), dep.clone())).isEqualTo(3);
    assertThat(mapSolution.findPlatform(arr.clone(), dep.clone())).isEqualTo(3);
  }
}
