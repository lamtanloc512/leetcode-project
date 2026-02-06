package com.practice.leetcode.arrays;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * CYCLIC SORT EXERCISES - SOLUTIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 */
class CyclicSortExercisesTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 1: FIND MISSING NUMBER - SOLUTION
    // ═══════════════════════════════════════════════════════════════════════════
    
    public int findMissingNumber(int[] nums) {
        int n = nums.length;
        
        // Step 1: Cyclic sort - đặt mỗi số về đúng vị trí
        for (int i = 0; i < n; i++) {
            // ⭐ WHILE (not if) - một số cần nhiều lần swap
            // ⭐ Check range: nums[i] < n (vì range [0, n])
            // ⭐ Check nums[i] != nums[nums[i]] tránh infinite loop
            while (nums[i] < n && nums[i] != nums[nums[i]]) {
                swap(nums, i, nums[i]);
            }
        }
        
        // Step 2: Scan để tìm missing
        for (int i = 0; i < n; i++) {
            if (nums[i] != i) {
                return i;  // Found missing!
            }
        }
        
        // If [0..n-1] all present, missing = n
        return n;
    }
    
    @Test
    @DisplayName("Ex1: Missing [3,0,1] → 2")
    void testMissingNumber() {
        assertThat(findMissingNumber(new int[]{3, 0, 1})).isEqualTo(2);
        assertThat(findMissingNumber(new int[]{0, 1})).isEqualTo(2);
        assertThat(findMissingNumber(new int[]{9,6,4,2,3,5,7,0,1})).isEqualTo(8);
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 2: FIND ALL DUPLICATES - SOLUTION
    // ═══════════════════════════════════════════════════════════════════════════
    
    public List<Integer> findDuplicates(int[] nums) {
        List<Integer> result = new ArrayList<>();
        
        // Step 1: Cyclic sort
        for (int i = 0; i < nums.length; i++) {
            // ⭐ For range [1, n]: correctIndex = nums[i] - 1
            // ⭐ Stop when nums[i] == nums[nums[i] - 1] (duplicate!)
            while (nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }
        
        // Step 2: Find duplicates
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                result.add(nums[i]);
            }
        }
        
        return result;
    }
    
    @Test
    @DisplayName("Ex2: Duplicates [4,3,2,7,8,2,3,1] → [2,3]")
    void testFindDuplicates() {
        assertThat(findDuplicates(new int[]{4,3,2,7,8,2,3,1}))
            .containsExactlyInAnyOrder(2, 3);
        assertThat(findDuplicates(new int[]{1,1,2}))
            .containsExactly(1);
        assertThat(findDuplicates(new int[]{1}))
            .isEmpty();
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 3: FIRST MISSING POSITIVE - SOLUTION
    // ═══════════════════════════════════════════════════════════════════════════
    
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        
        // Step 1: Cyclic sort (chỉ care positive trong range [1,n])
        for (int i = 0; i < n; i++) {
            // ⭐ Triple condition:
            // 1. nums[i] > 0 (positive only)
            // 2. nums[i] <= n (in range)
            // 3. nums[i] != nums[nums[i] - 1] (not duplicate)
            while (nums[i] > 0 && nums[i] <= n 
                   && nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }
        
        // Step 2: Find first missing positive
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        
        // If [1..n] all present, missing = n+1
        return n + 1;
    }
    
    @Test
    @DisplayName("Ex3: FirstMissingPositive [3,4,-1,1] → 2")
    void testFirstMissingPositive() {
        assertThat(firstMissingPositive(new int[]{1,2,0})).isEqualTo(3);
        assertThat(firstMissingPositive(new int[]{3,4,-1,1})).isEqualTo(2);
        assertThat(firstMissingPositive(new int[]{7,8,9,11,12})).isEqualTo(1);
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 4: FIND ALL MISSING NUMBERS - SOLUTION
    // ═══════════════════════════════════════════════════════════════════════════
    
    public List<Integer> findMissingNumbers(int[] nums) {
        List<Integer> result = new ArrayList<>();
        
        // Step 1: Cyclic sort
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }
        
        // Step 2: Find all missing
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                result.add(i + 1);
            }
        }
        
        return result;
    }
    
    @Test
    @DisplayName("Ex4: MissingNumbers [4,3,2,7,8,2,3,1] → [5,6]")
    void testFindMissingNumbers() {
        assertThat(findMissingNumbers(new int[]{4,3,2,7,8,2,3,1}))
            .containsExactly(5, 6);
        assertThat(findMissingNumbers(new int[]{1,1}))
            .containsExactly(2);
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // EXERCISE 5: FIND THE DUPLICATE NUMBER - SOLUTION
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * APPROACH 1: Cyclic Sort (modifies array)
     */
    public int findDuplicateCyclicSort(int[] nums) {
        int i = 0;
        while (i < nums.length) {
            if (nums[i] != i + 1) {
                int correctIndex = nums[i] - 1;
                if (nums[i] != nums[correctIndex]) {
                    swap(nums, i, correctIndex);
                } else {
                    // Found duplicate!
                    return nums[i];
                }
            } else {
                i++;
            }
        }
        return -1;
    }
    
    /**
     * APPROACH 2: Floyd's Cycle Detection (no modification!)
     * 
     * Treat array as LinkedList:
     * - Index 0 → nums[0]
     * - Index nums[0] → nums[nums[0]]
     * - ...
     * 
     * Duplicate creates cycle!
     * 
     * Example: [1, 3, 4, 2, 2]
     * Index: 0 → 1 → 3 → 2 → 4 → 2 (cycle back to index 2)
     *                 ↑___________|
     * 
     * Floyd's algorithm:
     * Phase 1: Find intersection of slow/fast
     * Phase 2: Find cycle entrance = duplicate
     */
    public int findDuplicate(int[] nums) {
        // Phase 1: Find intersection
        int slow = nums[0];
        int fast = nums[0];
        
        do {
            slow = nums[slow];           // Move 1 step
            fast = nums[nums[fast]];     // Move 2 steps
        } while (slow != fast);
        
        // Phase 2: Find entrance (duplicate)
        slow = nums[0];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        
        return slow;
    }
    
    @Test
    @DisplayName("Ex5: FindDuplicate [1,3,4,2,2] → 2")
    void testFindDuplicate() {
        assertThat(findDuplicate(new int[]{1,3,4,2,2})).isEqualTo(2);
        assertThat(findDuplicate(new int[]{3,1,3,4,2})).isEqualTo(3);
        assertThat(findDuplicate(new int[]{3,3,3,3,3})).isEqualTo(3);
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // BONUS: SET MISMATCH
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Array chứa n số trong range [1, n].
     * 1 số bị duplicate, 1 số missing.
     * Return [duplicate, missing].
     * 
     * VÍ DỤ:
     * nums = [1,2,2,4] → [2, 3]
     * nums = [1,1] → [1, 2]
     */
    public int[] findErrorNums(int[] nums) {
        // Cyclic sort
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }
        
        // Find duplicate and missing
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                return new int[]{nums[i], i + 1};
            }
        }
        
        return new int[]{-1, -1};
    }
    
    @Test
    @DisplayName("Bonus: SetMismatch [1,2,2,4] → [2,3]")
    void testSetMismatch() {
        assertThat(findErrorNums(new int[]{1,2,2,4}))
            .containsExactly(2, 3);
        assertThat(findErrorNums(new int[]{1,1}))
            .containsExactly(1, 2);
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // BONUS: FIND K MISSING POSITIVE NUMBERS
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ĐỀ BÀI:
     * Cho unsorted array và k.
     * Tìm k số positive đầu tiên missing.
     * 
     * VÍ DỤ:
     * nums = [3, -1, 4, 5, 5], k = 3 → [1, 2, 6]
     * nums = [2, 3, 4], k = 3 → [1, 5, 6]
     * 
     * HINT: Sau cyclic sort, check từ 1..n, rồi tiếp tục n+1, n+2,...
     */
    public List<Integer> findKMissingPositive(int[] nums, int k) {
        List<Integer> result = new ArrayList<>();
        
        // Step 1: Cyclic sort
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] > 0 && nums[i] <= nums.length 
                   && nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }
        
        // Step 2: Find missing in [1, n]
        java.util.Set<Integer> extras = new java.util.HashSet<>();
        for (int i = 0; i < nums.length && result.size() < k; i++) {
            if (nums[i] != i + 1) {
                result.add(i + 1);
                extras.add(nums[i]);
            }
        }
        
        // Step 3: If still need more, add n+1, n+2, ...
        int candidate = nums.length + 1;
        while (result.size() < k) {
            if (!extras.contains(candidate)) {
                result.add(candidate);
            }
            candidate++;
        }
        
        return result;
    }
    
    @Test
    @DisplayName("Bonus: K Missing [3,-1,4,5,5], k=3 → [1,2,6]")
    void testFindKMissingPositive() {
        assertThat(findKMissingPositive(new int[]{3, -1, 4, 5, 5}, 3))
            .containsExactly(1, 2, 6);
        assertThat(findKMissingPositive(new int[]{2, 3, 4}, 3))
            .containsExactly(1, 5, 6);
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER
    // ═══════════════════════════════════════════════════════════════════════════
    
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // COMPLEXITY ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * TIME COMPLEXITY:
     * 
     * Cyclic sort: O(n)
     * - Mỗi element di chuyển TỐI ĐA 1 lần đến đúng vị trí
     * - Total swaps: O(n)
     * - Outer loop: O(n)
     * - Total: O(n) + O(n) = O(n)
     * 
     * Why not O(n²)?
     * - WHILE loop looks scary, but:
     * - Each number moves AT MOST once to correct position
     * - Once at correct position, never swapped again
     * - Total movements across ALL iterations = n
     * 
     * SPACE COMPLEXITY: O(1)
     * - Only swap in-place
     * - No extra data structures
     * 
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * COMPARISON WITH OTHER APPROACHES:
     * 
     * ┌──────────────────┬──────────┬──────────┬────────────┐
     * │ Approach         │ Time     │ Space    │ Modify?    │
     * ├──────────────────┼──────────┼──────────┼────────────┤
     * │ Sort             │ O(n logn)│ O(1)     │ Yes        │
     * │ HashSet          │ O(n)     │ O(n)     │ No         │
     * │ Cyclic Sort      │ O(n)     │ O(1)     │ Yes        │
     * │ Floyd's Cycle    │ O(n)     │ O(1)     │ No         │
     * └──────────────────┴──────────┴──────────┴────────────┘
     * 
     * Cyclic Sort wins when:
     * - Range is [1, n] or [0, n]
     * - Can modify array
     * - Need O(1) space
     */
}
