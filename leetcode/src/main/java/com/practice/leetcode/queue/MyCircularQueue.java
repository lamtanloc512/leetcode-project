package com.practice.leetcode.queue;

/**
 * Problem: Design Circular Queue
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/design-circular-queue/
 *
 * Description:
 * Design your implementation of the circular queue. The circular queue is a
 * linear data structure in which the operations are performed based on FIFO
 * (First In First Out) principle, and the last position is connected back
 * to the first position to make a circle. It is also called "Ring Buffer".
 * 
 * One of the benefits of the circular queue is that we can make use of the
 * spaces in front of the queue. In a normal queue, once the queue becomes
 * full, we cannot insert the next element even if there is a space in front
 * of the queue. But using the circular queue, we can use the space to store
 * new values.
 * 
 * Implement the MyCircularQueue class:
 * - MyCircularQueue(int k) Initializes the object with the size of the queue
 * to be k.
 * - boolean enQueue(int value) Inserts an element into the circular queue.
 * Return true if the operation is successful.
 * - boolean deQueue() Deletes an element from the circular queue. Return
 * true if the operation is successful.
 * - int Front() Gets the front item from the queue. If the queue is empty,
 * return -1.
 * - int Rear() Gets the last item from the queue. If the queue is empty,
 * return -1.
 * - boolean isEmpty() Checks whether the circular queue is empty or not.
 * - boolean isFull() Checks whether the circular queue is full or not.
 *
 * Examples:
 * Input:
 * ["MyCircularQueue", "enQueue", "enQueue", "enQueue", "enQueue", "Rear",
 * "isFull", "deQueue", "enQueue", "Rear"]
 * [[3], [1], [2], [3], [4], [], [], [], [4], []]
 * 
 * Output: [null, true, true, true, false, 3, true, true, true, 4]
 *
 * Constraints:
 * - 1 <= k <= 1000
 * - 0 <= value <= 1000
 * - At most 3000 calls will be made to enQueue, deQueue, Front, Rear,
 * isEmpty, and isFull.
 */
public class MyCircularQueue {

  public MyCircularQueue(int k) {
    throw new UnsupportedOperationException("Implement this constructor");
  }

  public boolean enQueue(int value) {
    throw new UnsupportedOperationException("Implement this method");
  }

  public boolean deQueue() {
    throw new UnsupportedOperationException("Implement this method");
  }

  public int Front() {
    throw new UnsupportedOperationException("Implement this method");
  }

  public int Rear() {
    throw new UnsupportedOperationException("Implement this method");
  }

  public boolean isEmpty() {
    throw new UnsupportedOperationException("Implement this method");
  }

  public boolean isFull() {
    throw new UnsupportedOperationException("Implement this method");
  }
}
