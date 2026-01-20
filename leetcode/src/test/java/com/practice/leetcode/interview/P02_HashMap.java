package com.practice.leetcode.interview;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════════════════════════╗
 * ║                           HASHMAP / DICTIONARY PROBLEMS                                          ║
 * ║                   Tối ưu từ O(N²) → O(N) bằng HashMap                                            ║
 * ╚══════════════════════════════════════════════════════════════════════════════════════════════════╝
 * 
 * 📊 KHI NÀO DÙNG HASHMAP?
 * • Cần tìm kiếm nhanh O(1) thay vì O(N)
 * • Đếm tần suất xuất hiện
 * • Lưu trữ key-value pairs
 * • Phát hiện duplicate
 * • Tối ưu bài toán về tổng (sum problems)
 */
public class P02_HashMap {

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 49 - GROUP ANAGRAMS ⭐⭐⭐
    //                        https://leetcode.com/problems/group-anagrams/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Gom các từ anagram lại với nhau.                                                            │
     * │ Anagram: các từ có cùng ký tự nhưng xếp khác thứ tự                                         │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ ["eat","tea","tan","ate","nat","bat"]                                                       │
     * │ Output: [["bat"],["nat","tan"],["ate","eat","tea"]]                                         │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Key = sorted string (hoặc character count)                                                │
     * │ • Map<String, List<String>> để gom nhóm                                                     │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N * K log K) với K = độ dài string lớn nhất                                  │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        
        for (String s : strs) {
            // Tạo key = sorted characters
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = String.valueOf(chars);
            
            // Thêm vào group tương ứng
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        
        return new ArrayList<>(map.values());
    }
    
    // Cách 2: Dùng character count làm key (nhanh hơn khi string dài)
    public List<List<String>> groupAnagramsOptimized(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        
        for (String s : strs) {
            int[] count = new int[26];
            for (char c : s.toCharArray()) {
                count[c - 'a']++;
            }
            
            // Key = "a2b1c0..." format
            StringBuilder key = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                key.append((char) ('a' + i)).append(count[i]);
            }
            
            map.computeIfAbsent(key.toString(), k -> new ArrayList<>()).add(s);
        }
        
        return new ArrayList<>(map.values());
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 560 - SUBARRAY SUM EQUALS K ⭐⭐⭐
    //                    https://leetcode.com/problems/subarray-sum-equals-k/
    //                               (Prefix Sum + HashMap)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Đếm số subarray có tổng = k.                                                                │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [1, 1, 1], k = 2 → Output: 2                                                         │
     * │ Các subarray: [1,1] (index 0-1), [1,1] (index 1-2)                                          │
     * │                                                                                             │
     * │ TƯ DUY: Prefix Sum + HashMap                                                                │
     * │ • sum[i] = tổng từ index 0 đến i                                                            │
     * │ • Nếu sum[j] - sum[i] = k → subarray từ i+1 đến j có tổng = k                               │
     * │ • Map lưu: prefix_sum → số lần xuất hiện                                                    │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(N) không gian                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1); // Quan trọng: sum = 0 xuất hiện 1 lần (empty prefix)
        
        int sum = 0;
        int count = 0;
        
        for (int num : nums) {
            sum += num;
            
            // Nếu có prefix sum = (sum - k), tức là có subarray tổng = k
            if (prefixCount.containsKey(sum - k)) {
                count += prefixCount.get(sum - k);
            }
            
            // Cập nhật map
            prefixCount.put(sum, prefixCount.getOrDefault(sum, 0) + 1);
        }
        
        return count;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 128 - LONGEST CONSECUTIVE SEQUENCE ⭐⭐⭐
    //                https://leetcode.com/problems/longest-consecutive-sequence/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm độ dài dãy số liên tiếp dài nhất. Yêu cầu O(N) time.                                    │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [100, 4, 200, 1, 3, 2]                                                               │
     * │ Output: 4 (dãy 1, 2, 3, 4)                                                                  │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Đưa tất cả vào HashSet                                                                    │
     * │ • Với mỗi số, nếu nó là START của dãy (num-1 không tồn tại)                                 │
     * │   → Đếm độ dài dãy từ đó                                                                    │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(N) không gian                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        
        int maxLen = 0;
        
        for (int num : set) {
            // Chỉ bắt đầu đếm nếu num là START của dãy
            if (!set.contains(num - 1)) {
                int currentNum = num;
                int length = 1;
                
                while (set.contains(currentNum + 1)) {
                    currentNum++;
                    length++;
                }
                
                maxLen = Math.max(maxLen, length);
            }
        }
        
        return maxLen;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 146 - LRU CACHE ⭐⭐⭐
    //                          https://leetcode.com/problems/lru-cache/
    //                       (LinkedHashMap hoặc HashMap + DoublyLinkedList)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Thiết kế LRU (Least Recently Used) Cache với capacity giới hạn.                             │
     * │ • get(key): trả về value, -1 nếu không tồn tại                                              │
     * │ • put(key, value): thêm/cập nhật. Nếu đầy → xóa phần tử ít dùng nhất.                       │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ LRUCache cache = new LRUCache(2);                                                           │
     * │ cache.put(1, 1); cache.put(2, 2);                                                           │
     * │ cache.get(1);       // returns 1                                                            │
     * │ cache.put(3, 3);    // evicts key 2                                                         │
     * │ cache.get(2);       // returns -1                                                           │
     * │                                                                                             │
     * │ TƯ DUY: HashMap + Doubly Linked List                                                        │
     * │ • HashMap: O(1) lookup                                                                      │
     * │ • Doubly Linked List: O(1) remove/add                                                       │
     * │ • Head = most recently used, Tail = least recently used                                     │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    class LRUCache {
        // Node của Doubly Linked List
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
        private Node head, tail; // Dummy nodes
        
        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            
            // Tạo dummy head và tail
            head = new Node(0, 0);
            tail = new Node(0, 0);
            head.next = tail;
            tail.prev = head;
        }
        
        public int get(int key) {
            if (!cache.containsKey(key)) return -1;
            
            Node node = cache.get(key);
            // Di chuyển node lên đầu (most recently used)
            removeNode(node);
            addToHead(node);
            
            return node.value;
        }
        
        public void put(int key, int value) {
            if (cache.containsKey(key)) {
                // Update existing
                Node node = cache.get(key);
                node.value = value;
                removeNode(node);
                addToHead(node);
            } else {
                // Add new
                Node newNode = new Node(key, value);
                cache.put(key, newNode);
                addToHead(newNode);
                
                if (cache.size() > capacity) {
                    // Remove LRU (tail.prev)
                    Node lru = tail.prev;
                    removeNode(lru);
                    cache.remove(lru.key);
                }
            }
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

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 217 - CONTAINS DUPLICATE ⭐
    //                     https://leetcode.com/problems/contains-duplicate/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Kiểm tra mảng có phần tử trùng lặp không.                                                   │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ [1, 2, 3, 1] → true                                                                         │
     * │ [1, 2, 3, 4] → false                                                                        │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(N) không gian                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            if (!seen.add(num)) { // add() returns false if element exists
                return true;
            }
        }
        return false;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 242 - VALID ANAGRAM ⭐
    //                        https://leetcode.com/problems/valid-anagram/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Kiểm tra 2 string có phải anagram không.                                                    │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ "anagram", "nagaram" → true                                                                 │
     * │ "rat", "car" → false                                                                        │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian (chỉ 26 ký tự)                                 │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        
        int[] count = new int[26];
        
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
            count[t.charAt(i) - 'a']--;
        }
        
        for (int c : count) {
            if (c != 0) return false;
        }
        
        return true;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 387 - FIRST UNIQUE CHARACTER ⭐
    //              https://leetcode.com/problems/first-unique-character-in-a-string/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm index của ký tự đầu tiên không lặp lại.                                                 │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ "leetcode" → 0 (ký tự 'l')                                                                  │
     * │ "loveleetcode" → 2 (ký tự 'v')                                                              │
     * │ "aabb" → -1                                                                                 │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int firstUniqChar(String s) {
        int[] count = new int[26];
        
        // Đếm tần suất
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        
        // Tìm ký tự đầu tiên xuất hiện 1 lần
        for (int i = 0; i < s.length(); i++) {
            if (count[s.charAt(i) - 'a'] == 1) {
                return i;
            }
        }
        
        return -1;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                         TESTS
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    
    @Test
    void testGroupAnagrams() {
        List<List<String>> result = groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"});
        assertEquals(3, result.size());
    }
    
    @Test
    void testSubarraySum() {
        assertEquals(2, subarraySum(new int[]{1, 1, 1}, 2));
        assertEquals(2, subarraySum(new int[]{1, 2, 3}, 3));
    }
    
    @Test
    void testLongestConsecutive() {
        assertEquals(4, longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
        assertEquals(9, longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}));
    }
    
    @Test
    void testLRUCache() {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1));
        cache.put(3, 3); // evicts key 2
        assertEquals(-1, cache.get(2));
    }
    
    @Test
    void testContainsDuplicate() {
        assertTrue(containsDuplicate(new int[]{1, 2, 3, 1}));
        assertFalse(containsDuplicate(new int[]{1, 2, 3, 4}));
    }
    
    @Test
    void testIsAnagram() {
        assertTrue(isAnagram("anagram", "nagaram"));
        assertFalse(isAnagram("rat", "car"));
    }
    
    @Test
    void testFirstUniqChar() {
        assertEquals(0, firstUniqChar("leetcode"));
        assertEquals(2, firstUniqChar("loveleetcode"));
    }
}
