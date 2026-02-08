# 🚨 Microservices Edge Cases & Error Handling - Expert Guide

> **Mục tiêu**: Nâng level từ Mid-level → Senior/Staff Engineer thông qua việc xử lý các edge cases, failure scenarios và production-grade patterns.

---

## 📌 Mục lục

1. [Distributed Transactions & Edge Cases](#1-distributed-transactions--edge-cases)
2. [Kafka: Error Handling & DLQ Patterns](#2-kafka-error-handling--dlq-patterns)
3. [Idempotency: Duplicate Request Handling](#3-idempotency-duplicate-request-handling)
4. [Network Failures & Partial Failures](#4-network-failures--partial-failures)
5. [Database Edge Cases](#5-database-edge-cases)
6. [Race Conditions & Concurrency](#6-race-conditions--concurrency)
7. [API Design: Error Response Patterns](#7-api-design-error-response-patterns)
8. [Distributed Tracing & Debugging](#8-distributed-tracing--debugging)
9. [Chaos Engineering & Testing](#9-chaos-engineering--testing)
10. [Production Checklist](#10-production-checklist)

---

## 1. Distributed Transactions & Edge Cases

### 1.1. Saga Pattern - Các Edge Cases Thường Gặp

#### Edge Case 1: Compensation Failure (Rollback của Rollback)

```
Scenario:
1. Order Service → tạo đơn hàng ✓
2. Inventory Service → trừ stock ✓
3. Payment Service → thanh toán ✗ (thất bại)
4. Compensation: Inventory Service → hoàn stock ✗ (cũng thất bại!)
```

**Giải pháp Senior/Staff:**

```java
@Service
@Slf4j
public class SagaCompensationHandler {
    
    private final RetryTemplate retryTemplate;
    private final CompensationLogRepository compensationLog;
    private final AlertService alertService;
    
    /**
     * Compensation với retry và fallback
     */
    public void compensateWithRetry(CompensationTask task) {
        try {
            retryTemplate.execute(ctx -> {
                log.info("Compensation attempt {} for task {}", 
                    ctx.getRetryCount(), task.getId());
                executeCompensation(task);
                return null;
            }, ctx -> {
                // Recovery callback - sau khi hết retry
                handleCompensationFailure(task, ctx.getLastThrowable());
                return null;
            });
        } catch (Exception e) {
            // Critical: Compensation thất bại hoàn toàn
            saveToManualInterventionQueue(task, e);
            alertService.sendCriticalAlert(
                "SAGA_COMPENSATION_FAILED",
                Map.of("taskId", task.getId(), "error", e.getMessage())
            );
        }
    }
    
    /**
     * Lưu vào queue để xử lý thủ công
     */
    private void saveToManualInterventionQueue(CompensationTask task, Exception e) {
        CompensationLog log = CompensationLog.builder()
            .taskId(task.getId())
            .sagaId(task.getSagaId())
            .compensationType(task.getType())
            .payload(task.getPayload())
            .errorMessage(e.getMessage())
            .status(CompensationStatus.REQUIRES_MANUAL_INTERVENTION)
            .createdAt(Instant.now())
            .build();
        
        compensationLog.save(log);
    }
}
```

#### Edge Case 2: Saga Coordinator Dies Mid-Transaction

```
Scenario:
1. Saga Orchestrator bắt đầu transaction
2. Step 1, 2 hoàn thành
3. Orchestrator crash/restart
4. Step 3, 4 chưa thực hiện → Hệ thống inconsistent
```

**Giải pháp: Saga State Persistence**

```java
@Entity
@Table(name = "saga_instances")
public class SagaInstance {
    
    @Id
    private String sagaId;
    
    @Enumerated(EnumType.STRING)
    private SagaState state; // STARTED, STEP_1_COMPLETED, STEP_2_COMPLETED, etc.
    
    @Column(columnDefinition = "jsonb")
    private String context; // Toàn bộ data cần thiết để resume
    
    private Instant startedAt;
    private Instant lastUpdatedAt;
    
    @Version
    private Long version; // Optimistic locking
}

@Service
public class ResilientSagaOrchestrator {
    
    private final SagaInstanceRepository sagaRepo;
    
    /**
     * Resume incomplete sagas sau khi restart
     */
    @Scheduled(fixedDelay = 60000) // Mỗi phút
    @Transactional
    public void recoverIncompleteSagas() {
        Instant timeout = Instant.now().minus(5, ChronoUnit.MINUTES);
        
        List<SagaInstance> stuckSagas = sagaRepo
            .findByStateNotInAndLastUpdatedAtBefore(
                List.of(SagaState.COMPLETED, SagaState.COMPENSATED),
                timeout
            );
        
        for (SagaInstance saga : stuckSagas) {
            log.warn("Recovering stuck saga: {}", saga.getSagaId());
            resumeSaga(saga);
        }
    }
    
    private void resumeSaga(SagaInstance saga) {
        SagaContext context = deserialize(saga.getContext());
        
        switch (saga.getState()) {
            case STEP_1_COMPLETED:
                executeStep2(saga, context);
                break;
            case STEP_2_COMPLETED:
                executeStep3(saga, context);
                break;
            // Nếu state là STEP_X_FAILED → trigger compensation
            case STEP_2_FAILED:
                compensateStep1(saga, context);
                break;
            default:
                // Log và alert
                break;
        }
    }
}
```

#### Edge Case 3: At-Least-Once Delivery + Non-Idempotent Operations

```
Scenario:
1. Payment Service nhận message "deduct $100"
2. Xử lý xong, gửi ACK
3. ACK bị mất do network issue
4. Kafka resend message
5. Payment deduct thêm $100 lần nữa!
```

**Giải pháp: Idempotency với Deduplication Store**

```java
@Service
public class IdempotentPaymentProcessor {
    
    private final RedisTemplate<String, String> redis;
    private static final long DEDUP_TTL_HOURS = 24;
    
    @KafkaListener(topics = "payments")
    public void processPayment(ConsumerRecord<String, PaymentEvent> record) {
        String messageId = record.headers()
            .lastHeader("message-id")
            .value()
            .toString();
        
        String dedupKey = "payment:processed:" + messageId;
        
        // Kiểm tra đã xử lý chưa (atomic operation)
        Boolean isNew = redis.opsForValue()
            .setIfAbsent(dedupKey, "1", Duration.ofHours(DEDUP_TTL_HOURS));
        
        if (Boolean.FALSE.equals(isNew)) {
            log.info("Duplicate message detected, skipping: {}", messageId);
            return; // Skip duplicate
        }
        
        try {
            doProcessPayment(record.value());
        } catch (Exception e) {
            // Xóa key để có thể retry
            redis.delete(dedupKey);
            throw e;
        }
    }
}
```

### 1.2. Outbox Pattern - Edge Cases

#### Edge Case: Outbox Poll Race Condition

```
Scenario:
1. Instance A: SELECT ... FOR UPDATE từ outbox (row 1, 2, 3)
2. Instance A: Đang gửi message...
3. Instance A: Crash trước khi UPDATE status
4. Lock được release
5. Instance B: Poll lại row 1, 2, 3
6. Message bị duplicate!
```

**Giải pháp: Polling với Lease**

```java
@Service
public class OutboxPoller {
    
    @Transactional
    public List<OutboxMessage> pollWithLease(String instanceId, int batchSize) {
        Instant now = Instant.now();
        Instant leaseExpiry = now.plus(2, ChronoUnit.MINUTES);
        
        // Lấy messages chưa có lease hoặc lease đã expired
        List<OutboxMessage> messages = outboxRepo.findMessagesToProcess(
            now, batchSize
        );
        
        if (messages.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Gán lease cho instance này
        for (OutboxMessage msg : messages) {
            msg.setLeasedBy(instanceId);
            msg.setLeaseExpiresAt(leaseExpiry);
        }
        
        outboxRepo.saveAll(messages);
        return messages;
    }
    
    @Transactional
    public void markAsProcessed(List<Long> messageIds, String instanceId) {
        // Double check lease ownership
        int updated = outboxRepo.updateStatusByIdsAndLeasedBy(
            messageIds, 
            OutboxStatus.PROCESSED,
            instanceId
        );
        
        if (updated != messageIds.size()) {
            log.warn("Some messages were already processed by another instance");
        }
    }
}
```

---

## 2. Kafka: Error Handling & DLQ Patterns

### 2.1. Error Handling Strategy Matrix

| Error Type | Retry? | DLQ? | Alert? | Example |
|------------|--------|------|--------|---------|
| **Transient** | ✓ (with backoff) | After max retries | After DLQ | Network timeout, DB connection |
| **Poison Pill** | ✗ | ✓ Immediately | ✓ | Malformed JSON, schema mismatch |
| **Business Error** | Depends | ✓ | ✗ | Insufficient funds, item not found |
| **Infrastructure** | ✓ (indefinite) | ✗ | ✓ Critical | Kafka broker down |

### 2.2. Production-Grade Error Handler

```java
@Configuration
public class KafkaErrorHandlerConfig {
    
    @Bean
    public DefaultErrorHandler errorHandler(
            KafkaTemplate<String, Object> kafkaTemplate,
            MeterRegistry meterRegistry) {
        
        // Retry configuration
        ExponentialBackOffWithMaxRetries backOff = 
            new ExponentialBackOffWithMaxRetries(5);
        backOff.setInitialInterval(1000);    // 1s
        backOff.setMultiplier(2.0);          // 1s, 2s, 4s, 8s, 16s
        backOff.setMaxInterval(30000);       // Max 30s
        
        // Error handler với custom recovery
        DefaultErrorHandler handler = new DefaultErrorHandler(
            new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> {
                    // Thêm metadata vào DLQ message
                    return new TopicPartition(
                        record.topic() + ".DLQ", 
                        record.partition()
                    );
                }
            ),
            backOff
        );
        
        // Phân loại error: retry vs không retry
        handler.addNotRetryableExceptions(
            JsonProcessingException.class,    // Poison pill
            ValidationException.class,         // Bad data
            IllegalArgumentException.class
        );
        
        handler.addRetryableExceptions(
            TransientDataAccessException.class,
            OptimisticLockingFailureException.class,
            SocketTimeoutException.class
        );
        
        // Metrics cho observability
        handler.setRetryListeners((record, ex, deliveryAttempt) -> {
            meterRegistry.counter("kafka.consumer.retry",
                "topic", record.topic(),
                "attempt", String.valueOf(deliveryAttempt)
            ).increment();
        });
        
        return handler;
    }
}
```

### 2.3. DLQ Processing Pattern

```java
@Service
@Slf4j
public class DLQProcessor {

    private final ObjectMapper objectMapper;
    private final DLQRepository dlqRepository;
    private final AlertService alertService;

    /**
     * Consumer cho DLQ topic
     */
    @KafkaListener(
        topics = "${kafka.topics.orders}.DLQ",
        groupId = "dlq-processor"
    )
    public void processDLQ(ConsumerRecord<String, String> record) {
        DLQMessage dlqMessage = DLQMessage.builder()
            .originalTopic(extractHeader(record, "original-topic"))
            .originalPartition(extractIntHeader(record, "original-partition"))
            .originalOffset(extractLongHeader(record, "original-offset"))
            .errorMessage(extractHeader(record, "exception-message"))
            .errorStacktrace(extractHeader(record, "exception-stacktrace"))
            .payload(record.value())
            .receivedAt(Instant.now())
            .status(DLQStatus.PENDING)
            .retryCount(0)
            .build();
        
        // Phân loại error
        classifyAndProcess(dlqMessage);
    }
    
    private void classifyAndProcess(DLQMessage msg) {
        String errorMessage = msg.getErrorMessage();
        
        if (isPoisonPill(errorMessage)) {
            // Poison pill: lưu để review manual
            msg.setStatus(DLQStatus.REQUIRES_MANUAL_REVIEW);
            msg.setClassification("POISON_PILL");
            dlqRepository.save(msg);
            
            alertService.sendAlert(
                AlertLevel.WARNING,
                "DLQ Poison Pill detected",
                Map.of("topic", msg.getOriginalTopic(), "error", errorMessage)
            );
            
        } else if (isTransientError(errorMessage)) {
            // Transient: schedule retry
            msg.setStatus(DLQStatus.SCHEDULED_RETRY);
            msg.setClassification("TRANSIENT");
            msg.setNextRetryAt(Instant.now().plus(1, ChronoUnit.HOURS));
            dlqRepository.save(msg);
            
        } else {
            // Unknown: cần investigation
            msg.setStatus(DLQStatus.REQUIRES_INVESTIGATION);
            msg.setClassification("UNKNOWN");
            dlqRepository.save(msg);
            
            alertService.sendAlert(
                AlertLevel.HIGH,
                "DLQ Unknown error type",
                Map.of("messageId", msg.getId(), "error", errorMessage)
            );
        }
    }
    
    /**
     * Scheduled job để retry DLQ messages
     */
    @Scheduled(fixedDelay = 300000) // 5 phút
    public void retryScheduledMessages() {
        List<DLQMessage> toRetry = dlqRepository
            .findByStatusAndNextRetryAtBefore(
                DLQStatus.SCHEDULED_RETRY,
                Instant.now()
            );
        
        for (DLQMessage msg : toRetry) {
            if (msg.getRetryCount() >= MAX_DLQ_RETRIES) {
                msg.setStatus(DLQStatus.MAX_RETRIES_EXCEEDED);
                dlqRepository.save(msg);
                continue;
            }
            
            try {
                reprocessMessage(msg);
                msg.setStatus(DLQStatus.REPROCESSED_SUCCESS);
            } catch (Exception e) {
                msg.setRetryCount(msg.getRetryCount() + 1);
                msg.setNextRetryAt(calculateNextRetry(msg.getRetryCount()));
                msg.setLastError(e.getMessage());
            }
            
            dlqRepository.save(msg);
        }
    }
}
```

### 2.4. Kafka Consumer Edge Cases

#### Edge Case: Consumer Lag Spiraling

```
Scenario:
1. Consumer A xử lý chậm (10 msg/s)
2. Producer gửi nhanh (100 msg/s)
3. Lag tăng dần → Rebalance timeout
4. Rebalance → Consumer mất assignment
5. Sau rebalance → Lag còn lớn hơn!
```

**Giải pháp: Dynamic Scaling + Backpressure**

```java
@Component
public class KafkaLagMonitor {
    
    private final AdminClient adminClient;
    private final ScalingService scalingService;
    
    @Scheduled(fixedDelay = 30000)
    public void monitorAndScale() {
        Map<TopicPartition, Long> endOffsets = getEndOffsets();
        Map<TopicPartition, Long> currentOffsets = getCurrentCommittedOffsets();
        
        long totalLag = 0;
        for (TopicPartition tp : endOffsets.keySet()) {
            long lag = endOffsets.get(tp) - currentOffsets.getOrDefault(tp, 0L);
            totalLag += lag;
            
            // Alert nếu lag partition cụ thể quá cao
            if (lag > PARTITION_LAG_THRESHOLD) {
                alertService.sendAlert(
                    AlertLevel.WARNING,
                    String.format("High lag on %s: %d", tp, lag),
                    Map.of("partition", tp.toString(), "lag", lag)
                );
            }
        }
        
        // Auto-scale dựa trên lag
        if (totalLag > SCALE_UP_THRESHOLD) {
            scalingService.scaleUp("order-consumer", 1);
        } else if (totalLag < SCALE_DOWN_THRESHOLD) {
            scalingService.scaleDown("order-consumer", 1);
        }
    }
}
```

#### Edge Case: Exactly-Once Processing Pitfall

```
Scenario:
1. Consumer đọc message, xử lý xong
2. Commit offset thành công
3. Ghi DB → thất bại!
4. Message đã committed nhưng chưa xử lý → Data loss!
```

**Giải pháp: Transactional Outbox + Manual Offset**

```java
@Service
public class TransactionalConsumer {
    
    @KafkaListener(
        topics = "orders",
        // Tắt auto commit
        properties = {"enable.auto.commit=false"}
    )
    public void processWithTransaction(
            ConsumerRecord<String, OrderEvent> record,
            Acknowledgment ack) {
        
        // CRITICAL: Xử lý và commit trong cùng 1 DB transaction
        try {
            processOrderWithinTransaction(record.value(), record.offset());
            
            // Commit Kafka offset SAU KHI DB commit thành công
            ack.acknowledge();
            
        } catch (Exception e) {
            // Không acknowledge → message sẽ được redelivery
            log.error("Failed to process order, will retry", e);
            throw e;
        }
    }
    
    @Transactional
    public void processOrderWithinTransaction(OrderEvent event, long offset) {
        // 1. Check idempotency
        if (offsetAlreadyProcessed(event.getOrderId(), offset)) {
            log.info("Offset {} already processed, skipping", offset);
            return;
        }
        
        // 2. Business logic
        Order order = createOrder(event);
        orderRepository.save(order);
        
        // 3. Lưu offset đã xử lý (trong cùng transaction)
        ProcessedOffset po = new ProcessedOffset(
            event.getOrderId(), offset, Instant.now()
        );
        processedOffsetRepository.save(po);
        
        // 4. Publish event qua Outbox (transactional)
        outboxService.saveEvent("order.created", order);
    }
}
```

---

## 3. Idempotency: Duplicate Request Handling

### 3.1. Idempotency Key Strategies

| Strategy | Pros | Cons | Use Case |
|----------|------|------|----------|
| **Client-provided UUID** | Đơn giản, client control | Client có thể reuse key | Payment APIs |
| **Hash of request body** | Tự động, không cần client | Hash collision, request thay đổi | Batch processing |
| **Composite key** | Chính xác cho business | Phức tạp | Order creation |

### 3.2. Production Idempotency Implementation

```java
@Aspect
@Component
@Order(1) // Chạy trước @Transactional
public class IdempotencyAspect {
    
    private final IdempotencyKeyStore keyStore;
    private final ObjectMapper objectMapper;
    
    @Around("@annotation(idempotent)")
    public Object handleIdempotency(ProceedingJoinPoint pjp, Idempotent idempotent) 
            throws Throwable {
        
        String idempotencyKey = extractIdempotencyKey(pjp, idempotent);
        String lockKey = "idem:lock:" + idempotencyKey;
        String resultKey = "idem:result:" + idempotencyKey;
        
        // 1. Kiểm tra kết quả đã cache
        Optional<CachedResult> cached = keyStore.get(resultKey);
        if (cached.isPresent()) {
            log.info("Returning cached result for key: {}", idempotencyKey);
            return deserializeResult(cached.get(), pjp.getSignature());
        }
        
        // 2. Acquire distributed lock
        boolean lockAcquired = keyStore.tryLock(lockKey, 
            Duration.ofSeconds(idempotent.lockTimeoutSeconds()));
        
        if (!lockAcquired) {
            // Request đang được xử lý bởi instance khác
            throw new IdempotencyConflictException(
                "Request with key " + idempotencyKey + " is being processed"
            );
        }
        
        try {
            // 3. Double-check sau khi có lock
            cached = keyStore.get(resultKey);
            if (cached.isPresent()) {
                return deserializeResult(cached.get(), pjp.getSignature());
            }
            
            // 4. Execute business logic
            Object result = pjp.proceed();
            
            // 5. Cache result
            CachedResult toCache = CachedResult.builder()
                .result(objectMapper.writeValueAsString(result))
                .resultClass(result.getClass().getName())
                .createdAt(Instant.now())
                .build();
            
            keyStore.set(resultKey, toCache, 
                Duration.ofHours(idempotent.ttlHours()));
            
            return result;
            
        } finally {
            keyStore.releaseLock(lockKey);
        }
    }
    
    private String extractIdempotencyKey(ProceedingJoinPoint pjp, Idempotent idempotent) {
        // Ưu tiên: Header > Annotation SpEL > Request hash
        HttpServletRequest request = getCurrentRequest();
        
        String headerKey = request.getHeader("Idempotency-Key");
        if (StringUtils.hasText(headerKey)) {
            return headerKey;
        }
        
        if (StringUtils.hasText(idempotent.keyExpression())) {
            return evaluateSpEL(idempotent.keyExpression(), pjp);
        }
        
        // Fallback: hash request
        return hashRequest(pjp.getArgs());
    }
}

// Annotation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    String keyExpression() default "";
    int ttlHours() default 24;
    int lockTimeoutSeconds() default 30;
}

// Usage
@PostMapping("/orders")
@Idempotent(keyExpression = "#request.orderId + ':' + #request.customerId")
public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
    return orderService.createOrder(request);
}
```

### 3.3. Database-Level Idempotency (Upsert Pattern)

```java
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Atomic upsert với idempotency key
     * PostgreSQL specific
     */
    @Modifying
    @Query(value = """
        INSERT INTO orders (idempotency_key, customer_id, amount, status, created_at)
        VALUES (:idempotencyKey, :customerId, :amount, 'PENDING', NOW())
        ON CONFLICT (idempotency_key) 
        DO UPDATE SET 
            updated_at = NOW()
        WHERE orders.status = 'PENDING'
        RETURNING *
        """, nativeQuery = true)
    Optional<Order> upsertOrder(
        @Param("idempotencyKey") String idempotencyKey,
        @Param("customerId") Long customerId,
        @Param("amount") BigDecimal amount
    );
}
```

---

## 4. Network Failures & Partial Failures

### 4.1. Timeout Patterns

| Timeout Type | Default | Recommended | Why |
|--------------|---------|-------------|-----|
| **Connection** | 10s | 2-5s | Fast fail nếu không connect được |
| **Read/Socket** | 0 (infinite) | 10-30s | Tránh thread bị block mãi |
| **Request** | N/A | < Read timeout | Tổng thời gian cho toàn request |

```java
@Configuration
public class HttpClientConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        
        HttpComponentsClientHttpRequestFactory factory = 
            new HttpComponentsClientHttpRequestFactory();
        
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(Timeout.ofSeconds(3))      // Connect timeout
            .setResponseTimeout(Timeout.ofSeconds(10))    // Read timeout
            .setConnectionRequestTimeout(Timeout.ofSeconds(5)) // Pool wait
            .build();
        
        CloseableHttpClient httpClient = HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .setConnectionManager(createConnectionManager())
            .setRetryStrategy(new DefaultHttpRequestRetryStrategy(3, 
                TimeValue.ofSeconds(1)))
            .build();
        
        factory.setHttpClient(httpClient);
        template.setRequestFactory(factory);
        
        return template;
    }
    
    private PoolingHttpClientConnectionManager createConnectionManager() {
        return PoolingHttpClientConnectionManagerBuilder.create()
            .setMaxConnTotal(200)           // Total connections
            .setMaxConnPerRoute(50)          // Per host
            .setValidateAfterInactivity(TimeValue.ofSeconds(10))
            .build();
    }
}
```

### 4.2. Partial Failure Handling

```java
@Service
@Slf4j
public class OrderAggregatorService {
    
    /**
     * Gọi nhiều service, xử lý partial failure gracefully
     */
    public OrderDetailsResponse getOrderDetails(String orderId) {
        OrderDetailsResponse.OrderDetailsResponseBuilder builder = 
            OrderDetailsResponse.builder();
        
        // Core data - MUST succeed
        Order order = orderService.getOrder(orderId);
        builder.order(order);
        
        // Parallel calls với timeout riêng
        CompletableFuture<CustomerInfo> customerFuture = 
            CompletableFuture.supplyAsync(() -> 
                customerService.getCustomer(order.getCustomerId()))
            .orTimeout(2, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                log.warn("Failed to get customer info, using fallback", ex);
                return CustomerInfo.UNKNOWN;
            });
        
        CompletableFuture<List<Review>> reviewsFuture = 
            CompletableFuture.supplyAsync(() ->
                reviewService.getReviews(orderId))
            .orTimeout(3, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                log.warn("Failed to get reviews, returning empty", ex);
                return Collections.emptyList();
            });
        
        CompletableFuture<ShippingStatus> shippingFuture =
            CompletableFuture.supplyAsync(() ->
                shippingService.getStatus(orderId))
            .orTimeout(2, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                log.warn("Failed to get shipping status", ex);
                return ShippingStatus.UNKNOWN;
            });
        
        // Wait for all với overall timeout
        try {
            CompletableFuture.allOf(customerFuture, reviewsFuture, shippingFuture)
                .get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.warn("Overall timeout exceeded, partial data available");
        }
        
        return builder
            .customer(customerFuture.getNow(CustomerInfo.UNKNOWN))
            .reviews(reviewsFuture.getNow(Collections.emptyList()))
            .shipping(shippingFuture.getNow(ShippingStatus.UNKNOWN))
            // Indicate partial data
            .dataCompleteness(calculateCompleteness(customerFuture, reviewsFuture, shippingFuture))
            .build();
    }
}
```

---

## 5. Database Edge Cases

### 5.1. Connection Pool Exhaustion

```
Scenario:
1. Service A gọi DB query (10s)
2. Nhiều request đồng thời → Pool hết connection
3. Thread block chờ connection
4. Request timeout → Connection không được trả lại đúng cách
5. Pool exhausted → Service down!
```

**Giải pháp: HikariCP Configuration + Monitoring**

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 3000     # 3s - fail fast
      idle-timeout: 600000         # 10 min
      max-lifetime: 1800000        # 30 min
      leak-detection-threshold: 30000  # 30s - phát hiện connection leak
      
      # Metrics
      register-mbeans: true
```

```java
@Component
@Slf4j
public class HikariMetricsMonitor {
    
    private final HikariDataSource dataSource;
    private final MeterRegistry meterRegistry;
    
    @Scheduled(fixedDelay = 10000)
    public void logPoolMetrics() {
        HikariPoolMXBean pool = dataSource.getHikariPoolMXBean();
        
        int activeConnections = pool.getActiveConnections();
        int idleConnections = pool.getIdleConnections();
        int totalConnections = pool.getTotalConnections();
        int threadsAwaitingConnection = pool.getThreadsAwaitingConnection();
        
        // Alert nếu pool gần exhausted
        double utilizationRate = (double) activeConnections / totalConnections;
        
        if (utilizationRate > 0.8) {
            log.warn("Connection pool high utilization: {}%", 
                utilizationRate * 100);
            
            if (threadsAwaitingConnection > 0) {
                log.error("Threads waiting for connection: {}", 
                    threadsAwaitingConnection);
            }
        }
        
        // Record metrics
        meterRegistry.gauge("hikari.connections.active", activeConnections);
        meterRegistry.gauge("hikari.connections.pending", threadsAwaitingConnection);
    }
}
```

### 5.2. Deadlock Detection & Prevention

```java
@Service
@Slf4j
public class DeadlockPreventionService {
    
    /**
     * Luôn lock theo thứ tự để tránh deadlock
     */
    @Transactional(timeout = 10)
    public void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        // QUAN TRỌNG: Lock theo thứ tự ID để tránh deadlock
        Long firstId = Math.min(fromAccountId, toAccountId);
        Long secondId = Math.max(fromAccountId, toAccountId);
        
        Account first = accountRepository.findByIdWithPessimisticLock(firstId);
        Account second = accountRepository.findByIdWithPessimisticLock(secondId);
        
        Account from = fromAccountId.equals(firstId) ? first : second;
        Account to = toAccountId.equals(firstId) ? first : second;
        
        // Business logic
        from.debit(amount);
        to.credit(amount);
        
        accountRepository.saveAll(List.of(from, to));
    }
    
    /**
     * Retry với exponential backoff cho deadlock
     */
    @Retryable(
        retryFor = {DeadlockLoserDataAccessException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100, multiplier = 2)
    )
    @Transactional
    public void updateWithRetry(Long id, UpdateRequest request) {
        // Business logic với potential deadlock
    }
}
```

### 5.3. N+1 Query Detection

```java
@Configuration
public class QueryCounterConfig {
    
    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return properties -> {
            StatisticsImplementor statistics = new StatisticsImpl();
            properties.put("hibernate.stats.factory", 
                () -> statistics);
            properties.put("hibernate.generate_statistics", true);
        };
    }
}

@Aspect
@Component
@Profile("!production")
public class N1QueryDetector {
    
    private static final ThreadLocal<Integer> queryCount = 
        ThreadLocal.withInitial(() -> 0);
    
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object detectN1(ProceedingJoinPoint pjp) throws Throwable {
        queryCount.set(0);
        
        try {
            Object result = pjp.proceed();
            
            int count = queryCount.get();
            if (count > 10) {
                log.warn("Potential N+1 detected in {}: {} queries",
                    pjp.getSignature().toShortString(), count);
            }
            
            return result;
        } finally {
            queryCount.remove();
        }
    }
}
```

---

## 6. Race Conditions & Concurrency

### 6.1. Double-Submit Prevention

```java
@Service
public class DoubleSubmitGuard {
    
    private final StringRedisTemplate redis;
    
    /**
     * Atomic check-and-lock để prevent double submit
     */
    public <T> T executeOnce(String operationKey, Supplier<T> operation) {
        String lockKey = "submit:lock:" + operationKey;
        
        // Lua script để atomic check và set
        String luaScript = """
            if redis.call('exists', KEYS[1]) == 1 then
                return 0
            else
                redis.call('setex', KEYS[1], ARGV[1], '1')
                return 1
            end
            """;
        
        Long acquired = redis.execute(
            new DefaultRedisScript<>(luaScript, Long.class),
            List.of(lockKey),
            "30"  // 30 seconds TTL
        );
        
        if (acquired == 0) {
            throw new DuplicateSubmissionException(
                "Operation already in progress: " + operationKey
            );
        }
        
        try {
            return operation.get();
        } catch (Exception e) {
            // Business logic failed → xóa lock để có thể retry
            redis.delete(lockKey);
            throw e;
        }
        // Không xóa lock sau success → prevent re-submit trong TTL
    }
}
```

### 6.2. Optimistic Lock Retry Pattern

```java
@Service
@Slf4j
public class InventoryService {
    
    private static final int MAX_RETRIES = 5;
    private static final long INITIAL_BACKOFF_MS = 50;
    
    /**
     * Retry với jitter cho optimistic locking
     */
    public void decrementStock(Long productId, int quantity) {
        int attempt = 0;
        
        while (attempt < MAX_RETRIES) {
            try {
                doDecrementStock(productId, quantity);
                return; // Success
                
            } catch (OptimisticLockingFailureException e) {
                attempt++;
                
                if (attempt >= MAX_RETRIES) {
                    throw new StockUpdateFailedException(
                        "Failed to update stock after " + MAX_RETRIES + " attempts",
                        e
                    );
                }
                
                // Exponential backoff với jitter
                long backoff = calculateBackoffWithJitter(attempt);
                log.info("Optimistic lock conflict, retry {} after {}ms", 
                    attempt, backoff);
                
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted during retry", ie);
                }
            }
        }
    }
    
    private long calculateBackoffWithJitter(int attempt) {
        long exponentialBackoff = INITIAL_BACKOFF_MS * (1L << attempt);
        long jitter = ThreadLocalRandom.current().nextLong(exponentialBackoff / 2);
        return exponentialBackoff + jitter;
    }
    
    @Transactional
    protected void doDecrementStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
        
        if (product.getStock() < quantity) {
            throw new InsufficientStockException(productId, product.getStock(), quantity);
        }
        
        product.setStock(product.getStock() - quantity);
        productRepository.save(product); // @Version check tại đây
    }
}
```

### 6.3. Read-Your-Writes Consistency

```java
@Service
public class ConsistentReadService {
    
    private final JdbcTemplate primaryJdbc;    // Master
    private final JdbcTemplate replicaJdbc;    // Read replica
    private final RedisTemplate<String, String> redis;
    
    /**
     * Đảm bảo read-your-writes consistency với replication lag
     */
    public Order getOrder(Long orderId, String requesterId) {
        String lastWriteKey = "lastwrite:" + requesterId + ":" + orderId;
        String lastWriteTime = redis.opsForValue().get(lastWriteKey);
        
        if (lastWriteTime != null) {
            Instant writeInstant = Instant.parse(lastWriteTime);
            Instant threshold = Instant.now().minus(5, ChronoUnit.SECONDS);
            
            if (writeInstant.isAfter(threshold)) {
                // Write quá gần → đọc từ primary
                log.debug("Reading from primary for fresh write");
                return findFromPrimary(orderId);
            }
        }
        
        // Safe để đọc từ replica
        return findFromReplica(orderId);
    }
    
    @Transactional
    public Order updateOrder(Long orderId, UpdateOrderRequest request, String requesterId) {
        Order order = findFromPrimary(orderId);
        // Update logic...
        Order saved = orderRepository.save(order);
        
        // Track write time
        String lastWriteKey = "lastwrite:" + requesterId + ":" + orderId;
        redis.opsForValue().set(lastWriteKey, Instant.now().toString(), 
            Duration.ofSeconds(30));
        
        return saved;
    }
}
```

---

## 7. API Design: Error Response Patterns

### 7.1. RFC 7807 Problem Details

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setType(URI.create("https://api.example.com/errors/" + ex.getErrorCode()));
        problem.setTitle(ex.getTitle());
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));
        
        // Custom extensions
        problem.setProperty("errorCode", ex.getErrorCode());
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("traceId", MDC.get("traceId"));
        
        if (ex.getValidationErrors() != null) {
            problem.setProperty("validationErrors", ex.getValidationErrors());
        }
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(problem);
    }
    
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ProblemDetail> handleConcurrency(
            OptimisticLockingFailureException ex, HttpServletRequest request) {
        
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setType(URI.create("https://api.example.com/errors/concurrent-update"));
        problem.setTitle("Concurrent Update Conflict");
        problem.setDetail("The resource was modified by another request. Please retry.");
        problem.setProperty("retryable", true);
        problem.setProperty("suggestedAction", "GET the resource again and retry your update");
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }
    
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ProblemDetail> handleRateLimit(
            RateLimitExceededException ex) {
        
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.TOO_MANY_REQUESTS);
        problem.setTitle("Rate Limit Exceeded");
        problem.setDetail("You have exceeded the request limit. Please wait before retrying.");
        problem.setProperty("retryAfterSeconds", ex.getRetryAfterSeconds());
        problem.setProperty("limit", ex.getLimit());
        problem.setProperty("remaining", 0);
        problem.setProperty("reset", ex.getResetTime());
        
        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .header("Retry-After", String.valueOf(ex.getRetryAfterSeconds()))
            .header("X-RateLimit-Limit", String.valueOf(ex.getLimit()))
            .header("X-RateLimit-Remaining", "0")
            .header("X-RateLimit-Reset", String.valueOf(ex.getResetTime().getEpochSecond()))
            .body(problem);
    }
}
```

### 7.2. Retry-After & Status Code Guide

| Scenario | Status Code | Retry-After | Client Action |
|----------|-------------|-------------|---------------|
| Rate limit | 429 | ✓ (seconds) | Wait và retry |
| Service unavailable | 503 | ✓ (seconds/date) | Wait và retry |
| Conflict/Optimistic lock | 409 | ✗ | Refresh và retry |
| Bad request | 400 | ✗ | Fix request |
| Server error | 500 | ✗ | Alert, manual check |
| Gateway timeout | 504 | ✗ | Có thể retry |

---

## 8. Distributed Tracing & Debugging

### 8.1. Context Propagation Pattern

```java
@Configuration
public class TracingConfig {
    
    @Bean
    public Tracer tracer() {
        return Tracer.builder()
            .sampler(Sampler.create(0.1f))  // Sample 10%
            .build();
    }
}

@Component
public class TracingKafkaInterceptor implements ProducerInterceptor<String, Object> {
    
    private final Tracer tracer;
    
    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> record) {
        Span currentSpan = tracer.currentSpan();
        
        if (currentSpan != null) {
            TraceContext context = currentSpan.context();
            
            record.headers().add("X-Trace-Id", 
                context.traceId().getBytes(StandardCharsets.UTF_8));
            record.headers().add("X-Span-Id", 
                context.spanId().getBytes(StandardCharsets.UTF_8));
            record.headers().add("X-Parent-Span-Id", 
                context.parentId().getBytes(StandardCharsets.UTF_8));
        }
        
        return record;
    }
}

@Service
public class TracingAwareService {
    
    private final Tracer tracer;
    
    public void processWithTracing(String operation, Runnable task) {
        Span span = tracer.newChild(tracer.currentSpan().context())
            .name(operation)
            .start();
        
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            MDC.put("traceId", span.context().traceId());
            MDC.put("spanId", span.context().spanId());
            
            task.run();
            
        } catch (Exception e) {
            span.tag("error", e.getMessage());
            throw e;
        } finally {
            span.finish();
        }
    }
}
```

### 8.2. Structured Logging for Debugging

```java
@Aspect
@Component
@Slf4j
public class DebugLoggingAspect {
    
    private final ObjectMapper objectMapper;
    
    @Around("@annotation(Debuggable)")
    public Object logForDebug(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().toShortString();
        Object[] args = pjp.getArgs();
        
        // Log input (sanitized)
        log.info("→ {} | input: {}", 
            methodName, 
            sanitizeForLogging(args));
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = pjp.proceed();
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("← {} | duration: {}ms | output: {}", 
                methodName, 
                duration,
                sanitizeForLogging(result));
            
            return result;
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("✗ {} | duration: {}ms | error: {} | input: {}",
                methodName,
                duration,
                e.getMessage(),
                sanitizeForLogging(args));
            throw e;
        }
    }
    
    private String sanitizeForLogging(Object obj) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            // Mask sensitive fields
            return json.replaceAll(
                "\"(password|token|secret|cardNumber)\":\"[^\"]+\"",
                "\"$1\":\"***MASKED***\""
            );
        } catch (Exception e) {
            return obj.toString();
        }
    }
}
```

---

## 9. Chaos Engineering & Testing

### 9.1. Failure Injection Testing

```java
@Component
@ConditionalOnProperty(name = "chaos.enabled", havingValue = "true")
public class ChaosMonkey {
    
    @Value("${chaos.latency.probability:0.1}")
    private double latencyProbability;
    
    @Value("${chaos.latency.max-ms:3000}")
    private long maxLatencyMs;
    
    @Value("${chaos.exception.probability:0.05}")
    private double exceptionProbability;
    
    @Around("@within(org.springframework.stereotype.Service)")
    public Object injectChaos(ProceedingJoinPoint pjp) throws Throwable {
        
        // Random latency injection
        if (ThreadLocalRandom.current().nextDouble() < latencyProbability) {
            long delay = ThreadLocalRandom.current().nextLong(maxLatencyMs);
            log.warn("CHAOS: Injecting {}ms latency into {}", 
                delay, pjp.getSignature().toShortString());
            Thread.sleep(delay);
        }
        
        // Random exception injection
        if (ThreadLocalRandom.current().nextDouble() < exceptionProbability) {
            log.warn("CHAOS: Injecting exception into {}", 
                pjp.getSignature().toShortString());
            throw new RuntimeException("CHAOS: Simulated failure");
        }
        
        return pjp.proceed();
    }
}
```

### 9.2. Integration Testing Patterns

```java
@SpringBootTest
@Testcontainers
@AutoConfigureWireMock(port = 0)
class OrderServiceIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
    
    @Container
    static KafkaContainer kafka = new KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:latest"));
    
    @Autowired
    private OrderService orderService;
    
    @Test
    void shouldHandlePaymentServiceTimeout() {
        // Given: Payment service times out
        stubFor(post(urlPathEqualTo("/payments"))
            .willReturn(aResponse()
                .withStatus(200)
                .withFixedDelay(5000))); // 5s delay
        
        // When
        assertThatThrownBy(() -> 
            orderService.createOrder(testOrderRequest()))
            .isInstanceOf(PaymentTimeoutException.class);
        
        // Then: Order should be in PENDING_PAYMENT state
        Order order = orderRepository.findByIdempotencyKey(testIdempotencyKey());
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING_PAYMENT);
        
        // And: Compensation should be triggered
        verify(kafkaTemplate, times(1))
            .send(eq("order-compensation"), any());
    }
    
    @Test
    void shouldRecoverFromDatabaseFailure() {
        // Given: Database temporarily unavailable
        postgres.stop();
        
        // When: Create order fails
        assertThatThrownBy(() -> 
            orderService.createOrder(testOrderRequest()))
            .isInstanceOf(DataAccessException.class);
        
        // Restart database
        postgres.start();
        
        // Then: Retry should succeed
        OrderResponse response = orderService.createOrder(testOrderRequest());
        assertThat(response.getStatus()).isEqualTo("CREATED");
    }
}
```

---

## 10. Production Checklist

### 10.1. Pre-Production Review

| Category | Item | Status |
|----------|------|--------|
| **Idempotency** | Tất cả write operations có idempotency key | ☐ |
| **Timeouts** | Connection, read, request timeout đã configure | ☐ |
| **Retries** | Retry với exponential backoff cho transient errors | ☐ |
| **Circuit Breaker** | External calls có circuit breaker | ☐ |
| **DLQ** | Kafka consumers có DLQ configured | ☐ |
| **Alerts** | Critical path có alerting | ☐ |
| **Metrics** | Business & technical metrics exposed | ☐ |
| **Tracing** | Distributed tracing enabled | ☐ |
| **Health Checks** | Liveness & readiness probes | ☐ |
| **Rate Limiting** | API rate limits configured | ☐ |

### 10.2. Monitoring Dashboard Essentials

```yaml
# Prometheus alert rules example
groups:
  - name: microservice-alerts
    rules:
      - alert: HighErrorRate
        expr: rate(http_requests_total{status=~"5.."}[5m]) / rate(http_requests_total[5m]) > 0.05
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "High error rate detected"
          
      - alert: KafkaConsumerLag
        expr: kafka_consumer_lag_sum > 10000
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Kafka consumer lag is high"
          
      - alert: DatabaseConnectionPoolExhausted
        expr: hikaricp_connections_pending > 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "Database connection pool exhausted"
          
      - alert: CircuitBreakerOpen
        expr: resilience4j_circuitbreaker_state{state="open"} == 1
        for: 0m
        labels:
          severity: warning
        annotations:
          summary: "Circuit breaker is open"
```

### 10.3. Runbook Template

```markdown
## Incident: [Name]

### Severity Level
- [ ] P1 - Critical: Revenue impact, complete outage
- [ ] P2 - High: Degraded service, partial impact
- [ ] P3 - Medium: Non-critical feature affected
- [ ] P4 - Low: Cosmetic or logging issue

### Detection
- **Alert**: [Alert name]
- **Metric**: [Which metric triggered]
- **Threshold**: [What threshold was crossed]

### Initial Assessment
1. Check Grafana dashboard: [link]
2. Check recent deployments: [link]
3. Check external dependencies: [checklist]

### Mitigation Steps
1. [Step 1]
2. [Step 2]
3. [Rollback procedure if needed]

### Escalation
- On-call: @team-oncall
- Slack channel: #incidents
- PagerDuty: [policy]

### Post-Incident
- [ ] Create incident timeline
- [ ] Write post-mortem
- [ ] Create follow-up tickets
```

---

## 📚 Tham khảo thêm

| Topic | Resource |
|-------|----------|
| Saga Pattern | [Microservices.io - Saga](https://microservices.io/patterns/data/saga.html) |
| Outbox Pattern | [Debezium Documentation](https://debezium.io/blog/2020/02/10/event-sourcing-vs-cdc/) |
| Kafka Error Handling | [Confluent - Error Handling](https://www.confluent.io/blog/error-handling-patterns-in-kafka/) |
| Distributed Tracing | [OpenTelemetry Documentation](https://opentelemetry.io/docs/) |
| Chaos Engineering | [Principles of Chaos](https://principlesofchaos.org/) |
| Circuit Breaker | [Resilience4j Documentation](https://resilience4j.readme.io/) |

---

> 💡 **Tip Senior/Staff**: Document mọi production incident và lessons learned. Mỗi bug là một cơ hội để xây dựng hệ thống mạnh mẽ hơn.

*Tài liệu này được tạo để hỗ trợ quá trình nâng cao kỹ năng từ Mid → Senior/Staff Engineer.*
