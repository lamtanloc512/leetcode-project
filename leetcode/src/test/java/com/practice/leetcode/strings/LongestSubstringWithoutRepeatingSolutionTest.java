package com.practice.leetcode.strings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Longest Substring Without Repeating Characters
 * problem.
 */
class LongestSubstringWithoutRepeatingSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Sliding window with HashSet to track characters.
     * Time Complexity: O(n)
     * Space Complexity: O(min(m, n)) where m is charset size
     */
    public int lengthOfLongestSubstring(String s) {
      Set<Character> charSet = new HashSet<>();
      int maxLength = 0;
      int left = 0;

      for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);

        // Shrink window from left while duplicate exists
        while (charSet.contains(c)) {
          charSet.remove(s.charAt(left));
          left++;
        }

        charSet.add(c);
        maxLength = Math.max(maxLength, right - left + 1);
      }

      return maxLength;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Sliding window using reduce with state tracking.
     * Time Complexity: O(n)
     * Space Complexity: O(min(m, n))
     */
    public int lengthOfLongestSubstring(String s) {
      if (s.isEmpty())
        return 0;

      // State class (Java 8 compatible - replaces record)
      class State {
        final int left;
        final int maxLength;

        State(int left, int maxLength) {
          this.left = left;
          this.maxLength = maxLength;
        }
      }

      Map<Character, Integer> lastIndex = new HashMap<>();

      return IntStream.range(0, s.length())
          .boxed()
          .reduce(
              new State(0, 0),
              (state, right) -> {
                char c = s.charAt(right);
                int newLeft = Math.max(state.left,
                    lastIndex.getOrDefault(c, -1) + 1);
                lastIndex.put(c, right);
                int newMax = Math.max(state.maxLength, right - newLeft + 1);
                return new State(newLeft, newMax);
              },
              (a, b) -> a).maxLength;
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: 'abcabcbb' -> 3")
    void testExample1() {
      assertThat(imperativeSolution.lengthOfLongestSubstring("abcabcbb")).isEqualTo(3);
    }

    @Test
    @DisplayName("Example 2: 'bbbbb' -> 1")
    void testExample2() {
      assertThat(imperativeSolution.lengthOfLongestSubstring("bbbbb")).isEqualTo(1);
    }

    @Test
    @DisplayName("Example 3: 'pwwkew' -> 3")
    void testExample3() {
      assertThat(imperativeSolution.lengthOfLongestSubstring("pwwkew")).isEqualTo(3);
    }

    @Test
    @DisplayName("Empty string")
    void testEmptyString() {
      assertThat(imperativeSolution.lengthOfLongestSubstring("")).isEqualTo(0);
    }

    @Test
    @DisplayName("Single character")
    void testSingleChar() {
      assertThat(imperativeSolution.lengthOfLongestSubstring("a")).isEqualTo(1);
    }

    @Test
    @DisplayName("All unique characters")
    void testAllUnique() {
      assertThat(imperativeSolution.lengthOfLongestSubstring("abcdef")).isEqualTo(6);
    }

    @Test
    @DisplayName("With space: 'ab cd'")
    void testWithSpace() {
      assertThat(imperativeSolution.lengthOfLongestSubstring("ab cd")).isEqualTo(5);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: 'abcabcbb' -> 3")
    void testExample1() {
      assertThat(functionalSolution.lengthOfLongestSubstring("abcabcbb")).isEqualTo(3);
    }

    @Test
    @DisplayName("Example 2: 'bbbbb' -> 1")
    void testExample2() {
      assertThat(functionalSolution.lengthOfLongestSubstring("bbbbb")).isEqualTo(1);
    }

    @Test
    @DisplayName("Example 3: 'pwwkew' -> 3")
    void testExample3() {
      assertThat(functionalSolution.lengthOfLongestSubstring("pwwkew")).isEqualTo(3);
    }

    @Test
    @DisplayName("Empty string")
    void testEmptyString() {
      assertThat(functionalSolution.lengthOfLongestSubstring("")).isEqualTo(0);
    }

    @Test
    @DisplayName("Single character")
    void testSingleChar() {
      assertThat(functionalSolution.lengthOfLongestSubstring("a")).isEqualTo(1);
    }

    @Test
    @DisplayName("All unique characters")
    void testAllUnique() {
      assertThat(functionalSolution.lengthOfLongestSubstring("abcdef")).isEqualTo(6);
    }

    @Test
    @DisplayName("With space: 'ab cd'")
    void testWithSpace() {
      assertThat(functionalSolution.lengthOfLongestSubstring("ab cd")).isEqualTo(5);
    }
  }
}
