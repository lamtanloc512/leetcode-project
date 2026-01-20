package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                      DESIGN GOOGLE DOCS / NOTION                          ║
 * ║                  (Collaborative Editing - Soạn thảo Cộng tác)             ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. YÊU CẦU (REQUIREMENTS)
 * ═══════════════════════════════════════════════════════════════════════════
 * - Multiple users edit same document at same time (Real-time).
 * - Conflict Resolution: User A và B cùng gõ -> Merge hợp lý, không được mất chữ.
 * - History: Xem lại lịch sử chỉnh sửa.
 * - Offline support: Edit khi mất mạng, sync khi có mạng.
 *
 * 2. CORE CHALLENGE: CONCURRENCY CONTROL
 * ═══════════════════════════════════════════════════════════════════════════
 * Vấn đề:
 * - Doc ban đầu: "ABC"
 * - User A (Insert 'X' at 0): Muốn thành "XABC".
 * - User B (Delete 'C' at 2): Muốn thành "AB".
 *
 * Nếu Naive approach (gửi cả full text):
 * - User A save "XABC".
 * - User B save "AB" -> Overwrite mất chữ 'X' của A. -> SAI.
 *
 * GIẢI PHÁP: OPERATIONAL TRANSFORMATION (OT) hoặc CRDT (Conflict-free Replicated Data Type).
 * *Google Docs dùng OT.*
 *
 * 3. OPERATIONAL TRANSFORMATION (OT)
 * ═══════════════════════════════════════════════════════════════════════════
 * Thay vì gửi full text, gửi Operation:
 * - Insert(char, position)
 * - Delete(position)
 *
 * Ví dụ OT Logic:
 * - Client A: Insert('X', 0)
 * - Client B: Delete(2)  (Delete chữ 'C' ở vị trí 2 của "ABC")
 *
 * Khi Server nhận Insert('X', 0) trước:
 * - Doc Server thành: "XABC".
 * - Operation của Client B (Delete(2)) phải được "Transform" thành Delete(3) vì 'X' đã chèn vào đầu, đẩy 'C' dịch sang phải 1 bước.
 *
 * Formula: Transform(Op_B, Op_A) -> Op_B'
 *
 * 4. HIGH-LEVEL ARCHITECTURE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────┐   WebSocket    ┌──────────────┐ Store Ops    ┌──────────────┐
 * │ User A   │ <────────────> │              │ ───────────> │ Time Series  │ (Logs)
 * └──────────┘                │              │              │ DB           │
 *                             │ Collaboration│              └──────────────┘
 * ┌──────────┐   WebSocket    │ Server       │
 * │ User B   │ <────────────> │ (OT Engine)  │ Update       ┌──────────────┐
 * └──────────┘                │              │ ───────────> │ Document DB  │ (Snapshot)
 *                             └──────────────┘              │ (NoSQL)      │
 *                                                           └──────────────┘
 *
 * 5. DATA STRUCTURE
 * ═══════════════════════════════════════════════════════════════════════════
 * - Không lưu doc dưới dạng chuỗi dài (String performance kém khi insert giữa).
 * - Dùng: Piece Table, Gap Buffer, hoặc Rope (Tree structure).
 *
 * 6. COMMUNICATION FLOW
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. User A gõ phím -> Browser tạo Operation (Op).
 * 2. Apply Op ngay lập tức lên local view (để mượt).
 * 3. Gửi Op lên Server qua WebSocket.
 * 4. Server nhận Op:
 *    - Assign Version Number.
 *    - Transform Op nếu cần (dựa trên history các ops khác vừa nhận).
 *    - Apply vào Server State.
 *    - Broadcast Op (đã transform) tới User B.
 * 5. User B nhận Op -> Transform với local pending ops (nếu có) -> Apply vào view.
 * */
public class DesignGoogleDocs {
    // Class placeholder
}
