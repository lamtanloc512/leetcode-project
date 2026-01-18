package com.practice.leetcode.systemdesign;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║ SYSTEM DESIGN ESTIMATION GUIDE ║
 * ║ (Tính toán & Ước lượng cho System Design) ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 *
 * Trong phỏng vấn System Design, bạn thường được cho các THÔNG SỐ ĐẦU VÀO
 * và phải TÍNH TOÁN để đưa ra quyết định về:
 * - Storage (lưu trữ)
 * - Bandwidth (băng thông)
 * - Số servers/instances cần
 * - Chọn database phù hợp
 * - Caching strategy
 * - Sharding/Partitioning
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * BẢNG REFERENCE NUMBERS
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ POWER OF 2 │
 * │ │
 * │ 2^10 = 1 Thousand = 1 KB │
 * │ 2^20 = 1 Million = 1 MB │
 * │ 2^30 = 1 Billion = 1 GB │
 * │ 2^40 = 1 Trillion = 1 TB │
 * │ 2^50 = 1 Quadrillion = 1 PB │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ LATENCY NUMBERS (Độ trễ - cần nhớ!) │
 * │ │
 * │ L1 cache reference: 0.5 ns │
 * │ L2 cache reference: 7 ns │
 * │ Main memory reference: 100 ns │
 * │ SSD random read: 150 μs = 150,000 ns │
 * │ HDD seek: 10 ms = 10,000,000 ns │
 * │ Network round trip (same datacenter): 0.5 ms │
 * │ Network round trip (cross-region): 150 ms │
 * │ │
 * │ KEY INSIGHT: │
 * │ Memory >> SSD >> HDD │
 * │ Cache everything you can! │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │ TIME CONVERSIONS │
 * │ │
 * │ 1 day = 86,400 seconds ≈ 100,000 seconds (dễ nhớ) │
 * │ 1 month = 2.5 million seconds │
 * │ 1 year = 31 million seconds ≈ 30 million (dễ nhớ) │
 * └─────────────────────────────────────────────────────────────────────────────┘
 */
public class SystemDesignEstimationGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // STEP 1: QPS CALCULATION (Queries Per Second)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CÔNG THỨC TÍNH QPS:
   *
   * QPS = (Daily Active Users × Actions per User) / Seconds per Day
   *
   * Peak QPS = Average QPS × 2 (hoặc × 3 nếu có spike)
   *
   *
   * VÍ DỤ: Twitter-like service
   *
   * Given:
   * - 300 million monthly active users (MAU)
   * - 50% daily active users (DAU) = 150 million DAU
   * - Mỗi user đọc 100 tweets/ngày
   * - Mỗi user viết 2 tweets/ngày
   *
   * READ QPS:
   * = 150M × 100 / 100,000 seconds
   * = 15,000,000,000 / 100,000
   * = 150,000 QPS
   * Peak = 300,000 QPS
   *
   * WRITE QPS:
   * = 150M × 2 / 100,000
   * = 3,000 QPS
   * Peak = 6,000 QPS
   *
   * INSIGHT:
   * - Read-heavy system (50:1 ratio)
   * - Cần optimize cho reads: caching, CDN, read replicas
   */
  public static class QPSCalculator {

    public static double calculateQPS(long dailyActiveUsers,
        double actionsPerUser,
        int peakMultiplier) {
      double secondsPerDay = 86400;
      double averageQPS = (dailyActiveUsers * actionsPerUser) / secondsPerDay;
      return averageQPS * peakMultiplier;
    }

    // Ví dụ
    public static void example() {
      long DAU = 150_000_000L;

      double readQPS = calculateQPS(DAU, 100, 2); // Peak read
      double writeQPS = calculateQPS(DAU, 2, 2); // Peak write

      System.out.println("Read QPS: " + readQPS); // ~347,000
      System.out.println("Write QPS: " + writeQPS); // ~6,900
      System.out.println("Read:Write ratio = " + (readQPS / writeQPS)); // ~50:1
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // STEP 2: STORAGE ESTIMATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CÔNG THỨC TÍNH STORAGE:
   *
   * Daily Storage = Daily New Records × Size per Record
   * Yearly Storage = Daily Storage × 365
   * 5-Year Storage = Yearly × 5
   *
   *
   * VÍ DỤ: Twitter (tiếp)
   *
   * Given:
   * - 3,000 new tweets/second = 259M tweets/day
   * - Mỗi tweet:
   * + tweet_id: 8 bytes
   * + user_id: 8 bytes
   * + content: 280 chars = 280 bytes (UTF-8)
   * + timestamp: 8 bytes
   * + metadata: ~100 bytes
   * → Total: ~400 bytes per tweet
   *
   * Daily Storage:
   * = 259M × 400 bytes
   * = 103.6 GB/day
   *
   * Yearly:
   * = 103.6 × 365 = 37.8 TB/year
   *
   * 5-Year (retention):
   * = 189 TB
   *
   * Với replication factor 3:
   * = 567 TB
   *
   *
   * PROS & CONS của các storage options:
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ Storage Type │ Pros │ Cons │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ SQL (MySQL, │ ACID, joins, mature │ Hard to scale writes, │
   * │ PostgreSQL) │ tools │ schema changes costly │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ NoSQL (Cassandra│ Horizontal scaling, │ No joins, eventual │
   * │ MongoDB) │ flexible schema │ consistency │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Object Storage │ Cheap, unlimited, │ High latency, no queries │
   * │ (S3) │ durable │ │
   * └─────────────────────────────────────────────────────────────────────────┘
   */
  public static class StorageCalculator {

    public static long calculateDailyStorage(long recordsPerSecond,
        int bytesPerRecord) {
      long secondsPerDay = 86400;
      return recordsPerSecond * secondsPerDay * bytesPerRecord;
    }

    public static long calculateYearlyStorage(long dailyBytes) {
      return dailyBytes * 365;
    }

    public static String formatBytes(long bytes) {
      if (bytes >= 1_000_000_000_000L)
        return (bytes / 1_000_000_000_000L) + " TB";
      if (bytes >= 1_000_000_000L)
        return (bytes / 1_000_000_000L) + " GB";
      if (bytes >= 1_000_000L)
        return (bytes / 1_000_000L) + " MB";
      return bytes + " bytes";
    }

    public static void example() {
      long tweetsPerSecond = 3000;
      int bytesPerTweet = 400;

      long daily = calculateDailyStorage(tweetsPerSecond, bytesPerTweet);
      long yearly = calculateYearlyStorage(daily);

      System.out.println("Daily: " + formatBytes(daily)); // ~103 GB
      System.out.println("Yearly: " + formatBytes(yearly)); // ~37 TB
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // STEP 3: BANDWIDTH ESTIMATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CÔNG THỨC TÍNH BANDWIDTH:
   *
   * Bandwidth = QPS × Response Size
   *
   *
   * VÍ DỤ: Image sharing service (Instagram-like)
   *
   * Given:
   * - 10,000 image uploads/second
   * - Average image size: 500 KB
   *
   * Upload Bandwidth:
   * = 10,000 × 500 KB = 5,000,000 KB/s = 5 GB/s = 40 Gbps
   *
   * Với 100 views per image:
   * Download Bandwidth = 40 Gbps × 100 = 4000 Gbps = 4 Tbps
   *
   * SOLUTION:
   * - CDN để offload bandwidth
   * - Image compression
   * - Multiple image sizes (thumbnail, medium, full)
   */
  public static class BandwidthCalculator {

    public static double calculateBandwidthGbps(long qps, long bytesPerRequest) {
      long bitsPerSecond = qps * bytesPerRequest * 8;
      return bitsPerSecond / 1_000_000_000.0; // Convert to Gbps
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // STEP 4: SERVER ESTIMATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CÔNG THỨC TÍNH SỐ SERVERS:
   *
   * Một server thông thường có thể handle:
   * - CPU-bound tasks: 1000-5000 QPS
   * - Memory-bound tasks: 10,000-50,000 QPS
   * - I/O-bound tasks: 100-1000 QPS
   *
   * Number of Servers = Peak QPS / QPS per Server
   *
   *
   * VÍ DỤ:
   *
   * Peak Read QPS = 300,000
   * Giả sử mỗi server handle 5,000 QPS
   *
   * Số servers = 300,000 / 5,000 = 60 servers
   *
   * Với redundancy (N+2): 62 servers
   *
   *
   * SCALING STRATEGIES:
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ Strategy │ When to use │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Vertical (Scale │ Quick fix, small systems │
   * │ Up) │ CONS: Has ceiling, single point of failure │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Horizontal (Scale │ Large systems, high availability │
   * │ Out) │ CONS: Complexity, need load balancer │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Auto-scaling │ Variable load, cost optimization │
   * │ │ CONS: Cold start latency │
   * └─────────────────────────────────────────────────────────────────────────┘
   */
  public static class ServerCalculator {

    public static int calculateServers(long peakQPS, int qpsPerServer, int redundancy) {
      int baseServers = (int) Math.ceil((double) peakQPS / qpsPerServer);
      return baseServers + redundancy;
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // STEP 5: CACHING STRATEGY
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * CÔNG THỨC TÍNH CACHE SIZE:
   *
   * Cache Size = Working Set Size × Cache Ratio
   *
   * Working Set = Most frequently accessed data
   * Thường cache 20% data (Pareto: 80/20 rule)
   *
   *
   * VÍ DỤ: Twitter
   *
   * - 500 million total tweets
   * - Cache 20% most recent = 100 million tweets
   * - 400 bytes per tweet
   * - Cache size = 100M × 400 = 40 GB
   *
   * Redis memory:
   * - 1 Redis instance: 25-100 GB memory
   * - Cần: 40 GB → 2-3 Redis instances (with replication)
   *
   *
   * CACHE STRATEGIES:
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ Pattern │ Pros │ Cons │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Cache-Aside │ Simple, lazy loading │ Cache miss penalty │
   * │ (Read-through) │ │ │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Write-Through │ Data consistency │ Write latency │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Write-Behind │ Fast writes │ Data loss risk │
   * │ (Write-Back) │ │ │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Refresh-Ahead │ Low latency reads │ Complexity │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   *
   * EVICTION POLICIES:
   * - LRU (Least Recently Used) - Most common
   * - LFU (Least Frequently Used) - Good for stable access patterns
   * - TTL (Time To Live) - Good for time-sensitive data
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // STEP 6: DATABASE SHARDING
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * KHI NÀO CẦN SHARDING?
   *
   * - Single DB > 1-2 TB data
   * - Write QPS > 5,000-10,000
   * - Read QPS > 50,000 (với read replicas vẫn không đủ)
   *
   *
   * SHARDING KEYS (Partition Keys):
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ Key Type │ Pros │ Cons │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ User ID │ Data locality per user │ Hot users problem │
   * │ │ Good for user-centric │ │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Geographic │ Low latency per region │ Cross-region queries │
   * │ (Region) │ │ hard │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Time-based │ Good for time-series │ Recent data hotspot │
   * │ (Timestamp) │ Old data archival easy │ │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Hash-based │ Even distribution │ Range queries hard │
   * │ (Random) │ No hotspots │ │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   *
   * TÍNH SỐ SHARDS:
   *
   * Total Data Size = 200 TB
   * Target per Shard = 500 GB - 1 TB
   *
   * Number of Shards = 200 TB / 500 GB = 400 shards
   *
   * Với 3x replication = 1200 database instances
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // COMPLETE EXAMPLE: URL SHORTENER
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * REQUIREMENTS:
   * - 100 million new URLs per month
   * - Read:Write ratio = 100:1
   * - URL ~500 bytes, short code = 7 chars
   * - Keep for 5 years
   *
   *
   * STEP 1: QPS
   * Write: 100M / (30 days × 86400s) ≈ 40 URLs/second
   * Peak Write: 40 × 2 = 80/s
   *
   * Read: 40 × 100 = 4000/s
   * Peak Read: 8000/s
   *
   *
   * STEP 2: STORAGE
   * URLs per 5 years = 100M × 12 × 5 = 6 billion URLs
   * Size = 6B × 500 bytes = 3 TB
   *
   * → Single database có thể handle!
   * → Có thể dùng PostgreSQL hoặc MySQL
   *
   *
   * STEP 3: BANDWIDTH
   * Read: 8000 × 500 bytes = 4 MB/s = 32 Mbps
   * → Rất nhỏ, không cần lo
   *
   *
   * STEP 4: CACHING
   * Cache 20% hot URLs = 1.2 billion × 500 bytes = 600 GB
   * → 6-8 Redis instances (100GB each)
   *
   * Cache hit rate mục tiêu: 80%
   * → DB chỉ cần handle 1600 QPS (8000 × 20%)
   *
   *
   * STEP 5: SERVERS
   * 8000 QPS với 3000 QPS/server = 3 servers
   * Với redundancy: 5 servers
   *
   *
   * ARCHITECTURE DECISION:
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ Component │ Choice │ Reason │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Database │ PostgreSQL │ Single DB enough, strong │
   * │ │ (single master) │ consistency for URL mapping │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Cache │ Redis Cluster │ High read QPS, simple key-value │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ ID Generator │ Snowflake ID │ Distributed, sortable │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Load Balancer │ Nginx │ Low QPS, simple setup │
   * └─────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CHEAT SHEET - QUICK REFERENCE
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ QUICK FORMULAS │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ QPS = DAU × actions/user / 86400 │
   * │ Storage/day = QPS × bytes/record × 86400 │
   * │ Bandwidth = QPS × response_size │
   * │ Servers = Peak QPS / QPS per server │
   * │ Cache Size = Total Data × 20% │
   * │ Shards = Total Data / 500GB │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ TECHNOLOGY SELECTION │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ < 10K QPS read │ Single SQL database + cache │
   * │ < 100K QPS read │ SQL + Read replicas + Redis │
   * │ > 100K QPS read │ NoSQL (Cassandra, DynamoDB) │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ < 1K QPS write │ Single SQL database │
   * │ < 10K QPS write │ SQL with write-ahead log │
   * │ > 10K QPS write │ Sharded DB or NoSQL │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ < 1 TB data │ Single database │
   * │ 1-10 TB │ Vertical scaling or basic sharding │
   * │ > 10 TB │ Horizontal sharding required │
   * └─────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────┐
   * │ COMMON BOTTLENECKS │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ Symptom │ Solution │
   * ├─────────────────────────────────────────────────────────────────────────┤
   * │ High read latency │ Add cache, CDN, read replicas │
   * │ High write latency │ Async writes, message queue │
   * │ Database CPU high │ Add indexes, query optimization │
   * │ Memory pressure │ Increase cache TTL, compress data │
   * │ Network bottleneck │ CDN, edge servers, data compression │
   * │ Hot partitions │ Different sharding key, load balancing │
   * └─────────────────────────────────────────────────────────────────────────┘
   */
}
