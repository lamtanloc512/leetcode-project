package com.practice.leetcode.codility;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                     LESSON 3: TIME COMPLEXITY                               ║
 * ║                    Bài toán về độ phức tạp thời gian                        ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Lesson này tập trung vào việc tối ưu thuật toán để đạt độ phức tạp tốt nhất.
 * Đây là các bài toán cơ bản nhưng cần suy nghĩ để tránh cách giải O(N²).
 */
public class L3_TimeComplexity {

    /**
     * ╔═══════════════════════════════════════════════════════════════════════════╗
     * ║                            FROG JMP                                      ║
     * ║                         Ếch nhảy qua sông                                ║
     * ╚═══════════════════════════════════════════════════════════════════════════╝
     * 
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                    │
     * │ Một con ếch đang ở vị trí X và muốn nhảy đến vị trí >= Y.                  │
     * │ Mỗi lần nhảy, ếch di chuyển một khoảng cách cố định D.                     │
     * │ Tính số lần nhảy tối thiểu để đến đích.                                    │
     * │                                                                             │
     * │ VÍ DỤ:                                                                      │
     * │ X = 10, Y = 85, D = 30                                                     │
     * │ • Khoảng cách cần vượt: 85 - 10 = 75                                       │
     * │ • Sau 1 lần nhảy: 10 + 30 = 40                                             │
     * │ • Sau 2 lần nhảy: 40 + 30 = 70                                             │
     * │ • Sau 3 lần nhảy: 70 + 30 = 100 >= 85 ✓                                    │
     * │ → Đáp án: 3 lần nhảy                                                       │
     * └─────────────────────────────────────────────────────────────────────────────┘
     * 
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ PHÂN TÍCH - TƯ DUY GIẢI QUYẾT:                                             │
     * ├─────────────────────────────────────────────────────────────────────────────┤
     * │                                                                             │
     * │ 🎯 CÁCH SAI (Brute Force - TLE):                                           │
     * │ while (X < Y) { X += D; count++; }                                         │
     * │ → Độ phức tạp O((Y-X)/D) - có thể rất lớn!                                 │
     * │                                                                             │
     * │ 💡 CÁCH ĐÚNG (Toán học - O(1)):                                            │
     * │ • Khoảng cách cần vượt = Y - X                                             │
     * │ • Số lần nhảy = ceil((Y - X) / D)                                          │
     * │ • Công thức: (Y - X + D - 1) / D (để làm tròn lên với số nguyên)          │
     * │   Hoặc: (Y - X - 1) / D + 1 nếu (Y - X) > 0                                │
     * │                                                                             │
     * │ 📊 CÔNG THỨC LÀM TRÒN LÊN CHO SỐ NGUYÊN:                                   │
     * │ ┌─────────────────────────────────────────────────────────────────────────┐│
     * │ │ ceil(a / b) = (a + b - 1) / b                                          ││
     * │ │                                                                         ││
     * │ │ Ví dụ: ceil(75 / 30) = (75 + 30 - 1) / 30 = 104 / 30 = 3                ││
     * │ │        ceil(60 / 30) = (60 + 30 - 1) / 30 = 89 / 30 = 2                 ││
     * │ └─────────────────────────────────────────────────────────────────────────┘│
     * └─────────────────────────────────────────────────────────────────────────────┘
     * 
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỘ PHỨC TẠP:                                                               │
     * ├─────────────────────────────────────────────────────────────────────────────┤
     * │ • Thời gian: O(1) - chỉ là phép tính toán                                  │
     * │ • Không gian: O(1)                                                         │
     * └─────────────────────────────────────────────────────────────────────────────┘
     */
    public int frogJmp(int X, int Y, int D) {
        // Khoảng cách cần di chuyển
        int distance = Y - X;
        
        // Nếu đã ở đích hoặc xa hơn đích
        if (distance <= 0) {
            return 0;
        }
        
        // Số lần nhảy = ceil(distance / D)
        // Sử dụng công thức làm tròn lên: (a + b - 1) / b
        return (distance + D - 1) / D;
    }

    /**
     * ╔═══════════════════════════════════════════════════════════════════════════╗
     * ║                       PERM MISSING ELEM                                   ║
     * ║                    Tìm phần tử bị thiếu trong hoán vị                     ║
     * ╚═══════════════════════════════════════════════════════════════════════════╝
     * 
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                     │
     * │ Cho mảng A chứa N số nguyên khác nhau từ 1 đến N+1 (thiếu một số).          │
     * │ Tìm số bị thiếu.                                                            │
     * │                                                                             │
     * │ VÍ DỤ:                                                                      │
     * │ A = [2, 3, 1, 5]                                                            │
     * │ • Mảng có 4 phần tử → cần chứa các số từ 1 đến 5                            │
     * │ • Thiếu số 4 → Đáp án: 4                                                    │
     * └─────────────────────────────────────────────────────────────────────────────┘
     * 
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ PHÂN TÍCH - TƯ DUY GIẢI QUYẾT:                                              │
     * ├─────────────────────────────────────────────────────────────────────────────┤
     * │                                                                             │
     * │ 💡 CÁCH 1: Sử dụng công thức tổng (Gauss)                                   │
     * │ • Tổng từ 1 đến N+1 = (N+1)(N+2)/2                                          │
     * │ • Tổng các phần tử trong mảng = sum(A)                                      │
     * │ • Số bị thiếu = Tổng lý thuyết - Tổng thực tế                               │
     * │                                                                             │
     * │ 📊 MINH HỌA:                                                                │
     * │ ┌─────────────────────────────────────────────────────────────────────────┐ │
     * │ │ A = [2, 3, 1, 5], N = 4                                                 │ │
     * │ │ Tổng lý thuyết 1..5 = 5 * 6 / 2 = 15                                    │ │
     * │ │ Tổng thực tế = 2 + 3 + 1 + 5 = 11                                       │ │
     * │ │ Số thiếu = 15 - 11 = 4 ✓                                                │ │
     * │ └─────────────────────────────────────────────────────────────────────────┘ │
     * │                                                                             │
     * │ 💡 CÁCH 2: Sử dụng XOR                                                      │
     * │ • XOR tất cả số từ 1 đến N+1                                                │
     * │ • XOR với tất cả phần tử trong mảng                                         │
     * │ • Các số xuất hiện 2 lần sẽ triệt tiêu, còn lại số thiếu                    │
     * │                                                                             │
     * │ ⚠️ LƯU Ý VỀ OVERFLOW:                                                       │
     * │ • Với N lớn, tổng có thể vượt quá int                                       │
     * │ • Nên dùng long để tính toán                                                │
     * └─────────────────────────────────────────────────────────────────────────────┘
     * 
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỘ PHỨC TẠP:                                                                │
     * ├─────────────────────────────────────────────────────────────────────────────┤
     * │ • Thời gian: O(N) - duyệt mảng 1 lần                                        │
     * │ • Không gian: O(1) - không dùng mảng phụ                                    │
     * └─────────────────────────────────────────────────────────────────────────────┘
     */
    public int permMissingElem(int[] A) {
        int N = A.length;
        
        // Tổng từ 1 đến N+1 (dùng long để tránh overflow)
        long expectedSum = (long)(N + 1) * (N + 2) / 2;
        
        // Tổng các phần tử trong mảng
        long actualSum = 0;
        for (int num : A) {
            actualSum += num;
        }
        
        return (int)(expectedSum - actualSum);
    }
    
    /**
     * Cách 2: Sử dụng XOR - tránh overflow hoàn toàn
     */
    public int permMissingElemXOR(int[] A) {
        int N = A.length;
        int xor = 0;
        
        // XOR với tất cả số từ 1 đến N+1
        for (int i = 1; i <= N + 1; i++) {
            xor ^= i;
        }
        
        // XOR với tất cả phần tử trong mảng
        for (int num : A) {
            xor ^= num;
        }
        
        return xor;
    }

    /**
     * ╔═══════════════════════════════════════════════════════════════════════════╗
     * ║                       TAPE EQUILIBRIUM                                    ║
     * ║                    Điểm cân bằng của băng số                              ║
     * ╚═══════════════════════════════════════════════════════════════════════════╝
     * 
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỀ BÀI:                                                                     │
     * │ Cho mảng A có N phần tử. Chia mảng thành 2 phần tại vị trí P (1 ≤ P < N).   │
     * │ Tìm giá trị nhỏ nhất của |sum(A[0..P-1]) - sum(A[P..N-1])|                  │
     * │                                                                             │
     * │ VÍ DỤ:                                                                      │
     * │ A = [3, 1, 2, 4, 3]                                                         │
     * │ P=1: |3| - |1+2+4+3| = |3 - 10| = 7                                         │
     * │ P=2: |3+1| - |2+4+3| = |4 - 9| = 5                                          │
     * │ P=3: |3+1+2| - |4+3| = |6 - 7| = 1  ← Nhỏ nhất                              │
     * │ P=4: |3+1+2+4| - |3| = |10 - 3| = 7                                         │
     * └─────────────────────────────────────────────────────────────────────────────┘
     * 
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ PHÂN TÍCH - TƯ DUY GIẢI QUYẾT:                                              │
     * ├─────────────────────────────────────────────────────────────────────────────┤
     * │                                                                             │
     * │ 🎯 CÁCH SAI (O(N²)):                                                        │
     * │ For mỗi P, tính tổng 2 phần → Tốn O(N) cho mỗi P                            │
     * │                                                                             │
     * │ 💡 CÁCH ĐÚNG (O(N)):                                                        │
     * │ • Tính tổng toàn bộ mảng trước                                              │
     * │ • Duyệt qua mảng, duy trì tổng bên trái (leftSum)                           │
     * │ • Tổng bên phải = totalSum - leftSum                                        │
     * │                                                                             │
     * │ 📊 MINH HỌA:                                                                │
     * │ ┌─────────────────────────────────────────────────────────────────────────┐ │
     * │ │ A = [3, 1, 2, 4, 3], total = 13                                         │ │
     * │ │                                                                         │ │
     * │ │ P=1: left=3, right=13-3=10, diff=|3-10|=7                               │ │
     * │ │ P=2: left=4, right=13-4=9, diff=|4-9|=5                                 │ │
     * │ │ P=3: left=6, right=13-6=7, diff=|6-7|=1 ← min                           │ │
     * │ │ P=4: left=10, right=13-10=3, diff=|10-3|=7                              │ │
     * │ └─────────────────────────────────────────────────────────────────────────┘ │
     * └─────────────────────────────────────────────────────────────────────────────┘
     * 
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ ĐỘ PHỨC TẠP:                                                                │
     * ├─────────────────────────────────────────────────────────────────────────────┤
     * │ • Thời gian: O(N) - duyệt mảng 2 lần                                        │
     * │ • Không gian: O(1)                                                          │
     * └─────────────────────────────────────────────────────────────────────────────┘
     */
    public int tapeEquilibrium(int[] A) {
        int N = A.length;
        
        // Tính tổng toàn bộ mảng
        long totalSum = 0;
        for (int num : A) {
            totalSum += num;
        }
        
        long leftSum = 0;
        int minDiff = Integer.MAX_VALUE;
        
        // Duyệt từ 0 đến N-2 (P từ 1 đến N-1)
        for (int i = 0; i < N - 1; i++) {
            leftSum += A[i];
            long rightSum = totalSum - leftSum;
            int diff = (int) Math.abs(leftSum - rightSum);
            minDiff = Math.min(minDiff, diff);
        }
        
        return minDiff;
    }
    
    // ================================ TESTS ================================
    
    @Test
    void testFrogJmp_Example() {
        assertEquals(3, frogJmp(10, 85, 30));
    }
    
    @Test
    void testFrogJmp_ExactDivision() {
        assertEquals(2, frogJmp(10, 70, 30)); // 60 / 30 = 2
    }
    
    @Test
    void testFrogJmp_AlreadyAtTarget() {
        assertEquals(0, frogJmp(10, 10, 30));
    }
    
    @Test
    void testFrogJmp_SingleJump() {
        assertEquals(1, frogJmp(1, 5, 10));
    }
    
    @Test
    void testPermMissingElem_Example() {
        int[] A = {2, 3, 1, 5};
        assertEquals(4, permMissingElem(A));
    }
    
    @Test
    void testPermMissingElem_MissingFirst() {
        int[] A = {2, 3, 4, 5};
        assertEquals(1, permMissingElem(A));
    }
    
    @Test
    void testPermMissingElem_MissingLast() {
        int[] A = {1, 2, 3, 4};
        assertEquals(5, permMissingElem(A));
    }
    
    @Test
    void testPermMissingElem_Empty() {
        int[] A = {};
        assertEquals(1, permMissingElem(A));
    }
    
    @Test
    void testTapeEquilibrium_Example() {
        int[] A = {3, 1, 2, 4, 3};
        assertEquals(1, tapeEquilibrium(A));
    }
    
    @Test
    void testTapeEquilibrium_TwoElements() {
        int[] A = {3, 1};
        assertEquals(2, tapeEquilibrium(A));
    }
    
    @Test
    void testTapeEquilibrium_AllSame() {
        int[] A = {5, 5, 5, 5};
        assertEquals(0, tapeEquilibrium(A));
    }
}
