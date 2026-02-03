package com.practice.leetcode.bitwise;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CODILITY - BULBS (Đề bài gốc khó hiểu, đọc phiên bản đơn giản bên dưới)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Có N bóng đèn xếp thành hàng: [1, 2, 3, 4, 5, ...]
 * 
 * Quy tắc:
 * - Ban đầu: TẤT CẢ bóng TẮT
 * - Cho array A[] = thứ tự bật bóng
 * - Moment i: Bật bóng thứ A[i]
 * - Bóng k SÁNG ⇔ TẤT CẢ bóng từ 1→k đều đã BẬT
 * 
 * Hỏi: Có bao nhiêu MOMENT mà bóng vừa bật sẽ SÁNG?
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Input: A = [2, 1, 3, 5, 4]
 * 
 * Ban đầu: [💡, 💡, 💡, 💡, 💡] (Tất cả TẮT)
 *            1   2   3   4   5
 * 
 * Moment 0: Bật bóng 2
 * [💡, ✅, 💡, 💡, 💡]
 *  ↑ Bóng 1 chưa bật → Bóng 2 KHÔNG SÁNG ❌
 * 
 * Moment 1: Bật bóng 1
 * [✅, ✅, 💡, 💡, 💡]
 *  ↑ Bóng 1→2 đều bật → CẢ 2 bóng SÁNG 💡 Count = 1
 * 
 * Moment 2: Bật bóng 3
 * [✅, ✅, ✅, 💡, 💡]
 *  ↑ Bóng 1→3 đều bật → CẢ 3 bóng SÁNG 💡 Count = 2
 * 
 * Moment 3: Bật bóng 5
 * [✅, ✅, ✅, 💡, ✅]
 *              ↑ Bóng 4 chưa bật → Bóng 5 KHÔNG SÁNG ❌
 * 
 * Moment 4: Bật bóng 4
 * [✅, ✅, ✅, ✅, ✅]
 *  ↑ Bóng 1→5 đều bật → TẤT CẢ SÁNG 💡 Count = 3
 * 
 * Kết quả: 3 moments
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CỦA CODILITY (Khó hiểu, giữ lại để tham khảo)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * There are N bulbs, numbered from 1 to N, arranged in a row. The first bulb is
 * plugged into the power socket and each successive bulb is connected to the
 * previous one (the second bulb to the first, the third bulb to the second, etc.).
 * ⬆️ Phần này chỉ là "background story", không quan trọng!
 * 
 * Initially, all the bulbs are turned off. At moment K (for K from 0 to N−1), we turn
 * on the A[K]-th bulb. A bulb shines if it is on and all the previous bulbs are turned
 * on too.
 * ⬆️ ĐÂY LÀ PHẦN QUAN TRỌNG!
 * 
 * Write a function that, given an array A of N different integers from 1 to N,
 * returns the number of moments for which every turned on bulb shines.
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 1: BRUTE FORCE - SIMULATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Ý tưởng:
 * 1. Mô phỏng từng moment
 * 2. Mỗi moment: bật bóng A[i], kiểm tra tất cả bóng từ 1→N xem có sáng không
 * 
 * Time: O(n²)
 * Space: O(n)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 2: MAX TRACKING (OPTIMAL)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Key Insight:
 * Tại moment i, TẤT CẢ bóng từ 1 đến (i+1) sáng ⇔ max(A[0]...A[i]) == i+1
 * 
 * Giải thích:
 * - Nếu đã bật i+1 bóng
 * - Và bóng lớn nhất được bật là (i+1)
 * - → Tất cả bóng từ 1→(i+1) đều đã được bật!
 * 
 * Ví dụ: A = [2, 1, 3, 5, 4]
 * i=0: Bật 1 bóng, max=2 ≠ 1 → Không sáng
 * i=1: Bật 2 bóng, max=2 = 2 → Sáng ✅
 * i=2: Bật 3 bóng, max=3 = 3 → Sáng ✅
 * i=3: Bật 4 bóng, max=5 ≠ 4 → Không sáng
 * i=4: Bật 5 bóng, max=5 = 5 → Sáng ✅
 * 
 * Time: O(n)
 * Space: O(1)
 * ✅ OPTIMAL SOLUTION!
 */
class CodilityBulbsSolutionTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: BRUTE FORCE
    // ═══════════════════════════════════════════════════════════════════════════
    public int solutionBruteForce(int[] A) {
        int n = A.length;
        boolean[] bulbs = new boolean[n + 1]; // index 0 không dùng
        int count = 0;
        
        for (int moment = 0; moment < n; moment++) {
            // Bật bóng A[moment]
            bulbs[A[moment]] = true;
            
            // Kiểm tra tất cả bóng đã bật có sáng không
            boolean allShine = true;
            for (int i = 1; i <= moment + 1; i++) {
                if (!bulbs[i]) {
                    allShine = false;
                    break;
                }
            }
            
            if (allShine) {
                count++;
            }
        }
        
        return count;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: MAX TRACKING (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    /**
     * Key insight: Tất cả bóng từ 1→k sáng ⇔ max(A[0]...A[i]) = k
     * 
     * Time: O(n)
     * Space: O(1)
     */
    public int solution(int[] A) {
        int count = 0;
        int maxSoFar = 0;
        
        for (int i = 0; i < A.length; i++) {
            maxSoFar = Math.max(maxSoFar, A[i]);
            
            // Nếu đã bật (i+1) bóng và bóng lớn nhất = (i+1)
            // → Tất cả bóng từ 1 đến (i+1) đều đã bật
            if (maxSoFar == i + 1) {
                count++;
            }
        }
        
        return count;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Codility Bulbs - Example case")
    void testExample() {
        int[] A = {2, 1, 3, 5, 4};
        assertThat(solution(A)).isEqualTo(3);
        assertThat(solutionBruteForce(A)).isEqualTo(3);
    }
    
    @Test
    @DisplayName("Codility Bulbs - All in order")
    void testInOrder() {
        // Nếu bật theo thứ tự 1,2,3,4,5 → mỗi moment đều sáng
        int[] A = {1, 2, 3, 4, 5};
        assertThat(solution(A)).isEqualTo(5);
        assertThat(solutionBruteForce(A)).isEqualTo(5);
    }
    
    @Test
    @DisplayName("Codility Bulbs - Reverse order")
    void testReverseOrder() {
        // Nếu bật theo thứ tự 5,4,3,2,1 → chỉ moment cuối sáng
        int[] A = {5, 4, 3, 2, 1};
        assertThat(solution(A)).isEqualTo(1);
        assertThat(solutionBruteForce(A)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Codility Bulbs - Single bulb")
    void testSingleBulb() {
        int[] A = {1};
        assertThat(solution(A)).isEqualTo(1);
        assertThat(solutionBruteForce(A)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Codility Bulbs - Two bulbs")
    void testTwoBulbs() {
        // [2, 1] → Chỉ moment cuối sáng
        int[] A = {2, 1};
        assertThat(solution(A)).isEqualTo(1);
        
        // [1, 2] → Cả 2 moment đều sáng
        int[] B = {1, 2};
        assertThat(solution(B)).isEqualTo(2);
    }
}
