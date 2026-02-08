package com.practice.leetcode.strings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Minimum Deletions to Make String Balanced
 * 
 * Pattern Analysis:
 * 1. All 'a's: 0 deletions (already balanced)
 * 2. All 'b's: 0 deletions (already balanced)
 * 3. Already balanced (a*b*): 0 deletions
 * 4. Mixed: need to find optimal split point
 * 5. Edge cases: single character, alternating pattern
 */
class MinimumDeletionsToMakeStringBalancedTest {
    
    private MinimumDeletionsToMakeStringBalanced solution;
    
    @BeforeEach
    void setUp() {
        solution = new MinimumDeletionsToMakeStringBalanced();
    }
    
    @Test
    @DisplayName("Example 1: aababbab -> delete 2 characters")
    void testExample1() {
        String s = "aababbab";
        int expected = 2;
        assertEquals(expected, solution.minimumDeletions(s));
        assertEquals(expected, solution.minimumDeletionsPrefixSuffix(s));
        assertEquals(expected, solution.minimumDeletionsStack(s));
        assertEquals(expected, solution.minimumDeletionsThreeWay(s));
    }
    
    @Test
    @DisplayName("Example 2: bbaaaaabb -> delete 2 characters")
    void testExample2() {
        String s = "bbaaaaabb";
        int expected = 2;
        assertEquals(expected, solution.minimumDeletions(s));
        assertEquals(expected, solution.minimumDeletionsPrefixSuffix(s));
        assertEquals(expected, solution.minimumDeletionsStack(s));
        assertEquals(expected, solution.minimumDeletionsThreeWay(s));
    }
    
    @Test
    @DisplayName("Already balanced string - no deletions needed")
    void testAlreadyBalanced() {
        assertAll(
            () -> assertEquals(0, solution.minimumDeletions("aaabbb")),
            () -> assertEquals(0, solution.minimumDeletions("aaa")),
            () -> assertEquals(0, solution.minimumDeletions("bbb")),
            () -> assertEquals(0, solution.minimumDeletions("abb")),
            () -> assertEquals(0, solution.minimumDeletions("ab"))
        );
    }
    
    @Test
    @DisplayName("Single character - always balanced")
    void testSingleCharacter() {
        assertEquals(0, solution.minimumDeletions("a"));
        assertEquals(0, solution.minimumDeletions("b"));
    }
    
    @Test
    @DisplayName("Reverse order - maximum deletions")
    void testReverseOrder() {
        // "ba" -> delete 1 (either 'b' or 'a')
        assertEquals(1, solution.minimumDeletions("ba"));
        
        // "bbbaaaa" -> delete all 'a's or all 'b's, min is 3
        assertEquals(3, solution.minimumDeletions("bbbaaaa"));
        
        // "bbaa" -> delete 2
        assertEquals(2, solution.minimumDeletions("bbaa"));
    }
    
    @Test
    @DisplayName("Alternating pattern")
    void testAlternatingPattern() {
        // "abab" -> "aab" or "abb", delete 1
        assertEquals(1, solution.minimumDeletions("abab"));
        
        // "baba" -> "ba" or "aa" or "bb", delete 1
        assertEquals(1, solution.minimumDeletions("baba"));
        
        // "ababab" -> delete 2
        assertEquals(2, solution.minimumDeletions("ababab"));
    }
    
    @ParameterizedTest(name = "{0} -> delete {1}")
    @CsvSource({
        "a, 0",
        "b, 0",
        "ab, 0",
        "ba, 1",
        "aab, 0",
        "aba, 1",
        "baa, 1",
        "abb, 0",
        "bab, 1",
        "bba, 1",
        "aaaa, 0",
        "bbbb, 0",
        "aabb, 0",
        "abab, 1",
        "baba, 1",
        "bbaa, 2",
        "aababbab, 2",
        "bbaaaaabb, 2"
    })
    void testParameterized(String s, int expected) {
        assertEquals(expected, solution.minimumDeletions(s));
    }
    
    @Test
    @DisplayName("Complex mixed patterns")
    void testComplexPatterns() {
        // "aaabbbaaabbb" -> delete middle 'aaa' (3 deletions)
        assertEquals(3, solution.minimumDeletions("aaabbbaaabbb"));
        
        // "aabbbabaabbb" -> 2 deletions
        assertEquals(2, solution.minimumDeletions("aabbbabaabbb"));
    }
    
    @Test
    @DisplayName("All approaches produce same result")
    void testAllApproachesSame() {
        String[] testCases = {
            "aababbab",
            "bbaaaaabb",
            "ba",
            "abab",
            "ababab",
            "aaabbbaaabbb",
            "bbaa",
            "aaabbb"
        };
        
        for (String testCase : testCases) {
            int result1 = solution.minimumDeletions(testCase);
            int result2 = solution.minimumDeletionsPrefixSuffix(testCase);
            int result3 = solution.minimumDeletionsStack(testCase);
            int result4 = solution.minimumDeletionsThreeWay(testCase);
            
            assertEquals(result1, result2, "Prefix/Suffix should match DP for: " + testCase);
            assertEquals(result1, result3, "Stack should match DP for: " + testCase);
            assertEquals(result1, result4, "ThreeWay should match DP for: " + testCase);
        }
    }
    
    @Test
    @DisplayName("Large input performance")
    void testLargeInput() {
        // Create string with 10^5 characters
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50000; i++) {
            sb.append('b');
        }
        for (int i = 0; i < 50000; i++) {
            sb.append('a');
        }
        String s = sb.toString();
        
        // Should delete all of one type (50000)
        assertEquals(50000, solution.minimumDeletions(s));
    }
    
    @Test
    @DisplayName("Verify algorithm correctness - trace through")
    void testAlgorithmTrace() {
        // For "aababbab"
        // Position: 0  1  2  3  4  5  6  7
        // Char:     a  a  b  a  b  b  a  b
        // countB:   0  0  1  1  2  3  3  4
        // del:      0  0  0  1  1  1  2  2
        
        String s = "aababbab";
        assertEquals(2, solution.minimumDeletions(s));
        
        // Explanation:
        // Delete positions 2 and 6 -> "aaabbb"
        // Or delete positions 3 and 6 -> "aabbbb"
    }
    
    @Test
    @DisplayName("Edge case - only one type of character")
    void testSingleType() {
        String allA = "aaaaaaa";
        String allB = "bbbbbbb";
        
        assertEquals(0, solution.minimumDeletions(allA));
        assertEquals(0, solution.minimumDeletions(allB));
    }
    
    @Test
    @DisplayName("Minimum deletions with different split points")
    void testSplitPoints() {
        // "bbaab"
        // Split at 0: delete 0 b's + 3 a's = 3
        // Split at 1: delete 1 b + 3 a's = 4
        // Split at 2: delete 2 b's + 2 a's = 4
        // Split at 3: delete 2 b's + 2 a's = 4
        // Split at 4: delete 2 b's + 1 a = 3
        // Split at 5: delete 2 b's + 0 a's = 2 (optimal)
        // Result: Keep "aab" or "bbb", delete 2
        
        assertEquals(2, solution.minimumDeletions("bbaab"));
    }
}
