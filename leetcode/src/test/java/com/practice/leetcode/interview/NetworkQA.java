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

  // ═══════════════════════════════════════════════════════════════════════════
  // HTTP DEEP DIVE - HOW IT WORKS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: HTTP Request/Response structure?                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ REQUEST:                                                                    │
   * │ ┌─────────────────────────────────────────────────────────────────────────┐ │
   * │ │ GET /api/users HTTP/1.1          ← Request Line (Method, Path, Version) │ │
   * │ │ Host: example.com                ← Headers                              │ │
   * │ │ Accept: application/json                                                │ │
   * │ │ Authorization: Bearer xxx                                               │ │
   * │ │ Content-Type: application/json                                          │ │
   * │ │                                  ← Empty line (end of headers)          │ │
   * │ │ {"name": "value"}                ← Body (optional)                      │ │
   * │ └─────────────────────────────────────────────────────────────────────────┘ │
   * │                                                                             │
   * │ RESPONSE:                                                                   │
   * │ ┌─────────────────────────────────────────────────────────────────────────┐ │
   * │ │ HTTP/1.1 200 OK                  ← Status Line (Version, Code, Message) │ │
   * │ │ Content-Type: application/json   ← Headers                              │ │
   * │ │ Content-Length: 123                                                     │ │
   * │ │ Cache-Control: max-age=3600                                             │ │
   * │ │                                  ← Empty line                           │ │
   * │ │ {"id": 1, "name": "User"}        ← Body                                 │ │
   * │ └─────────────────────────────────────────────────────────────────────────┘ │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Khi gõ URL vào browser, chuyện gì xảy ra?                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 1. PARSE URL: Extract protocol, host, port, path                            │
   * │                                                                             │
   * │ 2. DNS LOOKUP: Domain → IP address                                          │
   * │    Browser cache → OS cache → DNS Resolver → Root → TLD → Authoritative     │
   * │                                                                             │
   * │ 3. TCP CONNECTION: 3-way handshake                                          │
   * │    SYN → SYN-ACK → ACK                                                      │
   * │                                                                             │
   * │ 4. TLS HANDSHAKE (if HTTPS): See SSL section below                          │
   * │                                                                             │
   * │ 5. SEND HTTP REQUEST: GET / HTTP/1.1 + Headers                              │
   * │                                                                             │
   * │ 6. SERVER PROCESSING: Server handles request                                │
   * │                                                                             │
   * │ 7. RECEIVE RESPONSE: Status + Headers + Body                                │
   * │                                                                             │
   * │ 8. RENDER PAGE: Parse HTML → Load CSS/JS → Build DOM → Paint                │
   * │                                                                             │
   * │ 9. CONNECTION: Keep-Alive (reuse) hoặc Close                                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: HTTP Keep-Alive là gì?                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ HTTP/1.0: Mỗi request = 1 TCP connection (đóng sau response)                │
   * │ HTTP/1.1: Connection: Keep-Alive (default) - reuse connection               │
   * │                                                                             │
   * │ WITHOUT Keep-Alive:              WITH Keep-Alive:                           │
   * │ ┌────────┐    ┌────────┐         ┌────────┐    ┌────────┐                   │
   * │ │ Client │    │ Server │         │ Client │    │ Server │                   │
   * │ └───┬────┘    └───┬────┘         └───┬────┘    └───┬────┘                   │
   * │     │ TCP Open    │                  │ TCP Open    │                        │
   * │     │────────────►│                  │────────────►│                        │
   * │     │ Request 1   │                  │ Request 1   │                        │
   * │     │────────────►│                  │────────────►│                        │
   * │     │ Response 1  │                  │ Response 1  │                        │
   * │     │◄────────────│                  │◄────────────│                        │
   * │     │ TCP Close   │                  │ Request 2   │ ← Same connection      │
   * │     │────────────►│                  │────────────►│                        │
   * │     │ TCP Open    │ ← New!           │ Response 2  │                        │
   * │     │────────────►│                  │◄────────────│                        │
   * │     │ Request 2   │                  │ TCP Close   │ (after timeout)        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SSL/TLS - HTTPS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: HTTP vs HTTPS?                                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                     │ HTTP              │ HTTPS                             │
   * │ Port                │ 80                │ 443                               │
   * │ Encryption          │ ❌ Plain text     │ ✓ TLS encrypted                   │
   * │ Certificate         │ ❌                │ ✓ Required                        │
   * │ Data integrity      │ ❌ Can be altered │ ✓ Tampering detected              │
   * │ Authentication      │ ❌                │ ✓ Server verified                 │
   * │ Speed               │ Faster            │ Slightly slower (handshake)       │
   * │                                                                             │
   * │ HTTPS = HTTP + TLS (Transport Layer Security)                               │
   * │ TLS = Successor of SSL (SSL deprecated, but name still used)                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: TLS Handshake diễn ra như thế nào? (TLS 1.2)                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │  Client                                              Server                 │
   * │    │                                                    │                   │
   * │    │ 1. ClientHello                                     │                   │
   * │    │    - TLS version (1.2, 1.3)                        │                   │
   * │    │    - Client random (32 bytes)                      │                   │
   * │    │    - Cipher suites supported                       │                   │
   * │    │    - Compression methods                           │                   │
   * │    │──────────────────────────────────────────────────►│                   │
   * │    │                                                    │                   │
   * │    │ 2. ServerHello                                     │                   │
   * │    │    - Chosen TLS version                            │                   │
   * │    │    - Server random (32 bytes)                      │                   │
   * │    │    - Chosen cipher suite                           │                   │
   * │    │◄──────────────────────────────────────────────────│                   │
   * │    │                                                    │                   │
   * │    │ 3. Certificate                                     │                   │
   * │    │    - Server's SSL certificate (public key)         │                   │
   * │    │◄──────────────────────────────────────────────────│                   │
   * │    │                                                    │                   │
   * │    │ 4. ServerHelloDone                                 │                   │
   * │    │◄──────────────────────────────────────────────────│                   │
   * │    │                                                    │                   │
   * │    │ 5. ClientKeyExchange                               │                   │
   * │    │    - Pre-master secret (encrypted with pub key)    │                   │
   * │    │──────────────────────────────────────────────────►│                   │
   * │    │                                                    │                   │
   * │    │ 6. ChangeCipherSpec + Finished                     │                   │
   * │    │    (Both sides compute master secret + session keys)                   │
   * │    │◄────────────────────────────────────────────────►│                   │
   * │    │                                                    │                   │
   * │    │ 7. Encrypted Application Data                      │                   │
   * │    │◄────────────────────────────────────────────────►│                   │
   * │                                                                             │
   * │ KEYS GENERATED:                                                             │
   * │ - Pre-master secret → Client random + Server random → Master secret         │
   * │ - Master secret → Session keys (symmetric encryption)                       │
   * │                                                                             │
   * │ TLS 1.3: Chỉ 1 round-trip (faster handshake)                                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: SSL Certificate là gì? Certificate chain?                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ CERTIFICATE CONTAINS:                                                       │
   * │ - Subject: Domain name (CN), Organization                                   │
   * │ - Issuer: CA that issued the cert                                           │
   * │ - Public key                                                                │
   * │ - Validity period (Not Before, Not After)                                   │
   * │ - Signature (by CA's private key)                                           │
   * │                                                                             │
   * │ CERTIFICATE CHAIN:                                                          │
   * │ ┌─────────────────────────────────────────────────────────────────────────┐ │
   * │ │ Root CA          (Pre-installed in browsers/OS, self-signed)            │ │
   * │ │    ↓ signs                                                              │ │
   * │ │ Intermediate CA  (Signed by Root, issues end-entity certs)              │ │
   * │ │    ↓ signs                                                              │ │
   * │ │ Server Cert      (Your domain's certificate)                            │ │
   * │ └─────────────────────────────────────────────────────────────────────────┘ │
   * │                                                                             │
   * │ VERIFICATION: Browser traces chain up to trusted Root CA                    │
   * │ If any step fails → Certificate warning!                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Symmetric vs Asymmetric encryption trong TLS?                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ ASYMMETRIC (RSA, ECDSA):                                                    │
   * │ - Public key + Private key                                                  │
   * │ - Slow, used for key exchange & certificate verification                   │
   * │ - Client encrypts pre-master secret with server's public key               │
   * │                                                                             │
   * │ SYMMETRIC (AES, ChaCha20):                                                  │
   * │ - Same key for encrypt/decrypt                                              │
   * │ - Fast, used for actual data encryption                                     │
   * │ - Session keys derived from master secret                                   │
   * │                                                                             │
   * │ TLS uses BOTH:                                                              │
   * │ 1. Asymmetric for handshake (exchange keys securely)                        │
   * │ 2. Symmetric for data (fast encryption)                                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: TLS versions và security?                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ SSL 2.0, 3.0   │ ❌ Deprecated, insecure                                    │
   * │ TLS 1.0, 1.1   │ ❌ Deprecated (2020), vulnerabilities                      │
   * │ TLS 1.2        │ ✓ Widely used, secure (with right cipher suites)          │
   * │ TLS 1.3        │ ✓ Latest (2018), faster handshake, more secure            │
   * │                                                                             │
   * │ TLS 1.3 IMPROVEMENTS:                                                       │
   * │ - 1-RTT handshake (vs 2-RTT in 1.2)                                         │
   * │ - 0-RTT resumption (for returning clients)                                  │
   * │ - Removed weak ciphers (RC4, 3DES, MD5, SHA1)                               │
   * │ - Forward secrecy mandatory                                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Perfect Forward Secrecy (PFS)?                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ PROBLEM: Nếu server private key bị lộ → decrypt tất cả past sessions?       │
   * │                                                                             │
   * │ SOLUTION: Diffie-Hellman key exchange                                       │
   * │ - Tạo temporary keys cho mỗi session                                        │
   * │ - Nếu private key lộ → chỉ ảnh hưởng future sessions                        │
   * │ - Past sessions vẫn an toàn (keys đã bị xóa)                                │
   * │                                                                             │
   * │ Cipher suite with PFS: ECDHE-RSA-AES256-GCM-SHA384                          │
   * │ - ECDHE: Elliptic Curve Diffie-Hellman Ephemeral                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Common SSL/TLS attacks?                                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ 1. MAN-IN-THE-MIDDLE (MITM)                                                 │
   * │    - Attacker intercepts connection                                         │
   * │    - Prevention: Verify certificate chain                                   │
   * │                                                                             │
   * │ 2. SSL STRIPPING                                                            │
   * │    - Downgrade HTTPS → HTTP                                                 │
   * │    - Prevention: HSTS (HTTP Strict Transport Security)                      │
   * │                                                                             │
   * │ 3. BEAST, POODLE, Heartbleed                                                │
   * │    - Vulnerabilities in old TLS/SSL versions                                │
   * │    - Prevention: Use TLS 1.2+ with modern ciphers                           │
   * │                                                                             │
   * │ 4. CERTIFICATE SPOOFING                                                     │
   * │    - Fake certificate from compromised CA                                   │
   * │    - Prevention: Certificate Transparency, CAA records                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
