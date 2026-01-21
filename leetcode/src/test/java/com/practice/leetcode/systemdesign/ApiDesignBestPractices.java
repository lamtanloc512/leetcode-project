package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                     API DESIGN BEST PRACTICES GUIDE                       ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class ApiDesignBestPractices {

  // ═══════════════════════════════════════════════════════════════════════════
  // 1. FUNDAMENTAL PRINCIPLES (NGUYÊN TẮC CƠ BẢN)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ RESTFUL API PRINCIPLES                                                      │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. RESOURCE-ORIENTED (Hướng tài nguyên)                                     │
   * │    - API xoay quanh "Resource" (Nouns), không phải "Action" (Verbs).        │
   * │    - URI định danh tài nguyên, HTTP Method định nghĩa hành động.            │
   * │                                                                             │
   * │    ✅ ĐÚNG:  GET /users/123                                                 │
   * │    ❌ SAI:   GET /getUser?id=123                                            │
   * │    ❌ SAI:   POST /createUser                                               │
   * │                                                                             │
   * │ 2. STATELESS (Phi trạng thái)                                               │
   * │    - Server không lưu client state giữa các requests.                       │
   * │    - Mỗi request phải chứa đủ thông tin để server xử lý (auth token, etc).  │
   * │                                                                             │
   * │ 3. STANDARD METHODS & STATUS CODES                                          │
   * │    - Sử dụng HTTP Methods (GET, POST, PUT, DELETE) đúng ngữ nghĩa.          │
   * │    - Trả về HTTP Status Codes chuẩn (200, 201, 400, 404, 500...).           │
   * │                                                                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // 2. RESOURCE NAMING (ĐẶT TÊN RESOURCE)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ URL STRUCTURE & NAMING CONVENTIONS                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. SỬ DỤNG NOUNS (DANH TỪ), KHÔNG DÙNG VERBS (ĐỘNG TỪ)                      │
   * │    Use:    /articles                                                        │
   * │    Avoid:  /getArticles, /createArticle, /deleteArticle                     │
   * │                                                                             │
   * │ 2. SỬ DỤNG PLURAL (SỐ NHIỀU) CHO COLLECTION                                 │
   * │    - Giữ nhất quán, tốt nhất là luôn dùng số nhiều.                         │
   * │    Use:    /users/123                                                       │
   * │    Avoid:  /user/123                                                        │
   * │                                                                             │
   * │ 3. HIERARCHY (PHÂN CẤP)                                                     │
   * │    - Thể hiện quan hệ cha-con hợp lý.                                       │
   * │                                                                             │
   * │    GET /users/123/orders       (Lấy danh sách đơn hàng của user 123)        │
   * │    GET /users/123/orders/5     (Lấy đơn hàng số 5 của user 123)             │
   * │                                                                             │
   * │    ⚠️ Đừng lồng quá sâu (Max 2-3 levels). Nếu cần sâu hơn, xem xét flat URL:│
   * │    GET /orders/5               (Thay vì /shops/1/categories/2/items/3...)   │
   * │                                                                             │
   * │ 4. NAMING CASE                                                              │
   * │    - Kebab-case (chữ thường, gạch nối) là chuẩn phổ biến nhất cho URI.      │
   * │                                                                             │
   * │    ✅ ĐÚNG:  /api/v1/pending-orders                                         │
   * │    ❌ SAI:   /api/v1/pendingOrders  (CamelCase)                             │
   * │    ❌ SAI:   /api/v1/PendingOrders  (PascalCase)                            │
   * │    ❌ SAI:   /api/v1/pending_orders (Snake_case)                            │
   * │                                                                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // 3. HTTP METHODS (CÁC PHƯƠNG THỨC HTTP)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌──────┬───────────────────────┬────────────┬────────────┬────────────────────┐
   * │ Verb │ Action                │ Idempotent?│ Safety?    │ Success Code       │
   * ├──────┼───────────────────────┼────────────┼────────────┼────────────────────┤
   * │ GET  │ Lấy resource          │ YES        │ YES        │ 200 OK             │
   * ├──────┼───────────────────────┼────────────┼────────────┼────────────────────┤
   * │ POST │ Tạo resource mới      │ NO         │ NO         │ 201 Created        │
   * ├──────┼───────────────────────┼────────────┼────────────┼────────────────────┤
   * │ PUT  │ Update toàn bộ (Full) │ YES        │ NO         │ 200 OK / 204 No Cnt│
   * ├──────┼───────────────────────┼────────────┼────────────┼────────────────────┤
   * │ PATCH│ Update một phần (Part)│ NO (Maybe) │ NO         │ 200 OK / 204 No Cnt│
   * ├──────┼───────────────────────┼────────────┼────────────┼────────────────────┤
   * │DELETE│ Xóa resource          │ YES        │ NO         │ 200 OK / 204 No Cnt│
   * └──────┴───────────────────────┴────────────┴────────────┴────────────────────┘
   *
   * • Idempotent: Gọi nhiều lần kết quả server giống hệt gọi 1 lần (tính chất quan
   *   trọng khi retry mạng).
   * • Safety: Không làm thay đổi state của resource (Read-only).
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // 4. HTTP STATUS CODES (MÃ TRẠNG THÁI)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 2xx: SUCCESS (Thành công)                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ • 200 OK:            Thành công chung (GET, PUT, PATCH).                    │
   * │ • 201 Created:       Đã tạo resource mới (Response của POST).               │
   * │ • 202 Accepted:      Request đã nhận, đang xử lý background (Async).        │
   * │ • 204 No Content:    Thành công nhưng không trả về body (DELETE, PUT).      │
   * │                                                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 3xx: REDIRECTION (Chuyển hướng)                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ • 301 Moved Perm:    Resource đã chuyển hẳn sang URL mới.                   │
   * │ • 304 Not Modified:  Cache còn hiệu lực, client dùng bản đã cache.          │
   * │                                                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 4xx: CLIENT ERRORS (Lỗi do Client)                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ • 400 Bad Request:   Input sai format, thiếu field, logic sai.              │
   * │ • 401 Unauthorized:  Chưa đăng nhập hoặc token hết hạn.                     │
   * │ • 403 Forbidden:     Đăng nhập rồi nhưng không có quyền (Role/Scope).       │
   * │ • 404 Not Found:     Resource không tồn tại.                                │
   * │ • 405 Method Not All:Method không hỗ trợ cho URL này.                       │
   * │ • 409 Conflict:      Xung đột state (e.g., trùng email khi đăng ký).        │
   * │ • 429 Too Many Req:  Rate limited.                                          │
   * │                                                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 5xx: SERVER ERRORS (Lỗi do Server)                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ • 500 Internal Err:  Bug của server, exception không được handle.           │
   * │ • 502 Bad Gateway:   Upstream server lỗi (proxy nhận lỗi từ app).           │
   * │ • 503 Service Unav:  Server quá tải hoặc đang bảo trì.                      │
   * │ • 504 Gateway Time:  Timeout khi gọi upstream (DB, 3rd party).              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // 5. FILTERING, SORTING, PAGINATION, FIELD SELECTION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 1. FILTERING (Lọc dữ liệu)                                                  │
   * │    - Sử dụng query parameters cho các điều kiện lọc.                        │
   * │    GET /cars?color=red&brand=toyota                                         │
   * │    GET /users?age_gte=18 (age >= 18)                                        │
   * │                                                                             │
   * │ 2. SORTING (Sắp xếp)                                                        │
   * │    - Dùng param `sort` với dấu +/- để chỉ chiều tăng/giảm.                  │
   * │    GET /users?sort=-created_at,name                                         │
   * │    (Sắp xếp theo created_at giảm dần, sau đó theo name tăng dần)            │
   * │                                                                             │
   * │ 3. FIELD SELECTION (Chọn trường trả về)                                     │
   * │    - Giúp client giảm tải băng thông (GraphQL style in REST).               │
   * │    GET /users?fields=id,name,email                                          │
   * │                                                                             │
   * │ 4. PAGINATION (Phân trang)                                                  │
   * │    A. Offset-based (Truyền thống)                                           │
   * │       GET /users?page=2&limit=20                                            │
   * │       GET /users?offset=20&limit=20                                         │
   * │       PROS: Dễ implement, nhảy trang bất kỳ (Jump to page 10).              │
   * │       CONS: Chậm khi offset lớn (DB scan), data trôi nếu có insert/delete.  │
   * │                                                                             │
   * │    B. Cursor-based (Khuyên dùng cho infinite scroll/data lớn)               │
   * │       GET /users?limit=20&after_cursor=xyz123                               │
   * │       PROS: Rất nhanh ở mọi row (Seek), data consistent.                    │
   * │       CONS: Không thể jump page, cần column sortable & unique.              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // 6. VERSIONING (ĐÁNH SỐ PHIÊN BẢN)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ API phải được version để tránh breaking changes cho client cũ.              │
   * │                                                                             │
   * │ 1. URI VERSIONING (Phổ biến nhất, dễ test)                                  │
   * │    GET /api/v1/users                                                        │
   * │    GET /api/v2/users                                                        │
   * │                                                                             │
   * │ 2. HEADER VERSIONING (Custom Header)                                        │
   * │    GET /users                                                               │
   * │    H: X-API-Version: 1                                                      │
   * │                                                                             │
   * │ 3. MEDIA TYPE VERSIONING (Content Negotiation)                              │
   * │    GET /users                                                               │
   * │    H: Accept: application/vnd.myapi.v1+json                                 │
   * │                                                                             │
   * │ RECOMMENDATION: Dùng URI Versioning (v1, v2) cho public API vì tính rõ ràng │
   * │ và dễ dàng explore bằng browser.                                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // 7. ERROR HANDLING (XỬ LÝ LỖI)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Trả về cấu trúc lỗi nhất quán (Consistent Error Payload).                   │
   * │ Đừng chỉ trả về mỗi mã 400 mà không có body.                                │
   * │                                                                             │
   * │ Example JSON Error Response:                                                │
   * │ {                                                                           │
   * │   "error": {                                                                │
   * │     "code": "USR_001",                   // Mã lỗi nội bộ để debug/lookup   │
   * │     "message": "Email already exists",   // Message cho developer           │
   * │     "user_message": "Email đã tồn tại",  // Message để hiển thị UI (opt)    │
   * │     "details": [                         // Chi tiết lỗi (validation)       │
   * │       {                                                                     │
   * │         "field": "email",                                                   │
   * │         "issue": "duplicate_value"                                          │
   * │       }                                                                     │
   * │     ]                                                                       │
   * │   }                                                                         │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // 8. SECURITY BEST PRACTICES (BẢO MẬT)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 1. USE HTTPS (TLS/SSL)                                                      │
   * │    - Bắt buộc cho mọi public API.                                           │
   * │                                                                             │
   * │ 2. AUTHENTICATION (Xác thực)                                                │
   * │    - Bearer Token (JWT) trong Header là chuẩn hiện đại.                     │
   * │    Authorization: Bearer <token>                                            │
   * │    - Tránh dùng Basic Auth hoặc gửi API Key trong URL params.               │
   * │                                                                             │
   * │ 3. RATE LIMITING (Giới hạn requests)                                        │
   * │    - Bảo vệ server khỏi DDoS hoặc abuse.                                    │
   * │    - Trả về headers để client biết trạng thái:                              │
   * │      X-RateLimit-Limit: 1000                                                │
   * │      X-RateLimit-Remaining: 998                                             │
   * │      X-RateLimit-Reset: 1609459200                                          │
   * │    - Trả về 429 Too Many Requests khi vượt lố.                            │
   * │                                                                             │
   * │ 4. SECURITY HEADERS                                                         │
   * │    - Content-Security-Policy                                                │
   * │    - X-Content-Type-Options: nosniff                                        │
   * │    - X-Frame-Options: DENY                                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // 9. PERFORMANCE & OPTIMIZATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ 1. JSON STRUCTURE                                                           │
   * │    - Giữ JSON gọn nhẹ. Dùng gzip/brotli compression.                        │
   * │                                                                             │
   * │ 2. CACHING WITH ETAG                                                        │
   * │    - Server trả về header `ETag: "hash-of-content"`.                        │
   * │    - Client gửi `If-None-Match: "hash-of-content"`.                        │
   * │    - Nếu chưa đổi, Server trả 304 Not Modified (không tốn body bandwidth).  │
   * │                                                                             │
   * │ 3. ASYNCHRONOUS PROCESSING                                                  │
   * │    - Heavy tasks (xử lý ảnh, báo cáo) không nên block request.              │
   * │    - Return 202 Accepted + URL để check status/webhook.                     │
   * │    Client -> POST /reports -> 202 Accepted (Location: /tasks/123)           │
   * │    Client -> GET /tasks/123 -> Status: "Processing/Completed".              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // 10. DOCUMENTATION (TÀI LIỆU)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ API không có document tốt = API chết.                                       │
   * │                                                                             │
   * │ 1. OPENAPI SPECIFICATION (SWAGGER)                                          │
   * │    - Chuẩn công nghiệp để mô tả REST API.                                   │
   * │    - Có thể generate code client/stub và UI document (Swagger UI).          │
   * │                                                                             │
   * │ 2. EXAMPLE CODE                                                             │
   * │    - Cung cấp ví dụ curl request và response mẫu cho mọi endpoint.          │
   * │                                                                             │
   * │ 3. CHANGELOG                                                                │
   * │    - Ghi lại các thay đổi cụ thể giữa các version.                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // 11. API QUALITY CHECKLIST (TIÊU CHÍ CỐT LÕI - NFRs)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Để trả lời "API cần đảm bảo gì?", Developer cần kiểm tra các tiêu chí sau:  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. TÍNH BẢO MẬT (SECURITY)                                                  │
   * │    - [ ] Confidentiality: Data nhạy cảm có được mã hóa (HTTPS/TLS)?         │
   * │    - [ ] Authentication: Xác định danh tính client rõ ràng (JWT/OAuth2)?    │
   * │    - [ ] Authorization: Client có quyền truy cập resource này không?        │
   * │    - [ ] Integrity: Data không bị thay đổi trên đường truyền?               │
   * │                                                                             │
   * │ 2. TÍNH TIN CẬY & ỔN ĐỊNH (RELIABILITY & STABILITY)                         │
   * │    - [ ] Availability: Hệ thống có High Availability (HA) không?            │
   * │    - [ ] Resilience: Có cơ chế retry, timeout, circuit breaker chưa?        │
   * │    - [ ] Error Handling: Lỗi trả về có rõ ràng và hữu ích không?            │
   * │                                                                             │
   * │ 3. HIỆU NĂNG (PERFORMANCE)                                                  │
   * │    - [ ] Latency: Thời gian phản hồi có đạt chuẩn (e.g. p99 < 200ms)?       │
   * │    - [ ] Throughput: Chịu được bao nhiêu RPS (Requests Per Second)?         │
   * │    - [ ] Scalability: Dễ dàng scale-out khi tải tăng không?                 │
   * │                                                                             │
   * │ 4. TRẢI NGHIỆM PHÁT TRIỂN (DX - DEVELOPER EXPERIENCE)                       │
   * │    - [ ] Consistency: Naming và response format có nhất quán không?         │
   * │    - [ ] Discoverability: Dễ dàng học và thử nghiệm (Swagger/Postman)?      │
   * │    - [ ] Standard: Tuân thủ chuẩn chung (HTTP, REST) thay vì phát minh lại? │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
