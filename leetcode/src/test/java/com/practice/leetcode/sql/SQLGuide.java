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
}
