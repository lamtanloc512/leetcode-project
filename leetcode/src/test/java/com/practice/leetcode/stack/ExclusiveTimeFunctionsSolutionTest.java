package com.practice.leetcode.stack;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║           EXCLUSIVE TIME OF FUNCTIONS (LeetCode 636)                      ║
 * ║                    Pattern: FUNCTION CALL STACK                           ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │  BÀI TOÁN:                                                                  │
 * │  - CPU single-threaded chạy n functions (ID từ 0 đến n-1)                   │
 * │  - Logs ghi lại: "{function_id}:{start|end}:{timestamp}"                    │
 * │  - Tính EXCLUSIVE TIME của mỗi function                                     │
 * │                                                                             │
 * │  EXCLUSIVE TIME = thời gian function TỰ NÓ chạy                             │
 * │                   (KHÔNG tính thời gian function con chạy bên trong)        │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │  TIMELINE EXAMPLE:                                                          │
 * │                                                                             │
 * │  Input: n = 2, logs = ["0:start:0","1:start:2","1:end:5","0:end:6"]         │
 * │                                                                             │
 * │  Timeline (mỗi ô = 1 time unit):                                            │
 * │                                                                             │
 * │    Time:    0     1     2     3     4     5     6                           │
 * │           ┌─────┬─────┬─────┬─────┬─────┬─────┬─────┐                       │
 * │  Func 0:  │█████│█████│     │     │     │     │█████│  = 3 units            │
 * │           └─────┴─────┴─────┴─────┴─────┴─────┴─────┘                       │
 * │                       ┌─────┬─────┬─────┬─────┐                             │
 * │  Func 1:              │█████│█████│█████│█████│        = 4 units            │
 * │                       └─────┴─────┴─────┴─────┘                             │
 * │                                                                             │
 * │  Output: [3, 4]                                                             │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │  ĐIỂM MẤU CHỐT VỀ TIMESTAMP:                                                │
 * │                                                                             │
 * │  • "start:3" → bắt đầu tại ĐẦẦU timestamp 3                                 │
 * │  • "end:5"   → kết thúc tại CUỐI timestamp 5                                │
 * │                                                                             │
 * │         ┌─────────────────────────────────────────────────────────┐         │
 * │         │                                                         │         │
 * │         │    ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐            │         │
 * │         │    │  0 │ │  1 │ │  2 │ │  3 │ │  4 │ │  5 │            │         │
 * │         │    └────┘ └────┘ └────┘ └────┘ └────┘ └────┘            │         │
 * │         │    ↑                                      ↑             │         │
 * │         │ start:0                                end:5            │         │
 * │         │ (đầu)                                  (cuối)           │         │
 * │         │                                                         │         │
 * │         │ → Duration = 5 - 0 + 1 = 6 units                        │         │
 * │         │                                                         │         │
 * │         └─────────────────────────────────────────────────────────┘         │
 * │                                                                             │
 * │  → Khi END: duration = endTime - prevTime + 1  (cần +1)                     │
 * │  → Sau END: prevTime = endTime + 1                                          │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class ExclusiveTimeFunctionsSolutionTest {

  // ═══════════════════════════════════════════════════════════════════════════
  // SOLUTION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │  THUẬT TOÁN:                                                            │
   * │                                                                         │
   * │  1. Dùng Stack để track function đang chạy (giống call stack thật)      │
   * │  2. prevTime = thời điểm bắt đầu của "đoạn thời gian hiện tại"          │
   * │                                                                         │
   * │  KHI GẶP START:                                                         │
   * │    - Nếu có function đang chạy (stack không rỗng):                      │
   * │        → Cộng thời gian (time - prevTime) cho function đó               │
   * │    - Push function mới vào stack                                        │
   * │    - prevTime = time                                                    │
   * │                                                                         │
   * │  KHI GẶP END:                                                           │
   * │    - Pop function ra khỏi stack                                         │
   * │    - Cộng thời gian (time - prevTime + 1) cho function đó               │
   * │      (+1 vì end là CUỐI timestamp)                                      │
   * │    - prevTime = time + 1                                                │
   * └─────────────────────────────────────────────────────────────────────────┘
   */
  public int[] exclusiveTime(int n, List<String> logs) {
    int[] result = new int[n];
    Deque<Integer> stack = new ArrayDeque<>(); // Stack chứa function ID
    int prevTime = 0;

    for (String log : logs) {
      // Parse log: "{funcId}:{start|end}:{timestamp}"
      String[] parts = log.split(":");
      int funcId = Integer.parseInt(parts[0]);
      boolean isStart = parts[1].equals("start");
      int time = Integer.parseInt(parts[2]);

      if (isStart) {
        // ┌─────────────────────────────────────────────────────────────┐
        // │  START: Function mới bắt đầu                                │
        // │                                                             │
        // │  Nếu có function đang chạy → tạm dừng nó                    │
        // │  và cộng thời gian đã chạy cho function đó                  │
        // └─────────────────────────────────────────────────────────────┘
        if (!stack.isEmpty()) {
          // Function trên đỉnh stack đã chạy từ prevTime đến time-1
          // (time là lúc bắt đầu function mới, nên không tính)
          result[stack.peek()] += time - prevTime;
        }
        stack.push(funcId);
        prevTime = time;
      } else {
        // ┌─────────────────────────────────────────────────────────────┐
        // │  END: Function kết thúc                                     │
        // │                                                             │
        // │  Pop function ra và cộng thời gian                          │
        // │  +1 vì end:5 nghĩa là chạy HẾT timestamp 5                  │
        // └─────────────────────────────────────────────────────────────┘
        result[stack.pop()] += time - prevTime + 1;
        prevTime = time + 1; // Timestamp tiếp theo bắt đầu từ time+1
      }
    }

    return result;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // DRY RUN EXAMPLE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │  DRY RUN: logs = ["0:start:0","1:start:2","1:end:5","0:end:6"]          │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │                                                                         │
   * │  BƯỚC 1: "0:start:0"                                                    │
   * │  ────────────────────                                                   │
   * │  • stack = [], prevTime = 0                                             │
   * │  • START func 0 at time 0                                               │
   * │  • stack rỗng → không cộng gì                                           │
   * │  • Push 0 → stack = [0]                                                 │
   * │  • prevTime = 0                                                         │
   * │  • result = [0, 0]                                                      │
   * │                                                                         │
   * │  Stack: │ 0 │                                                           │
   * │         └───┘                                                           │
   * │                                                                         │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │                                                                         │
   * │  BƯỚC 2: "1:start:2"                                                    │
   * │  ────────────────────                                                   │
   * │  • stack = [0], prevTime = 0                                            │
   * │  • START func 1 at time 2                                               │
   * │  • stack không rỗng → result[0] += 2 - 0 = 2                            │
   * │  • Push 1 → stack = [0, 1]                                              │
   * │  • prevTime = 2                                                         │
   * │  • result = [2, 0]                                                      │
   * │                                                                         │
   * │  Stack: │ 1 │ ← top (đang chạy)                                         │
   * │         │ 0 │ ← tạm dừng, đã tích lũy 2 units                           │
   * │         └───┘                                                           │
   * │                                                                         │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │                                                                         │
   * │  BƯỚC 3: "1:end:5"                                                      │
   * │  ──────────────────                                                     │
   * │  • stack = [0, 1], prevTime = 2                                         │
   * │  • END func 1 at time 5                                                 │
   * │  • Pop 1 → stack = [0]                                                  │
   * │  • result[1] += 5 - 2 + 1 = 4                                           │
   * │  • prevTime = 5 + 1 = 6                                                 │
   * │  • result = [2, 4]                                                      │
   * │                                                                         │
   * │  Stack: │ 0 │ ← resume (tiếp tục chạy từ time 6)                        │
   * │         └───┘                                                           │
   * │                                                                         │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │                                                                         │
   * │  BƯỚC 4: "0:end:6"                                                      │
   * │  ──────────────────                                                     │
   * │  • stack = [0], prevTime = 6                                            │
   * │  • END func 0 at time 6                                                 │
   * │  • Pop 0 → stack = []                                                   │
   * │  • result[0] += 6 - 6 + 1 = 1                                           │
   * │  • prevTime = 6 + 1 = 7                                                 │
   * │  • result = [3, 4] ✓                                                    │
   * │                                                                         │
   * │  Stack: (empty)                                                         │
   * │                                                                         │
   * └─────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // RECURSIVE FUNCTION EXAMPLE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │  EXAMPLE: Recursive call - cùng function gọi nhiều lần                     │
   * │                                                                            │
   * │  logs = ["0:start:0","0:start:2","0:end:5","0:start:6","0:end:6","0:end:7"]│
   * │                                                                            │
   * │  Timeline:                                                                 │
   * │    0     1     2     3     4     5     6     7                             │
   * │  ┌─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┐                         │
   * │  │ f0  │ f0  │ f0  │ f0  │ f0  │ f0  │ f0  │ f0  │                         │
   * │  │     │     │(2nd)│(2nd)│(2nd)│(2nd)│(3rd)│     │                         │
   * │  └─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┘                         │
   * │                                                                            │
   * │  Stack changes:                                                            │
   * │  [0] → [0,0] → [0] → [0,0] → [0] → []                                      │
   * │                                                                            │
   * │  Total exclusive time of func 0 = 8 units                                  │
   * └────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // COMPLEXITY ANALYSIS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │  TIME COMPLEXITY: O(L)                                                  │
   * │  - L = số lượng logs                                                    │
   * │  - Mỗi log được xử lý đúng 1 lần                                        │
   * │                                                                         │
   * │  SPACE COMPLEXITY: O(n)                                                 │
   * │  - n = số functions                                                     │
   * │  - Stack tối đa chứa n elements (worst case: nested calls)              │
   * │  - result array: O(n)                                                   │
   * └─────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // KEY INSIGHTS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │  TẠI SAO DÙNG STACK?                                                    │
   * │                                                                         │
   * │  1. Giống Call Stack trong thực tế:                                     │
   * │     - Function mới → push                                               │
   * │     - Function kết thúc → pop                                           │
   * │     - Top of stack = function đang chạy                                 │
   * │                                                                         │
   * │  2. LIFO property hoàn hảo cho nested function calls:                   │
   * │     - Function được gọi sau phải kết thúc trước                         │
   * │                                                                         │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │  TẠI SAO CẦN +1 KHI END?                                                │
   * │                                                                         │
   * │  • "end:5" = kết thúc ở CUỐI timestamp 5                                │
   * │  • Nghĩa là function đã "tiêu" toàn bộ timestamp 5                      │
   * │  • Nếu prevTime = 2, thì function chạy từ 2,3,4,5 = 4 units             │
   * │  • = 5 - 2 + 1 = 4 ✓                                                    │
   * │                                                                         │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │  TẠI SAO PREVTIME = TIME + 1 SAU KHI END?                               │
   * │                                                                         │
   * │  • Function vừa end ở timestamp 5 (hết cuối 5)                          │
   * │  • Đoạn thời gian tiếp theo sẽ bắt đầu từ timestamp 6                   │
   * │  • Nên prevTime = 5 + 1 = 6                                             │
   * └─────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // TESTS
  // ═══════════════════════════════════════════════════════════════════════════
  @Test
  void testBasicExample() {
    // 2 functions, function 0 gọi function 1
    List<String> logs = Arrays.asList("0:start:0", "1:start:2", "1:end:5", "0:end:6");
    int[] expected = {3, 4};
    assertArrayEquals(expected, exclusiveTime(2, logs));
  }

  @Test
  void testSingleFunction() {
    // Chỉ có 1 function
    List<String> logs = Arrays.asList("0:start:0", "0:end:6");
    int[] expected = {7}; // 6 - 0 + 1 = 7
    assertArrayEquals(expected, exclusiveTime(1, logs));
  }

  @Test
  void testRecursiveFunction() {
    // Function tự gọi chính nó
    List<String> logs =
        Arrays.asList("0:start:0", "0:start:2", "0:end:5", "0:start:6", "0:end:6", "0:end:7");
    int[] expected = {8}; // Total time từ 0-7 = 8 units
    assertArrayEquals(expected, exclusiveTime(1, logs));
  }

  @Test
  void testMultipleFunctions() {
    // 3 functions chồng nhau
    List<String> logs =
        Arrays.asList(
            "0:start:0", "1:start:1", "2:start:2", "2:end:3", "1:end:4", "0:end:5");
    int[] expected = {2, 2, 2};
    // func 0: time 0 + time 5 = 2
    // func 1: time 1 + time 4 = 2
    // func 2: time 2 + time 3 = 2
    assertArrayEquals(expected, exclusiveTime(3, logs));
  }

  @Test
  void testImmediateReturn() {
    // Function bắt đầu và kết thúc cùng timestamp
    List<String> logs = Arrays.asList("0:start:0", "0:end:0");
    int[] expected = {1}; // 0 - 0 + 1 = 1
    assertArrayEquals(expected, exclusiveTime(1, logs));
  }
}
