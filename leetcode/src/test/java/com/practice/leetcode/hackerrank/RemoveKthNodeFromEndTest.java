package com.practice.leetcode.hackerrank;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cho linked list và số k.
 * Tìm node thứ k TỪ CUỐI, rồi RETURN từ node đó trở đi (new head).
 * 
 * Ví dụ:
 * List: 5 → 6 → 7 → 8
 * k = 3
 * 
 * Đếm từ cuối:
 * - 8 là node thứ 1 từ cuối
 * - 7 là node thứ 2 từ cuối
 * - 6 là node thứ 3 từ cuối ← Return từ đây!
 * - 5 là node thứ 4 từ cuối
 * 
 * Result: 6 → 7 → 8 (return node 6 as new head)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * KEY INSIGHT - TWO POINTERS với khoảng cách K
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Ý tưởng: Dùng 2 pointers cách nhau k-1 nodes
 * 
 * Step 1: Di chuyển fast k-1 bước trước
 * Step 2: Di chuyển cả fast và slow cùng lúc
 * Step 3: Khi fast đến cuối → slow chính là node thứ k từ cuối!
 * 
 * Visualize:
 * 
 * List: 5 → 6 → 7 → 8, k=3
 * 
 * Step 1: Move fast k-1 = 2 steps
 * 
 *     slow
 *       ↓
 *       5 → 6 → 7 → 8
 *               ↑
 *             fast
 * 
 * Step 2: Move both until fast.next == null
 * 
 *           slow
 *             ↓
 *       5 → 6 → 7 → 8 → null
 *                   ↑
 *                 fast
 * 
 * Step 3: slow is the k-th node from end!
 * Return slow
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC HACKERRANK
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Given the head of a singly linked list and an integer k, remove the k-th node 
 * from the end in one traversal and return the new head. If k is invalid, return 
 * the original list.
 * 
 * Example:
 * Input: head = [5, 6, 7, 8], k = 3
 * Output: [6, 7, 8]
 * 
 * Explanation: 
 * The list has 4 nodes. The k-th node from the end with k=3 is the 4th node 
 * from the end (value 5), so we remove it.
 * 
 * Constraints:
 * - 0 <= number of nodes in head <= 1000
 */
class RemoveKthNodeFromEndTest {

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
    // SOLUTION: TWO POINTERS (ONE PASS)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * ALGORITHM
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Find k-th node from end, then return from that node
     * 
     * Step 1: Move fast pointer k-1 steps ahead
     * Step 2: Move both fast and slow together
     * Step 3: When fast reaches end, slow is the k-th node from end
     * Step 4: Return slow as new head
     * 
     * Time: O(n) - one pass!
     * Space: O(1)
     */
    public ListNode removeKthFromEnd(ListNode head, int k) {
        if (head == null || k <= 0) return head;
        
        ListNode slow = head;
        ListNode fast = head;
        
        // ─────────────────────────────────────────────────────────────────────
        // STEP 1: Move fast k-1 steps ahead
        // ─────────────────────────────────────────────────────────────────────
        
        for (int i = 0; i < k - 1; i++) {
            if (fast == null) {
                return head;  // k too large, return original
            }
            fast = fast.next;
        }
        
        // Check if k is valid
        if (fast == null) {
            return head;  // k > length
        }
        
        // ─────────────────────────────────────────────────────────────────────
        // STEP 2: Move both pointers until fast reaches end
        // ─────────────────────────────────────────────────────────────────────
        
        while (fast.next != null) {
            slow = slow.next;
            fast = fast.next;
        }
        
        // ─────────────────────────────────────────────────────────────────────
        // STEP 3: slow is now the k-th node from end
        // ─────────────────────────────────────────────────────────────────────
        
        return slow;  // ⭐ Return k-th node as new head!
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HACKERRANK FORMAT SOLUTION
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * HackerRank format với SinglyLinkedListNode
     * 
     * ⭐ Copy paste function này vào HackerRank!
     * 
     * Find k-th node from end and return it as new head
     */
    public static ListNode removeKthNodeFromEndHackerRank(ListNode head, int k) {
        if (head == null || k <= 0) return head;
        
        ListNode slow = head;
        ListNode fast = head;
        
        // Move fast k-1 steps ahead
        for (int i = 0; i < k - 1; i++) {
            if (fast == null) {
                return head;  // k too large
            }
            fast = fast.next;
        }
        
        if (fast == null) {
            return head;  // k > length
        }
        
        // Move both until fast reaches end
        while (fast.next != null) {
            slow = slow.next;
            fast = fast.next;
        }
        
        // slow is k-th node from end
        return slow;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Example: head = [5, 6, 7, 8], k = 3
     * 
     * Remove 3rd node from end = Remove node with value 6
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * INITIAL STATE
     * ─────────────────────────────────────────────────────────────────────────
     * 
     *   dummy → 5 → 6 → 7 → 8 → null
     *     ↑
     *   slow, fast
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * STEP 1: Move fast k+1 = 4 steps
     * ─────────────────────────────────────────────────────────────────────────
     * 
     *   dummy → 5 → 6 → 7 → 8 → null
     *     ↑               ↑
     *   slow           fast
     * 
     * Gap between slow and fast = 3 nodes (5, 6, 7)
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * STEP 2: Move both until fast == null
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Iteration 1:
     *   dummy → 5 → 6 → 7 → 8 → null
     *           ↑           ↑
     *         slow        fast
     * 
     * Iteration 2:
     *   dummy → 5 → 6 → 7 → 8 → null
     *               ↑           ↑
     *             slow        fast (null)
     * 
     * Stop! fast == null
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * STEP 3: Remove node
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * slow is at node 5
     * slow.next is node 6 (target!)
     * slow.next.next is node 7
     * 
     * Remove: slow.next = slow.next.next
     * 
     *   dummy → 5 ───→ 7 → 8 → null
     *               (skip 6)
     * 
     * Result: [5, 7, 8] ✓
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // WHY DUMMY NODE?
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Edge case: Remove head
     * 
     * Example: head = [1, 2], k = 2 (remove 1st from end = remove head)
     * 
     * WITHOUT dummy:
     *   Need special handling for head removal
     * 
     * WITH dummy:
     *   dummy → 1 → 2 → null
     *     ↑       ↑
     *   slow    fast (after k+1 steps)
     * 
     *   After moving:
     *   dummy → 1 → 2 → null
     *     ↑           ↑
     *   slow        fast (null)
     * 
     *   Remove: slow.next = slow.next.next
     *   dummy → 2 → null
     * 
     *   Return dummy.next = 2 ✓ (new head!)
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Build list from array
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
    
    // Convert list to array
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
    @DisplayName("Example: [5,6,7,8], k=3 → [5,7,8]")
    void testExample() {
        ListNode head = buildList(new int[]{5, 6, 7, 8});
        ListNode result = removeKthFromEnd(head, 3);
        assertThat(toArray(result)).containsExactly(5, 7, 8);
    }
    
    @Test
    @DisplayName("Remove head: [1,2], k=2 → [2]")
    void testRemoveHead() {
        ListNode head = buildList(new int[]{1, 2});
        ListNode result = removeKthFromEnd(head, 2);
        assertThat(toArray(result)).containsExactly(2);
    }
    
    @Test
    @DisplayName("Remove last: [1,2,3], k=1 → [1,2]")
    void testRemoveLast() {
        ListNode head = buildList(new int[]{1, 2, 3});
        ListNode result = removeKthFromEnd(head, 1);
        assertThat(toArray(result)).containsExactly(1, 2);
    }
    
    @Test
    @DisplayName("Single element: [1], k=1 → []")
    void testSingleElement() {
        ListNode head = buildList(new int[]{1});
        ListNode result = removeKthFromEnd(head, 1);
        assertThat(toArray(result)).isEmpty();
    }
    
    @Test
    @DisplayName("Invalid k: [1,2,3], k=5 → [1,2,3] (no change)")
    void testInvalidK() {
        ListNode head = buildList(new int[]{1, 2, 3});
        ListNode result = removeKthFromEnd(head, 5);
        assertThat(toArray(result)).containsExactly(1, 2, 3);
    }
    
    @Test
    @DisplayName("Middle element: [1,2,3,4,5], k=3 → [1,2,4,5]")
    void testMiddle() {
        ListNode head = buildList(new int[]{1, 2, 3, 4, 5});
        ListNode result = removeKthFromEnd(head, 3);
        assertThat(toArray(result)).containsExactly(1, 2, 4, 5);
    }
    
    @Test
    @DisplayName("Two elements: [1,2], k=1 → [1]")
    void testTwoElements() {
        ListNode head = buildList(new int[]{1, 2});
        ListNode result = removeKthFromEnd(head, 1);
        assertThat(toArray(result)).containsExactly(1);
    }
}
