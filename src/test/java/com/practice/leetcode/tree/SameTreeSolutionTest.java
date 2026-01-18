package com.practice.leetcode.tree;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Same Tree problem.
 */
class SameTreeSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * BFS approach comparing nodes level by level.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
      Queue<TreeNode> queue = new LinkedList<>();
      queue.offer(p);
      queue.offer(q);

      while (!queue.isEmpty()) {
        TreeNode node1 = queue.poll();
        TreeNode node2 = queue.poll();

        if (node1 == null && node2 == null)
          continue;
        if (node1 == null || node2 == null)
          return false;
        if (node1.val != node2.val)
          return false;

        queue.offer(node1.left);
        queue.offer(node2.left);
        queue.offer(node1.right);
        queue.offer(node2.right);
      }

      return true;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Recursive approach (functional style).
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
      if (p == null && q == null)
        return true;
      if (p == null || q == null)
        return false;

      return p.val == q.val
          && isSameTree(p.left, q.left)
          && isSameTree(p.right, q.right);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [1,2,3], [1,2,3] -> true")
    void testExample1() {
      TreeNode p = TreeNode.fromArray(new Integer[] { 1, 2, 3 });
      TreeNode q = TreeNode.fromArray(new Integer[] { 1, 2, 3 });
      assertThat(imperativeSolution.isSameTree(p, q)).isTrue();
    }

    @Test
    @DisplayName("Example 2: [1,2], [1,null,2] -> false")
    void testExample2() {
      TreeNode p = TreeNode.fromArray(new Integer[] { 1, 2 });
      TreeNode q = TreeNode.fromArray(new Integer[] { 1, null, 2 });
      assertThat(imperativeSolution.isSameTree(p, q)).isFalse();
    }

    @Test
    @DisplayName("Example 3: [1,2,1], [1,1,2] -> false")
    void testExample3() {
      TreeNode p = TreeNode.fromArray(new Integer[] { 1, 2, 1 });
      TreeNode q = TreeNode.fromArray(new Integer[] { 1, 1, 2 });
      assertThat(imperativeSolution.isSameTree(p, q)).isFalse();
    }

    @Test
    @DisplayName("Both empty")
    void testBothEmpty() {
      assertThat(imperativeSolution.isSameTree(null, null)).isTrue();
    }

    @Test
    @DisplayName("One empty")
    void testOneEmpty() {
      TreeNode p = new TreeNode(1);
      assertThat(imperativeSolution.isSameTree(p, null)).isFalse();
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [1,2,3], [1,2,3] -> true")
    void testExample1() {
      TreeNode p = TreeNode.fromArray(new Integer[] { 1, 2, 3 });
      TreeNode q = TreeNode.fromArray(new Integer[] { 1, 2, 3 });
      assertThat(functionalSolution.isSameTree(p, q)).isTrue();
    }

    @Test
    @DisplayName("Example 2: [1,2], [1,null,2] -> false")
    void testExample2() {
      TreeNode p = TreeNode.fromArray(new Integer[] { 1, 2 });
      TreeNode q = TreeNode.fromArray(new Integer[] { 1, null, 2 });
      assertThat(functionalSolution.isSameTree(p, q)).isFalse();
    }

    @Test
    @DisplayName("Example 3: [1,2,1], [1,1,2] -> false")
    void testExample3() {
      TreeNode p = TreeNode.fromArray(new Integer[] { 1, 2, 1 });
      TreeNode q = TreeNode.fromArray(new Integer[] { 1, 1, 2 });
      assertThat(functionalSolution.isSameTree(p, q)).isFalse();
    }

    @Test
    @DisplayName("Both empty")
    void testBothEmpty() {
      assertThat(functionalSolution.isSameTree(null, null)).isTrue();
    }

    @Test
    @DisplayName("One empty")
    void testOneEmpty() {
      TreeNode p = new TreeNode(1);
      assertThat(functionalSolution.isSameTree(p, null)).isFalse();
    }
  }
}
