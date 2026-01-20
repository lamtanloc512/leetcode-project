package com.practice.leetcode.codility;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                        LESSON 6: SORTING                                    ║
 * ║                         Bài toán sắp xếp                                    ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class L6_Sorting {

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                           DISTINCT
     *                    Đếm số phần tử khác nhau
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Đếm số phần tử distinct trong mảng A.
     * 
     * VÍ DỤ: A = [2, 1, 1, 2, 3, 1] → 3 phần tử distinct: {1, 2, 3}
     * 
     * CÁCH 1: HashSet - O(N) time, O(N) space
     * CÁCH 2: Sort + đếm - O(N log N) time, O(1) space
     */
    public int distinct(int[] A) {
        Set<Integer> set = new HashSet<>();
        for (int num : A) {
            set.add(num);
        }
        return set.size();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                           TRIANGLE
     *                   Kiểm tra tam giác hợp lệ
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Kiểm tra có tồn tại bộ 3 số tạo thành tam giác hợp lệ không.
     * Tam giác hợp lệ: A[P]+A[Q] > A[R], A[Q]+A[R] > A[P], A[R]+A[P] > A[Q]
     * 
     * VÍ DỤ: [10, 2, 5, 1, 8, 20] → (10, 5, 8) là tam giác hợp lệ
     * 
     * TƯ DUY SAU KHI SORT:
     * Sau khi sort: A[i] <= A[i+1] <= A[i+2]
     * • A[i] + A[i+2] > A[i+1] → luôn đúng (vì A[i+2] >= A[i+1])
     * • A[i+1] + A[i+2] > A[i] → luôn đúng
     * • Chỉ cần kiểm tra: A[i] + A[i+1] > A[i+2]
     * 
     * ⚠️ OVERFLOW: Dùng long khi cộng
     * 
     * ĐỘ PHỨC TẠP: O(N log N) thời gian
     */
    public int triangle(int[] A) {
        int N = A.length;
        if (N < 3) return 0;
        
        Arrays.sort(A);
        
        for (int i = 0; i < N - 2; i++) {
            // Dùng long tránh overflow
            if ((long) A[i] + A[i + 1] > A[i + 2]) {
                return 1;
            }
        }
        
        return 0;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                      MAX PRODUCT OF THREE
     *                    Tích lớn nhất của 3 số
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Tìm tích lớn nhất của bộ 3 phần tử trong mảng.
     * 
     * VÍ DỤ: A = [-3, 1, 2, -2, 5, 6] → max = 6 * 5 * 2 = 60
     * 
     * PHÂN TÍCH CÁC TRƯỜNG HỢP:
     * Sau khi sort: A[0] <= A[1] <= ... <= A[N-1]
     * 
     * Tích lớn nhất có thể là:
     * 1. Ba số dương lớn nhất: A[N-1] * A[N-2] * A[N-3]
     * 2. Hai số âm nhỏ nhất * số dương lớn nhất: A[0] * A[1] * A[N-1]
     *    (âm * âm = dương lớn)
     * 
     * → max(hai trường hợp trên)
     */
    public int maxProductOfThree(int[] A) {
        int N = A.length;
        Arrays.sort(A);
        
        // Trường hợp 1: 3 số lớn nhất
        int product1 = A[N - 1] * A[N - 2] * A[N - 3];
        
        // Trường hợp 2: 2 số âm nhỏ nhất * số dương lớn nhất
        int product2 = A[0] * A[1] * A[N - 1];
        
        return Math.max(product1, product2);
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                 NUMBER OF DISC INTERSECTIONS
     *                    Đếm số cặp đĩa giao nhau
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI:
     * N đĩa tại vị trí 0..N-1, đĩa i có bán kính A[i].
     * Đĩa i bao phủ [i - A[i], i + A[i]].
     * Đếm số cặp đĩa giao nhau. Return -1 nếu > 10^7.
     * 
     * VÍ DỤ: A = [1, 5, 2, 1, 4, 0]
     * Đĩa 0: [-1, 1], Đĩa 1: [-4, 6], Đĩa 2: [0, 4], ...
     * 
     * TƯ DUY: Đếm điểm bắt đầu và kết thúc
     * • Với mỗi vị trí, đếm số đĩa bắt đầu (left) và kết thúc (right)
     * • Số cặp mới = số đĩa active * số đĩa mới mở + C(số đĩa mới mở, 2)
     * 
     * ĐỘ PHỨC TẠP: O(N log N) hoặc O(N) với counting sort
     */
    public int numberOfDiscIntersections(int[] A) {
        int N = A.length;
        long[] start = new long[N];
        long[] end = new long[N];
        
        // Tính điểm bắt đầu và kết thúc của mỗi đĩa
        for (int i = 0; i < N; i++) {
            start[i] = (long) i - A[i];
            end[i] = (long) i + A[i];
        }
        
        Arrays.sort(start);
        Arrays.sort(end);
        
        long intersections = 0;
        int activeDiscs = 0;
        int endIdx = 0;
        
        for (int i = 0; i < N; i++) {
            // Đóng các đĩa đã kết thúc trước điểm bắt đầu mới
            while (endIdx < N && end[endIdx] < start[i]) {
                activeDiscs--;
                endIdx++;
            }
            
            // Đĩa mới giao với tất cả đĩa đang active
            intersections += activeDiscs;
            activeDiscs++;
            
            if (intersections > 10_000_000) return -1;
        }
        
        return (int) intersections;
    }
    
    // ================================ TESTS ================================
    
    @Test
    void testDistinct() {
        assertEquals(3, distinct(new int[]{2, 1, 1, 2, 3, 1}));
        assertEquals(1, distinct(new int[]{5, 5, 5}));
    }
    
    @Test
    void testTriangle() {
        assertEquals(1, triangle(new int[]{10, 2, 5, 1, 8, 20}));
        assertEquals(0, triangle(new int[]{10, 50, 5, 1}));
    }
    
    @Test
    void testMaxProductOfThree() {
        assertEquals(60, maxProductOfThree(new int[]{-3, 1, 2, -2, 5, 6}));
        assertEquals(125, maxProductOfThree(new int[]{-5, -5, 5, 4})); // (-5)*(-5)*5
    }
    
    @Test
    void testNumberOfDiscIntersections() {
        assertEquals(11, numberOfDiscIntersections(new int[]{1, 5, 2, 1, 4, 0}));
    }
}
