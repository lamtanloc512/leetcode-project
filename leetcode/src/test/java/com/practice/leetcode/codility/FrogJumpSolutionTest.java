package com.practice.leetcode.codility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CODILITY - FROG JUMP (Đề gốc khó hiểu, đọc phiên bản đơn giản)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Con ếch đang ở vị trí X, muốn nhảy tới vị trí Y.
 * Mỗi lần nhảy được D đơn vị.
 * 
 * Hỏi: Cần ÍT NHẤT bao nhiêu cú nhảy?
 * 
 * Quy tắc:
 * - Vị trí hiện tại: X
 * - Vị trí đích: Y
 * - Mỗi bước nhảy: +D
 * - Tìm số bước nhảy tối thiểu
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Input: X = 10, Y = 85, D = 30
 * 
 * Bước 0: Vị trí = 10 (chưa tới 85)
 * Bước 1: Vị trí = 10 + 30 = 40 (chưa tới 85)
 * Bước 2: Vị trí = 40 + 30 = 70 (chưa tới 85)
 * Bước 3: Vị trí = 70 + 30 = 100 (đã tới 85) ✅
 * 
 * Kết quả: 3 bước nhảy
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CODILITY (Khó hiểu, giữ lại để tham khảo)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * A small frog wants to get to the other side of the road. The frog is currently
 * located at position X and wants to get to a position greater than or equal to Y.
 * The small frog always jumps a fixed distance, D.
 * 
 * Count the minimal number of jumps that the small frog must perform to reach
 * its target.
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 1: SIMULATION (TLE for large values)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Mô phỏng từng bước nhảy cho đến khi tới đích.
 * 
 * Time: O((Y-X)/D)
 * Space: O(1)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 2: MATH FORMULA (OPTIMAL)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Key Insight:
 * Số bước nhảy = ceil((Y - X) / D)
 * 
 * Công thức: (Y - X + D - 1) / D  (integer division làm tròn lên)
 * 
 * Time: O(1)
 * Space: O(1)
 * ✅ OPTIMAL!
 */
class FrogJumpSolutionTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: SIMULATION
    // ═══════════════════════════════════════════════════════════════════════════
    public int solutionSimulation(int X, int Y, int D) {
        int jumps = 0;
        int position = X;
        
        while (position < Y) {
            position += D;
            jumps++;
        }
        
        return jumps;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: MATH FORMULA (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    public int solution(int X, int Y, int D) {
        int distance = Y - X;
        
        // Ceiling division: (a + b - 1) / b
        return (distance + D - 1) / D;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Frog Jump - Example case")
    void testExample() {
        assertThat(solution(10, 85, 30)).isEqualTo(3);
        assertThat(solutionSimulation(10, 85, 30)).isEqualTo(3);
    }
    
    @Test
    @DisplayName("Frog Jump - Already at target")
    void testAlreadyAtTarget() {
        assertThat(solution(10, 10, 5)).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Frog Jump - One jump enough")
    void testOneJump() {
        assertThat(solution(10, 15, 10)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Frog Jump - Large distance")
    void testLargeDistance() {
        assertThat(solution(1, 1000000000, 1)).isEqualTo(999999999);
    }
}
