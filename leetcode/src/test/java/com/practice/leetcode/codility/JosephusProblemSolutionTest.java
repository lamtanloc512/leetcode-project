package com.practice.leetcode.codility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU) - SIMULATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Có N người ngồi thành vòng tròn (đánh số 1 đến N).
 * Mỗi lượt:
 * 1. Đếm K người (bao gồm cả người đang cầm)
 * 2. Người thứ K bị loại
 * 3. Tiếp tục từ người kế tiếp
 * 
 * Tìm người CUỐI CÙNG còn lại (Josephus Problem).
 * 
 * Ví dụ: N=5, K=2
 * 
 * Vòng 1: [1, 2, 3, 4, 5]
 *         Start at 1, count 2 → 2 bị loại
 * 
 * Vòng 2: [1, 3, 4, 5]
 *         Start at 3, count 2 → 4 bị loại
 * 
 * Vòng 3: [1, 3, 5]
 *         Start at 5, count 2 → 1 bị loại
 * 
 * Vòng 4: [3, 5]
 *         Start at 3, count 2 → 5 bị loại
 * 
 * Vòng 5: [3]
 *         3 là người cuối cùng! Return 3
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Input:  N=5, K=2
 * Output: 3
 * 
 * Input:  N=7, K=3
 * Output: 4
 * 
 * Input:  N=1, K=1
 * Output: 1
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CODILITY (Khó hiểu, giữ lại để tham khảo)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * There are N people standing in a circle waiting to be executed. The counting 
 * out begins at some point in the circle and proceeds around the circle in a 
 * fixed direction. In each step, a certain number of people are skipped and the 
 * next person is executed. The elimination proceeds around the circle (which is 
 * becoming smaller and smaller as the executed people are removed), until only 
 * the last person remains, who is given freedom.
 * 
 * Given the total number of persons N and a number K which indicates that K-1 
 * people are skipped and Kth person is killed in circle. The task is to choose 
 * the place in the initial circle so that you are the last one remaining and so 
 * survive.
 * 
 * Write a function:
 * class Solution { public int solution(int N, int K); }
 * 
 * that, given two integers N and K, returns the position of the survivor 
 * (positions are numbered from 1 to N).
 * 
 * For example, if N = 5 and K = 2, the function should return 3, as explained above.
 * 
 * Assume that:
 * - N is an integer within the range [1..100,000];
 * - K is an integer within the range [1..100,000].
 */
class JosephusProblemSolutionTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: SIMULATION WITH LIST
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Mô phỏng quá trình loại dần
     * 
     * Time: O(n²) - remove from ArrayList is O(n)
     * Space: O(n)
     */
    public int solutionSimulation(int N, int K) {
        java.util.List<Integer> circle = new java.util.ArrayList<>();
        for (int i = 1; i <= N; i++) {
            circle.add(i);
        }
        
        int index = 0;
        
        while (circle.size() > 1) {
            // Đếm K người (K-1 skip + 1 current)
            index = (index + K - 1) % circle.size();
            
            // Loại người tại index
            circle.remove(index);
            
            // index giữ nguyên vì các phần tử shift left
        }
        
        return circle.get(0);
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: MATHEMATICAL FORMULA (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * JOSEPHUS FORMULA
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Công thức đệ quy:
     * josephus(n, k) = (josephus(n-1, k) + k) % n
     * josephus(1, k) = 0  (base case, vị trí 0-indexed)
     * 
     * Giải thích:
     * - Khi có n người, sau khi loại 1 người, còn n-1 người
     * - Vị trí trong circle mới = (vị trí cũ + k) % n
     * 
     * Convert từ 0-indexed sang 1-indexed: +1
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Time: O(n)
     * Space: O(1)
     */
    public int solution(int N, int K) {
        int survivor = 0;  // Base case: 1 người, vị trí 0 (0-indexed)
        
        for (int i = 2; i <= N; i++) {
            survivor = (survivor + K) % i;
        }
        
        return survivor + 1;  // Convert to 1-indexed
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Example: N=5, K=2
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * SIMULATION APPROACH
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Initial: circle = [1, 2, 3, 4, 5], index = 0
     * 
     * Round 1:
     *   index = (0 + 2 - 1) % 5 = 1
     *   Remove circle[1] = 2
     *   circle = [1, 3, 4, 5]
     * 
     * Round 2:
     *   index = (1 + 2 - 1) % 4 = 2
     *   Remove circle[2] = 4
     *   circle = [1, 3, 5]
     * 
     * Round 3:
     *   index = (2 + 2 - 1) % 3 = 0
     *   Remove circle[0] = 1
     *   circle = [3, 5]
     * 
     * Round 4:
     *   index = (0 + 2 - 1) % 2 = 1
     *   Remove circle[1] = 5
     *   circle = [3]
     * 
     * Survivor: 3
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * MATHEMATICAL APPROACH (0-indexed)
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Base: josephus(1, 2) = 0
     * 
     * i=2: survivor = (0 + 2) % 2 = 0
     * i=3: survivor = (0 + 2) % 3 = 2
     * i=4: survivor = (2 + 2) % 4 = 0
     * i=5: survivor = (0 + 2) % 5 = 2
     * 
     * Result: survivor = 2 (0-indexed)
     * Convert to 1-indexed: 2 + 1 = 3 ✓
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Verification với simulation:
     * 
     * N=5, positions = [1, 2, 3, 4, 5] (1-indexed)
     * Convert to 0-indexed = [0, 1, 2, 3, 4]
     * 
     * Survivor at 0-index = 2
     * Which is position 3 in 1-indexed ✓
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example 1: N=5, K=2 → 3")
    void testExample1() {
        assertThat(solution(5, 2)).isEqualTo(3);
        assertThat(solutionSimulation(5, 2)).isEqualTo(3);
    }
    
    @Test
    @DisplayName("Example 2: N=7, K=3 → 4")
    void testExample2() {
        assertThat(solution(7, 3)).isEqualTo(4);
    }
    
    @Test
    @DisplayName("Edge case: N=1, K=1 → 1")
    void testSinglePerson() {
        assertThat(solution(1, 1)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Edge case: N=1, K=100 → 1")
    void testSinglePersonLargeK() {
        assertThat(solution(1, 100)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("K=1 (eliminate every person)")
    void testK1() {
        // K=1 means always eliminate current person
        // Survivor is always the last one
        assertThat(solution(5, 1)).isEqualTo(5);
    }
    
    @Test
    @DisplayName("N=2, K=2 → 1")
    void testTwoPeople() {
        assertThat(solution(2, 2)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("N=10, K=3")
    void testLarger() {
        int result = solution(10, 3);
        int resultSim = solutionSimulation(10, 3);
        assertThat(result).isEqualTo(resultSim);
    }
    
    @Test
    @DisplayName("Large N")
    void testLargeN() {
        // Mathematical solution should handle this easily
        int result = solution(1000, 7);
        assertThat(result).isGreaterThan(0).isLessThanOrEqualTo(1000);
    }
}
