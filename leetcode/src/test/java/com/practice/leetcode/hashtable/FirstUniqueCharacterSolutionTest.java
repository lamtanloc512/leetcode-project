package com.practice.leetcode.hashtable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for First Unique Character in a String problem.
 */
class FirstUniqueCharacterSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Two-pass with character count array.
     * Time Complexity: O(n)
     * Space Complexity: O(1) - fixed 26 characters
     */
    public int firstUniqChar(String s) {
      int[] count = new int[26];

      // First pass: count frequencies
      for (char c : s.toCharArray()) {
        count[c - 'a']++;
      }

      // Second pass: find first unique
      for (int i = 0; i < s.length(); i++) {
        if (count[s.charAt(i) - 'a'] == 1) {
          return i;
        }
      }

      return -1;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Stream-based frequency counting.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int firstUniqChar(String s) {
      Map<Integer, Long> frequency = s.chars()
          .boxed()
          .collect(Collectors.groupingBy(
              Function.identity(),
              Collectors.counting()));

      return IntStream.range(0, s.length())
          .filter(i -> frequency.get((int) s.charAt(i)) == 1)
          .findFirst()
          .orElse(-1);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: 'leetcode' -> 0")
    void testExample1() {
      assertThat(imperativeSolution.firstUniqChar("leetcode")).isEqualTo(0);
    }

    @Test
    @DisplayName("Example 2: 'loveleetcode' -> 2")
    void testExample2() {
      assertThat(imperativeSolution.firstUniqChar("loveleetcode")).isEqualTo(2);
    }

    @Test
    @DisplayName("Example 3: 'aabb' -> -1")
    void testExample3() {
      assertThat(imperativeSolution.firstUniqChar("aabb")).isEqualTo(-1);
    }

    @Test
    @DisplayName("Single character")
    void testSingleChar() {
      assertThat(imperativeSolution.firstUniqChar("a")).isEqualTo(0);
    }

    @Test
    @DisplayName("All same characters")
    void testAllSame() {
      assertThat(imperativeSolution.firstUniqChar("aaaa")).isEqualTo(-1);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: 'leetcode' -> 0")
    void testExample1() {
      assertThat(functionalSolution.firstUniqChar("leetcode")).isEqualTo(0);
    }

    @Test
    @DisplayName("Example 2: 'loveleetcode' -> 2")
    void testExample2() {
      assertThat(functionalSolution.firstUniqChar("loveleetcode")).isEqualTo(2);
    }

    @Test
    @DisplayName("Example 3: 'aabb' -> -1")
    void testExample3() {
      assertThat(functionalSolution.firstUniqChar("aabb")).isEqualTo(-1);
    }

    @Test
    @DisplayName("Single character")
    void testSingleChar() {
      assertThat(functionalSolution.firstUniqChar("a")).isEqualTo(0);
    }

    @Test
    @DisplayName("All same characters")
    void testAllSame() {
      assertThat(functionalSolution.firstUniqChar("aaaa")).isEqualTo(-1);
    }
  }
}
