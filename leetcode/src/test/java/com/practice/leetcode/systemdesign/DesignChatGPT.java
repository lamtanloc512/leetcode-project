package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                      DESIGN CHATGPT / LLM SYSTEM                          ║
 * ║                     (Hệ thống AI Chat Inference)                          ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * 1. YÊU CẦU
 * ═══════════════════════════════════════════════════════════════════════════
 * - Streaming Response: Text hiện ra từng từ (Token streaming) để giảm cảm giác chờ đợi.
 * - Context Management: Hiểu hội thoại trước đó.
 * - Inference Latency: Cần tối ưu dù model rất nặng.
 *
 * 2. HIGH-LEVEL ARCHITECTURE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────┐     Wait (SSE)     ┌──────────────┐    GRPC/Internal  ┌──────────────┐
 * │  Client  │ <───────────────── │  Gateway /   │ ◄──────────────── │ Model Worker │ (GPU)
 * │          │  Stream tokens     │  API Server  │  Token Stream     │ (vLLM/TGI)   │
 * └────┬─────┘                    └──────┬───────┘                   └──────┬───────┘
 *      │ Request                         │                                  ▲
 *      └─────────────────────────────────┘                                  │
 *                                        │ Get Context                      │ Load Weights
 *                                        ▼                                  ▼
 *                                 ┌──────────────┐                   ┌──────────────┐
 *                                 │  Session DB  │                   │ Model Weights│
 *                                 │ (Redis/SQL)  │                   │ (S3 cache)   │
 *                                 └──────────────┘                   └──────────────┘
 *
 * 3. DETAILED COMPONENTS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A. STREAMING PROTOCOL: SERVER-SENT EVENTS (SSE)
 * - Khác với REST (Request-Response trọn gói), LLM trả về từng token.
 * - WebSockets: OK nhưng phức tạp, overkill cho 1 chiều Server->Client.
 * - SSE (Server-Sent Events): Chuẩn HTTP giữ kết nối open, server push text data liên tục. Rất phù hợp cho Chatbot.
 *
 * B. CONTEXT WINDOW MANAGEMENT
 * - Prompt gửi xuống Model = System Prompt + History (User/Bot) + New Query.
 * - Model có giới hạn Input (eg. 4096 tokens, 128k tokens).
 * - "Sliding Window": Khi quá dài, cắt bỏ các đoạn hội thoại cũ nhất.
 * - "Summarization": Dùng 1 model nhỏ tóm tắt hội thoại cũ thay vì cắt bỏ.
 *
 * C. INFERENCE SCHEDULER (KV CACHE)
 * - Chạy model rất tốn GPU RAM.
 * - KV Cache (Key-Value Cache): Lưu lại tính toán của các tokens trước đó để không phải tính lại từ đầu cho mỗi token mới sinh ra. -> Speed up 10x.
 * - Continuous Batching: Gom nhiều requests từ users khác nhau để chạy song song trên GPU thay vì chạy tuần tự. (Tools: vLLM, TGI).
 *
 * 4. RAG (RETRIEVAL AUGMENTED GENERATION) - Cung cấp kiến thức bên ngoài
 * ═══════════════════════════════════════════════════════════════════════════
 * Khi hỏi ChatGPT về dữ liệu riêng tư (không có trong training data).
 *
 * FLOW:
 * 1. Ingestion: Convert documents (PDF, Wiki) -> Chunks -> Vectors (dùng Embedding Model).
 * 2. Store: Lưu Vectors vào Vector DB (Pinecone, Milvus, Qdrant).
 * 3. Retrieval:
 *    - User question -> Vectorize.
 *    - Tìm top K chunks tương đồng trong Vector DB (Cosine Similarity).
 * 4. Generation:
 *    - Prompt = "Dựa vào context sau đây: [Chunk1, Chunk2]... Hãy trả lời câu hỏi: [User Query]"
 *    - Gửi Prompt này cho LLM.
 *
 * 5. TRAINING VS INFERENCE PIPELINE
 * ═══════════════════════════════════════════════════════════════════════════
 * - Training (Pre-training/Fine-tuning): Chạy off-line, tốn hàng tháng, cần hàng nghìn GPUs sync parameters. Output = Model Weights (Checkpoint).
 * - Inference: Chạy on-line, load Weights vào VRAM, serve user requests.
 * */
public class DesignChatGPT {
}
