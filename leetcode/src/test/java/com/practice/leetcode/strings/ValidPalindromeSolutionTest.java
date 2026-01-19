package com.practice.leetcode.strings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Valid Palindrome problem.
 */
class ValidPalindromeSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Two pointers approach, skipping non-alphanumeric characters.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public boolean isPalindrome(String s) {
      int left = 0;
      int right = s.length() - 1;

      while (left < right) {
        // Skip non-alphanumeric from left
        while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
          left++;
        }
        // Skip non-alphanumeric from right
        while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
          right--;
        }

        if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
          return false;
        }

        left++;
        right--;
      }

      return true;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Filter and compare using streams.
     * Time Complexity: O(n)
     * Space Complexity: O(n) - creates filtered string
     */
    public boolean isPalindrome(String s) {
      String cleaned = s.chars()
          .filter(Character::isLetterOrDigit)
          .map(Character::toLowerCase)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();

      return IntStream.range(0, cleaned.length() / 2)
          .allMatch(i -> cleaned.charAt(i) == cleaned.charAt(cleaned.length() - 1 - i));
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: 'A man, a plan, a canal: Panama' -> true")
    void testExample1() {
      assertThat(imperativeSolution.isPalindrome("A man, a plan, a canal: Panama")).isTrue();
    }

    @Test
    @DisplayName("Example 2: 'race a car' -> false")
    void testExample2() {
      assertThat(imperativeSolution.isPalindrome("race a car")).isFalse();
    }

    @Test
    @DisplayName("Example 3: ' ' -> true")
    void testExample3() {
      assertThat(imperativeSolution.isPalindrome(" ")).isTrue();
    }

    @Test
    @DisplayName("Empty string")
    void testEmptyString() {
      assertThat(imperativeSolution.isPalindrome("")).isTrue();
    }

    @Test
    @DisplayName("Single character")
    void testSingleChar() {
      assertThat(imperativeSolution.isPalindrome("a")).isTrue();
    }

    @Test
    @DisplayName("With numbers: '0P' -> false")
    void testWithNumbers() {
      assertThat(imperativeSolution.isPalindrome("0P")).isFalse();
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: 'A man, a plan, a canal: Panama' -> true")
    void testExample1() {
      assertThat(functionalSolution.isPalindrome("A man, a plan, a canal: Panama")).isTrue();
    }

    @Test
    @DisplayName("Example 2: 'race a car' -> false")
    void testExample2() {
      assertThat(functionalSolution.isPalindrome("race a car")).isFalse();
    }

    @Test
    @DisplayName("Example 3: ' ' -> true")
    void testExample3() {
      assertThat(functionalSolution.isPalindrome(" ")).isTrue();
    }

    @Test
    @DisplayName("Empty string")
    void testEmptyString() {
      assertThat(functionalSolution.isPalindrome("")).isTrue();
    }

    @Test
    @DisplayName("Single character")
    void testSingleChar() {
      assertThat(functionalSolution.isPalindrome("a")).isTrue();
    }

    @Test
    @DisplayName("With numbers: '0P' -> false")
    void testWithNumbers() {
      assertThat(functionalSolution.isPalindrome("0P")).isFalse();
    }
  }
}
