package com.practice.leetcode.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Definition for a binary tree node.
 * This class is used across all binary tree problems.
 */
public class TreeNode {
  public int val;
  public TreeNode left;
  public TreeNode right;

  public TreeNode() {
  }

  public TreeNode(int val) {
    this.val = val;
  }

  public TreeNode(int val, TreeNode left, TreeNode right) {
    this.val = val;
    this.left = left;
    this.right = right;
  }

  /**
   * Create a binary tree from level-order array representation.
   * Use null for missing nodes.
   *
   * @param values level-order array (null for missing nodes)
   * @return root of the binary tree
   */
  public static TreeNode fromArray(Integer[] values) {
    if (values == null || values.length == 0 || values[0] == null) {
      return null;
    }

    TreeNode root = new TreeNode(values[0]);
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    int i = 1;
    while (!queue.isEmpty() && i < values.length) {
      TreeNode current = queue.poll();

      if (i < values.length && values[i] != null) {
        current.left = new TreeNode(values[i]);
        queue.offer(current.left);
      }
      i++;

      if (i < values.length && values[i] != null) {
        current.right = new TreeNode(values[i]);
        queue.offer(current.right);
      }
      i++;
    }

    return root;
  }

  /**
   * Convert binary tree to level-order array representation.
   *
   * @param root root of the binary tree
   * @return level-order array representation
   */
  public static List<Integer> toList(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) {
      return result;
    }

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
      TreeNode current = queue.poll();
      if (current != null) {
        result.add(current.val);
        queue.offer(current.left);
        queue.offer(current.right);
      } else {
        result.add(null);
      }
    }

    // Remove trailing nulls
    while (!result.isEmpty() && result.get(result.size() - 1) == null) {
      result.remove(result.size() - 1);
    }

    return result;
  }

  @Override
  public String toString() {
    return "TreeNode{val=" + val + "}";
  }
}
