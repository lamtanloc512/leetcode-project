package com.practice.leetcode.codility;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                   LESSON 7: STACKS AND QUEUES                               ║
 * ║                      Ngăn xếp và hàng đợi                                   ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class L7_StacksQueues {

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                           BRACKETS
     *                   Kiểm tra dấu ngoặc hợp lệ
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Kiểm tra chuỗi chứa các dấu ngoặc (), [], {} có hợp lệ không.
     * 
     * VÍ DỤ:
     * "{[()()]}" → hợp lệ → return 1
     * "([)()]" → không hợp lệ → return 0
     * 
     * TƯ DUY: Sử dụng Stack
     * • Gặp ngoặc mở → push vào stack
     * • Gặp ngoặc đóng → kiểm tra stack top có khớp không
     * • Cuối cùng stack phải rỗng
     * 
     * ĐỘ PHỨC TẠP: O(N) thời gian, O(N) không gian
     */
    public int brackets(String S) {
        Deque<Character> stack = new ArrayDeque<>();
        
        for (char c : S.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return 0;
                
                char top = stack.pop();
                if ((c == ')' && top != '(') ||
                    (c == ']' && top != '[') ||
                    (c == '}' && top != '{')) {
                    return 0;
                }
            }
        }
        
        return stack.isEmpty() ? 1 : 0;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                            FISH
     *                   Mô phỏng cá bơi ngược chiều
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI:
     * N con cá bơi trong sông. A[i] = kích thước, B[i] = hướng bơi (0=lên, 1=xuống).
     * Cá bơi ngược chiều gặp nhau → cá lớn ăn cá nhỏ.
     * Đếm số cá còn sống.
     * 
     * VÍ DỤ: A = [4, 3, 2, 1, 5], B = [0, 1, 0, 0, 0]
     * • Cá 0 (size 4) bơi lên, không gặp ai phía trên
     * • Cá 1 (size 3) bơi xuống, gặp cá 2,3,4 bơi lên → ăn hết 2,3 nhưng bị 5 ăn
     * • Kết quả: 2 con sống (cá 0 và cá 4)
     * 
     * TƯ DUY: Stack lưu cá đang bơi xuống
     * • Cá bơi xuống (1) → push vào stack
     * • Cá bơi lên (0) → "đấu" với các cá trong stack cho đến khi thắng hoặc bị ăn
     */
    public int fish(int[] A, int[] B) {
        int N = A.length;
        Deque<Integer> downstream = new ArrayDeque<>(); // Stack cá bơi xuống
        int alive = 0;
        
        for (int i = 0; i < N; i++) {
            if (B[i] == 1) {
                // Cá bơi xuống → push vào stack
                downstream.push(A[i]);
            } else {
                // Cá bơi lên → đấu với cá trong stack
                while (!downstream.isEmpty() && downstream.peek() < A[i]) {
                    downstream.pop(); // Cá trong stack bị ăn
                }
                
                if (downstream.isEmpty()) {
                    // Không còn cá bơi xuống → cá này sống
                    alive++;
                }
                // Nếu stack không rỗng, cá này bị ăn (không làm gì)
            }
        }
        
        // Cá còn trong stack (bơi xuống) cũng sống
        return alive + downstream.size();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                           NESTING
     *                    Kiểm tra ngoặc đơn lồng nhau
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Kiểm tra chuỗi chỉ chứa () có hợp lệ không.
     * 
     * TƯ DUY: Chỉ cần đếm, không cần stack thực sự
     * • Gặp ( → tăng counter
     * • Gặp ) → giảm counter, nếu < 0 → không hợp lệ
     * • Cuối cùng counter phải = 0
     */
    public int nesting(String S) {
        int depth = 0;
        
        for (char c : S.toCharArray()) {
            if (c == '(') {
                depth++;
            } else {
                depth--;
                if (depth < 0) return 0;
            }
        }
        
        return depth == 0 ? 1 : 0;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                          STONE WALL
     *                    Xây tường với số khối tối thiểu
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI:
     * Xây tường với chiều cao thay đổi H[i] tại vị trí i.
     * Tìm số khối hình chữ nhật TỐI THIỂU cần dùng.
     * 
     * VÍ DỤ: H = [8, 8, 5, 7, 9, 8, 7, 4, 8]
     * Cần 7 khối
     * 
     * TƯ DUY: Stack lưu chiều cao các khối đang "mở"
     * • Chiều cao tăng → thêm khối mới (push)
     * • Chiều cao giảm → đóng khối cũ (pop) cho đến khi <= chiều cao mới
     * • Chiều cao bằng → có thể tái sử dụng khối
     */
    public int stoneWall(int[] H) {
        Deque<Integer> stack = new ArrayDeque<>();
        int blocks = 0;
        
        for (int height : H) {
            // Đóng các khối cao hơn chiều cao hiện tại
            while (!stack.isEmpty() && stack.peek() > height) {
                stack.pop();
            }
            
            if (stack.isEmpty() || stack.peek() < height) {
                // Cần thêm khối mới
                stack.push(height);
                blocks++;
            }
            // Nếu stack.peek() == height → tái sử dụng khối
        }
        
        return blocks;
    }
    
    // ================================ TESTS ================================
    
    @Test
    void testBrackets() {
        assertEquals(1, brackets("{[()()]}"));
        assertEquals(0, brackets("([)()]"));
        assertEquals(1, brackets(""));
    }
    
    @Test
    void testFish() {
        assertEquals(2, fish(new int[]{4, 3, 2, 1, 5}, new int[]{0, 1, 0, 0, 0}));
        assertEquals(1, fish(new int[]{1, 2}, new int[]{1, 0})); // Cá 2 ăn cá 1
    }
    
    @Test
    void testNesting() {
        assertEquals(1, nesting("(()(())())"));
        assertEquals(0, nesting("())"));
    }
    
    @Test
    void testStoneWall() {
        assertEquals(7, stoneWall(new int[]{8, 8, 5, 7, 9, 8, 7, 4, 8}));
    }
}
