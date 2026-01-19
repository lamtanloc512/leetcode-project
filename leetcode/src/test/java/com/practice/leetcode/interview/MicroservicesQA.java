package com.practice.leetcode.interview;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                      MICROSERVICES INTERVIEW Q&A                          ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class MicroservicesQA {

  // ═══════════════════════════════════════════════════════════════════════════
  // MICROSERVICES FUNDAMENTALS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Microservices là gì? Khi nào nên dùng?                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ DEFINITION:                                                                 │
   * │ Architecture style tổ chức app thành các services nhỏ, độc lập:            │
   * │ - Single responsibility                                                     │
   * │ - Independently deployable                                                  │
   * │ - Communicate qua network (REST, gRPC, messaging)                           │
   * │ - Own their data (database per service)                                     │
   * │                                                                             │
   * │ MONOLITH vs MICROSERVICES:                                                  │
   * │ ┌─────────────────┬─────────────────────┬─────────────────────┐             │
   * │ │                 │ Monolith            │ Microservices       │             │
   * │ ├─────────────────┼─────────────────────┼─────────────────────┤             │
   * │ │ Deployment      │ All or nothing      │ Independent         │             │
   * │ │ Scaling         │ Scale entire app    │ Scale per service   │             │
   * │ │ Team            │ One big team        │ Small focused teams │             │
   * │ │ Technology      │ Single stack        │ Polyglot            │             │
   * │ │ Complexity      │ In code             │ In infrastructure   │             │
   * │ │ Data            │ Shared DB           │ DB per service      │             │
   * │ └─────────────────┴─────────────────────┴─────────────────────┘             │
   * │                                                                             │
   * │ KHI NÀO NÊN DÙNG:                                                           │
   * │ ✓ Team lớn, cần scale độc lập                                               │
   * │ ✓ Domains phức tạp, bounded contexts rõ ràng                                │
   * │ ✓ Cần technology flexibility                                                │
   * │ ✓ Có DevOps maturity (CI/CD, monitoring)                                    │
   * │                                                                             │
   * │ KHI NÀO KHÔNG NÊN:                                                          │
   * │ ✗ Startup nhỏ, chưa product-market fit                                      │
   * │ ✗ Team nhỏ (< 5-10 developers)                                              │
   * │ ✗ Simple domain                                                             │
   * │ ✗ Thiếu DevOps experience                                                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SERVICE COMMUNICATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Synchronous vs Asynchronous communication?                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ SYNCHRONOUS (Request-Response):                                             │
   * │ ┌─────────┐   HTTP/gRPC   ┌─────────┐                                       │
   * │ │ Service │──────────────►│ Service │                                       │
   * │ │    A    │◄──────────────│    B    │                                       │
   * │ └─────────┘    Response   └─────────┘                                       │
   * │                                                                             │
   * │ ✓ Simple, familiar                                                          │
   * │ ✓ Immediate response                                                        │
   * │ ✗ Tight coupling                                                            │
   * │ ✗ Service B down → Service A fails                                          │
   * │ ✗ Latency adds up                                                           │
   * │                                                                             │
   * │ ASYNCHRONOUS (Event-Driven):                                                │
   * │ ┌─────────┐   Event    ┌─────────┐   Event    ┌─────────┐                   │
   * │ │ Service │───────────►│  Kafka  │───────────►│ Service │                   │
   * │ │    A    │            │  Queue  │            │    B    │                   │
   * │ └─────────┘            └─────────┘            └─────────┘                   │
   * │                                                                             │
   * │ ✓ Loose coupling                                                            │
   * │ ✓ Resilient (queue buffers)                                                 │
   * │ ✓ Scale independently                                                       │
   * │ ✗ Eventual consistency                                                      │
   * │ ✗ Harder to debug                                                           │
   * │ ✗ Message ordering complexity                                               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Service Discovery là gì? Các patterns?                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PROBLEM: Services cần tìm nhau, IP/port thay đổi liên tục                   │
   * │                                                                             │
   * │ 1. CLIENT-SIDE DISCOVERY:                                                   │
   * │    ┌────────┐  Query  ┌──────────┐                                          │
   * │    │ Client │────────►│ Registry │                                          │
   * │    └────┬───┘         │ (Eureka) │                                          │
   * │         │             └──────────┘                                          │
   * │         │ Direct call (with load balancing)                                 │
   * │         ▼                                                                   │
   * │    ┌─────────────────────────────┐                                          │
   * │    │ Service A  Service A  ...   │                                          │
   * │    └─────────────────────────────┘                                          │
   * │    Tools: Netflix Eureka, Consul, Zookeeper                                 │
   * │                                                                             │
   * │ 2. SERVER-SIDE DISCOVERY:                                                   │
   * │    ┌────────┐       ┌──────────────┐       ┌─────────┐                      │
   * │    │ Client │──────►│ Load Balancer│──────►│ Service │                      │
   * │    └────────┘       │ (knows all)  │       └─────────┘                      │
   * │                     └──────────────┘                                        │
   * │    Tools: Kubernetes Services, AWS ELB                                      │
   * │                                                                             │
   * │ 3. DNS-BASED:                                                               │
   * │    service-a.namespace.svc.cluster.local                                    │
   * │    Tools: Kubernetes DNS, Consul DNS                                        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // API GATEWAY
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: API Gateway pattern?                                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │   Client                                                                    │
   * │     │                                                                       │
   * │     ▼                                                                       │
   * │ ┌─────────────────────────────────────────────────────────────┐             │
   * │ │                      API GATEWAY                            │             │
   * │ │ ┌─────────────────────────────────────────────────────────┐ │             │
   * │ │ │ • Authentication/Authorization                          │ │             │
   * │ │ │ • Rate Limiting                                         │ │             │
   * │ │ │ • Request Routing                                       │ │             │
   * │ │ │ • Load Balancing                                        │ │             │
   * │ │ │ • SSL Termination                                       │ │             │
   * │ │ │ • Request/Response Transformation                       │ │             │
   * │ │ │ • Caching                                                │ │             │
   * │ │ │ • Logging/Monitoring                                    │ │             │
   * │ │ │ • Circuit Breaker                                       │ │             │
   * │ │ └─────────────────────────────────────────────────────────┘ │             │
   * │ └─────────────────────────────────────────────────────────────┘             │
   * │           │              │              │                                   │
   * │           ▼              ▼              ▼                                   │
   * │     ┌─────────┐    ┌─────────┐    ┌─────────┐                               │
   * │     │ User    │    │ Order   │    │ Payment │                               │
   * │     │ Service │    │ Service │    │ Service │                               │
   * │     └─────────┘    └─────────┘    └─────────┘                               │
   * │                                                                             │
   * │ TOOLS: Kong, AWS API Gateway, Netflix Zuul, Spring Cloud Gateway            │
   * │                                                                             │
   * │ BFF (Backend For Frontend):                                                 │
   * │ Gateway riêng cho mỗi loại client (Web, Mobile, IoT)                        │
   * │ → Optimize response cho từng client                                         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DATA MANAGEMENT
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Database per Service pattern?                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PRINCIPLE: Mỗi service OWN data của nó                                      │
   * │                                                                             │
   * │ ┌─────────────┐    ┌─────────────┐    ┌─────────────┐                       │
   * │ │ User Service│    │Order Service│    │Product Svc  │                       │
   * │ └──────┬──────┘    └──────┬──────┘    └──────┬──────┘                       │
   * │        │                  │                  │                              │
   * │        ▼                  ▼                  ▼                              │
   * │   ┌─────────┐       ┌─────────┐       ┌─────────┐                           │
   * │   │ User DB │       │Order DB │       │Product  │                           │
   * │   │(Postgres)│       │ (MySQL) │       │(MongoDB)│                           │
   * │   └─────────┘       └─────────┘       └─────────┘                           │
   * │                                                                             │
   * │ BENEFITS:                                                                   │
   * │ ✓ Loose coupling                                                            │
   * │ ✓ Choose right DB for each service                                          │
   * │ ✓ Independent scaling                                                       │
   * │                                                                             │
   * │ CHALLENGES:                                                                 │
   * │ ✗ No JOINs across services                                                  │
   * │ ✗ Distributed transactions                                                  │
   * │ ✗ Data consistency                                                          │
   * │ ✗ Data duplication                                                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: CQRS (Command Query Responsibility Segregation)?                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PRINCIPLE: Tách WRITE (Command) và READ (Query) thành 2 models              │
   * │                                                                             │
   * │           ┌──────────────────────────────────────────┐                      │
   * │           │              WRITE SIDE                  │                      │
   * │           │  ┌─────────┐        ┌─────────────┐      │                      │
   * │ Commands ─┼─►│ Command │───────►│ Write Model │      │                      │
   * │           │  │ Handler │        │ (Normalized)│      │                      │
   * │           │  └─────────┘        └──────┬──────┘      │                      │
   * │           └────────────────────────────┼─────────────┘                      │
   * │                                        │ Events                             │
   * │                                        ▼                                    │
   * │           ┌──────────────────────────────────────────┐                      │
   * │           │              READ SIDE                   │                      │
   * │           │  ┌─────────────┐     ┌─────────────┐     │                      │
   * │           │  │ Read Model  │◄────│   Event     │     │                      │
   * │  Queries ─┼─►│(Denormalized)│     │  Projector  │     │                      │
   * │           │  └─────────────┘     └─────────────┘     │                      │
   * │           └──────────────────────────────────────────┘                      │
   * │                                                                             │
   * │ BENEFITS:                                                                   │
   * │ ✓ Optimize read/write independently                                         │
   * │ ✓ Scale read replicas                                                       │
   * │ ✓ Different query models for different use cases                            │
   * │                                                                             │
   * │ USE WHEN:                                                                   │
   * │ - Read/Write ratio very different                                           │
   * │ - Complex querying requirements                                             │
   * │ - Event Sourcing                                                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DISTRIBUTED TRANSACTIONS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Saga Pattern cho distributed transactions?                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PROBLEM: ACID transactions không work across services                       │
   * │                                                                             │
   * │ SAGA = Sequence of local transactions với compensating actions              │
   * │                                                                             │
   * │ EXAMPLE: Create Order                                                       │
   * │ T1: Order Service → Create order (PENDING)                                  │
   * │ T2: Inventory Service → Reserve items                                       │
   * │ T3: Payment Service → Charge payment                                        │
   * │ T4: Order Service → Confirm order (COMPLETED)                               │
   * │                                                                             │
   * │ If T3 fails:                                                                │
   * │ C2: Inventory Service → Release reserved items (compensate T2)              │
   * │ C1: Order Service → Cancel order (compensate T1)                            │
   * │                                                                             │
   * │ 1. CHOREOGRAPHY (Event-based):                                              │
   * │    ┌───────┐  OrderCreated  ┌───────────┐  ItemsReserved  ┌─────────┐       │
   * │    │ Order │───────────────►│ Inventory │─────────────────►│ Payment │       │
   * │    └───────┘                └───────────┘                  └─────────┘       │
   * │        ▲                         ▲                              │           │
   * │        └─────────────────────────┴──────────────────────────────┘           │
   * │                            Events (Kafka)                                   │
   * │    ✓ Loose coupling, ✗ Hard to track, ✗ Cyclic dependencies                 │
   * │                                                                             │
   * │ 2. ORCHESTRATION (Central coordinator):                                     │
   * │                     ┌──────────────┐                                        │
   * │                     │ Saga         │                                        │
   * │                     │ Orchestrator │                                        │
   * │                     └──────┬───────┘                                        │
   * │           ┌────────────────┼────────────────┐                               │
   * │           ▼                ▼                ▼                               │
   * │      ┌───────┐       ┌───────────┐    ┌─────────┐                           │
   * │      │ Order │       │ Inventory │    │ Payment │                           │
   * │      └───────┘       └───────────┘    └─────────┘                           │
   * │    ✓ Easy to track, ✓ Single point of control, ✗ Single point of failure    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Two-Phase Commit (2PC) vs Saga?                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 2PC (Two-Phase Commit):                                                     │
   * │ Phase 1: Coordinator asks all → "Can you commit?"                           │
   * │ Phase 2: If all YES → "Commit!" | If any NO → "Rollback!"                   │
   * │                                                                             │
   * │ ┌─────────────────┬─────────────────────┬─────────────────────┐             │
   * │ │                 │ 2PC                 │ Saga                │             │
   * │ ├─────────────────┼─────────────────────┼─────────────────────┤             │
   * │ │ Consistency     │ Strong (ACID)       │ Eventual            │             │
   * │ │ Availability    │ Lower (blocking)    │ Higher              │             │
   * │ │ Latency         │ Higher              │ Lower               │             │
   * │ │ Complexity      │ Simpler logic       │ Compensating logic  │             │
   * │ │ Failure         │ Coordinator SPOF    │ More resilient      │             │
   * │ │ Scale           │ Poor                │ Better              │             │
   * │ └─────────────────┴─────────────────────┴─────────────────────┘             │
   * │                                                                             │
   * │ RECOMMENDATION:                                                             │
   * │ - 2PC: Rarely used in microservices (blocking, SPOF)                        │
   * │ - Saga: Preferred for distributed systems                                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // RESILIENCE PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Circuit Breaker Pattern?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PROBLEM: Service B down → Service A keeps calling → cascading failure       │
   * │                                                                             │
   * │ STATES:                                                                     │
   * │           success                      failure threshold                    │
   * │    ┌───────────────┐              ┌───────────────┐                         │
   * │    │               │              │               │                         │
   * │    ▼               │              ▼               │                         │
   * │ ┌──────┐  success  │  failures  ┌──────┐         │    timeout             │
   * │ │CLOSED│───────────┘───────────►│ OPEN │─────────┼───────────────┐         │
   * │ └──────┘                         └──────┘         │               ▼         │
   * │    ▲                                              │          ┌─────────┐    │
   * │    │                                              │          │HALF-OPEN│    │
   * │    └──────────────────────────────────────────────┴──────────┴─────────┘    │
   * │                    success                              failure             │
   * │                                                                             │
   * │ CLOSED: Normal, requests go through                                         │
   * │ OPEN: Fail fast, không gọi downstream (return fallback)                     │
   * │ HALF-OPEN: Test với một số requests                                         │
   * │                                                                             │
   * │ IMPLEMENTATION (Resilience4j):                                              │
   * │ @CircuitBreaker(name = "userService", fallbackMethod = "fallback")          │
   * │ public User getUser(Long id) { return userClient.getUser(id); }             │
   * │                                                                             │
   * │ public User fallback(Long id, Exception ex) {                               │
   * │     return User.defaultUser();  // Cached or default                        │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Bulkhead Pattern?                                                        │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ CONCEPT: Tàu có nhiều ngăn (bulkhead) → 1 ngăn vỡ không chìm cả tàu         │
   * │                                                                             │
   * │ APPLICATION: Isolate resources cho mỗi downstream service                   │
   * │                                                                             │
   * │ ┌─────────────────────────────────────────────────────────────────┐         │
   * │ │                      Service A                                  │         │
   * │ │  ┌───────────────┐  ┌───────────────┐  ┌───────────────┐        │         │
   * │ │  │ Thread Pool   │  │ Thread Pool   │  │ Thread Pool   │        │         │
   * │ │  │ for Service B │  │ for Service C │  │ for Service D │        │         │
   * │ │  │ (10 threads)  │  │ (20 threads)  │  │ (5 threads)   │        │         │
   * │ │  └───────┬───────┘  └───────┬───────┘  └───────┬───────┘        │         │
   * │ └──────────┼──────────────────┼──────────────────┼────────────────┘         │
   * │            ▼                  ▼                  ▼                          │
   * │       ┌─────────┐        ┌─────────┐        ┌─────────┐                     │
   * │       │Service B│        │Service C│        │Service D│                     │
   * │       │ (slow)  │        │         │        │         │                     │
   * │       └─────────┘        └─────────┘        └─────────┘                     │
   * │                                                                             │
   * │ Service B slow → Chỉ exhaust 10 threads                                     │
   * │                → Service C, D vẫn hoạt động bình thường                     │
   * │                                                                             │
   * │ TYPES:                                                                      │
   * │ - Thread Pool Bulkhead: Separate thread pools                               │
   * │ - Semaphore Bulkhead: Limit concurrent calls                                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Retry & Timeout patterns?                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ RETRY:                                                                      │
   * │ @Retry(name = "userService", fallbackMethod = "fallback")                   │
   * │ - Max attempts: 3                                                           │
   * │ - Wait duration: 500ms                                                      │
   * │ - Exponential backoff: 500ms → 1s → 2s → 4s ...                             │
   * │ - Jitter: Add random delay để tránh thundering herd                         │
   * │                                                                             │
   * │ TIMEOUT:                                                                    │
   * │ @TimeLimiter(name = "userService")                                          │
   * │ - Không chờ vô hạn                                                          │
   * │ - Fail fast                                                                 │
   * │                                                                             │
   * │ COMBINED (Order matters!):                                                  │
   * │ Retry → CircuitBreaker → RateLimiter → TimeLimiter → Bulkhead → Call        │
   * │                                                                             │
   * │ IMPORTANT: Retry chỉ an toàn với IDEMPOTENT operations!                     │
   * │ GET, PUT, DELETE: ✓ Safe to retry                                           │
   * │ POST (create): ✗ May create duplicates → Use idempotency key                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // OBSERVABILITY (Advanced)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: 3 Pillars of Observability?                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. LOGS:                                                                    │
   * │    - What happened at a specific point                                      │
   * │    - Structured logging (JSON)                                              │
   * │    - Centralized: ELK (Elasticsearch, Logstash, Kibana), Loki               │
   * │    - Correlation ID để trace across services                                │
   * │                                                                             │
   * │ 2. METRICS:                                                                 │
   * │    - Numeric measurements over time                                         │
   * │    - Counters (requests), Gauges (memory), Histograms (latency)             │
   * │    - Tools: Prometheus + Grafana                                            │
   * │    - RED: Rate, Errors, Duration                                            │
   * │    - USE: Utilization, Saturation, Errors                                   │
   * │                                                                             │
   * │ 3. TRACES:                                                                  │
   * │    - Request flow across services                                           │
   * │    - Tools: Jaeger, Zipkin, OpenTelemetry                                   │
   * │    - Span: Unit of work (method call)                                       │
   * │    - Trace: Collection of spans (full request)                              │
   * │                                                                             │
   * │ ┌─────────────────────────────────────────────────────────────────┐         │
   * │ │ Trace ID: abc123                                               │         │
   * │ │ ┌─────────────────────────────────────────────────────────────┐│         │
   * │ │ │ Span: API Gateway (50ms)                                    ││         │
   * │ │ │ ┌───────────────────────────────────────────────────────┐   ││         │
   * │ │ │ │ Span: Order Service (40ms)                            │   ││         │
   * │ │ │ │ ┌─────────────────────┐ ┌─────────────────────┐       │   ││         │
   * │ │ │ │ │Span: User Svc (10ms)│ │Span: Payment (20ms) │       │   ││         │
   * │ │ │ │ └─────────────────────┘ └─────────────────────┘       │   ││         │
   * │ │ │ └───────────────────────────────────────────────────────┘   ││         │
   * │ │ └─────────────────────────────────────────────────────────────┘│         │
   * │ └─────────────────────────────────────────────────────────────────┘         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Distributed Tracing - Context Propagation?                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PROBLEM: Làm sao trace request xuyên suốt nhiều services?                   │
   * │                                                                             │
   * │ SOLUTION: Propagate trace context qua headers                               │
   * │                                                                             │
   * │ HTTP Headers:                                                               │
   * │ traceparent: 00-abc123-def456-01                                            │
   * │ tracestate: vendor=info                                                     │
   * │                                                                             │
   * │ B3 Format (Zipkin):                                                         │
   * │ X-B3-TraceId: abc123                                                        │
   * │ X-B3-SpanId: def456                                                         │
   * │ X-B3-ParentSpanId: parent123                                                │
   * │ X-B3-Sampled: 1                                                             │
   * │                                                                             │
   * │ ASYNC (Kafka/RabbitMQ):                                                     │
   * │ - Inject trace context vào message headers                                  │
   * │ - Consumer extract và continue trace                                        │
   * │                                                                             │
   * │ THREAD POOLS:                                                               │
   * │ - Context lost khi switch threads!                                          │
   * │ - Use: Context-aware executors, @Async with propagation                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // ADVANCED PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Strangler Fig Pattern (Migration)?                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ USE CASE: Migrate monolith → microservices incrementally                    │
   * │                                                                             │
   * │ STEP 1: Facade                                                              │
   * │ ┌───────┐       ┌──────────┐       ┌──────────┐                             │
   * │ │Client │──────►│ Facade   │──────►│ Monolith │                             │
   * │ └───────┘       │ (all)    │       │          │                             │
   * │                 └──────────┘       └──────────┘                             │
   * │                                                                             │
   * │ STEP 2: Extract & Route                                                     │
   * │ ┌───────┐       ┌──────────┐       ┌──────────┐                             │
   * │ │Client │──────►│ Facade   │──────►│ Monolith │                             │
   * │ └───────┘       │          │       │ (legacy) │                             │
   * │                 │          │       └──────────┘                             │
   * │                 │          │                                                │
   * │                 │          │       ┌──────────┐                             │
   * │                 │          │──────►│ New      │                             │
   * │                 └──────────┘       │ Service  │                             │
   * │                                    └──────────┘                             │
   * │                                                                             │
   * │ STEP 3: Complete Migration                                                  │
   * │ Route tất cả traffic ra new services → shutdown monolith                    │
   * │                                                                             │
   * │ BENEFITS:                                                                   │
   * │ ✓ Low risk (gradual)                                                        │
   * │ ✓ Rollback easy                                                             │
   * │ ✓ Team learns incrementally                                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Sidecar Pattern?                                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ CONCEPT: Deploy helper container alongside main container                   │
   * │                                                                             │
   * │ ┌─────────────────────────────────────────────────────────────┐             │
   * │ │ Pod                                                         │             │
   * │ │ ┌─────────────────────┐    ┌─────────────────────┐          │             │
   * │ │ │   Main Container    │◄──►│   Sidecar Container │          │             │
   * │ │ │   (Your Service)    │    │   (Istio Envoy,     │          │             │
   * │ │ │                     │    │    Logging Agent,   │          │             │
   * │ │ │                     │    │    Config Sync)     │          │             │
   * │ │ └─────────────────────┘    └─────────────────────┘          │             │
   * │ │                                                             │             │
   * │ │                    Shared localhost                         │             │
   * │ └─────────────────────────────────────────────────────────────┘             │
   * │                                                                             │
   * │ USE CASES:                                                                  │
   * │ - Service Mesh (Istio Envoy): mTLS, traffic management                      │
   * │ - Logging (Fluentd): collect và ship logs                                   │
   * │ - Configuration: sync secrets from Vault                                    │
   * │ - Monitoring: expose metrics                                                │
   * │                                                                             │
   * │ BENEFITS:                                                                   │
   * │ ✓ Separation of concerns                                                    │
   * │ ✓ Language agnostic (sidecar is separate process)                           │
   * │ ✓ Independent lifecycle                                                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Service Mesh là gì? Istio?                                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PROBLEM: Cross-cutting concerns (security, observability, traffic)          │
   * │ → Đừng implement ở mỗi service!                                             │
   * │                                                                             │
   * │ SOLUTION: Infrastructure layer handles it                                   │
   * │                                                                             │
   * │ ┌─────────────────────────────────────────────────────────────────┐         │
   * │ │                    CONTROL PLANE (Istiod)                      │         │
   * │ │  ┌────────────┐  ┌────────────┐  ┌────────────┐                │         │
   * │ │  │   Config   │  │   Service  │  │ Certificate│                │         │
   * │ │  │ Management │  │  Discovery │  │  Authority │                │         │
   * │ │  └────────────┘  └────────────┘  └────────────┘                │         │
   * │ └─────────────────────────────────────────────────────────────────┘         │
   * │                              │                                              │
   * │            ┌─────────────────┼─────────────────┐                            │
   * │            ▼                 ▼                 ▼                            │
   * │ ┌────────────────┐ ┌────────────────┐ ┌────────────────┐                    │
   * │ │┌──────┐┌──────┐│ │┌──────┐┌──────┐│ │┌──────┐┌──────┐│                    │
   * │ ││Svc A ││Envoy ││ ││Svc B ││Envoy ││ ││Svc C ││Envoy ││                    │
   * │ │└──────┘└──────┘│ │└──────┘└──────┘│ │└──────┘└──────┘│                    │
   * │ └────────────────┘ └────────────────┘ └────────────────┘                    │
   * │        DATA PLANE (Service-to-service communication)                        │
   * │                                                                             │
   * │ ISTIO FEATURES:                                                             │
   * │ - mTLS: Automatic encryption between services                               │
   * │ - Traffic Management: Canary, A/B testing, traffic shifting                 │
   * │ - Observability: Metrics, traces, logs (no code change)                     │
   * │ - Fault Injection: Test resilience                                          │
   * │ - Rate Limiting, Retries, Timeouts                                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // HIDDEN GOTCHAS & INTERVIEW TRAPS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Distributed Monolith là gì? Làm sao tránh?                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ DEFINITION: Microservices architecture nhưng vẫn tight coupling             │
   * │ → Worst of both worlds: complexity + dependencies                           │
   * │                                                                             │
   * │ SYMPTOMS:                                                                   │
   * │ ✗ Deploy 1 service → phải deploy nhiều services khác                        │
   * │ ✗ Shared database                                                           │
   * │ ✗ Synchronous chains: A → B → C → D → E                                     │
   * │ ✗ Shared libraries with business logic                                      │
   * │ ✗ Chatty services (nhiều calls cho 1 operation)                             │
   * │                                                                             │
   * │ HOW TO AVOID:                                                               │
   * │ ✓ Database per service                                                      │
   * │ ✓ Async communication where possible                                        │
   * │ ✓ Contract testing (Pact)                                                   │
   * │ ✓ Independent deployability as goal                                         │
   * │ ✓ Domain-Driven Design (proper boundaries)                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Dual Write Problem?                                                      │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PROBLEM: Write to DB và publish event → không atomic                        │
   * │                                                                             │
   * │ @Transactional                                                              │
   * │ void createOrder(Order order) {                                             │
   * │     orderRepo.save(order);        // ✓ Success                              │
   * │     kafka.publish(orderCreated);  // ✗ Fail → DB has data, no event!        │
   * │ }                                                                           │
   * │                                                                             │
   * │ HOẶC:                                                                       │
   * │ void createOrder(Order order) {                                             │
   * │     orderRepo.save(order);        // ✓ Success                              │
   * │     kafka.publish(orderCreated);  // ✓ Success                              │
   * │     // TX rollback               // DB rollback, but event already sent!    │
   * │ }                                                                           │
   * │                                                                             │
   * │ SOLUTIONS:                                                                  │
   * │                                                                             │
   * │ 1. TRANSACTIONAL OUTBOX:                                                    │
   * │    ┌─────────────────────────────────────────────────────────────┐          │
   * │    │ @Transactional                                              │          │
   * │    │ void createOrder() {                                        │          │
   * │    │     orderRepo.save(order);                                  │          │
   * │    │     outboxRepo.save(new OutboxEvent(orderCreated));         │          │
   * │    │ }  // Single TX, atomic!                                    │          │
   * │    │                                                             │          │
   * │    │ Separate process: Poll outbox → Publish → Mark as sent      │          │
   * │    └─────────────────────────────────────────────────────────────┘          │
   * │                                                                             │
   * │ 2. CDC (Change Data Capture):                                               │
   * │    Debezium reads database log → publishes events                           │
   * │    No application code change, but extra infrastructure                     │
   * │                                                                             │
   * │ 3. Event Sourcing:                                                          │
   * │    Events ARE the source of truth, no separate DB write                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Thundering Herd Problem?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ SCENARIO 1: Cache Stampede                                                  │
   * │ - Popular cache key expires                                                 │
   * │ - 1000 requests hit simultaneously                                          │
   * │ - All 1000 requests query database                                          │
   * │                                                                             │
   * │ SOLUTION:                                                                   │
   * │ - Locking: Only 1 request rebuilds cache                                    │
   * │ - Probabilistic expiry: Refresh before actual expiry                        │
   * │ - Background refresh: Never actually expire                                 │
   * │                                                                             │
   * │ SCENARIO 2: Service Recovery                                                │
   * │ - Service B goes down                                                       │
   * │ - 1000 requests queued in Service A                                         │
   * │ - Service B comes back up                                                   │
   * │ - All 1000 requests hit Service B at once → crash again!                    │
   * │                                                                             │
   * │ SOLUTION:                                                                   │
   * │ - Exponential backoff with jitter                                           │
   * │ - Circuit breaker half-open lets only few requests                          │
   * │ - Rate limiting on recovery                                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Version Compatibility trong Microservices?                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PROBLEM: Service A v1 calls Service B v2 → incompatible!                    │
   * │                                                                             │
   * │ STRATEGIES:                                                                 │
   * │                                                                             │
   * │ 1. SEMANTIC VERSIONING + API VERSIONING                                     │
   * │    /api/v1/users                                                            │
   * │    /api/v2/users                                                            │
   * │    Run multiple versions simultaneously                                     │
   * │                                                                             │
   * │ 2. BACKWARD COMPATIBLE CHANGES                                              │
   * │    ✓ Add new fields (with defaults)                                         │
   * │    ✓ Add new endpoints                                                      │
   * │    ✗ Rename fields                                                          │
   * │    ✗ Remove required fields                                                 │
   * │    ✗ Change field types                                                     │
   * │                                                                             │
   * │ 3. CONSUMER-DRIVEN CONTRACTS (Pact)                                         │
   * │    Consumers define expected contract                                       │
   * │    Provider tests against contracts                                         │
   * │    Break caught before deployment                                           │
   * │                                                                             │
   * │ 4. EXPAND-CONTRACT PATTERN                                                  │
   * │    Step 1: Add new field (keep old)                                         │
   * │    Step 2: Migrate all consumers                                            │
   * │    Step 3: Remove old field                                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Service Startup Dependencies?                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PROBLEM: Service A depends on B, C, D → startup order?                      │
   * │                                                                             │
   * │ ANTI-PATTERN:                                                               │
   * │ ✗ Hardcode startup order (fragile)                                          │
   * │ ✗ Wait for dependencies forever (hangs)                                     │
   * │                                                                             │
   * │ SOLUTIONS:                                                                  │
   * │                                                                             │
   * │ 1. HEALTH CHECKS + RETRY                                                    │
   * │    - Spring Boot Actuator /health                                           │
   * │    - Kubernetes readinessProbe                                              │
   * │    - Retry with backoff if dependency not ready                             │
   * │                                                                             │
   * │ 2. GRACEFUL DEGRADATION                                                     │
   * │    - Start without full functionality                                       │
   * │    - Enable features as dependencies come online                            │
   * │                                                                             │
   * │ 3. LAZY INITIALIZATION                                                      │
   * │    - Don't connect until first request                                      │
   * │    - Circuit breaker for failing dependencies                               │
   * │                                                                             │
   * │ 4. KUBERNETES INIT CONTAINERS                                               │
   * │    initContainers:                                                          │
   * │    - name: wait-for-db                                                      │
   * │      command: ['sh', '-c', 'until nc -z db 5432; do sleep 1; done']         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Data Consistency với Eventual Consistency?                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ SCENARIO: User changes email                                                │
   * │ - User Service updates email                                                │
   * │ - Publishes EmailChanged event                                              │
   * │ - Order Service receives event (eventual)                                   │
   * │                                                                             │
   * │ PROBLEM: Between event publish và receive:                                  │
   * │ - Query User Service: new email                                             │
   * │ - Query Order Service: old email                                            │
   * │ - Inconsistent!                                                             │
   * │                                                                             │
   * │ SOLUTIONS:                                                                  │
   * │                                                                             │
   * │ 1. UI HANDLES INCONSISTENCY                                                 │
   * │    - Show "Updating..." status                                              │
   * │    - Optimistic UI updates                                                  │
   * │                                                                             │
   * │ 2. READ YOUR OWN WRITES                                                     │
   * │    - Query source of truth for recent changes                               │
   * │    - Cache invalidation after write                                         │
   * │                                                                             │
   * │ 3. EVENT VERSION / TIMESTAMP                                                │
   * │    - Ignore out-of-order/stale events                                       │
   * │    - Last-write-wins with version check                                     │
   * │                                                                             │
   * │ 4. SAGA WITH CONFIRMATION                                                   │
   * │    - Request → Reserve → Confirm pattern                                    │
   * │    - Two-phase commit at application level                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
