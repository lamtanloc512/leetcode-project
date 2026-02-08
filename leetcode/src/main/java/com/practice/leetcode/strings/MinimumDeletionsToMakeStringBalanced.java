package com.practice.leetcode.strings;

/**
 * 1653. Minimum Deletions to Make String Balanced
 * Medium
 * 
 * Problem:
 * Given a string s consisting only of 'a' and 'b', delete minimum characters to make s balanced.
 * s is balanced if there is no pair of indices (i,j) where i < j and s[i] = 'b' and s[j] = 'a'.
 * 
 * Key Insight:
 * - A balanced string has all 'a's before all 'b's (pattern: a*b*)
 * - For each position, we can decide to keep all 'a's before it and all 'b's after it
 * - Track deletions needed using DP
 * 
 * Approaches:
 * 1. DP with state tracking - O(n) time, O(1) space
 * 2. Prefix/Suffix counting - O(n) time, O(n) space
 * 3. Stack-based approach - O(n) time, O(n) space
 */
public class MinimumDeletionsToMakeStringBalanced {
    
    /**
     * Approach 1: Dynamic Programming with State Tracking
     * 
     * Intuition:
     * - At each position, we track minimum deletions to make string balanced up to that point
     * - If current char is 'a':
     *   - Either delete it (if we've seen 'b's before)
     *   - Or delete all previous 'b's
     * - If current char is 'b':
     *   - Just increment count of 'b's seen
     * 
     * State:
     * - deletions: minimum deletions needed so far
     * - countB: number of 'b's we've kept in our balanced string
     * 
     * Time: O(n), Space: O(1)
     */
    public int minimumDeletions(String s) {
        int deletions = 0;  // minimum deletions needed
        int countB = 0;      // count of 'b's in our balanced string so far
        
        for (char c : s.toCharArray()) {
            if (c == 'b') {
                // Keep this 'b', increment count
                countB++;
            } else {
                // c == 'a'
                // Option 1: Delete this 'a' (cost = deletions + 1)
                // Option 2: Delete all previous 'b's (cost = countB)
                // We choose minimum
                deletions = Math.min(deletions + 1, countB);
            }
        }
        
        return deletions;
    }
    
    /**
     * Approach 2: Prefix/Suffix Counting
     * 
     * Intuition:
     * - For each split point i, calculate:
     *   - Number of 'b's before i (should be deleted)
     *   - Number of 'a's after i (should be deleted)
     * - The answer is minimum across all split points
     * 
     * Time: O(n), Space: O(n)
     */
    public int minimumDeletionsPrefixSuffix(String s) {
        int n = s.length();
        
        // Count 'a's from right (suffix)
        int[] suffixA = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            suffixA[i] = suffixA[i + 1] + (s.charAt(i) == 'a' ? 1 : 0);
        }
        
        int minDeletions = n;
        int prefixB = 0;
        
        // Try each split point
        for (int i = 0; i <= n; i++) {
            // Delete all 'b's before i and all 'a's from i onwards
            minDeletions = Math.min(minDeletions, prefixB + suffixA[i]);
            
            if (i < n && s.charAt(i) == 'b') {
                prefixB++;
            }
        }
        
        return minDeletions;
    }
    
    /**
     * Approach 3: Stack-based Approach
     * 
     * Intuition:
     * - Build a balanced string greedily
     * - If we see 'ba' pattern, we can delete one character
     * - Use stack to track: when 'a' comes and top is 'b', we have a conflict
     * 
     * Time: O(n), Space: O(n)
     */
    public int minimumDeletionsStack(String s) {
        int deletions = 0;
        int countB = 0;  // Using counter instead of actual stack for space optimization
        
        for (char c : s.toCharArray()) {
            if (c == 'b') {
                countB++;
            } else {
                // c == 'a'
                if (countB > 0) {
                    // We have 'b' before 'a', need to delete one
                    deletions++;
                    countB--;  // Remove one 'b' from consideration
                }
            }
        }
        
        return deletions;
    }
    
    /**
     * Approach 4: Three-way DP (for understanding)
     * 
     * At each position, we can be in one of these states:
     * 1. Still in 'a' section
     * 2. Transitioned to 'b' section
     * 
     * Time: O(n), Space: O(1)
     */
    public int minimumDeletionsThreeWay(String s) {
        int deleteToA = 0;  // deletions if we keep everything as 'a's
        int deleteToB = 0;  // deletions if we transition to 'b's
        
        for (char c : s.toCharArray()) {
            if (c == 'a') {
                // If in 'b' section, we must delete this 'a'
                deleteToB = Math.min(deleteToB + 1, deleteToA);
            } else {
                // c == 'b'
                // If in 'a' section, we must delete this 'b'
                deleteToA++;
                // deleteToB stays same (we can keep this 'b')
            }
        }
        
        return Math.min(deleteToA, deleteToB);
    }
}
