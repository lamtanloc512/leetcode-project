package com.practice.leetcode.twopointers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ TWO POINTERS TEMPLATES (Skeletons)                                        ║ 
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 * <p>
 * File này chứa các bộ khung code (skeleton) chuẩn cho các dạng bài Two
 * Pointers.
 * Bạn có thể copy method này và điền logic cụ thể vào chỗ "TODO".
 */
public class TwoPointersTemplates {

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 1: OPPOSITE DIRECTION (Ngược chiều)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Two Sum (sorted), Palindrome, Reverse String, Container With Most
  // Water
  @SuppressWarnings("unused")
  public void solveOppositeDirection(int[] nums) {
    int left = 0;
    int right = nums.length - 1;

    while (left < right) {
      // Logic xử lý tại đây
      // int sum = nums[left] + nums[right];
      boolean condition = false; // TODO: Condition

      if (condition) {
        // TODO: Found solution or processing
        return;
      } else if (/* should move left */ false) {
        left++;
      } else {
        right--;
      }
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 2: SAME DIRECTION / FAST-SLOW (Cùng chiều)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Remove Duplicates, Remove Element, Move Zeroes
  public int solveSameDirection(int[] nums) {
    int slow = 0;

    for (int fast = 0; fast < nums.length; fast++) {
      // TODO: Điều kiện để giữ lại phần tử
      boolean shouldKeep = (nums[fast] != -1);

      if (shouldKeep) {
        // Swap hoặc Gán
        nums[slow] = nums[fast];
        slow++;
      }
    }

    return slow; // Trả về độ dài mới
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 3: TWO ARRAYS MERGE (Ghép 2 mảng sort)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Merge Sorted Array, Intersection of Two Arrays
  public List<Integer> solveMergeTwoArrays(int[] arr1, int[] arr2) {
    List<Integer> result = new ArrayList<>();
    int i = 0, j = 0;

    while (i < arr1.length && j < arr2.length) {
      if (arr1[i] < arr2[j]) {
        // TODO: Process arr1[i]
        i++;
      } else if (arr1[i] > arr2[j]) {
        // TODO: Process arr2[j]
        j++;
      } else {
        // TODO: Process Equal element (cho Intersection/Union)
        result.add(arr1[i]);
        i++;
        j++;
      }
    }

    // Xử lý phần còn lại (nếu cần)
    while (i < arr1.length)
      result.add(arr1[i++]);
    while (j < arr2.length)
      result.add(arr2[j++]);

    return result;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 4: 3SUM / K-SUM (Fix 1 + Two Pointers)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: 3Sum, 3Sum Closest, 4Sum
  public List<List<Integer>> solve3Sum(int[] nums) {
    Arrays.sort(nums); // Bắt buộc phải Sort
    List<List<Integer>> res = new ArrayList<>();

    for (int i = 0; i < nums.length - 2; i++) {
      // Skip duplicates cho số thứ 1
      if (i > 0 && nums[i] == nums[i - 1])
        continue;

      int left = i + 1, right = nums.length - 1;
      while (left < right) {
        int sum = nums[i] + nums[left] + nums[right];

        if (sum == 0) { // TODO: Target logic
          res.add(Arrays.asList(nums[i], nums[left], nums[right]));

          // Skip duplicates cho 2 pointers còn lại
          while (left < right && nums[left] == nums[left + 1])
            left++;
          while (left < right && nums[right] == nums[right - 1])
            right--;
          left++;
          right--;
        } else if (sum < 0) {
          left++;
        } else {
          right--;
        }
      }
    }
    return res;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 5: REVERSE ITERATION WITH CARRY (Cộng ngược)
  // ═══════════════════════════════════════════════════════════════════════════
  // Use for: Add Strings, Add Binary, Plus One, Add Array-Form
  public String solveReverseCarry(String num1, String num2) {
    StringBuilder sb = new StringBuilder();
    int i = num1.length() - 1;
    int j = num2.length() - 1;
    int carry = 0;

    // Lặp khi còn số hoặc còn nhớ
    while (i >= 0 || j >= 0 || carry > 0) {
      int sum = carry;

      if (i >= 0) {
        sum += num1.charAt(i) - '0'; // Convert char -> int
        i--;
      }

      if (j >= 0) {
        sum += num2.charAt(j) - '0'; // Convert char -> int
        j--;
      }

      sb.append(sum % 10); // Lấy phần đơn vị
      carry = sum / 10; // Tính nhớ
    }

    return sb.reverse().toString(); // Đảo ngược kết quả
  }
}
