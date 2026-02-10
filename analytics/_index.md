# Java Knowledge Base - Map of Content (MOC)

> **PhD-Level Java Backend Engineering Guide**
> Cross-referenced for Obsidian knowledge graph

---

## 🗂️ Quick Navigation

```
analytics/
├── java-core/           # JVM & Language Internals
├── concurrency/         # Threading & Synchronization
├── spring/              # Spring Framework Deep Dive
├── persistence/         # Database & ORM
└── microservices/       # Distributed Systems
```

---

## ☕ Java Core

Deep understanding of JVM internals and language fundamentals.

| Topic | File | Key Concepts |
|-------|------|--------------|
| **JVM Architecture** | [[java-core/jvm-architecture]] | Class loading, runtime areas, GC, JIT |
| **Memory Model** | [[java-core/memory-model]] | JMM, happens-before, volatile, barriers |
| **synchronized** | [[java-core/synchronized-internals]] | Monitors, biased/thin/fat locks, wait/notify |
| **Exceptions** | [[java-core/exceptions-errors]] | Error vs Exception, checked vs unchecked |
| **Serialization** | [[java-core/serialization]] | transient, Serializable, modern alternatives |

---

## 🧵 Concurrency

Thread-safe programming patterns and tools.

| Topic | File | Key Concepts |
|-------|------|--------------|
| **Thread Pools & Locks** | [[concurrency/thread-pools-locks]] | Executors, ReentrantLock, synchronizers |
| **Atomic Operations** | [[concurrency/thread-pools-locks#atomic-variables]] | CAS, AtomicInteger, VarHandle |
| **Concurrent Collections** | [[concurrency/thread-pools-locks#concurrent-collections]] | ConcurrentHashMap, BlockingQueue |

---

## 🌱 Spring Framework

Spring internals and advanced patterns.

| Topic | File | Key Concepts |
|-------|------|--------------|
| **Dependency Injection** | [[spring/dependency-injection]] | Constructor vs field, qualifiers, internals |
| **Expert Guide** | [[spring/spring-expert-guide]] | AutoConfiguration, BeanPostProcessor, AOP |
| **Transactions** | [[spring/transaction-management]] | @Transactional, propagation, isolation |

---

## 💾 Persistence

Database access and performance optimization.

| Topic | File | Key Concepts |
|-------|------|--------------|
| **JPA/Hibernate** | [[persistence/jpa-hibernate-advanced-case-studies]] | N+1, caching, batch operations |
| **Performance** | [[persistence/persistence-performance]] | Query optimization, connection pools |
| **Batch Processing** | [[persistence/batch-processing]] | Multi-language comparison, pipelines |
| **SAP Case Study** | [[persistence/sap-job-matching]] | Enterprise integration patterns |

---

## 🌐 Microservices

Distributed systems architecture and patterns.

| Topic | File | Key Concepts |
|-------|------|--------------|
| **Architecture** | [[microservices/microservices-architecture]] | Service decomposition, communication |
| **Patterns Deep Dive** | [[microservices/microservices-patterns-deep-dive]] | Saga, CQRS, Event Sourcing |
| **Edge Cases** | [[microservices/microservices-edge-cases-expert-guide]] | Failure handling, consistency |

---

## 🔗 Cross-Topic Connections

### Memory & Concurrency
- [[java-core/memory-model]] ↔ [[java-core/synchronized-internals]] — Memory barriers & monitor semantics
- [[java-core/memory-model]] ↔ [[concurrency/thread-pools-locks]] — Visibility guarantees

### JVM & Spring
- [[java-core/jvm-architecture]] ↔ [[spring/spring-expert-guide]] — Class loading, proxies
- [[java-core/serialization]] ↔ [[persistence/jpa-hibernate-advanced-case-studies]] — Entity DTOs

### Transactions & Concurrency
- [[spring/transaction-management]] ↔ [[concurrency/thread-pools-locks]] — ACID & threading
- [[spring/transaction-management]] ↔ [[persistence/persistence-performance]] — Locking strategies

---

## 📚 Recommended Reading Order

### For Interview Prep
1. [[java-core/jvm-architecture]] — Foundations
2. [[java-core/memory-model]] — Visibility
3. [[java-core/synchronized-internals]] — Locking
4. [[concurrency/thread-pools-locks]] — Patterns
5. [[spring/transaction-management]] — Spring

### For Deep Understanding
1. Complete `java-core/` folder
2. `concurrency/` with hands-on JCStress tests
3. `spring/` internals
4. `persistence/` performance tuning
5. `microservices/` patterns

---

## 🏷️ Tags

#java #jvm #concurrency #spring #microservices #interview #backend
