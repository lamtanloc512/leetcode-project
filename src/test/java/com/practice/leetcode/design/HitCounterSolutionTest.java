package com.practice.leetcode.design;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DESIGN HIT COUNTER - Đếm số lượt truy cập trong 5 phút gần nhất
 * 
 * Bài toán: Thiết kế bộ đếm hit với:
 * - hit(timestamp): Ghi nhận 1 hit tại timestamp
 * - getHits(timestamp): Đếm số hits trong 300 giây gần nhất
 * 
 * Đây là bài kinh điển về sliding window + data structure!
 */
class HitCounterSolutionTest {

  /**
   * THUẬT TOÁN 1: Queue
   * 
   * Ý tưởng đơn giản:
   * - Queue lưu tất cả timestamps
   * - Khi getHits, loại bỏ các timestamp cũ hơn 300 giây
   * 
   * Nhược điểm: Tốn memory khi rate cao
   * 
   * Time: O(1) amortized cho hit, O(n) worst case cho getHits
   * Space: O(n)
   */
  static class HitCounterQueue {
    private Queue<Integer> hits;

    public HitCounterQueue() {
      hits = new LinkedList<>();
    }

    public void hit(int timestamp) {
      hits.offer(timestamp);
    }

    public int getHits(int timestamp) {
      // Loại bỏ hits cũ hơn 300 giây
      while (!hits.isEmpty() && hits.peek() <= timestamp - 300) {
        hits.poll();
      }
      return hits.size();
    }
  }

  /**
   * THUẬT TOÁN 2: Fixed Array (Bucket)
   * 
   * Ý tưởng: Dùng 2 array cố định size 300:
   * - times[i]: timestamp cuối cùng tại bucket i
   * - hits[i]: số hits tại bucket i
   * 
   * Bucket index = timestamp % 300
   * 
   * Ưu điểm: O(1) space, O(300) = O(1) cho getHits
   * 
   * Ví dụ: timestamp = 301
   * bucket = 301 % 300 = 1
   * 
   * Nếu times[1] != 301 → Bucket cũ, reset hits[1] = 1
   * Nếu times[1] == 301 → Cùng giây, hits[1]++
   * 
   * Time: O(1) cho hit, O(300) = O(1) cho getHits
   * Space: O(300) = O(1)
   */
  static class HitCounterBucket {
    private int[] times;
    private int[] hits;

    public HitCounterBucket() {
      times = new int[300];
      hits = new int[300];
    }

    public void hit(int timestamp) {
      int bucket = timestamp % 300;

      if (times[bucket] != timestamp) {
        // Bucket mới, reset
        times[bucket] = timestamp;
        hits[bucket] = 1;
      } else {
        // Cùng giây, tăng đếm
        hits[bucket]++;
      }
    }

    public int getHits(int timestamp) {
      int count = 0;

      for (int i = 0; i < 300; i++) {
        // Chỉ đếm bucket trong 300 giây gần nhất
        if (timestamp - times[i] < 300) {
          count += hits[i];
        }
      }

      return count;
    }
  }

  // ========== TESTS ==========

  @Test
  @DisplayName("Hit counter with queue")
  void testQueueCounter() {
    HitCounterQueue counter = new HitCounterQueue();
    counter.hit(1);
    counter.hit(2);
    counter.hit(3);
    assertThat(counter.getHits(4)).isEqualTo(3);

    counter.hit(300);
    assertThat(counter.getHits(300)).isEqualTo(4);
    assertThat(counter.getHits(301)).isEqualTo(3); // hit(1) bị loại
  }

  @Test
  @DisplayName("Hit counter with bucket")
  void testBucketCounter() {
    HitCounterBucket counter = new HitCounterBucket();
    counter.hit(1);
    counter.hit(2);
    counter.hit(3);
    assertThat(counter.getHits(4)).isEqualTo(3);

    counter.hit(300);
    assertThat(counter.getHits(300)).isEqualTo(4);
    assertThat(counter.getHits(301)).isEqualTo(3);
  }
}
