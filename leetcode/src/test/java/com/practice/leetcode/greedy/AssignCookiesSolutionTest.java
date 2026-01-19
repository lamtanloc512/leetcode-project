package com.practice.leetcode.greedy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ASSIGN COOKIES - Phân chia bánh cho trẻ em
 * 
 * Bài toán: Có mảng g[] là "độ tham" của mỗi đứa trẻ,
 * mảng s[] là size của mỗi cái bánh.
 * Đứa trẻ i chỉ hài lòng nếu được bánh size >= g[i].
 * Tìm số trẻ tối đa có thể hài lòng.
 * 
 * Đây là bài điển hình của GREEDY (Tham lam)!
 */
class AssignCookiesSolutionTest {

  /**
   * THUẬT TOÁN: Greedy - Đứa ít tham nhất + Bánh nhỏ nhất
   * 
   * Ý tưởng:
   * 1. Sort cả 2 mảng tăng dần
   * 2. Với đứa trẻ ít tham nhất, tìm bánh nhỏ nhất đủ size
   * 3. Nếu tìm được → Match, xét đứa tiếp theo
   * 4. Nếu không → Bánh này không đủ cho ai, bỏ qua
   * 
   * Tại sao greedy đúng?
   * - Đứa ít tham dễ thỏa mãn nhất
   * - Nếu bánh nhỏ không đủ cho đứa ít tham nhất, thì cũng không đủ cho ai khác
   * - Cho đứa ít tham bánh vừa đủ, để dành bánh lớn cho đứa tham hơn
   * 
   * Ví dụ: g = [1,2,3], s = [1,1]
   * 
   * Sort: g = [1,2,3], s = [1,1]
   * 
   * child=0 (g=1), cookie=0 (s=1): 1 >= 1 → Match! count=1
   * child=1 (g=2), cookie=1 (s=1): 1 < 2 → Không đủ, thử cookie tiếp
   * Hết cookie → Dừng
   * 
   * Kết quả: 1 đứa trẻ hài lòng
   * 
   * Time: O(n log n + m log m) - do sort
   * Space: O(1)
   */
  static class Solution {
    public int findContentChildren(int[] g, int[] s) {
      Arrays.sort(g); // Sort độ tham
      Arrays.sort(s); // Sort size bánh

      int child = 0;
      int cookie = 0;

      while (child < g.length && cookie < s.length) {
        if (s[cookie] >= g[child]) {
          // Bánh đủ lớn cho đứa trẻ này
          child++;
        }
        // Bánh đã dùng (hoặc quá nhỏ), chuyển bánh tiếp theo
        cookie++;
      }

      return child; // Số đứa trẻ đã được thỏa mãn
    }
  }

  // ========== TESTS ==========
  private final Solution solution = new Solution();

  @Test
  @DisplayName("Assign cookies to maximize happy children")
  void testAssignCookies() {
    assertThat(solution.findContentChildren(new int[] { 1, 2, 3 }, new int[] { 1, 1 })).isEqualTo(1);
    assertThat(solution.findContentChildren(new int[] { 1, 2 }, new int[] { 1, 2, 3 })).isEqualTo(2);
  }
}
