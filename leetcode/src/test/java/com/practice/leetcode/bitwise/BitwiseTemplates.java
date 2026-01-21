package com.practice.leetcode.bitwise;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ BITWISE TEMPLATES                                                         ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class BitwiseTemplates {

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 1: BASIC OPERATIONS (SET, GET, CLEAR, TOGGLE)
  // ═══════════════════════════════════════════════════════════════════════════
  
  // Kiểm tra bit thứ i có bật không
  public boolean getBit(int n, int i) {
    return (n & (1 << i)) != 0;
  }

  // Bật bit thứ i (lên 1)
  public int setBit(int n, int i) {
    return n | (1 << i);
  }

  // Tắt bit thứ i (về 0)
  public int clearBit(int n, int i) {
    return n & ~(1 << i);
  }

  // Đảo bit thứ i (0->1, 1->0)
  public int toggleBit(int n, int i) {
    return n ^ (1 << i);
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 2: BIT TRICKS (MẸO HAY DÙNG)
  // ═══════════════════════════════════════════════════════════════════════════
  
  // Xóa bit 1 thấp nhất (Lowest Set Bit) -> n & (n-1)
  // Dùng để đếm số bit 1, hoặc check lũy thừa 2
  public int removeLowestSetBit(int n) {
    return n & (n - 1);
  }

  // Lấy bit 1 thấp nhất -> n & -n
  // Ví dụ: 1010 (10) -> 0010 (2)
  public int getLowestSetBit(int n) {
    return n & -n;
  }

  // Kiểm tra lũy thừa 2 (Power of Two)
  public boolean isPowerOfTwo(int n) {
    return n > 0 && (n & (n - 1)) == 0;
  }

  // Đếm số bit 1 (Brian Kernighan’s Algorithm - O(số bit 1))
  public int countSetBits(int n) {
    int count = 0;
    while (n != 0) {
      n &= (n - 1); // Xóa dần bit 1 cuối cùng
      count++;
    }
    return count;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATTERN 3: XOR TRICKS
  // ═══════════════════════════════════════════════════════════════════════════
  
  // Tìm số xuất hiện 1 lần (Single Number) trong mảng mà các số khác xuất hiện 2 lần
  public int singleNumber(int[] nums) {
    int result = 0;
    for (int num : nums) {
      result ^= num;
    }
    return result;
  }

  // Swap 2 số không cần biến tạm
  public void swapXOR(int a, int b) {
    a = a ^ b;
    b = a ^ b; // b thành a cũ
    a = a ^ b; // a thành b cũ
    // Lưu ý: Chỉ work nếu a và b là biến riêng biệt (không tham chiếu cùng ô nhớ)
  }
}
