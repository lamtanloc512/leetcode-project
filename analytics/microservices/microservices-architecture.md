# 🌐 Microservices System Design: Systematic Component Deep Dive

Tài liệu này hệ thống hóa các thành phần (components) cốt lõi của Microservices, phân tích sâu về kỹ thuật, các lựa chọn thay thế và chiến lược vận hành nâng cao.

---

## 🏗️ 1. Core Architectural Components

Lựa chọn thành phần đúng quyết định đến 80% sự ổn định và scalability của hệ thống.

### 1.1. Ingress & Traffic Management

#### Load Balancer (LB)
| Type | OSI Layer | Technology | Key Problems Solved | Why Trending? | Pros/Cons |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **L4 (Network LB)** | Layer 4 (TCP/UDP) | HAProxy, AWS NLB, F5 | Phân phối traffic dựa trên IP/Port. Cần throughput cực cao. | Tối ưu cho traffic "nặng" (video streaming, gaming). | **(+)** High performance, low latency. **(-)** Không hiểu nội dung bên trong (HTTP header/cookie). |
| **L7 (Application LB)** | Layer 7 (HTTP/HTTPS) | Nginx, AWS ALB, Traefik | Phân phối dựa trên URL path, Headers, Cookies. Hỗ trợ Sticky session. | "Thông minh", phù hợp cho Microservices định tuyến theo service name. | **(+)** Flexible routing, TLS termination. **(-)** Tốn tài nguyên CPU hơn L4. |

#### API Gateway
| Product | Language | Purpose | Why Trending? | Implementation Note |
| :--- | :--- | :--- | :--- | :--- |
| **Kong** | Lua (Nginx) | High performance, plugin ecosystem. | Plugin phong phú (Auth, Rate limiting, Logging). Cực nhanh. | Dùng cho hệ thống yêu cầu độ trễ cực thấp. |
| **Spring Cloud Gateway** | Java (Netty) | Native integration với Java/Spring ecosystem. | Dev Java dễ viết filter, dễ cấu hình qua code hoặc properties. | Trending trong các Java shop vì cùng stack. |
| **Istio Ingress** | Go (Envoy) | Kết hợp với Service Mesh. | Quản lý traffic đồng bộ từ entry point đến inter-service communication. | Trending trong hệ thống K8s-native phức tạp. |

---

### 1.2. Communication Protocols & Serialization

Cách các service "nói chuyện" với nhau ảnh hưởng trực tiếp đến băng thông và tốc độ xử lý.

#### Comparison of Protocols
| Protocol | Transport | Model | Key Features | Best Case |
| :--- | :--- | :--- | :--- | :--- |
| **REST** | HTTP/1.1 | Request/Response | Dễ dùng, Human-readable, Stateless. | Public APIs, Browser-to-Server. |
| **gRPC** | HTTP/2 | Bi-directional Streaming | Binary (Protobuf), Strong typing, Multiplexing. | Inter-service communication (Internal). |
| **GraphQL** | HTTP/1.1/2 | Query-based | Client lấy đúng data cần thiết, gom nhiều request thành 1. | Mobile apps, Front-end BFF (Backend for Frontend). |
| **Apache Thrift** | TCP | Binary | Support đa ngôn ngữ cực mạnh. Khởi đầu từ Facebook. | Legacy systems, high-performance rpc. |

#### Serialization Formats (Data on the Wires)
| Format | Type | Size | Performance | Why use it? |
| :--- | :--- | :--- | :--- | :--- |
| **JSON** | Text | Large | Slow (Parsing cost) | Human readable, dễ debug, tiêu chuẩn web. |
| **Protobuf** | Binary | Very Small | Fast | Strong schema, backward compatibility, tối ưu network. |
| **Avro** | Binary | Small | Fast | Schema gắn liền với data (hợp với Kafka/Analytics). |

---

### 1.3. High-Performance Storage Formats

Khi làm việc với Big Data hoặc Analytics trong Microservices, cách lưu trữ file quyết định tốc độ query.

| Format | Structure | Fast because... | Technology Stack |
| :--- | :--- | :--- | :--- |
| **Parquet** | Columnar | **Projection pushdown**: Chỉ đọc cột cần thiết. **Compression**: Cột chứa data giống nhau nén cực tốt. | Hadoop, Spark, AWS Athena. |
| **ORC** | Columnar | Tương tự Parquet nhưng tối ưu hơn cho hệ sinh thái Hive. | Hadoop, Hive, Presto. |
| **Avro (File)** | Row-based | Ghi dữ liệu cực nhanh (Append-only). | Kafka Storage, ETL landing zone. |
| **HDF5** | Hierarchical | Lưu trữ mảng dữ liệu khổng lồ (Multi-dimensional). | AI/ML, Scientific Research. |

---

### 1.4. State, Storage & Communication

#### Message Queues / Event Streaming
| Tech | Model | Best For | Why it's Trending? | Problem it Solves |
| :--- | :--- | :--- | :--- | :--- |
| **Kafka** | Pull-based (Log) | High throughput, Event Sourcing, Analytics. | Khả năng replay message và scale hàng triệu msg/s. | decoupled services, log aggregation, real-time data processing. |
| **RabbitMQ** | Push-based | Task Queues, Complex routing (AMQP). | Đơn vị định tuyến (Exchange) cực mạnh, dễ sử dụng. | Reliable asynchronous tasks, protocol translation. |
| **Redis Pub/Sub** | Memory-based | Real-time chat, Notifications (low reliability). | Tốc độ cực nhanh vì nằm hoàn toàn trên RAM. | Giao tiếp real-time tức thời không cần lưu trữ lâu dài. |

---

### 1.5. Messaging Resilience & Error Handling

Xử lý lỗi trong Message Queue là yếu tố then chốt để đảm bảo **Eventual Consistency**.

#### Error Handling Strategies
| Technique | Flow | Best Case | Why use it? |
| :--- | :--- | :--- | :--- |
| **Immediate Retry** | Thử lại ngay lập tức N lần. | Network gián đoạn cực ngắn. | Đơn giản, giải quyết được các lỗi "glitch" nhất thời. |
| **Exponential Backoff** | Thử lại với thời gian chờ tăng dần (2s, 4s, 8s...). | Service đích bị quá tải (Overload). | Tránh hiện tượng "Retry Storm" làm sập thêm service đang yếu. |
| **Backoff with Jitter** | Thử lại với thời gian chờ ngẫu nhiên (Randomness). | Hệ thống lớn, nhiều consumer đồng loạt retry. | Phân tán tải, tránh các consumer "đánh" vào server cùng một lúc. |
| **Dead Letter Queue (DLQ)** | Move message lỗi vào một Queue riêng sau khi hết lượt retry. | Lỗi logic code, Data sai định dạng. | Cô lập message lỗi để Dev có thể trace và sửa mà không làm nghẽn luồng chính. |

#### DLQ Lifecycle & Traceability
| Phase | Action | How to Trace? |
| :--- | :--- | :--- |
| **1. Detection** | Consumer ném Exception sau 3 lần retry. | Log `message_id` và `error_message`. |
| **2. Routing** | MQ Broker tự chuyển message vào `ORDER_RETRY_DLQ`. | Metadata của message chứa `x-death` header (lý do lỗi, số lần thử). |
| **3. Auditing** | Dev/SRE dùng Tool (Kibana/Loki) search `trace_id` của message lỗi. | Trích xuất `original_payload` để tái hiện lỗi ở local. |
| **4. Reprocessing** | Sau khi fix bug, dùng tool đẩy ngược message từ DLQ về Main Queue. | Đảm bảo không mất dữ liệu của khách hàng. |

---

#### Database Systems (The Right Choice)
| Type | Tech | Why choosing this? | trending Reason | Problem Solved |
| :--- | :--- | :--- | :--- | :--- |
| **Relational (RDBMS)** | PostgreSQL, MySQL | ACID compliance, Strong consistency. | Postgres ngày càng mạnh với JSONB, Extensions (PostGIS, TimescaleDB). | Dữ liệu giao chuyển (Finance), quan hệ phức tạp. |
| **Document (NoSQL)** | MongoDB | Schema-less, High write throughput. | Flexible schema giúp dev agile hơn, dễ scale ngang. | Hồ sơ người dùng, Content Management, dữ liệu thay đổi model liên tục. |
| **Key-Value** | Redis, DynamoDB | Sub-millisecond latency. | Tốc độ là ưu tiên hàng đầu. | Caching, Session management, Leaderboards. |
| **NewSQL** | TiDB, CockroachDB | Distributed SQL, ACID + Horizontal Scaling. | Kết hợp cả 2 thế giới: Sự tin cậy của SQL và khả năng scale của NoSQL. | Hệ thống Banking/Global apps cần scale DB ngang mà không cần sharding thủ công. |

---

## 🏗️ 2. Distributed Consistency & Reliability

Trong hệ thống phân tán, đảm bảo tính đúng đắn của dữ liệu là bài toán khó nhất.

### 2.1. Message Ordering & Idempotency
| Problem | Solution | Implementation | Why it works? |
| :--- | :--- | :--- | :--- |
| **Message Ordering** | Partition Key | Dùng `order_id` làm Key trong Kafka. | Đảm bảo mọi event của 1 đơn hàng vào cùng 1 partition. |
| **Duplicate Process** | Idempotency Key | Lưu UUID trong Redis/DB (TTL 24h). | Chặn xử lý nếu Key đã tồn tại. |
| **Concurrency Update** | Optimistic Locking | `UPDATE ... SET version = version + 1 WHERE version = current_v`. | Tránh ghi đè dữ liệu cũ (Lost Update). |
| **Network Failure** | Consumer Offset | Commmit offset sau khi xử lý thành công. | Đảm bảo "At-least-once" delivery. |

### 2.2. Distributed Transactions: Saga Pattern
| Type | Coordinator | Communication | Pros | Cons |
| :--- | :--- | :--- | :--- |
| **Choreography** | None (Decentralized) | Events / Pub-Sub | Dễ setup, không có Single point of failure. | Khó debug, khó visualize toàn bộ luồng. |
| **Orchestration** | Centralized (Saga Mgr) | Command / Response | Dễ quản lý logic phức tạp, dễ trace lỗi. | Coordinator có thể trở thành bottleneck. |

### 2.3. Payment State Machine Design
| State | Meaning | Trigger Action | Next Possible States |
| :--- | :--- | :--- | :--- |
| **INITIATED** | Giao dịch vừa tạo | Gọi Payment Gateway API | PENDING, FAILED |
| **PENDING** | Đang chờ xử lý (3DS, OTP) | Chờ Webhook/Callback | AUTHORIZED, FAILED |
| **AUTHORIZED** | Đã giữ tiền (Hold) | Merchant xác nhận giao hàng | CAPTURED, VOIDED |
| **CAPTURED** | Tiền đã về tài khoản | Hoàn tất đơn hàng | REFUNDED |
| **FAILED** | Giao dịch thất bại | Alert hoặc retry | N/A |

---

## 🏗️ 3. Search Engine Deep Dive: Elasticsearch

Elasticsearch (ES) không chỉ là storage, nó là một "Distributed Search & Analytics Engine" cực kỳ mạnh mẽ.

### 3.1. Tại sao Elasticsearch nhanh? (The "Magic" Internals)
Sự khác biệt lớn nhất giữa RDBMS (B-Tree) và ES là **Inverted Index** (Chỉ mục đảo ngược).

| Concept | Mechanism | Why it's Fast? |
| :--- | :--- | :--- |
| **Inverted Index** | Tách text thành các "tokens" và lưu vị trí của chúng. | Thay vì quét từng dòng (Full scan), ES chỉ cần look up token để biết nó nằm ở document nào. |
| **Lucene Segments** | Dữ liệu được ghi vào các Segments bất biến trên disk. | Tránh tranh chấp lock khi read/write (Concurrency cực tốt). |
| **FS Cache** | ES dùng bộ nhớ đệm của OS để cache các segment. | Tốc độ truy xuất I/O gần như instant. |
| **Sharding** | Chia index thành nhiều phần nhỏ nằm trên nhiều Node. | Query được thực hiện song song trên tất cả các Shards (Parallel processing). |

### 3.2. Elasticsearch Architecture
- **Nodes & Cluster**: Một cluster gồm nhiều node. Có node chuyên làm Master (quản lý meta), node chuyên làm Data (lưu data).
- **Shards & Replicas**:
    - **Primary Shard**: Nơi write data.
    - **Replica Shard**: Bản sao để read và dự phòng (High Availability). Nếu 1 node chết, Replica sẽ lên làm Primary.

### 3.3. Elasticsearch in Java Implementation
| Approach | Technology | Key Features | Recommendation |
| :--- | :--- | :--- | :--- |
| **Low-Level Client** | `RestLowLevelClient` | HTTP client thuần, dùng JSON string. | Không nên dùng (trừ khi cần cực kỳ tối ưu). |
| **Spring Data ES** | `ElasticsearchRepository` | Giống Spring Data JPA, dùng Query Derby hoặc Annotation. | **Khuyên dùng** cho app Spring Boot để dev nhanh. |
| **Java API Client** | `ElasticsearchClient` | Fluent API, hỗ trợ Lambda, Type-safe. Thay thế cho High Level cũ. | Dùng cho các logic phức tạp mà Spring Data không hỗ trợ hết. |

#### Java Implementation Sample (Spring Data)
```java
@Document(indexName = "products")
public class Product {
    @Id private String id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word") // Analyzer tách chữ
    private String name;
}

// Service
public List<Product> search(String keyword) {
    NativeQuery query = NativeQuery.builder()
        .withQuery(q -> q.match(m -> m.field("name").query(keyword)))
        .build();
    return elasticsearchTemplate.search(query, Product.class);
}
```

---

## 🏗️ 4. Microservices Security & Secret Management

Trong Microservices, Security không phải là một bức tường, mà là một hệ thống "Zero Trust".

### 4.1. Authentication vs. Authorization (AuthN vs AuthZ)
| Concept | Method | Implementation | Context |
| :--- | :--- | :--- | :--- |
| **AuthN (Identity)** | **OIDC (OpenID Connect)** | Layer trên OAuth2 để nhận diện "Ai là người đang login?". | Dùng ID Token (JWT). |
| **AuthZ (Permission)** | **OAuth2** | Flow cấp quyền: Client Credentials (m2m), Authorization Code (User). | Dùng Access Token. |
| **State Management** | **JWT (Stateless)** | Server không lưu session, verify token bằng Public Key. | Scalable cho hệ thống hàng nghìn pod. |

### 4.2. Khử tập trung hóa Security (Patterns)
- **Global Entry (Gateway)**: API Gateway check token lần đầu (Global Auth).
- **Internal Security (Service Mesh)**: Dùng **mTLS** (Mutual TLS) để mã hóa data truyền giữa service A và B. Cho dù hacker vào được mạng nội bộ cũng không "nghe lén" được.

### 4.3. Secret Management (Lưu trữ Key/Secret)
Không bao giờ commit password/key vào Git.

| Technology | Best For | Architecture | Why it's Professional? |
| :--- | :--- | :--- | :--- |
| **K8s Secrets** | Ứng dụng nhỏ chạy K8s. | Lưu as base64 trong ETCD. | Dễ dùng nhưng kém bảo mật nhất (chưa mã hóa mặc định). |
| **HashiCorp Vault** | Doanh nghiệp lớn (Enterprises). | Centralized, hỗ trợ Dynamic Secrets (sinh mật khẩu dùng 1 lần). | Bảo mật cực cao, hỗ trợ Audit log, Key rotation. |
| **AWS/GCP KMS** | Hệ thống Cloud-native. | Dùng dịch vụ quản lý key của Cloud provider. | Tích hợp sâu với IAM, quản lý permission cực chặt chẽ. |

#### Implementation in Java (Spring Cloud Vault)
```java
@Configuration
@ConfigurationProperties("my.secret")
public class AppConfig {
    private String apiKey; // Spring Cloud Vault tự động inject từ Vault server
}
```

---

## 🛠️ 5. Implementation: How-to & Patterns

### 3.1. Implementing Distributed Monitoring (OpenTelemetry)
| Layer | Component | Function | Status |
| :--- | :--- | :--- | :--- |
| **SDK** | OpenTelemetry SDK | Tự động inject `trace_id` vào header. | Trending/Standard |
| **Collector** | OTEL Collector | Gom metrics/traces từ nhiều source đẩy về storage. | Scalable |
| **Storage** | Jaeger / Tempo | Lưu trữ và visualize vết đi của request. | Essential for Debug |
| **Metrics** | Prometheus | Theo dõi thông số tài nguyên (CPU, RAM). | Standard |

### 3.2. Implementing Circuit Breaker (Resilience4j)
| Logic | Config Value | Description |
| :--- | :--- | :--- |
| **Failure Threshold** | `failureRateThreshold: 50` | Sập nếu 50% request lỗi trong 1 cửa sổ thời gian. |
| **Wait Duration** | `waitDurationInOpenState: 10s` | Thời gian "nghỉ" trước khi thử lại. |
| **Half-Open Calls** | `permittedCalls: 10` | Số request thử nghiệm để quyết định đóng/mở lại. |
| **Fallback** | `fallbackMethod` | Phương án dự phòng khi service lỗi (trả về cache/default). |

#### Sample Code Structure
```java
@CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
public PaymentResponse process(Order order) {
    return restTemplate.postForObject("/pay", order, PaymentResponse.class);
}

public PaymentResponse paymentFallback(Order order, Throwable t) {
    return new PaymentResponse("FAILED", "Hệ thống bận, vui lòng thử lại");
}
```

### 3.3. Distributed Debugging & Troubleshooting
| Problem | Technique | Tools | Why it works? |
| :--- | :--- | :--- | :--- |
| **Tracing logic qua services** | Correlation ID (CID) | Spring Cloud Sleuth/OTEL | Gắn 1 ID duy nhất vào log của mọi service liên quan đến user request. |
| **Debug service trong K8s** | Remote Debugging | Telepresence, Bridge to K8s | Chặn traffic từ K8s cluster chuyển về application đang chạy ở máy Local. |
| **Hiểu luồng dữ liệu** | Service Graph | Kiali (Istio), Jaeger Graph | Visualize trực quan service nào đang gọi service nào và latency tương ứng. |
| **Local Environment** | Mocking / Virtualization | Microcks, WireMock | Giả lập các service phụ thuộc để test logic service chính. |

### 3.4. Logging & Monitoring Infrastructure (Setup)

#### Architecture Stacks
| Components | ELK Stack (Standard) | PLG Stack (Modern) | Key Difference |
| :--- | :--- | :--- | :--- |
| **Log Agent** | Logstash/Filebeat | Promtail | Promtail siêu nhẹ, lấy label trực tiếp từ K8s. |
| **Log Storage** | Elasticsearch | Grafana Loki | Loki không index full-text mà chỉ index label -> Tiết kiệm storage cực lớn. |
| **Visualization** | Kibana | Grafana | Grafana mạnh mẽ hơn trong việc kết hợp cả Log và Metric. |

#### Installation Patterns (K8s)
| Method | Implementation | Pros | Cons |
| :--- | :--- | :--- | :--- |
| **DaemonSet** | 1 Agent chạy trên mỗi node. | Tiết kiệm tài nguyên, tự động thu thập cho mọi Container. | Khó tùy chỉnh riêng cho từng service. |
| **Sidecar Pattern** | 1 Agent chạy trong từng Pod. | Có thể biến đổi log riêng cho service đó. | Tốn tài nguyên CPU/RAM hơn. |
| **Library Push** | App đẩy log trực tiếp qua TCP. | Không phụ thuộc infra K8s. | Có thể làm chậm app nếu log server treo. |

#### Prometheus Monitoring Workflow
1. **Instrument**: Service expose endpoint `/metrics` (dùng Micrometer).
2. **Scrape**: Prometheus server "pull" metric theo chu kỳ (30s).
3. **Alert**: Nếu `p99 > 2s` -> Alertmanager bắn tin Telegram/Slack cho team On-call.

---

## 📊 6. Advanced Observability: The Truth of Percentiles

Tại sao "Average Latency" (Độ trễ trung bình) là một cái bẫy? Nếu 99 người thấy nhanh (1ms) nhưng 1 người thấy chậm (10s), trung bình là ~100ms. 100ms trông có vẻ tốt, nhưng thực tế 1% khách hàng đã bỏ đi.

### 2.1. Understanding Latency Percentiles
- **p50 (Median)**: 50% số request nhanh hơn giá trị này. Đại diện cho "trình trạng bình thường".
- **p90**: 90% nhanh hơn giá trị này. Chỉ có 10% request gặp vấn đề nhỏ.
- **p95 / p99**: **Cực kỳ quan trọng**. Đây là "Long Tail". 1% request chậm nhất có thể làm treo toàn bộ hệ thống (do cascading failure). p99 đại diện cho trải nghiệm của khách hàng khó tính nhất hoặc khi hệ thống bắt đầu quá tải.

**Tại sao cần theo dõi p99?**
1. **Detect Heavier Operations**: Một số request đặc biệt (nhiều dữ liệu) thường nằm ở p99.
2. **Stop Cascading Failure**: Một service chậm ở p99 có thể chiếm dụng toàn bộ thread pool, khiến service "vàng" (Healthy) cũng bị sập theo.
3. **SLA Monitoring**: Hầu hết các cam kết chất lượng dịch vụ (SLA) đều dựa trên p95 hoặc p99.

---

## 🚨 7. Professional On-call Techniques

On-call không chỉ là "trực đêm", đó là quy trình kỹ thuật để duy trì 99.99% uptime.

### 3.1. Kỹ thuật Phân loại (Triage)
- **Impact vs. Urgency**: Một bug UI nhỏ ảnh hưởng 1 triệu người (High impact) có thể quan trọng hơn một lỗi crash chỉ ảnh hưởng 1 người (Low impact).
- **Correlation (Mối tương quan)**: Khi API chậm đồng thời với CPU DB tăng cao -> Focus vào DB ngay lập tức.

### 3.2. Lifecycle of an Incident
1. **Detect**: Alert bắn về PagerDuty (dựa trên p99 latency hoặc error rate > 5%).
2. **Engage**: Thành lập chiến phòng (War room). Incident Manager (IM) điều phối, SRE tìm nguyên nhân.
3. **Fix/Mitigate**: Ưu tiên **Rollback** (an toàn nhất) hoặc **Scale up**.
4. **Post-mortem**: Họp rút kinh nghiệm. Không đổ lỗi (Blameless culture).

### 3.3. Trending: AIOps & Observability Pipelines
- Tự động phát hiện bất thường (Anomaly Detection) bằng AI thay vì cấu hình threshold cứng.
- Export metrics/log về một trung tâm xử lý duy nhất (OpenTelemetry) để tránh vendor lock-in.
