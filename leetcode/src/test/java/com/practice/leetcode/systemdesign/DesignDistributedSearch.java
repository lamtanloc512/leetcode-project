package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                      DESIGN DISTRIBUTED SEARCH (ELASTICSEARCH)            ║
 * ║                       (Hệ thống Tìm kiếm Phân tán)                        ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. YÊU CẦU
 * ═══════════════════════════════════════════════════════════════════════════
 * - Indexing: Ingest data text lớn (crawler, logs, documents).
 * - Searching: Full-text search, fuzzy search, filter.
 * - Speed: Kết quả trả về < 100ms.
 * - Scale: PB data.
 *
 * 2. CORE CONCEPT: INVERTED INDEX (Chỉ mục ngược)
 * ═══════════════════════════════════════════════════════════════════════════
 * Thay vì quét từng file để tìm chữ "Apple", ta tạo 1 Map.
 *
 * Document 1: "I like Apple"
 * Document 2: "Apple is red"
 *
 * Inverted Index:
 * ┌──────────┬──────────────┐
 * │ Term     │ Doc IDs      │
 * ├──────────┼──────────────┤
 * │ apple    │ [1, 2]       │
 * │ like     │ [1]          │
 * │ red      │ [2]          │
 * └──────────┴──────────────┘
 * -> Tìm "apple" -> Trả ngay [1, 2] cực nhanh O(1).
 *
 * 3. HIGH-LEVEL ARCHITECTURE
 * ═══════════════════════════════════════════════════════════════════════════
 * Cluster gồm nhiều Node. Data được chia thành nhiều Shards.
 *
 * ┌──────────┐      1. Search     ┌──────────────┐      2. Scatter     ┌──────────────┐
 * │  Client  │ ─────────────────> │ Coordinator  │ ──────────────────> │ Node 1       │
 * └──────────┘                    │ Node         │                     │ (Shard 1, 2) │
 *                                 └──────┬───────┘           ┌───────> └──────────────┘
 *                                        │                   │
 *                                        │                   │         ┌──────────────┐
 *                                        │ 3. Gather (Merge) └───────> │ Node 2       │
 *                                        │                             │ (Shard 3, 4) │
 *                                        ▼                             └──────────────┘
 *                                 Standardize Results
 *
 * 4. WORKFLOWS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * READ PATH (SEARCH):
 * 1. Client gửi query tới Node bất kỳ (Coordinator).
 * 2. Coordinator gửi query tới TẤT CẢ các shards (vì không biết doc nằm đâu).
 * 3. Mỗi shard tìm top K results locally, trả về Coordinator (chỉ ID + Score, chưa lấy full content).
 * 4. Coordinator merge các list, sort lại, chọn global top K.
 * 5. Coordinator fetch full content của top K documents đó từ các shards liên quan.
 * 6. Trả về Client.
 *
 * WRITE PATH (INDEXING):
 * 1. Client gửi document tới Coordinator.
 * 2. Hash(doc_id) % num_shards -> Xác định shard target (ví dụ Shard 3).
 * 3. Forward doc tới Primary Shard 3.
 * 4. Primary Shard write xong -> Sync sang Replica Shards.
 * 5. Ack về Client.
 *
 * 5. NEAR REAL-TIME SEARCH (NRT)
 * ═══════════════════════════════════════════════════════════════════════════
 * - Lucene Segment: Index file is immutable (Write-once).
 * - Khi có data mới -> Buffer in memory -> Sau mỗi 1s (refresh interval) -> Write thành 1 Segment nhỏ mới.
 * - Search engine query trên tất cả các Segments rồi merge.
 * - Merge policy: Background merging các segments nhỏ thành segments lớn để tối ưu.
 *
 * 6. RANKING & RELEVANCE
 * ═══════════════════════════════════════════════════════════════════════════
 * - TF-IDF (Term Frequency - Inverse Document Frequency).
 * - BM25 (Best Matching 25 - bản nâng cấp của TF-IDF).
 * - Logic: Words xuất hiện nhiều trong 1 doc (TF cao) -> quan trọng. Words xuất hiện ở khắp mọi nơi (IDF thấp - ví dụ "the", "a") -> ít quan trọng.
 * */
public class DesignDistributedSearch {
}
