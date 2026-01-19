package com.practice.leetcode.stack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Solutions and tests for Daily Temperatures problem.
 */
class DailyTemperaturesSolutionTest {

  // ========== IMPERATIVE SOLUTION ==========
  /**
   * THUẬT TOÁN: Monotonic Stack (Stack đơn điệu giảm)
   *
   * Bài toán: Tìm số ngày phải chờ để gặp nhiệt độ cao hơn.
   *
   * Ý tưởng: Dùng stack lưu INDEX của các ngày chưa tìm được ngày nóng hơn.
   * Stack duy trì tính chất: nhiệt độ giảm dần từ đáy lên đỉnh.
   *
   * Cách hoạt động:
   * 1. Duyệt từng ngày i
   * 2. Nếu temps[i] > temps[stack.top]:
   * - Ngày stack.top đã tìm được ngày nóng hơn (ngày i)
   * - Pop và tính khoảng cách: i - stack.top
   * 3. Push i vào stack (chờ tìm ngày nóng hơn)
   *
   * Ví dụ: temps = [73, 74, 75, 71, 69, 72, 76, 73]
   *
   * i=0: stack=[] → push 0, stack=[0]
   * i=1: 74>73 → pop 0, result[0]=1-0=1; push 1, stack=[1]
   * i=2: 75>74 → pop 1, result[1]=2-1=1; push 2, stack=[2]
   * i=3: 71<75 → push 3, stack=[2,3]
   * i=4: 69<71 → push 4, stack=[2,3,4]
   * i=5: 72>69 → pop 4, result[4]=5-4=1
   * 72>71 → pop 3, result[3]=5-3=2; push 5, stack=[2,5]
   * i=6: 76>72 → pop 5, result[5]=6-5=1
   * 76>75 → pop 2, result[2]=6-2=4; push 6, stack=[6]
   * i=7: 73<76 → push 7, stack=[6,7]
   *
   * Các index còn trong stack (6,7) không có ngày nóng hơn → result=0
   *
   * Kết quả: [1, 1, 4, 2, 1, 1, 0, 0]
   *
   * Time: O(n) - mỗi phần tử push/pop tối đa 1 lần
   * Space: O(n) - stack chứa tối đa n phần tử
   */
  static class ImperativeSolution {
    public int[] dailyTemperatures(int[] temperatures) {
      int n = temperatures.length;
      int[] result = new int[n];
      Deque<Integer> stack = new ArrayDeque<>(); // stores indices

      for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
          int prevIndex = stack.pop();
          result[prevIndex] = i - prevIndex;
        }
        stack.push(i);
      }

      return result;
    }
  }

  // ========== FUNCTIONAL/STREAM SOLUTION ==========
  static class FunctionalSolution {
    /**
     * Uses reduce with stack to process temperatures.
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] dailyTemperatures(int[] temperatures) {
      int n = temperatures.length;
      int[] result = new int[n];
      Deque<Integer> stack = new ArrayDeque<>();

      IntStream.range(0, n).forEach(i -> {
        while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
          int prevIndex = stack.pop();
          result[prevIndex] = i - prevIndex;
        }
        stack.push(i);
      });

      return result;
    }
  }

  // ========== TESTS ==========

  private final ImperativeSolution imperativeSolution = new ImperativeSolution();
  private final FunctionalSolution functionalSolution = new FunctionalSolution();

  @Nested
  @DisplayName("Imperative Solution Tests")
  class ImperativeTests {

    @Test
    @DisplayName("Example 1: [73,74,75,71,69,72,76,73] -> [1,1,4,2,1,1,0,0]")
    void testExample1() {
      int[] temperatures = { 73, 74, 75, 71, 69, 72, 76, 73 };
      assertThat(imperativeSolution.dailyTemperatures(temperatures))
          .containsExactly(1, 1, 4, 2, 1, 1, 0, 0);
    }

    @Test
    @DisplayName("Example 2: [30,40,50,60] -> [1,1,1,0]")
    void testExample2() {
      int[] temperatures = { 30, 40, 50, 60 };
      assertThat(imperativeSolution.dailyTemperatures(temperatures))
          .containsExactly(1, 1, 1, 0);
    }

    @Test
    @DisplayName("Example 3: [30,60,90] -> [1,1,0]")
    void testExample3() {
      int[] temperatures = { 30, 60, 90 };
      assertThat(imperativeSolution.dailyTemperatures(temperatures))
          .containsExactly(1, 1, 0);
    }

    @Test
    @DisplayName("Single element")
    void testSingleElement() {
      int[] temperatures = { 50 };
      assertThat(imperativeSolution.dailyTemperatures(temperatures))
          .containsExactly(0);
    }

    @Test
    @DisplayName("Decreasing temperatures")
    void testDecreasing() {
      int[] temperatures = { 90, 80, 70, 60 };
      assertThat(imperativeSolution.dailyTemperatures(temperatures))
          .containsExactly(0, 0, 0, 0);
    }
  }

  @Nested
  @DisplayName("Functional Solution Tests")
  class FunctionalTests {

    @Test
    @DisplayName("Example 1: [73,74,75,71,69,72,76,73] -> [1,1,4,2,1,1,0,0]")
    void testExample1() {
      int[] temperatures = { 73, 74, 75, 71, 69, 72, 76, 73 };
      assertThat(functionalSolution.dailyTemperatures(temperatures))
          .containsExactly(1, 1, 4, 2, 1, 1, 0, 0);
    }

    @Test
    @DisplayName("Example 2: [30,40,50,60] -> [1,1,1,0]")
    void testExample2() {
      int[] temperatures = { 30, 40, 50, 60 };
      assertThat(functionalSolution.dailyTemperatures(temperatures))
          .containsExactly(1, 1, 1, 0);
    }

    @Test
    @DisplayName("Example 3: [30,60,90] -> [1,1,0]")
    void testExample3() {
      int[] temperatures = { 30, 60, 90 };
      assertThat(functionalSolution.dailyTemperatures(temperatures))
          .containsExactly(1, 1, 0);
    }

    @Test
    @DisplayName("Single element")
    void testSingleElement() {
      int[] temperatures = { 50 };
      assertThat(functionalSolution.dailyTemperatures(temperatures))
          .containsExactly(0);
    }

    @Test
    @DisplayName("Decreasing temperatures")
    void testDecreasing() {
      int[] temperatures = { 90, 80, 70, 60 };
      assertThat(functionalSolution.dailyTemperatures(temperatures))
          .containsExactly(0, 0, 0, 0);
    }
  }
}
