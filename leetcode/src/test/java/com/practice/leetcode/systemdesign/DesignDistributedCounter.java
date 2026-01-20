package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                      DESIGN DISTRIBUTED COUNTER                           ║
 * ║                (Bộ đếm phân tán - View Count, Likes)                      ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. YÊU CẦU (REQUIREMENTS)
 * ═══════════════════════════════════════════════════════════════════════════
 * - High Write Throughput: Hàng triệu requests tăng đếm mỗi giây (Viral video views, Ad clicks).
 * - Real-time: Người dùng muốn thấy số liệu cập nhật ngay (hoặc trễ rất ít).
 * - Accuracy: Không được đếm thiếu quá nhiều (Approximate chấp nhận được ở scale cực lớn, nhưng Payment/Ads thì cần Exact).
 *
 * 2. VẤN ĐỀ: WRITE CONTENTION (Tranh chấp ghi)
 * ═══════════════════════════════════════════════════════════════════════════
 * Database thường (MySQL) update:
 *   UPDATE videos SET views = views + 1 WHERE id = 123;
 * -> Database lock row 123.
 * -> Các requests khác phải đợi Lock -> Bottleneck tại row đó.
 * -> Max throughput chi ~500 TPS (Transaction per second).
 *
 * 3. GIẢI PHÁP 1: REDIS (IN-MEMORY)
 * ═══════════════════════════════════════════════════════════════════════════
 * - Redis `INCR key` cực nhanh (~100k OPS).
 * - Vẫn là Single-threaded, nhưng nhanh nên chịu được load cao hơn DB đĩa.
 * - Nhược điểm: Nếu Redis chết chưa kịp persist -> Mất views.
 *
 * 4. GIẢI PHÁP 2: SHARDED COUNTERS (Distributed Counter)
 * ═══════════════════════════════════════════════════════════════════════════
 * Để giải quyết Contention, ta chia 1 counter to thành N counters nhỏ.
 *
 * Ví dụ: Video 123 có 10 sub-counters (slots).
 * Key: video_123_0, video_123_1, ..., video_123_9.
 *
 * WRITE FLOW:
 * - Khi user request +1 view:
 *   + Chọn ngẫu nhiên 1 trong 10 counter con (Random int 0-9).
 *   + Redis: INCR video_123_{random}
 *   + Contention giảm đi 10 lần.
 *
 * READ FLOW:
 * - Khi user muốn xem tổng views:
 *   + Lấy giá trị của cả 10 counters.
 *   + Sum(video_123_0 ... video_123_9) = Total Views.
 *
 * 5. GIẢI PHÁP 3: WRITE-BACK BUFFER (Aggregation Service)
 * ═══════════════════════════════════════════════════════════════════════════
 * Dùng cho scale cực lớn (FB Likes, YouTube Views).
 *
 * ┌──────────┐      1. Click View    ┌──────────────┐
 * │  Client  │ ────────────────────> │  Log / Edge  │
 * └──────────┘                       │  LoadBalancer│
 *                                    └──────┬───────┘
 *                                           │ 2. Aggregation in Memory
 *                                           │ (Buffer 5s)
 *                                           ▼
 *                                    ┌──────────────┐
 *                                    │ Aggregation  │ Counter += 1000
 *                                    │ Service      │
 *                                    └──────┬───────┘
 *                                           │ 3. Flush DB (One big write)
 *                                           ▼
 *                                    ┌──────────────┐
 *                                    │  Database    │ UPDATE views += 1000
 *                                    └──────────────┘
 * -> Giảm load DB hàng nghìn lần.
 * -> Trade-off: Nếu Agg Service crash -> Mất views trong RAM (chấp nhận được với metrics like/view).
 *
 * 6. APPROXIMATE COUNTING (HYPERLOGLOG)
 * ═══════════════════════════════════════════════════════════════════════════
 * - Đếm "Unique Views" (Distinct Count) là bài toán khó hơn đếm Total Views.
 * - Naive: Lưu Set(UserIDs) -> Tốn RAM khủng khiếp.
 * - HyperLogLog: Thuật toán xác suất.
 *   + Chỉ tốn 12KB bộ nhớ để đếm hàng tỷ unique items.
 *   + Sai số rất nhỏ (< 1%).
 *   + Redis có sẵn lệnh `PFADD` và `PFCOUNT`.
 * */
public class DesignDistributedCounter {
}
