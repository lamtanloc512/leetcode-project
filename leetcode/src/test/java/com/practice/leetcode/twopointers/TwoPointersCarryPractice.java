package com.practice.leetcode.twopointers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ TWO POINTERS - REVERSE ITERATION & CARRY (Cộng ngược với số nhớ)          ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ Ý TƯỞNG CỐT LÕI (CORE CONCEPT)                                              │
 * │                                                                             │
 * │ Dạng bài này thường yêu cầu cộng 2 chuỗi số, nối danh sách, hoặc mảng số.   │
 * │ - Ta duyệt từ CUỐI mảng về ĐẦU mảng (Reverse Iteration).                    │
 * │ - Luôn duy trì một biến `carry` (số nhớ).                                   │
 * │ - Điều kiện lặp: Còn phần tử ở mảng 1 HOẶC mảng 2 HOẶC carry > 0.           │
 * │                                                                             │
 * │ TEMPLATE CHUẨN:                                                             │
 * │                                                                             │
 * │ int i = len1 - 1, j = len2 - 1, carry = 0;                                  │
 * │                                                                             │
 * │ while (i >= 0 || j >= 0 || carry > 0) {                                     │
 * │     int sum = carry;               // Bắt đầu với số nhớ                    │
 * │     if (i >= 0) sum += nums1[i--]; // Cộng và giảm pointer                  │
 * │     if (j >= 0) sum += nums2[j--]; // Cộng và giảm pointer                  │
 * │                                                                             │
 * │     result.append(sum % 10);       // Lấy phần đơn vị                       │
 * │     carry = sum / 10;              // Tính số nhớ cho vòng sau              │
 * │ }                                                                           │
 * │                                                                             │
 * │ return result.reverse().toString(); // Đảo ngược lại kết quả cuối cùng      │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class TwoPointersCarryPractice {

  // ═══════════════════════════════════════════════════════════════════════════
  // #1. LC 66 - PLUS ONE (Easy)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Đề bài: Cộng thêm 1 vào số nguyên biểu diễn bởi mảng digits. [1,2,3] -> [1,2,4].
   *         [9,9] -> [1,0,0].
   *
   * Giải thích:
   * - Duyệt từ cuối về đầu.
   * - Nếu < 9: cộng 1 và return luôn (xong việc).
   * - Nếu = 9: gán thành 0 và tiếp tục vòng lặp (carry tự động lan truyền).
   * - Nếu hết vòng lặp vẫn chưa return (trường hợp toàn 99...9) -> Thêm 1 ở đầu.
   */
  public int[] plusOne(int[] digits) {
    int n = digits.length;
    for (int i = n - 1; i >= 0; i--) {
      if (digits[i] < 9) {
        digits[i]++;
        return digits; // Cộng xong không nhớ, trả về luôn
      }
      digits[i] = 0; // Nhớ 1, gán vị trí này thành 0, tiếp tục lặp
    }

    // Nếu chạy hết vòng lặp mà chưa return -> mảng ban đầu toàn số 9 (999...9)
    // -> Kết quả là 1 theo sau là n số 0 (100...0)
    int[] result = new int[n + 1];
    result[0] = 1;
    return result;
  }

  @Test
  void testPlusOne() {
    assertArrayEquals(new int[]{1, 2, 4}, plusOne(new int[]{1, 2, 3}));
    assertArrayEquals(new int[]{4, 3, 2, 2}, plusOne(new int[]{4, 3, 2, 1}));
    assertArrayEquals(new int[]{1, 0, 0}, plusOne(new int[]{9, 9})); // Overflow sizing
  }


  // ═══════════════════════════════════════════════════════════════════════════
  // #2. LC 415 - ADD STRINGS (Easy)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Đề bài: Cộng 2 số nguyên dương dạng chuỗi. KHÔNG dùng BigInteger.
   *         "11" + "123" -> "134"
   *
   * Giải thích: Áp dụng đúng Template chuẩn ở trên.
   */
  public String addStrings(String num1, String num2) {
    StringBuilder sb = new StringBuilder();
    int i = num1.length() - 1;
    int j = num2.length() - 1;
    int carry = 0;

    // Lặp khi còn số để cộng hoặc còn số nhớ
    while (i >= 0 || j >= 0 || carry > 0) {
      int sum = carry;

      // Cộng dồn giá trị từ num1
      if (i >= 0) {
        sum += num1.charAt(i) - '0';
        i--;
      }

      // Cộng dồn giá trị từ num2
      if (j >= 0) {
        sum += num2.charAt(j) - '0';
        j--;
      }

      sb.append(sum % 10); // Lấy chữ số hàng đơn vị
      carry = sum / 10;    // Tính số nhớ
    }

    // Kết quả đang bị ngược, cần đảo lại
    return sb.reverse().toString();
  }

  @Test
  void testAddStrings() {
    assertEquals("134", addStrings("11", "123"));
    assertEquals("533", addStrings("456", "77"));
    assertEquals("0", addStrings("0", "0"));
    assertEquals("100", addStrings("99", "1")); // Test carry ở cuối
  }


  // ═══════════════════════════════════════════════════════════════════════════
  // #3. LC 989 - ADD TO ARRAY-FORM OF INTEGER (Easy)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Đề bài: Mảng [1,2,0,0] và số k = 34. Kết quả [1,2,3,4].
   *
   * Giải thích:
   * - Biến tấu một chút: COI K CHÍNH LÀ CARDY TỔNG QUÁT.
   * - Thay vì `carry`, ta dùng trực tiếp `k`.
   * - Tại mỗi bước i:
   *     k += num[i];
   *     num[i] = k % 10;
   *     k /= 10;
   * - Khi duyệt hết mảng mà k vẫn > 0 -> thêm các chữ số còn lại của k vào đầu.
   */
  public List<Integer> addToArrayForm(int[] num, int k) {
    List<Integer> result = new ArrayList<>();
    int i = num.length - 1;

    // Lặp khi còn phần tử mảng HOẶC k vẫn còn giá trị
    while (i >= 0 || k > 0) {
      if (i >= 0) {
        k += num[i]; // Cộng dồn con số hiện tại vào k
        i--;
      }
      
      // Lúc này k chứa: (số nhớ cũ + giá trị mảng tại i)
      result.add(k % 10); // Lấy hàng đơn vị
      k /= 10;            // k trở thành số nhớ (carry) cho vòng sau
    }

    // Kết quả bị ngược -> Đảo lại
    Collections.reverse(result);
    return result;
  }

  @Test
  void testAddToArrayForm() {
    assertEquals(List.of(1, 2, 3, 4), addToArrayForm(new int[]{1, 2, 0, 0}, 34));
    assertEquals(List.of(4, 5, 5), addToArrayForm(new int[]{2, 7, 4}, 181));
    assertEquals(List.of(1, 0, 2, 1), addToArrayForm(new int[]{2, 1, 5}, 806));
    assertEquals(List.of(1, 0, 0, 0, 0), addToArrayForm(new int[]{9, 9, 9, 9}, 1));
  }
}
