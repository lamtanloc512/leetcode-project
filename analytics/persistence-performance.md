# Persistence và Performance trong Spring (JPA/Hibernate)

Tài liệu này giải thích các vấn đề phổ biến về performance trong JPA và cách Spring giúp giải quyết chúng.

---

## Mục lục

1. [N+1 Query Problem](#1-n1-query-problem)
2. [Các chiến lược Fetch](#2-các-chiến-lược-fetch)
3. [Spring Data JPA và EntityManager](#3-spring-data-jpa-và-entitymanager)
4. [Batch Processing](#4-batch-processing)
5. [Caching với Spring](#5-caching-với-spring)
6. [Best Practices](#6-best-practices)

---

## 1. N+1 Query Problem

### 1.1. Vấn đề là gì?

Khi load entity có relationship, JPA có thể thực hiện N+1 queries thay vì 1:

```java
@Entity
public class Post {
    @Id
    private Long id;
    private String title;
    
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments;
}

@Entity
public class Comment {
    @Id
    private Long id;
    private String content;
    
    @ManyToOne
    private Post post;
}
```

```java
// Code này gây N+1 problem
List<Post> posts = postRepository.findAll(); // 1 query: SELECT * FROM post

for (Post post : posts) {
    // Mỗi lần access comments → 1 query mới
    int count = post.getComments().size(); // N queries: SELECT * FROM comment WHERE post_id = ?
}
// Tổng: 1 + N queries!
```

**Log SQL:**

```sql
-- Query 1
SELECT * FROM post;

-- Query 2 (cho post 1)
SELECT * FROM comment WHERE post_id = 1;

-- Query 3 (cho post 2)
SELECT * FROM comment WHERE post_id = 2;

-- ... N queries nữa
```

### 1.2. Tại sao xảy ra?

- `FetchType.LAZY` (mặc định cho `@OneToMany`, `@ManyToMany`): Chỉ load khi access
- Khi access `post.getComments()` trong vòng lặp → mỗi lần là 1 query mới
- JPA không biết trước bạn sẽ access những gì

### 1.3. Hậu quả

- **Performance kém:** 100 posts = 101 queries
- **Database overload:** Nhiều round-trip đến DB
- **Latency cao:** Đặc biệt nghiêm trọng với remote DB

---

## 2. Các chiến lược Fetch

### 2.1. JOIN FETCH (JPQL)

**Là gì:** Load cả entity chính và relationship trong 1 query.

```java
public interface PostRepository extends JpaRepository<Post, Long> {
    
    @Query("SELECT p FROM Post p JOIN FETCH p.comments")
    List<Post> findAllWithComments();
}
```

**SQL sinh ra:**

```sql
SELECT p.*, c.* FROM post p 
LEFT JOIN comment c ON p.id = c.post_id
```

**Ưu điểm:**
- Chỉ 1 query
- Hiệu quả nhất khi biết trước cần relationship nào

**Nhược điểm:**
- Có thể tạo Cartesian product (N x M rows) với nhiều collections
- Không thể paginate với `JOIN FETCH` collection (warning từ Hibernate)

```java
// ❌ Vấn đề với pagination
@Query("SELECT p FROM Post p JOIN FETCH p.comments")
Page<Post> findAllWithComments(Pageable pageable); // HHH000104 warning!
```

### 2.2. @EntityGraph

**Là gì:** Khai báo fetch plan ở entity hoặc repository level.

**Cách 1: Define ở Entity**

```java
@Entity
@NamedEntityGraph(
    name = "Post.withComments",
    attributeNodes = @NamedAttributeNode("comments")
)
public class Post { ... }

public interface PostRepository extends JpaRepository<Post, Long> {
    
    @EntityGraph("Post.withComments")
    List<Post> findAll();
}
```

**Cách 2: Define inline ở Repository**

```java
public interface PostRepository extends JpaRepository<Post, Long> {
    
    @EntityGraph(attributePaths = {"comments", "author"})
    List<Post> findAll();
    
    @EntityGraph(attributePaths = {"comments"})
    Optional<Post> findById(Long id);
}
```

**Ưu điểm:**
- Không cần viết JPQL
- Có thể reuse với nhiều query methods
- Dễ đọc và maintain

**Nhược điểm:**
- Cùng vấn đề Cartesian product như JOIN FETCH

### 2.3. @BatchSize (Hibernate)

**Là gì:** Thay vì load từng collection một, load nhiều collections cùng lúc.

```java
@Entity
public class Post {
    @OneToMany(mappedBy = "post")
    @BatchSize(size = 25) // Load 25 collections mỗi lần
    private List<Comment> comments;
}
```

**SQL sinh ra:**

```sql
-- Thay vì 100 queries, chỉ cần 4 queries (100/25 = 4)
SELECT * FROM comment WHERE post_id IN (1, 2, 3, ..., 25);
SELECT * FROM comment WHERE post_id IN (26, 27, 28, ..., 50);
-- ...
```

**Ưu điểm:**
- Không cần sửa repository
- Tự động áp dụng cho tất cả queries
- Không có vấn đề Cartesian product

**Nhược điểm:**
- Vẫn nhiều hơn 1 query
- Cần tune batch size phù hợp

**Cấu hình global:**

```properties
# application.properties
spring.jpa.properties.hibernate.default_batch_fetch_size=25
```

### 2.4. Subselect Fetch (Hibernate)

**Là gì:** Load tất cả collections với 1 subquery.

```java
@Entity
public class Post {
    @OneToMany(mappedBy = "post")
    @Fetch(FetchMode.SUBSELECT)
    private List<Comment> comments;
}
```

**SQL sinh ra:**

```sql
-- 1 query load posts
SELECT * FROM post WHERE ...;

-- 1 query load ALL comments cho tất cả posts đã load
SELECT * FROM comment WHERE post_id IN (SELECT id FROM post WHERE ...);
```

**Ưu điểm:**
- Chỉ 2 queries tổng cộng
- Hiệu quả với collection lớn

**Nhược điểm:**
- Subquery có thể chậm với dữ liệu lớn
- Không flexible bằng @BatchSize

### 2.5. So sánh các chiến lược

| Chiến lược | Số queries | Use case |
|------------|------------|----------|
| Không xử lý | 1 + N | ❌ Tránh |
| JOIN FETCH | 1 | Biết trước cần gì, không cần pagination |
| @EntityGraph | 1 | Giống JOIN FETCH, syntax đẹp hơn |
| @BatchSize | 1 + N/batch | Default choice, cân bằng |
| SUBSELECT | 2 | Collections lớn, ít thay đổi |

---

## 3. Spring Data JPA và EntityManager

### 3.1. Shared EntityManager

Spring inject một **proxy** của `EntityManager` thay vì instance thật:

```java
@Repository
public class CustomRepository {
    
    @PersistenceContext
    private EntityManager entityManager; // Đây là proxy!
    
    public void doSomething() {
        // Proxy delegate đến EntityManager của transaction hiện tại
        entityManager.persist(entity);
    }
}
```

**Tại sao?**

- `EntityManager` không thread-safe
- Spring tạo proxy delegate đến thread-bound EntityManager
- Mọi repository trong cùng transaction dùng chung 1 EntityManager
- → Không có N+1 nếu entity đã được load trong transaction

```java
@Transactional
public void process() {
    Post post = postRepository.findById(1L).get(); // Load post + cache trong EntityManager
    
    // Gọi service khác
    otherService.doSomething(post); // Cùng EntityManager, cùng cache
    
    // Access lazy collection - không có thêm query nếu đã load
    post.getComments(); // Có thể vẫn lazy load, nhưng từ cùng session
}
```

### 3.2. Open EntityManager in View (OSIV)

**Là gì:** Pattern giữ EntityManager mở cho đến khi View được render.

```properties
# application.properties
spring.jpa.open-in-view=true  # Default: true
```

**Ưu điểm:**
- Có thể access lazy collections trong View/Controller
- Không bị `LazyInitializationException`

**Nhược điểm:**
- Giữ connection DB lâu hơn cần thiết
- N+1 có thể xảy ra ở View layer
- Khó debug performance issues

**Khuyến nghị:**

```properties
spring.jpa.open-in-view=false  # Tắt trong production
```

Thay vào đó, load hết data cần thiết trong Service:

```java
@Service
public class PostService {
    
    @Transactional(readOnly = true)
    public PostDto getPostWithComments(Long id) {
        Post post = postRepository.findByIdWithComments(id); // JOIN FETCH
        return new PostDto(post); // Map trong transaction
    }
}
```

---

## 4. Batch Processing

### 4.1. Vấn đề với bulk insert/update

```java
// ❌ Chậm: mỗi save = 1 INSERT
for (int i = 0; i < 10000; i++) {
    repository.save(new Entity(...));
}
```

### 4.2. Batch Insert

**Cấu hình:**

```properties
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

**Code:**

```java
@Transactional
public void batchInsert(List<Entity> entities) {
    int batchSize = 50;
    for (int i = 0; i < entities.size(); i++) {
        entityManager.persist(entities.get(i));
        
        if (i > 0 && i % batchSize == 0) {
            entityManager.flush();  // Gửi batch đến DB
            entityManager.clear();  // Giải phóng memory
        }
    }
}
```

### 4.3. Lưu ý với IDENTITY generation

```java
@Entity
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ❌ Không batch được!
}
```

**IDENTITY** strategy buộc Hibernate phải execute INSERT ngay để lấy ID → không batch được.

**Giải pháp:**

```java
@Entity
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entity_seq")
    @SequenceGenerator(name = "entity_seq", allocationSize = 50)
    private Long id; // ✅ Batch được với SEQUENCE
}
```

### 4.4. Bulk Update/Delete với JPQL

```java
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.lastLogin < :date")
    int deactivateInactiveUsers(@Param("status") String status, 
                                 @Param("date") LocalDate date);
    
    @Modifying
    @Query("DELETE FROM User u WHERE u.status = 'DELETED'")
    int purgeDeletedUsers();
}
```

**Lưu ý:**
- `@Modifying` bắt buộc cho UPDATE/DELETE
- Bypasses EntityManager cache → cần `clearAutomatically = true` nếu cần

```java
@Modifying(clearAutomatically = true)
@Query("UPDATE User u SET u.status = 'INACTIVE' WHERE u.id = :id")
int deactivate(@Param("id") Long id);
```

---

## 5. Caching với Spring

### 5.1. First-Level Cache (EntityManager)

- **Tự động:** Mọi entity load trong transaction được cache
- **Scope:** Trong transaction/session
- **Không cấu hình được**

```java
@Transactional
public void example() {
    User user1 = userRepository.findById(1L).get(); // Query DB
    User user2 = userRepository.findById(1L).get(); // Từ cache! Không query
    
    assert user1 == user2; // Cùng instance
}
```

### 5.2. Second-Level Cache (Hibernate)

**Cấu hình:**

```properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
spring.jpa.properties.javax.cache.provider=org.ehcache.jsr107.EhcacheCachingProvider
```

**Entity:**

```java
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product {
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
}
```

**Use case:**
- Dữ liệu ít thay đổi (categories, countries, ...)
- Read-heavy workload

### 5.3. Spring Cache Abstraction

```java
@Service
public class ProductService {
    
    @Cacheable("products")
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow();
    }
    
    @CacheEvict("products")
    public void updateProduct(Long id, ProductDto dto) {
        // ...
    }
    
    @CachePut(value = "products", key = "#product.id")
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
```

---

## 6. Best Practices

### 6.1. Projection cho read-only queries

```java
// DTO Projection - chỉ load columns cần thiết
public interface PostSummary {
    Long getId();
    String getTitle();
    int getCommentCount();
}

public interface PostRepository extends JpaRepository<Post, Long> {
    
    @Query("SELECT p.id as id, p.title as title, SIZE(p.comments) as commentCount " +
           "FROM Post p")
    List<PostSummary> findAllSummaries();
}
```

### 6.2. Pagination đúng cách

```java
// ❌ Sai: JOIN FETCH với Pageable
@Query("SELECT p FROM Post p JOIN FETCH p.comments")
Page<Post> findAll(Pageable pageable); // Memory pagination!

// ✅ Đúng: Paginate IDs trước, rồi fetch
@Query("SELECT p.id FROM Post p")
Page<Long> findAllIds(Pageable pageable);

@Query("SELECT p FROM Post p JOIN FETCH p.comments WHERE p.id IN :ids")
List<Post> findByIdsWithComments(@Param("ids") List<Long> ids);

// Service
public Page<Post> getPostsWithComments(Pageable pageable) {
    Page<Long> idPage = postRepository.findAllIds(pageable);
    List<Post> posts = postRepository.findByIdsWithComments(idPage.getContent());
    return new PageImpl<>(posts, pageable, idPage.getTotalElements());
}
```

### 6.3. Sử dụng readOnly cho queries

```java
@Transactional(readOnly = true) // Hibernate skip dirty checking
public List<Post> getAllPosts() {
    return postRepository.findAll();
}
```

### 6.4. Index database columns

```java
@Entity
@Table(indexes = {
    @Index(name = "idx_user_email", columnList = "email"),
    @Index(name = "idx_user_status_created", columnList = "status, createdAt")
})
public class User { ... }
```

### 6.5. Checklist Performance

- [ ] Enable SQL logging trong development để phát hiện N+1
- [ ] Sử dụng `@BatchSize` global hoặc JOIN FETCH
- [ ] Tắt OSIV trong production
- [ ] Dùng DTO Projection cho read-only queries
- [ ] Paginate đúng cách (không JOIN FETCH với Page)
- [ ] Đánh index cho columns thường query
- [ ] Dùng `@Transactional(readOnly = true)` cho read operations
- [ ] Batch insert với SEQUENCE strategy thay vì IDENTITY

### 6.6. Logging để debug

```properties
# application.properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Hoặc dùng datasource-proxy
spring.datasource.driver-class-name=net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener
```
