package com.practice.leetcode.hackerrank;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * HACKERRANK - CHECK FOR NON-IDENTICAL STRING ROTATION
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cho 2 strings s1 và s2 (cùng độ dài).
 * 
 * Kiểm tra: s2 có phải là ROTATION của s1 NHƯNG KHÔNG GIỐNG s1?
 * 
 * Định nghĩa Rotation:
 * - Rotate string = move một số ký tự từ đầu sang cuối
 * - "abcde" rotate 2 positions → "cdeab"
 * 
 * Return:
 * - 1 (true): s2 là rotation của s1 NHƯNG s2 ≠ s1
 * - 0 (false): s2 KHÔNG phải rotation HOẶC s2 == s1
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Example 1:
 * Input:  s1 = "abcde"
 *         s2 = "cdeab"
 * 
 * Rotation check:
 * - Rotate "abcde" by 2: "cdeab" ✓
 * - s2 != s1 ✓
 * Output: 1 (true)
 * 
 * Example 2:
 * Input:  s1 = "abcde"
 *         s2 = "abcde"
 * 
 * - s2 is rotation of s1 (rotate by 0)
 * - BUT s2 == s1 ✗ (identical!)
 * Output: 0 (false) - because identical
 * 
 * Example 3:
 * Input:  s1 = "a"
 *         s2 = "a"
 * 
 * - Single char, always identical
 * Output: 0 (false)
 * 
 * Example 4:
 * Input:  s1 = "abc"
 *         s2 = "bca"
 * 
 * - Rotate "abc" by 1: "bca" ✓
 * - s2 != s1 ✓
 * Output: 1 (true)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * TẠI SAO SOLUTION CỦA BẠN SAI?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Code của bạn:
 * ```java
 * for(int i = 0; i < s1.length(); i++) {
 *     Collections.rotate(c2, i);  // ❌ SAI!
 *     if(c1.equals(c2)) isIdentical = true;
 * }
 * ```
 * 
 * LỖI 1: Collections.rotate() KHÔNG reset về original sau mỗi loop!
 * 
 * Ví dụ: s2 = "abc"
 * i=0: rotate(c2, 0) → c2 = "abc"
 * i=1: rotate(c2, 1) → c2 = "cab" (rotate from "abc")
 * i=2: rotate(c2, 2) → c2 = "abc" (rotate from "cab")  ❌ WRONG!
 * 
 * Đúng phải là:
 * i=0: rotate 0 from original → "abc"
 * i=1: rotate 1 from original → "bca"
 * i=2: rotate 2 from original → "cab"
 * 
 * LỖI 2: Không check s1 != s2 (must be non-identical!)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 1: Brute Force - Try All Rotations
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Time: O(n²)
 * Space: O(n)
 */
class NonIdenticalRotationTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: FIXED VERSION của solution bạn
    // ═══════════════════════════════════════════════════════════════════════════
    
    public int solutionBruteForceFixed(String s1, String s2) {
        if (s1.length() != s2.length()) return 0;
        
        // Check if identical
        if (s1.equals(s2)) return 0;  // ⭐ Must be non-identical!
        
        // Try all rotations
        for (int i = 1; i < s1.length(); i++) {  // ⭐ Start from 1 (skip rotation 0)
            String rotated = s1.substring(i) + s1.substring(0, i);
            if (rotated.equals(s2)) {
                return 1;  // Found non-identical rotation!
            }
        }
        
        return 0;  // Not a rotation
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: CONCATENATION TRICK (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Key Insight:
     * s2 là rotation của s1 ⇔ s2 xuất hiện trong (s1 + s1)
     * 
     * Ví dụ:
     * s1 = "abcde"
     * s1 + s1 = "abcdeabcde"
     * 
     * All rotations của s1:
     * - "abcde" (rotate 0) → substring [0, 5]
     * - "bcdea" (rotate 1) → substring [1, 6]
     * - "cdeab" (rotate 2) → substring [2, 7]
     * - "deabc" (rotate 3) → substring [3, 8]
     * - "eabcd" (rotate 4) → substring [4, 9]
     * 
     * Tất cả đều là substring của "abcdeabcde"!
     * 
     * Time: O(n) - contains() uses KMP-like algorithm
     * Space: O(n)
     */
    public int solution(String s1, String s2) {
        // Check same length
        if (s1.length() != s2.length()) {
            return 0;
        }
        
        // Check if identical (must be NON-identical)
        if (s1.equals(s2)) {
            return 0;  // ⭐ Identical → return 0
        }
        
        // Check if s2 is substring of (s1 + s1)
        String concatenated = s1 + s1;
        return concatenated.contains(s2) ? 1 : 0;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Example 1: s1 = "abcde", s2 = "cdeab"
     * 
     * Step 1: Check length
     * - s1.length = 5, s2.length = 5 ✓
     * 
     * Step 2: Check if identical
     * - "abcde" != "cdeab" ✓
     * 
     * Step 3: Check if s2 in (s1 + s1)
     * - s1 + s1 = "abcdeabcde"
     * - s2 = "cdeab"
     * - "abcdeabcde".contains("cdeab") = true ✓
     *          ↑↑↑↑↑
     *          cdeab found at index 2!
     * 
     * Result: 1 (true)
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Example 2: s1 = "abcde", s2 = "abcde"
     * 
     * Step 1: Check length ✓
     * Step 2: Check if identical
     * - "abcde" == "abcde" ✗
     * 
     * Result: 0 (false) - identical strings not allowed!
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Example 3: s1 = "abc", s2 = "def"
     * 
     * Step 1: Check length ✓
     * Step 2: Check if identical ✓
     * Step 3: Check if s2 in (s1 + s1)
     * - s1 + s1 = "abcabc"
     * - s2 = "def"
     * - "abcabc".contains("def") = false ✗
     * 
     * Result: 0 (false) - not a rotation
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TẠI SAO (s1 + s1) CHỨA TẤT CẢ ROTATIONS?
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Visualization:
     * 
     * s1 = "abcde"
     * 
     * All possible rotations (by cutting at position k):
     * 
     * k=0: [abcde] = "abcde"
     * k=1: [bcde|a] = "bcdea" 
     * k=2: [cde|ab] = "cdeab"
     * k=3: [de|abc] = "deabc"
     * k=4: [e|abcd] = "eabcd"
     * 
     * Now look at (s1 + s1):
     * 
     *     a b c d e a b c d e
     *     ↑─────↑           k=0
     *       ↑─────↑         k=1
     *         ↑─────↑       k=2
     *           ↑─────↑     k=3
     *             ↑─────↑   k=4
     * 
     * Mọi rotation đều là substring liên tiếp độ dài 5!
     * 
     * Công thức:
     * rotation(s1, k) = s1[k:] + s1[:k]
     *                 = substring of (s1 + s1) from index k
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EDGE CASES
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Edge Case 1: Single character
     * s1 = "a", s2 = "a"
     * → Identical → Return 0
     * 
     * Edge Case 2: All same characters
     * s1 = "aaaa", s2 = "aaaa"
     * → Identical → Return 0
     * 
     * Edge Case 3: Different lengths
     * s1 = "abc", s2 = "ab"
     * → Cannot be rotation → Return 0
     * 
     * Edge Case 4: Not a rotation
     * s1 = "abc", s2 = "acb"
     * → "abcabc".contains("acb") = false → Return 0
     * 
     * Edge Case 5: Empty strings
     * s1 = "", s2 = ""
     * → Identical → Return 0
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example 1: abcde, cdeab → 1 (rotation)")
    void testExample1() {
        assertThat(solution("abcde", "cdeab")).isEqualTo(1);
        assertThat(solutionBruteForceFixed("abcde", "cdeab")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Example 2: abcde, abcde → 0 (identical)")
    void testIdentical() {
        assertThat(solution("abcde", "abcde")).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Example 3: a, a → 0 (single char identical)")
    void testSingleChar() {
        assertThat(solution("a", "a")).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Example 4: abc, bca → 1 (rotation)")
    void testRotation1() {
        assertThat(solution("abc", "bca")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Not rotation: abc, acb → 0")
    void testNotRotation() {
        assertThat(solution("abc", "acb")).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Different lengths: abc, ab → 0")
    void testDifferentLength() {
        assertThat(solution("abc", "ab")).isEqualTo(0);
    }
    
    @Test
    @DisplayName("All same: aaaa, aaaa → 0 (identical)")
    void testAllSame() {
        assertThat(solution("aaaa", "aaaa")).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Empty strings: \"\", \"\" → 0")
    void testEmpty() {
        assertThat(solution("", "")).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Rotation by 1: abcd, bcda → 1")
    void testRotationBy1() {
        assertThat(solution("abcd", "bcda")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Rotation by last: abcd, dabc → 1")
    void testRotationByLast() {
        assertThat(solution("abcd", "dabc")).isEqualTo(1);
    }
}
