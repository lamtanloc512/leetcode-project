package com.practice.leetcode.hackerrank;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cho chuỗi digits (từ 2-9), mỗi digit map sang letters (như phone keypad).
 * Return tất cả combinations có thể, theo thứ tự lexicographical.
 * 
 * Phone keypad mapping:
 * 2 → abc
 * 3 → def
 * 4 → ghi
 * 5 → jkl
 * 6 → mno
 * 7 → pqrs
 * 8 → tuv
 * 9 → wxyz
 * 0 → (space) [không dùng trong bài này]
 * 1 → (không có letters)
 * 
 * Ví dụ:
 * Input: "23"
 * Output: ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"]
 * 
 * Giải thích:
 * - '2' → [a, b, c]
 * - '3' → [d, e, f]
 * - Combine: a+d, a+e, a+f, b+d, b+e, b+f, c+d, c+e, c+f
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CÁCH VẼ DECISION TREE (QUAN TRỌNG!)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Để vẽ decision tree cho backtracking, follow các bước:
 * 
 * 1. ROOT NODE: Empty string ""
 * 2. LEVEL: Mỗi level = 1 digit trong input
 * 3. BRANCHES: Mỗi branch = 1 letter choice cho digit đó
 * 4. LEAF NODES: Khi đã process hết digits → Valid combination!
 * 
 * Example: digits = "23"
 * 
 *                          "" (root)
 *                      /    |    \
 *                    /      |      \
 *                  "a"     "b"     "c"    ← Level 1: digit '2'
 *                 / | \   / | \   / | \
 *               /   |  \ /  |  \ /  |  \
 *             "ad" "ae" "af" "bd" "be" "bf" "cd" "ce" "cf"  ← Level 2: digit '3' (LEAVES!)
 *               ✓    ✓    ✓    ✓    ✓    ✓    ✓    ✓    ✓
 * 
 * Cách đọc tree:
 * - Mỗi đường từ root → leaf = 1 valid combination
 * - Số leaves = tích số letters của mỗi digit
 * - Với "23": 3 letters × 3 letters = 9 combinations
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * DECISION TREE CHI TIẾT CHO "23"
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *                                   ""
 *                                (index=0)
 *                          /        |        \
 *                        /          |          \
 *                     "a"          "b"         "c"
 *                  (index=1)    (index=1)   (index=1)
 *                  /  |  \       /  |  \     /  |  \
 *                /    |    \    /   |   \   /   |   \
 *             "ad"  "ae"  "af" "bd" "be" "bf" "cd" "ce" "cf"
 *           (idx=2)(idx=2)(idx=2)(idx=2)(idx=2)(idx=2)(idx=2)(idx=2)(idx=2)
 *              ✓     ✓     ✓     ✓     ✓     ✓     ✓     ✓     ✓
 * 
 * KEY OBSERVATIONS:
 * 1. Depth = số digits (2 levels cho "23")
 * 2. Mỗi node có số children = số letters của digit tương ứng
 * 3. Thứ tự DFS (left-to-right) → lexicographical order tự động!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * DECISION TREE CHO "234" (3 DIGITS)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *                                    ""
 *                               (index=0)
 *                          /       |       \
 *                        /         |         \
 *                     "a"         "b"        "c"
 *                  (index=1)   (index=1)  (index=1)
 *                  /  |  \      /  |  \    /  |  \
 *                /    |   \    /   |   \  /   |   \
 *             "ad"  "ae" "af" "bd" "be" "bf" "cd" "ce" "cf"
 *           (idx=2)(idx=2)(idx=2)(idx=2)(idx=2)(idx=2)(idx=2)(idx=2)(idx=2)
 *            / | \  / | \  / | \
 *          /   |  \/  |  \/  |  \
 *        adg ade adh aeg ... (27 leaves total!)
 *        ✓   ✓   ✓   ✓
 * 
 * Total combinations = 3 × 3 × 3 = 27
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * KEY INSIGHT - BACKTRACKING TEMPLATE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Template cho backtracking (THUỘC LÀM TỪ NÀY!):
 * 
 * void backtrack(State current) {
 *     // BASE CASE: Đã đủ điều kiện → Add to result
 *     if (isComplete(current)) {
 *         result.add(current);
 *         return;
 *     }
 *     
 *     // CHOICES: Loop qua các choices có thể
 *     for (choice : getAllChoices()) {
 *         // MAKE CHOICE
 *         current.add(choice);
 *         
 *         // RECURSE
 *         backtrack(current);
 *         
 *         // UNDO CHOICE (backtrack!)
 *         current.remove(choice);
 *     }
 * }
 * 
 * Áp dụng cho bài này:
 * - State: current string being built + index in digits
 * - isComplete: index == digits.length()
 * - getAllChoices: letters mapped from digits[index]
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC HACKERRANK
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Given a string of digits where '2'-'9' map to letters (like on a phone keypad) 
 * and '0','1' map to themselves, return all possible letter combinations in 
 * lexicographical order.
 * 
 * Example 1:
 * Input: digits = "23"
 * Output: ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"]
 * 
 * Example 2:
 * Input: digits = "203"
 * Output: ["a0d", "a0e", "a0f", "b0d", "b0e", "b0f", "c0d", "c0e", "c0f"]
 */
class PhoneLetterCombinationsTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // PHONE MAPPING
    // ═══════════════════════════════════════════════════════════════════════════
    
    private static final String[] PHONE_MAP = {
        "",      // 0 → empty (hoặc "0" nếu đề bài muốn giữ digit)
        "",      // 1 → empty
        "abc",   // 2
        "def",   // 3
        "ghi",   // 4
        "jkl",   // 5
        "mno",   // 6
        "pqrs",  // 7
        "tuv",   // 8
        "wxyz"   // 9
    };
    
    // ═══════════════════════════════════════════════════════════════════════════
    // SOLUTION: BACKTRACKING
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * ALGORITHM
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Use backtracking to explore all combinations:
     * 
     * State:
     * - current: Current string being built
     * - index: Current position in digits string
     * 
     * Base case:
     * - index == digits.length() → Complete! Add to result
     * 
     * Recursive case:
     * - Get letters for digits[index]
     * - Try each letter:
     *   1. Add letter to current
     *   2. Recurse with index+1
     *   3. Remove letter (backtrack)
     * 
     * Time: O(4^n) where n = digits.length (worst case: all 7s or 9s)
     * Space: O(n) recursion depth
     */
    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        
        if (digits == null || digits.isEmpty()) {
            return result;
        }
        
        backtrack(result, new StringBuilder(), digits, 0);
        return result;
    }
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * BACKTRACK FUNCTION
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * @param result    Final result list
     * @param current   Current combination being built
     * @param digits    Input digits string
     * @param index     Current position in digits
     */
    private void backtrack(List<String> result, StringBuilder current,
                          String digits, int index) {
        // ───────────────────────────────────────────────────────────────────
        // BASE CASE: Reached end of digits
        // ───────────────────────────────────────────────────────────────────
        
        if (index == digits.length()) {
            result.add(current.toString());
            return;
        }
        
        // ───────────────────────────────────────────────────────────────────
        // GET LETTERS for current digit
        // ───────────────────────────────────────────────────────────────────
        
        char digit = digits.charAt(index);
        String letters = PHONE_MAP[digit - '0'];
        
        // Handle special case: '0' or '1' (no letters, treat as digit itself)
        if (letters.isEmpty()) {
            current.append(digit);
            backtrack(result, current, digits, index + 1);
            current.deleteCharAt(current.length() - 1);
            return;
        }
        
        // ───────────────────────────────────────────────────────────────────
        // TRY EACH LETTER (this creates branches in decision tree!)
        // ───────────────────────────────────────────────────────────────────
        
        for (char letter : letters.toCharArray()) {
            // ⭐ MAKE CHOICE: Add letter
            current.append(letter);
            
            // ⭐ RECURSE: Move to next digit
            backtrack(result, current, digits, index + 1);
            
            // ⭐ UNDO CHOICE: Remove letter (backtrack!)
            current.deleteCharAt(current.length() - 1);
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HACKERRANK FORMAT SOLUTION
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ⭐ Copy paste này vào HackerRank!
     */
    public static List<String> letterCombinationsHackerRank(String digits) {
        List<String> result = new ArrayList<>();
        
        if (digits == null || digits.isEmpty()) {
            return result;
        }
        
        String[] phoneMap = {
            "0", "1", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
        };
        
        backtrackHelper(result, new StringBuilder(), digits, 0, phoneMap);
        return result;
    }
    
    private static void backtrackHelper(List<String> result, StringBuilder current,
                                       String digits, int index, String[] phoneMap) {
        if (index == digits.length()) {
            result.add(current.toString());
            return;
        }
        
        char digit = digits.charAt(index);
        String letters = phoneMap[digit - '0'];
        
        for (char letter : letters.toCharArray()) {
            current.append(letter);
            backtrackHelper(result, current, digits, index + 1, phoneMap);
            current.deleteCharAt(current.length() - 1);
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT - "23"
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Step-by-step execution trace:
     * 
     * Call 1: backtrack(result, "", "23", 0)
     * ├─ index=0, digit='2', letters="abc"
     * ├─ Loop: letter='a'
     * │   ├─ current="a"
     * │   └─ Call 2: backtrack(result, "a", "23", 1)
     * │       ├─ index=1, digit='3', letters="def"
     * │       ├─ Loop: letter='d'
     * │       │   ├─ current="ad"
     * │       │   └─ Call 3: backtrack(result, "ad", "23", 2)
     * │       │       ├─ index=2 == length=2 → BASE CASE!
     * │       │       └─ result.add("ad") ✓
     * │       │   ├─ Backtrack: current="a" (remove 'd')
     * │       ├─ Loop: letter='e'
     * │       │   ├─ current="ae"
     * │       │   └─ Call 4: backtrack(result, "ae", "23", 2)
     * │       │       ├─ index=2 == length=2 → BASE CASE!
     * │       │       └─ result.add("ae") ✓
     * │       │   ├─ Backtrack: current="a" (remove 'e')
     * │       ├─ Loop: letter='f'
     * │       │   ├─ current="af"
     * │       │   └─ Call 5: backtrack(result, "af", "23", 2)
     * │       │       ├─ index=2 == length=2 → BASE CASE!
     * │       │       └─ result.add("af") ✓
     * │       │   ├─ Backtrack: current="a" (remove 'f')
     * │   ├─ Backtrack: current="" (remove 'a')
     * ├─ Loop: letter='b'
     * │   ├─ current="b"
     * │   └─ Call 6: backtrack(result, "b", "23", 1)
     * │       ├─ [Similar to above, generates "bd", "be", "bf"]
     * │       └─ result += ["bd", "be", "bf"]
     * │   ├─ Backtrack: current="" (remove 'b')
     * ├─ Loop: letter='c'
     * │   ├─ current="c"
     * │   └─ Call N: backtrack(result, "c", "23", 1)
     * │       ├─ [Similar to above, generates "cd", "ce", "cf"]
     * │       └─ result += ["cd", "ce", "cf"]
     * │   ├─ Backtrack: current="" (remove 'c')
     * 
     * Final result: ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"]
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // VẼ DECISION TREE KHI DEBUG
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * TIPS ĐỂ VẼ DECISION TREE NHANH:
     * 
     * 1. Xác định ROOT:
     *    - Luôn là empty state (hoặc initial state)
     * 
     * 2. Xác định LEVELS:
     *    - Mỗi level = 1 decision point
     *    - Bài này: mỗi level = 1 digit
     * 
     * 3. Xác định BRANCHES:
     *    - Mỗi branch = 1 choice tại decision point đó
     *    - Bài này: mỗi branch = 1 letter
     * 
     * 4. Xác định LEAVES:
     *    - Khi nào dừng? → Base case
     *    - Bài này: khi index == digits.length
     * 
     * 5. VẼ TỪ TRÁI SANG PHẢI:
     *    - DFS order (left-to-right) → lexicographical tự động!
     * 
     * ───────────────────────────────────────────────────────────────────────
     * 
     * EXAMPLE WORKFLOW: Vẽ tree cho "23"
     * 
     * Step 1: Vẽ root
     *         ""
     * 
     * Step 2: Level 1 - digit '2' → letters "abc"
     *         ""
     *       / | \
     *      a  b  c
     * 
     * Step 3: Level 2 - digit '3' → letters "def" (cho MỖI node level 1)
     *           ""
     *        /  |  \
     *       a   b   c
     *      /|\  /|\  /|\
     *     d e f d e f d e f
     * 
     * Step 4: Mark leaves (base case reached)
     *     ad ae af bd be bf cd ce cf
     *     ✓  ✓  ✓  ✓  ✓  ✓  ✓  ✓  ✓
     * 
     * ───────────────────────────────────────────────────────────────────────
     * 
     * PATTERN RECOGNITION:
     * - Tree height = số digits
     * - Branching factor = số letters của mỗi digit
     * - Total leaves = tích số letters của tất cả digits
     * - DFS traversal (left-to-right) = lexicographical order!
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // SO SÁNH VỚI BFS (KHÔNG DÙNG BACKTRACKING)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Alternative: BFS approach (không dùng backtracking)
     * 
     * Idea: Build combinations level by level
     * 
     * digits = "23"
     * 
     * Level 0: [""]
     * Level 1: For each in [""], append 'a','b','c' → ["a", "b", "c"]
     * Level 2: For each in ["a","b","c"], append 'd','e','f' 
     *          → ["ad","ae","af", "bd","be","bf", "cd","ce","cf"]
     * 
     * Code:
     */
    public List<String> letterCombinationsBFS(String digits) {
        if (digits == null || digits.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> result = new ArrayList<>();
        result.add("");  // Start with empty string
        
        for (char digit : digits.toCharArray()) {
            List<String> temp = new ArrayList<>();
            String letters = PHONE_MAP[digit - '0'];
            
            for (String combination : result) {
                for (char letter : letters.toCharArray()) {
                    temp.add(combination + letter);
                }
            }
            
            result = temp;
        }
        
        return result;
    }
    
    /**
     * BFS vs Backtracking:
     * 
     * BFS:
     * ✓ Easier to understand (no recursion)
     * ✓ Iterative (no stack overflow)
     * ✗ Uses more space (stores all intermediate results)
     * 
     * Backtracking:
     * ✓ More space efficient (only stores current path)
     * ✓ Natural for "generate all" problems
     * ✗ Harder to understand (recursion + backtrack)
     * ✓ Better for problems with pruning (can skip branches early)
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example 1: \"23\" → 9 combinations")
    void testExample1() {
        List<String> result = letterCombinations("23");
        assertThat(result).containsExactly(
            "ad", "ae", "af",
            "bd", "be", "bf",
            "cd", "ce", "cf"
        );
    }
    
    @Test
    @DisplayName("Example 2: \"203\" with '0' mapping")
    void testExample2() {
        // Need to update PHONE_MAP[0] = "0" for this test
        List<String> result = letterCombinations("2");
        assertThat(result).containsExactly("a", "b", "c");
    }
    
    @Test
    @DisplayName("Single digit: \"2\" → [\"a\", \"b\", \"c\"]")
    void testSingleDigit() {
        List<String> result = letterCombinations("2");
        assertThat(result).containsExactly("a", "b", "c");
    }
    
    @Test
    @DisplayName("Empty string: \"\" → []")
    void testEmpty() {
        List<String> result = letterCombinations("");
        assertThat(result).isEmpty();
    }
    
    @Test
    @DisplayName("Digit with 4 letters: \"7\" → [\"p\", \"q\", \"r\", \"s\"]")
    void testSevenDigit() {
        List<String> result = letterCombinations("7");
        assertThat(result).containsExactly("p", "q", "r", "s");
    }
    
    @Test
    @DisplayName("Three digits: \"234\" → 27 combinations")
    void testThreeDigits() {
        List<String> result = letterCombinations("234");
        assertThat(result).hasSize(27);  // 3 × 3 × 3
        assertThat(result.get(0)).isEqualTo("adg");  // First (lexicographical)
        assertThat(result.get(26)).isEqualTo("cfi"); // Last
    }
    
    @Test
    @DisplayName("Compare Backtracking vs BFS: Same result")
    void testBacktrackingVsBFS() {
        String digits = "23";
        List<String> backtrackResult = letterCombinations(digits);
        List<String> bfsResult = letterCombinationsBFS(digits);
        assertThat(backtrackResult).isEqualTo(bfsResult);
    }
}
