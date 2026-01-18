package com.practice.leetcode.dp;

/**
 * Problem: Coin Change
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/coin-change/
 *
 * Description:
 * You are given an integer array coins representing coins of different
 * denominations and an integer amount representing a total amount of money.
 * 
 * Return the fewest number of coins that you need to make up that amount.
 * If that amount of money cannot be made up by any combination of the coins,
 * return -1.
 * 
 * You may assume that you have an infinite number of each kind of coin.
 *
 * Examples:
 * Input: coins = [1,2,5], amount = 11
 * Output: 3
 * Explanation: 11 = 5 + 5 + 1
 *
 * Input: coins = [2], amount = 3
 * Output: -1
 *
 * Input: coins = [1], amount = 0
 * Output: 0
 *
 * Constraints:
 * - 1 <= coins.length <= 12
 * - 1 <= coins[i] <= 2^31 - 1
 * - 0 <= amount <= 10^4
 */
public class CoinChange {

  /**
   * Find minimum coins needed to make amount.
   *
   * @param coins  array of coin denominations
   * @param amount target amount
   * @return minimum number of coins, or -1 if impossible
   */
  public int coinChange(int[] coins, int amount) {
    throw new UnsupportedOperationException("Implement this method");
  }
}
