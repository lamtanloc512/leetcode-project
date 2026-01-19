package com.practice.leetcode.design;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MY CALENDAR - Lịch đặt phòng
 * 
 * Bài toán: Thiết kế lịch booking.
 * Mỗi booking là [start, end) - bao gồm start, không bao gồm end.
 * Return true nếu có thể book, false nếu conflict.
 * 
 * Đây là bài kinh điển về interval + data structure!
 */
class MyCalendarSolutionTest {

  /**
   * THUẬT TOÁN: TreeMap - Lưu các booking đã có
   * 
   * Ý tưởng:
   * - TreeMap<start, end> lưu các booking theo thứ tự start time
   * - Với booking mới [start, end), kiểm tra:
   * 1. Booking TRƯỚC đó (floorEntry): end của nó phải <= start của mới
   * 2. Booking SAU đó (ceilingEntry): start của nó phải >= end của mới
   * 
   * Ví dụ: Đã book [10, 20), [25, 35)
   * 
   * Book [15, 25)?
   * - floorEntry(15) = [10, 20): 20 > 15 → CONFLICT!
   * 
   * Book [20, 25)?
   * - floorEntry(20) = [10, 20): 20 <= 20 ✓
   * - ceilingEntry(20) = [25, 35): 25 >= 25 ✓
   * - OK to book!
   * 
   * Time: O(log n) cho mỗi book
   * Space: O(n)
   */
  static class MyCalendar {
    private TreeMap<Integer, Integer> bookings;

    public MyCalendar() {
      bookings = new TreeMap<>();
    }

    public boolean book(int start, int end) {
      // Tìm booking có start lớn nhất <= start mới
      Map.Entry<Integer, Integer> prev = bookings.floorEntry(start);
      // Tìm booking có start nhỏ nhất >= start mới
      Map.Entry<Integer, Integer> next = bookings.ceilingEntry(start);

      // Kiểm tra conflict với booking trước
      if (prev != null && prev.getValue() > start) {
        return false; // end của prev > start mới → conflict
      }

      // Kiểm tra conflict với booking sau
      if (next != null && next.getKey() < end) {
        return false; // start của next < end mới → conflict
      }

      // Không conflict → book thành công
      bookings.put(start, end);
      return true;
    }
  }

  /**
   * PHIÊN BẢN II: Cho phép DOUBLE BOOKING (2 người cùng lúc)
   * Nhưng không cho TRIPLE BOOKING!
   * 
   * Ý tưởng: Duy trì 2 list:
   * - calendar: tất cả bookings
   * - overlaps: các khoảng đã bị double book
   * 
   * Khi book mới:
   * 1. Kiểm tra có conflict với overlaps không → Nếu có = triple → return false
   * 2. Thêm các overlap mới vào overlaps
   * 3. Thêm booking vào calendar
   */
  static class MyCalendarII {
    private List<int[]> calendar;
    private List<int[]> overlaps;

    public MyCalendarII() {
      calendar = new ArrayList<>();
      overlaps = new ArrayList<>();
    }

    public boolean book(int start, int end) {
      // Kiểm tra triple booking
      for (int[] overlap : overlaps) {
        if (start < overlap[1] && end > overlap[0]) {
          return false; // Conflict với double book → triple!
        }
      }

      // Thêm overlaps mới
      for (int[] booking : calendar) {
        int overlapStart = Math.max(start, booking[0]);
        int overlapEnd = Math.min(end, booking[1]);
        if (overlapStart < overlapEnd) {
          overlaps.add(new int[] { overlapStart, overlapEnd });
        }
      }

      calendar.add(new int[] { start, end });
      return true;
    }
  }

  // ========== TESTS ==========

  @Test
  @DisplayName("Single booking calendar")
  void testCalendar() {
    MyCalendar cal = new MyCalendar();
    assertThat(cal.book(10, 20)).isTrue();
    assertThat(cal.book(15, 25)).isFalse(); // Conflict
    assertThat(cal.book(20, 30)).isTrue(); // OK, [10,20) và [20,30) không chồng
  }

  @Test
  @DisplayName("Double booking calendar")
  void testCalendarII() {
    MyCalendarII cal = new MyCalendarII();
    assertThat(cal.book(10, 20)).isTrue();
    assertThat(cal.book(50, 60)).isTrue();
    assertThat(cal.book(10, 40)).isTrue(); // OK, double book với [10,20)
    assertThat(cal.book(5, 15)).isFalse(); // Triple book!
    assertThat(cal.book(5, 10)).isTrue(); // OK
    assertThat(cal.book(25, 55)).isTrue(); // OK
  }
}
