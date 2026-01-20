package com.practice.leetcode.codility;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                   LESSON 9: MAXIMUM SLICE PROBLEM                           ║
 * ║                    Bài toán lát cắt lớn nhất                                ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Đây là nhóm bài toán quan trọng sử dụng KADANE'S ALGORITHM
 * - Thuật toán tìm subarray có tổng lớn nhất trong O(N)
 */
public class L9_MaxSlice {

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                          MAX PROFIT
     *                   Lợi nhuận mua bán cổ phiếu
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Cho mảng giá cổ phiếu A[i] vào ngày i.
     * Tìm lợi nhuận tối đa khi mua 1 ngày và bán sau đó.
     * 
     * VÍ DỤ: A = [23171, 21011, 21123, 21366, 21013, 21367]
     * Mua ngày 1 (21011), bán ngày 5 (21367) → lợi nhuận = 356
     * 
     * TƯ DUY:
     * • Track giá mua thấp nhất cho đến hiện tại
     * • Với mỗi ngày, tính lợi nhuận nếu bán hôm nay
     * • Cập nhật max profit
     * 
     * ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian
     */
    public int maxProfit(int[] A) {
        int N = A.length;
        if (N < 2) return 0;
        
        int minPrice = A[0];
        int maxProfit = 0;
        
        for (int i = 1; i < N; i++) {
            // Profit nếu bán hôm nay
            maxProfit = Math.max(maxProfit, A[i] - minPrice);
            // Cập nhật giá mua thấp nhất
            minPrice = Math.min(minPrice, A[i]);
        }
        
        return maxProfit;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                         MAX SLICE SUM
     *                  Tổng subarray lớn nhất (Kadane's)
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Tìm tổng lớn nhất của một slice (subarray liên tục không rỗng).
     * 
     * VÍ DỤ: A = [3, 2, -6, 4, 0] → max slice = [4, 0] hoặc [3, 2] = 5
     * 
     * KADANE'S ALGORITHM:
     * 
     * Ý tưởng: Với mỗi vị trí i, tính max ending here
     * - maxEndingHere[i] = max(A[i], maxEndingHere[i-1] + A[i])
     * 
     * Giải thích: Tại mỗi vị trí, ta chọn:
     * - Bắt đầu slice mới từ A[i]
     * - Hoặc mở rộng slice trước đó
     * 
     * MINH HỌA:
     * A = [3, 2, -6, 4, 0]
     * i=0: maxEnding = 3, maxSlice = 3
     * i=1: maxEnding = max(2, 3+2) = 5, maxSlice = 5
     * i=2: maxEnding = max(-6, 5-6) = -1, maxSlice = 5
     * i=3: maxEnding = max(4, -1+4) = 4, maxSlice = 5
     * i=4: maxEnding = max(0, 4+0) = 4, maxSlice = 5
     * 
     * ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian
     */
    public int maxSliceSum(int[] A) {
        int N = A.length;
        if (N == 0) return 0;
        
        int maxEndingHere = A[0];
        int maxSlice = A[0];
        
        for (int i = 1; i < N; i++) {
            maxEndingHere = Math.max(A[i], maxEndingHere + A[i]);
            maxSlice = Math.max(maxSlice, maxEndingHere);
        }
        
        return maxSlice;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                      MAX DOUBLE SLICE SUM
     *                    Tổng lớn nhất của double slice
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI:
     * Double slice (X, Y, Z) = tổng A[X+1..Y-1] + A[Y+1..Z-1]
     * Tức là lấy 2 slice không kề nhau, bỏ phần tử ở giữa (Y).
     * Tìm double slice có tổng lớn nhất.
     * 
     * VÍ DỤ: A = [3, 2, 6, -1, 4, 5, -1, 2]
     * Double slice (0, 3, 6) = [2, 6] + [4, 5] = 17
     * 
     * TƯ DUY:
     * • Tính maxEndingHere từ trái: maxLeft[i] = max slice kết thúc trước i
     * • Tính maxEndingHere từ phải: maxRight[i] = max slice bắt đầu sau i
     * • Với mỗi Y làm điểm chia: total = maxLeft[Y] + maxRight[Y]
     * 
     * ĐỘ PHỨC TẠP: O(N) thời gian, O(N) không gian
     */
    public int maxDoubleSliceSum(int[] A) {
        int N = A.length;
        if (N <= 3) return 0; // Không có double slice hợp lệ
        
        // maxLeft[i] = max slice sum kết thúc tại i-1 (không bao gồm i)
        int[] maxLeft = new int[N];
        // maxRight[i] = max slice sum bắt đầu tại i+1 (không bao gồm i)
        int[] maxRight = new int[N];
        
        // Tính maxLeft (bỏ qua index 0 vì không có slice kết thúc trước 0)
        for (int i = 1; i < N - 1; i++) {
            maxLeft[i] = Math.max(0, maxLeft[i - 1] + A[i]);
        }
        
        // Tính maxRight (bỏ qua index N-1)
        for (int i = N - 2; i > 0; i--) {
            maxRight[i] = Math.max(0, maxRight[i + 1] + A[i]);
        }
        
        // Tìm max double slice
        int maxDouble = 0;
        for (int Y = 1; Y < N - 1; Y++) {
            // Y là điểm bị bỏ, slice trái kết thúc tại Y-1, slice phải bắt đầu tại Y+1
            maxDouble = Math.max(maxDouble, maxLeft[Y - 1] + maxRight[Y + 1]);
        }
        
        return maxDouble;
    }
    
    // ================================ TESTS ================================
    
    @Test
    void testMaxProfit() {
        assertEquals(356, maxProfit(new int[]{23171, 21011, 21123, 21366, 21013, 21367}));
        assertEquals(0, maxProfit(new int[]{5, 4, 3, 2, 1})); // Giá giảm liên tục
    }
    
    @Test
    void testMaxSliceSum() {
        assertEquals(5, maxSliceSum(new int[]{3, 2, -6, 4, 0}));
        assertEquals(-1, maxSliceSum(new int[]{-2, -3, -1, -5})); // Tất cả âm
    }
    
    @Test
    void testMaxDoubleSliceSum() {
        assertEquals(17, maxDoubleSliceSum(new int[]{3, 2, 6, -1, 4, 5, -1, 2}));
        assertEquals(0, maxDoubleSliceSum(new int[]{1, 2, 3})); // Chỉ có 3 phần tử
    }
}
