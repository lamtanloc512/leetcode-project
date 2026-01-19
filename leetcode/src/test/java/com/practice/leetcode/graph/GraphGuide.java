package com.practice.leetcode.graph;

import java.util.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ GRAPH TECHNIQUE GUIDE ║
 * ║ (Kỹ thuật Đồ Thị) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ CÁCH BIỂU DIỄN GRAPH │
 * │ │
 * │ 1. ADJACENCY LIST (Danh sách kề) - PHỔ BIẾN NHẤT │
 * │ Map<Node, List<Node>> hoặc List<List<Integer>> │
 * │ │
 * │ 2. ADJACENCY MATRIX (Ma trận kề) │
 * │ int[][] matrix - matrix[i][j] = 1 nếu có cạnh i→j │
 * │ │
 * │ 3. EDGE LIST (Danh sách cạnh) │
 * │ List<int[]> edges - mỗi phần tử [from, to, weight] │
 * │ │
 * │ 4. IMPLICIT GRAPH (Grid) │
 * │ char[][] grid - di chuyển 4/8 hướng │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * CÁC THUẬT TOÁN CHÍNH
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * 1. DFS (Depth-First Search) - Đi sâu nhất trước
 * 2. BFS (Breadth-First Search) - Đi theo lớp, tìm đường ngắn nhất
 * 3. TOPOLOGICAL SORT - Sắp xếp DAG
 * 4. UNION-FIND - Nhóm các thành phần liên thông
 * 5. DIJKSTRA - Đường đi ngắn nhất có trọng số
 */
public class GraphGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // SETUP: BUILD GRAPH
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * BƯỚC ĐẦU TIÊN: Xây dựng đồ thị từ input
   *
   * Input thường gặp:
   * - int n: số nodes (0 đến n-1)
   * - int[][] edges: danh sách cạnh [from, to] hoặc [from, to, weight]
   */

  // Build Adjacency List (Undirected)
  Map<Integer, List<Integer>> buildGraphUndirected(int n, int[][] edges) {
    Map<Integer, List<Integer>> graph = new HashMap<>();

    // Khởi tạo tất cả nodes
    for (int i = 0; i < n; i++) {
      graph.put(i, new ArrayList<>());
    }

    // Thêm cạnh (2 chiều)
    for (int[] edge : edges) {
      int u = edge[0], v = edge[1];
      graph.get(u).add(v);
      graph.get(v).add(u); // Undirected
    }

    return graph;
  }

  // Build Adjacency List (Directed)
  Map<Integer, List<Integer>> buildGraphDirected(int n, int[][] edges) {
    Map<Integer, List<Integer>> graph = new HashMap<>();

    for (int i = 0; i < n; i++) {
      graph.put(i, new ArrayList<>());
    }

    for (int[] edge : edges) {
      int from = edge[0], to = edge[1];
      graph.get(from).add(to); // Chỉ 1 chiều
    }

    return graph;
  }

  // Build với List<List<Integer>> (nhanh hơn khi nodes là 0 đến n-1)
  List<List<Integer>> buildGraphList(int n, int[][] edges) {
    List<List<Integer>> graph = new ArrayList<>();

    for (int i = 0; i < n; i++) {
      graph.add(new ArrayList<>());
    }

    for (int[] edge : edges) {
      graph.get(edge[0]).add(edge[1]);
      graph.get(edge[1]).add(edge[0]);
    }

    return graph;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 1: DFS (Depth-First Search)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * DFS - Đi sâu nhất có thể trước khi quay lại
   *
   * Dùng cho:
   * - Tìm connected components
   * - Phát hiện cycle
   * - Path finding (không cần ngắn nhất)
   * - Flood fill (đếm đảo)
   *
   * CÔNG THỨC:
   * 1. Đánh dấu visited
   * 2. Xử lý node hiện tại
   * 3. Đệ quy sang neighbors chưa visit
   */

  // DFS trên Graph
  void dfsGraph(Map<Integer, List<Integer>> graph, int node, Set<Integer> visited) {
    // Đánh dấu visited
    visited.add(node);

    // Xử lý node (tùy bài)
    System.out.println("Visiting: " + node);

    // Đệ quy sang neighbors
    for (int neighbor : graph.get(node)) {
      if (!visited.contains(neighbor)) {
        dfsGraph(graph, neighbor, visited);
      }
    }
  }

  // DFS trên Grid (4 hướng)
  void dfsGrid(char[][] grid, int row, int col, boolean[][] visited) {
    int m = grid.length, n = grid[0].length;

    // Kiểm tra biên và điều kiện
    if (row < 0 || row >= m || col < 0 || col >= n)
      return;
    if (visited[row][col] || grid[row][col] == '0')
      return;

    // Đánh dấu visited
    visited[row][col] = true;

    // 4 hướng: trên, dưới, trái, phải
    int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    for (int[] dir : directions) {
      dfsGrid(grid, row + dir[0], col + dir[1], visited);
    }
  }

  // Đếm Connected Components
  int countComponents(int n, int[][] edges) {
    Map<Integer, List<Integer>> graph = buildGraphUndirected(n, edges);
    Set<Integer> visited = new HashSet<>();
    int count = 0;

    for (int i = 0; i < n; i++) {
      if (!visited.contains(i)) {
        dfsGraph(graph, i, visited);
        count++; // Mỗi DFS = 1 component
      }
    }

    return count;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 2: BFS (Breadth-First Search)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * BFS - Duyệt theo lớp, từ gần đến xa
   *
   * Dùng cho:
   * - Tìm đường ngắn nhất (unweighted)
   * - Level order traversal
   * - Shortest transformation sequence
   *
   * CÔNG THỨC:
   * 1. Khởi tạo Queue, thêm node bắt đầu
   * 2. Đánh dấu visited TRƯỚC khi vào queue
   * 3. Lặp: poll từ queue, thêm neighbors chưa visit
   */

  // BFS cơ bản
  void bfsBasic(Map<Integer, List<Integer>> graph, int start) {
    Set<Integer> visited = new HashSet<>();
    Queue<Integer> queue = new LinkedList<>();

    visited.add(start);
    queue.offer(start);

    while (!queue.isEmpty()) {
      int node = queue.poll();
      System.out.println("Visiting: " + node);

      for (int neighbor : graph.get(node)) {
        if (!visited.contains(neighbor)) {
          visited.add(neighbor); // Đánh dấu TRƯỚC khi offer
          queue.offer(neighbor);
        }
      }
    }
  }

  // BFS tìm đường ngắn nhất
  int shortestPath(Map<Integer, List<Integer>> graph, int start, int end) {
    Set<Integer> visited = new HashSet<>();
    Queue<Integer> queue = new LinkedList<>();

    visited.add(start);
    queue.offer(start);
    int distance = 0;

    while (!queue.isEmpty()) {
      int size = queue.size(); // Số node ở level hiện tại

      for (int i = 0; i < size; i++) {
        int node = queue.poll();

        if (node == end)
          return distance; // Tìm thấy!

        for (int neighbor : graph.get(node)) {
          if (!visited.contains(neighbor)) {
            visited.add(neighbor);
            queue.offer(neighbor);
          }
        }
      }

      distance++; // Sang level mới
    }

    return -1; // Không tìm thấy
  }

  // BFS trên Grid
  int bfsGrid(char[][] grid, int startRow, int startCol, int endRow, int endCol) {
    int m = grid.length, n = grid[0].length;
    boolean[][] visited = new boolean[m][n];
    Queue<int[]> queue = new LinkedList<>();

    visited[startRow][startCol] = true;
    queue.offer(new int[] { startRow, startCol });
    int steps = 0;

    int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    while (!queue.isEmpty()) {
      int size = queue.size();

      for (int i = 0; i < size; i++) {
        int[] curr = queue.poll();

        if (curr[0] == endRow && curr[1] == endCol)
          return steps;

        for (int[] dir : directions) {
          int newRow = curr[0] + dir[0];
          int newCol = curr[1] + dir[1];

          if (newRow >= 0 && newRow < m && newCol >= 0 && newCol < n
              && !visited[newRow][newCol] && grid[newRow][newCol] != '#') {
            visited[newRow][newCol] = true;
            queue.offer(new int[] { newRow, newCol });
          }
        }
      }

      steps++;
    }

    return -1;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 3: TOPOLOGICAL SORT
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * TOPOLOGICAL SORT - Sắp xếp DAG (Directed Acyclic Graph)
   *
   * Dùng cho:
   * - Course Schedule (prerequisites)
   * - Build order
   * - Task scheduling
   *
   * 2 cách implement:
   * 1. KAHN'S ALGORITHM (BFS với indegree)
   * 2. DFS với stack
   */

  // Kahn's Algorithm (BFS)
  List<Integer> topologicalSortKahn(int n, int[][] edges) {
    // Build graph và tính indegree
    List<List<Integer>> graph = new ArrayList<>();
    int[] indegree = new int[n];

    for (int i = 0; i < n; i++)
      graph.add(new ArrayList<>());

    for (int[] edge : edges) {
      int from = edge[0], to = edge[1];
      graph.get(from).add(to);
      indegree[to]++;
    }

    // Thêm nodes có indegree = 0 vào queue
    Queue<Integer> queue = new LinkedList<>();
    for (int i = 0; i < n; i++) {
      if (indegree[i] == 0)
        queue.offer(i);
    }

    List<Integer> result = new ArrayList<>();

    while (!queue.isEmpty()) {
      int node = queue.poll();
      result.add(node);

      for (int neighbor : graph.get(node)) {
        indegree[neighbor]--;
        if (indegree[neighbor] == 0) {
          queue.offer(neighbor);
        }
      }
    }

    // Nếu result.size() < n → có cycle
    return result.size() == n ? result : new ArrayList<>();
  }

  // Phát hiện cycle trong Directed Graph
  boolean hasCycleDirected(int n, int[][] edges) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++)
      graph.add(new ArrayList<>());
    for (int[] edge : edges)
      graph.get(edge[0]).add(edge[1]);

    // 0: unvisited, 1: visiting (in current path), 2: visited
    int[] state = new int[n];

    for (int i = 0; i < n; i++) {
      if (hasCycleDFS(graph, i, state))
        return true;
    }
    return false;
  }

  boolean hasCycleDFS(List<List<Integer>> graph, int node, int[] state) {
    if (state[node] == 1)
      return true; // Đang trong path → cycle!
    if (state[node] == 2)
      return false; // Đã xử lý xong

    state[node] = 1; // Đánh dấu đang visit

    for (int neighbor : graph.get(node)) {
      if (hasCycleDFS(graph, neighbor, state))
        return true;
    }

    state[node] = 2; // Đánh dấu hoàn thành
    return false;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // TEMPLATE 4: UNION-FIND
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * UNION-FIND (Disjoint Set Union)
   *
   * Dùng cho:
   * - Đếm connected components
   * - Kiểm tra 2 nodes có cùng component không
   * - Kruskal's MST
   *
   * 2 operations:
   * - find(x): Tìm root của x
   * - union(x, y): Gộp 2 components
   */

  class UnionFind {
    int[] parent;
    int[] rank;
    int count; // Số components

    UnionFind(int n) {
      parent = new int[n];
      rank = new int[n];
      count = n;
      for (int i = 0; i < n; i++)
        parent[i] = i;
    }

    // Tìm root với path compression
    int find(int x) {
      if (parent[x] != x) {
        parent[x] = find(parent[x]); // Path compression
      }
      return parent[x];
    }

    // Gộp 2 components với union by rank
    boolean union(int x, int y) {
      int rootX = find(x);
      int rootY = find(y);

      if (rootX == rootY)
        return false; // Đã cùng component

      // Union by rank
      if (rank[rootX] < rank[rootY]) {
        parent[rootX] = rootY;
      } else if (rank[rootX] > rank[rootY]) {
        parent[rootY] = rootX;
      } else {
        parent[rootY] = rootX;
        rank[rootX]++;
      }

      count--;
      return true;
    }

    boolean connected(int x, int y) {
      return find(x) == find(y);
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BẢNG LỰA CHỌN ALGORITHM
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌──────────────────────────────────────────────────────────────────────────┐
   * │ Loại bài │ Algorithm │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Connected components │ DFS/BFS hoặc Union-Find │
   * │ Đường ngắn nhất (unweighted) │ BFS │
   * │ Đường ngắn nhất (weighted) │ Dijkstra / Bellman-Ford │
   * │ Phát hiện cycle (undirected) │ DFS với parent / Union-Find │
   * │ Phát hiện cycle (directed) │ DFS với 3 states │
   * │ Sắp xếp phụ thuộc │ Topological Sort (Kahn/DFS) │
   * │ Đảo, vùng liên thông │ DFS Flood Fill │
   * │ Bipartite check │ BFS/DFS coloring │
   * │ MST │ Kruskal (Union-Find) / Prim │
   * └──────────────────────────────────────────────────────────────────────────┘
   *
   * CÁC BÀI THỰC HÀNH
   *
   * EASY/MEDIUM:
   * - Number of Islands (200) → DFS Grid
   * - Clone Graph (133) → DFS + HashMap
   * - Course Schedule (207) → Topological Sort
   * - Pacific Atlantic Water Flow (417) → DFS từ biên
   *
   * MEDIUM:
   * - Course Schedule II (210) → Topo Sort trả thứ tự
   * - Number of Provinces (547) → Union-Find
   * - Rotting Oranges (994) → Multi-source BFS
   * - Word Ladder (127) → BFS transformation
   *
   * HARD:
   * - Word Ladder II (126) → BFS + Backtracking
   * - Alien Dictionary (269) → Build graph + Topo Sort
   */
}
