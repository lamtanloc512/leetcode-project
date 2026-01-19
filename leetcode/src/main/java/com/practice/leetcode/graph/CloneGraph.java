package com.practice.leetcode.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Clone Graph
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/clone-graph/
 *
 * Description:
 * Given a reference of a node in a connected undirected graph.
 * Return a deep copy (clone) of the graph.
 * 
 * Each node in the graph contains a value (int) and a list (List[Node]) of
 * its neighbors.
 *
 * Test case format:
 * For simplicity, each node's value is the same as the node's index
 * (1-indexed). For example, the first node with val == 1, the second node
 * with val == 2, and so on.
 *
 * Examples:
 * Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
 * Output: [[2,4],[1,3],[2,4],[1,3]]
 * Explanation: There are 4 nodes in the graph.
 *
 * Constraints:
 * - The number of nodes in the graph is in the range [0, 100].
 * - 1 <= Node.val <= 100
 * - Node.val is unique for each node.
 * - There are no repeated edges and no self-loops in the graph.
 * - The Graph is connected and all nodes can be visited starting from the
 * given node.
 */
public class CloneGraph {

  // Definition for a Node
  public static class Node {
    public int val;
    public List<Node> neighbors;

    public Node() {
      val = 0;
      neighbors = new ArrayList<>();
    }

    public Node(int val) {
      this.val = val;
      neighbors = new ArrayList<>();
    }

    public Node(int val, ArrayList<Node> neighbors) {
      this.val = val;
      this.neighbors = neighbors;
    }
  }

  /**
   * Clone the graph.
   *
   * @param node a node in the graph
   * @return a deep copy of the graph
   */
  public Node cloneGraph(Node node) {
    throw new UnsupportedOperationException("Implement this method");
  }
}
