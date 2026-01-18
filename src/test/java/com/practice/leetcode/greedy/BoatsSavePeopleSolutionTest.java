package com.practice.leetcode.greedy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BOATS TO SAVE PEOPLE - Thuyền cứu người
 * 
 * Bài toán: Có mảng people[] là cân nặng của mỗi người.
 * Mỗi thuyền chở TỐI ĐA 2 người và tổng trọng lượng <= limit.
 * Tìm số thuyền TỐI THIỂU để chở hết mọi người.
 * 
 * Đây là bài kinh điển về ghép cặp tối ưu!
 */
class BoatsSavePeopleSolutionTest {

  /**
   * THUẬT TOÁN: Two Pointers - Ghép nặng nhất với nhẹ nhất
   * 
   * Ý tưởng:
   * 1. Sort theo cân nặng
   * 2. Dùng 2 con trỏ: left (nhẹ nhất), right (nặng nhất)
   * 3. Nếu người nhẹ + người nặng <= limit → Cùng 1 thuyền
   * 4. Nếu không → Người nặng đi 1 mình
   * 
   * Tại sao ghép nặng nhất với nhẹ nhất?
   * - Người nặng nhất chắc chắn cần 1 thuyền
   * - Nếu có thể ghép thêm ai đó, ghép người nhẹ nhất (để người nặng hơn có cơ
   * hội ghép sau)
   * 
   * Ví dụ: people = [1,2,2,3], limit = 3
   * 
   * Sort: [1, 2, 2, 3]
   * L R
   * 
   * Bước 1: 1 + 3 = 4 > 3 → Người 3 đi 1 mình, boats=1, R--
   * [1, 2, 2, 3]
   * L R
   * 
   * Bước 2: 1 + 2 = 3 <= 3 → Ghép cặp! boats=2, L++, R--
   * [1, 2, 2, 3]
   * L=R
   * 
   * Bước 3: Còn 1 người (2) → boats=3
   * 
   * Kết quả: 3 thuyền
   * 
   * Time: O(n log n)
   * Space: O(1)
   */
  static class Solution {
    public int numRescueBoats(int[] people, int limit) {
      Arrays.sort(people);

      int left = 0;
      int right = people.length - 1;
      int boats = 0;

      while (left <= right) {
        // Người nặng nhất luôn cần thuyền
        boats++;

        if (people[left] + people[right] <= limit) {
          // Người nhẹ nhất có thể đi cùng
          left++;
        }

        // Người nặng nhất đã lên thuyền
        right--;
      }

      return boats;
    }
  }

  // ========== TESTS ==========
  private final Solution solution = new Solution();

  @Test
  @DisplayName("Minimum boats to rescue all people")
  void testBoats() {
    assertThat(solution.numRescueBoats(new int[] { 1, 2 }, 3)).isEqualTo(1);
    assertThat(solution.numRescueBoats(new int[] { 3, 2, 2, 1 }, 3)).isEqualTo(3);
    assertThat(solution.numRescueBoats(new int[] { 3, 5, 3, 4 }, 5)).isEqualTo(4);
  }
}
