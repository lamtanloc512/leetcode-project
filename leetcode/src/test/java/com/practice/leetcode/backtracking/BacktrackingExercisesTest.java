package com.practice.leetcode.backtracking;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * BACKTRACKING EXERCISES - SOLUTIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Solutions cho BacktrackingExercises.
 * Dùng để check code của bạn!
 */
class BacktrackingExercisesTest {

  // ═══════════════════════════════════════════════════════════════════════════
  // EXERCISE 1: BINARY STRINGS - SOLUTION
  // ═══════════════════════════════════════════════════════════════════════════

  public List<String> generateBinaryStrings(int n) {
    List<String> result = new ArrayList<>();
    backtrackBinary(result, new StringBuilder(), n);
    return result;
  }

  // private void backtrackBinary(List<String> result, StringBuilder current, int
  // n) {
  // // BASE CASE: Đủ độ dài
  // if (current.length() == n) {
  // result.add(current.toString());
  // return;
  // }

  // // CHOICE 1: Add '0'
  // current.append('0');
  // backtrackBinary(result, current, n);
  // current.deleteCharAt(current.length() - 1); // UNDO

  // // CHOICE 2: Add '1'
  // current.append('1');
  // backtrackBinary(result, current, n);
  // current.deleteCharAt(current.length() - 1); // UNDO
  // }

  private void backtrackBinary(List<String> result, StringBuilder current, int n) {
    if (current.length() == n) {
      result.add(current.toString());
      return;
    }
    for (char c : "01".toCharArray()) {
      current.append(c);
      backtrackBinary(result, current, n); 
      current.deleteCharAt(current.length() - 1);
    }
  }

  @Test
  @DisplayName("Ex1: Binary n=2 → [\"00\", \"01\", \"10\", \"11\"]")
  void testBinaryStrings() {
    assertThat(generateBinaryStrings(2))
        .containsExactly("00", "01", "10", "11");

    assertThat(generateBinaryStrings(3))
        .containsExactly("000", "001", "010", "011", "100", "101", "110", "111");
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // EXERCISE 2: K-LENGTH STRINGS - SOLUTION
  // ═══════════════════════════════════════════════════════════════════════════

  public List<String> generateKLengthStrings(String alphabet, int k) {
    List<String> result = new ArrayList<>();
    backtrackKLength(result, new StringBuilder(), alphabet, k);
    return result;
  }

  private void backtrackKLength(List<String> result, StringBuilder current,
                                String alphabet, int k) {
    // BASE CASE
    if (current.length() == k) {
      result.add(current.toString());
      return;  // ⭐ MUST return! Không loop thêm!
    }

    // CHOICES: Mỗi ký tự trong alphabet (ALWAYS SAME!)
    // ⭐ Không cần index vì mỗi level đều loop cùng alphabet!
    for (char c : alphabet.toCharArray()) {
      current.append(c);                              // MAKE
      backtrackKLength(result, current, alphabet, k); // EXPLORE (no index!)
      current.deleteCharAt(current.length() - 1);     // UNDO
    }
  }

  @Test
  @DisplayName("Ex2: K-length \"ab\", k=2 → [\"aa\", \"ab\", \"ba\", \"bb\"]")
  void testKLengthStrings() {
    assertThat(generateKLengthStrings("ab", 2))
        .containsExactly("aa", "ab", "ba", "bb");

    assertThat(generateKLengthStrings("abc", 2))
        .hasSize(9); // 3^2
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // EXERCISE 3: SUBSETS - SOLUTION
  // ═══════════════════════════════════════════════════════════════════════════

  public List<List<Integer>> subsets(int[] nums) {
      List<List<Integer>> result = new ArrayList<>();
      List<Integer> path = new ArrayList<>();
      subsetsBacktrack(0, result, path,nums);
      return result;
  }
  
  private void subsetsBacktrack(int start, List<List<Integer>> result, List<Integer> path, int[] nums){
    result.add(new ArrayList<>(path));

    for(int i = start; i < nums.length; i++) {
      path.add(nums[i]);
      subsetsBacktrack(i + 1, result, path,nums);
      path.remove(path.size() - 1);
    }
    
  }

  @Test
  @DisplayName("Ex3: Subsets [1,2] → [[], [1], [2], [1,2]]")
  void testSubsets() {
    List<List<Integer>> result = subsets(new int[] { 1, 2 });
    assertThat(result).hasSize(4); // 2^2
    assertThat(result).contains(
        Arrays.asList(),
        Arrays.asList(1),
        Arrays.asList(2),
        Arrays.asList(1, 2));

    assertThat(subsets(new int[] { 1, 2, 3 })).hasSize(8); // 2^3
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // EXERCISE 4: PERMUTATIONS - SOLUTION
  // ═══════════════════════════════════════════════════════════════════════════

  public List<List<Integer>> permutations(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    boolean[] used = new boolean[nums.length];
    backtrackPermutations(result, new ArrayList<>(), nums, used);
    return result;
  }

  private void backtrackPermutations(List<List<Integer>> result, List<Integer> current,
      int[] nums, boolean[] used) {
    // BASE CASE: Đã dùng tất cả
    if (current.size() == nums.length) {
      result.add(new ArrayList<>(current));
      return;
    }

    // CHOICES: Mọi element chưa dùng
    for (int i = 0; i < nums.length; i++) {
      if (used[i])
        continue; // Skip đã dùng

      used[i] = true; // MAKE
      current.add(nums[i]);
      backtrackPermutations(result, current, nums, used); // EXPLORE
      current.remove(current.size() - 1); // UNDO
      used[i] = false;
    }
  }

  @Test
  @DisplayName("Ex4: Permutations [1,2] → [[1,2], [2,1]]")
  void testPermutations() {
    List<List<Integer>> result = permutations(new int[] { 1, 2 });
    assertThat(result).hasSize(2); // 2!
    assertThat(result).contains(
        Arrays.asList(1, 2),
        Arrays.asList(2, 1));

    assertThat(permutations(new int[] { 1, 2, 3 })).hasSize(6); // 3!
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // EXERCISE 5: COMBINATIONS - SOLUTION
  // ═══════════════════════════════════════════════════════════════════════════

  public List<List<Integer>> combinations(int n, int k) {
    List<List<Integer>> result = new ArrayList<>();
    backtrackCombinations(result, new ArrayList<>(), n, k, 1);
    return result;
  }

  private void backtrackCombinations(List<List<Integer>> result, List<Integer> current,
      int n, int k, int start) {
    // BASE CASE: Đủ k elements
    if (current.size() == k) {
      result.add(new ArrayList<>(current));
      return;
    }

    // ⭐ PRUNING: Cần thêm (k - current.size()) phần tử
    // Còn (n - i + 1) phần tử từ i đến n
    for (int i = start; i <= n - (k - current.size()) + 1; i++) {
      current.add(i); // MAKE
      backtrackCombinations(result, current, n, k, i + 1); // EXPLORE
      current.remove(current.size() - 1); // UNDO
    }
  }

  @Test
  @DisplayName("Ex5: Combinations n=4, k=2 → [[1,2], [1,3], [1,4], [2,3], [2,4], [3,4]]")
  void testCombinations() {
    List<List<Integer>> result = combinations(4, 2);
    assertThat(result).hasSize(6); // C(4,2) = 6
    assertThat(result).contains(
        Arrays.asList(1, 2),
        Arrays.asList(1, 3),
        Arrays.asList(1, 4),
        Arrays.asList(2, 3),
        Arrays.asList(2, 4),
        Arrays.asList(3, 4));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // EXERCISE 6: PALINDROME PARTITION - SOLUTION
  // ═══════════════════════════════════════════════════════════════════════════

  public List<List<String>> partitionPalindromes(String s) {
    List<List<String>> result = new ArrayList<>();
    backtrackPartition(result, new ArrayList<>(), s, 0);
    return result;
  }

  private void backtrackPartition(List<List<String>> result, List<String> current,
      String s, int start) {
    // BASE CASE: Đã xử lý hết string
    if (start == s.length()) {
      result.add(new ArrayList<>(current));
      return;
    }

    // CHOICES: Mỗi substring từ start
    for (int end = start + 1; end <= s.length(); end++) {
      String substring = s.substring(start, end);

      // ⭐ Chỉ thêm nếu là palindrome
      if (isPalindrome(substring)) {
        current.add(substring); // MAKE
        backtrackPartition(result, current, s, end); // EXPLORE
        current.remove(current.size() - 1); // UNDO
      }
    }
  }

  private boolean isPalindrome(String s) {
    int left = 0, right = s.length() - 1;
    while (left < right) {
      if (s.charAt(left++) != s.charAt(right--)) {
        return false;
      }
    }
    return true;
  }

  @Test
  @DisplayName("Ex6: Partition \"aab\" → [[\"a\",\"a\",\"b\"], [\"aa\",\"b\"]]")
  void testPartitionPalindromes() {
    List<List<String>> result = partitionPalindromes("aab");
    assertThat(result).hasSize(2);
    assertThat(result).contains(
        Arrays.asList("a", "a", "b"),
        Arrays.asList("aa", "b"));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // EXERCISE 7: COMBINATION SUM - SOLUTION
  // ═══════════════════════════════════════════════════════════════════════════

  public List<List<Integer>> combinationSum(int[] candidates, int target) {
    List<List<Integer>> result = new ArrayList<>();
    backtrackCombSum(result, new ArrayList<>(), candidates, target, 0);
    return result;
  }

  private void backtrackCombSum(List<List<Integer>> result, List<Integer> current,
      int[] candidates, int remaining, int start) {
    // BASE CASE 1: Found!
    if (remaining == 0) {
      result.add(new ArrayList<>(current));
      return;
    }

    // BASE CASE 2: Overshoot (pruning)
    if (remaining < 0) {
      return;
    }

    // CHOICES: Từ start (không phải start+1 để allow reuse!)
    for (int i = start; i < candidates.length; i++) {
      current.add(candidates[i]); // MAKE
      backtrackCombSum(result, current, candidates,
          remaining - candidates[i], i); // EXPLORE (i, not i+1!)
      current.remove(current.size() - 1); // UNDO
    }
  }

  @Test
  @DisplayName("Ex7: CombinationSum [2,3,5], target=8 → [[2,2,2,2], [2,3,3], [3,5]]")
  void testCombinationSum() {
    List<List<Integer>> result = combinationSum(new int[] { 2, 3, 5 }, 8);
    assertThat(result).hasSize(3);
    assertThat(result).contains(
        Arrays.asList(2, 2, 2, 2),
        Arrays.asList(2, 3, 3),
        Arrays.asList(3, 5));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // EXERCISE 8: VALID IP ADDRESSES - SOLUTION
  // ═══════════════════════════════════════════════════════════════════════════

  public List<String> generateValidIPs(String s) {
    List<String> result = new ArrayList<>();
    backtrackIP(result, new ArrayList<>(), s, 0);
    return result;
  }

  private void backtrackIP(List<String> result, List<String> parts, String s, int start) {
    // BASE CASE: 4 parts đủ
    if (parts.size() == 4) {
      if (start == s.length()) {
        // ⭐ Đã dùng hết string → Valid!
        result.add(String.join(".", parts));
      }
      return;
    }

    // CHOICES: Thử 1, 2, hoặc 3 digits
    for (int len = 1; len <= 3 && start + len <= s.length(); len++) {
      String part = s.substring(start, start + len);

      // ⭐ Validate
      if (isValidIPPart(part)) {
        parts.add(part); // MAKE
        backtrackIP(result, parts, s, start + len); // EXPLORE
        parts.remove(parts.size() - 1); // UNDO
      }
    }
  }

  private boolean isValidIPPart(String s) {
    // No leading zeros (except "0" itself)
    if (s.length() > 1 && s.charAt(0) == '0') {
      return false;
    }

    // Must be 0-255
    int val = Integer.parseInt(s);
    return val >= 0 && val <= 255;
  }

  @Test
  @DisplayName("Ex8: ValidIP \"25525511135\" → [\"255.255.11.135\", \"255.255.111.35\"]")
  void testGenerateValidIPs() {
    List<String> result = generateValidIPs("25525511135");
    assertThat(result).hasSize(2);
    assertThat(result).contains(
        "255.255.11.135",
        "255.255.111.35");
  }

  @Test
  @DisplayName("Ex8: ValidIP \"101023\" → 5 valid IPs")
  void testGenerateValidIPs2() {
    List<String> result = generateValidIPs("101023");
    assertThat(result).hasSize(5);
    assertThat(result).contains(
        "1.0.10.23",
        "1.0.102.3",
        "10.1.0.23",
        "10.10.2.3",
        "101.0.2.3");
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BONUS: N-QUEENS (HARD)
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * ĐỀ BÀI:
   * Đặt n quân hậu trên bàn cờ n×n sao cho không có 2 quân nào ăn nhau.
   * 
   * VÍ DỤ n=4:
   * 
   * Solution 1: Solution 2:
   * . Q . . . . Q .
   * . . . Q Q . . .
   * Q . . . . . . Q
   * . . Q . . Q . .
   * 
   * HƯỚNG DẪN:
   * - Đặt 1 quân mỗi row
   * - Check không conflict với các quân đã đặt
   * - Conflict: cùng column, diagonal, anti-diagonal
   */
  public List<List<String>> solveNQueens(int n) {
    List<List<String>> result = new ArrayList<>();
    char[][] board = new char[n][n];

    // Init board
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        board[i][j] = '.';
      }
    }

    backtrackNQueens(result, board, 0);
    return result;
  }

  private void backtrackNQueens(List<List<String>> result, char[][] board, int row) {
    // BASE CASE: Đã đặt hết n quân
    if (row == board.length) {
      result.add(constructBoard(board));
      return;
    }

    // CHOICES: Try mỗi column
    for (int col = 0; col < board.length; col++) {
      if (isValidNQueens(board, row, col)) {
        board[row][col] = 'Q'; // MAKE
        backtrackNQueens(result, board, row + 1); // EXPLORE
        board[row][col] = '.'; // UNDO
      }
    }
  }

  private boolean isValidNQueens(char[][] board, int row, int col) {
    // Check column (no need check row - we place 1 per row)
    for (int i = 0; i < row; i++) {
      if (board[i][col] == 'Q')
        return false;
    }

    // Check diagonal \
    for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
      if (board[i][j] == 'Q')
        return false;
    }

    // Check anti-diagonal /
    for (int i = row - 1, j = col + 1; i >= 0 && j < board.length; i--, j++) {
      if (board[i][j] == 'Q')
        return false;
    }

    return true;
  }

  private List<String> constructBoard(char[][] board) {
    List<String> result = new ArrayList<>();
    for (char[] row : board) {
      result.add(new String(row));
    }
    return result;
  }

  @Test
  @DisplayName("Bonus: N-Queens n=4 → 2 solutions")
  void testNQueens() {
    List<List<String>> result = solveNQueens(4);
    assertThat(result).hasSize(2);

    // First solution
    assertThat(result.get(0)).containsExactly(
        ".Q..",
        "...Q",
        "Q...",
        "..Q.");
  }

  @Test
  @DisplayName("Bonus: N-Queens n=8 → 92 solutions")
  void testNQueens8() {
    List<List<String>> result = solveNQueens(8);
    assertThat(result).hasSize(92); // Classic result!
  }
}
