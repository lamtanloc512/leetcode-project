package com.practice.leetcode.arrays;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * CYCLIC SORT - STEP-BY-STEP VISUALIZATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cách hiểu và trace cyclic sort một cách TRỰC QUAN!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CORE CONCEPT: INDEX AS HASH
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Thay vì dùng HashMap để mark số đã xuất hiện:
 * → Dùng chính INDEX của array!
 * 
 * VÍ DỤ: Range [1, n]
 * 
 * HashMap approach:                  Cyclic Sort approach:
 * map[1] = true                      Array[0] = 1 ← Index 0 là "hash" cho số 1
 * map[2] = true                      Array[1] = 2 ← Index 1 là "hash" cho số 2
 * map[3] = true                      Array[2] = 3 ← Index 2 là "hash" cho số 3
 * 
 * Space: O(n) ❌                     Space: O(1) ✅
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VISUALIZATION 1: [3, 4, -1, 1] → Find smallest missing positive
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * GOAL: Place each positive number at its "home" index
 * - Number 1 → Index 0
 * - Number 2 → Index 1
 * - Number 3 → Index 2
 * - Number 4 → Index 3
 * 
 * ─────────────────────────────────────────────────────────────────────────────
 * INITIAL STATE:
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 *   Index:  0   1   2   3
 *   Array: [3,  4, -1,  1]
 *           ↑
 *         i=0
 * 
 * Target positions:
 * - 3 wants index 2 (3-1)
 * - 4 wants index 3 (4-1)
 * - -1 is invalid (skip)
 * - 1 wants index 0 (1-1)
 * 
 * ─────────────────────────────────────────────────────────────────────────────
 * STEP 1: i=0, nums[0]=3
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * Current: [3,  4, -1,  1]
 *           ↑        ↑
 *          i=0   target=2
 * 
 * Q: Is 3 in range [1, n]? YES (n=4)
 * Q: Is 3 at correct position? NO (should be at index 2)
 * Q: Is nums[2] already 3? NO (nums[2]=-1)
 * → SWAP(0, 2)!
 * 
 * After: [-1,  4,  3,  1]
 *         ↑
 *        i=0
 * 
 * Now nums[0]=-1, check again!
 * 
 * ─────────────────────────────────────────────────────────────────────────────
 * STEP 2: i=0, nums[0]=-1
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * Current: [-1,  4,  3,  1]
 *           ↑
 *          i=0
 * 
 * Q: Is -1 in range [1, n]? NO!
 * → Skip! Move to i=1
 * 
 * ─────────────────────────────────────────────────────────────────────────────
 * STEP 3: i=1, nums[1]=4
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * Current: [-1,  4,  3,  1]
 *               ↑        ↑
 *              i=1   target=3
 * 
 * Q: Is 4 in range [1, 4]? YES
 * Q: Is 4 at correct position? NO (should be at index 3)
 * Q: Is nums[3] already 4? NO (nums[3]=1)
 * → SWAP(1, 3)!
 * 
 * After: [-1,  1,  3,  4]
 *               ↑
 *              i=1
 * 
 * Now nums[1]=1, check again!
 * 
 * ─────────────────────────────────────────────────────────────────────────────
 * STEP 4: i=1, nums[1]=1
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * Current: [-1,  1,  3,  4]
 *           ↑   ↑
 *       target i=1
 * 
 * Q: Is 1 in range [1, 4]? YES
 * Q: Is 1 at correct position? NO (should be at index 0)
 * Q: Is nums[0] already 1? NO (nums[0]=-1)
 * → SWAP(1, 0)!
 * 
 * After: [1, -1,  3,  4]
 *            ↑
 *           i=1
 * 
 * Now nums[1]=-1, out of range!
 * → Done with i=1, move to i=2
 * 
 * ─────────────────────────────────────────────────────────────────────────────
 * STEP 5: i=2, nums[2]=3
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * Current: [1, -1,  3,  4]
 *                   ↑
 *                  i=2
 * 
 * Q: Is 3 at correct position? YES (index 2 = 3-1) ✓
 * → Skip!
 * 
 * ─────────────────────────────────────────────────────────────────────────────
 * STEP 6: i=3, nums[3]=4
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * Current: [1, -1,  3,  4]
 *                       ↑
 *                      i=3
 * 
 * Q: Is 4 at correct position? YES (index 3 = 4-1) ✓
 * → Done!
 * 
 * ─────────────────────────────────────────────────────────────────────────────
 * FINAL STATE:
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 *   Index:  0   1   2   3
 *   Array: [1, -1,  3,  4]
 *           ↑   ↑   ↑   ↑
 *        Home  X  Home Home
 *            Missing: 2!
 * 
 * SCAN: Which index doesn't have its "home" value?
 * - Index 0: has 1 (1-1=0) ✓
 * - Index 1: has -1 (expected 2) ✗ → MISSING: 2!
 * - Index 2: has 3 (3-1=2) ✓
 * - Index 3: has 4 (4-1=3) ✓
 * 
 * ANSWER: 2
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VISUALIZATION 2: [4, 3, 2, 7, 8, 2, 3, 1] → Find duplicates
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * GOAL: Place numbers at home positions, duplicates won't fit!
 * 
 * Initial: [4, 3, 2, 7, 8, 2, 3, 1]
 * 
 * After many swaps (abbreviated):
 * [1, 2, 3, 4, 3, 2, 7, 8]
 *  ↑  ↑  ↑  ↑  ↑  ↑  ↑  ↑
 *  1  2  3  4  X  X  7  8
 * 
 * Index 4: expected 5, but has 3 → 3 is duplicate!
 * Index 5: expected 6, but has 2 → 2 is duplicate!
 * 
 * ANSWER: [2, 3]
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * KEY INSIGHTS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * 1. WHY WHILE (not if)?
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * Example: [3, 1, 2]
 * 
 * Using IF (wrong):
 * i=0: nums[0]=3 → swap with index 2 → [2, 1, 3]
 * i=1: nums[1]=1 → swap with index 0 → [1, 2, 3] ✓
 * 
 * But at i=0, after first swap we have [2, 1, 3]
 * → nums[0]=2 should swap again to index 1!
 * 
 * Using WHILE (correct):
 * i=0: nums[0]=3 → swap → [2, 1, 3]
 * i=0: nums[0]=2 (still not home!) → swap → [1, 2, 3] ✓
 * 
 * 2. WHY CHECK nums[i] != nums[correctIndex]?
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * Example: [1, 2, 2]
 * 
 * i=2: nums[2]=2 → should be at index 1
 * But nums[1]=2 already!
 * If we swap: [1, 2, 2] → [1, 2, 2] → [1, 2, 2] → INFINITE LOOP!
 * 
 * Check prevents this:
 * if (nums[2] == nums[1]) → Stop! (duplicate found)
 * 
 * 3. WHY O(n) NOT O(n²)?
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * WHILE loop looks scary: for + while = nested?
 * 
 * BUT: Each number moves AT MOST ONCE to its home!
 * 
 * Example: [3, 1, 2]
 * - 3 moves once: [3,1,2] → [2,1,3]
 * - 2 moves once: [2,1,3] → [1,2,3]
 * - 1 moves once: [1,2,3] → done
 * 
 * Total moves = n
 * Outer loop = n
 * Total = O(n) + O(n) = O(n) ✓
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN VARIATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * 1. RANGE [1, n] → correctIndex = nums[i] - 1
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * Number 1 → Index 0
 * Number 2 → Index 1
 * Number n → Index n-1
 * 
 * while (nums[i] > 0 && nums[i] <= n && nums[i] != nums[nums[i] - 1]) {
 *     swap(nums, i, nums[i] - 1);
 * }
 * 
 * 2. RANGE [0, n] → correctIndex = nums[i]
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * Number 0 → Index 0
 * Number 1 → Index 1
 * Number n → Index n
 * 
 * while (nums[i] < n && nums[i] != nums[nums[i]]) {
 *     swap(nums, i, nums[i]);
 * }
 * 
 * 3. WITH NEGATIVES → Only care about positives in [1, n]
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * while (nums[i] > 0 && nums[i] <= n && nums[i] != nums[nums[i] - 1]) {
 *     swap(nums, i, nums[i] - 1);
 * }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * COMMON BUGS & FIXES
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * ❌ BUG 1: Use IF instead of WHILE
 * ─────────────────────────────────────────────────────────────────────────────
 * if (nums[i] != nums[correctIndex]) {
 *     swap(...);
 * }
 * 
 * Problem: After swap, new value might also need swapping!
 * Fix: Use WHILE
 * 
 * ❌ BUG 2: Forget range check
 * ─────────────────────────────────────────────────────────────────────────────
 * while (nums[i] != nums[nums[i] - 1]) {  // ← Missing range check!
 *     swap(...);
 * }
 * 
 * Problem: nums[i] might be negative or > n → Index out of bounds!
 * Fix: Add range check: nums[i] > 0 && nums[i] <= n
 * 
 * ❌ BUG 3: Forget duplicate check
 * ─────────────────────────────────────────────────────────────────────────────
 * while (nums[i] != correctIndex) {  // ← Should check nums[correctIndex]!
 *     swap(...);
 * }
 * 
 * Problem: If nums[i] == nums[correctIndex], infinite loop!
 * Fix: Check nums[i] != nums[correctIndex]
 * 
 * ❌ BUG 4: Wrong mapping
 * ─────────────────────────────────────────────────────────────────────────────
 * // Range [1, n] but use nums[i] as index
 * swap(nums, i, nums[i]);  // ❌ Wrong! Should be nums[i] - 1
 * 
 * Fix: correctIndex = nums[i] - 1 for range [1, n]
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PRACTICE WORKFLOW
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * 1. IDENTIFY RANGE
 * ─────────────────────────────────────────────────────────────────────────────
 * Q: What's the range of numbers?
 * → [1, n]: correctIndex = value - 1
 * → [0, n]: correctIndex = value
 * 
 * 2. DRAW INITIAL STATE
 * ─────────────────────────────────────────────────────────────────────────────
 * Write indices and values:
 * Index:  0   1   2   3
 * Array: [3,  4, -1,  1]
 * 
 * 3. MARK TARGET POSITIONS
 * ─────────────────────────────────────────────────────────────────────────────
 * For each value, where should it go?
 * 3 → index 2
 * 4 → index 3
 * 1 → index 0
 * 
 * 4. SIMULATE SWAPS
 * ─────────────────────────────────────────────────────────────────────────────
 * Step by step, swap until value reaches home
 * 
 * 5. CHECK FINAL STATE
 * ─────────────────────────────────────────────────────────────────────────────
 * Which indices don't have expected values?
 * → Those are missing/duplicate!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * EXERCISES
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * See CyclicSortExercises.java for practice problems!
 * 
 * 1. Find Missing Number [0, n]
 * 2. Find All Duplicates [1, n]
 * 3. First Missing Positive
 * 4. Find All Missing Numbers
 * 5. Find The Duplicate Number
 * 
 * Trace MỖI bài trên giấy theo format trên!
 */
public class CyclicSortVisualization {
    // This is a documentation class - no implementation needed
}
