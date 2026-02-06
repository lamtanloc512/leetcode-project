package com.practice.leetcode.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * BACKTRACKING EXERCISES - PRACTICE SKELETON
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Các bài tập để practice backtracking pattern.
 * 
 * FORMAT:
 * - Main class này: SKELETON CODE (bạn implement)
 * - Test class: SOLUTION (để check)
 * 
 * HƯỚNG DẪN:
 * 1. Đọc đề bài
 * 2. Vẽ decision tree trên giấy
 * 3. Implement theo template
 * 4. Run test để check
 * 
 * TEMPLATE BACKTRACKING:
 * 
 * void backtrack(path, choices, result) {
 *     // BASE CASE: Complete?
 *     if (isComplete(path)) {
 *         result.add(new ArrayList<>(path));
 *         return;
 *     }
 *     
 *     // CHOICES: Try each choice
 *     for (choice : choices) {
 *         if (isValid(choice)) {
 *             path.add(choice);              // ⭐ MAKE
 *             backtrack(path, ..., result);  // ⭐ EXPLORE
 *             path.remove(path.size() - 1);  // ⭐ UNDO
 *         }
 *     }
 * }
 */
public class BacktrackingExercises {

    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 1: GENERATE ALL BINARY STRINGS OF LENGTH N
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Sinh tất cả binary strings có độ dài n.
     * 
     * VÍ DỤ:
     * n = 2 → ["00", "01", "10", "11"]
     * n = 3 → ["000", "001", "010", "011", "100", "101", "110", "111"]
     * 
     * VẼ DECISION TREE cho n=2:
     * 
     *           ""
     *         /    \
     *       "0"    "1"
     *      /  \    /  \
     *    "00" "01" "10" "11"
     *     ✓    ✓    ✓    ✓
     * 
     * HƯỚNG DẪN:
     * - Base case: current.length() == n
     * - Choices: Add '0' hoặc '1'
     * - No constraints!
     * 
     * TIME: O(2^n)
     * SPACE: O(n) recursion
     */
    public List<String> generateBinaryStrings(int n) {
        List<String> result = new ArrayList<>();
        backtrackBinary(result, new StringBuilder(), n);
        return result;
    }

    
    private void backtrackBinary(List<String> result, StringBuilder current, int n) {
      if(current.length() == n) {
        result.add(current.toString());
        return;
      }
      for(char c : "01".toCharArray()) {
        current.append(c);
        backtrackBinary(result, current, n);
        current.deleteCharAt(current.length() - 1);
      }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 2: GENERATE ALL K-LENGTH STRINGS FROM ALPHABET
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho alphabet (VD: "abc") và số k.
     * Sinh tất cả strings có độ dài k từ alphabet đó.
     * 
     * VÍ DỤ:
     * alphabet = "ab", k = 2 → ["aa", "ab", "ba", "bb"]
     * alphabet = "abc", k = 2 → ["aa", "ab", "ac", "ba", "bb", "bc", "ca", "cb", "cc"]
     * 
     * VẼ DECISION TREE cho alphabet="ab", k=2:
     * 
     *              ""
     *           /      \
     *         "a"      "b"
     *        /  \      /  \
     *      "aa" "ab" "ba" "bb"
     *       ✓    ✓    ✓    ✓
     * 
     * HƯỚNG DẪN:
     * - Base case: current.length() == k
     * - Choices: Mỗi ký tự trong alphabet
     * - Cho phép reuse (aa, bb,...)
     * 
     * TIME: O(alphabet.length^k)
     * SPACE: O(k) recursion
     */
    public List<String> generateKLengthStrings(String alphabet, int k) {
        List<String> result = new ArrayList<>();
        generateDfs(0, k, alphabet, new StringBuilder(), result);
        return result;
    }
    
    private void generateDfs(int index, int K, String alpha, StringBuilder bd, List<String> result) {
      if(bd.length() == K) {
        result.add(bd.toString());
      }

      for(char c : alpha.toCharArray()){
        bd.append(c);
        generateDfs(index + 1, K, alpha, bd, result);
        bd.deleteCharAt(bd.length() - 1);
      }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 3: GENERATE ALL SUBSETS (POWER SET)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho mảng nums, sinh tất cả subsets (power set).
     * 
     * VÍ DỤ:
     * nums = [1,2] → [[], [1], [2], [1,2]]
     * nums = [1,2,3] → [[], [1], [2], [3], [1,2], [1,3], [2,3], [1,2,3]]
     * 
     * VẼ DECISION TREE cho [1,2,3]:
     * 
     *                        []
     *            ┌────────────┼────────────┐
     *           [1]          [2]          [3]
     *        ┌───┴───┐       │
     *      [1,2]   [1,3]   [2,3]
     *        │
     *     [1,2,3]
     * 
     * Mỗi node = 1 valid subset!
     * 
     * HƯỚNG DẪN:
     * - Add current vào result NGAY ở đầu (không cần base case!)
     * - Choices: Từ start đến end
     * - Use start để tránh duplicate ([1,2] = [2,1])
     * 
     * TIME: O(2^n)
     * SPACE: O(n) recursion
     */
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

    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 4: GENERATE ALL PERMUTATIONS
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho mảng nums, sinh tất cả permutations (hoán vị).
     * 
     * VÍ DỤ:
     * nums = [1,2] → [[1,2], [2,1]]
     * nums = [1,2,3] → [[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]]
     * 
     * VẼ DECISION TREE cho [1,2,3]:
     * 
     *                        []
     *            ┌────────────┼────────────┐
     *           [1]          [2]          [3]
     *        ┌───┴───┐    ┌───┴───┐    ┌───┴───┐
     *      [1,2] [1,3]  [2,1] [2,3]  [3,1] [3,2]
     *        │     │      │     │      │     │
     *     [1,2,3][1,3,2][2,1,3][2,3,1][3,1,2][3,2,1]
     *        ✓     ✓      ✓     ✓      ✓     ✓
     * 
     * HƯỚNG DẪN:
     * - Base case: path.size() == nums.length
     * - Choices: Tất cả elements chưa dùng
     * - Cần boolean[] used để track
     * 
     * TIME: O(n!)
     * SPACE: O(n) recursion + used[]
     */
    public List<List<Integer>> permutations(int[] nums) {
      List<List<Integer>> result = new ArrayList<>();
      boolean[] used = new boolean[nums.length];
      permutationBacktrack(0, nums, used, new ArrayList<>(), result);
      return result;
    }

    private void permutationBacktrack(
        int index,
        int[] nums,
        boolean[] used,
        List<Integer> path,
        List<List<Integer>> result) {
      if (path.size() == nums.length) {
        result.add(new ArrayList<>(path));
        return;
      }
      
      for (int i = index; i < nums.length; i++) {
        if (used[i])
          continue;
        used[i] = true;
        path.add(nums[i]);
        permutationBacktrack(i + 1, nums, used, path, result);
        path.remove(path.size() - 1);
        used[i] = false;
      }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 5: GENERATE ALL COMBINATIONS (N CHOOSE K)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho n và k, sinh tất cả combinations chọn k số từ [1..n].
     * 
     * VÍ DỤ:
     * n=4, k=2 → [[1,2], [1,3], [1,4], [2,3], [2,4], [3,4]]
     * n=3, k=2 → [[1,2], [1,3], [2,3]]
     * 
     * VẼ DECISION TREE cho n=4, k=2:
     * 
     *                   []
     *        ┌───────┬───┴───┬───────┐
     *       [1]     [2]     [3]     [4]
     *      ┌─┴─┬─┐  │  │     │
     *    [1,2][1,3][1,4][2,3][2,4] [3,4]
     *      ✓    ✓    ✓    ✓    ✓     ✓
     * 
     * HƯỚNG DẪN:
     * - Base case: path.size() == k
     * - Choices: Từ start đến n
     * - Pruning: Nếu không đủ số còn lại → skip!
     * 
     * TIME: O(C(n,k))
     * SPACE: O(k) recursion
     */
    public static List<List<Integer>> combinations(int n, int k) {
      List<List<Integer>> result = new ArrayList<>();
      compBacktrack(1, n, k, new ArrayList<>(), result);
      return result;
    }

    private static void compBacktrack(
        int index,
        int n,
        int k,
        List<Integer> path,
        List<List<Integer>> result) {
      if (path.size() == k) {
        result.add(new ArrayList<>(path));
        return;
      }

      for (int i = index; i <= n; i++) {
        path.add(i);
        compBacktrack(i + 1, n, k, path, result);
        path.remove(path.size() - 1);
      }

    }

    public static void main(String[] args) {
      System.out.println(combinations(4, 2));
      
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 6: PARTITION STRING INTO PALINDROMES
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Chia string thành các substrings sao cho mỗi substring là palindrome.
     * 
     * VÍ DỤ:
     * s = "aab" → [["a","a","b"], ["aa","b"]]
     * s = "abc" → [["a","b","c"]]
     * 
     * VẼ DECISION TREE cho "aab":
     * 
     *                    ""
     *          ┌─────────┼─────────┐
     *         "a"       "aa"      "aab"❌
     *       ┌───┴───┐     │     (not palindrome)
     *      "a"    "ab"❌  "b"
     *       │           ✓["aa","b"]
     *      "b"
     *     ✓["a","a","b"]
     * 
     * HƯỚNG DẪN:
     * - Base case: start == s.length()
     * - Choices: Mỗi substring từ start đến end
     * - Check: Chỉ thêm nếu substring là palindrome
     * 
     * TIME: O(n * 2^n)
     * SPACE: O(n) recursion
     */
    public List<List<String>> partitionPalindromes(String s) {
        List<List<String>> result = new ArrayList<>();
        // TODO: Implement backtracking
        return result;
    }
    
    // TODO: Implement helper method + isPalindrome()
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 7: COMBINATION SUM (REUSE ALLOWED)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho mảng candidates và target, tìm tất cả combinations có tổng = target.
     * Cho phép dùng số NHIỀU LẦN.
     * 
     * VÍ DỤ:
     * candidates = [2,3,5], target = 8
     * → [[2,2,2,2], [2,3,3], [3,5]]
     * 
     * VẼ DECISION TREE cho candidates=[2,3], target=5:
     * 
     *                    [] (remaining=5)
     *            ┌────────┴────────┐
     *          [2] (rem=3)       [3] (rem=2)
     *        ┌───┴───┐         ┌───┴───┐
     *      [2,2]   [2,3]     [3,3]❌  [3,2]❌
     *     (rem=1)  (rem=0)  (rem<0)  (rem<0)
     *        │       ✓
     *    [2,2,2]❌
     *   (rem<0)
     * 
     * HƯỚNG DẪN:
     * - Base case: remaining == 0 (found!) hoặc remaining < 0 (prune!)
     * - Choices: Mỗi candidate từ start
     * - Use start index = i (KHÔNG phải i+1) để allow reuse!
     * 
     * TIME: O(2^target)
     * SPACE: O(target) recursion
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        // TODO: Implement backtracking
        return result;
    }
    
    // TODO: Implement helper method
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 8: GENERATE VALID IP ADDRESSES
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho string chứa digits, tạo tất cả valid IP addresses.
     * 
     * IP address format: "a.b.c.d" where 0 <= a,b,c,d <= 255
     * No leading zeros (except "0" itself)
     * 
     * VÍ DỤ:
     * s = "25525511135" → ["255.255.11.135", "255.255.111.35"]
     * s = "101023" → ["1.0.10.23", "1.0.102.3", "10.1.0.23", "10.10.2.3", "101.0.2.3"]
     * 
     * VẼ DECISION TREE (simplified):
     * 
     *                    "" (parts=0)
     *            ┌────────┴────────┐
     *         "1." (p=1)        "10." (p=1)
     *        ┌───┴───┐          ┌───┴───┐
     *      "1.0."  "1.01."❌  "10.1."  ...
     *     (p=2)   (leading0)  (p=2)
     * 
     * HƯỚNG DẪN:
     * - Base case: 4 parts && used all digits
     * - Choices: Take 1, 2, or 3 digits
     * - Validate: 0 <= val <= 255, no leading zeros
     * 
     * TIME: O(3^4) = O(81)
     * SPACE: O(1) recursion (max 4 parts)
     */
    public List<String> generateValidIPs(String s) {
        List<String> result = new ArrayList<>();
        // TODO: Implement backtracking
        return result;
    }
    
    // TODO: Implement helper method + validation
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER NOTES
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * COMMON PATTERNS SUMMARY:
     * 
     * 1. SUBSETS: Add every node, loop from start
     * 2. PERMUTATIONS: Need used[], loop from 0
     * 3. COMBINATIONS: Check size, loop from start with pruning
     * 4. PARTITION: Try every substring, check validity
     * 5. SUM: Track remaining, prune on negative
     * 
     * DEBUGGING TIPS:
     * 
     * 1. Print decision tree manually first
     * 2. Add debug prints in backtrack:
     *    System.out.println("Level: " + path.size() + ", Path: " + path);
     * 3. Check base case first!
     * 4. Verify UNDO is called (path restored)
     * 5. Watch for off-by-one errors (i+1 vs i)
     */
}
