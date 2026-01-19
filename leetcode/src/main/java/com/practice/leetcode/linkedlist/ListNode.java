package com.practice.leetcode.linkedlist;

/**
 * Definition for singly-linked list node.
 * This class is used across all linked list problems.
 */
public class ListNode {
  public int val;
  public ListNode next;

  public ListNode() {
  }

  public ListNode(int val) {
    this.val = val;
  }

  public ListNode(int val, ListNode next) {
    this.val = val;
    this.next = next;
  }

  /**
   * Helper method to create a linked list from an array.
   *
   * @param values array of values
   * @return head of the linked list
   */
  public static ListNode fromArray(int[] values) {
    if (values == null || values.length == 0) {
      return null;
    }

    ListNode dummy = new ListNode(0);
    ListNode current = dummy;

    for (int val : values) {
      current.next = new ListNode(val);
      current = current.next;
    }

    return dummy.next;
  }

  /**
   * Helper method to convert linked list to array.
   *
   * @param head head of the linked list
   * @return array of values
   */
  public static int[] toArray(ListNode head) {
    java.util.List<Integer> list = new java.util.ArrayList<>();
    ListNode current = head;

    while (current != null) {
      list.add(current.val);
      current = current.next;
    }

    return list.stream().mapToInt(Integer::intValue).toArray();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    ListNode current = this;
    while (current != null) {
      sb.append(current.val);
      if (current.next != null) {
        sb.append(" -> ");
      }
      current = current.next;
    }
    sb.append("]");
    return sb.toString();
  }
}
