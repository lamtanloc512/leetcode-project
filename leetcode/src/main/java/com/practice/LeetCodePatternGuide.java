package com.practice;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * LEETCODE PATTERNS COMPLETE GUIDE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Tổng hợp TẤT CẢ patterns từ LeetCode với bài tập đại diện
 * 
 * Total: 60+ patterns
 * Focus: 20 core patterns (master these first!)
 * Advanced: 40+ specialized patterns
 * 
 * Format:
 * - Pattern name + description
 * - When to use
 * - 3 representative problems (Easy/Medium/Hard)
 * - LeetCode numbers for practice
 * 
 * Study plan: 1-2 patterns/week → 6 months to master all core patterns
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 */
public class LeetCodePatternGuide {
    
    // ═══════════════════════════════════════════════════════════════════════════
    // PART 1: CORE PATTERNS (TOP 20 - MUST MASTER!)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 1. TWO POINTERS                                                          │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Use 2 pointers to traverse array/list from different positions
     * - Opposite ends (start/end)
     * - Same direction (fast/slow)
     * 
     * WHEN TO USE:
     * - Sorted array
     * - Find pair with condition
     * - Remove duplicates
     * - Detect cycle
     * - Palindrome check
     * 
     * TIME: O(n)
     * SPACE: O(1)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 167. Two Sum II - Input Array Is Sorted (Easy)
     *    Given sorted array, find two numbers sum to target
     *    → Use left/right pointers from opposite ends
     * 
     * 2️⃣ LC 15. 3Sum (Medium)
     *    Find all triplets that sum to zero
     *    → Sort + fix one + two pointers for remaining two
     * 
     * 3️⃣ LC 11. Container With Most Water (Medium)
     *    Find two lines that form container with most water
     *    → Greedy two pointers, move shorter line inward
     * 
     * BONUS PRACTICE:
     * - LC 26. Remove Duplicates from Sorted Array
     * - LC 283. Move Zeroes
     * - LC 125. Valid Palindrome
     * - LC 141. Linked List Cycle (Fast/Slow)
     * - LC 142. Linked List Cycle II
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 2. SLIDING WINDOW                                                        │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Maintain a window (subarray/substring) that slides through array
     * - Fixed size window
     * - Dynamic size window
     * 
     * WHEN TO USE:
     * Keywords: "subarray", "substring", "contiguous", "window", "at most K"
     * 
     * TIME: O(n)
     * SPACE: O(k) for tracking window state
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 209. Minimum Size Subarray Sum (Medium)
     *    Find minimum length subarray with sum >= target
     *    → Dynamic window, expand/shrink based on sum
     * 
     * 2️⃣ LC 3. Longest Substring Without Repeating Characters (Medium)
     *    Find longest substring without duplicate characters
     *    → Use Set to track characters in window
     * 
     * 3️⃣ LC 76. Minimum Window Substring (Hard)
     *    Find minimum window containing all characters of T
     *    → Use frequency map, track required/formed characters
     * 
     * BONUS PRACTICE:
     * - LC 643. Maximum Average Subarray I (fixed window)
     * - LC 904. Fruit Into Baskets
     * - LC 424. Longest Repeating Character Replacement
     * - LC 567. Permutation in String
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 3. FAST & SLOW POINTERS                                                  │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Two pointers moving at different speeds
     * - Fast moves 2 steps, slow moves 1 step
     * - Detect cycles, find middle, etc.
     * 
     * WHEN TO USE:
     * - LinkedList problems
     * - Cycle detection
     * - Find middle element
     * 
     * TIME: O(n)
     * SPACE: O(1)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 141. Linked List Cycle (Easy)
     *    Detect if linked list has a cycle
     *    → Fast/slow meet if cycle exists
     * 
     * 2️⃣ LC 142. Linked List Cycle II (Medium)
     *    Find start of cycle
     *    → After meeting, reset one pointer to head
     * 
     * 3️⃣ LC 876. Middle of the Linked List (Easy)
     *    Find middle node
     *    → When fast reaches end, slow is at middle
     * 
     * BONUS PRACTICE:
     * - LC 234. Palindrome Linked List
     * - LC 202. Happy Number
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 4. BINARY SEARCH                                                         │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Search in sorted array by repeatedly dividing search space in half
     * 
     * WHEN TO USE:
     * - Sorted array
     * - Search for target/condition
     * - "Find minimum/maximum that satisfies condition"
     * 
     * TIME: O(log n)
     * SPACE: O(1)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 704. Binary Search (Easy)
     *    Classic binary search
     *    → Basic template
     * 
     * 2️⃣ LC 33. Search in Rotated Sorted Array (Medium)
     *    Search in rotated array
     *    → Modified binary search, check which half is sorted
     * 
     * 3️⃣ LC 4. Median of Two Sorted Arrays (Hard)
     *    Find median of two sorted arrays
     *    → Binary search on smaller array
     * 
     * BONUS PRACTICE:
     * - LC 35. Search Insert Position
     * - LC 34. Find First and Last Position of Element
     * - LC 74. Search a 2D Matrix
     * - LC 153. Find Minimum in Rotated Sorted Array
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 5. DEPTH-FIRST SEARCH (DFS)                                              │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Explore as deep as possible before backtracking
     * - Recursive or stack-based
     * - Tree/graph traversal
     * 
     * WHEN TO USE:
     * - Tree/graph traversal
     * - Path finding
     * - Connected components
     * - Topological sort
     * 
     * TIME: O(V + E) for graphs, O(n) for trees
     * SPACE: O(h) recursion depth
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 104. Maximum Depth of Binary Tree (Easy)
     *    Find max depth of tree
     *    → DFS: max(left, right) + 1
     * 
     * 2️⃣ LC 200. Number of Islands (Medium)
     *    Count islands in grid
     *    → DFS to mark connected components
     * 
     * 3️⃣ LC 124. Binary Tree Maximum Path Sum (Hard)
     *    Find max path sum in tree
     *    → DFS with global max tracking
     * 
     * BONUS PRACTICE:
     * - LC 257. Binary Tree Paths
     * - LC 112. Path Sum
     * - LC 113. Path Sum II
     * - LC 543. Diameter of Binary Tree
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 6. BREADTH-FIRST SEARCH (BFS)                                            │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Explore level by level using queue
     * 
     * WHEN TO USE:
     * - Shortest path (unweighted)
     * - Level-order traversal
     * - Minimum steps/moves
     * 
     * TIME: O(V + E) for graphs, O(n) for trees
     * SPACE: O(w) where w is max width
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 102. Binary Tree Level Order Traversal (Medium)
     *    Traverse tree level by level
     *    → Queue-based BFS
     * 
     * 2️⃣ LC 994. Rotting Oranges (Medium)
     *    Find minimum time for all oranges to rot
     *    → Multi-source BFS
     * 
     * 3️⃣ LC 127. Word Ladder (Hard)
     *    Find shortest transformation sequence
     *    → BFS with word graph
     * 
     * BONUS PRACTICE:
     * - LC 103. Binary Tree Zigzag Level Order
     * - LC 199. Binary Tree Right Side View
     * - LC 133. Clone Graph
     * - LC 542. 01 Matrix
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 7. BACKTRACKING                                                          │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Try all possibilities with MAKE → EXPLORE → UNDO pattern
     * 
     * WHEN TO USE:
     * Keywords: "all combinations", "all permutations", "all subsets",
     *           "generate all", "find all"
     * 
     * TIME: O(2^n) or O(n!)
     * SPACE: O(n) recursion depth
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 78. Subsets (Medium)
     *    Generate all subsets
     *    → Include/exclude each element
     * 
     * 2️⃣ LC 46. Permutations (Medium)
     *    Generate all permutations
     *    → Swap or track used elements
     * 
     * 3️⃣ LC 51. N-Queens (Hard)
     *    Place N queens on N×N board
     *    → Row by row placement with conflict checking
     * 
     * BONUS PRACTICE:
     * - LC 77. Combinations
     * - LC 39. Combination Sum
     * - LC 22. Generate Parentheses
     * - LC 79. Word Search
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 8. DYNAMIC PROGRAMMING                                                   │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Break problem into overlapping subproblems, store results
     * - Top-down (memoization)
     * - Bottom-up (tabulation)
     * 
     * WHEN TO USE:
     * - Optimization problems (min/max)
     * - Counting problems
     * - Overlapping subproblems
     * - Optimal substructure
     * 
     * TIME: O(n²) to O(n³) typically
     * SPACE: O(n) to O(n²)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 70. Climbing Stairs (Easy)
     *    Count ways to climb n stairs
     *    → Fibonacci pattern: dp[i] = dp[i-1] + dp[i-2]
     * 
     * 2️⃣ LC 322. Coin Change (Medium)
     *    Minimum coins to make amount
     *    → Unbounded knapsack
     * 
     * 3️⃣ LC 72. Edit Distance (Hard)
     *    Minimum operations to transform string
     *    → 2D DP, compare characters
     * 
     * BONUS PRACTICE:
     * - LC 198. House Robber
     * - LC 300. Longest Increasing Subsequence
     * - LC 1143. Longest Common Subsequence
     * - LC 518. Coin Change II
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 9. GREEDY                                                                │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Make locally optimal choice at each step
     * 
     * WHEN TO USE:
     * - Optimization problems
     * - Can prove greedy choice is optimal
     * - Interval scheduling, activity selection
     * 
     * TIME: O(n) to O(n log n) (with sorting)
     * SPACE: O(1)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 455. Assign Cookies (Easy)
     *    Maximize children satisfied
     *    → Sort and greedy assignment
     * 
     * 2️⃣ LC 55. Jump Game (Medium)
     *    Can reach last index?
     *    → Track farthest reachable position
     * 
     * 3️⃣ LC 45. Jump Game II (Medium)
     *    Minimum jumps to reach end
     *    → BFS-like greedy, track current/next range
     * 
     * BONUS PRACTICE:
     * - LC 435. Non-overlapping Intervals
     * - LC 452. Minimum Number of Arrows to Burst Balloons
     * - LC 134. Gas Station
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 10. HASH TABLE                                                           │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * O(1) average lookup/insert/delete
     * - Frequency counter
     * - Index mapping
     * - Complement lookup
     * 
     * WHEN TO USE:
     * - Fast lookup needed
     * - Count frequencies
     * - Find complement/pair
     * 
     * TIME: O(1) average per operation
     * SPACE: O(n)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 1. Two Sum (Easy)
     *    Find two numbers that sum to target
     *    → Store complement in map
     * 
     * 2️⃣ LC 49. Group Anagrams (Medium)
     *    Group strings that are anagrams
     *    → Use sorted string as key
     * 
     * 3️⃣ LC 146. LRU Cache (Medium)
     *    Implement LRU cache
     *    → HashMap + Doubly LinkedList
     * 
     * BONUS PRACTICE:
     * - LC 217. Contains Duplicate
     * - LC 242. Valid Anagram
     * - LC 383. Ransom Note
     * - LC 387. First Unique Character in a String
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 11. HEAP (PRIORITY QUEUE)                                                │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Always access min/max element in O(log n)
     * - MinHeap: root is minimum
     * - MaxHeap: root is maximum
     * 
     * WHEN TO USE:
     * Keywords: "top K", "Kth largest/smallest", "median"
     * 
     * TIME: O(log n) insert/delete, O(1) peek
     * SPACE: O(n)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 215. Kth Largest Element in an Array (Medium)
     *    Find Kth largest element
     *    → MinHeap of size K
     * 
     * 2️⃣ LC 347. Top K Frequent Elements (Medium)
     *    Find K most frequent elements
     *    → Frequency map + heap
     * 
     * 3️⃣ LC 295. Find Median from Data Stream (Hard)
     *    Online median computation
     *    → Two heaps: maxHeap (left half) + minHeap (right half)
     * 
     * BONUS PRACTICE:
     * - LC 973. K Closest Points to Origin
     * - LC 703. Kth Largest Element in a Stream
     * - LC 23. Merge k Sorted Lists
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 12. PREFIX SUM                                                           │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Precompute cumulative sums for range queries
     * prefix[i] = sum of elements [0...i]
     * sum[i...j] = prefix[j] - prefix[i-1]
     * 
     * WHEN TO USE:
     * - Multiple range sum queries
     * - Subarray sum problems
     * 
     * TIME: O(n) preprocessing, O(1) query
     * SPACE: O(n)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 303. Range Sum Query - Immutable (Easy)
     *    Query sum of range [i, j] multiple times
     *    → Build prefix sum array
     * 
     * 2️⃣ LC 560. Subarray Sum Equals K (Medium)
     *    Count subarrays with sum = k
     *    → Prefix sum + HashMap
     * 
     * 3️⃣ LC 974. Subarray Sums Divisible by K (Medium)
     *    Count subarrays with sum divisible by K
     *    → Prefix sum modulo + frequency map
     * 
     * BONUS PRACTICE:
     * - LC 304. Range Sum Query 2D
     * - LC 525. Contiguous Array
     * - LC 523. Continuous Subarray Sum
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 13. SORTING                                                              │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Order elements by comparison
     * 
     * WHEN TO USE:
     * - Need ordered data
     * - Enable two pointers/binary search
     * - Interval problems
     * 
     * TIME: O(n log n)
     * SPACE: O(1) to O(n)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 912. Sort an Array (Medium)
     *    Implement sorting algorithm
     *    → QuickSort, MergeSort, HeapSort
     * 
     * 2️⃣ LC 56. Merge Intervals (Medium)
     *    Merge overlapping intervals
     *    → Sort by start time
     * 
     * 3️⃣ LC 75. Sort Colors (Medium)
     *    Sort array with 3 colors (Dutch National Flag)
     *    → Three-way partitioning
     * 
     * BONUS PRACTICE:
     * - LC 148. Sort List (LinkedList merge sort)
     * - LC 274. H-Index
     * - LC 164. Maximum Gap
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 14. STACK                                                                │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * LIFO data structure
     * 
     * WHEN TO USE:
     * - Matching pairs (parentheses, brackets)
     * - Next greater/smaller element
     * - Expression evaluation
     * - Monotonic stack
     * 
     * TIME: O(1) push/pop
     * SPACE: O(n)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 20. Valid Parentheses (Easy)
     *    Check if brackets are balanced
     *    → Stack to match pairs
     * 
     * 2️⃣ LC 739. Daily Temperatures (Medium)
     *    Find days until warmer temperature
     *    → Monotonic decreasing stack
     * 
     * 3️⃣ LC 84. Largest Rectangle in Histogram (Hard)
     *    Find largest rectangle area
     *    → Monotonic increasing stack
     * 
     * BONUS PRACTICE:
     * - LC 155. Min Stack
     * - LC 232. Implement Queue using Stacks
     * - LC 496. Next Greater Element I
     * - LC 394. Decode String
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 15. LINKED LIST                                                          │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Node-based data structure
     * 
     * WHEN TO USE:
     * - Dynamic size
     * - Frequent insertions/deletions
     * - No random access needed
     * 
     * TIME: O(1) insert/delete at known position, O(n) search
     * SPACE: O(1) for pointer manipulation
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 206. Reverse Linked List (Easy)
     *    Reverse list in-place
     *    → Iterative or recursive
     * 
     * 2️⃣ LC 2. Add Two Numbers (Medium)
     *    Add numbers represented as linked lists
     *    → Simulate addition with carry
     * 
     * 3️⃣ LC 25. Reverse Nodes in k-Group (Hard)
     *    Reverse every k nodes
     *    → Reverse sublist repeatedly
     * 
     * BONUS PRACTICE:
     * - LC 21. Merge Two Sorted Lists
     * - LC 19. Remove Nth Node From End
     * - LC 143. Reorder List
     * - LC 138. Copy List with Random Pointer
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 16. UNION-FIND (DISJOINT SET)                                            │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Track connected components with union/find operations
     * 
     * WHEN TO USE:
     * - Dynamic connectivity
     * - Detect cycles in undirected graph
     * - Connected components
     * - Kruskal's MST
     * 
     * TIME: O(α(n)) ≈ O(1) with path compression
     * SPACE: O(n)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 547. Number of Provinces (Medium)
     *    Count connected components
     *    → Union nodes in same province
     * 
     * 2️⃣ LC 200. Number of Islands (Medium)
     *    Count islands (can also use DFS)
     *    → Union adjacent land cells
     * 
     * 3️⃣ LC 1319. Number of Operations to Make Network Connected (Medium)
     *    Check if network is connectable
     *    → Count components and extra edges
     * 
     * BONUS PRACTICE:
     * - LC 684. Redundant Connection
     * - LC 721. Accounts Merge
     * - LC 128. Longest Consecutive Sequence
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 17. TOPOLOGICAL SORT                                                     │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Linear ordering of vertices in DAG (Directed Acyclic Graph)
     * - Kahn's algorithm (BFS)
     * - DFS-based
     * 
     * WHEN TO USE:
     * - Dependencies (prerequisites)
     * - Task scheduling
     * - Build systems
     * 
     * TIME: O(V + E)
     * SPACE: O(V)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 207. Course Schedule (Medium)
     *    Can finish all courses?
     *    → Detect cycle in directed graph
     * 
     * 2️⃣ LC 210. Course Schedule II (Medium)
     *    Return valid order to finish courses
     *    → Topological sort
     * 
     * 3️⃣ LC 269. Alien Dictionary (Hard) [Premium]
     *    Find order of alien alphabet
     *    → Build graph from string comparisons
     * 
     * BONUS PRACTICE:
     * - LC 310. Minimum Height Trees
     * - LC 1203. Sort Items by Groups Respecting Dependencies
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 18. BIT MANIPULATION                                                     │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Operate directly on bits
     * 
     * WHEN TO USE:
     * - Optimize space/time
     * - Set operations
     * - Single number problems
     * 
     * TIME: O(1) per operation
     * SPACE: O(1)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 136. Single Number (Easy)
     *    Find number that appears once
     *    → XOR all numbers (duplicates cancel)
     * 
     * 2️⃣ LC 191. Number of 1 Bits (Easy)
     *    Count set bits
     *    → n & (n-1) removes rightmost 1
     * 
     * 3️⃣ LC 137. Single Number II (Medium)
     *    Find number appearing once (others appear 3 times)
     *    → Bit manipulation with states
     * 
     * BONUS PRACTICE:
     * - LC 190. Reverse Bits
     * - LC 231. Power of Two
     * - LC 371. Sum of Two Integers (no +/-)
     * - LC 338. Counting Bits
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 19. MATH                                                                 │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Mathematical properties and formulas
     * 
     * WHEN TO USE:
     * - Number theory
     * - Geometry
     * - Probability
     * - Combinatorics
     * 
     * TIME: Varies
     * SPACE: O(1) typically
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 7. Reverse Integer (Medium)
     *    Reverse digits of integer
     *    → Handle overflow
     * 
     * 2️⃣ LC 202. Happy Number (Easy)
     *    Determine if number is happy
     *    → Cycle detection or HashSet
     * 
     * 3️⃣ LC 50. Pow(x, n) (Medium)
     *    Implement power function
     *    → Fast exponentiation
     * 
     * BONUS PRACTICE:
     * - LC 9. Palindrome Number
     * - LC 66. Plus One
     * - LC 172. Factorial Trailing Zeroes
     * - LC 168. Excel Sheet Column Title
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 20. INTERVALS / MERGE INTERVALS                                          │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * DESCRIPTION:
     * Handle overlapping/merging intervals
     * 
     * WHEN TO USE:
     * - Scheduling problems
     * - Calendar conflicts
     * - Meeting rooms
     * 
     * TIME: O(n log n) with sorting
     * SPACE: O(n)
     * 
     * REPRESENTATIVE PROBLEMS:
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * 1️⃣ LC 56. Merge Intervals (Medium)
     *    Merge overlapping intervals
     *    → Sort by start, merge if overlap
     * 
     * 2️⃣ LC 57. Insert Interval (Medium)
     *    Insert new interval and merge
     *    → Three parts: before, overlap, after
     * 
     * 3️⃣ LC 253. Meeting Rooms II (Medium) [Premium]
     *    Minimum meeting rooms needed
     *    → Heap or chronological ordering
     * 
     * BONUS PRACTICE:
     * - LC 252. Meeting Rooms I
     * - LC 435. Non-overlapping Intervals
     * - LC 986. Interval List Intersections
     */
    
    
    // ═══════════════════════════════════════════════════════════════════════════
    // PART 2: ADVANCED PATTERNS (20-60)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 21. TRIE (PREFIX TREE)                                                   │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 208. Implement Trie (Medium)
     * 2️⃣ LC 211. Design Add and Search Words Data Structure (Medium)
     * 3️⃣ LC 212. Word Search II (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 22. SEGMENT TREE                                                         │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 307. Range Sum Query - Mutable (Medium)
     * 2️⃣ LC 218. The Skyline Problem (Hard)
     * 3️⃣ LC 699. Falling Squares (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 23. BINARY INDEXED TREE (FENWICK TREE)                                   │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 307. Range Sum Query - Mutable (Medium)
     * 2️⃣ LC 315. Count of Smaller Numbers After Self (Hard)
     * 3️⃣ LC 493. Reverse Pairs (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 24. MONOTONIC STACK                                                      │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 496. Next Greater Element I (Easy)
     * 2️⃣ LC 739. Daily Temperatures (Medium)
     * 3️⃣ LC 84. Largest Rectangle in Histogram (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 25. MONOTONIC QUEUE                                                      │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 239. Sliding Window Maximum (Hard)
     * 2️⃣ LC 862. Shortest Subarray with Sum at Least K (Hard)
     * 3️⃣ LC 1425. Constrained Subsequence Sum (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 26. GRAPH THEORY                                                         │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 133. Clone Graph (Medium)
     * 2️⃣ LC 785. Is Graph Bipartite? (Medium)
     * 3️⃣ LC 1135. Connecting Cities With Minimum Cost (Medium) [Premium]
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 27. SHORTEST PATH (DIJKSTRA, BELLMAN-FORD)                               │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 743. Network Delay Time (Medium)
     * 2️⃣ LC 787. Cheapest Flights Within K Stops (Medium)
     * 3️⃣ LC 1631. Path With Minimum Effort (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 28. MINIMUM SPANNING TREE (KRUSKAL, PRIM)                                │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 1135. Connecting Cities With Minimum Cost (Medium) [Premium]
     * 2️⃣ LC 1584. Min Cost to Connect All Points (Medium)
     * 3️⃣ LC 1489. Find Critical and Pseudo-Critical Edges (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 29. STRING MATCHING (KMP, RABIN-KARP)                                    │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 28. Find the Index of the First Occurrence (Easy)
     * 2️⃣ LC 214. Shortest Palindrome (Hard)
     * 3️⃣ LC 1392. Longest Happy Prefix (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 30. ROLLING HASH                                                         │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 187. Repeated DNA Sequences (Medium)
     * 2️⃣ LC 1044. Longest Duplicate Substring (Hard)
     * 3️⃣ LC 718. Maximum Length of Repeated Subarray (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 31. GAME THEORY                                                          │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 292. Nim Game (Easy)
     * 2️⃣ LC 486. Predict the Winner (Medium)
     * 3️⃣ LC 877. Stone Game (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 32. GEOMETRY                                                             │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 892. Surface Area of 3D Shapes (Easy)
     * 2️⃣ LC 593. Valid Square (Medium)
     * 3️⃣ LC 587. Erect the Fence (Hard) - Convex Hull
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 33. NUMBER THEORY                                                        │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 204. Count Primes (Medium) - Sieve of Eratosthenes
     * 2️⃣ LC 279. Perfect Squares (Medium)
     * 3️⃣ LC 356. Line Reflection (Medium) [Premium]
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 34. COMBINATORICS                                                        │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 62. Unique Paths (Medium)
     * 2️⃣ LC 118. Pascal's Triangle (Easy)
     * 3️⃣ LC 1735. Count Ways to Make Array With Product (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 35. DIVIDE AND CONQUER                                                   │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 53. Maximum Subarray (Medium) - Can also use Kadane's
     * 2️⃣ LC 215. Kth Largest Element (Medium) - QuickSelect
     * 3️⃣ LC 4. Median of Two Sorted Arrays (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 36. DESIGN / DATA STRUCTURE DESIGN                                       │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 155. Min Stack (Medium)
     * 2️⃣ LC 146. LRU Cache (Medium)
     * 3️⃣ LC 460. LFU Cache (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 37. SIMULATION                                                           │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 54. Spiral Matrix (Medium)
     * 2️⃣ LC 289. Game of Life (Medium)
     * 3️⃣ LC 489. Robot Room Cleaner (Hard) [Premium]
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 38. MATRIX                                                               │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 48. Rotate Image (Medium)
     * 2️⃣ LC 73. Set Matrix Zeroes (Medium)
     * 3️⃣ LC 240. Search a 2D Matrix II (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 39. ENUMERATION                                                          │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 412. Fizz Buzz (Easy)
     * 2️⃣ LC 957. Prison Cells After N Days (Medium)
     * 3️⃣ LC 1622. Fancy Sequence (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 40. COUNTING                                                             │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 169. Majority Element (Easy)
     * 2️⃣ LC 347. Top K Frequent Elements (Medium)
     * 3️⃣ LC 1452. People Whose List of Favorite Companies Is Not Subset (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 41. ORDERED SET / MAP                                                    │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 220. Contains Duplicate III (Hard)
     * 2️⃣ LC 352. Data Stream as Disjoint Intervals (Hard)
     * 3️⃣ LC 732. My Calendar III (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 42. DOUBLY-LINKED LIST                                                   │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 146. LRU Cache (Medium)
     * 2️⃣ LC 460. LFU Cache (Hard)
     * 3️⃣ LC 432. All O`one Data Structure (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 43. MEMOIZATION (TOP-DOWN DP)                                            │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 70. Climbing Stairs (Easy)
     * 2️⃣ LC 139. Word Break (Medium)
     * 3️⃣ LC 329. Longest Increasing Path in a Matrix (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 44. RECURSION                                                            │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 509. Fibonacci Number (Easy)
     * 2️⃣ LC 206. Reverse Linked List (Easy)
     * 3️⃣ LC 779. K-th Symbol in Grammar (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 45. HASH FUNCTION                                                        │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 535. Encode and Decode TinyURL (Medium)
     * 2️⃣ LC 706. Design HashMap (Easy)
     * 3️⃣ LC 187. Repeated DNA Sequences (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 46. BINARY SEARCH TREE                                                   │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 98. Validate Binary Search Tree (Medium)
     * 2️⃣ LC 230. Kth Smallest Element in a BST (Medium)
     * 3️⃣ LC 450. Delete Node in a BST (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 47. BINARY TREE (GENERAL)                                                │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 94. Binary Tree Inorder Traversal (Easy)
     * 2️⃣ LC 105. Construct Binary Tree from Preorder and Inorder (Medium)
     * 3️⃣ LC 297. Serialize and Deserialize Binary Tree (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 48. MERGE SORT                                                           │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 148. Sort List (Medium)
     * 2️⃣ LC 23. Merge k Sorted Lists (Hard)
     * 3️⃣ LC 493. Reverse Pairs (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 49. QUICKSELECT                                                          │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 215. Kth Largest Element in an Array (Medium)
     * 2️⃣ LC 973. K Closest Points to Origin (Medium)
     * 3️⃣ LC 324. Wiggle Sort II (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 50. BUCKET SORT                                                          │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 347. Top K Frequent Elements (Medium)
     * 2️⃣ LC 451. Sort Characters By Frequency (Medium)
     * 3️⃣ LC 164. Maximum Gap (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 51. RADIX SORT                                                           │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 164. Maximum Gap (Hard)
     * 2️⃣ LC 1649. Create Sorted Array through Instructions (Hard)
     * 3️⃣ Custom implementation problems
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 52. COUNTING SORT                                                        │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 912. Sort an Array (Medium)
     * 2️⃣ LC 1051. Height Checker (Easy)
     * 3️⃣ LC 1365. How Many Numbers Are Smaller Than Current (Easy)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 53. RESERVOIR SAMPLING                                                   │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 382. Linked List Random Node (Medium)
     * 2️⃣ LC 398. Random Pick Index (Medium)
     * 3️⃣ LC 528. Random Pick with Weight (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 54. REJECTION SAMPLING                                                   │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 470. Implement Rand10() Using Rand7() (Medium)
     * 2️⃣ LC 478. Generate Random Point in a Circle (Medium)
     * 3️⃣ LC 497. Random Point in Non-overlapping Rectangles (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 55. RANDOMIZED ALGORITHMS                                                │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 384. Shuffle an Array (Medium)
     * 2️⃣ LC 380. Insert Delete GetRandom O(1) (Medium)
     * 3️⃣ LC 381. Insert Delete GetRandom O(1) - Duplicates (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 56. PROBABILITY AND STATISTICS                                           │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 519. Random Flip Matrix (Medium)
     * 2️⃣ LC 528. Random Pick with Weight (Medium)
     * 3️⃣ LC 710. Random Pick with Blacklist (Hard)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 57. CONCURRENCY                                                          │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 1114. Print in Order (Easy)
     * 2️⃣ LC 1115. Print FooBar Alternately (Medium)
     * 3️⃣ LC 1195. Fizz Buzz Multithreaded (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 58. BRAINTEASER                                                          │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 292. Nim Game (Easy)
     * 2️⃣ LC 319. Bulb Switcher (Medium)
     * 3️⃣ LC 1033. Moving Stones Until Consecutive (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 59. ITERATOR / GENERATOR                                                 │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 251. Flatten 2D Vector (Medium) [Premium]
     * 2️⃣ LC 281. Zigzag Iterator (Medium) [Premium]
     * 3️⃣ LC 341. Flatten Nested List Iterator (Medium)
     */
    
    /**
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │ 60. STRONGLY CONNECTED COMPONENT (TARJAN, KOSARAJU)                      │
     * └──────────────────────────────────────────────────────────────────────────┘
     * 
     * REPRESENTATIVE PROBLEMS:
     * 1️⃣ LC 1192. Critical Connections in a Network (Hard)
     * 2️⃣ LC 1489. Find Critical and Pseudo-Critical Edges (Hard)
     * 3️⃣ LC 1568. Minimum Number of Days to Disconnect Island (Hard)
     */
    
    
    // ═══════════════════════════════════════════════════════════════════════════
    // STUDY PLAN SUGGESTIONS
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * BEGINNER (Month 1-2):
     * - Focus: Core Patterns 1-10
     * - Do: 5-7 problems per pattern
     * - Goal: Understand pattern recognition
     * 
     * INTERMEDIATE (Month 3-4):
     * - Focus: Core Patterns 11-20
     * - Do: 7-10 problems per pattern
     * - Goal: Apply patterns to medium/hard problems
     * 
     * ADVANCED (Month 5-6):
     * - Focus: Advanced Patterns 21-40
     * - Do: 3-5 problems per pattern
     * - Goal: Handle rare patterns, optimize solutions
     * 
     * EXPERT (Month 6+):
     * - Focus: Patterns 41-60 + Contest problems
     * - Do: Weekly contests
     * - Goal: Speed + accuracy under time pressure
     */
    
    /**
     * RECOMMENDED RESOURCES:
     * 
     * 1. Grokking the Coding Interview
     *    - Pattern-based learning
     *    - 20 patterns with examples
     * 
     * 2. LeetCode Explore Cards
     *    - Structured learning paths
     *    - Topic-specific problems
     * 
     * 3. NeetCode.io
     *    - 150 curated problems
     *    - Video explanations
     * 
     * 4. AlgoExpert
     *    - 160 problems
     *    - Video + text solutions
     * 
     * 5. Books:
     *    - Cracking the Coding Interview
     *    - Elements of Programming Interviews
     */
}
