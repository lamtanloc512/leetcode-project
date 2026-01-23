package com.practice.leetcode.backtracking;

import java.util.ArrayList;
import java.util.List;

public class GrayCode {

    // ═══════════════════════════════════════════════════════════════════════════
    // VISUALIZATION & EXPLANATION
    // ═══════════════════════════════════════════════════════════════════════════
    /**
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ 1. Gray Code là gì? (Definition)                                            │
     * ├─────────────────────────────────────────────────────────────────────────────┤
     * │ Gray Code là chuỗi số nhị phân mà hai số LIÊN TIẾP chỉ khác nhau 1 bit.     │
     * └─────────────────────────────────────────────────────────────────────────────┘
     *
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ 2. Cách tạo theo phương pháp phản chiếu (Recursive Reflection)              │
     * ├─────────────────────────────────────────────────────────────────────────────┤
     * │ Ý tưởng:                                                                    │
     * │ - Lấy chuỗi Gray code (n-1) bit.                                            │
     * │ - Đảo ngược thứ tự (Reflect) chuỗi đó.                                      │
     * │ - Nửa đầu: thêm bit 0 vào trước.                                            │
     * │ - Nửa sau (đảo ngược): thêm bit 1 vào trước.                                │
     * │                                                                             │
     * │ Ví dụ tạo n=2 từ n=1:                                                       │
     * │                                                                             │
     * │   n=1      Reflect      Prefix 0 (Top)    Prefix 1 (Bottom)     Result (n=2)│
     * │  ┌───┐    ┌───────┐    ┌──────────────┐  ┌────────────────┐    ┌────────────┐
     * │  │ 0 │    │   1   │    │   0 + [0]    │  │    1 + [1]     │    │ 00 (0)     │
     * │  │ 1 │────┼───────┼───►│   0 + [1]    │  │    1 + [0]     │    │ 01 (1)     │
     * │  └───┘    │   0   │    └──────────────┘  └────────────────┘    │ 11 (3)     │
     * │           └───────┘                                            │ 10 (2)     │
     * │                                                                └────────────┘
     * └─────────────────────────────────────────────────────────────────────────────┘
     *
     * ┌─────────────────────────────────────────────────────────────────────────────┐
     * │ 3. Công thức Bitwise (Formula Logic)                                        │
     * ├─────────────────────────────────────────────────────────────────────────────┤
     * │ G(i) = i ^ (i >> 1)                                                         │
     * │                                                                             │
     * │ Tại sao công thức này hoạt động?                                            │
     * │ Nó đảm bảo tính chất "thay đổi 1 bit".                                      │
     * │                                                                             │
     * │ Minh họa với n=3:                                                           │
     * │                                                                             │
     * │   i (Binary)   |   i >> 1     |   Result (XOR)   |  Decimal                 │
     * │  ──────────────┼──────────────┼──────────────────┼──────────                │
     * │   000 (0)      |    000       |    000 ^ 000     |  000 (0)                 │
     * │   001 (1)      |    000       |    001 ^ 000     |  001 (1)                 │
     * │   010 (2)      |    001       |    010 ^ 001     |  011 (3)                 │
     * │   011 (3)      |    001       |    011 ^ 001     |  010 (2)                 │
     * │   100 (4)      |    010       |    100 ^ 010     |  110 (6)                 │
     * │   101 (5)      |    010       |    101 ^ 010     |  111 (7)                 │
     * │   110 (6)      |    011       |    110 ^ 011     |  101 (5)                 │
     * │   111 (7)      |    011       |    111 ^ 011     |  100 (4)                 │
     * └─────────────────────────────────────────────────────────────────────────────┘
     */
    
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
