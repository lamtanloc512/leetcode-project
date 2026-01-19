package com.practice.leetcode.javacollections;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                          STREAM API GUIDE                                 ║
 * ║                            (Java 8+)                                      ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ Stream = sequence of elements supporting sequential/parallel operations    │
 * │                                                                             │
 * │ KEY CONCEPTS:                                                               │
 * │ - LAZY: Intermediate operations không execute cho đến khi có terminal op   │
 * │ - SINGLE USE: Stream chỉ dùng 1 lần, không reuse                            │
 * │ - NON-MUTATING: Không thay đổi source collection                            │
 * │                                                                             │
 * │ PIPELINE: Source → Intermediate ops → Terminal op                          │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class StreamGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // CREATING STREAMS
  // ═══════════════════════════════════════════════════════════════════════════
  void createStreams() {
    // 1. From Collection
    List<String> list = Arrays.asList("a", "b", "c");
    Stream<String> stream1 = list.stream();
    Stream<String> parallel1 = list.parallelStream();

    // 2. From Array
    String[] arr = {"a", "b", "c"};
    Stream<String> stream2 = Arrays.stream(arr);
    Stream<String> stream3 = Stream.of("a", "b", "c");

    // 3. From values
    Stream<String> stream4 = Stream.of("x", "y", "z");
    Stream<String> empty = Stream.empty();

    // 4. Generate / Iterate (infinite streams!)
    Stream<Double> randoms = Stream.generate(Math::random).limit(10);
    Stream<Integer> counting = Stream.iterate(0, n -> n + 1).limit(10);

    // 5. Primitive streams (avoid boxing/unboxing)
    IntStream intStream = IntStream.of(1, 2, 3);
    IntStream range = IntStream.range(0, 10);      // 0-9
    IntStream rangeClosed = IntStream.rangeClosed(0, 10);  // 0-10
    LongStream longStream = LongStream.of(1L, 2L);
    DoubleStream doubleStream = DoubleStream.of(1.0, 2.0);

    // 6. From String
    IntStream chars = "hello".chars();  // stream of char codes

    // 7. From Map
    Map<String, Integer> map = new HashMap<>();
    map.put("a", 1);
    Stream<Map.Entry<String, Integer>> entries = map.entrySet().stream();
    Stream<String> keys = map.keySet().stream();
    Stream<Integer> values = map.values().stream();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // INTERMEDIATE OPERATIONS (returns Stream, lazy)
  // ═══════════════════════════════════════════════════════════════════════════
  void intermediateOperations() {
    List<String> words = Arrays.asList("apple", "banana", "cherry", "apple", "date");

    // FILTER - giữ elements matching predicate
    List<String> long_words = words.stream()
        .filter(s -> s.length() > 5)
        .collect(Collectors.toList());  // [banana, cherry]

    // MAP - transform each element
    List<Integer> lengths = words.stream()
        .map(String::length)
        .collect(Collectors.toList());  // [5, 6, 6, 5, 4]

    // MAP to primitive (avoid boxing)
    int[] lengthsArr = words.stream()
        .mapToInt(String::length)
        .toArray();

    // FLATMAP - flatten nested structures
    List<List<Integer>> nested = Arrays.asList(
        Arrays.asList(1, 2),
        Arrays.asList(3, 4, 5)
    );
    List<Integer> flat = nested.stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());  // [1, 2, 3, 4, 5]

    // DISTINCT - remove duplicates (uses equals())
    List<String> unique = words.stream()
        .distinct()
        .collect(Collectors.toList());

    // SORTED - natural order
    List<String> sorted1 = words.stream()
        .sorted()
        .collect(Collectors.toList());

    // SORTED - custom comparator
    List<String> sorted2 = words.stream()
        .sorted(Comparator.comparingInt(String::length))
        .collect(Collectors.toList());

    // LIMIT - take first n elements
    List<String> first3 = words.stream()
        .limit(3)
        .collect(Collectors.toList());

    // SKIP - skip first n elements
    List<String> skip2 = words.stream()
        .skip(2)
        .collect(Collectors.toList());

    // PEEK - debug (side effect, không nên dùng cho logic)
    words.stream()
        .peek(s -> System.out.println("Processing: " + s))
        .map(String::toUpperCase)
        .collect(Collectors.toList());
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TERMINAL OPERATIONS (triggers execution, returns result)
  // ═══════════════════════════════════════════════════════════════════════════
  void terminalOperations() {
    List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
    List<String> words = Arrays.asList("apple", "banana", "cherry");

    // FOREACH - perform action on each element
    nums.forEach(System.out::println);

    // COLLECT - gather results into collection
    List<Integer> list = nums.stream()
        .filter(n -> n > 2)
        .collect(Collectors.toList());

    Set<Integer> set = nums.stream()
        .collect(Collectors.toSet());

    // TOARRAY
    Integer[] arr = nums.stream()
        .toArray(Integer[]::new);

    // COUNT
    long count = words.stream()
        .filter(s -> s.length() > 5)
        .count();  // 2

    // REDUCE - combine all elements
    int sum = nums.stream()
        .reduce(0, Integer::sum);  // 15

    Optional<Integer> product = nums.stream()
        .reduce((a, b) -> a * b);  // Optional[120]

    // MIN / MAX
    Optional<Integer> min = nums.stream()
        .min(Comparator.naturalOrder());

    Optional<String> longest = words.stream()
        .max(Comparator.comparingInt(String::length));

    // FINDFIRST / FINDANY
    Optional<Integer> first = nums.stream()
        .filter(n -> n > 3)
        .findFirst();  // Optional[4]

    Optional<Integer> any = nums.parallelStream()
        .filter(n -> n > 3)
        .findAny();  // Any matching element

    // ANYMATCH / ALLMATCH / NONEMATCH
    boolean hasEven = nums.stream().anyMatch(n -> n % 2 == 0);   // true
    boolean allPositive = nums.stream().allMatch(n -> n > 0);    // true
    boolean noneNegative = nums.stream().noneMatch(n -> n < 0);  // true
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // COLLECTORS (for collect() terminal operation)
  // ═══════════════════════════════════════════════════════════════════════════
  void collectorsGuide() {
    List<String> words = Arrays.asList("apple", "banana", "avocado", "cherry");

    // TO COLLECTION
    List<String> list = words.stream().collect(Collectors.toList());
    Set<String> set = words.stream().collect(Collectors.toSet());
    LinkedList<String> linked = words.stream()
        .collect(Collectors.toCollection(LinkedList::new));

    // JOINING
    String joined1 = words.stream().collect(Collectors.joining());       // "applebananaa..."
    String joined2 = words.stream().collect(Collectors.joining(", "));   // "apple, banana, ..."
    String joined3 = words.stream().collect(Collectors.joining(", ", "[", "]")); // "[apple, ...]"

    // COUNTING
    long count = words.stream().collect(Collectors.counting());

    // SUMMING / AVERAGING
    int totalLength = words.stream().collect(Collectors.summingInt(String::length));
    double avgLength = words.stream().collect(Collectors.averagingInt(String::length));

    // STATISTICS
    IntSummaryStatistics stats = words.stream()
        .collect(Collectors.summarizingInt(String::length));
    // stats.getCount(), getSum(), getMin(), getMax(), getAverage()

    // MIN / MAX BY
    Optional<String> longest = words.stream()
        .collect(Collectors.maxBy(Comparator.comparingInt(String::length)));

    // GROUPING BY
    Map<Integer, List<String>> byLength = words.stream()
        .collect(Collectors.groupingBy(String::length));
    // {5=[apple], 6=[banana, cherry], 7=[avocado]}

    Map<Integer, Long> countByLength = words.stream()
        .collect(Collectors.groupingBy(String::length, Collectors.counting()));

    Map<Integer, String> joinedByLength = words.stream()
        .collect(Collectors.groupingBy(
            String::length,
            Collectors.joining(", ")
        ));

    // PARTITIONING BY (split into true/false)
    Map<Boolean, List<String>> partitioned = words.stream()
        .collect(Collectors.partitioningBy(s -> s.startsWith("a")));
    // {true=[apple, avocado], false=[banana, cherry]}

    // TO MAP
    Map<String, Integer> wordLengths = words.stream()
        .collect(Collectors.toMap(
            s -> s,           // key mapper
            String::length    // value mapper
        ));

    // TO MAP with merge function (handle duplicates)
    Map<Integer, String> lengthToWord = words.stream()
        .collect(Collectors.toMap(
            String::length,
            s -> s,
            (existing, replacement) -> existing + ", " + replacement  // merge
        ));

    // MAPPING
    Map<Integer, List<Character>> firstCharByLength = words.stream()
        .collect(Collectors.groupingBy(
            String::length,
            Collectors.mapping(s -> s.charAt(0), Collectors.toList())
        ));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // OPTIONAL
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ Optional = container that may or may not contain a value                  │
   * │ Dùng để tránh NullPointerException                                        │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  void optionalGuide() {
    // Creating Optional
    Optional<String> empty = Optional.empty();
    Optional<String> present = Optional.of("hello");  // throws if null!
    Optional<String> nullable = Optional.ofNullable(null);  // safe

    // Checking value
    boolean hasValue = present.isPresent();  // true
    boolean isEmpty = empty.isPresent();     // false

    // Getting value
    String value1 = present.get();  // "hello" (throws if empty!)
    String value2 = present.orElse("default");  // "hello"
    String value3 = empty.orElse("default");    // "default"
    String value4 = empty.orElseGet(() -> "computed");  // lazy
    // String value5 = empty.orElseThrow(() -> new RuntimeException());

    // Transform (returns Optional)
    Optional<Integer> length = present.map(String::length);  // Optional[5]
    Optional<String> upper = present.map(String::toUpperCase);  // Optional[HELLO]

    // FlatMap (when mapper returns Optional)
    Optional<String> result = present.flatMap(s -> 
        s.length() > 3 ? Optional.of(s.toUpperCase()) : Optional.empty()
    );

    // Filter
    Optional<String> filtered = present.filter(s -> s.length() > 3);

    // ifPresent
    present.ifPresent(System.out::println);

    // COMMON PATTERN: Chain operations
    String finalResult = Optional.ofNullable(getUserName())
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .orElse("Anonymous");
  }

  private String getUserName() {
    return null;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PRIMITIVE STREAMS
  // ═══════════════════════════════════════════════════════════════════════════
  void primitiveStreams() {
    // IntStream, LongStream, DoubleStream - avoid boxing overhead

    // Create
    IntStream ints = IntStream.of(1, 2, 3);
    IntStream range = IntStream.range(1, 10);  // 1-9
    IntStream rangeClosed = IntStream.rangeClosed(1, 10);  // 1-10

    // Convert from Stream<T>
    List<String> words = Arrays.asList("a", "bb", "ccc");
    IntStream lengths = words.stream().mapToInt(String::length);

    // Primitive-specific operations
    int sum = IntStream.range(1, 11).sum();          // 55
    OptionalDouble avg = IntStream.range(1, 11).average();  // 5.5
    OptionalInt max = IntStream.range(1, 11).max();
    IntSummaryStatistics stats = IntStream.range(1, 11).summaryStatistics();

    // Box to Stream<Integer>
    Stream<Integer> boxed = IntStream.range(1, 5).boxed();
    List<Integer> list = IntStream.range(1, 5)
        .boxed()
        .collect(Collectors.toList());

    // Convert primitive array to List
    int[] arr = {1, 2, 3, 4, 5};
    List<Integer> fromArr = Arrays.stream(arr)
        .boxed()
        .collect(Collectors.toList());
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // COMMON PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Frequency count                                                   │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  Map<Character, Long> charFrequency(String s) {
    return s.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Find top N by criteria                                            │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  List<String> topNLongest(List<String> words, int n) {
    return words.stream()
        .sorted(Comparator.comparingInt(String::length).reversed())
        .limit(n)
        .collect(Collectors.toList());
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Transform and filter combined                                     │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  List<String> processWords(List<String> words) {
    return words.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .map(String::toLowerCase)
        .distinct()
        .sorted()
        .collect(Collectors.toList());
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Flatten nested collections                                        │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  List<Integer> flattenMatrix(int[][] matrix) {
    return Arrays.stream(matrix)
        .flatMapToInt(Arrays::stream)
        .boxed()
        .collect(Collectors.toList());
  }

  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ PATTERN: Map to object                                                    │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  static class Person {
    String name;
    int age;

    Person(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

  Map<String, Integer> personToAgeMap(List<Person> people) {
    return people.stream()
        .collect(Collectors.toMap(
            p -> p.name,
            p -> p.age,
            (a1, a2) -> a1  // in case of duplicate keys
        ));
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PARALLEL STREAMS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ KHI NÀO DÙNG PARALLEL STREAM:                                             │
   * │ ✓ Large data sets (>10000 elements)                                       │
   * │ ✓ CPU-intensive operations                                                │
   * │ ✓ Stateless operations                                                    │
   * │ ✓ Independent elements                                                    │
   * │                                                                           │
   * │ KHI NÀO KHÔNG DÙNG:                                                       │
   * │ ✗ Small data sets                                                         │
   * │ ✗ I/O operations                                                          │
   * │ ✗ Operations with side effects                                            │
   * │ ✗ Order-dependent operations                                              │
   * └────────────────────────────────────────────────────────────────────────────┘
   */
  void parallelStreamExample() {
    List<Integer> nums = IntStream.range(0, 1000000)
        .boxed()
        .collect(Collectors.toList());

    // Parallel
    long sum = nums.parallelStream()
        .mapToLong(n -> n * n)
        .sum();

    // Convert to parallel
    long count = nums.stream()
        .parallel()
        .filter(n -> n % 2 == 0)
        .count();

    // ⚠️ AVOID: Parallel with shared mutable state
    // List<Integer> results = new ArrayList<>();  // NOT THREAD-SAFE!
    // nums.parallelStream().forEach(results::add);  // WRONG!

    // ✓ CORRECT: Use collect()
    List<Integer> evenNums = nums.parallelStream()
        .filter(n -> n % 2 == 0)
        .collect(Collectors.toList());
  }
}
