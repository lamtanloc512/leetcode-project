package com.practice.leetcode.interval;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * INSERT INTERVAL - Chèn khoảng mới vào danh sách intervals đã sort
 * 
 * Bài toán: Cho danh sách intervals KHÔNG CHỒNG và ĐÃ SORT,
 * chèn newInterval vào và gộp nếu cần.
 * 
 * Ví dụ thực tế:
 * - Thêm cuộc họp mới vào lịch
 * - Thêm booking vào hệ thống đặt phòng
 */
class InsertIntervalSolutionTest {

  /**
   * THUẬT TOÁN: 3 Giai Đoạn
   * 
   * Ý tưởng: Chia thành 3 phần:
   * 1. Các interval KẾT THÚC TRƯỚC newInterval bắt đầu → Giữ nguyên
   * 2. Các interval CHỒNG với newInterval → Merge
   * 3. Các interval BẮT ĐẦU SAU newInterval kết thúc → Giữ nguyên
   * 
   * Ví dụ: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
   * 
   * Phần 1: [1,2] kết thúc tại 2 < 4 (start của new) → Giữ nguyên
   * 
   * Phần 2 (merge):
   * [3,5]: 5 >= 4 và 3 <= 8 → Chồng! Merge: [min(3,4), max(5,8)] = [3,8]
   * [6,7]: 7 >= 3 và 6 <= 8 → Chồng! Merge: [3, 8]
   * [8,10]: 10 >= 3 và 8 <= 8 → Chồng! Merge: [3, 10]
   * 
   * Phần 3: [12,16] bắt đầu tại 12 > 10 (end của merged) → Giữ nguyên
   * 
   * Kết quả: [[1,2], [3,10], [12,16]]
   * 
   * Time: O(n)
   * Space: O(n)
   */
  static class Solution {
    public int[][] insert(int[][] intervals, int[] newInterval) {
      List<int[]> result = new ArrayList<>();
      int i = 0;
      int n = intervals.length;

      // Phần 1: Thêm tất cả intervals kết thúc TRƯỚC newInterval
      while (i < n && intervals[i][1] < newInterval[0]) {
        result.add(intervals[i]);
        i++;
      }

      // Phần 2: Merge tất cả intervals chồng với newInterval
      while (i < n && intervals[i][0] <= newInterval[1]) {
        newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
        newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
        i++;
      }
      result.add(newInterval);

      // Phần 3: Thêm tất cả intervals còn lại
      while (i < n) {
        result.add(intervals[i]);
        i++;
      }

      return result.toArray(new int[result.size()][]);
    }
  }

  // ========== TESTS ==========
  private final Solution solution = new Solution();

  @Test
  @DisplayName("Insert and merge interval")
  void testInsert() {
    assertThat(solution.insert(
        new int[][] { { 1, 2 }, { 3, 5 }, { 6, 7 }, { 8, 10 }, { 12, 16 } },
        new int[] { 4, 8 }))
        .isDeepEqualTo(new int[][] { { 1, 2 }, { 3, 10 }, { 12, 16 } });
  }
}
