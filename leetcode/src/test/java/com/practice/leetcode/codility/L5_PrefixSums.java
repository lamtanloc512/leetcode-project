package com.practice.leetcode.codility;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                      LESSON 5: PREFIX SUMS                                  ║
 * ║                         Tổng tiền tố                                        ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Prefix Sum là kỹ thuật tính trước tổng tích lũy để trả lời truy vấn O(1).
 * prefixSum[i] = A[0] + A[1] + ... + A[i-1]
 * sum(A[L..R]) = prefixSum[R+1] - prefixSum[L]
 */
public class L5_PrefixSums {

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                          PASSING CARS
     *                    Đếm cặp xe đi ngược chiều
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI:
     * Mảng A chứa 0 (xe đi hướng Đông) và 1 (xe đi hướng Tây).
     * Đếm số cặp (P, Q) với P < Q và A[P]=0, A[Q]=1 (xe 0 gặp xe 1 phía sau).
     * 
     * VÍ DỤ: A = [0, 1, 0, 1, 1]
     * Các cặp: (0,1), (0,3), (0,4), (2,3), (2,4) → 5 cặp
     * 
     * TƯ DUY:
     * • Mỗi xe 0 tại vị trí P sẽ tạo cặp với TẤT CẢ xe 1 ở sau P
     * • Đếm số xe 0 đã gặp, với mỗi xe 1 → thêm count0 vào kết quả
     * 
     * ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian
     */
    public int passingCars(int[] A) {
        int count0 = 0;    // Số xe 0 đã gặp
        long result = 0;   // Dùng long tránh overflow
        
        for (int car : A) {
            if (car == 0) {
                count0++;
            } else {
                // Mỗi xe 1 tạo cặp với tất cả xe 0 trước đó
                result += count0;
            }
        }
        
        // Trả về -1 nếu vượt 1 tỷ (theo đề bài)
        return result > 1_000_000_000 ? -1 : (int) result;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                          COUNT DIV
     *               Đếm số nguyên chia hết cho K trong [A, B]
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Đếm số nguyên trong đoạn [A, B] chia hết cho K.
     * 
     * VÍ DỤ: A=6, B=11, K=2 → {6, 8, 10} → 3 số
     * 
     * CÔNG THỨC:
     * • Số số chia hết cho K trong [0, X] = X / K + 1 (nếu X >= 0)
     * • Số số chia hết cho K trong [A, B] = count(0..B) - count(0..A-1)
     * 
     * CHÚ Ý: A có thể = 0, cần xử lý đặc biệt
     * 
     * ĐỘ PHỨC TẠP: O(1)
     */
    public int countDiv(int A, int B, int K) {
        // Số số chia hết trong [0, B]
        int countToB = B / K;
        
        // Số số chia hết trong [0, A-1]
        int countToAMinus1 = (A > 0) ? (A - 1) / K : 0;
        
        // Kết quả, +1 nếu A = 0 và K chia hết 0
        int result = countToB - countToAMinus1;
        
        return result;
    }
    
    /**
     * Cách 2: Công thức trực tiếp
     */
    public int countDivDirect(int A, int B, int K) {
        // Số đầu tiên chia hết cho K >= A
        int first = (A % K == 0) ? A : A + (K - A % K);
        
        if (first > B) return 0;
        
        // Số số chia hết = (B - first) / K + 1
        return (B - first) / K + 1;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                       GENOMIC RANGE QUERY
     *                     Truy vấn gene tối thiểu
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI:
     * Chuỗi DNA S gồm các ký tự A, C, G, T với "impact factor" là 1, 2, 3, 4.
     * Cho M truy vấn [P[i], Q[i]], tìm impact factor nhỏ nhất trong S[P..Q].
     * 
     * VÍ DỤ: S = "CAGCCTA", P = [2, 5, 0], Q = [4, 5, 6]
     * • S[2..4] = "GCC" → min = 2 (C)
     * • S[5..5] = "T" → min = 4 (T)
     * • S[0..6] = "CAGCCTA" → min = 1 (A)
     * 
     * TƯ DUY: Prefix sum cho từng loại nucleotide
     * • prefixA[i] = số lượng 'A' trong S[0..i-1]
     * • Tương tự cho C, G, T
     * • Kiểm tra từ impact thấp nhất (A) đến cao nhất (T)
     * 
     * ĐỘ PHỨC TẠP: O(N+M) thời gian, O(N) không gian
     */
    public int[] genomicRangeQuery(String S, int[] P, int[] Q) {
        int N = S.length();
        int M = P.length;
        
        // Prefix sums cho mỗi loại (chỉ cần A, C, G vì T là mặc định)
        int[] prefixA = new int[N + 1];
        int[] prefixC = new int[N + 1];
        int[] prefixG = new int[N + 1];
        
        // Xây prefix sum
        for (int i = 0; i < N; i++) {
            prefixA[i + 1] = prefixA[i] + (S.charAt(i) == 'A' ? 1 : 0);
            prefixC[i + 1] = prefixC[i] + (S.charAt(i) == 'C' ? 1 : 0);
            prefixG[i + 1] = prefixG[i] + (S.charAt(i) == 'G' ? 1 : 0);
        }
        
        int[] result = new int[M];
        
        for (int i = 0; i < M; i++) {
            int from = P[i];
            int to = Q[i];
            
            // Kiểm tra từ impact thấp nhất
            if (prefixA[to + 1] - prefixA[from] > 0) {
                result[i] = 1; // A
            } else if (prefixC[to + 1] - prefixC[from] > 0) {
                result[i] = 2; // C
            } else if (prefixG[to + 1] - prefixG[from] > 0) {
                result[i] = 3; // G
            } else {
                result[i] = 4; // T
            }
        }
        
        return result;
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════
     *                       MIN AVG TWO SLICE
     *               Tìm slice có giá trị trung bình nhỏ nhất
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * ĐỀ BÀI: Tìm vị trí bắt đầu của slice (P, Q) với P < Q có avg nhỏ nhất.
     * 
     * NHẬN XÉT TOÁN HỌC QUAN TRỌNG:
     * Slice có avg min phải có độ dài 2 HOẶC 3!
     * 
     * Chứng minh: Nếu slice dài >= 4, có thể chia thành 2 slice nhỏ hơn.
     * Một trong 2 slice nhỏ sẽ có avg <= avg slice lớn.
     * → Chỉ cần kiểm tra slice độ dài 2 và 3.
     * 
     * ĐỘ PHỨC TẠP: O(N) thời gian, O(1) không gian
     */
    public int minAvgTwoSlice(int[] A) {
        int N = A.length;
        double minAvg = Double.MAX_VALUE;
        int minIdx = 0;
        
        for (int i = 0; i < N - 1; i++) {
            // Slice độ dài 2: A[i..i+1]
            double avg2 = (A[i] + A[i + 1]) / 2.0;
            if (avg2 < minAvg) {
                minAvg = avg2;
                minIdx = i;
            }
            
            // Slice độ dài 3: A[i..i+2]
            if (i < N - 2) {
                double avg3 = (A[i] + A[i + 1] + A[i + 2]) / 3.0;
                if (avg3 < minAvg) {
                    minAvg = avg3;
                    minIdx = i;
                }
            }
        }
        
        return minIdx;
    }
    
    // ================================ TESTS ================================
    
    @Test
    void testPassingCars() {
        assertEquals(5, passingCars(new int[]{0, 1, 0, 1, 1}));
        assertEquals(0, passingCars(new int[]{1, 1, 1}));
    }
    
    @Test
    void testCountDiv() {
        assertEquals(3, countDiv(6, 11, 2));
        assertEquals(1, countDiv(0, 0, 11));
        assertEquals(20, countDiv(11, 345, 17));
    }
    
    @Test
    void testGenomicRangeQuery() {
        assertArrayEquals(new int[]{2, 4, 1}, 
            genomicRangeQuery("CAGCCTA", new int[]{2, 5, 0}, new int[]{4, 5, 6}));
    }
    
    @Test
    void testMinAvgTwoSlice() {
        assertEquals(1, minAvgTwoSlice(new int[]{4, 2, 2, 5, 1, 5, 8}));
    }
}
