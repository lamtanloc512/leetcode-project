package com.practice.leetcode.strings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Check if All A's Appears Before All B's
 * 
 * Pattern: This is a simpler version of problem 1653
 * - 1653: Find minimum deletions to make string balanced
 * - 2124: Check if string is already balanced
 */
class CheckIfAllAsAppearBeforeAllBsTest {
    
    private CheckIfAllAsAppearBeforeAllBs solution;
    
    @BeforeEach
    void setUp() {
        solution = new CheckIfAllAsAppearBeforeAllBs();
    }
    
    @Test
    @DisplayName("Example 1: aaabbb -> true")
    void testExample1() {
        assertTrue(solution.checkString("aaabbb"));
        assertTrue(solution.checkStringPattern("aaabbb"));
        assertTrue(solution.checkStringIndexBased("aaabbb"));
        assertTrue(solution.checkStringRegex("aaabbb"));
        assertTrue(solution.checkStringSorted("aaabbb"));
    }
    
    @Test
    @DisplayName("Example 2: abab -> false")
    void testExample2() {
        assertFalse(solution.checkString("abab"));
        assertFalse(solution.checkStringPattern("abab"));
        assertFalse(solution.checkStringIndexBased("abab"));
        assertFalse(solution.checkStringRegex("abab"));
        assertFalse(solution.checkStringSorted("abab"));
    }
    
    @Test
    @DisplayName("Example 3: bbb -> true (no 'a's)")
    void testExample3() {
        assertTrue(solution.checkString("bbb"));
        assertTrue(solution.checkStringPattern("bbb"));
        assertTrue(solution.checkStringIndexBased("bbb"));
        assertTrue(solution.checkStringRegex("bbb"));
        assertTrue(solution.checkStringSorted("bbb"));
    }
    
    @Test
    @DisplayName("Only 'a's -> true")
    void testOnlyAs() {
        assertTrue(solution.checkString("aaa"));
        assertTrue(solution.checkString("a"));
        assertTrue(solution.checkString("aaaaaaa"));
    }
    
    @Test
    @DisplayName("Single character -> always true")
    void testSingleChar() {
        assertTrue(solution.checkString("a"));
        assertTrue(solution.checkString("b"));
    }
    
    @Test
    @DisplayName("Simple 'ba' pattern -> false")
    void testSimpleBaPattern() {
        assertFalse(solution.checkString("ba"));
        assertFalse(solution.checkString("bba"));
        assertFalse(solution.checkString("baa"));
        assertFalse(solution.checkString("bbaa"));
    }
    
    @Test
    @DisplayName("Valid patterns")
    void testValidPatterns() {
        assertAll(
            () -> assertTrue(solution.checkString("ab")),
            () -> assertTrue(solution.checkString("aab")),
            () -> assertTrue(solution.checkString("abb")),
            () -> assertTrue(solution.checkString("aabb")),
            () -> assertTrue(solution.checkString("aaabbb")),
            () -> assertTrue(solution.checkString("a")),
            () -> assertTrue(solution.checkString("b")),
            () -> assertTrue(solution.checkString("aaa")),
            () -> assertTrue(solution.checkString("bbb"))
        );
    }
    
    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
        "a, true",
        "b, true",
        "ab, true",
        "ba, false",
        "aab, true",
        "aba, false",
        "baa, false",
        "abb, true",
        "bab, false",
        "bba, false",
        "aaaa, true",
        "bbbb, true",
        "aabb, true",
        "abab, false",
        "baba, false",
        "bbaa, false",
        "aaabbb, true",
        "aabaab, false",
        "aababbab, false"
    })
    void testParameterized(String input, boolean expected) {
        assertEquals(expected, solution.checkString(input));
    }
    
    @Test
    @DisplayName("All approaches produce same result")
    void testAllApproachesSame() {
        String[] testCases = {
            "a", "b", "ab", "ba", "aab", "aba", "baa",
            "aaabbb", "abab", "baba", "bbaa", "aababbab"
        };
        
        for (String testCase : testCases) {
            boolean result1 = solution.checkString(testCase);
            boolean result2 = solution.checkStringPattern(testCase);
            boolean result3 = solution.checkStringIndexBased(testCase);
            boolean result4 = solution.checkStringRegex(testCase);
            boolean result5 = solution.checkStringSorted(testCase);
            boolean result6 = solution.checkStringBruteForce(testCase);
            
            assertEquals(result1, result2, "Pattern should match State Tracking for: " + testCase);
            assertEquals(result1, result3, "IndexBased should match State Tracking for: " + testCase);
            assertEquals(result1, result4, "Regex should match State Tracking for: " + testCase);
            assertEquals(result1, result5, "Sorted should match State Tracking for: " + testCase);
            assertEquals(result1, result6, "BruteForce should match State Tracking for: " + testCase);
        }
    }
    
    @Test
    @DisplayName("Edge case - maximum length")
    void testMaxLength() {
        // 50 'a's followed by 50 'b's
        StringBuilder validBuilder = new StringBuilder();
        for (int i = 0; i < 50; i++) validBuilder.append('a');
        for (int i = 0; i < 50; i++) validBuilder.append('b');
        String valid = validBuilder.toString();
        assertTrue(solution.checkString(valid));
        
        // 50 'b's followed by 50 'a's
        StringBuilder invalidBuilder = new StringBuilder();
        for (int i = 0; i < 50; i++) invalidBuilder.append('b');
        for (int i = 0; i < 50; i++) invalidBuilder.append('a');
        String invalid = invalidBuilder.toString();
        assertFalse(solution.checkString(invalid));
    }
    
    @Test
    @DisplayName("Relationship with problem 1653")
    void testRelationshipWith1653() {
        // If checkString returns true, then minimumDeletions should be 0
        // If checkString returns false, then minimumDeletions should be > 0
        
        String[] validStrings = {"aaabbb", "aaa", "bbb", "ab"};
        String[] invalidStrings = {"abab", "ba", "bbaaa"};
        
        for (String s : validStrings) {
            assertTrue(solution.checkString(s), 
                s + " should be valid (no deletions needed)");
        }
        
        for (String s : invalidStrings) {
            assertFalse(solution.checkString(s), 
                s + " should be invalid (deletions needed)");
        }
    }
    
    @Test
    @DisplayName("Performance comparison - Pattern vs State Tracking")
    void testPerformanceComparison() {
        StringBuilder validBuilder = new StringBuilder();
        for (int i = 0; i < 50; i++) validBuilder.append('a');
        for (int i = 0; i < 50; i++) validBuilder.append('b');
        String longValid = validBuilder.toString();
        
        StringBuilder invalidBuilder = new StringBuilder();
        for (int i = 0; i < 25; i++) invalidBuilder.append("abab");
        String longInvalid = invalidBuilder.toString();
        
        // All should handle large inputs efficiently
        long start1 = System.nanoTime();
        solution.checkString(longValid);
        long time1 = System.nanoTime() - start1;
        
        long start2 = System.nanoTime();
        solution.checkStringPattern(longValid);
        long time2 = System.nanoTime() - start2;
        
        // Both should be fast (under 1ms)
        assertTrue(time1 < 1_000_000, "State tracking should be fast");
        assertTrue(time2 < 1_000_000, "Pattern matching should be fast");
    }
    
    @Test
    @DisplayName("Verify 'ba' substring detection")
    void testBaSubstringDetection() {
        // Contains "ba" -> false
        assertFalse(solution.checkString("aba"));
        assertFalse(solution.checkString("bba"));
        assertFalse(solution.checkString("aabaa"));
        assertFalse(solution.checkString("aabbaa"));
        
        // Does not contain "ba" -> true
        assertTrue(solution.checkString("ab"));
        assertTrue(solution.checkString("aabb"));
        assertTrue(solution.checkString("aaabbb"));
    }
}
