# 🎯 SAP Business Network - Senior Preparation Guide

Tài liệu này là lộ trình ôn tập (Review Outline) được thiết kế riêng cho vị trí Senior Developer tại SAP Business Network, dựa trên JD và codebase hiện tại.

---

## 📅 Đề cương Ôn tập (Review Outline)

### 🟢 Giai đoạn 1: Java Core & Senior Level (Days 1-2)
- **Mục tiêu**: Nắm vững "tại sao" thay vì "cái gì".
- **Nội dung**:
    - [Collections Deep Dive](../Java_Interview_Deep_Dive.md#1-collections-framework): HashMap internals, ConcurrentHashMap.
    - [Concurrency](../java_backend_study_guide.md#2-concurrency-deep-dive): Virtual Threads, ExecutorService tuning, Race conditions.
    - [JVM & Memory](../java_backend_study_guide.md#3-jvm-memory-management): GC Algorithms (G1, ZGC), Troubleshooting OutOfMemory.

### 🟡 Giai đoạn 2: Spring Boot & Data Persistence (Days 3-4)
- **Mục tiêu**: Tối ưu hóa hiệu năng hệ thống lớn.
- **Nội dung**:
    - [JPA Performance](./persistence-performance.md): N+1 problem, Batch processing, L2 Cache.
    - [DI & Proxy](./dependency-injection.md): `@Transactional` proxying, Bean lifecycle hooks.
    - [Transaction Management](./transaction-management.md): Propagation, Isolation levels, Distributed transactions.

### 🔵 Giai đoạn 3: Microservices & Cloud Native (Days 5-7)
- **Mục tiêu**: Trả lời các câu hỏi về kiến trúc và sự lựa chọn công nghệ.
- **Nội dung**:
    - [Microservices Deep Dive](./microservices-architecture.md): Patterns (Circuit Breaker, Saga), Technology decisions (Kafka vs MQ).
    - [Infrastructure](./microservices-architecture.md#2-infrastructure--orchestration-k8s): K8s basics, Service Mesh (Istio), Scaling strategies.
    - [SAP Ecosystem](./sap-job-matching.md#2-sap--cloud-technologies): BTP (Business Technology Platform), Cloud Integration, HANA database.

### 🔴 Giai đoạn 4: System Design & Operational (Days 8-10)
- **Mục tiêu**: Giải quyết sự cố thực tế và thiết kế hệ thống scale.
- **Nội dung**:
    - [Observability](./microservices-architecture.md#4-observability-the-golden-signals): Logging (ELK), Monitoring (Prometheus/Grafana).
    - [Incident Response](./microservices-architecture.md#5-incident-response--on-call-skills): Kỹ năng On-call, Root Cause Analysis (RCA).
    - [Mentoring & Leadership](./sap-job-matching.md#4-senior--leadership-requirements): Cách review code, guide junior và thiết kế kiến trúc sản phẩm lớn.

---

## 🏗️ Core Technical Stack Matching (JD vs. Codebase)

| Requirement | existing Resource | Status |
| :--- | :--- | :--- |
| **Java Deep Knowledge** | [Java_Interview_Deep_Dive.md](../Java_Interview_Deep_Dive.md) | ✅ Covered |
| **Spring Boot & JPA** | [persistence-performance.md](./persistence-performance.md) | ✅ Covered |
| **Microservices / K8s** | [microservices-architecture.md](./microservices-architecture.md) | ⭐ **New Deep Dive** |
| **Kafka / Messaging** | [microservices-architecture.md](./microservices-architecture.md#11-messaging-kafka-vs-rabbitmq) | ⭐ **New Deep Dive** |
| **SAP BTP / HANA** | [See SAP Section Below](#2-sap--cloud-technologies) | ✅ Documented |

---

## ☁️ 2. SAP & Cloud Technologies (Context)

... *(Nội dung chi tiết về BTP và HANA đã có ở bản trước)* ...

*(Ghi chú: Xem [microservices-architecture.md](./microservices-architecture.md) để biết thêm về các microservices patterns được áp dụng trong môi trường cloud của SAP)*
