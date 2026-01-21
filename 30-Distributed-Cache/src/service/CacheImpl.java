package service;

import enums.EvictionPolicy;
import model.CacheEntry;
import model.CacheStatistics;
import strategy.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Main cache implementation with pluggable eviction strategies.
 */
public class CacheImpl<K, V> implements Cache<K, V> {
    
    private final int capacity;
    private final Map<K, CacheEntry<K, V>> cache;
    private final EvictionStrategy<K, V> evictionStrategy;
    private final CacheStatistics statistics;
    private final EvictionPolicy policy;
    
    public CacheImpl(int capacity, EvictionPolicy policy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.statistics = new CacheStatistics();
        this.policy = policy;
        this.evictionStrategy = createEvictionStrategy(policy);
    }
    
    @Override
    public synchronized V get(K key) {
        if (key == null) {
            statistics.recordMiss();
            return null;
        }
        
        CacheEntry<K, V> entry = cache.get(key);
        
        if (entry == null) {
            statistics.recordMiss();
            return null;
        }
        
        // Check if expired
        if (entry.isExpired()) {
            cache.remove(key);
            evictionStrategy.onDelete(key);
            statistics.recordMiss();
            statistics.recordExpiration();
            return null;
        }
        
        // Update entry metadata
        entry.touch();
        evictionStrategy.onGet(key);
        statistics.recordHit();
        
        return entry.getValue();
    }
    
    @Override
    public synchronized void put(K key, V value) {
        put(key, value, 0);
    }
    
    @Override
    public synchronized void put(K key, V value, long ttlMillis) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value cannot be null");
        }
        
        // Update existing entry
        if (cache.containsKey(key)) {
            CacheEntry<K, V> entry = cache.get(key);
            entry.setValue(value);
            entry.updateTimestamp();
            if (ttlMillis > 0) {
                entry.setExpirationTime(ttlMillis);
            }
            evictionStrategy.onPut(key, value);
            return;
        }
        
        // Evict if at capacity
        if (cache.size() >= capacity) {
            K keyToEvict = evictionStrategy.evict();
            if (keyToEvict != null) {
                cache.remove(keyToEvict);
                statistics.recordEviction();
            }
        }
        
        // Add new entry
        CacheEntry<K, V> newEntry = new CacheEntry<>(key, value, ttlMillis);
        cache.put(key, newEntry);
        evictionStrategy.onPut(key, value);
    }
    
    @Override
    public synchronized void delete(K key) {
        if (key == null) {
            return;
        }
        
        if (cache.containsKey(key)) {
            cache.remove(key);
            evictionStrategy.onDelete(key);
        }
    }
    
    @Override
    public synchronized void clear() {
        cache.clear();
        evictionStrategy.clear();
        statistics.reset();
    }
    
    @Override
    public synchronized int size() {
        return cache.size();
    }
    
    @Override
    public int capacity() {
        return capacity;
    }
    
    @Override
    public synchronized boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        
        CacheEntry<K, V> entry = cache.get(key);
        if (entry == null) {
            return false;
        }
        
        // Check if expired
        if (entry.isExpired()) {
            cache.remove(key);
            evictionStrategy.onDelete(key);
            statistics.recordExpiration();
            return false;
        }
        
        return true;
    }
    
    @Override
    public CacheStatistics getStatistics() {
        return statistics;
    }
    
    /**
     * Get the eviction policy used by this cache.
     */
    public EvictionPolicy getPolicy() {
        return policy;
    }
    
    /**
     * Create eviction strategy based on policy.
     */
    private EvictionStrategy<K, V> createEvictionStrategy(EvictionPolicy policy) {
        switch (policy) {
            case LRU:
                return new LRUEvictionStrategy<>();
            case LFU:
                return new LFUEvictionStrategy<>();
            case FIFO:
                return new FIFOEvictionStrategy<>();
            case TTL:
                return new TTLEvictionStrategy<>(5000); // 5 second default TTL
            default:
                throw new IllegalArgumentException("Unsupported eviction policy: " + policy);
        }
    }
    
    /**
     * Display cache contents (for debugging).
     */
    public synchronized void display() {
        System.out.println("\n=== Cache Contents ===");
        System.out.println("Policy: " + policy);
        System.out.println("Size: " + size() + "/" + capacity);
        System.out.println("Entries:");
        
        for (Map.Entry<K, CacheEntry<K, V>> entry : cache.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue().getValue());
        }
        
        System.out.println(statistics);
        System.out.println("===================\n");
    }
}
