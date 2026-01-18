package com.practice.leetcode.graph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Course Schedule problem.
 */
class CourseScheduleSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * DFS-based cycle detection.
     * Time Complexity: O(V + E)
     * Space Complexity: O(V + E)
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
      List<List<Integer>> graph = new ArrayList<>();
      for (int i = 0; i < numCourses; i++) {
        graph.add(new ArrayList<>());
      }

      for (int[] prereq : prerequisites) {
        graph.get(prereq[1]).add(prereq[0]);
      }

      // 0 = unvisited, 1 = visiting, 2 = visited
      int[] state = new int[numCourses];

      for (int i = 0; i < numCourses; i++) {
        if (hasCycle(graph, i, state)) {
          return false;
        }
      }

      return true;
    }

    private boolean hasCycle(List<List<Integer>> graph, int course, int[] state) {
      if (state[course] == 1)
        return true; // Cycle detected
      if (state[course] == 2)
        return false; // Already processed

      state[course] = 1; // Mark as visiting

      for (int next : graph.get(course)) {
        if (hasCycle(graph, next, state)) {
          return true;
        }
      }

      state[course] = 2; // Mark as visited
      return false;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Kahn's algorithm (BFS-based topological sort).
     * Time Complexity: O(V + E)
     * Space Complexity: O(V + E)
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
      List<List<Integer>> graph = new ArrayList<>();
      int[] inDegree = new int[numCourses];

      IntStream.range(0, numCourses).forEach(i -> graph.add(new ArrayList<>()));

      Arrays.stream(prerequisites).forEach(prereq -> {
        graph.get(prereq[1]).add(prereq[0]);
        inDegree[prereq[0]]++;
      });

      Queue<Integer> queue = new LinkedList<>();
      IntStream.range(0, numCourses)
          .filter(i -> inDegree[i] == 0)
          .forEach(queue::offer);

      int processed = 0;
      while (!queue.isEmpty()) {
        int course = queue.poll();
        processed++;

        for (int next : graph.get(course)) {
          inDegree[next]--;
          if (inDegree[next] == 0) {
            queue.offer(next);
          }
        }
      }

      return processed == numCourses;
    }
  }

  // ========== TESTS ==========

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {
    private final ImperativeSolution solution = new ImperativeSolution();

    @Test
    @DisplayName("Example 1: numCourses=2, [[1,0]] -> true")
    void testExample1() {
      assertThat(solution.canFinish(2, new int[][] { { 1, 0 } })).isTrue();
    }

    @Test
    @DisplayName("Example 2: numCourses=2, [[1,0],[0,1]] -> false (cycle)")
    void testExample2() {
      assertThat(solution.canFinish(2, new int[][] { { 1, 0 }, { 0, 1 } })).isFalse();
    }

    @Test
    @DisplayName("No prerequisites")
    void testNoPrerequisites() {
      assertThat(solution.canFinish(3, new int[][] {})).isTrue();
    }

    @Test
    @DisplayName("Linear chain")
    void testLinearChain() {
      assertThat(solution.canFinish(4, new int[][] { { 1, 0 }, { 2, 1 }, { 3, 2 } })).isTrue();
    }

    @Test
    @DisplayName("Single course")
    void testSingleCourse() {
      assertThat(solution.canFinish(1, new int[][] {})).isTrue();
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {
    private final FunctionalSolution solution = new FunctionalSolution();

    @Test
    @DisplayName("Example 1: numCourses=2, [[1,0]] -> true")
    void testExample1() {
      assertThat(solution.canFinish(2, new int[][] { { 1, 0 } })).isTrue();
    }

    @Test
    @DisplayName("Example 2: numCourses=2, [[1,0],[0,1]] -> false (cycle)")
    void testExample2() {
      assertThat(solution.canFinish(2, new int[][] { { 1, 0 }, { 0, 1 } })).isFalse();
    }

    @Test
    @DisplayName("No prerequisites")
    void testNoPrerequisites() {
      assertThat(solution.canFinish(3, new int[][] {})).isTrue();
    }

    @Test
    @DisplayName("Linear chain")
    void testLinearChain() {
      assertThat(solution.canFinish(4, new int[][] { { 1, 0 }, { 2, 1 }, { 3, 2 } })).isTrue();
    }

    @Test
    @DisplayName("Single course")
    void testSingleCourse() {
      assertThat(solution.canFinish(1, new int[][] {})).isTrue();
    }
  }
}
