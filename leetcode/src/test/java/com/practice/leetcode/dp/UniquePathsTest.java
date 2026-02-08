package com.practice.leetcode.dp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Unique Paths
 * 
 * This demonstrates 2D DP pattern evolution from 1D (Climbing Stairs)
 */
class UniquePathsTest {
    
    private UniquePaths solution;
    
    @BeforeEach
    void setUp() {
        solution = new UniquePaths();
    }
    
    @Test
    @DisplayName("Example 1: 3x7 grid -> 28 paths")
    void testExample1() {
        int m = 3, n = 7;
        assertEquals(28, solution.uniquePaths(m, n));
        assertEquals(28, solution.uniquePathsOptimized(m, n));
        assertEquals(28, solution.uniquePathsMemo(m, n));
        assertEquals(28, solution.uniquePathsMath(m, n));
    }
    
    @Test
    @DisplayName("Example 2: 3x2 grid -> 3 paths")
    void testExample2() {
        int m = 3, n = 2;
        assertEquals(3, solution.uniquePaths(m, n));
        assertEquals(3, solution.uniquePathsOptimized(m, n));
        assertEquals(3, solution.uniquePathsMemo(m, n));
        assertEquals(3, solution.uniquePathsMath(m, n));
    }
    
    @Test
    @DisplayName("1x1 grid -> 1 path (already at destination)")
    void testSingleCell() {
        assertEquals(1, solution.uniquePaths(1, 1));
        assertEquals(1, solution.uniquePathsOptimized(1, 1));
        assertEquals(1, solution.uniquePathsMemo(1, 1));
        assertEquals(1, solution.uniquePathsMath(1, 1));
    }
    
    @Test
    @DisplayName("1xN grid -> 1 path (only go right)")
    void testSingleRow() {
        assertEquals(1, solution.uniquePaths(1, 5));
        assertEquals(1, solution.uniquePaths(1, 10));
        assertEquals(1, solution.uniquePaths(1, 100));
    }
    
    @Test
    @DisplayName("Mx1 grid -> 1 path (only go down)")
    void testSingleColumn() {
        assertEquals(1, solution.uniquePaths(5, 1));
        assertEquals(1, solution.uniquePaths(10, 1));
        assertEquals(1, solution.uniquePaths(100, 1));
    }
    
    @Test
    @DisplayName("2x2 grid -> 2 paths")
    void testTwoByTwo() {
        // Paths: RD or DR
        assertEquals(2, solution.uniquePaths(2, 2));
    }
    
    @Test
    @DisplayName("3x3 grid -> 6 paths")
    void testThreeByThree() {
        // Need 2 Down, 2 Right = C(4,2) = 6
        assertEquals(6, solution.uniquePaths(3, 3));
        assertEquals(6, solution.uniquePathsOptimized(3, 3));
        assertEquals(6, solution.uniquePathsMemo(3, 3));
        assertEquals(6, solution.uniquePathsMath(3, 3));
    }
    
    @ParameterizedTest(name = "{0}x{1} grid -> {2} paths")
    @CsvSource({
        "1, 1, 1",
        "1, 2, 1",
        "2, 1, 1",
        "2, 2, 2",
        "2, 3, 3",
        "3, 2, 3",
        "3, 3, 6",
        "3, 7, 28",
        "4, 4, 20",
        "5, 5, 70"
    })
    void testParameterized(int m, int n, int expected) {
        assertEquals(expected, solution.uniquePaths(m, n));
    }
    
    @Test
    @DisplayName("Verify symmetry: paths(m,n) = paths(n,m)")
    void testSymmetry() {
        // Grid is symmetric, so paths should be same
        assertEquals(solution.uniquePaths(3, 7), solution.uniquePaths(7, 3));
        assertEquals(solution.uniquePaths(4, 5), solution.uniquePaths(5, 4));
        assertEquals(solution.uniquePaths(10, 15), solution.uniquePaths(15, 10));
    }
    
    @Test
    @DisplayName("All approaches produce same result")
    void testAllApproachesSame() {
        int[][] testCases = {
            {3, 7}, {3, 2}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {10, 10}
        };
        
        for (int[] test : testCases) {
            int m = test[0], n = test[1];
            int result1 = solution.uniquePaths(m, n);
            int result2 = solution.uniquePathsOptimized(m, n);
            int result3 = solution.uniquePathsMemo(m, n);
            int result4 = solution.uniquePathsMath(m, n);
            
            assertEquals(result1, result2, "Optimized should match 2D DP");
            assertEquals(result1, result3, "Memo should match 2D DP");
            assertEquals(result1, result4, "Math should match 2D DP");
        }
    }
    
    @Test
    @DisplayName("Trace through 3x3 grid manually")
    void testTraceThreeByThree() {
        // Manual calculation for 3x3:
        //   0  1  2
        // 0 1  1  1
        // 1 1  2  3
        // 2 1  3  6
        
        // dp[2][2] = dp[1][2] + dp[2][1] = 3 + 3 = 6
        assertEquals(6, solution.uniquePaths(3, 3));
    }
    
    @Test
    @DisplayName("Verify mathematical formula: C(m+n-2, m-1)")
    void testMathematicalFormula() {
        // For 3x3: need 2 Down, 2 Right
        // Total moves = 4, choose 2 for Down: C(4,2) = 6
        assertEquals(6, solution.uniquePathsMath(3, 3));
        
        // For 3x7: need 2 Down, 6 Right
        // Total moves = 8, choose 2: C(8,2) = 28
        assertEquals(28, solution.uniquePathsMath(3, 7));
    }
    
    @Test
    @DisplayName("Large input: 10x10 grid")
    void testLargeGrid() {
        int m = 10, n = 10;
        // Need 9 Down, 9 Right = C(18,9) = 48,620
        int expected = 48620;
        
        assertEquals(expected, solution.uniquePaths(m, n));
        assertEquals(expected, solution.uniquePathsOptimized(m, n));
        assertEquals(expected, solution.uniquePathsMemo(m, n));
        assertEquals(expected, solution.uniquePathsMath(m, n));
    }
    
    @Test
    @DisplayName("Maximum constraint: 100x100 grid")
    void testMaxConstraint() {
        int m = 100, n = 100;
        
        // All approaches should handle this
        long result1 = solution.uniquePaths(m, n);
        long result2 = solution.uniquePathsOptimized(m, n);
        long result3 = solution.uniquePathsMemo(m, n);
        long result4 = solution.uniquePathsMath(m, n);
        
        assertTrue(result1 > 0 && result1 <= 2_000_000_000L);
        assertEquals(result1, result2);
        assertEquals(result1, result3);
        assertEquals(result1, result4);
    }
    
    @Test
    @DisplayName("Recursive approach (small grids only)")
    void testRecursiveSmallGrids() {
        // Only test small grids (recursive is exponential)
        assertEquals(1, solution.uniquePathsRecursive(1, 1));
        assertEquals(2, solution.uniquePathsRecursive(2, 2));
        assertEquals(3, solution.uniquePathsRecursive(3, 2));
        assertEquals(6, solution.uniquePathsRecursive(3, 3));
    }
    
    @Test
    @DisplayName("Understand DP table construction")
    void testDpTableConstruction() {
        // For 3x2 grid:
        //   0  1
        // 0 1  1
        // 1 1  2
        // 2 1  3
        
        // Manually verify key cells
        int m = 3, n = 2;
        assertEquals(3, solution.uniquePaths(m, n));
        
        // The three paths are:
        // 1. Down -> Down -> Right
        // 2. Down -> Right -> Down
        // 3. Right -> Down -> Down
    }
    
    @Test
    @DisplayName("Edge case: extremely narrow grids")
    void testNarrowGrids() {
        assertEquals(1, solution.uniquePaths(1, 100));
        assertEquals(1, solution.uniquePaths(100, 1));
    }
    
    @Test
    @DisplayName("Performance: Optimized vs 2D Array")
    void testPerformance() {
        int m = 50, n = 50;
        
        long start1 = System.nanoTime();
        solution.uniquePaths(m, n);
        long time1 = System.nanoTime() - start1;
        
        long start2 = System.nanoTime();
        solution.uniquePathsOptimized(m, n);
        long time2 = System.nanoTime() - start2;
        
        long start3 = System.nanoTime();
        solution.uniquePathsMath(m, n);
        long time3 = System.nanoTime() - start3;
        
        // Math should be fastest
        assertTrue(time3 <= time1, "Math formula should be fast");
        assertTrue(time3 <= time2, "Math formula should be fast");
    }
    
    @Test
    @DisplayName("Relationship with Pascal's Triangle")
    void testPascalTriangleRelation() {
        // The DP table values form Pascal's Triangle pattern
        // For a square grid n×n:
        // - Center value relates to Pascal's Triangle entry
        
        // 2x2: Center path count = 2 = C(2,1)
        assertEquals(2, solution.uniquePaths(2, 2));
        
        // 3x3: Center path count = 6 = C(4,2)
        assertEquals(6, solution.uniquePaths(3, 3));
    }
}
