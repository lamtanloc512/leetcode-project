package com.practice.leetcode.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ BINARY TREE TECHNIQUE GUIDE ║
 * ║ (Kỹ thuật Cây Nhị Phân) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ CẤU TRÚC CƠ BẢN - TreeNode │
 * │                            │
 * │ class TreeNode {           │
 * │    int val;                │ 
 * │    TreeNode left;          │
 * │    TreeNode right;         │
 * │ }                          │
 * │                            │
 * │ Visualize:                 │
 * │ 1 ← root                   │
 * │ / \                        │
 * │ 2 3 ← level 1              │
 * │ / \ \                      │
 * │ 4 5 6 ← level 2 (leaves)   │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * 2 CÁCH DUYỆT CÂY CHÍNH
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ 1. DFS (Depth-First Search) - Duyệt theo chiều sâu │
 * │ - Đi sâu nhất có thể trước khi quay lại │
 * │ - Dùng RECURSION hoặc STACK │
 * │ - 3 thứ tự: Preorder, Inorder, Postorder │
 * │ │
 * │ 2. BFS (Breadth-First Search) - Duyệt theo chiều rộng │
 * │ - Duyệt từng level từ trên xuống │
 * │ - Dùng QUEUE │
 * │ - Level Order Traversal │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class BinaryTreeGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // DFS - 3 THỨU TỰ DUYỆT
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 1
   * / \
   * 2 3
   * / \
   * 4 5
   *
   * PREORDER (Tiền thứ tự): Root → Left → Right → [1, 2, 4, 5, 3]
   * INORDER (Trung thứ tự): Left → Root → Right → [4, 2, 5, 1, 3]
   * POSTORDER (Hậu thứ tự): Left → Right → Root → [4, 5, 2, 3, 1]
   *
   * Mẹo nhớ:
   * - PRE: xử lý Root TRƯỚC khi đi xuống
   * - IN: xử lý Root Ở GIỮA (giữa left và right)
   * - POST: xử lý Root SAU khi đã xử lý con
   */

  // ─────────────────────────────────────────────────────────────────────────
  // TEMPLATE: DFS RECURSION (Đệ quy)
  // ─────────────────────────────────────────────────────────────────────────
  /**
   * CÔNG THỨC ĐỆ QUY:
   *
   * 1. Base case: if (root == null) return ...;
   * 2. Xử lý node hiện tại (tùy preorder/inorder/postorder)
   * 3. Đệ quy xuống left và right
   * 4. Combine kết quả (nếu cần)
   */

  // PREORDER Template
  void preorderTemplate(TreeNode root) {
    if (root == null)
      return;

    // Xử lý ROOT trước
    process(root);

    // Rồi đi xuống con
    preorderTemplate(root.left);
    preorderTemplate(root.right);
  }

  // INORDER Template (BST: cho ra thứ tự tăng dần!)
  void inorderTemplate(TreeNode root) {
    if (root == null)
      return;

    inorderTemplate(root.left);

    // Xử lý ROOT ở giữa
    process(root);

    inorderTemplate(root.right);
  }

  // POSTORDER Template (xử lý con trước, cha sau)
  void postorderTemplate(TreeNode root) {
    if (root == null)
      return;

    postorderTemplate(root.left);
    postorderTemplate(root.right);

    // Xử lý ROOT sau cùng
    process(root);
  }

  // ─────────────────────────────────────────────────────────────────────────
  // TEMPLATE: DFS với RETURN VALUE
  // ─────────────────────────────────────────────────────────────────────────
  /**
   * Dùng khi cần TÍNH TOÁN giá trị từ dưới lên
   *
   * Ví dụ: Tính max depth, check balanced, tính sum, v.v.
   *
   * Pattern:
   * 1. Base case: return giá trị mặc định
   * 2. Đệ quy lấy kết quả từ left và right
   * 3. Combine và return
   */

  // Ví dụ: Max Depth
  int maxDepth(TreeNode root) {
    // Base case
    if (root == null)
      return 0;

    // Đệ quy lấy kết quả từ con
    int leftDepth = maxDepth(root.left);
    int rightDepth = maxDepth(root.right);

    // Combine: max của 2 con + 1 (node hiện tại)
    return Math.max(leftDepth, rightDepth) + 1;
  }

  // Ví dụ: Check Same Tree
  boolean isSameTree(TreeNode p, TreeNode q) {
    // Base cases
    if (p == null && q == null)
      return true;
    if (p == null || q == null)
      return false;
    if (p.val != q.val)
      return false;

    // Đệ quy và combine (AND)
    return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE: BFS - LEVEL ORDER
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CÔNG THỨC BFS:
   *
   * 1. Khởi tạo Queue, thêm root
   * 2. Lặp while queue không rỗng:
   * a. Lấy size hiện tại (= số node trong level)
   * b. Xử lý tất cả node trong level
   * c. Thêm con của chúng vào queue
   *
   * Visualize:
   * 1 Queue: [1]
   * / \
   * 2 3 Level 0: process 1, add 2,3 → Queue: [2,3]
   * / \ \
   * 4 5 6 Level 1: process 2,3, add 4,5,6 → Queue: [4,5,6]
   * Level 2: process 4,5,6 → Queue: []
   */
  List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null)
      return result;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
      int levelSize = queue.size(); // Quan trọng! Lấy size trước
      List<Integer> currentLevel = new ArrayList<>();

      for (int i = 0; i < levelSize; i++) {
        TreeNode node = queue.poll();
        currentLevel.add(node.val);

        if (node.left != null)
          queue.offer(node.left);
        if (node.right != null)
          queue.offer(node.right);
      }

      result.add(currentLevel);
    }

    return result;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE: DFS VỚI THAM SỐ PHỤ
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Dùng khi cần TRUYỀN THÔNG TIN từ trên xuống
   *
   * Ví dụ: Path Sum, depth hiện tại, max value đến hiện tại, v.v.
   */

  // Ví dụ: Path Sum (có path từ root đến leaf bằng target không?)
  boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null)
      return false;

    // Leaf node: kiểm tra
    if (root.left == null && root.right == null) {
      return root.val == targetSum;
    }

    // Đệ quy với target đã trừ đi node hiện tại
    int remaining = targetSum - root.val;
    return hasPathSum(root.left, remaining) || hasPathSum(root.right, remaining);
  }

  // Ví dụ: Collect all paths
  void collectPaths(TreeNode root, List<Integer> path,
      List<List<Integer>> result) {
    if (root == null)
      return;

    path.add(root.val);

    if (root.left == null && root.right == null) {
      // Leaf: thêm path vào result
      result.add(new ArrayList<>(path));
    } else {
      collectPaths(root.left, path, result);
      collectPaths(root.right, path, result);
    }

    path.remove(path.size() - 1); // Backtrack!
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BẢNG LỰA CHỌN APPROACH
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌──────────────────────────────────────────────────────────────────────────┐
   * │ Loại bài                            │ Approach                           │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Tính giá trị (depth, sum)           │ DFS + Return value                 │
   * │ So sánh 2 cây                       │ DFS song song                      │
   * │ Level by level                      │ BFS với levelSize                  │
   * │ Khoảng cách giữa nodes              │ BFS (shortest path)                │
   * │ Path từ root đến leaf               │ DFS + parameter                    │
   * │ Modify/Build tree                   │ DFS + return new node              │
   * │ BST operations                      │ Inorder (sorted!)                  │
   * │ Serialize/Deserialize               │ Preorder + null markers            │
   * └──────────────────────────────────────────────────────────────────────────┘
   *
   *
   * CHECKLIST KHI GIẢI BÀI
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ 1. Thông tin cần đi TỪ TRÊN XUỐNG hay TỪ DƯỚI LÊN?                      │
   * │ → Trên xuống: DFS với tham số                                           │
   * │ → Dưới lên: DFS với return value                                        │
   * │                                                                         │
   * │ 2. Cần xử lý theo LEVEL không?                                          │
   * │ → Có: BFS với levelSize                                                 │
   * │ → Không: DFS thường nhanh hơn                                           │
   * │                                                                         │
   * │ 3. Là BST không?                                                        │
   * │ → Có: Tận dụng tính chất sorted (inorder)                               │
   * │ → left < root < right                                                   │
   * └─────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CÁC BÀI THỰC HÀNH
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * EASY:
   * - Maximum Depth of Binary Tree (104) → DFS return value
   * - Same Tree (100) → DFS parallel
   * - Invert Binary Tree (226) → DFS modify
   * - Symmetric Tree (101) → DFS parallel với mirror
   *
   * MEDIUM:
   * - Binary Tree Level Order Traversal (102) → BFS
   * - Validate BST (98) → Inorder / DFS với range
   * - Lowest Common Ancestor (236) → DFS tìm từ dưới lên
   * - Construct Tree from Preorder/Inorder (105)
   * - Path Sum II (113) → DFS + backtracking
   *
   * HARD:
   * - Serialize and Deserialize (297) → Preorder
   * - Binary Tree Maximum Path Sum (124)
   */

  // Helper class và method
  static class TreeNode {
    int val;
    TreeNode left, right;

    TreeNode(int val) {
      this.val = val;
    }
  }

  void process(TreeNode node) {
    // Placeholder
  }
}
