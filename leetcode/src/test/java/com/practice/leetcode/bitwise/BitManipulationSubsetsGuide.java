package com.practice.leetcode.bitwise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ HƯỚNG DẪN BIT MANIPULATION VỚI SUBSETS TRONG JAVA                      ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * Tóm tắt về kỹ thuật bit manipulation để xử lý subsets, đặc biệt hữu ích
 * cho các bài toán như tạo tất cả subsets hoặc DP với bitmask trên LeetCode.
 */
public class BitManipulationSubsetsGuide {

    // ═══════════════════════════════════════════════════════════════════════════
    // PHẦN 1: TẠO TẤT CẢ SUBSETS (KHÔNG CÓ PHẦN TỬ TRÙNG)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Với mảng có n phần tử, có 2^n subsets có thể.
     * Sử dụng vòng lặp từ 0 đến (1 << n) - 1.
     * Với mỗi mask i, kiểm tra các bit để xây dựng subset.
     */
    public static List<List<Integer>> generateSubsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;
        for (int i = 0; i < (1 << n); i++) {
            List<Integer> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {  // Kiểm tra bit j có được bật không
                    subset.add(nums[j]);
                }
            }
            result.add(subset);
        }
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PHẦN 2: TẠO SUBSETS VỚI PHẦN TỬ TRÙNG (SUBSETS II)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Sắp xếp mảng trước để nhóm các phần tử trùng.
     * Sử dụng bitmask tương tự, nhưng thêm kiểm tra để bỏ qua các mask không hợp lệ
     * gây ra subset trùng.
     * Luật: Nếu nums[j] == nums[j-1] và phần tử trước không được chọn (bit j-1 = 0), bỏ qua.
     */
    public static List<List<Integer>> generateSubsetsWithDuplicates(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;
        for (int i = 0; i < (1 << n); i++) {
            List<Integer> subset = new ArrayList<>();
            boolean valid = true;
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    if (j > 0 && nums[j] == nums[j - 1] && (i & (1 << (j - 1))) == 0) {
                        valid = false;
                        break;
                    }
                    subset.add(nums[j]);
                }
            }
            if (valid) result.add(subset);
        }
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PHẦN 3: DUYỆT QUA TẤT CẢ SUBMASK CỦA MỘT MASK CHO TRƯỚC
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Hữu ích cho bitmask DP hoặc khi bạn có một tập hợp cố định và muốn tất cả subsets của nó.
     * Bắt đầu với sub = mask, lặp lại sub = (sub - 1) & mask cho đến khi sub == 0.
     */
    public static void iterateSubmasks(int mask) {
        System.out.println("Submasks của " + mask + ":");
        for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {
            System.out.println(sub);
        }
        System.out.println(0);  // Đừng quên subset rỗng nếu cần
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PHẦN 4: CÁC THAO TÁC BIT CƠ BẢN SỬ DỤNG
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * - Kiểm tra bit: (mask & (1 << j)) != 0
     * - Bật bit: mask | (1 << j)
     * - Tắt bit: mask & ~(1 << j)
     * - Đảo bit: mask ^ (1 << j)
     * - Đếm bit 1: Integer.bitCount(mask)
     */

    // Ví dụ sử dụng
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        System.out.println("Subsets: " + generateSubsets(nums));

        int[] numsWithDup = {1, 2, 2};
        System.out.println("Subsets với trùng: " + generateSubsetsWithDuplicates(numsWithDup));

        iterateSubmasks(5);  // 5 = 101 binary
    }
}