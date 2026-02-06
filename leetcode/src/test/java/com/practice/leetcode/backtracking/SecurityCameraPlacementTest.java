package com.practice.leetcode.backtracking;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * PLACE N CAMERAS WITHOUT CONFLICT (N-QUEENS VARIANT)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * ĐỀ BÀI:
 * Cho N×N grid với:
 * - 0 = empty (có thể đặt camera)
 * - 1 = blocked (obstacle)
 * 
 * Đặt N cameras sao cho:
 * - Mỗi ROW có ĐÚNG 1 camera
 * - Không 2 cameras cùng COLUMN
 * - Không 2 cameras cùng DIAGONAL
 * 
 * Return true nếu có thể đặt được.
 * 
 * VÍ DỤ 1:
 * N = 4
 * grid = [[0,0,0,0],
 *          [0,0,0,0],
 *          [0,0,0,0],
 *          [0,0,0,0]]
 * 
 * Output: true
 * 
 * Valid placement:
 * [C, ·, ·, ·]  Row 0 → col 1
 * [·, ·, ·, C]  Row 1 → col 3
 * [·, C, ·, ·]  Row 2 → col 0
 * [·, ·, C, ·]  Row 3 → col 2
 * 
 * VÍ DỤ 2:
 * N = 4
 * grid = [[0,1,0,0],
 *          [0,0,0,1],
 *          [1,0,0,0],
 *          [0,0,1,0]]
 * 
 * Output: true
 * 
 * Valid placement:
 * [·, X, C, ·]  Row 0 → col 2
 * [C, ·, ·, X]  Row 1 → col 0
 * [X, ·, ·, C]  Row 2 → col 3
 * [·, C, X, ·]  Row 3 → col 1
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH: BACKTRACKING (N-QUEENS PATTERN)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Place 1 camera per row, check conflicts:
 * - Column conflict: Same column used
 * - Diagonal conflict: |row1-row2| == |col1-col2|
 * 
 * Backtrack khi conflict hoặc blocked cell.
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * DECISION TREE CHO N=4
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *                          Row 0
 *             ┌────────┬────┴────┬────────┐
 *           Col 0    Col 1    Col 2    Col 3
 *             │        │        │        │
 *          Row 1    Row 1    Row 1    Row 1
 *         (check)  (check)  (check)  (check)
 *             ...
 * 
 * Try every column for each row, backtrack on conflict!
 */
class SecurityCameraPlacementTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // SOLUTION: BACKTRACKING
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Check if can place N cameras on N×N grid with conflicts
     * 
     * Rules:
     * - Each ROW must have exactly 1 camera
     * - No 2 cameras in same COLUMN
     * - No 2 cameras in same DIAGONAL
     * - Can't place on blocked cells (1)
     * 
     * @param N Grid size (N×N)
     * @param grid Grid with 0=empty, 1=blocked
     * @return true if can place N cameras
     */
    public static boolean canPlaceSecurityCameras(int N, List<List<Integer>> grid) {
        if (N == 0) return true;
        if (grid == null || grid.isEmpty()) return false;
        
        boolean[] cols = new boolean[N];     // Track used columns
        boolean[] diag1 = new boolean[2*N];  // Track diagonals \
        boolean[] diag2 = new boolean[2*N];  // Track diagonals /
        
        return backtrack(0, N, grid, cols, diag1, diag2);
    }
    
    /**
     * Try to place cameras row by row
     * 
     * @param row Current row
     * @param N Grid size
     * @param grid The grid
     * @param cols Columns already used
     * @param diag1 Diagonals \\ already used (row-col is constant)
     * @param diag2 Diagonals / already used (row+col is constant)
     */
    private static boolean backtrack(int row, int N, List<List<Integer>> grid,
                                    boolean[] cols, boolean[] diag1, boolean[] diag2) {
        // ─────────────────────────────────────────────────────────────────────
        // BASE CASE: Placed camera in all N rows!
        // ─────────────────────────────────────────────────────────────────────
        if (row == N) {
            return true;  // Success!
        }
        
        // ─────────────────────────────────────────────────────────────────────
        // Try placing camera in each column of current row
        // ─────────────────────────────────────────────────────────────────────
        for (int col = 0; col < N; col++) {
            // Check if position is valid
            if (grid.get(row).get(col) == 1) {
                continue;  // Blocked cell, skip
            }
            
            // Check conflicts
            int d1 = row - col + N;  // Diagonal \\ index (always positive)
            int d2 = row + col;      // Diagonal / index
            
            if (cols[col] || diag1[d1] || diag2[d2]) {
                continue;  // Conflict! Skip this column
            }
            
            // ⭐ MAKE: Place camera
            cols[col] = true;
            diag1[d1] = true;
            diag2[d2] = true;
            
            // ⭐ EXPLORE: Try next row
            if (backtrack(row + 1, N, grid, cols, diag1, diag2)) {
                return true;  // Found valid placement!
            }
            
            // ⭐ UNDO: Remove camera (backtrack)
            cols[col] = false;
            diag1[d1] = false;
            diag2[d2] = false;
        }
        
        return false;  // Cannot place camera in this row
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE EXAMPLE
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Grid: [[0, 0], [0, 0]], N=2
     * 
     * Call 1: backtrack(0, 0, 2, grid)
     * ├─ Try SKIP (0,0)
     * │   └─ Call 2: backtrack(0, 1, 2, grid)
     * │       ├─ Try SKIP (0,1)
     * │       │   └─ Call 3: backtrack(1, 0, 2, grid)
     * │       │       ├─ Try SKIP (1,0)
     * │       │       │   └─ Call 4: backtrack(1, 1, 2, grid)
     * │       │       │       ├─ Try SKIP (1,1)
     * │       │       │       │   └─ Call 5: backtrack(2, 0, 2, grid)
     * │       │       │       │       └─ row=2 >= size=2 → return false
     * │       │       │       ├─ Try PLACE (1,1)
     * │       │       │       │   └─ Call 6: backtrack(2, 0, 1, grid)
     * │       │       │       │       └─ row=2 >= size=2 → return false
     * │       │       │       └─ return false
     * │       │       ├─ Try PLACE (1,0)
     * │       │       │   └─ Call 7: backtrack(1, 1, 1, grid)
     * │       │       │       ├─ Try SKIP (1,1)
     * │       │       │       │   └─ Call 8: backtrack(2, 0, 1, grid)
     * │       │       │       │       └─ return false
     * │       │       │       ├─ Try PLACE (1,1)
     * │       │       │       │   └─ Call 9: backtrack(2, 0, 0, grid)
     * │       │       │       │       └─ remaining=0 → return TRUE! ✓
     * │       │       │       └─ return TRUE!
     * │       │       └─ return TRUE!
     * │       └─ return TRUE!
     * └─ return TRUE!
     * 
     * Result: true (placed at (1,0) and (1,1))
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example 1: 4×4 empty grid, N=4 → true")
    void testExample1() {
        List<List<Integer>> grid = Arrays.asList(
            Arrays.asList(0, 0, 0, 0),
            Arrays.asList(0, 0, 0, 0),
            Arrays.asList(0, 0, 0, 0),
            Arrays.asList(0, 0, 0, 0)
        );
        assertThat(canPlaceSecurityCameras(4, grid)).isTrue();
    }
    
    @Test
    @DisplayName("Example 2: 4×4 grid with obstacles, N=4 → true")
    void testExample2() {
        List<List<Integer>> grid = Arrays.asList(
            Arrays.asList(0, 1, 0, 0),
            Arrays.asList(0, 0, 0, 1),
            Arrays.asList(1, 0, 0, 0),
            Arrays.asList(0, 0, 1, 0)
        );
        assertThat(canPlaceSecurityCameras(4, grid)).isTrue();
    }
    
    @Test
    @DisplayName("Example 3: 4×4 all blocked → false")
    void testAllBlocked() {
        List<List<Integer>> grid = Arrays.asList(
            Arrays.asList(1, 1, 1, 1),
            Arrays.asList(1, 1, 1, 1),
            Arrays.asList(1, 1, 1, 1),
            Arrays.asList(1, 1, 1, 1)
        );
        assertThat(canPlaceSecurityCameras(4, grid)).isFalse();
    }
    
    @Test
    @DisplayName("Edge case: N=0 → true")
    void testZero() {
        List<List<Integer>> grid = Arrays.asList(
            Arrays.asList(0, 0)
        );
        assertThat(canPlaceSecurityCameras(0, grid)).isTrue();
    }
    
    @Test
    @DisplayName("Edge case: N=1 (1×1 empty) → true")
    void testOneByOne() {
        List<List<Integer>> grid = Arrays.asList(
            Arrays.asList(0)
        );
        assertThat(canPlaceSecurityCameras(1, grid)).isTrue();
    }
    
    @Test
    @DisplayName("Edge case: N=2 (2×2 empty) → true")
    void testTwoByTwo() {
        List<List<Integer>> grid = Arrays.asList(
            Arrays.asList(0, 0),
            Arrays.asList(0, 0)
        );
        assertThat(canPlaceSecurityCameras(2, grid)).isTrue();
    }
    
    @Test
    @DisplayName("Conflict: 2×2 with diagonal block → false")
    void testDiagonalConflict() {
        // Both valid positions (0,0) and (1,1) are on same diagonal!
        List<List<Integer>> grid = Arrays.asList(
            Arrays.asList(0, 1),
            Arrays.asList(1, 0)
        );
        assertThat(canPlaceSecurityCameras(2, grid)).isFalse();
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // COMPLEXITY ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * TIME COMPLEXITY: O(2^(m*n))
     * 
     * Why?
     * - For each cell, we have 2 choices: SKIP or PLACE
     * - Total cells = m * n
     * - Total combinations = 2^(m*n)
     * 
     * Example: 3×3 grid = 9 cells → 2^9 = 512 combinations
     * 
     * Optimization: Early termination when remaining = 0
     * 
     * SPACE COMPLEXITY: O(m*n)
     * - Recursion depth = m*n (worst case: iterate through all cells)
     * - Grid is modified in-place (no extra space)
     */
}
