package com.practice.leetcode.binarysearch;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ BINARY SEARCH GUIDE ║
 * ║ (Tìm kiếm nhị phân) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ BINARY SEARCH là gì? │
 * │ │
 * │ - Chia đôi search space mỗi bước │
 * │ - Giảm từ O(n) xuống O(log n) │
 * │ - YÊU CẦU: Mảng SORTED hoặc có tính chất MONOTONIC │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class BinarySearchGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // 3 TEMPLATE CƠ BẢN
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * TEMPLATE 1: Basic Binary Search
   * - Tìm CHÍNH XÁC target
   * - while (left <= right)
   * - Return index hoặc -1
   */
  int basicBinarySearch(int[] nums, int target) {
    int left = 0, right = nums.length - 1;

    while (left <= right) {
      int mid = left + (right - left) / 2; // Tránh overflow

      if (nums[mid] == target) {
        return mid;
      } else if (nums[mid] < target) {
        left = mid + 1;
      } else {
        right = mid - 1;
      }
    }

    return -1;
  }

  /**
   * TEMPLATE 2: Left Bound (Lower Bound)
   * - Tìm vị trí ĐẦU TIÊN >= target
   * - while (left < right)
   * - Dùng cho: first occurrence, insert position
   *
   * Key: Khi nums[mid] >= target, right = mid (giữ mid làm candidate)
   */
  int leftBound(int[] nums, int target) {
    int left = 0, right = nums.length; // right = n, not n-1!

    while (left < right) {
      int mid = left + (right - left) / 2;

      if (nums[mid] >= target) {
        right = mid; // mid có thể là đáp án
      } else {
        left = mid + 1;
      }
    }

    return left; // Vị trí đầu tiên >= target
  }

  /**
   * TEMPLATE 3: Right Bound (Upper Bound)
   * - Tìm vị trí ĐẦU TIÊN > target (hoặc CUỐI CÙNG <= target)
   * - while (left < right)
   *
   * Key: Khi nums[mid] <= target, left = mid + 1
   */
  int rightBound(int[] nums, int target) {
    int left = 0, right = nums.length;

    while (left < right) {
      int mid = left + (right - left) / 2;

      if (nums[mid] > target) {
        right = mid;
      } else {
        left = mid + 1; // mid không phải đáp án
      }
    }

    return left; // Vị trí đầu tiên > target
    // Vị trí cuối cùng của target = left - 1
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // VÍ DỤ: FIRST AND LAST POSITION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Tìm vị trí đầu tiên và cuối cùng của target trong sorted array
   *
   * Sử dụng: leftBound và rightBound
   */
  int[] searchRange(int[] nums, int target) {
    if (nums.length == 0)
      return new int[] { -1, -1 };

    int left = leftBound(nums, target);

    // Kiểm tra target có tồn tại không
    if (left >= nums.length || nums[left] != target) {
      return new int[] { -1, -1 };
    }

    int right = rightBound(nums, target) - 1;

    return new int[] { left, right };
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN: SEARCH IN ROTATED ARRAY
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Array sorted bị rotate: [4,5,6,7,0,1,2]
   *
   * Key insight: Một nửa LUÔN sorted!
   *
   * Cách xác định:
   * - Nếu nums[left] <= nums[mid] → Nửa trái sorted
   * - Ngược lại → Nửa phải sorted
   *
   * Rồi check target nằm trong nửa sorted hay không
   */
  int searchRotated(int[] nums, int target) {
    int left = 0, right = nums.length - 1;

    while (left <= right) {
      int mid = left + (right - left) / 2;

      if (nums[mid] == target)
        return mid;

      // Nửa trái sorted
      if (nums[left] <= nums[mid]) {
        // Target trong nửa trái?
        if (nums[left] <= target && target < nums[mid]) {
          right = mid - 1;
        } else {
          left = mid + 1;
        }
      }
      // Nửa phải sorted
      else {
        // Target trong nửa phải?
        if (nums[mid] < target && target <= nums[right]) {
          left = mid + 1;
        } else {
          right = mid - 1;
        }
      }
    }

    return -1;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN: FIND PEAK / MINIMUM
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Tìm peak element hoặc minimum trong rotated array
   *
   * Key: So sánh mid với neighbor để quyết định đi trái hay phải
   */

  // Find Peak Element
  int findPeakElement(int[] nums) {
    int left = 0, right = nums.length - 1;

    while (left < right) {
      int mid = left + (right - left) / 2;

      if (nums[mid] > nums[mid + 1]) {
        // Đang đi xuống → peak ở bên trái (hoặc chính mid)
        right = mid;
      } else {
        // Đang đi lên → peak ở bên phải
        left = mid + 1;
      }
    }

    return left;
  }

  // Find Minimum in Rotated Sorted Array
  int findMin(int[] nums) {
    int left = 0, right = nums.length - 1;

    while (left < right) {
      int mid = left + (right - left) / 2;

      if (nums[mid] > nums[right]) {
        // Min ở bên phải
        left = mid + 1;
      } else {
        // Min ở bên trái (có thể chính là mid)
        right = mid;
      }
    }

    return nums[left];
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BẢNG CHỌN TEMPLATE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌──────────────────────────────────────────────────────────────────────────┐
   * │ Loại bài │ Template │ Condition │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Tìm chính xác target │ Basic │ while (left <= right) │
   * │ First occurrence │ Left Bound │ right = mid │
   * │ Last occurrence │ Right Bound │ left = mid + 1 │
   * │ Insert position │ Left Bound │ right = mid │
   * │ Rotated array │ Basic │ Check which half sorted│
   * │ Peak/Min element │ Left Bound │ Compare with neighbor │
   * │ Binary search on answer │ Left Bound │ Check feasibility │
   * └──────────────────────────────────────────────────────────────────────────┘
   *
   *
   * MẸO TRÁNH LỖI THƯỜNG GẶP
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ 1. Overflow: Dùng mid = left + (right - left) / 2 │
   * │ 2. Infinite loop: Đảm bảo left hoặc right THAY ĐỔI mỗi iteration │
   * │ 3. Off-by-one: Cẩn thận với right = n hay n-1 │
   * │ 4. Empty array: Kiểm tra nums.length == 0 │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   *
   * CÁC BÀI THỰC HÀNH
   *
   * EASY:
   * - Binary Search (704)
   * - Search Insert Position (35)
   * - First Bad Version (278)
   *
   * MEDIUM:
   * - Find First and Last Position (34)
   * - Search in Rotated Sorted Array (33)
   * - Find Peak Element (162)
   * - Find Minimum in Rotated Sorted Array (153)
   *
   * HARD:
   * - Median of Two Sorted Arrays (4)
   * - Find in Mountain Array (1095)
   */
}
