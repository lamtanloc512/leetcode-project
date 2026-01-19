package com.practice.leetcode.sql;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                             NoSQL GUIDE                                   ║
 * ║               Best Practices, Niches & Advanced Techniques                ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class NoSQLGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // NoSQL CATEGORIES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌────────────────┬─────────────────────┬────────────────────────────────────┐
   * │ Type           │ Examples            │ Use Cases                          │
   * ├────────────────┼─────────────────────┼────────────────────────────────────┤
   * │ Document       │ MongoDB, CouchDB    │ Content management, catalogs       │
   * │ Key-Value      │ Redis, DynamoDB     │ Caching, sessions, real-time       │
   * │ Wide-Column    │ Cassandra, HBase    │ Time-series, IoT, analytics        │
   * │ Graph          │ Neo4j, Amazon Neptune│ Social networks, recommendations  │
   * └────────────────┴─────────────────────┴────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // KHI NÀO DÙNG NoSQL?
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ✓ Schema flexible, thay đổi thường xuyên
   * ✓ Horizontal scaling quan trọng
   * ✓ High write throughput (IoT, logs, events)
   * ✓ Denormalized data (embedded documents)
   * ✓ Geographic distribution
   * ✓ Simple queries (key lookups, no complex JOINs)
   *
   * ✗ KHÔNG dùng khi:
   * ✗ Complex transactions cần ACID
   * ✗ Complex JOINs và relationships
   * ✗ Strong consistency là bắt buộc
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // MONGODB - Document Database
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CONCEPTS:
   * • Database → Collections → Documents (BSON/JSON)
   * • _id field (auto-generated ObjectId or custom)
   * • Embedded documents vs References
   *
   * WHEN TO EMBED vs REFERENCE:
   * ┌────────────────────────────────────────────────────────────────────────────┐
   * │ EMBED when:                    │ REFERENCE when:                          │
   * │ • Data always accessed together│ • Many-to-many relationships             │
   * │ • 1:1 or 1:Few relationships   │ • Large subdocuments                     │
   * │ • Data doesn't change often    │ • Data changes frequently                │
   * │ • Bounded arrays               │ • Unbounded growth                       │
   * └────────────────────────────────────────────────────────────────────────────┘
   *
   * INDEXING:
   * db.users.createIndex({ email: 1 })           // Single field
   * db.users.createIndex({ name: 1, age: -1 })   // Compound
   * db.users.createIndex({ email: 1 }, { unique: true })
   * db.users.createIndex({ location: "2dsphere" }) // Geospatial
   * db.articles.createIndex({ content: "text" })   // Full-text search
   *
   * COMMON PATTERNS:
   * • Bucket pattern: Group time-series data into buckets
   * • Polymorphic pattern: Different document structures in same collection
   * • Outlier pattern: Handle unbounded arrays with overflow documents
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // REDIS - Key-Value / Cache
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * DATA STRUCTURES:
   * • Strings: Simple key-value, counters
   * • Lists: Queues, recent items
   * • Sets: Tags, unique items
   * • Sorted Sets: Leaderboards, priority queues
   * • Hashes: Object-like storage
   * • Streams: Event logs, message queues
   *
   * COMMON PATTERNS:
   *
   * 1. CACHING
   *    SET user:123 "{...}" EX 3600    // Expire in 1 hour
   *    GET user:123
   *
   * 2. SESSION STORAGE
   *    HSET session:abc user_id 123 expires_at 1234567890
   *    HGETALL session:abc
   *
   * 3. RATE LIMITING
   *    INCR rate:user:123:minute
   *    EXPIRE rate:user:123:minute 60
   *
   * 4. LEADERBOARD
   *    ZADD leaderboard 100 "user1" 200 "user2"
   *    ZREVRANGE leaderboard 0 9 WITHSCORES   // Top 10
   *
   * 5. DISTRIBUTED LOCK
   *    SET lock:resource NX EX 30    // NX = Only if not exists
   *
   * 6. PUB/SUB
   *    PUBLISH channel message
   *    SUBSCRIBE channel
   *
   * PERSISTENCE:
   * • RDB: Periodic snapshots
   * • AOF: Append-only file (every write)
   * • Hybrid: RDB + AOF
   *
   * CLUSTER:
   * • Sharding: Data distributed across nodes
   * • Replication: Master-Replica for HA
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DYNAMODB - AWS Key-Value
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CONCEPTS:
   * • Tables → Items → Attributes
   * • Partition Key (PK) + optional Sort Key (SK)
   * • Provisioned vs On-Demand capacity
   *
   * SINGLE TABLE DESIGN:
   * Thay vì nhiều tables, dùng 1 table với PK/SK patterns
   *
   * PK              │ SK              │ Data
   * ────────────────┼─────────────────┼─────────────────────
   * USER#123        │ PROFILE         │ { name, email }
   * USER#123        │ ORDER#001       │ { total, status }
   * USER#123        │ ORDER#002       │ { total, status }
   * ORDER#001       │ ORDER#001       │ { user_id, items }
   *
   * GSI (Global Secondary Index):
   * • Query by different attributes
   * • Example: GSI1PK = status, GSI1SK = created_at
   *
   * PATTERNS:
   * • Adjacency list: Model relationships
   * • Time-series: SK = timestamp
   * • Hierarchical: SK = path (ORG#1#DEPT#2#EMP#3)
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CASSANDRA - Wide-Column
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CONCEPTS:
   * • Keyspace → Tables → Rows → Columns
   * • Partition Key: Determines data distribution
   * • Clustering Key: Sorts data within partition
   * • Designed for writes, eventual consistency
   *
   * SCHEMA DESIGN:
   * • Design based on queries (denormalize!)
   * • 1 table per query pattern
   * • Avoid large partitions (< 100MB recommended)
   *
   * CREATE TABLE posts_by_user (
   *   user_id UUID,
   *   post_id TIMEUUID,
   *   content TEXT,
   *   PRIMARY KEY (user_id, post_id)
   * ) WITH CLUSTERING ORDER BY (post_id DESC);
   *
   * CONSISTENCY LEVELS:
   * • ONE, QUORUM, ALL
   * • Read + Write consistency > Replication Factor → Strong consistency
   * • Example: RF=3, Read=QUORUM, Write=QUORUM → Strong
   *
   * ANTI-PATTERNS:
   * ✗ Reading many partitions in one query
   * ✗ Large partitions (millions of rows)
   * ✗ Secondary indexes on high-cardinality columns
   * ✗ Updates/Deletes (creates tombstones)
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // ELASTICSEARCH - Search Engine
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CONCEPTS:
   * • Index → Documents (JSON)
   * • Inverted index for full-text search
   * • Near real-time search
   *
   * USE CASES:
   * • Full-text search
   * • Log analytics (ELK stack)
   * • Product search with filters
   * • Autocomplete
   *
   * MAPPINGS:
   * • text: Full-text search, analyzed
   * • keyword: Exact match, not analyzed
   * • date, long, double, boolean
   *
   * QUERY TYPES:
   * • match: Full-text search
   * • term: Exact match
   * • range: Numeric/date ranges
   * • bool: Combine queries (must, should, filter)
   *
   * AGGREGATIONS:
   * • Metrics: avg, sum, min, max, cardinality
   * • Bucket: terms, date_histogram, range
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DATA MODELING PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 1. DENORMALIZATION
   *    - Duplicate data để avoid JOINs
   *    - Trade-off: Storage vs Query performance
   *
   * 2. AGGREGATION
   *    - Pre-compute và store aggregates
   *    - Example: daily_counts, monthly_totals
   *
   * 3. BUCKETING
   *    - Group time-series data
   *    - 1 document = 1 hour of data
   *
   * 4. MATERIALIZED VIEWS
   *    - Pre-computed query results
   *    - Update on write or periodically
   *
   * 5. EVENT SOURCING
   *    - Store events, not state
   *    - Rebuild state from events
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CONSISTENCY & AVAILABILITY
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CAP THEOREM:
   * • CP: MongoDB, HBase (consistency over availability)
   * • AP: Cassandra, DynamoDB (availability over consistency)
   *
   * CONSISTENCY PATTERNS:
   * • Strong consistency: Immediate, expensive
   * • Eventual consistency: Eventually same, cheap
   * • Read-your-writes: User sees own changes
   *
   * CONFLICT RESOLUTION:
   * • Last-write-wins (LWW)
   * • Vector clocks
   * • Application-level merge
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SCALING PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 1. REPLICATION
   *    • Read replicas for read scaling
   *    • Primary-Secondary (async/sync)
   *
   * 2. SHARDING
   *    • Horizontal partitioning
   *    • Shard key selection critical!
   *    • Hot spots: Bad shard key → uneven distribution
   *
   * 3. CACHING LAYER
   *    • Redis/Memcached in front of database
   *    • Cache-aside, write-through patterns
   *
   * SHARD KEY SELECTION:
   * ✓ High cardinality
   * ✓ Even distribution
   * ✓ Query isolation (queries hit single shard)
   *
   * ✗ Monotonically increasing (timestamp, auto-increment)
   * ✗ Low cardinality (status, country)
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // BEST PRACTICES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * 1. Design for your queries first
   * 2. Denormalize strategically
   * 3. Choose right shard/partition key
   * 4. Set TTL for temporary data
   * 5. Monitor and tune regularly
   * 6. Plan for data growth
   * 7. Use indexes wisely (impact write performance)
   * 8. Implement proper backup/recovery
   * 9. Consider multi-region for HA
   * 10. Test with realistic data volumes
   */
}
