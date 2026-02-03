package com.practice.leetcode.codility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CODILITY - PERM MISSING ELEM (Đề gốc khó hiểu, đọc phiên bản đơn giản)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cho array A[] chứa N số từ 1 đến (N+1), nhưng thiếu 1 số.
 * Tìm số bị thiếu.
 * 
 * Ví dụ:
 * - A = [2, 3, 1, 5] → Thiếu số 4
 * - A = [1, 2, 3] → Thiếu số 4
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Input: A = [2, 3, 1, 5]
 * 
 * Dãy đầy đủ phải là: [1, 2, 3, 4, 5]
 * Dãy hiện tại:       [1, 2, 3,    5]
 *                              ↑
 *                         Thiếu 4
 * 
 * Kết quả: 4
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CODILITY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * An array A consisting of N different integers is given. The array contains
 * integers in the range [1..(N + 1)], which means that exactly one element is missing.
 * 
 * Your goal is to find that missing element.
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 1: SORTING
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * 1. Sort array
 * 2. Duyệt qua, tìm vị trí i mà A[i] != i+1
 * 
 * Time: O(n log n)
 * Space: O(1)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 2: MATH FORMULA (OPTIMAL)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Key Insight:
 * Tổng từ 1 đến (N+1) = (N+1) * (N+2) / 2
 * Số thiếu = Tổng lý thuyết - Tổng thực tế
 * 
 * Time: O(n)
 * Space: O(1)
 * ✅ OPTIMAL!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 3: XOR BIT MANIPULATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Key Insight:
 * a ⊕ a = 0
 * a ⊕ 0 = a
 * 
 * XOR tất cả số từ 1→(N+1) và XOR với array
 * → Số xuất hiện 2 lần bị triệt tiêu
 * → Còn lại số thiếu
 * 
 * Time: O(n)
 * Space: O(1)
 */
class PermMissingElemSolutionTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: SORTING
    // ═══════════════════════════════════════════════════════════════════════════
    public int solutionSorting(int[] A) {
        if (A.length == 0) return 1;
        
        java.util.Arrays.sort(A);
        
        for (int i = 0; i < A.length; i++) {
            if (A[i] != i + 1) {
                return i + 1;
            }
        }
        
        return A.length + 1;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: MATH FORMULA (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    public int solution(int[] A) {
        int n = A.length;
        
        // Tổng lý thuyết từ 1 đến (N+1)
        long expectedSum = (long)(n + 1) * (n + 2) / 2;
        
        // Tổng thực tế
        long actualSum = 0;
        for (int num : A) {
            actualSum += num;
        }
        
        return (int)(expectedSum - actualSum);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 3: XOR BIT MANIPULATION
    // ═══════════════════════════════════════════════════════════════════════════
    public int solutionXOR(int[] A) {
        int xor = 0;
        int n = A.length;
        
        // XOR với tất cả số từ 1 đến (N+1)
        for (int i = 1; i <= n + 1; i++) {
            xor ^= i;
        }
        
        // XOR với tất cả số trong array
        for (int num : A) {
            xor ^= num;
        }
        
        return xor;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("PermMissingElem - Example case")
    void testExample() {
        int[] A = {2, 3, 1, 5};
        assertThat(solution(A)).isEqualTo(4);
        assertThat(solutionXOR(A)).isEqualTo(4);
        assertThat(solutionSorting(A)).isEqualTo(4);
    }
    
    @Test
    @DisplayName("PermMissingElem - Missing first")
    void testMissingFirst() {
        int[] A = {2, 3, 4};
        assertThat(solution(A)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("PermMissingElem - Missing last")
    void testMissingLast() {
        int[] A = {1, 2, 3};
        assertThat(solution(A)).isEqualTo(4);
    }
    
    @Test
    @DisplayName("PermMissingElem - Empty array")
    void testEmpty() {
        int[] A = {};
        assertThat(solution(A)).isEqualTo(1);
    }
}
