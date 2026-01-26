package com.practice.leetcode.bitwise;

// JUnit imports removed

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

    // @Test
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
        int setBitRes = n | (1 << 2); // Bật bit 2 (đang là 0) thành 1 → 1010 | 0100 = 1110 (14)
        assertEquals(14, setBitRes);

        // 3. CLEAR BIT (Tắt bit thứ i về 0)
        // n & ~(1 << i)
        int clearBitRes = n & ~(1 << 1); // Tắt bit 1 (đang là 1) → 1010 & 1101 = 1000 (8)
        assertEquals(8, clearBitRes);
        
        // 4. TOGGLE BIT (Đảo trạng thái bit thứ i)
        // n ^ (1 << i)
        int toggleBitRes = n ^ (1 << 3); // Đảo bit 3 (đang 1) thành 0 → 1010 ^ 1000 = 0010 (2)
        assertEquals(2, toggleBitRes);
        
        // 5. UPDATE BIT (Đặt bit i thành giá trị v - 0 hoặc 1)
        // Clear trước rồi Set sau
        // (n & ~(1 << i)) | (v << i)
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PART 2: ESSENTIAL TRICKS (CÁC MẸO HAY DÙNG)
    // ═══════════════════════════════════════════════════════════════════════════

    // @Test
    void essentialTricks() {
        int n = 40; // Binary: 101000
        
        // 1. CHECK POWER OF 2 (Kiểm tra lũy thừa 2)
        // x & (x - 1) == 0 (với x > 0)
        assertTrue((16 & (16 - 1)) == 0);
        assertFalse((18 & (18 - 1)) == 0);

        // 2. CLEAR LOWEST SET BIT (Xóa bit 1 thấp nhất) - Brian Kernighan’s Alg
        // n = n & (n - 1)
        // Ví dụ: 101000 (40) → 101000 & 100111 = 100000 (32)
        assertEquals(32, n & (n - 1));

        // 3. GET LOWEST SET BIT (Lấy bit 1 thấp nhất)
        // n & -n
        // Giải thích: -n là bù 2 của n (~n + 1). 
        // 40 (101000) → -40 (011000) → 40 & -40 = 001000 (8)
        assertEquals(8, n & -n);

        // 4. CLEAR ALL BITS FROM MSB TO i (Inclusive)
        // n & ((1 << i) - 1) → Modulo 2^i
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
    
    // @Test
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

    // @Test
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

    // @Test
    void grayCodeFormula() {
        // G(i) = i ^ (i >> 1)
        // Đã được verify ở bài GrayCode.java
        int i = 2; // 010
        int gray = i ^ (i >> 1); // 010 ^ 001 = 011 (3)
        assertEquals(3, gray);
    }
    
    // @Test
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

    // @Test
    void test() {
      // Java có sẵn Integer.bitCount(n).
      // Nhưng nếu tự implement (Brian Kernighan):
      System.out.println(4 - 3);
      System.out.println(4 + (~3 + 1));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PART 5: COMBINATORICS PATTERNS (CÁC MẪU TỔ HỢP)
    // ═══════════════════════════════════════════════════════════════════════════

    // @Test
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


    // ═══════════════════════════════════════════════════════════════════════════
    // PART 6: CASE STUDY - LEETCODE 393 (UTF-8 VALIDATION)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * 393. UTF-8 Validation
     * 
     * NGUYÊN LÝ:
     * Kiểm tra xem mảng số nguyên có đại diện cho chuỗi UTF-8 hợp lệ không.
     * 
     * QUY TẮC UTF-8:
     * 1. 1-byte char: 0xxxxxxx (Bắt đầu bằng 0)
     * 2. 2-byte char: 110xxxxx 10xxxxxx
     * 3. 3-byte char: 1110xxxx 10xxxxxx 10xxxxxx
     * 4. 4-byte char: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
     * 
     * CÁCH GIẢI (Bit Manipulation):
     * - Duyệt từng byte trong mảng.
     * - Nếu `numberOfBytesToProcess == 0`:
     *   - Kiểm tra xem byte này là header của loại mấy bytes?
     *   - Nếu 0xxxxxxx → 1-byte char, OK, tiếp tục.
     *   - Nếu 110xxxxx → Cần thêm 1 byte (set numberOfBytesToProcess = 1).
     *   - Nếu 1110xxxx → Cần thêm 2 byte (set numberOfBytesToProcess = 2).
     *   - Nếu 11110xxx → Cần thêm 3 byte (set numberOfBytesToProcess = 3).
     *   - Khác → Sai (Invalid header, ví dụ 10xxxxxx hay 11111xxx).
     * - Nếu `numberOfBytesToProcess > 0`:
     *   - Kiểm tra byte hiện tại có phải là continuation byte (10xxxxxx) không?
     *   - Nếu đúng (byte & 11000000 == 10000000) → Giảm numberOfBytesToProcess.
     *   - Nếu sai → Return `false`.
     * 
     * BITMASKS QUAN TRỌNG:
     * - 0x80 (10000000): Check bit đầu tiên.
     * - 0xE0 (11100000) = 0xC0 (110 on header): Mask check headers.
     * 
     * @param data Mảng các số nguyên (chỉ quan tâm 8 bit cuối)
     * @return true nếu hợp lệ
     */
    public boolean validUtf8(int[] data) {
        int numberOfBytesToProcess = 0;

        // Các Mask để kiểm tra header
        int mask1 = 1 << 7; // 10000000 (0x80)
        int mask2 = 1 << 6; // 01000000 (0x40)

        for (int i = 0; i < data.length; i++) {
            // Chỉ lấy 8 bit cuối của số nguyên (phòng trường hợp input > 255 nhưng đề bài nói <= 255)
            // Tuy nhiên int trong Java là 32 bit, nên thao tác bit vẫn chuẩn.
            int bin = data[i];

            // Nếu đang không chờ byte nào (đang ở đầu một ký tự mới)
            if (numberOfBytesToProcess == 0) {
                // Đếm số lượng bit 1 liên tiếp ở đầu
                int mask = 1 << 7;
                while ((bin & mask) != 0) {
                    numberOfBytesToProcess++;
                    mask = mask >> 1;
                }

                // Trường hợp 1 byte: 0xxxxxxx → count = 0
                if (numberOfBytesToProcess == 0) {
                    continue;
                }

                // UTF-8 chỉ cho phép 1 đến 4 bytes
                // Header 10xxxxxx (count=1) là invalid cho start byte
                // Header 11111xxx (count>4) là invalid
                if (numberOfBytesToProcess == 1 || numberOfBytesToProcess > 4) {
                    return false;
                }
                
                // Trừ đi 1 vì header byte đã được tính vào tổng length,
                // numberOfBytesToProcess ở đây ý nghĩa là "số byte TIẾP THEO cần check"
                numberOfBytesToProcess--; 
            } else {
                // Đang trong chuỗi byte (Continuation Byte)
                // Bắt buộc phải là dạng 10xxxxxx
                // Check: bit 7 là 1 VÀ bit 6 là 0
                boolean isBit1Set = (bin & mask1) != 0;
                boolean isBit2Set = (bin & mask2) != 0;

                if (!(isBit1Set && !isBit2Set)) {
                    return false;
                }
                
                numberOfBytesToProcess--;
            }
        }

        // Nếu duyệt hết mà vẫn còn đang chờ byte tiếp theo → False
        return numberOfBytesToProcess == 0;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PART 7: CASE STUDY - LEETCODE 397 (INTEGER REPLACEMENT)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * 397. Integer Replacement
     * 
     * MỤC TIÊU:
     * Đưa số n về 1 với số bước ít nhất.
     * Quy tắc:
     * - Nếu chẵn: n / 2
     * - Nếu lẻ: n + 1 hoặc n - 1
     * 
     * CHIẾN THUẬT GREEDY (Dựa trên Bit):
     * Để giảm nhanh nhất, ta muốn tạo ra càng nhiều số chẵn (để chia đôi) càng tốt.
     * Cụ thể là muốn tạo ra số chia hết cho 4.
     * 
     * Phân tích số lẻ dưới dạng nhị phân (...xx):
     * 1. Kết thúc là 01 (n % 4 == 1):
     *    - n - 1 → ...00 (chia hết cho 4) → TỐT
     *    - n + 1 → ...10 (chỉ chia hết cho 2)
     *    => Chọn n - 1
     * 
     * 2. Kết thúc là 11 (n % 4 == 3):
     *    - n + 1 → ...00 (chia hết cho 4, nhớ có nhớ bit) → TỐT HƠN
     *    - n - 1 → ...10 (chỉ chia hết cho 2)
     *    => Chọn n + 1
     * 
     * NGOẠI LỆ: 
     * - n = 3 (binary 11): 
     *   - n - 1 → 2 → 1 (2 bước)
     *   - n + 1 → 4 → 2 → 1 (3 bước)
     *   => Với n=3, chọn n - 1 dù nó kết thúc là 11.
     * 
     * BIẾN KIỂM SOÁT:
     * - Sử dụng `long` để tránh Overflow khi n = Integer.MAX_VALUE (vì n + 1 sẽ tràn).
     */
    public int integerReplacement(int n) {
        long num = n; // Ép kiểu sang long để tránh overflow
        int count = 0;

        while (num > 1) {
            if ((num & 1) == 0) {
                // Nếu là số chẵn → Chia đôi
                num >>= 1;
            } else {
                // Nếu là số lẻ
                if (num == 3) {
                    // Ngoại lệ đặc biệt
                    num--;
                } else if ((num & 3) == 3) { // Tương đương num % 4 == 3 (Binary ...11)
                    // Chọn +1 để tạo ra nhiều số 0 phía sau hơn (overflow bit 1 sang trái)
                    num++;
                } else { // Tương đương num % 4 == 1 (Binary ...01)
                    num--;
                }
            }
            count++;
        }
        return count;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PART 8: CASE STUDY - LEETCODE 401 (BINARY WATCH)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * 401. Binary Watch
     * 
     * MÔ TẢ:
     * Một chiếc đồng hồ nhị phân có 4 đèn LED ở trên (giờ: 0-11) và 6 đèn LED ở dưới (phút: 0-59).
     * Cho số nguyên `turnedOn` là tổng số đèn đang sáng, trả về tất cả các thời điểm có thể.
     * 
     * CHIẾN THUẬT (Bit Counting):
     * Thay vì sinh ra các tổ hợp bit phức tạp, ta có thể duyệt qua tất cả các mốc thời gian 
     * khả thi (12 giờ * 60 phút = 720 trạng thái) và đếm số bit 1.
     * 
     * CÔNG THỨC:
     * - Số bit 1 của giờ: Integer.bitCount(h)
     * - Số bit 1 của phút: Integer.bitCount(m)
     * - Điều kiện: bitCount(h) + bitCount(m) == turnedOn
     * 
     * ĐỊNH DẠNG ĐẦU RA:
     * - Giờ: Không có số 0 ở đầu (VD: "1:00").
     * - Phút: Luôn có 2 chữ số (VD: "10:02"). Sử dụng String.format("%d:%02d", h, m).
     * 
     * @param turnedOn Tổng số đèn LED đang sáng
     * @return Danh sách các thời điểm hợp lệ
     */
    public List<String> readBinaryWatch(int turnedOn) {
        List<String> times = new ArrayList<>();
        
        // Duyệt qua tất cả giờ (0-11) và phút (0-59)
        for (int h = 0; h < 12; h++) {
            for (int m = 0; m < 60; m++) {
                // Đếm tổng số bit 1 trong h và m
                if (Integer.bitCount(h) + Integer.bitCount(m) == turnedOn) {
                    // Format phút luôn có 2 chữ số
                    times.add(String.format("%d:%02d", h, m));
                }
            }
        }
        
        return times;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PART 9: CASE STUDY - LEETCODE 405 (CONVERT TO HEXADECIMAL)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * 405. Convert a Number to Hexadecimal
     * 
     * NGUYÊN LÝ:
     * Số nguyên 32-bit có thể được biểu diễn bằng 8 chữ số Hexadecimal (mỗi chữ số 4 bit).
     * 
     * THUẬT TOÁN:
     * 1. Dùng Mask `0xf` (1111) để lấy 4 bit cuối cùng.
     * 2. Ánh xạ 4 bit đó sang ký tự Hex ('0'-'9', 'a'-'f').
     * 3. Dùng dịch phải không dấu `>>>` 4 bit để đưa nhóm 4 bit tiếp theo về cuối.
     * 4. Lặp lại cho đến khi số bằng 0 (hoặc tối đa 8 lần để xử lý số âm).
     * 
     * TẠI SAO DÙNG `>>>`?
     * - Với số âm, `>>` (dịch phải có dấu) sẽ giữ nguyên bit 1 ở đầu (Sign extension), 
     *   dẫn đến vòng lặp vô tận hoặc sai kết quả.
     * - `>>>` sẽ chèn số 0 vào bên trái, giúp số âm cuối cùng cũng sẽ trở về 0.
     * 
     * @param num Số nguyên cần chuyển đổi
     * @return Chuỗi Hexadecimal
     */
    public String toHex(int num) {
        if (num == 0) return "0";
        
        char[] hexChars = "0123456789abcdef".toCharArray();
        StringBuilder sb = new StringBuilder();
        
        // Một số int 32 bit có tối đa 8 ký tự hex
        // Dùng vòng lặp hoặc check num != 0
        while (num != 0 && sb.length() < 8) {
            // Lấy 4 bit cuối
            int val = num & 0xf;
            sb.append(hexChars[val]);
            // Dịch phải không dấu 4 bit
            num >>>= 4;
        }
        
        // Vì ta lấy từ phải sang trái, nên cần reverse
        return sb.reverse().toString();
    }

    // @Test
    void testReadBinaryWatch() {
        // Case: 1 LED sáng
        List<String> res1 = readBinaryWatch(1);
        assertTrue(res1.contains("1:00"));
        assertTrue(res1.contains("0:01"));
        assertTrue(res1.contains("0:32"));
        assertEquals(10, res1.size()); // 4 đèn giờ + 6 đèn phút = 10 cách

        // Case: 0 LED sáng
        List<String> res0 = readBinaryWatch(0);
        assertEquals(1, res0.size());
        assertEquals("0:00", res0.get(0));

        // Case: Nhiều LED sáng (Max có thể là 8: 1011 (3) + 111011 (5))
        List<String> res9 = readBinaryWatch(9);
        assertEquals(0, res9.size());
    }

    // @Test
    void testIntegerReplacement() {
        assertEquals(3, integerReplacement(8));  // 8 → 4 → 2 → 1
        assertEquals(4, integerReplacement(7));  // 7 → 8 → 4 → 2 → 1
        assertEquals(2, integerReplacement(3));  // 3 → 2 → 1 (Exception case)
        assertEquals(32, integerReplacement(Integer.MAX_VALUE)); // 2147483647 → +1 overflow logic handled
    }
    // @Test
    void testValidUtf8() {
         assertTrue(validUtf8(new int[]{197, 130, 1}));
         assertFalse(validUtf8(new int[]{235, 140, 4}));
    }

    // @Test
    void testToHex() {
        assertEquals("1a", toHex(26));
        assertEquals("ffffffff", toHex(-1));
        assertEquals("0", toHex(0));
        assertEquals("e", toHex(14));
        assertEquals("fffffffb", toHex(-5));
    }

    public static void main(String[] args) {
        System.out.println("Running BitManipulationGuide tests...");
        BitManipulationGuide guide = new BitManipulationGuide();
        try {
            guide.basicOperations();
            System.out.println("  [PASS] basicOperations");
            guide.essentialTricks();
            System.out.println("  [PASS] essentialTricks");
            guide.xorMagic();
            System.out.println("  [PASS] xorMagic");
            guide.iterateSubsets();
            System.out.println("  [PASS] iterateSubsets");
            guide.grayCodeFormula();
            System.out.println("  [PASS] grayCodeFormula");
            guide.countSetBits();
            System.out.println("  [PASS] countSetBits");
            guide.test();
            guide.subsetsWithDuplicates();
            System.out.println("  [PASS] subsetsWithDuplicates");
            guide.testValidUtf8();
            System.out.println("  [PASS] testValidUtf8");
            guide.testIntegerReplacement();
            System.out.println("  [PASS] testIntegerReplacement");
            guide.testReadBinaryWatch();
            System.out.println("  [PASS] testReadBinaryWatch");
            guide.testToHex();
            System.out.println("  [PASS] testToHex");
            System.out.println("ALL TESTS PASSED!");
        } catch (Exception e) {
            System.err.println("TEST FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper assertions to replace JUnit
    private static void assertTrue(boolean condition) {
        if (!condition) throw new RuntimeException("Assertion failed: expected true");
    }
    
    private static void assertTrue(boolean condition, String message) {
        if (!condition) throw new RuntimeException(message);
    }
    
    private static void assertFalse(boolean condition) {
        if (condition) throw new RuntimeException("Assertion failed: expected false");
    }
    
    private static void assertFalse(boolean condition, String message) {
        if (condition) throw new RuntimeException(message);
    }

    private static void assertEquals(int expected, int actual) {
        if (expected != actual) throw new RuntimeException("Assertion failed: expected " + expected + ", but got " + actual);
    }
    
    private static void assertEquals(long expected, long actual) {
         if (expected != actual) throw new RuntimeException("Assertion failed: expected " + expected + ", but got " + actual);
    }
    
    private static void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) return;
        if (expected == null || !expected.equals(actual)) {
            throw new RuntimeException("Assertion failed: expected " + expected + ", but got " + actual);
        }
    }
}
