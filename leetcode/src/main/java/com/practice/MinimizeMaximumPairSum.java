package com.practice;

import java.util.Arrays;
import java.util.Random;

public class MinimizeMaximumPairSum {

    // Standard Approach: O(N log N) using built-in dual-pivot Quicksort
    public int minPairSumStandard(int[] nums) {
        Arrays.sort(nums);
        int max = 0;
        int n = nums.length;
        for (int i = 0; i < n / 2; i++) {
            max = Math.max(max, nums[i] + nums[n - 1 - i]);
        }
        return max;
    }

    // Counting Sort Approach: O(N + K) where K is range of numbers
    // Valid because nums[i] <= 100000
    public int minPairSumCounting(int[] nums) {
        int maxVal = 100000;
        int[] count = new int[maxVal + 1];
        for (int num : nums) {
            count[num]++;
        }

        int[] sorted = new int[nums.length];
        int index = 0;
        for (int i = 0; i <= maxVal; i++) {
            while (count[i] > 0) {
                sorted[index++] = i;
                count[i]--;
            }
        }

        // Similar to standard logic, but verifying if we can just walk pointers
        // Reconstructing array is O(N) anyway.
        int max = 0;
        int n = sorted.length;
        for (int i = 0; i < n / 2; i++) {
            max = Math.max(max, sorted[i] + sorted[n - 1 - i]);
        }
        return max;
    }

    // Radix Sort Approach: O(N * D) where D is number of digits (approx 5-6)
    // Uses bit manipulation principles
    public int minPairSumRadix(int[] nums) {
        int max = 0;
        for (int num : nums) {
            max = Math.max(max, num);
        }

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countSortRadix(nums, exp);
        }

        int maxPair = 0;
        int n = nums.length;
        for (int i = 0; i < n / 2; i++) {
            maxPair = Math.max(maxPair, nums[i] + nums[n - 1 - i]);
        }
        return maxPair;
    }

    // Pure Bit Manipulation Approach (Binary Radix Sort)
    // Examines bits from 0 to 30.
    public int minPairSumBitManip(int[] nums) {
        // We know constraints are <= 10^5, so 17 bits (2^17 = 131072) is enough.
        // Or generic 31 bits.
        int n = nums.length;
        int[] output = new int[n];

        for (int shift = 0; shift < 18; shift++) { // 10^5 < 2^17
            // Count loops for 0 and 1
            int zeros = 0;
            for (int num : nums) {
                if (((num >> shift) & 1) == 0) {
                    zeros++;
                }
            }

            int ones = zeros; // Start position for ones
            zeros = 0; // Start position for zeros

            for (int num : nums) {
                if (((num >> shift) & 1) == 0) {
                    output[zeros++] = num;
                } else {
                    output[ones++] = num;
                }
            }

            // Copy back
            System.arraycopy(output, 0, nums, 0, n);

            // Check if we are done (optional optimization: check if max < 1<<shift)
            // simplified for this demo
        }

        int max = 0;
        for (int i = 0; i < n / 2; i++) {
            max = Math.max(max, nums[i] + nums[n - 1 - i]);
        }
        return max;
    }

    private void countSortRadix(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];
        Arrays.fill(count, 0);

        for (int i = 0; i < n; i++) {
            count[(arr[i] / exp) % 10]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }

        System.arraycopy(output, 0, arr, 0, n);
    }

    public static void main(String[] args) {
        MinimizeMaximumPairSum solver = new MinimizeMaximumPairSum();
        int N = 100000; // Large input
        int[] nums = new int[N];
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            nums[i] = rand.nextInt(100000) + 1;
        }

        // Test Standard
        int[] nums1 = Arrays.copyOf(nums, N);
        long start = System.nanoTime();
        int res1 = solver.minPairSumStandard(nums1);
        long end = System.nanoTime();
        System.out.println("Standard Sort: " + (end - start) / 1e6 + " ms. Result: " + res1);

        // Test Radix
        int[] nums2 = Arrays.copyOf(nums, N);
        start = System.nanoTime();
        int res2 = solver.minPairSumRadix(nums2);
        end = System.nanoTime();
        System.out.println("Radix Sort:    " + (end - start) / 1e6 + " ms. Result: " + res2);

        // Test Counting
        int[] nums3 = Arrays.copyOf(nums, N);
        start = System.nanoTime();
        int res3 = solver.minPairSumCounting(nums3);
        end = System.nanoTime();
        System.out.println("Counting Sort: " + (end - start) / 1e6 + " ms. Result: " + res3);

        // Test Bit Manip (Binary Radix)
        int[] nums4 = Arrays.copyOf(nums, N);
        start = System.nanoTime();
        int res4 = solver.minPairSumBitManip(nums4);
        end = System.nanoTime();
        System.out.println("Bit Manip Sort:" + (end - start) / 1e6 + " ms. Result: " + res4);

        System.out.println("\nConclusion: Counting Sort is generally fastest because range K ~ N.");
        System.out.println(
                "Bit Manip (Base-2 Radix) is also O(N) but iterates 17-18 times (bits) vs 5-6 times (digits), usually slower than Base-10 Radix.");
        System.out.println("Standard Sort is O(N log N) but Java's Dual-Pivot Quicksort is extremely optimized.");
    }
}
