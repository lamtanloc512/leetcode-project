package com.practice.leetcode.bitwise;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║          187. Repeated DNA Sequences                                      ║
 * ╠═══════════════════════════════════════════════════════════════════════════╣
 * ║ Difficulty: Medium                                                        ║
 * ║ Topics: Hash Table, String, Bit Manipulation, Sliding Window, Rolling Hash║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class LC187_RepeatedDNASequences {

  // ═══════════════════════════════════════════════════════════════════════════
  // PROBLEM ANALYSIS (Phân tích đề bài)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ ĐỀ BÀI:                                                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ Cho một chuỗi DNA `s` chỉ chứa các ký tự 'A', 'C', 'G', 'T'.                │
   * │ Trả về tất cả các chuỗi con độ dài 10 (10-letter-long sequences)            │
   * │ xuất hiện nhiều hơn một lần trong `s`.                                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ PHÂN TÍCH BIT MANIPULATION (Rolling Hash/Mask):                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 1. Mã hóa ký tự (Encoding):                                                 │
   * │    Ta chỉ có 4 ký tự, nên chỉ cần 2 bit để biểu diễn:                       │
   * │    A = 00, C = 01, G = 10, T = 11                                           │
   * │    (Mẹo: Dùng ASCII & 7 hoặc map thủ công đều được, ta sẽ map thủ công)     │
   * │                                                                             │
   * │ 2. Kích thước cửa sổ (Sliding Window):                                      │
   * │    Độ dài chuỗi cần tìm = 10.                                               │
   * │    Mỗi ký tự 2 bit -> Tổng 20 bit cho một chuỗi.                            │
   * │    20 bit nằm gọn trong một số kiểu `int` (32 bit).                         │
   * │                                                                             │
   * │ 3. Cơ chế trượt (Rolling):                                                  │
   * │    Khi cửa sổ trượt sang phải 1 bước:                                       │
   * │    - Bỏ ký tự cũ nhất bên trái (tương ứng 2 bit cao nhất của 20 bit).       │
   * │    - Thêm ký tự mới nhất bên phải (tương ứng 2 bit thấp nhất).              │
   * │                                                                             │
   * │    Công thức cập nhật mask:                                                 │
   * │    mask = ((mask << 2) | new_char_bits) & 0xFFFFF                           │
   * │                                                                             │
   * │    Giải thích:                                                              │
   * │    - `mask << 2`: Đẩy các bit sang trái để lấy chỗ cho ký tự mới.           │
   * │    - `| new_char_bits`: Thêm mã của ký tự mới vào 2 bit cuối.               │
   * │    - `& 0xFFFFF`: Giữ lại đúng 20 bit cuối (tương ứng 10 ký tự).            │
   * │      (0xFFFFF = 11111111111111111111 binary - 20 số 1)                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SOLUTION
  // ═══════════════════════════════════════════════════════════════════════════

  public List<String> findRepeatedDnaSequences(String s) {
    // Nếu chuỗi ngắn hơn 10 ký tự, không thể có chuỗi con độ dài 10
    if (s == null || s.length() < 10) {
      return new ArrayList<>();
    }

    // `seen`: lưu các mask đã gặp 1 lần
    Set<Integer> seen = new HashSet<>();
    // `repeated`: lưu các chuỗi kết quả (để tránh duplicate trong kết quả)
    Set<String> repeated = new HashSet<>();
    
    // Map character to integer code: A=0, C=1, G=2, T=3
    int[] charToBit = new int[26];
    charToBit['A' - 'A'] = 0; // 00
    charToBit['C' - 'A'] = 1; // 01
    charToBit['G' - 'A'] = 2; // 10
    charToBit['T' - 'A'] = 3; // 11

    int mask = 0;
    
    // ┌───────────────────────────────────────────────────────────────────────────┐
    // │ BƯỚC 1: KHỞI TẠO CỬA SỔ (PRE-LOAD)                                    │
    // ├───────────────────────────────────────────────────────────────────────────┤
    // │ Ta cần một cửa sổ 10 ký tự để kiểm tra.                               │
    // │ Vòng lặp này chỉ chạy 9 lần (0 đến 8) để nạp 9 ký tự đầu tiên vào mask.│
    // │ Lúc này mask mới có 18 bit (9 x 2).                                   │
    // │                                                                       │
    // │ Tại sao chỉ 9?                                                        │
    // │ Để vòng lặp chính (bên dưới) có cấu trúc đồng nhất:                   │
    // │ "Nạp thêm 1 ký tự cuối" -> "Kiểm tra đủ 10 chưa" -> "Xử lý".          │
    // └───────────────────────────────────────────────────────────────────────────┘
    for (int i = 0; i < 9; i++) {
      mask = (mask << 2) | charToBit[s.charAt(i) - 'A'];
    }

    // Bắt đầu trượt từ ký tự thứ 10
    for (int i = 9; i < s.length(); i++) {
        // Cập nhật mask:
        // 1. Đẩy sang trái 2 bit (bỏ ký tự cũ nhất ở đầu)
        // 2. Thêm 2 bit của ký tự mới vào cuối
        // 3. Giữ lại đúng 20 bit cuối bằng AND 0xFFFFF
        //    Tại sao 5 chữ F? 
        //    - 1 chữ Hex (F) = 4 bit (1111)
        //    - Ta cần 20 bit -> 20 / 4 = 5 chữ F.
        //    - 0xFFFFF = 1111 1111 1111 1111 1111 (20 bits 1)
        mask = ((mask << 2) | charToBit[s.charAt(i) - 'A']) & 0xFFFFF;

        if (seen.contains(mask)) {
            // Nếu mask này đã gặp rồi -> đây là chuỗi lặp lại
            // Lấy substring từ chuỗi gốc để đưa vào kết quả
            // Substring từ (i - 9) đến (i + 1)
            repeated.add(s.substring(i - 9, i + 1));
        } else {
            // Đánh dấu đã gặp mask này
            seen.add(mask);
        }
    }

    return new ArrayList<>(repeated);
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // COMPLEXITY ANALYSIS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Time Complexity: O(N)
   *   - Duyệt qua chuỗi s một lần (N ký tự).
   *   - Các thao tác bitwise, hash set add/contains là O(1) trung bình.
   *   - Substring tốn O(L) với L=10, coi như hằng số O(1).
   *
   * Space Complexity: O(min(N, 4^10))
   *   - `seen` set chứa tối đa N phần tử (hoặc 4^10 trường hợp).
   *   - `repeated` set chứa các chuỗi kết quả.
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // TESTS
  // ═══════════════════════════════════════════════════════════════════════════

  @Test
  void testExample1() {
    // Input: s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
    // Output: ["AAAAACCCCC","CCCCCAAAAA"]
    String s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
    List<String> result = findRepeatedDnaSequences(s);
    
    assertTrue(result.contains("AAAAACCCCC"));
    assertTrue(result.contains("CCCCCAAAAA"));
    assertEquals(2, result.size());
  }

  @Test
  void testExample2() {
    // Input: s = "AAAAAAAAAAAAA"
    // Output: ["AAAAAAAAAA"]
    String s = "AAAAAAAAAAAAA";
    List<String> result = findRepeatedDnaSequences(s);
    
    assertEquals(1, result.size());
    assertEquals("AAAAAAAAAA", result.get(0));
  }

  @Test
  void testShortString() {
    // Chuỗi ngắn hơn 10 ký tự -> 0 kết quả
    String s = "ACGT";
    List<String> result = findRepeatedDnaSequences(s);
    assertTrue(result.isEmpty());
  }
  
  @Test
  void testNoRepeats() {
    // Chuỗi dài nhưng không lặp
    String s = "ACGTACGTACGT"; // 12 chars: ACGTACGTAC, CGTACGTACG, GTACGTACGT - đều khác nhau
    List<String> result = findRepeatedDnaSequences(s);
    assertTrue(result.isEmpty());
  }

  @Test
  void testOverlappingRepeats() {
      // 11 chữ A: AAAAAAAAAA (index 0) và AAAAAAAAAA (index 1) -> 1 kết quả duy nhất
      // 12 chữ A: thêm 1 lần nữa -> vẫn 1 kết quả ("AAAAAAAAAA") trong list vì Set khử trùng
     String s = "AAAAAAAAAAA"; // 11 'A's
     List<String> result = findRepeatedDnaSequences(s);
     assertEquals(1, result.size());
     assertEquals("AAAAAAAAAA", result.get(0));
  }
}
