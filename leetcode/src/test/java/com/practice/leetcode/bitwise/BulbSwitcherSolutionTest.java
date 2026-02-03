package com.practice.leetcode.bitwise;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * BULB SWITCHER (LeetCode 319)
 *
 * Problem:
 * There are n bulbs that are initially off. You first turn on all the bulbs.
 * Then, you turn off every second bulb. On the third round, you toggle every third bulb.
 * For the i-th round, you toggle every i-th bulb. For the n-th round, you only toggle the last bulb.
 * Find how many bulbs are on after n rounds.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * APPROACH 1: BRUTE FORCE - SIMULATION
 * ═══════════════════════════════════════════════════════════════════════════
 * 
 * Ý tưởng: Mô phỏng đúng như đề bài
 * 1. Tạo mảng boolean[] bulbs để lưu trạng thái bóng (true = sáng)
 * 2. Với mỗi vòng i (từ 1 đến n):
 *    - Toggle các bóng ở vị trí i, 2i, 3i, ...
 * 3. Đếm số bóng sáng cuối cùng
 * 
 * Time: O(n²) - n vòng, mỗi vòng toggle n/i bóng
 * Space: O(n)
 * 
 * ⚠️ TLE (Time Limit Exceeded) với n lớn!
 * 
 * ═══════════════════════════════════════════════════════════════════════════
 * APPROACH 2: COUNT DIVISORS
 * ═══════════════════════════════════════════════════════════════════════════
 * 
 * Insight: Bóng thứ i được toggle bao nhiêu lần?
 * → Số lần = Số ước của i
 * 
 * Ví dụ:
 * - Bóng 1: Ước {1} → Toggle 1 lần → SÁNG ✅
 * - Bóng 6: Ước {1,2,3,6} → Toggle 4 lần → TẮT ❌
 * - Bóng 9: Ước {1,3,9} → Toggle 3 lần → SÁNG ✅
 * 
 * Quy luật:
 * - Số ước LẺ → Bóng SÁNG
 * - Số ước CHẴN → Bóng TẮT
 * 
 * Time: O(n√n) - với mỗi số đếm ước mất O(√n)
 * Space: O(1)
 * 
 * ⚠️ Vẫn chậm với n lớn!
 * 
 * ═══════════════════════════════════════════════════════════════════════════
 * APPROACH 3: PERFECT SQUARE (OPTIMAL)
 * ═══════════════════════════════════════════════════════════════════════════
 * 
 * Key Insight: Khi nào một số có số ước LẺ?
 * 
 * Thông thường các ước đi thành CẶP:
 * - 12 = 1×12, 2×6, 3×4 → {1,2,3,4,6,12} (6 ước - CHẴN)
 * 
 * Ngoại lệ - Số chính phương:
 * - 9 = 1×9, 3×3 → {1,3,9} (3 ước - LẺ)
 *   ↑ Vì 3 ghép với chính nó!
 * 
 * KẾT LUẬN: Chỉ số chính phương mới có số ước LẺ!
 * 
 * Giải pháp:
 * - Đếm số lượng số chính phương từ 1 đến n
 * - Công thức: floor(√n)
 * 
 * Ví dụ:
 * - n=9  → √9=3   → Có 3 số: 1,4,9
 * - n=15 → √15≈3.87 → floor=3 → Có 3 số: 1,4,9
 * - n=25 → √25=5  → Có 5 số: 1,4,9,16,25
 * 
 * Time: O(1)
 * Space: O(1)
 * ✅ BEST SOLUTION!
 *
 */
class BulbSwitcherSolutionTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: BRUTE FORCE - SIMULATION (TLE)
    // ═══════════════════════════════════════════════════════════════════════════
    /**
     * Mô phỏng chính xác đề bài: Toggle từng bóng qua n vòng.
     * 
     * Time: O(n²)
     * Space: O(n)
     */
    public int bulbSwitchBruteForce(int n) {
        boolean[] bulbs = new boolean[n + 1]; // index 0 không dùng
        
        // n vòng
        for (int round = 1; round <= n; round++) {
            // Toggle các bóng ở vị trí round, 2*round, 3*round, ...
            for (int bulbId = round; bulbId <= n; bulbId += round) {
                bulbs[bulbId] = !bulbs[bulbId];
            }
        }
        
        // Đếm số bóng sáng
        int count = 0;
        for (int i = 1; i <= n; i++) {
            if (bulbs[i]) count++;
        }
        
        return count;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: COUNT DIVISORS
    // ═══════════════════════════════════════════════════════════════════════════
    /**
     * Đếm số ước của từng bóng, nếu số ước lẻ → bóng sáng.
     * 
     * Time: O(n√n)
     * Space: O(1)
     */
    public int bulbSwitchCountDivisors(int n) {
        int count = 0;
        
        for (int i = 1; i <= n; i++) {
            // Đếm số ước của i
            int divisors = countDivisors(i);
            
            // Nếu số ước lẻ → bóng sáng
            if (divisors % 2 == 1) {
                count++;
            }
        }
        
        return count;
    }
    
    private int countDivisors(int n) {
        int count = 0;
        for (int i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                count++; // Đếm i
                if (i != n / i) {
                    count++; // Đếm n/i (nếu khác i)
                }
            }
        }
        return count;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 3: PERFECT SQUARE (OPTIMAL) ✅
    // ═══════════════════════════════════════════════════════════════════════════
    /**
     * Chỉ số chính phương mới có số ước lẻ → chỉ cần đếm số chính phương.
     * 
     * Time: O(1)
     * Space: O(1)
     */
    public int bulbSwitch(int n) {
        return (int) Math.sqrt(n);
    }

    /**
     * BULB SWITCHER II (LeetCode 672)
     * 
     * Problem:
     * There are n bulbs and 4 buttons that perform different toggles.
     * Find the number of different possible status of bulbs after m operations.
     */
    public int flipLights(int n, int m) {
        if (m == 0) return 1;
        if (n == 1) return 2;
        if (n == 2) {
            return m == 1 ? 3 : 4;
        }
        if (m == 1) return 4;
        if (m == 2) return 7;
        return 8;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // VARIANT: Input as Array/List
    // ═══════════════════════════════════════════════════════════════════════════
    /**
     * Nếu input là array/list các INDEX bóng đèn:
     * 
     * Case 1: positions = [0, 1, 2, 3, 4] (tương đương n=5)
     * → Vẫn áp dụng công thức √n
     * 
     * Case 2: Nếu ý nghĩa khác (ví dụ: toggle theo ID cụ thể)
     * → Cần mô phỏng từng bóng
     */
    public int bulbSwitchArray(int[] bulbIds) {
        int n = bulbIds.length;
        boolean[] bulbs = new boolean[n];
        
        // n vòng toggle
        for (int round = 1; round <= n; round++) {
            // Toggle các bóng tại index (round-1), (2*round-1), ...
            for (int idx = round - 1; idx < n; idx += round) {
                bulbs[idx] = !bulbs[idx];
            }
        }
        
        // Đếm số bóng sáng
        int count = 0;
        for (boolean bulb : bulbs) {
            if (bulb) count++;
        }
        
        return count;
    }
    
    /**
     * Nếu input là LIST các vị trí bóng (không liên tục):
     * Ví dụ: positions = [1, 4, 9, 16] (4 bóng ở vị trí đặc biệt)
     * 
     * Quy tắc: Bóng tại vị trí p được toggle bởi các ước của p
     */
    public int bulbSwitchPositions(int[] positions) {
        int count = 0;
        
        for (int pos : positions) {
            // Đếm số ước của vị trí này
            int divisors = countDivisors(pos);
            
            // Nếu số ước lẻ → bóng sáng
            if (divisors % 2 == 1) {
                count++;
            }
        }
        
        return count;
    }

    /**
     * VARIANT: Input as List<Integer>
     * 
     * Khi input là List thay vì array, logic tương tự nhưng dùng List API.
     * List linh hoạt hơn array (dynamic size, có nhiều method tiện ích).
     */
    public int bulbSwitchList(List<Integer> bulbIds) {
        int n = bulbIds.size();
        boolean[] bulbs = new boolean[n];
        
        // n vòng toggle
        for (int round = 1; round <= n; round++) {
            // Toggle các bóng tại index (round-1), (2*round-1), ...
            for (int idx = round - 1; idx < n; idx += round) {
                bulbs[idx] = !bulbs[idx];
            }
        }
        
        // Đếm số bóng sáng
        int count = 0;
        for (boolean bulb : bulbs) {
            if (bulb) count++;
        }
        
        return count;
    }
    
    /**
     * VARIANT: List<Integer> positions (vị trí không liên tục)
     * 
     * Ví dụ: positions = List.of(1, 4, 9, 16)
     * → Tất cả là số chính phương → tất cả sáng
     */
    public int bulbSwitchListPositions(List<Integer> positions) {
        int count = 0;
        
        for (int pos : positions) {
            // Đếm số ước của vị trí này
            int divisors = countDivisors(pos);
            
            // Nếu số ước lẻ → bóng sáng
            if (divisors % 2 == 1) {
                count++;
            }
        }
        
        return count;
    }

    @Test
    @DisplayName("Bulb Switcher I - Basic cases")
    void testBulbSwitcher() {
        assertThat(bulbSwitch(3)).isEqualTo(1); // Only bulb 1 (1*1)
        assertThat(bulbSwitch(0)).isEqualTo(0);
        assertThat(bulbSwitch(1)).isEqualTo(1);
        assertThat(bulbSwitch(9)).isEqualTo(3); // 1, 4, 9
    }

    @Test
    @DisplayName("Bulb Switcher II - Status count")
    void testFlipLights() {
        assertThat(flipLights(1, 1)).isEqualTo(2);
        assertThat(flipLights(2, 1)).isEqualTo(3);
        assertThat(flipLights(3, 1)).isEqualTo(4);
    }
    
    @Test
    @DisplayName("Bulb Switcher - Array Input")
    void testBulbSwitchArray() {
        // Case 1: Array liên tục [0,1,2,3,4] tương đương n=5
        int[] bulbs5 = {0, 1, 2, 3, 4};
        assertThat(bulbSwitchArray(bulbs5)).isEqualTo(2); // √5 = 2
        
        int[] bulbs9 = new int[9]; // 9 bóng
        assertThat(bulbSwitchArray(bulbs9)).isEqualTo(3); // √9 = 3
    }
    
    @Test
    @DisplayName("Bulb Switcher - Positions Input")
    void testBulbSwitchPositions() {
        // Case: Các vị trí đặc biệt [1, 4, 9, 16] - tất cả là số chính phương
        int[] perfectSquares = {1, 4, 9, 16};
        assertThat(bulbSwitchPositions(perfectSquares)).isEqualTo(4); // Tất cả sáng
        
        // Case: Hỗn hợp [1, 2, 4, 6, 9]
        int[] mixed = {1, 2, 4, 6, 9};
        // 1: Ước {1} - lẻ → sáng
        // 2: Ước {1,2} - chẵn → tắt
        // 4: Ước {1,2,4} - lẻ → sáng
        // 6: Ước {1,2,3,6} - chẵn → tắt
        // 9: Ước {1,3,9} - lẻ → sáng
        assertThat(bulbSwitchPositions(mixed)).isEqualTo(3); // 1, 4, 9 sáng
    }
    
    @Test
    @DisplayName("Bulb Switcher - List Input")
    void testBulbSwitchList() {
        // Case 1: List liên tục tương đương n=5
        List<Integer> list5 = Arrays.asList(0, 1, 2, 3, 4);
        assertThat(bulbSwitchList(list5)).isEqualTo(2); // √5 = 2
        
        // Case 2: List với 9 phần tử
        List<Integer> list9 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        assertThat(bulbSwitchList(list9)).isEqualTo(3); // √9 = 3
    }
    
    @Test
    @DisplayName("Bulb Switcher - List Positions")
    void testBulbSwitchListPositions() {
        // Case 1: Tất cả số chính phương
        List<Integer> perfectSquares = Arrays.asList(1, 4, 9, 16, 25);
        assertThat(bulbSwitchListPositions(perfectSquares)).isEqualTo(5);
        
        // Case 2: Hỗn hợp
        List<Integer> mixed = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        // Chỉ 1, 4, 9 là số chính phương → 3 bóng sáng
        assertThat(bulbSwitchListPositions(mixed)).isEqualTo(3);
    }
}
