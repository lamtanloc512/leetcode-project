package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                    SYSTEM DESIGN CONCEPTS GUIDE                           ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class SystemDesignConceptsGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // IDEMPOTENCY
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ IDEMPOTENCY = Thực hiện nhiều lần cho cùng kết quả như 1 lần               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ VÍ DỤ:                                                                      │
   * │ ✓ GET /users/123         → Idempotent (đọc không thay đổi state)           │
   * │ ✓ PUT /users/123         → Idempotent (set value, nhiều lần = 1 lần)       │
   * │ ✓ DELETE /users/123      → Idempotent (xóa nhiều lần = xóa 1 lần)          │
   * │ ✗ POST /orders           → NOT Idempotent (tạo mới mỗi lần gọi)            │
   * │ ✗ PATCH /users/123/count → NOT Idempotent (increment mỗi lần)              │
   * │                                                                             │
   * │ TẠI SAO QUAN TRỌNG?                                                         │
   * │ - Network failures → Client retry                                           │
   * │ - Distributed systems → Message duplication                                 │
   * │ - Payment systems → Double charge prevention                                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ CÁCH IMPLEMENT IDEMPOTENCY                                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. IDEMPOTENCY KEY (phổ biến nhất)                                          │
   * │    - Client gửi unique key trong header: X-Idempotency-Key: uuid            │
   * │    - Server lưu key + response trong cache/DB                               │
   * │    - Nếu key đã tồn tại → return cached response                            │
   * │                                                                             │
   * │    ┌─────────┐    POST /payment        ┌─────────┐                          │
   * │    │ Client  │ ──────────────────────> │ Server  │                          │
   * │    │         │    Key: abc-123         │         │                          │
   * │    └─────────┘                         └────┬────┘                          │
   * │                                              │                              │
   * │                                              ▼                              │
   * │                                     ┌───────────────┐                       │
   * │                                     │ Check key     │                       │
   * │                                     │ in Redis/DB   │                       │
   * │                                     └───────┬───────┘                       │
   * │                              ┌──────────────┴──────────────┐                │
   * │                              │                             │                │
   * │                        Key exists?                   Key not found          │
   * │                              │                             │                │
   * │                              ▼                             ▼                │
   * │                      Return cached             Process request              │
   * │                        response               Save key + response           │
   * │                                                                             │
   * │ 2. DATABASE CONSTRAINTS                                                     │
   * │    - Unique constraint trên order_id, transaction_id                        │
   * │    - INSERT ... ON CONFLICT DO NOTHING                                      │
   * │                                                                             │
   * │ 3. CONDITIONAL UPDATES                                                      │
   * │    - UPDATE ... WHERE version = expected_version                            │
   * │    - ETag / If-Match headers                                                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CAP THEOREM
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ CAP = Consistency, Availability, Partition Tolerance                        │
   * │ Chỉ có thể chọn 2 trong 3!                                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │           Consistency                                                       │
   * │              /\                                                             │
   * │             /  \                                                            │
   * │            /    \                                                           │
   * │           / CA   \                                                          │
   * │          /________\                                                         │
   * │         /          \                                                        │
   * │        / CP      AP \                                                       │
   * │       /______________\                                                      │
   * │    Partition      Availability                                              │
   * │    Tolerance                                                                │
   * │                                                                             │
   * │ • CONSISTENCY: Mọi node trả về data giống nhau tại cùng thời điểm          │
   * │ • AVAILABILITY: Mọi request đều nhận được response (không error)           │
   * │ • PARTITION TOLERANCE: Hệ thống vẫn hoạt động khi network partition        │
   * │                                                                             │
   * │ TRONG THỰC TẾ: Network partition LUÔN xảy ra → Chọn giữa C và A            │
   * │                                                                             │
   * │ ┌──────────────┬─────────────────────────────────────────────────────┐      │
   * │ │ Type         │ Examples                                            │      │
   * │ ├──────────────┼─────────────────────────────────────────────────────┤      │
   * │ │ CP (no A)    │ MongoDB, HBase, Redis Cluster                       │      │
   * │ │              │ → Chọn consistency, có thể unavailable khi partition│      │
   * │ ├──────────────┼─────────────────────────────────────────────────────┤      │
   * │ │ AP (no C)    │ Cassandra, DynamoDB, CouchDB                        │      │
   * │ │              │ → Chọn availability, eventual consistency           │      │
   * │ ├──────────────┼─────────────────────────────────────────────────────┤      │
   * │ │ CA (no P)    │ Traditional RDBMS (single node)                     │      │
   * │ │              │ → Không thể trong distributed system                │      │
   * │ └──────────────┴─────────────────────────────────────────────────────┘      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CONSISTENCY MODELS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ CONSISTENCY MODELS (Strong → Weak)                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. STRONG CONSISTENCY                                                       │
   * │    - Read luôn trả về write gần nhất                                        │
   * │    - Đắt nhất về latency                                                    │
   * │    - Ví dụ: Banking transactions                                            │
   * │                                                                             │
   * │ 2. EVENTUAL CONSISTENCY                                                     │
   * │    - Data sẽ consistent "eventually" (sau một khoảng thời gian)            │
   * │    - High availability, low latency                                         │
   * │    - Ví dụ: Social media feeds, DNS                                         │
   * │                                                                             │
   * │ 3. CAUSAL CONSISTENCY                                                       │
   * │    - Operations có quan hệ cause-effect được ordered                        │
   * │    - Ví dụ: Comment phải sau Post                                           │
   * │                                                                             │
   * │ 4. READ-YOUR-WRITES                                                         │
   * │    - User luôn thấy writes của chính mình                                   │
   * │    - Ví dụ: User update profile, ngay lập tức thấy changes                  │
   * │                                                                             │
   * │ 5. MONOTONIC READS                                                          │
   * │    - Nếu đã đọc value X, không bao giờ đọc lại value cũ hơn X              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // RATE LIMITING
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ RATE LIMITING = Giới hạn số requests trong khoảng thời gian                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ALGORITHMS:                                                                 │
   * │                                                                             │
   * │ 1. TOKEN BUCKET                                                             │
   * │    - Bucket chứa tokens, refill theo rate                                   │
   * │    - Mỗi request lấy 1 token                                                │
   * │    - Cho phép burst (nếu bucket đầy)                                        │
   * │                                                                             │
   * │    ┌─────────────────┐                                                      │
   * │    │ ● ● ● ● ○ ○ ○ ○ │ capacity = 8, tokens = 4                            │
   * │    └─────────────────┘                                                      │
   * │          ↑ refill rate: 1 token/second                                      │
   * │                                                                             │
   * │ 2. LEAKY BUCKET                                                             │
   * │    - Requests vào bucket, ra với fixed rate                                 │
   * │    - Smooths out bursts                                                     │
   * │                                                                             │
   * │    Requests ──▶ │████│ ──▶ Process (fixed rate)                             │
   * │                 │████│                                                      │
   * │                 │████│                                                      │
   * │                                                                             │
   * │ 3. FIXED WINDOW COUNTER                                                     │
   * │    - Đếm requests trong window cố định (e.g., per minute)                   │
   * │    - Đơn giản nhưng có edge case ở boundary                                 │
   * │                                                                             │
   * │    |----Window 1----|----Window 2----|                                      │
   * │    |  count: 100    |  count: 50     |                                      │
   * │                                                                             │
   * │ 4. SLIDING WINDOW LOG                                                       │
   * │    - Lưu timestamp mỗi request                                              │
   * │    - Chính xác nhưng memory intensive                                       │
   * │                                                                             │
   * │ 5. SLIDING WINDOW COUNTER                                                   │
   * │    - Kết hợp fixed window + weighted count                                  │
   * │    - Balance giữa accuracy và memory                                        │
   * │                                                                             │
   * │ IMPLEMENTATION:                                                             │
   * │ - Redis: INCR + EXPIRE, hoặc Redis Rate Limiter                             │
   * │ - API Gateway: Kong, AWS API Gateway                                        │
   * │ - Application: Guava RateLimiter, Resilience4j                              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CIRCUIT BREAKER
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ CIRCUIT BREAKER = Ngăn cascade failures trong distributed systems          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ STATES:                                                                     │
   * │                                                                             │
   * │    ┌────────┐   failures > threshold   ┌────────┐                           │
   * │    │ CLOSED │ ───────────────────────▶ │  OPEN  │                           │
   * │    │        │                          │        │                           │
   * │    └────────┘                          └───┬────┘                           │
   * │         ▲                                  │                                │
   * │         │ success                   timeout│                                │
   * │         │                                  ▼                                │
   * │         │                          ┌────────────┐                           │
   * │         └────────────────────────  │ HALF-OPEN  │                           │
   * │              test request OK       │            │                           │
   * │                                    └────────────┘                           │
   * │                                          │                                  │
   * │                                   test fails                                │
   * │                                          │                                  │
   * │                                          ▼                                  │
   * │                                    Back to OPEN                             │
   * │                                                                             │
   * │ • CLOSED: Bình thường, requests đi qua                                      │
   * │ • OPEN: Fail fast, không gọi service, return error/fallback                 │
   * │ • HALF-OPEN: Test với 1 request, nếu OK → CLOSED, fail → OPEN              │
   * │                                                                             │
   * │ LIBRARIES: Resilience4j, Hystrix (deprecated), Sentinel                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CACHING STRATEGIES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ CACHING STRATEGIES                                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. CACHE-ASIDE (Lazy Loading)                                               │
   * │    - App đọc cache, nếu miss → đọc DB → write cache                         │
   * │    - Phổ biến nhất, đơn giản                                                │
   * │                                                                             │
   * │    App ──1. Get──▶ Cache ──miss──▶ 2. Get ──▶ DB                            │
   * │     ◀──4. Return── Cache ◀──3. Set──                                        │
   * │                                                                             │
   * │ 2. READ-THROUGH                                                             │
   * │    - Cache tự động load từ DB khi miss                                      │
   * │    - App chỉ giao tiếp với cache                                            │
   * │                                                                             │
   * │ 3. WRITE-THROUGH                                                            │
   * │    - Write cả cache và DB synchronously                                     │
   * │    - Data consistency cao, write chậm                                       │
   * │                                                                             │
   * │    App ──Write──▶ Cache ──Write──▶ DB                                       │
   * │                                                                             │
   * │ 4. WRITE-BEHIND (Write-Back)                                                │
   * │    - Write cache ngay, async write DB sau                                   │
   * │    - Nhanh nhưng risk mất data                                              │
   * │                                                                             │
   * │ 5. WRITE-AROUND                                                             │
   * │    - Write trực tiếp DB, không qua cache                                    │
   * │    - Tránh cache pollution cho data ít đọc                                  │
   * │                                                                             │
   * │ EVICTION POLICIES:                                                          │
   * │ • LRU (Least Recently Used) - phổ biến nhất                                 │
   * │ • LFU (Least Frequently Used)                                               │
   * │ • FIFO (First In First Out)                                                 │
   * │ • TTL (Time To Live)                                                        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DATABASE PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ DATABASE SCALING PATTERNS                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. REPLICATION                                                              │
   * │    ┌────────┐      sync/async      ┌─────────┐                              │
   * │    │ Master │ ───────────────────▶ │ Replica │                              │
   * │    │ (R/W)  │                      │  (R)    │                              │
   * │    └────────┘                      └─────────┘                              │
   * │                                                                             │
   * │    • Master-Slave: 1 master writes, nhiều slaves read                       │
   * │    • Master-Master: Nhiều masters, conflict resolution                      │
   * │                                                                             │
   * │ 2. SHARDING (Horizontal Partitioning)                                       │
   * │    Chia data theo shard key                                                 │
   * │                                                                             │
   * │    ┌─────────┐  ┌─────────┐  ┌─────────┐                                    │
   * │    │ Shard 1 │  │ Shard 2 │  │ Shard 3 │                                    │
   * │    │ A-H     │  │ I-P     │  │ Q-Z     │                                    │
   * │    └─────────┘  └─────────┘  └─────────┘                                    │
   * │                                                                             │
   * │    Strategies:                                                              │
   * │    • Range-based: user_id 1-1000, 1001-2000, ...                            │
   * │    • Hash-based: hash(user_id) % num_shards                                 │
   * │    • Directory-based: Lookup table                                          │
   * │                                                                             │
   * │ 3. READ REPLICAS                                                            │
   * │    - Scale reads bằng cách thêm read replicas                               │
   * │    - Writes vẫn đi qua master                                               │
   * │                                                                             │
   * │ 4. CQRS (Command Query Responsibility Segregation)                          │
   * │    - Tách read model và write model                                         │
   * │    - Optimize riêng cho từng use case                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // MESSAGE QUEUE PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ MESSAGE QUEUE PATTERNS                                                      │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. POINT-TO-POINT (Queue)                                                   │
   * │    - 1 message → 1 consumer                                                 │
   * │    - Load balancing giữa consumers                                          │
   * │                                                                             │
   * │    Producer ──▶ [Queue] ──▶ Consumer                                        │
   * │                                                                             │
   * │ 2. PUBLISH-SUBSCRIBE (Topic)                                                │
   * │    - 1 message → nhiều subscribers                                          │
   * │    - Broadcasting                                                           │
   * │                                                                             │
   * │    Publisher ──▶ [Topic] ──▶ Subscriber 1                                   │
   * │                         └──▶ Subscriber 2                                   │
   * │                         └──▶ Subscriber 3                                   │
   * │                                                                             │
   * │ DELIVERY SEMANTICS:                                                         │
   * │ • At-most-once: Fire and forget, có thể mất message                         │
   * │ • At-least-once: Retry nếu không ack, có thể duplicate                      │
   * │ • Exactly-once: Khó nhất, cần idempotency + transactions                    │
   * │                                                                             │
   * │ ORDERING:                                                                   │
   * │ • Kafka: Ordered within partition                                           │
   * │ • RabbitMQ: FIFO per queue                                                  │
   * │ • SQS: Best effort (FIFO queues available)                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DISTRIBUTED TRANSACTIONS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ DISTRIBUTED TRANSACTIONS                                                    │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. TWO-PHASE COMMIT (2PC)                                                   │
   * │    - Coordinator hỏi tất cả participants: "Ready?"                          │
   * │    - Nếu tất cả OK → Commit, nếu có 1 fail → Rollback                       │
   * │                                                                             │
   * │    Phase 1 (Prepare):                                                       │
   * │    Coordinator ──Prepare──▶ Participant 1 ──Vote Yes/No──▶                  │
   * │                 ──Prepare──▶ Participant 2 ──Vote Yes/No──▶                  │
   * │                                                                             │
   * │    Phase 2 (Commit/Rollback):                                               │
   * │    Coordinator ──Commit/Rollback──▶ All Participants                        │
   * │                                                                             │
   * │    ⚠️ Blocking, slow, coordinator = SPOF                                    │
   * │                                                                             │
   * │ 2. SAGA PATTERN                                                             │
   * │    - Chuỗi local transactions                                               │
   * │    - Nếu fail → compensating transactions (rollback)                        │
   * │                                                                             │
   * │    T1 ──▶ T2 ──▶ T3 ──▶ T4 (Success)                                        │
   * │                   │                                                         │
   * │                   ▼ (Fail)                                                  │
   * │    C1 ◀── C2 ◀───┘ (Compensate)                                             │
   * │                                                                             │
   * │    Types:                                                                   │
   * │    • Choreography: Events trigger next step                                 │
   * │    • Orchestration: Central coordinator                                     │
   * │                                                                             │
   * │ 3. OUTBOX PATTERN                                                           │
   * │    - Write to DB + Outbox table in same transaction                         │
   * │    - Separate process reads outbox → publish events                         │
   * │    - Ensures atomicity between DB write and event publish                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // LOAD BALANCING
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ LOAD BALANCING ALGORITHMS                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌──────────────────────┬────────────────────────────────────────────┐       │
   * │ │ Algorithm            │ Description                                │       │
   * │ ├──────────────────────┼────────────────────────────────────────────┤       │
   * │ │ Round Robin          │ Lần lượt từng server                       │       │
   * │ │ Weighted Round Robin │ Server mạnh nhận nhiều requests hơn        │       │
   * │ │ Least Connections    │ Route đến server ít connection nhất        │       │
   * │ │ IP Hash              │ Same client IP → same server (sticky)      │       │
   * │ │ Random               │ Random server                              │       │
   * │ │ Least Response Time  │ Server có response time nhanh nhất         │       │
   * │ └──────────────────────┴────────────────────────────────────────────┘       │
   * │                                                                             │
   * │ L4 vs L7:                                                                   │
   * │ • L4 (Transport): Dựa trên IP/Port, nhanh, không thấy content              │
   * │ • L7 (Application): Dựa trên URL/Headers/Cookies, flexible                  │
   * │                                                                             │
   * │ HEALTH CHECKS:                                                              │
   * │ • Active: LB gửi periodic health check requests                             │
   * │ • Passive: LB monitor responses từ real traffic                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DISTRIBUTED ID GENERATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ DISTRIBUTED ID GENERATION                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. UUID                                                                     │
   * │    - 128-bit, globally unique                                               │
   * │    - Không cần coordination                                                 │
   * │    - ⚠️ Không sortable, bad for DB indexes                                  │
   * │                                                                             │
   * │ 2. DATABASE AUTO-INCREMENT                                                  │
   * │    - Simple, sortable                                                       │
   * │    - ⚠️ SPOF, không scale horizontally                                      │
   * │                                                                             │
   * │ 3. TWITTER SNOWFLAKE                                                        │
   * │    - 64-bit ID, sortable by time                                            │
   * │                                                                             │
   * │    ┌─────────────────────────────────────────────────────────────────┐      │
   * │    │ 1 │    41 bits     │  10 bits  │     12 bits      │            │      │
   * │    │ 0 │  timestamp ms  │ machine ID│  sequence number │            │      │
   * │    └─────────────────────────────────────────────────────────────────┘      │
   * │                                                                             │
   * │    - Timestamp: ~69 years                                                   │
   * │    - Machine ID: 1024 machines                                              │
   * │    - Sequence: 4096 IDs per millisecond per machine                         │
   * │                                                                             │
   * │ 4. ULID                                                                     │
   * │    - Lexicographically sortable                                             │
   * │    - 128-bit, URL-safe                                                      │
   * │                                                                             │
   * │ 5. TICKET SERVER (Flickr)                                                   │
   * │    - Dedicated servers generate IDs                                         │
   * │    - Multiple servers với different ranges                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SERVICE DISCOVERY
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SERVICE DISCOVERY                                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. CLIENT-SIDE DISCOVERY                                                    │
   * │    - Client query registry, client chọn instance                            │
   * │    - Ví dụ: Netflix Eureka + Ribbon                                         │
   * │                                                                             │
   * │    ┌────────┐  1. Query  ┌──────────┐                                       │
   * │    │ Client │ ─────────▶ │ Registry │                                       │
   * │    │        │ ◀───────── │          │                                       │
   * │    └────┬───┘  2. List   └──────────┘                                       │
   * │         │                                                                   │
   * │         │ 3. Call directly                                                  │
   * │         ▼                                                                   │
   * │    ┌─────────┐                                                              │
   * │    │ Service │                                                              │
   * │    └─────────┘                                                              │
   * │                                                                             │
   * │ 2. SERVER-SIDE DISCOVERY                                                    │
   * │    - Load balancer query registry                                           │
   * │    - Ví dụ: AWS ALB, Kubernetes                                             │
   * │                                                                             │
   * │    ┌────────┐         ┌──────────────┐         ┌─────────┐                  │
   * │    │ Client │ ──────▶ │ Load Balancer│ ──────▶ │ Service │                  │
   * │    └────────┘         └──────┬───────┘         └─────────┘                  │
   * │                              │                                              │
   * │                              ▼                                              │
   * │                         ┌──────────┐                                        │
   * │                         │ Registry │                                        │
   * │                         └──────────┘                                        │
   * │                                                                             │
   * │ TOOLS: Consul, etcd, ZooKeeper, Kubernetes DNS                              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // API DESIGN BEST PRACTICES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ API DESIGN BEST PRACTICES                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. VERSIONING                                                               │
   * │    • URL: /api/v1/users                                                     │
   * │    • Header: Accept: application/vnd.api+json;version=1                     │
   * │    • Query: /api/users?version=1                                            │
   * │                                                                             │
   * │ 2. PAGINATION                                                               │
   * │    • Offset-based: ?page=2&limit=20                                         │
   * │    • Cursor-based: ?cursor=abc123&limit=20 (better for large datasets)      │
   * │                                                                             │
   * │ 3. ERROR HANDLING                                                           │
   * │    {                                                                        │
   * │      "error": {                                                             │
   * │        "code": "USER_NOT_FOUND",                                            │
   * │        "message": "User with ID 123 not found",                             │
   * │        "details": {...}                                                     │
   * │      }                                                                      │
   * │    }                                                                        │
   * │                                                                             │
   * │ 4. BACKWARD COMPATIBILITY                                                   │
   * │    • Add fields, don't remove                                               │
   * │    • Deprecate before removing                                              │
   * │    • Default values for new required fields                                 │
   * │                                                                             │
   * │ 5. RATE LIMITING HEADERS                                                    │
   * │    X-RateLimit-Limit: 100                                                   │
   * │    X-RateLimit-Remaining: 45                                                │
   * │    X-RateLimit-Reset: 1640000000                                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
