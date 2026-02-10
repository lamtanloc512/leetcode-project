# Serialization Deep Dive

> Understanding Java's object persistence, transient keyword, and modern alternatives

---

## Related Topics

- [[exceptions-errors]] - NotSerializableException
- [[jvm-architecture]] - Object memory layout
- [[../spring/spring-expert-guide]] - Spring serialization

---

## 1. Java Serialization Overview

**Serialization** = Converting object graph to byte stream
**Deserialization** = Reconstructing objects from byte stream

```java
// Serialization
try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.ser"))) {
    oos.writeObject(myObject);
}

// Deserialization
try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.ser"))) {
    MyClass obj = (MyClass) ois.readObject();
}
```

---

## 2. Serializable vs Externalizable

| Interface | Control | Usage |
|-----------|---------|-------|
| `Serializable` | JVM handles | Marker interface, minimal code |
| `Externalizable` | You handle | Full control, must implement |

### 2.1 Serializable (Default Mechanism)

```java
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private int age;
    private transient String password;  // Not serialized
}
```

### 2.2 Externalizable (Full Control)

```java
public class User implements Externalizable {
    private String name;
    private int age;
    
    // MUST have public no-arg constructor
    public User() {}
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
        out.writeInt(age);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        name = in.readUTF();
        age = in.readInt();
    }
}
```

---

## 3. serialVersionUID Explained

### 3.1 Purpose

Unique identifier for serialization compatibility:

```java
private static final long serialVersionUID = 1L;
```

**If not declared**: JVM generates one based on class structure (fields, methods). **Any change** to class = different UID = `InvalidClassException` on deserialization.

### 3.2 Compatibility Strategy

| Change Type | Compatible? | Recommendation |
|-------------|-------------|----------------|
| Add field | ✅ (gets default value) | OK |
| Remove field | ⚠️ (ignored on deser) | Typically OK |
| Change field type | ❌ | Avoid |
| Add method | ✅ | OK |
| Rename field | ❌ | Avoid |

```java
// Versioning example
public class User implements Serializable {
    // Version 1: name, age
    // Version 2: name, age, email (added)
    
    private static final long serialVersionUID = 1L;  // Keep same if compatible
    
    private String name;
    private int age;
    private String email;  // New field - gets null when deserializing old data
}
```

---

## 4. transient Keyword

### 4.1 Basic Usage

Fields marked `transient` are **not serialized**:

```java
public class Connection implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String url;
    private transient Socket socket;    // Not serialized (can't serialize socket)
    private transient Logger logger;    // Not serialized (recreate on load)
    private transient String cachedHash;// Not serialized (can recompute)
}
```

### 4.2 Transient vs Static

| Modifier | Serialized? | Reason |
|----------|-------------|--------|
| `transient` | ❌ No | Excluded by design |
| `static` | ❌ No | Class-level, not instance |
| `transient static` | ❌ No | Redundant but allowed |

### 4.3 When to Use transient

```java
public class SecureUser implements Serializable {
    private String username;
    private transient char[] password;     // Security: don't persist
    
    private transient Connection dbConn;   // Resource: can't serialize
    
    private transient Cache<String, Data> cache;  // Derived: rebuild on load
    
    private transient ReentrantLock lock;  // Concurrency: recreate
}
```

---

## 5. Custom Serialization

### 5.1 writeObject / readObject

Override default serialization:

```java
public class EncryptedData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String sensitiveData;
    private transient String cachedDecrypted;
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        // Write non-transient fields first
        out.defaultWriteObject();
        
        // Custom: write encrypted form
        out.writeObject(encrypt(sensitiveData));
    }
    
    private void readObject(ObjectInputStream in) 
            throws IOException, ClassNotFoundException {
        // Read non-transient fields
        in.defaultReadObject();
        
        // Custom: decrypt what we wrote
        sensitiveData = decrypt((byte[]) in.readObject());
        
        // Reinitialize transient fields
        cachedDecrypted = sensitiveData;
    }
}
```

### 5.2 writeReplace / readResolve

Replace object during serialization:

```java
public class Singleton implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Singleton INSTANCE = new Singleton();
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        return INSTANCE;
    }
    
    // Called during deserialization - return the real singleton
    private Object readResolve() {
        return INSTANCE;  // Don't create new instance!
    }
}
```

```java
// writeReplace example: Serialize a proxy instead
public class LargeObject implements Serializable {
    private byte[] hugeData;
    
    // Replace with smaller proxy during serialization
    private Object writeReplace() {
        return new LargeObjectProxy(computeId());
    }
}

public class LargeObjectProxy implements Serializable {
    private String id;
    
    private Object readResolve() {
        return loadFromDatabase(id);  // Reconstruct on read
    }
}
```

---

## 6. Serialization Pitfalls

### 6.1 Non-Serializable Parent Class

```java
public class Animal {  // Not Serializable
    protected String name;
    
    public Animal(String name) { this.name = name; }
}

public class Dog extends Animal implements Serializable {
    private String breed;
    
    public Dog(String name, String breed) {
        super(name);
        this.breed = breed;
    }
}

// On deserialization:
// - Dog.breed is restored ✅
// - Animal.name uses no-arg constructor value (Animal MUST have no-arg constructor!) ❌
```

> [!WARNING]
> If a non-serializable superclass has no no-arg constructor, deserialization will fail with `InvalidClassException`.

### 6.2 Object Graph Problems

```java
public class Node implements Serializable {
    private Object data;
    private Node next;  // Could create huge chain
    private Node parent;  // Circular reference
}

// Serialization handles cycles correctly (writes back-references)
// But: Large object graphs = OutOfMemoryError during serialization
```

### 6.3 Deserialization Attacks

```java
// ❌ DANGEROUS: Deserialize untrusted data
ObjectInputStream ois = new ObjectInputStream(untrustedInput);
Object obj = ois.readObject();  // Could execute malicious code!
```

**Mitigations:**

```java
// 1. Use ObjectInputFilter (Java 9+)
ObjectInputFilter filter = ObjectInputFilter.Config.createFilter(
    "com.myapp.*;!*"  // Allow only com.myapp classes
);
ois.setObjectInputFilter(filter);

// 2. Better: Don't use Java serialization for untrusted data
// Use JSON, Protobuf, etc.
```

---

## 7. Modern Alternatives

### 7.1 Comparison

| Format | Speed | Size | Cross-platform | Human-readable |
|--------|-------|------|----------------|----------------|
| **Java Serialization** | Slow | Large | ❌ Java only | ❌ |
| **JSON** | Medium | Medium | ✅ | ✅ |
| **Protocol Buffers** | Fast | Small | ✅ | ❌ |
| **Avro** | Fast | Small | ✅ | ❌ |
| **Kryo** | Very Fast | Small | ❌ Java | ❌ |

### 7.2 JSON (Jackson)

```java
ObjectMapper mapper = new ObjectMapper();

// Serialize
String json = mapper.writeValueAsString(myObject);

// Deserialize  
MyClass obj = mapper.readValue(json, MyClass.class);
```

**Transient equivalent in Jackson:**

```java
public class User {
    private String name;
    
    @JsonIgnore                    // Like transient
    private String password;
    
    @JsonProperty(access = READ_ONLY)  // Serialize but don't deserialize
    private LocalDateTime createdAt;
}
```

### 7.3 Protocol Buffers

```protobuf
// user.proto
message User {
    string name = 1;
    int32 age = 2;
}
```

```java
// Generated code
User user = User.newBuilder()
    .setName("Alice")
    .setAge(30)
    .build();

// Serialize
byte[] bytes = user.toByteArray();

// Deserialize
User restored = User.parseFrom(bytes);
```

---

## 8. Record Serialization (Java 16+)

```java
public record Point(int x, int y) implements Serializable {
    // Records have built-in serialization support
    // Uses canonical constructor for deserialization (safe!)
}
```

**Advantage**: Record deserialization calls the constructor, which validates input. Standard deserialization bypasses constructors, which can create invalid objects.

---

## 9. Quick Reference: Serialization Hooks

| Method | When Called | Purpose |
|--------|-------------|---------|
| `writeObject(ObjectOutputStream)` | During serialization | Custom field writing |
| `readObject(ObjectInputStream)` | During deserialization | Custom field reading |
| `writeReplace()` | Before serialization | Substitute object |
| `readResolve()` | After deserialization | Substitute object |
| `readObjectNoData()` | Superclass added to hierarchy | Initialize new fields |

---

## 10. Best Practices

```java
// ✅ 1. Always declare serialVersionUID
private static final long serialVersionUID = 1L;

// ✅ 2. Use transient for non-serializable or sensitive fields
private transient Connection connection;
private transient char[] password;

// ✅ 3. Consider @Serial annotation (Java 14+)
@Serial
private static final long serialVersionUID = 1L;

@Serial
private void writeObject(ObjectOutputStream out) throws IOException {
    // ...
}

// ✅ 4. Prefer modern formats for new systems
// JSON for APIs, Protobuf for internal services

// ✅ 5. Never deserialize untrusted data
```
