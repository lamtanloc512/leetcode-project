package com.practice.leetcode.slidingwindow;

/**
 * Problem: Fruit Into Baskets
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/fruit-into-baskets/
 *
 * Description:
 * You are visiting a farm that has a single row of fruit trees arranged from
 * left to right. The trees are represented by an integer array fruits where
 * fruits[i] is the type of fruit the ith tree produces.
 * 
 * You want to collect as much fruit as possible. However, the owner has some
 * strict rules that you must follow:
 * - You only have two baskets, and each basket can only hold a single type
 * of fruit.
 * - Starting from any tree of your choice, you must pick exactly one fruit
 * from every tree (including the start tree) while moving to the right.
 * - Once you reach a tree with fruit that cannot fit in your baskets, you
 * must stop.
 * 
 * Return the maximum number of fruits you can pick.
 *
 * Examples:
 * Input: fruits = [1,2,1]
 * Output: 3
 * Explanation: We can pick from all 3 trees.
 *
 * Input: fruits = [0,1,2,2]
 * Output: 3
 * Explanation: We can pick from trees [1,2,2].
 *
 * Input: fruits = [1,2,3,2,2]
 * Output: 4
 * Explanation: We can pick from trees [2,3,2,2].
 *
 * Constraints:
 * - 1 <= fruits.length <= 10^5
 * - 0 <= fruits[i] < fruits.length
 */
public class FruitIntoBaskets {

  /**
   * Find maximum fruits that can be collected with two baskets.
   *
   * @param fruits array of fruit types
   * @return maximum number of fruits
   */
  public int totalFruit(int[] fruits) {
    throw new UnsupportedOperationException("Implement this method");
  }
}
