package com.practice.leetcode.stack;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ STACK GUIDE ║
 * ║ (Ngăn xếp) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ STACK là gì? │
 * │ │
 * │ - LIFO: Last In, First Out (vào sau, ra trước) │
 * │ - Operations: push(), pop(), peek() - đều O(1) │
 * │ │
 * │ Visualize: │
 * │ push(1) → push(2) → push(3) → pop()=3 → pop()=2 │
 * │ │
 * │ | | | | | 3 | | | | | │
 * │ | | → | 2 | → | 2 | → | 2 | → | | │
 * │ | 1 | | 1 | | 1 | | 1 | | 1 | │
 * │ └───┘ └───┘ └───┘ └───┘ └───┘ │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class StackGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 1: MATCHING (Khớp cặp)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Dùng cho: Matching brackets, tags, expressions
   *
   * Ví dụ: Valid Parentheses
   * - Push khi gặp mở: (, [, {
   * - Pop và check khi gặp đóng: ), ], }
   *
   * Template:
   * for (char c : s) {
   * if (isOpen(c)) {
   * stack.push(c);
   * } else {
   * if (stack.isEmpty() || !isMatch(stack.pop(), c)) {
   * return false;
   * }
   * }
   * }
   * return stack.isEmpty();
   */
  boolean isValid(String s) {
    java.util.Deque<Character> stack = new java.util.ArrayDeque<>();

    for (char c : s.toCharArray()) {
      if (c == '(' || c == '[' || c == '{') {
        stack.push(c);
      } else {
        if (stack.isEmpty())
          return false;
        char open = stack.pop();
        if ((c == ')' && open != '(') ||
            (c == ']' && open != '[') ||
            (c == '}' && open != '{')) {
          return false;
        }
      }
    }

    return stack.isEmpty();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 2: MONOTONIC STACK (Stack đơn điệu)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ĐÂY LÀ PATTERN QUAN TRỌNG NHẤT!
   *
   * Monotonic Stack: Stack luôn duy trì thứ tự TĂNG hoặc GIẢM
   *
   * Dùng cho:
   * - Next Greater Element
   * - Previous Smaller Element
   * - Daily Temperatures
   * - Largest Rectangle in Histogram
   *
   * Template NEXT GREATER:
   *
   * for (int i = 0; i < n; i++) {
   * // Pop tất cả phần tử NHỎ HƠN current
   * while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
   * int idx = stack.pop();
   * result[idx] = nums[i]; // next greater của idx là i
   * }
   * stack.push(i); // Push index, không push value!
   * }
   *
   * Lưu ý: Stack lưu INDEX, không lưu value!
   */

  // Next Greater Element
  int[] nextGreaterElement(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];
    java.util.Arrays.fill(result, -1); // Default: không có greater

    java.util.Deque<Integer> stack = new java.util.ArrayDeque<>();

    for (int i = 0; i < n; i++) {
      // Pop những phần tử nhỏ hơn current
      while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
        int idx = stack.pop();
        result[idx] = nums[i];
      }
      stack.push(i);
    }

    return result;
  }

  // Daily Temperatures
  int[] dailyTemperatures(int[] temps) {
    int n = temps.length;
    int[] result = new int[n];
    java.util.Deque<Integer> stack = new java.util.ArrayDeque<>();

    for (int i = 0; i < n; i++) {
      while (!stack.isEmpty() && temps[stack.peek()] < temps[i]) {
        int idx = stack.pop();
        result[idx] = i - idx; // Số ngày chờ
      }
      stack.push(i);
    }

    return result;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 3: EXPRESSION EVALUATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Dùng cho: Calculator, evaluate expressions, postfix/prefix
   *
   * Template cho Basic Calculator:
   * - Stack lưu (sign, result) khi gặp '('
   * - Pop và compute khi gặp ')'
   * - Accumulate khi gặp digit
   * - Update sign khi gặp +/-
   */
  int calculate(String s) {
    java.util.Deque<Integer> stack = new java.util.ArrayDeque<>();
    int result = 0;
    int number = 0;
    int sign = 1;

    for (char c : s.toCharArray()) {
      if (Character.isDigit(c)) {
        number = number * 10 + (c - '0');
      } else if (c == '+') {
        result += sign * number;
        number = 0;
        sign = 1;
      } else if (c == '-') {
        result += sign * number;
        number = 0;
        sign = -1;
      } else if (c == '(') {
        stack.push(result);
        stack.push(sign);
        result = 0;
        sign = 1;
      } else if (c == ')') {
        result += sign * number;
        number = 0;
        result *= stack.pop(); // sign before (
        result += stack.pop(); // result before (
      }
    }

    return result + sign * number;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 4: HISTOGRAM/AREA
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Dùng cho: Largest Rectangle in Histogram, Maximal Rectangle
   *
   * Ý tưởng:
   * - Với mỗi bar, tìm biên trái và phải (first smaller bar)
   * - Area = height × (right - left - 1)
   * - Dùng Monotonic Increasing Stack
   */
  int largestRectangleArea(int[] heights) {
    java.util.Deque<Integer> stack = new java.util.ArrayDeque<>();
    int maxArea = 0;
    int n = heights.length;

    for (int i = 0; i <= n; i++) {
      int currentHeight = (i == n) ? 0 : heights[i];

      while (!stack.isEmpty() && heights[stack.peek()] > currentHeight) {
        int height = heights[stack.pop()];
        int width = stack.isEmpty() ? i : i - stack.peek() - 1;
        maxArea = Math.max(maxArea, height * width);
      }

      stack.push(i);
    }

    return maxArea;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 5: MIN STACK / SPECIAL STACK
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Dùng cho: Get min/max in O(1), stack with special operations
   *
   * Ý tưởng: Lưu thêm thông tin phụ cùng với mỗi element
   */
  class MinStack {
    private java.util.Deque<int[]> stack; // [value, min_so_far]

    MinStack() {
      stack = new java.util.ArrayDeque<>();
    }

    void push(int val) {
      int currentMin = stack.isEmpty() ? val : Math.min(val, stack.peek()[1]);
      stack.push(new int[] { val, currentMin });
    }

    void pop() {
      stack.pop();
    }

    int top() {
      return stack.peek()[0];
    }

    int getMin() {
      return stack.peek()[1];
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BẢNG TỔNG HỢP
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌──────────────────────────────────────────────────────────────────────────┐
   * │ Pattern │ Bài toán mẫu │ Key │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Matching │ Valid Parentheses │ Push open, pop close │
   * │ Monotonic (Increase) │ Next Greater Element │ Pop smaller, push │
   * │ Monotonic (Decrease) │ Next Smaller Element │ Pop larger, push │
   * │ Expression │ Calculator │ Sign + parentheses │
   * │ Histogram │ Largest Rectangle │ Pop when smaller bar │
   * │ Special Stack │ Min Stack │ Store extra info │
   * └──────────────────────────────────────────────────────────────────────────┘
   *
   *
   * Khi nào dùng Monotonic Stack?
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ - Tìm "NEXT" greater/smaller element │
   * │ - Tìm "PREVIOUS" greater/smaller element │
   * │ - Tính width dựa trên left/right boundary │
   * │ - Bài về histogram, temperature, stock span │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   *
   * CÁC BÀI THỰC HÀNH
   *
   * EASY:
   * - Valid Parentheses (20)
   * - Min Stack (155)
   *
   * MEDIUM:
   * - Daily Temperatures (739)
   * - Next Greater Element II (503)
   * - Evaluate Reverse Polish Notation (150)
   * - Decode String (394)
   *
   * HARD:
   * - Largest Rectangle in Histogram (84)
   * - Basic Calculator (224, 227, 772)
   * - Maximal Rectangle (85)
   */
}
