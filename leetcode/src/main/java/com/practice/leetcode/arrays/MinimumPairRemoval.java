package com.practice.leetcode.arrays;

import java.util.ArrayList;
import java.util.List;

public class MinimumPairRemoval {

    public int minPairRemoval(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            list.add(num);
        }

        int operations = 0;
        while (!isSorted(list)) {
            operations++;
            int minSum = Integer.MAX_VALUE;
            int minIndex = -1;

            // Find the adjacent pair with the minimum sum
            // If multiple exist, chose the leftmost one (which this loop does naturally)
            for (int i = 0; i < list.size() - 1; i++) {
                int sum = list.get(i) + list.get(i + 1);
                if (sum < minSum) {
                    minSum = sum;
                    minIndex = i;
                }
            }

            // Replace the pair with their sum
            if (minIndex != -1) {
                // Remove elements at minIndex and minIndex + 1
                // Removing minIndex twice works because the next element shifts down
                list.remove(minIndex);
                list.remove(minIndex); // This was originally at minIndex + 1
                list.add(minIndex, minSum);
            }
        }

        return operations;
    }

    private boolean isSorted(List<Integer> list) {
        if (list.size() <= 1) {
            return true;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        MinimumPairRemoval solver = new MinimumPairRemoval();

        // Test Case 1
        int[] nums1 = {2, 1, 3, 2, 4};
        // 2, 1 -> 3 => [3, 3, 2, 4]
        // 3, 2 -> 5 => [3, 5, 4]
        // 5, 4 -> 9 => [3, 9] (Sorted)
        // Operations: 3? Or maybe better choices?
        // Wait, "Select the adjacent pair with the minimum sum".
        // [2, 1, 3, 2, 4]
        // Pairs: (2+1)=3, (1+3)=4, (3+2)=5, (2+4)=6. Min is 3 at index 0.
        // Replace (2, 1) with 3. Array: [3, 3, 2, 4].
        // Pairs: (3+3)=6, (3+2)=5, (2+4)=6. Min is 5 at index 1 (values 3, 2).
        // Replace (3, 2) with 5. Array: [3, 5, 4].
        // Pairs: (3+5)=8, (5+4)=9. Min is 8 at index 0.
        // Replace (3, 5) with 8. Array: [8, 4].
        // Pairs: (8+4)=12. Min is 12.
        // Replace (8, 4) with 12. Array: [12]. Sorted.
        // Total ops: 4.
        // Let's check logic.

        System.out.println("Test Case 1 Result: " + solver.minPairRemoval(nums1));

        // Test Case 2: Sorted
        int[] nums2 = {1, 2, 3, 4};
        System.out.println("Test Case 2 Result: " + solver.minPairRemoval(nums2));

        // Test Case 3: Reverse sorted
        int[] nums3 = {5, 4, 3};
        System.out.println("Test Case 3 Result: " + solver.minPairRemoval(nums3));
    }
}
