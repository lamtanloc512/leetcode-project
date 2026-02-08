package com.practice.leetcode.dp;

/**
 * 63. Unique Paths II
 * Medium - DP with Obstacles
 * 
 * Problem:
 * Same as Unique Paths I, but now grid contains obstacles (1).
 * Robot cannot step on obstacles.
 * 
 * grid[i][j] = 0 (empty), 1 (obstacle)
 * 
 * ANALYSIS OF USER'S IDEA (Subtractive Approach):
 * ────────────────────────────────────────────────
 * User asked: "Can we calculate Total Paths (ignoring obstacles) - Paths through obstacles?"
 * 
 * Why this is HARD:
 * 1. Single Obstacle: Yes, simpler. Total - (Ways to Obstacle * Ways from Obstacle to End).
 * 2. Multiple Obstacles: Very complex due to Inclusion-Exclusion Principle.
 *    - Example: Obstacles at A and B.
 *    - Bad Paths = (Paths through A) + (Paths through B) - (Paths through A AND B).
 *    - With many obstacles, this becomes a combinatorial nightmare.
 * 
 * BETTER APPROACH: DIRECT DP
 * ──────────────────────────
 * Instead of subtracting bad paths, just don't add them.
 * 
 * Logic:
 * - If grid[i][j] is an obstacle: dp[i][j] = 0 (0 ways to reach/pass through).
 * - Else: dp[i][j] = dp[i-1][j] + dp[i][j-1] (same as Unique Paths I).
 * 
 * Base Cases:
 * - Start (0,0): If obstacle, return 0. Else dp[0][0] = 1.
 * - First Row/Col: If blocked at any point, all subsequent cells in that row/col are unreachable (0).
 */
public class UniquePathsII {

    /**
     * Approach 1: 2D DP Array
     * 
     * Time: O(m*n)
     * Space: O(m*n)
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        
        // If start or end is blocked, impossible
        if (obstacleGrid[0][0] == 1 || obstacleGrid[m-1][n-1] == 1) {
            return 0;
        }
        
        int[][] dp = new int[m][n];
        
        // Base Case: Start position
        dp[0][0] = 1;
        
        // Fill first column
        for (int i = 1; i < m; i++) {
            // Only reachable if not obstacle AND previous cell reachable
            if (obstacleGrid[i][0] == 0 && dp[i-1][0] == 1) {
                dp[i][0] = 1;
            } else {
                dp[i][0] = 0; // Blocked or unreachable
            }
        }
        
        // Fill first row
        for (int j = 1; j < n; j++) {
            // Only reachable if not obstacle AND previous cell reachable
            if (obstacleGrid[0][j] == 0 && dp[0][j-1] == 1) {
                dp[0][j] = 1;
            } else {
                dp[0][j] = 0;
            }
        }
        
        // Fill rest
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0; // Obstacle
                } else {
                    dp[i][j] = dp[i-1][j] + dp[i][j-1];
                }
            }
        }
        
        return dp[m-1][n-1];
    }
    
    /**
     * Approach 2: Space Optimized 1D DP
     * 
     * Time: O(m*n)
     * Space: O(n)
     */
    public int uniquePathsWithObstaclesOptimized(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        if (obstacleGrid[0][0] == 1) return 0;
        
        int[] dp = new int[n];
        dp[0] = 1; // Start point
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    // Current cell is obstacle
                    dp[j] = 0;
                } else {
                    // If not obstacle, add value from left (dp[j-1])
                    // dp[j] already contains value from top (previous iteration)
                    if (j > 0) {
                        dp[j] += dp[j-1];
                    }
                    // If j=0, dp[0] remains same from top unless it was reset to 0 by obstacle
                }
            }
        }
        
        return dp[n-1];
    }
}
