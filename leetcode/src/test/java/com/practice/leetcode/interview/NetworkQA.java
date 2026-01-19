package com.practice.leetcode.interview;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                       NETWORK INTERVIEW Q&A                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class NetworkQA {

  // ═══════════════════════════════════════════════════════════════════════════
  // OSI MODEL
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: 7 layers của OSI Model?                                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 7. Application  - HTTP, FTP, DNS       - Data                               │
   * │ 6. Presentation - SSL/TLS, JPEG        - Data                               │
   * │ 5. Session      - NetBIOS, RPC         - Data                               │
   * │ 4. Transport    - TCP, UDP             - Segment                            │
   * │ 3. Network      - IP, Router           - Packet                             │
   * │ 2. Data Link    - Ethernet, Switch     - Frame                              │
   * │ 1. Physical     - Cables, Hubs         - Bits                               │
   * │                                                                             │
   * │ MẸO NHỚ: "All People Seem To Need Data Processing"                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // TCP vs UDP
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: TCP vs UDP?                                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                     │ TCP                 │ UDP                             │
   * │ Connection          │ Connection-oriented │ Connectionless                  │
   * │ Reliability         │ Guaranteed          │ No guarantee                    │
   * │ Ordering            │ Ordered             │ No ordering                     │
   * │ Speed               │ Slower              │ Faster                          │
   * │                                                                             │
   * │ TCP: HTTP, Email, File transfer, SSH (need reliability)                     │
   * │ UDP: DNS, Video streaming, Gaming, VoIP (need speed)                        │
   * │                                                                             │
   * │ TCP 3-WAY HANDSHAKE:                                                        │
   * │ Client → SYN → Server                                                       │
   * │ Client ← SYN-ACK ← Server                                                   │
   * │ Client → ACK → Server → ESTABLISHED                                         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // HTTP VERSIONS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: HTTP/1.1 vs HTTP/2 vs HTTP/3?                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                 │ HTTP/1.1     │ HTTP/2       │ HTTP/3                      │
   * │ Transport       │ TCP          │ TCP          │ QUIC (UDP)                  │
   * │ Format          │ Text         │ Binary       │ Binary                      │
   * │ Multiplexing    │ ❌           │ ✓            │ ✓ (better)                  │
   * │ Header compress │ ❌           │ HPACK        │ QPACK                       │
   * │ Server Push     │ ❌           │ ✓            │ ✓                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: HTTP Status Codes?                                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 2xx - Success: 200 OK, 201 Created, 204 No Content                          │
   * │ 3xx - Redirect: 301 Permanent, 302 Temporary, 304 Not Modified              │
   * │ 4xx - Client Error: 400 Bad Request, 401 Unauthorized, 403 Forbidden        │
   * │ 5xx - Server Error: 500 Internal, 502 Bad Gateway, 503 Unavailable          │
   * │                                                                             │
   * │ 401 vs 403: 401 = not authenticated, 403 = authenticated but not allowed    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: HTTP Methods và idempotency?                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ GET      │ Safe ✓ │ Idempotent ✓ │ Read resource                            │
   * │ PUT      │ ✗      │ ✓            │ Replace resource                         │
   * │ DELETE   │ ✗      │ ✓            │ Delete resource                          │
   * │ POST     │ ✗      │ ✗            │ Create resource                          │
   * │ PATCH    │ ✗      │ ✗            │ Partial update                           │
   * │                                                                             │
   * │ SAFE: Không thay đổi server state                                           │
   * │ IDEMPOTENT: Gọi nhiều lần = gọi 1 lần                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // DNS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: DNS Resolution?                                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 1. Browser cache → 2. OS cache → 3. Resolver cache                          │
   * │ 4. Root DNS → 5. TLD servers (.com) → 6. Authoritative nameserver           │
   * │                                                                             │
   * │ RECORD TYPES:                                                               │
   * │ A: Domain → IPv4 | AAAA: → IPv6 | CNAME: alias | MX: Mail | NS: Nameserver  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // LOAD BALANCING & CDN
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: L4 vs L7 Load Balancing?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ L4 (Transport): Based on IP + Port, faster, content-agnostic                │
   * │ L7 (Application): Based on URL/headers, smarter routing, can terminate SSL  │
   * │                                                                             │
   * │ ALGORITHMS: Round Robin, Weighted, Least Connections, IP Hash               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: CDN (Content Delivery Network)?                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ Network các edge servers phân tán địa lý                                    │
   * │ BENEFITS: Giảm latency, giảm origin load, DDoS protection                   │
   * │ CACHE: Images, CSS, JS, videos (static content)                             │
   * │ EXAMPLES: CloudFlare, AWS CloudFront, Akamai                                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // WEBSOCKET
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: WebSocket vs HTTP?                                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ HTTP: Request-Response, Client → Server, Stateless                          │
   * │ WebSocket: Persistent full-duplex, Bi-directional, Stateful                 │
   * │                                                                             │
   * │ ALTERNATIVES: Polling, Long Polling, Server-Sent Events (SSE)               │
   * │ USE CASES: Chat, gaming, live feeds, collaborative editing                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
