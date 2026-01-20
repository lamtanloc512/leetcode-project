package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║              DESIGN WHATSAPP / FACEBOOK MESSENGER / TELEGRAN              ║
 * ║                      (Hệ thống Chat Real-time)                            ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. YÊU CẦU (REQUIREMENTS)
 * ═══════════════════════════════════════════════════════════════════════════
 * Functional Requirements:
 * - 1-on-1 Chat: Gửi text, image, file.
 * - Group Chat: Chat nhóm (max 256 users).
 * - Message Status: Sent (1 tick), Delivered (2 ticks), Read (2 blue ticks).
 * - Last Seen / Online Status.
 * - Chat History: Lưu trữ lịch sử tin nhắn vĩnh viễn (hoặc backup).
 *
 * Non-Functional Requirements:
 * - Real-time: Latency cực thấp.
 * - Consistency: Message order phải đúng (FIFO).
 * - Availability: Cao, nhưng có thể hy sinh Availability cho Consistency (CAP theorem) trong 1 số case.
 * - Security: End-to-End Encryption (E2EE).
 *
 * 2. ƯỚC LƯỢNG (ESTIMATION)
 * ═══════════════════════════════════════════════════════════════════════════
 * - 2 Tỷ users. 100 Tỷ messages/ngày.
 * - Storage:
 *   + Mỗi msg 100 bytes.
 *   + Total = 100 Tỷ * 100 bytes = 10 TB/ngày.
 *   + 5 năm = 18 PB.
 *   => Cần Database Key-Value store support write cực mạnh (Cassandra/HBase).
 * - Bandwidth: Upload ảnh/video chiếm phần lớn. Text không đáng kể.
 *
 * 3. CONNECTION ARCHITECTURE: WEBSOCKET
 * ═══════════════════════════════════════════════════════════════════════════
 * Tại sao không dùng HTTP Polling?
 * - Polling tốn resource server, latency cao.
 * - Chat cần 2-way communication -> Dùng WebSocket.
 *
 * CHAT SERVER (Stateful):
 * - Giữ kết nối WebSocket persistent với Client.
 * - Vì server giữ state (connection), nên Load Balancer cần "Sticky Session" hoặc Service Discovery.
 *
 * 4. HIGH-LEVEL DESIGN
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌────────┐ (WebSocket)  ┌─────────────┐            ┌──────────────┐
 * │ User A │ ───────────> │ Chat Server │ ────────-> │ Msg Queue    │ (Kafka)
 * └────────┘              │    (1)      │            │              │
 *                         └──────┬──────┘            └──────┬───────┘
 *                                │                          │
 *                         (User B connected?)               │ (Store Msg)
 *                                │                          ▼
 *                                │                   ┌──────────────┐
 *                                │                   │ Key-Value DB │ (HBase/Cassandra)
 *                                ▼                   │ (History)    │
 *                         ┌─────────────┐            └──────────────┘
 *                         │ Discovery   │ (Zookeeper/Redis)
 *                         │ Service     │
 *                         └──────┬──────┘
 *                                │ Use B is on Chat Server (2)
 *                                ▼
 *                         ┌─────────────┐ (WebSocket) ┌────────┐
 *                         │ Chat Server │ ──────────> │ User B │
 *                         │    (2)      │             └────────┘
 *                         └─────────────┘
 *
 * FLOW GỬI TIN NHẮN (User A -> User B):
 * 1. User A gửi msg qua WebSocket tới Chat Server 1.
 * 2. Server 1 tìm xem User B đang kết nối tới Chat Server nào (qua Discovery Service - Redis).
 *    - Redis lưu mapping: UserID -> ServerIP.
 * 3. Nếu User B online (đang ở Server 2):
 *    - Server 1 forward msg tới Server 2 (qua RPC hoặc Message Queue).
 *    - Server 2 push msg qua WebSocket xuống User B.
 * 4. Nếu User B offline:
 *    - Lưu msg vào DB (Temporary storage hoặc Main DB).
 *    - Khi User B online lại -> Pull unsent messages.
 *
 * 5. DATABASE SCHEMA (NoSQL)
 * ═══════════════════════════════════════════════════════════════════════════
 * Tại sao dùng NoSQL (Like Cassandra/HBase)?
 * - Write throughput cực lớn.
 * - Query pattern đơn giản: Lấy messages theo conversation_id + time range.
 * - Dễ dàng scale horizontal.
 *
 * Table: Messages
 * - Partition Key: conversation_id (Để group msg cùng hội thoại vào 1 node).
 * - Clustering Key: message_id (TimeUUID) (Để sort msg theo thời gian).
 *
 * ┌─────────────────┬─────────────────┬──────────────┬───────────────┐
 * │ conversation_id │ message_id (PK) │ sender_id    │ content       │
 * ├─────────────────┼─────────────────┼──────────────┼───────────────┤
 * │ chat_123        │ 20230101_100000 │ user_A       │ Hello         │
 * │ chat_123        │ 20230101_100005 │ user_B       │ Hi there      │
 * └─────────────────┴─────────────────┴──────────────┴───────────────┘
 *
 * 6. MESSAGE ACKNOWLEDGEMENT (Sent -> Delivered -> Read)
 * ═══════════════════════════════════════════════════════════════════════════
 * - Sent (1 tick): Server nhận được msg từ A -> Gửi Ack về A.
 * - Delivered (2 ticks): Server push được msg xuống B -> B gửi Ack về Server -> Server báo A.
 * - Read (Blue ticks): B mở app xem -> Gửi "Read Receipt" -> Server -> A.
 *
 * 7. GROUP CHAT
 * ═══════════════════════════════════════════════════════════════════════════
 * Challenge: A gửi msg vào group 100 người -> Server phải gửi 100 msgs.
 *
 * Solution:
 * - Server lookup danh sách members của GroupID.
 * - Với mỗi member, tìm connection server và push msg.
 * - Limit group size (ví dụ 256) để tránh block server quá lâu.
 *
 * 8. MEDIA FILES (Images/Videos)
 * ═══════════════════════════════════════════════════════════════════════════
 * - KHÔNG gửi file binary qua WebSocket chat flow (làm chậm chat server).
 * - Flow:
 *   1. User upload ảnh lên Blob Storage (S3) qua HTTP upload server.
 *   2. Nhận được URL ảnh.
 *   3. Gửi message chứa URL ảnh qua kênh Chat.
 *   4. User B nhận message -> Tự download ảnh từ URL.
 *
 * 9. END-TO-END ENCRYPTION (E2EE)
 * ═══════════════════════════════════════════════════════════════════════════
 * - Server KHÔNG ĐƯỢC ĐỌC nội dung tin nhắn.
 * - Key Exchange Mechanism (Signal Protocol / Diffie-Hellman):
 *   + User A có Public Key & Private Key.
 *   + Khi A gửi cho B: A encrypt bằng Public Key của B.
 *   + Chỉ B (có Private Key của B) mới giải mã được.
 *   + Server chỉ trung chuyển cục data encrypted "rác", không hiểu gì cả.
 * */
public class DesignWhatsApp {
    // Class placeholder
}
