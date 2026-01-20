package com.practice.leetcode.codility;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                         LESSON 8: LEADER                                    ║
 * ║                       Phần tử chủ đạo (Dominator)                           ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Leader/Dominator là phần tử xuất hiện > N/2 lần trong mảng.
 * Thuật toán Boyer-Moore Voting là kỹ thuật tìm leader trong O(N) time, O(1) space.
 */
public class L8_Leader {

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                          DOMINATOR
     *              Tìm phần tử xuất hiện hơn nửa mảng
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Tìm index của phần tử xuất hiện > N/2 lần. Return -1 nếu không có.
     * 
     * VÍ DỤ: A = [3, 4, 3, 2, 3, -1, 3, 3]
     * 3 xuất hiện 5 lần > 8/2 = 4 → return bất kỳ index nào của 3
     * 
     * THUẬT TOÁN BOYER-MOORE VOTING:
     * 
     * Ý tưởng: "Bầu cử" - loại bỏ cặp phần tử khác nhau
     * 
     * Phase 1: Tìm ứng viên
     * - Duy trì candidate và count
     * - Gặp phần tử = candidate → count++
     * - Gặp phần tử ≠ candidate → count--
     * - Nếu count = 0 → chọn candidate mới
     * 
     * Phase 2: Xác nhận ứng viên
     * - Đếm xem candidate có thực sự > N/2 không
     * 
     * TẠI SAO THUẬT TOÁN ĐÚNG?
     * Nếu tồn tại dominator (> N/2), khi "loại bỏ cặp khác nhau",
     * dominator vẫn còn lại vì số lượng > tất cả phần tử khác cộng lại.
     * 
     * ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian
     */
    public int dominator(int[] A) {
        int N = A.length;
        if (N == 0) return -1;
        
        // Phase 1: Tìm ứng viên (Boyer-Moore)
        int candidate = 0;
        int count = 0;
        int candidateIndex = -1;
        
        for (int i = 0; i < N; i++) {
            if (count == 0) {
                candidate = A[i];
                candidateIndex = i;
                count = 1;
            } else if (A[i] == candidate) {
                count++;
            } else {
                count--;
            }
        }
        
        // Phase 2: Xác nhận ứng viên
        int actualCount = 0;
        int resultIndex = -1;
        for (int i = 0; i < N; i++) {
            if (A[i] == candidate) {
                actualCount++;
                resultIndex = i;
            }
        }
        
        return actualCount > N / 2 ? resultIndex : -1;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                         EQUI LEADER
     *              Đếm số vị trí chia mảng có cùng leader
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI:
     * Tìm số vị trí S (0 ≤ S < N-1) sao cho cả hai phần A[0..S] và A[S+1..N-1]
     * đều có cùng leader (phần tử xuất hiện > half).
     * 
     * VÍ DỤ: A = [4, 3, 4, 4, 4, 2]
     * Leader toàn mảng = 4
     * S=0: [4] và [3,4,4,4,2] → leader = 4 và 4 ✓
     * S=2: [4,3,4] và [4,4,2] → leader = 4 và 4 ✓
     * ...
     * Đáp án: 2
     * 
     * TƯ DUY:
     * 1. Tìm leader toàn mảng (nếu không có → return 0)
     * 2. Duyệt từng vị trí S, đếm leader bên trái
     * 3. Kiểm tra xem leader có phải là leader của cả 2 phần không
     * 
     * ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian
     */
    public int equiLeader(int[] A) {
        int N = A.length;
        
        // Bước 1: Tìm leader của toàn mảng
        int candidate = 0;
        int count = 0;
        
        for (int num : A) {
            if (count == 0) {
                candidate = num;
                count = 1;
            } else if (num == candidate) {
                count++;
            } else {
                count--;
            }
        }
        
        // Đếm số lần xuất hiện của candidate
        int totalCount = 0;
        for (int num : A) {
            if (num == candidate) totalCount++;
        }
        
        // Kiểm tra có phải leader thực sự không
        if (totalCount <= N / 2) return 0;
        
        // Bước 2: Đếm equi leaders
        int equiLeaders = 0;
        int leftCount = 0;
        
        for (int S = 0; S < N - 1; S++) {
            if (A[S] == candidate) leftCount++;
            
            int leftSize = S + 1;
            int rightSize = N - leftSize;
            int rightCount = totalCount - leftCount;
            
            // Kiểm tra leader ở cả 2 phần
            if (leftCount > leftSize / 2 && rightCount > rightSize / 2) {
                equiLeaders++;
            }
        }
        
        return equiLeaders;
    }
    
    // ================================ TESTS ================================
    
    @Test
    void testDominator() {
        int[] A = {3, 4, 3, 2, 3, -1, 3, 3};
        int idx = dominator(A);
        assertTrue(idx >= 0 && A[idx] == 3);
    }
    
    @Test
    void testDominator_NoDominator() {
        assertEquals(-1, dominator(new int[]{1, 2, 3, 4}));
    }
    
    @Test
    void testEquiLeader() {
        assertEquals(2, equiLeader(new int[]{4, 3, 4, 4, 4, 2}));
    }
    
    @Test
    void testEquiLeader_NoLeader() {
        assertEquals(0, equiLeader(new int[]{1, 2, 3, 4}));
    }
}
