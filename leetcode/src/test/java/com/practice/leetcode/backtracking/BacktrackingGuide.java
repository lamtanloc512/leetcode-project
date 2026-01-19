package com.practice.leetcode.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ BACKTRACKING GUIDE ║
 * ║ (Quay lui) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ BACKTRACKING là gì? │
 * │ │
 * │ - Thử TẤT CẢ các khả năng theo cấu trúc DECISION TREE │
 * │ - Khi gặp ngõ cụt (không thỏa điều kiện) → QUAY LUI và thử tiếp │
 * │ - Dùng cho: Subsets, Permutations, Combinations, Sudoku, N-Queens │
 * │ │
 * │ Visualize Decision Tree cho Subsets [1,2,3]: │
 * │ [] │
 * │ / \ │
 * │ [1] [] │
 * │ / \ / \ │
 * │ [1,2] [1] [2] [] │
 * │ / \ / \ / \ / \ │
 * │ [1,2,3] [1,2].. [2,3] [2] [3] [] │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class BacktrackingGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE CHUNG
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CÔNG THỨC BACKTRACKING:
   *
   * void backtrack(path, choices) {
   * // Base case: đã hoàn thành 1 solution
   * if (isComplete(path)) {
   * result.add(new ArrayList<>(path)); // Copy path!
   * return;
   * }
   *
   * for (choice : choices) {
   * if (isValid(choice)) {
   * // 1. MAKE: Thêm choice vào path
   * path.add(choice);
   *
   * // 2. EXPLORE: Đệ quy với state mới
   * backtrack(path, newChoices);
   *
   * // 3. UNDO: Bỏ choice ra (BACKTRACK!)
   * path.remove(path.size() - 1);
   * }
   * }
   * }
   *
   * BA BƯỚC QUAN TRỌNG: MAKE → EXPLORE → UNDO
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 1: SUBSETS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán: Tìm TẤT CẢ subsets của mảng
   *
   * Đặc điểm:
   * - Mỗi phần tử: CHỌN hoặc KHÔNG CHỌN
   * - Không quan tâm thứ tự
   * - Kết quả = 2^n subsets
   *
   * Ví dụ: [1,2,3] → [[], [1], [2], [3], [1,2], [1,3], [2,3], [1,2,3]]
   */
  List<List<Integer>> subsets(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    backtrackSubsets(nums, 0, new ArrayList<>(), result);
    return result;
  }

  void backtrackSubsets(int[] nums, int start,
      List<Integer> path,
      List<List<Integer>> result) {
    // Mỗi path đều là 1 valid subset
    result.add(new ArrayList<>(path));

    // Thử thêm từng phần tử từ start đến cuối
    for (int i = start; i < nums.length; i++) {
      path.add(nums[i]); // MAKE
      backtrackSubsets(nums, i + 1, path, result); // EXPLORE (i+1 để tránh duplicate)
      path.remove(path.size() - 1); // UNDO
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 2: PERMUTATIONS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán: Tìm TẤT CẢ permutations (hoán vị)
   *
   * Đặc điểm:
   * - Dùng TẤT CẢ phần tử, chỉ khác THỨ TỰ
   * - Cần track phần tử đã dùng (used[] hoặc swap)
   * - Kết quả = n! permutations
   *
   * Ví dụ: [1,2,3] → [[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]]
   */
  List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    boolean[] used = new boolean[nums.length];
    backtrackPermute(nums, used, new ArrayList<>(), result);
    return result;
  }

  void backtrackPermute(int[] nums, boolean[] used,
      List<Integer> path,
      List<List<Integer>> result) {
    // Base: đủ n phần tử
    if (path.size() == nums.length) {
      result.add(new ArrayList<>(path));
      return;
    }

    // Thử mọi phần tử chưa dùng
    for (int i = 0; i < nums.length; i++) {
      if (used[i])
        continue; // Skip đã dùng

      used[i] = true; // MAKE
      path.add(nums[i]);
      backtrackPermute(nums, used, path, result); // EXPLORE
      path.remove(path.size() - 1); // UNDO
      used[i] = false;
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 3: COMBINATIONS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán: Chọn k phần tử từ n phần tử
   *
   * Đặc điểm:
   * - Giống Subsets nhưng có điều kiện size = k
   * - Kết quả = C(n,k)
   *
   * Ví dụ: n=4, k=2 → [[1,2], [1,3], [1,4], [2,3], [2,4], [3,4]]
   */
  List<List<Integer>> combine(int n, int k) {
    List<List<Integer>> result = new ArrayList<>();
    backtrackCombine(n, k, 1, new ArrayList<>(), result);
    return result;
  }

  void backtrackCombine(int n, int k, int start,
      List<Integer> path,
      List<List<Integer>> result) {
    // Base: đủ k phần tử
    if (path.size() == k) {
      result.add(new ArrayList<>(path));
      return;
    }

    // Pruning: còn đủ phần tử để chọn không?
    // Cần thêm (k - path.size()) phần tử
    // Còn (n - i + 1) phần tử
    for (int i = start; i <= n - (k - path.size()) + 1; i++) {
      path.add(i); // MAKE
      backtrackCombine(n, k, i + 1, path, result); // EXPLORE
      path.remove(path.size() - 1); // UNDO
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 4: COMBINATION SUM
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán: Tìm tổ hợp có tổng = target
   *
   * Biến thể:
   * - Combination Sum I: mỗi số dùng NHIỀU LẦN
   * - Combination Sum II: mỗi số dùng MỘT LẦN, có duplicates
   *
   * Key: Kiểm tra remaining sum thay vì chỉ kiểm tra size
   */
  List<List<Integer>> combinationSum(int[] candidates, int target) {
    List<List<Integer>> result = new ArrayList<>();
    backtrackCombSum(candidates, target, 0, new ArrayList<>(), result);
    return result;
  }

  void backtrackCombSum(int[] candidates, int remaining, int start,
      List<Integer> path,
      List<List<Integer>> result) {
    // Base case
    if (remaining == 0) {
      result.add(new ArrayList<>(path));
      return;
    }
    if (remaining < 0)
      return; // Pruning

    for (int i = start; i < candidates.length; i++) {
      path.add(candidates[i]);
      // i (không phải i+1) vì có thể dùng lại
      backtrackCombSum(candidates, remaining - candidates[i], i, path, result);
      path.remove(path.size() - 1);
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 5: STRING/PARTITION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán: Chia string thành các phần thỏa điều kiện
   *
   * Ví dụ: Palindrome Partitioning - chia "aab" thành các palindrome
   * → [["a","a","b"], ["aa","b"]]
   */
  List<List<String>> partition(String s) {
    List<List<String>> result = new ArrayList<>();
    backtrackPartition(s, 0, new ArrayList<>(), result);
    return result;
  }

  void backtrackPartition(String s, int start,
      List<String> path,
      List<List<String>> result) {
    if (start == s.length()) {
      result.add(new ArrayList<>(path));
      return;
    }

    for (int end = start + 1; end <= s.length(); end++) {
      String substring = s.substring(start, end);
      if (isPalindrome(substring)) {
        path.add(substring);
        backtrackPartition(s, end, path, result);
        path.remove(path.size() - 1);
      }
    }
  }

  boolean isPalindrome(String s) {
    int l = 0, r = s.length() - 1;
    while (l < r) {
      if (s.charAt(l++) != s.charAt(r--))
        return false;
    }
    return true;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BẢNG CHỌN PATTERN
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌──────────────────────────────────────────────────────────────────────────┐
   * │ Loại bài │ Pattern │ Key │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Tất cả subsets │ Subsets │ i+1, không cần base case │
   * │ Tất cả hoán vị │ Permutations │ used[] track, size == n │
   * │ Chọn k từ n │ Combinations │ i+1, size == k │
   * │ Tổ hợp có tổng │ Combination Sum │ remaining, i hoặc i+1 │
   * │ Chia string │ Partition │ substring(start, end) │
   * │ N-Queens, Sudoku │ Grid │ isValid() check │
   * └──────────────────────────────────────────────────────────────────────────┘
   *
   *
   * PRUNING (Tỉa nhánh) - RẤT QUAN TRỌNG!
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ 1. Skip duplicates: if (i > start && nums[i] == nums[i-1]) continue; │
   * │ 2. Early termination: if (remaining < 0) return; │
   * │ 3. Bound check: if (path.size() + remaining < k) return; │
   * │ 4. Sort first: Cho phép pruning hiệu quả hơn │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   *
   * CÁC BÀI THỰC HÀNH
   *
   * EASY/MEDIUM:
   * - Subsets (78)
   * - Permutations (46)
   * - Combinations (77)
   * - Combination Sum (39, 40)
   *
   * MEDIUM:
   * - Letter Combinations of Phone (17)
   * - Palindrome Partitioning (131)
   * - Generate Parentheses (22)
   *
   * HARD:
   * - N-Queens (51)
   * - Sudoku Solver (37)
   * - Word Search II (212)
   */
}
