package com.practice.leetcode.sorting;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════════════════════════╗
 * ║                            LEETCODE SORTING PROBLEMS                                             ║
 * ║                    Các bài tập sắp xếp trên LeetCode (2 Easy + 4 Medium)                         ║
 * ╚══════════════════════════════════════════════════════════════════════════════════════════════════╝
 */
public class LeetCodeSortingProblems {

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                    EASY #1: LC 88 - MERGE SORTED ARRAY
    //                                    https://leetcode.com/problems/merge-sorted-array/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Trộn 2 mảng đã sắp xếp nums1 và nums2 vào nums1.                                            │
     * │ nums1 có độ dài m + n, với n phần tử cuối là 0 (placeholder).                               │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums1 = [1, 2, 3, 0, 0, 0], m = 3                                                           │
     * │ nums2 = [2, 5, 6], n = 3                                                                    │
     * │ Output: [1, 2, 2, 3, 5, 6]                                                                  │
     * │                                                                                             │
     * │ TƯ DUY: Điền từ CUỐI về ĐẦU (tránh ghi đè)                                                  │
     * │ • So sánh phần tử lớn nhất của 2 mảng                                                       │
     * │ • Đặt vào vị trí cuối của nums1                                                             │
     * │ • Di chuyển con trỏ về phía trước                                                           │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(m + n) thời gian, O(1) không gian                                            │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public void mergeSortedArray(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;      // Con trỏ cuối phần có giá trị của nums1
        int j = n - 1;      // Con trỏ cuối nums2
        int k = m + n - 1;  // Con trỏ cuối nums1 (vị trí điền)
        
        // Điền từ cuối về đầu
        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                nums1[k--] = nums1[i--];
            } else {
                nums1[k--] = nums2[j--];
            }
        }
        
        // Nếu nums2 còn phần tử, copy vào nums1
        // (Không cần xử lý nums1 vì đã ở đúng vị trí)
        while (j >= 0) {
            nums1[k--] = nums2[j--];
        }
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                    EASY #2: LC 169 - MAJORITY ELEMENT
    //                                    https://leetcode.com/problems/majority-element/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm phần tử xuất hiện hơn n/2 lần trong mảng.                                               │
     * │ Đề bảo đảm luôn tồn tại majority element.                                                   │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [3, 2, 3] → Output: 3                                                                │
     * │ nums = [2, 2, 1, 1, 1, 2, 2] → Output: 2                                                    │
     * │                                                                                             │
     * │ CÁCH 1: SORT - Phần tử ở giữa chính là majority                                             │
     * │         Vì xuất hiện > n/2 lần → chắc chắn chiếm vị trí giữa                                │
     * │                                                                                             │
     * │ CÁCH 2: BOYER-MOORE VOTING (tối ưu hơn)                                                     │
     * │         Dùng biến đếm, giữ candidate, gặp khác thì trừ đếm                                  │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP:                                                                                │
     * │ • Sort: O(N log N) time, O(1) space                                                         │
     * │ • Boyer-Moore: O(N) time, O(1) space                                                        │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    // Cách 1: Sort
    public int majorityElementSort(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }
    
    // Cách 2: Boyer-Moore Voting (RECOMMENDED)
    public int majorityElementVoting(int[] nums) {
        int candidate = nums[0];
        int count = 1;
        
        for (int i = 1; i < nums.length; i++) {
            if (count == 0) {
                candidate = nums[i];
                count = 1;
            } else if (nums[i] == candidate) {
                count++;
            } else {
                count--;
            }
        }
        
        return candidate;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                    MEDIUM #1: LC 56 - MERGE INTERVALS
    //                                    https://leetcode.com/problems/merge-intervals/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Cho mảng intervals, trộn các interval giao nhau.                                            │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ intervals = [[1,3],[2,6],[8,10],[15,18]]                                                    │
     * │ Output: [[1,6],[8,10],[15,18]]                                                              │
     * │ Giải thích: [1,3] và [2,6] giao nhau → [1,6]                                                │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ 1. SORT theo start time                                                                     │
     * │ 2. Duyệt từng interval:                                                                     │
     * │    • Nếu giao với interval trước → mở rộng end                                              │
     * │    • Nếu không giao → thêm interval mới                                                     │
     * │                                                                                             │
     * │ GIAO NHAU: current.start <= prev.end                                                        │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N log N) thời gian, O(N) không gian                                          │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int[][] mergeIntervals(int[][] intervals) {
        if (intervals.length <= 1) return intervals;
        
        // Sort theo start time
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        
        List<int[]> result = new ArrayList<>();
        int[] current = intervals[0];
        result.add(current);
        
        for (int[] interval : intervals) {
            if (interval[0] <= current[1]) {
                // Giao nhau: mở rộng end
                current[1] = Math.max(current[1], interval[1]);
            } else {
                // Không giao: thêm interval mới
                current = interval;
                result.add(current);
            }
        }
        
        return result.toArray(new int[result.size()][]);
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                    MEDIUM #2: LC 75 - SORT COLORS
    //                                    https://leetcode.com/problems/sort-colors/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI: (Dutch National Flag Problem)                                                       │
     * │ Mảng chứa 0 (đỏ), 1 (trắng), 2 (xanh). Sắp xếp in-place.                                    │
     * │ KHÔNG ĐƯỢC dùng built-in sort.                                                              │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [2, 0, 2, 1, 1, 0]                                                                   │
     * │ Output: [0, 0, 1, 1, 2, 2]                                                                  │
     * │                                                                                             │
     * │ TƯ DUY: 3 CON TRỎ (Dutch Flag Algorithm)                                                    │
     * │ • low: vị trí đặt số 0 tiếp theo                                                            │
     * │ • mid: vị trí đang xét                                                                      │
     * │ • high: vị trí đặt số 2 tiếp theo                                                           │
     * │                                                                                             │
     * │ QUY TẮC:                                                                                    │
     * │ • nums[mid] == 0: swap với low, tăng cả low và mid                                          │
     * │ • nums[mid] == 1: chỉ tăng mid                                                              │
     * │ • nums[mid] == 2: swap với high, giảm high (không tăng mid!)                                │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian - ONE PASS                                     │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public void sortColors(int[] nums) {
        int low = 0;                // Vị trí đặt 0
        int mid = 0;                // Vị trí đang xét
        int high = nums.length - 1; // Vị trí đặt 2
        
        while (mid <= high) {
            if (nums[mid] == 0) {
                // Swap với low, tăng cả low và mid
                swap(nums, low, mid);
                low++;
                mid++;
            } else if (nums[mid] == 1) {
                // Giữ nguyên, chỉ tăng mid
                mid++;
            } else { // nums[mid] == 2
                // Swap với high, giảm high
                swap(nums, mid, high);
                high--;
                // KHÔNG tăng mid vì phần tử swap về có thể là 0 hoặc 2
            }
        }
    }
    
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                    MEDIUM #3: LC 179 - LARGEST NUMBER
    //                                    https://leetcode.com/problems/largest-number/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Sắp xếp mảng số nguyên sao cho khi ghép lại tạo thành số lớn nhất.                          │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [3, 30, 34, 5, 9]                                                                    │
     * │ Output: "9534330"                                                                           │
     * │ Giải thích: 9 > 5 > 34 > 3 > 30 (theo thứ tự nối)                                           │
     * │                                                                                             │
     * │ TƯ DUY: CUSTOM COMPARATOR                                                                   │
     * │ So sánh 2 số a, b: (a + b) vs (b + a) dạng string                                           │
     * │ Ví dụ: 3 vs 30 → "330" vs "303" → "330" > "303" → 3 đứng trước 30                           │
     * │                                                                                             │
     * │ ⚠️ EDGE CASE: Mảng toàn số 0 → return "0" (không phải "0000")                               │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N log N * K) với K là độ dài số                                              │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public String largestNumber(int[] nums) {
        // Chuyển sang String array
        String[] strNums = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strNums[i] = String.valueOf(nums[i]);
        }
        
        // Sort với custom comparator
        // So sánh: (b + a) vs (a + b) để sort giảm dần
        Arrays.sort(strNums, (a, b) -> (b + a).compareTo(a + b));
        
        // Edge case: mảng toàn số 0
        if (strNums[0].equals("0")) {
            return "0";
        }
        
        // Ghép kết quả
        StringBuilder sb = new StringBuilder();
        for (String s : strNums) {
            sb.append(s);
        }
        
        return sb.toString();
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                           MEDIUM #4: LC 347 - TOP K FREQUENT ELEMENTS
    //                           https://leetcode.com/problems/top-k-frequent-elements/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm K phần tử xuất hiện nhiều nhất trong mảng.                                              │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [1, 1, 1, 2, 2, 3], k = 2                                                            │
     * │ Output: [1, 2]                                                                              │
     * │                                                                                             │
     * │ CÁCH 1: HEAP (Min-Heap)                                                                     │
     * │ • Đếm tần suất bằng HashMap                                                                 │
     * │ • Dùng Min-Heap size K, giữ K phần tử có tần suất cao nhất                                  │
     * │ • O(N log K) time                                                                           │
     * │                                                                                             │
     * │ CÁCH 2: BUCKET SORT (tối ưu nhất)                                                           │
     * │ • Đếm tần suất bằng HashMap                                                                 │
     * │ • Tạo bucket theo tần suất (index = frequency)                                              │
     * │ • Duyệt ngược từ bucket lớn nhất lấy K phần tử                                              │
     * │ • O(N) time                                                                                 │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    // Cách 1: Min-Heap
    public int[] topKFrequentHeap(int[] nums, int k) {
        // Đếm tần suất
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }
        
        // Min-Heap theo frequency
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            minHeap.offer(new int[]{entry.getKey(), entry.getValue()});
            if (minHeap.size() > k) {
                minHeap.poll(); // Loại phần tử có frequency thấp nhất
            }
        }
        
        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = minHeap.poll()[0];
        }
        
        return result;
    }
    
    // Cách 2: Bucket Sort (RECOMMENDED)
    @SuppressWarnings("unchecked")
    public int[] topKFrequentBucket(int[] nums, int k) {
        // Đếm tần suất
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }
        
        // Bucket: index = frequency, value = list of numbers
        List<Integer>[] bucket = new List[nums.length + 1];
        for (int i = 0; i < bucket.length; i++) {
            bucket[i] = new ArrayList<>();
        }
        
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            int freq = entry.getValue();
            bucket[freq].add(entry.getKey());
        }
        
        // Duyệt ngược từ bucket lớn nhất
        int[] result = new int[k];
        int index = 0;
        for (int i = bucket.length - 1; i >= 0 && index < k; i--) {
            for (int num : bucket[i]) {
                result[index++] = num;
                if (index == k) break;
            }
        }
        
        return result;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                         TESTS
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    
    @Test
    void testMergeSortedArray() {
        int[] nums1 = {1, 2, 3, 0, 0, 0};
        mergeSortedArray(nums1, 3, new int[]{2, 5, 6}, 3);
        assertArrayEquals(new int[]{1, 2, 2, 3, 5, 6}, nums1);
    }
    
    @Test
    void testMajorityElement() {
        assertEquals(3, majorityElementSort(new int[]{3, 2, 3}));
        assertEquals(2, majorityElementVoting(new int[]{2, 2, 1, 1, 1, 2, 2}));
    }
    
    @Test
    void testMergeIntervals() {
        int[][] result = mergeIntervals(new int[][]{{1,3},{2,6},{8,10},{15,18}});
        assertArrayEquals(new int[][]{{1,6},{8,10},{15,18}}, result);
    }
    
    @Test
    void testSortColors() {
        int[] nums = {2, 0, 2, 1, 1, 0};
        sortColors(nums);
        assertArrayEquals(new int[]{0, 0, 1, 1, 2, 2}, nums);
    }
    
    @Test
    void testLargestNumber() {
        assertEquals("9534330", largestNumber(new int[]{3, 30, 34, 5, 9}));
        assertEquals("0", largestNumber(new int[]{0, 0}));
    }
    
    @Test
    void testTopKFrequent() {
        assertArrayEquals(new int[]{1, 2}, topKFrequentBucket(new int[]{1,1,1,2,2,3}, 2));
        assertArrayEquals(new int[]{1}, topKFrequentHeap(new int[]{1}, 1));
    }
}
