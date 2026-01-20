package com.practice.leetcode.bitwise;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║              BIT MANIPULATION GUIDE - LEETCODE                            ║
 * ╠═══════════════════════════════════════════════════════════════════════════╣
 * ║ Hiểu tường tận bitwise operations và các template giải bài                ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class BitManipulationGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // BINARY FUNDAMENTALS (Nền tảng nhị phân)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SỐ NHỊ PHÂN LÀ GÌ?                                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ Hệ thập phân (decimal): 10 chữ số (0-9)                                     │
   * │ Hệ nhị phân (binary):   2 chữ số (0 và 1)                                   │
   * │                                                                             │
   * │ Máy tính dùng binary vì transistor chỉ có 2 trạng thái: tắt(0) / bật(1)     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ CÁCH CHUYỂN DECIMAL → BINARY                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ CÁCH 1: CHIA 2 LẤY DƯ (Division Method)                                     │
   * │ ────────────────────────────────────────                                    │
   * │ Chia liên tục cho 2, lấy phần dư, đọc ngược từ dưới lên                     │
   * │                                                                             │
   * │ VD: Chuyển 13 sang binary:                                                  │
   * │                                                                             │
   * │   13 ÷ 2 = 6 dư 1  ↑                                                        │
   * │    6 ÷ 2 = 3 dư 0  │                                                        │
   * │    3 ÷ 2 = 1 dư 1  │  Đọc ngược: 1101                                       │
   * │    1 ÷ 2 = 0 dư 1  │                                                        │
   * │                                                                             │
   * │ → 13 = 1101 (binary)                                                        │
   * │                                                                             │
   * │ VD: Chuyển 25 sang binary:                                                  │
   * │                                                                             │
   * │   25 ÷ 2 = 12 dư 1  ↑                                                       │
   * │   12 ÷ 2 = 6  dư 0  │                                                       │
   * │    6 ÷ 2 = 3  dư 0  │  Đọc ngược: 11001                                     │
   * │    3 ÷ 2 = 1  dư 1  │                                                       │
   * │    1 ÷ 2 = 0  dư 1  │                                                       │
   * │                                                                             │
   * │ → 25 = 11001 (binary)                                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ CÁCH 2: BẢNG LŨY THỪA 2 (Position Method) - NHANH HƠN!                      │
   * │ ────────────────────────────────────────────────────────                    │
   * │ Mỗi vị trí bit đại diện cho 1 lũy thừa của 2:                               │
   * │                                                                             │
   * │   Bit vị trí:   7     6     5     4     3    2    1    0                    │
   * │   Giá trị:    128    64    32    16     8    4    2    1                    │
   * │               2^7   2^6   2^5   2^4   2^3  2^2  2^1  2^0                    │
   * │                                                                             │
   * │ VD: Chuyển 13:                                                              │
   * │   13 = 8 + 4 + 1 = 2³ + 2² + 2⁰                                             │
   * │                                                                             │
   * │   Bit:   8   4   2   1                                                      │
   * │          ↓   ↓   ↓   ↓                                                      │
   * │   13  =  1   1   0   1  = 1101                                              │
   * │          ✓   ✓   ✗   ✓                                                      │
   * │                                                                             │
   * │ VD: Chuyển 25:                                                              │
   * │   25 = 16 + 8 + 1 = 2⁴ + 2³ + 2⁰                                            │
   * │                                                                             │
   * │   Bit:  16   8   4   2   1                                                  │
   * │          ↓   ↓   ↓   ↓   ↓                                                  │
   * │   25  =  1   1   0   0   1  = 11001                                         │
   * │          ✓   ✓   ✗   ✗   ✓                                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ TẠI SAO CÓ LÚC 0101, LÚC 00000101, LÚC 00000000000000000000000000000101?    │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ⭐ BIT WIDTH (Độ rộng bit) - SỐ Ô NHỚ ĐƯỢC CẤP                              │
   * │                                                                             │
   * │ Cùng số 5, nhưng biểu diễn khác nhau tùy theo "độ rộng":                    │
   * │                                                                             │
   * │   4-bit:                        0101  (thường dùng để giải thích)           │
   * │   8-bit (byte):             00000101  (1 byte = 8 bits)                     │
   * │  16-bit (short):       0000000000000101                                     │
   * │  32-bit (int):  00000000000000000000000000000101  (Java int)                │
   * │  64-bit (long): ... 64 bits ...                   (Java long)               │
   * │                                                                             │
   * │ TẤT CẢ ĐỀU LÀ SỐ 5! Chỉ khác số bit 0 đệm ở đầu.                            │
   * │                                                                             │
   * │ TRONG THỰC TẾ:                                                              │
   * │ • Java int luôn là 32 bits (4 bytes)                                        │
   * │ • Java long luôn là 64 bits (8 bytes)                                       │
   * │ • Khi giải thích, ta thường bỏ bớt 0 ở đầu cho dễ nhìn                      │
   * │                                                                             │
   * │ VÍ DỤ THỰC TẾ:                                                              │
   * │                                                                             │
   * │   int a = 5;                                                                │
   * │   // Thực tế trong bộ nhớ: 00000000 00000000 00000000 00000101              │
   * │   // Viết tắt khi giải thích: 0101 hoặc 101                                 │
   * │                                                                             │
   * │   int b = 255;                                                              │
   * │   // Thực tế: 00000000 00000000 00000000 11111111                           │
   * │   // Viết tắt: 11111111 (8 bits vì cần 8 bit để biểu diễn 255)              │
   * │                                                                             │
   * │ QUY TẮC VIẾT TẮT:                                                           │
   * │ • Khi giải thích: bỏ 0 đầu, giữ đủ bit để hiểu                              │
   * │ • Khi so sánh 2 số: padding 0 để bằng số bit                                │
   * │     5 & 3:  0101 & 0011 (cùng 4 bits để dễ căn hàng)                        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ BINARY → DECIMAL (Ngược lại)                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ Cộng giá trị các vị trí có bit = 1:                                         │
   * │                                                                             │
   * │   1101 = ?                                                                  │
   * │                                                                             │
   * │   Vị trí:  3   2   1   0                                                    │
   * │   Bit:     1   1   0   1                                                    │
   * │   Giá trị: 8   4   2   1                                                    │
   * │            ↓   ↓       ↓                                                    │
   * │            8 + 4  +    1 = 13                                               │
   * │                                                                             │
   * │   11001 = 16 + 8 + 0 + 0 + 1 = 25                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ THUẬT NGỮ QUAN TRỌNG                                                        │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ MSB (Most Significant Bit):  Bit TRái nhất (có giá trị lớn nhất)            │
   * │ LSB (Least Significant Bit): Bit PHải nhất (có giá trị nhỏ nhất)            │
   * │                                                                             │
   * │ VD với số 13 = 1101:                                                        │
   * │                                                                             │
   * │            MSB           LSB                                                │
   * │             ↓             ↓                                                 │
   * │             1   1   0   1                                                   │
   * │            bit3 bit2 bit1 bit0                                              │
   * │                                                                             │
   * │ SET bit:   bit đó = 1 (bật)                                                 │
   * │ CLEAR bit: bit đó = 0 (tắt)                                                 │
   * │ TOGGLE:    đảo bit (0→1, 1→0)                                               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ JAVA: LÀM VIỆC VỚI BINARY                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // Viết số binary trực tiếp (prefix 0b):                                    │
   * │ int a = 0b1101;      // = 13                                                │
   * │ int b = 0b11001;     // = 25                                                │
   * │                                                                             │
   * │ // Chuyển int → binary string:                                              │
   * │ Integer.toBinaryString(13);   // "1101"                                     │
   * │ Integer.toBinaryString(255);  // "11111111"                                 │
   * │                                                                             │
   * │ // Chuyển binary string → int:                                              │
   * │ Integer.parseInt("1101", 2);  // 13                                         │
   * │                                                                             │
   * │ // In ra dạng binary có padding 0:                                          │
   * │ String.format("%8s", Integer.toBinaryString(5)).replace(' ', '0');          │
   * │ // Output: "00000101" (8 bits)                                              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // 6 BITWISE OPERATORS (6 phép toán bit)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 1. AND (&) - "Cả 2 đều 1 mới ra 1"                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │   5 & 3 = ?                                                                 │
   * │   5 = 0101                                                                  │
   * │   3 = 0011                                                                  │
   * │   ────────                                                                  │
   * │   & = 0001 = 1                                                              │
   * │                                                                             │
   * │ USE CASES:                                                                  │
   * │ • Check bit: (n & (1 << i)) != 0  → Bit i có bật không?                     │
   * │ • Clear bit: n & ~(1 << i)        → Tắt bit i                               │
   * │ • Check even/odd: (n & 1) == 0    → Số chẵn?                                │
   * │ • Get last k bits: n & ((1 << k) - 1)                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 2. OR (|) - "1 trong 2 là 1 thì ra 1"                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │   5 | 3 = ?                                                                 │
   * │   5 = 0101                                                                  │
   * │   3 = 0011                                                                  │
   * │   ────────                                                                  │
   * │   | = 0111 = 7                                                              │
   * │                                                                             │
   * │ USE CASES:                                                                  │
   * │ • Set bit: n | (1 << i)           → Bật bit i                               │
   * │ • Combine flags: READ | WRITE | EXECUTE                                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 3. XOR (^) - "Khác nhau thì ra 1" (QUAN TRỌNG NHẤT!)                        │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │   5 ^ 3 = ?                                                                 │
   * │   5 = 0101                                                                  │
   * │   3 = 0011                                                                  │
   * │   ────────                                                                  │
   * │   ^ = 0110 = 6                                                              │
   * │                                                                             │
   * │ ⭐ XOR PROPERTIES (Tính chất quan trọng):                                   │
   * │   a ^ 0 = a         (XOR với 0 giữ nguyên)                                  │
   * │   a ^ a = 0         (XOR với chính nó = 0)                                  │
   * │   a ^ b ^ a = b     (Self-canceling)                                        │
   * │   a ^ b = b ^ a     (Commutative)                                           │
   * │   (a ^ b) ^ c = a ^ (b ^ c)  (Associative)                                  │
   * │                                                                             │
   * │ USE CASES:                                                                  │
   * │ • Find single number: XOR all → pairs cancel, single remains                │
   * │ • Toggle bit: n ^ (1 << i)    → Đảo bit i                                   │
   * │ • Swap: a ^= b; b ^= a; a ^= b;  (without temp)                             │
   * │ • Check if different: (a ^ b) != 0                                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 4. NOT (~) - "Đảo tất cả bit"                                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │   ~5 = ?                                                                    │
   * │   5 = 00000000 00000000 00000000 00000101                                   │
   * │  ~5 = 11111111 11111111 11111111 11111010 = -6                              │
   * │                                                                             │
   * │ ⚠️ CHÚ Ý: ~n = -(n + 1)                                                     │
   * │   ~0 = -1, ~1 = -2, ~5 = -6                                                 │
   * │                                                                             │
   * │ USE CASES:                                                                  │
   * │ • Create mask: ~(1 << i)      → Tất cả 1, trừ bit i                         │
   * │ • Clear bits: n & ~mask                                                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 5. LEFT SHIFT (<<) - "Nhân 2"                                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │   5 << 1 = ?                                                                │
   * │   5 = 0101                                                                  │
   * │   << 1 = 1010 = 10                                                          │
   * │                                                                             │
   * │ FORMULA: n << k = n × 2^k                                                   │
   * │   5 << 1 = 5 × 2 = 10                                                       │
   * │   5 << 2 = 5 × 4 = 20                                                       │
   * │   1 << 3 = 8 (= 2^3)                                                        │
   * │                                                                             │
   * │ USE CASES:                                                                  │
   * │ • Create mask for bit i: 1 << i                                             │
   * │ • Fast multiply by power of 2                                               │
   * │ • 1 << n = 2^n                                                              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 6. RIGHT SHIFT (>>) - "Chia 2"                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │   10 >> 1 = ?                                                               │
   * │   10 = 1010                                                                 │
   * │   >> 1 = 0101 = 5                                                           │
   * │                                                                             │
   * │ FORMULA: n >> k = n ÷ 2^k (làm tròn xuống)                                  │
   * │   10 >> 1 = 10 ÷ 2 = 5                                                      │
   * │   10 >> 2 = 10 ÷ 4 = 2                                                      │
   * │                                                                             │
   * │ 2 LOẠI:                                                                     │
   * │ • >> : Arithmetic shift (giữ sign bit) - dùng cho signed int                │
   * │ • >>>: Logical shift (fill với 0) - dùng cho unsigned                       │
   * │                                                                             │
   * │ USE CASES:                                                                  │
   * │ • Fast divide by power of 2                                                 │
   * │ • Get bit i: (n >> i) & 1                                                   │
   * │ • Count bits / iterate bits                                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // COMMON BIT TRICKS (Các kỹ thuật thường dùng)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ BIT MANIPULATION CHEAT SHEET                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. CHECK bit i:     (n >> i) & 1   hoặc  (n & (1 << i)) != 0                │
   * │ 2. SET bit i:       n | (1 << i)                                            │
   * │ 3. CLEAR bit i:     n & ~(1 << i)                                           │
   * │ 4. TOGGLE bit i:    n ^ (1 << i)                                            │
   * │ 5. Check EVEN:      (n & 1) == 0                                            │
   * │ 6. Check ODD:       (n & 1) == 1                                            │
   * │ 7. Check POWER OF 2: n > 0 && (n & (n-1)) == 0                              │
   * │ 8. Get LOWEST set bit:  n & (-n)     hoặc  n & ~(n-1)                       │
   * │ 9. Clear LOWEST set bit: n & (n-1)                                          │
   * │ 10. Count set bits:  Integer.bitCount(n)                                    │
   * │ 11. Get last k bits: n & ((1 << k) - 1)                                     │
   * │ 12. Clear all bits from i to 0: n & ~((1 << (i+1)) - 1)                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 1: XOR - Find Single/Missing Number
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Pattern: Tìm số xuất hiện 1 lần khi các số khác xuất hiện 2 lần
   * LeetCode: 136, 268, 287, 389
   *
   * Nguyên lý:
   * - a ^ a = 0 (số giống nhau XOR = 0)
   * - a ^ 0 = a (XOR với 0 giữ nguyên)
   * - XOR tất cả → các cặp triệt tiêu, còn lại số lẻ
   */
  public int findSingleNumber(int[] nums) {
    int result = 0;
    for (int num : nums) {
      result ^= num;  // XOR tất cả
    }
    return result;  // Các cặp triệt tiêu, còn lại số duy nhất
  }

  /**
   * Pattern: Tìm số missing từ 0 đến n
   * LeetCode: 268
   *
   * Nguyên lý: XOR index với value, các cặp triệt tiêu
   * [0,1,3] length=3 → missing = 2
   * XOR indices: 0^1^2^3 = ...
   * XOR values:  0^1^3
   * Result = index ^ values = 2 (số missing)
   */
  public int findMissingNumber(int[] nums) {
    int n = nums.length;
    int result = n;  // Bắt đầu với n (index cuối không có trong array)
    for (int i = 0; i < n; i++) {
      result ^= i ^ nums[i];  // XOR cả index và value
    }
    return result;
  }

  @Test
  void testSingleNumber() {
    assertEquals(1, findSingleNumber(new int[]{2, 2, 1}));
    assertEquals(4, findSingleNumber(new int[]{4, 1, 2, 1, 2}));
  }

  @Test
  void testMissingNumber() {
    assertEquals(2, findMissingNumber(new int[]{3, 0, 1}));
    assertEquals(8, findMissingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1}));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 2: n & (n-1) - Power of 2 / Count Bits
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Pattern: Check if power of 2
   * LeetCode: 231
   *
   * Nguyên lý: Power of 2 chỉ có 1 bit = 1
   *   8 = 1000
   *   7 = 0111
   *   8 & 7 = 0  → Power of 2!
   *
   *   6 = 0110
   *   5 = 0101
   *   6 & 5 = 0100 ≠ 0  → Không phải power of 2
   */
  public boolean isPowerOfTwo(int n) {
    return n > 0 && (n & (n - 1)) == 0;
  }

  /**
   * Pattern: Count number of 1 bits (Hamming Weight)
   * LeetCode: 191
   *
   * Nguyên lý: n & (n-1) xóa bit 1 thấp nhất
   * Đếm số lần xóa được = số bit 1
   */
  public int countOneBits(int n) {
    int count = 0;
    while (n != 0) {
      n &= (n - 1);  // Xóa bit 1 thấp nhất
      count++;
    }
    return count;
  }

  @Test
  void testPowerOfTwo() {
    assertTrue(isPowerOfTwo(1));   // 2^0
    assertTrue(isPowerOfTwo(16));  // 2^4
    assertFalse(isPowerOfTwo(3));
    assertFalse(isPowerOfTwo(0));
  }

  @Test
  void testCountBits() {
    assertEquals(3, countOneBits(0b1011));  // 11 = 1011
    assertEquals(1, countOneBits(0b1000));  // 8 = 1000
    assertEquals(31, countOneBits(Integer.MAX_VALUE));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 3: Bit Masking for Subsets
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Pattern: Generate all subsets using bitmask
   * LeetCode: 78, 90
   *
   * Nguyên lý: n phần tử có 2^n subsets
   * Mỗi số từ 0 đến 2^n-1 đại diện cho 1 subset
   * Bit i = 1 → bao gồm phần tử thứ i
   *
   * [1,2,3]:
   * 000 = []
   * 001 = [1]
   * 010 = [2]
   * 011 = [1,2]
   * 100 = [3]
   * 101 = [1,3]
   * 110 = [2,3]
   * 111 = [1,2,3]
   */
  public List<List<Integer>> generateSubsets(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    int n = nums.length;
    int totalSubsets = 1 << n;  // 2^n

    for (int mask = 0; mask < totalSubsets; mask++) {
      List<Integer> subset = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        // Check if bit i is set
        if ((mask & (1 << i)) != 0) {
          subset.add(nums[i]);
        }
      }
      result.add(subset);
    }
    return result;
  }

  @Test
  void testSubsets() {
    List<List<Integer>> subsets = generateSubsets(new int[]{1, 2, 3});
    assertEquals(8, subsets.size());  // 2^3 = 8 subsets
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 4: Get/Set specific bits
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Pattern: Reverse bits
   * LeetCode: 190
   */
  public int reverseBits(int n) {
    int result = 0;
    for (int i = 0; i < 32; i++) {
      // Lấy bit cuối của n
      int bit = (n >> i) & 1;
      // Đặt vào vị trí đối xứng (31-i) trong result
      result |= (bit << (31 - i));
    }
    return result;
  }

  /**
   * Pattern: Hamming Distance (số bit khác nhau)
   * LeetCode: 461
   */
  public int hammingDistance(int x, int y) {
    // XOR tìm bits khác nhau, rồi đếm
    return Integer.bitCount(x ^ y);
  }

  @Test
  void testReverseBits() {
    // 00000010100101000001111010011100
    // →
    // 00111001011110000010100101000000
    assertEquals(964176192, reverseBits(43261596));
  }

  @Test
  void testHammingDistance() {
    // 1 = 0001, 4 = 0100 → 2 bits khác
    assertEquals(2, hammingDistance(1, 4));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 5: Two Single Numbers (Advanced XOR)
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Pattern: Tìm 2 số xuất hiện 1 lần
   * LeetCode: 260
   *
   * Nguyên lý:
   * 1. XOR all → x ^ y (vì các cặp triệt tiêu)
   * 2. Tìm bit khác nhau giữa x và y (ít nhất 1 bit)
   * 3. Dùng bit đó để chia thành 2 nhóm
   * 4. XOR mỗi nhóm → ra x và y
   */
  public int[] findTwoSingleNumbers(int[] nums) {
    // Step 1: XOR all → x ^ y
    int xorResult = 0;
    for (int num : nums) {
      xorResult ^= num;
    }

    // Step 2: Tìm bit set bất kỳ (dùng lowest set bit)
    int diffBit = xorResult & (-xorResult);

    // Step 3: Chia thành 2 nhóm và XOR
    int x = 0, y = 0;
    for (int num : nums) {
      if ((num & diffBit) == 0) {
        x ^= num;  // Nhóm bit đó = 0
      } else {
        y ^= num;  // Nhóm bit đó = 1
      }
    }

    return new int[]{x, y};
  }

  @Test
  void testTwoSingleNumbers() {
    int[] result = findTwoSingleNumbers(new int[]{1, 2, 1, 3, 2, 5});
    Arrays.sort(result);
    assertArrayEquals(new int[]{3, 5}, result);
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 6: Bit DP - Bitmask Dynamic Programming
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Pattern: DP với bitmask để track visited states
   * LeetCode: 1863, 2172, 698
   *
   * Dùng khi:
   * - Cần track tập hợp các phần tử đã chọn
   * - n nhỏ (n ≤ 20, vì 2^20 ≈ 1 triệu states)
   */
  public int subsetXORSum(int[] nums) {
    int n = nums.length;
    int totalSum = 0;

    // Duyệt tất cả 2^n subsets
    for (int mask = 0; mask < (1 << n); mask++) {
      int xorSum = 0;
      for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) != 0) {
          xorSum ^= nums[i];
        }
      }
      totalSum += xorSum;
    }
    return totalSum;
  }

  @Test
  void testSubsetXORSum() {
    // [1,3]: [] + [1] + [3] + [1,3] = 0 + 1 + 3 + 2 = 6
    assertEquals(6, subsetXORSum(new int[]{1, 3}));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // LEETCODE PROBLEMS BY PATTERN
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ XOR PATTERN:                                                                │
   * │ • 136. Single Number (Easy)                                                 │
   * │ • 268. Missing Number (Easy)                                                │
   * │ • 389. Find the Difference (Easy)                                           │
   * │ • 260. Single Number III (Medium) - 2 single numbers                        │
   * │ • 137. Single Number II (Medium) - others appear 3 times                    │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ n & (n-1) PATTERN:                                                          │
   * │ • 191. Number of 1 Bits (Easy)                                              │
   * │ • 231. Power of Two (Easy)                                                  │
   * │ • 338. Counting Bits (Easy) - DP variant                                    │
   * │ • 342. Power of Four (Easy)                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ BIT MANIPULATION:                                                           │
   * │ • 190. Reverse Bits (Easy)                                                  │
   * │ • 461. Hamming Distance (Easy)                                              │
   * │ • 371. Sum of Two Integers (Medium) - without + operator                    │
   * │ • 201. Bitwise AND of Numbers Range (Medium)                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ BITMASK DP:                                                                 │
   * │ • 78. Subsets (Medium)                                                      │
   * │ • 1863. Sum of All Subset XOR Totals (Easy)                                 │
   * │ • 2044. Count Number of Maximum Bitwise-OR Subsets (Medium)                 │
   * │ • 698. Partition to K Equal Sum Subsets (Medium)                            │
   * │ • 847. Shortest Path Visiting All Nodes (Hard)                              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // JAVA BUILT-IN METHODS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ JAVA BIT UTILITIES                                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ Integer.bitCount(n)        // Đếm số bit 1                                  │
   * │ Integer.highestOneBit(n)   // Bit cao nhất                                  │
   * │ Integer.lowestOneBit(n)    // Bit thấp nhất = n & (-n)                      │
   * │ Integer.numberOfLeadingZeros(n)   // Số 0 ở đầu                             │
   * │ Integer.numberOfTrailingZeros(n)  // Số 0 ở cuối                            │
   * │ Integer.reverse(n)         // Đảo ngược bits                                │
   * │ Integer.toBinaryString(n)  // Chuyển sang string binary                     │
   * │                                                                             │
   * │ Long.bitCount(n)           // Tương tự cho long                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  @Test
  void testJavaBuiltIns() {
    assertEquals(3, Integer.bitCount(0b1011));
    assertEquals(8, Integer.highestOneBit(0b1011));
    assertEquals(2, Integer.lowestOneBit(0b1010));  // 10 = 1010, lowest bit at pos 1 = 2
    assertEquals("1011", Integer.toBinaryString(11));
  }
}
