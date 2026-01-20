package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                      DESIGN TINYURL / BIT.LY                              ║
 * ║                   (Hệ thống Rút gọn Liên kết)                             ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. YÊU CẦU (REQUIREMENTS)
 * ═══════════════════════════════════════════════════════════════════════════
 * Functional Requirements:
 * - Rút gọn URL: Input long URL (https://www.google.com/search?q=design) -> Output: https://tiny.url/AbCd12
 * - Redirection: Truy cập https://tiny.url/AbCd12 -> Redirect tới long URL gốc.
 * - Custom URL: User có thể chọn custom alias (optional).
 * - Expiration: URL có thể hết hạn sau 1 khoảng thời gian (optional).
 *
 * Non-Functional Requirements:
 * - High Availability: Hệ thống redirection cần cực kỳ ổn định (100% uptime nếu có thể).
 * - Latency thấp: Redirection phải nhanh (< 200ms).
 * - URL không đoán được (non-predictable) để tránh security issues.
 *
 * 2. ƯỚC LƯỢNG (BACK-OF-THE-ENVELOPE ESTIMATION)
 * ═══════════════════════════════════════════════════════════════════════════
 * Assumptions:
 * - Write: 100 triệu URL mới/tháng.
 * - Read/Write ratio: 100:1 (Nhiều người click link hơn là tạo link).
 *
 * Tính toán:
 * - Write QPS: 100M / (30 days * 24h * 3600s) ≈ 40 QPS.
 * - Read QPS: 40 * 100 = 4,000 QPS.
 * - Storage (5 năm):
 *   + 100M records/tháng * 12 tháng * 5 năm = 6 Tỷ records.
 *   + Mỗi record 500 bytes -> 6 Tỷ * 500 bytes = 3 TB.
 *   => Database nào cũng chứa được 3TB (khá nhỏ).
 * 
 *
 * 3. API DESIGN
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * POST /api/v1/urls
 * - Request: { "long_url": "https://...", "custom_alias": "my-link", "expire_date": "2025-..." }
 * - Response: { "short_url": "https://tiny.url/AbCd12" }
 *
 * GET /{short_url_key}
 * - Response: 301 Redirect (Permanent) hoặc 302 Redirect (Temporary) tới Long URL.
 *   + 301: Browser cache mapping -> Server giảm load nhưng không track được analytics.
 *   + 302: Luôn hit server -> Track được click count, location... (Thường dùng cho Marketing).
 *
 * 4. DATABASE SCHEMA
 * ═══════════════════════════════════════════════════════════════════════════
 * Bảng: urls
 * ┌───────────────┬───────────────────┬──────────────────────────────────┐
 * │ Column        │ Type              │ Note                             │
 * ├───────────────┼───────────────────┼──────────────────────────────────┤
 * │ id            │ BIGINT PK         │ Auto-increment / Snowflake ID    │
 * │ short_key     │ VARCHAR(7)        │ Unique Index (The shortened part)│
 * │ long_url      │ VARCHAR(2048)     │ URL gốc                          │
 * │ user_id       │ BIGINT            │ Người tạo (FK)                   │
 * │ created_at    │ TIMESTAMP         │                                  │
 * │ expires_at    │ TIMESTAMP         │                                  │
 * └───────────────┴───────────────────┴──────────────────────────────────┘
 *
 * 5. HIGH-LEVEL DESIGN & ALGORITHMS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * CÁCH 1: HASHING (MD5/SHA256)
 * - Hash(long_url) -> MD5 string (32 chars).
 * - Lấy 6-7 ký tự đầu.
 * - Vấn đề: Collision (trùng lặp). Nếu trùng hash của URL khác -> Phải xử lý (append salt, re-hash).
 *
 * CÁCH 2: BASE62 ENCODING (Recommended)
 * - Sử dụng Unique ID (Auto-increment hoặc Snowflake ID) chuyển sang hệ cơ số 62 (a-z, A-Z, 0-9).
 * - Ví dụ: ID = 100,000,000 -> Base62 = "6LAZe"
 * - Với 7 ký tự Base62: 62^7 ≈ 3.5 nghìn tỷ URLs (Đủ cho > 100 năm).
 *
 * FLOW TẠO URL NGẮN (Encoding):
 * 1. User gửi Long URL.
 * 2. Service check trong DB xem Long URL đã tồn tại chưa (optional, để dedup).
 * 3. Sinh Unique ID (Dùng database auto-increment hoặc distributed ID generator như Snowflake).
 *    - Distributed ID Gen tốt hơn vì scale được nhiều server DB.
 * 4. Convert Unique ID -> Base62 String (Short Key).
 * 5. Lưu (ID, Short Key, Long URL) vào DB.
 * 6. Trả về Short URL.
 *
 * ┌──────────┐      1. Create     ┌─────────────┐      2. Get ID      ┌──────────────┐
 * │  Client  │ ─────────────────> │ Web Server  │ ──────────────────> │ ID Generator │
 * └──────────┘                    └──────┬──────┘                     └──────┬───────┘
 *                                        │                                   │
 *                                        │ 3. ID=12345                       │
 *                                        │                                   │
 *                                        ▼                                   │
 *                                 ┌─────────────┐                            │
 *                                 │ ID -> Base62│                            │
 *                                 │ Key = abc12 │                            │
 *                                 └──────┬──────┘                            │
 *                                        │                                   │
 *                                        │ 4. Save DB                        │
 *                                        ▼                                   │
 *                                 ┌─────────────┐     Write              ┌───┴────┐
 *                                 │  Database   │ <───────────────────── │ Cache  │ (Redis)
 *                                 └─────────────┘                        └────────┘
 *
 * FLOW REDIRECT (Decoding):
 * 1. User access https://tiny.url/abc12.
 * 2. LB forward tới Web Server.
 * 3. Server check Redis Cache với key="abc12".
 *    - Hit: Redirect ngay lập tức (302).
 *    - Miss: Query DB tìm long_url where short_key = "abc12".
 *      + Có: Write vào Cache, Redirect.
 *      + Không: Trả về 404.
 *
 * 6. DEEP DIVE & SCALING
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * DATABASE SCALING:
 * - Vì data chỉ 3TB trong 5 năm, 1 instance Master-Slave là đủ.
 * - Nếu cần scale write cực lớn (tỷ requests/ngày): Sharding database.
 *   + Shard theo Hash(short_key) -> query redirect nhanh.
 *
 * CACHING:
 * - Rất quan trọng vì Read operation chiếm đa số.
 * - Cache policy: LRU (Link mới tạo hoặc hot link sẽ được giữ lại).
 * - Cache 20% traffic -> Giảm load DB đáng kể.
 *
 * ID GENERATOR KHI NHIỀU APP SERVER:
 * - Để tránh trùng ID khi tạo đồng thời:
 *   + Cách 1: Database Ticket Server (Flickr idea) - 1 bảng chuyên sinh ID.
 *   + Cách 2: Twitter Snowflake (Time + MachineID + Sequence).
 *   + Cách 3: Pre-generated Keys (Key Generation Service - KGS).
 *     - Một service chạy nền sinh sẵn random keys, lưu vào 2 bảng:
 *       1. Unused_Keys
 *       2. Used_Keys
 *     - Khi request tới, lấy 1 key từ Unused chuyển sang Used.
 *     - Ưu điểm: Không cần encode/decode, không bao giờ collision.
 *     - Nhược điểm: Phức tạp hơn để maintain KGS.
 *
 * CLEANUP (EXPIRED URLs):
 * - Lazy expiry: Khi user access link, check expires_at. Nếu hết hạn -> Xóa và báo lỗi.
 * - Cron job: Chạy định kỳ (ví dụ mỗi đêm) quét các expired URL để xóa khỏi DB giải phóng storage.
 *
 * 7. FAQ: ID GENERATOR & REDIS RELATIONSHIP?
 * ═══════════════════════════════════════════════════════════════════════════
 * Q: ID Generator và Redis Cache có liên quan gì nhau trong sơ đồ trên?
 * A: Có 2 mối quan hệ chính:
 *
 * 1. REDIS LÀM ID GENERATOR (Implementation):
 *    - ID Generator thường được implement bằng chính Redis.
 *    - Sử dụng lệnh `INCR global_counter` của Redis để sinh số tự tăng (1, 2, 3...) cực nhanh và thread-safe (atomic).
 *    - Vì vậy, component "ID Generator" trong hình vẽ thực tế có thể chính là 1 cụm Redis riêng biệt (hoặc dùng chung cụm Redis Cache).
 *
 * 2. DATA FLOW (Logic):
 * 3. SUMMARY: infrastructure CẦN GÌ? (User Question)
 *    - YES, bạn cần tối thiểu:
 *      + 1 MySQL (hoặc Postgres): Để lưu trữ vĩnh viễn (Persistent Storage). Nếu Redis mất điện, data vẫn còn ở đây.
 *      + 1 Redis: Đóng 2 vai trò.
 *        1. Làm "Bộ đếm" (Counter) để sinh ID (Role: ID Generator).
 *        2. Làm "Bộ nhớ đệm" (Cache) để đọc nhanh (Role: Cache).
 *
 *    - FLOW CHI TIẾT KHI CREATE (Write Path):
 *      1. App -> Redis (Counter): "Cho tao xin 1 số mới" (INCR). Redis trả về: 100.
 *      2. App (Logic code): Convert 100 -> "abc".
 *      3. App -> MySQL: INSERT INTO urls (id, short_key, long_url) VALUES (100, "abc", "google.com"). (Để lưu vĩnh viễn).
 *      4. App -> Redis (Cache): SET "abc" "google.com". (Để lát nữa đọc cho lẹ).
 */
public class DesignTinyURL {

    /**
     * Mô phỏng thuật toán Base62 Encoding.
     */
    public static class Base62 {
        private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final int BASE = ALPHABET.length();

        public static String encode(long id) {
            StringBuilder sb = new StringBuilder();
            while (id > 0) {
                sb.append(ALPHABET.charAt((int) (id % BASE)));
                id /= BASE;
            }
            return sb.reverse().toString();
        }

        public static long decode(String str) {
            long id = 0;
            for (int i = 0; i < str.length(); i++) {
                id = id * BASE + ALPHABET.indexOf(str.charAt(i));
            }
            return id;
        }
    }

    public static void main(String[] args) {
        long id = 123456789L;
        String shortKey = Base62.encode(id);
        System.out.println("ID: " + id);
        System.out.println("Short Key: " + shortKey);
        System.out.println("Decoded ID: " + Base62.decode(shortKey));
    }
}
