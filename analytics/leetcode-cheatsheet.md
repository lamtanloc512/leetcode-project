# LeetCode Patterns Cheatsheet

> Quick reference for common patterns, tree traversals, and algorithm tricks

---

## 🌳 Tree Traversals

### So sánh 3 loại

| Traversal | Thứ tự | Tính chất | Use Cases |
|-----------|--------|-----------|-----------|
| **Inorder** | Left → Root → Right | BST → **Sorted** | Validate BST, Kth element |
| **Preorder** | Root → Left → Right | Root **đầu tiên** | Serialize, Clone tree |
| **Postorder** | Left → Right → Root | Root **cuối cùng** | Delete tree, Calculate height |

### Tính chất quan trọng

```
BST + Inorder = Sorted Array ⭐

Preorder + Inorder = Rebuild Tree ⭐
Postorder + Inorder = Rebuild Tree ⭐

Preorder + Postorder ≠ Unique Tree (thiếu thông tin)
```

### Ví dụ

```
       4
      / \
     2   6
    / \ / \
   1  3 5  7

Inorder:   [1,2,3,4,5,6,7]  ← Sorted!
Preorder:  [4,2,1,3,6,5,7]  ← Root = 4 (đầu tiên)
Postorder: [1,3,2,5,7,6,4]  ← Root = 4 (cuối cùng)
```

---

## 🔧 Common Patterns

### 1. Build Balanced BST từ Sorted Array
```java
// LC 108, 109, 1382
TreeNode build(int[] nums, int left, int right) {
    if (left > right) return null;
    int mid = left + (right - left) / 2;
    TreeNode node = new TreeNode(nums[mid]);
    node.left = build(nums, left, mid - 1);
    node.right = build(nums, mid + 1, right);
    return node;
}
```

### 2. Validate BST (Inorder phải tăng dần)
```java
// LC 98
Integer prev = null;
boolean isValidBST(TreeNode node) {
    if (node == null) return true;
    if (!isValidBST(node.left)) return false;
    if (prev != null && node.val <= prev) return false;
    prev = node.val;
    return isValidBST(node.right);
}
```

### 3. Build Tree từ Preorder + Inorder
```java
// LC 105
// Preorder[0] = root
// Tìm root trong Inorder → chia left/right subtree
```

---

## 📊 Two Pointers

| Pattern | Template |
|---------|----------|
| **Opposite** | `left=0, right=n-1` → move toward center |
| **Same direction** | `slow, fast` → fast moves ahead |
| **Sliding Window** | `left, right` → expand right, shrink left |

```java
// Two Sum (sorted)
while (left < right) {
    int sum = arr[left] + arr[right];
    if (sum == target) return true;
    if (sum < target) left++;
    else right--;
}
```

---

## 🔄 Binary Search

```java
// Template: find first true condition
int left = 0, right = n;
while (left < right) {
    int mid = left + (right - left) / 2;
    if (condition(mid)) right = mid;
    else left = mid + 1;
}
return left;
```

| Variant | Condition | Return |
|---------|-----------|--------|
| First >= target | `nums[mid] >= target` | `left` |
| First > target | `nums[mid] > target` | `left` |
| Last <= target | `nums[mid] > target` | `left - 1` |

---

## 🌊 BFS vs DFS

| BFS | DFS |
|-----|-----|
| Queue | Stack / Recursion |
| Level by level | Go deep first |
| Shortest path (unweighted) | Path finding, exhaustive |
| More memory (wide tree) | Less memory (balanced) |

```java
// BFS Template
Queue<Node> queue = new LinkedList<>();
queue.offer(root);
while (!queue.isEmpty()) {
    int size = queue.size();  // Level size
    for (int i = 0; i < size; i++) {
        Node node = queue.poll();
        // Process node
        if (node.left != null) queue.offer(node.left);
        if (node.right != null) queue.offer(node.right);
    }
}
```

---

## 🧮 Dynamic Programming

### Nhận biết DP
- Optimal substructure (bài toán con tối ưu)
- Overlapping subproblems (bài toán con lặp lại)
- "Đếm số cách", "Tìm min/max"

### Template
```java
// 1D DP
dp[i] = f(dp[i-1], dp[i-2], ...)

// 2D DP
dp[i][j] = f(dp[i-1][j], dp[i][j-1], ...)
```

| Pattern | Example |
|---------|---------|
| Linear | Fibonacci, House Robber |
| Grid | Unique Paths, Min Path Sum |
| String | LCS, Edit Distance |
| Knapsack | 0/1 Knapsack, Coin Change |

---

## ⏰ Time Complexity Quick Ref

| Pattern | Complexity |
|---------|------------|
| Two nested loops | O(n²) |
| Binary Search | O(log n) |
| Sort + process | O(n log n) |
| HashMap lookup | O(1) average |
| DFS/BFS on tree | O(n) |
| DFS/BFS on graph | O(V + E) |
| Generate subsets | O(2ⁿ) |
| Generate permutations | O(n!) |

---

## 🎯 LeetCode Problems by Pattern

| Pattern | Problems |
|---------|----------|
| **Tree Build** | LC 105, 106, 108, 109, 1382 |
| **Validate BST** | LC 98, 230, 285 |
| **Two Pointers** | LC 1, 15, 167, 11, 42 |
| **Binary Search** | LC 33, 34, 153, 162 |
| **BFS** | LC 102, 103, 199, 207, 994 |
| **DFS** | LC 200, 79, 39, 46, 78 |
| **DP** | LC 70, 198, 300, 322, 1143 |
| **Sliding Window** | LC 3, 438, 76, 239 |
