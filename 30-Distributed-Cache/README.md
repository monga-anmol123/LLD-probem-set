# Problem 30: Distributed Cache System

## 🎯 Difficulty: Hard ⭐⭐⭐⭐

## 📝 Problem Statement

Design a distributed cache system that supports multiple eviction policies (LRU, LFU, FIFO, TTL), write strategies (write-through, write-back, cache-aside), TTL support, cache statistics, and sharding for horizontal scalability. This is one of the most frequently asked LLD problems at FAANG companies.

---

## 🔍 Functional Requirements (FR)

### FR1: Basic Cache Operations
- `put(key, value)` - Store key-value pair
- `get(key)` - Retrieve value by key
- `delete(key)` - Remove key-value pair
- `clear()` - Clear entire cache
- `size()` - Get current cache size

### FR2: Eviction Policies
- **LRU (Least Recently Used)** - Evict least recently accessed item
- **LFU (Least Frequently Used)** - Evict least frequently accessed item
- **FIFO (First In First Out)** - Evict oldest item
- **TTL (Time To Live)** - Evict expired items
- **Random** - Evict random item

### FR3: TTL (Time To Live) Support
- Set TTL per key
- Automatic expiration
- Lazy deletion on access
- Active cleanup of expired entries

### FR4: Write Strategies
- **Write-Through** - Write to cache and database synchronously
- **Write-Back** - Write to cache, async to database
- **Cache-Aside** - Application manages cache and database

### FR5: Cache Statistics
- Hit rate (hits / total requests)
- Miss rate
- Eviction count
- Total requests
- Average access time

### FR6: Capacity Management
- Maximum capacity limit
- Current size tracking
- Memory usage estimation

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Performance
- O(1) get/put operations for LRU
- O(log n) for LFU (using min-heap)
- Sub-millisecond access time
- Efficient memory usage

### NFR2: Thread Safety
- Concurrent access support
- Thread-safe operations
- No race conditions

### NFR3: Scalability
- Support for millions of entries
- Horizontal scaling with sharding
- Consistent hashing for distribution

### NFR4: Reliability
- Handle edge cases (null keys, capacity 0)
- Graceful degradation
- Proper cleanup of expired entries

---

## 🎨 Design Patterns to Use

### 1. **Strategy Pattern**
- **Where:** Eviction policies (LRU, LFU, FIFO, TTL, Random)
- **Why:** Different eviction algorithms with same interface

### 2. **Singleton Pattern**
- **Where:** CacheManager (single instance)
- **Why:** Global cache access point

### 3. **Decorator Pattern**
- **Where:** TTL wrapper, statistics wrapper
- **Why:** Add functionality without modifying core cache

### 4. **Factory Pattern** (Optional)
- **Where:** Cache creation with different policies
- **Why:** Centralized cache instantiation

---

## 📋 Core Entities

### 1. **CacheEntry<K, V>**
- Attributes: `key`, `value`, `timestamp`, `accessCount`, `expirationTime`
- Methods: `isExpired()`, `touch()`, `incrementAccessCount()`

### 2. **EvictionPolicy<K, V>** (Interface)
- Methods: `evict()`, `onGet(key)`, `onPut(key, value)`, `onDelete(key)`

### 3. **LRUCache<K, V>**
- Data Structure: HashMap + Doubly Linked List
- Time Complexity: O(1) for get/put
- Implementation: Move accessed items to head

### 4. **LFUCache<K, V>**
- Data Structure: HashMap + Min-Heap (Priority Queue)
- Time Complexity: O(log n) for get/put
- Implementation: Track access frequency

### 5. **Cache<K, V>** (Main Interface)
- Methods: `get()`, `put()`, `delete()`, `clear()`, `size()`, `getStats()`

### 6. **CacheStatistics**
- Attributes: `hits`, `misses`, `evictions`, `totalRequests`
- Methods: `getHitRate()`, `getMissRate()`, `recordHit()`, `recordMiss()`

---

## 🧪 Test Scenarios

### Scenario 1: LRU Cache Basic Operations
```
1. Create LRU cache with capacity 3
2. Put (1, "A"), (2, "B"), (3, "C")
3. Get(1) - should return "A"
4. Put (4, "D") - should evict key 2 (least recently used)
5. Verify key 2 is evicted
6. Display statistics
```

### Scenario 2: LFU Cache with Frequency Tracking
```
1. Create LFU cache with capacity 3
2. Put (1, "A"), (2, "B"), (3, "C")
3. Get(1) twice, Get(2) once
4. Put (4, "D") - should evict key 3 (least frequently used)
5. Verify eviction based on frequency
```

### Scenario 3: FIFO Cache
```
1. Create FIFO cache with capacity 3
2. Put keys in order: 1, 2, 3
3. Put key 4 - should evict key 1 (first in)
4. Verify FIFO order maintained
```

### Scenario 4: TTL-Based Expiration
```
1. Create cache with TTL support
2. Put key with 2-second TTL
3. Get immediately - should succeed
4. Wait 3 seconds
5. Get again - should return null (expired)
6. Verify expired entries cleaned up
```

### Scenario 5: Cache Statistics
```
1. Perform multiple get/put operations
2. Track hits and misses
3. Calculate hit rate
4. Display statistics (hits, misses, evictions)
```

### Scenario 6: Capacity and Eviction
```
1. Create cache with capacity 5
2. Fill to capacity
3. Add more items
4. Verify eviction policy applied
5. Check size remains at capacity
```

### Scenario 7: Edge Cases
```
1. Test null key/value handling
2. Test capacity 0
3. Test get on empty cache
4. Test delete non-existent key
5. Test clear operation
```

---

## ⏱️ Time Allocation (90 minutes)

- **10 mins:** Clarify requirements, discuss trade-offs
- **10 mins:** Design entities and relationships
- **10 mins:** Identify design patterns
- **50 mins:** Write code (enums → model → strategy → service → main)
- **10 mins:** Test with demo, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: LRU Implementation with HashMap + Doubly Linked List</summary>

```java
public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> cache;
    private final DoublyLinkedList<K, V> list;
    
    public V get(K key) {
        if (!cache.containsKey(key)) return null;
        Node<K, V> node = cache.get(key);
        list.moveToHead(node); // Mark as recently used
        return node.value;
    }
    
    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            Node<K, V> node = cache.get(key);
            node.value = value;
            list.moveToHead(node);
        } else {
            if (cache.size() >= capacity) {
                Node<K, V> tail = list.removeTail();
                cache.remove(tail.key);
            }
            Node<K, V> newNode = new Node<>(key, value);
            list.addToHead(newNode);
            cache.put(key, newNode);
        }
    }
}
```
</details>

<details>
<summary>Hint 2: LFU Implementation with Frequency Map</summary>

```java
public class LFUCache<K, V> {
    private final Map<K, CacheEntry<K, V>> cache;
    private final Map<Integer, LinkedHashSet<K>> frequencyMap;
    private int minFrequency;
    
    public V get(K key) {
        if (!cache.containsKey(key)) return null;
        CacheEntry<K, V> entry = cache.get(key);
        updateFrequency(entry);
        return entry.value;
    }
    
    private void updateFrequency(CacheEntry<K, V> entry) {
        int oldFreq = entry.frequency;
        frequencyMap.get(oldFreq).remove(entry.key);
        
        if (frequencyMap.get(oldFreq).isEmpty()) {
            frequencyMap.remove(oldFreq);
            if (minFrequency == oldFreq) {
                minFrequency++;
            }
        }
        
        entry.frequency++;
        frequencyMap.computeIfAbsent(entry.frequency, k -> new LinkedHashSet<>())
                    .add(entry.key);
    }
}
```
</details>

<details>
<summary>Hint 3: TTL Support</summary>

```java
public class CacheEntry<K, V> {
    K key;
    V value;
    long expirationTime; // System.currentTimeMillis() + ttl
    
    public boolean isExpired() {
        return expirationTime > 0 && System.currentTimeMillis() > expirationTime;
    }
}

// In cache get():
public V get(K key) {
    CacheEntry<K, V> entry = cache.get(key);
    if (entry == null) return null;
    
    if (entry.isExpired()) {
        cache.remove(key);
        return null;
    }
    
    return entry.value;
}
```
</details>

<details>
<summary>Hint 4: Cache Statistics</summary>

```java
public class CacheStatistics {
    private long hits = 0;
    private long misses = 0;
    private long evictions = 0;
    
    public void recordHit() { hits++; }
    public void recordMiss() { misses++; }
    public void recordEviction() { evictions++; }
    
    public double getHitRate() {
        long total = hits + misses;
        return total == 0 ? 0.0 : (double) hits / total * 100;
    }
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Consistent Hashing**
   - Distribute keys across multiple cache nodes
   - Handle node addition/removal

2. **Write-Through Cache**
   - Integrate with mock database
   - Synchronous writes

3. **Write-Back Cache**
   - Async writes with queue
   - Dirty bit tracking

4. **Cache Warming**
   - Pre-load frequently accessed data
   - Background refresh

5. **Multi-Level Cache**
   - L1 (in-memory) + L2 (distributed)
   - Hierarchical lookup

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Implement LRU cache with O(1) operations
- [ ] Implement LFU cache with frequency tracking
- [ ] Implement FIFO cache
- [ ] Support TTL with automatic expiration
- [ ] Track cache statistics (hit rate, miss rate)
- [ ] Use Strategy pattern for eviction policies
- [ ] Handle edge cases (null, capacity 0, expired entries)
- [ ] Demonstrate all scenarios in Main.java
- [ ] Be thread-safe (basic synchronization)

---

## 📁 File Structure

```
30-Distributed-Cache/
├── README.md (this file)
├── SOLUTION.md
├── COMPILATION-GUIDE.md
└── src/
    ├── enums/
    │   ├── EvictionPolicy.java
    │   └── WriteStrategy.java
    ├── model/
    │   ├── CacheEntry.java
    │   ├── CacheStatistics.java
    │   └── Node.java (for doubly linked list)
    ├── strategy/
    │   ├── EvictionStrategy.java (interface)
    │   ├── LRUEvictionStrategy.java
    │   ├── LFUEvictionStrategy.java
    │   ├── FIFOEvictionStrategy.java
    │   └── TTLEvictionStrategy.java
    ├── service/
    │   ├── Cache.java (interface)
    │   ├── CacheImpl.java
    │   └── CacheManager.java (Singleton)
    └── Main.java
```

---

## 🏢 Interview Tips

### Common Follow-up Questions:
1. **"How would you make this distributed?"**
   - Consistent hashing
   - Sharding strategies
   - Replication for fault tolerance

2. **"What about thread safety?"**
   - Synchronized methods
   - ReadWriteLock for better performance
   - ConcurrentHashMap

3. **"How to handle cache invalidation?"**
   - TTL-based
   - Event-driven invalidation
   - Cache-aside pattern

4. **"What are the trade-offs between LRU and LFU?"**
   - LRU: O(1) but may evict frequently used items
   - LFU: Better for frequency but O(log n) and complex

5. **"How would you monitor cache performance in production?"**
   - Hit/miss rates
   - Eviction rates
   - Memory usage
   - Latency metrics

---

**Good luck! This is a critical problem for FAANG interviews! 🚀**
