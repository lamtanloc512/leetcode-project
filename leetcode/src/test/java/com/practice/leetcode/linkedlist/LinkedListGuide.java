package com.practice.leetcode.linkedlist;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ LINKED LIST TECHNIQUE GUIDE ║
 * ║ (Kỹ thuật Danh Sách Liên Kết) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ CẤU TRÚC CƠ BẢN - ListNode │
 * │ │
 * │ class ListNode { │
 * │ int val; │
 * │ ListNode next; │
 * │ } │
 * │ │
 * │ Visualize: │
 * │ head → [1] → [2] → [3] → [4] → [5] → null │
 * │ ↑ ↑ │
 * │ head tail │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * CÁC KỸ THUẬT CHÍNH
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * 1. DUMMY HEAD - Node giả ở đầu
 * 2. TWO POINTERS - Hai con trỏ (fast/slow hoặc prev/curr)
 * 3. REVERSE - Đảo ngược
 * 4. MERGE - Gộp 2 list
 * 5. CYCLE DETECTION - Phát hiện vòng (Floyd's)
 */
public class LinkedListGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 1: DUMMY HEAD
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * TẠI SAO CẦN DUMMY HEAD?
   *
   * - Tránh xử lý đặc biệt cho head
   * - Code sạch hơn, ít if/else
   * - Luôn có node đứng trước node cần thao tác
   *
   * Khi nào dùng:
   * - Thêm/xóa node ở đầu list
   * - Merge 2 lists
   * - Khi head có thể thay đổi
   *
   * CÔNG THỨC:
   *
   * 1. Tạo dummy: ListNode dummy = new ListNode(0);
   * 2. dummy.next = head (hoặc để làm điểm bắt đầu)
   * 3. Thao tác với list
   * 4. Return dummy.next
   */
  ListNode dummyHeadTemplate(ListNode head) {
    // Tạo dummy node
    ListNode dummy = new ListNode(0);
    dummy.next = head;

    // Thao tác với list...
    ListNode curr = dummy;
    while (curr.next != null) {
      // Process curr.next
      curr = curr.next;
    }

    // Return từ dummy.next (head thật)
    return dummy.next;
  }

  // Ví dụ: Remove duplicates
  ListNode removeDuplicates(ListNode head) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    ListNode prev = dummy;

    while (prev.next != null && prev.next.next != null) {
      if (prev.next.val == prev.next.next.val) {
        int duplicate = prev.next.val;
        // Skip all nodes with duplicate value
        while (prev.next != null && prev.next.val == duplicate) {
          prev.next = prev.next.next;
        }
      } else {
        prev = prev.next;
      }
    }

    return dummy.next;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 2: TWO POINTERS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 2A: FAST/SLOW POINTERS (Rùa và Thỏ)
   *
   * Dùng cho:
   * - Tìm middle của list
   * - Phát hiện cycle
   * - Tìm node thứ k từ cuối
   *
   * Pattern:
   * - slow đi 1 bước, fast đi 2 bước
   * - Khi fast đến cuối, slow ở giữa
   */

  // Tìm middle node
  ListNode findMiddle(ListNode head) {
    ListNode slow = head;
    ListNode fast = head;

    // fast đi 2 bước, slow đi 1 bước
    while (fast != null && fast.next != null) {
      slow = slow.next;
      fast = fast.next.next;
    }

    // slow giờ ở middle
    // Nếu even: slow ở node thứ 2 của middle pair
    return slow;
  }

  // Tìm node thứ n từ cuối
  ListNode findNthFromEnd(ListNode head, int n) {
    ListNode fast = head;
    ListNode slow = head;

    // Fast đi trước n bước
    for (int i = 0; i < n; i++) {
      fast = fast.next;
    }

    // Cả 2 đi cùng tốc độ
    while (fast != null) {
      slow = slow.next;
      fast = fast.next;
    }

    // slow giờ ở vị trí n từ cuối
    return slow;
  }

  /**
   * 2B: PREV/CURR POINTERS
   *
   * Dùng cho:
   * - Xóa node
   * - Reverse
   * - Thay đổi cấu trúc
   *
   * Pattern: Luôn giữ reference đến node TRƯỚC node cần thao tác
   */

  // Xóa node (biết giá trị cần xóa)
  ListNode removeElements(ListNode head, int val) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    ListNode prev = dummy;
    ListNode curr = head;

    while (curr != null) {
      if (curr.val == val) {
        // Xóa: prev.next bỏ qua curr
        prev.next = curr.next;
      } else {
        prev = curr;
      }
      curr = curr.next;
    }

    return dummy.next;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 3: REVERSE LINKED LIST
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ĐÂY LÀ KỸ THUẬT QUAN TRỌNG NHẤT!
   *
   * Visualize từng bước:
   *
   * Ban đầu: 1 → 2 → 3 → 4 → null prev = null
   * ↑
   * curr
   *
   * Bước 1: 1 ← 2 → 3 → 4 → null 1.next = prev (null)
   * ↑ ↑ prev = 1
   * prev curr curr = 2
   *
   * Bước 2: 1 ← 2 ← 3 → 4 → null 2.next = 1
   * ↑ ↑ prev = 2
   * prev curr curr = 3
   *
   * Cuối: 1 ← 2 ← 3 ← 4 return prev (= 4)
   *
   * CÔNG THỨC:
   * 1. Lưu next = curr.next (để không mất)
   * 2. Đảo: curr.next = prev
   * 3. Di chuyển: prev = curr, curr = next
   */

  // Iterative Reverse
  ListNode reverseIterative(ListNode head) {
    ListNode prev = null;
    ListNode curr = head;

    while (curr != null) {
      ListNode next = curr.next; // 1. Lưu next
      curr.next = prev; // 2. Đảo pointer
      prev = curr; // 3. Di chuyển prev
      curr = next; // 4. Di chuyển curr
    }

    return prev; // prev là head mới
  }

  // Recursive Reverse
  ListNode reverseRecursive(ListNode head) {
    // Base case
    if (head == null || head.next == null) {
      return head;
    }

    // Đệ quy: reverse phần còn lại
    ListNode newHead = reverseRecursive(head.next);

    // head.next giờ là CUỐI của reversed list
    head.next.next = head; // Node sau trỏ về head
    head.next = null; // Head trỏ ra null (thành tail)

    return newHead;
  }

  // Reverse một đoạn từ left đến right
  ListNode reverseBetween(ListNode head, int left, int right) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    ListNode prev = dummy;

    // Di chuyển đến vị trí left - 1
    for (int i = 0; i < left - 1; i++) {
      prev = prev.next;
    }

    // Reverse từ left đến right
    ListNode curr = prev.next;
    for (int i = 0; i < right - left; i++) {
      ListNode next = curr.next;
      curr.next = next.next;
      next.next = prev.next;
      prev.next = next;
    }

    return dummy.next;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 4: MERGE TWO LISTS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CÔNG THỨC MERGE (sorted lists):
   * 1. Tạo dummy head
   * 2. So sánh 2 node, chọn nhỏ hơn
   * 3. Nối vào tail, tiến con trỏ của list được chọn
   * 4. Nối phần còn lại
   */
  ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0);
    ListNode tail = dummy;

    while (l1 != null && l2 != null) {
      if (l1.val <= l2.val) {
        tail.next = l1;
        l1 = l1.next;
      } else {
        tail.next = l2;
        l2 = l2.next;
      }
      tail = tail.next;
    }

    // Nối phần còn lại
    tail.next = (l1 != null) ? l1 : l2;

    return dummy.next;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 5: CYCLE DETECTION (Floyd's)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * THUẬT TOÁN RÙA VÀ THỎ:
   *
   * Nếu có cycle, fast sẽ "đuổi kịp" slow từ phía sau
   *
   * Phase 1: Phát hiện cycle
   * - slow đi 1, fast đi 2
   * - Nếu gặp nhau → có cycle
   *
   * Phase 2: Tìm điểm bắt đầu cycle
   * - Reset slow về head
   * - Cả 2 đi 1 bước
   * - Điểm gặp nhau = điểm bắt đầu cycle
   */
  boolean hasCycle(ListNode head) {
    ListNode slow = head;
    ListNode fast = head;

    while (fast != null && fast.next != null) {
      slow = slow.next;
      fast = fast.next.next;

      if (slow == fast)
        return true;
    }

    return false;
  }

  ListNode detectCycleStart(ListNode head) {
    ListNode slow = head;
    ListNode fast = head;

    // Phase 1: Tìm điểm gặp nhau
    while (fast != null && fast.next != null) {
      slow = slow.next;
      fast = fast.next.next;

      if (slow == fast) {
        // Phase 2: Tìm điểm bắt đầu cycle
        slow = head;
        while (slow != fast) {
          slow = slow.next;
          fast = fast.next;
        }
        return slow; // Điểm bắt đầu cycle
      }
    }

    return null; // Không có cycle
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BẢNG LỰA CHỌN TECHNIQUE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌──────────────────────────────────────────────────────────────────────────┐
   * │ Loại bài │ Technique │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Thêm/Xóa ở đầu │ Dummy Head │
   * │ Tìm middle │ Fast/Slow (2x speed) │
   * │ Tìm từ cuối │ Fast đi trước n bước │
   * │ Đảo ngược │ Prev/Curr/Next │
   * │ Phát hiện cycle │ Floyd's (Fast/Slow) │
   * │ Merge sorted lists │ Dummy + compare │
   * │ Palindrome check │ Find middle + Reverse + Compare │
   * │ Reorder (1→n→2→n-1...) │ Split + Reverse + Merge │
   * └──────────────────────────────────────────────────────────────────────────┘
   *
   *
   * CÁC BÀI THỰC HÀNH
   *
   * EASY:
   * - Reverse Linked List (206)
   * - Merge Two Sorted Lists (21)
   * - Linked List Cycle (141)
   * - Remove Duplicates (83)
   * - Middle of Linked List (876)
   *
   * MEDIUM:
   * - Remove Nth Node From End (19) → Two pointers
   * - Linked List Cycle II (142) → Floyd's Phase 2
   * - Reorder List (143) → Split + Reverse + Merge
   * - Add Two Numbers (2) → Dummy + carry
   * - Rotate List (61) → Find length + connect
   *
   * HARD:
   * - Reverse Nodes in k-Group (25)
   * - Merge k Sorted Lists (23) → Divide & Conquer hoặc Heap
   */

  // Helper class
  static class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
      this.val = val;
    }
  }
}
