package com.practice.leetcode.backtracking;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * HƯỚNG DẪN VẼ DECISION TREE CHO BACKTRACKING
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Decision tree giúp bạn VISUALIZE toàn bộ quá trình backtracking!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 5 BƯỚC VẼ DECISION TREE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * BƯỚC 1: XÁC ĐỊNH ROOT
 * ─────────────────────────────────────────────────────────────────────────────
 * Root = Initial state (empty/starting point)
 * 
 * Ví dụ:
 * - Generate strings: root = ""
 * - Subsets: root = []
 * - N-Queens: root = empty board
 * 
 * 
 * BƯỚC 2: XÁC ĐỊNH LEVELS (CHIỀU CAO)
 * ─────────────────────────────────────────────────────────────────────────────
 * Mỗi level = 1 decision point
 * 
 * Ví dụ:
 * - Generate binary n=3: 3 levels (mỗi level chọn 1 bit)
 * - Phone letters "23": 2 levels (mỗi level chọn letter cho 1 digit)
 * - Permutations [1,2,3]: 3 levels (mỗi level chọn 1 số)
 * 
 * 
 * BƯỚC 3: XÁC ĐỊNH BRANCHES (RỘNG)
 * ─────────────────────────────────────────────────────────────────────────────
 * Mỗi node có bao nhiêu children?
 * 
 * Ví dụ:
 * - Binary strings: 2 branches (0 hoặc 1)
 * - Phone letters "23": 3 branches (a, b, c) rồi 3 branches (d, e, f)
 * - Subsets: Giảm dần (n, n-1, n-2, ..., 1, 0)
 * 
 * 
 * BƯỚC 4: XÁC ĐỊNH LEAVES (BASE CASE)
 * ─────────────────────────────────────────────────────────────────────────────
 * Khi nào dừng? → Đó là leaves!
 * 
 * Ví dụ:
 * - Binary n=3: Khi length == 3
 * - Permutations: Khi đã dùng n phần tử
 * - Combinations: Khi đã chọn k phần tử
 * 
 * 
 * BƯỚC 5: VẼ TỪ TRÁI SANG PHẢI (DFS ORDER)
 * ─────────────────────────────────────────────────────────────────────────────
 * Backtracking = DFS traversal
 * → Vẽ từ trái sang phải = thứ tự output!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ 1: GENERATE BINARY STRINGS (n=2)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Step 1: ROOT
 * 
 *     ""
 * 
 * Step 2: LEVEL 1 - Chọn bit đầu tiên (0 hoặc 1)
 * 
 *        ""
 *      /    \
 *    "0"    "1"
 * 
 * Step 3: LEVEL 2 - Chọn bit thứ 2 (0 hoặc 1)
 * 
 *           ""
 *         /    \
 *       "0"    "1"
 *      /  \    /  \
 *   "00" "01" "10" "11"
 * 
 * Step 4: Mark leaves (length == 2)
 * 
 *   "00"  "01"  "10"  "11"
 *    ✓     ✓     ✓     ✓
 * 
 * Step 5: Read left-to-right (DFS order)
 * Result: ["00", "01", "10", "11"] ✓
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ 2: PHONE LETTERS "23"
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Mapping: 2→abc, 3→def
 * 
 * Step 1-2: ROOT + LEVEL 1 (digit '2')
 * 
 *              ""
 *         /     |     \
 *       "a"    "b"    "c"
 * 
 * Step 3: LEVEL 2 (digit '3')
 * 
 *                   ""
 *              /     |     \
 *            "a"    "b"    "c"
 *          / | \   / | \   / | \
 *        ad ae af bd be bf cd ce cf
 * 
 * Step 4: Leaves (index == digits.length)
 * Step 5: DFS order = ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"] ✓
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ 3: SUBSETS [1,2,3]
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Special: MỖI NODE là 1 valid subset (không chỉ leaves!)
 * 
 *                          [] ✓
 *              ┌────────────┼────────────┐
 *             [1] ✓        [2] ✓        [3] ✓
 *          ┌───┴───┐         │
 *        [1,2] ✓ [1,3] ✓   [2,3] ✓
 *          │
 *       [1,2,3] ✓
 * 
 * Tại sao không có [1,2] → [2,1]?
 * → Vì dùng start index (i+1) để tránh duplicate!
 * 
 * DFS order: [[], [1], [1,2], [1,2,3], [1,3], [2], [2,3], [3]] ✓
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ 4: PERMUTATIONS [1,2]
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *                    []
 *              /           \
 *           [1]            [2]
 *       (used[0]=T)    (used[1]=T)
 *            |              |
 *          [1,2] ✓        [2,1] ✓
 *      (used[1]=T)    (used[0]=T)
 * 
 * DFS order: [[1,2], [2,1]] ✓
 * 
 * Key: boolean[] used để track!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ 5: COMBINATION SUM [2,3], target=5
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *                    [] (remaining=5)
 *              /                    \
 *          [2] (rem=3)            [3] (rem=2)
 *        /           \           /           \
 *    [2,2] (rem=1) [2,3] (rem=0) [3,3] (rem<0) [3,2] (rem<0)
 *       |             ✓           ❌ prune!    ❌ prune!
 *   [2,2,2] (rem<0)
 *   ❌ prune!
 * 
 * Valid: [[2,3]] (only 1 solution!)
 * 
 * Key: 
 * - Use i (not i+1) để allow reuse [2,2,3]
 * - Prune when remaining < 0
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN RECOGNITION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * ┌─────────────────────┬──────────────────┬────────────────┬───────────────┐
 * │ Pattern             │ Branching Factor │ Height         │ Total Nodes   │
 * ├─────────────────────┼──────────────────┼────────────────┼───────────────┤
 * │ Binary strings n=3  │ 2 (0 or 1)       │ 3              │ 2^3 = 8       │
 * │ Phone "23"          │ 3 then 3         │ 2              │ 3×3 = 9       │
 * │ Subsets [1,2,3]     │ n, n-1, ...      │ 3              │ 2^3 = 8       │
 * │ Permutations [1,2,3]│ n, n-1, ...      │ 3              │ 3! = 6        │
 * │ Combinations C(4,2) │ 4, 3, 2          │ 2              │ C(4,2) = 6    │
 * └─────────────────────┴──────────────────┴────────────────┴───────────────┘
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * TIPS KHI VẼ TREE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * 1. VẼ TRÊN GIẤY TRƯỚC!
 *    - Không cần vẽ full tree, chỉ cần 2-3 levels
 *    - Giúp hiểu pattern
 * 
 * 2. MARK BASE CASES
 *    - Dùng ✓ cho valid leaves
 *    - Dùng ❌ cho pruned branches
 * 
 * 3. TRACK STATE
 *    - Ghi rõ state ở mỗi node (path, index, remaining,...)
 *    - VD: [1,2] (index=2, used=[T,T,F])
 * 
 * 4. VERIFY DFS ORDER
 *    - Read từ trái sang phải
 *    - Phải match expected output
 * 
 * 5. COUNT NODES
 *    - Số leaves = số solutions
 *    - Total nodes = complexity estimate
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * COMMON MISTAKES
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * ❌ MISTAKE 1: Quên base case
 *    → Tree vô tận, stack overflow!
 * 
 * ❌ MISTAKE 2: Sai branching (i+1 vs i)
 *    → Duplicates hoặc missing solutions
 * 
 * ❌ MISTAKE 3: Không track state
 *    → Conflict (VD: permutations dùng số 2 lần)
 * 
 * ❌ MISTAKE 4: Quên backtrack (UNDO)
 *    → Path bị polluted, sai kết quả
 * 
 * ❌ MISTAKE 5: Sai validation
 *    → Invalid solutions được add
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PRACTICE WORKFLOW
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * 1. ĐỌC ĐỀ → Xác định:
 *    - What to generate?
 *    - Constraints?
 *    - Expected output?
 * 
 * 2. VẼ TREE → Paper/whiteboard:
 *    - Root, levels, branches, leaves
 *    - Mark valid solutions
 * 
 * 3. IDENTIFY PATTERN:
 *    - Subsets? Permutations? Combinations? Other?
 *    - Reuse allowed? Order matters?
 * 
 * 4. WRITE TEMPLATE:
 *    - Base case
 *    - Choices (loop)
 *    - MAKE → EXPLORE → UNDO
 * 
 * 5. TEST:
 *    - Small input first (n=2)
 *    - Verify với tree đã vẽ
 *    - Add debug prints if needed
 * 
 * 6. OPTIMIZE:
 *    - Add pruning
 *    - Early termination
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * EXERCISES TO PRACTICE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * See BacktrackingExercises.java for:
 * 1. Binary strings
 * 2. K-length strings
 * 3. Subsets
 * 4. Permutations
 * 5. Combinations
 * 6. Palindrome partition
 * 7. Combination sum
 * 8. Valid IP addresses
 * Bonus: N-Queens
 * 
 * Practice cách vẽ tree cho MỖI bài!
 */
public class DecisionTreeGuide {
    // This is a documentation class - no implementation needed
}
