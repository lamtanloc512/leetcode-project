package com.practice.leetcode.hackerrank;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * HACKERRANK - CHECK PALINDROME BY FILTERING NON-LETTERS
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cho string chứa letters, digits, symbols.
 * Kiểm tra xem có phải palindrome không khi:
 * - Chỉ xét letters (A-Z, a-z)
 * - Bỏ qua digits và symbols
 * - Case-insensitive (A = a)
 * 
 * Return: 1 nếu là palindrome, 0 nếu không
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Example 1:
 * Input:  "A1b2B!a"
 * Step 1: Extract letters → ['A', 'b', 'B', 'a']
 * Step 2: Convert to lowercase → ['a', 'b', 'b', 'a']
 * Step 3: Compare forward and backward → "abba" == "abba" ✓
 * Output: 1 (true)
 * 
 * Example 2:
 * Input:  "Z"
 * Step 1: Extract letters → ['Z']
 * Step 2: Convert to lowercase → ['z']
 * Step 3: Single letter is palindrome
 * Output: 1 (true)
 * 
 * Example 3:
 * Input:  "abc123"
 * Step 1: Extract letters → ['a', 'b', 'c']
 * Step 2: Lowercase → ['a', 'b', 'c']
 * Step 3: "abc" != "cba"
 * Output: 0 (false)
 * 
 * Example 4:
 * Input:  "12345"
 * Step 1: Extract letters → []
 * Step 2: Empty string
 * Step 3: Empty is palindrome? → YES (by convention)
 * Output: 1 (true)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 1: StringBuilder Filter + Reverse
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Time: O(n)
 * Space: O(n)
 */
class CheckPalindromeFilteringTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: Filter + Reverse
    // ═══════════════════════════════════════════════════════════════════════════
    
    public int solutionStringBuilder(String code) {
        // Step 1: Extract only letters and convert to lowercase
        StringBuilder filtered = new StringBuilder();
        
        for (char c : code.toCharArray()) {
            if (Character.isLetter(c)) {
                filtered.append(Character.toLowerCase(c));
            }
        }
        
        // Step 2: Check palindrome
        String str = filtered.toString();
        String reversed = filtered.reverse().toString();
        
        return str.equals(reversed) ? 1 : 0;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: Two Pointers (OPTIMAL - O(n) time, O(1) space)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Key Insight:
     * - Không cần tạo filtered string
     * - Dùng 2 pointers trực tiếp trên original string
     * - Skip non-letters
     * 
     * Time: O(n)
     * Space: O(1)
     */
    public int solution(String code) {
        int left = 0;
        int right = code.length() - 1;
        
        while (left < right) {
            // Skip non-letters from left
            while (left < right && !Character.isLetter(code.charAt(left))) {
                left++;
            }
            
            // Skip non-letters from right
            while (left < right && !Character.isLetter(code.charAt(right))) {
                right--;
            }
            
            // Compare letters (case-insensitive)
            if (left < right) {
                char leftChar = Character.toLowerCase(code.charAt(left));
                char rightChar = Character.toLowerCase(code.charAt(right));
                
                if (leftChar != rightChar) {
                    return 0;  // Not palindrome
                }
                
                left++;
                right--;
            }
        }
        
        return 1;  // Is palindrome
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 3: Stream API (Functional style)
    // ═══════════════════════════════════════════════════════════════════════════
    
    public int solutionStream(String code) {
        String filtered = code.chars()
            .filter(Character::isLetter)
            .map(Character::toLowerCase)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        
        String reversed = new StringBuilder(filtered).reverse().toString();
        
        return filtered.equals(reversed) ? 1 : 0;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Example: code = "A1b2B!a"
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * TWO POINTERS APPROACH
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Initial: "A1b2B!a"
     *           ↑     ↑
     *          L=0   R=6
     * 
     * Step 1:
     * L=0: code[0]='A' → is letter ✓
     * R=6: code[6]='a' → is letter ✓
     * Compare: toLowerCase('A')='a' == toLowerCase('a')='a' ✓
     * L++, R--
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Current: "A1b2B!a"
     *            ↑   ↑
     *           L=1 R=5
     * 
     * Step 2:
     * L=1: code[1]='1' → not letter, L++
     * L=2: code[2]='b' → is letter ✓
     * R=5: code[5]='!' → not letter, R--
     * R=4: code[4]='B' → is letter ✓
     * Compare: toLowerCase('b')='b' == toLowerCase('B')='b' ✓
     * L++, R--
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Current: "A1b2B!a"
     *             ↑↑
     *           L=3 R=3
     * 
     * L >= R → Done!
     * 
     * Result: All checks passed → Return 1 (palindrome)
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EDGE CASES TO HANDLE
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Edge Case 1: Empty string or no letters
     * Input: ""
     * Expected: 1 (empty is palindrome by convention)
     * 
     * Edge Case 2: Only digits/symbols
     * Input: "12345!@#"
     * Expected: 1 (no letters = empty = palindrome)
     * 
     * Edge Case 3: Single letter
     * Input: "Z"
     * Expected: 1 (single letter is palindrome)
     * 
     * Edge Case 4: All same letters
     * Input: "aaa"
     * Expected: 1 (palindrome)
     * 
     * Edge Case 5: Mixed case
     * Input: "AaBbAa"
     * Expected: 1 (case-insensitive palindrome)
     * 
     * Edge Case 6: Long string with many non-letters
     * Input: "A!@#$%^&*()B1234567890B!@#$%^&*()A"
     * Filtered: "ABBA"
     * Expected: 1 (palindrome)
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example: A1b2B!a → 1 (palindrome)")
    void testExample1() {
        assertThat(solution("A1b2B!a")).isEqualTo(1);
        assertThat(solutionStringBuilder("A1b2B!a")).isEqualTo(1);
        assertThat(solutionStream("A1b2B!a")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Single letter: Z → 1")
    void testSingleLetter() {
        assertThat(solution("Z")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Not palindrome: abc123 → 0")
    void testNotPalindrome() {
        assertThat(solution("abc123")).isEqualTo(0);
    }
    
    @Test
    @DisplayName("No letters: 12345 → 1 (empty palindrome)")
    void testNoLetters() {
        assertThat(solution("12345")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Empty string → 1")
    void testEmpty() {
        assertThat(solution("")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Mixed case: AaBbAa → 1")
    void testMixedCase() {
        assertThat(solution("AaBbAa")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Only symbols: !@#$%^& → 1")
    void testOnlySymbols() {
        assertThat(solution("!@#$%^&")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Complex: race-car → 1")
    void testRaceCar() {
        assertThat(solution("race-car")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Not palindrome with symbols: a!b!c → 0")
    void testNotPalindromeSymbols() {
        assertThat(solution("a!b!c")).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Long palindrome with noise")
    void testLongPalindrome() {
        assertThat(solution("A1B2C3C2B1A")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Case sensitive check: Aa → 1 (should be case-insensitive)")
    void testCaseSensitive() {
        assertThat(solution("Aa")).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Almost palindrome: abc123cba → 1")
    void testAlmostPalindrome() {
      assertThat(solution("abc123cba")).isEqualTo(1);
    }
}
