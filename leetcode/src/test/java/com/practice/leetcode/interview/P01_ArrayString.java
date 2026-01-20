package com.practice.leetcode.interview;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════════════════════════╗
 * ║                    ARRAY & STRING PROBLEMS - INTERVIEW ESSENTIALS                                ║
 * ║                Two Pointers + Sliding Window (Dựa trên thống kê phỏng vấn 2024-2025)             ║
 * ╚══════════════════════════════════════════════════════════════════════════════════════════════════╝
 * 
 * 📊 THỐNG KÊ TỪ GOOGLE INTERVIEWS 2024-2025:
 * • Two Sum variants: 23 lần
 * • Merge Intervals: 19 lần  
 * • Longest Substring Without Repeating: 15 lần
 * • 3Sum: rất phổ biến ở nhiều công ty
 */
public class P01_ArrayString {

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                    LC 1 - TWO SUM ⭐⭐⭐
    //                          https://leetcode.com/problems/two-sum/
    //                               (TOP 1 phổ biến mọi thời đại)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm 2 số trong mảng có tổng = target. Trả về index của 2 số đó.                             │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [2, 7, 11, 15], target = 9 → [0, 1] (vì 2 + 7 = 9)                                   │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Brute force: O(N²) - duyệt 2 vòng lặp                                                     │
     * │ • Tối ưu HashMap: O(N) - lưu complement cần tìm                                             │
     * │                                                                                             │
     * │ CÁCH LÀM:                                                                                   │
     * │ Với mỗi số nums[i], kiểm tra (target - nums[i]) có trong map chưa?                          │
     * │ • Có → trả về [map.get(complement), i]                                                      │
     * │ • Chưa → lưu nums[i] vào map                                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>(); // value -> index
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            
            map.put(nums[i], i);
        }
        
        return new int[]{-1, -1}; // Không tìm thấy
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                    LC 15 - 3SUM ⭐⭐⭐
    //                             https://leetcode.com/problems/3sum/
    //                              (TWO POINTERS + SORTING classic)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm tất cả bộ 3 số có tổng = 0, không trùng lặp.                                            │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [-1, 0, 1, 2, -1, -4]                                                                │
     * │ Output: [[-1, -1, 2], [-1, 0, 1]]                                                           │
     * │                                                                                             │
     * │ TƯ DUY: Sort + Two Pointers                                                                 │
     * │ 1. Sort mảng                                                                                │
     * │ 2. Fix số thứ nhất (i), dùng two pointers tìm 2 số còn lại                                  │
     * │ 3. Skip các số trùng để tránh duplicate                                                     │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N²) thời gian, O(1) không gian (không tính output)                           │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        
        for (int i = 0; i < nums.length - 2; i++) {
            // Skip số trùng
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            // Tối ưu: nếu số nhỏ nhất > 0, không thể tìm được tổng = 0
            if (nums[i] > 0) break;
            
            int left = i + 1;
            int right = nums.length - 1;
            int target = -nums[i]; // Cần tìm 2 số có tổng = -nums[i]
            
            while (left < right) {
                int sum = nums[left] + nums[right];
                
                if (sum == target) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    // Skip duplicates
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    
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

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 11 - CONTAINER WITH MOST WATER ⭐⭐
    //                    https://leetcode.com/problems/container-with-most-water/
    //                                     (TWO POINTERS)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Cho mảng height[i] là chiều cao cột tại vị trí i.                                           │
     * │ Tìm 2 cột tạo container chứa nhiều nước nhất.                                               │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ height = [1,8,6,2,5,4,8,3,7]                                                                │
     * │ Output: 49 (giữa cột 1 và cột 8, chiều cao = 7, chiều rộng = 7)                             │
     * │                                                                                             │
     * │      |                   |                                                                  │
     * │      |___|___|___|___|___|___|                                                              │
     * │      8   6   2   5   4   8   7                                                              │
     * │                                                                                             │
     * │ TƯ DUY: Two Pointers từ 2 đầu                                                               │
     * │ • Diện tích = min(height[left], height[right]) * (right - left)                             │
     * │ • Di chuyển con trỏ có chiều cao THẤP HƠN (vì cột cao hơn không giúp tăng diện tích)        │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxWater = 0;
        
        while (left < right) {
            int h = Math.min(height[left], height[right]);
            int w = right - left;
            maxWater = Math.max(maxWater, h * w);
            
            // Di chuyển cột thấp hơn
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxWater;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                         LC 3 - LONGEST SUBSTRING WITHOUT REPEATING ⭐⭐⭐
    //              https://leetcode.com/problems/longest-substring-without-repeating-characters/
    //                                    (SLIDING WINDOW classic)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm độ dài của substring dài nhất không có ký tự lặp lại.                                   │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ "abcabcbb" → 3 ("abc")                                                                      │
     * │ "bbbbb" → 1 ("b")                                                                           │
     * │ "pwwkew" → 3 ("wke")                                                                        │
     * │                                                                                             │
     * │ TƯ DUY: Sliding Window + HashMap                                                            │
     * │ • Duy trì window [left, right] không có ký tự trùng                                         │
     * │ • Map lưu vị trí cuối cùng của mỗi ký tự                                                    │
     * │ • Khi gặp ký tự trùng → di chuyển left về sau vị trí trùng                                  │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(min(N, M)) không gian (M = số ký tự distinct)                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastIndex = new HashMap<>();
        int maxLen = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            
            // Nếu ký tự đã xuất hiện trong window hiện tại
            if (lastIndex.containsKey(c) && lastIndex.get(c) >= left) {
                left = lastIndex.get(c) + 1; // Di chuyển left về sau vị trí trùng
            }
            
            lastIndex.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        
        return maxLen;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 42 - TRAPPING RAIN WATER ⭐⭐⭐
    //                     https://leetcode.com/problems/trapping-rain-water/
    //                              (Two Pointers hoặc DP - HARD)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Cho mảng height biểu diễn độ cao các cột. Tính lượng nước có thể chứa sau khi mưa.          │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ height = [0,1,0,2,1,0,1,3,2,1,2,1]                                                          │
     * │ Output: 6                                                                                   │
     * │                                                                                             │
     * │ MINH HỌA:                                                                                   │
     * │       |                                                                                     │
     * │   |   ||~|                                                                                  │
     * │ _||~|||||||~|                                                                               │
     * │                                                                                             │
     * │ TƯ DUY: Two Pointers                                                                        │
     * │ • leftMax: max height từ đầu đến left                                                       │
     * │ • rightMax: max height từ right đến cuối                                                    │
     * │ • Nước tại vị trí i = min(leftMax, rightMax) - height[i]                                    │
     * │ • Di chuyển con trỏ có max nhỏ hơn                                                          │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int trap(int[] height) {
        if (height == null || height.length == 0) return 0;
        
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int water = 0;
        
        while (left < right) {
            if (height[left] < height[right]) {
                // Nước phụ thuộc vào leftMax
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    water += leftMax - height[left];
                }
                left++;
            } else {
                // Nước phụ thuộc vào rightMax
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    water += rightMax - height[right];
                }
                right--;
            }
        }
        
        return water;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 53 - MAXIMUM SUBARRAY ⭐⭐
    //                       https://leetcode.com/problems/maximum-subarray/
    //                                   (Kadane's Algorithm)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm subarray có tổng lớn nhất.                                                              │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]                                                      │
     * │ Output: 6 (subarray [4, -1, 2, 1])                                                          │
     * │                                                                                             │
     * │ TƯ DUY: Kadane's Algorithm                                                                  │
     * │ • currentSum: tổng tốt nhất kết thúc tại vị trí hiện tại                                    │
     * │ • Nếu currentSum < 0 → bắt đầu lại từ phần tử hiện tại                                      │
     * │ • Nếu currentSum >= 0 → cộng thêm phần tử hiện tại                                          │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int maxSubArray(int[] nums) {
        int currentSum = nums[0];
        int maxSum = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            // Quyết định: bắt đầu mới hoặc tiếp tục
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 238 - PRODUCT OF ARRAY EXCEPT SELF ⭐⭐
    //                   https://leetcode.com/problems/product-of-array-except-self/
    //                                   (Prefix/Suffix Product)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Trả về mảng answer[i] = tích tất cả phần tử NGOẠI TRỪ nums[i].                              │
     * │ KHÔNG được dùng phép chia. Yêu cầu O(N) time.                                               │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [1, 2, 3, 4]                                                                         │
     * │ Output: [24, 12, 8, 6]                                                                      │
     * │ 24 = 2*3*4, 12 = 1*3*4, 8 = 1*2*4, 6 = 1*2*3                                                │
     * │                                                                                             │
     * │ TƯ DUY: Prefix * Suffix                                                                     │
     * │ answer[i] = (tích các số bên trái i) * (tích các số bên phải i)                             │
     * │                                                                                             │
     * │ Bước 1: Tính prefix product từ trái sang phải                                               │
     * │ Bước 2: Nhân với suffix product từ phải sang trái                                           │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian (không tính output)                            │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];
        
        // Bước 1: answer[i] = tích các số bên trái i
        answer[0] = 1;
        for (int i = 1; i < n; i++) {
            answer[i] = answer[i - 1] * nums[i - 1];
        }
        
        // Bước 2: Nhân với suffix (tích các số bên phải)
        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) {
            answer[i] *= suffix;
            suffix *= nums[i];
        }
        
        return answer;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                         LC 76 - MINIMUM WINDOW SUBSTRING ⭐⭐⭐
    //                  https://leetcode.com/problems/minimum-window-substring/
    //                              (SLIDING WINDOW nâng cao)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm substring ngắn nhất của s chứa TẤT CẢ ký tự trong t (kể cả trùng lặp).                  │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ s = "ADOBECODEBANC", t = "ABC"                                                              │
     * │ Output: "BANC"                                                                              │
     * │                                                                                             │
     * │ TƯ DUY: Sliding Window + HashMap                                                            │
     * │ 1. Mở rộng window (right++) cho đến khi chứa đủ tất cả ký tự của t                          │
     * │ 2. Thu hẹp window (left++) để tìm window nhỏ nhất vẫn hợp lệ                                │
     * │ 3. Lặp lại                                                                                  │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(S + T) thời gian, O(T) không gian                                            │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public String minWindow(String s, String t) {
        if (s.length() < t.length()) return "";
        
        // Đếm số lượng ký tự cần trong t
        Map<Character, Integer> need = new HashMap<>();
        for (char c : t.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }
        
        Map<Character, Integer> window = new HashMap<>();
        int left = 0, right = 0;
        int valid = 0; // Số ký tự đã thỏa mãn
        int minLen = Integer.MAX_VALUE;
        int start = 0;
        
        while (right < s.length()) {
            char c = s.charAt(right);
            right++;
            
            // Mở rộng window
            if (need.containsKey(c)) {
                window.put(c, window.getOrDefault(c, 0) + 1);
                if (window.get(c).equals(need.get(c))) {
                    valid++;
                }
            }
            
            // Thu hẹp window khi đã thỏa mãn
            while (valid == need.size()) {
                if (right - left < minLen) {
                    minLen = right - left;
                    start = left;
                }
                
                char d = s.charAt(left);
                left++;
                
                if (need.containsKey(d)) {
                    if (window.get(d).equals(need.get(d))) {
                        valid--;
                    }
                    window.put(d, window.get(d) - 1);
                }
            }
        }
        
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 5 - LONGEST PALINDROMIC SUBSTRING ⭐⭐
    //                   https://leetcode.com/problems/longest-palindromic-substring/
    //                                   (Expand from center)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm substring palindrome dài nhất trong s.                                                  │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ s = "babad" → "bab" hoặc "aba"                                                              │
     * │ s = "cbbd" → "bb"                                                                           │
     * │                                                                                             │
     * │ TƯ DUY: Expand Around Center                                                                │
     * │ • Với mỗi vị trí i, mở rộng ra 2 phía khi còn là palindrome                                 │
     * │ • Xét 2 trường hợp: độ dài lẻ (center = i) và độ dài chẵn (center = i, i+1)                 │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N²) thời gian, O(1) không gian                                               │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public String longestPalindrome(String s) {
        if (s == null || s.length() < 2) return s;
        
        int start = 0, maxLen = 1;
        
        for (int i = 0; i < s.length(); i++) {
            // Palindrome độ dài lẻ
            int len1 = expandFromCenter(s, i, i);
            // Palindrome độ dài chẵn
            int len2 = expandFromCenter(s, i, i + 1);
            
            int len = Math.max(len1, len2);
            if (len > maxLen) {
                maxLen = len;
                start = i - (len - 1) / 2;
            }
        }
        
        return s.substring(start, start + maxLen);
    }
    
    private int expandFromCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                         TESTS
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    
    @Test
    void testTwoSum() {
        assertArrayEquals(new int[]{0, 1}, twoSum(new int[]{2, 7, 11, 15}, 9));
        assertArrayEquals(new int[]{1, 2}, twoSum(new int[]{3, 2, 4}, 6));
    }
    
    @Test
    void testThreeSum() {
        List<List<Integer>> result = threeSum(new int[]{-1, 0, 1, 2, -1, -4});
        assertEquals(2, result.size());
    }
    
    @Test
    void testMaxArea() {
        assertEquals(49, maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
    }
    
    @Test
    void testLengthOfLongestSubstring() {
        assertEquals(3, lengthOfLongestSubstring("abcabcbb"));
        assertEquals(1, lengthOfLongestSubstring("bbbbb"));
        assertEquals(3, lengthOfLongestSubstring("pwwkew"));
    }
    
    @Test
    void testTrap() {
        assertEquals(6, trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
    }
    
    @Test
    void testMaxSubArray() {
        assertEquals(6, maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
    }
    
    @Test
    void testProductExceptSelf() {
        assertArrayEquals(new int[]{24, 12, 8, 6}, productExceptSelf(new int[]{1, 2, 3, 4}));
    }
    
    @Test
    void testMinWindow() {
        assertEquals("BANC", minWindow("ADOBECODEBANC", "ABC"));
    }
    
    @Test
    void testLongestPalindrome() {
        String result = longestPalindrome("babad");
        assertTrue(result.equals("bab") || result.equals("aba"));
    }
}
