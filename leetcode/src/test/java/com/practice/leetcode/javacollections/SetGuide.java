package com.practice.leetcode.javacollections;

import java.util.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                               SET GUIDE                                   ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * Set = Collection KHÔNG cho phép duplicate
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ HashSet         - O(1) add/remove/contains, KHÔNG ordered                   │
 * │ LinkedHashSet   - O(1) operations, giữ INSERTION order                      │
 * │ TreeSet         - O(log n), SORTED order                                    │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class SetGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // KHỞI TẠO SET
  // ═══════════════════════════════════════════════════════════════════════════
  void createSet() {
    // 1. Empty set
    Set<String> hashSet = new HashSet<>();
    Set<String> linkedSet = new LinkedHashSet<>();  // Insertion order
    Set<String> treeSet = new TreeSet<>();          // Sorted order

    // 2. With initial capacity (HashSet)
    Set<String> set = new HashSet<>(100);

    // 3. From collection (remove duplicates!)
    List<String> listWithDups = Arrays.asList("a", "b", "a", "c");
    Set<String> unique = new HashSet<>(listWithDups);  // {a, b, c}

    // 4. Immutable set
    Set<String> immutable = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("a", "b", "c"))
    );

    // 5. TreeSet with custom comparator
    Set<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BASIC OPERATIONS
  // ═══════════════════════════════════════════════════════════════════════════
  void basicOperations() {
    Set<String> set = new HashSet<>();

    // ADD - returns true if added, false if already exists
    boolean added = set.add("apple");   // true
    boolean dup = set.add("apple");     // false (duplicate!)
    set.addAll(Arrays.asList("b", "c"));

    // REMOVE
    boolean removed = set.remove("apple");  // true if existed
    set.removeIf(s -> s.length() > 3);

    // CONTAINS - O(1) for HashSet!
    boolean exists = set.contains("apple");
    boolean all = set.containsAll(Arrays.asList("a", "b"));

    // SIZE & EMPTY
    int size = set.size();
    boolean empty = set.isEmpty();

    // CLEAR
    set.clear();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // SET OPERATIONS (Math set operations)
  // ═══════════════════════════════════════════════════════════════════════════
  void setOperations() {
    Set<Integer> a = new HashSet<>(Arrays.asList(1, 2, 3, 4));
    Set<Integer> b = new HashSet<>(Arrays.asList(3, 4, 5, 6));

    // UNION (A ∪ B)
    Set<Integer> union = new HashSet<>(a);
    union.addAll(b);  // {1, 2, 3, 4, 5, 6}

    // INTERSECTION (A ∩ B)
    Set<Integer> intersection = new HashSet<>(a);
    intersection.retainAll(b);  // {3, 4}

    // DIFFERENCE (A - B)
    Set<Integer> difference = new HashSet<>(a);
    difference.removeAll(b);  // {1, 2}

    // SYMMETRIC DIFFERENCE (A △ B) = (A ∪ B) - (A ∩ B)
    Set<Integer> symDiff = new HashSet<>(a);
    symDiff.addAll(b);
    Set<Integer> common = new HashSet<>(a);
    common.retainAll(b);
    symDiff.removeAll(common);  // {1, 2, 5, 6}

    // SUBSET CHECK
    Set<Integer> small = new HashSet<>(Arrays.asList(1, 2));
    boolean isSubset = a.containsAll(small);  // true

    // DISJOINT CHECK (no common elements)
    Set<Integer> c = new HashSet<>(Arrays.asList(7, 8));
    boolean disjoint = Collections.disjoint(a, c);  // true
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // ITERATION
  // ═══════════════════════════════════════════════════════════════════════════
  void iteration() {
    Set<String> set = new LinkedHashSet<>(Arrays.asList("a", "b", "c"));

    // 1. For-each
    for (String s : set) {
      System.out.println(s);
    }

    // 2. Iterator
    Iterator<String> it = set.iterator();
    while (it.hasNext()) {
      String s = it.next();
      if (s.equals("b")) {
        it.remove();  // Safe removal during iteration
      }
    }

    // 3. forEach method (Java 8)
    set.forEach(System.out::println);

    // 4. Stream (Java 8)
    set.stream()
        .filter(s -> s.length() > 0)
        .forEach(System.out::println);
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TREESET SPECIFIC
  // ═══════════════════════════════════════════════════════════════════════════
  void treeSetOperations() {
    TreeSet<Integer> treeSet = new TreeSet<>(Arrays.asList(1, 3, 5, 7, 9));

    // Navigation methods
    Integer first = treeSet.first();     // 1
    Integer last = treeSet.last();       // 9

    Integer lower = treeSet.lower(5);    // 3 (strictly less than)
    Integer floor = treeSet.floor(5);    // 5 (less than or equal)
    Integer higher = treeSet.higher(5);  // 7 (strictly greater)
    Integer ceiling = treeSet.ceiling(5);// 5 (greater than or equal)

    // Poll (remove and return)
    Integer pollFirst = treeSet.pollFirst();  // 1 (removed!)
    Integer pollLast = treeSet.pollLast();    // 9 (removed!)

    // Subsets (views!)
    SortedSet<Integer> head = treeSet.headSet(5);     // [3] (< 5)
    SortedSet<Integer> tail = treeSet.tailSet(5);     // [5, 7] (>= 5)
    SortedSet<Integer> sub = treeSet.subSet(3, 7);    // [3, 5] (>= 3 and < 7)

    // Descending
    NavigableSet<Integer> desc = treeSet.descendingSet();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // COMMON PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Remove duplicates from List                                      │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  List<Integer> removeDuplicates(List<Integer> list) {
    // Không giữ order
    return new ArrayList<>(new HashSet<>(list));

    // Giữ order
    // return new ArrayList<>(new LinkedHashSet<>(list));
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Find duplicates in List                                          │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  Set<Integer> findDuplicates(List<Integer> list) {
    Set<Integer> seen = new HashSet<>();
    Set<Integer> duplicates = new HashSet<>();

    for (Integer num : list) {
      if (!seen.add(num)) {  // add returns false if already exists
        duplicates.add(num);
      }
    }
    return duplicates;
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Check if two arrays have common elements                         │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  boolean hasCommon(int[] a, int[] b) {
    Set<Integer> setA = new HashSet<>();
    for (int n : a) setA.add(n);

    for (int n : b) {
      if (setA.contains(n)) return true;
    }
    return false;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // CUSTOM OBJECTS IN SET
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ QUAN TRỌNG: Để custom object hoạt động đúng trong HashSet                 │
   * │ PHẢI override cả equals() VÀ hashCode()!                                  │
   * │                                                                           │
   * │ CONTRACT:                                                                 │
   * │ - Nếu a.equals(b) = true → a.hashCode() == b.hashCode()                   │
   * │ - Ngược lại không bắt buộc (collision OK)                                 │
   * │                                                                           │
   * │ TreeSet: Objects phải implement Comparable hoặc provide Comparator        │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  static class Person {
    String name;
    int age;

    Person(String name, int age) {
      this.name = name;
      this.age = age;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Person)) return false;
      Person p = (Person) o;
      return age == p.age && Objects.equals(name, p.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, age);
    }
  }
}
