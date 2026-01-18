package com.practice.leetcode.interval;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MEETING ROOMS II - Tìm số phòng họp tối thiểu cần thiết
 * 
 * Bài toán: Cho mảng các cuộc họp [start, end], tìm số phòng tối thiểu
 * để tất cả cuộc họp có thể diễn ra.
 * 
 * Đây là bài kinh điển về xử lý interval và được hỏi rất nhiều!
 */
class MeetingRoomsSolutionTest {

  /**
   * THUẬT TOÁN: Event Timeline (Dòng thời gian sự kiện)
   * 
   * Ý tưởng: Tách mỗi cuộc họp thành 2 sự kiện:
   * - START (+1): Cần thêm 1 phòng
   * - END (-1): Giải phóng 1 phòng
   * 
   * Sort theo thời gian, duyệt và đếm số phòng cần dùng đồng thời.
   * 
   * Ví dụ: meetings = [[0,30], [5,10], [15,20]]
   * 
   * Tách sự kiện:
   * - t=0: +1 (start meeting 1)
   * - t=5: +1 (start meeting 2)
   * - t=10: -1 (end meeting 2)
   * - t=15: +1 (start meeting 3)
   * - t=20: -1 (end meeting 3)
   * - t=30: -1 (end meeting 1)
   * 
   * Duyệt theo thời gian:
   * t=0: rooms = 0 + 1 = 1
   * t=5: rooms = 1 + 1 = 2 ← MAX
   * t=10: rooms = 2 - 1 = 1
   * t=15: rooms = 1 + 1 = 2
   * t=20: rooms = 2 - 1 = 1
   * t=30: rooms = 1 - 1 = 0
   * 
   * Kết quả: Cần tối thiểu 2 phòng
   * 
   * Lưu ý: Nếu start = end cùng thời điểm, xử lý END trước (phòng được giải phóng
   * trước khi cuộc họp mới bắt đầu).
   * 
   * Time: O(n log n)
   * Space: O(n)
   */
  static class Solution {
    public int minMeetingRooms(int[][] intervals) {
      int n = intervals.length;
      int[] starts = new int[n];
      int[] ends = new int[n];

      for (int i = 0; i < n; i++) {
        starts[i] = intervals[i][0];
        ends[i] = intervals[i][1];
      }

      Arrays.sort(starts);
      Arrays.sort(ends);

      int rooms = 0;
      int maxRooms = 0;
      int startPtr = 0;
      int endPtr = 0;

      while (startPtr < n) {
        if (starts[startPtr] < ends[endPtr]) {
          // Cuộc họp mới bắt đầu trước khi có cuộc nào kết thúc
          rooms++;
          startPtr++;
        } else {
          // Một cuộc họp kết thúc, giải phóng phòng
          rooms--;
          endPtr++;
        }
        maxRooms = Math.max(maxRooms, rooms);
      }

      return maxRooms;
    }
  }

  /**
   * CÁCH 2: Min Heap
   * 
   * Ý tưởng: Heap lưu end time của các cuộc họp đang diễn ra.
   * - Nếu cuộc họp mới start >= heap.top (end sớm nhất) → Dùng lại phòng
   * - Ngược lại → Cần phòng mới
   */
  static class HeapSolution {
    public int minMeetingRooms(int[][] intervals) {
      if (intervals.length == 0)
        return 0;

      // Sort theo start time
      Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

      // Min heap lưu end time của các cuộc họp đang chạy
      PriorityQueue<Integer> heap = new PriorityQueue<>();
      heap.offer(intervals[0][1]);

      for (int i = 1; i < intervals.length; i++) {
        // Nếu cuộc họp mới bắt đầu sau khi cuộc sớm nhất kết thúc
        if (intervals[i][0] >= heap.peek()) {
          heap.poll(); // Dùng lại phòng
        }
        heap.offer(intervals[i][1]); // Thêm end time mới
      }

      return heap.size(); // Số phần tử = số phòng cần
    }
  }

  // ========== TESTS ==========
  private final Solution solution = new Solution();
  private final HeapSolution heapSolution = new HeapSolution();

  @Test
  @DisplayName("Find minimum meeting rooms")
  void testMinRooms() {
    assertThat(solution.minMeetingRooms(new int[][] { { 0, 30 }, { 5, 10 }, { 15, 20 } })).isEqualTo(2);
    assertThat(heapSolution.minMeetingRooms(new int[][] { { 0, 30 }, { 5, 10 }, { 15, 20 } })).isEqualTo(2);
  }
}
