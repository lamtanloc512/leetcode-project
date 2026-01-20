package com.practice.leetcode.codility;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║              LESSON 10: PRIME AND COMPOSITE NUMBERS                         ║
 * ║                    Số nguyên tố và hợp số                                   ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Kỹ thuật quan trọng: Chỉ cần kiểm tra đến √N vì ước số đi theo cặp.
 */
public class L10_PrimeComposite {

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                         COUNT FACTORS
     *                      Đếm số ước của N
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Đếm số ước của N.
     * 
     * VÍ DỤ: N = 24 → ước: 1,2,3,4,6,8,12,24 → 8 ước
     * 
     * TƯ DUY: Ước đi theo cặp (d, N/d)
     * • Với mỗi d từ 1 đến √N: nếu N % d == 0 → có 2 ước
     * • Nếu d == N/d (N là số chính phương) → chỉ đếm 1
     * 
     * ĐỘ PHỨC TẠP: O(√N)
     */
    public int countFactors(int N) {
        int count = 0;
        int i = 1;
        
        while (i * i < N) {
            if (N % i == 0) {
                count += 2; // Cặp (i, N/i)
            }
            i++;
        }
        
        // Kiểm tra số chính phương
        if (i * i == N) {
            count++;
        }
        
        return count;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                     MIN PERIMETER RECTANGLE
     *                Chu vi nhỏ nhất của hình chữ nhật
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Cho diện tích N, tìm chu vi nhỏ nhất của HCN có cạnh nguyên.
     * 
     * VÍ DỤ: N = 30 → các cặp (1,30), (2,15), (3,10), (5,6)
     * Chu vi: 62, 34, 26, 22 → min = 22
     * 
     * TƯ DUY: Hình vuông có chu vi nhỏ nhất
     * → Tìm cặp ước (a, b) sao cho |a - b| nhỏ nhất
     * → Chỉ cần duyệt đến √N
     */
    public int minPerimeterRectangle(int N) {
        int minPerimeter = Integer.MAX_VALUE;
        
        for (int a = 1; a * a <= N; a++) {
            if (N % a == 0) {
                int b = N / a;
                int perimeter = 2 * (a + b);
                minPerimeter = Math.min(minPerimeter, perimeter);
            }
        }
        
        return minPerimeter;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                            PEAKS
     *               Chia mảng thành blocks có peak
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI:
     * Peak là vị trí A[P-1] < A[P] > A[P+1].
     * Chia mảng thành K blocks bằng nhau sao cho mỗi block có ít nhất 1 peak.
     * Tìm K lớn nhất.
     * 
     * VÍ DỤ: A = [1, 2, 3, 4, 3, 4, 1, 2, 3, 4, 6, 2]
     * Peaks tại vị trí: 3, 5, 10
     * Có thể chia thành 3 blocks (mỗi block 4 phần tử), mỗi block có 1 peak.
     * 
     * TƯ DUY:
     * 1. Tìm tất cả peaks và lưu prefix sum
     * 2. Thử các K là ước của N
     * 3. Với mỗi K, kiểm tra xem mỗi block có peak không
     */
    public int peaks(int[] A) {
        int N = A.length;
        if (N < 3) return 0;
        
        // Tìm peaks và tạo prefix sum
        int[] peakPrefix = new int[N + 1];
        for (int i = 1; i < N - 1; i++) {
            peakPrefix[i + 1] = peakPrefix[i];
            if (A[i - 1] < A[i] && A[i] > A[i + 1]) {
                peakPrefix[i + 1]++;
            }
        }
        peakPrefix[N] = peakPrefix[N - 1];
        
        int totalPeaks = peakPrefix[N];
        if (totalPeaks == 0) return 0;
        
        // Thử các số block K từ lớn đến nhỏ
        for (int K = totalPeaks; K >= 1; K--) {
            if (N % K != 0) continue;
            
            int blockSize = N / K;
            boolean valid = true;
            
            for (int block = 0; block < K; block++) {
                int start = block * blockSize;
                int end = start + blockSize;
                int peaksInBlock = peakPrefix[end] - peakPrefix[start];
                
                if (peaksInBlock == 0) {
                    valid = false;
                    break;
                }
            }
            
            if (valid) return K;
        }
        
        return 0;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                            FLAGS
     *                    Cắm cờ trên các đỉnh núi
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI:
     * Cắm K cờ trên peaks, mỗi cặp cờ phải cách nhau ít nhất K đơn vị.
     * Tìm K lớn nhất có thể cắm được.
     * 
     * TƯ DUY: Binary Search
     * • Nếu có thể cắm K cờ → có thể cắm K-1 cờ
     * • Binary search trên K
     * • Với mỗi K, greedy cắm từ peak đầu tiên
     */
    public int flags(int[] A) {
        int N = A.length;
        List<Integer> peaks = new ArrayList<>();
        
        // Tìm tất cả peaks
        for (int i = 1; i < N - 1; i++) {
            if (A[i - 1] < A[i] && A[i] > A[i + 1]) {
                peaks.add(i);
            }
        }
        
        int P = peaks.size();
        if (P == 0) return 0;
        if (P == 1) return 1;
        
        // Binary search trên số cờ K
        int maxK = (int) Math.sqrt(peaks.get(P - 1) - peaks.get(0)) + 1;
        int result = 1;
        
        for (int K = 2; K <= maxK && K <= P; K++) {
            if (canPlaceFlags(peaks, K)) {
                result = K;
            }
        }
        
        return result;
    }
    
    private boolean canPlaceFlags(List<Integer> peaks, int K) {
        int placed = 1;
        int lastPos = peaks.get(0);
        
        for (int i = 1; i < peaks.size() && placed < K; i++) {
            if (peaks.get(i) - lastPos >= K) {
                placed++;
                lastPos = peaks.get(i);
            }
        }
        
        return placed >= K;
    }
    
    // ================================ TESTS ================================
    
    @Test
    void testCountFactors() {
        assertEquals(8, countFactors(24));
        assertEquals(1, countFactors(1));
        assertEquals(2, countFactors(7)); // Số nguyên tố
    }
    
    @Test
    void testMinPerimeterRectangle() {
        assertEquals(22, minPerimeterRectangle(30));
        assertEquals(4, minPerimeterRectangle(1));
    }
    
    @Test
    void testPeaks() {
        assertEquals(3, peaks(new int[]{1, 2, 3, 4, 3, 4, 1, 2, 3, 4, 6, 2}));
    }
    
    @Test
    void testFlags() {
        assertTrue(flags(new int[]{1, 5, 3, 4, 3, 4, 1, 2, 3, 4, 6, 2}) >= 2);
    }
}
