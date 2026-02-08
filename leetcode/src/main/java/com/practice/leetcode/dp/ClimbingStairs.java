package com.practice.leetcode.dp;

/**
 * 70. Climbing Stairs
 * Easy - DP Classic Problem
 * 
 * Problem:
 * You are climbing a staircase with n steps.
 * Each time you can climb 1 or 2 steps.
 * Return the number of distinct ways to reach the top.
 * 
 * ═══════════════════════════════════════════════════════════════════
 * HOW TO "BREAK" A PROBLEM INTO SUBPROBLEMS (DP Analysis Framework)
 * ═══════════════════════════════════════════════════════════════════
 * 
 * STEP 1: ASK THE KEY QUESTION
 * ────────────────────────────
 * "To reach step n, where could I have come from?"
 * Answer: Either from step (n-1) or step (n-2)
 * 
 * STEP 2: DEFINE THE SUBPROBLEM
 * ────────────────────────────
 * Let f(n) = number of ways to reach step n
 * Then: f(n) = f(n-1) + f(n-2)
 * 
 * Why? Because:
 * - All ways to reach (n-1) can take 1 more step to reach n
 * - All ways to reach (n-2) can take 2 more steps to reach n
 * 
 * STEP 3: IDENTIFY BASE CASES
 * ────────────────────────────
 * f(0) = 1  (already at destination, one way: do nothing)
 * f(1) = 1  (only one way: take 1 step)
 * f(2) = 2  (two ways: 1+1 or 2)
 * 
 * STEP 4: BUILD THE RECURRENCE RELATION
 * ────────────────────────────────────
 * f(n) = f(n-1) + f(n-2)  for n ≥ 2
 * 
 * This is the Fibonacci sequence!
 * 
 * STEP 5: CHECK DP PROPERTIES
 * ────────────────────────────
 * ✓ Optimal Substructure: f(n) depends on optimal solutions of f(n-1), f(n-2)
 * ✓ Overlapping Subproblems: f(3) is needed by both f(4) and f(5)
 * 
 * Example Trace (n=5):
 * ────────────────────────────
 * n=0: 1 way
 * n=1: 1 way  (1)
 * n=2: 2 ways (1+1, 2)
 * n=3: 3 ways (1+1+1, 1+2, 2+1)
 * n=4: 5 ways (1+1+1+1, 1+1+2, 1+2+1, 2+1+1, 2+2)
 * n=5: 8 ways (sum of ways for n=4 and n=3)
 */
public class ClimbingStairs {
    
    /**
     * Approach 1: Bottom-Up DP with Array
     * 
     * Build solution from smallest subproblem (n=0) to target (n)
     * 
     * Time: O(n), Space: O(n)
     */
    public int climbStairs(int n) {
        if (n <= 2) return n;
        
        // dp[i] = number of ways to reach step i
        int[] dp = new int[n + 1];
        
        // Base cases
        dp[0] = 1;  // Already at start
        dp[1] = 1;  // One way to reach step 1
        
        // Build up from step 2 to n
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }
    
    /**
     * Approach 2: Space-Optimized DP (Optimal)
     * 
     * Observation: We only need the last 2 values, not entire array
     * 
     * Time: O(n), Space: O(1)
     */
    public int climbStairsOptimized(int n) {
        if (n <= 2) return n;
        
        int prev2 = 1;  // f(n-2)
        int prev1 = 2;  // f(n-1)
        
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
    
    /**
     * Approach 3: Top-Down DP with Memoization
     * 
     * Start from n and recursively break down to base cases
     * Cache results to avoid recomputation
     * 
     * Time: O(n), Space: O(n)
     */
    public int climbStairsMemo(int n) {
        int[] memo = new int[n + 1];
        return helper(n, memo);
    }
    
    private int helper(int n, int[] memo) {
        // Base cases
        if (n <= 2) return n;
        
        // Check if already computed
        if (memo[n] != 0) return memo[n];
        
        // Recursive relation with memoization
        memo[n] = helper(n - 1, memo) + helper(n - 2, memo);
        return memo[n];
    }
    
    /**
     * Approach 4: Pure Recursion (For Understanding Only)
     * 
     * This shows the natural recursive structure
     * WARNING: Exponential time O(2^n) - DO NOT USE for large n!
     * 
     * Time: O(2^n), Space: O(n) call stack
     */
    public int climbStairsRecursive(int n) {
        if (n <= 2) return n;
        return climbStairsRecursive(n - 1) + climbStairsRecursive(n - 2);
    }
    
    /**
     * Approach 5: Mathematical Formula (Bonus)
     * 
     * This is actually the Fibonacci sequence!
     * Can use Binet's formula for O(1) time
     * 
     * Time: O(1), Space: O(1)
     */
    public int climbStairsMath(int n) {
        double sqrt5 = Math.sqrt(5);
        double phi = (1 + sqrt5) / 2;
        double psi = (1 - sqrt5) / 2;
        
        // Fibonacci formula (adjusted for our base cases)
        return (int) Math.round((Math.pow(phi, n + 1) - Math.pow(psi, n + 1)) / sqrt5);
    }
}
