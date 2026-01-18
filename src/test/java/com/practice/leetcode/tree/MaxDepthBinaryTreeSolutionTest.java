package com.practice.leetcode.tree;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Maximum Depth of Binary Tree problem.
 */
class MaxDepthBinaryTreeSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * BFS approach using queue.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int maxDepth(TreeNode root) {
      if (root == null) {
        return 0;
      }

      Queue<TreeNode> queue = new LinkedList<>();
      queue.offer(root);
      int depth = 0;

      while (!queue.isEmpty()) {
        int levelSize = queue.size();
        depth++;

        for (int i = 0; i < levelSize; i++) {
          TreeNode node = queue.poll();
          if (node.left != null)
            queue.offer(node.left);
          if (node.right != null)
            queue.offer(node.right);
        }
      }

      return depth;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Recursive DFS approach (functional style).
     * Time Complexity: O(n)
     * Space Complexity: O(h) where h is height
     */
    public int maxDepth(TreeNode root) {
      return Optional.ofNullable(root)
          .map(node -> 1 + Math.max(maxDepth(node.left), maxDepth(node.right)))
          .orElse(0);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [3,9,20,null,null,15,7] -> 3")
    void testExample1() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 3, 9, 20, null, null, 15, 7 });
      assertThat(imperativeSolution.maxDepth(root)).isEqualTo(3);
    }

    @Test
    @DisplayName("Example 2: [1,null,2] -> 2")
    void testExample2() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 1, null, 2 });
      assertThat(imperativeSolution.maxDepth(root)).isEqualTo(2);
    }

    @Test
    @DisplayName("Empty tree")
    void testEmptyTree() {
      assertThat(imperativeSolution.maxDepth(null)).isEqualTo(0);
    }

    @Test
    @DisplayName("Single node")
    void testSingleNode() {
      TreeNode root = new TreeNode(1);
      assertThat(imperativeSolution.maxDepth(root)).isEqualTo(1);
    }

    @Test
    @DisplayName("Left-skewed tree")
    void testLeftSkewed() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 1, 2, null, 3, null });
      assertThat(imperativeSolution.maxDepth(root)).isEqualTo(3);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [3,9,20,null,null,15,7] -> 3")
    void testExample1() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 3, 9, 20, null, null, 15, 7 });
      assertThat(functionalSolution.maxDepth(root)).isEqualTo(3);
    }

    @Test
    @DisplayName("Example 2: [1,null,2] -> 2")
    void testExample2() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 1, null, 2 });
      assertThat(functionalSolution.maxDepth(root)).isEqualTo(2);
    }

    @Test
    @DisplayName("Empty tree")
    void testEmptyTree() {
      assertThat(functionalSolution.maxDepth(null)).isEqualTo(0);
    }

    @Test
    @DisplayName("Single node")
    void testSingleNode() {
      TreeNode root = new TreeNode(1);
      assertThat(functionalSolution.maxDepth(root)).isEqualTo(1);
    }

    @Test
    @DisplayName("Left-skewed tree")
    void testLeftSkewed() {
      TreeNode root = TreeNode.fromArray(new Integer[] { 1, 2, null, 3, null });
      assertThat(functionalSolution.maxDepth(root)).isEqualTo(3);
    }
  }
}
