package com.practice.leetcode.stack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Min Stack problem.
 */
class MinStackSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeMinStack {
    private final Deque<Integer> stack;
    private final Deque<Integer> minStack;

    /**
     * Uses two stacks - one for values, one for tracking minimums.
     * All operations: O(1) time, O(n) space
     */
    public ImperativeMinStack() {
      stack = new ArrayDeque<>();
      minStack = new ArrayDeque<>();
    }

    public void push(int val) {
      stack.push(val);
      if (minStack.isEmpty() || val <= minStack.peek()) {
        minStack.push(val);
      }
    }

    public void pop() {
      int removed = stack.pop();
      if (removed == minStack.peek()) {
        minStack.pop();
      }
    }

    public int top() {
      return stack.peek();
    }

    public int getMin() {
      return minStack.peek();
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalMinStack {
    // StackNode class (Java 8 compatible - replaces record)
    private static class StackNode {
      final int val;
      final int min;

      StackNode(int val, int min) {
        this.val = val;
        this.min = min;
      }

      int val() {
        return val;
      }

      int min() {
        return min;
      }
    }

    private final Deque<StackNode> stack;

    /**
     * Uses a single stack storing both value and current min.
     * All operations: O(1) time, O(n) space
     */
    public FunctionalMinStack() {
      stack = new ArrayDeque<>();
    }

    public void push(int val) {
      int currentMin = stack.isEmpty() ? val : Math.min(val, stack.peek().min());
      stack.push(new StackNode(val, currentMin));
    }

    public void pop() {
      stack.pop();
    }

    public int top() {
      return stack.peek().val();
    }

    public int getMin() {
      return stack.peek().min();
    }
  }

  // ========== TESTS ==========

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example from problem")
    void testExample() {
      ImperativeMinStack minStack = new ImperativeMinStack();
      minStack.push(-2);
      minStack.push(0);
      minStack.push(-3);
      assertThat(minStack.getMin()).isEqualTo(-3);
      minStack.pop();
      assertThat(minStack.top()).isEqualTo(0);
      assertThat(minStack.getMin()).isEqualTo(-2);
    }

    @Test
    @DisplayName("All same values")
    void testAllSameValues() {
      ImperativeMinStack minStack = new ImperativeMinStack();
      minStack.push(1);
      minStack.push(1);
      minStack.push(1);
      assertThat(minStack.getMin()).isEqualTo(1);
      minStack.pop();
      assertThat(minStack.getMin()).isEqualTo(1);
    }

    @Test
    @DisplayName("Increasing values")
    void testIncreasingValues() {
      ImperativeMinStack minStack = new ImperativeMinStack();
      minStack.push(1);
      minStack.push(2);
      minStack.push(3);
      assertThat(minStack.getMin()).isEqualTo(1);
      assertThat(minStack.top()).isEqualTo(3);
    }

    @Test
    @DisplayName("Decreasing values")
    void testDecreasingValues() {
      ImperativeMinStack minStack = new ImperativeMinStack();
      minStack.push(3);
      minStack.push(2);
      minStack.push(1);
      assertThat(minStack.getMin()).isEqualTo(1);
      minStack.pop();
      assertThat(minStack.getMin()).isEqualTo(2);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example from problem")
    void testExample() {
      FunctionalMinStack minStack = new FunctionalMinStack();
      minStack.push(-2);
      minStack.push(0);
      minStack.push(-3);
      assertThat(minStack.getMin()).isEqualTo(-3);
      minStack.pop();
      assertThat(minStack.top()).isEqualTo(0);
      assertThat(minStack.getMin()).isEqualTo(-2);
    }

    @Test
    @DisplayName("All same values")
    void testAllSameValues() {
      FunctionalMinStack minStack = new FunctionalMinStack();
      minStack.push(1);
      minStack.push(1);
      minStack.push(1);
      assertThat(minStack.getMin()).isEqualTo(1);
      minStack.pop();
      assertThat(minStack.getMin()).isEqualTo(1);
    }

    @Test
    @DisplayName("Increasing values")
    void testIncreasingValues() {
      FunctionalMinStack minStack = new FunctionalMinStack();
      minStack.push(1);
      minStack.push(2);
      minStack.push(3);
      assertThat(minStack.getMin()).isEqualTo(1);
      assertThat(minStack.top()).isEqualTo(3);
    }

    @Test
    @DisplayName("Decreasing values")
    void testDecreasingValues() {
      FunctionalMinStack minStack = new FunctionalMinStack();
      minStack.push(3);
      minStack.push(2);
      minStack.push(1);
      assertThat(minStack.getMin()).isEqualTo(1);
      minStack.pop();
      assertThat(minStack.getMin()).isEqualTo(2);
    }
  }
}
