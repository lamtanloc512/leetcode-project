package com.practice.leetcode.javacollections;

import java.util.*;
import java.util.stream.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                              LIST GUIDE                                   ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class ListGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // KHỞI TẠO LIST
  // ═══════════════════════════════════════════════════════════════════════════
  void createList() {
    // 1. Empty list
    List<String> list1 = new ArrayList<>();
    List<String> list2 = new LinkedList<>();

    // 2. With initial capacity (ArrayList)
    List<String> list3 = new ArrayList<>(100);

    // 3. From another collection
    List<String> list4 = new ArrayList<>(list1);

    // 4. From varargs (fixed-size, không add/remove được!)
    List<String> fixed = Arrays.asList("a", "b", "c");
    // fixed.add("d"); // UnsupportedOperationException!

    // 5. Mutable copy from Arrays.asList
    List<String> mutable = new ArrayList<>(Arrays.asList("a", "b", "c"));

    // 6. Immutable (Collections.unmodifiableList)
    List<String> immutable = Collections.unmodifiableList(Arrays.asList("a", "b", "c"));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BASIC OPERATIONS
  // ═══════════════════════════════════════════════════════════════════════════
  void basicOperations() {
    List<String> list = new ArrayList<>();

    // ADD
    list.add("apple");              // Add to end
    list.add(0, "banana");          // Add at index
    list.addAll(Arrays.asList("c", "d")); // Add all

    // GET
    String first = list.get(0);     // By index O(1) ArrayList, O(n) LinkedList
    String last = list.get(list.size() - 1);

    // SET (replace)
    list.set(0, "new value");       // Replace at index, returns old value

    // REMOVE
    list.remove(0);                 // By index, returns removed element
    list.remove("apple");           // By value, returns boolean
    list.removeIf(s -> s.length() > 3);  // By condition (Java 8)

    // SEARCH
    boolean exists = list.contains("apple");  // O(n)
    int index = list.indexOf("apple");        // First occurrence, -1 if not found
    int lastIdx = list.lastIndexOf("apple");  // Last occurrence

    // SIZE & EMPTY
    int size = list.size();
    boolean empty = list.isEmpty();

    // CLEAR
    list.clear();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ITERATION
  // ═══════════════════════════════════════════════════════════════════════════
  void iteration() {
    List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

    // 1. For-each (most common)
    for (String s : list) {
      System.out.println(s);
    }

    // 2. Traditional for loop (when need index)
    for (int i = 0; i < list.size(); i++) {
      System.out.println(i + ": " + list.get(i));
    }

    // 3. Iterator (when need to remove during iteration)
    Iterator<String> it = list.iterator();
    while (it.hasNext()) {
      String s = it.next();
      if (s.equals("b")) {
        it.remove();  // SAFE! Không ConcurrentModificationException
      }
    }

    // 4. ListIterator (bidirectional)
    ListIterator<String> lit = list.listIterator();
    while (lit.hasNext()) {
      String s = lit.next();
      lit.set(s.toUpperCase());  // Can modify!
    }
    while (lit.hasPrevious()) {
      System.out.println(lit.previous());  // Go backward
    }

    // 5. forEach method (Java 8)
    list.forEach(System.out::println);
    list.forEach(s -> System.out.println(s.toUpperCase()));

    // 6. Stream (Java 8)
    list.stream()
        .filter(s -> s.length() > 1)
        .map(String::toUpperCase)
        .forEach(System.out::println);
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // SORTING
  // ═══════════════════════════════════════════════════════════════════════════
  void sorting() {
    List<Integer> nums = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5));
    List<String> words = new ArrayList<>(Arrays.asList("banana", "apple", "cherry"));

    // 1. Natural order (Comparable)
    Collections.sort(nums);           // [1, 1, 3, 4, 5]
    nums.sort(null);                  // Same as above
    nums.sort(Comparator.naturalOrder());

    // 2. Reverse order
    Collections.sort(nums, Collections.reverseOrder());
    nums.sort(Comparator.reverseOrder());

    // 3. Custom comparator
    words.sort((a, b) -> a.length() - b.length());  // By length
    words.sort(Comparator.comparingInt(String::length));  // Same

    // 4. Multiple criteria
    List<String> items = new ArrayList<>(Arrays.asList("bb", "aaa", "c", "aa"));
    items.sort(Comparator
        .comparingInt(String::length)         // First by length
        .thenComparing(Comparator.naturalOrder()));  // Then alphabetically
    // Result: [c, aa, bb, aaa]

    // 5. With nulls
    List<String> withNulls = new ArrayList<>(Arrays.asList("b", null, "a"));
    withNulls.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // CONVERSION
  // ═══════════════════════════════════════════════════════════════════════════
  void conversion() {
    List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

    // ─────────────────────────────────────────────────────────────────────────
    // LIST ↔ ARRAY
    // ─────────────────────────────────────────────────────────────────────────
    // List → Array
    String[] arr1 = list.toArray(new String[0]);  // Recommended
    Object[] arr2 = list.toArray();               // Object array

    // Array → List
    String[] arr = {"a", "b", "c"};
    List<String> list1 = Arrays.asList(arr);      // Fixed-size! Backed by array
    List<String> list2 = new ArrayList<>(Arrays.asList(arr));  // Mutable copy

    // ─────────────────────────────────────────────────────────────────────────
    // PRIMITIVE ARRAYS
    // ─────────────────────────────────────────────────────────────────────────
    // int[] → List<Integer>
    int[] intArr = {1, 2, 3};
    List<Integer> intList = Arrays.stream(intArr)
        .boxed()
        .collect(Collectors.toList());

    // List<Integer> → int[]
    List<Integer> integers = Arrays.asList(1, 2, 3);
    int[] intArr2 = integers.stream()
        .mapToInt(Integer::intValue)
        .toArray();

    // double[] → List<Double>
    double[] doubleArr = {1.0, 2.0, 3.0};
    List<Double> doubleList = Arrays.stream(doubleArr)
        .boxed()
        .collect(Collectors.toList());

    // ─────────────────────────────────────────────────────────────────────────
    // LIST ↔ SET
    // ─────────────────────────────────────────────────────────────────────────
    // List → Set (remove duplicates)
    Set<String> hashSet = new HashSet<>(list);            // No order
    Set<String> linkedSet = new LinkedHashSet<>(list);    // Insertion order
    Set<String> treeSet = new TreeSet<>(list);            // Sorted order

    // Set → List
    Set<String> set = new HashSet<>(Arrays.asList("c", "a", "b"));
    List<String> fromSet = new ArrayList<>(set);          // Order not guaranteed
    List<String> sortedList = new ArrayList<>(new TreeSet<>(set));  // Sorted

    // ─────────────────────────────────────────────────────────────────────────
    // LIST ↔ MAP
    // ─────────────────────────────────────────────────────────────────────────
    // List → Map (element → derived value)
    List<String> words = Arrays.asList("apple", "banana", "cherry");
    Map<String, Integer> wordToLength = words.stream()
        .collect(Collectors.toMap(s -> s, String::length));

    // List → Map (index → element)
    Map<Integer, String> indexMap = new HashMap<>();
    for (int i = 0; i < words.size(); i++) {
      indexMap.put(i, words.get(i));
    }
    // Or with stream:
    Map<Integer, String> indexMap2 = IntStream.range(0, words.size())
        .boxed()
        .collect(Collectors.toMap(i -> i, words::get));

    // Map keys → List
    Map<String, Integer> map = new HashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    List<String> keys = new ArrayList<>(map.keySet());

    // Map values → List
    List<Integer> values = new ArrayList<>(map.values());

    // Map entries → List
    List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());

    // ─────────────────────────────────────────────────────────────────────────
    // STRING ↔ LIST
    // ─────────────────────────────────────────────────────────────────────────
    // List → String
    String joined = String.join(", ", list);  // "a, b, c"
    String joined2 = list.stream().collect(Collectors.joining(", ", "[", "]"));

    // String → List<Character>
    String str = "hello";
    List<Character> chars = str.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.toList());

    // Split String → List
    String csv = "a,b,c,d";
    List<String> parts = Arrays.asList(csv.split(","));
    List<String> partsMutable = new ArrayList<>(Arrays.asList(csv.split(",")));

    // List<Character> → String
    List<Character> charList = Arrays.asList('h', 'e', 'l', 'l', 'o');
    String fromChars = charList.stream()
        .map(String::valueOf)
        .collect(Collectors.joining());  // "hello"

    // ─────────────────────────────────────────────────────────────────────────
    // LIST ↔ QUEUE/DEQUE
    // ─────────────────────────────────────────────────────────────────────────
    // List → Queue
    Queue<String> queue = new LinkedList<>(list);
    Deque<String> deque = new ArrayDeque<>(list);

    // Queue → List
    List<String> fromQueue = new ArrayList<>(queue);

    // ─────────────────────────────────────────────────────────────────────────
    // 2D ARRAY ↔ LIST
    // ─────────────────────────────────────────────────────────────────────────
    // int[][] → List<List<Integer>>
    int[][] matrix = {{1, 2}, {3, 4}, {5, 6}};
    List<List<Integer>> listOfLists = Arrays.stream(matrix)
        .map(row -> Arrays.stream(row).boxed().collect(Collectors.toList()))
        .collect(Collectors.toList());

    // List<List<Integer>> → int[][]
    List<List<Integer>> matrix2 = Arrays.asList(
        Arrays.asList(1, 2),
        Arrays.asList(3, 4)
    );
    int[][] arr2d = matrix2.stream()
        .map(row -> row.stream().mapToInt(Integer::intValue).toArray())
        .toArray(int[][]::new);

    // Flatten 2D to 1D
    List<Integer> flat = listOfLists.stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // SUBLIST & VIEWS
  // ═══════════════════════════════════════════════════════════════════════════
  void sublistAndViews() {
    List<Integer> list = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));

    // subList returns a VIEW (not a copy!)
    List<Integer> sub = list.subList(1, 4);  // [1, 2, 3]

    // Changes to subList affect original!
    sub.set(0, 99);  // list is now [0, 99, 2, 3, 4, 5]

    // Clear range efficiently
    list.subList(1, 3).clear();  // Removes elements 1-2

    // Copy if need independent list
    List<Integer> copy = new ArrayList<>(list.subList(0, 2));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // USEFUL UTILITY METHODS
  // ═══════════════════════════════════════════════════════════════════════════
  void utilityMethods() {
    List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9));

    // Max / Min
    int max = Collections.max(list);
    int min = Collections.min(list);

    // Frequency
    int count = Collections.frequency(list, 1);  // 2

    // Reverse (in-place)
    Collections.reverse(list);

    // Shuffle (in-place)
    Collections.shuffle(list);

    // Fill
    Collections.fill(list, 0);  // All elements become 0

    // Swap
    Collections.swap(list, 0, 1);

    // Rotate
    List<Integer> nums = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    Collections.rotate(nums, 2);  // [4, 5, 1, 2, 3]

    // Binary search (list MUST be sorted!)
    List<Integer> sorted = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    int idx = Collections.binarySearch(sorted, 3);  // 2

    // Unmodifiable view
    List<Integer> readOnly = Collections.unmodifiableList(list);
    // readOnly.add(1); // UnsupportedOperationException

    // Synchronized wrapper (thread-safe)
    List<Integer> syncList = Collections.synchronizedList(list);
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ARRAYLIST vs LINKEDLIST
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ USE ARRAYLIST (99% of cases):                                             │
   * │ - Random access (get by index)                                            │
   * │ - Iteration                                                               │
   * │ - Add to end                                                              │
   * │                                                                           │
   * │ USE LINKEDLIST:                                                           │
   * │ - Frequent add/remove at beginning                                        │
   * │ - Implement Queue/Deque (but ArrayDeque is better!)                       │
   * │                                                                           │
   * │ IN PRACTICE: Almost always use ArrayList!                                 │
   * │ LinkedList has poor cache locality and extra memory overhead.             │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
}
