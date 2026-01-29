# 📚 Study Guide: Senior Java Backend Engineer

> **Mục tiêu**: Ôn tập toàn diện để pass phỏng vấn vị trí Senior Java Backend Engineer (5+ năm kinh nghiệm)

---

## 📋 Checklist Tổng Quan

| Chủ đề | Mức độ ưu tiên | Status |
|--------|---------------|--------|
| Java Core & JVM | 🔴 Critical | ⬜ |
| Concurrency & Multithreading | 🔴 Critical | ⬜ |
| SQL & MySQL | 🔴 Critical | ⬜ |
| Redis & NoSQL | 🟡 Important | ⬜ |
| HTTP & RESTful API | 🔴 Critical | ⬜ |
| Kubernetes & Docker | 🟡 Important | ⬜ |
| AWS & Cloud | 🟢 Nice to have | ⬜ |
| System Design | 🟡 Important | ⬜ |

---

## 1. 🔥 Java Core & JVM

### 1.1 JVM Architecture

```
┌─────────────────────────────────────────┐
│              JVM Architecture            │
├─────────────────────────────────────────┤
│  Class Loader → Runtime Data Areas       │
│       ↓                                  │
│  ┌─────────────────────────────────┐    │
│  │ Method Area │ Heap │ Stack │ PC │    │
│  └─────────────────────────────────┘    │
│       ↓                                  │
│  Execution Engine (JIT, GC)              │
└─────────────────────────────────────────┘
```

**Câu hỏi thường gặp:**
- [ ] JVM memory model (Heap vs Stack)
- [ ] Garbage Collection algorithms (G1, ZGC, Serial, Parallel)
- [ ] Class loading mechanism
- [ ] JIT compilation

**Tips nhớ:**
> **"HSM-PC"**: Heap, Stack, Method Area, PC Register, Native Stack

### 1.2 Java I/O

| Loại | Class | Use Case |
|------|-------|----------|
| Byte Stream | InputStream/OutputStream | Binary data |
| Character Stream | Reader/Writer | Text data |
| Buffered I/O | BufferedReader/Writer | Performance |
| NIO | Channel, Buffer, Selector | Non-blocking, high performance |

**Câu hỏi quan trọng:**
- [ ] Blocking I/O vs Non-blocking I/O
- [ ] NIO Channels và Buffers
- [ ] Selector pattern

---

## 2. 🧵 Concurrency & Multithreading (FOCUS MẠNH)

> [!IMPORTANT]
> Đây là topic quan trọng nhất cho Senior Java. Cần nắm vững code patterns thực tế.

---

### 2.1 ExecutorService - Industry Standard Template

```java
// ✅ CHUẨN: Sử dụng ThreadPoolExecutor thay vì Executors factory
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    4,                      // corePoolSize
    10,                     // maximumPoolSize  
    60L, TimeUnit.SECONDS,  // keepAliveTime
    new LinkedBlockingQueue<>(100),  // workQueue với capacity
    new ThreadPoolExecutor.CallerRunsPolicy()  // rejection handler
);

// Shutdown properly
executor.shutdown();
try {
    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
        executor.shutdownNow();
    }
} catch (InterruptedException e) {
    executor.shutdownNow();
    Thread.currentThread().interrupt();
}
```

> [!WARNING]
> **Tránh dùng** `Executors.newFixedThreadPool()` vì dùng unbounded queue → OOM risk

---

### 2.2 CompletableFuture - Async Programming

```java
// ✅ Pattern 1: Chain multiple async operations
CompletableFuture.supplyAsync(() -> fetchUserFromDB(userId), executor)
    .thenApplyAsync(user -> enrichUserData(user), executor)
    .thenAcceptAsync(user -> saveToCache(user), executor)
    .exceptionally(ex -> {
        log.error("Error: ", ex);
        return null;
    });

// ✅ Pattern 2: Combine multiple futures
CompletableFuture<User> userFuture = CompletableFuture.supplyAsync(() -> getUser(id));
CompletableFuture<List<Order>> ordersFuture = CompletableFuture.supplyAsync(() -> getOrders(id));

CompletableFuture.allOf(userFuture, ordersFuture)
    .thenRun(() -> {
        User user = userFuture.join();
        List<Order> orders = ordersFuture.join();
        // combine results
    });

// ✅ Pattern 3: Timeout handling
CompletableFuture.supplyAsync(() -> slowOperation())
    .orTimeout(5, TimeUnit.SECONDS)
    .exceptionally(ex -> fallbackValue);
```

---

### 2.3 Synchronized vs Lock - Khi nào dùng gì?

```java
// ✅ synchronized: Simple cases, auto-release
public synchronized void simpleMethod() {
    // critical section
}

// ✅ ReentrantLock: Advanced control (tryLock, timeout, fairness)
private final ReentrantLock lock = new ReentrantLock(true); // fair lock

public void advancedMethod() {
    if (lock.tryLock(1, TimeUnit.SECONDS)) {
        try {
            // critical section
        } finally {
            lock.unlock();  // ⚠️ PHẢI unlock trong finally
        }
    }
}

// ✅ ReadWriteLock: Read-heavy workloads
private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

public Data read() {
    rwLock.readLock().lock();
    try { return data; } 
    finally { rwLock.readLock().unlock(); }
}

public void write(Data data) {
    rwLock.writeLock().lock();
    try { this.data = data; } 
    finally { rwLock.writeLock().unlock(); }
}
```

---

### 2.4 volatile vs Atomic vs synchronized

| Feature | volatile | Atomic | synchronized |
|---------|----------|--------|--------------|
| Visibility | ✅ | ✅ | ✅ |
| Atomicity | ❌ (chỉ read/write) | ✅ | ✅ |
| Compound ops | ❌ | ✅ (CAS) | ✅ |
| Performance | Nhanh nhất | Nhanh | Chậm nhất |
| Use case | Flag, status | Counter | Complex logic |

```java
// volatile: chỉ dùng cho flag đơn giản
private volatile boolean running = true;

// AtomicInteger: counter thread-safe
private final AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();  // atomic operation

// CAS pattern
int current, next;
do {
    current = counter.get();
    next = current + 1;
} while (!counter.compareAndSet(current, next));
```

---

### 2.5 Java NIO & Multithreading Relationship

> [!NOTE]
> **NIO + Concurrency** = High-performance server (như Netty, Tomcat NIO)

```
┌─────────────────────────────────────────────────────────┐
│  Traditional I/O (Blocking)                              │
│  1 Thread ↔ 1 Connection → 10K connections = 10K threads │
│  → Context switching overhead, memory waste              │
├─────────────────────────────────────────────────────────┤
│  NIO (Non-blocking) + Selector                           │
│  1 Thread ↔ N Connections → 10K connections = few threads│
│  → Event-driven, scalable                                │
└─────────────────────────────────────────────────────────┘
```

```java
// NIO Selector pattern (Reactor pattern)
Selector selector = Selector.open();
ServerSocketChannel server = ServerSocketChannel.open();
server.configureBlocking(false);
server.register(selector, SelectionKey.OP_ACCEPT);

while (true) {
    selector.select();  // blocks until events
    Set<SelectionKey> keys = selector.selectedKeys();
    for (SelectionKey key : keys) {
        if (key.isAcceptable()) handleAccept(key);
        if (key.isReadable()) handleRead(key);
    }
}
```

**Mối quan hệ:**
- **NIO** giải quyết I/O bottleneck
- **Multithreading** xử lý CPU-bound tasks
- Combine: NIO selector + ThreadPool workers = High-performance server

---

### 2.6 Thread-safe Collections

```java
// ✅ ConcurrentHashMap: Read không lock, write lock segment
ConcurrentHashMap<String, User> cache = new ConcurrentHashMap<>();
cache.computeIfAbsent(key, k -> loadFromDB(k));  // atomic

// ✅ CopyOnWriteArrayList: Read-heavy, write-rare
CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();

// ✅ BlockingQueue: Producer-Consumer pattern
BlockingQueue<Task> queue = new LinkedBlockingQueue<>(100);
queue.put(task);      // blocks if full
queue.take();         // blocks if empty
```

---

### 2.7 Common Concurrency Problems & Solutions

| Problem | Cause | Solution |
|---------|-------|----------|
| **Deadlock** | Circular wait | Lock ordering, tryLock with timeout |
| **Race condition** | Shared mutable state | Immutability, synchronization |
| **Starvation** | Unfair scheduling | Fair locks, priority |
| **Livelock** | Threads react to each other | Random backoff |

```java
// Deadlock prevention: Always lock in same order
void transfer(Account a, Account b, int amount) {
    Account first = a.id < b.id ? a : b;
    Account second = a.id < b.id ? b : a;
    synchronized(first) {
        synchronized(second) {
            // transfer logic
        }
    }
}
```

---

### 2.8 Race Condition - Chi Tiết Cách Handle

> [!CAUTION]
> Race condition là bug khó detect nhất vì nó non-deterministic!

**Các cách handle Race Condition:**

```java
// ✅ 1. Immutability - Best approach
public final class ImmutableUser {
    private final String name;
    private final int age;
    
    public ImmutableUser(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public ImmutableUser withAge(int newAge) {
        return new ImmutableUser(this.name, newAge);  // Return new object
    }
}

// ✅ 2. Thread Confinement - ThreadLocal
private static final ThreadLocal<SimpleDateFormat> dateFormat = 
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

public String formatDate(Date date) {
    return dateFormat.get().format(date);  // Each thread has own instance
}

// ✅ 3. Atomic Operations với CAS
private final AtomicReference<State> state = new AtomicReference<>(State.IDLE);

public boolean tryStart() {
    return state.compareAndSet(State.IDLE, State.RUNNING);
}

// ✅ 4. Double-Checked Locking (Singleton pattern)
private static volatile Singleton instance;
public static Singleton getInstance() {
    if (instance == null) {
        synchronized (Singleton.class) {
            if (instance == null) {
                instance = new Singleton();
            }
        }
    }
    return instance;
}

// ✅ 5. Lock-free algorithms với AtomicReferenceFieldUpdater
private static final AtomicReferenceFieldUpdater<Node, Node> NEXT_UPDATER =
    AtomicReferenceFieldUpdater.newUpdater(Node.class, Node.class, "next");
```

---

### 2.9 Debug & Detect Memory Leak, Race Condition

> [!IMPORTANT]
> Kỹ năng debugging rất quan trọng cho Senior level!

#### Memory Leak Detection

```bash
# 1. JVM flags để detect leak
java -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/tmp/heapdump.hprof \
     -Xmx512m -jar app.jar

# 2. JConsole / VisualVM - GUI monitoring
jvisualvm &

# 3. jmap - Heap dump
jmap -dump:format=b,file=heap.hprof <pid>

# 4. jstat - GC statistics
jstat -gc <pid> 1000  # every 1 second
```

**Common Memory Leak Patterns:**

| Pattern | Cause | Detection |
|---------|-------|-----------|
| Collection không clear | add() mà không remove() | Heap grows over time |
| Static references | static Map chứa objects | Never GC'd |
| Listeners not removed | addEventListener() | VisualVM shows growth |
| ThreadLocal not removed | set() without remove() | Thread pool leak |
| Unclosed resources | Stream, Connection | Resource monitor |

```java
// ✅ Fix ThreadLocal leak
try {
    threadLocal.set(value);
    // use value
} finally {
    threadLocal.remove();  // PHẢI remove sau khi dùng!
}

// ✅ Try-with-resources
try (Connection conn = dataSource.getConnection();
     PreparedStatement ps = conn.prepareStatement(sql)) {
    // use resources
}  // Auto-closed
```

#### Race Condition Detection

```bash
# 1. Thread dump để analyze
jstack <pid> > thread_dump.txt

# 2. Java Flight Recorder (JFR)
java -XX:StartFlightRecording=duration=60s,filename=recording.jfr -jar app.jar
```

**Tools:**
| Tool | Purpose |
|------|---------|
| **ThreadSanitizer** | Compile-time race detection |
| **FindBugs/SpotBugs** | Static analysis |
| **JCStress** | Java Concurrency Stress tests |
| **IntelliJ Inspector** | IDE-based detection |

```java
// JCStress example - detect race condition
@JCStressTest
@State
public class RaceConditionTest {
    int x;
    
    @Actor
    public void writer() { x = 1; }
    
    @Actor
    public void reader(I_Result r) { r.r1 = x; }
}
```

---

### 2.10 🔥 Batch Processing Pipeline (CSV Example)

> [!NOTE]
> Đây là pattern rất phổ biến trong production systems!

**Yêu cầu:** Process CSV lớn, chia batch, multithread, giữ thứ tự, thread-safe

```
┌─────────────────────────────────────────────────────────────────┐
│                    Batch Processing Pipeline                     │
├─────────────────────────────────────────────────────────────────┤
│  CSV File                                                        │
│     ↓                                                            │
│  [Reader Thread] → BlockingQueue<Batch>                          │
│     ↓                                                            │
│  [Worker Pool] → Process each row → ConcurrentMap<rowId, Result> │
│     ↓                                                            │
│  [Aggregator] → Collect results in order → Output                │
└─────────────────────────────────────────────────────────────────┘
```

**Complete Implementation:**

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.io.*;
import java.util.*;

public class CsvBatchProcessor {
    
    private final int BATCH_SIZE = 1000;
    private final int WORKER_THREADS = Runtime.getRuntime().availableProcessors();
    
    // Thread-safe structures
    private final BlockingQueue<List<CsvRow>> batchQueue = new LinkedBlockingQueue<>(10);
    private final ConcurrentMap<Long, ProcessResult> results = new ConcurrentHashMap<>();
    private final AtomicLong rowIdGenerator = new AtomicLong(0);
    private final AtomicBoolean readerDone = new AtomicBoolean(false);
    private final AtomicInteger processedBatches = new AtomicInteger(0);
    private volatile int totalBatches = 0;
    
    public void process(String csvPath) throws Exception {
        ExecutorService executor = new ThreadPoolExecutor(
            WORKER_THREADS, WORKER_THREADS,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        
        CountDownLatch completionLatch = new CountDownLatch(1);
        
        // 1. Reader Thread - đọc CSV và chia batch
        Thread readerThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
                List<CsvRow> batch = new ArrayList<>(BATCH_SIZE);
                String line;
                reader.readLine(); // skip header
                
                while ((line = reader.readLine()) != null) {
                    long rowId = rowIdGenerator.incrementAndGet();
                    batch.add(new CsvRow(rowId, line));
                    
                    if (batch.size() >= BATCH_SIZE) {
                        batchQueue.put(new ArrayList<>(batch));
                        totalBatches++;
                        batch.clear();
                    }
                }
                
                if (!batch.isEmpty()) {
                    batchQueue.put(batch);
                    totalBatches++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readerDone.set(true);
            }
        });
        
        // 2. Worker Threads - process từng batch
        Thread workerManager = new Thread(() -> {
            try {
                while (!readerDone.get() || !batchQueue.isEmpty()) {
                    List<CsvRow> batch = batchQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (batch == null) continue;
                    
                    // Submit batch to thread pool
                    List<CompletableFuture<Void>> futures = new ArrayList<>();
                    
                    for (CsvRow row : batch) {
                        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                            ProcessResult result = processRow(row);
                            results.put(row.id, result);  // Thread-safe put
                        }, executor);
                        futures.add(future);
                    }
                    
                    // Wait for all rows in batch to complete
                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                    processedBatches.incrementAndGet();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                completionLatch.countDown();
            }
        });
        
        readerThread.start();
        workerManager.start();
        
        // Wait for completion
        completionLatch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
        
        // 3. Output results IN ORDER
        outputResultsInOrder();
    }
    
    private ProcessResult processRow(CsvRow row) {
        // Simulate processing
        String[] fields = row.data.split(",");
        // ... business logic
        return new ProcessResult(row.id, "processed: " + fields[0]);
    }
    
    private void outputResultsInOrder() {
        // Results are stored by rowId, output in order
        long totalRows = rowIdGenerator.get();
        for (long i = 1; i <= totalRows; i++) {
            ProcessResult result = results.get(i);
            System.out.println(result);  // Or write to file
        }
    }
    
    // Data classes
    static class CsvRow {
        final long id;
        final String data;
        CsvRow(long id, String data) { this.id = id; this.data = data; }
    }
    
    static class ProcessResult {
        final long rowId;
        final String result;
        ProcessResult(long rowId, String result) { this.rowId = rowId; this.result = result; }
    }
}
```

**Key Design Points:**

| Component | Thread-safety Mechanism |
|-----------|------------------------|
| `batchQueue` | BlockingQueue với capacity limit |
| `results` | ConcurrentHashMap cho thread-safe put |
| `rowIdGenerator` | AtomicLong đảm bảo unique & ordered IDs |
| `readerDone` | AtomicBoolean để signal completion |
| Output order | Iterate by rowId (1, 2, 3...) |

**Alternative: Using ExecutorCompletionService**

```java
// Nếu cần lấy kết quả ngay khi xong (không cần order)
ExecutorCompletionService<ProcessResult> completionService = 
    new ExecutorCompletionService<>(executor);

// Submit tasks
for (CsvRow row : batch) {
    completionService.submit(() -> processRow(row));
}

// Get results as they complete
for (int i = 0; i < batch.size(); i++) {
    Future<ProcessResult> future = completionService.take();  // blocks until one ready
    ProcessResult result = future.get();
}
```

---

**Câu hỏi phỏng vấn thường gặp:**
- [ ] Giải thích ThreadPoolExecutor parameters
- [ ] CompletableFuture vs Future
- [ ] Khi nào dùng volatile vs Atomic?
- [ ] Cách detect và prevent deadlock
- [ ] NIO Selector hoạt động thế nào?
- [ ] Thiết kế batch processing pipeline như thế nào?
- [ ] Cách debug memory leak trong production?

> [!TIP]
> 📚 **Xem thêm**: [Batch Processing Multi-Language Comparison](file:///home/ethan/Projects/leetcode-project/batch_processing_comparison.md) - So sánh implementations với Spring, Scala, Akka, Go, Rust

---

### 3.1 Query Optimization

```sql
-- Sử dụng EXPLAIN để phân tích query
EXPLAIN SELECT * FROM users WHERE email = 'test@example.com';

-- Index strategies
CREATE INDEX idx_email ON users(email);
CREATE INDEX idx_composite ON orders(user_id, created_at);
```

### 3.2 Index Types

| Type | Use Case |
|------|----------|
| B-Tree | Default, range queries |
| Hash | Exact match |
| Full-text | Text search |
| Composite | Multiple columns |

### 3.3 Transaction & ACID

```sql
START TRANSACTION;
-- operations
COMMIT; -- or ROLLBACK;
```

**Isolation Levels**: READ UNCOMMITTED → READ COMMITTED → REPEATABLE READ → SERIALIZABLE

**Câu hỏi quan trọng:**
- [ ] Query execution plan
- [ ] Index optimization strategies
- [ ] N+1 problem
- [ ] Normalization vs Denormalization
- [ ] Partitioning & Sharding

---

## 4. 🔴 Redis & NoSQL

### 4.1 Redis Data Structures

| Structure | Commands | Use Case |
|-----------|----------|----------|
| String | GET, SET, INCR | Cache, counter |
| Hash | HGET, HSET | User profile |
| List | LPUSH, RPOP | Queue, timeline |
| Set | SADD, SMEMBERS | Tags, unique items |
| Sorted Set | ZADD, ZRANGE | Leaderboard |

### 4.2 Caching Patterns

```
┌──────────────────────────────────────────────────┐
│ Cache-Aside (Lazy Loading)                        │
│ 1. App checks cache first                         │
│ 2. If miss → query DB → update cache             │
│                                                    │
│ Write-Through                                      │
│ 1. Write to cache and DB synchronously           │
│                                                    │
│ Write-Behind (Write-Back)                         │
│ 1. Write to cache → async write to DB            │
└──────────────────────────────────────────────────┘
```

**Câu hỏi quan trọng:**
- [ ] Cache invalidation strategies
- [ ] Redis persistence (RDB vs AOF)
- [ ] Redis Cluster & Sentinel
- [ ] Cache stampede prevention

---

## 5. 🌐 HTTP & RESTful API

### 5.1 HTTP Methods

| Method | Idempotent | Safe | Use Case |
|--------|------------|------|----------|
| GET | ✅ | ✅ | Read resource |
| POST | ❌ | ❌ | Create resource |
| PUT | ✅ | ❌ | Update (full) |
| PATCH | ❌ | ❌ | Update (partial) |
| DELETE | ✅ | ❌ | Delete resource |

### 5.2 HTTP Status Codes

```
2xx Success: 200 OK, 201 Created, 204 No Content
3xx Redirect: 301 Moved, 304 Not Modified
4xx Client Error: 400 Bad Request, 401 Unauthorized, 404 Not Found
5xx Server Error: 500 Internal Error, 503 Service Unavailable
```

### 5.3 RESTful Best Practices

```
GET    /api/v1/users          → List users
GET    /api/v1/users/{id}     → Get user
POST   /api/v1/users          → Create user
PUT    /api/v1/users/{id}     → Update user
DELETE /api/v1/users/{id}     → Delete user
```

**Câu hỏi quan trọng:**
- [ ] REST vs GraphQL vs gRPC
- [ ] API versioning strategies
- [ ] Rate limiting & throttling
- [ ] Authentication (JWT, OAuth2)

---

## 6. 🐳 Docker & Kubernetes

### 6.1 Docker Basics

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/app.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
# Essential commands
docker build -t myapp .
docker run -p 8080:8080 myapp
docker-compose up -d
```

### 6.2 Kubernetes Concepts

```
┌─────────────────────────────────────────┐
│               Kubernetes                 │
├─────────────────────────────────────────┤
│ Pod → Deployment → Service → Ingress    │
│                                          │
│ ConfigMap: Configuration                 │
│ Secret: Sensitive data                   │
│ PVC: Persistent storage                  │
└─────────────────────────────────────────┘
```

**Câu hỏi quan trọng:**
- [ ] Pod lifecycle
- [ ] Service types (ClusterIP, NodePort, LoadBalancer)
- [ ] Horizontal Pod Autoscaling
- [ ] Liveness vs Readiness probes

---

## 7. ☁️ AWS & Serverless

### 7.1 Core AWS Services

| Service | Purpose |
|---------|---------|
| EC2 | Virtual servers |
| S3 | Object storage |
| RDS | Managed databases |
| Lambda | Serverless compute |
| API Gateway | API management |
| ECS/EKS | Container orchestration |

### 7.2 Serverless Architecture

```
API Gateway → Lambda → DynamoDB/RDS
     ↓
   CloudWatch (Monitoring)
```

---

## 8. 🏗️ System Design

### 8.1 Design Checklist

- [ ] Requirements clarification
- [ ] Back-of-envelope estimation
- [ ] High-level design
- [ ] Database schema
- [ ] API design
- [ ] Scalability considerations
- [ ] Trade-offs discussion

### 8.2 Common Patterns

| Pattern | Use Case |
|---------|----------|
| Load Balancer | Distribute traffic |
| Message Queue | Async processing |
| Circuit Breaker | Fault tolerance |
| CQRS | Read/Write separation |
| Event Sourcing | Audit trail |

---

## 📅 Lộ Trình Ôn Tập (4 Tuần)

### Tuần 1: Java Core & Concurrency
- [ ] JVM internals
- [ ] Memory management & GC
- [ ] Thread, ExecutorService
- [ ] Concurrent collections

### Tuần 2: Database & Caching
- [ ] MySQL optimization
- [ ] Index strategies
- [ ] Redis data structures
- [ ] Caching patterns

### Tuần 3: API & Infrastructure
- [ ] RESTful design
- [ ] Docker & K8s basics
- [ ] AWS core services
- [ ] CI/CD concepts

### Tuần 4: System Design & Mock Interview
- [ ] Practice system design
- [ ] Review weak areas
- [ ] Mock interviews
- [ ] Behavioral questions

---

## 🎯 Practice Resources

### LeetCode Topics
1. **Concurrency**: [Concurrency Problems](https://leetcode.com/tag/concurrency/)
2. **Database**: [SQL Problems](https://leetcode.com/tag/database/)

### Recommended Reading
- "Java Concurrency in Practice" - Brian Goetz
- "Designing Data-Intensive Applications" - Martin Kleppmann
- "System Design Interview" - Alex Xu

### Mock Interview Questions
1. Design a URL shortener
2. Design a rate limiter
3. Design a distributed cache
4. Explain how you'd optimize a slow SQL query
5. How would you handle a memory leak in production?

---

> [!TIP]
> **Interview Tips**
> - Luôn clarify requirements trước khi trả lời
> - Nêu trade-offs của các giải pháp
> - Chuẩn bị STAR method cho behavioral questions
> - Practice coding problems daily (2-3 bài/ngày)
