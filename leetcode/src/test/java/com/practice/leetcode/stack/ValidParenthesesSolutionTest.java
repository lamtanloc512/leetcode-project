package com.practice.leetcode.stack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Valid Parentheses problem.
 */
class ValidParenthesesSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Uses a stack to match opening and closing brackets.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean isValid(String s) {
      Deque<Character> stack = new ArrayDeque<>();

      for (char c : s.toCharArray()) {
        if (c == '(' || c == '{' || c == '[') {
          stack.push(c);
        } else {
          if (stack.isEmpty()) {
            return false;
          }

          char top = stack.pop();
          if ((c == ')' && top != '(') ||
              (c == '}' && top != '{') ||
              (c == ']' && top != '[')) {
            return false;
          }
        }
      }

      return stack.isEmpty();
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    private static final Map<Character, Character> BRACKETS;
    static {
      BRACKETS = new HashMap<>();
      BRACKETS.put(')', '(');
      BRACKETS.put('}', '{');
      BRACKETS.put(']', '[');
    }

    /**
     * Uses reduce to simulate stack operations functionally.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean isValid(String s) {
      Deque<Character> result = s.chars()
          .mapToObj(c -> (char) c)
          .reduce(
              new ArrayDeque<Character>(),
              (stack, c) -> {
                if (BRACKETS.containsValue(c)) {
                  stack.push(c);
                } else if (!stack.isEmpty() && stack.peek().equals(BRACKETS.get(c))) {
                  stack.pop();
                } else {
                  stack.push(c); // Invalid case marker
                }
                return stack;
              },
              (a, b) -> a);

      return result.isEmpty();
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: '()' -> true")
    void testExample1() {
      assertThat(imperativeSolution.isValid("()")).isTrue();
    }

    @Test
    @DisplayName("Example 2: '()[]{}' -> true")
    void testExample2() {
      assertThat(imperativeSolution.isValid("()[]{}")).isTrue();
    }

    @Test
    @DisplayName("Example 3: '(]' -> false")
    void testExample3() {
      assertThat(imperativeSolution.isValid("(]")).isFalse();
    }

    @Test
    @DisplayName("Nested brackets: '([{}])' -> true")
    void testNestedBrackets() {
      assertThat(imperativeSolution.isValid("([{}])")).isTrue();
    }

    @Test
    @DisplayName("Unbalanced: '(((' -> false")
    void testUnbalanced() {
      assertThat(imperativeSolution.isValid("(((")).isFalse();
    }

    @Test
    @DisplayName("Single closing: ')' -> false")
    void testSingleClosing() {
      assertThat(imperativeSolution.isValid(")")).isFalse();
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: '()' -> true")
    void testExample1() {
      assertThat(functionalSolution.isValid("()")).isTrue();
    }

    @Test
    @DisplayName("Example 2: '()[]{}' -> true")
    void testExample2() {
      assertThat(functionalSolution.isValid("()[]{}")).isTrue();
    }

    @Test
    @DisplayName("Example 3: '(]' -> false")
    void testExample3() {
      assertThat(functionalSolution.isValid("(]")).isFalse();
    }

    @Test
    @DisplayName("Nested brackets: '([{}])' -> true")
    void testNestedBrackets() {
      assertThat(functionalSolution.isValid("([{}])")).isTrue();
    }

    @Test
    @DisplayName("Unbalanced: '(((' -> false")
    void testUnbalanced() {
      assertThat(functionalSolution.isValid("(((")).isFalse();
    }

    @Test
    @DisplayName("Single closing: ')' -> false")
    void testSingleClosing() {
      assertThat(functionalSolution.isValid(")")).isFalse();
    }
  }
}
