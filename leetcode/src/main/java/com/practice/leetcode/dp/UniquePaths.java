package com.practice.leetcode.dp;

/**
 * 62. Unique Paths
 * Medium - 2D DP Classic
 * 
 * Problem:
 * Robot on m x n grid at (0,0), wants to reach (m-1, n-1).
 * Can only move RIGHT or DOWN.
 * Return number of unique paths.
 * 
 * ═══════════════════════════════════════════════════════════════════
 * DP ANALYSIS: FROM 1D TO 2D (Evolution of Climbing Stairs)
 * ═══════════════════════════════════════════════════════════════════
 * 
 * COMPARISON WITH CLIMBING STAIRS:
 * ────────────────────────────────
 * Climbing Stairs (1D):
 *   - State: dp[i] = ways to reach step i
 *   - Choices: come from (i-1) OR (i-2)
 *   - Relation: dp[i] = dp[i-1] + dp[i-2]
 * 
 * Unique Paths (2D):
 *   - State: dp[i][j] = ways to reach cell (i,j)
 *   - Choices: come from TOP (i-1,j) OR LEFT (i,j-1)
 *   - Relation: dp[i][j] = dp[i-1][j] + dp[i][j-1]
 * 
 * KEY INSIGHT:
 * ────────────────────────────────
 * "To reach (i,j), where could I have come from?"
 * Answer: Only from (i-1,j) ↓ or (i,j-1) →
 * 
 * VISUAL EXAMPLE (3x7 grid):
 * ────────────────────────────────
 *     0   1   2   3   4   5   6
 *   ┌───┬───┬───┬───┬───┬───┬───┐
 * 0 │ 1 │ 1 │ 1 │ 1 │ 1 │ 1 │ 1 │
 *   ├───┼───┼───┼───┼───┼───┼───┤
 * 1 │ 1 │ 2 │ 3 │ 4 │ 5 │ 6 │ 7 │
 *   ├───┼───┼───┼───┼───┼───┼───┤
 * 2 │ 1 │ 3 │ 6 │10 │15 │21 │28 │
 *   └───┴───┴───┴───┴───┴───┴───┘
 * 
 * dp[2][6] = dp[1][6] + dp[2][5]
 *          =    7     +    21    = 28
 * 
 * BASE CASES:
 * ────────────────────────────────
 * dp[0][j] = 1  (first row: only go right)
 * dp[i][0] = 1  (first col: only go down)
 * 
 * RECURRENCE RELATION:
 * ────────────────────────────────
 * dp[i][j] = dp[i-1][j] + dp[i][j-1]  for i,j > 0
 */
public class UniquePaths {
    
    /**
     * Approach 1: 2D DP Array (Standard Approach)
     * 
     * Build table from top-left to bottom-right
     * 
     * Time: O(m*n), Space: O(m*n)
     */
    public int uniquePaths(int m, int n) {
        // dp[i][j] = number of ways to reach cell (i,j)
        int[][] dp = new int[m][n];
        
        // Base case: first row - only one way (keep going right)
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        
        // Base case: first column - only one way (keep going down)
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        
        // Fill the rest of the table
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // Ways to reach (i,j) = ways from top + ways from left
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
        
        return dp[m-1][n-1];
    }
    
    /**
     * Approach 2: Space-Optimized 1D DP (Optimal)
     * 
     * Observation: We only need previous row to compute current row
     * Can reduce space from O(m*n) to O(n)
     * 
     * Time: O(m*n), Space: O(n)
     */
    public int uniquePathsOptimized(int m, int n) {
        // dp[j] represents current row
        int[] dp = new int[n];
        
        // Initialize first row (all 1's)
        for (int j = 0; j < n; j++) {
            dp[j] = 1;
        }
        
        // Process each row
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // dp[j] currently holds value from previous row (top)
                // dp[j-1] holds value from same row (left)
                dp[j] = dp[j] + dp[j-1];
            }
        }
        
        return dp[n-1];
    }
    
    /**
     * Approach 3: Top-Down DP with Memoization
     * 
     * Recursive approach starting from (m-1, n-1) going back to (0,0)
     * 
     * Time: O(m*n), Space: O(m*n)
     */
    public int uniquePathsMemo(int m, int n) {
        int[][] memo = new int[m][n];
        return helper(m - 1, n - 1, memo);
    }
    
    private int helper(int i, int j, int[][] memo) {
        // Base cases
        if (i == 0 && j == 0) return 1;  // Starting position
        if (i < 0 || j < 0) return 0;     // Out of bounds
        
        // Check memo
        if (memo[i][j] != 0) return memo[i][j];
        
        // Recursive relation: can come from top or left
        memo[i][j] = helper(i - 1, j, memo) + helper(i, j - 1, memo);
        return memo[i][j];
    }
    
    /**
     * Approach 4: Mathematical/Combinatorics Approach
     * 
     * KEY INSIGHT:
     * - To reach (m-1, n-1) from (0,0):
     *   Need exactly (m-1) DOWN moves and (n-1) RIGHT moves
     * - Total moves = (m-1) + (n-1) = m+n-2
     * - Question: In how many ways can we arrange (m-1) D's and (n-1) R's?
     * - Answer: C(m+n-2, m-1) = (m+n-2)! / ((m-1)! * (n-1)!)
     * 
     * Example: m=3, n=3
     * - Need 2 Down, 2 Right = 4 total moves
     * - Choose 2 positions out of 4 for Down: C(4,2) = 6 ways
     * 
     * Time: O(min(m,n)), Space: O(1)
     */
    public int uniquePathsMath(int m, int n) {
        // Need to calculate C(m+n-2, m-1)
        // Use smaller of m-1 or n-1 to minimize iterations
        int totalMoves = m + n - 2;
        int k = Math.min(m - 1, n - 1);
        
        long result = 1;
        
        // Calculate C(totalMoves, k) = totalMoves! / (k! * (totalMoves-k)!)
        // Optimized: C(n,k) = n * (n-1) * ... * (n-k+1) / k!
        for (int i = 0; i < k; i++) {
            result = result * (totalMoves - i) / (i + 1);
        }
        
        return (int) result;
    }
    
    /**
     * Approach 5: Pure Recursion (For Understanding Only)
     * 
     * WARNING: Exponential time O(2^(m+n)) - DO NOT USE!
     * Shows the natural recursive structure
     * 
     * Time: O(2^(m+n)), Space: O(m+n) call stack
     */
    public int uniquePathsRecursive(int m, int n) {
        return helperRecursive(0, 0, m, n);
    }
    
    private int helperRecursive(int i, int j, int m, int n) {
        // Base cases
        if (i == m - 1 && j == n - 1) return 1;  // Reached destination
        if (i >= m || j >= n) return 0;           // Out of bounds
        
        // Recursive: try going down and going right
        return helperRecursive(i + 1, j, m, n) +  // Go down
               helperRecursive(i, j + 1, m, n);   // Go right
    }
}
