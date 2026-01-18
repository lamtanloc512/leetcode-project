package com.practice.leetcode.graph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Clone Graph problem.
 */
class CloneGraphSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * DFS with HashMap to track cloned nodes.
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     */
    public CloneGraph.Node cloneGraph(CloneGraph.Node node) {
      if (node == null) {
        return null;
      }

      Map<CloneGraph.Node, CloneGraph.Node> visited = new HashMap<>();
      return dfs(node, visited);
    }

    private CloneGraph.Node dfs(CloneGraph.Node node, Map<CloneGraph.Node, CloneGraph.Node> visited) {
      if (visited.containsKey(node)) {
        return visited.get(node);
      }

      CloneGraph.Node clone = new CloneGraph.Node(node.val);
      visited.put(node, clone);

      for (CloneGraph.Node neighbor : node.neighbors) {
        clone.neighbors.add(dfs(neighbor, visited));
      }

      return clone;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * BFS with HashMap to track cloned nodes.
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     */
    public CloneGraph.Node cloneGraph(CloneGraph.Node node) {
      if (node == null) {
        return null;
      }

      Map<CloneGraph.Node, CloneGraph.Node> visited = new HashMap<>();
      Queue<CloneGraph.Node> queue = new LinkedList<>();

      visited.put(node, new CloneGraph.Node(node.val));
      queue.offer(node);

      while (!queue.isEmpty()) {
        CloneGraph.Node current = queue.poll();
        CloneGraph.Node clonedCurrent = visited.get(current);

        current.neighbors.forEach(neighbor -> {
          if (!visited.containsKey(neighbor)) {
            visited.put(neighbor, new CloneGraph.Node(neighbor.val));
            queue.offer(neighbor);
          }
          clonedCurrent.neighbors.add(visited.get(neighbor));
        });
      }

      return visited.get(node);
    }
  }

  // ========== HELPER METHODS ==========

  private CloneGraph.Node createGraph(int[][] adjList) {
    if (adjList == null || adjList.length == 0) {
      return null;
    }

    List<CloneGraph.Node> nodes = new ArrayList<>();
    for (int i = 0; i < adjList.length; i++) {
      nodes.add(new CloneGraph.Node(i + 1));
    }

    for (int i = 0; i < adjList.length; i++) {
      for (int neighbor : adjList[i]) {
        nodes.get(i).neighbors.add(nodes.get(neighbor - 1));
      }
    }

    return nodes.get(0);
  }

  // ========== TESTS ==========

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {
    private final ImperativeSolution solution = new ImperativeSolution();

    @Test
    @DisplayName("Example 1: [[2,4],[1,3],[2,4],[1,3]]")
    void testExample1() {
      CloneGraph.Node original = createGraph(new int[][] { { 2, 4 }, { 1, 3 }, { 2, 4 }, { 1, 3 } });
      CloneGraph.Node clone = solution.cloneGraph(original);

      assertThat(clone).isNotNull();
      assertThat(clone).isNotSameAs(original);
      assertThat(clone.val).isEqualTo(1);
      assertThat(clone.neighbors).hasSize(2);
    }

    @Test
    @DisplayName("Single node with no neighbors")
    void testSingleNode() {
      CloneGraph.Node original = new CloneGraph.Node(1);
      CloneGraph.Node clone = solution.cloneGraph(original);

      assertThat(clone).isNotNull();
      assertThat(clone).isNotSameAs(original);
      assertThat(clone.val).isEqualTo(1);
      assertThat(clone.neighbors).isEmpty();
    }

    @Test
    @DisplayName("Null graph")
    void testNullGraph() {
      assertThat(solution.cloneGraph(null)).isNull();
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {
    private final FunctionalSolution solution = new FunctionalSolution();

    @Test
    @DisplayName("Example 1: [[2,4],[1,3],[2,4],[1,3]]")
    void testExample1() {
      CloneGraph.Node original = createGraph(new int[][] { { 2, 4 }, { 1, 3 }, { 2, 4 }, { 1, 3 } });
      CloneGraph.Node clone = solution.cloneGraph(original);

      assertThat(clone).isNotNull();
      assertThat(clone).isNotSameAs(original);
      assertThat(clone.val).isEqualTo(1);
      assertThat(clone.neighbors).hasSize(2);
    }

    @Test
    @DisplayName("Single node with no neighbors")
    void testSingleNode() {
      CloneGraph.Node original = new CloneGraph.Node(1);
      CloneGraph.Node clone = solution.cloneGraph(original);

      assertThat(clone).isNotNull();
      assertThat(clone).isNotSameAs(original);
      assertThat(clone.val).isEqualTo(1);
      assertThat(clone.neighbors).isEmpty();
    }

    @Test
    @DisplayName("Null graph")
    void testNullGraph() {
      assertThat(solution.cloneGraph(null)).isNull();
    }
  }
}
