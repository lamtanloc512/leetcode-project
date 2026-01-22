package com.practice.leetcode.bitwise;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ LEETCODE 137: SINGLE NUMBER II - TÀI LIỆU TOÀN TẬP (ULTIMATE GUIDE)       ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. TỪ TƯ DUY BAN ĐẦU ĐẾN GIẢI PHÁP (INTUITION)
 * ─────────────────────────────────────────────────────────────────────────────
 *    - Tư duy: "XOR (^) giúp 2 số giống nhau triệt tiêu về 0 (A^A=0). 
 *      Ước gì có phép toán làm 3 số giống nhau triệt tiêu về 0?"
 *    - Bản chất: XOR là cộng Modulo 2 (1+1=0). Ta cần cộng Modulo 3 (1+1+1=0).
 *    - Giải pháp: Vì máy tính chỉ có hệ nhị phân, ta dùng 2 bit (`ones`, `twos`) 
 *      để giả lập một "bộ đếm hệ 3".
 *
 * 2. SO SÁNH TƯ DUY: BITWISE vs TOÁN HỌC (ANALOGIES)
 * ─────────────────────────────────────────────────────────────────────────────
 *    Bạn hỏi: "& giống cộng, | giống merge, ^ giống trừ?" -> Gần đúng, nhưng chính xác hơn là:
 *
 *    - `|` (OR)   ≈ MERGE/UNION (Gộp).
 *      (Có gì lấy nấy, gộp tất cả số 1 lại).
 *      Ví dụ: `Set A | Set B`.
 *
 *    - `&` (AND)  ≈ INTERSECTION (Giao nhau/Tìm điểm chung).
 *      (Chỉ giữ lại cái gì CẢ HAI cùng có).
 *      Ví dụ: `ones & num` -> Tìm bit vừa có trong `ones` vừa có trong `num`.
 *      *Lưu ý*: Nó KHÔNG phải cộng. Trong mạch điện tử, nó dùng để tính số nhớ (carry) của phép cộng.
 *
 *    - `^` (XOR)  ≈ TOGGLE (Công tắc) / CỘNG KHÔNG NHỚ (Modulo 2).
 *      (Khác nhau thì bật, giống nhau thì tắt).
 *      `1^1=0` (Triệt tiêu giống phép trừ).
 *      `0^1=1` (Giữ nguyên giống phép cộng).
 *      => Nó là phép toán "đảo trạng thái".
 *
 * 3. KHI NÀO DÙNG `~` (NOT)?
 * ─────────────────────────────────────────────────────────────────────────────
 *    Phép `~` đảo ngược tất cả bit (0->1, 1->0). Nó thường dùng trong 2 trường hợp chính:
 *
 *    a) TẠO MẶT NẠ ĐỂ XÓA (Clean/Unset bits) - *Dùng trong bài này*
 *       - Bạn muốn xóa (turn off) các bit được quy định bởi biến `mask`.
 *       - Công thức: `x &= ~mask;`
 *       - Ví dụ: `mask = 0010` (muốn xóa bit 1).
 *         `~mask = 1101` (giữ tất cả trừ bit 1).
 *         `x & 1101` -> Kết quả là x nhưng bit 1 bị ép về 0.
 *
 *    b) SỐ ÂM (Two's Complement)
 *       - Trong máy tính: `-x = ~x + 1`.
 *       - Ví dụ: Muốn lấy số đối của x, ta đảo bit rồi cộng 1.
 *
 * 4. TẠI SAO PHẢI TÍNH TWOS TRƯỚC RỒI MỚI TÍNH ONES?
 * ─────────────────────────────────────────────────────────────────────────────
 *    - Vấn đề: Sự phụ thuộc dữ liệu (Data Dependency).
 *    - Phải tính `twos` (ai sẽ lên lớp?) dựa trên `ones` CŨ, 
 *      sau đó mới cập nhật `ones` (cho tốt nghiệp/rời lớp cũ).
 */
public class SingleNumber2Guide {

    public static void main(String[] args) {
        System.out.println("=== CHẠY THỬ NGHIỆM VỚI INPUT [2, 2, 3, 2] ===");
        // 2 (binary 10), 3 (binary 11)
        int[] nums = {2, 2, 3, 2};
        int result = singleNumberWithExplanation(nums);
        System.out.println("\n>>> KẾT QUẢ CUỐI CÙNG: " + result);
    }

    public static int singleNumberWithExplanation(int[] nums) {
        int ones = 0; // Bộ nhớ các bit xuất hiện 1 lần (dư 1)
        int twos = 0; // Bộ nhớ các bit xuất hiện 2 lần (dư 2)

        System.out.println("Cơ chế: Giả lập phép cộng Modulo 3 (1+1+1=0)");
        
        for (int num : nums) {
            System.out.println("\n--- Xử lý số: " + num + " (" + Integer.toBinaryString(num) + ") ---");
            
            // BƯỚC 1: XÁC ĐỊNH CÁC BIT LÊN CẤP 2 (Update Twos)
            // Dùng AND (&) để TÌM GIAO NHAU giữa ones và num -> chính là bit lập lại lần 2.
            // Dùng OR (|) để GỘP các bit này vào twos.
            int incomingTwos = ones & num; 
            twos = twos | incomingTwos;
            System.out.printf("   [Twos update] (Ones cũ & num) -> Thăng hạng lên Twos: %s%n", Integer.toBinaryString(twos));

            // BƯỚC 2: CẬP NHẬT ONES (Update Ones)
            // Dùng XOR (^) để ĐẢO TRẠNG THÁI. Có thì mất, không thì có.
            ones = ones ^ num;
            System.out.printf("   [Ones update] (XOR) -> Đảo trạng thái Ones:       %s%n", Integer.toBinaryString(ones));

            // BƯỚC 3: XỬ LÝ TRƯỜNG HỢP "3 LẦN" (Reset Modulo 3)
            // Nếu bit nào vừa có mặt ở cả Ones và Twos -> 11 (3 lần).
            int threes = ones & twos;
            
            if (threes != 0) {
                System.out.printf("   [RESET] Phát hiện bit đạt 3 lần (mask %s) -> Xóa sạch!%n", Integer.toBinaryString(threes));
                
                // Dùng ~ (NOT) để tạo mặt nạ xóa (Unset Mask)
                // threes = ...0010 -> ~threes = ...1101
                // AND với mask này sẽ giữ nguyên mọi thứ trừ bit tại vị trí threes.
                int mask = ~threes;
                ones &= mask;
                twos &= mask;
            }
            
            System.out.printf("   => Kết thúc vòng: Ones=%s, Twos=%s%n", Integer.toBinaryString(ones), Integer.toBinaryString(twos));
        }
        
        return ones;
    }
}