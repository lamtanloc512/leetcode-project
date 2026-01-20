package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                      DESIGN TIKTOK / REELS FEED SYSTEM                    ║
 * ║                (Hệ thống Recommendation & Infinite Scroll)                ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * NOTE: Phần upload & streaming giống YouTube. File này tập trung vào FEED và RECOMMENDATION.
 *
 * 1. YÊU CẦU
 * ═══════════════════════════════════════════════════════════════════════════
 * - "For You" Feed: Gợi ý videos cá nhân hóa cực cao.
 * - Zero Latency Swipe: Vuốt là chạy ngay (Pre-loading).
 * - Viral effect: Video mới có cơ hội viral nhanh chóng.
 *
 * 2. CORE: RECOMMENDATION ENGINE
 * ═══════════════════════════════════════════════════════════════════════════
 * Input Signals:
 * - User Profile: Age, Gender, Location.
 * - Behavior: Watch time (quan trọng nhất - xem hết video = thích), Like, Share, Comment, Re-watch.
 * - Video Features: Verify audio, hashtags, content analysis (Computer Vision).
 *
 * Funnel (Phễu lọc):
 * Total Videos (Billions)
 *    │
 *    │ 1. Retrieval (Recall) - Candidate Generation
 *    ▼
 *  Candidates (~1000 videos) -> Lấy từ nhiều nguồn:
 *    - Collaborative Filtering ("Những người giống bạn đã xem...").
 *    - Content-based ("Bạn thích chó -> video chó").
 *    - Social Graph (Followings).
 *    - Trending/Popular.
 *    │
 *    │ 2. Ranking (Lightweight Model)
 *    ▼
 *  Top ~500 -> Chạy model đơn giản (Logistic Regression) để lọc sơ.
 *    │
 *    │ 3. Re-Ranking (Deep Learning Model)
 *    ▼
 *  Top ~10 -> Chạy model phức tạp (Neural Network) để predict tỷ lệ user sẽ xem hết video.
 *  Sắp xếp theo Score = w1*WatchTime + w2*Like + w3*Share...
 *
 * 3. PRE-LOADING ARCHITECTURE (ZERO LATENCY)
 * ═══════════════════════════════════════════════════════════════════════════
 * TikTok mượt vì video tiếp theo ĐÃ được tải sẵn.
 *
 * 1. Client request Feed -> Server trả về list 10 Video URLs.
 * 2. Client đang play Video 1.
 * 3. Client ngầm download (Pre-fetch) Video 2, 3, 4 vào Cache.
 * 4. User swipe -> Video 2 play ngay từ Cache (Local).
 *
 * 4. SYSTEM ARCHITECTURE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────┐     Request Feed     ┌──────────────┐
 * │  Client  │ ───────────────────> │ API Gateway  │
 * └──────────┘                      └──────┬───────┘
 *      ▲                                   │
 *      │ (Pre-fetch Video)                 ▼
 *      │                            ┌──────────────┐
 *    ┌────┐                         │ Rec Sys      │ (Recommendation Service)
 *    │CDN │                         └──────┬───────┘
 *    └────┘                                │
 *                                   ┌──────┴───────┐
 *                                   │              │
 *                            ┌──────▼─────┐  ┌─────▼──────┐
 *                            │ User Model │  │ Video Index│
 *                            └────────────┘  └────────────┘
 *
 * 5. COLD START PROBLEM (Video mới & User mới)
 * ═══════════════════════════════════════════════════════════════════════════
 * Video mới upload làm sao để viral?
 * - Test Batch (Shark Tank mechanism):
 *   + Video mới -> Push cho 100 random users.
 *   + Đo engagement (watch time, like).
 *   + Nếu tốt -> Push cho 1000 users.
 *   + Nếu vẫn tốt -> Push 10k -> 1M -> Viral.
 *   + Nếu tệ -> Stop sharing.
 *
 * -> Cơ hội công bằng cho mọi creator.
 * */
public class DesignTikTok {
}
