package com.practice.leetcode.graph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Number of Islands problem.
 */
class NumberOfIslandsSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: DFS Flood Fill (Lan tỏa đổ màu)
   *
   * Bài toán: Đếm số đảo. Đảo là vùng đất liền '1' nối nhau theo 4 hướng.
   *
   * Ý tưởng:
   * 1. Duyệt qua từng ô trong grid
   * 2. Khi gặp ô đất '1' → Tìm thấy đảo mới → count++
   * 3. Dùng DFS "nhấn chìm" toàn bộ đảo (đổi '1' thành '0')
   * để không đếm lại
   *
   * Ví dụ:
   * Grid: Sau DFS từ (0,0):
   * 1 1 0 0 0 0 0 0 0 0 (đảo 1 bị nhấn chìm)
   * 1 1 0 0 0 → 0 0 0 0 0
   * 0 0 1 0 0 0 0 1 0 0 (đảo 2 chưa xét)
   * 0 0 0 1 1 0 0 0 1 1 (đảo 3 chưa xét)
   *
   * Tiếp tục duyệt, gặp (2,2)='1' → count=2, DFS nhấn chìm
   * Tiếp tục duyệt, gặp (3,3)='1' → count=3, DFS nhấn chìm
   *
   * Kết quả: 3 đảo
   *
   * DFS đệ quy:
   * - Base case: Ra ngoài biên hoặc gặp nước → return
   * - Đánh dấu ô hiện tại = '0'
   * - Gọi đệ quy 4 hướng: trên, dưới, trái, phải
   *
   * Time: O(m×n) - mỗi ô được thăm tối đa 1 lần
   * Space: O(m×n) - độ sâu đệ quy trong trường hợp xấu nhất
   */
  static class ImperativeSolution {
    public int numIslands(char[][] grid) {
      if (grid == null || grid.length == 0) {
        return 0;
      }

      int count = 0;
      int rows = grid.length;
      int cols = grid[0].length;

      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          if (grid[i][j] == '1') {
            count++;
            dfs(grid, i, j);
          }
        }
      }

      return count;
    }

    private void dfs(char[][] grid, int i, int j) {
      if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != '1') {
        return;
      }

      grid[i][j] = '0'; // Mark as visited
      dfs(grid, i + 1, j);
      dfs(grid, i - 1, j);
      dfs(grid, i, j + 1);
      dfs(grid, i, j - 1);
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    private static final int[][] DIRECTIONS = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

    /**
     * BFS approach with queue.
     * Time Complexity: O(m * n)
     * Space Complexity: O(min(m, n)) for queue
     */
    public int numIslands(char[][] grid) {
      if (grid == null || grid.length == 0) {
        return 0;
      }

      int rows = grid.length;
      int cols = grid[0].length;

      return (int) IntStream.range(0, rows)
          .flatMap(i -> IntStream.range(0, cols)
              .filter(j -> grid[i][j] == '1')
              .peek(j -> bfs(grid, i, j)))
          .count();
    }

    private void bfs(char[][] grid, int startRow, int startCol) {
      Queue<int[]> queue = new LinkedList<>();
      queue.offer(new int[] { startRow, startCol });
      grid[startRow][startCol] = '0';

      while (!queue.isEmpty()) {
        int[] cell = queue.poll();
        for (int[] dir : DIRECTIONS) {
          int newRow = cell[0] + dir[0];
          int newCol = cell[1] + dir[1];

          if (newRow >= 0 && newRow < grid.length &&
              newCol >= 0 && newCol < grid[0].length &&
              grid[newRow][newCol] == '1') {
            grid[newRow][newCol] = '0';
            queue.offer(new int[] { newRow, newCol });
          }
        }
      }
    }
  }

  // ========== TESTS ==========

  private char[][] copyGrid(char[][] grid) {
    char[][] copy = new char[grid.length][];
    for (int i = 0; i < grid.length; i++) {
      copy[i] = grid[i].clone();
    }
    return copy;
  }

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {
    private final ImperativeSolution solution = new ImperativeSolution();

    @Test
    @DisplayName("Example 1: Single island -> 1")
    void testExample1() {
      char[][] grid = {
          { '1', '1', '1', '1', '0' },
          { '1', '1', '0', '1', '0' },
          { '1', '1', '0', '0', '0' },
          { '0', '0', '0', '0', '0' }
      };
      assertThat(solution.numIslands(grid)).isEqualTo(1);
    }

    @Test
    @DisplayName("Example 2: Three islands -> 3")
    void testExample2() {
      char[][] grid = {
          { '1', '1', '0', '0', '0' },
          { '1', '1', '0', '0', '0' },
          { '0', '0', '1', '0', '0' },
          { '0', '0', '0', '1', '1' }
      };
      assertThat(solution.numIslands(grid)).isEqualTo(3);
    }

    @Test
    @DisplayName("Empty grid")
    void testEmptyGrid() {
      assertThat(solution.numIslands(new char[][] {})).isEqualTo(0);
    }

    @Test
    @DisplayName("All water")
    void testAllWater() {
      char[][] grid = { { '0', '0' }, { '0', '0' } };
      assertThat(solution.numIslands(grid)).isEqualTo(0);
    }

    @Test
    @DisplayName("All land")
    void testAllLand() {
      char[][] grid = { { '1', '1' }, { '1', '1' } };
      assertThat(solution.numIslands(grid)).isEqualTo(1);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {
    private final FunctionalSolution solution = new FunctionalSolution();

    @Test
    @DisplayName("Example 1: Single island -> 1")
    void testExample1() {
      char[][] grid = {
          { '1', '1', '1', '1', '0' },
          { '1', '1', '0', '1', '0' },
          { '1', '1', '0', '0', '0' },
          { '0', '0', '0', '0', '0' }
      };
      assertThat(solution.numIslands(grid)).isEqualTo(1);
    }

    @Test
    @DisplayName("Example 2: Three islands -> 3")
    void testExample2() {
      char[][] grid = {
          { '1', '1', '0', '0', '0' },
          { '1', '1', '0', '0', '0' },
          { '0', '0', '1', '0', '0' },
          { '0', '0', '0', '1', '1' }
      };
      assertThat(solution.numIslands(grid)).isEqualTo(3);
    }

    @Test
    @DisplayName("Empty grid")
    void testEmptyGrid() {
      assertThat(solution.numIslands(new char[][] {})).isEqualTo(0);
    }

    @Test
    @DisplayName("All water")
    void testAllWater() {
      char[][] grid = { { '0', '0' }, { '0', '0' } };
      assertThat(solution.numIslands(grid)).isEqualTo(0);
    }

    @Test
    @DisplayName("All land")
    void testAllLand() {
      char[][] grid = { { '1', '1' }, { '1', '1' } };
      assertThat(solution.numIslands(grid)).isEqualTo(1);
    }
  }
}
