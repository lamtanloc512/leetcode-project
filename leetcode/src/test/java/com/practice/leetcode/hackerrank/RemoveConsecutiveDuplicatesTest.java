package com.practice.leetcode.hackerrank;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cho SORTED linked list (có thể có duplicates).
 * Xóa các node TRÙNG LIÊN TIẾP, chỉ giữ lại 1 node đầu tiên.
 * 
 * Ví dụ:
 * Input:  1 → 2 → 2 → 2 → 3 → 4 → 4 → 5
 * Output: 1 → 2 → 3 → 4 → 5
 * 
 * Giải thích:
 * - Gặp 2 liên tiếp 3 lần → Giữ 1, xóa 2
 * - Gặp 4 liên tiếp 2 lần → Giữ 1, xóa 1
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * KEY INSIGHT
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Duyệt qua list:
 * - Nếu curr.val == curr.next.val → Skip curr.next (duplicate!)
 * - Nếu khác → Move to next
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC HACKERRANK
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Write a function "deleteDuplicates" that removes consecutive duplicate nodes 
 * in-place, retaining only the first node of each code. Return the head of the 
 * resulting list.
 * 
 * Example:
 * Input: head = [1, 2, 2, 2, 3, 4, 4, 5]
 * Output: [1, 2, 3, 4, 5]
 * 
 * Explanation:
 * - Given 1→2→2→2→3→4→4→5.
 * - Start at 1 (next is 2, no skip).
 * - Move to 2; skip all consecutive 2's so that 2 links directly to 3 (removing 2, 2).
 * - Now list is 1→2→3→4→4→5.
 * - Move to 3 (next is 4, no skip).
 * - At 4, skip the duplicate 4 so it links to 5.
 * - The resulting list is 1→2→3→4→5.
 * 
 * Constraints:
 * - 0 <= n <= 1000, where n is the number of nodes in the linked list
 * - -10^6 <= Node.val <= 10^5 for each node in the list
 * - The linked list is sorted in non-decreasing order: for each node u with 
 *   successor v, u.val <= v.val
 */
class RemoveConsecutiveDuplicatesTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // LINKED LIST NODE DEFINITION
    // ═══════════════════════════════════════════════════════════════════════════
    
    static class ListNode {
        int val;
        ListNode next;
        
        ListNode(int val) {
            this.val = val;
        }
        
        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // SOLUTION: ONE PASS
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * ALGORITHM
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Iterate through list:
     * - If current.val == current.next.val → Skip next (duplicate)
     * - Else → Move to next
     * 
     * Time: O(n)
     * Space: O(1)
     */
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) return null;
        
        ListNode curr = head;
        
        while (curr != null && curr.next != null) {
            if (curr.val == curr.next.val) {
                // ⭐ Skip duplicate: point to next.next
                curr.next = curr.next.next;
                // ⭐ Don't move curr! Check again with new next
            } else {
                // Different value, move forward
                curr = curr.next;
            }
        }
        
        return head;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HACKERRANK FORMAT SOLUTION
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ⭐ Copy paste này vào HackerRank!
     * (Đổi ListNode thành SinglyLinkedListNode)
     */
    public static ListNode deleteDuplicatesHackerRank(ListNode head) {
        if (head == null) return null;
        
        ListNode curr = head;
        
        while (curr != null && curr.next != null) {
            if (curr.val == curr.next.val) {
                curr.next = curr.next.next;  // Skip duplicate
            } else {
                curr = curr.next;  // Move forward
            }
        }
        
        return head;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Example: [1, 2, 2, 2, 3, 4, 4, 5]
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Initial: 1 → 2 → 2 → 2 → 3 → 4 → 4 → 5
     *          ↑
     *        curr
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Step 1: curr=1, next=2
     * 1 != 2 → Move forward
     * 
     * 1 → 2 → 2 → 2 → 3 → 4 → 4 → 5
     *     ↑
     *   curr
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Step 2: curr=2, next=2
     * 2 == 2 → Skip! curr.next = curr.next.next
     * 
     * 1 → 2 ───→ 2 → 3 → 4 → 4 → 5
     *     ↑    (skip)
     *   curr
     * 
     * curr không move! Check lại với next mới
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Step 3: curr=2, next=2 (still duplicate!)
     * 2 == 2 → Skip again!
     * 
     * 1 → 2 ───────→ 3 → 4 → 4 → 5
     *     ↑    (skip 2)
     *   curr
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Step 4: curr=2, next=3
     * 2 != 3 → Move forward
     * 
     * 1 → 2 → 3 → 4 → 4 → 5
     *         ↑
     *       curr
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Step 5: curr=3, next=4
     * 3 != 4 → Move forward
     * 
     * 1 → 2 → 3 → 4 → 4 → 5
     *             ↑
     *           curr
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Step 6: curr=4, next=4
     * 4 == 4 → Skip!
     * 
     * 1 → 2 → 3 → 4 ───→ 5
     *             ↑  (skip)
     *           curr
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Step 7: curr=4, next=5
     * 4 != 5 → Move forward
     * 
     * 1 → 2 → 3 → 4 → 5
     *                 ↑
     *               curr
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Step 8: curr=5, next=null
     * Stop!
     * 
     * Final: 1 → 2 → 3 → 4 → 5 ✓
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // WHY DON'T MOVE CURR WHEN SKIPPING?
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ❌ WRONG - Move curr after skip:
     * 
     * if (curr.val == curr.next.val) {
     *     curr.next = curr.next.next;
     *     curr = curr.next;  // ❌ Move forward
     * }
     * 
     * Problem: [1, 2, 2, 2, 3]
     * 
     * Step 1: curr=2, next=2 → Skip, curr moves to 2 (second one)
     * Step 2: curr=2, next=2 → Skip, curr moves to 2 (third one)
     * Step 3: curr=2, next=3 → No skip, move to 3
     * 
     * Result: 1 → 2 → 2 → 3 ❌ (Still has duplicate!)
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * ✅ CORRECT - Don't move curr:
     * 
     * if (curr.val == curr.next.val) {
     *     curr.next = curr.next.next;
     *     // ⭐ Stay at curr, check new next!
     * }
     * 
     * Step 1: curr=2, next=2 → Skip, stay at curr=2
     * Step 2: curr=2, next=2 (new) → Skip again, stay at curr=2
     * Step 3: curr=2, next=3 → Different, move to 3
     * 
     * Result: 1 → 2 → 3 ✓
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════════════════
    
    private ListNode buildList(int[] values) {
        if (values.length == 0) return null;
        
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        for (int val : values) {
            curr.next = new ListNode(val);
            curr = curr.next;
        }
        
        return dummy.next;
    }
    
    private int[] toArray(ListNode head) {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        
        while (head != null) {
            result.add(head.val);
            head = head.next;
        }
        
        return result.stream().mapToInt(i -> i).toArray();
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example: [1,2,2,2,3,4,4,5] → [1,2,3,4,5]")
    void testExample() {
        ListNode head = buildList(new int[]{1, 2, 2, 2, 3, 4, 4, 5});
        ListNode result = deleteDuplicates(head);
        assertThat(toArray(result)).containsExactly(1, 2, 3, 4, 5);
    }
    
    @Test
    @DisplayName("All same: [1,1,1,1] → [1]")
    void testAllSame() {
        ListNode head = buildList(new int[]{1, 1, 1, 1});
        ListNode result = deleteDuplicates(head);
        assertThat(toArray(result)).containsExactly(1);
    }
    
    @Test
    @DisplayName("No duplicates: [1,2,3] → [1,2,3]")
    void testNoDuplicates() {
        ListNode head = buildList(new int[]{1, 2, 3});
        ListNode result = deleteDuplicates(head);
        assertThat(toArray(result)).containsExactly(1, 2, 3);
    }
    
    @Test
    @DisplayName("Empty list: [] → []")
    void testEmpty() {
        ListNode head = buildList(new int[]{});
        ListNode result = deleteDuplicates(head);
        assertThat(toArray(result)).isEmpty();
    }
    
    @Test
    @DisplayName("Single element: [1] → [1]")
    void testSingleElement() {
        ListNode head = buildList(new int[]{1});
        ListNode result = deleteDuplicates(head);
        assertThat(toArray(result)).containsExactly(1);
    }
    
    @Test
    @DisplayName("Two same: [1,1] → [1]")
    void testTwoSame() {
        ListNode head = buildList(new int[]{1, 1});
        ListNode result = deleteDuplicates(head);
        assertThat(toArray(result)).containsExactly(1);
    }
    
    @Test
    @DisplayName("Two different: [1,2] → [1,2]")
    void testTwoDifferent() {
        ListNode head = buildList(new int[]{1, 2});
        ListNode result = deleteDuplicates(head);
        assertThat(toArray(result)).containsExactly(1, 2);
    }
    
    @Test
    @DisplayName("Duplicates at end: [1,2,3,3,3] → [1,2,3]")
    void testDuplicatesAtEnd() {
        ListNode head = buildList(new int[]{1, 2, 3, 3, 3});
        ListNode result = deleteDuplicates(head);
        assertThat(toArray(result)).containsExactly(1, 2, 3);
    }
    
    @Test
    @DisplayName("Duplicates at start: [1,1,1,2,3] → [1,2,3]")
    void testDuplicatesAtStart() {
        ListNode head = buildList(new int[]{1, 1, 1, 2, 3});
        ListNode result = deleteDuplicates(head);
        assertThat(toArray(result)).containsExactly(1, 2, 3);
    }
}
