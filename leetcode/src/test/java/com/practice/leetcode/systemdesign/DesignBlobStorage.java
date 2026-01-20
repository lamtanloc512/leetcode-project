package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                  DESIGN BLOB STORAGE (GOOGLE DRIVE / S3)                  ║
 * ║                      (Hệ thống Lưu trữ Đối tượng)                         ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. YÊU CẦU (REQUIREMENTS)
 * ═══════════════════════════════════════════════════════════════════════════
 * Functional Requirements:
 * - Upload/Download/Delete files.
 * - Files có thể rất lớn (vài GB tới TB) hoặc rất nhỏ (vài KB).
 * - Organize theo Buckets / Folders (Logic).
 * - Versioning (Optional): Giữ lại các phiên bản cũ của file.
 *
 * Non-Functional Requirements:
 * - Durability (Độ bền): Quan trọng nhất! (11 số 9 - 99.999999999%). Không bao giờ làm mất data user.
 * - Availability: Cao (99.99%).
 * - Scalability: Lưu trữ vô hạn.
 *
 * 2. API DESIGN
 * ═══════════════════════════════════════════════════════════════════════════
 * RESTful API đơn giản:
 * - PUT /bucket/filename (Upload)
 * - GET /bucket/filename (Download)
 * - DELETE /bucket/filename
 * - HEAD /bucket/filename (Get metadata)
 *
 * 3. HIGH-LEVEL ARCHITECTURE
 * ═══════════════════════════════════════════════════════════════════════════
 * Cần tách biệt Metadata và Block Data.
 *
 * ┌──────────┐      1. Request      ┌──────────────┐      2. Check Auth    ┌──────────────┐
 * │  Client  │ ───────────────────> │ Load Balancer│ ────────────────────> │  IAM & Auth  │
 * └──────────┘                      └──────┬───────┘                       └──────────────┘
 *                                          │
 *                                          │ 3. Route
 *                                          ▼
 *                                   ┌──────────────┐
 *                                   │  API Server  │ (Frontend Service)
 *                                   └──────┬───────┘
 *                           ┌──────────────┴──────────────┐
 *                           │                             │
 *                           ▼                             ▼
 *                    ┌──────────────┐              ┌──────────────┐
 *                    │ Metadata DB  │              │  Block Store │ (Data Nodes)
 *                    │ (Sharded SQL)│              │  Service     │
 *                    └──────────────┘              └──────────────┘
 *
 * 4. DETAILED COMPONENTS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A. DATA ORGANIZATION (Block Store)
 * - Không lưu cả file lớn vào ổ cứng làm 1 cục.
 * - Chia file thành các "Chunks" (Blocks), ví dụ 64MB/chunk.
 * - Mỗi chunk có ID riêng (ChunkID = Hash(content)).
 * - Data Nodes: Các server chứa ổ cứng (HDD/SSD) để lưu raw chunks.
 *
 * B. METADATA DATABASE
 * - Lưu thông tin "File A gồm những chunks nào".
 * - Schema:
 *   File Table: { file_id, name, bucket_id, size, created_at }
 *   Chunk Map Table: { file_id, chunk_index, chunk_id }
 * - Vì metadata nhỏ nhưng access nhiều -> Dùng Sharded SQL hoặc NewSQL (CockroachDB/TiDB).
 *
 * C. REPLICATION & DURABILITY
 * - Để đạt 99.999999999% durability, không thể chỉ lưu 1 bản.
 * - Strategy 1: Replication (Easy)
 *   + Mỗi chunk được copy ra 3 bản trên 3 server ở 3 rack/zone khác nhau.
 *   + Tốn 300% storage overhead.
 *
 * - Strategy 2: Erasure Coding (Advanced - Like RAID)
 *   + Chia data thành n phần data + m phần parity.
 *   + Ví dụ (4+2): Chia chunk thành 4 phần, tính thêm 2 phần parity -> Tổng 6 phần lưu ở 6 server.
 *   + Chỉ cần 4 phần bất kỳ là khôi phục được data gốc. Cho phép chết 2 server.
 *   + Overhead chỉ 50% (thấp hơn 200% của replication).
 *
 * 5. UPLOAD FLOW (MULTIPART UPLOAD)
 * ═══════════════════════════════════════════════════════════════════════════
 * Với file lớn (ví dụ 5GB):
 * 1. Client request "Initiate Multipart Upload". Server trả về UploadID.
 * 2. Client chia file thành 100 phần (parts).
 * 3. Client upload song song (parallel) các parts lên Server.
 * 4. Server lưu từng part vào Block Store tạm thời.
 * 5. Khi xong hết, Client request "Complete Upload".
 * 6. Server ghép metadata các parts lại thành 1 file hoàn chỉnh (Logic merge, không cần physical merge).
 * -> Lợi ích: Resume được nếu rớt mạng, upload nhanh hơn.
 *
 * 6. GARBAGE COLLECTION
 * ═══════════════════════════════════════════════════════════════════════════
 * - Data trong Block Store là immutable (không sửa, chỉ ghi mới).
 * - Khi user xóa file hoặc overwrite -> Chỉ mark "Deleted" trong Metadata DB.
 * - Garbage Collector (Background Service) sẽ quét các chunks không còn được reference bởi file nào -> Xóa vật lý để giải phóng ổ cứng.
 *
 * 7. CACHING & HOT/COLD STORAGE
 * ═══════════════════════════════════════════════════════════════════════════
 * - Hot Data (Access thường xuyên): Lưu SSD, replicate nhiều, cache ở CDN.
 * - Warm Data: Lưu HDD.
 * - Cold Data (Glacier/Archive): Lưu trên Tape hoặc HDD mật độ cao, tắt điện, access chậm (vài giờ) nhưng rẻ.
 * */
public class DesignBlobStorage {
    // Class placeholder
}
