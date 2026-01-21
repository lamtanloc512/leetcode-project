package com.practice.leetcode.strings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ STRING TEMPLATES                                                          ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class StringTemplates {

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 1: CHARACTER FREQUENCY (LOWERCASE ENGLISH LETTERS)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Anagram, Isomorphic Strings, First Unique Char
  public boolean solveAnagram(String s, String t) {
    if (s.length() != t.length()) return false;

    int[] counts = new int[26]; // 0 -> 'a', 25 -> 'z'

    for (char c : s.toCharArray()) {
      counts[c - 'a']++;
    }

    for (char c : t.toCharArray()) {
      counts[c - 'a']--;
      if (counts[c - 'a'] < 0) {
        return false;
      }
    }
    return true;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 2: GENERAL FREQUENCY MAP
  // ═══════════════════════════════════════════════════════════════════════════
  // Use when characters can be unicode or special symbols
  public Map<Character, Integer> buildFreqMap(String s) {
    Map<Character, Integer> map = new HashMap<>();
    for (char c : s.toCharArray()) {
      map.put(c, map.getOrDefault(c, 0) + 1);
    }
    return map;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 3: PALINDROME CHECK (EXPAND OUTWARDS)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Longest Palindromic Substring
  public String longestPalindrome(String s) {
    if (s == null || s.length() < 1) return "";
    int start = 0, end = 0;

    for (int i = 0; i < s.length(); i++) {
      int len1 = expandAroundCenter(s, i, i);      // Odd length (e.g., "aba")
      int len2 = expandAroundCenter(s, i, i + 1);  // Even length (e.g., "abba")
      int len = Math.max(len1, len2);

      if (len > end - start) {
        start = i - (len - 1) / 2;
        end = i + len / 2;
      }
    }
    return s.substring(start, end + 1);
  }

  private int expandAroundCenter(String s, int left, int right) {
    while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
      left--;
      right++;
    }
    return right - left - 1; // Length
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 4: SORTED STRING (KEY)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Group Anagrams
  public String getSortedKey(String s) {
    char[] chars = s.toCharArray();
    Arrays.sort(chars);
    return new String(chars);
  }
}
