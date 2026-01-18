package com.practice.leetcode.linkedlist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Remove Nth Node From End of List problem.
 */
class RemoveNthFromEndSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Two-pointer approach with n+1 gap.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
      ListNode dummy = new ListNode(0, head);
      ListNode first = dummy;
      ListNode second = dummy;

      // Advance first pointer n+1 steps
      for (int i = 0; i <= n; i++) {
        first = first.next;
      }

      // Move both until first reaches end
      while (first != null) {
        first = first.next;
        second = second.next;
      }

      // Remove the nth node
      second.next = second.next.next;

      return dummy.next;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Recursive approach to find and remove nth node from end.
     * Time Complexity: O(n)
     * Space Complexity: O(n) - due to recursion stack
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
      int[] position = { 0 }; // Using array to allow mutation in lambda
      return removeHelper(head, n, position);
    }

    private ListNode removeHelper(ListNode node, int n, int[] position) {
      if (node == null) {
        return null;
      }

      node.next = removeHelper(node.next, n, position);
      position[0]++;

      if (position[0] == n) {
        return node.next; // Skip this node
      }

      return node;
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [1,2,3,4,5], n=2 -> [1,2,3,5]")
    void testExample1() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2, 3, 4, 5 });
      ListNode result = imperativeSolution.removeNthFromEnd(head, 2);
      assertThat(ListNode.toArray(result)).containsExactly(1, 2, 3, 5);
    }

    @Test
    @DisplayName("Example 2: [1], n=1 -> []")
    void testExample2() {
      ListNode head = ListNode.fromArray(new int[] { 1 });
      ListNode result = imperativeSolution.removeNthFromEnd(head, 1);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Example 3: [1,2], n=1 -> [1]")
    void testExample3() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2 });
      ListNode result = imperativeSolution.removeNthFromEnd(head, 1);
      assertThat(ListNode.toArray(result)).containsExactly(1);
    }

    @Test
    @DisplayName("Remove first node: [1,2], n=2 -> [2]")
    void testRemoveFirst() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2 });
      ListNode result = imperativeSolution.removeNthFromEnd(head, 2);
      assertThat(ListNode.toArray(result)).containsExactly(2);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [1,2,3,4,5], n=2 -> [1,2,3,5]")
    void testExample1() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2, 3, 4, 5 });
      ListNode result = functionalSolution.removeNthFromEnd(head, 2);
      assertThat(ListNode.toArray(result)).containsExactly(1, 2, 3, 5);
    }

    @Test
    @DisplayName("Example 2: [1], n=1 -> []")
    void testExample2() {
      ListNode head = ListNode.fromArray(new int[] { 1 });
      ListNode result = functionalSolution.removeNthFromEnd(head, 1);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Example 3: [1,2], n=1 -> [1]")
    void testExample3() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2 });
      ListNode result = functionalSolution.removeNthFromEnd(head, 1);
      assertThat(ListNode.toArray(result)).containsExactly(1);
    }

    @Test
    @DisplayName("Remove first node: [1,2], n=2 -> [2]")
    void testRemoveFirst() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2 });
      ListNode result = functionalSolution.removeNthFromEnd(head, 2);
      assertThat(ListNode.toArray(result)).containsExactly(2);
    }
  }
}
