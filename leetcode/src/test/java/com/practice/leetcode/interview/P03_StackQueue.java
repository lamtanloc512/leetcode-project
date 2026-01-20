package com.practice.leetcode.interview;

import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════════════════════════╗
 * ║                              STACK & QUEUE PROBLEMS                                              ║
 * ║                        LIFO (Stack) vs FIFO (Queue) Classics                                     ║
 * ╚══════════════════════════════════════════════════════════════════════════════════════════════════╝
 * 
 * 📊 STACK thường dùng cho:
 * • Matching (parentheses, brackets)
 * • Monotonic stack (next greater element)
 * • Expression evaluation
 * • Undo/Redo operations
 * 
 * 📊 QUEUE thường dùng cho:
 * • BFS (Breadth-First Search)
 * • Level-order traversal
 * • Sliding window problems (Deque)
 */
public class P03_StackQueue {

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 20 - VALID PARENTHESES ⭐⭐⭐
    //                     https://leetcode.com/problems/valid-parentheses/
    //                              (Bài Stack kinh điển nhất)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Kiểm tra chuỗi ngoặc có hợp lệ không. Ngoặc: '()', '{}', '[]'                               │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ "()" → true                                                                                 │
     * │ "()[]{}" → true                                                                             │
     * │ "(]" → false                                                                                │
     * │ "([)]" → false                                                                              │
     * │ "{[]}" → true                                                                               │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Gặp ngoặc mở → push vào stack                                                             │
     * │ • Gặp ngoặc đóng → pop và kiểm tra có match không                                           │
     * │ • Cuối cùng stack phải rỗng                                                                 │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(N) không gian                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> pairs = Map.of(')', '(', '}', '{', ']', '[');
        
        for (char c : s.toCharArray()) {
            if (pairs.containsValue(c)) {
                // Ngoặc mở
                stack.push(c);
            } else if (pairs.containsKey(c)) {
                // Ngoặc đóng
                if (stack.isEmpty() || stack.pop() != pairs.get(c)) {
                    return false;
                }
            }
        }
        
        return stack.isEmpty();
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 155 - MIN STACK ⭐⭐
    //                          https://leetcode.com/problems/min-stack/
    //                       (Design Stack với getMin() O(1))
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Thiết kế Stack với các operation O(1):                                                      │
     * │ • push(x), pop(), top(), getMin()                                                           │
     * │                                                                                             │
     * │ TƯ DUY: Dùng 2 stacks hoặc lưu cặp (value, minSoFar)                                        │
     * │                                                                                             │
     * │ CÁCH 1: 2 Stacks (1 cho values, 1 cho min)                                                  │
     * │ CÁCH 2: 1 Stack lưu (value, currentMin) - tiết kiệm hơn                                     │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    class MinStack {
        private Stack<int[]> stack; // [value, minSoFar]
        
        public MinStack() {
            stack = new Stack<>();
        }
        
        public void push(int val) {
            int currentMin = stack.isEmpty() ? val : Math.min(val, stack.peek()[1]);
            stack.push(new int[]{val, currentMin});
        }
        
        public void pop() {
            stack.pop();
        }
        
        public int top() {
            return stack.peek()[0];
        }
        
        public int getMin() {
            return stack.peek()[1];
        }
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 739 - DAILY TEMPERATURES ⭐⭐
    //                     https://leetcode.com/problems/daily-temperatures/
    //                              (Monotonic Stack classic)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Với mỗi ngày, tìm số ngày phải đợi để có nhiệt độ cao hơn.                                  │
     * │ Nếu không có ngày nào ấm hơn, trả về 0.                                                     │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ [73, 74, 75, 71, 69, 72, 76, 73]                                                            │
     * │ Output: [1, 1, 4, 2, 1, 1, 0, 0]                                                            │
     * │                                                                                             │
     * │ TƯ DUY: Monotonic Decreasing Stack                                                          │
     * │ • Stack chứa INDEX của các nhiệt độ chưa tìm được ngày ấm hơn                               │
     * │ • Khi gặp nhiệt độ cao hơn → pop và tính khoảng cách                                        │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(N) không gian                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] result = new int[n];
        Stack<Integer> stack = new Stack<>(); // Lưu index
        
        for (int i = 0; i < n; i++) {
            // Pop tất cả các ngày có nhiệt độ thấp hơn
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prevDay = stack.pop();
                result[prevDay] = i - prevDay;
            }
            stack.push(i);
        }
        
        return result;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 150 - EVALUATE REVERSE POLISH NOTATION ⭐⭐
    //               https://leetcode.com/problems/evaluate-reverse-polish-notation/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tính giá trị biểu thức hậu tố (Reverse Polish Notation).                                    │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ ["2", "1", "+", "3", "*"] → (2 + 1) * 3 = 9                                                 │
     * │ ["4", "13", "5", "/", "+"] → 4 + (13 / 5) = 6                                               │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Gặp số → push vào stack                                                                   │
     * │ • Gặp operator → pop 2 số, tính toán, push kết quả                                          │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(N) không gian                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        Set<String> operators = Set.of("+", "-", "*", "/");
        Map<String, BiFunction<Integer, Integer, Integer>> map = new HashMap<>();   
        map.put("+", (x, y) -> x + y);
        map.put("-", (x, y) -> x - y);
        map.put("*", (x, y) -> x * y);
        map.put("/", (x, y) -> x / y);

        for (String token : tokens) {
            if (operators.contains(token)) {
                int b = stack.pop();
                int a = stack.pop();
                int result = map.get(token).apply(b, a);
                stack.push(result);
            } else {
                stack.push(Integer.parseInt(token));
            }
        }

        return stack.pop();
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 232 - IMPLEMENT QUEUE USING STACKS ⭐
    //               https://leetcode.com/problems/implement-queue-using-stacks/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Implement Queue chỉ dùng 2 Stacks.                                                          │
     * │                                                                                             │
     * │ TƯ DUY:                                                                                     │
     * │ • Stack input: để push                                                                      │
     * │ • Stack output: để pop/peek                                                                 │
     * │ • Khi output rỗng → chuyển hết từ input sang output                                         │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: Amortized O(1) cho mỗi operation                                               │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    class MyQueue {
        private Stack<Integer> input;
        private Stack<Integer> output;
        
        public MyQueue() {
            input = new Stack<>();
            output = new Stack<>();
        }
        
        public void push(int x) {
            input.push(x);
        }
        
        public int pop() {
            peek(); // Đảm bảo output có data
            return output.pop();
        }
        
        public int peek() {
            if (output.isEmpty()) {
                while (!input.isEmpty()) {
                    output.push(input.pop());
                }
            }
            return output.peek();
        }
        
        public boolean empty() {
            return input.isEmpty() && output.isEmpty();
        }
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 239 - SLIDING WINDOW MAXIMUM ⭐⭐⭐
    //                https://leetcode.com/problems/sliding-window-maximum/
    //                              (Monotonic Deque - HARD)
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ Tìm max trong mỗi sliding window kích thước k.                                              │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums = [1,3,-1,-3,5,3,6,7], k = 3                                                           │
     * │ Output: [3, 3, 5, 5, 6, 7]                                                                  │
     * │                                                                                             │
     * │ TƯ DUY: Monotonic Decreasing Deque                                                          │
     * │ • Deque luôn giữ các phần tử theo thứ tự giảm dần                                           │
     * │ • Front của deque luôn là max của window hiện tại                                           │
     * │ • Khi thêm phần tử mới, xóa tất cả phần tử nhỏ hơn từ back                                  │
     * │                                                                                             │
     * │ ĐỘ PHỨC TẠP: O(N) thời gian, O(K) không gian                                                │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length == 0 || k == 0) return new int[0];
        
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>(); // Lưu index
        
        for (int i = 0; i < n; i++) {
            // Xóa phần tử ngoài window
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            
            // Xóa các phần tử nhỏ hơn từ back
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            
            deque.offerLast(i);
            
            // Thêm vào kết quả khi window đủ lớn
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        
        return result;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                              LC 496 - NEXT GREATER ELEMENT I ⭐
    //                https://leetcode.com/problems/next-greater-element-i/
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                                     │
     * │ nums1 là subset của nums2. Với mỗi phần tử trong nums1, tìm phần tử lớn hơn                 │
     * │ kế tiếp nó trong nums2.                                                                     │
     * │                                                                                             │
     * │ VÍ DỤ:                                                                                      │
     * │ nums1 = [4,1,2], nums2 = [1,3,4,2]                                                          │
     * │ Output: [-1, 3, -1]                                                                         │
     * │ • 4 không có số lớn hơn sau nó → -1                                                         │
     * │ • 1 có 3 sau nó → 3                                                                         │
     * │ • 2 không có số lớn hơn sau nó → -1                                                         │
     * │                                                                                             │
     * │ TƯ DUY: Monotonic Stack + HashMap                                                           │
     * │ • Tính next greater cho tất cả phần tử trong nums2                                          │
     * │ • Lưu vào HashMap                                                                           │
     * │ • Tra cứu cho nums1                                                                         │
     * └─────────────────────────────────────────────────────────────────────────────────────────────┘
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> nextGreater = new HashMap<>();
        Stack<Integer> stack = new Stack<>();
        
        // Tính next greater cho nums2
        for (int num : nums2) {
            while (!stack.isEmpty() && stack.peek() < num) {
                nextGreater.put(stack.pop(), num);
            }
            stack.push(num);
        }
        
        // Các phần tử còn trong stack không có next greater
        while (!stack.isEmpty()) {
            nextGreater.put(stack.pop(), -1);
        }
        
        // Tra cứu cho nums1
        int[] result = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            result[i] = nextGreater.get(nums1[i]);
        }
        
        return result;
    }

    // ════════════════════════════════════════════════════════════════════════════════════════════════
    //                                         TESTS
    // ════════════════════════════════════════════════════════════════════════════════════════════════
    
    @Test
    void testIsValid() {
        assertTrue(isValid("()"));
        assertTrue(isValid("()[]{}"));
        assertFalse(isValid("(]"));
        assertFalse(isValid("([)]"));
        assertTrue(isValid("{[]}"));
    }
    
    @Test
    void testMinStack() {
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        assertEquals(-3, minStack.getMin());
        minStack.pop();
        assertEquals(0, minStack.top());
        assertEquals(-2, minStack.getMin());
    }
    
    @Test
    void testDailyTemperatures() {
        assertArrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, 
                         dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));
    }
    
    @Test
    void testEvalRPN() {
        assertEquals(9, evalRPN(new String[]{"2", "1", "+", "3", "*"}));
        assertEquals(6, evalRPN(new String[]{"4", "13", "5", "/", "+"}));
    }
    
    @Test
    void testMyQueue() {
        MyQueue queue = new MyQueue();
        queue.push(1);
        queue.push(2);
        assertEquals(1, queue.peek());
        assertEquals(1, queue.pop());
        assertFalse(queue.empty());
    }
    
    @Test
    void testMaxSlidingWindow() {
        assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7}, 
                         maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
    }
    
    @Test
    void testNextGreaterElement() {
        assertArrayEquals(new int[]{-1, 3, -1}, 
                         nextGreaterElement(new int[]{4, 1, 2}, new int[]{1, 3, 4, 2}));
    }
}
