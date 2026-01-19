package com.practice.leetcode.interview;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║              MESSAGE QUEUE & KAFKA INTERVIEW Q&A                          ║
 * ║                   Advanced & Tricky Questions                             ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class MessageQueueKafkaQA {

  // ═══════════════════════════════════════════════════════════════════════════
  // KAFKA FUNDAMENTALS
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Q: Kafka architecture components?
   *
   * A: - Broker: Kafka server, stores data
   *    - Topic: Category/stream of messages
   *    - Partition: Ordered, immutable sequence within topic
   *    - Producer: Publishes messages
   *    - Consumer: Reads messages
   *    - Consumer Group: Group of consumers sharing work
   *    - ZooKeeper/KRaft: Cluster coordination (KRaft replacing ZK)
   *    - Offset: Position of message in partition
   */

  /**
   * Q: Làm sao Kafka đảm bảo message ordering?
   *
   * A: - Ordering CHỈ đảm bảo TRONG 1 partition
   *    - Messages cùng key → cùng partition → ordered
   *    - Nếu cần global ordering → dùng 1 partition (giảm throughput)
   *
   *    CÁCH CHỌN PARTITION:
   *    - Có key: hash(key) % num_partitions
   *    - Không key: Round-robin
   *    - Custom: Implement Partitioner interface
   *
   *    VÍ DỤ: Order events cho cùng order_id phải ordered
   *    → Dùng order_id làm key → cùng partition → ordered
   */

  /**
   * Q: Kafka replication hoạt động như thế nào?
   *
   * A: - Mỗi partition có 1 Leader + N Followers (replicas)
   *    - Producer/Consumer chỉ giao tiếp với Leader
   *    - Followers fetch từ Leader để sync
   *
   *    ISR (In-Sync Replicas):
   *    - Replicas đang sync với Leader
   *    - Leader fail → 1 ISR được elect làm new Leader
   *    - min.insync.replicas: số ISR tối thiểu để write thành công
   *
   *    EXAMPLE CONFIG:
   *    - replication.factor = 3
   *    - min.insync.replicas = 2
   *    - acks = all
   *    → Cần ít nhất 2 replicas confirm trước khi ack producer
   */

  /**
   * Q: Giải thích acks configuration?
   *
   * A: acks = 0: Fire and forget (fastest, có thể mất data)
   *    acks = 1: Leader ack (default, có thể mất nếu Leader fail trước replicate)
   *    acks = all/-1: Tất cả ISR ack (safest, slowest)
   *
   *    TRADE-OFF:
   *    - acks=0: Highest throughput, no durability
   *    - acks=all + min.insync.replicas=2: Strong durability, lower throughput
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CONSUMER & OFFSET MANAGEMENT
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Q: Consumer Group hoạt động thế nào?
   *
   * A: - Mỗi partition chỉ assign cho 1 consumer trong group
   *    - Nếu consumers > partitions → có consumer idle
   *    - Nếu partitions > consumers → 1 consumer xử lý nhiều partitions
   *
   *    REBALANCING:
   *    - Consumer join/leave → rebalance partitions
   *    - Trong khi rebalance → consumers tạm dừng
   *    - Strategies: Range, RoundRobin, Sticky, Cooperative
   *
   *    BEST PRACTICE:
   *    - partitions >= số consumers expected
   *    - Dùng Cooperative/Sticky để giảm stop-the-world rebalance
   */

  /**
   * Q: Offset commit strategies?
   *
   * A: 1. AUTO COMMIT (enable.auto.commit=true)
   *       - Tự động commit mỗi auto.commit.interval.ms
   *       - ⚠️ Risk: Process xong message nhưng chưa commit → consumer crash
   *         → Message được reprocess (at-least-once)
   *
   *    2. MANUAL COMMIT
   *       - commitSync(): Blocking, đợi broker confirm
   *       - commitAsync(): Non-blocking, callback on success/failure
   *
   *    3. MANUAL + ASYNC với SYNC fallback
   *       try {
   *         consumer.commitAsync();
   *       } finally {
   *         consumer.commitSync(); // On close
   *       }
   *
   *    BEST PRACTICE:
   *    - Commit SAU KHI xử lý xong
   *    - Batch processing: commit sau mỗi batch
   */

  /**
   * Q: Làm sao handle consumer lag?
   *
   * A: NGUYÊN NHÂN:
   *    - Consumer chậm hơn producer
   *    - Processing time cao
   *    - Network issues
   *
   *    GIẢI PHÁP:
   *    1. Scale consumers (thêm consumers, thêm partitions)
   *    2. Tăng fetch.min.bytes, fetch.max.wait.ms (batch lớn hơn)
   *    3. Optimize processing logic
   *    4. Async processing với internal queue
   *
   *    MONITOR:
   *    - Lag = Latest offset - Current offset
   *    - Dùng kafka-consumer-groups.sh hoặc metrics
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DELIVERY SEMANTICS
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Q: Giải thích At-most-once, At-least-once, Exactly-once?
   *
   * A: AT-MOST-ONCE:
   *    - Message có thể mất, không duplicate
   *    - Commit offset TRƯỚC khi process
   *    - Use case: Metrics, logs (không critical)
   *
   *    AT-LEAST-ONCE:
   *    - Message không mất, CÓ THỂ duplicate
   *    - Commit offset SAU khi process
   *    - Default behavior, most common
   *    - ⚠️ Consumer phải idempotent!
   *
   *    EXACTLY-ONCE:
   *    - Message không mất, không duplicate
   *    - Kafka Transactions + Idempotent producer
   *    - enable.idempotence=true
   *    - transactional.id configured
   *    - Most complex, overhead performance
   */

  /**
   * Q: Kafka Transactions hoạt động thế nào?
   *
   * A: PRODUCER TRANSACTIONS:
   *    producer.initTransactions();
   *    producer.beginTransaction();
   *    try {
   *      producer.send(record1);
   *      producer.send(record2);
   *      producer.sendOffsetsToTransaction(offsets, consumerGroupId);
   *      producer.commitTransaction();
   *    } catch (Exception e) {
   *      producer.abortTransaction();
   *    }
   *
   *    CONSUMER:
   *    - isolation.level=read_committed → chỉ đọc committed messages
   *    - Default read_uncommitted → đọc cả uncommitted
   *
   *    USE CASE:
   *    - Consume-transform-produce pattern
   *    - Atomic writes to multiple topics/partitions
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // IDEMPOTENCY IN MICROSERVICES
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Q: Tại sao Idempotency quan trọng trong microservices?
   *
   * A: PROBLEMS:
   *    - Network timeout → retry → duplicate request
   *    - Message queue retry → duplicate message
   *    - Load balancer retry → duplicate request
   *    - User double-click → duplicate submit
   *
   *    CONSEQUENCES WITHOUT IDEMPOTENCY:
   *    - Double payment
   *    - Double order creation
   *    - Inconsistent data
   *
   *    RULE: Assume EVERY operation will be retried!
   */

  /**
   * Q: Các cách implement Idempotency?
   *
   * A: 1. IDEMPOTENCY KEY
   *       - Client gửi unique key với mỗi request
   *       - Server lưu key + response trong cache/DB
   *       - Nếu key exists → return cached response
   *
   *       // Controller
   *       @PostMapping("/payments")
   *       public Payment create(@Header("Idempotency-Key") String key, @Body req) {
   *         String cacheKey = "payment:" + key;
   *         if (redis.exists(cacheKey)) {
   *           return redis.get(cacheKey);
   *         }
   *         Payment payment = paymentService.create(req);
   *         redis.setex(cacheKey, 24h, payment);
   *         return payment;
   *       }
   *
   *    2. DATABASE CONSTRAINTS
   *       - Unique constraint on business key (order_id, transaction_id)
   *       - INSERT ... ON CONFLICT DO NOTHING
   *
   *    3. OPTIMISTIC LOCKING
   *       UPDATE accounts SET balance = balance - 100, version = version + 1
   *       WHERE id = 1 AND version = 5;
   *       -- If affected_rows = 0 → concurrent update
   *
   *    4. DEDUPLICATION TABLE
   *       - Lưu processed message IDs
   *       - Check before processing
   */

  /**
   * Q: Handle duplicate messages trong Kafka consumer?
   *
   * A: MESSAGE DEDUPLICATION:
   *
   *    void processMessage(Message msg) {
   *      String messageId = msg.getHeader("messageId");
   *
   *      // Check if already processed
   *      if (processedIds.contains(messageId)) {
   *        log.info("Duplicate message, skipping: {}", messageId);
   *        return;
   *      }
   *
   *      // Use transaction to ensure atomicity
   *      @Transactional
   *      void processAndMark() {
   *        // Insert dedup record (fails if duplicate)
   *        dedupRepo.insert(messageId, now());
   *
   *        // Process business logic
   *        businessService.process(msg);
   *      }
   *    }
   *
   *    STORAGE OPTIONS:
   *    - Redis SET with TTL (fast, may lose on restart)
   *    - Database table with unique constraint (durable)
   *    - Bloom filter (memory efficient, allows false positives)
   */

  /**
   * Q: Idempotent trong Saga Pattern?
   *
   * A: PROBLEM:
   *    - Saga step fails → compensating transaction
   *    - Network issue → compensating transaction retried
   *    - Compensating cũng phải idempotent!
   *
   *    SOLUTION:
   *    1. Mỗi saga step có unique saga_id + step_id
   *    2. Lưu trạng thái của mỗi step
   *    3. Check trạng thái trước khi execute/compensate
   *
   *    class SagaStep {
   *      enum Status { PENDING, COMPLETED, COMPENSATED }
   *
   *      void execute(String sagaId, String stepId) {
   *        SagaStepRecord record = repo.findBySagaIdAndStepId(sagaId, stepId);
   *
   *        if (record != null && record.status == COMPLETED) {
   *          return; // Already done
   *        }
   *
   *        // Execute and mark as completed atomically
   *        @Transactional {
   *          businessLogic.execute();
   *          repo.save(new SagaStepRecord(sagaId, stepId, COMPLETED));
   *        }
   *      }
   *    }
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // KAFKA ADVANCED PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Q: Outbox Pattern là gì?
   *
   * A: PROBLEM:
   *    - Cần đảm bảo DB write + Event publish cùng thành công/fail
   *    - Không thể dùng distributed transaction (expensive, complex)
   *
   *    SOLUTION:
   *    @Transactional
   *    void createOrder(Order order) {
   *      // 1. Save order
   *      orderRepo.save(order);
   *
   *      // 2. Save event to outbox table (same transaction!)
   *      outboxRepo.save(new OutboxEvent("order.created", order));
   *    }
   *
   *    // Separate process polls outbox → publishes to Kafka
   *    @Scheduled(fixedRate = 1000)
   *    void publishOutboxEvents() {
   *      List<OutboxEvent> events = outboxRepo.findPending();
   *      for (OutboxEvent event : events) {
   *        kafka.send(event.topic, event.payload);
   *        event.markPublished();
   *      }
   *    }
   *
   *    BETTER: Debezium CDC (Change Data Capture)
   *    - Đọc database transaction log
   *    - Tự động publish changes to Kafka
   */

  /**
   * Q: Dead Letter Queue (DLQ) pattern?
   *
   * A: PROBLEM:
   *    - Message processing fails repeatedly
   *    - Không muốn block queue hoặc lose message
   *
   *    SOLUTION:
   *    void processWithRetry(Message msg) {
   *      int retryCount = msg.getRetryCount();
   *
   *      try {
   *        businessLogic.process(msg);
   *      } catch (RetryableException e) {
   *        if (retryCount < MAX_RETRIES) {
   *          msg.incrementRetry();
   *          retryTopic.send(msg); // Send to retry topic with delay
   *        } else {
   *          dlqTopic.send(msg);   // Send to DLQ for manual handling
   *        }
   *      } catch (NonRetryableException e) {
   *        dlqTopic.send(msg);     // Direct to DLQ
   *      }
   *    }
   *
   *    TOPICS:
   *    - main-topic: Original messages
   *    - main-topic-retry-1: Retry with 1min delay
   *    - main-topic-retry-2: Retry with 5min delay
   *    - main-topic-dlq: Failed messages for investigation
   */

  /**
   * Q: Làm sao implement Event Sourcing với Kafka?
   *
   * A: CONCEPT:
   *    - Store events, không store state
   *    - State = replay all events
   *    - Kafka topic = event log
   *
   *    PATTERNS:
   *    1. Event Store: Kafka topic per aggregate type
   *    2. Snapshots: Periodic state snapshots để avoid full replay
   *    3. Projections: Consume events → build read models
   *
   *    // Event types
   *    OrderCreated { orderId, customerId, items }
   *    ItemAdded { orderId, item }
   *    OrderPaid { orderId, amount }
   *    OrderShipped { orderId, trackingNumber }
   *
   *    // Rebuild state
   *    Order rebuildOrder(String orderId) {
   *      List<Event> events = eventStore.getEvents(orderId);
   *      Order order = new Order();
   *      for (Event e : events) {
   *        order.apply(e);
   *      }
   *      return order;
   *    }
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // TROUBLESHOOTING & OPTIMIZATION
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Q: Kafka consumer bị rebalance liên tục, xử lý thế nào?
   *
   * A: NGUYÊN NHÂN:
   *    - session.timeout.ms quá nhỏ
   *    - max.poll.interval.ms < processing time
   *    - Consumer crash/restart
   *
   *    GIẢI PHÁP:
   *    - Tăng session.timeout.ms (default 10s → 30s)
   *    - Tăng max.poll.interval.ms (default 5min → 10min)
   *    - Giảm max.poll.records (ít messages per poll)
   *    - Optimize processing time
   *    - Dùng Cooperative rebalance strategy
   */

  /**
   * Q: Optimize Kafka throughput?
   *
   * A: PRODUCER:
   *    - batch.size: Lớn hơn → ít request → higher throughput
   *    - linger.ms: Đợi accumulate messages → larger batches
   *    - compression.type: snappy/lz4 (fast) or gzip (smaller)
   *    - buffer.memory: Memory for buffering
   *
   *    CONSUMER:
   *    - fetch.min.bytes: Đợi accumulate → larger fetches
   *    - max.poll.records: Số messages per poll
   *
   *    BROKER:
   *    - Nhiều partitions → parallel processing
   *    - Page cache: Đủ RAM cho active data
   *    - Fast disk: SSD preferred
   *    - Network: Dedicated network cho replication
   */

  /**
   * Q: Handle message ordering across services?
   *
   * A: PROBLEM:
   *    Service A publish: Event1, Event2
   *    Service B receive: Event2, Event1 (out of order!)
   *
   *    SOLUTIONS:
   *    1. Single partition với key (guaranteed order)
   *    2. Sequence numbers
   *       - Include sequence in message
   *       - Consumer buffer out-of-order, reorder before process
   *
   *    3. Event versioning
   *       - Include aggregate version
   *       - Reject if version != expected
   *
   *    4. Causal ordering
   *       - Include vector clock
   *       - Process only when dependencies satisfied
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // COMPARISON: KAFKA vs OTHER MESSAGE QUEUES
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Q: Kafka vs RabbitMQ vs SQS?
   *
   * A:
   * ┌────────────────┬───────────────────┬───────────────────┬──────────────────┐
   * │ Feature        │ Kafka             │ RabbitMQ          │ SQS              │
   * ├────────────────┼───────────────────┼───────────────────┼──────────────────┤
   * │ Model          │ Log-based         │ Queue-based       │ Queue-based      │
   * │ Retention      │ Configurable      │ Until consumed    │ 14 days max      │
   * │ Replay         │ ✓ Yes             │ ✗ No              │ ✗ No             │
   * │ Ordering       │ Per partition     │ Per queue (FIFO)  │ Best effort/FIFO │
   * │ Throughput     │ Very high         │ High              │ High             │
   * │ Latency        │ Low               │ Very low          │ Higher           │
   * │ Management     │ Self/MSK          │ Self/CloudAMQP    │ Fully managed    │
   * │ Use case       │ Event streaming   │ Task queues       │ Simple queues    │
   * └────────────────┴───────────────────┴───────────────────┴──────────────────┘
   *
   * KAFKA: Log analytics, Event sourcing, Stream processing
   * RABBITMQ: Task queues, RPC, Complex routing
   * SQS: Simple async, AWS native, Serverless
   */
}
