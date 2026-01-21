package com.practice.leetcode.bitwise;

/**
 * Class này giúp trực quan hóa quá trình Reverse Bits bằng thuật toán Loop (Iterative).
 * Chạy main() để xem kết quả visual trên console.
 */
public class ReverseBitsVisualizer {

  public static void main(String[] args) throws InterruptedException {
    int n = 0b00000000000000000000000000001101; // Ví dụ số 13 (1101) cho dễ nhìn
    // int n = 43261596; 
    
    System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
    System.out.println("║                TRỰC QUAN HÓA REVERSE BITS (ITERATIVE)                ║");
    System.out.println("║                (Chuyển bit từ vị trí i -> 31-i)                      ║");
    System.out.println("╚══════════════════════════════════════════════════════════════════════╝\n");

    System.out.println("INPUT n:      " + toBinary(n));
    System.out.println("Mục tiêu:     Lấy bit ở [i] của n -> Đặt vào [31-i] của result\n");
    
    int result = 0;
    
    // Chỉ demo 4 bit đầu và 4 bit cuối để đỡ dài, hoặc chạy hết nếu cần
    // Ở đây mình chạy mẫu vài vòng lặp đầu tiên để user hiểu nguyên lý
    
    for (int i = 0; i < 32; i++) {
      // Chỉ in chi tiết cho 4 vòng đầu và 4 vòng cuối
      boolean showDetail = (i < 4) || (i >= 28);
      
      // 1. Lấy bit tại i
      int bit = (n >> i) & 1;
      
      // 2. Đặt vào vị trí 31-i
      result |= (bit << (31 - i));
      
      if (showDetail) {
        System.out.println("LOOP i = " + String.format("%2d", i) + " --------------------------------------------------------");
        
        // Visual dòng n (Source)
        System.out.println("n (Source):   " + toBinary(n));
        System.out.println("              " + getPointer(i) + " (Lấy bit " + bit + " tại index " + i + ")");
        
        // Visual mũi tên di chuyển
        // System.out.println("                       ↓ DI CHUYỂN ĐẾN index " + (31-i));
        
        // Visual dòng result (Target)
        System.out.println("res (Dest):   " + toBinary(result));
        System.out.println("              " + getPointer(31 - i) + " (Đặt bit " + bit + " vào index " + (31-i) + ")");
        System.out.println();
      } else if (i == 4) {
        System.out.println("... (tiếp tục lặp i từ 4 đến 27) ...\n");
      }
    }

    System.out.println("========================================================================");
    System.out.println("KẾT QUẢ CUỐI CÙNG:");
    System.out.println("Input:  " + toBinary(n) + " (" + n + ")");
    System.out.println("Output: " + toBinary(result) + " (" + result + ")");
  }

  // Hàm in binary 32 bit đầy đủ
  private static String toBinary(int n) {
    return String.format("%32s", Integer.toBinaryString(n)).replace(' ', '0');
  }

  // Tạo mũi tên chỉ vào vị trí k (tính từ phải sang trái, 0-indexed)
  // String binary dài 32 ký tự. Vị trí k tương ứng index (31 - k) trong string.
  private static String getPointer(int k) {
    StringBuilder sb = new StringBuilder();
    for (int j = 0; j < 32; j++) {
      sb.append(" ");
    }
    // String in ra là từ MSB (bit 31) đến LSB (bit 0)
    // Bit k nằm ở vị trí thứ (31 - k) từ trái sang
    sb.setCharAt(31 - k, '^'); 
    return sb.toString();
  }
}

