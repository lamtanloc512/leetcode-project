package com.practice.leetcode.queue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Design Circular Queue problem.
 */
class MyCircularQueueSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeMyCircularQueue {
    private final int[] data;
    private int head;
    private int tail;
    private int size;

    /**
     * Array-based circular queue with head and tail pointers.
     * All operations: O(1) time, O(k) space
     */
    public ImperativeMyCircularQueue(int k) {
      data = new int[k];
      head = 0;
      tail = -1;
      size = 0;
    }

    public boolean enQueue(int value) {
      if (isFull()) {
        return false;
      }
      tail = (tail + 1) % data.length;
      data[tail] = value;
      size++;
      return true;
    }

    public boolean deQueue() {
      if (isEmpty()) {
        return false;
      }
      head = (head + 1) % data.length;
      size--;
      return true;
    }

    public int Front() {
      if (isEmpty()) {
        return -1;
      }
      return data[head];
    }

    public int Rear() {
      if (isEmpty()) {
        return -1;
      }
      return data[tail];
    }

    public boolean isEmpty() {
      return size == 0;
    }

    public boolean isFull() {
      return size == data.length;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalMyCircularQueue {
    // QueueState class (Java 8 compatible - replaces record)
    private static class QueueState {
      final int[] data;
      final int head;
      final int count;

      QueueState(int[] data, int head, int count) {
        this.data = data;
        this.head = head;
        this.count = count;
      }

      static QueueState create(int capacity) {
        return new QueueState(new int[capacity], 0, 0);
      }

      int capacity() {
        return data.length;
      }

      int tail() {
        return (head + count - 1 + capacity()) % capacity();
      }
    }

    private QueueState state;

    /**
     * Record-based state management for cleaner code.
     * All operations: O(1) time, O(k) space
     */
    public FunctionalMyCircularQueue(int k) {
      state = QueueState.create(k);
    }

    public boolean enQueue(int value) {
      if (isFull()) {
        return false;
      }
      int newTail = (state.head + state.count) % state.capacity();
      state.data[newTail] = value;
      state = new QueueState(state.data, state.head, state.count + 1);
      return true;
    }

    public boolean deQueue() {
      if (isEmpty()) {
        return false;
      }
      int newHead = (state.head + 1) % state.capacity();
      state = new QueueState(state.data, newHead, state.count - 1);
      return true;
    }

    public int Front() {
      return isEmpty() ? -1 : state.data[state.head];
    }

    public int Rear() {
      return isEmpty() ? -1 : state.data[state.tail()];
    }

    public boolean isEmpty() {
      return state.count == 0;
    }

    public boolean isFull() {
      return state.count == state.capacity();
    }
  }

  // ========== TESTS ==========

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example from problem")
    void testExample() {
      ImperativeMyCircularQueue queue = new ImperativeMyCircularQueue(3);
      assertThat(queue.enQueue(1)).isTrue();
      assertThat(queue.enQueue(2)).isTrue();
      assertThat(queue.enQueue(3)).isTrue();
      assertThat(queue.enQueue(4)).isFalse();
      assertThat(queue.Rear()).isEqualTo(3);
      assertThat(queue.isFull()).isTrue();
      assertThat(queue.deQueue()).isTrue();
      assertThat(queue.enQueue(4)).isTrue();
      assertThat(queue.Rear()).isEqualTo(4);
    }

    @Test
    @DisplayName("Empty queue operations")
    void testEmptyQueue() {
      ImperativeMyCircularQueue queue = new ImperativeMyCircularQueue(3);
      assertThat(queue.isEmpty()).isTrue();
      assertThat(queue.Front()).isEqualTo(-1);
      assertThat(queue.Rear()).isEqualTo(-1);
      assertThat(queue.deQueue()).isFalse();
    }

    @Test
    @DisplayName("Full cycle")
    void testFullCycle() {
      ImperativeMyCircularQueue queue = new ImperativeMyCircularQueue(2);
      queue.enQueue(1);
      queue.enQueue(2);
      queue.deQueue();
      queue.enQueue(3);
      assertThat(queue.Front()).isEqualTo(2);
      assertThat(queue.Rear()).isEqualTo(3);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example from problem")
    void testExample() {
      FunctionalMyCircularQueue queue = new FunctionalMyCircularQueue(3);
      assertThat(queue.enQueue(1)).isTrue();
      assertThat(queue.enQueue(2)).isTrue();
      assertThat(queue.enQueue(3)).isTrue();
      assertThat(queue.enQueue(4)).isFalse();
      assertThat(queue.Rear()).isEqualTo(3);
      assertThat(queue.isFull()).isTrue();
      assertThat(queue.deQueue()).isTrue();
      assertThat(queue.enQueue(4)).isTrue();
      assertThat(queue.Rear()).isEqualTo(4);
    }

    @Test
    @DisplayName("Empty queue operations")
    void testEmptyQueue() {
      FunctionalMyCircularQueue queue = new FunctionalMyCircularQueue(3);
      assertThat(queue.isEmpty()).isTrue();
      assertThat(queue.Front()).isEqualTo(-1);
      assertThat(queue.Rear()).isEqualTo(-1);
      assertThat(queue.deQueue()).isFalse();
    }

    @Test
    @DisplayName("Full cycle")
    void testFullCycle() {
      FunctionalMyCircularQueue queue = new FunctionalMyCircularQueue(2);
      queue.enQueue(1);
      queue.enQueue(2);
      queue.deQueue();
      queue.enQueue(3);
      assertThat(queue.Front()).isEqualTo(2);
      assertThat(queue.Rear()).isEqualTo(3);
    }
  }
}
