# JPA/Hibernate Advanced Case Studies (Senior Level)

> Các tình huống thực tế phức tạp trong production systems

---

## 📚 Table of Contents

1. [N+1 Query Problem - The Silent Killer](#case-1-n1-query-problem)
2. [LazyInitializationException - Production Nightmare](#case-2-lazyinitializationexception)
3. [Optimistic vs Pessimistic Locking - Banking System](#case-3-locking-strategies)
4. [Batch Processing Performance - 100K Records](#case-4-batch-processing)
5. [Bidirectional Relationship Pitfalls](#case-5-bidirectional-relationships)
6. [Second-Level Cache in Multi-Instance](#case-6-second-level-cache)
7. [Entity Inheritance Strategies](#case-7-entity-inheritance)
8. [Transaction Propagation - Tricky Scenarios](#case-8-transaction-propagation)

---

## CASE 1: N+1 Query Problem

### 🎯 SCENARIO: E-Commerce Order Listing

**Requirement:** Hiển thị danh sách 1000 orders với:
- Order info
- Customer name
- Product names (5 products/order trung bình)
- Total price

### ❌ BAD CODE (The Silent Killer)

```java
// Query 1: Load all orders
List<Order> orders = orderRepository.findAll();  

for (Order order : orders) {
    // Query 2, 3, 4, ..., N+1: Load customer cho mỗi order
    String customerName = order.getCustomer().getName();
    
    // Query N+2, N+3, ...: Load items cho mỗi order
    for (OrderItem item : order.getItems()) {
        String productName = item.getProduct().getName();
    }
}
```

**💀 Result:** 1000 orders → **1 + 1000 + 5000 = 6001 queries!**
- Response time: 30 seconds
- Database load: 100%
- User: Timeout!

### WHY IT HAPPENS

```java
@Entity
class Order {
    @ManyToOne(fetch = FetchType.LAZY)  // Default
    private Customer customer;
    
    @OneToMany(fetch = FetchType.LAZY)  // Default
    private List<OrderItem> items;
}
```

→ Lazy loading triggers query **mỗi lần access!**

---

### ✅ SOLUTION 1: JOIN FETCH (Simple cases)

```java
// Step 1: Fetch orders với customers
@Query("""
    SELECT o FROM Order o 
    JOIN FETCH o.customer
    WHERE o.orderDate >= :startDate
""")
List<Order> findOrdersWithCustomer(@Param("startDate") LocalDate date);

// Step 2: Fetch items với products
@Query("""
    SELECT DISTINCT o FROM Order o 
    JOIN FETCH o.items i
    JOIN FETCH i.product
    WHERE o IN :orders
""")
List<Order> findOrdersWithItems(@Param("orders") List<Order> orders);
```

**✅ Result:** 2 queries thay vì 6001!

**⚠️ WARNING:** Không thể JOIN FETCH nhiều collections cùng lúc!

```sql
-- ❌ MultipleBagFetchException
SELECT o FROM Order o 
JOIN FETCH o.items      
JOIN FETCH o.payments   -- Error!
```

---

### ✅ SOLUTION 2: Entity Graph (Flexible & Reusable)

```java
@NamedEntityGraph(
    name = "Order.detail",
    attributeNodes = {
        @NamedAttributeNode("customer"),
        @NamedAttributeNode(
            value = "items",
            subgraph = "items-subgraph"
        )
    },
    subgraphs = {
        @NamedSubgraph(
            name = "items-subgraph",
            attributeNodes = {
                @NamedAttributeNode("product")
            }
        )
    }
)
@Entity
class Order { ... }

// Usage:
EntityGraph<?> graph = entityManager.getEntityGraph("Order.detail");

List<Order> orders = entityManager
    .createQuery("SELECT o FROM Order o", Order.class)
    .setHint("javax.persistence.fetchgraph", graph)
    .getResultList();
```

**✅ Chỉ 1 query với tất cả JOINs!**

---

### ✅ SOLUTION 3: @BatchSize (Fallback)

```java
@Entity
class Order {
    @OneToMany(mappedBy = "order")
    @BatchSize(size = 25)  // ⭐ Hibernate-specific
    private List<OrderItem> items;
}
```

**Result:** 1000 orders → 1 + ceil(1000/25) + ceil(5000/25) = **241 queries**

- ✅ Tốt hơn 6001 queries
- ❌ Không tối ưu bằng JOIN FETCH

**Use case:** Dynamic conditions không dùng được JOIN FETCH

---

### ✅ SOLUTION 4: DTO Projection (Best for read-only)

```java
record OrderDTO(
    Long id,
    String customerName,
    Date orderDate,
    List<OrderItemDTO> items
) {}

@Query("""
    SELECT new com.example.OrderDTO(
        o.id,
        o.customer.name,
        o.orderDate,
        (SELECT new com.example.OrderItemDTO(
            i.product.name,
            i.quantity,
            i.price
        ) FROM OrderItem i WHERE i.order = o)
    )
    FROM Order o
""")
List<OrderDTO> findOrderDTOs();
```

**Benefits:**
- ✅ No entity overhead
- ✅ Only fetch needed data
- ✅ Best performance

---

### 🔍 DETECTION TOOLS

#### 1. Hibernate Statistics

```properties
spring.jpa.properties.hibernate.generate_statistics=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

#### 2. Datasource-proxy (Production-ready)

```xml
<dependency>
    <groupId>net.ttddyy</groupId>
    <artifactId>datasource-proxy</artifactId>
</dependency>
```

#### 3. Spring Boot Actuator

Monitor metric: `hibernate.query.executions`
Alert if > threshold

---

## CASE 2: LazyInitializationException

### 🎯 SCENARIO: REST API với Service Layer

### ❌ BAD CODE

```java
// Service
@Transactional
public Order getOrder(Long id) {
    return orderRepository.findById(id);
}  // ← Transaction CLOSED here!

// Controller
Order order = orderService.getOrder(1L);

// 💀 LazyInitializationException!
String customerName = order.getCustomer().getName();
```

---

### 💀 ANTI-PATTERN 1: EAGER FETCH EVERYWHERE

```java
@ManyToOne(fetch = FetchType.EAGER)  // ❌ NEVER DO THIS!
private Customer customer;

@OneToMany(fetch = FetchType.EAGER)  // ❌ WORSE!
private List<OrderItem> items;
```

**Problems:**
- Load data không cần thiết
- Waste memory & bandwidth
- Cascade EAGER loading
- Cannot override to LAZY at runtime

---

### 💀 ANTI-PATTERN 2: Open Session In View (OSIV)

```properties
# application.properties
spring.jpa.open-in-view=true  # ❌ Default in Spring Boot!
```

**Problems:**
1. Database connection held quá lâu (entire HTTP request)
2. Hidden N+1 problems (chỉ xuất hiện production)
3. Performance degradation under load
4. Cannot scale horizontally
5. Accidental lazy loading ở view layer

**✅ ALWAYS DISABLE:**
```properties
spring.jpa.open-in-view=false
```

---

### ✅ SOLUTION 1: DTO Projection (RECOMMENDED)

```java
record OrderDTO(
    Long id,
    String customerName,
    String customerEmail,
    Date orderDate,
    List<OrderItemDTO> items
) {}

@Query("""
    SELECT new com.example.OrderDTO(
        o.id,
        o.customer.name,
        o.customer.email,
        o.orderDate,
        (SELECT new com.example.OrderItemDTO(
            i.product.name,
            i.quantity,
            i.price
        ) FROM OrderItem i WHERE i.order = o)
    )
    FROM Order o
    WHERE o.id = :orderId
""")
OrderDTO findOrderDTO(@Param("orderId") Long orderId);
```

**Benefits:**
- ✅ No LazyInitializationException
- ✅ Only fetch needed data
- ✅ Type-safe
- ✅ Testable
- ✅ Clear contract

---

### ✅ SOLUTION 2: Explicit JOIN FETCH

```java
@Transactional(readOnly = true)
public Order getOrderWithDetails(Long id) {
    // Query 1: Fetch order with customer
    Order order = entityManager.createQuery(
        "SELECT o FROM Order o " +
        "JOIN FETCH o.customer " +
        "WHERE o.id = :id",
        Order.class
    ).setParameter("id", id)
     .getSingleResult();
    
    // Query 2: Fetch items with products
    entityManager.createQuery(
        "SELECT DISTINCT o FROM Order o " +
        "JOIN FETCH o.items i " +
        "JOIN FETCH i.product " +
        "WHERE o = :order",
        Order.class
    ).setParameter("order", order)
     .getSingleResult();
    
    return order;  // Fully initialized!
}
```

**Trade-offs:**
- ✅ All associations loaded
- ✅ Only 2 queries
- ❌ Returns entity (not DTO)

---

### ✅ SOLUTION 3: Hibernate.initialize() (Tactical)

```java
@Transactional
public Order getOrderWithDetails(Long id) {
    Order order = orderRepository.findById(id);
    
    // Force initialization INSIDE transaction
    Hibernate.initialize(order.getCustomer());
    Hibernate.initialize(order.getItems());
    
    order.getItems().forEach(item -> {
        Hibernate.initialize(item.getProduct());
    });
    
    return order;
}
```

**Trade-offs:**
- ✅ Explicit control
- ✅ Easy to debug
- ❌ Imperative code
- ❌ Easy to forget
- ❌ Not type-safe

---

### 📊 DECISION TREE

```
Read-only query?
    → Use DTO Projection ✅

Need to modify entity?
    → JOIN FETCH + return entity

Dynamic loading based on condition?
    → Hibernate.initialize() carefully

Complex nested object graph?
    → Entity Graph + DTO mapping
```

---

## CASE 3: Locking Strategies

### 🎯 SCENARIO: Banking - Concurrent Withdrawals

**Initial balance:** $1000

**Threads:**
- Thread 1: Withdraw $600
- Thread 2: Withdraw $500

### ❌ WITHOUT LOCKING (Lost Update Problem)

```java
T1: Read balance = $1000
T2: Read balance = $1000  (still reads $1000!)
T1: Write balance = $400  ($1000 - $600)
T2: Write balance = $500  ($1000 - $500)  ← Overwrites T1!
```

**💀 Final balance:** $500 (WRONG! Should be -$100 or reject T2)
**💰 Lost $600!** Critical bug!

---

### ✅ SOLUTION 1: Optimistic Locking

```java
@Entity
class BankAccount {
    @Id
    private Long id;
    
    private Double balance;
    
    @Version  // ⭐ MAGIC COLUMN
    private Long version;
    
    public void withdraw(Double amount) {
        if (balance < amount) {
            throw new InsufficientFundsException();
        }
        balance -= amount;
    }
}

// Service
@Transactional
public void withdraw(Long accountId, Double amount) {
    try {
        BankAccount account = accountRepo.findById(accountId);
        account.withdraw(amount);
        
    } catch (OptimisticLockException e) {
        throw new ConcurrentModificationException(
            "Account modified. Please retry."
        );
    }
}
```

#### HOW IT WORKS

```sql
T1: Read: id=1, balance=1000, version=1
T2: Read: id=1, balance=1000, version=1

T1: UPDATE bank_account 
    SET balance=400, version=2 
    WHERE id=1 AND version=1  -- Success! 1 row updated

T2: UPDATE bank_account 
    SET balance=500, version=2 
    WHERE id=1 AND version=1  -- Fail! 0 rows (version=2 now)
```

→ OptimisticLockException for T2
→ T2 can retry with new version

**Pros:**
- ✅ High concurrency
- ✅ No database locks
- ✅ Better performance
- ✅ Scalable

**Cons:**
- ❌ Requires retry logic
- ❌ May fail under high contention
- ❌ User sees "Please retry"

---

### ✅ SOLUTION 2: Pessimistic Locking

```java
@Repository
interface BankAccountRepository {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM BankAccount a WHERE a.id = :id")
    BankAccount findByIdWithLock(@Param("id") Long id);
}

// Service
@Transactional
public void withdraw(Long accountId, Double amount) {
    BankAccount account = accountRepo
        .findByIdWithLock(accountId);  // SELECT ... FOR UPDATE
    
    account.withdraw(amount);  // Safe!
}
```

#### SQL Generated

```sql
SELECT * FROM bank_account WHERE id = ? FOR UPDATE
```

#### HOW IT WORKS

```
T1: SELECT ... FOR UPDATE → Acquires ROW LOCK ✅
T2: SELECT ... FOR UPDATE → BLOCKS, waits for T1

T1: Updates and commits → Releases lock
T2: Now acquires lock, reads balance=400
T2: Tries to withdraw $500 → InsufficientFundsException ✅
```

**Pros:**
- ✅ Prevents conflicts upfront
- ✅ No retry needed
- ✅ Guaranteed consistency

**Cons:**
- ❌ Reduces concurrency (blocking)
- ❌ Connection held longer
- ❌ Potential deadlocks
- ❌ Lower throughput

---

### 📊 COMPARISON

| Aspect | Optimistic | Pessimistic |
|--------|-----------|-------------|
| Concurrency | High ✅ | Low ❌ |
| Conflict handling | Detect at commit | Prevent upfront |
| DB resources | Minimal ✅ | Locks ❌ |
| Throughput | High ✅ | Medium ❌ |
| User experience | Retry prompt ❌ | Just waits ✅ |
| Deadlock risk | None ✅ | Possible ⚠️ |
| Complexity | Retry logic ❌ | Simple ✅ |

---

### 🎯 DECISION RULES

#### Use OPTIMISTIC when:
- ✅ Low contention (< 10% conflict rate)
- ✅ Read-heavy workload
- ✅ High scalability needed
- ✅ Can afford retry logic

**Examples:**
- Blog post editing
- Product inventory (many readers)
- User profile updates

#### Use PESSIMISTIC when:
- ✅ High contention (> 30% conflict rate)
- ✅ Critical operations
- ✅ Financial transactions
- ✅ Cannot afford retries

**Examples:**
- Bank transfers
- Ticket booking (limited seats)
- Sequential number generation
- Accounting journal entries

---

### 🚀 ADVANCED: Hybrid Approach

```java
@Transactional
public void withdraw(Long accountId, Double amount) {
    BankAccount account = accountRepo.findById(accountId);
    
    // Large amount → Use pessimistic lock
    if (amount > 10000) {
        account = accountRepo.findByIdWithLock(accountId);
    }
    
    account.withdraw(amount);
}
```

✅ Best of both worlds!

---

## CASE 4: Batch Processing

### 🎯 SCENARIO: Import 100,000 Products from CSV

### ❌ BAD CODE (OOM + Slow)

```java
@Transactional
public void importProducts(List<ProductDTO> dtos) {
    for (ProductDTO dto : dtos) {  // 100,000 items
        Product product = new Product(dto);
        entityManager.persist(product);
    }
    // entityManager caches ALL 100,000 entities → OOM! 💀
}
```

**Problems:**
1. First-level cache holds all entities → 500MB+ memory
2. 100,000 individual INSERTs → 5 minutes
3. OutOfMemoryError after ~50K records

---

### ✅ SOLUTION: Batch Processing

#### Configuration

```properties
# application.properties
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
spring.jpa.properties.hibernate.batch_versioned_data=true
```

#### Entity Setup

```java
@Entity
class Product {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,  // ✅ Not IDENTITY!
        generator = "product_seq"
    )
    @SequenceGenerator(
        name = "product_seq",
        sequenceName = "product_sequence",
        allocationSize = 50  // Match batch_size
    )
    private Long id;
    
    private String name;
    private Double price;
}
```

#### Service Implementation

```java
@Transactional
public void importProducts(List<ProductDTO> dtos) {
    int batchSize = 50;
    
    for (int i = 0; i < dtos.size(); i++) {
        Product product = new Product(dtos.get(i));
        entityManager.persist(product);
        
        if (i > 0 && i % batchSize == 0) {
            // Flush batch to database
            entityManager.flush();
            
            // Clear first-level cache ⭐ CRITICAL!
            entityManager.clear();
        }
    }
    
    // Flush remaining
    entityManager.flush();
    entityManager.clear();
}
```

**✅ Results:**
- Memory: Constant ~10MB (only 50 entities cached)
- Speed: 100,000 records in 30 seconds (vs 5 minutes)
- Batching: 2,000 batch INSERTs

---

### ⚠️ WHY NOT IDENTITY Generator?

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)  // ❌
```

**Problem:** IDENTITY requires immediate INSERT to get ID
→ Disables batching! (must INSERT one-by-one)

**SEQUENCE generator:**
→ Pre-allocates IDs in memory
→ Can batch 50 INSERTs together
→ **10x faster!**

---

### 🚀 ADVANCED: Pure JDBC Batch

```java
@Autowired
private JdbcTemplate jdbcTemplate;

public void importProductsJdbc(List<ProductDTO> dtos) {
    String sql = "INSERT INTO product (id, name, price) VALUES (?, ?, ?)";
    
    List<Object[]> batchArgs = dtos.stream()
        .map(dto -> new Object[]{
            sequenceGenerator.nextId(),
            dto.name(),
            dto.price()
        })
        .toList();
    
    jdbcTemplate.batchUpdate(sql, batchArgs);
}
```

**Trade-offs:**
- ✅ 3x faster than Hibernate batch
- ✅ Minimal memory
- ❌ No entity callbacks (@PrePersist)
- ❌ No validation

---

## CASE 5: Bidirectional Relationships

### 🎯 PROBLEM: Infinite JSON + Sync Issues

```java
@Entity
class Author {
    @Id
    private Long id;
    private String name;
    
    @OneToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();
}

@Entity
class Book {
    @Id
    private Long id;
    private String title;
    
    @ManyToOne
    private Author author;
}
```

### ❌ PITFALL 1: Không sync cả 2 chiều

```java
Book book = new Book("Clean Code");
author.getBooks().add(book);  // ❌ Chỉ set 1 chiều!
// book.author vẫn null!
```

### ✅ SOLUTION: Helper Methods

```java
@Entity
class Author {
    // ... fields ...
    
    public void addBook(Book book) {
        books.add(book);
        book.setAuthor(this);  // ⭐ Sync both sides
    }
    
    public void removeBook(Book book) {
        books.remove(book);
        book.setAuthor(null);
    }
}
```

### ❌ PITFALL 2: Infinite JSON Recursion

```json
{
  "id": 1,
  "name": "Robert Martin",
  "books": [
    {
      "id": 1,
      "title": "Clean Code",
      "author": {
        "id": 1,
        "name": "Robert Martin",
        "books": [ ... ]  // ← Infinite loop!
      }
    }
  ]
}
```

### ✅ SOLUTIONS

#### Option 1: @JsonManagedReference / @JsonBackReference

```java
@Entity
class Author {
    @OneToMany(mappedBy = "author")
    @JsonManagedReference
    private List<Book> books;
}

@Entity
class Book {
    @ManyToOne
    @JsonBackReference
    private Author author;  // Won't serialize
}
```

#### Option 2: @JsonIgnore

```java
@Entity
class Book {
    @ManyToOne
    @JsonIgnore  // Simple but loses data
    private Author author;
}
```

#### Option 3: DTOs (BEST!)

```java
record AuthorDTO(Long id, String name, List<String> bookTitles) {}

// Clean, controlled, no recursion
```

---

## CASE 6: Second-Level Cache

### 🎯 PROBLEM: Stale Data in Multi-Instance

**Scenario:**
- 3 application servers
- Each has local Ehcache (2nd level cache)
- Product price updated on Server 1
- Server 2 & 3 still see old price!

### ✅ SOLUTION 1: Distributed Cache

```xml
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast-hibernate53</artifactId>
</dependency>
```

```properties
hibernate.cache.region.factory_class=\
    com.hazelcast.hibernate.HazelcastCacheRegionFactory
```

### ✅ SOLUTION 2: Cache Only Immutable Entities

```java
@Entity
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
class Country {
    // Countries rarely change
}
```

### ✅ SOLUTION 3: Monitor Cache Hit Ratio

```properties
spring.jpa.properties.hibernate.generate_statistics=true
```

**Watch metrics:**
- `hibernate.cache.second_level_cache.hit_count`
- `hibernate.cache.second_level_cache.miss_count`

**Target:** Hit ratio > 70%

---

## CASE 7: Entity Inheritance

### 🎯 SCENARIO: Payment System

### STRATEGY 1: SINGLE_TABLE (Default)

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type")
abstract class Payment {
    @Id
    private Long id;
    private Double amount;
}

@Entity
@DiscriminatorValue("CREDIT_CARD")
class CreditCardPayment extends Payment {
    private String cardNumber;
}

@Entity
@DiscriminatorValue("PAYPAL")
class PayPalPayment extends Payment {
    private String email;
}
```

**Pros:**
- ✅ Best query performance (no JOINs)
- ✅ Polymorphic queries easy

**Cons:**
- ❌ Nullable columns (waste space)
- ❌ Table grows large

---

### STRATEGY 2: JOINED (Normalized)

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Payment {
    @Id
    private Long id;
    private Double amount;
}

@Entity
@Table(name = "credit_card_payment")
class CreditCardPayment extends Payment {
    private String cardNumber;  // Own table
}
```

**Pros:**
- ✅ Normalized
- ✅ Clean data model

**Cons:**
- ❌ Requires JOINs (slower)
- ❌ Complex queries

---

### STRATEGY 3: TABLE_PER_CLASS (Avoid!)

**Cons:**
- ❌ Polymorphic queries use UNION (very slow!)
- ❌ Duplicate columns
- ❌ Never use in production

---

### 📊 COMPARISON

| Strategy | Tables | Queries | Recommendation |
|----------|--------|---------|----------------|
| SINGLE_TABLE | 1 | Fast ✅ | Default choice |
| JOINED | N+1 | Slow ❌ | Highly related |
| TABLE_PER_CLASS | N | Slow ❌ | Avoid |

---

## CASE 8: Transaction Propagation

### 🎯 SCENARIO: Order Processing với Audit Log

**Requirement:** Audit log MUST persist even if order fails

### ❌ BAD CODE

```java
@Transactional
public void processOrder(Order order) {
    orderRepository.save(order);
    auditService.log("Order created");  // Same transaction!
    
    if (order.getAmount() > 10000) {
        throw new BusinessException("Amount too high");
    }
}

// Audit log ROLLBACK cùng với order! 💀
```

---

### ✅ SOLUTION: REQUIRES_NEW

```java
@Transactional
public void processOrder(Order order) {
    try {
        orderRepository.save(order);
        
        if (order.getAmount() > 10000) {
            throw new BusinessException("Amount too high");
        }
        
        auditService.log("Order created");
        
    } catch (Exception e) {
        auditService.log("Order failed: " + e.getMessage());
        throw e;
    }
}

// AuditService
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void log(String message) {
    auditRepository.save(new AuditLog(message));
}  // ⭐ Independent transaction!
```

**Result:**
- Order fails → rollback
- Audit logs persist → ✅ Both logs saved!

---

### 📊 PROPAGATION CHEAT SHEET

| Type | Behavior |
|------|----------|
| **REQUIRED** | Use existing or create new (default) |
| **REQUIRES_NEW** | Suspend outer, create new ⭐ |
| **NESTED** | Nested tx (savepoint), rare |
| **MANDATORY** | Must have existing, else exception |
| **NEVER** | Must NOT have tx, else exception |
| **NOT_SUPPORTED** | Suspend tx, run non-transactional |
| **SUPPORTS** | Use existing if available |

**Most used:** REQUIRED (95%), REQUIRES_NEW (4%), Others (1%)

---

## 🎯 KEY TAKEAWAYS

1. **Always monitor queries** (N+1 detection)
2. **Disable OSIV:** `spring.jpa.open-in-view=false`
3. **Use DTO projections** for read-only
4. **Optimistic locking** by default, pessimistic when critical
5. **Batch processing:** flush + clear every 50 entities
6. **Avoid IDENTITY generator** for batch inserts
7. **Distributed cache** or no cache
8. **REQUIRES_NEW** for independent transactions

---

## 📚 FURTHER READING

### Books
- **"High-Performance Java Persistence"** by Vlad Mihalcea
- **"Java Persistence with Hibernate"** by Christian Bauer

### Resources
- https://vladmihalcea.com/blog/
- https://thorben-janssen.com/
- Spring Data JPA Documentation

---

## 💡 PRACTICE EXERCISES

1. **N+1 Detection:** Add logging to your existing project, find N+1 problems
2. **Lock Testing:** Create concurrent withdrawal test with JUnit + CountDownLatch
3. **Batch Performance:** Measure time for 10K inserts with/without batching
4. **Cache Analysis:** Monitor cache hit ratio in your application
5. **Transaction Debugging:** Use TransactionSynchronizationManager to track boundaries

---

**Tài liệu này tổng hợp kinh nghiệm thực tế từ production systems.**
**Áp dụng cẩn thận và đo lường performance trước/sau optimization!**
