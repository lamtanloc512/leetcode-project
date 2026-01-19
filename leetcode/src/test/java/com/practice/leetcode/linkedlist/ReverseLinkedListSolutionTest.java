package com.practice.leetcode.linkedlist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Reverse Linked List problem.
 */
class ReverseLinkedListSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Iterative approach with three pointers.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public ListNode reverseList(ListNode head) {
      ListNode prev = null;
      ListNode current = head;

      while (current != null) {
        ListNode nextTemp = current.next;
        current.next = prev;
        prev = current;
        current = nextTemp;
      }

      return prev;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Recursive approach (more functional in nature).
     * Time Complexity: O(n)
     * Space Complexity: O(n) - due to recursion stack
     */
    public ListNode reverseList(ListNode head) {
      return reverseHelper(head, null);
    }

    private ListNode reverseHelper(ListNode current, ListNode prev) {
      if (current == null) {
        return prev;
      }
      ListNode nextTemp = current.next;
      current.next = prev;
      return reverseHelper(nextTemp, current);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [1,2,3,4,5] -> [5,4,3,2,1]")
    void testExample1() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2, 3, 4, 5 });
      ListNode result = imperativeSolution.reverseList(head);
      assertThat(ListNode.toArray(result)).containsExactly(5, 4, 3, 2, 1);
    }

    @Test
    @DisplayName("Example 2: [1,2] -> [2,1]")
    void testExample2() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2 });
      ListNode result = imperativeSolution.reverseList(head);
      assertThat(ListNode.toArray(result)).containsExactly(2, 1);
    }

    @Test
    @DisplayName("Example 3: [] -> []")
    void testExample3() {
      ListNode result = imperativeSolution.reverseList(null);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Single element")
    void testSingleElement() {
      ListNode head = ListNode.fromArray(new int[] { 1 });
      ListNode result = imperativeSolution.reverseList(head);
      assertThat(ListNode.toArray(result)).containsExactly(1);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [1,2,3,4,5] -> [5,4,3,2,1]")
    void testExample1() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2, 3, 4, 5 });
      ListNode result = functionalSolution.reverseList(head);
      assertThat(ListNode.toArray(result)).containsExactly(5, 4, 3, 2, 1);
    }

    @Test
    @DisplayName("Example 2: [1,2] -> [2,1]")
    void testExample2() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2 });
      ListNode result = functionalSolution.reverseList(head);
      assertThat(ListNode.toArray(result)).containsExactly(2, 1);
    }

    @Test
    @DisplayName("Example 3: [] -> []")
    void testExample3() {
      ListNode result = functionalSolution.reverseList(null);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Single element")
    void testSingleElement() {
      ListNode head = ListNode.fromArray(new int[] { 1 });
      ListNode result = functionalSolution.reverseList(head);
      assertThat(ListNode.toArray(result)).containsExactly(1);
    }
  }
}
