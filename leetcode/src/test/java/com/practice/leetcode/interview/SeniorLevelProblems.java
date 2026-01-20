package com.practice.leetcode.interview;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════════════════════════╗
 * ║                         SENIOR LEVEL INTERVIEW PROBLEMS                                          ║
 * ║                   15 bài MEDIUM-HARD hay hỏi nhất ở vị trí Senior                                ║
 * ╚══════════════════════════════════════════════════════════════════════════════════════════════════╝
 * 
 * 📊 ĐẶC ĐIỂM PHỎNG VẤN SENIOR:
 * • Không hỏi bài Easy, focus vào Medium-Hard
 * • Yêu cầu tối ưu hóa time/space complexity
 * • Hỏi follow-up questions (scale to millions of records, etc.)
 * • Đánh giá khả năng communicate và giải thích approach
 * • Có thể yêu cầu multiple approaches và so sánh trade-offs
 * 
 * 📊 CẤU TRÚC: 3 bài/topic × 5 topics = 15 bài
 */
public class SeniorLevelProblems {

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                    TOPIC 1: ARRAY & STRING (SENIOR LEVEL)
    // ════════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 42 - TRAPPING RAIN WATER ⭐⭐⭐ [HARD]                                                    │
     * │ https://leetcode.com/problems/trapping-rain-water/                                          │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Có nhiều approaches: DP, Two Pointers, Monotonic Stack                                    │
     * │ • Interviewer sẽ hỏi: "Có cách nào O(1) space không?"                                       │
     * │ • Test khả năng optimize từ O(N) space → O(1) space                                         │
     * │                                                                                             │
     * │ FOLLOW-UP QUESTIONS:                                                                        │
     * │ • "Nếu mảng lớn 10^9 elements, approach nào tốt nhất?"                                      │
     * │ • "Giải quyết bài toán 2D thì sao?" (LC 407)                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int trap(int[] height) {
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int water = 0;
        
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    water += leftMax - height[left];
                }
                left++;
            } else {
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

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 76 - MINIMUM WINDOW SUBSTRING ⭐⭐⭐ [HARD]                                               │
     * │ https://leetcode.com/problems/minimum-window-substring/                                     │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Sliding Window template quan trọng nhất                                                   │
     * │ • Đòi hỏi xử lý edge cases cẩn thận                                                         │
     * │ • Có thể extend sang nhiều bài toán thực tế                                                 │
     * │                                                                                             │
     * │ FOLLOW-UP QUESTIONS:                                                                        │
     * │ • "Nếu t có duplicate characters thì sao?"                                                  │
     * │ • "Optimize cho streaming data (không biết trước độ dài s)"                                 │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public String minWindow(String s, String t) {
        if (s.length() < t.length()) return "";
        
        Map<Character, Integer> need = new HashMap<>();
        for (char c : t.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }
        
        Map<Character, Integer> window = new HashMap<>();
        int left = 0, right = 0;
        int valid = 0;
        int minLen = Integer.MAX_VALUE, start = 0;
        
        while (right < s.length()) {
            char c = s.charAt(right++);
            
            if (need.containsKey(c)) {
                window.put(c, window.getOrDefault(c, 0) + 1);
                if (window.get(c).equals(need.get(c))) {
                    valid++;
                }
            }
            
            while (valid == need.size()) {
                if (right - left < minLen) {
                    minLen = right - left;
                    start = left;
                }
                
                char d = s.charAt(left++);
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

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 41 - FIRST MISSING POSITIVE ⭐⭐⭐ [HARD]                                                 │
     * │ https://leetcode.com/problems/first-missing-positive/                                       │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Yêu cầu O(N) time + O(1) space - rất khó                                                  │
     * │ • Test tư duy "in-place modification"                                                       │
     * │ • Trick: dùng array index làm hash                                                          │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Đặt mỗi số i vào vị trí index i-1 (nếu hợp lệ)                                            │
     * │ • Sau đó duyệt tìm vị trí đầu tiên nums[i] != i+1                                           │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        
        // Đặt mỗi số i vào vị trí i-1
        for (int i = 0; i < n; i++) {
            while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                // Swap nums[i] với nums[nums[i] - 1]
                int temp = nums[nums[i] - 1];
                nums[nums[i] - 1] = nums[i];
                nums[i] = temp;
            }
        }
        
        // Tìm số dương nhỏ nhất bị thiếu
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        
        return n + 1;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                    TOPIC 2: HASHMAP & DESIGN (SENIOR LEVEL)
    // ════════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 146 - LRU CACHE ⭐⭐⭐ [MEDIUM nhưng rất hay hỏi Senior]                                  │
     * │ https://leetcode.com/problems/lru-cache/                                                    │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Design question - test system design thinking                                             │
     * │ • Kết hợp HashMap + Doubly Linked List                                                      │
     * │ • Real-world application (Redis, Memcached)                                                 │
     * │                                                                                             │
     * │ FOLLOW-UP QUESTIONS:                                                                        │
     * │ • "Làm sao thread-safe?" → ConcurrentHashMap + synchronized                                 │
     * │ • "LFU Cache khác gì?" → LC 460                                                              │
     * │ • "Distributed cache system?" → Consistent hashing                                          │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    class LRUCache {
        class Node {
            int key, value;
            Node prev, next;
            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
        
        private Map<Integer, Node> cache;
        private int capacity;
        private Node head, tail;
        
        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            head = new Node(0, 0);
            tail = new Node(0, 0);
            head.next = tail;
            tail.prev = head;
        }
        
        public int get(int key) {
            if (!cache.containsKey(key)) return -1;
            Node node = cache.get(key);
            moveToHead(node);
            return node.value;
        }
        
        public void put(int key, int value) {
            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                node.value = value;
                moveToHead(node);
            } else {
                Node newNode = new Node(key, value);
                cache.put(key, newNode);
                addToHead(newNode);
                
                if (cache.size() > capacity) {
                    Node lru = tail.prev;
                    removeNode(lru);
                    cache.remove(lru.key);
                }
            }
        }
        
        private void moveToHead(Node node) {
            removeNode(node);
            addToHead(node);
        }
        
        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        
        private void addToHead(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }
    }

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 380 - INSERT DELETE GETRANDOM O(1) ⭐⭐⭐ [MEDIUM]                                        │
     * │ https://leetcode.com/problems/insert-delete-getrandom-o1/                                   │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Design data structure question                                                            │
     * │ • Kết hợp HashMap + ArrayList                                                               │
     * │ • Trick: swap với phần tử cuối khi delete                                                   │
     * │                                                                                             │
     * │ FOLLOW-UP:                                                                                  │
     * │ • "Cho phép duplicates?" → LC 381                                                           │
     * │ • "Weighted random?" → dùng prefix sum                                                      │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    class RandomizedSet {
        private Map<Integer, Integer> valToIndex;
        private List<Integer> values;
        private Random rand;
        
        public RandomizedSet() {
            valToIndex = new HashMap<>();
            values = new ArrayList<>();
            rand = new Random();
        }
        
        public boolean insert(int val) {
            if (valToIndex.containsKey(val)) return false;
            valToIndex.put(val, values.size());
            values.add(val);
            return true;
        }
        
        public boolean remove(int val) {
            if (!valToIndex.containsKey(val)) return false;
            
            int index = valToIndex.get(val);
            int lastVal = values.get(values.size() - 1);
            
            // Swap với phần tử cuối
            values.set(index, lastVal);
            valToIndex.put(lastVal, index);
            
            // Xóa phần tử cuối
            values.remove(values.size() - 1);
            valToIndex.remove(val);
            
            return true;
        }
        
        public int getRandom() {
            return values.get(rand.nextInt(values.size()));
        }
    }

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 295 - FIND MEDIAN FROM DATA STREAM ⭐⭐⭐ [HARD]                                          │
     * │ https://leetcode.com/problems/find-median-from-data-stream/                                 │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Real-time data processing - rất thực tế                                                   │
     * │ • Sử dụng 2 heaps (max-heap + min-heap)                                                     │
     * │ • Balancing giữa 2 heaps                                                                    │
     * │                                                                                             │
     * │ FOLLOW-UP:                                                                                  │
     * │ • "Memory-efficient cho 10^9 numbers?" → Sampling hoặc approximate                          │
     * │ • "Sliding window median?" → LC 480                                                         │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    class MedianFinder {
        private PriorityQueue<Integer> maxHeap; // Nửa nhỏ (top = max của nửa nhỏ)
        private PriorityQueue<Integer> minHeap; // Nửa lớn (top = min của nửa lớn)
        
        public MedianFinder() {
            maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            minHeap = new PriorityQueue<>();
        }
        
        public void addNum(int num) {
            // Thêm vào maxHeap trước
            maxHeap.offer(num);
            
            // Balance: chuyển max của maxHeap sang minHeap
            minHeap.offer(maxHeap.poll());
            
            // Đảm bảo maxHeap.size >= minHeap.size
            if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }
        
        public double findMedian() {
            if (maxHeap.size() > minHeap.size()) {
                return maxHeap.peek();
            }
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                    TOPIC 3: STACK & MONOTONIC STACK (SENIOR LEVEL)
    // ════════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 84 - LARGEST RECTANGLE IN HISTOGRAM ⭐⭐⭐ [HARD]                                         │
     * │ https://leetcode.com/problems/largest-rectangle-in-histogram/                               │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Monotonic Stack pattern quan trọng                                                        │
     * │ • Tìm previous/next smaller element                                                         │
     * │ • Có thể extend sang 2D (LC 85 - Maximal Rectangle)                                         │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Với mỗi bar, tìm left boundary và right boundary                                          │
     * │ • Area = height * (right - left - 1)                                                        │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        
        for (int i = 0; i <= n; i++) {
            int h = (i == n) ? 0 : heights[i];
            
            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            
            stack.push(i);
        }
        
        return maxArea;
    }

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 239 - SLIDING WINDOW MAXIMUM ⭐⭐⭐ [HARD]                                                │
     * │ https://leetcode.com/problems/sliding-window-maximum/                                       │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Monotonic Deque pattern                                                                   │
     * │ • O(N) solution yêu cầu tư duy tốt                                                          │
     * │ • Real-time data processing application                                                     │
     * │                                                                                             │
     * │ FOLLOW-UP:                                                                                  │
     * │ • "Sliding window minimum?" → đổi sang monotonic increasing                                 │
     * │ • "Sliding window median?" → LC 480, dùng 2 heaps + lazy deletion                           │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length == 0 || k == 0) return new int[0];
        
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();
        
        for (int i = 0; i < n; i++) {
            // Remove elements outside window
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            
            // Remove smaller elements (useless)
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            
            deque.offerLast(i);
            
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        
        return result;
    }

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 32 - LONGEST VALID PARENTHESES ⭐⭐⭐ [HARD]                                              │
     * │ https://leetcode.com/problems/longest-valid-parentheses/                                    │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Nhiều approaches: Stack, DP, Two-pass                                                     │
     * │ • Test khả năng optimize                                                                    │
     * │ • Edge cases phức tạp                                                                       │
     * │                                                                                             │
     * │ TƯ DUY (Stack):                                                                             │
     * │ • Stack lưu index của unmatched parentheses                                                 │
     * │ • Push -1 làm base để tính length                                                           │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int longestValidParentheses(String s) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1); // Base index
        int maxLen = 0;
        
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i); // New base
                } else {
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }
        
        return maxLen;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                    TOPIC 4: BINARY SEARCH ADVANCED (SENIOR LEVEL)
    // ════════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 4 - MEDIAN OF TWO SORTED ARRAYS ⭐⭐⭐ [HARD]                                             │
     * │ https://leetcode.com/problems/median-of-two-sorted-arrays/                                  │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Top 3 bài Binary Search khó nhất                                                          │
     * │ • Yêu cầu O(log(min(m,n))) - không dễ                                                       │
     * │ • Test deep understanding of binary search                                                  │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Binary search trên mảng ngắn hơn                                                          │
     * │ • Partition 2 mảng sao cho left half = right half                                           │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
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
                if ((m + n) % 2 == 0) {
                    return (Math.max(maxLeft1, maxLeft2) + Math.min(minRight1, minRight2)) / 2.0;
                }
                return Math.max(maxLeft1, maxLeft2);
            } else if (maxLeft1 > minRight2) {
                right = partition1 - 1;
            } else {
                left = partition1 + 1;
            }
        }
        
        return 0;
    }

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 410 - SPLIT ARRAY LARGEST SUM ⭐⭐⭐ [HARD]                                               │
     * │ https://leetcode.com/problems/split-array-largest-sum/                                      │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Binary Search on Answer pattern                                                           │
     * │ • Minimize the maximum (minimax)                                                            │
     * │ • Thực tế: load balancing, task scheduling                                                  │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Binary search trên kết quả (min = max element, max = sum)                                 │
     * │ • Với mỗi "max sum", kiểm tra có thể chia thành k phần không                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int splitArray(int[] nums, int k) {
        int left = 0, right = 0;
        
        for (int num : nums) {
            left = Math.max(left, num);  // Minimum possible max = largest element
            right += num;                 // Maximum possible max = sum of all
        }
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (canSplit(nums, k, mid)) {
                right = mid; // Có thể chia, thử giảm max sum
            } else {
                left = mid + 1; // Không thể chia, tăng max sum
            }
        }
        
        return left;
    }
    
    private boolean canSplit(int[] nums, int k, int maxSum) {
        int count = 1;
        int currentSum = 0;
        
        for (int num : nums) {
            if (currentSum + num > maxSum) {
                count++;
                currentSum = num;
                if (count > k) return false;
            } else {
                currentSum += num;
            }
        }
        
        return true;
    }

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 162 - FIND PEAK ELEMENT ⭐⭐ + FOLLOW-UP [MEDIUM]                                        │
     * │ https://leetcode.com/problems/find-peak-element/                                            │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Binary Search on non-sorted array                                                         │
     * │ • Gradient-based search concept                                                             │
     * │ • Follow-up: 2D Peak Element (LC 1901)                                                      │
     * │                                                                                             │
     * │ FOLLOW-UP: Tìm peak trong 2D matrix?                                                        │
     * │ → O(N log M) hoặc O(M log N) solution                                                       │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int findPeakElement(int[] nums) {
        int left = 0, right = nums.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] < nums[mid + 1]) {
                // Đang đi lên, peak ở phía phải
                left = mid + 1;
            } else {
                // Đang đi xuống hoặc là peak, peak ở phía trái (bao gồm mid)
                right = mid;
            }
        }
        
        return left;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                    TOPIC 5: DYNAMIC PROGRAMMING (SENIOR LEVEL)
    // ════════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 72 - EDIT DISTANCE ⭐⭐⭐ [MEDIUM nhưng classic]                                          │
     * │ https://leetcode.com/problems/edit-distance/                                                │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Classic 2D DP - phải nắm vững                                                             │
     * │ • Real-world: spell checker, DNA sequence alignment                                         │
     * │ • Có thể optimize space từ O(mn) → O(n)                                                     │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ dp[i][j] = min operations để biến word1[0..i-1] thành word2[0..j-1]                         │
     * │ • Insert: dp[i][j-1] + 1                                                                    │
     * │ • Delete: dp[i-1][j] + 1                                                                    │
     * │ • Replace: dp[i-1][j-1] + (word1[i-1] != word2[j-1] ? 1 : 0)                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // Base cases
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],  // Replace
                                   Math.min(dp[i - 1][j],       // Delete
                                           dp[i][j - 1]));      // Insert
                }
            }
        }
        
        return dp[m][n];
    }

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 312 - BURST BALLOONS ⭐⭐⭐ [HARD]                                                        │
     * │ https://leetcode.com/problems/burst-balloons/                                               │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Interval DP pattern                                                                       │
     * │ • Reverse thinking: "Which balloon to burst LAST?"                                          │
     * │ • O(N³) solution                                                                            │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Thay vì "nổ đầu tiên", nghĩ "nổ cuối cùng trong range [i,j]"                              │
     * │ • dp[i][j] = max coins khi nổ hết balloons từ i đến j                                       │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int maxCoins(int[] nums) {
        int n = nums.length;
        
        // Thêm 1 ở 2 đầu
        int[] balloons = new int[n + 2];
        balloons[0] = balloons[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            balloons[i + 1] = nums[i];
        }
        
        int[][] dp = new int[n + 2][n + 2];
        
        // Duyệt theo độ dài interval
        for (int len = 1; len <= n; len++) {
            for (int left = 1; left <= n - len + 1; left++) {
                int right = left + len - 1;
                
                // Thử nổ mỗi balloon làm balloon CUỐI CÙNG trong range
                for (int k = left; k <= right; k++) {
                    int coins = balloons[left - 1] * balloons[k] * balloons[right + 1];
                    coins += dp[left][k - 1] + dp[k + 1][right];
                    dp[left][right] = Math.max(dp[left][right], coins);
                }
            }
        }
        
        return dp[1][n];
    }

    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ LC 1143 - LONGEST COMMON SUBSEQUENCE ⭐⭐⭐ [MEDIUM nhưng fundamental]                       │
     * │ https://leetcode.com/problems/longest-common-subsequence/                                   │
     * │                                                                                             │
     * │ TẠI SAO HAY HỎI SENIOR?                                                                     │
     * │ • Foundation cho nhiều bài DP khác                                                          │
     * │ • Follow-up: in ra actual LCS                                                               │
     * │ • Follow-up: 3 strings LCS                                                                  │
     * │                                                                                             │
     * │ FOLLOW-UP QUAN TRỌNG:                                                                       │
     * │ • "Print the actual LCS?" → Backtrack từ dp table                                           │
     * │ • "Longest Common Substring?" → reset khi không match                                        │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }
    
    // FOLLOW-UP: In ra actual LCS string
    public String printLCS(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // Build dp table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // Backtrack to find LCS
        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                lcs.append(text1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        
        return lcs.reverse().toString();
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                         TESTS
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    
    @Test
    void testTrap() {
        assertEquals(6, trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
    }
    
    @Test
    void testMinWindow() {
        assertEquals("BANC", minWindow("ADOBECODEBANC", "ABC"));
    }
    
    @Test
    void testFirstMissingPositive() {
        assertEquals(3, firstMissingPositive(new int[]{1, 2, 0}));
        assertEquals(2, firstMissingPositive(new int[]{3, 4, -1, 1}));
    }
    
    @Test
    void testLRUCache() {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1));
        cache.put(3, 3);
        assertEquals(-1, cache.get(2));
    }
    
    @Test
    void testLargestRectangleArea() {
        assertEquals(10, largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}));
    }
    
    @Test
    void testMaxSlidingWindow() {
        assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7}, 
                         maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
    }
    
    @Test
    void testLongestValidParentheses() {
        assertEquals(2, longestValidParentheses("(()"));
        assertEquals(4, longestValidParentheses(")()())"));
    }
    
    @Test
    void testFindMedianSortedArrays() {
        assertEquals(2.0, findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));
    }
    
    @Test
    void testSplitArray() {
        assertEquals(18, splitArray(new int[]{7, 2, 5, 10, 8}, 2));
    }
    
    @Test
    void testMinDistance() {
        assertEquals(3, minDistance("horse", "ros"));
    }
    
    @Test
    void testMaxCoins() {
        assertEquals(167, maxCoins(new int[]{3, 1, 5, 8}));
    }
    
    @Test
    void testLongestCommonSubsequence() {
        assertEquals(3, longestCommonSubsequence("abcde", "ace"));
        assertEquals("ace", printLCS("abcde", "ace"));
    }
}
