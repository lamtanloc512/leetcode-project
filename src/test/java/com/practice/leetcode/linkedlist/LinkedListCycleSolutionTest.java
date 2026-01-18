package com.practice.leetcode.linkedlist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Linked List Cycle problem.
 */
class LinkedListCycleSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Floyd's Cycle Detection (Rùa và Thỏ)
   *
   * Ý tưởng: Dùng 2 con trỏ di chuyển với tốc độ khác nhau.
   * - Slow (rùa): đi 1 bước mỗi lần
   * - Fast (thỏ): đi 2 bước mỗi lần
   *
   * Nếu có cycle: Fast sẽ "đuổi kịp" Slow từ phía sau (như chạy vòng quanh sân
   * vận động)
   * Nếu không có cycle: Fast sẽ đến null trước
   *
   * Tại sao chúng sẽ gặp nhau?
   * - Giả sử khoảng cách giữa fast và slow là d
   * - Mỗi bước, fast tiến 2, slow tiến 1 → khoảng cách giảm 1
   * - Sau d bước, khoảng cách = 0 → gặp nhau!
   *
   * Ví dụ: 1 → 2 → 3 → 4 → 2 (cycle at node 2)
   *
   * Step 0: slow=1, fast=1
   * Step 1: slow=2, fast=3
   * Step 2: slow=3, fast=2 (fast loop back)
   * Step 3: slow=4, fast=4 → GẶP NHAU! Có cycle.
   *
   * Time: O(n)
   * Space: O(1) - chỉ dùng 2 con trỏ
   */
  static class ImperativeSolution {
    public boolean hasCycle(ListNode head) {
      if (head == null || head.next == null) {
        return false;
      }

      ListNode slow = head;
      ListNode fast = head.next;

      while (slow != fast) {
        if (fast == null || fast.next == null) {
          return false;
        }
        slow = slow.next;
        fast = fast.next.next;
      }

      return true;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Uses a HashSet to track visited nodes.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean hasCycle(ListNode head) {
      Set<ListNode> visited = new HashSet<>();
      ListNode current = head;

      while (current != null) {
        if (!visited.add(current)) {
          return true;
        }
        current = current.next;
      }

      return false;
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  // Helper to create a cycle
  private ListNode createCycleList(int[] values, int pos) {
    if (values.length == 0)
      return null;

    ListNode head = new ListNode(values[0]);
    ListNode current = head;
    ListNode cycleNode = (pos == 0) ? head : null;

    for (int i = 1; i < values.length; i++) {
      current.next = new ListNode(values[i]);
      current = current.next;
      if (i == pos) {
        cycleNode = current;
      }
    }

    if (pos >= 0 && cycleNode != null) {
      current.next = cycleNode;
    }

    return head;
  }

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [3,2,0,-4], pos=1 -> true")
    void testExample1() {
      ListNode head = createCycleList(new int[] { 3, 2, 0, -4 }, 1);
      assertThat(imperativeSolution.hasCycle(head)).isTrue();
    }

    @Test
    @DisplayName("Example 2: [1,2], pos=0 -> true")
    void testExample2() {
      ListNode head = createCycleList(new int[] { 1, 2 }, 0);
      assertThat(imperativeSolution.hasCycle(head)).isTrue();
    }

    @Test
    @DisplayName("Example 3: [1], pos=-1 -> false")
    void testExample3() {
      ListNode head = new ListNode(1);
      assertThat(imperativeSolution.hasCycle(head)).isFalse();
    }

    @Test
    @DisplayName("Empty list")
    void testEmptyList() {
      assertThat(imperativeSolution.hasCycle(null)).isFalse();
    }

    @Test
    @DisplayName("No cycle - multiple elements")
    void testNoCycle() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2, 3, 4, 5 });
      assertThat(imperativeSolution.hasCycle(head)).isFalse();
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [3,2,0,-4], pos=1 -> true")
    void testExample1() {
      ListNode head = createCycleList(new int[] { 3, 2, 0, -4 }, 1);
      assertThat(functionalSolution.hasCycle(head)).isTrue();
    }

    @Test
    @DisplayName("Example 2: [1,2], pos=0 -> true")
    void testExample2() {
      ListNode head = createCycleList(new int[] { 1, 2 }, 0);
      assertThat(functionalSolution.hasCycle(head)).isTrue();
    }

    @Test
    @DisplayName("Example 3: [1], pos=-1 -> false")
    void testExample3() {
      ListNode head = new ListNode(1);
      assertThat(functionalSolution.hasCycle(head)).isFalse();
    }

    @Test
    @DisplayName("Empty list")
    void testEmptyList() {
      assertThat(functionalSolution.hasCycle(null)).isFalse();
    }

    @Test
    @DisplayName("No cycle - multiple elements")
    void testNoCycle() {
      ListNode head = ListNode.fromArray(new int[] { 1, 2, 3, 4, 5 });
      assertThat(functionalSolution.hasCycle(head)).isFalse();
    }
  }
}
