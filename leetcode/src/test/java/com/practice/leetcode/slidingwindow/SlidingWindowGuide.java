package com.practice.leetcode.slidingwindow;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ SLIDING WINDOW TECHNIQUE GUIDE ║
 * ║ (Kỹ thuật Cửa Sổ Trượt) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ SLIDING WINDOW là gì? │
 * │ │
 * │ - Kỹ thuật dùng 2 con trỏ (left, right) tạo thành "cửa sổ" │
 * │ - Cửa sổ trượt từ trái sang phải qua mảng/chuỗi │
 * │ - Giúp giảm độ phức tạp từ O(n²) xuống O(n) │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * 2 LOẠI SLIDING WINDOW
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ LOẠI 1: FIXED SIZE (Kích thước cố định) │
 * │ │
 * │ Đặc điểm: │
 * │ - Window size = k (cho trước) │
 * │ - Cả left và right đều tăng đều │
 * │ │
 * │ Bài toán điển hình: │
 * │ - Maximum Average Subarray (tổng/trung bình lớn nhất của k phần tử) │
 * │ - Maximum Sum Subarray of Size K │
 * │ - Find All Anagrams in a String │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ LOẠI 2: VARIABLE SIZE (Kích thước thay đổi)                                 │
 * │                                                                             │
 * │ Đặc điểm:                                                                   │
 * │ - Window size thay đổi dựa trên điều kiện                                   │
 * │ - Right luôn mở rộng, left thu hẹp khi cần                                  │
 * │                                                                             │
 * │ Bài toán điển hình:                                                         │
 * │ - Minimum Size Subarray Sum (tổng >= target, tìm độ dài nhỏ nhất)           │
 * │ - Longest Substring Without Repeating Characters                            │
 * │ - Fruit Into Baskets (tối đa k loại phần tử khác nhau)                      │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * TEMPLATE CODE
 * ═══════════════════════════════════════════════════════════════════════════════
 */
public class SlidingWindowGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 1: FIXED SIZE WINDOW
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CÔNG THỨC FIXED SIZE:
   *
   * 1. Khởi tạo window đầu tiên (0 đến k-1)
   * 2. Lặp từ k đến n-1:
   * a. Thêm phần tử mới (right) vào window
   * b. Loại bỏ phần tử cũ (left) khỏi window
   * c. Cập nhật kết quả
   *
   * Visualize:
   * Array: [a, b, c, d, e, f, g], k = 3
   *
   * Step 0: [a, b, c] d e f g ← Window ban đầu
   * └───────┘
   *
   * Step 1: a [b, c, d] e f g ← Slide: bỏ a, thêm d
   * └───────┘
   *
   * Step 2: a b [c, d, e] f g ← Slide: bỏ b, thêm e
   * └───────┘
   * ...
   */
  public int fixedWindowTemplate(int[] nums, int k) {
    int n = nums.length;

    // Bước 1: Tính giá trị cho window đầu tiên
    int windowSum = 0;
    for (int i = 0; i < k; i++) {
      windowSum += nums[i];
    }

    int result = windowSum; // Hoặc logic khác tùy bài

    // Bước 2: Trượt window
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
  // TEMPLATE 2: VARIABLE SIZE WINDOW
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CÔNG THỨC VARIABLE SIZE:
   *
   * 1. Mở rộng window (right++) cho đến khi THỎA MÃN điều kiện
   * 2. Thu hẹp window (left++) trong khi vẫn THỎA MÃN
   * 3. Cập nhật kết quả
   *
   * Template chung:
   *
   * int left = 0;
   * for (int right = 0; right < n; right++) {
   * // Bước 1: Mở rộng - thêm nums[right] vào window
   * updateWindow(nums[right]);
   *
   * // Bước 2: Thu hẹp - loại bỏ từ left khi VI PHẠM điều kiện
   * while (windowViolatesCondition()) {
   * removeFromWindow(nums[left]);
   * left++;
   * }
   *
   * // Bước 3: Cập nhật kết quả với window hiện tại [left, right]
   * result = updateResult(right - left + 1);
   * }
   *
   * Visualize:
   * Tìm substring dài nhất không có ký tự lặp: "abcabcbb"
   *
   * right=0: [a]bcabcbb, window={a}, maxLen=1
   * right=1: [a,b]cabcbb, window={a,b}, maxLen=2
   * right=2: [a,b,c]abcbb, window={a,b,c}, maxLen=3
   * right=3: [a,b,c,a] ← 'a' lặp! → Thu hẹp
   * left++ → [b,c,a]bcbb, window={b,c,a}, maxLen=3
   * right=4: [b,c,a,b] ← 'b' lặp! → Thu hẹp
   * left++ → [c,a,b]cbb, window={c,a,b}, maxLen=3
   * ...
   */
  public int variableWindowTemplate_FindMax(int[] nums) {
    int n = nums.length;
    int left = 0;
    int maxLength = 0;

    // Cấu trúc dữ liệu hỗ trợ (tùy bài)
    java.util.Set<Integer> windowSet = new java.util.HashSet<>();

    for (int right = 0; right < n; right++) {
      // Bước 1: Mở rộng - thêm phần tử mới
      // Nếu vi phạm điều kiện → thu hẹp trước
      while (windowSet.contains(nums[right])) {
        windowSet.remove(nums[left]);
        left++;
      }
      windowSet.add(nums[right]);

      // Bước 2: Cập nhật kết quả (tìm MAX length)
      maxLength = Math.max(maxLength, right - left + 1);
    }

    return maxLength;
  }

  /**
   * BIẾN THỂ: Tìm MIN length thỏa điều kiện
   *
   * Khác biệt:
   * - Tìm MAX: Thu hẹp khi VI PHẠM, cập nhật sau khi thu hẹp
   * - Tìm MIN: Thu hẹp khi THỎA MÃN, cập nhật TRONG KHI thu hẹp
   */
  public int variableWindowTemplate_FindMin(int[] nums, int target) {
    int n = nums.length;
    int left = 0;
    int windowSum = 0;
    int minLength = Integer.MAX_VALUE;

    for (int right = 0; right < n; right++) {
      // Bước 1: Mở rộng
      windowSum += nums[right];

      // Bước 2: Thu hẹp TRONG KHI vẫn thỏa mãn
      while (windowSum >= target) {
        // Cập nhật MIN trước khi thu hẹp
        minLength = Math.min(minLength, right - left + 1);

        // Thu hẹp
        windowSum -= nums[left];
        left++;
      }
    }

    return minLength == Integer.MAX_VALUE ? 0 : minLength;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 3: K DISTINCT ELEMENTS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * BÀI TOÁN: Tìm window dài nhất/ngắn nhất với TỐI ĐA K loại phần tử khác nhau
   *
   * Ví dụ: Fruit Into Baskets (k=2 loại trái cây)
   *
   * Cấu trúc dữ liệu: HashMap<phần_tử, số_lần_xuất_hiện>
   *
   * Điều kiện vi phạm: map.size() > k
   * Thu hẹp: Giảm count, nếu count=0 thì remove khỏi map
   */
  public int kDistinctTemplate(int[] nums, int k) {
    int n = nums.length;
    int left = 0;
    int maxLength = 0;

    // Map đếm số lần xuất hiện của mỗi phần tử trong window
    java.util.Map<Integer, Integer> freq = new java.util.HashMap<>();

    for (int right = 0; right < n; right++) {
      // Mở rộng: thêm nums[right]
      freq.merge(nums[right], 1, Integer::sum);

      // Thu hẹp: nếu có hơn k loại
      while (freq.size() > k) {
        int leftVal = nums[left];
        freq.merge(leftVal, -1, Integer::sum);
        if (freq.get(leftVal) == 0) {
          freq.remove(leftVal);
        }
        left++;
      }

      // Cập nhật kết quả
      maxLength = Math.max(maxLength, right - left + 1);
    }

    return maxLength;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // CHECKLIST KHI GIẢI BÀI
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ BƯỚC 1: Nhận dạng bài Sliding Window │
   * │ │
   * │ Dấu hiệu nhận biết: │
   * │ ✓ Tìm subarray/substring LIÊN TỤC │
   * │ ✓ Có từ khóa: "consecutive", "contiguous", "subarray", "substring" │
   * │ ✓ Tìm min/max length thỏa điều kiện │
   * │ ✓ Đếm số subarray thỏa điều kiện │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ BƯỚC 2: Xác định loại window │
   * │ │
   * │ Nếu k cho trước và cố định → FIXED SIZE │
   * │ Nếu k là điều kiện (sum >= k, tối đa k loại) → VARIABLE SIZE │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ BƯỚC 3: Xác định điều kiện và cập nhật │
   * │ │
   * │ Hỏi: Khi nào window VI PHẠM điều kiện? │
   * │ → Đó là lúc cần THU HẸP │
   * │ │
   * │ Hỏi: Cần tìm MIN hay MAX? │
   * │ → MAX: cập nhật sau khi thu hẹp xong │
   * │ → MIN: cập nhật trong khi thu hẹp │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ BƯỚC 4: Chọn cấu trúc dữ liệu hỗ trợ │
   * │ │
   * │ Sum/Average → biến int │
   * │ Unique elements → HashSet │
   * │ Count of elements → HashMap<element, count> │
   * │ Character frequency → int[26] hoặc int[128] │
   * └─────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CÁC BÀI THỰC HÀNH
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * EASY:
   * - Maximum Average Subarray I (LeetCode 643) → Fixed size
   * - Contains Duplicate II (LeetCode 219) → Fixed size + Set
   *
   * MEDIUM:
   * - Minimum Size Subarray Sum (LeetCode 209) → Variable, find MIN
   * - Longest Substring Without Repeating (LeetCode 3) → Variable + Set
   * - Fruit Into Baskets (LeetCode 904) → Variable + K distinct
   * - Longest Repeating Character Replacement (LeetCode 424)
   * - Permutation in String (LeetCode 567) → Fixed + frequency
   *
   * HARD:
   * - Minimum Window Substring (LeetCode 76) → Variable + frequency
   * - Sliding Window Maximum (LeetCode 239) → Fixed + Deque
   * - Subarrays with K Different Integers (LeetCode 992)
   */
}
