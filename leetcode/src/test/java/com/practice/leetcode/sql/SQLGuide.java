package com.practice.leetcode.sql;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                              SQL GUIDE                                    ║
 * ║               Best Practices, Niches & Advanced Techniques                ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class SQLGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // SQL vs NoSQL - KHI NÀO DÙNG SQL?
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * DÙNG SQL KHI:
   * ✓ Data có structure rõ ràng, ít thay đổi schema
   * ✓ Cần ACID transactions (Banking, E-commerce orders)
   * ✓ Complex queries với JOINs
   * ✓ Data integrity quan trọng (Foreign keys, constraints)
   * ✓ Reports, Analytics với complex aggregations
   *
   * POPULAR DATABASES:
   * • PostgreSQL - Feature-rich, JSONB support
   * • MySQL/MariaDB - Fast reads, widely used
   * • SQL Server - Enterprise, .NET ecosystem
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // INDEXING BEST PRACTICES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 1. B-TREE INDEX (Default)
   *    - Tốt cho: =, <, >, BETWEEN, LIKE 'prefix%'
   *    CREATE INDEX idx_users_email ON users(email);
   *
   * 2. COMPOSITE INDEX (Multi-column)
   *    - Order matters! Leftmost prefix rule
   *    CREATE INDEX idx_orders ON orders(user_id, created_at);
   *    -- ✓ Uses: WHERE user_id = 1
   *    -- ✗ NOT:  WHERE created_at > '2024-01-01' (missing leftmost!)
   *
   * 3. COVERING INDEX
   *    - Index chứa tất cả columns cần → không cần đọc table
   *    CREATE INDEX idx_cover ON orders(user_id, status, total);
   *
   * 4. PARTIAL INDEX (PostgreSQL)
   *    CREATE INDEX idx_active ON users(email) WHERE status = 'active';
   *
   * ANTI-PATTERNS:
   * ✗ Over-indexing (mỗi INSERT/UPDATE phải update indexes)
   * ✗ Index on low-cardinality columns
   * ✗ Functions on indexed columns: WHERE YEAR(date) = 2024
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // QUERY OPTIMIZATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 1. Use EXPLAIN / EXPLAIN ANALYZE
   * 2. SELECT only needed columns (not SELECT *)
   * 3. Use LIMIT for large result sets
   * 4. Use EXISTS thay vì IN cho subqueries lớn
   * 5. Batch INSERT operations
   * 6. Use UNION ALL thay vì UNION nếu không cần dedupe
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // TRANSACTIONS & ISOLATION LEVELS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ACID: Atomicity, Consistency, Isolation, Durability
   *
   * ISOLATION LEVELS (Low → High):
   * 1. READ UNCOMMITTED - Dirty reads possible
   * 2. READ COMMITTED   - No dirty reads (PostgreSQL default)
   * 3. REPEATABLE READ  - No non-repeatable reads (MySQL default)
   * 4. SERIALIZABLE     - Full isolation, slowest
   *
   * LOCKING:
   * • Pessimistic: SELECT ... FOR UPDATE (lock row)
   * • Optimistic: WHERE version = ? (check before update)
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // WINDOW FUNCTIONS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * -- Row numbering
   * SELECT name, ROW_NUMBER() OVER (ORDER BY salary DESC) as rank
   * FROM employees;
   *
   * -- Partition by department
   * SELECT name, department,
   *   RANK() OVER (PARTITION BY department ORDER BY salary DESC)
   * FROM employees;
   *
   * -- Running total
   * SELECT date, amount,
   *   SUM(amount) OVER (ORDER BY date) as running_total
   * FROM transactions;
   *
   * -- Moving average
   * SELECT date, price,
   *   AVG(price) OVER (ORDER BY date ROWS BETWEEN 6 PRECEDING AND CURRENT ROW)
   * FROM stock_prices;
   *
   * -- Lead/Lag
   * SELECT date, LAG(price, 1) OVER (ORDER BY date) as prev_price
   * FROM stock_prices;
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CTE (Common Table Expressions)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * -- Basic CTE
   * WITH active_users AS (
   *   SELECT * FROM users WHERE status = 'active'
   * )
   * SELECT * FROM active_users WHERE created_at > '2024-01-01';
   *
   * -- Recursive CTE (hierarchical data)
   * WITH RECURSIVE org_chart AS (
   *   SELECT id, name, manager_id, 1 as level
   *   FROM employees WHERE manager_id IS NULL
   *   UNION ALL
   *   SELECT e.id, e.name, e.manager_id, oc.level + 1
   *   FROM employees e JOIN org_chart oc ON e.manager_id = oc.id
   * )
   * SELECT * FROM org_chart;
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // UPSERT / MERGE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * -- PostgreSQL
   * INSERT INTO users (email, name) VALUES ('a@b.com', 'Test')
   * ON CONFLICT (email) DO UPDATE SET name = EXCLUDED.name;
   *
   * -- MySQL
   * INSERT INTO users (email, name) VALUES ('a@b.com', 'Test')
   * ON DUPLICATE KEY UPDATE name = VALUES(name);
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // PAGINATION PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 1. OFFSET (Simple but slow for large offsets)
   *    SELECT * FROM posts ORDER BY date DESC LIMIT 20 OFFSET 1000;
   *    ⚠️ DB phải scan 1000 rows trước!
   *
   * 2. KEYSET/CURSOR (Recommended for large datasets)
   *    SELECT * FROM posts
   *    WHERE (created_at, id) < ('2024-01-15', 12345)
   *    ORDER BY created_at DESC, id DESC LIMIT 20;
   *    ✓ Uses index efficiently
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // N+1 QUERY PROBLEM
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ✗ Bad: N+1 queries
   * SELECT * FROM orders;
   * -- For each order: SELECT * FROM users WHERE id = ?
   *
   * ✓ Good: Single JOIN
   * SELECT o.*, u.name FROM orders o JOIN users u ON o.user_id = u.id;
   *
   * ✓ Good: Batch fetch
   * SELECT * FROM users WHERE id IN (1, 2, 3, 4, 5);
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SCHEMA DESIGN
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * NORMALIZATION: 1NF → 2NF → 3NF (reduce redundancy)
   * DENORMALIZATION: Trade-off for read performance
   *
   * DATA TYPES:
   * • Primary Key: BIGINT or UUID
   * • Money: DECIMAL(19,4) or store cents as BIGINT
   * • Dates: TIMESTAMP WITH TIME ZONE
   * • JSON: JSONB (PostgreSQL)
   *
   * ALWAYS ADD: created_at, updated_at columns
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // USEFUL SNIPPETS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * -- Delete duplicates
   * DELETE FROM users WHERE id NOT IN (
   *   SELECT MIN(id) FROM users GROUP BY email
   * );
   *
   * -- Pivot table
   * SELECT user_id,
   *   SUM(CASE WHEN status='pending' THEN 1 ELSE 0 END) as pending,
   *   SUM(CASE WHEN status='done' THEN 1 ELSE 0 END) as done
   * FROM orders GROUP BY user_id;
   *
   * -- Random row
   * SELECT * FROM users ORDER BY RANDOM() LIMIT 1; -- PostgreSQL
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // MVCC (Multi-Version Concurrency Control)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ HOW MVCC WORKS                                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ • Mỗi row có hidden columns: xmin (created by TX), xmax (deleted by TX)     │
   * │ • Readers không block writers, writers không block readers                  │
   * │ • Mỗi transaction thấy "snapshot" của data tại thời điểm bắt đầu            │
   * │                                                                             │
   * │ EXAMPLE (PostgreSQL):                                                       │
   * │ TX1: BEGIN; SELECT * FROM users WHERE id=1;  -- xmin=100, name='Alice'      │
   * │ TX2: UPDATE users SET name='Bob' WHERE id=1; COMMIT;  -- Creates new row    │
   * │ TX1: SELECT * FROM users WHERE id=1;  -- Vẫn thấy 'Alice' (old version)     │
   * │ TX1: COMMIT;                                                                │
   * │ TX1: SELECT * FROM users WHERE id=1;  -- Giờ mới thấy 'Bob'                 │
   * │                                                                             │
   * │ VACUUM:                                                                     │
   * │ • PostgreSQL cần VACUUM để cleanup dead tuples (old versions)               │
   * │ • autovacuum chạy tự động, nhưng heavy write có thể cần manual tuning       │
   * │ • Dead tuples gây table bloat → performance degradation                     │
   * │                                                                             │
   * │ MYSQL InnoDB:                                                               │
   * │ • Dùng undo logs cho MVCC                                                   │
   * │ • Undo logs trong system tablespace (tự động cleanup)                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // LOCKING DEEP DIVE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ LOCK TYPES                                                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. ROW-LEVEL LOCKS (InnoDB, PostgreSQL)                                     │
   * │ ────────────────────────────────────────                                    │
   * │ SELECT ... FOR UPDATE     -- Exclusive lock, block other writers/readers   │
   * │ SELECT ... FOR SHARE      -- Shared lock, allow other readers              │
   * │ SELECT ... FOR UPDATE SKIP LOCKED  -- Skip locked rows                     │
   * │ SELECT ... FOR UPDATE NOWAIT       -- Fail immediately if locked           │
   * │                                                                             │
   * │ 2. TABLE-LEVEL LOCKS                                                        │
   * │ ─────────────────────                                                       │
   * │ LOCK TABLE users IN EXCLUSIVE MODE;                                         │
   * │ -- Blocks all concurrent access                                             │
   * │                                                                             │
   * │ 3. ADVISORY LOCKS (Application-level)                                       │
   * │ ──────────────────────────────────────                                      │
   * │ -- PostgreSQL                                                               │
   * │ SELECT pg_advisory_lock(12345);    -- Acquire lock                          │
   * │ SELECT pg_advisory_unlock(12345);  -- Release lock                          │
   * │                                                                             │
   * │ Use case: Prevent duplicate cron jobs, distributed locking                  │
   * │                                                                             │
   * │ ┌───────────────────────┬─────────────────┬───────────────────────────────┐ │
   * │ │ Lock Type             │ Blocks Writers? │ Blocks Readers?               │ │
   * │ ├───────────────────────┼─────────────────┼───────────────────────────────┤ │
   * │ │ FOR SHARE             │ Yes             │ No                            │ │
   * │ │ FOR UPDATE            │ Yes             │ Yes (only FOR UPDATE readers) │ │
   * │ │ FOR UPDATE SKIP LOCKED│ Yes (non-lock)  │ N/A (skips)                   │ │
   * │ └───────────────────────┴─────────────────┴───────────────────────────────┘ │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ OPTIMISTIC vs PESSIMISTIC LOCKING                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PESSIMISTIC (FOR UPDATE):                                                   │
   * │ BEGIN;                                                                      │
   * │ SELECT * FROM inventory WHERE product_id = 1 FOR UPDATE;                    │
   * │ UPDATE inventory SET quantity = quantity - 1 WHERE product_id = 1;          │
   * │ COMMIT;                                                                     │
   * │                                                                             │
   * │ ✓ Simple, guaranteed consistency                                            │
   * │ ✗ Blocks concurrent access, deadlock risk                                   │
   * │                                                                             │
   * │ OPTIMISTIC (Version column):                                                │
   * │ SELECT id, quantity, version FROM inventory WHERE product_id = 1;           │
   * │ -- App logic: new_quantity = quantity - 1                                   │
   * │ UPDATE inventory                                                            │
   * │   SET quantity = :new_quantity, version = version + 1                       │
   * │   WHERE product_id = 1 AND version = :old_version;                          │
   * │ -- Check rows affected: if 0, retry!                                        │
   * │                                                                             │
   * │ ✓ No locks, high concurrency                                                │
   * │ ✗ Must handle retries, not good for high contention                         │
   * │                                                                             │
   * │ WHEN TO USE:                                                                │
   * │ - Pessimistic: High contention, short transactions                          │
   * │ - Optimistic: Low contention, read-heavy                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DEADLOCKS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ HOW DEADLOCK OCCURS                                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ TX1: BEGIN; UPDATE accounts SET balance = 100 WHERE id = 1;  -- Lock row 1 │
   * │ TX2: BEGIN; UPDATE accounts SET balance = 200 WHERE id = 2;  -- Lock row 2 │
   * │ TX1: UPDATE accounts SET balance = 150 WHERE id = 2;  -- Wait for TX2      │
   * │ TX2: UPDATE accounts SET balance = 250 WHERE id = 1;  -- Wait for TX1      │
   * │                                                                             │
   * │ → DEADLOCK! DB sẽ kill 1 transaction                                        │
   * │                                                                             │
   * │ PREVENTION:                                                                 │
   * │ 1. Lock resources in consistent order                                       │
   * │    -- Always lock smaller ID first                                          │
   * │    UPDATE accounts SET ... WHERE id = 1;                                    │
   * │    UPDATE accounts SET ... WHERE id = 2;                                    │
   * │                                                                             │
   * │ 2. Use short transactions                                                   │
   * │                                                                             │
   * │ 3. Use SELECT ... FOR UPDATE tại đầu TX                                     │
   * │    -- Lock all needed rows upfront                                          │
   * │    SELECT * FROM accounts WHERE id IN (1, 2) FOR UPDATE;                    │
   * │                                                                             │
   * │ 4. Use NOWAIT to fail fast                                                  │
   * │    SELECT * FROM accounts WHERE id = 1 FOR UPDATE NOWAIT;                   │
   * │    -- Throw error immediately if locked                                     │
   * │                                                                             │
   * │ 5. Set lock_timeout                                                         │
   * │    SET lock_timeout = '5s';  -- Fail after 5 seconds                        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // QUERY EXECUTION PLAN (EXPLAIN)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ READING EXPLAIN OUTPUT                                                      │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'test@example.com';       │
   * │                                                                             │
   * │ KEY THINGS TO LOOK FOR:                                                     │
   * │                                                                             │
   * │ 1. SCAN TYPES (best → worst):                                               │
   * │ ┌──────────────────────┬─────────────────────────────────────────────────┐  │
   * │ │ Index Only Scan      │ Best: All data from index, no table access      │  │
   * │ │ Index Scan           │ Good: Uses index, then table lookup             │  │
   * │ │ Bitmap Index Scan    │ OK: Multiple indexes combined                   │  │
   * │ │ Seq Scan             │ Bad: Full table scan (unless small table)       │  │
   * │ └──────────────────────┴─────────────────────────────────────────────────┘  │
   * │                                                                             │
   * │ 2. COST:                                                                    │
   * │ cost=0.29..8.31 rows=1 width=64                                             │
   * │ ↑ startup    ↑ total   ↑ estimate rows                                      │
   * │                                                                             │
   * │ 3. ACTUAL TIME (EXPLAIN ANALYZE only):                                      │
   * │ actual time=0.015..0.016 rows=1 loops=1                                     │
   * │ ↑ startup    ↑ total    ↑ actual rows returned                              │
   * │                                                                             │
   * │ 4. JOINS:                                                                   │
   * │ • Nested Loop: Good for small tables                                        │
   * │ • Hash Join: Good for large tables, needs memory                            │
   * │ • Merge Join: Good when both inputs are sorted                              │
   * │                                                                             │
   * │ RED FLAGS:                                                                  │
   * │ ⚠️ Seq Scan on large table                                                  │
   * │ ⚠️ actual rows >> estimated rows (statistics outdated)                      │
   * │ ⚠️ Sort with high cost (consider index for ORDER BY)                        │
   * │ ⚠️ Nested Loop with high loops count                                        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // B-TREE INDEX INTERNALS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ B-TREE STRUCTURE                                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │                        ┌─────────────────┐                                  │
   * │                        │  Root Node      │                                  │
   * │                        │  [10 | 20 | 30] │                                  │
   * │                        └────┬────┬───┬───┘                                  │
   * │               ┌─────────────┘    │   └──────────────┐                       │
   * │               ▼                  ▼                  ▼                       │
   * │        ┌──────────┐       ┌──────────┐       ┌──────────┐                   │
   * │        │ [1|5|9]  │       │[11|15|19]│       │[21|25|29]│                   │
   * │        │ Leaf     │       │ Leaf     │       │ Leaf     │                   │
   * │        └──────────┘       └──────────┘       └──────────┘                   │
   * │             │                  │                  │                         │
   * │             └──────────────────┴──────────────────┘                         │
   * │                   Linked for range scans                                    │
   * │                                                                             │
   * │ PROPERTIES:                                                                 │
   * │ • Balanced: All leaves at same depth → O(log n) search                      │
   * │ • Sorted: Enables range queries, ORDER BY                                   │
   * │ • Leaf nodes linked: Efficient range scans                                  │
   * │ • High fanout: Shallow tree, few disk reads                                 │
   * │                                                                             │
   * │ WHY `LIKE 'prefix%'` WORKS:                                                 │
   * │ • B-tree is sorted → can binary search to 'prefix'                          │
   * │ • Then scan right until no longer matches                                   │
   * │                                                                             │
   * │ WHY `LIKE '%suffix'` DOESN'T WORK:                                          │
   * │ • No way to binary search → must full scan                                  │
   * │ • Solution: Reverse index (suffix stored as prefix)                         │
   * │   CREATE INDEX idx ON t(REVERSE(column));                                   │
   * │   WHERE REVERSE(column) LIKE REVERSE('%suffix');                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // PARTITIONING
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ TABLE PARTITIONING                                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ TYPES:                                                                      │
   * │ 1. RANGE: By date, ID range                                                 │
   * │ 2. LIST: By specific values (country, status)                               │
   * │ 3. HASH: Distribute evenly                                                  │
   * │                                                                             │
   * │ POSTGRESQL EXAMPLE (Range by date):                                         │
   * │ CREATE TABLE orders (                                                       │
   * │   id BIGINT, created_at TIMESTAMP, amount DECIMAL                           │
   * │ ) PARTITION BY RANGE (created_at);                                          │
   * │                                                                             │
   * │ CREATE TABLE orders_2024_01 PARTITION OF orders                             │
   * │   FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');                          │
   * │                                                                             │
   * │ CREATE TABLE orders_2024_02 PARTITION OF orders                             │
   * │   FOR VALUES FROM ('2024-02-01') TO ('2024-03-01');                          │
   * │                                                                             │
   * │ BENEFITS:                                                                   │
   * │ ✓ Partition pruning: Query chỉ scan partitions cần thiết                    │
   * │ ✓ Easy data lifecycle: DROP partition thay vì DELETE                        │
   * │ ✓ Parallel query trên multiple partitions                                   │
   * │ ✓ Smaller indexes per partition                                             │
   * │                                                                             │
   * │ USE WHEN:                                                                   │
   * │ • Table > 100GB                                                             │
   * │ • Time-series data cần archive                                              │
   * │ • Query patterns align với partition key                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // REPLICATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ REPLICATION TYPES                                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. SYNCHRONOUS:                                                             │
   * │    Primary ──write──▶ Replica (wait for ACK) ──▶ Commit                     │
   * │    ✓ Strong consistency, no data loss                                       │
   * │    ✗ Higher latency, availability affected if replica down                  │
   * │                                                                             │
   * │ 2. ASYNCHRONOUS:                                                            │
   * │    Primary ──write──▶ Commit ──async──▶ Replica                             │
   * │    ✓ Low latency, better availability                                       │
   * │    ✗ Replication lag, potential data loss                                   │
   * │                                                                             │
   * │ 3. SEMI-SYNCHRONOUS:                                                        │
   * │    Wait for at least 1 replica ACK before commit                            │
   * │    Balance between consistency and performance                              │
   * │                                                                             │
   * │ READ REPLICAS:                                                              │
   * │ ┌─────────┐                                                                 │
   * │ │ Primary │◀── All writes                                                   │
   * │ └────┬────┘                                                                 │
   * │      │ replicate                                                            │
   * │      ▼                                                                      │
   * │ ┌─────────┐ ┌─────────┐                                                     │
   * │ │Replica 1│ │Replica 2│◀── Scale reads                                      │
   * │ └─────────┘ └─────────┘                                                     │
   * │                                                                             │
   * │ REPLICATION LAG ISSUES:                                                     │
   * │ • Read-after-write: User creates data, immediate read returns empty         │
   * │ • Solution: Read from primary for recently written data                     │
   * │                                                                             │
   * │ FAILOVER:                                                                   │
   * │ • Automatic: Patroni, ProxySQL (PostgreSQL), MySQL Group Replication        │
   * │ • Watch out: Split-brain scenario                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CONNECTION POOLING
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ WHY CONNECTION POOLING?                                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PROBLEM:                                                                    │
   * │ • DB connection = expensive (TCP handshake, auth, session setup)            │
   * │ • PostgreSQL: ~2-5 MB per connection                                        │
   * │ • Max connections limited (default: 100)                                    │
   * │                                                                             │
   * │ SOLUTION:                                                                   │
   * │ ┌───────────────┐     ┌────────────────┐     ┌──────────┐                   │
   * │ │  Application  │────▶│ Connection Pool│────▶│ Database │                   │
   * │ │  (100 threads)│     │ (10 connections)│    │          │                   │
   * │ └───────────────┘     └────────────────┘     └──────────┘                   │
   * │                                                                             │
   * │ POPULAR POOLS:                                                              │
   * │ • HikariCP (fastest, default Spring Boot)                                   │
   * │ • PgBouncer (external, PostgreSQL)                                          │
   * │ • ProxySQL (external, MySQL)                                                │
   * │                                                                             │
   * │ HIKARICP CONFIG:                                                            │
   * │ spring.datasource.hikari.maximum-pool-size=10                               │
   * │ spring.datasource.hikari.minimum-idle=5                                     │
   * │ spring.datasource.hikari.connection-timeout=30000                           │
   * │ spring.datasource.hikari.max-lifetime=1800000                               │
   * │                                                                             │
   * │ POOL SIZE FORMULA:                                                          │
   * │ connections = ((core_count * 2) + effective_spindle_count)                  │
   * │ • For SSD: spindle_count = 1                                                │
   * │ • 4 cores = (4 * 2) + 1 = 9 connections                                     │
   * │                                                                             │
   * │ ⚠️ MORE CONNECTIONS ≠ BETTER                                                │
   * │ → Context switching overhead, lock contention                               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // INTERVIEW GOTCHAS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ COMMON DB INTERVIEW QUESTIONS                                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ Q: SELECT query chậm, làm sao debug?                                        │
   * │ A: 1. EXPLAIN ANALYZE                                                       │
   * │    2. Check Seq Scan → Add index                                            │
   * │    3. Check estimated vs actual rows → ANALYZE table                        │
   * │    4. Check for implicit type conversion                                    │
   * │    5. Check for functions on indexed columns                                │
   * │                                                                             │
   * │ Q: Sao index không được dùng?                                               │
   * │ A: • Query optimizer chọn Seq Scan (small table)                            │
   * │    • Function on column: WHERE LOWER(name) = 'x'                            │
   * │    • Type mismatch: WHERE id = '123' (id is INT)                            │
   * │    • Leading wildcard: LIKE '%abc'                                          │
   * │    • OR conditions: WHERE a=1 OR b=2                                        │
   * │    • Low selectivity column                                                 │
   * │                                                                             │
   * │ Q: Làm sao handle counter với high concurrency?                             │
   * │ A: • Option 1: SELECT FOR UPDATE (low throughput)                           │
   * │    • Option 2: UPDATE counter SET value = value + 1                         │
   * │    • Option 3: Sharded counters (multiple rows, sum on read)                │
   * │    • Option 4: Redis INCR (eventually persist to DB)                        │
   * │                                                                             │
   * │ Q: Soft delete vs Hard delete?                                              │
   * │ A: Soft: UPDATE SET deleted_at = NOW()                                      │
   * │    ✓ Audit trail, easy restore                                              │
   * │    ✗ Queries cần WHERE deleted_at IS NULL                                   │
   * │    ✗ Unique constraints phức tạp                                            │
   * │    Hard: DELETE FROM table                                                  │
   * │    ✓ Simple, clean data                                                     │
   * │    ✗ No recovery without backup                                             │
   * │                                                                             │
   * │ Q: Tại sao không dùng UUID làm primary key?                                 │
   * │ A: • Random → Poor B-tree performance (page splits)                         │
   * │    • Larger size (16 bytes vs 8 bytes BIGINT)                               │
   * │    • Solution: UUID v7 (time-ordered), ULID                                 │
   * │                                                                             │
   * │ Q: BIGINT vs INT cho ID?                                                    │
   * │ A: • INT: max 2.1 billion (5KB inserts/sec = 13 years)                      │
   * │    • BIGINT: 9 quintillion (practically infinite)                           │
   * │    • Storage: 4 bytes vs 8 bytes                                            │
   * │    • Recommendation: Always BIGINT for new systems                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DATABASE COMPARISON
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ MYSQL vs POSTGRESQL                                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌────────────────────┬─────────────────────┬─────────────────────────────┐  │
   * │ │                    │ MySQL               │ PostgreSQL                  │  │
   * │ ├────────────────────┼─────────────────────┼─────────────────────────────┤  │
   * │ │ Philosophy         │ Speed, simplicity   │ Standards, features         │  │
   * │ │ Default Isolation  │ REPEATABLE READ     │ READ COMMITTED              │  │
   * │ │ JSON Support       │ JSON (basic)        │ JSONB (indexed, faster)     │  │
   * │ │ Full-text Search   │ Basic               │ Advanced (tsvector)         │  │
   * │ │ Array/Range types  │ ❌                  │ ✓ Native                    │  │
   * │ │ Upsert             │ ON DUPLICATE KEY    │ ON CONFLICT                 │  │
   * │ │ Window Functions   │ Limited (older)     │ Full support                │  │
   * │ │ CTEs               │ Since 8.0           │ Full + Recursive            │  │
   * │ │ Partial Indexes    │ ❌                  │ ✓                           │  │
   * │ │ Materialized Views │ ❌                  │ ✓                           │  │
   * │ │ Extensions         │ Limited             │ Rich (PostGIS, pg_trgm...)  │  │
   * │ │ Replication        │ Easy setup          │ More complex                │  │
   * │ │ Clustering         │ MySQL Cluster, Vitess│ Citus, Patroni             │  │
   * │ └────────────────────┴─────────────────────┴─────────────────────────────┘  │
   * │                                                                             │
   * │ CHỌN MYSQL KHI:                                                             │
   * │ • Simple CRUD, read-heavy workloads                                         │
   * │ • Team familiar với MySQL ecosystem                                         │
   * │ • Need easy master-slave replication                                        │
   * │ • WordPress, common CMSes                                                   │
   * │                                                                             │
   * │ CHỌN POSTGRESQL KHI:                                                        │
   * │ • Complex queries, advanced data types                                      │
   * │ • Need JSONB, GIS, Full-text search                                         │
   * │ • Data integrity critical                                                   │
   * │ • Analytics, reporting                                                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SQL vs NoSQL                                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌────────────────────┬─────────────────────┬─────────────────────────────┐  │
   * │ │                    │ SQL (RDBMS)         │ NoSQL                       │  │
   * │ ├────────────────────┼─────────────────────┼─────────────────────────────┤  │
   * │ │ Schema             │ Fixed, structured   │ Flexible, schemaless        │  │
   * │ │ Scaling            │ Vertical (harder)   │ Horizontal (easier)         │  │
   * │ │ ACID               │ ✓ Full support      │ Varies (eventual cons.)     │  │
   * │ │ JOINs              │ ✓ Efficient         │ ✗ or Limited                │  │
   * │ │ Query language     │ SQL (standard)      │ Varies per DB               │  │
   * │ │ Use case           │ Transactions, BI    │ Big data, real-time         │  │
   * │ └────────────────────┴─────────────────────┴─────────────────────────────┘  │
   * │                                                                             │
   * │ NOSQL TYPES:                                                                │
   * │ • Document: MongoDB, CouchDB (JSON docs)                                    │
   * │ • Key-Value: Redis, DynamoDB (fast cache)                                   │
   * │ • Wide-Column: Cassandra, HBase (time-series)                               │
   * │ • Graph: Neo4j, Amazon Neptune (relationships)                              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DATABASE SCALING STRATEGIES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SCALING APPROACHES                                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. VERTICAL SCALING (Scale Up)                                              │
   * │ ──────────────────────────────                                              │
   * │ Nâng cấp hardware: More CPU, RAM, faster SSD                                │
   * │                                                                             │
   * │ ✓ Pros: Simple, no code changes                                             │
   * │ ✗ Cons: Limited by hardware, expensive, single point of failure             │
   * │                                                                             │
   * │ AWS RDS: db.t3.micro → db.r5.24xlarge (768GB RAM)                           │
   * │                                                                             │
   * │ 2. READ REPLICAS (Scale Reads)                                              │
   * │ ──────────────────────────────                                              │
   * │ ┌──────────┐                                                                │
   * │ │ Primary  │◀── All WRITES                                                  │
   * │ │ (Master) │                                                                │
   * │ └────┬─────┘                                                                │
   * │      │ Async replication                                                    │
   * │      ├─────────────────┬─────────────────┐                                  │
   * │      ▼                 ▼                 ▼                                  │
   * │ ┌─────────┐      ┌─────────┐      ┌─────────┐                               │
   * │ │ Replica │      │ Replica │      │ Replica │◀── All READS                  │
   * │ └─────────┘      └─────────┘      └─────────┘                               │
   * │                                                                             │
   * │ ✓ Pros: Easy to setup, scale reads linearly                                 │
   * │ ✗ Cons: Replication lag, writes not scaled                                  │
   * │                                                                             │
   * │ 3. HORIZONTAL SCALING (Sharding)                                            │
   * │ ──────────────────────────────────                                          │
   * │ Distribute data across multiple servers                                     │
   * │                                                                             │
   * │ ┌─────────────────────────────────────────────────────────────────────────┐ │
   * │ │                          Application Layer                              │ │
   * │ │                               │                                         │ │
   * │ │                    ┌──────────┼──────────┐                               │ │
   * │ │                    ▼          ▼          ▼                               │ │
   * │ │              ┌─────────┐┌─────────┐┌─────────┐                           │ │
   * │ │              │ Shard 1 ││ Shard 2 ││ Shard 3 │                           │ │
   * │ │              │ Users   ││ Users   ││ Users   │                           │ │
   * │ │              │ A-H     ││ I-P     ││ Q-Z     │                           │ │
   * │ │              └─────────┘└─────────┘└─────────┘                           │ │
   * │ └─────────────────────────────────────────────────────────────────────────┘ │
   * │                                                                             │
   * │ ✓ Pros: Scale both reads and writes, no single point of failure             │
   * │ ✗ Cons: Complex, cross-shard queries expensive, rebalancing hard            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SHARDING STRATEGIES                                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. RANGE-BASED SHARDING                                                     │
   * │    Shard 1: user_id 1-1M                                                    │
   * │    Shard 2: user_id 1M-2M                                                   │
   * │                                                                             │
   * │    ✓ Range queries efficient                                                │
   * │    ✗ Hot spots if recent data accessed more                                 │
   * │                                                                             │
   * │ 2. HASH-BASED SHARDING                                                      │
   * │    shard_id = hash(user_id) % num_shards                                    │
   * │                                                                             │
   * │    ✓ Even distribution                                                      │
   * │    ✗ Adding shards requires rehashing (consistent hashing helps)            │
   * │                                                                             │
   * │ 3. DIRECTORY-BASED SHARDING                                                 │
   * │    Lookup table: entity_id → shard_id                                       │
   * │                                                                             │
   * │    ✓ Flexible, easy to move data                                            │
   * │    ✗ Lookup table is SPOF, extra lookup latency                             │
   * │                                                                             │
   * │ 4. GEO-BASED SHARDING                                                       │
   * │    US users → US shard, EU users → EU shard                                 │
   * │                                                                             │
   * │    ✓ Low latency for users                                                  │
   * │    ✗ Cross-region queries complex                                           │
   * │                                                                             │
   * │ SHARD KEY SELECTION (Critical!):                                            │
   * │ • High cardinality (many unique values)                                     │
   * │ • Even distribution                                                         │
   * │ • Frequently used in queries (avoid cross-shard)                            │
   * │ • Examples: user_id, tenant_id, order_date                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // MASTER-SLAVE ARCHITECTURE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ MASTER-SLAVE (PRIMARY-REPLICA)                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ┌───────────────────────────────────────────────────────────────────────┐   │
   * │ │                        Load Balancer                                  │   │
   * │ │                     (ProxySQL, HAProxy)                               │   │
   * │ │                            │                                          │   │
   * │ │              ┌─────────────┴─────────────┐                             │   │
   * │ │              │ Writes      │             │ Reads                      │   │
   * │ │              ▼             │             ▼                            │   │
   * │ │         ┌─────────┐       │        ┌─────────┐                        │   │
   * │ │         │ MASTER  │───────┼───────▶│ SLAVE 1 │                        │   │
   * │ │         │ (RW)    │       │        │ (R)     │                        │   │
   * │ │         └─────────┘       │        └─────────┘                        │   │
   * │ │              │            │             │                             │   │
   * │ │              │ Binary Log │             │                             │   │
   * │ │              └────────────┼─────────────┼─────────────┐               │   │
   * │ │                           │             ▼             ▼               │   │
   * │ │                           │        ┌─────────┐   ┌─────────┐          │   │
   * │ │                           │        │ SLAVE 2 │   │ SLAVE 3 │          │   │
   * │ │                           │        │ (R)     │   │ (R)     │          │   │
   * │ │                           │        └─────────┘   └─────────┘          │   │
   * │ └───────────────────────────┴───────────────────────────────────────────┘   │
   * │                                                                             │
   * │ HOW IT WORKS:                                                               │
   * │ 1. Master receives write → writes to binary log                             │
   * │ 2. Slaves replicate binary log → apply changes                              │
   * │ 3. Reads distributed across slaves                                          │
   * │                                                                             │
   * │ SPRING BOOT CONFIG (Multiple DataSources):                                  │
   * │ @Configuration                                                              │
   * │ public class DataSourceConfig {                                             │
   * │     @Bean @Primary                                                          │
   * │     public DataSource masterDataSource() { ... }                            │
   * │                                                                             │
   * │     @Bean                                                                   │
   * │     public DataSource slaveDataSource() { ... }                             │
   * │ }                                                                           │
   * │                                                                             │
   * │ // Routing DataSource                                                       │
   * │ public class RoutingDataSource extends AbstractRoutingDataSource {          │
   * │     @Override                                                               │
   * │     protected Object determineCurrentLookupKey() {                          │
   * │         return TransactionSynchronizationManager.isCurrentTransactionReadOnly()│
   * │             ? "slave" : "master";                                           │
   * │     }                                                                       │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ REPLICATION LAG PROBLEMS                                                    │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ PROBLEM: User writes → Reads from slave → Data not there yet!               │
   * │                                                                             │
   * │ Timeline:                                                                   │
   * │ T0: User creates post (write to master)                                     │
   * │ T0: Master: ✓ has post                                                      │
   * │ T0: Slave: ❌ lag, no post                                                  │
   * │ T1: User refreshes → reads from slave → "Where's my post?!"                 │
   * │ T2: Slave finally replicates                                                │
   * │                                                                             │
   * │ SOLUTIONS:                                                                  │
   * │                                                                             │
   * │ 1. READ FROM MASTER (after write)                                           │
   * │    // Force read from master for X seconds after write                      │
   * │    Session: last_write_time = now()                                         │
   * │    if (now - last_write_time < 5s) → read from master                       │
   * │                                                                             │
   * │ 2. CAUSAL CONSISTENCY                                                       │
   * │    Track LSN (Log Sequence Number)                                          │
   * │    Write returns LSN → subsequent reads wait for slave to catch up          │
   * │                                                                             │
   * │ 3. SYNCHRONOUS REPLICATION                                                  │
   * │    Wait for at least 1 slave before ACK                                     │
   * │    ✗ Higher latency                                                         │
   * │                                                                             │
   * │ 4. STICKY SESSIONS                                                          │
   * │    Route same user to same server for duration                              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ FAILOVER STRATEGIES                                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ WHEN MASTER FAILS:                                                          │
   * │                                                                             │
   * │ 1. MANUAL FAILOVER                                                          │
   * │    • DBA promotes slave to master                                           │
   * │    • Update connection strings                                              │
   * │    • Minutes of downtime                                                    │
   * │                                                                             │
   * │ 2. AUTOMATIC FAILOVER                                                       │
   * │    Tools: Patroni, Orchestrator, MySQL Group Replication                    │
   * │                                                                             │
   * │    ┌─────────┐     ┌─────────┐     ┌─────────┐                              │
   * │    │ Master  │────▶│ Slave 1 │     │ Slave 2 │                              │
   * │    │ (fails) │     │         │     │         │                              │
   * │    └────╳────┘     └────┬────┘     └─────────┘                              │
   * │                         │                                                   │
   * │                         ▼ Promoted to Master                                │
   * │                    ┌─────────┐                                              │
   * │                    │ NEW     │                                              │
   * │                    │ Master  │                                              │
   * │                    └─────────┘                                              │
   * │                                                                             │
   * │ SPLIT-BRAIN PROBLEM:                                                        │
   * │ Network partition → Both servers think they're master                       │
   * │ → Data divergence, corruption!                                              │
   * │                                                                             │
   * │ PREVENTION:                                                                 │
   * │ • Quorum-based (majority votes)                                             │
   * │ • STONITH (Shoot The Other Node In The Head)                                │
   * │ • Fencing (cut off network/storage access)                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CAP THEOREM
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ CAP THEOREM                                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ In distributed system, can only guarantee 2 of 3:                           │
   * │                                                                             │
   * │                        Consistency                                          │
   * │                           /\                                                │
   * │                          /  \                                               │
   * │                         /    \                                              │
   * │                        / CA   \                                             │
   * │                       /  (SQL) \                                            │
   * │                      /──────────\                                           │
   * │                     /            \                                          │
   * │          CP        /              \        AP                               │
   * │       (MongoDB,   /                \   (Cassandra,                          │
   * │        HBase)    /                  \   DynamoDB)                           │
   * │                 ▲──────────────────▲                                        │
   * │            Partition         Availability                                   │
   * │            Tolerance                                                        │
   * │                                                                             │
   * │ DEFINITIONS:                                                                │
   * │ • Consistency: All nodes see same data at same time                         │
   * │ • Availability: Every request gets response (success/failure)               │
   * │ • Partition Tolerance: System works despite network failures                │
   * │                                                                             │
   * │ REALITY: Network partitions WILL happen                                     │
   * │ → Must choose between C and A during partition                              │
   * │                                                                             │
   * │ ┌──────────────┬─────────────────────┬────────────────────────────────────┐ │
   * │ │ Choice       │ During Partition    │ Examples                           │ │
   * │ ├──────────────┼─────────────────────┼────────────────────────────────────┤ │
   * │ │ CP           │ May reject writes   │ MongoDB, HBase, Redis Cluster      │ │
   * │ │              │ to ensure consistency│                                   │ │
   * │ ├──────────────┼─────────────────────┼────────────────────────────────────┤ │
   * │ │ AP           │ Accept writes,      │ Cassandra, DynamoDB, CouchDB       │ │
   * │ │              │ eventual consistency│                                    │ │
   * │ ├──────────────┼─────────────────────┼────────────────────────────────────┤ │
   * │ │ CA           │ Not possible in     │ Single-node RDBMS (no partition)   │ │
   * │ │              │ distributed system  │                                    │ │
   * │ └──────────────┴─────────────────────┴────────────────────────────────────┘ │
   * │                                                                             │
   * │ PACELC (Extended CAP):                                                      │
   * │ If Partition → choose A or C                                                │
   * │ Else → choose Latency or Consistency                                        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ══════════════════════════════════════════════════════════════════════════════
  // SCALING DECISION FLOW
  // ══════════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ WHEN TO SCALE? (Decision Tree)                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ START: DB getting slow?                                                     │
   * │     │                                                                       │
   * │     ▼                                                                       │
   * │ ┌────────────────────┐                                                      │
   * │ │ 1. OPTIMIZE FIRST  │ ◀─── Don't scale if you can optimize!                │
   * │ │    • Add indexes   │                                                      │
   * │ │    • Query tuning  │                                                      │
   * │ │    • Connection pool│                                                     │
   * │ │    • Caching (Redis)│                                                     │
   * │ └─────────┬──────────┘                                                      │
   * │           │ Still slow?                                                     │
   * │           ▼                                                                 │
   * │ ┌────────────────────┐                                                      │
   * │ │ 2. VERTICAL SCALE  │ ◀─── Simplest, try first                             │
   * │ │    • More RAM      │                                                      │
   * │ │    • More CPU      │                                                      │
   * │ │    • Faster SSD    │                                                      │
   * │ └─────────┬──────────┘                                                      │
   * │           │ Hit hardware limits?                                            │
   * │           ▼                                                                 │
   * │ ┌────────────────────┐      ┌────────────────────┐                          │
   * │ │ Read-heavy?        │─Yes─▶│ 3. READ REPLICAS   │                          │
   * │ │                    │      │    Scale reads     │                          │
   * │ └─────────┬──────────┘      └────────────────────┘                          │
   * │           │ No (Write-heavy)                                                │
   * │           ▼                                                                 │
   * │ ┌────────────────────┐                                                      │
   * │ │ 4. SHARDING        │ ◀─── Last resort, highest complexity                 │
   * │ │    Scale writes    │                                                      │
   * │ │    Consider NoSQL? │                                                      │
   * │ └────────────────────┘                                                      │
   * │                                                                             │
   * │ TYPICAL SCALING JOURNEY:                                                    │
   * │ 0-10K users:  Single DB, optimize queries                                   │
   * │ 10K-100K:     Vertical scaling + caching                                    │
   * │ 100K-1M:      Read replicas                                                 │
   * │ 1M-10M:       Sharding or managed DB (Aurora, Spanner)                      │
   * │ 10M+:         Multi-region, specialized solutions                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
