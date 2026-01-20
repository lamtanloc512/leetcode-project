package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                     DESIGN DISTRIBUTED JOB SCHEDULER                      ║
 * ║                (Hệ thống lập lịch Distributed Cron/Task)                  ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. YÊU CẦU (REQUIREMENTS)
 * ═══════════════════════════════════════════════════════════════════════════
 * - Submit Job: User có thể tạo job chạy 1 lần (One-off) hoặc định kỳ (Cron).
 * - High Reliability: Job phải được chạy, không được phép miss.
 * - Idempotency: Mỗi job chỉ nên chạy đúng 1 lần (Exactly-once semantics là rất khó -> At-least-once + Idempotent Workers).
 * - Scalability: Hỗ trợ hàng triệu jobs.
 *
 * 2. CORE COMPONENTS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A. SCHEDULER NODE
 * - Nhiệm vụ: Quét DB tìm các job "sắp đến giờ chạy".
 * - Issue: Nếu có 10 Scheduler servers, làm sao không chạy trùng job?
 *   -> MASTER ELECTION (Leader Election).
 *   -> Chỉ 1 Master node được quét DB và push job vào Queue.
 *   -> Tool: Zookeeper / Etcd / Redis Lock.
 *
 * B. EXECUTION QUEUE (Message Queue)
 * - Kafka / RabbitMQ.
 * - Tách biệt việc "Lập lịch" (When to run) và "Thực thi" (How to run).
 *
 * C. WORKER NODES
 * - Subscribe Queue và thực thi job.
 * - Nếu worker chết khi đang chạy? -> Cơ chế Ack/Nack của Queue sẽ đẩy lại cho worker khác.
 *
 * 3. ARCHITECTURE DIAGRAM
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────┐ Submit  ┌──────────────┐ Store    ┌──────────────┐
 * │  Client  │ ──────> │  API Server  │ ───────> │    SQL DB    │ (Jobs Table)
 * └──────────┘         └──────────────┘          └──────┬───────┘
 *                                                       │
 *                                                       │ Poll (Master Only)
 *                                                       ▼
 * ┌──────────┐  Elect  ┌──────────────┐          ┌──────────────┐
 * │ Zookeeper│ <────── │  Scheduler   │ ───────> │  Msg Queue   │ (Kafka)
 * └──────────┘         │  (Cluster)   │   Push   └──────┬───────┘
 *                      └──────────────┘                 │
 *                                                       │ Pull
 *                                                       ▼
 *                                                ┌──────────────┐
 *                                                │ Worker Nodes │
 *                                                │ (Execute)    │
 *                                                └──────────────┘
 *
 * 4. DETAILED JOB MANAGEMENT
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * DATABASE SCHEMA:
 * - jobs (id, cron_expression, next_run_at, status, payload)
 *
 * LEADER (SCHEDULER) LOGIC:
 * - Loop vô tận (mỗi giây):
 *   1. SELECT * FROM jobs WHERE next_run_at <= NOW() AND status = 'WAITING'.
 *   2. Với mỗi job tìm được:
 *      - Push to Kafka.
 *      - Calculate new `next_run_at` (nếu là cron).
 *      - UPDATE jobs SET next_run_at = ..., status = 'QUEUED'.
 *
 * DELAYED QUEUE IDEA:
 * - Kafka không native support "Delay 1 tiếng nữa mới deliver".
 * - Có thể dùng Redis Sorted Set (ZSET) với Score = timestamp.
 * - Scheduler poll Redis ZSET range[0, now].
 *
 * 5. HANDLING FAILURES
 * ═══════════════════════════════════════════════════════════════════════════
 * - Worker đang chạy thì crash?
 *   -> Kafka không thấy Ack sau timeout -> Re-deliver message cho Worker khác.
 * - Job chạy quá lâu?
 *   -> Cần timeout mechanism.
 * - Job fail liên tục?
 *   -> Dead Letter Queue (DLQ) để dev investigate.
 * */
public class DesignTaskScheduler {
}
