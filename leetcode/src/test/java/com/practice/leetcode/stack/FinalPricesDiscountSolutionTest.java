package com.practice.leetcode.stack;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║      FINAL PRICES WITH A SPECIAL DISCOUNT (LeetCode 1475)                  ║
 * ║              Pattern: MONOTONIC STACK - Next Smaller Element               ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │  BÀI TOÁN:                                                                  │
 * │  - Mua item i với giá prices[i]                                             │
 * │  - Được giảm giá = prices[j], với j là index NHỎ NHẤT thỏa mãn:             │
 * │      • j > i  (phải đứng sau)                                               │
 * │      • prices[j] <= prices[i]  (giá nhỏ hơn hoặc bằng)                       │
 * │  - Trả về mảng giá cuối cùng phải trả                                       │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │  NHẬN DIỆN: Đây là bài NEXT SMALLER OR EQUAL ELEMENT                        │
 * │                                                                             │
 * │  So sánh với template Next Greater:                                         │
 * │  ┌─────────────────────────┬─────────────────────────────────────────────┐  │
 * │  │ Next Greater            │ Next Smaller/Equal                          │  │
 * │  ├─────────────────────────┼─────────────────────────────────────────────┤  │
 * │  │ Pop khi peek() < curr   │ Pop khi peek() >= curr                      │  │
 * │  │ Monotonic DECREASING    │ Monotonic INCREASING                        │  │
 * │  └─────────────────────────┴─────────────────────────────────────────────┘  │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class FinalPricesDiscountSolutionTest {

  // ═══════════════════════════════════════════════════════════════════════════
  // SOLUTION 1: MONOTONIC STACK (Optimal)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │  THUẬT TOÁN:                                                            │
   * │                                                                         │
   * │  1. Duyệt từ trái sang phải                                             │
   * │  2. Với mỗi prices[i], pop tất cả phần tử >= prices[i]                  │
   * │     → prices[i] chính là "next smaller/equal" của các phần tử bị pop    │
   * │  3. Push index i vào stack                                              │
   * │                                                                         │
   * │  ĐẶC ĐIỂM: Stack luôn duy trì TĂNG DẦN (monotonic increasing)           │
   * └─────────────────────────────────────────────────────────────────────────┘
   */
  public int[] finalPrices(int[] prices) {
    int n = prices.length;
    int[] result = prices.clone(); // Mặc định: không có discount

    Deque<Integer> stack = new ArrayDeque<>(); // Stack lưu INDEX

    for (int i = 0; i < n; i++) {
      // Pop tất cả phần tử có giá >= prices[i]
      // → prices[i] là discount của chúng
      while (!stack.isEmpty() && prices[stack.peek()] >= prices[i]) {
        int idx = stack.pop();
        result[idx] = prices[idx] - prices[i]; // Áp dụng discount
      }
      stack.push(i);
    }

    // Các phần tử còn lại trong stack không có discount
    // → giữ nguyên giá gốc (đã clone từ đầu)

    return result;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // DRY RUN
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │  DRY RUN: prices = [8, 4, 6, 2, 3]                                      │
   * │  result = [8, 4, 6, 2, 3] (clone)                                       │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │                                                                         │
   * │  i=0, prices[0]=8                                                       │
   * │  ────────────────                                                       │
   * │  • stack = [] → không pop                                               │
   * │  • Push 0 → stack = [0]                                                 │
   * │                                                                         │
   * │  Stack: │ 0(8) │                                                        │
   * │         └──────┘                                                        │
   * │                                                                         │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │                                                                         │
   * │  i=1, prices[1]=4                                                       │
   * │  ────────────────                                                       │
   * │  • prices[0]=8 >= 4 → Pop 0, result[0] = 8 - 4 = 4                      │
   * │  • stack rỗng → dừng                                                    │
   * │  • Push 1 → stack = [1]                                                 │
   * │  • result = [4, 4, 6, 2, 3]                                             │
   * │                                                                         │
   * │  Stack: │ 1(4) │                                                        │
   * │         └──────┘                                                        │
   * │                                                                         │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │                                                                         │
   * │  i=2, prices[2]=6                                                       │
   * │  ────────────────                                                       │
   * │  • prices[1]=4 < 6 → không pop                                          │
   * │  • Push 2 → stack = [1, 2]                                              │
   * │                                                                         │
   * │  Stack: │ 2(6) │                                                        │
   * │         │ 1(4) │                                                        │
   * │         └──────┘                                                        │
   * │                                                                         │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │                                                                         │
   * │  i=3, prices[3]=2                                                       │
   * │  ────────────────                                                       │
   * │  • prices[2]=6 >= 2 → Pop 2, result[2] = 6 - 2 = 4                      │
   * │  • prices[1]=4 >= 2 → Pop 1, result[1] = 4 - 2 = 2                      │
   * │  • stack rỗng → dừng                                                    │
   * │  • Push 3 → stack = [3]                                                 │
   * │  • result = [4, 2, 4, 2, 3]                                             │
   * │                                                                         │
   * │  Stack: │ 3(2) │                                                        │
   * │         └──────┘                                                        │
   * │                                                                         │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │                                                                         │
   * │  i=4, prices[4]=3                                                       │
   * │  ────────────────                                                       │
   * │  • prices[3]=2 < 3 → không pop                                          │
   * │  • Push 4 → stack = [3, 4]                                              │
   * │                                                                         │
   * │  Stack: │ 4(3) │                                                        │
   * │         │ 3(2) │  ← còn lại trong stack = không có discount             │
   * │         └──────┘                                                        │
   * │                                                                         │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │                                                                         │
   * │  KẾT QUẢ: [4, 2, 4, 2, 3] ✓                                             │
   * │                                                                         │
   * └─────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SOLUTION 2: BRUTE FORCE (để so sánh)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Time: O(n²) - với mỗi i, tìm j từ i+1 đến n-1
   * Space: O(1) nếu modify in-place, O(n) nếu tạo result mới
   */
  public int[] finalPricesBruteForce(int[] prices) {
    int n = prices.length;
    int[] result = new int[n];

    for (int i = 0; i < n; i++) {
      result[i] = prices[i]; // Mặc định không có discount
      for (int j = i + 1; j < n; j++) {
        if (prices[j] <= prices[i]) {
          result[i] = prices[i] - prices[j];
          break; // Tìm thấy j nhỏ nhất → dừng
        }
      }
    }

    return result;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // COMPLEXITY ANALYSIS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │  MONOTONIC STACK SOLUTION:                                              │
   * │                                                                         │
   * │  TIME: O(n)                                                             │
   * │  - Mỗi phần tử được push vào stack đúng 1 lần                           │
   * │  - Mỗi phần tử được pop ra khỏi stack tối đa 1 lần                      │
   * │  - Tổng: 2n operations = O(n)                                           │
   * │                                                                         │
   * │  SPACE: O(n)                                                            │
   * │  - Stack tối đa chứa n phần tử (worst case: mảng tăng dần)              │
   * │  - Result array: O(n)                                                   │
   * └─────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // KEY INSIGHTS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │  MONOTONIC STACK CHEAT SHEET:                                           │
   * │                                                                         │
   * │  ┌─────────────────────┬─────────────────┬─────────────────────────┐    │
   * │  │ Tìm gì?             │ Pop condition   │ Stack order             │    │
   * │  ├─────────────────────┼─────────────────┼─────────────────────────┤    │
   * │  │ Next Greater        │ peek() < curr   │ Decreasing (giảm dần)   │    │
   * │  │ Next Smaller        │ peek() > curr   │ Increasing (tăng dần)   │    │
   * │  │ Next Greater/Equal  │ peek() <= curr  │ Strictly Decreasing     │    │
   * │  │ Next Smaller/Equal  │ peek() >= curr  │ Strictly Increasing     │    │
   * │  └─────────────────────┴─────────────────┴─────────────────────────┘    │
   * │                                                                         │
   * │  RULE OF THUMB:                                                         │
   * │  - Tìm GREATER → Pop những cái NHỎ HƠN → Stack GIẢM DẦN                 │
   * │  - Tìm SMALLER → Pop những cái LỚN HƠN → Stack TĂNG DẦN                 │
   * └─────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // TESTS
  // ═══════════════════════════════════════════════════════════════════════════
  @Test
  void testBasicExample() {
    int[] prices = {8, 4, 6, 2, 3};
    int[] expected = {4, 2, 4, 2, 3};
    assertArrayEquals(expected, finalPrices(prices));
  }

  @Test
  void testAnotherExample() {
    int[] prices = {1, 2, 3, 4, 5};
    int[] expected = {1, 2, 3, 4, 5}; // Tăng dần → không có discount
    assertArrayEquals(expected, finalPrices(prices));
  }

  @Test
  void testDecreasing() {
    int[] prices = {10, 1, 1, 6};
    int[] expected = {9, 0, 1, 6};
    // prices[0]=10: discount=prices[1]=1 → pay 9
    // prices[1]=1: discount=prices[2]=1 → pay 0
    // prices[2]=1: không có → pay 1
    // prices[3]=6: không có → pay 6
    assertArrayEquals(expected, finalPrices(prices));
  }

  @Test
  void testAllSame() {
    int[] prices = {5, 5, 5, 5};
    int[] expected = {0, 0, 0, 5};
    // Mỗi phần tử được discount bởi phần tử tiếp theo (bằng nhau)
    assertArrayEquals(expected, finalPrices(prices));
  }

  @Test
  void testSingleElement() {
    int[] prices = {10};
    int[] expected = {10}; // Không có discount
    assertArrayEquals(expected, finalPrices(prices));
  }

  @Test
  void testTwoElements() {
    int[] prices = {3, 1};
    int[] expected = {2, 1}; // 3 - 1 = 2
    assertArrayEquals(expected, finalPrices(prices));
  }
}
