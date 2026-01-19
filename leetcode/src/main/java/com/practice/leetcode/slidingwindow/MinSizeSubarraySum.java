package com.practice.leetcode.slidingwindow;

/**
 * Problem: Minimum Size Subarray Sum
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/minimum-size-subarray-sum/
 *
 * Description:
 * Given an array of positive integers nums and a positive integer target,
 * return the minimal length of a subarray whose sum is greater than or equal
 * to target. If there is no such subarray, return 0 instead.
 *
 * Examples:
 * Input: target = 7, nums = [2,3,1,2,4,3]
 * Output: 2
 * Explanation: The subarray [4,3] has the minimal length under the problem
 * constraint.
 *
 * Input: target = 4, nums = [1,4,4]
 * Output: 1
 *
 * Input: target = 11, nums = [1,1,1,1,1,1,1,1]
 * Output: 0
 *
 * Constraints:
 * - 1 <= target <= 10^9
 * - 1 <= nums.length <= 10^5
 * - 1 <= nums[i] <= 10^4
 *
 * Follow up: If you have figured out the O(n) solution, try coding another
 * solution of which the time complexity is O(n log(n)).
 */
public class MinSizeSubarraySum {

  /**
   * Find minimum length subarray with sum >= target.
   *
   * @param target target sum
   * @param nums   array of positive integers
   * @return minimum length of qualifying subarray, or 0 if none exists
   */
  public int minSubArrayLen(int target, int[] nums) {
    throw new UnsupportedOperationException("Implement this method");
  }
}
