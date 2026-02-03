package com.practice.leetcode.codility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CODILITY - MAX COUNTERS (Đề gốc khó hiểu, đọc phiên bản đơn giản)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Có N counters (đếm) ban đầu = [0, 0, 0, ..., 0]
 * 
 * Cho array A[], mỗi phần tử là 1 operation:
 * - Nếu 1 ≤ A[K] ≤ N: Tăng counter[A[K]] lên 1
 * - Nếu A[K] = N+1: Set TẤT CẢ counters = max hiện tại
 * 
 * Trả về: Trạng thái cuối cùng của counters
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Input: N = 5, A = [3, 4, 4, 6, 1, 4, 4]
 * 
 * Ban đầu:     counters = [0, 0, 0, 0, 0]
 * 
 * A[0]=3:      counters[3]++ → [0, 0, 1, 0, 0]
 * A[1]=4:      counters[4]++ → [0, 0, 1, 1, 0]
 * A[2]=4:      counters[4]++ → [0, 0, 1, 2, 0]
 * A[3]=6 (N+1): max_counter  → [2, 2, 2, 2, 2] (max=2)
 * A[4]=1:      counters[1]++ → [3, 2, 2, 2, 2]
 * A[5]=4:      counters[4]++ → [3, 2, 2, 3, 2]
 * A[6]=4:      counters[4]++ → [3, 2, 2, 4, 2]
 * 
 * Kết quả: [3, 2, 2, 4, 2]
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CODILITY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * You are given N counters, initially set to 0, and you have two possible
 * operations on them:
 * 
 * - increase(X) − counter X is increased by 1,
 * - max counter − all counters are set to the maximum value of any counter.
 * 
 * A non-empty array A of M integers is given. This array represents consecutive
 * operations:
 * - if A[K] = X, such that 1 ≤ X ≤ N, then operation K is increase(X),
 * - if A[K] = N + 1 then operation K is max counter.
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 1: NAIVE SIMULATION (TLE)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Mô phỏng đúng theo đề:
 * - Gặp A[K] ≤ N: Tăng counter
 * - Gặp A[K] = N+1: Set tất cả = max
 * 
 * Time: O(N * M)  ← Quá chậm!
 * Space: O(N)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 2: LAZY UPDATE (OPTIMAL)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Key Insight:
 * - KHÔNG set tất cả counters ngay lập tức
 * - Chỉ lưu "baseline" = giá trị min mà counter phải có
 * - Khi tăng counter: Nếu < baseline → set = baseline trước
 * - Cuối cùng: Apply baseline cho các counter chưa update
 * 
 * Time: O(N + M)  ✅ OPTIMAL!
 * Space: O(N)
 * 
 * Flow:
 * 1. Track currentMax (max hiện tại)
 * 2. Track baseline (giá trị max_counter lần cuối)
 * 3. Khi increase(X):
 *    - Nếu counter[X] < baseline → set counter[X] = baseline
 *    - counter[X]++
 *    - Update currentMax
 * 4. Khi max_counter: baseline = currentMax
 * 5. Cuối cùng: Set tất cả < baseline thành baseline
 */
class MaxCountersSolutionTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: NAIVE SIMULATION
    // ═══════════════════════════════════════════════════════════════════════════
    public int[] solutionNaive(int N, int[] A) {
        int[] counters = new int[N];
        
        for (int operation : A) {
            if (operation >= 1 && operation <= N) {
                // Increase counter
                counters[operation - 1]++;
            } else if (operation == N + 1) {
                // Max counter
                int max = 0;
                for (int c : counters) {
                    max = Math.max(max, c);
                }
                for (int i = 0; i < N; i++) {
                    counters[i] = max;
                }
            }
        }
        
        return counters;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: LAZY UPDATE (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    public int[] solution(int N, int[] A) {
        int[] counters = new int[N];
        int currentMax = 0;    // Max hiện tại
        int baseline = 0;      // Giá trị min mà counter phải có
        
        for (int operation : A) {
            if (operation >= 1 && operation <= N) {
                int idx = operation - 1;
                
                // Nếu counter này chưa được update kể từ lần max_counter cuối
                if (counters[idx] < baseline) {
                    counters[idx] = baseline;
                }
                
                counters[idx]++;
                currentMax = Math.max(currentMax, counters[idx]);
                
            } else if (operation == N + 1) {
                // Lazy update: chỉ lưu baseline
                baseline = currentMax;
            }
        }
        
        // Apply baseline cho các counter chưa được update
        for (int i = 0; i < N; i++) {
            if (counters[i] < baseline) {
                counters[i] = baseline;
            }
        }
        
        return counters;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("MaxCounters - Example case")
    void testExample() {
        int[] A = {3, 4, 4, 6, 1, 4, 4};
        int[] expected = {3, 2, 2, 4, 2};
        
        assertThat(solution(5, A)).isEqualTo(expected);
        assertThat(solutionNaive(5, A)).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("MaxCounters - No max operation")
    void testNoMax() {
        int[] A = {1, 2, 3};
        int[] expected = {1, 1, 1};
        
        assertThat(solution(3, A)).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("MaxCounters - All max operations")
    void testAllMax() {
        int[] A = {4, 4, 4};
        int[] expected = {0, 0, 0};
        
        assertThat(solution(3, A)).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("MaxCounters - Max at end")
    void testMaxAtEnd() {
        int[] A = {1, 1, 1, 4};
        int[] expected = {3, 3, 3};
        
        assertThat(solution(3, A)).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("MaxCounters - Complex case")
    void testComplex() {
        int[] A = {1, 2, 3, 4, 1, 2};
        int[] expected = {2, 2, 1};
        
        assertThat(solution(3, A)).isEqualTo(expected);
    }
}
