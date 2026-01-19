package com.practice.leetcode.javacollections;

import java.util.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                               MAP GUIDE                                   ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * Map = Collection lưu trữ key-value pairs
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ HashMap         - O(1) get/put, KHÔNG ordered, allows null key              │
 * │ LinkedHashMap   - O(1) operations, giữ INSERTION order                      │
 * │ TreeMap         - O(log n), SORTED by key                                   │
 * │ ConcurrentHashMap - Thread-safe, NO null key/value                          │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class MapGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // KHỞI TẠO MAP
  // ═══════════════════════════════════════════════════════════════════════════
  void createMap() {
    // 1. Empty map
    Map<String, Integer> hashMap = new HashMap<>();
    Map<String, Integer> linkedMap = new LinkedHashMap<>();  // Insertion order
    Map<String, Integer> treeMap = new TreeMap<>();          // Sorted by key

    // 2. With initial capacity
    Map<String, Integer> map = new HashMap<>(100);

    // 3. Copy from another map
    Map<String, Integer> copy = new HashMap<>(hashMap);

    // 4. Initialize with values (Java 8)
    Map<String, Integer> init = new HashMap<>();
    init.put("a", 1);
    init.put("b", 2);
    init.put("c", 3);

    // 5. Double-brace initialization (anonymous subclass - use carefully!)
    Map<String, Integer> doubleBrace = new HashMap<String, Integer>() {{
      put("a", 1);
      put("b", 2);
    }};

    // 6. Immutable map
    Map<String, Integer> immutable = Collections.unmodifiableMap(init);

    // 7. TreeMap with custom comparator
    Map<String, Integer> byLength = new TreeMap<>(
        Comparator.comparingInt(String::length)
    );
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BASIC OPERATIONS
  // ═══════════════════════════════════════════════════════════════════════════
  void basicOperations() {
    Map<String, Integer> map = new HashMap<>();

    // PUT
    map.put("apple", 1);              // returns previous value (null if new)
    map.put("apple", 2);              // overwrites! returns 1
    Map<String, Integer> other = new HashMap<>();
    other.put("b", 2);
    other.put("c", 3);
    map.putAll(other);

    // PUT IF ABSENT (chỉ put nếu key chưa có) - Java 8
    map.putIfAbsent("apple", 99);     // KHÔNG overwrite vì đã có
    map.putIfAbsent("new", 99);       // Put vì chưa có

    // GET
    Integer value = map.get("apple");       // null if not found
    Integer def = map.getOrDefault("x", 0); // default if not found (Java 8)

    // CONTAINS
    boolean hasKey = map.containsKey("apple");
    boolean hasValue = map.containsValue(1);  // O(n)!

    // REMOVE
    Integer removed = map.remove("apple");    // returns value or null
    boolean removed2 = map.remove("key", 1);  // remove only if value matches (Java 8)

    // SIZE
    int size = map.size();
    boolean empty = map.isEmpty();

    // CLEAR
    map.clear();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ITERATION
  // ═══════════════════════════════════════════════════════════════════════════
  void iteration() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);

    // 1. Iterate entries (MOST COMMON)
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
    }

    // 2. Iterate keys only
    for (String key : map.keySet()) {
      System.out.println(key);
    }

    // 3. Iterate values only
    for (Integer value : map.values()) {
      System.out.println(value);
    }

    // 4. forEach method (Java 8)
    map.forEach((k, v) -> System.out.println(k + ": " + v));

    // 5. Stream (Java 8)
    map.entrySet().stream()
        .filter(e -> e.getValue() > 1)
        .forEach(e -> System.out.println(e.getKey()));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // COMPUTE METHODS (Java 8)
  // ═══════════════════════════════════════════════════════════════════════════
  void computeMethods() {
    Map<String, Integer> map = new HashMap<>();

    // COMPUTE IF ABSENT - tính và put nếu key chưa có
    // Rất hữu ích cho counter, memoization
    map.computeIfAbsent("key", k -> expensiveCompute(k));

    // COMPUTE IF PRESENT - update nếu key đã có
    map.computeIfPresent("key", (k, v) -> v + 1);

    // COMPUTE - luôn tính (có thể tạo mới hoặc update)
    map.compute("key", (k, v) -> (v == null) ? 1 : v + 1);

    // MERGE - combine old và new value
    // Nếu key chưa có: put new value
    // Nếu key đã có: apply function(oldValue, newValue)
    map.merge("key", 1, Integer::sum);  // count++

    // REPLACE
    map.replace("key", 100);              // replace if exists
    map.replace("key", 1, 100);           // replace only if value matches
    map.replaceAll((k, v) -> v * 2);      // apply to all
  }

  private Integer expensiveCompute(String k) {
    return k.length();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // COMMON PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Frequency Counter                                                │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  Map<Character, Integer> countFrequency(String s) {
    Map<Character, Integer> freq = new HashMap<>();

    // Way 1: getOrDefault
    for (char c : s.toCharArray()) {
      freq.put(c, freq.getOrDefault(c, 0) + 1);
    }

    // Way 2: merge (cleaner) - Java 8
    // for (char c : s.toCharArray()) {
    //   freq.merge(c, 1, Integer::sum);
    // }

    // Way 3: compute - Java 8
    // for (char c : s.toCharArray()) {
    //   freq.compute(c, (k, v) -> v == null ? 1 : v + 1);
    // }

    return freq;
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Group by                                                          │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  Map<Integer, List<String>> groupByLength(List<String> words) {
    Map<Integer, List<String>> groups = new HashMap<>();

    for (String word : words) {
      groups.computeIfAbsent(word.length(), k -> new ArrayList<>())
          .add(word);
    }

    return groups;
    // Or with streams (Java 8):
    // return words.stream().collect(Collectors.groupingBy(String::length));
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Two Sum (HashMap pattern)                                         │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();  // value -> index

    for (int i = 0; i < nums.length; i++) {
      int complement = target - nums[i];
      if (map.containsKey(complement)) {
        return new int[]{map.get(complement), i};
      }
      map.put(nums[i], i);
    }
    return new int[]{};
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: LRU Cache (LinkedHashMap)                                        │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  static class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
      // accessOrder = true: order by access (LRU)
      super(capacity, 0.75f, true);
      this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
      return size() > capacity;  // Remove oldest when capacity exceeded
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TREEMAP SPECIFIC
  // ═══════════════════════════════════════════════════════════════════════════
  void treeMapOperations() {
    TreeMap<Integer, String> treeMap = new TreeMap<>();
    treeMap.put(1, "a");
    treeMap.put(3, "c");
    treeMap.put(5, "e");
    treeMap.put(7, "g");

    // Navigation
    Integer firstKey = treeMap.firstKey();     // 1
    Integer lastKey = treeMap.lastKey();       // 7

    Integer lowerKey = treeMap.lowerKey(5);    // 3 (strictly less)
    Integer floorKey = treeMap.floorKey(5);    // 5 (less or equal)
    Integer higherKey = treeMap.higherKey(5);  // 7 (strictly greater)
    Integer ceilingKey = treeMap.ceilingKey(5);// 5 (greater or equal)

    // Entries
    Map.Entry<Integer, String> firstEntry = treeMap.firstEntry();
    Map.Entry<Integer, String> lastEntry = treeMap.lastEntry();

    // Poll (remove and return)
    Map.Entry<Integer, String> poll = treeMap.pollFirstEntry();

    // SubMaps (views!)
    SortedMap<Integer, String> head = treeMap.headMap(5);    // keys < 5
    SortedMap<Integer, String> tail = treeMap.tailMap(5);    // keys >= 5
    SortedMap<Integer, String> sub = treeMap.subMap(3, 7);   // 3 <= key < 7

    // Descending
    NavigableMap<Integer, String> desc = treeMap.descendingMap();
  }
}
