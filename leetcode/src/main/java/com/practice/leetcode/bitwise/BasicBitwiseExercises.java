package com.practice.leetcode.bitwise;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ BÀI TẬP THỰC HÀNH BITWISE TỪ CƠ BẢN ĐẾN NÂNG CAO (LEVEL 1 - 6)            ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * MỤC TIÊU:
 * Rèn luyện phản xạ với 18 bài tập Bitwise phổ biến nhất trong phỏng vấn.
 */
public class BasicBitwiseExercises {

    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("   BẮT ĐẦU LUYỆN TẬP BITWISE (AUTO GRADING)");
        System.out.println("════════════════════════════════════════════════════════");

        // LEVEL 1
        System.out.println("\n--- [LEVEL 1] KIỂM TRA CƠ BẢN ---");
        test("1. Kiểm tra số lẻ (isOdd)", isOdd(5), true);
        test("2. Check bit thứ 2 của 5 (101) → true", isBitSet(5, 2), true);

        // LEVEL 2
        System.out.println("\n--- [LEVEL 2] THAO TÁC CƠ BẢN ---");
        testVal("3. Bật bit 1 của 5 (101 → 111)", setBit(5, 1), 7);
        testVal("4. Tắt bit 1 của 7 (111 → 101)", clearBit(7, 1), 5);
        testVal("4b. [BONUS] Tắt bit từ 2 về 0 của 31 (11111 → 11000)", clearBitsFromItTo0(31, 2), 24);
        testVal("4c. [BONUS] Tắt bit từ MSB về 2 của 31 (11111 → 00011)", clearBitsFromMSBToIt(31, 2), 3);
        testVal("5. Đảo bit 0 của 5 (101 → 100)", toggleBit(5, 0), 4);

        // LEVEL 3
        System.out.println("\n--- [LEVEL 3] CÁC THỦ THUẬT (TRICKS) ---");
        testVal("6. Xóa bit 1 thấp nhất của 12 (1100 → 1000)", removeLastSetBit(12), 8);
        testVal("7. Giữ lại bit 1 thấp nhất của 12 (1100 → 0100)", isolateRightmostBit(12), 4);
        test("8. Kiểm tra 16 là lũy thừa 2", isPowerOfTwo(16), true);

        // LEVEL 4 (NEW)
        System.out.println("\n--- [LEVEL 4] TÍNH TOÁN & ĐẾM (ARITHMETIC) ---");
        testVal("9. Đếm số bit 1 của 7 (111)", countSetBits(7), 3);
        testVal("9. Đếm số bit 1 của 128 (10000000)", countSetBits(128), 1);
        testVal("10. Hamming Distance(1, 4) (001, 100) → 2", hammingDistance(1, 4), 2);
        
        int[] swapped = swap(10, 20);
        String swapRes = (swapped[0] == 20 && swapped[1] == 10) ? "✅ PASS" : "❌ FAIL";
        System.out.printf("%-50s | %s%n", "11. Swap 10, 20 without temp", swapRes);

        // LEVEL 5 (NEW)
        System.out.println("\n--- [LEVEL 5] XOR PATTERNS ---");
        testVal("12. Tìm số xuất hiện 1 lần [2,2,3,2] (Single Number I)", findSingleNumber(new int[]{4, 1, 2, 1, 2}), 4);
        testVal("13. Tìm số còn thiếu [3,0,1]", findMissingNumber(new int[]{3, 0, 1}), 2);
        test("14. Check xen kẽ bits (5=101)", hasAlternatingBits(5), true);
        test("14. Check xen kẽ bits (7=111)", hasAlternatingBits(7), false);

        // LEVEL 6 (NEW)
        System.out.println("\n--- [LEVEL 6] THUẬT TOÁN (ALGORITHMS) ---");
        test("15. Is Power of 4 (16)", isPowerOfFour(16), true);
        test("15. Is Power of 4 (8)", isPowerOfFour(8), false);
        testVal("16. Find Complement (5=101 → 010=2)", findComplement(5), 2);
        testVal("17. Binary Gap (22=10110 → 2)", binaryGap(22), 2);
        testVal("18. Reverse Bits (43261596 → 964176192)", reverseBits(43261596), 964176192);

        int[] res645 = findErrorNums(new int[]{1, 2, 2, 4});
        testArray("19. Set Mismatch [1,2,2,4] → [2,3]", res645, new int[]{2, 3});
    }

    private static void testArray(String name, int[] actual, int[] expected) {
        boolean match = java.util.Arrays.equals(actual, expected);
        String result = match ? "✅ PASS" : "❌ FAIL (Got " + java.util.Arrays.toString(actual) + ")";
        System.out.printf("%-50s | %s%n", name, result);
    }

    // ════ LEVEL 1-3 (OLD) ════
    public static boolean isOdd(int n) { return (n & 1) == 1; }
    public static boolean isBitSet(int n, int k) { return (n & (1 << k)) != 0; }
    public static int setBit(int n, int k) { return n | (1 << k); }
    public static int clearBit(int n, int k) { return n & ~(1 << k); }

    /**
     * [BONUS] Tắt tất cả bit từ i về 0 (Clear bits from i to 0).
     * Ví dụ: n = 11111 (31), i = 2.
     * Muốn giữ lại: 11000.
     * Mask: ~((1 << (i + 1)) - 1)  → ~(00111) → 11000
     */
    public static int clearBitsFromItTo0(int n, int i) {
        int mask = ~((1 << (i + 1)) - 1);
        return n & mask;
    }

    /**
     * [BONUS] Tắt tất cả bit từ MSB về i (Clear bits from MSB to i).
     * Ví dụ: n = 11111 (31), i = 2.
     * Muốn giữ lại: 00011.
     * Mask: (1 << i) - 1. → 00011.
     */
    public static int clearBitsFromMSBToIt(int n, int i) {
        int mask = (1 << i) - 1;
        return n & mask;
    }
    public static int toggleBit(int n, int k) { return n ^ (1 << k); }
    public static int removeLastSetBit(int n) { return n & (n - 1); }
    public static int isolateRightmostBit(int n) { return n & (-n); }
    public static boolean isPowerOfTwo(int n) { return n > 0 && (n & (n - 1)) == 0; }

    // ════ LEVEL 4: ARITHMETIC & COUNTING ════

    /**
     * Bài 9: Đếm số lượng bit 1 (Hamming Weight).
     * Cách tối ưu: Brian Kernighan's Algorithm.
     * Mỗi lần n & (n-1) sẽ xóa đi một bit 1. Đếm số lần xóa là xong.
     */
    public static int countSetBits(int n) {
        int count = 0;
        while (n != 0) {
            n = n & (n - 1); // Xóa bit 1 thấp nhất
            count++;
        }
        return count;
    }

    /**
     * Bài 10: Hamming Distance.
     * Khoảng cách giữa 2 số là số lượng bit khác nhau tại cùng vị trí.
     * Gợi ý: XOR 2 số sẽ ra các bit khác nhau (là 1). Sau đó đếm bit 1 của kết quả.
     */
    public static int hammingDistance(int x, int y) {
        return countSetBits(x ^ y);
    }

    /**
     * Bài 11: Hoán đổi 2 số không dùng biến tạm.
     * Nguyên lý: a ^ a = 0.
     */
    public static int[] swap(int a, int b) {
        a = a ^ b;
        b = a ^ b; // b = (a^b)^b = a
        a = a ^ b; // a = (a^b)^a = b (vì b lúc này đã là a cũ)
        return new int[]{a, b};
    }

    // ════ LEVEL 5: XOR PATTERNS ════

    /**
     * Bài 12: Tìm số duy nhất xuất hiện 1 lần, các số khác xuất hiện 2 lần.
     * Gợi ý: XOR tất cả lại. A^A=0. Chỉ còn lại số đơn lẻ.
     */
    public static int findSingleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }

    /**
     * Bài 13: Tìm số còn thiếu trong dãy 0..n.
     * Gợi ý: XOR index với value.
     * Mảng: [3, 0, 1] (thiếu 2)
     * Index: 0, 1, 2
     * XOR tất cả index (và n) và tất cả value lại. Cái nào có cặp sẽ mất.
     */
    public static int findMissingNumber(int[] nums) {
        int result = nums.length; // n
        for (int i = 0; i < nums.length; i++) {
            result ^= i ^ nums[i];
        }
        return result;
    }

    /**
     * Bài 14: Check xem bit có xen kẽ không (vd: 101, 1010).
     * Gợi ý: Nếu n xen kẽ, thì n ^ (n >> 1) sẽ toàn là bit 1.
     * Vd: 101 ^ 010 = 111.
     * Kiểm tra toàn bit 1 bằng cách: (x & (x+1)) == 0.
     */
    public static boolean hasAlternatingBits(int n) {
        int temp = n ^ (n >> 1);
        return (temp & (temp + 1)) == 0;
    }

    // ════ LEVEL 6: ALGORITHMS ════

    /**
     * Bài 15: Power of Four.
     * Là lũy thừa 4 khi:
     * 1. Là lũy thừa 2 (n > 0 và n & n-1 == 0).
     * 2. Bit 1 nằm ở vị trí chẵn (0, 2, 4...).
     * Mask 0x55555555 (0101...) lọc các vị trí chẵn.
     */
    public static boolean isPowerOfFour(int n) {
        return (n > 0) && ((n & (n - 1)) == 0) && ((n & 0x55555555) != 0);
    }

    /**
     * Bài 16: Number Complement (Đảo bit, nhưng không đảo phần leading zeros).
     * Ví dụ: 5 (101) → 2 (010).
     * Gợi ý: Tạo mask toàn 1 với độ dài bằng n, rồi XOR với n.
     */
    public static int findComplement(int n) {
        if (n == 0) return 1;
        long mask = Integer.highestOneBit(n); 
        mask = (mask << 1) - 1; 
        return n ^ (int)mask;
    }

    /**
     * Bài 17: Set Mismatch (LeetCode 645)
     * Tìm số bị lặp (duplicate) và số bị thiếu (missing).
     * 
     * THUẬT TOÁN XOR:
     * 1. XOR tất cả các phần tử trong nums VÀ các số từ 1 → n.
     *    Kết quả: xorAll = duplicate ^ missing.
     * 2. Tìm bit khác biệt thấp nhất: lowbit = xorAll & -xorAll.
     * 3. Chia tất cả các số (trong mảng và từ 1 → n) thành 2 nhóm dựa trên lowbit.
     * 4. XOR mỗi nhóm để tìm ra 2 số tiềm năng (a và b).
     * 5. Duyệt lại mảng để xem a hay b là số xuất hiện trong nums.
     */
    public static int[] findErrorNums(int[] nums) {
        int xorAll = 0;
        int n = nums.length;
        
        // Bước 1: XOR hết để ra (duplicate ^ missing)
        for (int x : nums) xorAll ^= x;
        for (int i = 1; i <= n; i++) xorAll ^= i;
        
        // Bước 2: Tìm bit khác biệt thấp nhất
        int lowbit = xorAll & -xorAll;
        
        // Bước 3: Chia nhóm và XOR
        int a = 0, b = 0;
        for (int x : nums) {
            if ((x & lowbit) == 0) a ^= x;
            else b ^= x;
        }
        for (int i = 1; i <= n; i++) {
            if ((i & lowbit) == 0) a ^= i;
            else b ^= i;
        }
        
        // Bước 4: Xác định số nào là lặp
        for (int x : nums) {
            if (x == a) return new int[]{a, b};
        }
        return new int[]{b, a};
    }

    /**
     * Bài 18: Binary Gap. Khoảng cách lớn nhất giữa hai số 1 bao quanh.
     * Vd: 22 (10110). 1→0→1 (kc 2), 1→1 (kc 1). Max = 2.
     */
    public static int binaryGap(int n) {
        int lastPos = -1;
        int maxDist = 0;
        for (int i = 0; i < 32; i++) {
            if (((n >> i) & 1) == 1) {
                if (lastPos != -1) {
                    maxDist = Math.max(maxDist, i - lastPos);
                }
                lastPos = i;
            }
        }
        return maxDist;
    }

    /**
     * Bài 18: Reverse Bits.
     * Đảo ngược 32 bit của số nguyên.
     */
    public static int reverseBits(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            result <<= 1;      // Đẩy kết quả sang trái
            if ((n & 1) == 1) result++; // Nếu bit cuối của n là 1, cộng vào result
            n >>= 1;           // Đẩy n sang phải
        }
        return result;
    }

    // TEST HELPER
    private static void test(String name, boolean actual, boolean expected) {
        String result = (actual == expected) ? "✅ PASS" : "❌ FAIL";
        System.out.printf("%-50s | %s%n", name, result);
    }
    private static void testVal(String name, int actual, int expected) {
        String result = (actual == expected) ? "✅ PASS" : "❌ FAIL (Got " + actual + ", Want " + expected + ")";
        System.out.printf("%-50s | %s%n", name, result);
    }
}
