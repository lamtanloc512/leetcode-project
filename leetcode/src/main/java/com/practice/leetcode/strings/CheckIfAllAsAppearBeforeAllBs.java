package com.practice.leetcode.strings;

/**
 * 2124. Check if All A's Appears Before All B's
 * Easy
 * 
 * Problem:
 * Return true if every 'a' appears before every 'b' in the string.
 * Valid pattern: a*b* (zero or more 'a's followed by zero or more 'b's)
 * 
 * Key Insight:
 * - String is valid if it matches pattern: a*b*
 * - Invalid if we see 'b' followed by 'a' (the "ba" substring)
 * - Multiple approaches with different complexity
 */
public class CheckIfAllAsAppearBeforeAllBs {
    
    /**
     * Approach 1: State Tracking (Your DP Idea - Best Approach!)
     * 
     * Intuition:
     * - Track if we've seen 'b' yet
     * - If we see 'a' after seeing 'b' → invalid (pattern "ba")
     * 
     * Time: O(n), Space: O(1)
     * This is the OPTIMAL solution!
     */
    public boolean checkString(String s) {
        boolean seenB = false;
        
        for (char c : s.toCharArray()) {
            if (c == 'b') {
                seenB = true;
            } else if (seenB) {
                // Current is 'a' but we've seen 'b' before
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Approach 2: Pattern Detection (Even Simpler!)
     * 
     * Intuition:
     * - String is invalid if and only if it contains "ba" substring
     * - Use built-in string methods
     * 
     * Time: O(n), Space: O(1)
     */
    public boolean checkStringPattern(String s) {
        return !s.contains("ba");
    }
    
    /**
     * Approach 3: Find Last 'a' and First 'b'
     * 
     * Intuition:
     * - If last 'a' comes after first 'b' → invalid
     * - Otherwise valid
     * 
     * Time: O(n), Space: O(1)
     */
    public boolean checkStringIndexBased(String s) {
        int lastA = s.lastIndexOf('a');
        int firstB = s.indexOf('b');
        
        // If no 'a' or no 'b', always valid
        if (lastA == -1 || firstB == -1) {
            return true;
        }
        
        // Valid if last 'a' comes before first 'b'
        return lastA < firstB;
    }
    
    /**
     * Approach 4: Regex (One-liner, but slower)
     * 
     * Pattern: zero or more 'a's followed by zero or more 'b's
     * 
     * Time: O(n), Space: O(1)
     */
    public boolean checkStringRegex(String s) {
        return s.matches("a*b*");
    }
    
    /**
     * Approach 5: Compare with Sorted String
     * 
     * Intuition:
     * - A valid string should equal its sorted version
     * - Because 'a' < 'b' in ASCII
     * 
     * Time: O(n log n), Space: O(n)
     * Not optimal, but interesting approach!
     */
    public boolean checkStringSorted(String s) {
        char[] chars = s.toCharArray();
        java.util.Arrays.sort(chars);
        return s.equals(new String(chars));
    }
    
    /**
     * WRONG Approach: Your Brute Force (for comparison)
     * 
     * This works but is inefficient O(n²)
     * Kept here to show why it's not optimal
     */
    public boolean checkStringBruteForce(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'a') {
                // Check if any 'b' exists before this 'a'
                for (int j = 0; j < i; j++) {
                    if (s.charAt(j) == 'b') {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
