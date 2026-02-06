package com.practice.leetcode.hackerrank;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * HACKERRANK - FIND THE SMALLEST MISSING POSITIVE INTEGER
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cho mảng số nguyên (có thể có số âm, 0, dương).
 * Tìm số nguyên dương NHỎ NHẤT (>= 1) KHÔNG có trong mảng.
 * 
 * Constraints:
 * - Time: O(n)
 * - Space: O(1) - constant extra space
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Example 1:
 * Input:  [3, 4, -1, 1]
 * Output: 2
 * Giải thích: Có 1, 3, 4 → Thiếu 2
 * 
 * Example 2:
 * Input:  [1, 2, 3, 4, 5]
 * Output: 6
 * Giải thích: Có đủ 1→5 → Thiếu 6
 * 
 * Example 3:
 * Input:  [7, 8, 9, 10]
 * Output: 1
 * Giải thích: Không có 1 → Thiếu 1
 * 
 * Example 4:
 * Input:  [-1, -2, -3]
 * Output: 1
 * Giải thích: Không có số dương nào → Thiếu 1
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * Ý TƯỞNG CHÍNH
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Key Insight:
 * - Đáp án chỉ có thể nằm trong range [1, n+1]
 * - Nếu mảng có đủ [1, 2, 3, ..., n] → đáp án là n+1
 * - Nếu thiếu số nào → đáp án là số đó
 * 
 * Strategy: "Index as Hash"
 * - Dùng chính array index làm "hash table"
 * - Index i đại diện cho số (i+1)
 * - Đánh dấu sự hiện diện bằng cách làm âm giá trị
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 1: BRUTE FORCE (Không tối ưu)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Time: O(n²)
 * Space: O(1)
 * 
 * Ý tưởng: Check từng số 1, 2, 3, ... cho đến khi tìm thấy số không có
 */
class SmallestMissingPositiveIntegerTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: BRUTE FORCE
    // ═══════════════════════════════════════════════════════════════════════════
    
    public int solutionBruteForce(int[] arr) {
        int candidate = 1;
        
        while (true) {
            boolean found = false;
            
            // Tìm candidate trong array
            for (int num : arr) {
                if (num == candidate) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                return candidate;  // Tìm thấy số thiếu!
            }
            
            candidate++;
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: HASH SET (Dễ hiểu nhưng O(n) space)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Time: O(n)
     * Space: O(n) - không đạt yêu cầu O(1) space!
     */
    public int solutionHashSet(int[] arr) {
        java.util.Set<Integer> set = new java.util.HashSet<>();
        
        // Thêm tất cả số dương vào set
        for (int num : arr) {
            if (num > 0) {
                set.add(num);
            }
        }
        
        // Tìm số dương nhỏ nhất không có trong set
        int candidate = 1;
        while (set.contains(candidate)) {
            candidate++;
        }
        
        return candidate;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 3: INDEX AS HASH (OPTIMAL - HackerRank Expected)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * GIẢI THÍCH CHI TIẾT TỪNG BƯỚC
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Ý tưởng: Dùng chính array làm "hash table"
     * - Array có n phần tử → đáp án chỉ có thể là 1→(n+1)
     * - Sử dụng index để đánh dấu số đã xuất hiện
     * - Index i đại diện cho số (i+1)
     * 
     * BƯỚC 1: Đưa các số "đúng chỗ"
     * - Số 1 nên ở index 0
     * - Số 2 nên ở index 1
     * - Số k nên ở index (k-1)
     * 
     * BƯỚC 2: Scan để tìm index đầu tiên có giá trị sai
     * - Nếu arr[i] != i+1 → số (i+1) bị thiếu!
     * 
     * Time: O(n)
     * Space: O(1)
     */
    public int solution(int[] arr) {
        int n = arr.length;
        
        // ═══════════════════════════════════════════════════════════════════════
        // STEP 1: Đưa các số về "đúng vị trí"
        // ═══════════════════════════════════════════════════════════════════════
        // 
        // Mục tiêu: arr[i] = i+1 (nếu có)
        // - Số 1 → index 0
        // - Số 2 → index 1
        // - Số k → index k-1
        //
        // Chỉ swap nếu:
        // 1. arr[i] nằm trong range [1, n]
        // 2. arr[i] chưa ở đúng vị trí
        // 3. Vị trí đích khác giá trị hiện tại (tránh infinite loop)
        
        for (int i = 0; i < n; i++) {
            // Keep swapping until arr[i] is in correct position or invalid
            while (arr[i] > 0 &&                    // Số dương
                   arr[i] <= n &&                   // Trong range [1, n]
                   arr[i] != i + 1 &&               // Chưa đúng vị trí
                   arr[i] != arr[arr[i] - 1]) {     // Tránh duplicate swap
                
                // Swap arr[i] to its correct position
                int correctIndex = arr[i] - 1;
                int temp = arr[i];
                arr[i] = arr[correctIndex];
                arr[correctIndex] = temp;
            }
        }
        
        // ═══════════════════════════════════════════════════════════════════════
        // STEP 2: Tìm index đầu tiên có giá trị không đúng
        // ═══════════════════════════════════════════════════════════════════════
        //
        // Nếu arr[i] != i+1 → số (i+1) bị thiếu!
        
        for (int i = 0; i < n; i++) {
            if (arr[i] != i + 1) {
                return i + 1;  // Found missing number!
            }
        }
        
        // Nếu tất cả đều đúng → mảng có [1, 2, 3, ..., n]
        // Số thiếu là n+1
        return n + 1;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Example: arr = [3, 4, -1, 1]
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * STEP 1: Đưa các số về đúng vị trí
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Initial: [3, 4, -1, 1]
     *           ↑
     *          i=0
     * 
     * arr[0]=3 → Số 3 nên ở index 2 (3-1=2)
     * Swap arr[0] với arr[2]:
     * Result: [-1, 4, 3, 1]
     * 
     * arr[0]=-1 → Số âm, bỏ qua, i++
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * i=1: [-1, 4, 3, 1]
     *           ↑
     * 
     * arr[1]=4 → Số 4 nên ở index 3 (4-1=3)
     * Swap arr[1] với arr[3]:
     * Result: [-1, 1, 3, 4]
     * 
     * arr[1]=1 → Số 1 nên ở index 0 (1-1=0)
     * Swap arr[1] với arr[0]:
     * Result: [1, -1, 3, 4]
     * 
     * arr[1]=-1 → Số âm, i++
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * i=2: [1, -1, 3, 4]
     *            ↑
     * 
     * arr[2]=3 → Đã đúng vị trí (3 ở index 2)! i++
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * i=3: [1, -1, 3, 4]
     *               ↑
     * 
     * arr[3]=4 → Đã đúng vị trí (4 ở index 3)! i++
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * STEP 2: Tìm index có giá trị sai
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Final array: [1, -1, 3, 4]
     * 
     * i=0: arr[0]=1 = 0+1 ✓
     * i=1: arr[1]=-1 ≠ 1+1 ✗ → Missing number is 2!
     * 
     * Answer: 2
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example 1: [3, 4, -1, 1] → 2")
    void testExample1() {
        int[] arr = {3, 4, -1, 1};
        assertThat(solution(arr)).isEqualTo(2);
        assertThat(solutionBruteForce(new int[]{3, 4, -1, 1})).isEqualTo(2);
        assertThat(solutionHashSet(new int[]{3, 4, -1, 1})).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Example 2: [1, 2, 3, 4, 5] → 6")
    void testExample2() {
        int[] arr = {1, 2, 3, 4, 5};
        assertThat(solution(arr)).isEqualTo(6);
    }
    
    @Test
    @DisplayName("Example 3: [7, 8, 9, 10] → 1")
    void testExample3() {
        int[] arr = {7, 8, 9, 10};
        assertThat(solution(arr)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Example 4: [-1, -2, -3] → 1")
    void testExample4() {
        int[] arr = {-1, -2, -3};
        assertThat(solution(arr)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Edge case: [1] → 2")
    void testSingleElement() {
        int[] arr = {1};
        assertThat(solution(arr)).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Edge case: [2] → 1")
    void testMissingOne() {
        int[] arr = {2};
        assertThat(solution(arr)).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Edge case: [1, 1, 1, 1] → 2")
    void testDuplicates() {
        int[] arr = {1, 1, 1, 1};
        assertThat(solution(arr)).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Large gap: [1, 100, 200] → 2")
    void testLargeGap() {
        int[] arr = {1, 100, 200};
        assertThat(solution(arr)).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Mixed: [3, 1, 5, 2, 7] → 4")
    void testMixed() {
        int[] arr = {3, 1, 5, 2, 7};
        assertThat(solution(arr)).isEqualTo(4);
    }
}
