package com.practice.leetcode.codility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cho 3 số nguyên 30-bit A, B, C.
 * 
 * Định nghĩa "conform":
 * - Số X conform to Y nếu: Ở mọi vị trí mà Y có bit=1, X cũng phải có bit=1
 * - Ở vị trí Y có bit=0, X có thể là 0 hoặc 1 (tùy ý)
 * 
 * Đếm có bao nhiêu số nguyên 30-bit conform to ít nhất một trong {A, B, C}.
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Example: B = 1101 (4 bits)
 * 
 * B có bit=1 ở positions: 0, 2, 3
 * B có bit=0 ở position: 1
 * 
 * Các số conform to B:
 * - 1101: bit[1]=0 ✓
 * - 1111: bit[1]=1 ✓ (vị trí 0 tùy ý)
 * 
 * Count = 2^(số bit 0) = 2^1 = 2
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * Ý TƯỞNG CHÍNH
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * 1. Đếm số conform to mỗi số:
 *    Count(X) = 2^(số bit 0 trong X)
 * 
 * 2. Xử lý overlap bằng Inclusion-Exclusion:
 *    |A ∪ B ∪ C| = |A| + |B| + |C| 
 *                  - |A ∩ B| - |A ∩ C| - |B ∩ C| 
 *                  + |A ∩ B ∩ C|
 * 
 * 3. Tính intersection:
 *    A ∩ B = A | B (OR operation)
 *    Vì: Số conform to cả A và B phải có bit=1 ở tất cả vị trí mà A hoặc B có bit=1
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CODILITY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * [Full English problem statement as provided by user]
 */
class BinaryConformCountTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER: Count conforming numbers
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Đếm số lượng numbers conform to X
     * 
     * Formula: 2^(số bit 0 trong X)
     * 
     * Ví dụ:
     * X = 11111111 11111111 11111111 10011111
     *                                 ↑00↑↑↑↑↑
     * 
     * Có 2 bits = 0 → 2^2 = 4 numbers conform
     */
    private long countConforming(int X) {
        // Count số bit 0 (trong 30 bits)
        int zeroBits = 30 - Integer.bitCount(X);
        
        // 2^zeroBits
        return 1L << zeroBits;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // SOLUTION: Inclusion-Exclusion Principle
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * INCLUSION-EXCLUSION EXPLAINED
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Để đếm |A ∪ B ∪ C|:
     * 
     * Step 1: Cộng tất cả
     *   + Count(A) + Count(B) + Count(C)
     * 
     * Step 2: Trừ overlap 2-way
     *   - Count(A ∩ B) - Count(A ∩ C) - Count(B ∩ C)
     * 
     * Step 3: Cộng lại overlap 3-way (bị trừ 2 lần ở step 2)
     *   + Count(A ∩ B ∩ C)
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Key: Tính intersection
     * 
     * A ∩ B nghĩa là: số conform to CẢ A và B
     * → Phải có bit=1 ở tất cả vị trí mà A hoặc B có bit=1
     * → A ∩ B = A | B (OR operation)
     * 
     * Ví dụ:
     * A = 1100
     * B = 1010
     * A ∩ B = 1100 | 1010 = 1110
     * 
     * Số conform to cả A và B phải có:
     * - bit[3] = 1 (vì A[3]=1)
     * - bit[2] = 1 (vì A[2]=1)
     * - bit[1] = 1 (vì B[1]=1)
     * - bit[0] = anything (cả A và B đều =0)
     * 
     * → 1110 đại diện cho constraint!
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     */
    public int solution(int A, int B, int C) {
        // Single sets
        long countA = countConforming(A);
        long countB = countConforming(B);
        long countC = countConforming(C);
        
        // Pairwise intersections (A ∩ B = A | B)
        long countAB = countConforming(A | B);
        long countAC = countConforming(A | C);
        long countBC = countConforming(B | C);
        
        // Triple intersection
        long countABC = countConforming(A | B | C);
        
        // Inclusion-Exclusion
        long result = countA + countB + countC
                    - countAB - countAC - countBC
                    + countABC;
        
        return (int) result;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Example from problem:
     * 
     * A = 11111111 11111111 11111111 10011111 = 1,073,741,727
     * B = 11111111 11111111 11111111 00111111 = 1,073,741,631
     * C = 11111111 11111111 11111111 01101111 = 1,073,741,679
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * STEP 1: Count conforming for each
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * A = ...10011111
     *        ↑00↑↑↑↑↑
     *     2 zero bits → Count(A) = 2^2 = 4
     * 
     * B = ...00111111
     *        00↑↑↑↑↑↑
     *     2 zero bits → Count(B) = 2^2 = 4
     * 
     * C = ...01101111
     *        0↑↑0↑↑↑↑
     *     2 zero bits → Count(C) = 2^2 = 4
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * STEP 2: Count pairwise intersections
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * A | B = 10011111 | 00111111 = 10111111
     *                                ↑0↑↑↑↑↑↑
     *         1 zero bit → Count(A∩B) = 2^1 = 2
     * 
     * A | C = 10011111 | 01101111 = 11111111
     *                                ↑↑↑↑↑↑↑↑
     *         0 zero bits → Count(A∩C) = 2^0 = 1
     * 
     * B | C = 00111111 | 01101111 = 01111111
     *                                0↑↑↑↑↑↑↑
     *         1 zero bit → Count(B∩C) = 2^1 = 2
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * STEP 3: Count triple intersection
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * A | B | C = 11111111
     *             ↑↑↑↑↑↑↑↑
     *             0 zero bits → Count(A∩B∩C) = 2^0 = 1
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * STEP 4: Inclusion-Exclusion
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Result = Count(A) + Count(B) + Count(C)
     *        - Count(A∩B) - Count(A∩C) - Count(B∩C)
     *        + Count(A∩B∩C)
     *        
     *        = 4 + 4 + 4 - 2 - 1 - 2 + 1
     *        = 12 - 5 + 1
     *        = 8 ✓
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example from problem: should return 8")
    void testExample() {
        int A = 0b111111111111111111111110011111; // 1,073,741,727
        int B = 0b111111111111111111111100111111; // 1,073,741,631
        int C = 0b111111111111111111111101101111; // 1,073,741,679
        
        assertThat(solution(A, B, C)).isEqualTo(8);
    }
    
    @Test
    @DisplayName("All same: A=B=C → no overlap subtraction needed")
    void testAllSame() {
        int A = 0b111111; // 63
        
        // Count(A) = 2^0 = 1
        // All intersections = 1
        // Result = 1 + 1 + 1 - 1 - 1 - 1 + 1 = 1
        assertThat(solution(A, A, A)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("All zeros: maximum conforming numbers")
    void testAllZeros() {
        int A = 0;
        int B = 0;
        int C = 0;
        
        // Count(0) = 2^30 = 1,073,741,824
        // But result = 1 (all overlap)
        assertThat(solution(A, B, C)).isEqualTo(1073741824);
    }
    
    @Test
    @DisplayName("No overlap: disjoint constraints")
    void testNoOverlap() {
        // A = 100000, B = 010000, C = 001000 (disjoint bits)
        int A = 0b100000;
        int B = 0b010000;
        int C = 0b001000;
        
        // Each has 29 zero bits → 2^29 each
        // But they overlap when OR together
        int result = solution(A, B, C);
        assertThat(result).isGreaterThan(0);
    }
    
    @Test
    @DisplayName("Edge case: Full mask (all 1s)")
    void testFullMask() {
        int full = (1 << 30) - 1; // 30 bits all 1s
        
        // Count = 2^0 = 1 (only one number: itself)
        assertThat(solution(full, full, full)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Simple case: 4-bit example")
    void testSimple() {
        // A = 1100, B = 1010, C = 0011
        int A = 0b1100;
        int B = 0b1010;
        int C = 0b0011;
        
        int result = solution(A, B, C);
        assertThat(result).isGreaterThan(0);
    }
}
