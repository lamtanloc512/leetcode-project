package com.practice.leetcode.interview;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════════════════════════╗
 * ║                              BINARY SEARCH PROBLEMS                                              ║
 * ║                    O(log N) - Giảm nửa không gian tìm kiếm mỗi bước                              ║
 * ╚══════════════════════════════════════════════════════════════════════════════════════════════════╝
 * 
 * 📊 KHI NÀO DÙNG BINARY SEARCH?
 * • Mảng đã SORTED (hoặc có tính chất monotonic)
 * • Cần tìm kiếm nhanh hơn O(N)
 * • Tìm boundary (first/last position)
 * • Optimization problems (minimize/maximize)
 * 
 * 📊 TEMPLATE CƠ BẢN:
 * while (left <= right) {
 *     int mid = left + (right - left) / 2;
 *     if (target found) return mid;
 *     else if (target < mid) right = mid - 1;
 *     else left = mid + 1;
 * }
 */
public class P04_BinarySearch {

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 704 - BINARY SEARCH ⭐
    //                       https://leetcode.com/problems/binary-search/
    //                              (Template cơ bản)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm target trong mảng sorted. Return index hoặc -1.                                         │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [-1, 0, 3, 5, 9, 12], target = 9 → 4                                                 │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(log N) thời gian, O(1) không gian                                            │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int search(int[] nums, int target) {
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

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                    LC 33 - SEARCH IN ROTATED SORTED ARRAY ⭐⭐⭐
    //            https://leetcode.com/problems/search-in-rotated-sorted-array/
    //                              (Biến thể quan trọng nhất)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Mảng sorted bị rotate. Tìm target.                                                          │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [4, 5, 6, 7, 0, 1, 2], target = 0 → 4                                                │
     * │ nums = [4, 5, 6, 7, 0, 1, 2], target = 3 → -1                                               │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ Một nửa luôn sorted. Xác định target nằm ở nửa nào.                                         │
     * │                                                                                             │
     * │ • Nếu left half sorted (nums[left] <= nums[mid]):                                           │
     * │   - Target trong [left, mid) → search left                                                  │
     * │   - Ngược lại → search right                                                                │
     * │ • Nếu right half sorted:                                                                    │
     * │   - Target trong (mid, right] → search right                                                │
     * │   - Ngược lại → search left                                                                 │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(log N) thời gian, O(1) không gian                                            │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int searchRotated(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) return mid;
            
            // Xác định nửa nào sorted
            if (nums[left] <= nums[mid]) {
                // Left half sorted
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // Right half sorted
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                    LC 34 - FIND FIRST AND LAST POSITION ⭐⭐
    //     https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
    //                              (Lower Bound + Upper Bound)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm vị trí đầu và cuối của target trong mảng sorted.                                        │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [5, 7, 7, 8, 8, 10], target = 8 → [3, 4]                                             │
     * │ nums = [5, 7, 7, 8, 8, 10], target = 6 → [-1, -1]                                           │
     * │                                                                                             │
     * │ TƯ DUY: 2 lần binary search                                                                 │
     * │ • Tìm vị trí ĐẦU TIÊN: khi tìm thấy, tiếp tục search bên trái                               │
     * │ • Tìm vị trí CUỐI CÙNG: khi tìm thấy, tiếp tục search bên phải                              │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(log N) thời gian, O(1) không gian                                            │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int[] searchRange(int[] nums, int target) {
        int first = findBound(nums, target, true);
        int last = findBound(nums, target, false);
        return new int[]{first, last};
    }
    
    private int findBound(int[] nums, int target, boolean findFirst) {
        int left = 0, right = nums.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                result = mid;
                if (findFirst) {
                    right = mid - 1; // Tiếp tục tìm bên trái
                } else {
                    left = mid + 1;  // Tiếp tục tìm bên phải
                }
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 153 - FIND MINIMUM IN ROTATED SORTED ARRAY ⭐⭐
    //              https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm phần tử nhỏ nhất trong mảng sorted bị rotate.                                           │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ [3, 4, 5, 1, 2] → 1                                                                         │
     * │ [4, 5, 6, 7, 0, 1, 2] → 0                                                                   │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • nums[mid] > nums[right]: min ở nửa phải                                                   │
     * │ • nums[mid] <= nums[right]: min ở nửa trái (bao gồm mid)                                    │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(log N) thời gian, O(1) không gian                                            │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int findMin(int[] nums) {
        int left = 0, right = nums.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] > nums[right]) {
                // Điểm rotate nằm bên phải
                left = mid + 1;
            } else {
                // Min có thể là mid hoặc bên trái
                right = mid;
            }
        }
        
        return nums[left];
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 4 - MEDIAN OF TWO SORTED ARRAYS ⭐⭐⭐
    //              https://leetcode.com/problems/median-of-two-sorted-arrays/
    //                              (Binary Search - HARD nhưng hay hỏi)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm median của 2 mảng sorted. Yêu cầu O(log(m+n)).                                          │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums1 = [1, 3], nums2 = [2] → 2.0                                                           │
     * │ nums1 = [1, 2], nums2 = [3, 4] → 2.5                                                        │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Partition 2 mảng sao cho: left half = right half                                          │
     * │ • Binary search trên mảng ngắn hơn                                                          │
     * │ • Điều kiện: maxLeft1 <= minRight2 && maxLeft2 <= minRight1                                 │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(log(min(m, n))) thời gian, O(1) không gian                                   │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // Đảm bảo nums1 là mảng ngắn hơn
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        
        int m = nums1.length, n = nums2.length;
        int left = 0, right = m;
        
        while (left <= right) {
            int partition1 = (left + right) / 2;
            int partition2 = (m + n + 1) / 2 - partition1;
            
            int maxLeft1 = partition1 == 0 ? Integer.MIN_VALUE : nums1[partition1 - 1];
            int minRight1 = partition1 == m ? Integer.MAX_VALUE : nums1[partition1];
            
            int maxLeft2 = partition2 == 0 ? Integer.MIN_VALUE : nums2[partition2 - 1];
            int minRight2 = partition2 == n ? Integer.MAX_VALUE : nums2[partition2];
            
            if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
                // Found correct partition
                if ((m + n) % 2 == 0) {
                    return (Math.max(maxLeft1, maxLeft2) + Math.min(minRight1, minRight2)) / 2.0;
                } else {
                    return Math.max(maxLeft1, maxLeft2);
                }
            } else if (maxLeft1 > minRight2) {
                right = partition1 - 1;
            } else {
                left = partition1 + 1;
            }
        }
        
        return 0.0;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 74 - SEARCH A 2D MATRIX ⭐⭐
    //                  https://leetcode.com/problems/search-a-2d-matrix/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Ma trận m x n với mỗi hàng sorted và số đầu hàng sau > số cuối hàng trước.                  │
     * │ Tìm target.                                                                                 │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3 → true                         │
     * │                                                                                             │
     * │ TƯ DUY: Coi ma trận như mảng 1D                                                             │
     * │ • index = mid → row = mid / cols, col = mid % cols                                          │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(log(m*n)) thời gian, O(1) không gian                                         │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int left = 0, right = m * n - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int row = mid / n;
            int col = mid % n;
            int value = matrix[row][col];
            
            if (value == target) {
                return true;
            } else if (value < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return false;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                         TESTS
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    
    @Test
    void testSearch() {
        assertEquals(4, search(new int[]{-1, 0, 3, 5, 9, 12}, 9));
        assertEquals(-1, search(new int[]{-1, 0, 3, 5, 9, 12}, 2));
    }
    
    @Test
    void testSearchRotated() {
        assertEquals(4, searchRotated(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
        assertEquals(-1, searchRotated(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));
    }
    
    @Test
    void testSearchRange() {
        assertArrayEquals(new int[]{3, 4}, searchRange(new int[]{5, 7, 7, 8, 8, 10}, 8));
        assertArrayEquals(new int[]{-1, -1}, searchRange(new int[]{5, 7, 7, 8, 8, 10}, 6));
    }
    
    @Test
    void testFindMin() {
        assertEquals(1, findMin(new int[]{3, 4, 5, 1, 2}));
        assertEquals(0, findMin(new int[]{4, 5, 6, 7, 0, 1, 2}));
    }
    
    @Test
    void testFindMedianSortedArrays() {
        assertEquals(2.0, findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));
        assertEquals(2.5, findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}));
    }
    
    @Test
    void testSearchMatrix() {
        int[][] matrix = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        assertTrue(searchMatrix(matrix, 3));
        assertFalse(searchMatrix(matrix, 13));
    }
}
