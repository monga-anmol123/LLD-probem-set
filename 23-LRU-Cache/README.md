# Problem 23: LRU Cache

## 🎯 Difficulty: Hard ⭐⭐⭐⭐

## 📝 Problem Statement

Design and implement a Least Recently Used (LRU) Cache with configurable eviction strategies, supporting generic key-value pairs with O(1) time complexity for get and put operations.

---

## 🔍 Functional Requirements (FR)

### FR1: Basic Cache Operations
- **Get(key):** Return value if key exists, otherwise return null
- **Put(key, value):** Insert or update key-value pair
- **Remove(key):** Remove key-value pair
- **Clear():** Remove all entries
- **Size():** Return current number of entries

### FR2: Capacity Management
- Fixed maximum capacity
- Automatic eviction when capacity is reached
- Configurable capacity at creation time

### FR3: Eviction Strategies (Strategy Pattern)
- **LRU (Least Recently Used):** Evict least recently accessed item
- **LFU (Least Frequently Used):** Evict least frequently accessed item
- **FIFO (First In First Out):** Evict oldest item
- **MRU (Most Recently Used):** Evict most recently accessed item

### FR4: Cache Statistics
- Hit count (successful gets)
- Miss count (failed gets)
- Eviction count
- Hit rate calculation
- Total operations count

### FR5: Thread Safety (Optional)
- Support concurrent access
- Thread-safe operations
- No race conditions

### FR6: TTL Support (Time To Live)
- Optional expiration time for entries
- Automatic removal of expired entries
- Configurable default TTL

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Performance
- **Get operation:** O(1) time complexity
- **Put operation:** O(1) time complexity
- **Remove operation:** O(1) time complexity
- **Space complexity:** O(n) where n is capacity

### NFR2: Scalability
- Handle large capacities (10,000+ entries)
- Efficient memory usage
- No memory leaks

### NFR3: Extensibility
- Easy to add new eviction strategies
- Support for custom eviction policies
- Pluggable statistics collectors

### NFR4: Reliability
- Handle edge cases:
  - Null keys/values
  - Zero or negative capacity
  - Concurrent modifications
  - Expired entries

### NFR5: Maintainability
- Clean, readable code
- Well-documented
- Comprehensive test coverage

---

## 🎨 Design Patterns to Use

### 1. **Strategy Pattern**
- **Where:** Eviction strategies
- **Why:** Different algorithms for eviction, switchable at runtime

### 2. **Singleton Pattern** (Optional)
- **Where:** Global cache instance
- **Why:** Single cache instance for application-wide use

### 3. **Factory Pattern** (Optional)
- **Where:** Cache creation with different strategies
- **Why:** Centralize cache creation logic

---

## 📋 Core Components

### 1. **CacheNode<K, V>**
- Attributes: `key`, `value`, `frequency`, `timestamp`, `expiryTime`
- Doubly-linked list node for O(1) insertion/deletion

### 2. **LRUCache<K, V>**
- Attributes: `capacity`, `cache (HashMap)`, `evictionStrategy`, `statistics`
- Methods: `get()`, `put()`, `remove()`, `clear()`, `size()`

### 3. **EvictionStrategy** (Interface)
- Methods: `evict(cache)`, `onAccess(node)`, `onInsert(node)`
- Implementations: `LRUEviction`, `LFUEviction`, `FIFOEviction`, `MRUEviction`

### 4. **CacheStatistics**
- Attributes: `hits`, `misses`, `evictions`, `totalOps`
- Methods: `recordHit()`, `recordMiss()`, `recordEviction()`, `getHitRate()`

### 5. **DoublyLinkedList<K, V>**
- Methods: `addFirst()`, `addLast()`, `remove()`, `moveToFront()`, `removeLast()`
- Used for maintaining access order

---

## 🧪 Test Scenarios

### Scenario 1: Basic LRU Cache Operations
```
1. Create cache with capacity 3
2. Put (1, "A"), (2, "B"), (3, "C")
3. Get(1) → "A" (moves to front)
4. Put (4, "D") → Evicts (2, "B") as LRU
5. Get(2) → null (evicted)
6. Get(3) → "C"
```

### Scenario 2: LFU Eviction Strategy
```
1. Create cache with LFU strategy, capacity 3
2. Put (1, "A"), (2, "B"), (3, "C")
3. Get(1) twice, Get(2) once
4. Put (4, "D") → Evicts (3, "C") as least frequent
```

### Scenario 3: FIFO Eviction Strategy
```
1. Create cache with FIFO strategy, capacity 3
2. Put (1, "A"), (2, "B"), (3, "C")
3. Get(1) multiple times
4. Put (4, "D") → Evicts (1, "A") as first in
```

### Scenario 4: Cache Statistics
```
1. Perform multiple get/put operations
2. Check hit count, miss count
3. Calculate hit rate
4. View eviction count
```

### Scenario 5: TTL Expiration
```
1. Put entry with 2-second TTL
2. Get immediately → Success
3. Wait 3 seconds
4. Get again → null (expired)
```

### Scenario 6: Capacity and Eviction
```
1. Create cache with capacity 5
2. Fill cache completely
3. Add 6th element → Triggers eviction
4. Verify oldest/LRU element removed
```

### Scenario 7: Edge Cases
```
1. Get from empty cache
2. Put with null key/value
3. Remove non-existent key
4. Put to cache with capacity 0
5. Update existing key
```

---

## ⏱️ Time Allocation (75-90 minutes)

- **10 mins:** Clarify requirements, understand LRU mechanics
- **15 mins:** Design data structures (HashMap + DoublyLinkedList)
- **10 mins:** Identify design patterns
- **40 mins:** Write code (model → strategy → service → main)
- **10 mins:** Test with demo, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: Data Structure Choice</summary>

Use **HashMap + Doubly Linked List**:
```java
class LRUCache<K, V> {
    private Map<K, CacheNode<K, V>> cache;  // O(1) lookup
    private DoublyLinkedList<K, V> list;     // O(1) insertion/deletion
    private int capacity;
}
```

HashMap provides O(1) get, DoublyLinkedList provides O(1) move to front/remove.
</details>

<details>
<summary>Hint 2: LRU Implementation</summary>

```java
public V get(K key) {
    if (!cache.containsKey(key)) {
        return null;  // Miss
    }
    
    CacheNode<K, V> node = cache.get(key);
    list.moveToFront(node);  // Mark as recently used
    return node.value;
}

public void put(K key, V value) {
    if (cache.containsKey(key)) {
        // Update existing
        CacheNode<K, V> node = cache.get(key);
        node.value = value;
        list.moveToFront(node);
    } else {
        // Add new
        if (cache.size() >= capacity) {
            evict();  // Remove LRU
        }
        CacheNode<K, V> newNode = new CacheNode<>(key, value);
        cache.put(key, newNode);
        list.addFirst(newNode);
    }
}

private void evict() {
    CacheNode<K, V> lru = list.removeLast();
    cache.remove(lru.key);
}
```
</details>

<details>
<summary>Hint 3: Doubly Linked List</summary>

```java
class DoublyLinkedList<K, V> {
    private CacheNode<K, V> head;
    private CacheNode<K, V> tail;
    
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
    }
    
    public void moveToFront(CacheNode<K, V> node) {
        remove(node);
        addFirst(node);
    }
    
    public CacheNode<K, V> removeLast() {
        if (tail == null) return null;
        CacheNode<K, V> node = tail;
        remove(node);
        return node;
    }
}
```
</details>

<details>
<summary>Hint 4: Eviction Strategy</summary>

```java
public interface EvictionStrategy<K, V> {
    void evict(LRUCache<K, V> cache);
    void onAccess(CacheNode<K, V> node);
}

public class LRUEviction<K, V> implements EvictionStrategy<K, V> {
    public void evict(LRUCache<K, V> cache) {
        // Remove least recently used (tail of list)
        CacheNode<K, V> lru = cache.getList().removeLast();
        cache.getCache().remove(lru.getKey());
    }
    
    public void onAccess(CacheNode<K, V> node) {
        // Move to front of list
        cache.getList().moveToFront(node);
    }
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Write-Through Cache**
   - Persist to backing store on write
   - Load from backing store on miss

2. **Write-Behind Cache**
   - Asynchronous writes to backing store
   - Batch updates

3. **Multi-Level Cache**
   - L1 (small, fast) + L2 (large, slower)
   - Automatic promotion/demotion

4. **Cache Warming**
   - Pre-load frequently accessed data
   - Background refresh

5. **Event Listeners**
   - Notify on eviction, expiration
   - Custom event handlers

6. **Distributed Cache**
   - Consistent hashing
   - Replication

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Strategy pattern for eviction policies
- [ ] Achieve O(1) time complexity for get and put
- [ ] Handle all test scenarios
- [ ] Maintain cache statistics
- [ ] Support multiple eviction strategies
- [ ] Handle edge cases (null, capacity 0, etc.)
- [ ] Be thread-safe (optional)
- [ ] Support TTL (optional)
- [ ] Have comprehensive demo with 7+ scenarios

---

## 📁 File Structure

```
23-LRU-Cache/
├── README.md (this file)
├── src/
│   ├── enums/
│   │   └── EvictionPolicy.java
│   ├── model/
│   │   ├── CacheNode.java
│   │   ├── DoublyLinkedList.java
│   │   └── CacheStatistics.java
│   ├── strategy/
│   │   ├── EvictionStrategy.java
│   │   ├── LRUEviction.java
│   │   ├── LFUEviction.java
│   │   ├── FIFOEviction.java
│   │   └── MRUEviction.java
│   ├── service/
│   │   └── LRUCache.java
│   └── Main.java
└── SOLUTION.md
```

---

**Good luck! This is a challenging problem that tests both data structures and design patterns! 🚀**

