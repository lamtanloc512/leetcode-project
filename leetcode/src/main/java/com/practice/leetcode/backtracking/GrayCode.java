package com.practice.leetcode.backtracking;

import java.util.ArrayList;
import java.util.List;

public class GrayCode {

    /**
     * Generates an n-bit Gray Code sequence using the formula: G(i) = i ^ (i >> 1).
     *
     * @param n The number of bits.
     * @return A list of integers representing the Gray Code sequence.
     */
    public List<Integer> grayCode(int n) {
        List<Integer> result = new ArrayList<>();
        // Total numbers in n-bit Gray Code is 2^n
        int limit = 1 << n; // 2^n

        for (int i = 0; i < limit; i++) {
            // Apply the formula:
            // The i-th Gray Code is obtained by XORing i with i shifted right by 1.
            int grayReq = i ^ (i >> 1);
            result.add(grayReq);
        }

        return result;
    }

    public static void main(String[] args) {
        GrayCode solver = new GrayCode();

        // Verification
        System.out.println("n = 1: " + solver.grayCode(1));
        System.out.println("n = 2: " + solver.grayCode(2));
        System.out.println("n = 3: " + solver.grayCode(3));

        // Validation logic for n=3
        List<Integer> r3 = solver.grayCode(3);
        boolean isValid = true;
        for (int i = 0; i < r3.size(); i++) {
            int curr = r3.get(i);
            int next = r3.get((i + 1) % r3.size());
            int diff = curr ^ next;
            // Check if diff is a power of 2 (exactly 1 bit difference)
            if (diff == 0 || (diff & (diff - 1)) != 0) {
                isValid = false;
                System.out.println("Invalid transition between " + curr + " and " + next);
            }
        }
        System.out.println("n = 3 Valid? " + isValid);
    }
}
