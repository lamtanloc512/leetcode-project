package com.practice.leetcode.hackerrank;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Implement Stack với 4 operations, TẤT CẢ phải O(1):
 * 
 * 1. push(x) - Thêm x vào stack
 * 2. pop()   - Xóa top element
 * 3. top()   - Xem top element (không xóa)
 * 4. getMin() - Lấy MINIMUM element trong stack ⭐ KEY!
 * 
 * Challenge: getMin() phải O(1)!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Operations: ["push 2", "push 0", "push 3", "push 0", "getMin", "pop", "getMin"]
 * 
 * push(2)   → stack=[2], min=2
 * push(0)   → stack=[2,0], min=0
 * push(3)   → stack=[2,0,3], min=0
 * push(0)   → stack=[2,0,3,0], min=0
 * getMin()  → return 0
 * pop()     → stack=[2,0,3], min=0
 * getMin()  → return 0
 * 
 * Output: [0, 0]
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * Ý TƯỞNG CHÍNH - 2 STACKS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Stack 1 (mainStack): Normal stack chứa elements
 * Stack 2 (minStack):  Track minimum tại mỗi level
 * 
 * Visualize:
 * 
 * After push(2), push(0), push(3):
 * 
 * mainStack:  [2, 0, 3]
 *              ↑  ↑  ↑
 * minStack:   [2, 0, 0]
 *              ↑  ↑  ↑
 *             min min min
 *             @   @   @
 *            lvl lvl lvl
 *             1   2   3
 * 
 * minStack[i] = minimum của mainStack[0..i]
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC HACKERRANK
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * [Original problem as shown in image]
 */
class MinTrackingStackTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // SOLUTION: TWO STACKS
    // ═══════════════════════════════════════════════════════════════════════════
    
    static class MinStack {
        
        private Deque<Integer> mainStack;  // Normal stack
        private Deque<Integer> minStack;   // Track min
        
        public MinStack() {
            mainStack = new ArrayDeque<>();
            minStack = new ArrayDeque<>();
        }
        
        /**
         * Push element x
         * 
         * Time: O(1)
         */
        public void push(int x) {
            mainStack.push(x);
            
            // ⭐ KEY: Update minStack
            if (minStack.isEmpty()) {
                minStack.push(x);  // First element
            } else {
                // Push min(x, current min)
                int currentMin = minStack.peek();
                minStack.push(Math.min(x, currentMin));
            }
        }
        
        /**
         * Remove top element
         * 
         * Time: O(1)
         */
        public void pop() {
            mainStack.pop();
            minStack.pop();  // ⭐ Sync with mainStack!
        }
        
        /**
         * Get top element (without removing)
         * 
         * Time: O(1)
         */
        public int top() {
            return mainStack.peek();
        }
        
        /**
         * Get minimum element
         * 
         * Time: O(1) ⭐ THIS IS THE MAGIC!
         */
        public int getMin() {
            return minStack.peek();  // ⭐ Just peek minStack!
        }
        
        public boolean isEmpty() {
            return mainStack.isEmpty();
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HACKERRANK SOLUTION - Process operations from List<String>
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * HackerRank format:
     * 
     * Input: ["push 2", "push 0", "push 3", "getMin", "pop", "getMin"]
     * 
     * Operations:
     * - "push x" → push value x
     * - "pop"    → pop top
     * - "top"    → return top value
     * - "getMin" → return minimum value
     * 
     * Output: Only values from "top" and "getMin" operations
     */
    public static List<Integer> processCouponStackOperations(List<String> operations) {
        MinStack stack = new MinStack();
        List<Integer> result = new ArrayList<>();
        
        for (String operation : operations) {
            // Tokenize: split by space
            String[] tokens = operation.split(" ");
            String command = tokens[0];
            
            switch (command) {
                case "push":
                    int value = Integer.parseInt(tokens[1]);
                    stack.push(value);
                    break;
                    
                case "pop":
                    if (!stack.isEmpty()) {
                        stack.pop();
                    }
                    break;
                    
                case "top":
                    if (!stack.isEmpty()) {
                        result.add(stack.top());
                    }
                    break;
                    
                case "getMin":
                    if (!stack.isEmpty()) {
                        result.add(stack.getMin());
                    }
                    break;
                    
                default:
                    // Ignore unknown operations
                    break;
            }
        }
        
        return result;
    }
    
    // ⭐ Wrapper for testing
    public List<Integer> solution(String[] operations) {
        return processCouponStackOperations(Arrays.asList(operations));
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TRACE VÍ DỤ CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Operations: ["push 2", "push 0", "push 3", "push 0", "getMin", "pop", "getMin", "pop", "top"]
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * Initial: mainStack=[], minStack=[]
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * push(2):
     *   mainStack=[2]
     *   minStack=[2]     ← min so far = 2
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * push(0):
     *   mainStack=[2, 0]
     *   minStack=[2, 0]  ← min(0, 2) = 0
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * push(3):
     *   mainStack=[2, 0, 3]
     *   minStack=[2, 0, 0]  ← min(3, 0) = 0 (duplicate!)
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * push(0):
     *   mainStack=[2, 0, 3, 0]
     *   minStack=[2, 0, 0, 0]  ← min(0, 0) = 0
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * getMin():
     *   minStack.peek() = 0  ⭐ O(1)!
     *   Output: 0
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * pop():
     *   mainStack=[2, 0, 3]     ← remove 0
     *   minStack=[2, 0, 0]      ← remove 0
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * getMin():
     *   minStack.peek() = 0  ⭐ Still 0!
     *   Output: 0
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * pop():
     *   mainStack=[2, 0]
     *   minStack=[2, 0]
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * top():
     *   mainStack.peek() = 0
     *   Output: 0
     * 
     * Final Output: [0, 0, 0]
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TẠI SAO SOLUTION NÀY ĐÚNG?
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Key Invariant (Bất biến):
     * 
     * minStack[i] = min(mainStack[0], mainStack[1], ..., mainStack[i])
     * 
     * Tại mọi thời điểm:
     * - minStack.peek() = min của TẤT CẢ elements trong mainStack
     * 
     * Khi pop():
     * - Remove cả main và min → minStack.peek() vẫn là min của phần còn lại!
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Example:
     * 
     * mainStack: [5, 2, 7, 1, 3]
     * minStack:  [5, 2, 2, 1, 1]
     *             ↑  ↑  ↑  ↑  ↑
     *            min min min min min
     *            of  of  of  of  of
     *            [5] [5,2] [5,2,7] [5,2,7,1] [5,2,7,1,3]
     * 
     * After pop(): mainStack=[5,2,7,1]
     *             minStack=[5,2,2,1]
     *                           ↑
     *                          peek()=1 ✓ Correct!
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Example from HackerRank")
    void testExample() {
        String[] ops = {
            "push 2", "push 0", "push 3", "push 0", 
            "getMin", "pop", "getMin", "pop", "top"
        };
        
        List<Integer> result = solution(ops);
        assertThat(result).containsExactly(0, 0, 0);
    }
    
    @Test
    @DisplayName("Simple push and getMin")
    void testSimple() {
        MinStack stack = new MinStack();
        stack.push(5);
        stack.push(2);
        stack.push(7);
        
        assertThat(stack.getMin()).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Pop and getMin updates correctly")
    void testPopMin() {
        MinStack stack = new MinStack();
        stack.push(5);
        stack.push(2);
        stack.push(7);
        
        assertThat(stack.getMin()).isEqualTo(2);
        
        stack.pop(); // Remove 7
        assertThat(stack.getMin()).isEqualTo(2);
        
        stack.pop(); // Remove 2
        assertThat(stack.getMin()).isEqualTo(5);
    }
    
    @Test
    @DisplayName("All same values")
    void testAllSame() {
        MinStack stack = new MinStack();
        stack.push(3);
        stack.push(3);
        stack.push(3);
        
        assertThat(stack.getMin()).isEqualTo(3);
        stack.pop();
        assertThat(stack.getMin()).isEqualTo(3);
    }
    
    @Test
    @DisplayName("Decreasing values")
    void testDecreasing() {
        MinStack stack = new MinStack();
        stack.push(5);
        assertThat(stack.getMin()).isEqualTo(5);
        
        stack.push(3);
        assertThat(stack.getMin()).isEqualTo(3);
        
        stack.push(1);
        assertThat(stack.getMin()).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Increasing values")
    void testIncreasing() {
        MinStack stack = new MinStack();
        stack.push(1);
        stack.push(3);
        stack.push(5);
        
        assertThat(stack.getMin()).isEqualTo(1);
        assertThat(stack.top()).isEqualTo(5);
    }
}
