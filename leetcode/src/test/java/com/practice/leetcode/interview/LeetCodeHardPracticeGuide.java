package com.practice.leetcode.interview;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════════════════════════╗
 * ║                      LEETCODE HARD PRACTICE GUIDE (DÀNH CHO SENIOR)                              ║
 * ║                   Chiến lược chinh phục bài Hard trong phỏng vấn                                 ║
 * ╚══════════════════════════════════════════════════════════════════════════════════════════════════╝
 *
 * 📊 MINDSET KHI GẶP BÀI HARD:
 * 1. Đừng hoảng sợ: Bài Hard thường là tổ hợp của 2-3 bài Medium hoặc một bài Medium với constraints khó.
 * 2. Simplify: Thử giải quyết phiên bản đơn giản hơn (ví dụ: bài toán trên cây -> bài toán trên mảng).
 * 3. Pattern Recognition: Nhận diện pattern (DP, Graph, Backtracking, Sliding Window, etc.).
 * 4. Constraints Analysis: Nhìn vào constraints để đoán độ phức tạp (N <= 20 -> O(2^N), N <= 10^5 -> O(NlogN)).
 *
 * 📅 KẾ HOẠCH LUYỆN TẬP 8 TUẦN (Mỗi ngày 1-2 bài):
 *
 * TUẦN 1: ARRAY & TWO POINTERS & SLIDING WINDOW (Nâng cao)
 * - [ ] LC 4: Median of Two Sorted Arrays (Binary Search upgrade)
 * - [ ] LC 41: First Missing Positive (Array manipulation trick)
 * - [ ] LC 42: Trapping Rain Water (Classic Hard)
 * - [ ] LC 76: Minimum Window Substring (Sliding Window template)
 * - [ ] LC 239: Sliding Window Maximum (Monotonic Queue)
 *
 * TUẦN 2: DYNAMIC PROGRAMMING (Cơ bản - 1D/2D)
 * - [ ] LC 72: Edit Distance (String DP)
 * - [ ] LC 312: Burst Balloons (Interval DP - Tư duy ngược)
 * - [ ] LC 32: Longest Valid Parentheses (DP/Stack)
 * - [ ] LC 10: Regular Expression Matching (String DP phức tạp)
 * - [ ] LC 123: Best Time to Buy and Sell Stock III (State Machine DP)
 *
 * TUẦN 3: GRAPH & UNION FIND
 * - [ ] LC 127: Word Ladder (BFS tìm đường ngắn nhất nhưng state là string)
 * - [ ] LC 126: Word Ladder II (BFS + Backtracking để print path)
 * - [ ] LC 269: Alien Dictionary (Topological Sort)
 * - [ ] LC 778: Swim in Rising Water (Dijkstra/Mẫu BFS + Binary Search)
 * - [ ] LC 329: Longest Increasing Path in a Matrix (DFS + Memoization)
 *
 * TUẦN 4: TREE & RECURSION
 * - [ ] LC 124: Binary Tree Maximum Path Sum (Top-down logic)
 * - [ ] LC 297: Serialize and Deserialize Binary Tree (BFS/DFS traversal)
 * - [ ] LC 968: Binary Tree Cameras (Greedy trên cây)
 * - [ ] LC 99: Recover Binary Search Tree (Morris Traversal - O(1) space)
 * - [ ] LC 295: Find Median from Data Stream (Heap design)
 *
 * TUẦN 5: BACKTRACKING & TRIE
 * - [ ] LC 51: N-Queens (Classic Backtracking)
 * - [ ] LC 37: Sudoku Solver (Backtracking with logic)
 * - [ ] LC 212: Word Search II (Backtracking + Trie optimization)
 * - [ ] LC 472: Concatenated Words (DFS + Trie/Set)
 *
 * TUẦN 6: HEAP & GREEDY & INTERVALS
 * - [ ] LC 23: Merge k Sorted Lists (Heap/Divide & Conquer)
 * - [ ] LC 218: The Skyline Problem (Sweep Line + Heap/TreeMap)
 * - [ ] LC 632: Smallest Range Covering Elements from K Lists (Heap ~ Sliding Window)
 * - [ ] LC 135: Candy (Two pass Greedy)
 * - [ ] LC 45: Jump Game II (Greedy BFS)
 *
 * TUẦN 7: ADVANCED DATA STRUCTURES (Segment Tree, Binary Index Tree)
 * - [ ] LC 307: Range Sum Query - Mutable (Fenwick Tree/Segment Tree)
 * - [ ] LC 315: Count of Smaller Numbers After Self (Merge Sort/BIT)
 * - [ ] LC 493: Reverse Pairs (Merge Sort modification)
 * - [ ] LC 84: Largest Rectangle in Histogram (Monotonic Stack)
 * - [ ] LC 85: Maximal Rectangle (Extension of LC 84)
 *
 * TUẦN 8: MOCK INTERVIEW & REVIEW
 * - Pick random 3 problems (1 Easy, 1 Medium, 1 Hard) solve in 45 mins.
 * - Focus on explaining thinking process.
 */
public class LeetCodeHardPracticeGuide {

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                  PROBLEM 1: MERGE K SORTED LISTS
    // ════════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * LC 23 - MERGE K SORTED LISTS ⭐⭐⭐ [HARD]
     * https://leetcode.com/problems/merge-k-sorted-lists/
     *
     * TẠI SAO QUAN TRỌNG?
     * - Kiểm tra hiểu biết về Heap (PriorityQueue).
     * - Có thể hỏi follow-up về Divide & Conquer (như Merge Sort).
     * - Độ phức tạp: O(N log K) với N là tổng số nodes, K là số lists.
     *
     * TƯ DUY (Min-Heap Approach):
     * 1. Luôn cần lấy node nhỏ nhất trong số các heads của K lists.
     * 2. Dùng Min-Heap để duy trì K heads hiện tại.
     * 3. Lấy min ra, add next node của list đó vào heap.
     */
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;

        // Min-Heap so sánh theo val của ListNode
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.val));

        // Thêm head của tất cả non-empty lists vào heap
        for (ListNode list : lists) {
            if (list != null) {
                pq.offer(list);
            }
        }

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        while (!pq.isEmpty()) {
            ListNode minNode = pq.poll();
            tail.next = minNode;
            tail = tail.next;

            if (minNode.next != null) {
                pq.offer(minNode.next);
            }
        }

        return dummy.next;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                  PROBLEM 2: WORD SEARCH II
    // ════════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * LC 212 - WORD SEARCH II ⭐⭐⭐ [HARD]
     * https://leetcode.com/problems/word-search-ii/
     *
     * TẠI SAO QUAN TRỌNG?
     * - Kết hợp Backtracking (DFS) và Trie (Prefix Tree).
     * - Tối ưu hóa search space cực tốt (Pruning).
     * - Bài toán thực tế: Boggle game solver.
     *
     * TƯ DUY:
     * 1. Xây dựng Trie từ danh sách words cần tìm.
     * 2. Duyệt từng ô trên board, DFS theo các hướng.
     * 3. Nếu path hiện tại match node trên Trie -> tiếp tục.
     * 4. Nếu tìm thấy word (isEnd = true) -> add result, đánh dấu đã tìm để tránh duplicate.
     * 5. Optimization: Cắt tỉa nhánh Trie khi đã tìm hết word con.
     */
    public List<String> findWords(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();
        TrieNode root = buildTrie(words);
        int m = board.length;
        int n = board[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dfs(board, i, j, root, result);
            }
        }
        return result;
    }

    private void dfs(char[][] board, int i, int j, TrieNode p, List<String> result) {
        char c = board[i][j];
        if (c == '#' || p.next[c - 'a'] == null) return; // Đã visit hoặc không có trong Trie

        p = p.next[c - 'a'];
        if (p.word != null) { // Tìm thấy 1 từ
            result.add(p.word);
            p.word = null; // Tranh duplicate: tìm rồi thì xóa marker đi
        }

        board[i][j] = '#'; // Mark visited
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int k = 0; k < 4; k++) {
            int ni = i + dx[k];
            int nj = j + dy[k];
            if (ni >= 0 && ni < board.length && nj >= 0 && nj < board[0].length) {
                dfs(board, ni, nj, p, result);
            }
        }

        board[i][j] = c; // Backtrack
    }

    private TrieNode buildTrie(String[] words) {
        TrieNode root = new TrieNode();
        for (String w : words) {
            TrieNode p = root;
            for (char c : w.toCharArray()) {
                int i = c - 'a';
                if (p.next[i] == null) p.next[i] = new TrieNode();
                p = p.next[i];
            }
            p.word = w; // Lưu word ở node cuối
        }
        return root;
    }

    class TrieNode {
        TrieNode[] next = new TrieNode[26];
        String word;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                  PROBLEM 3: N-QUEENS
    // ════════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * LC 51 - N-QUEENS ⭐⭐⭐ [HARD]
     * https://leetcode.com/problems/n-queens/
     *
     * TẠI SAO QUAN TRỌNG?
     * - Bài toán kinh điển về Backtracking.
     * - Học cách validate trạng thái nhanh (dùng Set hoặc boolean array).
     *
     * TƯ DUY:
     * - Đặt Queen từng hàng (row).
     * - Tại mỗi row, thử đặt vào từng col.
     * - Check valid: Cột dọc, đường chéo chính (row - col), đường chéo phụ (row + col).
     */
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        char[][] board = new char[n][n];
        for (int i = 0; i < n; i++) Arrays.fill(board[i], '.');
        
        boolean[] cols = new boolean[n];
        boolean[] diag1 = new boolean[2 * n]; // row - col
        boolean[] diag2 = new boolean[2 * n]; // row + col
        
        backtrackNQueens(0, n, board, result, cols, diag1, diag2);
        return result;
    }

    private void backtrackNQueens(int r, int n, char[][] board, List<List<String>> result,
                                  boolean[] cols, boolean[] diag1, boolean[] diag2) {
        if (r == n) {
            result.add(constructBoard(board));
            return;
        }

        for (int c = 0; c < n; c++) {
            int id1 = r - c + n; // Offset để tránh âm cho diag1
            int id2 = r + c;
            
            if (!cols[c] && !diag1[id1] && !diag2[id2]) {
                board[r][c] = 'Q';
                cols[c] = true;
                diag1[id1] = true;
                diag2[id2] = true;

                backtrackNQueens(r + 1, n, board, result, cols, diag1, diag2);

                // Backtrack
                board[r][c] = '.';
                cols[c] = false;
                diag1[id1] = false;
                diag2[id2] = false;
            }
        }
    }

    private List<String> constructBoard(char[][] board) {
        List<String> res = new ArrayList<>();
        for (char[] row : board) res.add(new String(row));
        return res;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                  PROBLEM 4: THE SKYLINE PROBLEM
    // ════════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * LC 218 - THE SKYLINE PROBLEM ⭐⭐⭐ [HARD]
     * https://leetcode.com/problems/the-skyline-problem/
     *
     * TẠI SAO QUAN TRỌNG?
     * - Kỹ thuật Sweep Line (Quét đường thẳng).
     * - Sử dụng PriorityQueue/TreeMap để quản lý "active heights".
     * - Xử lý edge cases cực nhiều (trùng điểm đầu, trùng điểm cuối, cùng height).
     *
     * TƯ DUY:
     * 1. Biến mỗi tòa nhà thành 2 sự kiện: (left, -height) và (right, height).
     *    - Tại sao left là âm? Để khi sort, nếu trùng x, sự kiện START (bắt đầu tòa nhà) được xử lý trước.
     *    - Nếu trùng x và đều là START, tòa cao hơn xử lý trước (do -height bé hơn sẽ đứng trước).
     * 2. Sort các sự kiện theo x.
     * 3. Duyệt sự kiện:
     *    - Gặp START: push height vào Heap.
     *    - Gặp END: remove height khỏi Heap.
     * 4. Nếu max height trong Heap thay đổi -> có key point mới của skyline.
     */
    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        List<int[]> points = new ArrayList<>();

        for (int[] b : buildings) {
            points.add(new int[]{b[0], -b[2]}); // Start: height âm
            points.add(new int[]{b[1], b[2]});  // End: height dương
        }

        // Sort: tăng dần x. Nếu x bằng nhau, sort theo h tăng dần.
        // - Start (-h) < Start (-h') nếu h > h' -> Tòa cao hơn start trước.
        // - Start (-h) < End (h') -> Start trước End.
        // - End (h) < End (h') -> Tòa thấp hơn end trước (thật ra end thứ tự ko quá qtrong bằng start/start).
        points.sort((a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            return a[1] - b[1];
        });

        // Max heap lưu heights active. Thêm 0 làm ground level.
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
        pq.offer(0);
        int prevMax = 0;

        for (int[] p : points) {
            int x = p[0];
            int h = p[1];

            if (h < 0) { // Start point
                pq.offer(-h);
            } else { // End point
                pq.remove(h); // O(N) trong Java PQ, TreeMap O(logN) sẽ tốt hơn nếu N lớn
            }

            int currMax = pq.peek();
            if (currMax != prevMax) {
                result.add(Arrays.asList(x, currMax));
                prevMax = currMax;
            }
        }

        return result;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                         TESTS
    // ════════════════════════════════════════════════════════════════════════════════════════════════

    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    @Test
    public void testMergeKLists() {
        ListNode l1 = new ListNode(1); l1.next = new ListNode(4); l1.next.next = new ListNode(5);
        ListNode l2 = new ListNode(1); l2.next = new ListNode(3); l2.next.next = new ListNode(4);
        ListNode l3 = new ListNode(2); l3.next = new ListNode(6);

        ListNode res = mergeKLists(new ListNode[]{l1, l2, l3});
        List<Integer> vals = new ArrayList<>();
        while(res != null) { vals.add(res.val); res = res.next; }
        
        assertEquals(Arrays.asList(1, 1, 2, 3, 4, 4, 5, 6), vals);
    }

    @Test
    public void testWordSearchII() {
        char[][] board = {
            {'o','a','a','n'},
            {'e','t','a','e'},
            {'i','h','k','r'},
            {'i','f','l','v'}
        };
        String[] words = {"oath","pea","eat","rain"};
        List<String> res = findWords(board, words);
        assertTrue(res.contains("oath"));
        assertTrue(res.contains("eat"));
        assertFalse(res.contains("pea"));
    }

    @Test
    public void testNQueens() {
        List<List<String>> res = solveNQueens(4);
        assertEquals(2, res.size()); // 4-Queens có 2 solutions
    }

    @Test
    public void testSkyline() {
        int[][] buildings = {{2,9,10}, {3,7,15}, {5,12,12}, {15,20,10}, {19,24,8}};
        List<List<Integer>> res = getSkyline(buildings);
        // Expect: [[2,10], [3,15], [7,12], [12,0], [15,10], [20,8], [24,0]]
        assertEquals(Arrays.asList(2, 10), res.get(0));
        assertEquals(Arrays.asList(3, 15), res.get(1));
        assertEquals(Arrays.asList(7, 12), res.get(2));
        assertEquals(Arrays.asList(24, 0), res.get(res.size()-1));
    }
}
