package com.practice.leetcode.linkedlist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Merge Two Sorted Lists problem.
 */
class MergeTwoSortedListsSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Iterative approach with dummy head.
     * Time Complexity: O(n + m)
     * Space Complexity: O(1)
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
      ListNode dummy = new ListNode(0);
      ListNode current = dummy;

      while (list1 != null && list2 != null) {
        if (list1.val <= list2.val) {
          current.next = list1;
          list1 = list1.next;
        } else {
          current.next = list2;
          list2 = list2.next;
        }
        current = current.next;
      }

      // Attach remaining nodes
      current.next = (list1 != null) ? list1 : list2;

      return dummy.next;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Recursive approach (more functional in nature).
     * Time Complexity: O(n + m)
     * Space Complexity: O(n + m) - due to recursion stack
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
      if (list1 == null)
        return list2;
      if (list2 == null)
        return list1;

      if (list1.val <= list2.val) {
        list1.next = mergeTwoLists(list1.next, list2);
        return list1;
      } else {
        list2.next = mergeTwoLists(list1, list2.next);
        return list2;
      }
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [1,2,4] + [1,3,4] -> [1,1,2,3,4,4]")
    void testExample1() {
      ListNode list1 = ListNode.fromArray(new int[] { 1, 2, 4 });
      ListNode list2 = ListNode.fromArray(new int[] { 1, 3, 4 });
      ListNode result = imperativeSolution.mergeTwoLists(list1, list2);
      assertThat(ListNode.toArray(result)).containsExactly(1, 1, 2, 3, 4, 4);
    }

    @Test
    @DisplayName("Example 2: [] + [] -> []")
    void testExample2() {
      ListNode result = imperativeSolution.mergeTwoLists(null, null);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Example 3: [] + [0] -> [0]")
    void testExample3() {
      ListNode list2 = ListNode.fromArray(new int[] { 0 });
      ListNode result = imperativeSolution.mergeTwoLists(null, list2);
      assertThat(ListNode.toArray(result)).containsExactly(0);
    }

    @Test
    @DisplayName("First list empty")
    void testFirstEmpty() {
      ListNode list2 = ListNode.fromArray(new int[] { 1, 2, 3 });
      ListNode result = imperativeSolution.mergeTwoLists(null, list2);
      assertThat(ListNode.toArray(result)).containsExactly(1, 2, 3);
    }

    @Test
    @DisplayName("Second list empty")
    void testSecondEmpty() {
      ListNode list1 = ListNode.fromArray(new int[] { 1, 2, 3 });
      ListNode result = imperativeSolution.mergeTwoLists(list1, null);
      assertThat(ListNode.toArray(result)).containsExactly(1, 2, 3);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [1,2,4] + [1,3,4] -> [1,1,2,3,4,4]")
    void testExample1() {
      ListNode list1 = ListNode.fromArray(new int[] { 1, 2, 4 });
      ListNode list2 = ListNode.fromArray(new int[] { 1, 3, 4 });
      ListNode result = functionalSolution.mergeTwoLists(list1, list2);
      assertThat(ListNode.toArray(result)).containsExactly(1, 1, 2, 3, 4, 4);
    }

    @Test
    @DisplayName("Example 2: [] + [] -> []")
    void testExample2() {
      ListNode result = functionalSolution.mergeTwoLists(null, null);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Example 3: [] + [0] -> [0]")
    void testExample3() {
      ListNode list2 = ListNode.fromArray(new int[] { 0 });
      ListNode result = functionalSolution.mergeTwoLists(null, list2);
      assertThat(ListNode.toArray(result)).containsExactly(0);
    }

    @Test
    @DisplayName("First list empty")
    void testFirstEmpty() {
      ListNode list2 = ListNode.fromArray(new int[] { 1, 2, 3 });
      ListNode result = functionalSolution.mergeTwoLists(null, list2);
      assertThat(ListNode.toArray(result)).containsExactly(1, 2, 3);
    }

    @Test
    @DisplayName("Second list empty")
    void testSecondEmpty() {
      ListNode list1 = ListNode.fromArray(new int[] { 1, 2, 3 });
      ListNode result = functionalSolution.mergeTwoLists(list1, null);
      assertThat(ListNode.toArray(result)).containsExactly(1, 2, 3);
    }
  }
}
