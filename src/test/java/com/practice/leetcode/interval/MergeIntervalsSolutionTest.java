package com.practice.leetcode.interval;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MERGE INTERVALS - Gộp các khoảng thời gian chồng lấn
 * 
 * Bài toán: Cho mảng intervals[i] = [start, end], gộp các khoảng chồng lấn.
 * 
 * Ví dụ thực tế:
 * - Gộp các cuộc họp có thời gian trùng nhau
 * - Gộp các khoảng IP address
 * - Xử lý booking lịch
 */
class MergeIntervalsSolutionTest {

  /**
   * THUẬT TOÁN: Sort + Linear Scan
   * 
   * Ý tưởng:
   * 1. SẮP XẾP theo start time
   * 2. Duyệt qua, nếu interval hiện tại CHỒNG với interval cuối → MỞ RỘNG
   * Ngược lại → THÊM MỚI
   * 
   * Hai interval [a,b] và [c,d] CHỒNG khi: c <= b (start của sau <= end của
   * trước)
   * 
   * Ví dụ: [[1,3], [2,6], [8,10], [15,18]]
   * 
   * Sau sort: [[1,3], [2,6], [8,10], [15,18]] (đã sort)
   * 
   * Bước 1: result = [[1,3]]
   * Bước 2: [2,6] chồng với [1,3] (2 <= 3) → Mở rộng: [[1,6]]
   * Bước 3: [8,10] không chồng (8 > 6) → Thêm mới: [[1,6], [8,10]]
   * Bước 4: [15,18] không chồng (15 > 10) → Thêm: [[1,6], [8,10], [15,18]]
   * 
   * Time: O(n log n) - do sort
   * Space: O(n) - lưu kết quả
   */
  static class Solution {
    public int[][] merge(int[][] intervals) {
      if (intervals.length <= 1)
        return intervals;

      // Sort theo start time
      Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

      List<int[]> result = new ArrayList<>();
      int[] current = intervals[0];
      result.add(current);

      for (int[] interval : intervals) {
        int currentEnd = current[1];
        int nextStart = interval[0];
        int nextEnd = interval[1];

        if (nextStart <= currentEnd) {
          // Chồng lấn → mở rộng end
          current[1] = Math.max(currentEnd, nextEnd);
        } else {
          // Không chồng → thêm mới
          current = interval;
          result.add(current);
        }
      }

      return result.toArray(new int[result.size()][]);
    }
  }

  // ========== TESTS ==========
  private final Solution solution = new Solution();

  @Test
  @DisplayName("Merge overlapping intervals")
  void testMerge() {
    assertThat(solution.merge(new int[][] { { 1, 3 }, { 2, 6 }, { 8, 10 }, { 15, 18 } }))
        .isDeepEqualTo(new int[][] { { 1, 6 }, { 8, 10 }, { 15, 18 } });
  }
}
