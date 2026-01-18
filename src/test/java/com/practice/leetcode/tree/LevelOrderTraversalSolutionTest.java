package com.practice.leetcode.tree;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Binary Tree Level Order Traversal problem.
 */
class LevelOrderTraversalSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * BFS approach using queue.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
      List<List<Integer>> result = new ArrayList<>();

      if (root == null) {
        return result;
      }

      Queue<TreeNode> queue = new LinkedList<>();
      queue.offer(root);

      while (!queue.isEmpty()) {
        int levelSize = queue.size();
        List<Integer> level = new ArrayList<>();

        for (int i = 0; i < levelSize; i++) {
          TreeNode node = queue.poll();
          level.add(node.val);

          if (node.left != null)
            queue.offer(node.left);
          if (node.right != null)
            queue.offer(node.right);
        }

        result.add(level);
      }

      return result;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Recursive DFS approach with level tracking.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
      List<List<Integer>> result = new ArrayList<>();
      traverse(root, 0, result);
      return result;
    }

    private void traverse(TreeNode node, int level, List<List<Integer>> result) {
      if (node == null)
        return;

      // Expand result list if needed
      if (level >= result.size()) {
        result.add(new ArrayList<>());
      }

      result.get(level).add(node.val);
      traverse(node.left, level + 1, result);
      traverse(node.right, level + 1, result);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [3,9,20,null,null,15,7] -> [[3],[9,20],[15,7]]")
    void testExample1() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 3, 9, 20, null, null, 15, 7 });
      List<List<Integer>> result = imperativeSolution.levelOrder(root);

      assertThat(result).hasSize(3);
      assertThat(result.get(0)).containsExactly(3);
      assertThat(result.get(1)).containsExactly(9, 20);
      assertThat(result.get(2)).containsExactly(15, 7);
    }

    @Test
    @DisplayName("Example 2: [1] -> [[1]]")
    void testExample2() {
      TreeNode root = new TreeNode(1);
      List<List<Integer>> result = imperativeSolution.levelOrder(root);

      assertThat(result).hasSize(1);
      assertThat(result.get(0)).containsExactly(1);
    }

    @Test
    @DisplayName("Example 3: [] -> []")
    void testExample3() {
      List<List<Integer>> result = imperativeSolution.levelOrder(null);
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Left-skewed tree")
    void testLeftSkewed() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 1, 2, null, 3 });
      List<List<Integer>> result = imperativeSolution.levelOrder(root);

      assertThat(result).hasSize(3);
      assertThat(result.get(0)).containsExactly(1);
      assertThat(result.get(1)).containsExactly(2);
      assertThat(result.get(2)).containsExactly(3);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [3,9,20,null,null,15,7] -> [[3],[9,20],[15,7]]")
    void testExample1() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 3, 9, 20, null, null, 15, 7 });
      List<List<Integer>> result = functionalSolution.levelOrder(root);

      assertThat(result).hasSize(3);
      assertThat(result.get(0)).containsExactly(3);
      assertThat(result.get(1)).containsExactly(9, 20);
      assertThat(result.get(2)).containsExactly(15, 7);
    }

    @Test
    @DisplayName("Example 2: [1] -> [[1]]")
    void testExample2() {
      TreeNode root = new TreeNode(1);
      List<List<Integer>> result = functionalSolution.levelOrder(root);

      assertThat(result).hasSize(1);
      assertThat(result.get(0)).containsExactly(1);
    }

    @Test
    @DisplayName("Example 3: [] -> []")
    void testExample3() {
      List<List<Integer>> result = functionalSolution.levelOrder(null);
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Left-skewed tree")
    void testLeftSkewed() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 1, 2, null, 3 });
      List<List<Integer>> result = functionalSolution.levelOrder(root);

      assertThat(result).hasSize(3);
      assertThat(result.get(0)).containsExactly(1);
      assertThat(result.get(1)).containsExactly(2);
      assertThat(result.get(2)).containsExactly(3);
    }
  }
}
