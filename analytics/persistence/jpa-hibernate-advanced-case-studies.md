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

---

## CASE 9: Custom EntityManager Operations

### 🎯 SCENARIO: Dynamic Query Builder

**Requirement:** Build flexible search với nhiều optional filters

### ❌ BAD CODE: String Concatenation

```java
public List<Product> searchProducts(ProductSearchDTO criteria) {
    String sql = "SELECT p FROM Product p WHERE 1=1";
    
    if (criteria.getName() != null) {
        sql += " AND p.name LIKE '%" + criteria.getName() + "%'";
    }
    
    if (criteria.getMinPrice() != null) {
        sql += " AND p.price >= " + criteria.getMinPrice();
    }
    
    // ❌ SQL Injection vulnerability!
    // ❌ No parameter binding
    // ❌ Cannot use query cache
    
    return entityManager.createQuery(sql, Product.class)
        .getResultList();
}
```

**Problems:**
- 💀 **SQL Injection** vulnerability
- ❌ No query plan cache
- ❌ Type unsafe
- ❌ Maintenance nightmare

---

### ✅ SOLUTION 1: CriteriaBuilder (Type-safe)

```java
@Repository
public class ProductRepository {
    
    @PersistenceContext
    private EntityManager em;
    
    public List<Product> searchProducts(ProductSearchDTO criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        // Name filter
        if (criteria.getName() != null) {
            predicates.add(
                cb.like(root.get("name"), "%" + criteria.getName() + "%")
            );
        }
        
        // Price range
        if (criteria.getMinPrice() != null) {
            predicates.add(
                cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice())
            );
        }
        
        if (criteria.getMaxPrice() != null) {
            predicates.add(
                cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice())
            );
        }
        
        // Category filter
        if (criteria.getCategoryId() != null) {
            predicates.add(
                cb.equal(root.get("category").get("id"), criteria.getCategoryId())
            );
        }
        
        // Date range
        if (criteria.getCreatedAfter() != null) {
            predicates.add(
                cb.greaterThanOrEqualTo(
                    root.get("createdDate"), 
                    criteria.getCreatedAfter()
                )
            );
        }
        
        // Combine all predicates
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        
        // Sorting
        if ("price".equals(criteria.getSortBy())) {
            query.orderBy(
                criteria.isAscending() 
                    ? cb.asc(root.get("price")) 
                    : cb.desc(root.get("price"))
            );
        }
        
        TypedQuery<Product> typedQuery = em.createQuery(query);
        
        // Pagination
        if (criteria.getPage() != null && criteria.getSize() != null) {
            typedQuery.setFirstResult(criteria.getPage() * criteria.getSize());
            typedQuery.setMaxResults(criteria.getSize());
        }
        
        return typedQuery.getResultList();
    }
}
```

**✅ Benefits:**
- Type-safe (compile-time checking)
- SQL injection proof
- Query plan cache
- Reusable predicates
- IDE autocomplete support

---

### ✅ SOLUTION 2: Specification Pattern (Spring Data JPA)

```java
public class ProductSpecifications {
    
    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> 
            name == null ? null : 
            cb.like(root.get("name"), "%" + name + "%");
    }
    
    public static Specification<Product> priceGreaterThan(Double minPrice) {
        return (root, query, cb) -> 
            minPrice == null ? null : 
            cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }
    
    public static Specification<Product> priceLessThan(Double maxPrice) {
        return (root, query, cb) -> 
            maxPrice == null ? null : 
            cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
    
    public static Specification<Product> belongsToCategory(Long categoryId) {
        return (root, query, cb) -> 
            categoryId == null ? null : 
            cb.equal(root.get("category").get("id"), categoryId);
    }
    
    public static Specification<Product> createdAfter(LocalDate date) {
        return (root, query, cb) -> 
            date == null ? null : 
            cb.greaterThanOrEqualTo(root.get("createdDate"), date);
    }
}

// Repository
public interface ProductRepository extends JpaRepository<Product, Long>, 
                                           JpaSpecificationExecutor<Product> {
}

// Service
@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepo;
    
    public Page<Product> searchProducts(
        ProductSearchDTO criteria, 
        Pageable pageable
    ) {
        Specification<Product> spec = Specification.where(null);
        
        spec = spec.and(ProductSpecifications.hasName(criteria.getName()));
        spec = spec.and(ProductSpecifications.priceGreaterThan(criteria.getMinPrice()));
        spec = spec.and(ProductSpecifications.priceLessThan(criteria.getMaxPrice()));
        spec = spec.and(ProductSpecifications.belongsToCategory(criteria.getCategoryId()));
        spec = spec.and(ProductSpecifications.createdAfter(criteria.getCreatedAfter()));
        
        return productRepo.findAll(spec, pageable);
    }
}
```

**✅ Benefits:**
- Composable và reusable
- Clean separation
- Spring Data integration
- Easy testing

---

### 🎯 CASE 10: Bulk Operations với EntityManager

### SCENARIO: Update 50,000 Product Prices (+10%)

### ❌ BAD CODE: Load All Entities

```java
@Transactional
public void increaseAllPrices() {
    List<Product> products = productRepo.findAll();  // Load 50K entities!
    
    for (Product product : products) {
        product.setPrice(product.getPrice() * 1.1);
    }
    
    // entityManager.flush(); triggers 50,000 UPDATE queries!
}
```

**Problems:**
- 💀 OutOfMemoryError (50K entities cached)
- 💀 50,000 individual UPDATE queries
- 💀 Takes 5+ minutes

---

### ✅ SOLUTION 1: Bulk Update Query

```java
@Transactional
public int increaseAllPrices(double percentage) {
    return entityManager.createQuery(
        "UPDATE Product p SET p.price = p.price * :multiplier"
    )
    .setParameter("multiplier", 1 + percentage / 100)
    .executeUpdate();
}
```

**Generated SQL:**
```sql
UPDATE product SET price = price * 1.1
```

**✅ Benefits:**
- ⚡ Single UPDATE query
- ⚡ Executes in < 1 second
- ✅ No memory overhead
- ✅ Database-side operation

**⚠️ WARNING:**
```java
// Bulk update bypasses entity lifecycle!
// - No @PreUpdate callback
// - No version increment (optimistic locking)
// - First-level cache NOT updated

// Solution: Clear cache after bulk update
entityManager.clear();
```

---

### ✅ SOLUTION 2: Bulk Update với Conditional Logic

```java
@Transactional
public void applyDynamicPricing() {
    // Increase luxury products by 15%
    entityManager.createQuery("""
        UPDATE Product p 
        SET p.price = p.price * 1.15
        WHERE p.category.name = 'Luxury'
    """)
    .executeUpdate();
    
    // Decrease clearance items by 30%
    entityManager.createQuery("""
        UPDATE Product p 
        SET p.price = p.price * 0.7,
            p.status = 'CLEARANCE'
        WHERE p.stock < 10 
        AND p.lastUpdated < :date
    """)
    .setParameter("date", LocalDate.now().minusMonths(6))
    .executeUpdate();
    
    // Clear cache to reflect changes
    entityManager.clear();
}
```

---

### ✅ SOLUTION 3: Bulk Delete

```java
@Transactional
public int deleteOldOrders(int months) {
    int deleted = entityManager.createQuery("""
        DELETE FROM Order o 
        WHERE o.status = 'COMPLETED' 
        AND o.completedDate < :cutoffDate
    """)
    .setParameter("cutoffDate", LocalDate.now().minusMonths(months))
    .executeUpdate();
    
    entityManager.clear();
    
    return deleted;
}
```

**⚠️ CASCADE WARNING:**
```java
// Bulk DELETE does NOT trigger cascade!

@Entity
class Order {
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderItem> items;  // ❌ NOT deleted by bulk DELETE!
}

// Solution: Manual cascade delete
entityManager.createQuery("DELETE FROM OrderItem i WHERE i.order.id IN ...")
    .executeUpdate();
entityManager.createQuery("DELETE FROM Order o WHERE ...").executeUpdate();
```

---

### 🎯 CASE 11: Native Queries với EntityManager

### SCENARIO: Complex Reporting Query

**Requirement:** Monthly sales report với:
- Total revenue per category
- Product count
- Average price
- Window functions

### ✅ SOLUTION 1: Native Query với Scalar Results

```java
public List<MonthlySalesReport> getMonthlySalesReport(
    int year, 
    int month
) {
    String sql = """
        SELECT 
            c.name AS category_name,
            COUNT(DISTINCT p.id) AS product_count,
            SUM(oi.quantity * oi.price) AS total_revenue,
            AVG(oi.price) AS avg_price,
            RANK() OVER (ORDER BY SUM(oi.quantity * oi.price) DESC) AS revenue_rank
        FROM order_items oi
        JOIN products p ON oi.product_id = p.id
        JOIN categories c ON p.category_id = c.id
        JOIN orders o ON oi.order_id = o.id
        WHERE YEAR(o.order_date) = :year 
        AND MONTH(o.order_date) = :month
        GROUP BY c.id, c.name
        ORDER BY total_revenue DESC
    """;
    
    @SuppressWarnings("unchecked")
    List<Object[]> results = entityManager.createNativeQuery(sql)
        .setParameter("year", year)
        .setParameter("month", month)
        .getResultList();
    
    return results.stream()
        .map(row -> new MonthlySalesReport(
            (String) row[0],      // category_name
            ((Number) row[1]).intValue(),    // product_count
            ((Number) row[2]).doubleValue(), // total_revenue
            ((Number) row[3]).doubleValue(), // avg_price
            ((Number) row[4]).intValue()     // revenue_rank
        ))
        .toList();
}
```

---

### ✅ SOLUTION 2: @SqlResultSetMapping

```java
@SqlResultSetMapping(
    name = "MonthlySalesReportMapping",
    classes = @ConstructorResult(
        targetClass = MonthlySalesReport.class,
        columns = {
            @ColumnResult(name = "category_name", type = String.class),
            @ColumnResult(name = "product_count", type = Integer.class),
            @ColumnResult(name = "total_revenue", type = Double.class),
            @ColumnResult(name = "avg_price", type = Double.class),
            @ColumnResult(name = "revenue_rank", type = Integer.class)
        }
    )
)
@Entity
class Product { ... }

// Usage
public List<MonthlySalesReport> getMonthlySalesReport(int year, int month) {
    return entityManager.createNativeQuery(sql, "MonthlySalesReportMapping")
        .setParameter("year", year)
        .setParameter("month", month)
        .getResultList();
}
```

**✅ Benefits:**
- Type-safe mapping
- Reusable
- No manual casting

---

### ✅ SOLUTION 3: Stored Procedure Call

```java
@Entity
@NamedStoredProcedureQuery(
    name = "calculateMonthlyRevenue",
    procedureName = "sp_calculate_monthly_revenue",
    parameters = {
        @StoredProcedureParameter(
            mode = ParameterMode.IN, 
            name = "year", 
            type = Integer.class
        ),
        @StoredProcedureParameter(
            mode = ParameterMode.IN, 
            name = "month", 
            type = Integer.class
        ),
        @StoredProcedureParameter(
            mode = ParameterMode.OUT, 
            name = "total_revenue", 
            type = Double.class
        )
    }
)
class Order { ... }

// Usage
public Double calculateMonthlyRevenue(int year, int month) {
    StoredProcedureQuery query = entityManager
        .createNamedStoredProcedureQuery("calculateMonthlyRevenue");
    
    query.setParameter("year", year);
    query.setParameter("month", month);
    query.execute();
    
    return (Double) query.getOutputParameterValue("total_revenue");
}
```

---

### 🎯 CASE 12: EntityManager Flush Strategies

### SCENARIO: Complex Business Logic với Multiple Steps

```java
@Transactional
public void processComplexOrder(OrderDTO dto) {
    // Step 1: Create order
    Order order = new Order(dto);
    entityManager.persist(order);
    
    // Step 2: Need order ID for audit (not yet flushed!)
    // ❌ order.getId() = null
    
    // ✅ SOLUTION: Manual flush
    entityManager.flush();  // ⭐ Triggers INSERT, assigns ID
    
    // Step 3: Create audit with order ID
    AuditLog audit = new AuditLog(
        "Order created", 
        order.getId()  // ✅ Now available!
    );
    entityManager.persist(audit);
    
    // Step 4: Update inventory
    for (OrderItemDTO itemDto : dto.getItems()) {
        Product product = entityManager.find(Product.class, itemDto.getProductId());
        product.decreaseStock(itemDto.getQuantity());
        
        // Check constraint immediately
        entityManager.flush();  // ⭐ Trigger constraint check
        
        if (product.getStock() < 0) {
            throw new InsufficientStockException(
                "Product " + product.getName() + " out of stock"
            );
        }
    }
    
    // Step 5: Send notification (need all IDs)
    notificationService.sendOrderConfirmation(order);
}
```

---

### FlushMode Strategies

```java
// AUTO (default): Flush before query execution
entityManager.setFlushMode(FlushModeType.AUTO);

// COMMIT: Only flush on transaction commit
entityManager.setFlushMode(FlushModeType.COMMIT);
```

#### Use Case: COMMIT mode

```java
@Transactional
public void importProducts(List<ProductDTO> dtos) {
    // Optimize by deferring all flushes
    entityManager.setFlushMode(FlushModeType.COMMIT);
    
    for (int i = 0; i < dtos.size(); i++) {
        Product product = new Product(dtos.get(i));
        entityManager.persist(product);
        
        // Manual flush every 50
        if (i % 50 == 0) {
            entityManager.flush();
            entityManager.clear();
        }
    }
}
```

---

### 🎯 CASE 13: EntityManager Detachment & Reattachment

### SCENARIO: Long-running Conversation

**Problem:** User edits entity across multiple requests (wizard-style form)

```java
// Request 1: Load entity
@GetMapping("/orders/{id}/edit")
public OrderDTO getOrderForEdit(@PathVariable Long id) {
    Order order = entityManager.find(Order.class, id);
    return OrderDTO.from(order);
}  // Transaction ends, order becomes DETACHED

// Request 2: Update entity
@PostMapping("/orders/{id}")
public void updateOrder(
    @PathVariable Long id, 
    @RequestBody OrderDTO dto
) {
    Order order = new Order(dto);
    order.setId(id);
    
    // ❌ Problem: order is TRANSIENT, not managed!
    // entityManager doesn't know about it
}
```

---

### ✅ SOLUTION 1: merge() - Safe Reattachment

```java
@Transactional
public Order updateOrder(Long id, OrderDTO dto) {
    Order detachedOrder = new Order(dto);
    detachedOrder.setId(id);
    
    // ⭐ merge() returns MANAGED copy
    Order managedOrder = entityManager.merge(detachedOrder);
    
    return managedOrder;
}
```

**How merge() works:**
```java
// 1. Checks if entity with same ID exists in persistence context
// 2. If exists → copies state to existing managed entity
// 3. If not → loads from DB, then copies state
// 4. Returns the managed entity
```

---

### ✅ SOLUTION 2: Load then Update

```java
@Transactional
public Order updateOrder(Long id, OrderDTO dto) {
    // Load managed entity
    Order order = entityManager.find(Order.class, id);
    
    // Update fields
    order.setStatus(dto.getStatus());
    order.setShippingAddress(dto.getShippingAddress());
    
    // No need to call entityManager.persist()!
    // Dirty checking will auto-UPDATE on flush
    
    return order;
}
```

**✅ Benefits:**
- Optimistic locking works (@Version)
- JPA lifecycle callbacks triggered
- Dirty checking efficient

---

### ⚠️ PITFALL: Detached Entity Modifications

```java
@Transactional
public void problematicUpdate() {
    Order order = entityManager.find(Order.class, 1L);
    entityManager.detach(order);  // Manually detach
    
    order.setStatus("SHIPPED");  // Modify detached entity
    
    // ❌ No UPDATE executed! Changes lost!
    // entityManager doesn't track detached entities
}
```

**Solution:**
```java
Order managedOrder = entityManager.merge(order);  // Re-attach
```

---

### 🎯 CASE 14: Custom EntityManager Interceptors

### SCENARIO: Automatic Audit Trail

**Requirement:** Track all entity changes (who, when, what)

### ✅ SOLUTION: Hibernate Interceptor

```java
@Component
public class AuditInterceptor extends EmptyInterceptor {
    
    @Autowired
    private AuditLogRepository auditRepo;
    
    @Override
    public boolean onSave(
        Object entity,
        Serializable id,
        Object[] state,
        String[] propertyNames,
        Type[] types
    ) {
        if (entity instanceof Auditable) {
            auditRepo.save(new AuditLog(
                "INSERT",
                entity.getClass().getSimpleName(),
                id.toString(),
                getCurrentUser(),
                LocalDateTime.now()
            ));
        }
        return false;
    }
    
    @Override
    public boolean onFlushDirty(
        Object entity,
        Serializable id,
        Object[] currentState,
        Object[] previousState,
        String[] propertyNames,
        Type[] types
    ) {
        if (entity instanceof Auditable) {
            List<String> changes = new ArrayList<>();
            
            for (int i = 0; i < propertyNames.length; i++) {
                if (!Objects.equals(currentState[i], previousState[i])) {
                    changes.add(propertyNames[i] + ": " + 
                        previousState[i] + " → " + currentState[i]);
                }
            }
            
            if (!changes.isEmpty()) {
                auditRepo.save(new AuditLog(
                    "UPDATE",
                    entity.getClass().getSimpleName(),
                    id.toString(),
                    String.join(", ", changes),
                    getCurrentUser(),
                    LocalDateTime.now()
                ));
            }
        }
        return false;
    }
    
    @Override
    public void onDelete(
        Object entity,
        Serializable id,
        Object[] state,
        String[] propertyNames,
        Type[] types
    ) {
        if (entity instanceof Auditable) {
            auditRepo.save(new AuditLog(
                "DELETE",
                entity.getClass().getSimpleName(),
                id.toString(),
                getCurrentUser(),
                LocalDateTime.now()
            ));
        }
    }
    
    private String getCurrentUser() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();
    }
}

// Configuration
@Configuration
public class HibernateConfig {
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        DataSource dataSource,
        AuditInterceptor auditInterceptor
    ) {
        LocalContainerEntityManagerFactoryBean em = 
            new LocalContainerEntityManagerFactoryBean();
        
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.entity");
        
        HibernateJpaVendorAdapter vendorAdapter = 
            new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.ejb.interceptor", auditInterceptor);
        em.setJpaPropertyMap(properties);
        
        return em;
    }
}
```

**✅ Benefits:**
- Automatic audit for ALL entities
- Centralized logging logic
- No code duplication
- Transparent to business logic

---

### Alternative: JPA Entity Listeners

```java
@EntityListeners(AuditListener.class)
@Entity
class Product {
    // ...
}

@Component
public class AuditListener {
    
    @Autowired
    private AuditLogRepository auditRepo;
    
    @PrePersist
    public void prePersist(Object entity) {
        auditRepo.save(new AuditLog("INSERT", entity));
    }
    
    @PreUpdate
    public void preUpdate(Object entity) {
        auditRepo.save(new AuditLog("UPDATE", entity));
    }
    
    @PreRemove
    public void preRemove(Object entity) {
        auditRepo.save(new AuditLog("DELETE", entity));
    }
}
```

**Trade-offs:**
- ✅ Simpler than Interceptor
- ✅ Per-entity control
- ❌ Requires annotation on each entity
- ❌ Cannot access old values easily

---

## CASE 15: @OneToMany Mapping Deep Dive

### 🎯 SCENARIO: Blog Post với Comments

### ⚠️ Understanding @OneToMany Behavior

```java
@Entity
class Post {
    @Id
    private Long id;
    
    private String title;
    private String content;
    
    @OneToMany(mappedBy = "post")  // ⭐ Bidirectional
    private List<Comment> comments = new ArrayList<>();
}

@Entity
class Comment {
    @Id
    private Long id;
    
    private String text;
    
    @ManyToOne  // ⭐ Owning side (has FK)
    @JoinColumn(name = "post_id")  // FK column name
    private Post post;
}
```

**Generated SQL:**
```sql
CREATE TABLE post (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255),
    content TEXT
);

CREATE TABLE comment (
    id BIGINT PRIMARY KEY,
    text VARCHAR(500),
    post_id BIGINT,  -- ⭐ Foreign Key
    FOREIGN KEY (post_id) REFERENCES post(id)
);
```

---

### 🔥 CASCADE Types Deep Dive

#### CASCADE.PERSIST

```java
@OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
private List<Comment> comments;

// Usage:
Post post = new Post("Title");
Comment comment = new Comment("Nice post!");
post.addComment(comment);  // Helper method sets both sides

em.persist(post);  // ⭐ Auto-persists comment too!
// No need: em.persist(comment);
```

**When to use:**
- ✅ Parent-child creation together
- ✅ Aggregates (Order → OrderItems)
- ❌ Independent entities (User → Posts)

---

#### CASCADE.REMOVE

```java
@OneToMany(
    mappedBy = "post", 
    cascade = CascadeType.REMOVE,  // ⚠️ DANGEROUS!
    orphanRemoval = true
)
private List<Comment> comments;

// Usage:
em.remove(post);  
// ⭐ Deletes ALL comments automatically!
// SQL: DELETE FROM comment WHERE post_id = ?;
//      DELETE FROM post WHERE id = ?;
```

**⚠️ WARNING:**
```java
// Soft delete scenario:
post.setDeleted(true);
em.persist(post);

// ❌ Comments NOT deleted! (only CASCADE.REMOVE on em.remove())

// ✅ Solution: Manual delete or @PreUpdate callback
```

**When to use:**
- ✅ True composition (Order → OrderItems)
- ✅ Data lifecycle tied together
- ❌ Shared entities (avoid accidental cascade delete!)

---

#### CASCADE.MERGE

```java
@OneToMany(mappedBy = "post", cascade = CascadeType.MERGE)
private List<Comment> comments;

// Scenario: Detached entity update
Post detachedPost = loadFromCache();  // Detached
Comment detachedComment = new Comment("Updated");
detachedPost.getComments().add(detachedComment);

Post managedPost = em.merge(detachedPost);
// ⭐ Auto-merges all comments too!
```

**When to use:**
- ✅ Stateless REST APIs (entities detached between requests)
- ✅ DTOs → Entities conversion
- ✅ Long conversations

---

#### CASCADE.REFRESH

```java
@OneToMany(mappedBy = "post", cascade = CascadeType.REFRESH)
private List<Comment> comments;

// Reload from database (discard in-memory changes)
em.refresh(post);
// ⭐ Refreshes all comments too!
```

**When to use:**
- ✅ Revert unsaved changes
- ✅ Reload after external DB update
- ❌ Rarely used in practice

---

#### CASCADE.DETACH

```java
@OneToMany(mappedBy = "post", cascade = CascadeType.DETACH)
private List<Comment> comments;

em.detach(post);
// ⭐ Detaches all comments from persistence context
```

---

#### CASCADE.ALL (⚠️ Use with Caution!)

```java
@OneToMany(
    mappedBy = "post", 
    cascade = CascadeType.ALL,  // ⚠️ Everything cascades!
    orphanRemoval = true
)
private List<Comment> comments;

// Equivalent to:
// cascade = {
//     CascadeType.PERSIST,
//     CascadeType.MERGE,
//     CascadeType.REMOVE,
//     CascadeType.REFRESH,
//     CascadeType.DETACH
// }
```

**When to use:**
- ✅ True aggregates (Order → OrderItems)
- ✅ Tight lifecycle coupling
- ❌ Shared/reusable entities

---

### 🔥 orphanRemoval Behavior

```java
@OneToMany(
    mappedBy = "post",
    cascade = CascadeType.PERSIST,
    orphanRemoval = true  // ⭐ Key difference from CASCADE.REMOVE
)
private List<Comment> comments;

// Scenario 1: Remove from collection
post.getComments().remove(comment);  
// ⭐ orphanRemoval triggers DELETE!
// SQL: DELETE FROM comment WHERE id = ?;

// Scenario 2: Clear collection
post.getComments().clear();
// ⭐ Deletes ALL comments!

// Scenario 3: Replace collection
post.setComments(new ArrayList<>());
// ⭐ Deletes old comments!
```

**CASCADE.REMOVE vs orphanRemoval:**

| Action | CASCADE.REMOVE | orphanRemoval |
|--------|----------------|---------------|
| `em.remove(parent)` | ✅ Delete children | ✅ Delete children |
| `parent.children.remove(child)` | ❌ No delete | ✅ Delete child |
| `parent.children.clear()` | ❌ No delete | ✅ Delete all |
| `child.setParent(null)` | ❌ No delete | ✅ Delete child |

---

### ⚠️ COMMON PITFALLS

#### Pitfall 1: Forgot to sync both sides

```java
// ❌ BAD
Comment comment = new Comment("Hello");
post.getComments().add(comment);
// comment.post is still null! → FK violation

// ✅ GOOD: Helper method
public void addComment(Comment comment) {
    comments.add(comment);
    comment.setPost(this);  // ⭐ Sync both sides
}
```

#### Pitfall 2: Cascade on Many side

```java
// ❌ WRONG
@ManyToOne(cascade = CascadeType.REMOVE)  // ⚠️ DANGEROUS!
private Post post;

// em.remove(comment); → Deletes the Post! 💀
// All other comments lose their post!
```

**Rule:** Cascade should flow from **ONE to MANY**, not reverse!

#### Pitfall 3: Bidirectional infinite loop

```java
@Override
public String toString() {
    return "Post{comments=" + comments + "}";  // ❌
}

@Override
public String toString() {
    return "Comment{post=" + post + "}";  // ❌
}

// Stack overflow! Post → Comments → Post → ...
```

**Solution:**
```java
@Override
public String toString() {
    return "Post{id=" + id + ", title='" + title + "'}";
}
```

---

### 🎯 BEST PRACTICES for @OneToMany

#### 1. Always use Helper Methods

```java
public void addComment(Comment comment) {
    comments.add(comment);
    comment.setPost(this);
}

public void removeComment(Comment comment) {
    comments.remove(comment);
    comment.setPost(null);
}
```

#### 2. Initialize Collections

```java
@OneToMany(mappedBy = "post")
private List<Comment> comments = new ArrayList<>();  // ⭐ Never null
```

#### 3. Use Set for better performance

```java
// ❌ List requires full scan for contains()
private List<Comment> comments = new ArrayList<>();

// ✅ Set uses hashCode() - O(1) lookup
private Set<Comment> comments = new HashSet<>();

// Must override equals/hashCode in Comment!
```

#### 4. Limit Collection Size

```java
// ❌ BAD: Load all comments (could be 10K+)
@OneToMany(mappedBy = "post")
private List<Comment> comments;

// ✅ GOOD: Paginated query
@Query("SELECT c FROM Comment c WHERE c.post = :post")
Page<Comment> findByPost(@Param("post") Post post, Pageable pageable);
```

---

## CASE 16: @ManyToOne Mapping Deep Dive

### 🎯 Understanding @ManyToOne

```java
@Entity
class Comment {
    @Id
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)  // ⭐ Default: EAGER!
    @JoinColumn(
        name = "post_id",           // FK column name
        nullable = false,           // NOT NULL constraint
        foreignKey = @ForeignKey(   // FK constraint name
            name = "fk_comment_post"
        )
    )
    private Post post;
}
```

**Generated SQL:**
```sql
ALTER TABLE comment
ADD CONSTRAINT fk_comment_post
FOREIGN KEY (post_id) REFERENCES post(id);
```

---

### 🔥 FetchType Deep Dive

#### FetchType.LAZY (Recommended)

```java
@ManyToOne(fetch = FetchType.LAZY)  // ⭐ Always use LAZY!
private Post post;

// Load comment
Comment comment = em.find(Comment.class, 1L);
// SQL: SELECT * FROM comment WHERE id = 1;
// post is proxy (not loaded yet)

// Access post triggers query
String title = comment.getPost().getTitle();
// SQL: SELECT * FROM post WHERE id = ?;
```

**Benefits:**
- ✅ Load only when needed
- ✅ Better performance
- ✅ Avoid unnecessary data

**Gotcha:**
```java
Comment comment = em.find(Comment.class, 1L);
em.close();  // Close session

comment.getPost().getTitle();  // 💀 LazyInitializationException!
```

---

#### FetchType.EAGER (Avoid!)

```java
@ManyToOne(fetch = FetchType.EAGER)  // ❌ Default, but bad!
private Post post;

// Load comment
Comment comment = em.find(Comment.class, 1L);
// SQL: SELECT * FROM comment c 
//      LEFT JOIN post p ON c.post_id = p.id
//      WHERE c.id = 1;
// Post loaded immediately!
```

**Problems:**
- ❌ Always loads post (even if not needed)
- ❌ Cannot override to LAZY at runtime
- ❌ Cascade EAGER loading
- ❌ N+1 query problem

**⚠️ EAGER Cascade:**
```java
@Entity
class Comment {
    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;  // EAGER
}

@Entity
class Post {
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;  // EAGER
}

@Entity
class User {
    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;  // EAGER
}

// Load 1 comment:
Comment comment = em.find(Comment.class, 1L);
// Loads: Comment + Post + User + Country (4 tables!)
// Even if you only need comment.text!
```

---

### 🔥 @JoinColumn Options

```java
@ManyToOne
@JoinColumn(
    name = "post_id",              // FK column name (default: post_id)
    
    nullable = false,              // NOT NULL constraint
    
    unique = false,                // UNIQUE constraint (for @OneToOne)
    
    insertable = true,             // Include in INSERT statements
    updatable = true,              // Include in UPDATE statements
    
    columnDefinition = "BIGINT",   // Custom column definition
    
    foreignKey = @ForeignKey(
        name = "fk_comment_post",   // FK constraint name
        
        value = ConstraintMode.CONSTRAINT,  // Create FK
        // value = ConstraintMode.NO_CONSTRAINT  // Skip FK (legacy DB)
    ),
    
    referencedColumnName = "id"    // Target column (default: PK)
)
private Post post;
```

---

### ⚠️ insertable/updatable Use Case

**Scenario:** Composite FK from legacy database

```java
@Entity
class OrderItem {
    @Id
    private Long id;
    
    // Composite FK: (order_id, order_version)
    @ManyToOne
    @JoinColumns({
        @JoinColumn(
            name = "order_id", 
            referencedColumnName = "id"
        ),
        @JoinColumn(
            name = "order_version", 
            referencedColumnName = "version",
            insertable = false,  // ⭐ Don't include in INSERT
            updatable = false    // ⭐ Don't include in UPDATE
        )
    })
    private Order order;
    
    // Separate field for version (for optimistic locking)
    @Column(name = "order_version")
    private Integer orderVersion;
}
```

---

### 🎯 BEST PRACTICES for @ManyToOne

#### 1. Always use LAZY

```java
@ManyToOne(fetch = FetchType.LAZY)  // ✅
private Post post;
```

#### 2. Use optional = false for required relationships

```java
@ManyToOne(
    fetch = FetchType.LAZY,
    optional = false  // ⭐ Implies nullable = false
)
private Post post;
```

#### 3. Avoid CASCADE on @ManyToOne

```java
// ❌ DANGEROUS
@ManyToOne(cascade = CascadeType.REMOVE)
private Post post;

// Deleting comment deletes post! 💀
```

**Exceptions (rare):**
```java
// ✅ OK for value objects
@ManyToOne(cascade = CascadeType.PERSIST)
private Address shippingAddress;  // Embedded-like
```

---

## CASE 17: @ManyToMany Mapping Deep Dive

### 🎯 SCENARIO: Student ↔ Course

### Basic @ManyToMany

```java
@Entity
class Student {
    @Id
    private Long id;
    
    private String name;
    
    @ManyToMany
    @JoinTable(
        name = "student_course",           // Join table name
        joinColumns = @JoinColumn(
            name = "student_id"             // FK to student
        ),
        inverseJoinColumns = @JoinColumn(
            name = "course_id"              // FK to course
        )
    )
    private Set<Course> courses = new HashSet<>();
}

@Entity
class Course {
    @Id
    private Long id;
    
    private String name;
    
    @ManyToMany(mappedBy = "courses")  // ⭐ Inverse side
    private Set<Student> students = new HashSet<>();
}
```

**Generated SQL:**
```sql
CREATE TABLE student (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE course (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE student_course (  -- ⭐ Join table
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
);
```

---

### ⚠️ @ManyToMany Problems

#### Problem 1: Cannot store extra data in join table

```java
// ❌ Cannot do this with @ManyToMany:
CREATE TABLE student_course (
    student_id BIGINT,
    course_id BIGINT,
    enrolled_date DATE,      -- ❌ Cannot map this!
    grade VARCHAR(2),         -- ❌ Cannot map this!
    PRIMARY KEY (student_id, course_id)
);
```

---

### ✅ SOLUTION: Convert to 2x @OneToMany

```java
@Entity
class Student {
    @Id
    private Long id;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Enrollment> enrollments = new HashSet<>();
    
    // Helper method
    public void enrollCourse(Course course, LocalDate date) {
        Enrollment enrollment = new Enrollment(this, course, date);
        enrollments.add(enrollment);
        course.getEnrollments().add(enrollment);
    }
}

@Entity
class Course {
    @Id
    private Long id;
    
    @OneToMany(mappedBy = "course")
    private Set<Enrollment> enrollments = new HashSet<>();
}

@Entity
class Enrollment {  // ⭐ Join table as entity!
    @EmbeddedId
    private EnrollmentId id;
    
    @ManyToOne
    @MapsId("studentId")  // ⭐ Part of composite key
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne
    @MapsId("courseId")  // ⭐ Part of composite key
    @JoinColumn(name = "course_id")
    private Course course;
    
    // ⭐ Extra fields!
    private LocalDate enrolledDate;
    private String grade;
    private Integer attendance;
}

@Embeddable
class EnrollmentId implements Serializable {
    private Long studentId;
    private Long courseId;
    
    // equals/hashCode
}
```

**Benefits:**
- ✅ Can store extra data (grade, date, etc.)
- ✅ Can query join table directly
- ✅ Better control over lifecycle
- ✅ More explicit

---

### 🔥 @ManyToMany CASCADE Behavior

```java
@ManyToMany(
    cascade = {  // ⚠️ Be careful!
        CascadeType.PERSIST,  // ✅ Usually OK
        CascadeType.MERGE     // ✅ Usually OK
    }
    // ❌ NEVER: CascadeType.REMOVE
)
private Set<Course> courses;

// Why no REMOVE?
student.getCourses().add(course1);
student.getCourses().add(course2);

em.remove(student);
// With CASCADE.REMOVE → Deletes course1 AND course2! 💀
// But course1, course2 might have other students!
```

---

### 🎯 BEST PRACTICES for @ManyToMany

#### 1. Use Set (not List)

```java
// ❌ BAD
private List<Course> courses = new ArrayList<>();
// Hibernate may execute DELETE all + INSERT all on update!

// ✅ GOOD
private Set<Course> courses = new HashSet<>();
// Only affected rows changed
```

#### 2. Implement equals/hashCode

```java
@Entity
class Course {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
```

#### 3. Convert to @OneToMany when you need extra fields

#### 4. Never use CASCADE.REMOVE

---

## CASE 18: @OneToOne Mapping Deep Dive

### 🎯 SCENARIO: User ↔ Profile (1:1)

### Strategy 1: Shared Primary Key

```java
@Entity
class User {
    @Id
    @GeneratedValue
    private Long id;
    
    private String username;
    
    @OneToOne(
        mappedBy = "user", 
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private UserProfile profile;
}

@Entity
class UserProfile {
    @Id
    private Long id;  // ⭐ Same ID as User
    
    @OneToOne
    @MapsId  // ⭐ Share PK with User
    @JoinColumn(name = "id")
    private User user;
    
    private String bio;
    private String avatarUrl;
}
```

**Generated SQL:**
```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255)
);

CREATE TABLE user_profile (
    id BIGINT PRIMARY KEY,  -- ⭐ Same as user.id
    bio TEXT,
    avatar_url VARCHAR(255),
    FOREIGN KEY (id) REFERENCES user(id)
);
```

**Benefits:**
- ✅ No extra FK column
- ✅ Guaranteed 1:1 relationship
- ✅ Efficient JOIN

---

### Strategy 2: Foreign Key in Parent

```java
@Entity
class User {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
        name = "profile_id",  // ⭐ FK in user table
        unique = true         // ⭐ Ensure 1:1
    )
    private UserProfile profile;
}

@Entity
class UserProfile {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne(mappedBy = "profile")
    private User user;
}
```

**Generated SQL:**
```sql
CREATE TABLE user_profile (
    id BIGINT PRIMARY KEY,
    bio TEXT
);

CREATE TABLE user (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255),
    profile_id BIGINT UNIQUE,  -- ⭐ FK here
    FOREIGN KEY (profile_id) REFERENCES user_profile(id)
);
```

---

### Strategy 3: Foreign Key in Child (Most Common)

```java
@Entity
class User {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private UserProfile profile;
}

@Entity
class UserProfile {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne
    @JoinColumn(
        name = "user_id",  // ⭐ FK in profile table
        unique = true,     // ⭐ Ensure 1:1
        nullable = false
    )
    private User user;
}
```

**Generated SQL:**
```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255)
);

CREATE TABLE user_profile (
    id BIGINT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,  -- ⭐ FK here
    bio TEXT,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

**✅ RECOMMENDED** (Most flexible)

---

### ⚠️ @OneToOne Lazy Loading Problem

```java
@Entity
class User {
    @OneToOne(
        mappedBy = "user",
        fetch = FetchType.LAZY  // ⭐ Doesn't work on inverse side!
    )
    private UserProfile profile;
}

// Load user
User user = em.find(User.class, 1L);
// Hibernate MUST query profile to check if null!
// Cannot create proxy for optional @OneToOne
```

**Why?**
- `user.getProfile()` could return `null` or `Profile`
- Hibernate can't return proxy (proxy can't be null)
- Must query to know if profile exists!

---

### ✅ SOLUTION: Make it non-optional

```java
@OneToOne(
    mappedBy = "user",
    fetch = FetchType.LAZY,
    optional = false  // ⭐ Profile always exists!
)
private UserProfile profile;

// Now Hibernate can use proxy (never null)
```

**Alternative: Use @ManyToOne instead**

```java
// UserProfile (owning side)
@ManyToOne(fetch = FetchType.LAZY)  // ⭐ Always lazy!
@JoinColumn(name = "user_id", unique = true)
private User user;

// User (inverse side) - skip if not needed
// Don't map bidirectional if you don't need it!
```

---

### 🎯 BEST PRACTICES for @OneToOne

#### 1. Put FK in child table

```java
@Entity
class User {  // ⭐ Parent (no FK)
    @OneToOne(mappedBy = "user")
    private UserProfile profile;
}

@Entity
class UserProfile {  // ⭐ Child (has FK)
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
```

#### 2. Use optional = false when possible

```java
@OneToOne(
    mappedBy = "user",
    optional = false  // ✅ Enables lazy loading
)
private UserProfile profile;
```

#### 3. Consider @ManyToOne for lazy loading

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", unique = true)
private User user;  // ✅ True lazy loading
```

#### 4. Use CASCADE.ALL + orphanRemoval for tight coupling

```java
@OneToOne(
    mappedBy = "user",
    cascade = CascadeType.ALL,
    orphanRemoval = true
)
private UserProfile profile;
```

---

## 📊 RELATIONSHIP MAPPING COMPARISON TABLE

### CASCADE Types Summary

| CascadeType | Effect | Common Use |
|-------------|--------|------------|
| **PERSIST** | `em.persist(parent)` → persist children | Creating aggregates |
| **MERGE** | `em.merge(parent)` → merge children | Detached entity updates |
| **REMOVE** | `em.remove(parent)` → remove children | True composition |
| **REFRESH** | `em.refresh(parent)` → refresh children | Reload from DB |
| **DETACH** | `em.detach(parent)` → detach children | Rare |
| **ALL** | All of the above | Tight lifecycle coupling |

### Cascade vs orphanRemoval

| Scenario | CASCADE.REMOVE | orphanRemoval |
|----------|----------------|---------------|
| `em.remove(parent)` | ✅ Delete children | ✅ Delete children |
| `parent.children.remove(child)` | ❌ No delete | ✅ Delete child |
| `parent.children.clear()` | ❌ No delete | ✅ Delete all |
| `child.setParent(null)` | ❌ No delete | ✅ Delete child |
| **Use case** | Delete entire aggregate | Manage collection membership |

---

### FetchType Summary

| Relationship | Default FetchType | Recommended |
|--------------|-------------------|-------------|
| **@OneToOne** | EAGER | LAZY + optional=false |
| **@ManyToOne** | EAGER ⚠️ | **LAZY** (always!) |
| **@OneToMany** | LAZY | LAZY |
| **@ManyToMany** | LAZY | LAZY |

**⚠️ WARNING:** `@ManyToOne` default is EAGER - always override to LAZY!

---

### Owning Side vs Inverse Side

| Aspect | Owning Side | Inverse Side |
|--------|-------------|-------------|
| **Has @JoinColumn** | ✅ Yes | ❌ No |
| **Has FK in DB** | ✅ Yes | ❌ No |
| **Controls relationship** | ✅ Yes | ❌ No (read-only) |
| **mappedBy attribute** | ❌ No | ✅ Yes |
| **Changes persisted** | ✅ Yes | ❌ Ignored by Hibernate |

**Rule:** Only changes to OWNING SIDE are persisted!

```java
// ❌ WRONG
post.getComments().add(comment);  // Inverse side change
// Not persisted! FK remains null

// ✅ CORRECT
comment.setPost(post);  // Owning side change
post.getComments().add(comment);  // For in-memory consistency
```

---

### Relationship Strategy Decision Tree

#### When to use @OneToMany?

```
Parent has multiple children?
  → Can children exist without parent?
    → YES: Don't use cascade (User → Posts)
    → NO: Use CASCADE.ALL + orphanRemoval (Order → OrderItems)
```

#### When to use @ManyToOne?

```
Child belongs to one parent?
  → Always use LAZY fetch!
  → Rarely cascade (only for value objects)
```

#### When to use @ManyToMany?

```
Many-to-many relationship?
  → Need extra data in join table? (date, status, etc.)
    → YES: Use 2x @OneToMany + join entity
    → NO: Use @ManyToMany
  → Never use CASCADE.REMOVE!
```

#### When to use @OneToOne?

```
One-to-one relationship?
  → Which strategy?
    → Same lifecycle → Shared PK (@MapsId)
    → Independent lifecycle → FK in child table
    → Optional relationship → Consider nullable FK
  → Use optional=false for true lazy loading
```

---

## 🎯 REAL-WORLD MAPPING EXAMPLES

### Example 1: E-Commerce Order (Strong Composition)

```java
@Entity
class Order {
    @Id
    @GeneratedValue
    private Long id;
    
    // Strong ownership - items cannot exist without order
    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,  // ✅ Full cascade
        orphanRemoval = true,       // ✅ Remove orphans
        fetch = FetchType.LAZY
    )
    private List<OrderItem> items = new ArrayList<>();
    
    // Helper methods
    public void addItem(Product product, int quantity) {
        OrderItem item = new OrderItem(this, product, quantity);
        items.add(item);
    }
    
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);  // Trigger orphanRemoval
    }
}

@Entity
class OrderItem {
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false  // Order always exists
    )
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    private Integer quantity;
    private Double price;
}

@Entity
class Product {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private Double price;
    
    // ❌ Don't map inverse side - not needed!
    // @OneToMany(mappedBy = "product")
    // private List<OrderItem> orderItems;
}
```

**Why this design?**
- ✅ Order controls OrderItems lifecycle
- ✅ Deleting Order deletes all items
- ✅ Removing item from collection deletes it
- ✅ Product independent (no cascade from OrderItem)

---

### Example 2: Blog Platform (Weak Composition)

```java
@Entity
class User {
    @Id
    @GeneratedValue
    private Long id;
    
    // Posts can exist independently (draft, scheduled, etc.)
    @OneToMany(
        mappedBy = "author",
        cascade = {  // ❌ No CASCADE.REMOVE!
            CascadeType.PERSIST,
            CascadeType.MERGE
        },
        fetch = FetchType.LAZY
    )
    private List<Post> posts = new ArrayList<>();
}

@Entity
class Post {
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    // Comments depend on post
    @OneToMany(
        mappedBy = "post",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();
}

@Entity
class Comment {
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
}
```

**Why this design?**
- ✅ Deleting User does NOT delete Posts (archive instead)
- ✅ Deleting Post DOES delete Comments
- ✅ User, Post, Comment are independently queryable

---

### Example 3: Student-Course Enrollment (Many-to-Many with Data)

```java
@Entity
class Student {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMany(
        mappedBy = "student",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<Enrollment> enrollments = new HashSet<>();
    
    public void enrollCourse(Course course, LocalDate date) {
        Enrollment enrollment = new Enrollment(this, course, date);
        enrollments.add(enrollment);
        course.getEnrollments().add(enrollment);
    }
    
    public void dropCourse(Course course) {
        Enrollment enrollment = enrollments.stream()
            .filter(e -> e.getCourse().equals(course))
            .findFirst()
            .orElseThrow();
        
        enrollments.remove(enrollment);
        course.getEnrollments().remove(enrollment);
        enrollment.setStudent(null);  // Trigger orphanRemoval
        enrollment.setCourse(null);
    }
}

@Entity
class Course {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "course")
    private Set<Enrollment> enrollments = new HashSet<>();
}

@Entity
class Enrollment {
    @EmbeddedId
    private EnrollmentId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;
    
    private LocalDate enrolledDate;
    private String grade;
    private Integer attendance;
    
    // Constructor
    public Enrollment(Student student, Course course, LocalDate date) {
        this.student = student;
        this.course = course;
        this.enrolledDate = date;
        this.id = new EnrollmentId(student.getId(), course.getId());
    }
}

@Embeddable
class EnrollmentId implements Serializable {
    private Long studentId;
    private Long courseId;
    
    // equals/hashCode required!
}
```

**Why this design?**
- ✅ Can store enrollment date, grade, attendance
- ✅ Can query enrollments directly
- ✅ Student controls enrollment lifecycle
- ✅ Dropping course removes enrollment (orphanRemoval)

---

### Example 4: User Profile (One-to-One)

```java
@Entity
class User {
    @Id
    @GeneratedValue
    private Long id;
    
    private String username;
    private String email;
    
    // Profile always exists with user
    @OneToOne(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        optional = false,  // ✅ True lazy loading
        fetch = FetchType.LAZY
    )
    private UserProfile profile;
    
    // Settings optional (not all users have custom settings)
    @OneToOne(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private UserSettings settings;
}

@Entity
class UserProfile {
    @Id
    private Long id;
    
    @OneToOne
    @MapsId  // ✅ Share PK with User
    @JoinColumn(name = "id")
    private User user;
    
    private String firstName;
    private String lastName;
    private String bio;
    private String avatarUrl;
}

@Entity
class UserSettings {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        unique = true,
        nullable = false
    )
    private User user;
    
    private String theme;
    private String language;
    private Boolean emailNotifications;
}
```

**Why this design?**
- ✅ Profile shares PK (always exists with User)
- ✅ Settings has own PK (optional)
- ✅ Deleting User deletes both Profile and Settings
- ✅ True lazy loading for Profile (optional=false)

---

## 🎯 KEY TAKEAWAYS - Relationship Mapping

### General Rules

1. **Always use LAZY fetch** (especially @ManyToOne!)
2. **CASCADE flows from ONE to MANY** (never reverse)
3. **Implement equals/hashCode** for entities in collections
4. **Use helper methods** to sync both sides
5. **Initialize collections** to avoid NullPointerException
6. **Prefer Set over List** for @OneToMany/@ManyToMany
7. **Only map bidirectional when needed** (unidirectional is simpler)

### CASCADE Rules

| Relationship | PERSIST | MERGE | REMOVE | orphanRemoval |
|--------------|---------|-------|--------|---------------|
| **Parent → Children** (composition) | ✅ | ✅ | ✅ | ✅ |
| **Parent → Children** (aggregation) | ✅ | ✅ | ❌ | ❌ |
| **Child → Parent** | ❌ | ❌ | ❌ | N/A |
| **Many-to-Many** | Maybe | Maybe | ❌ | ❌ |

### Performance Rules

1. **@OneToMany:** Use `@BatchSize` if cannot use JOIN FETCH
2. **@ManyToOne:** Always LAZY + JOIN FETCH when needed
3. **@ManyToMany:** Convert to 2x @OneToMany if need extra fields
4. **@OneToOne:** Use `optional=false` for true lazy loading
5. **Collections:** Don't load all - use pagination queries

---

## 💡 PRACTICE EXERCISES - Relationship Mapping

1. **Design E-Commerce:** Model Product, Category, Order, OrderItem with proper cascades
2. **Blog Platform:** Model User, Post, Comment, Tag with @ManyToMany
3. **University:** Model Student, Course, Enrollment with extra fields
4. **Social Network:** Model User, Friendship (self-referencing @ManyToMany)
5. **Employee Hierarchy:** Model Employee, Department with @ManyToOne (self-referencing)
6. **Fix N+1:** Convert EAGER to LAZY + proper JOIN FETCH

---

## 🎯 KEY TAKEAWAYS - Custom EntityManager

---

## 💡 PRACTICE EXERCISES - EntityManager

1. **Dynamic Search:** Build flexible product search with 10+ filters using CriteriaBuilder
2. **Bulk Update:** Implement category price adjustment (different % per category)
3. **Native Query:** Create monthly sales report with window functions
4. **Audit Trail:** Implement full audit logging with Interceptor
5. **Detached Entity:** Build wizard-style multi-step form with merge()
6. **Performance Test:** Compare bulk UPDATE vs individual updates (measure time)

---

**Tài liệu này tổng hợp kinh nghiệm thực tế từ production systems.**
**Áp dụng cẩn thận và đo lường performance trước/sau optimization!**
