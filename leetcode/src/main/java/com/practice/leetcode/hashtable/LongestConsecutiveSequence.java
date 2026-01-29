package com.practice.leetcode.hashtable;

import java.util.HashSet;
import java.util.Set;

/**
 * LeetCode 128. Longest Consecutive Sequence
 * 
 * Given an unsorted array of integers nums, return the length of the longest
 * consecutive elements sequence.
 * 
 * Approach: Use a HashSet for O(1) lookups.
 * For each number, check if it's the START of a sequence (num - 1 doesn't exist).
 * If it's a start, count how many consecutive numbers follow.
 * 
 * Time Complexity: O(n) - Each number is visited at most twice
 * Space Complexity: O(n) - For the HashSet
 */
public class LongestConsecutiveSequence {

    public int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // Step 1: Add all numbers to a HashSet for O(1) lookup
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }

        int maxLength = 0;

        // Step 2: For each number, check if it's the start of a sequence
        for (int num : numSet) {
            // Only start counting if this is the beginning of a sequence
            // (i.e., num - 1 doesn't exist in the set)
            if (!numSet.contains(num - 1)) {
                int currentNum = num;
                int currentLength = 1;

                // Count consecutive numbers
                while (numSet.contains(currentNum + 1)) {
                    currentNum++;
                    currentLength++;
                }

                maxLength = Math.max(maxLength, currentLength);
            }
        }

        return maxLength;
    }

    public static void main(String[] args) {
        LongestConsecutiveSequence solution = new LongestConsecutiveSequence();

        // Test case 1: [100,4,200,1,3,2] -> 4
        int[] nums1 = {100, 4, 200, 1, 3, 2};
        System.out.println("Test 1: " + solution.longestConsecutive(nums1)); // Expected: 4

        // Test case 2: [0,3,7,2,5,8,4,6,0,1] -> 9
        int[] nums2 = {0, 3, 7, 2, 5, 8, 4, 6, 0, 1};
        System.out.println("Test 2: " + solution.longestConsecutive(nums2)); // Expected: 9

        // Test case 3: [1,0,1,2] -> 3
        int[] nums3 = {1, 0, 1, 2};
        System.out.println("Test 3: " + solution.longestConsecutive(nums3)); // Expected: 3

        // Edge cases
        System.out.println("Empty: " + solution.longestConsecutive(new int[]{})); // Expected: 0
        System.out.println("Single: " + solution.longestConsecutive(new int[]{5})); // Expected: 1
    }
}
