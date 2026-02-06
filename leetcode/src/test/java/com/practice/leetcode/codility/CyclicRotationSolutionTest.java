package com.practice.leetcode.codility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Xoay mảng sang PHẢI K lần.
 * 
 * Ví dụ: [3, 8, 9, 7, 6] xoay phải 3 lần
 * - Xoay 1: [6, 3, 8, 9, 7]
 * - Xoay 2: [7, 6, 3, 8, 9]
 * - Xoay 3: [9, 7, 6, 3, 8]
 * 
 * Hoặc công thức nhanh:
 * - Lấy 3 phần tử cuối: [9, 7, 6]
 * - Đem ra đầu: [9, 7, 6, 3, 8]
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Input:  A = [3, 8, 9, 7, 6], K = 3
 * Output: [9, 7, 6, 3, 8]
 * 
 * Input:  A = [0, 0, 0], K = 1
 * Output: [0, 0, 0]
 * 
 * Input:  A = [1, 2, 3, 4], K = 4
 * Output: [1, 2, 3, 4]  (xoay đủ 1 vòng = giữ nguyên)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CODILITY (Khó hiểu, giữ lại để tham khảo)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * An array A consisting of N integers is given. Rotation of the array means that 
 * each element is shifted right by one index, and the last element of the array 
 * is moved to the first place. For example, the rotation of array 
 * A = [3, 8, 9, 7, 6] is [6, 3, 8, 9, 7] (elements are shifted right by one index 
 * and 6 is moved to the first place).
 * 
 * The goal is to rotate array A K times; that is, each element of A will be 
 * shifted to the right K times.
 * 
 * Write a function:
 * class Solution { public int[] solution(int[] A, int K); }
 * 
 * that, given an array A consisting of N integers and an integer K, returns the 
 * array A rotated K times.
 * 
 * For example, given
 *     A = [3, 8, 9, 7, 6]
 *     K = 3
 * the function should return [9, 7, 6, 3, 8]. Three rotations were made:
 *     [3, 8, 9, 7, 6] -> [6, 3, 8, 9, 7]
 *     [6, 3, 8, 9, 7] -> [7, 6, 3, 8, 9]
 *     [7, 6, 3, 8, 9] -> [9, 7, 6, 3, 8]
 * 
 * Assume that:
 * - N and K are integers within the range [0..100];
 * - each element of array A is an integer within the range [−1,000..1,000].
 */
class CyclicRotationSolutionTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: BRUTE FORCE - Xoay từng bước
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Time: O(n * k) - Với mỗi lần xoay, duyệt hết mảng
     * Space: O(1)
     */
    public int[] solutionBruteForce(int[] A, int K) {
        if (A.length == 0) return A;
        
        for (int rotation = 0; rotation < K; rotation++) {
            // Xoay 1 lần: move phần tử cuối ra đầu
            int last = A[A.length - 1];
            for (int i = A.length - 1; i > 0; i--) {
                A[i] = A[i - 1];
            }
            A[0] = last;
        }
        
        return A;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: REVERSE 3 TIMES (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Sử dụng reverse 3 lần để xoay mảng
     * 
     * RIGHT ROTATE by K:
     * 1. Reverse toàn bộ mảng
     * 2. Reverse K phần tử đầu
     * 3. Reverse phần còn lại
     * 
     * Ví dụ: [3, 8, 9, 7, 6], K=3
     * 
     * Step 1: Reverse all → [6, 7, 9, 8, 3]
     * Step 2: Reverse [0, K) → [9, 7, 6, 8, 3]
     * Step 3: Reverse [K, n) → [9, 7, 6, 3, 8] ✓
     * 
     * Time: O(n)
     * Space: O(1)
     */
    public int[] solution(int[] A, int K) {
        int n = A.length;
        if (n == 0) return A;
        
        K = K % n;  // Handle K > n
        if (K == 0) return A;
        
        // Step 1: Reverse entire array
        reverse(A, 0, n - 1);
        
        // Step 2: Reverse first K elements
        reverse(A, 0, K - 1);
        
        // Step 3: Reverse remaining elements
        reverse(A, K, n - 1);
        
        return A;
    }
    
    private void reverse(int[] arr, int start, int end) {
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 3: EXTRA ARRAY
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Sử dụng mảng phụ để lưu kết quả
     * 
     * Time: O(n)
     * Space: O(n)
     */
    public int[] solutionExtraArray(int[] A, int K) {
        int n = A.length;
        if (n == 0) return A;
        
        K = K % n;
        int[] result = new int[n];
        
        for (int i = 0; i < n; i++) {
            result[(i + K) % n] = A[i];
        }
        
        return result;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example 1: [3,8,9,7,6], K=3 → [9,7,6,3,8]")
    void testExample1() {
        int[] A = {3, 8, 9, 7, 6};
        assertThat(solution(A, 3)).isEqualTo(new int[]{9, 7, 6, 3, 8});
    }
    
    @Test
    @DisplayName("Example 2: [0,0,0], K=1 → [0,0,0]")
    void testAllSame() {
        int[] A = {0, 0, 0};
        assertThat(solution(A, 1)).isEqualTo(new int[]{0, 0, 0});
    }
    
    @Test
    @DisplayName("Example 3: [1,2,3,4], K=4 → [1,2,3,4]")
    void testFullRotation() {
        int[] A = {1, 2, 3, 4};
        assertThat(solution(A, 4)).isEqualTo(new int[]{1, 2, 3, 4});
    }
    
    @Test
    @DisplayName("Edge case: Empty array")
    void testEmpty() {
        int[] A = {};
        assertThat(solution(A, 3)).isEqualTo(new int[]{});
    }
    
    @Test
    @DisplayName("Edge case: K=0")
    void testZeroRotation() {
        int[] A = {1, 2, 3};
        assertThat(solution(A, 0)).isEqualTo(new int[]{1, 2, 3});
    }
    
    @Test
    @DisplayName("Edge case: K > N")
    void testLargeK() {
        int[] A = {1, 2, 3};
        assertThat(solution(A, 5)).isEqualTo(new int[]{2, 3, 1}); // 5 % 3 = 2
    }
    
    @Test
    @DisplayName("Edge case: Single element")
    void testSingleElement() {
        int[] A = {5};
        assertThat(solution(A, 100)).isEqualTo(new int[]{5});
    }
}
