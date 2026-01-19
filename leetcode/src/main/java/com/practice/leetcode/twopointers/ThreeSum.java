package com.practice.leetcode.twopointers;

import java.util.List;

/**
 * Problem: 3Sum
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/3sum/
 *
 * Description:
 * Given an integer array nums, return all the triplets [nums[i], nums[j],
 * nums[k]] such that i != j, i != k, and j != k, and nums[i] + nums[j] +
 * nums[k] == 0.
 * 
 * Notice that the solution set must not contain duplicate triplets.
 *
 * Examples:
 * Input: nums = [-1,0,1,2,-1,-4]
 * Output: [[-1,-1,2],[-1,0,1]]
 * Explanation:
 * nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0.
 * nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0.
 * nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0.
 * The distinct triplets are [-1,0,1] and [-1,-1,2].
 *
 * Input: nums = [0,1,1]
 * Output: []
 *
 * Input: nums = [0,0,0]
 * Output: [[0,0,0]]
 *
 * Constraints:
 * - 3 <= nums.length <= 3000
 * - -10^5 <= nums[i] <= 10^5
 */
public class ThreeSum {

  /**
   * Find all unique triplets that sum to zero.
   *
   * @param nums array of integers
   * @return list of triplets that sum to zero
   */
  public List<List<Integer>> threeSum(int[] nums) {
    throw new UnsupportedOperationException("Implement this method");
  }
}
