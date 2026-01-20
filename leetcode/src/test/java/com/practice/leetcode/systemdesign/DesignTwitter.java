package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                      DESIGN TWITTER / FACEBOOK NEWS FEED                  ║
 * ║                         (Hệ thống Bảng tin MXH)                           ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. YÊU CẦU
 * ═══════════════════════════════════════════════════════════════════════════
 * - Post Tweet: User đăng bài (text, image).
 * - TimeLine/NewsFeed: User thấy bài viết từ những người mình follow, sắp xếp theo thời gian (hoặc ranking).
 * - Follow: User A follow User B.
 *
 * 2. CHALLENGE: FAN-OUT (Phát tán dữ liệu)
 * ═══════════════════════════════════════════════════════════════════════════
 * User A user post bài. A có 1 triệu followers. Làm sao 1 triệu người đó thấy bài của A?
 *
 * OPTION 1: PULL MODEL (Fan-out on Read)
 * - Khi User B load feed:
 *   1. Query danh sách người B follow -> [A, C, D...]
 *   2. Query tweets của A, C, D...
 *   3. Merge & Sort in memory.
 * - Pros: Write cực nhanh (User A post -> chỉ save DB). Tiết kiệm storage.
 * - Cons: Read cực chậm khi follow nhiều người.
 *
 * OPTION 2: PUSH MODEL (Fan-out on Write) - Used by Twitter for celebrities usually handled differently
 * - Mỗi User có 1 danh sách "Pre-computed Feed" (Timeline Cache) trong Redis.
 * - Khi User A post bài:
 *   1. Save Tweet to DB.
 *   2. Tìm tất cả followers của A.
 *   3. Insert Tweet ID vào Timeline Cache của từng follower.
 * - Pros: Read cực nhanh (Load feed = Get from Redis List).
 * - Cons: Write chậm nếu A có quá nhiều followers (Lady Gaga problem).
 *
 * OPTION 3: HYBRID (Chiến lược tốt nhất)
 * - Với user thường (ít followers): Dùng PUSH model.
 * - Với Celebrities (triệu followers): Dùng PULL model.
 * - Khi User B load feed:
 *   Feed = (Pull tweets from Celebrities) + (Get Push Feed from Cache).
 *
 * 3. DATABASE SCHEMA
 * ═══════════════════════════════════════════════════════════════════════════
 * Table: Users (id, name...)
 * Table: Tweets (id, user_id, content, created_at)
 * Table: Follows (follower_id, followee_id)
 *
 * Cache (Redis):
 * - Key: Timeline_{user_id}
 * - Value: List<TweetID> [102, 101, 99...]
 *
 * 4. SYSTEM ARCHITECTURE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────┐   Post    ┌─────────────┐Save DB  ┌──────────────┐
 * │ Sender   │ ────────> │ Web Server  │ ──────> │  Tweets DB   │ (SQL/NoSQL)
 * │ (User A) │           └──────┬──────┘         └──────────────┘
 * └──────────┘                  │
 *                               │ Fan-out
 *                               ▼
 *                        ┌─────────────┐ Get Followers ┌──────────────┐
 *                        │ Fanout Svc  │ <───────────> │ Graph DB     │ (Follows)
 *                        └──────┬──────┘               └──────────────┘
 *                               │
 *                               │ Update Cache
 *                               ▼
 *                        ┌─────────────┐
 *                        │ Redis Cache │ (Timelines)
 *                        │ {User_B}:[T1]
 *                        │ {User_C}:[T1]
 *                        └─────────────┘
 *
 * 5. API DESIGN
 * ═══════════════════════════════════════════════════════════════════════════
 * - GET /v1/timeline?cursor=123&limit=20
 *   Dùng Cursor-based pagination thay vì Offset để tránh duplicate/mising items khi feed update liên tục.
 * */
public class DesignTwitter {
    // Class placeholder
}
