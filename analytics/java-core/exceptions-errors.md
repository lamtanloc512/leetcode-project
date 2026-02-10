# Exception & Error Hierarchy - Deep Dive

> Understanding Java's throwable taxonomy, performance implications, and best practices

---

## Related Topics

- [[jvm-architecture]] - Stack frames & exception tables
- [[serialization]] - Exception serialization
- [[../spring/transaction-management]] - Rollback on exceptions

---

## 1. The Throwable Hierarchy

```
java.lang.Throwable
│
├── java.lang.Error                    ← JVM/System problems (don't catch)
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   ├── NoClassDefFoundError
│   ├── LinkageError
│   │   ├── NoSuchMethodError
│   │   └── UnsatisfiedLinkError
│   └── VirtualMachineError
│       ├── InternalError
│       └── UnknownError
│
└── java.lang.Exception                ← Program-level problems (can recover)
    │
    ├── RuntimeException               ← Unchecked (don't need throws)
    │   ├── NullPointerException
    │   ├── IllegalArgumentException
    │   ├── IllegalStateException
    │   ├── IndexOutOfBoundsException
    │   ├── ClassCastException
    │   ├── ArithmeticException
    │   ├── ConcurrentModificationException
    │   └── UnsupportedOperationException
    │
    └── Checked Exceptions             ← Must handle or declare (throws)
        ├── IOException
        │   ├── FileNotFoundException
        │   └── SocketException
        ├── SQLException
        ├── ClassNotFoundException
        ├── InterruptedException
        └── ReflectiveOperationException
```

---

## 2. Error vs Exception

| Aspect | Error | Exception |
|--------|-------|-----------|
| **Source** | JVM / System | Application logic |
| **Recoverability** | Usually not | Often yes |
| **Catch?** | ❌ Usually don't | ✅ Yes |
| **Examples** | OOM, StackOverflow | IOException, NPE |
| **When thrown** | Serious problems | Logic/data issues |

> [!CAUTION]
> **Never catch `Error`** unless you're implementing a framework that must report errors gracefully before exiting.

```java
// ❌ WRONG: Catching all throwables
try {
    riskyOperation();
} catch (Throwable t) {  // Catches Error too!
    log.error("Failed", t);
}

// ✅ CORRECT: Be specific
try {
    riskyOperation();
} catch (SpecificException e) {
    handleRecovery(e);
}
```

---

## 3. Checked vs Unchecked Philosophy

### 3.1 The Design Debate

| Checked | Unchecked |
|---------|-----------|
| Forces caller to handle | Caller may ignore |
| Part of method signature | Not in signature |
| Compiler enforces | Runtime detection |
| Recoverable errors | Programming errors |

**Java Philosophy**: Checked exceptions for recoverable conditions, unchecked for programming errors.

**Modern Criticism**: Checked exceptions add boilerplate, often wrapped anyway (Spring, frameworks prefer unchecked).

### 3.2 When to Use Which

```java
// ✅ Checked: Caller CAN and SHOULD handle
public User findUser(String id) throws UserNotFoundException {
    // Caller can try another ID or show error message
}

// ✅ Unchecked: Programming error - fix the code
public void setAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative: " + age);
    }
}

// ✅ Unchecked wrapper: Framework pattern
public User findUser(String id) {
    try {
        return jdbcTemplate.queryForObject(...);
    } catch (DataAccessException e) {  // Checked → Unchecked
        throw new UserServiceException("Failed to find user", e);
    }
}
```

---

## 4. Exception Internals

### 4.1 StackTraceElement

Each exception captures the call stack:

```java
Exception e = new Exception();
StackTraceElement[] stack = e.getStackTrace();

for (StackTraceElement frame : stack) {
    System.out.println(frame.getClassName() + "." + 
                       frame.getMethodName() + "(" + 
                       frame.getFileName() + ":" + 
                       frame.getLineNumber() + ")");
}
```

**Performance Note**: Stack trace capture is expensive (~10μs). For frequent lightweight exceptions, consider:

```java
// Pre-created exception for control flow (avoid in new code)
private static final BreakException BREAK = new BreakException();

static class BreakException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;  // Don't capture stack trace
    }
}
```

### 4.2 Exception Table (Bytecode)

```java
try {
    riskyOperation();  // line 10
} catch (IOException e) {
    handleIO(e);       // line 12
} catch (Exception e) {
    handleOther(e);    // line 14
}
```

**Compiled Exception Table:**

```
Exception table:
   from    to  target type
      0    10    12   Class java/io/IOException
      0    10    14   Class java/lang/Exception
```

The JVM uses this table to find the appropriate catch block when an exception is thrown.

---

## 5. Suppressed Exceptions (try-with-resources)

### 5.1 The Problem

```java
// What if close() throws after doWork() throws?
InputStream is = new FileInputStream("file.txt");
try {
    is.read();  // Throws IOException
} finally {
    is.close(); // Also throws IOException - first exception lost!
}
```

### 5.2 Try-with-resources Solution (Java 7+)

```java
try (InputStream is = new FileInputStream("file.txt")) {
    is.read();
}
// If both throw, primary exception is thrown with close() exception "suppressed"
```

**Accessing Suppressed Exceptions:**

```java
try {
    // ...
} catch (IOException e) {
    log.error("Primary: " + e.getMessage());
    for (Throwable suppressed : e.getSuppressed()) {
        log.error("Suppressed: " + suppressed.getMessage());
    }
}
```

### 5.3 Multiple Resources

```java
// Closed in REVERSE order of declaration
try (Connection conn = getConnection();
     PreparedStatement ps = conn.prepareStatement(sql);
     ResultSet rs = ps.executeQuery()) {
    // Closed: rs → ps → conn
}
```

---

## 6. Exception Chaining

### 6.1 Wrapping Exceptions

```java
public void processFile(String path) {
    try {
        readFile(path);
    } catch (IOException e) {
        // Wrap lower-level exception
        throw new ProcessingException("Failed to process: " + path, e);
    }
}

// Later: access the cause chain
public void diagnose(Exception e) {
    Throwable current = e;
    while (current != null) {
        System.out.println(current.getClass().getName() + ": " + current.getMessage());
        current = current.getCause();
    }
}
```

### 6.2 initCause() vs Constructor

```java
// Option 1: Constructor (preferred)
throw new MyException("message", cause);

// Option 2: initCause (for legacy exceptions without cause constructor)
SomeOldException e = new SomeOldException("message");
e.initCause(originalException);
throw e;
```

---

## 7. Custom Exceptions

### 7.1 Recommended Pattern

```java
public class UserNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String userId;
    
    public UserNotFoundException(String userId) {
        super("User not found: " + userId);
        this.userId = userId;
    }
    
    public UserNotFoundException(String userId, Throwable cause) {
        super("User not found: " + userId, cause);
        this.userId = userId;
    }
    
    public String getUserId() {
        return userId;
    }
}
```

### 7.2 Multiple Exception Types Strategy

```java
// ❌ Avoid: Too many specific exceptions
UserNotFoundException, UserDisabledException, UserExpiredException...

// ✅ Better: Use error codes or enums
public class UserException extends RuntimeException {
    public enum ErrorCode {
        NOT_FOUND, DISABLED, EXPIRED, VALIDATION_FAILED
    }
    
    private final ErrorCode errorCode;
    
    public UserException(ErrorCode code, String message) {
        super(message);
        this.errorCode = code;
    }
}
```

---

## 8. Performance Considerations

### 8.1 Exception Creation Cost

| Operation | Approximate Cost |
|-----------|------------------|
| Create exception | ~5-10 μs |
| Throw & catch (same method) | ~1-5 μs |
| Throw & propagate (10 frames) | ~20-50 μs |
| Stack trace print | ~100+ μs |

### 8.2 Avoid Exceptions for Control Flow

```java
// ❌ WRONG: Using exceptions for normal flow
public int parseOrDefault(String s, int defaultValue) {
    try {
        return Integer.parseInt(s);
    } catch (NumberFormatException e) {
        return defaultValue;  // Common case uses exception!
    }
}

// ✅ CORRECT: Check before potentially throwing
public int parseOrDefault(String s, int defaultValue) {
    if (s == null || !s.matches("-?\\d+")) {
        return defaultValue;
    }
    return Integer.parseInt(s);
}

// ✅ OR: Use Optional (Java 8+)
public Optional<Integer> tryParse(String s) {
    try {
        return Optional.of(Integer.parseInt(s));
    } catch (NumberFormatException e) {
        return Optional.empty();
    }
}
```

### 8.3 Pre-computed Exceptions (Carefully!)

```java
// Only for TRULY hot paths where exception frequency is high
private static final NotFoundException CACHE_MISS = new NotFoundException() {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;  // Skip expensive stack walk
    }
};

// BUT: Loss of stack trace makes debugging harder!
```

---

## 9. Logging Best Practices

```java
// ✅ Log FULL exception (message + stack trace)
log.error("Operation failed", exception);

// ❌ WRONG: Only logs message, loses stack trace
log.error("Operation failed: " + exception.getMessage());

// ✅ Add context
log.error("Failed to process order {} for user {}", orderId, userId, exception);

// ✅ Re-throwing after logging (avoid double-logging)
try {
    process();
} catch (Exception e) {
    log.error("Processing failed, will retry", e);
    throw e;  // Don't log again in caller!
}
```

---

## 10. Quick Reference: Common Exceptions

| Exception | Meaning | Typical Cause |
|-----------|---------|---------------|
| `NullPointerException` | Null dereference | Uninitialized variable |
| `IllegalArgumentException` | Bad method input | Validation failure |
| `IllegalStateException` | Object in wrong state | Method called at wrong time |
| `IndexOutOfBoundsException` | Array/list index wrong | Off-by-one error |
| `ConcurrentModificationException` | Collection modified while iterating | Missing synchronization |
| `ClassCastException` | Invalid cast | Wrong type assumption |
| `IOException` | I/O operation failed | File/network issues |
| `SQLException` | Database operation failed | Query/connection issues |
| `InterruptedException` | Thread interrupted | Shutdown signal |
