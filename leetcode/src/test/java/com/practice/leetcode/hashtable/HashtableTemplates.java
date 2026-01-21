package com.practice.leetcode.hashtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ HASHTABLE TEMPLATES                                                       ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class HashtableTemplates {

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 1: FREQUENCY MAP
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Counting elements, finding majority, etc.
  public Map<Integer, Integer> buildFrequencyMap(int[] nums) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int num : nums) {
      map.put(num, map.getOrDefault(num, 0) + 1);
    }
    return map;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 2: FIND PAIR WITH TARGET (TWO SUM)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Two Sum, Subarray Sum equals K (prefix sum map)
  public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>(); // val -> index

    for (int i = 0; i < nums.length; i++) {
      int complement = target - nums[i];
      if (map.containsKey(complement)) {
        return new int[]{map.get(complement), i};
      }
      map.put(nums[i], i);
    }
    return new int[]{-1, -1};
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 3: GROUPING / BUCKETS
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Group Anagrams, Group by characteristics
  public List<List<String>> groupStrings(String[] strings) {
    Map<String, List<String>> map = new HashMap<>();

    for (String s : strings) {
      String key = "TODO: Generate Key for " + s; 
      // e.g., Sorted string, or feature hash
      
      map.putIfAbsent(key, new ArrayList<>());
      map.get(key).add(s);
    }
    
    return new ArrayList<>(map.values());
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 4: SET FOR TRAVERSAL / UNIQUENESS
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Longest Consecutive Sequence, Intersection
  public int longestConsecutive(int[] nums) {
    Set<Integer> set = new HashSet<>();
    for (int num : nums) set.add(num);

    int maxLen = 0;

    for (int num : set) {
      // Chỉ bắt đầu đếm nếu là điểm khởi đầu (không có num-1)
      if (!set.contains(num - 1)) {
        int currentNum = num;
        int currentLen = 1;

        while (set.contains(currentNum + 1)) {
          currentNum++;
          currentLen++;
        }
        maxLen = Math.max(maxLen, currentLen);
      }
    }
    return maxLen;
  }
}
