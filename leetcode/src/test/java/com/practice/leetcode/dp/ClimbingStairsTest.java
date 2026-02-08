package com.practice.leetcode.dp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Climbing Stairs
 * 
 * This problem demonstrates the fundamental DP pattern:
 * - Breaking problem into subproblems
 * - Identifying recurrence relation
 * - Building from base cases
 */
class ClimbingStairsTest {
    
    private ClimbingStairs solution;
    
    @BeforeEach
    void setUp() {
        solution = new ClimbingStairs();
    }
    
    @Test
    @DisplayName("n=1 -> 1 way")
    void testOneStep() {
        assertEquals(1, solution.climbStairs(1));
        assertEquals(1, solution.climbStairsOptimized(1));
        assertEquals(1, solution.climbStairsMemo(1));
        assertEquals(1, solution.climbStairsMath(1));
    }
    
    @Test
    @DisplayName("n=2 -> 2 ways (1+1 or 2)")
    void testTwoSteps() {
        assertEquals(2, solution.climbStairs(2));
        assertEquals(2, solution.climbStairsOptimized(2));
        assertEquals(2, solution.climbStairsMemo(2));
        assertEquals(2, solution.climbStairsMath(2));
    }
    
    @Test
    @DisplayName("n=3 -> 3 ways (1+1+1, 1+2, 2+1)")
    void testThreeSteps() {
        assertEquals(3, solution.climbStairs(3));
        assertEquals(3, solution.climbStairsOptimized(3));
        assertEquals(3, solution.climbStairsMemo(3));
        assertEquals(3, solution.climbStairsMath(3));
    }
    
    @Test
    @DisplayName("n=5 -> 8 ways (Fibonacci sequence)")
    void testFiveSteps() {
        assertEquals(8, solution.climbStairs(5));
        assertEquals(8, solution.climbStairsOptimized(5));
        assertEquals(8, solution.climbStairsMemo(5));
        assertEquals(8, solution.climbStairsMath(5));
    }
    
    @ParameterizedTest(name = "n={0} -> {1} ways")
    @CsvSource({
        "1, 1",
        "2, 2",
        "3, 3",
        "4, 5",
        "5, 8",
        "6, 13",
        "7, 21",
        "8, 34",
        "9, 55",
        "10, 89"
    })
    void testFibonacciSequence(int n, int expected) {
        assertEquals(expected, solution.climbStairs(n));
    }
    
    @Test
    @DisplayName("Verify Fibonacci pattern: f(n) = f(n-1) + f(n-2)")
    void testFibonacciPattern() {
        for (int n = 3; n <= 10; n++) {
            int fn = solution.climbStairs(n);
            int fn1 = solution.climbStairs(n - 1);
            int fn2 = solution.climbStairs(n - 2);
            
            assertEquals(fn1 + fn2, fn, 
                String.format("f(%d) should equal f(%d) + f(%d)", n, n-1, n-2));
        }
    }
    
    @Test
    @DisplayName("All approaches produce same result")
    void testAllApproachesSame() {
        int[] testCases = {1, 2, 3, 4, 5, 10, 15, 20};
        
        for (int n : testCases) {
            int result1 = solution.climbStairs(n);
            int result2 = solution.climbStairsOptimized(n);
            int result3 = solution.climbStairsMemo(n);
            int result4 = solution.climbStairsMath(n);
            
            assertEquals(result1, result2, "Optimized should match Array for n=" + n);
            assertEquals(result1, result3, "Memo should match Array for n=" + n);
            assertEquals(result1, result4, "Math should match Array for n=" + n);
        }
    }
    
    @Test
    @DisplayName("Large input: n=45 (max constraint)")
    void testLargeInput() {
        int n = 45;
        long expected = 1836311903L; // 46th Fibonacci number
        
        assertEquals(expected, solution.climbStairs(n));
        assertEquals(expected, solution.climbStairsOptimized(n));
        assertEquals(expected, solution.climbStairsMemo(n));
    }
    
    @Test
    @DisplayName("Trace through example: n=4")
    void testTraceExample() {
        // n=4 has 5 ways:
        // 1. 1+1+1+1
        // 2. 1+1+2
        // 3. 1+2+1
        // 4. 2+1+1
        // 5. 2+2
        
        int n = 4;
        int ways = solution.climbStairs(n);
        assertEquals(5, ways);
        
        // Verify: ways(4) = ways(3) + ways(2) = 3 + 2 = 5
        int ways3 = solution.climbStairs(3); // 3
        int ways2 = solution.climbStairs(2); // 2
        assertEquals(ways3 + ways2, ways);
    }
    
    @Test
    @DisplayName("Understand subproblem breakdown")
    void testSubproblemBreakdown() {
        // For n=5, to reach step 5:
        // - Come from step 4 (take 1 step)  → ways(4) = 5
        // - Come from step 3 (take 2 steps) → ways(3) = 3
        // Total: 5 + 3 = 8
        
        int ways5 = solution.climbStairs(5);
        int ways4 = solution.climbStairs(4);
        int ways3 = solution.climbStairs(3);
        
        assertEquals(8, ways5);
        assertEquals(ways4 + ways3, ways5, "ways(5) = ways(4) + ways(3)");
    }
    
    @Test
    @DisplayName("Base cases verification")
    void testBaseCases() {
        // Base case 1: n=1
        assertEquals(1, solution.climbStairs(1), "Only one way to climb 1 step");
        
        // Base case 2: n=2
        assertEquals(2, solution.climbStairs(2), "Two ways: (1+1) or (2)");
    }
    
    @Test
    @DisplayName("Performance: Optimized vs Array")
    void testPerformance() {
        int n = 40;
        
        long start1 = System.nanoTime();
        int result1 = solution.climbStairs(n);
        long time1 = System.nanoTime() - start1;
        
        long start2 = System.nanoTime();
        int result2 = solution.climbStairsOptimized(n);
        long time2 = System.nanoTime() - start2;
        
        assertEquals(result1, result2);
        
        // Optimized should be at least as fast (usually faster due to no array allocation)
        assertTrue(time2 <= time1 * 2, "Optimized should be competitive");
    }
    
    @Test
    @DisplayName("Recursive approach (small n only)")
    void testRecursiveSmallInput() {
        // Only test small values for recursive (exponential time)
        for (int n = 1; n <= 10; n++) {
            int expected = solution.climbStairs(n);
            int recursive = solution.climbStairsRecursive(n);
            assertEquals(expected, recursive, "Recursive should match DP for n=" + n);
        }
    }
    
    @Test
    @DisplayName("Demonstrate exponential growth of recursive calls")
    void testRecursiveComplexity() {
        // For n=10, recursive makes ~177 calls
        // For n=20, recursive makes ~21,891 calls
        // This shows why we need DP!
        
        int n = 15;
        
        // Timed recursive
        long start = System.nanoTime();
        solution.climbStairsRecursive(n);
        long recursiveTime = System.nanoTime() - start;
        
        // Timed DP
        start = System.nanoTime();
        solution.climbStairs(n);
        long dpTime = System.nanoTime() - start;
        
        // DP should be MUCH faster
        assertTrue(dpTime < recursiveTime, 
            "DP should be faster than pure recursion");
    }
}
