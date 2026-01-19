package com.practice.leetcode.javacollections;

import java.util.*;
import java.util.stream.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                             ARRAYS GUIDE                                  ║
 * ║                       (java.util.Arrays utility)                          ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ Arrays class cung cấp các utility methods để thao tác với arrays            │
 * │                                                                             │
 * │ KEY POINT: Arrays là fixed-size, không thể thay đổi length                  │
 * │ Muốn dynamic size → dùng ArrayList                                          │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class ArraysGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // KHỞI TẠO ARRAY
  // ═══════════════════════════════════════════════════════════════════════════
  void createArrays() {
    // 1. Declaration with size
    int[] arr1 = new int[5];           // [0, 0, 0, 0, 0] - default values
    String[] arr2 = new String[3];     // [null, null, null]
    boolean[] arr3 = new boolean[2];   // [false, false]

    // 2. Declaration with values
    int[] arr4 = {1, 2, 3, 4, 5};
    int[] arr5 = new int[]{1, 2, 3};   // Explicit type
    String[] arr6 = {"a", "b", "c"};

    // 3. 2D Arrays
    int[][] matrix1 = new int[3][4];   // 3 rows, 4 cols
    int[][] matrix2 = {{1, 2}, {3, 4}, {5, 6}};
    int[][] jagged = new int[3][];     // Jagged array (rows have different lengths)
    jagged[0] = new int[2];
    jagged[1] = new int[4];
    jagged[2] = new int[1];
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ARRAYS.SORT()
  // ═══════════════════════════════════════════════════════════════════════════
  void sorting() {
    // ─────────────────────────────────────────────────────────────────────────
    // PRIMITIVE ARRAYS
    // ─────────────────────────────────────────────────────────────────────────
    int[] nums = {5, 2, 8, 1, 9};
    Arrays.sort(nums);                 // [1, 2, 5, 8, 9] - O(n log n)

    // Sort range only [fromIndex, toIndex)
    int[] nums2 = {5, 2, 8, 1, 9};
    Arrays.sort(nums2, 1, 4);          // [5, 1, 2, 8, 9] - sort index 1-3

    // ⚠️ PRIMITIVE: KHÔNG có reverse sort trực tiếp!
    // Workaround: sort rồi reverse thủ công, hoặc dùng Integer[]
    Integer[] boxed = {5, 2, 8, 1, 9};
    Arrays.sort(boxed, Collections.reverseOrder());  // [9, 8, 5, 2, 1]

    // ─────────────────────────────────────────────────────────────────────────
    // OBJECT ARRAYS
    // ─────────────────────────────────────────────────────────────────────────
    String[] words = {"banana", "apple", "cherry"};

    // Natural order
    Arrays.sort(words);                // [apple, banana, cherry]

    // Reverse order
    Arrays.sort(words, Collections.reverseOrder());

    // Custom comparator
    Arrays.sort(words, (a, b) -> a.length() - b.length());         // By length
    Arrays.sort(words, Comparator.comparingInt(String::length));   // Same

    // Multiple criteria
    String[] items = {"bb", "aaa", "c", "aa"};
    Arrays.sort(items, Comparator
        .comparingInt(String::length)
        .thenComparing(Comparator.naturalOrder()));

    // ─────────────────────────────────────────────────────────────────────────
    // 2D ARRAY SORTING
    // ─────────────────────────────────────────────────────────────────────────
    int[][] intervals = {{3, 5}, {1, 2}, {2, 4}};

    // Sort by first element
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    // Or: Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

    // Sort by first, then by second
    Arrays.sort(intervals, (a, b) -> {
      if (a[0] != b[0]) return a[0] - b[0];
      return a[1] - b[1];
    });

    // Using Comparator.comparing
    Arrays.sort(intervals, Comparator
        .comparingInt((int[] a) -> a[0])
        .thenComparingInt(a -> a[1]));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ARRAYS.BINARYSEARCH()
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ QUAN TRỌNG: Array PHẢI ĐƯỢC SORT trước khi binary search!                 │
   * │                                                                           │
   * │ Return:                                                                   │
   * │ - index nếu tìm thấy                                                      │
   * │ - (-(insertion point) - 1) nếu không tìm thấy                             │
   * │   → insertion point = vị trí sẽ insert để giữ sorted order                │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  void binarySearch() {
    int[] arr = {1, 3, 5, 7, 9};

    int idx1 = Arrays.binarySearch(arr, 5);   // 2 (found at index 2)
    int idx2 = Arrays.binarySearch(arr, 4);   // -3 (not found, insertion point = 2)
    int idx3 = Arrays.binarySearch(arr, 0);   // -1 (insertion point = 0)
    int idx4 = Arrays.binarySearch(arr, 10);  // -6 (insertion point = 5)

    // Search in range [fromIndex, toIndex)
    int idx5 = Arrays.binarySearch(arr, 1, 4, 5);  // Search index 1-3

    // Object arrays with comparator
    String[] words = {"apple", "banana", "cherry"};
    Arrays.sort(words);  // Must sort first!
    int idx6 = Arrays.binarySearch(words, "banana");

    // Tìm insertion point từ kết quả negative
    int result = Arrays.binarySearch(arr, 4);  // -3
    int insertionPoint = -(result + 1);        // 2
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ARRAYS.FILL()
  // ═══════════════════════════════════════════════════════════════════════════
  void fill() {
    // Fill entire array
    int[] arr = new int[5];
    Arrays.fill(arr, 10);              // [10, 10, 10, 10, 10]

    // Fill range [fromIndex, toIndex)
    int[] arr2 = new int[5];
    Arrays.fill(arr2, 1, 4, 99);       // [0, 99, 99, 99, 0]

    // Common use: Initialize with max/min for DP
    int[] dp = new int[10];
    Arrays.fill(dp, Integer.MAX_VALUE);
    Arrays.fill(dp, -1);               // Mark as unvisited

    // ⚠️ 2D ARRAY: Không dùng fill trực tiếp!
    int[][] matrix = new int[3][3];
    // Arrays.fill(matrix, 5);         // WRONG! fills with same reference

    // Correct way for 2D
    for (int[] row : matrix) {
      Arrays.fill(row, 5);
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ARRAYS.COPYOF() & ARRAYS.COPYOFRANGE()
  // ═══════════════════════════════════════════════════════════════════════════
  void copy() {
    int[] original = {1, 2, 3, 4, 5};

    // Copy entire array
    int[] copy1 = Arrays.copyOf(original, original.length);  // [1, 2, 3, 4, 5]

    // Copy with new length (truncate or pad with 0)
    int[] copy2 = Arrays.copyOf(original, 3);    // [1, 2, 3]
    int[] copy3 = Arrays.copyOf(original, 8);    // [1, 2, 3, 4, 5, 0, 0, 0]

    // Copy range [from, to)
    int[] copy4 = Arrays.copyOfRange(original, 1, 4);  // [2, 3, 4]

    // Alternative: System.arraycopy (faster for large arrays)
    int[] dest = new int[3];
    System.arraycopy(original, 1, dest, 0, 3);  // Copy 3 elements from index 1
    // dest = [2, 3, 4]

    // Clone (shallow copy)
    int[] clone = original.clone();

    // 2D array copy (SHALLOW - inner arrays are same references!)
    int[][] matrix = {{1, 2}, {3, 4}};
    int[][] shallowCopy = matrix.clone();
    shallowCopy[0][0] = 99;  // ⚠️ Also modifies matrix[0][0]!

    // Deep copy 2D array
    int[][] deepCopy = new int[matrix.length][];
    for (int i = 0; i < matrix.length; i++) {
      deepCopy[i] = Arrays.copyOf(matrix[i], matrix[i].length);
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ARRAYS.EQUALS() & ARRAYS.DEEPEQUALS()
  // ═══════════════════════════════════════════════════════════════════════════
  void equals() {
    int[] a = {1, 2, 3};
    int[] b = {1, 2, 3};
    int[] c = {1, 2, 4};

    // ⚠️ ĐỪNG dùng == cho arrays!
    boolean wrong = (a == b);        // false (different references)

    // equals() - compare contents
    boolean eq1 = Arrays.equals(a, b);   // true
    boolean eq2 = Arrays.equals(a, c);   // false

    // Compare range
    //boolean eq3 = Arrays.equals(a, 0, 2, b, 0, 2);  // Java 9+

    // 2D arrays: dùng deepEquals()
    int[][] m1 = {{1, 2}, {3, 4}};
    int[][] m2 = {{1, 2}, {3, 4}};
    boolean eq4 = Arrays.equals(m1, m2);      // false! (compares references)
    boolean eq5 = Arrays.deepEquals(m1, m2);  // true (deep comparison)
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ARRAYS.TOSTRING() & ARRAYS.DEEPTOSTRING()
  // ═══════════════════════════════════════════════════════════════════════════
  void toStringMethods() {
    int[] arr = {1, 2, 3, 4, 5};

    // ⚠️ ĐỪNG dùng arr.toString()!
    String wrong = arr.toString();           // [I@1234abcd (memory address)

    // Correct way
    String str = Arrays.toString(arr);       // [1, 2, 3, 4, 5]

    // 2D arrays
    int[][] matrix = {{1, 2}, {3, 4}};
    String str2 = Arrays.toString(matrix);      // [[I@..., [I@...]
    String str3 = Arrays.deepToString(matrix);  // [[1, 2], [3, 4]]

    // For debugging
    System.out.println(Arrays.toString(arr));
    System.out.println(Arrays.deepToString(matrix));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ARRAYS.STREAM()
  // ═══════════════════════════════════════════════════════════════════════════
  void streamOperations() {
    int[] nums = {1, 2, 3, 4, 5};

    // Create stream
    IntStream stream1 = Arrays.stream(nums);
    IntStream stream2 = Arrays.stream(nums, 1, 4);  // Range [1, 4)

    // Common operations
    int sum = Arrays.stream(nums).sum();                    // 15
    OptionalInt max = Arrays.stream(nums).max();            // OptionalInt[5]
    OptionalInt min = Arrays.stream(nums).min();            // OptionalInt[1]
    double avg = Arrays.stream(nums).average().orElse(0);   // 3.0
    long count = Arrays.stream(nums).count();               // 5

    // Filter and collect
    int[] evens = Arrays.stream(nums)
        .filter(n -> n % 2 == 0)
        .toArray();  // [2, 4]

    // Map
    int[] squared = Arrays.stream(nums)
        .map(n -> n * n)
        .toArray();  // [1, 4, 9, 16, 25]

    // Reduce
    int product = Arrays.stream(nums)
        .reduce(1, (a, b) -> a * b);  // 120

    // Convert to List
    List<Integer> list = Arrays.stream(nums)
        .boxed()
        .collect(Collectors.toList());

    // Object array stream
    String[] words = {"a", "b", "c"};
    Stream<String> wordStream = Arrays.stream(words);
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ARRAYS.ASLIST()
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ ⚠️ QUAN TRỌNG: Arrays.asList() trả về FIXED-SIZE list!                    │
   * │ - Backed by original array (changes reflect both ways)                    │
   * │ - KHÔNG thể add() hoặc remove()                                           │
   * │ - CÓ THỂ set() (modify elements)                                          │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  void asList() {
    // Basic usage
    List<String> list = Arrays.asList("a", "b", "c");
    // list.add("d");  // UnsupportedOperationException!
    list.set(0, "X");  // OK - list is now [X, b, c]

    // Backed by array
    String[] arr = {"a", "b", "c"};
    List<String> backed = Arrays.asList(arr);
    arr[0] = "X";      // backed is also [X, b, c]!
    backed.set(1, "Y"); // arr is also [X, Y, c]!

    // To get mutable list
    List<String> mutable = new ArrayList<>(Arrays.asList("a", "b", "c"));
    mutable.add("d");  // OK

    // ⚠️ TRAP với primitive arrays!
    int[] intArr = {1, 2, 3};
    List<int[]> wrong = Arrays.asList(intArr);  // List of int[] (size 1!)

    // Correct for primitives
    Integer[] boxed = {1, 2, 3};
    List<Integer> correct = Arrays.asList(boxed);

    // Or use stream
    List<Integer> fromStream = Arrays.stream(intArr)
        .boxed()
        .collect(Collectors.toList());
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // COMMON PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Reverse array                                                    │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  void reverseArray(int[] arr) {
    int left = 0, right = arr.length - 1;
    while (left < right) {
      int temp = arr[left];
      arr[left] = arr[right];
      arr[right] = temp;
      left++;
      right--;
    }
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Find min/max                                                     │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  void findMinMax() {
    int[] arr = {3, 1, 4, 1, 5, 9};

    // Using stream
    int max = Arrays.stream(arr).max().orElse(Integer.MIN_VALUE);
    int min = Arrays.stream(arr).min().orElse(Integer.MAX_VALUE);

    // Traditional loop (faster for simple cases)
    int maxLoop = arr[0], minLoop = arr[0];
    for (int num : arr) {
      maxLoop = Math.max(maxLoop, num);
      minLoop = Math.min(minLoop, num);
    }
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Sum array                                                        │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  int sumArray(int[] arr) {
    // Using stream
    return Arrays.stream(arr).sum();

    // Traditional
    // int sum = 0;
    // for (int n : arr) sum += n;
    // return sum;
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Check if array contains value                                    │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  boolean contains(int[] arr, int target) {
    // Using stream
    return Arrays.stream(arr).anyMatch(n -> n == target);

    // Using binary search (if sorted)
    // return Arrays.binarySearch(arr, target) >= 0;

    // For objects
    // return Arrays.asList(objArr).contains(target);
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Initialize prefix sum array                                      │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  int[] prefixSum(int[] arr) {
    int[] prefix = new int[arr.length + 1];
    for (int i = 0; i < arr.length; i++) {
      prefix[i + 1] = prefix[i] + arr[i];
    }
    return prefix;  // prefix[i] = sum of arr[0..i-1]
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Rotate array                                                     │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  void rotateRight(int[] arr, int k) {
    int n = arr.length;
    k = k % n;  // Handle k > n

    reverse(arr, 0, n - 1);
    reverse(arr, 0, k - 1);
    reverse(arr, k, n - 1);
  }

  private void reverse(int[] arr, int start, int end) {
    while (start < end) {
      int temp = arr[start];
      arr[start] = arr[end];
      arr[end] = temp;
      start++;
      end--;
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // COMPLEXITY SUMMARY
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ Operation           │ Time          │ Notes                               │
   * │ ─────────────────────┼───────────────┼─────────────────────────────────────│
   * │ Arrays.sort()       │ O(n log n)    │ Dual-pivot quicksort (primitives)   │
   * │                     │               │ TimSort (objects)                   │
   * │ Arrays.binarySearch │ O(log n)      │ Requires sorted array               │
   * │ Arrays.fill()       │ O(n)          │                                     │
   * │ Arrays.copyOf()     │ O(n)          │                                     │
   * │ Arrays.equals()     │ O(n)          │                                     │
   * │ Arrays.toString()   │ O(n)          │                                     │
   * │ Access by index     │ O(1)          │                                     │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
}
