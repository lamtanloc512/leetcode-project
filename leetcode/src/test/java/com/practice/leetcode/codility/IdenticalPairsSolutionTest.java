package com.practice.leetcode.codility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Đếm số cặp (P, Q) sao cho A[P] = A[Q] và P < Q.
 * 
 * Ví dụ: [3, 5, 6, 3, 3, 5]
 * 
 * Các cặp giống nhau:
 * - (0, 3): A[0]=3, A[3]=3 ✓
 * - (0, 4): A[0]=3, A[4]=3 ✓
 * - (1, 5): A[1]=5, A[5]=5 ✓
 * - (3, 4): A[3]=3, A[4]=3 ✓
 * 
 * Tổng = 4 cặp
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Input:  [3, 5, 6, 3, 3, 5]
 * Output: 4
 * 
 * Input:  [1, 1, 1, 1]
 * Output: 6
 * Giải thích: C(4,2) = 4*3/2 = 6 cặp
 * - (0,1), (0,2), (0,3), (1,2), (1,3), (2,3)
 * 
 * Input:  [1, 2, 3, 4]
 * Output: 0
 * Giải thích: Không có cặp nào giống nhau
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CODILITY (Khó hiểu, giữ lại để tham khảo)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * An array A consisting of N integers is given. A pair of integers (P, Q), such 
 * that 0 ≤ P < Q < N, is called identical if A[P] = A[Q].
 * 
 * For example, consider array A such that:
 *   A[0] = 3
 *   A[1] = 5
 *   A[2] = 6
 *   A[3] = 3
 *   A[4] = 3
 *   A[5] = 5
 * 
 * The following pairs of indices are identical:
 *   (0, 3), because A[0] = A[3] = 3,
 *   (0, 4), because A[0] = A[4] = 3,
 *   (1, 5), because A[1] = A[5] = 5,
 *   (3, 4), because A[3] = A[4] = 3.
 * 
 * There are 4 identical pairs in total.
 * 
 * Write a function:
 * class Solution { public int solution(int[] A); }
 * 
 * that, given an array A consisting of N integers, returns the number of 
 * identical pairs in A.
 * 
 * The function should return −1 if the number of identical pairs exceeds 1,000,000,000.
 * 
 * Write an efficient algorithm for the following assumptions:
 * - N is an integer within the range [0..100,000];
 * - each element of array A is an integer within the range [−1,000,000..1,000,000].
 */
class IdenticalPairsSolutionTest {

    private static final int MAX_PAIRS = 1_000_000_000;
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: BRUTE FORCE
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Check tất cả các cặp
     * 
     * Time: O(n²)
     * Space: O(1)
     */
    public int solutionBruteForce(int[] A) {
        int count = 0;
        
        for (int P = 0; P < A.length; P++) {
            for (int Q = P + 1; Q < A.length; Q++) {
                if (A[P] == A[Q]) {
                    count++;
                    if (count > MAX_PAIRS) return -1;
                }
            }
        }
        
        return count;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: HASH MAP + COMBINATION FORMULA (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * Ý TƯỞNG
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Nếu số X xuất hiện K lần → số cặp = C(K, 2) = K * (K-1) / 2
     * 
     * Ví dụ: [3, 5, 6, 3, 3, 5]
     * 
     * Frequency:
     * - 3 xuất hiện 3 lần → C(3,2) = 3*2/2 = 3 cặp
     * - 5 xuất hiện 2 lần → C(2,2) = 2*1/2 = 1 cặp
     * - 6 xuất hiện 1 lần → C(1,2) = 0 cặp
     * 
     * Tổng = 3 + 1 + 0 = 4 cặp ✓
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Time: O(n)
     * Space: O(n)
     */
    public int solution(int[] A) {
        if (A.length == 0) return 0;
        
        // Count frequency
        java.util.Map<Integer, Long> freq = new java.util.HashMap<>();
        
        for (int num : A) {
            freq.put(num, freq.getOrDefault(num, 0L) + 1);
        }
        
        // Calculate pairs using combination formula
        long totalPairs = 0;
        
        for (long count : freq.values()) {
            if (count >= 2) {
                // C(count, 2) = count * (count - 1) / 2
                long pairs = count * (count - 1) / 2;
                totalPairs += pairs;
                
                if (totalPairs > MAX_PAIRS) {
                    return -1;
                }
            }
        }
        
        return (int) totalPairs;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Example: A = [3, 5, 6, 3, 3, 5]
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * STEP 1: Count Frequency
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * freq = {
     *   3 → 3,  // xuất hiện ở index 0, 3, 4
     *   5 → 2,  // xuất hiện ở index 1, 5
     *   6 → 1   // xuất hiện ở index 2
     * }
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * STEP 2: Calculate Pairs
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * For 3 (count=3):
     *   C(3, 2) = 3 * 2 / 2 = 3 cặp
     *   Cụ thể: (0,3), (0,4), (3,4)
     * 
     * For 5 (count=2):
     *   C(2, 2) = 2 * 1 / 2 = 1 cặp
     *   Cụ thể: (1,5)
     * 
     * For 6 (count=1):
     *   C(1, 2) = 0 (cần ít nhất 2 phần tử)
     * 
     * Total = 3 + 1 + 0 = 4 ✓
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Example 2: A = [1, 1, 1, 1]
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * freq = { 1 → 4 }
     * 
     * C(4, 2) = 4 * 3 / 2 = 6 cặp
     * Cụ thể: (0,1), (0,2), (0,3), (1,2), (1,3), (2,3)
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example 1: [3,5,6,3,3,5] → 4")
    void testExample1() {
        int[] A = {3, 5, 6, 3, 3, 5};
        assertThat(solution(A)).isEqualTo(4);
    }
    
    @Test
    @DisplayName("Example 2: [1,1,1,1] → 6")
    void testAllSame() {
        int[] A = {1, 1, 1, 1};
        assertThat(solution(A)).isEqualTo(6);
    }
    
    @Test
    @DisplayName("Example 3: [1,2,3,4] → 0")
    void testAllDifferent() {
        int[] A = {1, 2, 3, 4};
        assertThat(solution(A)).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Edge case: Empty array → 0")
    void testEmpty() {
        int[] A = {};
        assertThat(solution(A)).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Edge case: Single element → 0")
    void testSingleElement() {
        int[] A = {5};
        assertThat(solution(A)).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Edge case: Two same → 1")
    void testTwoSame() {
        int[] A = {7, 7};
        assertThat(solution(A)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Edge case: Exceed limit → -1")
    void testExceedLimit() {
        // Create array with 50000 same elements
        // C(50000, 2) = 50000 * 49999 / 2 = 1,249,975,000 > 1B
        int[] A = new int[50000];
        java.util.Arrays.fill(A, 1);
        assertThat(solution(A)).isEqualTo(-1);
    }
    
    @Test
    @DisplayName("Edge case: Negative numbers")
    void testNegative() {
        int[] A = {-5, -5, -5, 3, 3};
        // C(3,2) + C(2,2) = 3 + 1 = 4
        assertThat(solution(A)).isEqualTo(4);
    }
    
    @Test
    @DisplayName("Multiple groups")
    void testMultipleGroups() {
        int[] A = {1, 1, 2, 2, 2, 3, 3, 3, 3};
        // C(2,2) + C(3,2) + C(4,2) = 1 + 3 + 6 = 10
        assertThat(solution(A)).isEqualTo(10);
    }
}
