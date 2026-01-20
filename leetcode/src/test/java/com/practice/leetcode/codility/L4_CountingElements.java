package com.practice.leetcode.codility;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                    LESSON 4: COUNTING ELEMENTS                              ║
 * ║                       Bài toán về đếm phần tử                               ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class L4_CountingElements {

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                         FROG RIVER ONE
     *                    Ếch qua sông khi có đủ lá
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI:
     * Con ếch muốn qua sông rộng X đơn vị. Lá rơi xuống theo thời gian.
     * A[K] = vị trí lá rơi tại giây K. Tìm giây sớm nhất để ếch qua sông
     * (cần lá ở TẤT CẢ vị trí 1 đến X).
     * 
     * VÍ DỤ: X = 5, A = [1, 3, 1, 4, 2, 3, 5, 4]
     * Giây 6: lá ở {1, 2, 3, 4, 5} → Đáp án: 6
     * 
     * TƯ DUY: Dùng boolean[] track vị trí đã có lá, đếm số vị trí unique.
     * 
     * ĐỘ PHỨC TẠP: O(N) thời gian, O(X) không gian
     */
    public int frogRiverOne(int X, int[] A) {
        boolean[] covered = new boolean[X + 1];
        int count = 0;
        
        for (int i = 0; i < A.length; i++) {
            int position = A[i];
            if (position <= X && !covered[position]) {
                covered[position] = true;
                count++;
                if (count == X) return i;
            }
        }
        return -1;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                           PERM CHECK
     *                   Kiểm tra mảng có phải hoán vị không
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Kiểm tra mảng A có phải hoán vị của [1..N] không.
     * 
     * VÍ DỤ:
     * [4, 1, 3, 2] → là hoán vị → return 1
     * [4, 1, 3] → không phải → return 0
     * 
     * TƯ DUY: Đánh dấu số đã xuất hiện, kiểm tra phạm vi và trùng lặp.
     * 
     * ĐỘ PHỨC TẠP: O(N) thời gian, O(N) không gian
     */
    public int permCheck(int[] A) {
        int N = A.length;
        boolean[] seen = new boolean[N + 1];
        
        for (int num : A) {
            if (num < 1 || num > N || seen[num]) return 0;
            seen[num] = true;
        }
        return 1;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                        MISSING INTEGER
     *              Tìm số nguyên dương nhỏ nhất không có trong mảng
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Tìm số nguyên DƯƠNG nhỏ nhất không xuất hiện trong mảng.
     * 
     * VÍ DỤ:
     * [1, 3, 6, 4, 1, 2] → return 5
     * [-1, -3] → return 1
     * 
     * NHẬN XÉT QUAN TRỌNG: Đáp án nằm trong [1, N+1]
     * Vì mảng N phần tử tốt nhất chứa 1..N → thiếu N+1.
     * 
     * TƯ DUY: Đánh dấu số dương trong [1,N], tìm số nhỏ nhất chưa đánh dấu.
     * 
     * ĐỘ PHỨC TẠP: O(N) thời gian, O(N) không gian
     */
    public int missingInteger(int[] A) {
        int N = A.length;
        boolean[] present = new boolean[N + 2];
        
        for (int num : A) {
            if (num > 0 && num <= N) {
                present[num] = true;
            }
        }
        
        for (int i = 1; i <= N + 1; i++) {
            if (!present[i]) return i;
        }
        return N + 1;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                         MAX COUNTERS
     *                    Mảng bộ đếm với lazy propagation
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: N bộ đếm, ban đầu = 0. M thao tác:
     * • A[K] ∈ [1, N]: tăng counter[A[K]] lên 1
     * • A[K] = N+1: set tất cả counters = max counter hiện tại
     * 
     * VÍ DỤ: N=5, A=[3,4,4,6,1,4,4] → [3, 2, 2, 4, 2]
     * 
     * LAZY PROPAGATION: Thay vì set tất cả counters (O(N) mỗi lần),
     * lưu "baseline" = giá trị tối thiểu. Apply baseline khi cần.
     * 
     * ĐỘ PHỨC TẠP: O(N+M) thay vì O(N*M)
     */
    public int[] maxCounters(int N, int[] A) {
        int[] counters = new int[N];
        int baseline = 0;    // Giá trị tối thiểu (lazy)
        int maxValue = 0;    // Giá trị lớn nhất hiện tại
        
        for (int op : A) {
            if (op >= 1 && op <= N) {
                int idx = op - 1;
                if (counters[idx] < baseline) {
                    counters[idx] = baseline;
                }
                counters[idx]++;
                maxValue = Math.max(maxValue, counters[idx]);
            } else {
                baseline = maxValue;
            }
        }
        
        // Apply baseline cho các counters chưa được cập nhật
        for (int i = 0; i < N; i++) {
            if (counters[i] < baseline) {
                counters[i] = baseline;
            }
        }
        return counters;
    }
    
    // ================================ TESTS ================================
    
    @Test
    void testFrogRiverOne() {
        assertEquals(6, frogRiverOne(5, new int[]{1, 3, 1, 4, 2, 3, 5, 4}));
        assertEquals(-1, frogRiverOne(5, new int[]{1, 3, 1, 4, 2, 3, 4}));
    }
    
    @Test
    void testPermCheck() {
        assertEquals(1, permCheck(new int[]{4, 1, 3, 2}));
        assertEquals(0, permCheck(new int[]{4, 1, 3}));
    }
    
    @Test
    void testMissingInteger() {
        assertEquals(5, missingInteger(new int[]{1, 3, 6, 4, 1, 2}));
        assertEquals(1, missingInteger(new int[]{-1, -3}));
    }
    
    @Test
    void testMaxCounters() {
        assertArrayEquals(new int[]{3, 2, 2, 4, 2}, maxCounters(5, new int[]{3, 4, 4, 6, 1, 4, 4}));
    }
}
