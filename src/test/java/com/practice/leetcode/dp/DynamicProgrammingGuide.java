package com.practice.leetcode.dp;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ DYNAMIC PROGRAMMING GUIDE ║
 * ║ (Quy hoạch động) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ DYNAMIC PROGRAMMING là gì? │
 * │ │
 * │ DP = Chia bài toán thành các BÀI TOÁN CON TRÙNG LẶP, │
 * │ giải mỗi bài một lần và LƯU LẠI kết quả. │
 * │ │
 * │ 2 tính chất để dùng DP: │
 * │ 1. Optimal Substructure: Lời giải tối ưu chứa lời giải tối ưu của bài con│
 * │ 2. Overlapping Subproblems: Các bài con lặp lại nhiều lần │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * 2 CÁCH TIẾP CẬN DP
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ 1. TOP-DOWN (Memoization) - Đệ quy + Cache │
 * │ - Bắt đầu từ bài toán LỚN, chia nhỏ dần │
 * │ - Dùng recursion + HashMap/Array để cache │
 * │ - Dễ nghĩ, code trực quan │
 * │ │
 * │ 2. BOTTOM-UP (Tabulation) - Lặp từ nhỏ đến lớn │
 * │ - Bắt đầu từ bài toán NHỎ NHẤT, xây dần lên │
 * │ - Dùng array/table lưu kết quả │
 * │ - Tối ưu hơn (không có recursion overhead) │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class DynamicProgrammingGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // FRAMEWORK GIẢI BÀI DP
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * BƯỚC 1: Định nghĩa STATE
   * - dp[i] hoặc dp[i][j] đại diện cho gì?
   * - Thường là "kết quả tối ưu khi xét đến phần tử thứ i"
   *
   * BƯỚC 2: Tìm RECURRENCE RELATION (Công thức truy hồi)
   * - dp[i] được tính từ những dp trước đó như thế nào?
   * - Đây là bước QUAN TRỌNG NHẤT!
   *
   * BƯỚC 3: Xác định BASE CASE
   * - dp[0] = ? dp[1] = ?
   * - Giá trị ban đầu để bắt đầu tính
   *
   * BƯỚC 4: Xác định ORDER tính toán
   * - Tính từ nhỏ đến lớn (bottom-up)
   * - Hoặc đệ quy với memo (top-down)
   *
   * BƯỚC 5: Tối ưu SPACE (nếu cần)
   * - Nếu dp[i] chỉ phụ thuộc dp[i-1], chỉ cần 2 biến
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 1: FIBONACCI-STYLE (1D DP)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Đặc điểm:
   * - dp[i] phụ thuộc vào 1-2 phần tử trước
   * - Có thể tối ưu space xuống O(1)
   *
   * Bài toán: Climbing Stairs, House Robber, Fibonacci
   *
   * Ví dụ: CLIMBING STAIRS
   * - Có thể bước 1 hoặc 2 bậc
   * - Có bao nhiêu cách lên n bậc?
   *
   * STATE: dp[i] = số cách lên bậc thứ i
   * RECURRENCE: dp[i] = dp[i-1] + dp[i-2]
   * (từ i-1 bước 1, hoặc từ i-2 bước 2)
   * BASE: dp[1] = 1, dp[2] = 2
   */
  int climbStairs(int n) {
    if (n <= 2)
      return n;

    // Space O(n)
    int[] dp = new int[n + 1];
    dp[1] = 1;
    dp[2] = 2;
    for (int i = 3; i <= n; i++) {
      dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp[n];
  }

  int climbStairsOptimized(int n) {
    if (n <= 2)
      return n;

    // Space O(1)
    int prev2 = 1, prev1 = 2;
    for (int i = 3; i <= n; i++) {
      int curr = prev1 + prev2;
      prev2 = prev1;
      prev1 = curr;
    }
    return prev1;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 2: CHOOSE/NOT CHOOSE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Đặc điểm:
   * - Với mỗi phần tử, có 2 lựa chọn: LẤY hoặc KHÔNG LẤY
   * - dp[i] = max/min của 2 trường hợp
   *
   * Bài toán: House Robber, 0/1 Knapsack, Subset Sum
   *
   * Ví dụ: HOUSE ROBBER
   * - Không được cướp 2 nhà liền kề
   * - Tìm tổng tiền lớn nhất
   *
   * STATE: dp[i] = max tiền khi xét đến nhà thứ i
   * RECURRENCE: dp[i] = max(
   * dp[i-1], // Không cướp nhà i
   * dp[i-2] + nums[i] // Cướp nhà i
   * )
   * BASE: dp[0] = nums[0], dp[1] = max(nums[0], nums[1])
   */
  int houseRobber(int[] nums) {
    if (nums.length == 1)
      return nums[0];

    int prev2 = nums[0];
    int prev1 = Math.max(nums[0], nums[1]);

    for (int i = 2; i < nums.length; i++) {
      int curr = Math.max(prev1, prev2 + nums[i]);
      prev2 = prev1;
      prev1 = curr;
    }

    return prev1;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 3: UNBOUNDED KNAPSACK
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Đặc điểm:
   * - Mỗi item có thể dùng NHIỀU LẦN
   * - Tối ưu trong giới hạn capacity/amount
   *
   * Bài toán: Coin Change, Unbounded Knapsack, Rod Cutting
   *
   * Ví dụ: COIN CHANGE
   * - Tìm số xu ÍT NHẤT để đổi amount
   * - Mỗi loại xu có thể dùng nhiều lần
   *
   * STATE: dp[i] = min số xu để đổi amount = i
   * RECURRENCE: dp[i] = min(dp[i], dp[i - coin] + 1) cho mỗi coin
   * BASE: dp[0] = 0
   */
  int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    java.util.Arrays.fill(dp, amount + 1);
    dp[0] = 0;

    for (int i = 1; i <= amount; i++) {
      for (int coin : coins) {
        if (coin <= i) {
          dp[i] = Math.min(dp[i], dp[i - coin] + 1);
        }
      }
    }

    return dp[amount] > amount ? -1 : dp[amount];
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 4: LCS/LIS (Sequence DP)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Đặc điểm:
   * - Tìm subsequence dài nhất thỏa điều kiện
   * - Thường là 2D DP cho 2 strings, 1D cho 1 array
   *
   * Bài toán: LCS, LIS, Edit Distance
   *
   * Ví dụ: LONGEST INCREASING SUBSEQUENCE
   *
   * STATE: dp[i] = độ dài LIS kết thúc tại i
   * RECURRENCE: dp[i] = max(dp[j] + 1) với j < i và nums[j] < nums[i]
   * BASE: dp[i] = 1 (mỗi phần tử là subsequence của chính nó)
   */
  int lengthOfLIS(int[] nums) {
    int n = nums.length;
    int[] dp = new int[n];
    java.util.Arrays.fill(dp, 1);
    int maxLen = 1;

    for (int i = 1; i < n; i++) {
      for (int j = 0; j < i; j++) {
        if (nums[j] < nums[i]) {
          dp[i] = Math.max(dp[i], dp[j] + 1);
        }
      }
      maxLen = Math.max(maxLen, dp[i]);
    }

    return maxLen;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 5: 2D GRID DP
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Đặc điểm:
   * - Di chuyển trên grid từ góc này sang góc kia
   * - Đếm số đường đi hoặc tìm min/max cost
   *
   * Bài toán: Unique Paths, Minimum Path Sum
   *
   * Ví dụ: UNIQUE PATHS
   *
   * STATE: dp[i][j] = số đường đi đến ô (i,j)
   * RECURRENCE: dp[i][j] = dp[i-1][j] + dp[i][j-1]
   * BASE: dp[0][j] = dp[i][0] = 1
   */
  int uniquePaths(int m, int n) {
    int[][] dp = new int[m][n];

    // Base: hàng đầu và cột đầu = 1
    for (int i = 0; i < m; i++)
      dp[i][0] = 1;
    for (int j = 0; j < n; j++)
      dp[0][j] = 1;

    for (int i = 1; i < m; i++) {
      for (int j = 1; j < n; j++) {
        dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
      }
    }

    return dp[m - 1][n - 1];
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BẢNG TỔNG HỢP PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌──────────────────────────────────────────────────────────────────────────┐
   * │ Pattern │ Bài toán mẫu │ Recurrence │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Fibonacci │ Climbing Stairs │ dp[i]=dp[i-1]+dp[i-2] │
   * │ Choose/Not Choose │ House Robber, Knapsack │ max(take, skip) │
   * │ Unbounded Knapsack │ Coin Change │ min(dp[i-coin]+1) │
   * │ LCS/LIS │ LIS, Edit Distance │ dp[i]=dp[j]+1 if cond │
   * │ Grid DP │ Unique Paths │ dp[i][j]=top+left │
   * │ Interval DP │ Burst Balloons │ dp[i][j]=best split │
   * │ State Machine │ Best Time Buy/Sell │ hold/sold states │
   * └──────────────────────────────────────────────────────────────────────────┘
   *
   *
   * TIPS KHI GIẢI DP
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ 1. Bắt đầu với BRUTE FORCE recursion │
   * │ 2. Nhận dạng OVERLAPPING subproblems → Thêm memo │
   * │ 3. Chuyển sang BOTTOM-UP nếu cần tối ưu │
   * │ 4. Tối ưu SPACE nếu chỉ cần O(1) hoặc O(n) thay vì O(n²) │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   *
   * CÁC BÀI THỰC HÀNH
   *
   * EASY:
   * - Climbing Stairs (70)
   * - House Robber (198)
   * - Maximum Subarray (53)
   *
   * MEDIUM:
   * - Coin Change (322)
   * - Longest Increasing Subsequence (300)
   * - Unique Paths (62)
   * - Word Break (139)
   *
   * HARD:
   * - Edit Distance (72)
   * - Longest Valid Parentheses (32)
   * - Burst Balloons (312)
   */
}
