package com.practice.leetcode.codility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cho string S gồm chữ cái a-z.
 * Tìm độ dài substring dài nhất KHÔNG có ký tự lặp lại.
 * 
 * Ví dụ: "abbcda"
 * 
 * Các substring không lặp:
 * - "a" (length 1)
 * - "ab" (length 2)
 * - "bc" (length 2)
 * - "bcd" (length 3) ← Dài nhất!
 * - "cda" (length 3) ← Dài nhất!
 * 
 * Trả về: 3
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Input:  "abbcda"
 * Output: 3
 * Giải thích: "bcd" hoặc "cda"
 * 
 * Input:  "aaaa"
 * Output: 1
 * Giải thích: Chỉ có "a"
 * 
 * Input:  "abcdef"
 * Output: 6
 * Giải thích: Toàn bộ string không lặp
 * 
 * Input:  ""
 * Output: 0
 * Giải thích: Empty string
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CODILITY (Khó hiểu, giữ lại để tham khảo)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * A string S consisting of N characters is considered to be divided into as many 
 * as possible non-empty non-overlapping substrings such that each substring 
 * contains only identical consecutive characters. Such a division is called a 
 * simple one.
 * 
 * For example, the string "aabbcc" can be divided into: "aa", "bb" and "cc". 
 * Note that the division "aa", "b", "b", "cc" is not valid, as the middle two 
 * substrings contain identical consecutive characters and should be merged.
 * 
 * Write a function:
 * class Solution { public int solution(String S); }
 * 
 * that, given a string S consisting of N characters, returns the length of the 
 * longest substring that contains no repeating characters.
 * 
 * For example, given S = "abbcda", the function should return 3 (substring "bcd" 
 * or "cda"). Given S = "aaaa", the function should return 1. Given S = "abcdef", 
 * the function should return 6 (the whole string).
 * 
 * Write an efficient algorithm for the following assumptions:
 * - N is an integer within the range [0..100,000];
 * - string S consists only of lowercase letters (a−z).
 */
class LongestSubstringWithoutRepeatingTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: BRUTE FORCE
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Check tất cả các substring
     * 
     * Time: O(n³) - n² substrings, mỗi cái check O(n)
     * Space: O(n)
     */
    public int solutionBruteForce(String S) {
        int maxLen = 0;
        
        for (int i = 0; i < S.length(); i++) {
            for (int j = i + 1; j <= S.length(); j++) {
                String sub = S.substring(i, j);
                if (hasAllUniqueChars(sub)) {
                    maxLen = Math.max(maxLen, sub.length());
                }
            }
        }
        
        return maxLen;
    }
    
    private boolean hasAllUniqueChars(String s) {
        java.util.Set<Character> set = new java.util.HashSet<>();
        for (char c : s.toCharArray()) {
            if (set.contains(c)) return false;
            set.add(c);
        }
        return true;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: SLIDING WINDOW + HASH SET (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * SLIDING WINDOW - Ý TƯỞNG
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Duy trì window [left, right] không có ký tự lặp:
     * 
     * 1. Mở rộng right → thêm ký tự mới
     * 2. Nếu gặp duplicate → thu nhỏ left cho đến khi loại bỏ duplicate
     * 3. Update maxLen
     * 
     * Example: "abbcda"
     * 
     * Step 1: [a]         → maxLen=1
     * Step 2: [ab]        → maxLen=2
     * Step 3: [abb]       → duplicate 'b'! Thu nhỏ left
     *         [bb]        → vẫn duplicate
     *         [b]         → OK
     * Step 4: [bc]        → maxLen=2
     * Step 5: [bcd]       → maxLen=3 ✓
     * Step 6: [bcda]      → duplicate 'a'? No, continue
     *                       maxLen=4? No wait...
     * 
     * Actually "abbcda":
     * - 'a' at 0
     * - 'b' at 1, 2
     * - 'c' at 3
     * - 'd' at 4
     * - 'a' at 5 → duplicate with index 0!
     * 
     * Window: [bcd] (index 2-4) length=3
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Time: O(n)
     * Space: O(min(n, 26)) - at most 26 lowercase letters
     */
    public int solution(String S) {
        if (S.length() == 0) return 0;
        
        java.util.Set<Character> window = new java.util.HashSet<>();
        int maxLen = 0;
        int left = 0;
        
        for (int right = 0; right < S.length(); right++) {
            char c = S.charAt(right);
            
            // Nếu duplicate, thu nhỏ window từ left
            while (window.contains(c)) {
                window.remove(S.charAt(left));
                left++;
            }
            
            // Thêm ký tự mới vào window
            window.add(c);
            
            // Update maxLen
            maxLen = Math.max(maxLen, right - left + 1);
        }
        
        return maxLen;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 3: SLIDING WINDOW + HASH MAP (Optimized)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Thay vì thu nhỏ từng bước, jump left trực tiếp đến vị trí sau duplicate
     * 
     * Time: O(n)
     * Space: O(min(n, 26))
     */
    public int solutionHashMap(String S) {
        if (S.length() == 0) return 0;
        
        java.util.Map<Character, Integer> lastIndex = new java.util.HashMap<>();
        int maxLen = 0;
        int left = 0;
        
        for (int right = 0; right < S.length(); right++) {
            char c = S.charAt(right);
            
            // Nếu c đã xuất hiện trong window
            if (lastIndex.containsKey(c) && lastIndex.get(c) >= left) {
                // Jump left đến vị trí sau duplicate
                left = lastIndex.get(c) + 1;
            }
            
            // Update last index
            lastIndex.put(c, right);
            
            // Update maxLen
            maxLen = Math.max(maxLen, right - left + 1);
        }
        
        return maxLen;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Example: S = "abbcda"
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * SLIDING WINDOW + HASH SET
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Initial: left=0, right=0, window={}, maxLen=0
     * 
     * right=0, c='a':
     *   window={a}, left=0, maxLen=1
     *   String: [a]bbcda
     * 
     * right=1, c='b':
     *   window={a,b}, left=0, maxLen=2
     *   String: [ab]bcda
     * 
     * right=2, c='b':
     *   'b' in window! Remove from left
     *   Remove 'a': window={b}, left=1
     *   'b' still in window!
     *   Remove 'b': window={}, left=2
     *   Add 'b': window={b}, maxLen=2
     *   String: ab[b]cda
     * 
     * right=3, c='c':
     *   window={b,c}, left=2, maxLen=2
     *   String: ab[bc]da
     * 
     * right=4, c='d':
     *   window={b,c,d}, left=2, maxLen=3 ✓
     *   String: ab[bcd]a
     * 
     * right=5, c='a':
     *   window={b,c,d,a}, left=2, maxLen=4
     *   String: ab[bcda]
     * 
     * Final: maxLen=4
     * 
     * Wait, this seems wrong! Let me recalculate...
     * 
     * Actually "abbcda" has:
     * - "abcd" at indices [0-3] (no wait, 'b' repeats!)
     * - "bcd" at indices [2-4] ✓ length=3
     * - "cda" at indices [3-5] ✓ length=3
     * 
     * Let me retrace with correct logic...
     * 
     * right=5, c='a':
     *   'a' NOT in current window {b,c,d}
     *   window={b,c,d,a}, maxLen=4
     * 
     * Hmm, but "bcda" has no repeating chars!
     * So maxLen should be 4!
     * 
     * Actually I need to verify the problem statement...
     * The problem asks for longest substring WITHOUT repeating characters.
     * "bcda" has no repeats, so answer is 4!
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example 1: abbcda → 4 (bcda)")
    void testExample1() {
        // "bcda" has length 4 and no repeating chars
        assertThat(solution("abbcda")).isEqualTo(4);
    }
    
    @Test
    @DisplayName("Example 2: aaaa → 1")
    void testAllSame() {
        assertThat(solution("aaaa")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Example 3: abcdef → 6")
    void testAllUnique() {
        assertThat(solution("abcdef")).isEqualTo(6);
    }
    
    @Test
    @DisplayName("Edge case: Empty string → 0")
    void testEmpty() {
        assertThat(solution("")).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Edge case: Single char → 1")
    void testSingleChar() {
        assertThat(solution("a")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Edge case: Two same → 1")
    void testTwoSame() {
        assertThat(solution("aa")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Edge case: Two different → 2")
    void testTwoDifferent() {
        assertThat(solution("ab")).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Complex: abcabcbb → 3 (abc)")
    void testComplex() {
        assertThat(solution("abcabcbb")).isEqualTo(3);
    }
    
    @Test
    @DisplayName("Complex: pwwkew → 3 (wke)")
    void testComplex2() {
        assertThat(solution("pwwkew")).isEqualTo(3);
    }
    
    @Test
    @DisplayName("Complex: bbbbb → 1")
    void testAllB() {
        assertThat(solution("bbbbb")).isEqualTo(1);
    }
}
