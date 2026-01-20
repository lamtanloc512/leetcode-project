package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                  DESIGN YOUTUBE / TIKTOK / NETFLIX                        ║
 * ║               (Hệ thống Video Streaming & Sharing)                        ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. YÊU CẦU (REQUIREMENTS)
 * ═══════════════════════════════════════════════════════════════════════════
 * Functional Requirements:
 * - Upload video: User upload video (có thể file lớn).
 * - View video: User xem video mượt mà, không giật lag (buffering).
 * - Change quality: Hỗ trợ nhiều độ phân giải (360p, 720p, 1080p...).
 * - Search: Tìm kiếm video theo title.
 * - Interaction: Like, Comment (Optional).
 *
 * Non-Functional Requirements:
 * - High Availability: Xem video luôn available.
 * - Low Latency: Thời gian start video thấp (Time to first byte).
 * - Scalability: Hỗ trợ lượng user khổng lồ xem cùng lúc (Viral video).
 * - Reliability: Video upload không bị mất.
 *
 * 2. ƯỚC LƯỢNG (BACK-OF-THE-ENVELOPE ESTIMATION)
 * ═══════════════════════════════════════════════════════════════════════════
 * Assumptions:
 * - 500 triệu Daily Active Users (DAU).
 * - 1 user xem 5 videos/ngày.
 * - 10% user upload 1 video/ngày.
 * - Video trung bình 100MB (sau khi nén).
 *
 * Tính toán traffic:
 * - View QPS:
 *   + 500M * 5 views = 2.5 Tỷ views/ngày.
 *   + 2.5 Tỷ / 86400s ≈ 30,000 QPS. Peak ≈ 60,000 QPS.
 * - Upload QPS:
 *   + 500M * 10% = 50M uploads/ngày.
 *   + 50M / 86400s ≈ 600 videos/second uploads.
 *
 * Tính toán Storage (Khổng lồ!):
 * - 50M videos * 100MB = 5000M MB = 5 PB (Petabytes)/ngày.
 * - 1 năm = 5 PB * 365 = 1825 PB = 1.8 EB (Exabytes).
 * - Cần strategy để optimize storage (xóa video rác, cold storage...).
 *
 * Tính toán Bandwidth:
 * - Nếu mỗi view tốn 5Mbps.
 * - Total bandwidth = 60,000 concurrent viewers * 5Mbps = 300 Gbps (Peak có thể tới Tbps).
 * => Bắt buộc phải dùng CDN (Content Delivery Network).
 *
 * 3. HIGH-LEVEL ARCHITECTURE
 * ═══════════════════════════════════════════════════════════════════════════
 * Hệ thống chia làm 3 flows chính:
 * 1. Upload Flow
 * 2. Processing Flow (Transcoding)
 * 3. Streaming Flow
 *
 * ┌──────────┐      1. Upload      ┌──────────────┐      2. Save Raw    ┌──────────────┐
 * │  Client  │ ──────────────────> │ Web Server   │ ──────────────────> │ Blob Storage │
 * │ (Creator)│                     │ (Upload Svc) │                     │ (S3) Original│
 * └──────────┘                     └──────┬───────┘                     └──────┬───────┘
 *                                         │                                    │
 *                                         │ 3. Notify                          │
 *                                         ▼                                    │
 *                                  ┌──────────────┐                            │
 *                                  │ Message Queue│ (Kafka)                    │
 *                                  └──────┬───────┘                            │
 *                                         │                                    │
 *                                         ▼                                    │
 *                                  ┌──────────────┐      4. Read Raw           │
 *                                  │ Transcoding  │ <──────────────────────────┘
 *                                  │   Servers    │
 *                                  └──────┬───────┘
 *                                         │ 5. Save Transcoded (mp4, hls segments)
 *                                         ▼
 *                                  ┌──────────────┐
 *                                  │ Blob Storage │ (S3) Processed
 *                                  └──────┬───────┘
 *                                         │ 6. Push to Edge
 *                                         ▼
 *     ┌──────────┐     7. Stream   ┌──────────────┐
 *     │  Client  │ <────────────── │     CDN      │ (Cloudfront/Akamai)
 *     │ (Viewer) │                 └──────────────┘
 *     └──────────┘
 *
 * 4. DETAILED COMPONENT DESIGN
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A. VIDEO UPLOAD & STORAGE
 * - Dùng Pre-signed URL (Amazon S3):
 *   + Client gọi Web Server -> Server trả về URL upload trực tiếp lên S3.
 *   + Giảm tải cho Web Server, tận dụng bandwidth của S3.
 * - Blob Storage Structure:
 *   /videos/original/{video_id}.mp4
 *   /videos/transcoded/{video_id}/360p.mp4
 *   /videos/transcoded/{video_id}/720p.mp4
 *
 * B. VIDEO TRANSCODING (Encode)
 * - Tại sao cần transcode?
 *   1. Đổi format (avi, mov -> mp4, webm) support mọi trình duyệt.
 *   2. Đổi resolution (1080p -> 720p, 480p) tùy mạng user.
 *   3. Chunking (chia nhỏ video) cho streaming protocols.
 * - Protocols:
 *   + HLS (HTTP Live Streaming - Apple): Phổ biến nhất. Video chia thành các file .ts nhỏ (vài giây) + file manifest .m3u8.
 *   + DASH (Dynamic Adaptive Streaming over HTTP): Tương tự HLS, open standard.
 * - Quy trình Transcoding:
 *   + Dùng DAG (Directed Acyclic Graph) model vì quy trình phức tạp:
 *     [Extract Audio] -> [Encode Audio] ─┐
 *     [Extract Video] -> [Resize 360p] -> [Encode 360p] -> [Merge] -> Upload S3
 *                     -> [Resize 720p] -> [Encode 720p] -> [Merge] -> Upload S3
 *
 * C. ADAPTIVE BITRATE STREAMING
 * - Kỹ thuật tự động đổi chất lượng video dựa trên tốc độ mạng user.
 * - Client download file manifest (.m3u8), thấy các quality options.
 * - Client tự quyết định download chunk của quality nào tiếp theo.
 * - Ví dụ: Mạng mạnh -> tải chunk 1080p. Mạng yếu đi -> chunk tiếp theo tải 360p.
 *
 * D. CONTENT DELIVERY NETWORK (CDN)
 * - Video static files (.ts, .m3u8) được cache tại CDN Edge Servers khắp thế giới.
 * - User ở Vietnam sẽ lấy video từ Edge Server tại Singapore hoặc VN, thay vì server gốc ở US.
 * - Tiết kiệm bandwidth cho Origin Server và giảm latency cực lớn.
 *
 * 5. DATABASE & METADATA
 * ═══════════════════════════════════════════════════════════════════════════
 * Cần 2 loại Database:
 *
 * 1. Relational DB (MySQL/Postgres) Sharded:
 * - Lưu Users, Video Metadata (Title, Description, Size, UploaderID).
 * - Sharding key: VideoID (để query thông tin video nhanh).
 * - Với traffic lớn, cần Master-Slave replication.
 *
 * 2. NoSQL (Cassandra/HBase):
 * - Lưu Watch History, Likes, Comments.
 * - Vì write throughput cực lớn và không cần strong consistency (Eventual consistency OK).
 *
 * 6. OPTIMIZATIONS & ERROR HANDLING
 * ═══════════════════════════════════════════════════════════════════════════
 * - Deduplication:
 *   + Check hash (MD5/SHA) của file gốc khi upload để tránh lưu trùng video.
 * - Thumbnails:
 *   + Generate ảnh thumbnail khi transcoding. Dùng Sprite sheet để tối ưu khi hover trên thanh timeline.
 * - Safety:
 *   + Copyright check, NSFW detection chạy song song trong quá trình transcoding.
 * - Cost Saving:
 *   + Video hot -> CDN.
 *   + Video ít người xem (Long-tail) -> Chỉ để ở S3 (Origin), không push ra CDN edge.
 * */
public class DesignYouTube {
    // Class placeholder for structure
}
