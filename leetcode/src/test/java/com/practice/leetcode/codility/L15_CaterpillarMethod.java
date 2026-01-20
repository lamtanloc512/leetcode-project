package com.practice.leetcode.codility;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║          LESSON 15: CATERPILLAR METHOD (Two Pointers)                       ║
 * ║                   Phương pháp sâu bướm / Hai con trỏ                        ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Caterpillar Method sử dụng 2 con trỏ di chuyển theo cùng hướng,
 * giống như sâu bướm co giãn để bò. Thường dùng cho subarray có tính chất monotonic.
 */
public class L15_CaterpillarMethod {

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                        COUNT DISTINCT SLICES
     *                 Đếm số slice có phần tử distinct
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI:
     * Đếm số slice (P, Q) sao cho tất cả phần tử trong A[P..Q] đều distinct.
     * Return min(result, 1_000_000_000).
     * 
     * VÍ DỤ: A = [3, 4, 5, 5, 2]
     * Các slice distinct: (0,0), (0,1), (0,2), (1,1), (1,2), (2,2), (3,3), (3,4), (4,4)
     * → 9 slices
     * 
     * TƯ DUY CATERPILLAR:
     * • Front pointer mở rộng khi có thể (phần tử mới distinct)
     * • Back pointer thu hẹp khi có duplicate
     * • Với mỗi back, đếm số slice kết thúc từ back đến front
     */
    public int countDistinctSlices(int M, int[] A) {
        int N = A.length;
        boolean[] seen = new boolean[M + 1];
        long count = 0;
        int front = 0;
        
        for (int back = 0; back < N; back++) {
            // Mở rộng front càng xa càng tốt
            while (front < N && !seen[A[front]]) {
                seen[A[front]] = true;
                front++;
            }
            
            // Số slice bắt đầu từ back: (back, back), (back, back+1), ..., (back, front-1)
            count += front - back;
            
            if (count >= 1_000_000_000) return 1_000_000_000;
            
            // Loại bỏ phần tử ở back khi di chuyển
            seen[A[back]] = false;
        }
        
        return (int) count;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                         ABS DISTINCT
     *                Đếm số giá trị tuyệt đối distinct
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Đếm số giá trị |A[i]| distinct trong mảng đã sắp xếp.
     * 
     * VÍ DỤ: A = [-5, -3, -1, 0, 3, 6] → |values|: 5, 3, 1, 0, 3, 6 → 5 distinct
     * 
     * TƯ DUY: Hai con trỏ từ 2 đầu
     * • left từ đầu (số âm có |value| lớn)
     * • right từ cuối (số dương lớn)
     * • So sánh |A[left]| và |A[right]|, di chuyển con trỏ có giá trị lớn hơn
     * • Skip duplicates
     */
    public int absDistinct(int[] A) {
        int N = A.length;
        int left = 0, right = N - 1;
        int count = 0;
        
        while (left <= right) {
            count++;
            
            long leftVal = Math.abs((long) A[left]);
            long rightVal = Math.abs((long) A[right]);
            
            if (leftVal == rightVal) {
                // Cùng giá trị tuyệt đối → skip cả hai phía
                long current = leftVal;
                while (left <= right && Math.abs((long) A[left]) == current) left++;
                while (left <= right && Math.abs((long) A[right]) == current) right--;
            } else if (leftVal > rightVal) {
                // Skip duplicates ở trái
                long current = leftVal;
                while (left <= right && Math.abs((long) A[left]) == current) left++;
            } else {
                // Skip duplicates ở phải
                long current = rightVal;
                while (left <= right && Math.abs((long) A[right]) == current) right--;
            }
        }
        
        return count;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                      COUNT TRIANGLES
     *                    Đếm số tam giác hợp lệ
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Đếm số bộ 3 chỉ số (P, Q, R) với P < Q < R tạo thành tam giác hợp lệ.
     * Tam giác hợp lệ: A[P] + A[Q] > A[R]
     * 
     * TƯ DUY:
     * 1. Sort mảng
     * 2. Fix P, với mỗi Q > P, tìm R lớn nhất sao cho A[P]+A[Q] > A[R]
     * 3. Sử dụng caterpillar: khi Q tăng, R chỉ có thể tăng (không giảm)
     * 
     * ĐỘ PHỨC TẠP: O(N²)
     */
    public int countTriangles(int[] A) {
        int N = A.length;
        if (N < 3) return 0;
        
        Arrays.sort(A);
        int count = 0;
        
        for (int P = 0; P < N - 2; P++) {
            int R = P + 2;
            
            for (int Q = P + 1; Q < N - 1; Q++) {
                // R chỉ tăng, không giảm
                while (R < N && A[P] + A[Q] > A[R]) {
                    R++;
                }
                
                // Số tam giác với P, Q cố định: từ Q+1 đến R-1
                count += R - Q - 1;
            }
        }
        
        return count;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                         MIN ABS SUM OF TWO
     *               Tổng tuyệt đối nhỏ nhất của 2 phần tử
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Tìm |A[P] + A[Q]| nhỏ nhất với 0 ≤ P ≤ Q < N.
     * 
     * VÍ DỤ: A = [1, 4, -3] → min = |(-3) + 4| = 1
     * 
     * TƯ DUY: Sort + Two Pointers
     * • Sort mảng
     * • Hai con trỏ: left = 0, right = N-1
     * • Nếu sum > 0 → cần giảm → right--
     * • Nếu sum < 0 → cần tăng → left++
     * • Cập nhật min abs
     */
    public int minAbsSumOfTwo(int[] A) {
        int N = A.length;
        Arrays.sort(A);
        
        int left = 0, right = N - 1;
        int minAbs = Math.abs(A[0] + A[0]); // P = Q = 0
        
        while (left <= right) {
            int sum = A[left] + A[right];
            minAbs = Math.min(minAbs, Math.abs(sum));
            
            if (sum == 0) return 0;
            
            if (sum > 0) {
                right--;
            } else {
                left++;
            }
        }
        
        return minAbs;
    }
    
    // ================================ TESTS ================================
    
    @Test
    void testCountDistinctSlices() {
        assertEquals(9, countDistinctSlices(6, new int[]{3, 4, 5, 5, 2}));
    }
    
    @Test
    void testAbsDistinct() {
        assertEquals(5, absDistinct(new int[]{-5, -3, -1, 0, 3, 6}));
    }
    
    @Test
    void testCountTriangles() {
        assertEquals(4, countTriangles(new int[]{10, 2, 5, 1, 8, 12}));
    }
    
    @Test
    void testMinAbsSumOfTwo() {
        assertEquals(1, minAbsSumOfTwo(new int[]{1, 4, -3}));
        assertEquals(0, minAbsSumOfTwo(new int[]{-8, 4, 5, -10, 3})); // -8 + 8 không có, nhưng 5 + (-5)?
    }
}
