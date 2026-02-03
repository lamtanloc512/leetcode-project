package com.practice.leetcode.codility;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CODILITY - FROG RIVER ONE (Đề gốc khó hiểu, đọc phiên bản đơn giản)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI PHIÊN BẢN ĐƠN GIẢN (DỄ HIỂU)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Con ếch muốn nhảy qua sông từ vị trí 0 → X.
 * 
 * Cần có lá cây rơi ở TẤT CẢ vị trí từ 1 đến X mới nhảy được.
 * 
 * Cho array A[]:
 * - A[K] = vị trí lá cây rơi tại giây K
 * 
 * Hỏi: Giây nào ếch có thể nhảy qua sông (sớm nhất)?
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VÍ DỤ ĐƠN GIẢN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Input: X = 5, A = [1, 3, 1, 4, 2, 3, 5, 4]
 * 
 * Ếch cần lá ở vị trí: {1, 2, 3, 4, 5}
 * 
 * Giây 0: Lá rơi vị trí 1 → Có {1}
 * Giây 1: Lá rơi vị trí 3 → Có {1, 3}
 * Giây 2: Lá rơi vị trí 1 → Có {1, 3} (đã có rồi)
 * Giây 3: Lá rơi vị trí 4 → Có {1, 3, 4}
 * Giây 4: Lá rơi vị trí 2 → Có {1, 2, 3, 4}
 * Giây 5: Lá rơi vị trí 3 → Có {1, 2, 3, 4} (đã có rồi)
 * Giây 6: Lá rơi vị trí 5 → Có {1, 2, 3, 4, 5} ✅ ĐỦ RỒI!
 * 
 * Kết quả: Giây 6
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ĐỀ BÀI GỐC CODILITY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * A small frog wants to get to the other side of a river. The frog is initially
 * located on one bank of the river (position 0) and wants to get to the opposite
 * bank (position X+1). Leaves fall from a tree onto the surface of the river.
 * 
 * You are given an array A consisting of N integers representing the falling leaves.
 * A[K] represents the position where one leaf falls at time K, measured in seconds.
 * 
 * The goal is to find the earliest time when the frog can jump to the other side
 * of the river. The frog can cross only when leaves appear at every position across
 * the river from 1 to X.
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 1: BRUTE FORCE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Với mỗi giây K:
 * - Kiểm tra xem đã có đủ lá từ 1→X chưa
 * 
 * Time: O(n * X)
 * Space: O(X)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROACH 2: SET TRACKING (OPTIMAL)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Key Insight:
 * - Dùng Set để track vị trí đã có lá
 * - Mỗi lần thêm lá mới vào Set
 * - Khi Set.size() == X → Đủ rồi!
 * 
 * Time: O(n)
 * Space: O(X)
 * ✅ OPTIMAL!
 */
class FrogRiverOneSolutionTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 1: BRUTE FORCE
    // ═══════════════════════════════════════════════════════════════════════════
    public int solutionBruteForce(int X, int[] A) {
        for (int time = 0; time < A.length; time++) {
            Set<Integer> positions = new HashSet<>();
            
            for (int k = 0; k <= time; k++) {
                positions.add(A[k]);
            }
            
            // Kiểm tra có đủ từ 1→X không
            if (positions.size() >= X) {
                boolean hasAll = true;
                for (int i = 1; i <= X; i++) {
                    if (!positions.contains(i)) {
                        hasAll = false;
                        break;
                    }
                }
                if (hasAll) return time;
            }
        }
        
        return -1;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 2: SET TRACKING (OPTIMAL)
    // ═══════════════════════════════════════════════════════════════════════════
    public int solution(int X, int[] A) {
        Set<Integer> positions = new HashSet<>();
        
        for (int time = 0; time < A.length; time++) {
            positions.add(A[time]);
            
            // Nếu đã có đủ X vị trí khác nhau
            if (positions.size() == X) {
                return time;
            }
        }
        
        return -1;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROACH 3: COUNTER OPTIMIZATION
    // ═══════════════════════════════════════════════════════════════════════════
    /**
     * Dùng counter thay vì kiểm tra Set.size()
     * - Mỗi lần gặp vị trí mới → counter++
     * - Khi counter == X → Đủ rồi!
     */
    public int solutionCounter(int X, int[] A) {
        boolean[] seen = new boolean[X + 1];
        int counter = 0;
        
        for (int time = 0; time < A.length; time++) {
            int position = A[time];
            
            if (position <= X && !seen[position]) {
                seen[position] = true;
                counter++;
                
                if (counter == X) {
                    return time;
                }
            }
        }
        
        return -1;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("FrogRiverOne - Example case")
    void testExample() {
        int[] A = {1, 3, 1, 4, 2, 3, 5, 4};
        assertThat(solution(5, A)).isEqualTo(6);
        assertThat(solutionCounter(5, A)).isEqualTo(6);
        assertThat(solutionBruteForce(5, A)).isEqualTo(6);
    }
    
    @Test
    @DisplayName("FrogRiverOne - Immediate success")
    void testImmediate() {
        int[] A = {1};
        assertThat(solution(1, A)).isEqualTo(0);
    }
    
    @Test
    @DisplayName("FrogRiverOne - Never complete")
    void testNeverComplete() {
        int[] A = {1, 2, 3};
        assertThat(solution(5, A)).isEqualTo(-1);
    }
    
    @Test
    @DisplayName("FrogRiverOne - Last moment")
    void testLastMoment() {
        int[] A = {2, 3, 1};
        assertThat(solution(3, A)).isEqualTo(2);
    }
}
