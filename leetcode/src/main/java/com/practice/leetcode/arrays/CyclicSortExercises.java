package com.practice.leetcode.arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * CYCLIC SORT PATTERN EXERCISES
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Pattern: "INDEX AS HASH" - Sử dụng chính index của array làm hash!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CORE IDEA
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Khi array có range [1..n] hoặc [0..n-1]:
 * → Có thể dùng INDEX làm hash để mark số đã xuất hiện!
 * 
 * VÍ DỤ: [3, 4, -1, 1]
 * 
 * Ý tưởng: Đặt mỗi số về "đúng vị trí" của nó:
 * - Số 1 → index 0
 * - Số 2 → index 1
 * - Số 3 → index 2
 * - Số 4 → index 3
 * 
 * Sau khi swap xong:
 * [1, -1, 3, 4]
 *  ↑   ↑  ↑  ↑
 *  1   X  3  4  → Missing: 2 (ở index 1)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * TEMPLATE CYCLIC SORT
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * for (int i = 0; i < n; i++) {
 *     // ⭐ Keep swapping until nums[i] is at correct position
 *     while (isInRange(nums[i]) && nums[i] != nums[correctIndex(nums[i])]) {
 *         swap(nums, i, correctIndex(nums[i]));
 *     }
 * }
 * 
 * // Then scan for missing
 * for (int i = 0; i < n; i++) {
 *     if (nums[i] != expectedValue(i)) {
 *         return expectedValue(i);  // Found missing!
 *     }
 * }
 * 
 * KEY POINTS:
 * 1. Use WHILE (not if) - một số cần nhiều lần swap
 * 2. Check nums[i] != nums[correctIndex] để tránh infinite loop (duplicates)
 * 3. Check range để skip invalid values
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * KHI NÀO DÙNG CYCLIC SORT?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * ✅ DÙNG KHI:
 * - Array có range cố định [1..n] hoặc [0..n-1]
 * - Tìm missing/duplicate numbers
 * - Yêu cầu O(n) time, O(1) space
 * - Có thể modify array
 * 
 * ❌ KHÔNG DÙNG KHI:
 * - Range không liên tục hoặc rất lớn
 * - Không được modify array
 * - Cần giữ thứ tự ban đầu
 */
public class CyclicSortExercises {

    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 1: FIND MISSING NUMBER
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho array chứa n số KHÁC NHAU trong range [0, n].
     * Tìm số MISSING duy nhất.
     * 
     * VÍ DỤ:
     * nums = [3, 0, 1] → 2
     * nums = [0, 1] → 2
     * nums = [9,6,4,2,3,5,7,0,1] → 8
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * TRACE CHI TIẾT: [3, 0, 1]
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Goal: Đặt mỗi số vào index = value của nó
     * - 0 → index 0
     * - 1 → index 1
     * - 3 → index 3
     * 
     * Initial: [3, 0, 1]
     *           ↑
     *         i=0
     * 
     * Step 1: i=0, nums[0]=3
     * - 3 should be at index 3
     * - But array length = 3 (indices 0,1,2)
     * - 3 is OUT OF RANGE (3 >= n)
     * - Skip! Move to i=1
     * 
     * Current: [3, 0, 1]
     *              ↑
     *            i=1
     * 
     * Step 2: i=1, nums[1]=0
     * - 0 should be at index 0
     * - nums[0] = 3 ≠ 0
     * - SWAP(1, 0): [0, 3, 1]
     * - After swap, nums[1]=3 (out of range)
     * - Done with i=1
     * 
     * Current: [0, 3, 1]
     *                 ↑
     *               i=2
     * 
     * Step 3: i=2, nums[2]=1
     * - 1 should be at index 1
     * - nums[1] = 3 ≠ 1
     * - SWAP(2, 1): [0, 1, 3]
     * - After swap, nums[2]=3 (out of range)
     * - Done!
     * 
     * Final: [0, 1, 3]
     *         ↑  ↑  ↑
     *         0  1  X  → Missing: 2!
     * 
     * Scan:
     * - i=0: nums[0]=0 ✓
     * - i=1: nums[1]=1 ✓
     * - i=2: nums[2]=3 ✗ (expected 2)
     * → Return 2!
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * CONSTRAINTS:
     * - n == nums.length
     * - 1 <= n <= 10^4
     * - 0 <= nums[i] <= n
     * - All numbers are unique
     * 
     * TIME: O(n)
     * SPACE: O(1)
     */
    public int findMissingNumber(int[] nums) {
        int n = nums.length;
        
        // TODO: Step 1 - Cyclic sort (đặt mỗi số về đúng vị trí)
        // for (int i = 0; i < n; i++) {
        //     while (nums[i] < n && nums[i] != nums[nums[i]]) {
        //         swap(nums, i, nums[i]);
        //     }
        // }
        
        // TODO: Step 2 - Scan để tìm missing
        // for (int i = 0; i < n; i++) {
        //     if (nums[i] != i) {
        //         return i;
        //     }
        // }
        
        // TODO: If all present, missing = n
        // return n;
        
        return -1;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 2: FIND ALL DUPLICATES
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho array chứa n số trong range [1, n].
     * Mỗi số xuất hiện 1 HOẶC 2 LẦN.
     * Tìm TẤT CẢ số xuất hiện 2 lần.
     * 
     * VÍ DỤ:
     * nums = [4,3,2,7,8,2,3,1] → [2, 3]
     * nums = [1,1,2] → [1]
     * nums = [1] → []
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * TRACE CHI TIẾT: [4, 3, 2, 7, 8, 2, 3, 1]
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Goal: Đặt mỗi số vào index = value - 1
     * - 1 → index 0
     * - 2 → index 1
     * - 3 → index 2
     * - 4 → index 3
     * - ...
     * 
     * Initial: [4, 3, 2, 7, 8, 2, 3, 1]
     *           ↑
     *         i=0
     * 
     * Step 1: i=0, nums[0]=4
     * - 4 should be at index 3 (4-1)
     * - nums[3] = 7 ≠ 4
     * - SWAP(0, 3): [7, 3, 2, 4, 8, 2, 3, 1]
     * - Now nums[0]=7, continue swapping at i=0
     * 
     * Step 2: i=0, nums[0]=7
     * - 7 should be at index 6 (7-1)
     * - nums[6] = 3 ≠ 7
     * - SWAP(0, 6): [3, 3, 2, 4, 8, 2, 7, 1]
     * - Now nums[0]=3, continue swapping at i=0
     * 
     * Step 3: i=0, nums[0]=3
     * - 3 should be at index 2 (3-1)
     * - nums[2] = 2 ≠ 3
     * - SWAP(0, 2): [2, 3, 3, 4, 8, 2, 7, 1]
     * - Now nums[0]=2, continue swapping at i=0
     * 
     * Step 4: i=0, nums[0]=2
     * - 2 should be at index 1 (2-1)
     * - nums[1] = 3 ≠ 2
     * - SWAP(0, 1): [3, 2, 3, 4, 8, 2, 7, 1]
     * - Now nums[0]=3, continue swapping at i=0
     * 
     * Step 5: i=0, nums[0]=3
     * - 3 should be at index 2 (3-1)
     * - nums[2] = 3 == 3 ✓ (DUPLICATE!)
     * - STOP swapping (tránh infinite loop)
     * - Move to i=1
     * 
     * ... (continue for remaining indices)
     * 
     * Final: [3, 2, 3, 4, 8, 2, 7, 1]
     *         ↑     ↑        ↑
     *        dup   dup     dup
     * 
     * Scan:
     * - i=0: nums[0]=3, expected 1 (0+1) → 3 is not at correct position
     * - i=1: nums[1]=2, expected 2 (1+1) ✓
     * - i=2: nums[2]=3, expected 3 (2+1) ✓
     * - ...
     * 
     * Wait, how to find duplicates?
     * → After cyclic sort, if nums[i] != i+1, then nums[i] is duplicate!
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * CONSTRAINTS:
     * - n == nums.length
     * - 1 <= n <= 10^5
     * - 1 <= nums[i] <= n
     * - Each number appears once or twice
     * 
     * TIME: O(n)
     * SPACE: O(1) (excluding output)
     */
    public List<Integer> findDuplicates(int[] nums) {
        List<Integer> result = new ArrayList<>();
        
        // TODO: Step 1 - Cyclic sort
        // for (int i = 0; i < nums.length; i++) {
        //     while (nums[i] != nums[nums[i] - 1]) {
        //         swap(nums, i, nums[i] - 1);
        //     }
        // }
        
        // TODO: Step 2 - Find duplicates
        // for (int i = 0; i < nums.length; i++) {
        //     if (nums[i] != i + 1) {
        //         result.add(nums[i]);
        //     }
        // }
        
        return result;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 3: FIRST MISSING POSITIVE
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho unsorted array, tìm smallest POSITIVE integer missing.
     * 
     * VÍ DỤ:
     * nums = [1,2,0] → 3
     * nums = [3,4,-1,1] → 2
     * nums = [7,8,9,11,12] → 1
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * TRACE CHI TIẾT: [3, 4, -1, 1]
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Goal: Đặt positive integers vào đúng vị trí
     * - 1 → index 0
     * - 2 → index 1
     * - 3 → index 2
     * - 4 → index 3
     * 
     * Initial: [3, 4, -1, 1]
     *           ↑
     *         i=0
     * 
     * Step 1: i=0, nums[0]=3
     * - Is 3 in range [1, n]? Yes (n=4)
     * - 3 should be at index 2 (3-1)
     * - nums[2] = -1 ≠ 3
     * - SWAP(0, 2): [-1, 4, 3, 1]
     * - Now nums[0]=-1, check again
     * 
     * Step 2: i=0, nums[0]=-1
     * - Is -1 in range [1, n]? NO!
     * - Skip, move to i=1
     * 
     * Current: [-1, 4, 3, 1]
     *               ↑
     *             i=1
     * 
     * Step 3: i=1, nums[1]=4
     * - Is 4 in range [1, 4]? Yes
     * - 4 should be at index 3 (4-1)
     * - nums[3] = 1 ≠ 4
     * - SWAP(1, 3): [-1, 1, 3, 4]
     * - Now nums[1]=1, check again
     * 
     * Step 4: i=1, nums[1]=1
     * - Is 1 in range [1, 4]? Yes
     * - 1 should be at index 0 (1-1)
     * - nums[0] = -1 ≠ 1
     * - SWAP(1, 0): [1, -1, 3, 4]
     * - Now nums[1]=-1, out of range
     * - Done with i=1
     * 
     * Current: [1, -1, 3, 4]
     *                  ↑
     *                i=2
     * 
     * Step 5: i=2, nums[2]=3
     * - 3 should be at index 2 (3-1)
     * - nums[2] = 3 == 3 ✓
     * - Already correct!
     * 
     * Step 6: i=3, nums[3]=4
     * - 4 should be at index 3 (4-1)
     * - nums[3] = 4 == 4 ✓
     * - Already correct!
     * 
     * Final: [1, -1, 3, 4]
     *         ↑   ↑  ↑  ↑
     *         1   X  3  4
     * 
     * Scan:
     * - i=0: nums[0]=1 ✓
     * - i=1: nums[1]=-1 ✗ (expected 2)
     * → Return 2!
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * CONSTRAINTS:
     * - 1 <= nums.length <= 10^5
     * - -2^31 <= nums[i] <= 2^31 - 1
     * 
     * TIME: O(n)
     * SPACE: O(1)
     */
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        
        // TODO: Step 1 - Cyclic sort (chỉ care positive trong range [1,n])
        // for (int i = 0; i < n; i++) {
        //     while (nums[i] > 0 && nums[i] <= n && nums[i] != nums[nums[i] - 1]) {
        //         swap(nums, i, nums[i] - 1);
        //     }
        // }
        
        // TODO: Step 2 - Find first missing positive
        // for (int i = 0; i < n; i++) {
        //     if (nums[i] != i + 1) {
        //         return i + 1;
        //     }
        // }
        
        // TODO: If [1..n] all present, missing = n+1
        // return n + 1;
        
        return -1;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 4: FIND ALL MISSING NUMBERS
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho array chứa n số trong range [1, n].
     * Tìm TẤT CẢ số missing.
     * 
     * VÍ DỤ:
     * nums = [4,3,2,7,8,2,3,1] → [5, 6]
     * nums = [1,1] → [2]
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * TRACE CHI TIẾT: [4, 3, 2, 7, 8, 2, 3, 1]
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * After cyclic sort: [1, 2, 3, 4, 3, 2, 7, 8]
     *                     ↑  ↑  ↑  ↑  ↑  ↑  ↑  ↑
     *                     1  2  3  4  X  X  7  8
     *                    (expected: 5  6)
     * 
     * Scan:
     * - i=0: nums[0]=1 ✓
     * - i=1: nums[1]=2 ✓
     * - i=2: nums[2]=3 ✓
     * - i=3: nums[3]=4 ✓
     * - i=4: nums[4]=3 ✗ (expected 5) → Missing: 5
     * - i=5: nums[5]=2 ✗ (expected 6) → Missing: 6
     * - i=6: nums[6]=7 ✓
     * - i=7: nums[7]=8 ✓
     * 
     * Result: [5, 6]
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * TIME: O(n)
     * SPACE: O(1) (excluding output)
     */
    public List<Integer> findMissingNumbers(int[] nums) {
        List<Integer> result = new ArrayList<>();
        
        // TODO: Step 1 - Cyclic sort
        // TODO: Step 2 - Find missing
        
        return result;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 5: FIND THE DUPLICATE NUMBER
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho array chứa n+1 số trong range [1, n].
     * Chỉ có DUY NHẤT 1 số duplicate (có thể lặp nhiều lần).
     * Tìm số duplicate đó.
     * 
     * CONSTRAINT: KHÔNG ĐƯỢC modify array!
     * 
     * VÍ DỤ:
     * nums = [1,3,4,2,2] → 2
     * nums = [3,1,3,4,2] → 3
     * nums = [3,3,3,3,3] → 3
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * APPROACH 1: CYCLIC SORT (modify array)
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Trace: [1, 3, 4, 2, 2]
     * 
     * After sort: [1, 2, 3, 4, 2]
     *              ↑  ↑  ↑  ↑  ↑
     *              1  2  3  4  X (expected 5, but got 2)
     * 
     * → nums[4] = 2 is duplicate!
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * APPROACH 2: FLOYD'S CYCLE DETECTION (no modify!)
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Treat array as LinkedList:
     * - Index i points to index nums[i]
     * - Duplicate creates cycle!
     * 
     * Example: [1, 3, 4, 2, 2]
     * 
     * Index: 0 → 1 → 3 → 2 → 4 → 2 (cycle!)
     *             ↑_______________|
     * 
     * Use slow/fast pointers to find cycle entry = duplicate!
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * TIME: O(n)
     * SPACE: O(1)
     */
    public int findDuplicate(int[] nums) {
        // TODO: Approach 1 - Cyclic sort (if modification allowed)
        
        // TODO: Approach 2 - Floyd's cycle detection (no modification)
        // int slow = nums[0];
        // int fast = nums[0];
        
        // Phase 1: Find intersection
        // do {
        //     slow = nums[slow];
        //     fast = nums[nums[fast]];
        // } while (slow != fast);
        
        // Phase 2: Find entrance (duplicate)
        // slow = nums[0];
        // while (slow != fast) {
        //     slow = nums[slow];
        //     fast = nums[fast];
        // }
        
        // return slow;
        
        return -1;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER METHOD
    // ═══════════════════════════════════════════════════════════════════════════
    
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // KEY TAKEAWAYS
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * PATTERN SUMMARY:
     * 
     * 1. RANGE [1, n]: Use index = value - 1
     *    - Value 1 → index 0
     *    - Value 2 → index 1
     *    - ...
     * 
     * 2. RANGE [0, n]: Use index = value
     *    - Value 0 → index 0
     *    - Value 1 → index 1
     *    - ...
     * 
     * 3. CYCLIC SORT TEMPLATE:
     *    while (isInRange && nums[i] != nums[correctIndex]) {
     *        swap(i, correctIndex);
     *    }
     * 
     * 4. AFTER SORT:
     *    - If nums[i] != expected → Missing or duplicate!
     * 
     * 5. COMMON BUGS:
     *    - Use IF instead of WHILE → Số không về đúng vị trí
     *    - Forget range check → Out of bounds!
     *    - Forget duplicate check → Infinite loop!
     */
}
