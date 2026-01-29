package com.practice.leetcode.graph;

import java.util.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                    BÀI TẬP THỰC HÀNH GRAPH CƠ BẢN                         ║
 * ║                   (Từ dễ đến khó, có giải thích đời thực)                 ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * Thứ tự học gợi ý:
 * 1. Bài 1-3: Làm quen với cách duyệt graph (DFS/BFS)
 * 2. Bài 4-5: Áp dụng đếm degree
 * 3. Bài 6-7: Nâng cao hơn
 */
public class GraphPractice {

  // ═══════════════════════════════════════════════════════════════════════════
  // BÀI 1: ĐẾM SỐ NHÓM BẠN (Connected Components)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 🎯 VẤN ĐỀ THỰC TẾ:
   * Trong lớp học có N học sinh (đánh số 0 đến N-1).
   * Một số học sinh là bạn bè với nhau.
   * Hỏi: Có bao nhiêu nhóm bạn riêng biệt trong lớp?
   *
   * 💡 TẠI SAO CẦN GIẢI?
   * - Giáo viên muốn chia lớp thành các nhóm để làm dự án
   * - Cần biết có bao nhiêu "đảo" bạn bè tách biệt
   *
   * 📝 VÍ DỤ:
   * Input: n = 5, friendships = [[0,1], [1,2], [3,4]]
   *
   *   Nhóm 1: 0 -- 1 -- 2
   *   Nhóm 2: 3 -- 4
   *
   * Output: 2 (có 2 nhóm bạn)
   *
   * 🔑 GỢI Ý:
   * - Dùng DFS/BFS để "khám phá" từ 1 học sinh
   * - Đánh dấu những ai đã được khám phá (visited)
   * - Mỗi lần bắt đầu DFS mới = 1 nhóm mới
   *
   * 📌 LEETCODE: 547. Number of Provinces
   * https://leetcode.com/problems/number-of-provinces/
   */
  public int countFriendGroups(int n, int[][] friendships) {
    // Bước 1: Build graph (adjacency list)
    Map<Integer, List<Integer>> graph = buildGraph(n, friendships);
    // Bước 2: Dùng DFS/BFS đếm số lần bắt đầu duyệt mới
    int ans = 0;
    Set<Integer> visited = new HashSet<>();
    for(int i = 0; i < n; i++) {
      if(!visited.contains(i)){
        dfsTemplate(graph, n, visited);
        ans++;
      }
    }
    return ans;
  }

  private Map<Integer, List<Integer>> buildGraph(int n, int[][] friendships) {
    Map<Integer, List<Integer>> graph = new HashMap<>();
    for(int i = 0; i < n; i++) graph.put(i, new ArrayList<>());
    for(int[] friend : friendships) {
      int u = friend[0], v = friend[1];
      graph.get(u).add(v);
      graph.get(v).add(u);
    }
    return graph;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BÀI 2: TÌM ĐƯỜNG ĐI NGẮN NHẤT (BFS Shortest Path)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 🎯 VẤN ĐỀ THỰC TẾ:
   * Trong một mê cung, bạn đứng ở vị trí 'S' và cần đến đích 'E'.
   * Bạn chỉ có thể đi lên/xuống/trái/phải (không đi xuyên tường '#').
   * Hỏi: Cần tối thiểu bao nhiêu bước để đến đích?
   *
   * 💡 TẠI SAO CẦN GIẢI?
   * - Đây là nền tảng của GPS, Google Maps
   * - Robot tìm đường trong nhà kho
   * - AI trong game điều khiển nhân vật
   *
   * 📝 VÍ DỤ:
   * Input:
   * [
   *   ['S', '.', '.', '#'],
   *   ['#', '#', '.', '#'],
   *   ['.', '.', '.', 'E']
   * ]
   *
   * Đường đi: S → (0,1) → (0,2) → (1,2) → (2,2) → E
   * Output: 5 bước
   *
   * 🔑 GỢI Ý:
   * - Dùng BFS vì BFS đảm bảo tìm đường ngắn nhất
   * - Queue lưu [row, col]
   * - 4 hướng: lên [-1,0], xuống [1,0], trái [0,-1], phải [0,1]
   *
   * 📌 LEETCODE: 1091. Shortest Path in Binary Matrix
   * https://leetcode.com/problems/shortest-path-in-binary-matrix/
   */
  public int shortestPathInMaze(char[][] maze) {
    // TODO: Viết code của bạn ở đây
    // Bước 1: Tìm vị trí S và E
    // Bước 2: BFS từ S, đếm số bước
    // Bước 3: Return số bước khi đến E
    return -1;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BÀI 3: ĐẾM SỐ ĐẢO (Flood Fill / DFS trên Grid)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 🎯 VẤN ĐỀ THỰC TẾ:
   * Bạn nhìn bản đồ từ trên cao: '1' là đất, '0' là nước.
   * Các ô '1' nối liền nhau (lên/xuống/trái/phải) tạo thành 1 đảo.
   * Hỏi: Có bao nhiêu đảo trên bản đồ?
   *
   * 💡 TẠI SAO CẦN GIẢI?
   * - Phân tích ảnh vệ tinh
   * - Nhận dạng vùng trong hình ảnh y tế
   * - Đếm các cụm dữ liệu trong data mining
   *
   * 📝 VÍ DỤ:
   * Input:
   * [
   *   ['1', '1', '0', '0', '0'],
   *   ['1', '1', '0', '0', '0'],
   *   ['0', '0', '1', '0', '0'],
   *   ['0', '0', '0', '1', '1']
   * ]
   *
   * Đảo 1: góc trên trái (4 ô '1' nối nhau)
   * Đảo 2: ở giữa (1 ô '1')
   * Đảo 3: góc dưới phải (2 ô '1' nối nhau)
   *
   * Output: 3 đảo
   *
   * 🔑 GỢI Ý:
   * - Duyệt từng ô trong grid
   * - Nếu gặp '1' chưa visited → DFS để "đánh dấu" cả đảo → đếm +1
   * - DFS sẽ lan ra 4 hướng và đánh dấu tất cả ô '1' liên thông
   *
   * 📌 LEETCODE: 200. Number of Islands
   * https://leetcode.com/problems/number-of-islands/
   */
  public int countIslands(char[][] grid) {
    // TODO: Viết code của bạn ở đây
    // Bước 1: Duyệt từng ô
    // Bước 2: Nếu là '1', DFS để đánh dấu cả đảo, count++
    // Bước 3: Return count
    return -1;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BÀI 4: TÌM THẨM PHÁN (In-Degree / Out-Degree) ⭐ BẮT ĐẦU TỪ ĐÂY
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 🎯 VẤN ĐỀ THỰC TẾ:
   * Trong thị trấn có N người (đánh số 1 đến N).
   * Có tin đồn rằng 1 người là "Thẩm phán bí mật":
   * - Thẩm phán KHÔNG TIN bất kỳ ai
   * - TẤT CẢ người khác đều TIN thẩm phán
   *
   * 💡 TẠI SAO CẦN GIẢI?
   * - Tìm "influencer" trong mạng xã hội
   * - Tìm người được tất cả tín nhiệm trong team
   * - Xác định node "sink" trong directed graph
   *
   * 📝 VÍ DỤ:
   * Input: n = 3, trust = [[1,3], [2,3]]
   *
   *   Người 1 ──tin──► Người 3
   *   Người 2 ──tin──► Người 3
   *
   * Phân tích:
   * - Người 3: được 2 người tin (✓), không tin ai (✓) → JUDGE!
   *
   * Output: 3
   *
   * 🔑 GỢI Ý:
   * - Dùng mảng score[i] = số người tin i - số người i tin
   * - Với mỗi [a,b]: score[a]--, score[b]++
   * - Thẩm phán có score = n-1
   *
   * 📌 LEETCODE: 997. Find the Town Judge
   * https://leetcode.com/problems/find-the-town-judge/
   */
  public int findTownJudge(int n, int[][] trust) {
    // TODO: Viết code của bạn ở đây
    // Bước 1: Tạo mảng score[n+1]
    // Bước 2: Duyệt trust[], cập nhật score
    // Bước 3: Tìm người có score = n-1
    return -1;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BÀI 5: KIỂM TRA CÓ THỂ HỌC HẾT CÁC MÔN KHÔNG (Cycle Detection)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 🎯 VẤN ĐỀ THỰC TẾ:
   * Trường có N môn học (đánh số 0 đến N-1).
   * Một số môn yêu cầu phải học môn khác trước (prerequisites).
   * Hỏi: Có thể học hết tất cả các môn không?
   *
   * 💡 TẠI SAO CẦN GIẢI?
   * - Hệ thống đăng ký học
   * - Quản lý dependency trong npm/maven
   * - Lập lịch công việc có phụ thuộc
   *
   * 📝 VÍ DỤ 1:
   * Input: numCourses = 2, prerequisites = [[1,0]]
   * Nghĩa là: Để học môn 1, phải học môn 0 trước
   *
   *   Môn 0 ────► Môn 1
   *
   * Output: true (Học 0 → Học 1)
   *
   * 📝 VÍ DỤ 2:
   * Input: numCourses = 2, prerequisites = [[1,0], [0,1]]
   * Nghĩa là: Môn 1 cần 0, Môn 0 cần 1 → VÔ LÝ!
   *
   *   Môn 0 ◄────► Môn 1 (Cycle!)
   *
   * Output: false
   *
   * 🔑 GỢI Ý:
   * - Nếu có cycle (vòng lặp) → không thể học hết
   * - Dùng Topological Sort: nếu không sort được = có cycle
   * - Hoặc dùng DFS với 3 trạng thái để phát hiện cycle
   *
   * 📌 LEETCODE: 207. Course Schedule
   * https://leetcode.com/problems/course-schedule/
   */
  public boolean canFinishAllCourses(int numCourses, int[][] prerequisites) {
    // TODO: Viết code của bạn ở đây
    // Cách 1: Kahn's Algorithm (Topological Sort với in-degree)
    // Cách 2: DFS với 3 states (0=chưa thăm, 1=đang thăm, 2=đã xong)
    return false;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BÀI 6: CAM THỐI RỮA (Multi-source BFS)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 🎯 VẤN ĐỀ THỰC TẾ:
   * Trong rổ trái cây:
   * - '2' = cam thối
   * - '1' = cam tươi
   * - '0' = ô trống
   *
   * Mỗi phút, cam thối làm các cam tươi KỀ NÓ (4 hướng) cũng bị thối.
   * Hỏi: Sau bao nhiêu phút thì TẤT CẢ cam đều thối?
   * (Trả về -1 nếu có cam không thể bị thối)
   *
   * 💡 TẠI SAO CẦN GIẢI?
   * - Mô phỏng dịch bệnh lan truyền
   * - Mô phỏng cháy rừng
   * - Tính toán vùng phủ sóng
   *
   * 📝 VÍ DỤ:
   * Input:
   * [
   *   [2, 1, 1],
   *   [1, 1, 0],
   *   [0, 1, 1]
   * ]
   *
   * Phút 0: 2 thối ban đầu ở (0,0)
   * Phút 1: (0,1) và (1,0) bị thối
   * Phút 2: (0,2), (1,1) bị thối
   * Phút 3: (2,1) bị thối
   * Phút 4: (2,2) bị thối
   *
   * Output: 4 phút
   *
   * 🔑 GỢI Ý:
   * - Multi-source BFS: Thêm TẤT CẢ cam thối vào queue trước
   * - BFS theo từng "lớp" = từng phút
   * - Sau BFS, kiểm tra còn cam tươi không
   *
   * 📌 LEETCODE: 994. Rotting Oranges
   * https://leetcode.com/problems/rotting-oranges/
   */
  public int minutesToRotAll(int[][] grid) {
    // TODO: Viết code của bạn ở đây
    // Bước 1: Tìm tất cả cam thối ban đầu, thêm vào queue
    // Bước 2: BFS theo từng lớp (mỗi lớp = 1 phút)
    // Bước 3: Kiểm tra còn cam tươi không
    return -1;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BÀI 7: KIỂM TRA ĐỒ THỊ 2 PHÍA (Bipartite Check)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 🎯 VẤN ĐỀ THỰC TẾ:
   * Bạn muốn chia N người thành 2 đội để chơi game.
   * Một số cặp người có mâu thuẫn (không thể cùng đội).
   * Hỏi: Có thể chia được không?
   *
   * 💡 TẠI SAO CẦN GIẢI?
   * - Chia lớp thành 2 nhóm sao cho bạn bè không cùng nhóm
   * - Kiểm tra xem graph có phải bipartite không
   * - Bài toán 2-coloring
   *
   * 📝 VÍ DỤ 1:
   * Input: n = 4, dislikes = [[0,1], [0,2], [1,3], [2,3]]
   *
   *     0 ── 1
   *     |    |
   *     2 ── 3
   *
   * Chia: Đội A = {0, 3}, Đội B = {1, 2}
   * Output: true
   *
   * 📝 VÍ DỤ 2:
   * Input: n = 3, dislikes = [[0,1], [1,2], [0,2]]
   *
   *     0 ── 1
   *      \  /
   *       2
   *
   * Output: false (tam giác không thể 2-coloring)
   *
   * 🔑 GỢI Ý:
   * - Tô màu graph: node = 0 hoặc 1
   * - Nếu 2 node kề nhau cùng màu → false
   * - Dùng BFS/DFS để tô màu
   *
   * 📌 LEETCODE: 785. Is Graph Bipartite?
   * https://leetcode.com/problems/is-graph-bipartite/
   */
  public boolean canSplitIntoTwoTeams(int n, int[][] dislikes) {
    // TODO: Viết code của bạn ở đây
    // Bước 1: Build graph
    // Bước 2: Dùng BFS/DFS tô màu (0 hoặc 1)
    // Bước 3: Nếu 2 node kề cùng màu → return false
    return false;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BẢNG TÓM TẮT
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────┬─────────────────────────┬──────────────────┬───────────────────┐
   * │ Bài    │ Vấn đề                  │ Kỹ thuật         │ Độ khó            │
   * ├────────┼─────────────────────────┼──────────────────┼───────────────────┤
   * │ 1      │ Đếm nhóm bạn            │ DFS/BFS đếm CC   │ ⭐ Easy           │
   * │ 2      │ Tìm đường ngắn nhất     │ BFS              │ ⭐ Easy           │
   * │ 3      │ Đếm số đảo              │ DFS Flood Fill   │ ⭐ Easy           │
   * │ 4      │ Tìm thẩm phán           │ In/Out Degree    │ ⭐ Easy           │
   * │ 5      │ Học hết các môn?        │ Topo Sort/Cycle  │ ⭐⭐ Medium       │
   * │ 6      │ Cam thối rữa            │ Multi-source BFS │ ⭐⭐ Medium       │
   * │ 7      │ Chia 2 đội              │ Bipartite/2-color│ ⭐⭐ Medium       │
   * └────────┴─────────────────────────┴──────────────────┴───────────────────┘
   *
   * 🎯 THỨ TỰ LÀM KHUYẾN NGHỊ: 1 → 3 → 4 → 2 → 5 → 6 → 7
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // HELPER: Template DFS để tham khảo
  // ═══════════════════════════════════════════════════════════════════════════
  private void dfsTemplate(Map<Integer, List<Integer>> graph, int node, Set<Integer> visited) {
    visited.add(node);
    for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
      if (!visited.contains(neighbor)) {
        dfsTemplate(graph, neighbor, visited);
      }
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // HELPER: Template BFS để tham khảo
  // ═══════════════════════════════════════════════════════════════════════════
  private int bfsTemplate(Map<Integer, List<Integer>> graph, int start, int end) {
    Set<Integer> visited = new HashSet<>();
    Queue<Integer> queue = new LinkedList<>();

    visited.add(start);
    queue.offer(start);
    int steps = 0;

    while (!queue.isEmpty()) {
      int size = queue.size();
      for (int i = 0; i < size; i++) {
        int node = queue.poll();
        if (node == end) return steps;

        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
          if (!visited.contains(neighbor)) {
            visited.add(neighbor);
            queue.offer(neighbor);
          }
        }
      }
      steps++;
    }
    return -1;
  }
}
