package com.practice.leetcode.graph;

import java.util.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                        GRAPH TECHNIQUE GUIDE                              ║
 * ║                          (Kỹ thuật Đồ Thị)                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * PHẦN 0: DIRECTED vs UNDIRECTED GRAPH
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ UNDIRECTED GRAPH (Đồ thị vô hướng)                                          │
 * │                                                                             │
 * │   A ── B     Cạnh A-B có thể đi 2 chiều: A→B và B→A                         │
 * │   │    │                                                                    │
 * │   C ── D     VÍ DỤ THỰC TẾ:                                                 │
 * │              • Bạn bè Facebook (A kết bạn B = B kết bạn A)                  │
 * │              • Đường phố 2 chiều                                            │
 * │              • Mạng điện, mạng nước                                         │
 * │                                                                             │
 * │ CODE: Thêm cạnh 2 chiều                                                     │
 * │   graph.get(u).add(v);                                                      │
 * │   graph.get(v).add(u);  // ← Thêm chiều ngược                               │
 * │                                                                             │
 * │ TỪ KHÓA NHẬN DIỆN:                                                          │
 * │   • "connected", "connection", "edge"                                       │
 * │   • "friend", "neighbor", "adjacent"                                        │
 * │   • Không nhắc đến "direction" hay "from...to"                              │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ DIRECTED GRAPH (Đồ thị có hướng)                                            │
 * │                                                                             │
 * │   A ──→ B    Cạnh A→B chỉ đi được 1 chiều                                   │
 * │   ↑     ↓                                                                   │
 * │   C ←── D    VÍ DỤ THỰC TẾ:                                                 │
 * │              • Follow Twitter (A follow B ≠ B follow A)                     │
 * │              • Prerequisites (học A trước mới học B được)                   │
 * │              • Đường 1 chiều, pipeline, workflow                            │
 * │                                                                             │
 * │ CODE: Chỉ thêm 1 chiều                                                      │
 * │   graph.get(from).add(to);  // Chỉ from → to                                │
 * │                                                                             │
 * │ TỪ KHÓA NHẬN DIỆN:                                                          │
 * │   • "directed", "from...to", "prerequisite"                                 │
 * │   • "dependency", "order", "sequence"                                       │
 * │   • "follow", "point to", "lead to"                                         │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ BẢNG SO SÁNH                                                                │
 * ├───────────────────┬─────────────────────┬───────────────────────────────────┤
 * │ Tiêu chí          │ Undirected          │ Directed                          │
 * ├───────────────────┼─────────────────────┼───────────────────────────────────┤
 * │ Cycle detection   │ DFS + parent track  │ DFS + 3 states (0/1/2)            │
 * │ Build graph       │ Thêm 2 cạnh         │ Thêm 1 cạnh                       │
 * │ Topological Sort  │ ❌ Không áp dụng    │ ✅ Áp dụng (DAG)                  │
 * │ In-degree         │ = Out-degree        │ Khác nhau                         │
 * │ Connected check   │ BFS/DFS từ 1 node   │ Cần check strongly connected      │
 * └───────────────────┴─────────────────────┴───────────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * PHẦN 0.5: TỪ KHÓA & TRICKS NHẬN DIỆN BÀI GRAPH
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ 🔍 KEYWORDS → ALGORITHM MAPPING                                             │
 * ├─────────────────────────────────────────────────────────────────────────────┤
 * │                                                                             │
 * │ "Shortest path" + Unweighted     → BFS                                      │
 * │ "Shortest path" + Weighted       → Dijkstra                                 │
 * │ "Shortest path" + Negative edges → Bellman-Ford                             │
 * │                                                                             │
 * │ "Number of islands/regions"      → DFS Flood Fill                           │
 * │ "Connected components"           → DFS/BFS hoặc Union-Find                  │
 * │ "Are A and B connected?"         → Union-Find                               │
 * │                                                                             │
 * │ "Course schedule/Prerequisites"  → Topological Sort                         │
 * │ "Task ordering/Build order"      → Topological Sort                         │
 * │ "Detect cycle" (directed)        → DFS với 3 states                         │
 * │ "Detect cycle" (undirected)      → Union-Find hoặc DFS+parent               │
 * │                                                                             │
 * │ "Minimum spanning tree"          → Kruskal (Union-Find) / Prim              │
 * │ "Bipartite / 2-coloring"         → BFS/DFS với color                        │
 * │ "Level by level"                 → BFS                                      │
 * │                                                                             │
 * │ "Town Judge/Celebrity"           → In-Degree/Out-Degree Counting            │
 * │ "Everyone trusts/knows X"        → In-Degree/Out-Degree Counting            │
 * │                                                                             │
 * │ "Grid traversal/maze"            → BFS (shortest) / DFS (any path)          │
 * │ "Rotting oranges/fire spread"    → Multi-source BFS                         │
 * │ "Word ladder/transformation"     → BFS                                      │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ 🎯 TRICKS PHỔ BIẾN                                                          │
 * ├─────────────────────────────────────────────────────────────────────────────┤
 * │                                                                             │
 * │ TRICK 1: IMPLICIT GRAPH - Không có edges[] cho sẵn                          │
 * │   • Grid → 4/8 hướng là neighbors                                           │
 * │   • Numbers → Các số có thể transform thành nhau                            │
 * │   • Strings → Thay 1 ký tự = 1 edge                                         │
 * │                                                                             │
 * │ TRICK 2: MULTI-SOURCE BFS - Nhiều điểm bắt đầu                              │
 * │   • Thêm TẤT CẢ sources vào queue trước khi BFS                             │
 * │   • VD: Rotting oranges, walls and gates                                    │
 * │                                                                             │
 * │ TRICK 3: REVERSE GRAPH - Đảo ngược hướng edges                              │
 * │   • "Nodes có thể reach target" → Đảo graph, BFS từ target                  │
 * │   • VD: Pacific Atlantic Water Flow                                         │
 * │                                                                             │
 * │ TRICK 4: VIRTUAL/DUMMY NODE                                                 │
 * │   • Tạo node ảo nối với nhiều nodes để đơn giản hóa                         │
 * │   • VD: Tạo super-source nối tất cả starting points                         │
 * │                                                                             │
 * │ TRICK 5: STATE COMPRESSION                                                  │
 * │   • Thêm dimension vào visited: visited[node][state]                        │
 * │   • VD: dist[node][keysCollected] trong bài keys and rooms                  │
 * │                                                                             │
 * │ TRICK 6: 0-1 BFS (Deque)                                                    │
 * │   • Khi weights chỉ có 0 và 1                                               │
 * │   • Weight 0 → addFirst, Weight 1 → addLast                                 │
 * │                                                                             │
 * │ TRICK 7: BIDIRECTIONAL BFS                                                  │
 * │   • BFS từ cả start và end, gặp nhau ở giữa                                 │
 * │   • Giảm time từ O(b^d) xuống O(b^(d/2))                                    │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ ⚠️  COMMON PITFALLS - LỖI HAY GẶP                                           │
 * ├─────────────────────────────────────────────────────────────────────────────┤
 * │                                                                             │
 * │ ❌ BFS: Đánh dấu visited SAU khi poll                                       │
 * │ ✅ BFS: Đánh dấu visited TRƯỚC khi offer (tránh duplicate)                  │
 * │                                                                             │
 * │ ❌ Undirected: Quên thêm cạnh ngược                                         │
 * │ ✅ Undirected: Luôn thêm cả u→v và v→u                                      │
 * │                                                                             │
 * │ ❌ Cycle (directed): Dùng simple visited boolean                            │
 * │ ✅ Cycle (directed): Dùng 3 states (0=white, 1=gray, 2=black)               │
 * │                                                                             │
 * │ ❌ Dijkstra: Dùng với negative weights                                      │
 * │ ✅ Dijkstra: Chỉ dùng với non-negative weights                              │
 * │                                                                             │
 * │ ❌ Grid: Không check bounds trước khi access                                │
 * │ ✅ Grid: if (r >= 0 && r < m && c >= 0 && c < n)                            │
 * │                                                                             │
 * │ ❌ Graph: Giả định nodes là 0 đến n-1 liên tục                              │
 * │ ✅ Graph: Dùng Map khi nodes có thể không liên tục                          │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * PHẦN 1: CÁCH BIỂU DIỄN GRAPH
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ 1. ADJACENCY LIST (Danh sách kề) - PHỔ BIẾN NHẤT                            │
 * │    Map<Node, List<Node>> hoặc List<List<Integer>>                           │
 * │    ✅ Tiết kiệm space O(V+E), duyệt neighbor nhanh                          │
 * │    ❌ Check edge tồn tại O(degree)                                          │
 * │                                                                             │
 * │ 2. ADJACENCY MATRIX (Ma trận kề)                                            │
 * │    int[][] matrix - matrix[i][j] = 1 nếu có cạnh i→j                        │
 * │    ✅ Check edge O(1), đơn giản                                             │
 * │    ❌ Space O(V²), không tốt cho sparse graph                               │
 * │                                                                             │
 * │ 3. EDGE LIST (Danh sách cạnh)                                               │
 * │    List<int[]> edges - mỗi phần tử [from, to, weight]                       │
 * │    ✅ Tốt cho Kruskal MST, input format đơn giản                            │
 * │    ❌ Không tốt cho traversal                                               │
 * │                                                                             │
 * │ 4. IMPLICIT GRAPH (Grid/Matrix)                                             │
 * │    char[][] grid - di chuyển 4/8 hướng                                      │
 * │    ✅ Không cần build graph riêng                                           │
 * │    ❌ Chỉ áp dụng cho bài grid                                              │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * PHẦN 2: CÁC THUẬT TOÁN CHÍNH
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * 1. DFS (Depth-First Search) - Đi sâu nhất trước
 * 2. BFS (Breadth-First Search) - Đi theo lớp, tìm đường ngắn nhất
 * 3. TOPOLOGICAL SORT - Sắp xếp DAG (Kahn's hoặc DFS)
 * 4. UNION-FIND - Nhóm các thành phần liên thông
 * 5. DIJKSTRA - Đường đi ngắn nhất có trọng số dương
 * 6. BELLMAN-FORD - Đường ngắn nhất, cho phép trọng số âm
 * 7. FLOYD-WARSHALL - All-pairs shortest paths
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
  // TEMPLATE 0: IN-DEGREE / OUT-DEGREE COUNTING
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * IN-DEGREE / OUT-DEGREE - Đếm số cạnh vào/ra của mỗi node
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ KHÁI NIỆM                                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │   IN-DEGREE (bậc vào): Số cạnh ĐI VÀO node                                  │
   * │   OUT-DEGREE (bậc ra): Số cạnh ĐI RA khỏi node                              │
   * │                                                                             │
   * │   VÍ DỤ: edge [A → B]                                                       │
   * │   • A: out-degree++ (cạnh đi RA khỏi A)                                     │
   * │   • B: in-degree++  (cạnh đi VÀO B)                                         │
   * │                                                                             │
   * │         ┌───────────┐                                                       │
   * │    A ───│──────────►│─── B                                                  │
   * │         └───────────┘                                                       │
   * │       out-degree=1      in-degree=1                                         │
   * │                                                                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ KHI NÀO DÙNG?                                                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ✅ Find the Town Judge (LC 997)                                             │
   * │    → Judge: in-degree = n-1, out-degree = 0                                 │
   * │                                                                             │
   * │ ✅ Find the Celebrity (LC 277)                                              │
   * │    → Celebrity: được tất cả biết, không biết ai                             │
   * │                                                                             │
   * │ ✅ Topological Sort (Kahn's Algorithm)                                      │
   * │    → Bắt đầu từ nodes có in-degree = 0                                      │
   * │                                                                             │
   * │ ✅ Course Schedule problems                                                 │
   * │    → Đếm prerequisites của mỗi course                                       │
   * │                                                                             │
   * │ ✅ Find Minimum Height Trees (LC 310)                                       │
   * │    → Loại bỏ leaf nodes (degree = 1)                                        │
   * │                                                                             │
   * │ TỪ KHÓA NHẬN DIỆN:                                                          │
   * │   • "trust", "follow", "know", "vote for"                                   │
   * │   • "prerequisite", "dependency"                                            │
   * │   • "everyone trusts/knows", "nobody trusts"                                │
   * │   • Bài có quan hệ 1 chiều, cần đếm ai ảnh hưởng ai                         │
   * │                                                                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 3 CÁCH IMPLEMENT                                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ CÁCH 1: Dùng 2 mảng riêng biệt                                              │
   * │   int[] inDegree = new int[n];                                              │
   * │   int[] outDegree = new int[n];                                             │
   * │   → Rõ ràng, dễ hiểu                                                        │
   * │                                                                             │
   * │ CÁCH 2: Dùng 1 mảng score = inDegree - outDegree                            │
   * │   int[] score = new int[n];                                                 │
   * │   score[from]--; score[to]++;                                               │
   * │   → Tối ưu space, hay dùng cho Town Judge                                   │
   * │                                                                             │
   * │ CÁCH 3: Dùng khi cần cả neighbor list                                       │
   * │   Map<Integer, Set<Integer>> incoming;                                      │
   * │   Map<Integer, Set<Integer>> outgoing;                                      │
   * │   → Khi cần biết cụ thể ai vào/ra                                           │
   * │                                                                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ─────────────────────────────────────────────────────────────────────────────
  // CÁCH 1: Đếm in-degree và out-degree riêng biệt
  // ─────────────────────────────────────────────────────────────────────────────
  /**
   * Đếm in-degree và out-degree cho mỗi node
   *
   * @param n     số nodes (đánh số 1 đến n)
   * @param edges danh sách cạnh [from, to]
   * @return int[2][] với [0] = inDegree, [1] = outDegree
   *
   *         VÍ DỤ: n=3, edges=[[1,3],[2,3]]
   *         - Node 1: out=1 (tin 3), in=0
   *         - Node 2: out=1 (tin 3), in=0
   *         - Node 3: out=0, in=2 (được 1,2 tin)
   */
  int[][] countDegrees(int n, int[][] edges) {
    int[] inDegree = new int[n + 1];  // +1 vì nodes đánh số từ 1
    int[] outDegree = new int[n + 1];

    for (int[] edge : edges) {
      int from = edge[0];
      int to = edge[1];
      outDegree[from]++;  // Cạnh đi RA khỏi from
      inDegree[to]++;     // Cạnh đi VÀO to
    }

    return new int[][] { inDegree, outDegree };
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // CÁCH 2: Dùng 1 mảng score (tối ưu cho Town Judge pattern)
  // ─────────────────────────────────────────────────────────────────────────────
  /**
   * Tính trust score = inDegree - outDegree
   *
   * Ý NGHĨA:
   * - score > 0: được nhiều người tin hơn là tin người khác
   * - score < 0: tin người khác nhiều hơn được tin
   * - score = n-1: Town Judge! (được n-1 người tin, không tin ai)
   *
   * VÍ DỤ: n=3, trust=[[1,3],[2,3]]
   * - Score[1] = 0 - 1 = -1 (tin 1 người, không ai tin)
   * - Score[2] = 0 - 1 = -1 (tin 1 người, không ai tin)
   * - Score[3] = 2 - 0 = +2 = n-1 → JUDGE!
   */
  int[] calculateTrustScore(int n, int[][] edges) {
    int[] score = new int[n + 1];  // score[i] = inDegree[i] - outDegree[i]

    for (int[] edge : edges) {
      int from = edge[0];  // from tin to
      int to = edge[1];
      score[from]--;  // from tin người khác → không phải judge
      score[to]++;    // to được tin
    }

    return score;
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // BÀI MẪU: Find the Town Judge (LeetCode 997)
  // ─────────────────────────────────────────────────────────────────────────────
  /**
   * Tìm Town Judge trong n người
   *
   * ĐIỀU KIỆN JUDGE:
   * 1. Không tin ai (out-degree = 0)
   * 2. Tất cả n-1 người khác đều tin (in-degree = n-1)
   * 3. Chỉ có đúng 1 người thỏa mãn
   *
   * TRICK: score = in - out = (n-1) - 0 = n-1
   *
   * @param n     số người (1 đến n)
   * @param trust trust[i] = [a, b] nghĩa là a tin b
   * @return label của judge, hoặc -1 nếu không có
   *
   *         VÍ DỤ 1: n=3, trust=[[1,3],[2,3]] → return 3
   *         VÍ DỤ 2: n=3, trust=[[1,3],[2,3],[3,1]] → return -1
   *         VÍ DỤ 3: n=1, trust=[] → return 1
   */
  int findJudge(int n, int[][] trust) {
    // Dùng score = inDegree - outDegree
    int[] score = new int[n + 1];

    for (int[] t : trust) {
      int a = t[0];  // a tin b
      int b = t[1];
      score[a]--;    // a tin người khác → không phải judge
      score[b]++;    // b được tin → gần hơn với judge
    }

    // Judge có score = n - 1 (được n-1 người tin, không tin ai)
    for (int i = 1; i <= n; i++) {
      if (score[i] == n - 1) {
        return i;
      }
    }

    return -1;  // Không tìm thấy judge
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // CÁCH 3: Đếm degree với Map (khi cần biết ai vào/ra cụ thể)
  // ─────────────────────────────────────────────────────────────────────────────
  /**
   * Lưu chi tiết các nodes đi vào và đi ra
   * Dùng khi cần biết CỤ THỂ ai tin ai, không chỉ đếm số lượng
   *
   * VÍ DỤ ỨNG DỤNG:
   * - Tìm tất cả người tin node X
   * - Tìm tất cả người mà node X tin
   * - Check nếu A tin B hay không trong O(1)
   */
  Map<Integer, Set<Integer>> buildIncomingMap(int n, int[][] edges) {
    Map<Integer, Set<Integer>> incoming = new HashMap<>();

    for (int i = 1; i <= n; i++) {
      incoming.put(i, new HashSet<>());
    }

    for (int[] edge : edges) {
      int from = edge[0];
      int to = edge[1];
      incoming.get(to).add(from);  // from đi vào to
    }

    return incoming;
  }

  Map<Integer, Set<Integer>> buildOutgoingMap(int n, int[][] edges) {
    Map<Integer, Set<Integer>> outgoing = new HashMap<>();

    for (int i = 1; i <= n; i++) {
      outgoing.put(i, new HashSet<>());
    }

    for (int[] edge : edges) {
      int from = edge[0];
      int to = edge[1];
      outgoing.get(from).add(to);  // from đi đến to
    }

    return outgoing;
  }

  /**
   * BẢNG TÓM TẮT IN-DEGREE/OUT-DEGREE
   *
   * ┌──────────────────────┬──────────────────────────────────────────────────────┐
   * │ Pattern              │ Điều kiện cần tìm                                    │
   * ├──────────────────────┼──────────────────────────────────────────────────────┤
   * │ Town Judge           │ in = n-1, out = 0 → score = n-1                      │
   * │ Celebrity            │ in = n-1, out = 0 (tương tự Judge)                   │
   * │ Topological Sort     │ Bắt đầu từ nodes có in = 0                           │
   * │ Leaf Nodes           │ degree = 1 (tổng in + out = 1)                       │
   * │ Source/Sink          │ Source: in=0, out>0 | Sink: in>0, out=0              │
   * │ Strongly Connected   │ Mọi node đều có in>0 và out>0                        │
   * └──────────────────────┴──────────────────────────────────────────────────────┘
   *
   * TIME COMPLEXITY: O(E) với E = số edges
   * SPACE COMPLEXITY: O(N) với N = số nodes
   */

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
