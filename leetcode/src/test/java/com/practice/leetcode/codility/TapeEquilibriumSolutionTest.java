package com.practice.leetcode.codility;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CODILITY - TAPE EQUILIBRIUM (Đề gốc khó hiểu, đọc phiên bản đơn giản)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cho array A[], cắt thành 2 phần tại vị trí P (1 ≤ P < N):
 * - Phần trái: A[0] → A[P-1]
 * - Phần phải: A[P] → A[N-1]
 * 
 * Tính: |sum(trái) - sum(phải)|
 * 
 * Tìm: Giá trị nhỏ nhất có thể
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Input: A = [3, 1, 2, 4, 3]
 * A' = [3, 4, 6, 10, 13]
 * 
 * Thử các vị trí cắt:
 * 
 * P=1: [3] | [1,2,4,3]
 * sum=3 | sum=10 → |3-10| = 7
 * 
 * P=2: [3,1] | [2,4,3]
 * sum=4 | sum=9 → |4-9| = 5
 * 
 * P=3: [3,1,2] | [4,3]
 * sum=6 | sum=7 → |6-7| = 1 ✅ MIN
 * 
 * P=4: [3,1,2,4] | [3]
 * sum=10 | sum=3 → |10-3| = 7
 * 
 * Kết quả: 1
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CODILITY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * A non-empty array A consisting of N integers is given. Array A represents
 * numbers on a tape.
 * 
 * Any integer P, such that 0 < P < N, splits this tape into two non-empty
 * parts:
 * A[0], A[1], ..., A[P − 1] and A[P], A[P + 1], ..., A[N − 1].
 * 
 * The difference between the two parts is the value of:
 * |(A[0] + A[1] + ... + A[P − 1]) − (A[P] + A[P + 1] + ... + A[N − 1])|
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 1: BRUTE FORCE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Với mỗi vị trí P:
 * - Tính tổng trái
 * - Tính tổng phải
 * - Tính diff
 * 
 * Time: O(n²)
 * Space: O(1)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 2: PREFIX SUM (OPTIMAL)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Key Insight:
 * - Tính tổng toàn bộ array trước
 * - Duyệt qua từng P:
 * + leftSum tăng dần
 * + rightSum = totalSum - leftSum
 * 
 * Time: O(n)
 * Space: O(1)
 * ✅ OPTIMAL!
 */
class TapeEquilibriumSolutionTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: BRUTE FORCE
    // ═══════════════════════════════════════════════════════════════════════════
    public int solutionBruteForce(int[] A) {
        int n = A.length;
        int minDiff = Integer.MAX_VALUE;

        for (int P = 1; P < n; P++) {
            int leftSum = 0;
            int rightSum = 0;

            for (int i = 0; i < P; i++) {
                leftSum += A[i];
            }

            for (int i = P; i < n; i++) {
                rightSum += A[i];
            }

            int diff = Math.abs(leftSum - rightSum);
            minDiff = Math.min(minDiff, diff);
        }

        return minDiff;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: PREFIX SUM (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    public int solution(int[] A) {
        int n = A.length;

        // Tính tổng toàn bộ
        int totalSum = 0;
        for (int num : A) {
            totalSum += num;
        }

        int minDiff = Integer.MAX_VALUE;
        int leftSum = 0;

        // Duyệt từng vị trí cắt
        for (int P = 1; P < n; P++) {
            leftSum += A[P - 1];
            int rightSum = totalSum - leftSum;

            int diff = Math.abs(leftSum - rightSum);
            minDiff = Math.min(minDiff, diff);
        }

        return minDiff;
    }

    private int prefixSum(int[] A) {
        int[] prefix = new int[A.length];
        prefix[0] = A[0];
        for (int i = 1; i < A.length; i++) {
            prefix[i] = A[i] + prefix[i - 1];
        }
        int total = Arrays.stream(prefix).sum();
        int min = Integer.MAX_VALUE;
        int left = 0;
        for (int i = 0; i < A.length; i++) {
            left += prefix[i];
            int right = total - left;
            int sum = Math.abs(right - left);
            min = Math.min(sum, min);
        }
        return min;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("TapeEquilibrium - Example case")
    void testExample() {
        int[] A = { 3, 1, 2, 4, 3 };
        assertThat(prefixSum(A)).isEqualTo(1);
        assertThat(prefixSum(A)).isEqualTo(1);
    }

    @Test
    @DisplayName("TapeEquilibrium - Two elements")
    void testTwoElements() {
        int[] A = { 1, 2 };
        assertThat(solution(A)).isEqualTo(1);
    }

    @Test
    @DisplayName("TapeEquilibrium - All same")
    void testAllSame() {
        int[] A = { 5, 5, 5, 5 };
        assertThat(solution(A)).isEqualTo(0);
    }

    @Test
    @DisplayName("TapeEquilibrium - Negative numbers")
    void testNegative() {
        int[] A = { -1000, 1000 };
        assertThat(solution(A)).isEqualTo(2000);
    }

    @Test
    @DisplayName("Prefixsum")
    void testXyz() {
        int[] A = { 3, 1, 2, 4, 3 };
        int[] prefix = new int[A.length];
        prefix[0] = A[0];
        for (int i = 1; i < A.length; i++) {
            prefix[i] = A[i] + prefix[i - 1];
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < A.length; i++) {
            int right = prefix[A.length - 1] - prefix[i];
            int left = prefix[i];
            int sum = Math.abs(right - left);
            min = Math.min(sum, min);
        }
        System.out.println(min);
    }
}
