package com.practice.leetcode.bitwise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ BIT MANIPULATION GUIDE & TEMPLATES                                        ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * Tổng hợp các công thức, mẹo vặt và mẫu code (templates) quan trọng về Bitwise.
 * Dùng làm tài liệu tra cứu (Cheat Sheet) cho LeetCode.
 */
public class BitManipulationGuide {

    // ═══════════════════════════════════════════════════════════════════════════
    // PART 1: BASIC OPERATIONS (CÁC THAO TÁC CƠ BẢN)
    // ═══════════════════════════════════════════════════════════════════════════
    /*
     * INDEXING: Bit vị trí 0 là bit ngoài cùng bên phải (Least Significant Bit).
     * MASK 1 << i: Tạo ra số chỉ có bit thứ i bằng 1, còn lại bằng 0.
     */

    @Test
    void basicOperations() {
        int n = 0b0000_1010; // 10 in decimal, bit index: 3, 1 are 1

        // 1. GET BIT (Lấy giá trị bit thứ i)
        // (n >> i) & 1  HOẶC  (n & (1 << i)) != 0
        int i = 3;
        boolean isBitSet = ((n >> i) & 1) == 1;
        assertTrue(isBitSet, "Bit 3 of 10 should be 1");
        assertFalse(((n >> 2) & 1) == 1, "Bit 2 of 10 should be 0");

        // 2. SET BIT (Bật bit thứ i lên 1)
        // n | (1 << i)
        int setBitRes = n | (1 << 2); // Bật bit 2 (đang là 0) thành 1 -> 1010 | 0100 = 1110 (14)
        assertEquals(14, setBitRes);

        // 3. CLEAR BIT (Tắt bit thứ i về 0)
        // n & ~(1 << i)
        int clearBitRes = n & ~(1 << 1); // Tắt bit 1 (đang là 1) -> 1010 & 1101 = 1000 (8)
        assertEquals(8, clearBitRes);
        
        // 4. TOGGLE BIT (Đảo trạng thái bit thứ i)
        // n ^ (1 << i)
        int toggleBitRes = n ^ (1 << 3); // Đảo bit 3 (đang 1) thành 0 -> 1010 ^ 1000 = 0010 (2)
        assertEquals(2, toggleBitRes);
        
        // 5. UPDATE BIT (Đặt bit i thành giá trị v - 0 hoặc 1)
        // Clear trước rồi Set sau
        // (n & ~(1 << i)) | (v << i)
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PART 2: ESSENTIAL TRICKS (CÁC MẸO HAY DÙNG)
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void essentialTricks() {
        int n = 40; // Binary: 101000
        
        // 1. CHECK POWER OF 2 (Kiểm tra lũy thừa 2)
        // x & (x - 1) == 0 (với x > 0)
        assertTrue((16 & (16 - 1)) == 0);
        assertFalse((18 & (18 - 1)) == 0);

        // 2. CLEAR LOWEST SET BIT (Xóa bit 1 thấp nhất) - Brian Kernighan’s Alg
        // n = n & (n - 1)
        // Ví dụ: 101000 (40) -> 101000 & 100111 = 100000 (32)
        assertEquals(32, n & (n - 1));

        // 3. GET LOWEST SET BIT (Lấy bit 1 thấp nhất)
        // n & -n
        // Giải thích: -n là bù 2 của n (~n + 1). 
        // 40 (101000) -> -40 (011000) -> 40 & -40 = 001000 (8)
        assertEquals(8, n & -n);

        // 4. CLEAR ALL BITS FROM MSB TO i (Inclusive)
        // n & ((1 << i) - 1) -> Modulo 2^i
        // Giữ lại các bit từ 0 đến i-1
        int x = 0b11101; // 29
        // Muốn giữ lại 3 bit cuối: & với 0b111 (7)
        assertEquals(5, x & ((1 << 3) - 1)); // 11101 & 00111 = 00101 (5)
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PART 3: XOR MAGIC (SỨC MẠNH CỦA XOR)
    // ═══════════════════════════════════════════════════════════════════════════
    /*
     * Tính chất:
     * a ^ a = 0
     * a ^ 0 = a
     * a ^ b ^ a = b
     */
    
    @Test
    void xorMagic() {
        // 1. SWAP WITHOUT TEMP
        int a = 5, b = 7;
        a = a ^ b;
        b = a ^ b; // (a^b)^b = a
        a = a ^ b; // (a^b)^a = b
        assertEquals(7, a);
        assertEquals(5, b);

        // 2. FIND MISSING NUMBER (0 to n)
        // XOR tất cả index và tất cả value. Cái còn lại là số thiếu.
        int[] nums = {3, 0, 1}; // Thiếu 2
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            res ^= i ^ nums[i];
        }
        res ^= nums.length;
        assertEquals(2, res);

        // 3. FIND SINGLE NUMBER (Mọi số xuất hiện 2 lần trừ 1 số)
        int[] arr = {4, 1, 2, 1, 2};
        int single = 0;
        for (int x : arr) single ^= x;
        assertEquals(4, single);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PART 4: ADVANCED PATTERNS (CÁC MẪU NÂNG CAO)
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void iterateSubsets() {
        // 1. ITERATE ALL SUBMASK OF A MASK (Duyệt mọi tập con của một tập hợp bit)
        // Đây là kỹ thuật cực mạnh cho bài toán Bitmask DP.
        // mask = 101 (Decimal 5), subsets: 101, 100, 001, 000
        
        int mask = 5; 
        List<Integer> subsets = new ArrayList<>();
        
        // Template duyệt giảm dần
        for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {
            subsets.add(sub);
        }
        subsets.add(0); // Đừng quên tập rỗng nếu cần

        // Expected: 5, 4, 1, 0
        assertTrue(subsets.contains(5));
        assertTrue(subsets.contains(4));
        assertTrue(subsets.contains(1));
        assertTrue(subsets.contains(0));
        assertEquals(4, subsets.size());
    }

    @Test
    void grayCodeFormula() {
        // G(i) = i ^ (i >> 1)
        // Đã được verify ở bài GrayCode.java
        int i = 2; // 010
        int gray = i ^ (i >> 1); // 010 ^ 001 = 011 (3)
        assertEquals(3, gray);
    }
    
    @Test
    void countSetBits() {
        // Java có sẵn Integer.bitCount(n).
        // Nhưng nếu tự implement (Brian Kernighan):
        int n = 0b101101;
        int count = 0;
        while (n > 0) {
            n &= (n - 1); // Xóa bit 1 cuối cùng
            count++;
        }
        assertEquals(4, count);
    }

    @Test
    void test() {
      // Java có sẵn Integer.bitCount(n).
      // Nhưng nếu tự implement (Brian Kernighan):
      System.out.println(4 - 3);
      System.out.println(4 + (~3 + 1));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PART 5: COMBINATORICS PATTERNS (CÁC MẪU TỔ HỢP)
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void subsetsWithDuplicates() {
        /*
         * SUBSETS II (Mảng có phần tử trùng)
         * Input: [1, 2, 2]
         * Cách giải: Bit Manipulation + Duplicate Check
         */
        
        int[] nums = {1, 2, 2};
        Arrays.sort(nums); // BẮT BUỘC: Gom các số trùng lại gần nhau
        int n = nums.length;
        List<List<Integer>> result = new ArrayList<>();

        // Duyệt mọi trạng thái từ 0 đến 2^n - 1
        for (int i = 0; i < (1 << n); i++) {
            List<Integer> subset = new ArrayList<>();
            boolean isBadMask = false;

            for (int j = 0; j < n; j++) {
                // Kiểm tra xem bit thứ j có được bật không
                if ((i >> j & 1) == 1) {
                    // -----------------------------------------------------------
                    // LUẬT CHỐNG TRÙNG (THE DUPLICATE RULE):
                    // "Nếu tôi giống thằng trước, tôi chỉ được đi nếu thằng trước cũng đi."
                    // 1. nums[j] == nums[j-1]: Check GIÁ TRỊ trùng
                    // 2. (i >> (j-1) & 1) == 0: Check TRẠNG THÁI thằng trước (0 = Không chọn)
                    // -----------------------------------------------------------
                    if (j > 0 && nums[j] == nums[j - 1] && (i >> (j - 1) & 1) == 0) {
                        isBadMask = true;
                        break; // Mask này không hợp lệ, bỏ qua luôn
                    }
                    subset.add(nums[j]);
                }
            }

            if (!isBadMask) {
                result.add(subset);
            }
        }

        // Kiểm tra kết quả
        // Expected size: 6 ([], [1], [2], [1,2], [2,2], [1,2,2])
        // Nếu không check trùng sẽ ra 8 (dư [2] và [1,2])
        assertEquals(6, result.size());
        assertTrue(result.contains(Arrays.asList(1, 2, 2)));
        assertTrue(result.contains(Arrays.asList(2)));
    }


    @Test
    void testt() {
      int[] nums = {1,1,2};

      for(int i = 0; i < (1 << nums.length); i++) {

        for(int j = 0; j < nums.length; j++) {

          if(j > 0 && nums[j] == nums[j - 1] && ((i << j - 1) & 1) == 0) {
            System.out.println("dup");
          }

        }

      }
    }
}
