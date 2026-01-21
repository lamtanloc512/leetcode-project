package com.practice.leetcode.strings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Valid Anagram problem.
 */
class ValidAnagramSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Uses character count array for lowercase English letters.
     * Time Complexity: O(n)
     * Space Complexity: O(1) - fixed 26 character array
     */
    public boolean isAnagram(String s, String t) {
      if (s.length() != t.length()) {
        return false;
      }

      int[] charCount = new int[26];

      for (int i = 0; i < s.length(); i++) {
        charCount[s.charAt(i) - 'a']++;
        charCount[t.charAt(i) - 'a']--;
      }

      for (int count : charCount) {
        if (count != 0) {
          return false;
        }
      }

      return true;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Uses streams to count character frequencies and compare.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean isAnagram(String s, String t) {
      if (s.length() != t.length()) {
        return false;
      }

      Map<Integer, Long> sFreq = s.chars()
          .boxed()
          .collect(Collectors.groupingBy(
              Function.identity(),
              Collectors.counting()));

      Map<Integer, Long> tFreq = t.chars()
          .boxed()
          .collect(Collectors.groupingBy(
              Function.identity(),
              Collectors.counting()));

      return sFreq.equals(tFreq);
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: anagram, nagaram -> true")
    void testExample1() {
      assertThat(imperativeSolution.isAnagram("anagram", "nagaram")).isTrue();
    }

    @Test
    @DisplayName("Example 2: rat, car -> false")
    void testExample2() {
      assertThat(imperativeSolution.isAnagram("rat", "car")).isFalse();
    }

    @Test
    @DisplayName("Same string")
    void testSameString() {
      assertThat(imperativeSolution.isAnagram("hello", "hello")).isTrue();
    }

    @Test
    @DisplayName("Different lengths")
    void testDifferentLengths() {
      assertThat(imperativeSolution.isAnagram("abc", "abcd")).isFalse();
    }

    @Test
    @DisplayName("Single character - same")
    void testSingleCharSame() {
      assertThat(imperativeSolution.isAnagram("a", "a")).isTrue();
    }

    @Test
    @DisplayName("Single character - different")
    void testSingleCharDifferent() {
      assertThat(imperativeSolution.isAnagram("a", "b")).isFalse();
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: anagram, nagaram -> true")
    void testExample1() {
      assertThat(functionalSolution.isAnagram("anagram", "nagaram")).isTrue();
    }

    @Test
    @DisplayName("Example 2: rat, car -> false")
    void testExample2() {
      assertThat(functionalSolution.isAnagram("rat", "car")).isFalse();
    }

    @Test
    @DisplayName("Same string")
    void testSameString() {
      assertThat(functionalSolution.isAnagram("hello", "hello")).isTrue();
    }

    @Test
    @DisplayName("Different lengths")
    void testDifferentLengths() {
      assertThat(functionalSolution.isAnagram("abc", "abcd")).isFalse();
    }

    @Test
    @DisplayName("Single character - same")
    void testSingleCharSame() {
      assertThat(functionalSolution.isAnagram("a", "a")).isTrue();
    }

    @Test
    @DisplayName("Single character - different")
    void testSingleCharDifferent() {
      assertThat(functionalSolution.isAnagram("a", "b")).isFalse();
    }
  }
}
