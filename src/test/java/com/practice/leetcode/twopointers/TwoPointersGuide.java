package com.practice.leetcode.twopointers;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ TWO POINTERS GUIDE ║
 * ║ (Kỹ thuật hai con trỏ) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ TWO POINTERS là gì? │
 * │ │
 * │ - Dùng 2 con trỏ di chuyển trên mảng/chuỗi │
 * │ - Giảm độ phức tạp từ O(n²) xuống O(n) │
 * │ - Thường dùng trên SORTED array hoặc khi cần so sánh 2 vị trí │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * 3 LOẠI TWO POINTERS
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * 1. OPPOSITE DIRECTION: left → ← right (từ 2 đầu vào giữa)
 * 2. SAME DIRECTION: slow → fast → (fast/slow pointers)
 * 3. TWO ARRAYS: Duyệt 2 mảng đồng thời
 */
public class TwoPointersGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 1: OPPOSITE DIRECTION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Dùng cho:
   * - Two Sum (sorted array)
   * - Container With Most Water
   * - Valid Palindrome
   * - 3Sum, 4Sum (kết hợp với sort)
   *
   * Template:
   *
   * int left = 0, right = n - 1;
   * while (left < right) {
   * int sum = arr[left] + arr[right];
   *
   * if (sum == target) {
   * // Found!
   * } else if (sum < target) {
   * left++; // Cần tăng sum
   * } else {
   * right--; // Cần giảm sum
   * }
   * }
   */

  // Two Sum II (Sorted Array)
  int[] twoSumSorted(int[] nums, int target) {
    int left = 0, right = nums.length - 1;

    while (left < right) {
      int sum = nums[left] + nums[right];

      if (sum == target) {
        return new int[] { left + 1, right + 1 }; // 1-indexed
      } else if (sum < target) {
        left++;
      } else {
        right--;
      }
    }

    return new int[] { -1, -1 };
  }

  // Container With Most Water
  int maxArea(int[] height) {
    int left = 0, right = height.length - 1;
    int maxArea = 0;

    while (left < right) {
      int width = right - left;
      int h = Math.min(height[left], height[right]);
      maxArea = Math.max(maxArea, width * h);

      // Di chuyển cột THẤP hơn (hy vọng tìm cột cao hơn)
      if (height[left] < height[right]) {
        left++;
      } else {
        right--;
      }
    }

    return maxArea;
  }

  // Valid Palindrome
  boolean isPalindrome(String s) {
    int left = 0, right = s.length() - 1;

    while (left < right) {
      // Skip non-alphanumeric
      while (left < right && !Character.isLetterOrDigit(s.charAt(left)))
        left++;
      while (left < right && !Character.isLetterOrDigit(s.charAt(right)))
        right--;

      if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
        return false;
      }
      left++;
      right--;
    }

    return true;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 2: SAME DIRECTION (Fast/Slow)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Dùng cho:
   * - Remove Duplicates
   * - Move Zeros
   * - Remove Element
   * - Partition array
   *
   * Template:
   *
   * int slow = 0;
   * for (int fast = 0; fast < n; fast++) {
   * if (condition(arr[fast])) {
   * arr[slow] = arr[fast];
   * slow++;
   * }
   * }
   *
   * Ý nghĩa:
   * - slow: vị trí cần điền tiếp theo
   * - fast: đang xét phần tử nào
   * - [0, slow): phần đã xử lý OK
   * - [slow, fast): phần bị "loại bỏ"
   */

  // Remove Duplicates from Sorted Array
  int removeDuplicates(int[] nums) {
    if (nums.length == 0)
      return 0;

    int slow = 1; // Vị trí cần điền

    for (int fast = 1; fast < nums.length; fast++) {
      if (nums[fast] != nums[slow - 1]) {
        nums[slow] = nums[fast];
        slow++;
      }
    }

    return slow; // Số phần tử unique
  }

  // Move Zeros to End
  void moveZeroes(int[] nums) {
    int slow = 0;

    // Đưa tất cả non-zero lên đầu
    for (int fast = 0; fast < nums.length; fast++) {
      if (nums[fast] != 0) {
        // Swap
        int temp = nums[slow];
        nums[slow] = nums[fast];
        nums[fast] = temp;
        slow++;
      }
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 3: 3SUM / KSUM
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Ý tưởng: Fix 1 phần tử, dùng Two Pointers cho 2 phần tử còn lại
   *
   * 3Sum = O(n²): Sort + For + Two Pointers
   * 4Sum = O(n³): Sort + For + For + Two Pointers
   *
   * Key: Skip duplicates để tránh kết quả trùng!
   */
  java.util.List<java.util.List<Integer>> threeSum(int[] nums) {
    java.util.Arrays.sort(nums);
    java.util.List<java.util.List<Integer>> result = new java.util.ArrayList<>();

    for (int i = 0; i < nums.length - 2; i++) {
      // Skip duplicates for i
      if (i > 0 && nums[i] == nums[i - 1])
        continue;

      int target = -nums[i];
      int left = i + 1, right = nums.length - 1;

      while (left < right) {
        int sum = nums[left] + nums[right];

        if (sum == target) {
          result.add(java.util.Arrays.asList(nums[i], nums[left], nums[right]));

          // Skip duplicates for left and right
          while (left < right && nums[left] == nums[left + 1])
            left++;
          while (left < right && nums[right] == nums[right - 1])
            right--;
          left++;
          right--;
        } else if (sum < target) {
          left++;
        } else {
          right--;
        }
      }
    }

    return result;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 4: TWO ARRAYS / MERGE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Dùng cho: Merge sorted arrays/lists, Intersection, Union
   *
   * Template:
   *
   * int i = 0, j = 0;
   * while (i < m && j < n) {
   * if (arr1[i] < arr2[j]) {
   * // Process arr1[i]
   * i++;
   * } else if (arr1[i] > arr2[j]) {
   * // Process arr2[j]
   * j++;
   * } else {
   * // Equal
   * i++; j++;
   * }
   * }
   * // Handle remaining
   */

  // Merge Sorted Arrays
  void merge(int[] nums1, int m, int[] nums2, int n) {
    int p1 = m - 1;
    int p2 = n - 1;
    int p = m + n - 1;

    // Merge từ cuối
    while (p1 >= 0 && p2 >= 0) {
      if (nums1[p1] > nums2[p2]) {
        nums1[p] = nums1[p1];
        p1--;
      } else {
        nums1[p] = nums2[p2];
        p2--;
      }
      p--;
    }

    // Copy remaining từ nums2
    while (p2 >= 0) {
      nums1[p] = nums2[p2];
      p2--;
      p--;
    }
  }

  // Intersection of Two Arrays
  int[] intersect(int[] nums1, int[] nums2) {
    java.util.Arrays.sort(nums1);
    java.util.Arrays.sort(nums2);

    java.util.List<Integer> result = new java.util.ArrayList<>();
    int i = 0, j = 0;

    while (i < nums1.length && j < nums2.length) {
      if (nums1[i] < nums2[j]) {
        i++;
      } else if (nums1[i] > nums2[j]) {
        j++;
      } else {
        result.add(nums1[i]);
        i++;
        j++;
      }
    }

    return result.stream().mapToInt(x -> x).toArray();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BẢNG CHỌN PATTERN
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌──────────────────────────────────────────────────────────────────────────┐
   * │ Bài toán │ Pattern │ Điều kiện │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Tìm cặp trong sorted array │ Opposite │ Array sorted │
   * │ Palindrome check │ Opposite │ So sánh 2 đầu │
   * │ 3Sum, 4Sum │ Fix + Opposite │ Sort first │
   * │ Max area/water │ Opposite │ Maximize width │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Remove duplicates │ Same (slow/fast) │ In-place modify │
   * │ Move/Remove elements │ Same (slow/fast) │ Partition │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Merge sorted arrays │ Two Arrays │ Both sorted │
   * │ Intersection/Union │ Two Arrays │ Sort first │
   * └──────────────────────────────────────────────────────────────────────────┘
   *
   *
   * CÁC BÀI THỰC HÀNH
   *
   * EASY:
   * - Two Sum II (167)
   * - Valid Palindrome (125)
   * - Merge Sorted Array (88)
   * - Remove Duplicates (26)
   *
   * MEDIUM:
   * - 3Sum (15)
   * - Container With Most Water (11)
   * - 4Sum (18)
   * - Sort Colors (75) - Dutch National Flag
   *
   * HARD:
   * - Trapping Rain Water (42)
   */
}
