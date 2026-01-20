package com.practice.leetcode.bitwise;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║          3314. Construct the Minimum Bitwise Array I                      ║
 * ╠═══════════════════════════════════════════════════════════════════════════╣
 * ║ Difficulty: Easy                                                          ║
 * ║ Topics: Bit Manipulation, Array                                           ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class LC3314_ConstructMinBitwiseArray {

  // ═══════════════════════════════════════════════════════════════════════════
  // PROBLEM ANALYSIS (Phân tích đề bài)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ ĐỀ BÀI:                                                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ Cho mảng nums gồm các số nguyên tố.                                         │
   * │ Tìm mảng ans sao cho: ans[i] OR (ans[i] + 1) == nums[i]                     │
   * │ Và ans[i] phải là giá trị NHỎ NHẤT thỏa mãn điều kiện.                      │
   * │ Nếu không tìm được → trả về -1.                                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ KEY INSIGHT: x OR (x+1) hoạt động như thế nào?                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ Khi cộng 1 vào số x:                                                        │
   * │ • Các bit 1 liên tiếp từ phải (trailing 1s) → đổi thành 0                   │
   * │ • Bit 0 đầu tiên gặp được → đổi thành 1                                     │
   * │                                                                             │
   * │ VD:   5 = 101                                                               │
   * │     + 1                                                                     │
   * │     ─────                                                                   │
   * │       6 = 110    (bit 0 từ 1→0, bit 1 từ 0→1)                               │
   * │                                                                             │
   * │ Khi OR:  5 OR 6 = 101 OR 110 = 111 = 7                                      │
   * │                                                                             │
   * │ ⭐ NHẬN XÉT QUAN TRỌNG:                                                     │
   * │ x OR (x+1) sẽ BẬT tất cả bits từ vị trí 0 đến vị trí bit 0 đầu tiên của x   │
   * │                                                                             │
   * │ Vì vậy: x OR (x+1) luôn có dạng ...1111 (trailing 1s từ LSB)                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ PHÂN TÍCH CÁC TRƯỜNG HỢP:                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ nums = 7 = 111:                                                             │
   * │   Trailing 1s: bit 0, 1, 2 (3 bit 1 liên tiếp)                              │
   * │   Bit 0 đầu tiên của 7: không có trong phạm vi → ở vị trí 3                 │
   * │                                                                             │
   * │   Muốn x OR (x+1) = 111:                                                    │
   * │   • x cần có bit 0 tại vị trí 2 (để x+1 bật bit 2)                          │
   * │   • x = 011 = 3                                                             │
   * │   • Check: 3 OR 4 = 011 OR 100 = 111 = 7 ✓                                  │
   * │                                                                             │
   * │ nums = 11 = 1011:                                                           │
   * │   Bit 0 đầu tiên: tại vị trí 2 (từ phải đếm: 1,0,1,1 → vị trí 2 là 0)       │
   * │   Không phải! Let me recount: 1011 → bit0=1, bit1=1, bit2=0, bit3=1         │
   * │   Bit 0 đầu tiên: vị trí 2                                                  │
   * │                                                                             │
   * │   Nhưng ta cần trailing 1s. 1011 có bit0=1 là 1, bit1 cũng=1? Không!        │
   * │   1011: bit0=1, bit1=1, bit2=0 → trailing 1s chỉ có 1 bit (bit 0)           │
   * │                                                                             │
   * │   Bit 0 đầu tiên: vị trí 1                                                  │
   * │   x = 1011 với bit 0 cleared = 1010 = 10                                    │
   * │   Check: 10 OR 11 = 1010 OR 1011 = 1011 = 11 ✓                              │
   * │                                                                             │
   * │ nums = 2 = 10:                                                              │
   * │   Bit 0 (LSB) = 0! → x OR (x+1) luôn có bit 0 = 1                           │
   * │   Không thể có kết quả có bit 0 = 0                                         │
   * │   → Trả về -1                                                               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ THUẬT TOÁN:                                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. Tìm vị trí bit 0 THẤP NHẤT trong nums[i]                                 │
   * │    (Vị trí đầu tiên từ phải sang mà bit = 0)                                │
   * │                                                                             │
   * │ 2. Nếu vị trí đó = 0 (tức LSB = 0) → return -1                              │
   * │    Vì x OR (x+1) không thể có LSB = 0                                       │
   * │                                                                             │
   * │ 3. Nếu không, CLEAR bit tại vị trí (lowest_zero_pos - 1)                    │
   * │    ans[i] = nums[i] & ~(1 << (lowest_zero_pos - 1))                         │
   * │                                                                             │
   * │ GIẢI THÍCH BƯỚC 3:                                                          │
   * │ • nums[i] có trailing 1s từ bit 0 đến bit (k-1), với bit k là 0             │
   * │ • Ta cần x sao cho khi +1, bit k-1 → 0 và bit k → 1                         │
   * │ • Điều này có nghĩa x cần có bit 0 tại vị trí k-1                           │
   * │ • Vậy x = nums[i] với bit k-1 bị CLEAR                                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SOLUTION
  // ═══════════════════════════════════════════════════════════════════════════

  public int[] minBitwiseArray(java.util.List<Integer> nums) {
    int n = nums.size();
    int[] ans = new int[n];

    for (int i = 0; i < n; i++) {
      int num = nums.get(i);
      ans[i] = findMinX(num);
    }

    return ans;
  }

  /**
   * Tìm x nhỏ nhất sao cho x OR (x+1) = target
   */
  private int findMinX(int target) {
    // Bước 1: Tìm vị trí bit 0 thấp nhất
    int lowestZeroPos = findLowestZeroBit(target);

    // Bước 2: Nếu bit 0 ở vị trí 0 (LSB = 0) → không thể
    // Vì x OR (x+1) luôn có bit 0 = 1 (hoặc x lẻ, hoặc x+1 lẻ)
    if (lowestZeroPos == 0) {
      return -1;
    }

    // Bước 3: Clear bit tại vị trí (lowestZeroPos - 1)
    // Đây là bit cao nhất trong chuỗi trailing 1s
    int bitToClear = lowestZeroPos - 1;
    return target & ~(1 << bitToClear);
  }

  /**
   * Tìm vị trí bit 0 thấp nhất (từ phải sang)
   *
   * VD: 7 = 111 → trailing 1s, bit 0 đầu tiên ở vị trí 3 (ngoài số)
   *     11 = 1011 → bit0=1, bit1=1, bit2=0 → vị trí 2
   *     2 = 10 → bit0=0 → vị trí 0
   */
  private int findLowestZeroBit(int n) {
    int pos = 0;
    while ((n & 1) == 1) {  // Trong khi bit hiện tại = 1
      n >>= 1;              // Dịch phải
      pos++;                // Tăng vị trí
    }
    return pos;  // Vị trí bit 0 đầu tiên
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // VISUAL WALKTHROUGH (Minh họa từng bước)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ VÍ DỤ 1: nums = [7]                                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ target = 7 = 111 (binary)                                                   │
   * │                                                                             │
   * │ Bước 1: Tìm bit 0 thấp nhất                                                 │
   * │   111 → bit0=1 → shift → 11 → bit0=1 → shift → 1 → bit0=1 → shift → 0       │
   * │   pos = 3 (sau 3 lần shift mới gặp bit 0)                                   │
   * │                                                                             │
   * │ Bước 2: pos = 3 ≠ 0 → tiếp tục                                              │
   * │                                                                             │
   * │ Bước 3: Clear bit tại vị trí 2 (= 3-1)                                      │
   * │   target = 111                                                              │
   * │   ~(1 << 2) = ~100 = ...11111011                                            │
   * │   111 & ...11111011 = 011 = 3                                               │
   * │                                                                             │
   * │ Kiểm tra: 3 OR 4 = 011 OR 100 = 111 = 7 ✓                                   │
   * │                                                                             │
   * │ ans = [3]                                                                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ VÍ DỤ 2: nums = [2]                                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ target = 2 = 10 (binary)                                                    │
   * │                                                                             │
   * │ Bước 1: Tìm bit 0 thấp nhất                                                 │
   * │   10 → bit0=0 → pos = 0 (gặp bit 0 ngay lập tức)                            │
   * │                                                                             │
   * │ Bước 2: pos = 0 → KHÔNG THỂ!                                                │
   * │   Vì x OR (x+1) luôn có bit 0 = 1:                                          │
   * │   • Nếu x chẵn: x = ...0, x+1 = ...1 → OR có bit0 = 1                       │
   * │   • Nếu x lẻ: x = ...1 → OR có bit0 = 1                                     │
   * │                                                                             │
   * │ ans = [-1]                                                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ VÍ DỤ 3: nums = [11, 13, 31]                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ─── target = 11 = 1011 ───                                                  │
   * │ Bit 0 thấp nhất: pos = 2 (1011 → bit0=1, bit1=1, bit2=0)                    │
   * │ Clear bit 1: 1011 & ~(10) = 1011 & 1101 = 1001 = 9                          │
   * │ Hmm, that doesn't seem right. Let me recalculate.                           │
   * │                                                                             │
   * │ Actually: 1011 → trailing 1s analysis:                                      │
   * │   bit0 = 1 (continue)                                                       │
   * │   bit1 = 1 (continue)                                                       │
   * │   Wait, 1011: rightmost bit is 1, next is 1, next is 0, next is 1           │
   * │   So 11 = 1011: bit0=1, bit1=1, bit2=0                                      │
   * │   First 0 at position 2? No wait let me recount                             │
   * │   11 in binary: 1011                                                        │
   * │   Position: 3210                                                            │
   * │   Bits:     1011                                                            │
   * │   bit0=1, bit1=1, bit2=0, bit3=1                                            │
   * │   First 0 is at position 2                                                  │
   * │                                                                             │
   * │   Clear bit (2-1)=1: 1011 & ~(0010) = 1011 & 1101 = 1001 = 9                │
   * │   Check: 9 OR 10 = 1001 OR 1010 = 1011 = 11 ✓                               │
   * │                                                                             │
   * │ Wait, I made an error above. Let me redo:                                   │
   * │   11 = 8 + 2 + 1 = 1011                                                     │
   * │   Checking trailing 1s: bit0=1 ✓ → shift                                    │
   * │   5 = 0101: bit0=1 ✓ → shift                                                │
   * │   2 = 0010: bit0=0 ✗ → stop at pos=2                                        │
   * │                                                                             │
   * │ Hmm 11 >> 1 = 5 = 101, not matching my bit analysis.                        │
   * │ 11 = 1011, 11>>1 = 5 = 101                                                  │
   * │ 5 & 1 = 1, so continue                                                      │
   * │ 5 >> 1 = 2 = 10                                                             │
   * │ 2 & 1 = 0, stop                                                             │
   * │ pos = 2 is wrong, it should be 2 iterations, so pos = 2? Let me trace:      │
   * │                                                                             │
   * │ n=11, pos=0                                                                 │
   * │ 11 & 1 = 1 (bit0=1) → n=5, pos=1                                            │
   * │ 5 & 1 = 1 (bit0=1) → n=2, pos=2                                             │
   * │ 2 & 1 = 0 (bit0=0) → stop, return pos=2                                     │
   * │                                                                             │
   * │ Hmm but 11 = 1011, the 0 is at bit position 2? Let's see:                   │
   * │ bit3 bit2 bit1 bit0                                                         │
   * │  1    0    1    1                                                           │
   * │ So bit2 = 0. The first 0 from right is at position 2. But wait...           │
   * │ trailing 1s means consecutive 1s from bit0.                                 │
   * │ 1011: bit0=1, bit1=1, bit2=0                                                │
   * │ So we have 2 trailing 1s (at positions 0 and 1), first 0 at position 2.     │
   * │                                                                             │
   * │ Wait no, my algorithm shifts and counts. After 2 shifts (pos=2), we find 0. │
   * │ But bit1 of 11 = (11 >> 1) & 1 = 5 & 1 = 1                                  │
   * │ So bit0=1, bit1=1 are both 1, meaning trailing 1s = 2 bits.                 │
   * │ First 0 is at position 2.                                                   │
   * │ Clear bit 1: 11 & ~2 = 1011 & 1101 = 1001 = 9                               │
   * │ Check: 9 | 10 = 1001 | 1010 = 1011 = 11 ✓                                   │
   * │                                                                             │
   * │ But wait, the expected output for 11 should be 10 based on my earlier       │
   * │ analysis. Let me re-verify:                                                 │
   * │ 10 | 11 = 1010 | 1011 = 1011 = 11 ✓                                         │
   * │ 9 | 10 = 1001 | 1010 = 1011 = 11 ✓                                          │
   * │ Both work! But we need MINIMUM. 9 < 10, so answer is 9.                     │
   * │                                                                             │
   * │ My algorithm gives 9, which is correct (minimum).                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // COMPLEXITY ANALYSIS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Time Complexity: O(n * 32) = O(n)
   *   - Duyệt qua n phần tử
   *   - Mỗi phần tử tìm bit 0 thấp nhất: tối đa 32 bit
   *
   * Space Complexity: O(n)
   *   - Mảng kết quả có n phần tử
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // TESTS
  // ═══════════════════════════════════════════════════════════════════════════

  @Test
  void testExample1() {
    // nums = [2, 3, 5, 7]
    // 2: LSB=0 → -1
    // 3 = 11: first 0 at pos 2, clear bit 1 → 01 = 1. Check: 1|2 = 11 = 3 ✓
    // 5 = 101: first 0 at pos 1, clear bit 0 → 100 = 4. Check: 4|5 = 101 = 5 ✓
    // 7 = 111: first 0 at pos 3, clear bit 2 → 011 = 3. Check: 3|4 = 111 = 7 ✓
    int[] result = minBitwiseArray(java.util.Arrays.asList(2, 3, 5, 7));
    assertArrayEquals(new int[]{-1, 1, 4, 3}, result);
  }

  @Test
  void testExample2() {
    // nums = [11, 13, 31]
    // 11 = 1011: first 0 at pos 2, clear bit 1 → 1001 = 9
    // 13 = 1101: first 0 at pos 1, clear bit 0 → 1100 = 12
    // 31 = 11111: first 0 at pos 5, clear bit 4 → 01111 = 15
    int[] result = minBitwiseArray(java.util.Arrays.asList(11, 13, 31));
    assertArrayEquals(new int[]{9, 12, 15}, result);
  }

  @Test
  void testSingleElement() {
    assertArrayEquals(new int[]{-1}, minBitwiseArray(java.util.Arrays.asList(2)));
    assertArrayEquals(new int[]{1}, minBitwiseArray(java.util.Arrays.asList(3)));
    assertArrayEquals(new int[]{4}, minBitwiseArray(java.util.Arrays.asList(5)));
  }

  @Test
  void testVerifyOrProperty() {
    // Verify: ans OR (ans+1) == nums for all valid cases
    int[] nums = {3, 5, 7, 11, 13, 17, 19, 23, 29, 31};
    for (int num : nums) {
      int ans = findMinX(num);
      if (ans != -1) {
        assertEquals(num, ans | (ans + 1),
            String.format("Failed for num=%d: %d OR %d = %d",
                num, ans, ans + 1, ans | (ans + 1)));
      }
    }
  }
}
