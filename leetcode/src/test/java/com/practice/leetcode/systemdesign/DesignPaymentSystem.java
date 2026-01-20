package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                      DESIGN PAYMENT SYSTEM / GATEWAY                      ║
 * ║                  (Hệ thống Thanh toán & Ví điện tử)                       ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. YÊU CẦU (REQUIREMENTS)
 * ═══════════════════════════════════════════════════════════════════════════
 * Functional Requirements:
 * - Payment Processing: Xử lý giao dịch nạp/rút/chuyển tiền (Deposit/Withdraw/Transfer).
 * - Reconciliation: Đối soát giao dịch với ngân hàng/bên thứ 3 để đảm bảo không mất tiền.
 * - History: Xem lịch sử giao dịch.
 *
 * Non-Functional Requirements:
 * - RELIABILITY & ACCURACY: QUAN TRỌNG NHẤT. Không được sai lệch 1 xu.
 * - Consistency: ACID transactions là bắt buộc.
 * - Security: Encryption, PCI DSS compliance.
 *
 * 2. CORE CONCEPTS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A. DOUBLE-ENTRY LEDGER (Sổ cái kép)
 * - Nguyên tắc kế toán: Tiền không tự sinh ra hay mất đi, chỉ chuyển từ account này sang account khác.
 * - Mọi giao dịch phải ghi ít nhất 2 dòng: 1 Debit (Nợ) và 1 Credit (Có).
 * - Sum(Debit) = Sum(Credit).
 *
 *   Ví dụ: A chuyển $10 cho B.
 *   Account      | Debit | Credit
 *   -------------|-------|-------
 *   User A       | $10   |   0     (A giảm tiền)
 *   User B       |   0   | $10     (B tăng tiền)
 *
 * B. IDEMPOTENCY (Tính lũy đẳng)
 * - Mạng lag, client retry -> Server không được trừ tiền 2 lần.
 * - Client gửi kèm `idempotency_key` (UUID).
 * - Server check key trong DB/Redis: Nếu đã xử lý -> Trả về kết quả cũ, không execute lại.
 *
 * 3. HIGH-LEVEL ARCHITECTURE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────┐     1. Pay      ┌──────────────┐     2. Event     ┌──────────────┐
 * │  Client  │ ──────────────> │ Pmt Service  │ ───────────────> │ Risk Engine  │ (Check Fraud)
 * └──────────┘                 └──────┬───────┘                  └──────────────┘
 *                                     │
 *                                     │ 3. Execute
 *                                     ▼
 *                              ┌──────────────┐
 *                              │  PSP Client  │ (Payment Service Provider)
 *                              └──────┬───────┘
 *                                     │
 *          4. Call External API       │ 5. Callback / Webhook
 *          ───────────────────►       │ ◄────────────────────
 *                                     │
 *                              ┌──────▼───────┐
 *                              │ External Bank│ (Stripe/PayPal/Visa)
 *                              └──────────────┘
 *
 * 4. DETAILED COMPONENT: PAYMENT SERVICE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A. STATE MACHINE
 * - Một Payment có các trạng thái nghiêm ngặt:
 *   [CREATED] ──(User enters info)──> [PENDING] ──(Send to Bank)──> [PROCESSING]
 *                                        │                              │
 *                                        │                              ├───────► [SUCCESS]
 *                                        │                              │
 *                                        └──────────────────────────────┴───────► [FAILED]
 *
 * B. DISTRIBUTED TRANSACTIONS (Internal Wallet)
 * - Nếu hệ thống có ví nội bộ (Wallet A -> Wallet B):
 * - Dùng Transactional Database (RDBMS like Postgres/MySQL) với Isolation Level = SERIALIZABLE hoặc REPEATABLE READ.
 * - Lock row khi update balance để tránh Race Condition.
 *   SELECT * FROM balance WHERE user_id = A FOR UPDATE;
 *
 * 5. RECONCILIATION (ĐỐI SOÁT) - "The Safety Net"
 * ═══════════════════════════════════════════════════════════════════════════
 * - Dù hệ thống tốt đến đâu, network vẫn có thể fail ở bước cuối cùng (Bank trừ tiền nhưng không báo lại).
 * - Batch Job chạy hàng ngày (T+1):
 *   1. Download Settlement File từ Bank (CSV chứa tất cả giao dịch ngày hôm qua).
 *   2. So sánh với Internal Database.
 *   3. Phát hiện lệch (Discrepancy):
 *      - Có ở Bank, thiếu ở DB -> Fix: Cập nhật trạng thái transaction thành SUCCESS.
 *      - Có ở DB (Success), thiếu ở Bank -> Fix: Mark transaction FAILED, hoàn tiền cho user.
 *
 * 6. SECURITY & COMPLIANCE
 * ═══════════════════════════════════════════════════════════════════════════
 * - HTTPS/TLS 1.3 cho mọi kết nối.
 * - Tokenization: Không lưu số thẻ (PAN) trực tiếp. Gửi sang Bank đổi lấy Token.
 * - PCI DSS: Chuẩn bảo mật thẻ thanh toán.
 * */
public class DesignPaymentSystem {
    // Class placeholder
}
