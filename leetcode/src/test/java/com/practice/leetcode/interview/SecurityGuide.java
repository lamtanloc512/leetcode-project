package com.practice.leetcode.interview;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║              SECURITY INTERVIEW GUIDE                                     ║
 * ╠═══════════════════════════════════════════════════════════════════════════╣
 * ║ OWASP Top 10, Security Best Practices, Common Vulnerabilities            ║
 * ║ Dành cho Senior Backend Developer Interview                               ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class SecurityGuide {

  // ═══════════════════════════════════════════════════════════════════════════
  // OWASP TOP 10 (2021) - CÁC LỖ HỔNG PHỔ BIẾN NHẤT
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ OWASP TOP 10 (2021) OVERVIEW                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. A01:2021 - Broken Access Control (Kiểm soát truy cập bị lỗi)            │
   * │ 2. A02:2021 - Cryptographic Failures (Lỗi mã hóa)                          │
   * │ 3. A03:2021 - Injection (SQL, NoSQL, OS, LDAP...)                          │
   * │ 4. A04:2021 - Insecure Design (Thiết kế không an toàn)                     │
   * │ 5. A05:2021 - Security Misconfiguration (Cấu hình sai)                     │
   * │ 6. A06:2021 - Vulnerable Components (Thư viện có lỗ hổng)                  │
   * │ 7. A07:2021 - Authentication Failures (Lỗi xác thực)                       │
   * │ 8. A08:2021 - Data Integrity Failures (Lỗi toàn vẹn dữ liệu)               │
   * │ 9. A09:2021 - Security Logging Failures (Logging không đủ)                 │
   * │ 10. A10:2021 - Server-Side Request Forgery (SSRF)                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // A01: BROKEN ACCESS CONTROL
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ A01: BROKEN ACCESS CONTROL (Phổ biến #1!)                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ LỖI: User có thể truy cập tài nguyên KHÔNG được phép                        │
   * │                                                                             │
   * │ CÁC LOẠI:                                                                   │
   * │ ────────                                                                    │
   * │ 1. IDOR (Insecure Direct Object Reference):                                 │
   * │    GET /api/orders/123  → User A xem được order của User B                  │
   * │                                                                             │
   * │ 2. Privilege Escalation:                                                    │
   * │    User thường → Admin bằng cách sửa role trong request                     │
   * │                                                                             │
   * │ 3. Missing Function Level Access Control:                                   │
   * │    /admin/deleteUser accessible mà không check role                         │
   * │                                                                             │
   * │ 4. Metadata Manipulation:                                                   │
   * │    Sửa JWT claims, hidden fields, cookies                                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ ❌ VÍ DỤ LỖI:                                                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ @GetMapping("/orders/{orderId}")                                            │
   * │ public Order getOrder(@PathVariable Long orderId) {                         │
   * │     return orderRepository.findById(orderId);  // KHÔNG CHECK OWNER!        │
   * │ }                                                                           │
   * │                                                                             │
   * │ // User A có thể xem order của User B bằng cách đổi orderId                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ ✓ CÁCH SỬA:                                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ @GetMapping("/orders/{orderId}")                                            │
   * │ public Order getOrder(@PathVariable Long orderId,                           │
   * │                       @AuthenticationPrincipal User currentUser) {          │
   * │     Order order = orderRepository.findById(orderId)                         │
   * │         .orElseThrow(() -> new NotFoundException());                        │
   * │                                                                             │
   * │     // CHECK OWNER!                                                         │
   * │     if (!order.getUserId().equals(currentUser.getId())) {                   │
   * │         throw new ForbiddenException("Not your order!");                    │
   * │     }                                                                       │
   * │     return order;                                                           │
   * │ }                                                                           │
   * │                                                                             │
   * │ // HOẶC dùng Spring Security:                                               │
   * │ @PreAuthorize("@orderSecurity.isOwner(#orderId, authentication)")           │
   * │ public Order getOrder(@PathVariable Long orderId) { ... }                   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ BEST PRACTICES:                                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. Default DENY: Mặc định từ chối, chỉ cho phép khi cần                     │
   * │ 2. Server-side authorization: KHÔNG tin client                              │
   * │ 3. Validate ownership: Luôn check user có quyền với resource không          │
   * │ 4. Use UUIDs: Thay vì sequential IDs (khó đoán hơn)                         │
   * │ 5. Audit logging: Log tất cả access để detect anomalies                     │
   * │ 6. Rate limiting: Giới hạn số request                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // A02: CRYPTOGRAPHIC FAILURES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ A02: CRYPTOGRAPHIC FAILURES                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ LỖI: Bảo vệ dữ liệu nhạy cảm không đúng cách                                │
   * │                                                                             │
   * │ COMMON MISTAKES:                                                            │
   * │ ────────────────                                                            │
   * │ 1. Storing passwords in plaintext                                           │
   * │ 2. Using weak algorithms (MD5, SHA1, DES)                                   │
   * │ 3. Hardcoded encryption keys                                                │
   * │ 4. Missing HTTPS                                                            │
   * │ 5. Weak random number generation                                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ PASSWORD HASHING - ĐÚNG CÁCH                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ❌ SAIS:                                                                     │
   * │ String hash = DigestUtils.md5Hex(password);  // MD5 quá yếu!                │
   * │ String hash = DigestUtils.sha256Hex(password);  // Không có salt!           │
   * │                                                                             │
   * │ ✓ ĐÚNG: Dùng BCrypt, Argon2, PBKDF2                                         │
   * │                                                                             │
   * │ // BCrypt (recommended):                                                    │
   * │ BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);              │
   * │ String hashed = encoder.encode(rawPassword);                                │
   * │ boolean matches = encoder.matches(rawPassword, hashed);                     │
   * │                                                                             │
   * │ // Argon2 (more secure, Java 9+):                                           │
   * │ Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(16, 32, 1, 65536);│
   * │                                                                             │
   * │ TẠI SAO BCrypt/Argon2?                                                      │
   * │ • Auto-generated salt                                                       │
   * │ • Adaptive: có thể tăng cost factor theo thời gian                          │
   * │ • Slow by design: chống brute-force                                         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ ENCRYPTION BEST PRACTICES                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ SYMMETRIC (AES):                                                            │
   * │ • AES-256-GCM (authenticated encryption)                                    │
   * │ • Never reuse IV/nonce                                                      │
   * │ • Store keys in secrets manager (AWS KMS, HashiCorp Vault)                  │
   * │                                                                             │
   * │ ASYMMETRIC (RSA):                                                           │
   * │ • RSA-2048 minimum, RSA-4096 recommended                                    │
   * │ • Use OAEP padding                                                          │
   * │                                                                             │
   * │ HASHING (non-password):                                                     │
   * │ • SHA-256 hoặc SHA-3                                                        │
   * │ • Dùng cho integrity check, signatures                                      │
   * │                                                                             │
   * │ ⚠️ DEPRECATED:                                                              │
   * │ • MD5, SHA-1: collision vulnerabilities                                     │
   * │ • DES, 3DES: key size quá nhỏ                                               │
   * │ • RC4: biased output                                                        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // A03: INJECTION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ A03: INJECTION (SQL, NoSQL, Command, LDAP...)                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ LỖI: User input được thực thi như code/query                                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SQL INJECTION                                                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ❌ VULNERABLE:                                                               │
   * │ String query = "SELECT * FROM users WHERE email = '" + email + "'";         │
   * │                                                                             │
   * │ // Attack: email = "' OR '1'='1' --"                                        │
   * │ // Query becomes: SELECT * FROM users WHERE email = '' OR '1'='1' --'       │
   * │ // → Returns ALL users!                                                     │
   * │                                                                             │
   * │ ✓ FIX 1: Parameterized Queries (PreparedStatement)                          │
   * │ PreparedStatement ps = conn.prepareStatement(                               │
   * │     "SELECT * FROM users WHERE email = ?"                                   │
   * │ );                                                                          │
   * │ ps.setString(1, email);                                                     │
   * │                                                                             │
   * │ ✓ FIX 2: JPA/Hibernate (nếu dùng đúng cách)                                 │
   * │ @Query("SELECT u FROM User u WHERE u.email = :email")                       │
   * │ User findByEmail(@Param("email") String email);                             │
   * │                                                                             │
   * │ ❌ VẪN VULNERABLE với native query + concat:                                 │
   * │ @Query(value = "SELECT * FROM users WHERE email = '" + email + "'",         │
   * │        nativeQuery = true)  // ĐỪNG LÀM THẾ NÀY!                            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ COMMAND INJECTION                                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ❌ VULNERABLE:                                                               │
   * │ Runtime.getRuntime().exec("ping " + userInput);                             │
   * │                                                                             │
   * │ // Attack: userInput = "8.8.8.8; rm -rf /"                                  │
   * │                                                                             │
   * │ ✓ FIX:                                                                      │
   * │ 1. Avoid executing shell commands with user input                           │
   * │ 2. Whitelist allowed values                                                 │
   * │ 3. Use ProcessBuilder with separate arguments                               │
   * │                                                                             │
   * │ ProcessBuilder pb = new ProcessBuilder("ping", "-c", "4", validatedHost);   │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ NOSQL INJECTION (MongoDB)                                                   │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ❌ VULNERABLE:                                                               │
   * │ // Login: username=admin, password={"$ne": null}                            │
   * │ db.users.find({username: "admin", password: {"$ne": null}})                 │
   * │ // → Bypass password check!                                                 │
   * │                                                                             │
   * │ ✓ FIX: Validate input types, use ODM properly                               │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // A04: INSECURE DESIGN
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ A04: INSECURE DESIGN                                                        │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ Không phải bug code, mà là THIẾT KẾ SAI từ đầu!                             │
   * │                                                                             │
   * │ VÍ DỤ:                                                                      │
   * │ ──────                                                                      │
   * │ 1. Password reset via email without expiration                              │
   * │    → Token có thể dùng mãi mãi                                              │
   * │                                                                             │
   * │ 2. "Secret questions" for recovery                                          │
   * │    → Dễ đoán hoặc tìm trên social media                                     │
   * │                                                                             │
   * │ 3. Unlimited OTP attempts                                                   │
   * │    → Brute-force 6 digits = 1 triệu attempts                                │
   * │                                                                             │
   * │ 4. Business logic flaws:                                                    │
   * │    → "Buy 1 get 1 free" không giới hạn số lần                               │
   * │    → Negative quantity trong order → tiền âm                                │
   * │                                                                             │
   * │ PREVENTION:                                                                 │
   * │ ───────────                                                                 │
   * │ • Threat modeling trước khi code                                            │
   * │ • Security requirements trong user stories                                  │
   * │ • Design reviews với security team                                          │
   * │ • "Abuse case" testing                                                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // A05: SECURITY MISCONFIGURATION
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ A05: SECURITY MISCONFIGURATION                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ COMMON ISSUES:                                                              │
   * │ ──────────────                                                              │
   * │ 1. Default credentials: admin/admin, root/root                              │
   * │ 2. Unnecessary features enabled: debug endpoints, TRACE method              │
   * │ 3. Detailed error messages in production                                    │
   * │ 4. Missing security headers                                                 │
   * │ 5. Directory listing enabled                                                │
   * │ 6. Cloud storage public by default (S3 buckets)                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SPRING SECURITY HEADERS                                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ @Configuration                                                              │
   * │ public class SecurityConfig extends WebSecurityConfigurerAdapter {          │
   * │     @Override                                                               │
   * │     protected void configure(HttpSecurity http) throws Exception {          │
   * │         http.headers()                                                      │
   * │             // Chống clickjacking                                           │
   * │             .frameOptions().deny()                                          │
   * │             // Chống XSS                                                    │
   * │             .xssProtection().block(true)                                    │
   * │             // Chống MIME sniffing                                          │
   * │             .contentTypeOptions()                                           │
   * │             // HSTS (force HTTPS)                                           │
   * │             .httpStrictTransportSecurity()                                  │
   * │                 .maxAgeInSeconds(31536000)                                  │
   * │                 .includeSubDomains(true)                                    │
   * │             // CSP                                                          │
   * │             .contentSecurityPolicy("script-src 'self'");                    │
   * │     }                                                                       │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ ERROR HANDLING                                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ❌ PRODUCTION LEAK:                                                          │
   * │ {                                                                           │
   * │   "error": "NullPointerException at UserService.java:42",                   │
   * │   "stackTrace": "at com.app.UserService.getUser(UserService.java:42)..."    │
   * │ }                                                                           │
   * │                                                                             │
   * │ ✓ SAFE:                                                                     │
   * │ {                                                                           │
   * │   "error": "An error occurred",                                             │
   * │   "errorId": "ERR-12345"  // Log chi tiết server-side với ID này            │
   * │ }                                                                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // A07: AUTHENTICATION FAILURES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ A07: IDENTIFICATION AND AUTHENTICATION FAILURES                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ COMMON ISSUES:                                                              │
   * │ 1. Weak passwords allowed                                                   │
   * │ 2. Credential stuffing / brute force not prevented                          │
   * │ 3. Missing MFA for sensitive operations                                     │
   * │ 4. Session IDs in URL                                                       │
   * │ 5. Sessions not invalidated on logout                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ BRUTE FORCE PROTECTION                                                      │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // 1. Rate Limiting (requests per IP)                                       │
   * │ @RateLimiter(name = "login", fallbackMethod = "rateLimitExceeded")          │
   * │ public ResponseEntity login(LoginRequest request) { ... }                   │
   * │                                                                             │
   * │ // 2. Account Lockout                                                       │
   * │ if (failedAttempts >= 5) {                                                  │
   * │     lockAccountFor(30, MINUTES);                                            │
   * │     sendAlertEmail();                                                       │
   * │ }                                                                           │
   * │                                                                             │
   * │ // 3. Progressive Delays                                                    │
   * │ Thread.sleep(Math.min(failedAttempts * 1000, 30000));                       │
   * │                                                                             │
   * │ // 4. CAPTCHA after N failures                                              │
   * │ if (failedAttempts >= 3) { requireCaptcha = true; }                         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SESSION MANAGEMENT BEST PRACTICES                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. Regenerate session ID after login:                                       │
   * │    session.invalidate();                                                    │
   * │    request.getSession(true);                                                │
   * │                                                                             │
   * │ 2. Secure cookie flags:                                                     │
   * │    Cookie cookie = new Cookie("JSESSIONID", sessionId);                     │
   * │    cookie.setHttpOnly(true);   // Chống XSS đọc cookie                      │
   * │    cookie.setSecure(true);     // Chỉ gửi qua HTTPS                         │
   * │    cookie.setSameSite("Strict"); // Chống CSRF                              │
   * │                                                                             │
   * │ 3. Session timeout:                                                         │
   * │    server.servlet.session.timeout=30m  // application.properties            │
   * │                                                                             │
   * │ 4. Invalidate on logout:                                                    │
   * │    SecurityContextHolder.clearContext();                                    │
   * │    session.invalidate();                                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // JWT SECURITY
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ JWT SECURITY BEST PRACTICES                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ JWT = Header.Payload.Signature                                              │
   * │                                                                             │
   * │ COMMON VULNERABILITIES:                                                     │
   * │ ───────────────────────                                                     │
   * │                                                                             │
   * │ 1. Algorithm None Attack:                                                   │
   * │    ❌ Header: {"alg": "none"}  → Signature bị bỏ qua!                        │
   * │    ✓ FIX: Whitelist allowed algorithms                                      │
   * │                                                                             │
   * │ 2. Algorithm Confusion (RS256 → HS256):                                     │
   * │    ❌ Server dùng public key như secret cho HS256                            │
   * │    ✓ FIX: Explicitly specify algorithm khi verify                           │
   * │                                                                             │
   * │ 3. Weak Secret:                                                             │
   * │    ❌ secret = "password123"                                                 │
   * │    ✓ FIX: 256+ bit random key, hoặc dùng RS256                              │
   * │                                                                             │
   * │ 4. Sensitive Data in Payload:                                               │
   * │    ❌ Payload chứa password, SSN                                             │
   * │    ✓ JWT được encode, KHÔNG encrypted!                                      │
   * │                                                                             │
   * │ 5. No Expiration:                                                           │
   * │    ❌ Token sống mãi mãi                                                     │
   * │    ✓ exp claim + refresh token pattern                                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ JWT IMPLEMENTATION                                                          │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ // GENERATE:                                                                │
   * │ String token = Jwts.builder()                                               │
   * │     .setSubject(userId)                                                     │
   * │     .setIssuedAt(new Date())                                                │
   * │     .setExpiration(new Date(System.currentTimeMillis() + 15*60*1000))       │
   * │     .signWith(SignatureAlgorithm.HS256, secretKey)                          │
   * │     .compact();                                                             │
   * │                                                                             │
   * │ // VERIFY (với explicit algorithm):                                         │
   * │ Claims claims = Jwts.parser()                                               │
   * │     .setSigningKey(secretKey)                                               │
   * │     .require("alg", "HS256")  // Force algorithm!                           │
   * │     .parseClaimsJws(token)                                                  │
   * │     .getBody();                                                             │
   * │                                                                             │
   * │ // REFRESH TOKEN PATTERN:                                                   │
   * │ Access Token:  15 phút, stateless                                           │
   * │ Refresh Token: 7 ngày, stored in DB (có thể revoke)                         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // XSS (CROSS-SITE SCRIPTING)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ XSS - CROSS-SITE SCRIPTING                                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ LỖI: Attacker inject script vào page, chạy trên browser của nạn nhân        │
   * │                                                                             │
   * │ 3 LOẠI:                                                                     │
   * │ ───────                                                                     │
   * │ 1. STORED XSS:                                                              │
   * │    Comment: <script>stealCookies()</script>                                 │
   * │    → Saved DB → Rendered cho tất cả users                                   │
   * │                                                                             │
   * │ 2. REFLECTED XSS:                                                           │
   * │    URL: /search?q=<script>stealCookies()</script>                           │
   * │    → Server reflect input vào response                                      │
   * │                                                                             │
   * │ 3. DOM-BASED XSS:                                                           │
   * │    Client-side JS đọc URL và render không an toàn                           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ XSS PREVENTION                                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. OUTPUT ENCODING (escape HTML entities):                                  │
   * │    <script> → &lt;script&gt;                                                │
   * │    " → &quot;                                                               │
   * │                                                                             │
   * │    // Java:                                                                 │
   * │    StringEscapeUtils.escapeHtml4(userInput);                                │
   * │                                                                             │
   * │    // Thymeleaf (auto-escape by default):                                   │
   * │    <p th:text="${userInput}"></p>  <!-- Escaped -->                         │
   * │    <p th:utext="${userInput}"></p> <!-- DANGEROUS! -->                      │
   * │                                                                             │
   * │ 2. CONTENT SECURITY POLICY (CSP):                                           │
   * │    Content-Security-Policy: script-src 'self'; object-src 'none';           │
   * │    → Chỉ cho phép script từ same origin                                     │
   * │                                                                             │
   * │ 3. HTTP-ONLY COOKIES:                                                       │
   * │    cookie.setHttpOnly(true);                                                │
   * │    → JavaScript không thể đọc cookie                                        │
   * │                                                                             │
   * │ 4. INPUT VALIDATION:                                                        │
   * │    Whitelist allowed characters                                             │
   * │    Reject known XSS patterns                                                │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CSRF (CROSS-SITE REQUEST FORGERY)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ CSRF - CROSS-SITE REQUEST FORGERY                                           │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ LỖI: Attacker trick user thực hiện action không mong muốn                   │
   * │                                                                             │
   * │ ATTACK SCENARIO:                                                            │
   * │ ─────────────────                                                           │
   * │ 1. User đăng nhập bank.com                                                  │
   * │ 2. User visit attacker.com (tab khác)                                       │
   * │ 3. attacker.com có:                                                         │
   * │    <img src="https://bank.com/transfer?to=attacker&amount=10000">           │
   * │ 4. Browser tự động gửi cookie của bank.com → Transfer thành công!           │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ CSRF PREVENTION                                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. CSRF TOKEN (Synchronizer Token Pattern):                                 │
   * │    - Server generate random token, gửi trong form                           │
   * │    - Server verify token với mỗi POST request                               │
   * │                                                                             │
   * │    <form method="POST">                                                     │
   * │        <input type="hidden" name="_csrf" value="${csrfToken}">              │
   * │    </form>                                                                  │
   * │                                                                             │
   * │ 2. SAMESITE COOKIE:                                                         │
   * │    Set-Cookie: sessionId=xxx; SameSite=Strict                               │
   * │    → Cookie chỉ gửi nếu request từ same site                                │
   * │                                                                             │
   * │ 3. VERIFY ORIGIN HEADER:                                                    │
   * │    Check Origin và Referer header                                           │
   * │                                                                             │
   * │ SPRING SECURITY:                                                            │
   * │ http.csrf()                                                                 │
   * │     .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());    │
   * │                                                                             │
   * │ ⚠️ API-only (stateless): có thể disable CSRF nếu dùng JWT                   │
   * │ http.csrf().disable();  // Chỉ khi KHÔNG dùng cookie-based auth!            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SSRF (SERVER-SIDE REQUEST FORGERY) - A10
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ A10: SSRF - SERVER-SIDE REQUEST FORGERY                                     │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ LỖI: Attacker khiến server gửi request đến URL tùy ý                        │
   * │                                                                             │
   * │ ATTACK SCENARIO:                                                            │
   * │ ─────────────────                                                           │
   * │ // API fetch URL và return content                                          │
   * │ POST /api/fetch-url                                                         │
   * │ {"url": "http://169.254.169.254/latest/meta-data/"}                         │
   * │                                                                             │
   * │ → Server fetch AWS metadata → Leak IAM credentials!                         │
   * │                                                                             │
   * │ TARGETS:                                                                    │
   * │ • Cloud metadata: 169.254.169.254 (AWS, GCP, Azure)                         │
   * │ • Internal services: http://localhost:8080/admin                            │
   * │ • Internal network: http://192.168.1.x/                                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SSRF PREVENTION                                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. WHITELIST ALLOWED DOMAINS:                                               │
   * │    Set<String> allowedHosts = Set.of("api.example.com", "cdn.example.com"); │
   * │    if (!allowedHosts.contains(url.getHost())) throw new ForbiddenException(); │
   * │                                                                             │
   * │ 2. BLOCK PRIVATE IPs:                                                       │
   * │    if (isPrivateIP(url.getHost())) throw new ForbiddenException();          │
   * │    // 10.x.x.x, 172.16-31.x.x, 192.168.x.x, 127.x.x.x, 169.254.x.x          │
   * │                                                                             │
   * │ 3. DISABLE REDIRECTS:                                                       │
   * │    HttpURLConnection conn = (HttpURLConnection) url.openConnection();       │
   * │    conn.setInstanceFollowRedirects(false);                                  │
   * │                                                                             │
   * │ 4. NETWORK SEGMENTATION:                                                    │
   * │    Application server không thể access internal services                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SECURITY INTERVIEW QUESTIONS (Hard)
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q1: Làm sao bảo vệ API khỏi brute force attack?                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ A:                                                                          │
   * │ 1. Rate limiting (per IP, per user)                                         │
   * │ 2. Account lockout sau N failed attempts                                    │
   * │ 3. Progressive delays                                                       │
   * │ 4. CAPTCHA sau vài lần fail                                                 │
   * │ 5. Monitoring và alerting cho anomalies                                     │
   * │ 6. Fail2ban hoặc WAF rules                                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q2: JWT vs Session-based auth - pros/cons?                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ JWT:                                                                        │
   * │ ✓ Stateless → scalable, không cần session store                             │
   * │ ✓ Mobile-friendly                                                           │
   * │ ✓ Cross-domain dễ                                                           │
   * │ ✗ Không thể revoke ngay lập tức (trừ khi blacklist)                         │
   * │ ✗ Token size lớn hơn session ID                                             │
   * │ ✗ Payload có thể bị decode (dù không sửa được)                              │
   * │                                                                             │
   * │ Session:                                                                    │
   * │ ✓ Dễ invalidate (xóa session)                                               │
   * │ ✓ Server control hoàn toàn                                                  │
   * │ ✗ Cần session store (Redis) cho scaling                                     │
   * │ ✗ CSRF vulnerability nếu dùng cookie                                        │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q3: Làm sao handle secrets trong application?                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ĐỪNG làm:                                                                   │
   * │ • Hardcode trong source code                                                │
   * │ • Commit vào Git                                                            │
   * │ • Store trong plaintext config                                              │
   * │                                                                             │
   * │ NÊN làm:                                                                    │
   * │ 1. Environment variables (12-factor app)                                    │
   * │ 2. Secrets Manager (AWS Secrets Manager, HashiCorp Vault)                   │
   * │ 3. Encrypted config (Spring Cloud Config + encryption)                      │
   * │ 4. Kubernetes Secrets (với encryption at rest)                              │
   * │ 5. Rotate secrets định kỳ                                                   │
   * │ 6. Audit access to secrets                                                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q4: Giải thích defense in depth?                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ Multiple layers of security, mỗi layer bảo vệ nếu layer khác bị breach:     │
   * │                                                                             │
   * │ Layer 1: Network (Firewall, WAF, DDoS protection)                           │
   * │ Layer 2: Perimeter (Load balancer, API Gateway, rate limiting)              │
   * │ Layer 3: Server (OS hardening, patches, minimal packages)                   │
   * │ Layer 4: Application (Input validation, output encoding, auth)              │
   * │ Layer 5: Data (Encryption at rest, access control, backups)                 │
   * │ Layer 6: Monitoring (Logging, alerting, intrusion detection)                │
   * │                                                                             │
   * │ → Attacker phải bypass TẤT CẢ layers để thành công                          │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q5: Secure password reset flow?                                             │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ 1. User request reset → Generate random token (256-bit)                     │
   * │ 2. Store HASH của token trong DB (không plain token)                        │
   * │ 3. Token expires sau 30 phút                                                │
   * │ 4. Send link qua email: /reset?token=xxx                                    │
   * │ 5. User click → Verify token hash + expiration                              │
   * │ 6. Allow password change                                                    │
   * │ 7. Invalidate token sau khi dùng (one-time use)                             │
   * │ 8. Invalidate ALL sessions của user                                         │
   * │ 9. Send notification email về password change                               │
   * │                                                                             │
   * │ ⚠️ ĐỪNG:                                                                    │
   * │ • Reveal user existence ("Email not found" vs "Email sent")                 │
   * │ • Use sequential/predictable tokens                                         │
   * │ • Allow unlimited resets (rate limit!)                                      │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // SECURITY CHECKLIST FOR CODE REVIEW
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ SECURITY CODE REVIEW CHECKLIST                                              │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                                                                             │
   * │ ☐ Authentication & Authorization                                            │
   * │   ☐ All endpoints require appropriate auth                                  │
   * │   ☐ Resource ownership validated                                            │
   * │   ☐ Role-based access properly enforced                                     │
   * │                                                                             │
   * │ ☐ Input Validation                                                          │
   * │   ☐ All user inputs validated server-side                                   │
   * │   ☐ Parameterized queries used                                              │
   * │   ☐ File uploads validated (type, size, content)                            │
   * │                                                                             │
   * │ ☐ Output Encoding                                                           │
   * │   ☐ HTML entities escaped                                                   │
   * │   ☐ JSON properly encoded                                                   │
   * │   ☐ No user data in error messages                                          │
   * │                                                                             │
   * │ ☐ Cryptography                                                              │
   * │   ☐ Passwords hashed with bcrypt/argon2                                     │
   * │   ☐ No hardcoded secrets                                                    │
   * │   ☐ Strong algorithms used                                                  │
   * │                                                                             │
   * │ ☐ Session Management                                                        │
   * │   ☐ Session regenerated after login                                         │
   * │   ☐ Secure cookie flags set                                                 │
   * │   ☐ Proper logout implementation                                            │
   * │                                                                             │
   * │ ☐ Logging & Monitoring                                                      │
   * │   ☐ Security events logged                                                  │
   * │   ☐ No sensitive data in logs                                               │
   * │   ☐ Alerts configured                                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
