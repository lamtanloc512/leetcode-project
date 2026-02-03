package com.practice.leetcode.design;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PARKING DILEMMA
 * 
 * Problem:
 * You are given an array of integers where each integer represents the position of a parked car.
 * You are also given an integer k, which is the number of cars you want to cover with a roof.
 * Find the minimum length of the roof that can cover k cars.
 * 
 * Constraints:
 * 1 <= n <= 10^5
 * 1 <= k <= n
 * 1 <= pos[i] <= 10^9
 * 
 * Algorithm 1 - Sorting + Sliding Window: O(n log n) ⭐ RECOMMENDED
 * 1. Sort the car positions.
 * 2. Use a sliding window of size k.
 * 3. The length of a roof covering cars from index i to i + k - 1 is (cars[i + k - 1] - cars[i] + 1).
 * 4. Find the minimum of these lengths.
 * 5. BEST for constraint pos[i] <= 10^9 (range có thể rất lớn).
 * 
 * Algorithm 2 - Bit Mask Approach: O(n + range)
 * 1. Create a bit mask marking positions with cars.
 * 2. Use sliding window on the bit mask to find k consecutive 1-bits.
 * 3. ⚠️ ONLY use when range (maxPos - minPos) is small (<= 10^6).
 * 4. With pos[i] <= 10^9, this approach can use up to 125 MB memory!
 * 
 * Algorithm 3 - Counting Sort: O(n + range)
 * 1. If the range (maxPos - minPos) is reasonable, use counting sort.
 * 2. Mark positions in a boolean array.
 * 3. Scan the array and collect car positions in sorted order.
 * 4. Apply sliding window on the sorted list.
 * 5. ⚠️ Same limitation as Bit Mask: only good for small range.
 */
class ParkingDilemmaSolutionTest {

    /**
     * APPROACH 1: Sorting + Sliding Window
     * Time: O(n log n) - dominated by sorting
     * Space: O(1)
     */
    public long minRoofLength(long[] cars, int k) {
        if (cars == null || cars.length < k) {
            return 0;
        }
        Arrays.sort(cars);
        long minLength = Long.MAX_VALUE;
        // Sliding window of size k (template approach)
        int j = 0;
        for (int i = 0; i < cars.length; i++) {
            // Maintain window size = k
            while (i - j + 1 > k) {
                j++;
            }
            // When window has exactly k cars, calculate roof length
            if (i - j + 1 == k) {
                long currentLength = cars[i] - cars[j] + 1;
                minLength = Math.min(minLength, currentLength);
            }
        }

        return minLength; 
    }

    /**
     * APPROACH 2: Bitwise Operators + Sliding Window (Tránh sort)
     * Time: O(n + range) - không cần sort, linear scan
     * Space: O(range / 64) - dùng long[] làm bitmask
     * 
     * Ý tưởng:
     * 1. Nếu range ≤ 64: Dùng 1 số long duy nhất làm bitmask (64 bits).
     * 2. Nếu range > 64: Dùng long[] (mỗi long chứa 64 bits).
     * 3. Set bit: mask[idx/64] |= (1L << (idx%64))
     * 4. Check bit: (mask[idx/64] & (1L << (idx%64))) != 0
     * 
     * Giải thích tại sao cần long[]:
     * - 1 số long chỉ có 64 bits → chỉ đủ cho range ≤ 64.
     * - Nếu xe đậu từ vị trí 0 đến 1000 → cần 1000/64 = 16 số long.
     */
    public long minRoofLengthBitMask(long[] cars, int k) {
        if (cars == null || cars.length < k) {
            return 0;
        }

        // Tìm min và max để xác định range
        long minPos = Long.MAX_VALUE;
        long maxPos = Long.MIN_VALUE;
        for (long pos : cars) {
            minPos = Math.min(minPos, pos);
            maxPos = Math.max(maxPos, pos);
        }

        int range = (int)(maxPos - minPos + 1);
        
        // Fallback nếu range quá lớn
        if (range > 10_000_000) {
            return minRoofLength(cars, k);
        }

        // TH1: Range nhỏ (≤ 64), dùng 1 số long duy nhất
        if (range <= 64) {
            return minRoofLengthSingleBitmask(cars, k, minPos, range);
        }

        // TH2: Range lớn (> 64), dùng long[]
        // Tạo bitmask: mỗi long chứa 64 bits
        int arraySize = (range + 63) / 64; // Ceiling division
        long[] bitMask = new long[arraySize];
        
        for (long pos : cars) {
            int idx = (int)(pos - minPos);
            int wordIdx = idx / 64;           // Index trong long[]
            int bitIdx = idx % 64;            // Vị trí bit trong long
            bitMask[wordIdx] |= (1L << bitIdx); // Set bit
        }

        // Sliding window để tìm k xe gần nhau nhất
        long minLength = Long.MAX_VALUE;
        int count = 0;
        int left = 0;

        for (int right = 0; right < range; right++) {
            // Check nếu có xe tại vị trí right
            int wordIdx = right / 64;
            int bitIdx = right % 64;
            if ((bitMask[wordIdx] & (1L << bitIdx)) != 0) {
                count++;
            }

            // Khi đủ k xe, tính chiều dài
            while (count >= k) {
                long length = right - left + 1;
                minLength = Math.min(minLength, length);

                // Thu hẹp window từ bên trái
                int leftWordIdx = left / 64;
                int leftBitIdx = left % 64;
                if ((bitMask[leftWordIdx] & (1L << leftBitIdx)) != 0) {
                    count--;
                }
                left++;
            }
        }

        return minLength;
    }

    /**
     * Helper: Dùng 1 số long duy nhất làm bitmask (range ≤ 64)
     */
    private long minRoofLengthSingleBitmask(long[] cars, int k, long minPos, int range) {
        long mask = 0L; // 1 số long = 64 bits
        
        // Set bit cho từng xe
        for (long pos : cars) {
            int idx = (int)(pos - minPos);
            mask |= (1L << idx);
        }

        // Sliding window
        long minLength = Long.MAX_VALUE;
        int count = 0;
        int left = 0;

        for (int right = 0; right < range; right++) {
            // Check bit tại vị trí right
            if ((mask & (1L << right)) != 0) {
                count++;
            }

            while (count >= k) {
                long length = right - left + 1;
                minLength = Math.min(minLength, length);

                // Thu hẹp window
                if ((mask & (1L << left)) != 0) {
                    count--;
                }
                left++;
            }
        }

        return minLength;
    }

    /**
     * APPROACH 3: Counting Sort + Sliding Window (ĐƠN GIẢN NHẤT)
     * Time: O(n + range) - Linear nếu range nhỏ
     * Space: O(range)
     * 
     * Ý tưởng:
     * 1. Đánh dấu vị trí có xe trong boolean array.
     * 2. Duyệt array để lấy danh sách vị trí đã sort.
     * 3. Áp dụng sliding window như approach 1.
     * 
     * Đơn giản hơn Bit Mask vì không cần đếm trong window phức tạp.
     */
    public long minRoofLengthCountingSort(long[] cars, int k) {
        if (cars == null || cars.length < k) {
            return 0;
        }

        // Tìm min và max
        long minPos = Long.MAX_VALUE;
        long maxPos = Long.MIN_VALUE;
        for (long pos : cars) {
            minPos = Math.min(minPos, pos);
            maxPos = Math.max(maxPos, pos);
        }

        int range = (int)(maxPos - minPos + 1);
        
        // Nếu range quá lớn, fallback về sorting
        if (range > 1_000_000) {
            return minRoofLength(cars, k);
        }

        // Đánh dấu vị trí có xe
        boolean[] hasCarAt = new boolean[range];
        for (long pos : cars) {
            hasCarAt[(int)(pos - minPos)] = true;
        }

        // Thu thập vị trí đã sort
        long[] sortedPositions = new long[cars.length];
        int idx = 0;
        for (int i = 0; i < range; i++) {
            if (hasCarAt[i]) {
                sortedPositions[idx++] = i + minPos;
            }
        }

        // Sliding window trên sorted array
        long minLength = Long.MAX_VALUE;
        for (int i = 0; i <= sortedPositions.length - k; i++) {
            long length = sortedPositions[i + k - 1] - sortedPositions[i] + 1;
            minLength = Math.min(minLength, length);
        }

        return minLength;
    }

    @Test
    @DisplayName("Parking Dilemma - Standard Case")
    void testParkingDilemma() {
        long[] cars = {6, 2, 12, 7};
        int k = 3;
        // Sorted: 2, 6, 7, 12
        // Windows of size 3:
        // [2, 6, 7] -> length = 7 - 2 + 1 = 6
        // [6, 7, 12] -> length = 12 - 6 + 1 = 7
        // assertThat(minRoofLength(cars, k)).isEqualTo(6);
        assertThat(minRoofLengthBitMask(cars, k)).isEqualTo(6);
        // assertThat(minRoofLengthCountingSort(cars, k)).isEqualTo(6);
    }

    @Test
    @DisplayName("Parking Dilemma - All cars covered")
    void testAllCarsCovered() {
        long[] cars = {1, 10, 20};
        int k = 3;
        assertThat(minRoofLength(cars, k)).isEqualTo(20 - 1 + 1);
        assertThat(minRoofLengthBitMask(cars, k)).isEqualTo(20 - 1 + 1);
        assertThat(minRoofLengthCountingSort(cars, k)).isEqualTo(20 - 1 + 1);
    }

    @Test
    @DisplayName("Parking Dilemma - Bit Mask Edge Case")
    void testBitMaskEdgeCase() {
        long[] cars = {1, 2, 3, 100};
        int k = 2;
        // Min roof for 2 cars: [1, 2] -> length = 2
        assertThat(minRoofLengthBitMask(cars, k)).isEqualTo(2);
    }
}
