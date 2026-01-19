package com.practice.leetcode.queue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Implement Queue using Stacks problem.
 */
class MyQueueSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeMyQueue {
    private final Deque<Integer> input;
    private final Deque<Integer> output;

    /**
     * Two-stack approach with lazy transfer.
     * Push: O(1), Pop/Peek: Amortized O(1)
     */
    public ImperativeMyQueue() {
      input = new ArrayDeque<>();
      output = new ArrayDeque<>();
    }

    public void push(int x) {
      input.push(x);
    }

    public int pop() {
      peek(); // Ensure output has elements
      return output.pop();
    }

    public int peek() {
      if (output.isEmpty()) {
        while (!input.isEmpty()) {
          output.push(input.pop());
        }
      }
      return output.peek();
    }

    public boolean empty() {
      return input.isEmpty() && output.isEmpty();
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalMyQueue {
    private final Deque<Integer> stack1;
    private final Deque<Integer> stack2;

    /**
     * Same two-stack approach, structured differently.
     * Demonstrates immutable-friendly design.
     */
    public FunctionalMyQueue() {
      stack1 = new ArrayDeque<>();
      stack2 = new ArrayDeque<>();
    }

    public void push(int x) {
      stack1.push(x);
    }

    public int pop() {
      transferIfNeeded();
      return stack2.pop();
    }

    public int peek() {
      transferIfNeeded();
      return stack2.peek();
    }

    public boolean empty() {
      return stack1.isEmpty() && stack2.isEmpty();
    }

    private void transferIfNeeded() {
      if (stack2.isEmpty()) {
        stack1.stream()
            .collect(java.util.stream.Collectors.toList())
            .forEach(val -> {
              stack1.pop();
              stack2.push(val);
            });
      }
    }
  }

  // ========== TESTS ==========

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example from problem")
    void testExample() {
      ImperativeMyQueue queue = new ImperativeMyQueue();
      queue.push(1);
      queue.push(2);
      assertThat(queue.peek()).isEqualTo(1);
      assertThat(queue.pop()).isEqualTo(1);
      assertThat(queue.empty()).isFalse();
    }

    @Test
    @DisplayName("Empty queue")
    void testEmptyQueue() {
      ImperativeMyQueue queue = new ImperativeMyQueue();
      assertThat(queue.empty()).isTrue();
    }

    @Test
    @DisplayName("Push and pop multiple")
    void testPushPopMultiple() {
      ImperativeMyQueue queue = new ImperativeMyQueue();
      queue.push(1);
      queue.push(2);
      queue.push(3);
      assertThat(queue.pop()).isEqualTo(1);
      assertThat(queue.pop()).isEqualTo(2);
      queue.push(4);
      assertThat(queue.pop()).isEqualTo(3);
      assertThat(queue.pop()).isEqualTo(4);
      assertThat(queue.empty()).isTrue();
    }

    @Test
    @DisplayName("Alternating push and pop")
    void testAlternating() {
      ImperativeMyQueue queue = new ImperativeMyQueue();
      queue.push(1);
      assertThat(queue.pop()).isEqualTo(1);
      queue.push(2);
      assertThat(queue.pop()).isEqualTo(2);
      assertThat(queue.empty()).isTrue();
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example from problem")
    void testExample() {
      FunctionalMyQueue queue = new FunctionalMyQueue();
      queue.push(1);
      queue.push(2);
      assertThat(queue.peek()).isEqualTo(1);
      assertThat(queue.pop()).isEqualTo(1);
      assertThat(queue.empty()).isFalse();
    }

    @Test
    @DisplayName("Empty queue")
    void testEmptyQueue() {
      FunctionalMyQueue queue = new FunctionalMyQueue();
      assertThat(queue.empty()).isTrue();
    }

    @Test
    @DisplayName("Push and pop multiple")
    void testPushPopMultiple() {
      FunctionalMyQueue queue = new FunctionalMyQueue();
      queue.push(1);
      queue.push(2);
      queue.push(3);
      assertThat(queue.pop()).isEqualTo(1);
      assertThat(queue.pop()).isEqualTo(2);
      queue.push(4);
      assertThat(queue.pop()).isEqualTo(3);
      assertThat(queue.pop()).isEqualTo(4);
      assertThat(queue.empty()).isTrue();
    }

    @Test
    @DisplayName("Alternating push and pop")
    void testAlternating() {
      FunctionalMyQueue queue = new FunctionalMyQueue();
      queue.push(1);
      assertThat(queue.pop()).isEqualTo(1);
      queue.push(2);
      assertThat(queue.pop()).isEqualTo(2);
      assertThat(queue.empty()).isTrue();
    }
  }
}
