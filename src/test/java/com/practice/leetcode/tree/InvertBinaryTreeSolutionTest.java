package com.practice.leetcode.tree;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Invert Binary Tree problem.
 */
class InvertBinaryTreeSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * BFS approach, swap left and right at each node.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public TreeNode invertTree(TreeNode root) {
      if (root == null) {
        return null;
      }

      Queue<TreeNode> queue = new LinkedList<>();
      queue.offer(root);

      while (!queue.isEmpty()) {
        TreeNode node = queue.poll();

        // Swap children
        TreeNode temp = node.left;
        node.left = node.right;
        node.right = temp;

        if (node.left != null)
          queue.offer(node.left);
        if (node.right != null)
          queue.offer(node.right);
      }

      return root;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Recursive approach (functional style).
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public TreeNode invertTree(TreeNode root) {
      return Optional.ofNullable(root)
          .map(node -> {
            TreeNode left = invertTree(node.right);
            TreeNode right = invertTree(node.left);
            node.left = left;
            node.right = right;
            return node;
          })
          .orElse(null);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [4,2,7,1,3,6,9] -> [4,7,2,9,6,3,1]")
    void testExample1() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 4, 2, 7, 1, 3, 6, 9 });
      TreeNode result = imperativeSolution.invertTree(root);
      assertThat(TreeNode.toList(result)).containsExactly(4, 7, 2, 9, 6, 3, 1);
    }

    @Test
    @DisplayName("Example 2: [2,1,3] -> [2,3,1]")
    void testExample2() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 2, 1, 3 });
      TreeNode result = imperativeSolution.invertTree(root);
      assertThat(TreeNode.toList(result)).containsExactly(2, 3, 1);
    }

    @Test
    @DisplayName("Empty tree")
    void testEmptyTree() {
      assertThat(imperativeSolution.invertTree(null)).isNull();
    }

    @Test
    @DisplayName("Single node")
    void testSingleNode() {
      TreeNode root = new TreeNode(1);
      TreeNode result = imperativeSolution.invertTree(root);
      assertThat(result.val).isEqualTo(1);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [4,2,7,1,3,6,9] -> [4,7,2,9,6,3,1]")
    void testExample1() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 4, 2, 7, 1, 3, 6, 9 });
      TreeNode result = functionalSolution.invertTree(root);
      assertThat(TreeNode.toList(result)).containsExactly(4, 7, 2, 9, 6, 3, 1);
    }

    @Test
    @DisplayName("Example 2: [2,1,3] -> [2,3,1]")
    void testExample2() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 2, 1, 3 });
      TreeNode result = functionalSolution.invertTree(root);
      assertThat(TreeNode.toList(result)).containsExactly(2, 3, 1);
    }

    @Test
    @DisplayName("Empty tree")
    void testEmptyTree() {
      assertThat(functionalSolution.invertTree(null)).isNull();
    }

    @Test
    @DisplayName("Single node")
    void testSingleNode() {
      TreeNode root = new TreeNode(1);
      TreeNode result = functionalSolution.invertTree(root);
      assertThat(result.val).isEqualTo(1);
    }
  }
}
