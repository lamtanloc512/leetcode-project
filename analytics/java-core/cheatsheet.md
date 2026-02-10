# Java Core Interview Cheatsheet

> Quick reference for common traps and gotchas

---

## Related Topics

- [[jvm-architecture]] | [[synchronized-internals]] | [[memory-model]] | [[exceptions-errors]] | [[serialization]]

---

## 🧠 Memory Layout

| What | Where |
|------|-------|
| Local primitives (`int x = 10`) | Stack |
| Local references (`String s`) | Stack (pointer) |
| Objects / Arrays | Heap |
| Static fields | Metaspace |
| String literals | String Pool (Heap) |

```java
int[] arr = new int[3];
// 'arr' (pointer) → Stack
// Array object [0,0,0] → Heap
```

---

## 🔥 Common Interview Traps

### 1. equals() & hashCode()
```java
// ❌ Override equals() but forget hashCode()
// → HashMap/HashSet will be broken!

if (a.equals(b)) {
    assert a.hashCode() == b.hashCode();  // MUST be true
}
```

### 2. String Comparison
```java
String a = "hello";
String b = "hello";
String c = new String("hello");

a == b;      // true  (same pool ref)
a == c;      // false (different objects)
a.equals(c); // true  (content match)
```

### 3. Pass-by-Value
```java
void modify(StringBuilder sb) {
    sb.append("X");              // ✅ Modifies original
    sb = new StringBuilder("Y"); // ❌ Local change only
}
// Java is ALWAYS pass-by-value (copies the reference)
```

### 4. Integer Cache
```java
Integer a = 127, b = 127;
Integer c = 128, d = 128;

a == b;  // true  (-128 to 127 cached)
c == d;  // false (new objects)
```

---

## ⚡ Concurrency Gotchas

| Trap | Problem | Fix |
|------|---------|-----|
| `count++` on shared var | Not atomic | `AtomicInteger` |
| `volatile count++` | Still not atomic! | `AtomicInteger` |
| `synchronized("lock")` | String literals shared | `private final Object LOCK` |
| Double-checked lock | Partial publication | Add `volatile` |
| ThreadLocal in pool | Memory leak | Call `remove()` |

### volatile vs synchronized
```java
volatile int x;  // Visibility only, NOT atomic ops
synchronized     // Visibility + Atomicity + Mutual exclusion
```

---

## 📦 Collections Quick Facts

| Collection | Thread-safe? | Null keys/values |
|------------|-------------|------------------|
| HashMap | ❌ | 1 null key, many null values |
| Hashtable | ✅ | ❌ No nulls |
| ConcurrentHashMap | ✅ | ❌ No nulls |
| TreeMap | ❌ | ❌ No null keys |
| LinkedHashMap | ❌ | ✅ Yes |

### HashMap Internals
```
Capacity × LoadFactor(0.75) = Threshold
When size > threshold → resize (double capacity)
Bucket: LinkedList → Red-Black Tree (when >8 nodes, capacity ≥64)
```

---

## 💀 Exception Hierarchy

```
Throwable
├── Error (don't catch)
│   ├── OutOfMemoryError
│   └── StackOverflowError
└── Exception
    ├── RuntimeException (unchecked)
    │   ├── NullPointerException
    │   ├── IllegalArgumentException
    │   └── IndexOutOfBoundsException
    └── Checked (must handle)
        ├── IOException
        └── SQLException
```

---

## 🔒 synchronized Reentrancy

```java
synchronized void methodA() {
    methodB();  // ✅ Same thread can enter
}
synchronized void methodB() { }
// Lock tracks recursion count internally
```

---

## 📝 Quick Answers

| Question | Answer |
|----------|--------|
| Is Java pass-by-value or reference? | **Always pass-by-value** (copies reference) |
| Can you override static methods? | No, only **hide** (compile-time binding) |
| Interface vs Abstract class? | Interface = contract, Abstract = partial impl |
| synchronized reentrant? | **Yes**, tracks recursion count |
| final, finally, finalize? | keyword / exception block / GC hook (deprecated) |
