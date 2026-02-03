package com.practice.leetcode.slidingwindow;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * FIXED SIZE SLIDING WINDOW TEMPLATE (Template cửa sổ trượt kích thước cố định)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Template này dành cho các bài toán có window size K CỐ ĐỊNH.
 * 
 * Đặc điểm:
 * - Window luôn có kích thước = k
 * - Thêm 1 phần tử bên phải → loại bỏ 1 phần tử bên trái
 * - Time: O(n), Space: O(1) hoặc O(k) tùy bài
 * 
 * Ví dụ bài toán:
 * - Maximum Sum Subarray of Size K
 * - Maximum Average Subarray
 * - Parking Dilemma (tìm k xe gần nhau nhất)
 * - Sliding Window Maximum
 */
public class FixedSizeWindowTemplate {

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 1: TWO POINTERS STYLE (i, j) - DÀNH CHO BẠN
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * TEMPLATE QUE THUỘC - TWO POINTERS (i, j)
   * 
   * Cơ chế:
   * - i (right): Duyệt qua từng phần tử
   * - j (left): Duy trì window size = k
   * - Window size = i - j + 1
   * 
   * Bước thực hiện:
   * 1. Thêm phần tử nums[i] vào window (cập nhật windowSum/windowState)
   * 2. Nếu window size > k → thu hẹp bên trái (j++)
   * 3. Khi window size == k → xử lý kết quả
   */
  public int fixedWindowTwoPointers(int[] nums, int k) {
    int result = Integer.MIN_VALUE;
    int windowSum = 0;
    int j = 0;

    for (int i = 0; i < nums.length; i++) {
      // Bước 1: Thêm phần tử hiện tại vào window
      windowSum += nums[i];

      // Bước 2: Duy trì window size = k
      while (i - j + 1 > k) {
        windowSum -= nums[j];
        j++;
      }

      // Bước 3: Khi window đủ k phần tử, cập nhật kết quả
      if (i - j + 1 == k) {
        result = Math.max(result, windowSum);
      }
    }

    return result;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 2: CLASSIC SLIDING WINDOW
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * TEMPLATE CỔ ĐIỂN - Khởi tạo window đầu rồi slide
   * 
   * Cơ chế:
   * 1. Tính giá trị cho window đầu tiên [0, k-1]
   * 2. Slide từ vị trí k trở đi:
   * - Thêm nums[right]
   * - Loại bỏ nums[left]
   * 
   * Visualize:
   * Array: [a, b, c, d, e, f, g], k = 3
   * 
   * Step 0: [a, b, c] d e f g ← Window ban đầu
   * Step 1: a [b, c, d] e f g ← Bỏ a, thêm d
   * Step 2: a b [c, d, e] f g ← Bỏ b, thêm e
   */
  public int fixedWindowClassic(int[] nums, int k) {
    int n = nums.length;

    // Bước 1: Tính giá trị cho window đầu tiên
    int windowSum = 0;
    for (int i = 0; i < k; i++) {
      windowSum += nums[i];
    }

    int result = windowSum;

    // Bước 2: Truợt window
    for (int right = k; right < n; right++) {
      int left = right - k;

      // Thêm phần tử mới vào window
      windowSum += nums[right];

      // Loại bỏ phần tử cũ khỏi window
      windowSum -= nums[left];

      // Cập nhật kết quả
      result = Math.max(result, windowSum);
    }

    return result;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // EXAMPLE: MAXIMUM AVERAGE SUBARRAY (LeetCode 643)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán: Tìm trung bình lớn nhất của subarray có k phần tử.
   * 
   * Input: nums = [1,12,-5,-6,50,3], k = 4
   * Output: 12.75 (từ [12,-5,-6,50])
   */
  public double findMaxAverage(int[] nums, int k) {
    int windowSum = 0;
    int j = 0;
    double maxAvg = Double.NEGATIVE_INFINITY;

    for (int i = 0; i < nums.length; i++) {
      windowSum += nums[i];

      // Duy trì window size = k
      while (i - j + 1 > k) {
        windowSum -= nums[j];
        j++;
      }

      // Khi window đủ k phần tử
      if (i - j + 1 == k) {
        double avg = (double) windowSum / k;
        maxAvg = Math.max(maxAvg, avg);
      }
    }

    return maxAvg;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // EXAMPLE: PARKING DILEMMA TEMPLATE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Áp dụng: Tìm k xe gần nhau nhất (chiều dài mái che nhỏ nhất).
   * 
   * Input: positions = [2, 6, 7, 12], k = 3
   * Output: 6 (từ [2, 6, 7] → length = 7 - 2 + 1 = 6)
   */
  public long minRoofLengthTemplate(long[] positions, int k) {
    // Bước 0: Sort để các xe gần nhau nằm liền kề
    java.util.Arrays.sort(positions);

    long minLength = Long.MAX_VALUE;
    int j = 0;

    for (int i = 0; i < positions.length; i++) {
      // Duy trì window size = k
      while (i - j + 1 > k) {
        j++;
      }

      // Khi window đủ k xe, tính chiều dài mái che
      if (i - j + 1 == k) {
        long length = positions[i] - positions[j] + 1;
        minLength = Math.min(minLength, length);
      }
    }

    return minLength;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // KEY POINTS (Điểm quan trọng)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 1. Window size = i - j + 1
   * - i: chỉ số phần tử phải
   * - j: chỉ số phần tử trái
   * 
   * 2. Duy trì window size:
   * while (i - j + 1 > k) {
   * // Loại bỏ phần tử bên trái
   * j++;
   * }
   * 
   * 3. Xử lý kết quả:
   * if (i - j + 1 == k) {
   * // Logic tính toán của bạn
   * }
   * 
   * 4. Biến thể:
   * - Tính SUM: windowSum += nums[i]; windowSum -= nums[j];
   * - Đếm frequency: map.put(nums[i], count+1); map.put(nums[j], count-1);
   * - Tính range: maxVal = Math.max(...); minVal = Math.min(...);
   */
}
