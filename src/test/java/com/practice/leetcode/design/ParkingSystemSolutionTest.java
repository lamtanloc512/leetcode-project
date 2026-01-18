package com.practice.leetcode.design;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DESIGN PARKING SYSTEM - Thiết kế hệ thống đậu xe
 * 
 * Bài toán: Thiết kế bãi đậu xe với 3 loại:
 * - Big (lớn)
 * - Medium (trung bình)
 * - Small (nhỏ)
 * 
 * Mỗi loại có số chỗ giới hạn. Xe chỉ đậu vào chỗ đúng loại.
 */
class ParkingSystemSolutionTest {

  /**
   * THUẬT TOÁN: Array đếm chỗ trống
   * 
   * Ý tưởng đơn giản:
   * - Dùng array slots[3] lưu số chỗ còn trống của mỗi loại
   * - addCar(type): Kiểm tra và giảm slots[type-1] nếu còn chỗ
   * 
   * Ví dụ: big=1, medium=1, small=0
   * 
   * Initial: slots = [1, 1, 0]
   * 
   * addCar(1) → slots[0]=1 > 0 → slots=[0,1,0], return true
   * addCar(2) → slots[1]=1 > 0 → slots=[0,0,0], return true
   * addCar(3) → slots[2]=0 → return false
   * addCar(1) → slots[0]=0 → return false
   * 
   * Time: O(1) cho mỗi operation
   * Space: O(1)
   */
  static class ParkingSystem {
    private int[] slots;

    public ParkingSystem(int big, int medium, int small) {
      slots = new int[] { big, medium, small };
    }

    public boolean addCar(int carType) {
      if (slots[carType - 1] > 0) {
        slots[carType - 1]--;
        return true;
      }
      return false;
    }
  }

  /**
   * PHIÊN BẢN NÂNG CAO: Tracking vị trí cụ thể
   * 
   * Thực tế phỏng vấn có thể hỏi thêm:
   * - Trả về vị trí đậu xe
   * - Tìm chỗ trống gần nhất
   * - Hỗ trợ rời bãi
   */
  static class ParkingSystemAdvanced {
    private TreeSet<Integer>[] availableSpots;
    private Map<Integer, int[]> parkedCars; // carId -> [type, spotId]

    @SuppressWarnings("unchecked")
    public ParkingSystemAdvanced(int big, int medium, int small) {
      availableSpots = new TreeSet[3];
      for (int i = 0; i < 3; i++) {
        availableSpots[i] = new TreeSet<>();
      }

      // Khởi tạo các chỗ đậu: 0 đến big-1 cho big, ...
      for (int i = 0; i < big; i++)
        availableSpots[0].add(i);
      for (int i = 0; i < medium; i++)
        availableSpots[1].add(i);
      for (int i = 0; i < small; i++)
        availableSpots[2].add(i);

      parkedCars = new HashMap<>();
    }

    /**
     * Đậu xe, trả về vị trí (hoặc -1 nếu hết chỗ)
     */
    public int parkCar(int carId, int carType) {
      TreeSet<Integer> spots = availableSpots[carType - 1];
      if (spots.isEmpty())
        return -1;

      int spot = spots.pollFirst(); // Lấy chỗ nhỏ nhất (gần nhất)
      parkedCars.put(carId, new int[] { carType, spot });
      return spot;
    }

    /**
     * Xe rời bãi
     */
    public boolean leaveCar(int carId) {
      if (!parkedCars.containsKey(carId))
        return false;

      int[] info = parkedCars.remove(carId);
      int type = info[0];
      int spot = info[1];
      availableSpots[type - 1].add(spot); // Trả lại chỗ
      return true;
    }

    /**
     * Đếm chỗ trống theo loại
     */
    public int getAvailableSpots(int carType) {
      return availableSpots[carType - 1].size();
    }
  }

  // ========== TESTS ==========

  @Test
  @DisplayName("Basic parking system")
  void testBasicParking() {
    ParkingSystem parking = new ParkingSystem(1, 1, 0);
    assertThat(parking.addCar(1)).isTrue(); // big
    assertThat(parking.addCar(2)).isTrue(); // medium
    assertThat(parking.addCar(3)).isFalse(); // small (hết chỗ)
    assertThat(parking.addCar(1)).isFalse(); // big (hết chỗ)
  }

  @Test
  @DisplayName("Advanced parking with spot tracking")
  void testAdvancedParking() {
    ParkingSystemAdvanced parking = new ParkingSystemAdvanced(2, 1, 1);

    assertThat(parking.parkCar(101, 1)).isEqualTo(0); // Car 101 đậu big spot 0
    assertThat(parking.parkCar(102, 1)).isEqualTo(1); // Car 102 đậu big spot 1
    assertThat(parking.parkCar(103, 1)).isEqualTo(-1); // Hết big

    assertThat(parking.leaveCar(101)).isTrue(); // Car 101 rời
    assertThat(parking.parkCar(104, 1)).isEqualTo(0); // Spot 0 available lại
  }
}
