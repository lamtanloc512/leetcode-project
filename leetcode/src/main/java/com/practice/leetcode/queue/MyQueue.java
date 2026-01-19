package com.practice.leetcode.queue;

/**
 * Problem: Implement Queue using Stacks
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/implement-queue-using-stacks/
 *
 * Description:
 * Implement a first in first out (FIFO) queue using only two stacks. The
 * implemented queue should support all the functions of a normal queue
 * (push, peek, pop, and empty).
 * 
 * Implement the MyQueue class:
 * - void push(int x) Pushes element x to the back of the queue.
 * - int pop() Removes the element from the front of the queue and returns it.
 * - int peek() Returns the element at the front of the queue.
 * - boolean empty() Returns true if the queue is empty, false otherwise.
 *
 * Notes:
 * - You must use only standard operations of a stack, which means only push
 * to top, peek/pop from top, size, and is empty operations are valid.
 * - Depending on your language, the stack may not be supported natively. You
 * may simulate a stack using a list or deque (double-ended queue) as long
 * as you use only a stack's standard operations.
 *
 * Examples:
 * Input:
 * ["MyQueue", "push", "push", "peek", "pop", "empty"]
 * [[], [1], [2], [], [], []]
 * 
 * Output: [null, null, null, 1, 1, false]
 *
 * Constraints:
 * - 1 <= x <= 9
 * - At most 100 calls will be made to push, pop, peek, and empty.
 * - All the calls to pop and peek are valid.
 *
 * Follow-up: Can you implement the queue such that each operation is amortized
 * O(1) time complexity?
 */
public class MyQueue {

  public MyQueue() {
    throw new UnsupportedOperationException("Implement this constructor");
  }

  public void push(int x) {
    throw new UnsupportedOperationException("Implement this method");
  }

  public int pop() {
    throw new UnsupportedOperationException("Implement this method");
  }

  public int peek() {
    throw new UnsupportedOperationException("Implement this method");
  }

  public boolean empty() {
    throw new UnsupportedOperationException("Implement this method");
  }
}
