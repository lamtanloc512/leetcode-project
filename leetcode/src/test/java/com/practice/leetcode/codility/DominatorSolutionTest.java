package com.practice.leetcode.codility;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Tìm phần tử xuất hiện > N/2 lần trong mảng (leader).
 * 
 * Leader = Phần tử chiếm hơn nửa mảng.
 * 
 * Ví dụ:
 * [3, 4, 3, 2, 3, -1, 3, 3]
 * - Độ dài = 8, N/2 = 4
 * - Số 3 xuất hiện 5 lần > 4 → 3 là leader
 * - Trả về index BẤT KỲ của số 3 (có thể 0, 2, 4, 6, 7)
 * 
 * Nếu không có leader → return -1
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Input:  [3, 4, 3, 2, 3, -1, 3, 3]
 * Output: 0 (hoặc 2, 4, 6, 7 - bất kỳ index nào có value=3)
 * Giải thích: Số 3 xuất hiện 5 lần > 8/2 = 4
 * 
 * Input:  [1, 2, 3, 4]
 * Output: -1
 * Giải thích: Không có số nào xuất hiện > 4/2 = 2 lần
 * 
 * Input:  [1, 1, 1, 2, 2]
 * Output: 0 (hoặc 1, 2 - index của số 1)
 * Giải thích: Số 1 xuất hiện 3 lần > 5/2 = 2.5
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CODILITY (Khó hiểu, giữ lại để tham khảo)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * An array A consisting of N integers is given. The dominator of array A is the 
 * value that occurs in more than half of the elements of A.
 * 
 * For example, consider array A such that
 *  A[0] = 3    A[1] = 4    A[2] = 3
 *  A[3] = 2    A[4] = 3    A[5] = -1
 *  A[6] = 3    A[7] = 3
 * 
 * The dominator of A is 3 because it occurs in 5 out of 8 elements of A (namely 
 * in those with indices 0, 2, 4, 6 and 7) and 5 is more than a half of 8.
 * 
 * Write a function
 * class Solution { public int solution(int[] A); }
 * 
 * that, given an array A consisting of N integers, returns index of any element 
 * of array A in which the dominator of A occurs. The function should return −1 
 * if array A does not have a dominator.
 * 
 * For example, given array A such that
 *  A[0] = 3    A[1] = 4    A[2] = 3
 *  A[3] = 2    A[4] = 3    A[5] = -1
 *  A[6] = 3    A[7] = 3
 * the function may return 0, 2, 4, 6 or 7, as explained above.
 * 
 * Write an efficient algorithm for the following assumptions:
 * - N is an integer within the range [0..100,000];
 * - each element of array A is an integer within the range [−2,147,483,648..2,147,483,647].
 */
class DominatorSolutionTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: HASH MAP (Dễ hiểu)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Đếm số lần xuất hiện của mỗi phần tử
     * 
     * Time: O(n)
     * Space: O(n)
     */
    public int solutionHashMap(int[] A) {
        if (A.length == 0) return -1;
        
        Map<Integer, Integer> count = new HashMap<>();
        int n = A.length;
        
        // Đếm frequency
        for (int i = 0; i < n; i++) {
            count.put(A[i], count.getOrDefault(A[i], 0) + 1);
        }
        
        // Tìm dominator
        for (int i = 0; i < n; i++) {
            if (count.get(A[i]) > n / 2) {
                return i;  // Trả về index
            }
        }
        
        return -1;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: BOYER-MOORE VOTING ALGORITHM (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * BOYER-MOORE ALGORITHM - Ý TƯỞNG
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Voting system:
     * - Mỗi lần gặp candidate hiện tại → vote++
     * - Gặp số khác → vote--
     * - Khi vote = 0 → chọn candidate mới
     * 
     * Nếu có majority element (>n/2), nó sẽ survive!
     * 
     * Ví dụ: [3, 4, 3, 2, 3, -1, 3, 3]
     * 
     * i=0: candidate=3, vote=1    [3 wins]
     * i=1: value=4≠3, vote=0      [tie, 3 vs 4]
     * i=2: candidate=3, vote=1    [3 comeback]
     * i=3: value=2≠3, vote=0      [tie]
     * i=4: candidate=3, vote=1    [3 again]
     * i=5: value=-1≠3, vote=0     [tie]
     * i=6: candidate=3, vote=1    [3 again]
     * i=7: value=3, vote=2        [3 dominates!]
     * 
     * Candidate cuối = 3 → Verify có thực sự >n/2 không
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Time: O(n)
     * Space: O(1)
     */
    public int solution(int[] A) {
        if (A.length == 0) return -1;
        
        int n = A.length;
        
        // ═══════════════════════════════════════════════════════════════════════
        // PHASE 1: Tìm candidate (potential dominator)
        // ═══════════════════════════════════════════════════════════════════════
        
        int candidate = A[0];
        int votes = 1;
        
        for (int i = 1; i < n; i++) {
            if (votes == 0) {
                candidate = A[i];
                votes = 1;
            } else if (A[i] == candidate) {
                votes++;  // Vote for candidate
            } else {
                votes--;  // Vote against
            }
        }
        
        // ═══════════════════════════════════════════════════════════════════════
        // PHASE 2: Verify candidate có thực sự là dominator
        // ═══════════════════════════════════════════════════════════════════════
        
        int count = 0;
        int index = -1;
        
        for (int i = 0; i < n; i++) {
            if (A[i] == candidate) {
                count++;
                index = i;  // Lưu index
            }
        }
        
        // Check if count > n/2
        if (count > n / 2) {
            return index;
        }
        
        return -1;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Example: A = [3, 4, 3, 2, 3, -1, 3, 3]
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * PHASE 1: Tìm candidate
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * i=0: A[0]=3, candidate=3, votes=1
     * 
     * i=1: A[1]=4 ≠ candidate(3)
     *      votes-- → votes=0
     * 
     * i=2: votes=0 → New candidate
     *      candidate=3, votes=1
     * 
     * i=3: A[3]=2 ≠ candidate(3)
     *      votes-- → votes=0
     * 
     * i=4: votes=0 → New candidate
     *      candidate=3, votes=1
     * 
     * i=5: A[5]=-1 ≠ candidate(3)
     *      votes-- → votes=0
     * 
     * i=6: votes=0 → New candidate
     *      candidate=3, votes=1
     * 
     * i=7: A[7]=3 = candidate(3)
     *      votes++ → votes=2
     * 
     * Final candidate: 3
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * PHASE 2: Verify
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Count occurrences of 3:
     * i=0: A[0]=3 → count=1, index=0
     * i=2: A[2]=3 → count=2, index=2
     * i=4: A[4]=3 → count=3, index=4
     * i=6: A[6]=3 → count=4, index=6
     * i=7: A[7]=3 → count=5, index=7
     * 
     * count=5 > 8/2=4 ✓
     * 
     * Return: 7 (last index, có thể return 0, 2, 4, 6 cũng được)
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example 1: [3,4,3,2,3,-1,3,3] → any index of 3")
    void testExample1() {
        int[] A = {3, 4, 3, 2, 3, -1, 3, 3};
        int result = solution(A);
        assertThat(result).isIn(0, 2, 4, 6, 7);
        assertThat(A[result]).isEqualTo(3);
    }
    
    @Test
    @DisplayName("Example 2: [1,2,3,4] → -1 (no dominator)")
    void testNoDominator() {
        int[] A = {1, 2, 3, 4};
        assertThat(solution(A)).isEqualTo(-1);
    }
    
    @Test
    @DisplayName("Example 3: [1,1,1,2,2] → index of 1")
    void testExample3() {
        int[] A = {1, 1, 1, 2, 2};
        int result = solution(A);
        assertThat(result).isIn(0, 1, 2);
        assertThat(A[result]).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Edge case: Empty array → -1")
    void testEmpty() {
        int[] A = {};
        assertThat(solution(A)).isEqualTo(-1);
    }
    
    @Test
    @DisplayName("Edge case: Single element → 0")
    void testSingleElement() {
        int[] A = {5};
        assertThat(solution(A)).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Edge case: All same → any index")
    void testAllSame() {
        int[] A = {7, 7, 7, 7, 7};
        int result = solution(A);
        assertThat(result).isBetween(0, 4);
    }
    
    @Test
    @DisplayName("Edge case: Tie (50/50) → -1")
    void testTie() {
        int[] A = {1, 1, 2, 2};
        assertThat(solution(A)).isEqualTo(-1);
    }
    
    @Test
    @DisplayName("Large array: dominator at end")
    void testDominatorAtEnd() {
        int[] A = {1, 2, 3, 5, 5, 5, 5};
        int result = solution(A);
        assertThat(result).isIn(3, 4, 5, 6);
        assertThat(A[result]).isEqualTo(5);
    }
}
