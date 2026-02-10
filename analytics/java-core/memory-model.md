# Java Memory Model (JMM) & volatile Deep Dive

> **Authors Referenced**: Doug Lea (JSR-133 Cookbook), Brian Goetz (Java Concurrency in Practice), Oracle JSR-133

---

## Related Topics

- [[jvm-architecture]] - Runtime data areas
- [[synchronized-internals]] - Monitor-based synchronization
- [[../concurrency/volatile-atomics]] - Atomic classes & CAS
- [[../concurrency/locks-conditions]] - Explicit locks

---

## 1. Why We Need the JMM

**The Problem**: Modern CPUs use multiple optimization techniques that can reorder operations:

```
Source Code:                    What CPU Might Execute:
─────────────                   ─────────────────────────
x = 1;                          y = 2;     // Reordered!
y = 2;                          x = 1;
```

Without proper synchronization, one thread may see **stale** or **partially updated** values written by another thread.

```
Thread A                Thread B
────────                ────────
x = 42;                 
ready = true;           if (ready) {
                            print(x);  // Might print 0!
                        }
```

---

## 2. Happens-Before Relationship

The **happens-before** order is the fundamental guarantee of JMM:

> If action A **happens-before** action B, then A's effects are **visible** to B.

### 2.1 Built-in Happens-Before Rules

| Rule | Description |
|------|-------------|
| **Program Order** | Each action in a thread HB every subsequent action in that thread |
| **Monitor Lock** | Unlock HB every subsequent lock of the same monitor |
| **Volatile Write** | Write to volatile field HB every subsequent read of that field |
| **Thread Start** | `Thread.start()` HB first action in the started thread |
| **Thread Join** | Last action in thread HB return from `join()` on that thread |
| **Interruption** | `interrupt()` HB interrupted thread detects it |
| **Finalization** | End of constructor HB start of `finalize()` |
| **Transitivity** | If A HB B and B HB C, then A HB C |

### 2.2 Visualizing Happens-Before

```
Thread 1              Thread 2
────────              ────────
   │                     │
write x = 42             │
   │                     │
[unlock monitor]  ────────────────────> [lock monitor]
   │              happens-before            │
   │                                     read x
   │                                     (sees 42 ✅)
```

Without the lock/unlock pair:

```
Thread 1              Thread 2
────────              ────────
   │                     │
write x = 42             │
   │                  read x
   ╳ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─╳
   No happens-before!
   (might see 0 or 42 ❓)
```

---

## 3. Memory Barriers / Fences

JMM is implemented through **memory barriers** that restrict reordering:

| Barrier | Prevents |
|---------|----------|
| **LoadLoad** | Loads before barrier not reordered with loads after |
| **StoreStore** | Stores before barrier not reordered with stores after |
| **LoadStore** | Loads before barrier not reordered with stores after |
| **StoreLoad** | Stores before barrier not reordered with loads after (most expensive) |

### 3.1 Where Barriers Are Inserted

| Construct | Barriers |
|-----------|----------|
| **volatile read** | LoadLoad + LoadStore (after read) |
| **volatile write** | StoreStore (before write) + StoreLoad (after write) |
| **synchronized enter** | LoadLoad + LoadStore (after acquiring) |
| **synchronized exit** | StoreStore + LoadStore (before release) |

### 3.2 Barrier Visualization

```
volatile write:
┌─────────────────────────────────────┐
│ [StoreStore barrier]                │  ← Previous writes complete
│ store volatile_field = value        │  ← The volatile write
│ [StoreLoad barrier]                 │  ← Flush to main memory
└─────────────────────────────────────┘

volatile read:
┌─────────────────────────────────────┐
│ load value = volatile_field         │  ← The volatile read
│ [LoadLoad barrier]                  │  ← Subsequent loads see fresh data
│ [LoadStore barrier]                 │  ← Subsequent stores after this load
└─────────────────────────────────────┘
```

---

## 4. volatile Keyword Deep Dive

### 4.1 What volatile Guarantees

| Guarantee | Description |
|-----------|-------------|
| **Visibility** | Writes are immediately visible to all threads |
| **Ordering** | Prevents reordering across the volatile access |
| **64-bit atomicity** | Atomic reads/writes for `long` and `double` |

### 4.2 What volatile Does NOT Guarantee

| NOT Guaranteed | Example |
|----------------|---------|
| **Atomicity of compound ops** | `volatile int x; x++;` is NOT atomic |
| **Mutual exclusion** | Multiple threads can read/write simultaneously |

```java
// ❌ WRONG: volatile does not make increment atomic
private volatile int counter = 0;

void increment() {
    counter++;  // Read → Increment → Write (3 operations!)
}

// ✅ CORRECT: Use AtomicInteger
private final AtomicInteger counter = new AtomicInteger(0);

void increment() {
    counter.incrementAndGet();  // Single atomic CAS operation
}
```

### 4.3 When to Use volatile

```java
// ✅ Use Case 1: Status flag
private volatile boolean running = true;

void run() {
    while (running) {  // Guaranteed to see updates
        // work
    }
}

void stop() {
    running = false;  // Immediately visible to reader
}

// ✅ Use Case 2: Double-checked locking (with volatile!)
private static volatile Singleton instance;

public static Singleton getInstance() {
    if (instance == null) {
        synchronized (Singleton.class) {
            if (instance == null) {
                instance = new Singleton();
            }
        }
    }
    return instance;
}

// ✅ Use Case 3: Publishing immutable objects
volatile ImmutableConfig config;

void updateConfig(ImmutableConfig newConfig) {
    config = newConfig;  // Safe publication of immutable object
}
```

---

## 5. Safe Publication

**Publication** = Making an object available to code outside the current thread.

### 5.1 Unsafe Publication Example

```java
// ❌ UNSAFE: Other threads may see partially constructed object
public class Holder {
    private int value;
    
    public Holder(int value) {
        this.value = value;  // Another thread might see value = 0
    }
}

// Publishing without synchronization
public Holder holder;  // non-volatile!

void initialize() {
    holder = new Holder(42);  // UNSAFE!
}
```

### 5.2 Safe Publication Idioms

| Idiom | Example |
|-------|---------|
| **Static initializer** | `static Holder holder = new Holder(42);` |
| **volatile field** | `volatile Holder holder = new Holder(42);` |
| **AtomicReference** | `AtomicReference<Holder> holder = ...` |
| **Synchronized** | `synchronized` block when writing and reading |
| **final field** | `final Holder holder = new Holder(42);` |
| **Concurrent collection** | `ConcurrentHashMap.put()` |

### 5.3 final Field Semantics

```java
public class FinalFieldExample {
    private final int x;
    private int y;
    
    public FinalFieldExample() {
        x = 3;  // Final field write
        y = 4;  // Non-final field write
    }
}

// Reader thread is GUARANTEED to see x = 3 after construction completes
// Reader thread may NOT see y = 4 (no happens-before for non-final)
```

> [!IMPORTANT]
> **Final Field Freeze**: At the end of constructor, a "freeze" action occurs for final fields. Any thread that sees a correctly published reference to the object is guaranteed to see correct final field values.

---

## 6. Reordering & Compiler Optimizations

### 6.1 Legal Reorderings

| Reordering | Impact |
|------------|--------|
| **Compiler** | Reorder instructions for CPU pipeline efficiency |
| **CPU** | Out-of-order execution, speculative execution |
| **Store Buffer** | Writes buffered before going to cache/memory |
| **Cache Coherence** | Different cores see different memory states |

### 6.2 Example: Why Reordering Breaks Code

```java
// Initial state: x = 0, y = 0

// Thread 1           // Thread 2
x = 1;                int r1 = y;
y = 1;                int r2 = x;

// Without synchronization, possible outcome: r1 = 1, r2 = 0
// This seems impossible sequentially but is allowed by JMM!
```

**How It Happens:**

```
Thread 1's store to x buffered in store buffer
Thread 1's store to y flushes immediately
Thread 2 sees y = 1, but x still 0 (from old cache line)
```

---

## 7. Memory Model for Common Patterns

### 7.1 Producer-Consumer with volatile

```java
class ProducerConsumer {
    private volatile boolean dataReady = false;
    private Object data;  // Non-volatile is OK!
    
    void produce() {
        data = computeData();    // (1) Write data
        dataReady = true;        // (2) volatile write
        // StoreStore barrier between (1) and (2)
        // Plus StoreLoad after (2)
    }
    
    void consume() {
        if (dataReady) {         // (3) volatile read
            // LoadLoad + LoadStore barriers after (3)
            process(data);        // (4) Safe: sees data written in (1)
        }
    }
}
```

### 7.2 Double-Checked Locking Explained

```java
class Singleton {
    private static volatile Singleton instance;  // MUST be volatile!
    
    public static Singleton getInstance() {
        Singleton local = instance;  // First read (may be cached)
        if (local == null) {
            synchronized (Singleton.class) {
                local = instance;  // Second read (inside sync)
                if (local == null) {
                    instance = local = new Singleton();
                }
            }
        }
        return local;
    }
}
```

**Why volatile is Required:**

Without volatile, the constructor call `new Singleton()` can be reordered:

```
// What we expect:
1. Allocate memory
2. Run constructor (initialize fields)
3. Assign reference to instance

// What CPU might do without volatile:
1. Allocate memory
3. Assign reference to instance  ← Another thread sees non-null but uninitialized!
2. Run constructor
```

---

## 8. Atomic Variables & CAS

### 8.1 Compare-And-Swap (CAS)

```java
// Conceptual CAS operation (atomic in hardware)
boolean compareAndSwap(AtomicInteger ref, int expected, int newValue) {
    if (ref.value == expected) {
        ref.value = newValue;
        return true;
    }
    return false;
}

// Usage pattern
AtomicInteger counter = new AtomicInteger(0);

void atomicIncrement() {
    int current, next;
    do {
        current = counter.get();
        next = current + 1;
    } while (!counter.compareAndSet(current, next));
}
```

### 8.2 Memory Semantics of Atomic Operations

| Operation | Memory Effect |
|-----------|---------------|
| `get()` | volatile read semantics |
| `set()` | volatile write semantics |
| `compareAndSet()` | volatile read + volatile write |
| `lazySet()` | Eventually visible (weaker, faster) |
| `getAcquire()` | Acquire semantics (JDK 9+) |
| `setRelease()` | Release semantics (JDK 9+) |

### 8.3 VarHandle (JDK 9+)

```java
// Lower-level memory access with explicit ordering modes
VarHandle vh = MethodHandles.lookup()
    .findVarHandle(MyClass.class, "field", int.class);

// Different memory ordering modes
vh.get(obj);                    // Plain read
vh.getVolatile(obj);            // volatile read
vh.getAcquire(obj);             // Acquire semantics
vh.getOpaque(obj);              // Opaque: no reordering but no cache coherence

vh.set(obj, 42);                // Plain write
vh.setVolatile(obj, 42);        // volatile write
vh.setRelease(obj, 42);         // Release semantics
```

---

## 9. Diagnosing Memory Visibility Issues

### 9.1 Symptoms

| Symptom | Likely Cause |
|---------|--------------|
| Field "never" updates | Missing volatile / synchronization |
| Intermittent wrong values | Race condition on compound operation |
| Works in debugger, fails in prod | Debugger adds memory barriers |
| Works with `-Xint`, fails normally | JIT reordering |

### 9.2 Debugging Tips

```bash
# Disable JIT to check if JIT reordering is the issue
java -Xint YourApp

# Force all fields to act as volatile (for testing only!)
-XX:+JVMCICompiler -Dgraal.TraceMemoryAccess=true

# Use JCStress for concurrency testing
# https://github.com/openjdk/jcstress
```

### 9.3 JCStress Test Example

```java
@JCStressTest
@Outcome(id = "0, 0", expect = ACCEPTABLE, desc = "Both reads see initial value")
@Outcome(id = "1, 1", expect = ACCEPTABLE, desc = "Both reads see new value")
@Outcome(id = "0, 1", expect = ACCEPTABLE, desc = "Read X first, then Y written")
@Outcome(id = "1, 0", expect = ACCEPTABLE_INTERESTING, desc = "Reordering observed!")
@State
public class ReorderingTest {
    int x, y;
    
    @Actor
    public void actor1() {
        x = 1;
        y = 1;
    }
    
    @Actor
    public void actor2(II_Result r) {
        r.r1 = y;
        r.r2 = x;
    }
}
```

---

## 10. Quick Reference: Visibility Guarantees

| Construct | Visibility Guarantee |
|-----------|---------------------|
| **Local variable** | Thread-confined, no sharing issue |
| **Non-volatile field** | No guarantee across threads |
| **volatile field** | Full visibility + ordering |
| **synchronized block** | Full visibility + mutual exclusion |
| **final field** | Visible after safe publication |
| **Atomic* classes** | volatile semantics + atomicity |

---

## 11. Best Practices

```java
// ✅ 1. Prefer immutability
final class ImmutableValue {
    private final int value;
    // ...
}

// ✅ 2. Use volatile for flags
private volatile boolean done = false;

// ✅ 3. Use Atomic* for counters
private final AtomicLong counter = new AtomicLong();

// ✅ 4. Use synchronized for compound operations
synchronized (lock) {
    if (condition) {
        doSomething();
    }
}

// ✅ 5. Use concurrent collections for shared data structures
ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
```
