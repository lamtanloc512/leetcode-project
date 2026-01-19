package com.practice.leetcode.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ BINARY SEARCH ON ANSWER TECHNIQUE ║
 * ║ (Tìm kiếm nhị phân trên đáp án) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * ĐÂY LÀ KỸ THUẬT CỰC KỲ QUAN TRỌNG TRONG PHỎNG VẤN!
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ KHI NÀO DÙNG? │
 * │ │
 * │ Dấu hiệu nhận biết: │
 * │ ✓ Bài yêu cầu tìm MIN/MAX của một giá trị thỏa điều kiện │
 * │ ✓ KHÔNG CÓ công thức trực tiếp để tính │
 * │ ✓ Có thể KIỂM TRA được một giá trị có thỏa mãn hay không │
 * │ ✓ Đáp án có tính MONOTONIC (nếu X thỏa thì X+1 cũng thỏa, hoặc ngược lại) │
 * │ │
 * │ Từ khóa thường gặp: │
 * │ - "minimum time to..." │
 * │ - "maximum capacity..." │
 * │ - "smallest/largest ... such that..." │
 * │ - "at least / at most" │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * TEMPLATE CHUNG
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * CÔNG THỨC:
 *
 * 1. Xác định SEARCH SPACE: [min_possible, max_possible]
 * 2. Viết hàm CHECK(x): x có thỏa điều kiện không?
 * 3. Binary Search tìm MIN/MAX thỏa mãn
 *
 * int left = MIN_POSSIBLE;
 * int right = MAX_POSSIBLE;
 *
 * while (left < right) {
 * int mid = left + (right - left) / 2;
 *
 * if (check(mid)) {
 * // mid thỏa mãn
 * // → Tìm MIN: thu hẹp sang trái (right = mid)
 * // → Tìm MAX: thu hẹp sang phải (left = mid + 1, nhưng cần adjust)
 * } else {
 * // mid không thỏa
 * // → Tìm MIN: mở rộng sang phải (left = mid + 1)
 * // → Tìm MAX: thu hẹp sang trái (right = mid)
 * }
 * }
 *
 * return left; // (hoặc right, tùy bài)
 */
public class BinarySearchOnAnswerGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // VÍ DỤ 1: KOKO EATING BANANAS (Tìm tốc độ ăn tối thiểu)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán:
   * - Có n đống chuối, pile[i] quả
   * - Koko ăn k quả/giờ, mỗi giờ chỉ ăn 1 đống
   * - Có h giờ để ăn hết
   * - Tìm k TỐI THIỂU để ăn hết trong h giờ
   *
   * Phân tích:
   * - Search space: k ∈ [1, max(pile)]
   * - Check(k): Với tốc độ k, có ăn hết trong h giờ không?
   * - Monotonic: k càng lớn → càng dễ ăn hết (thỏa mãn)
   *
   * Ví dụ: piles = [3,6,7,11], h = 8
   *
   * k=1: 3+6+7+11 = 27 giờ > 8 ✗
   * k=4: 1+2+2+3 = 8 giờ = 8 ✓
   * k=3: 1+2+3+4 = 10 giờ > 8 ✗
   *
   * → k_min = 4
   *
   * PROS:
   * - Giảm từ O(n * max) xuống O(n * log(max))
   * - Dễ implement một khi nhận ra pattern
   *
   * CONS:
   * - Khó nhận ra khi nào dùng
   * - Cần xác định đúng search space
   */
  public int minEatingSpeed(int[] piles, int h) {
    int left = 1;
    int right = Arrays.stream(piles).max().getAsInt();

    while (left < right) {
      int mid = left + (right - left) / 2;

      if (canFinish(piles, mid, h)) {
        right = mid; // mid OK, thử nhỏ hơn
      } else {
        left = mid + 1; // mid không OK, cần lớn hơn
      }
    }

    return left;
  }

  private boolean canFinish(int[] piles, int speed, int hours) {
    int totalHours = 0;
    for (int pile : piles) {
      totalHours += (pile + speed - 1) / speed; // ceil(pile / speed)
    }
    return totalHours <= hours;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // VÍ DỤ 2: CAPACITY TO SHIP PACKAGES (Sức chứa tàu tối thiểu)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán:
   * - Có n gói hàng, weights[i] là trọng lượng
   * - Phải ship THEO THỨ TỰ trong d ngày
   * - Tìm sức chứa tàu TỐI THIỂU
   *
   * Phân tích:
   * - Search space: [max(weights), sum(weights)]
   * - Min: phải chở được gói nặng nhất
   * - Max: chở hết 1 ngày
   * - Check(capacity): Với capacity này, ship hết trong d ngày không?
   *
   * STEP-BY-STEP:
   * weights = [1,2,3,4,5,6,7,8,9,10], d = 5
   *
   * Min capacity = 10 (gói nặng nhất)
   * Max capacity = 55 (tổng)
   *
   * Check(15):
   * - Day 1: 1+2+3+4+5 = 15 ✓
   * - Day 2: 6+7 = 13 ✓ (6+7+8=21 > 15)
   * - Day 3: 8 ✓
   * - Day 4: 9 ✓
   * - Day 5: 10 ✓
   * → 5 ngày = d ✓
   *
   * PROS:
   * - Áp dụng được cho nhiều bài capacity/resource allocation
   *
   * CONS:
   * - Check function có thể phức tạp
   */
  public int shipWithinDays(int[] weights, int days) {
    int left = Arrays.stream(weights).max().getAsInt();
    int right = Arrays.stream(weights).sum();

    while (left < right) {
      int mid = left + (right - left) / 2;

      if (canShip(weights, mid, days)) {
        right = mid;
      } else {
        left = mid + 1;
      }
    }

    return left;
  }

  private boolean canShip(int[] weights, int capacity, int days) {
    int daysNeeded = 1;
    int currentLoad = 0;

    for (int w : weights) {
      if (currentLoad + w > capacity) {
        daysNeeded++;
        currentLoad = 0;
      }
      currentLoad += w;
    }

    return daysNeeded <= days;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // VÍ DỤ 3: MINIMIZE MAX DISTANCE TO GAS STATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * Bài toán:
   * - Có n trạm xăng tại các vị trí cho trước
   * - Được thêm k trạm mới
   * - Tìm khoảng cách LỚN NHẤT giữa 2 trạm liền kề TỐI THIỂU
   *
   * Đây là bài MINIMIZE MAXIMUM → Binary Search on Answer!
   *
   * Phân tích:
   * - Search space: [0, max_gap] (float!)
   * - Check(d): Với max distance = d, cần bao nhiêu trạm mới?
   * Nếu gap giữa 2 trạm = g, cần ceil(g/d) - 1 trạm mới
   * - Monotonic: d càng lớn → cần ít trạm hơn
   *
   * TEMPLATE CHO FLOAT BINARY SEARCH:
   * - Thay while (left < right) bằng for (100 iterations)
   * - Hoặc while (right - left > epsilon)
   */
  public double minmaxGasDist(int[] stations, int k) {
    double left = 0;
    double right = 0;

    for (int i = 1; i < stations.length; i++) {
      right = Math.max(right, stations[i] - stations[i - 1]);
    }

    // Binary search với precision
    for (int iter = 0; iter < 100; iter++) {
      double mid = (left + right) / 2;

      if (canAchieve(stations, k, mid)) {
        right = mid; // mid OK, thử nhỏ hơn
      } else {
        left = mid; // mid không OK
      }
    }

    return left;
  }

  private boolean canAchieve(int[] stations, int k, double maxDist) {
    int stationsNeeded = 0;
    for (int i = 1; i < stations.length; i++) {
      double gap = stations[i] - stations[i - 1];
      stationsNeeded += (int) (gap / maxDist);
    }
    return stationsNeeded <= k;
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // BẢNG TỔNG HỢP
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌──────────────────────────────────────────────────────────────────────────┐
   * │ Loại bài │ Search Space │ Check Function │
   * ├──────────────────────────────────────────────────────────────────────────┤
   * │ Min speed to finish │ [1, max_item] │ canFinish(speed) │
   * │ Min capacity to ship │ [max_w, sum_w] │ canShip(cap) │
   * │ Min time for all │ [0, worst_time] │ canComplete(time) │
   * │ Max min distance │ [0, max_gap] │ canAchieve(dist) │
   * │ Split array min max sum │ [max, sum] │ canSplit(maxSum) │
   * │ Median of two arrays │ [0, len] │ partitionValid() │
   * └──────────────────────────────────────────────────────────────────────────┘
   *
   *
   * CHECKLIST GIẢI BÀI
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ 1. Bài hỏi "tìm MIN/MAX của X thỏa điều kiện Y" không? │
   * │ │
   * │ 2. X có tính MONOTONIC không? │
   * │ - Nếu X=k thỏa → X=k+1 cũng thỏa (hoặc ngược lại) │
   * │ │
   * │ 3. Có thể viết CHECK(x) trong O(n) hoặc O(n log n) không? │
   * │ │
   * │ 4. Search space: [MIN_POSSIBLE, MAX_POSSIBLE] là gì? │
   * │ - Nghĩ về edge cases: min khi không có constraint, max khi full │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   *
   * CÁC BÀI THỰC HÀNH
   *
   * EASY/MEDIUM:
   * - Koko Eating Bananas (875)
   * - Capacity To Ship Packages (1011)
   * - Split Array Largest Sum (410)
   * - Magnetic Force Between Balls (1552)
   *
   * HARD:
   * - Minimize Max Distance to Gas Station (774)
   * - Find Median from Data Stream với Binary Search
   */

  // ========== TESTS ==========
  @Test
  @DisplayName("Koko eating bananas")
  void testKoko() {
    assertThat(minEatingSpeed(new int[] { 3, 6, 7, 11 }, 8)).isEqualTo(4);
    assertThat(minEatingSpeed(new int[] { 30, 11, 23, 4, 20 }, 5)).isEqualTo(30);
  }

  @Test
  @DisplayName("Ship packages")
  void testShip() {
    assertThat(shipWithinDays(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, 5)).isEqualTo(15);
  }
}
