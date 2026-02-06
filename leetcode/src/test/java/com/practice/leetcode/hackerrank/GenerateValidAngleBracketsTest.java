package com.practice.leetcode.hackerrank;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cho số n, sinh ra tất cả các chuỗi VALID của n cặp '<' và '>' with proper nesting.
 * 
 * Ví dụ:
 * n = 1 → ["<>"]
 * n = 2 → ["<><>", "<<>>"]
 * n = 3 → ["<><><>", "<><><>", "<><><>", "<<<>>>", "<<><>>"]
 * 
 * Valid nghĩa là gì?
 * - Số '<' = số '>' = n
 * - Tại mọi vị trí, số '<' đã dùng >= số '>' đã dùng (proper nesting)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * KEY INSIGHT - BACKTRACKING
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Ý tưởng: Build từng ký tự, có 2 choice:
 * 
 * 1. Thêm '<':
 *    - Điều kiện: Số '<' đã dùng < n
 *    - Why? Chỉ được dùng tối đa n cặp
 * 
 * 2. Thêm '>':
 *    - Điều kiện: Số '>' đã dùng < số '<' đã dùng
 *    - Why? Để proper nesting (không thể có '>' mà chưa có '<')
 * 
 * Base case: Khi len(current) == 2*n → Add to result!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC HACKERRANK
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Given n, return all valid sequences of n pairs of '<' and '>' with proper nesting.
 * 
 * Examples:
 * 
 * Example 1:
 * Input: n = 1
 * Output: ["<>"]
 * Explanation: With one pair of angle brackets, the only valid nesting is "< >".
 * The function starts with an empty string and can only place a '<' then a '>' 
 * yielding "<>".
 * 
 * Example 2:
 * Input: n = 2
 * Output: ["<><>", "<<>>"]
 * Explanation: There are two valid sequences for two pairs:
 * - "<><>" — place the two pairs side by side.
 * - "<<>>" — nest the second pair inside the first.
 * The backtracking builds these by adding '<' when we have not yet placed 2, 
 * and '>' when there is an unmatched '<'.
 * 
 * Example 3:
 * Input: n = 3
 * Output: ["<<<>>>", "<<><>>", "<<>><>", "<><><>", "<><<>>"]
 */
class GenerateValidAngleBracketsTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // SOLUTION: BACKTRACKING
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * ALGORITHM
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Use backtracking to generate all valid sequences:
     * 
     * State:
     * - current: Current string being built
     * - open: Number of '<' used so far
     * - close: Number of '>' used so far
     * 
     * Choices:
     * 1. Add '<': if open < n
     * 2. Add '>': if close < open (must have unmatched '<')
     * 
     * Base case: current.length() == 2*n → Add to result!
     * 
     * Time: O(4^n / √n) - Catalan number
     * Space: O(n) - recursion depth
     */
    public List<String> generateBrackets(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, new StringBuilder(), 0, 0, n);
        return result;
    }
    
    private void backtrack(List<String> result, StringBuilder current, 
                          int open, int close, int n) {
        // ─────────────────────────────────────────────────────────────────────
        // BASE CASE: Complete sequence
        // ─────────────────────────────────────────────────────────────────────
        
        if (current.length() == 2 * n) {
            result.add(current.toString());
            return;
        }
        
        // ─────────────────────────────────────────────────────────────────────
        // CHOICE 1: Add '<'
        // ─────────────────────────────────────────────────────────────────────
        
        if (open < n) {
            current.append('<');
            backtrack(result, current, open + 1, close, n);
            current.deleteCharAt(current.length() - 1);  // ⭐ Backtrack!
        }
        
        // ─────────────────────────────────────────────────────────────────────
        // CHOICE 2: Add '>'
        // ─────────────────────────────────────────────────────────────────────
        
        if (close < open) {
            current.append('>');
            backtrack(result, current, open, close + 1, n);
            current.deleteCharAt(current.length() - 1);  // ⭐ Backtrack!
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HACKERRANK FORMAT SOLUTION
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ⭐ Copy paste này vào HackerRank!
     */
    public static List<String> generateBracketsHackerRank(int n) {
        List<String> result = new ArrayList<>();
        backtrackHelper(result, new StringBuilder(), 0, 0, n);
        return result;
    }
    
    private static void backtrackHelper(List<String> result, StringBuilder current,
                                       int open, int close, int n) {
        if (current.length() == 2 * n) {
            result.add(current.toString());
            return;
        }
        
        if (open < n) {
            current.append('<');
            backtrackHelper(result, current, open + 1, close, n);
            current.deleteCharAt(current.length() - 1);
        }
        
        if (close < open) {
            current.append('>');
            backtrackHelper(result, current, open, close + 1, n);
            current.deleteCharAt(current.length() - 1);
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT - n=2
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Backtracking tree cho n=2:
     * 
     *                          ""
     *                         (0,0)
     *                           |
     *                          "<"
     *                         (1,0)
     *                        /     \
     *                      /         \
     *                    "<"          ">"
     *                   (2,0)        (1,1)
     *                     |             |
     *                    ">"           "<"
     *                   (2,1)         (2,1)
     *                     |             |
     *                    ">"           ">"
     *                   (2,2)         (2,2)
     *                     |             |
     *                  "<<>>"        "<><>"
     *                    ✓             ✓
     * 
     * ───────────────────────────────────────────────────────────────────────
     * 
     * Step-by-step:
     * 
     * 1. Start: current="", open=0, close=0
     *    - Can add '<'? Yes (0 < 2)
     *    - Can add '>'? No (0 >= 0 is false)
     * 
     * 2. current="<", open=1, close=0
     *    - Can add '<'? Yes (1 < 2)
     *    - Can add '>'? Yes (0 < 1)
     *    - Try '<' first
     * 
     * 3. current="<<", open=2, close=0
     *    - Can add '<'? No (2 >= 2)
     *    - Can add '>'? Yes (0 < 2)
     *    - Must add '>'
     * 
     * 4. current="<<>", open=2, close=1
     *    - Can add '<'? No (2 >= 2)
     *    - Can add '>'? Yes (1 < 2)
     *    - Must add '>'
     * 
     * 5. current="<<>>", open=2, close=2, length=4
     *    - Complete! Add to result: "<<>>"
     *    - Backtrack to step 3
     * 
     * 6. Back to current="<", open=1, close=0
     *    - Already tried '<', now try '>'
     * 
     * 7. current="<>", open=1, close=1
     *    - Can add '<'? Yes (1 < 2)
     *    - Can add '>'? No (1 >= 1 is false)
     *    - Must add '<'
     * 
     * 8. current="<><", open=2, close=1
     *    - Can add '<'? No (2 >= 2)
     *    - Can add '>'? Yes (1 < 2)
     *    - Must add '>'
     * 
     * 9. current="<><>", open=2, close=2, length=4
     *    - Complete! Add to result: "<><>"
     * 
     * Final result: ["<<>>", "<><>"]
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // WHY close < open?
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ❌ If we allow close >= open:
     * 
     * Example: n=1
     * 
     * Start: ""
     * Add '>': ">" (close=1, open=0) ❌ INVALID!
     * 
     * Why invalid? 
     * - We have '>' without matching '<'
     * - Not proper nesting
     * 
     * ─────────────────────────────────────────────────────────────────────
     * 
     * ✅ With close < open:
     * 
     * Start: ""
     * - Can add '<'? Yes (open=0 < n=1)
     * - Can add '>'? No (close=0 >= open=0 is false)
     * - Must add '<' first!
     * 
     * current="<"
     * - Can add '<'? No (open=1 >= n=1)
     * - Can add '>'? Yes (close=0 < open=1)
     * - Add '>'
     * 
     * current="<>"
     * - Complete! ✓
     * 
     * This ensures proper nesting!
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example 1: n=1 → [\"<>\"]")
    void testExample1() {
        List<String> result = generateBrackets(1);
        assertThat(result).containsExactlyInAnyOrder("<>");
    }
    
    @Test
    @DisplayName("Example 2: n=2 → [\"<><>\", \"<<>>\"]")
    void testExample2() {
        List<String> result = generateBrackets(2);
        assertThat(result).containsExactlyInAnyOrder("<><>", "<<>>");
    }
    
    @Test
    @DisplayName("Example 3: n=3 → 5 valid sequences")
    void testExample3() {
        List<String> result = generateBrackets(3);
        assertThat(result).containsExactlyInAnyOrder(
            "<<<>>>",
            "<<><>>",
            "<<>><>",
            "<><><>",
            "<><<>>"
        );
    }
    
    @Test
    @DisplayName("Edge case: n=0 → [\"\"]")
    void testZero() {
        List<String> result = generateBrackets(0);
        assertThat(result).containsExactly("");
    }
    
    @Test
    @DisplayName("n=4 → 14 valid sequences (Catalan number)")
    void testFour() {
        List<String> result = generateBrackets(4);
        assertThat(result).hasSize(14);  // C(4) = 14
        
        // All should have length 8 and be valid
        for (String s : result) {
            assertThat(s).hasSize(8);
            assertThat(isValid(s)).isTrue();
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER: VALIDATE SEQUENCE
    // ═══════════════════════════════════════════════════════════════════════════
    
    private boolean isValid(String s) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (c == '<') {
                count++;
            } else if (c == '>') {
                count--;
                if (count < 0) return false;  // More '>' than '<'
            }
        }
        return count == 0;  // Must be balanced
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // COMPLEXITY ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Time Complexity: O(4^n / √n)
     * 
     * Why?
     * - Number of valid sequences = Catalan number C(n) = (2n)! / ((n+1)! * n!)
     * - C(n) ≈ 4^n / (n^(3/2) * √π)
     * - Each sequence takes O(n) to build
     * 
     * Examples:
     * - n=1: C(1) = 1
     * - n=2: C(2) = 2
     * - n=3: C(3) = 5
     * - n=4: C(4) = 14
     * - n=5: C(5) = 42
     * - n=10: C(10) = 16,796
     * 
     * Space Complexity: O(n)
     * - Recursion depth: O(2n) = O(n)
     * - StringBuilder: O(2n) = O(n)
     * - Output: O(n * C(n)) but not counted as "extra" space
     */
}
