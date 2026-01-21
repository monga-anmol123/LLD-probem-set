# Solution: LRU Cache

## ✅ Complete Implementation

This folder contains a production-quality LRU Cache implementation with multiple eviction strategies (LRU, LFU, FIFO, MRU) demonstrating the Strategy pattern with O(1) time complexity for get and put operations.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                         Main.java                            │
│                    (Demo/Entry Point)                        │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
        ▼               ▼               ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐
│   Strategy   │ │   Service   │ │   Model    │
│              │ │             │ │            │
│ Eviction     │ │ LRUCache    │ │ CacheNode  │
│ Strategy     │ │             │ │ DLL        │
│ (4 types)    │ │             │ │ Statistics │
└──────────────┘ └─────────────┘ └────────────┘
```

**Key Components:**
- **HashMap:** O(1) key lookup
- **Doubly Linked List:** O(1) insertion/deletion
- **Strategy Pattern:** Pluggable eviction policies
- **Statistics Tracking:** Hit rate, evictions, etc.

---

## 📦 Package Structure

```
src/
├── enums/
│   └── EvictionPolicy.java       # LRU, LFU, FIFO, MRU
│
├── model/
│   ├── CacheNode.java            # Node with key, value, pointers
│   ├── DoublyLinkedList.java     # O(1) operations
│   └── CacheStatistics.java      # Hit/miss tracking
│
├── strategy/
│   ├── EvictionStrategy.java     # Strategy interface
│   ├── LRUEviction.java          # Least Recently Used
│   ├── LFUEviction.java          # Least Frequently Used
│   ├── FIFOEviction.java         # First In First Out
│   └── MRUEviction.java          # Most Recently Used
│
├── service/
│   └── LRUCache.java             # Main cache implementation
│
└── Main.java                      # Demo with 7 scenarios
```

---

## 🎨 Design Pattern: Strategy Pattern

### Purpose
Allow different eviction algorithms to be swapped at runtime without changing cache implementation.

### Implementation

```java
// Strategy interface
public interface EvictionStrategy<K, V> {
    void onAccess(CacheNode<K, V> node, DoublyLinkedList<K, V> list);
    CacheNode<K, V> evict(DoublyLinkedList<K, V> list);
    String getStrategyName();
}

// LRU Strategy: Evict least recently used
public class LRUEviction<K, V> implements EvictionStrategy<K, V> {
    @Override
    public void onAccess(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // Move accessed node to front (most recently used)
        list.moveToFront(node);
    }
    
    @Override
    public CacheNode<K, V> evict(DoublyLinkedList<K, V> list) {
        // Remove from tail (least recently used)
        return list.removeLast();
    }
}

// LFU Strategy: Evict least frequently used
public class LFUEviction<K, V> implements EvictionStrategy<K, V> {
    @Override
    public void onAccess(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // Increment frequency counter
        node.incrementFrequency();
    }
    
    @Override
    public CacheNode<K, V> evict(DoublyLinkedList<K, V> list) {
        // Find and remove node with lowest frequency
        return list.removeLeastFrequent();
    }
}

// FIFO Strategy: Evict oldest entry
public class FIFOEviction<K, V> implements EvictionStrategy<K, V> {
    @Override
    public void onAccess(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // Do nothing - access doesn't affect order
    }
    
    @Override
    public CacheNode<K, V> evict(DoublyLinkedList<K, V> list) {
        // Remove from tail (oldest)
        return list.removeLast();
    }
}

// MRU Strategy: Evict most recently used
public class MRUEviction<K, V> implements EvictionStrategy<K, V> {
    @Override
    public void onAccess(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // Move to front
        list.moveToFront(node);
    }
    
    @Override
    public CacheNode<K, V> evict(DoublyLinkedList<K, V> list) {
        // Remove from front (most recently used)
        return list.removeFirst();
    }
}
```

### Benefits
- ✅ Easy to add new eviction strategies
- ✅ Switch strategies at runtime
- ✅ Open-Closed Principle (OCP)
- ✅ Each strategy encapsulated

---

## 🔑 Key Design Decisions

### 1. Data Structure: HashMap + Doubly Linked List

**Why this combination?**

```java
public class LRUCache<K, V> {
    private Map<K, CacheNode<K, V>> cache;  // HashMap for O(1) lookup
    private DoublyLinkedList<K, V> list;     // DLL for O(1) reordering
    private int capacity;
    private EvictionStrategy<K, V> strategy;
}
```

**HashMap:**
- O(1) get by key
- O(1) put by key
- O(1) remove by key

**Doubly Linked List:**
- O(1) add to front
- O(1) remove from tail
- O(1) move node to front (with node reference)
- O(1) remove any node (with node reference)

**Combined:**
- HashMap stores key → node mapping
- DLL maintains access order
- Both operations remain O(1)!

---

### 2. Cache Node Structure

```java
public class CacheNode<K, V> {
    private K key;
    private V value;
    private CacheNode<K, V> prev;
    private CacheNode<K, V> next;
    private int frequency;        // For LFU
    private long timestamp;       // For FIFO
    
    public CacheNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.frequency = 0;
        this.timestamp = System.currentTimeMillis();
    }
    
    public void incrementFrequency() {
        this.frequency++;
    }
}
```

**Why store key in node?**
- When evicting, we need to remove from HashMap
- HashMap.remove(key) requires the key
- Node stores key for easy removal

---

### 3. Doubly Linked List Operations

```java
public class DoublyLinkedList<K, V> {
    private CacheNode<K, V> head;
    private CacheNode<K, V> tail;
    private int size;
    
    // Add to front (most recently used)
    public void addFirst(CacheNode<K, V> node) {
        node.next = head;
        node.prev = null;
        
        if (head != null) {
            head.prev = node;
        }
        head = node;
        
        if (tail == null) {
            tail = node;
        }
        size++;
    }
    
    // Remove from tail (least recently used)
    public CacheNode<K, V> removeLast() {
        if (tail == null) return null;
        
        CacheNode<K, V> node = tail;
        remove(node);
        return node;
    }
    
    // Remove any node (O(1) with node reference)
    public void remove(CacheNode<K, V> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        size--;
    }
    
    // Move to front (mark as recently used)
    public void moveToFront(CacheNode<K, V> node) {
        remove(node);
        addFirst(node);
    }
}
```

**Time Complexity:**
- `addFirst()`: O(1)
- `removeLast()`: O(1)
- `remove(node)`: O(1) - because we have node reference
- `moveToFront()`: O(1) - remove + addFirst

---

### 4. Cache Operations

#### Get Operation

```java
public V get(K key) {
    if (!cache.containsKey(key)) {
        statistics.recordMiss();
        return null;
    }
    
    CacheNode<K, V> node = cache.get(key);
    
    // Update access order based on strategy
    strategy.onAccess(node, list);
    
    statistics.recordHit();
    return node.getValue();
}
```

**Time Complexity:** O(1)
1. HashMap lookup: O(1)
2. Strategy onAccess: O(1) (moveToFront or increment frequency)
3. Total: O(1)

#### Put Operation

```java
public void put(K key, V value) {
    if (cache.containsKey(key)) {
        // Update existing entry
        CacheNode<K, V> node = cache.get(key);
        node.setValue(value);
        strategy.onAccess(node, list);
    } else {
        // Add new entry
        if (cache.size() >= capacity) {
            evict();
        }
        
        CacheNode<K, V> newNode = new CacheNode<>(key, value);
        cache.put(key, newNode);
        list.addFirst(newNode);
    }
}
```

**Time Complexity:** O(1)
1. HashMap containsKey: O(1)
2. Eviction (if needed): O(1)
3. HashMap put: O(1)
4. List addFirst: O(1)
5. Total: O(1)

#### Evict Operation

```java
private void evict() {
    if (list.isEmpty()) return;
    
    // Strategy determines which node to evict
    CacheNode<K, V> evicted = strategy.evict(list);
    
    if (evicted != null) {
        cache.remove(evicted.getKey());
        statistics.recordEviction();
    }
}
```

**Time Complexity:** O(1)
- Strategy evict: O(1) (removeLast or removeFirst)
- HashMap remove: O(1)
- Total: O(1)

---

## 🎯 Eviction Strategy Comparison

| Strategy | When to Evict | Use Case | Pros | Cons |
|----------|---------------|----------|------|------|
| **LRU** | Least recently accessed | General purpose | Good hit rate | May evict popular items |
| **LFU** | Least frequently accessed | Stable access patterns | Keeps popular items | Slow to adapt |
| **FIFO** | Oldest entry | Simple, predictable | Easy to understand | Ignores access patterns |
| **MRU** | Most recently accessed | Specific workloads | Good for scans | Counter-intuitive |

### LRU (Least Recently Used)
**Best for:** General-purpose caching, web caches, database buffers

**Example:**
```
Cache: [A, B, C] (capacity: 3)
Access: A → [A, B, C]
Access: D → [D, A, B] (C evicted)
```

### LFU (Least Frequently Used)
**Best for:** Stable access patterns, content delivery networks

**Example:**
```
Cache: [A(freq:5), B(freq:3), C(freq:1)]
Access: D → [A(freq:5), B(freq:3), D(freq:1)] (C evicted)
```

### FIFO (First In First Out)
**Best for:** Predictable access, simple requirements

**Example:**
```
Cache: [A, B, C] (capacity: 3)
Access: D → [B, C, D] (A evicted, oldest)
```

### MRU (Most Recently Used)
**Best for:** Sequential scans, one-time access patterns

**Example:**
```
Cache: [A, B, C] (capacity: 3)
Access: D → [A, B, D] (C evicted, most recent)
```

---

## 📊 Cache Statistics

```java
public class CacheStatistics {
    private long hits;
    private long misses;
    private long evictions;
    
    public void recordHit() {
        hits++;
    }
    
    public void recordMiss() {
        misses++;
    }
    
    public void recordEviction() {
        evictions++;
    }
    
    public double getHitRate() {
        long total = hits + misses;
        return total == 0 ? 0.0 : (double) hits / total * 100.0;
    }
    
    public void display() {
        System.out.println("=== Cache Statistics ===");
        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Evictions: " + evictions);
        System.out.println("Hit Rate: " + String.format("%.2f%%", getHitRate()));
    }
}
```

---

## ⚖️ Trade-offs

### 1. HashMap vs LinkedHashMap

**Current:** HashMap + Custom DoublyLinkedList

**Alternative:** LinkedHashMap (built-in)

```java
// Using LinkedHashMap (simpler but less flexible)
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private int capacity;
    
    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); // accessOrder = true
        this.capacity = capacity;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
```

**Pros of LinkedHashMap:**
- ✅ Simpler implementation
- ✅ Less code to maintain
- ✅ Built-in access ordering

**Cons of LinkedHashMap:**
- ❌ Only supports LRU (no LFU, FIFO, MRU)
- ❌ Less control over eviction
- ❌ Can't add custom statistics
- ❌ Can't implement Strategy pattern easily

**Verdict:** Custom implementation is better for learning and flexibility.

---

### 2. Thread Safety

**Current:** Not thread-safe (for simplicity)

**Production Solution:**

```java
public class ThreadSafeLRUCache<K, V> {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final LRUCache<K, V> cache;
    
    public V get(K key) {
        lock.readLock().lock();
        try {
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }
}

// Or use ConcurrentHashMap + synchronized methods
public synchronized V get(K key) { ... }
public synchronized void put(K key, V value) { ... }
```

---

### 3. Memory Overhead

**Current:** Each node stores prev, next, frequency, timestamp

**Memory per entry:**
- Key: varies
- Value: varies
- Prev pointer: 8 bytes
- Next pointer: 8 bytes
- Frequency: 4 bytes
- Timestamp: 8 bytes
- **Total overhead: ~28 bytes per entry**

**For 10,000 entries:** ~280 KB overhead (acceptable)

**Optimization:**
```java
// Remove unused fields based on strategy
public class LRUNode<K, V> {
    K key;
    V value;
    LRUNode<K, V> prev, next;
    // No frequency, no timestamp (LRU only)
}
```

---

## 🧪 Test Scenarios

### Scenario 1: Basic LRU Operations
```
Cache capacity: 3
Put(1, "A") → [1]
Put(2, "B") → [2, 1]
Put(3, "C") → [3, 2, 1]
Get(1) → "A", [1, 3, 2] (1 moved to front)
Put(4, "D") → [4, 1, 3] (2 evicted as LRU)
Get(2) → null (evicted)
```

### Scenario 2: LFU Strategy
```
Cache capacity: 3
Put(1, "A") freq:0 → [1]
Put(2, "B") freq:0 → [2, 1]
Put(3, "C") freq:0 → [3, 2, 1]
Get(1) freq:1 → [1, 3, 2]
Get(1) freq:2 → [1, 3, 2]
Get(2) freq:1 → [2, 1, 3]
Put(4, "D") → [4, 2, 1] (3 evicted, freq:0)
```

### Scenario 3: Hit Rate Calculation
```
10 gets: 7 hits, 3 misses
Hit Rate = 7/10 * 100 = 70%
```

---

## 🚀 How to Compile and Run

### Command Line
```bash
cd src/
javac enums/*.java model/*.java strategy/*.java service/*.java Main.java
java Main
```

### Expected Output
```
========================================
  LRU CACHE SYSTEM DEMO
========================================

SCENARIO 1: Basic LRU Cache
✓ Created cache with capacity 3
Put(1, "A") → Cache: [1]
Put(2, "B") → Cache: [2, 1]
Put(3, "C") → Cache: [3, 2, 1]
Get(1) → "A" (HIT) → Cache: [1, 3, 2]
Put(4, "D") → Cache: [4, 1, 3] (Evicted: 2)
Get(2) → null (MISS)

=== Cache Statistics ===
Hits: 1
Misses: 1
Evictions: 1
Hit Rate: 50.00%

...
```

---

## 📈 Extensions

### 1. TTL (Time To Live)
```java
public class CacheNode<K, V> {
    private long expiryTime;
    
    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public V get(K key) {
    CacheNode<K, V> node = cache.get(key);
    if (node != null && node.isExpired()) {
        remove(key);
        return null;
    }
    // ...
}
```

### 2. Write-Through Cache
```java
public interface CacheLoader<K, V> {
    V load(K key);
    void persist(K key, V value);
}

public void put(K key, V value) {
    // Write to cache
    cache.put(key, value);
    
    // Write to backing store
    loader.persist(key, value);
}
```

### 3. Cache Warming
```java
public void warmUp(List<K> keys) {
    for (K key : keys) {
        V value = loader.load(key);
        put(key, value);
    }
}
```

---

## 🎯 Interview Tips

### Common Questions

**Q: "Why HashMap + DoublyLinkedList?"**
- HashMap: O(1) lookup
- DLL: O(1) reordering
- Combined: O(1) get and put

**Q: "How do you achieve O(1) for moveToFront?"**
- Store node reference in HashMap
- With reference, remove() is O(1)
- addFirst() is also O(1)
- Total: O(1)

**Q: "What if capacity is 0 or negative?"**
```java
if (capacity <= 0) {
    throw new IllegalArgumentException("Capacity must be positive");
}
```

**Q: "How would you make it thread-safe?"**
- Use `synchronized` methods
- Or use `ReadWriteLock`
- Or use `ConcurrentHashMap` + synchronized blocks

**Q: "LRU vs LFU - which is better?"**
- LRU: Better for general purpose, adapts quickly
- LFU: Better for stable patterns, keeps popular items
- Depends on access pattern!

---

## ✅ Checklist

- [ ] Understand HashMap + DLL combination
- [ ] Can implement O(1) get and put
- [ ] Can explain Strategy pattern
- [ ] Can implement all 4 eviction strategies
- [ ] Can calculate hit rate
- [ ] Can handle edge cases
- [ ] Can discuss thread safety
- [ ] Can explain trade-offs

---

**This solution demonstrates a production-quality cache with O(1) operations and flexible eviction strategies! 🚀**

