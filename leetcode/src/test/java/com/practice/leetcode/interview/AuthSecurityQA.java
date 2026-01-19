package com.practice.leetcode.interview;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                   AUTH & SECURITY INTERVIEW Q&A                           ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class AuthSecurityQA {

  // ═══════════════════════════════════════════════════════════════════════════
  // AUTHENTICATION vs AUTHORIZATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Authentication vs Authorization?                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ AUTHENTICATION (AuthN): "Bạn là ai?"                                        │
   * │ - Verify identity                                                           │
   * │ - Username/password, OAuth, biometrics                                      │
   * │ - Response: 401 Unauthorized                                                │
   * │                                                                             │
   * │ AUTHORIZATION (AuthZ): "Bạn được phép làm gì?"                              │
   * │ - Check permissions                                                         │
   * │ - Roles, permissions, ACL                                                   │
   * │ - Response: 403 Forbidden                                                   │
   * │                                                                             │
   * │ FLOW: Authenticate FIRST → Then Authorize                                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SESSION vs TOKEN
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Session-based vs Token-based Authentication?                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ SESSION-BASED:                                                              │
   * │ ┌─────────────────────────────────────────────────────────────────────────┐ │
   * │ │ 1. User login → Server tạo session, lưu vào memory/DB                   │ │
   * │ │ 2. Server gửi session ID trong Cookie                                   │ │
   * │ │ 3. Mỗi request, client gửi cookie → Server lookup session               │ │
   * │ └─────────────────────────────────────────────────────────────────────────┘ │
   * │ ✓ Server kiểm soát (có thể revoke)                                          │
   * │ ✗ Cần sticky sessions hoặc shared storage                                   │
   * │ ✗ Khó scale horizontally                                                    │
   * │                                                                             │
   * │ TOKEN-BASED (JWT):                                                          │
   * │ ┌─────────────────────────────────────────────────────────────────────────┐ │
   * │ │ 1. User login → Server tạo signed token                                 │ │
   * │ │ 2. Client lưu token (localStorage, cookie)                              │ │
   * │ │ 3. Mỗi request, gửi token trong Authorization header                    │ │
   * │ │ 4. Server verify signature, không cần lookup                            │ │
   * │ └─────────────────────────────────────────────────────────────────────────┘ │
   * │ ✓ Stateless, dễ scale                                                       │
   * │ ✓ Works với microservices                                                   │
   * │ ✗ Không thể revoke trước khi expire                                         │
   * │ ✗ Token size lớn hơn session ID                                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // JWT (JSON WEB TOKEN)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: JWT structure?                                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: 3 phần, separated bằng dấu chấm: xxxxx.yyyyy.zzzzz                       │
   * │                                                                             │
   * │ 1. HEADER (xxxx):                                                           │
   * │    {                                                                        │
   * │      "alg": "HS256",  // Algorithm                                          │
   * │      "typ": "JWT"                                                           │
   * │    }                                                                        │
   * │    → Base64Url encoded                                                      │
   * │                                                                             │
   * │ 2. PAYLOAD (yyyy):                                                          │
   * │    {                                                                        │
   * │      "sub": "1234567890",  // Subject (user ID)                             │
   * │      "name": "John Doe",                                                    │
   * │      "iat": 1516239022,    // Issued at                                     │
   * │      "exp": 1516242622     // Expiration                                    │
   * │    }                                                                        │
   * │    → Base64Url encoded (NOT encrypted!)                                     │
   * │                                                                             │
   * │ 3. SIGNATURE (zzzz):                                                        │
   * │    HMACSHA256(                                                              │
   * │      base64UrlEncode(header) + "." + base64UrlEncode(payload),              │
   * │      secret                                                                 │
   * │    )                                                                        │
   * │                                                                             │
   * │ ⚠️ JWT payload is NOT secret! Anyone can decode. Only VERIFY integrity.     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Access Token vs Refresh Token?                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ ┌───────────────────┬─────────────────────┬─────────────────────────────┐   │
   * │ │                   │ Access Token        │ Refresh Token               │   │
   * │ ├───────────────────┼─────────────────────┼─────────────────────────────┤   │
   * │ │ Purpose           │ Access resources    │ Get new access token        │   │
   * │ │ Lifetime          │ Short (15 min)      │ Long (7 days - 30 days)     │   │
   * │ │ Sent with         │ Every API request   │ Only to refresh endpoint    │   │
   * │ │ Storage           │ Memory/Cookie       │ Cookie (httpOnly)           │   │
   * │ └───────────────────┴─────────────────────┴─────────────────────────────┘   │
   * │                                                                             │
   * │ FLOW:                                                                       │
   * │ 1. Login → Nhận access + refresh token                                      │
   * │ 2. Access API với access token                                              │
   * │ 3. Access token expire → Gửi refresh token để lấy access mới                │
   * │ 4. Refresh token expire → Phải login lại                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // OAuth 2.0
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: OAuth 2.0 là gì? Các roles?                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Authorization framework cho third-party access                           │
   * │                                                                             │
   * │ ROLES:                                                                      │
   * │ - Resource Owner: User (owns the data)                                      │
   * │ - Client: App muốn access data                                              │
   * │ - Authorization Server: Issues tokens (Google, Facebook)                    │
   * │ - Resource Server: Hosts protected resources (API)                          │
   * │                                                                             │
   * │ VÍ DỤ: "Login with Google"                                                  │
   * │ - User = Resource Owner                                                     │
   * │ - Your App = Client                                                         │
   * │ - Google = Authorization Server + Resource Server                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: OAuth 2.0 Grant Types?                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ 1. AUTHORIZATION CODE (Most common, most secure):                           │
   * │    - Web apps với server-side                                               │
   * │    - User redirected → Auth server → Code → Exchange for token              │
   * │    - Best for: Traditional web apps                                         │
   * │                                                                             │
   * │ 2. AUTHORIZATION CODE + PKCE:                                               │
   * │    - Như trên + code_verifier/code_challenge                                │
   * │    - Best for: Mobile apps, SPAs (không có secret)                          │
   * │                                                                             │
   * │ 3. CLIENT CREDENTIALS:                                                      │
   * │    - Machine-to-machine                                                     │
   * │    - Client ID + Secret → Token                                             │
   * │    - Best for: Microservices, background jobs                               │
   * │                                                                             │
   * │ 4. RESOURCE OWNER PASSWORD (Deprecated):                                    │
   * │    - Username + Password trực tiếp                                          │
   * │    - CHỈ dùng khi own cả client và auth server                              │
   * │                                                                             │
   * │ 5. IMPLICIT (Deprecated):                                                   │
   * │    - Token trả về trực tiếp (không có code)                                 │
   * │    - Không an toàn, replaced by Auth Code + PKCE                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CORS & CSRF
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: CORS là gì? Tại sao cần?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Cross-Origin Resource Sharing                                            │
   * │                                                                             │
   * │ SAME-ORIGIN POLICY:                                                         │
   * │ Browser block requests từ different origin (domain/port/protocol)           │
   * │ → Bảo vệ user data                                                          │
   * │                                                                             │
   * │ CORS cho phép exceptions:                                                   │
   * │ - Server respond với Access-Control headers                                 │
   * │ - Browser check headers, cho phép hoặc block                                │
   * │                                                                             │
   * │ KEY HEADERS:                                                                │
   * │ Access-Control-Allow-Origin: https://example.com (hoặc *)                   │
   * │ Access-Control-Allow-Methods: GET, POST, PUT, DELETE                        │
   * │ Access-Control-Allow-Headers: Content-Type, Authorization                   │
   * │ Access-Control-Allow-Credentials: true (cho cookies)                        │
   * │                                                                             │
   * │ PREFLIGHT REQUEST:                                                          │
   * │ - Browser gửi OPTIONS request trước                                         │
   * │ - Check nếu actual request được phép                                        │
   * │ - Chỉ với "non-simple" requests (PUT, DELETE, custom headers)               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: CSRF Attack là gì? Cách phòng chống?                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: Cross-Site Request Forgery                                               │
   * │                                                                             │
   * │ ATTACK SCENARIO:                                                            │
   * │ 1. User đã login bank.com (có session cookie)                               │
   * │ 2. User visit evil.com                                                      │
   * │ 3. evil.com có hidden form POST tới bank.com/transfer                       │
   * │ 4. Browser tự động gửi cookie → Bank xử lý như legitimate request!          │
   * │                                                                             │
   * │ PHÒNG CHỐNG:                                                                │
   * │                                                                             │
   * │ 1. CSRF TOKEN:                                                              │
   * │    - Server generate random token, gửi cho client                           │
   * │    - Client gửi kèm mỗi request (trong form/header)                         │
   * │    - Server verify token                                                    │
   * │                                                                             │
   * │ 2. SAME-SITE COOKIES:                                                       │
   * │    Set-Cookie: session=abc; SameSite=Strict                                 │
   * │    - Strict: Không gửi cookie trong cross-site requests                     │
   * │    - Lax: Chỉ gửi với safe methods (GET)                                    │
   * │                                                                             │
   * │ 3. CHECK ORIGIN/REFERER HEADER                                              │
   * │                                                                             │
   * │ TOKEN-BASED AUTH: Ít vulnerable hơn session (token không tự động gửi)       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // HTTPS & TLS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: HTTPS hoạt động như nào? TLS Handshake?                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A: HTTP + TLS (Transport Layer Security)                                    │
   * │                                                                             │
   * │ TLS HANDSHAKE (simplified):                                                 │
   * │                                                                             │
   * │ 1. CLIENT HELLO:                                                            │
   * │    - Supported TLS versions                                                 │
   * │    - Supported cipher suites                                                │
   * │    - Random number                                                          │
   * │                                                                             │
   * │ 2. SERVER HELLO:                                                            │
   * │    - Chosen TLS version & cipher                                            │
   * │    - Server certificate (public key)                                        │
   * │    - Random number                                                          │
   * │                                                                             │
   * │ 3. CERTIFICATE VERIFICATION:                                                │
   * │    - Client verify certificate với CA                                       │
   * │    - Check domain, expiry, chain of trust                                   │
   * │                                                                             │
   * │ 4. KEY EXCHANGE:                                                            │
   * │    - Client generate pre-master secret                                      │
   * │    - Encrypt với server public key → gửi                                    │
   * │    - Cả 2 derive session key từ pre-master + randoms                        │
   * │                                                                             │
   * │ 5. ENCRYPTED COMMUNICATION:                                                 │
   * │    - Dùng symmetric encryption với session key                              │
   * │    - Nhanh hơn asymmetric cho data transfer                                 │
   * │                                                                             │
   * │ SUMMARY: Asymmetric để trao đổi key → Symmetric để encrypt data             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // PASSWORD HASHING
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Tại sao hash password? Thuật toán nào nên dùng?                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ A:                                                                          │
   * │                                                                             │
   * │ TẠI SAO KHÔNG LƯU PLAINTEXT:                                                │
   * │ - Database breach → All passwords exposed                                   │
   * │ - Admins có thể thấy passwords                                              │
   * │                                                                             │
   * │ TẠI SAO KHÔNG DÙNG MD5/SHA1/SHA256:                                         │
   * │ - Quá nhanh! → Brute force dễ dàng                                          │
   * │ - GPU có thể thử billions hashes/second                                     │
   * │ - Rainbow table attacks                                                     │
   * │                                                                             │
   * │ NÊN DÙNG:                                                                   │
   * │                                                                             │
   * │ ┌───────────────────┬─────────────────────────────────────────────────────┐ │
   * │ │ Algorithm         │ Notes                                               │ │
   * │ ├───────────────────┼─────────────────────────────────────────────────────┤ │
   * │ │ bcrypt            │ Standard, built-in salt, cost factor adjustable     │ │
   * │ │ Argon2            │ Winner of PHC (2015), memory-hard                   │ │
   * │ │ scrypt            │ Memory-hard, used by crypto                         │ │
   * │ │ PBKDF2            │ NIST approved, but less secure than above           │ │
   * │ └───────────────────┴─────────────────────────────────────────────────────┘ │
   * │                                                                             │
   * │ KEY CONCEPTS:                                                               │
   * │ - SALT: Random value, unique per password → chống rainbow table            │
   * │ - COST/WORK FACTOR: Làm chậm algorithm → chống brute force                  │
   * │ - Memory-hard: Cần nhiều RAM → khó dùng GPU                                 │
   * │                                                                             │
   * │ EXAMPLE (bcrypt):                                                           │
   * │ $2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/X4.qNTYOOPTiCQX7e               │
   * │  ↑   ↑                                                                      │
   * │ algo cost(2^12)     salt + hash                                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // COMMON VULNERABILITIES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ OWASP TOP 10 (Quick Reference)                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. BROKEN ACCESS CONTROL                                                    │
   * │    Missing authorization checks                                             │
   * │    → Check permissions on every endpoint                                    │
   * │                                                                             │
   * │ 2. CRYPTOGRAPHIC FAILURES                                                   │
   * │    Weak encryption, exposed data                                            │
   * │    → Use strong algorithms, HTTPS everywhere                                │
   * │                                                                             │
   * │ 3. INJECTION (SQL, NoSQL, Command)                                          │
   * │    Untrusted data in queries                                                │
   * │    → Parameterized queries, ORMs                                            │
   * │                                                                             │
   * │ 4. INSECURE DESIGN                                                          │
   * │    Missing security in architecture                                         │
   * │    → Threat modeling, secure design patterns                                │
   * │                                                                             │
   * │ 5. SECURITY MISCONFIGURATION                                                │
   * │    Default configs, unnecessary features                                    │
   * │    → Harden configs, disable defaults                                       │
   * │                                                                             │
   * │ 6. VULNERABLE COMPONENTS                                                    │
   * │    Outdated libraries with CVEs                                             │
   * │    → Regular updates, dependency scanning                                   │
   * │                                                                             │
   * │ 7. IDENTIFICATION & AUTH FAILURES                                           │
   * │    Weak passwords, session management                                       │
   * │    → MFA, secure session handling                                           │
   * │                                                                             │
   * │ 8. SOFTWARE & DATA INTEGRITY FAILURES                                       │
   * │    Unsigned updates, insecure CI/CD                                         │
   * │    → Verify integrity, secure pipelines                                     │
   * │                                                                             │
   * │ 9. SECURITY LOGGING FAILURES                                                │
   * │    No logs, no monitoring                                                   │
   * │    → Centralized logging, alerting                                          │
   * │                                                                             │
   * │ 10. SERVER-SIDE REQUEST FORGERY (SSRF)                                      │
   * │     Making requests to internal resources                                   │
   * │     → Validate URLs, whitelist destinations                                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
