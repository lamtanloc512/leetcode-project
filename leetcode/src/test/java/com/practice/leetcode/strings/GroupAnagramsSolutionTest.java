package com.practice.leetcode.strings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Group Anagrams problem.
 */
class GroupAnagramsSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  static class ImperativeSolution {
    /**
     * Group by sorted character string as key.
     * Time Complexity: O(n * k log k) where k is max string length
     * Space Complexity: O(n * k)
     */
    public List<List<String>> groupAnagrams(String[] strs) {
      Map<String, List<String>> groups = new HashMap<>();

      for (String str : strs) {
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        String key = new String(chars);

        groups.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
      }

      return new ArrayList<>(groups.values());
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Uses stream groupingBy with sorted string as key.
     * Time Complexity: O(n * k log k)
     * Space Complexity: O(n * k)
     */
    public List<List<String>> groupAnagrams(String[] strs) {
      return new ArrayList<>(
          Arrays.stream(strs)
              .collect(Collectors.groupingBy(
                  s -> s.chars()
                      .sorted()
                      .collect(StringBuilder::new,
                          StringBuilder::appendCodePoint,
                          StringBuilder::append)
                      .toString()))
              .values());
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [eat,tea,tan,ate,nat,bat]")
    void testExample1() {
      String[] strs = { "eat", "tea", "tan", "ate", "nat", "bat" };
      List<List<String>> result = imperativeSolution.groupAnagrams(strs);

      assertThat(result).hasSize(3);
      assertThat(result.stream().flatMap(List::stream).collect(Collectors.toSet()))
          .containsExactlyInAnyOrder("eat", "tea", "tan", "ate", "nat", "bat");
    }

    @Test
    @DisplayName("Example 2: [''] -> [['']]")
    void testExample2() {
      String[] strs = { "" };
      List<List<String>> result = imperativeSolution.groupAnagrams(strs);

      assertThat(result).hasSize(1);
      assertThat(result.get(0)).containsExactly("");
    }

    @Test
    @DisplayName("Example 3: ['a'] -> [['a']]")
    void testExample3() {
      String[] strs = { "a" };
      List<List<String>> result = imperativeSolution.groupAnagrams(strs);

      assertThat(result).hasSize(1);
      assertThat(result.get(0)).containsExactly("a");
    }

    @Test
    @DisplayName("No anagrams")
    void testNoAnagrams() {
      String[] strs = { "abc", "def", "ghi" };
      List<List<String>> result = imperativeSolution.groupAnagrams(strs);

      assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("All same anagrams")
    void testAllSameAnagrams() {
      String[] strs = { "abc", "bca", "cab", "acb" };
      List<List<String>> result = imperativeSolution.groupAnagrams(strs);

      assertThat(result).hasSize(1);
      assertThat(result.get(0)).hasSize(4);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [eat,tea,tan,ate,nat,bat]")
    void testExample1() {
      String[] strs = { "eat", "tea", "tan", "ate", "nat", "bat" };
      List<List<String>> result = functionalSolution.groupAnagrams(strs);

      assertThat(result).hasSize(3);
      assertThat(result.stream().flatMap(List::stream).collect(Collectors.toSet()))
          .containsExactlyInAnyOrder("eat", "tea", "tan", "ate", "nat", "bat");
    }

    @Test
    @DisplayName("Example 2: [''] -> [['']]")
    void testExample2() {
      String[] strs = { "" };
      List<List<String>> result = functionalSolution.groupAnagrams(strs);

      assertThat(result).hasSize(1);
      assertThat(result.get(0)).containsExactly("");
    }

    @Test
    @DisplayName("Example 3: ['a'] -> [['a']]")
    void testExample3() {
      String[] strs = { "a" };
      List<List<String>> result = functionalSolution.groupAnagrams(strs);

      assertThat(result).hasSize(1);
      assertThat(result.get(0)).containsExactly("a");
    }

    @Test
    @DisplayName("No anagrams")
    void testNoAnagrams() {
      String[] strs = { "abc", "def", "ghi" };
      List<List<String>> result = functionalSolution.groupAnagrams(strs);

      assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("All same anagrams")
    void testAllSameAnagrams() {
      String[] strs = { "abc", "bca", "cab", "acb" };
      List<List<String>> result = functionalSolution.groupAnagrams(strs);

      assertThat(result).hasSize(1);
      assertThat(result.get(0)).hasSize(4);
    }
  }
}
