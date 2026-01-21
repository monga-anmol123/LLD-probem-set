package service;

import model.*;
import strategy.*;
import java.util.*;

/**
 * Generic LRU Cache implementation with pluggable eviction strategies
 * Provides O(1) time complexity for get and put operations
 * @param <K> Key type
 * @param <V> Value type
 */
public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, CacheNode<K, V>> cache;
    private final DoublyLinkedList<K, V> list;
    private final EvictionStrategy<K, V> evictionStrategy;
    private final CacheStatistics statistics;
    
    /**
     * Create cache with default LRU eviction strategy
     */
    public LRUCache(int capacity) {
        this(capacity, new LRUEviction<>());
    }
    
    /**
     * Create cache with custom eviction strategy
     */
    public LRUCache(int capacity, EvictionStrategy<K, V> evictionStrategy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.list = new DoublyLinkedList<>();
        this.evictionStrategy = evictionStrategy;
        this.statistics = new CacheStatistics();
    }
    
    /**
     * Get value for key
     * Time Complexity: O(1)
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        CacheNode<K, V> node = cache.get(key);
        
        if (node == null) {
            statistics.recordMiss();
            return null;
        }
        
        // Check if expired
        if (node.isExpired()) {
            remove(key);
            statistics.recordExpiration();
            statistics.recordMiss();
            return null;
        }
        
        // Update access order
        evictionStrategy.onAccess(node, list);
        statistics.recordHit();
        
        return node.getValue();
    }
    
    /**
     * Put key-value pair
     * Time Complexity: O(1) for LRU, O(n) for LFU
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        CacheNode<K, V> existingNode = cache.get(key);
        
        if (existingNode != null) {
            // Update existing entry
            existingNode.setValue(value);
            evictionStrategy.onAccess(existingNode, list);
        } else {
            // Add new entry
            if (cache.size() >= capacity) {
                evict();
            }
            
            CacheNode<K, V> newNode = new CacheNode<>(key, value);
            cache.put(key, newNode);
            evictionStrategy.onInsert(newNode, list);
        }
        
        statistics.recordPut();
    }
    
    /**
     * Put key-value pair with TTL (Time To Live)
     */
    public void put(K key, V value, long ttlMillis) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        CacheNode<K, V> existingNode = cache.get(key);
        
        if (existingNode != null) {
            // Update existing entry
            existingNode.setValue(value);
            evictionStrategy.onAccess(existingNode, list);
        } else {
            // Add new entry
            if (cache.size() >= capacity) {
                evict();
            }
            
            CacheNode<K, V> newNode = new CacheNode<>(key, value, ttlMillis);
            cache.put(key, newNode);
            evictionStrategy.onInsert(newNode, list);
        }
        
        statistics.recordPut();
    }
    
    /**
     * Remove key from cache
     * Time Complexity: O(1)
     */
    public boolean remove(K key) {
        if (key == null) {
            return false;
        }
        
        CacheNode<K, V> node = cache.remove(key);
        
        if (node != null) {
            list.remove(node);
            statistics.recordRemove();
            return true;
        }
        
        return false;
    }
    
    /**
     * Clear all entries from cache
     */
    public void clear() {
        cache.clear();
        // Clear list by removing all nodes
        while (!list.isEmpty()) {
            list.removeFirst();
        }
    }
    
    /**
     * Check if key exists in cache
     */
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        
        CacheNode<K, V> node = cache.get(key);
        
        if (node != null && node.isExpired()) {
            remove(key);
            statistics.recordExpiration();
            return false;
        }
        
        return node != null;
    }
    
    /**
     * Get current size of cache
     */
    public int size() {
        return cache.size();
    }
    
    /**
     * Check if cache is empty
     */
    public boolean isEmpty() {
        return cache.isEmpty();
    }
    
    /**
     * Get cache capacity
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Get cache statistics
     */
    public CacheStatistics getStatistics() {
        return statistics;
    }
    
    /**
     * Get eviction strategy name
     */
    public String getEvictionStrategyName() {
        return evictionStrategy.getStrategyName();
    }
    
    /**
     * Evict one entry based on eviction strategy
     */
    private void evict() {
        CacheNode<K, V> evicted = evictionStrategy.evict(cache, list);
        if (evicted != null) {
            statistics.recordEviction();
            System.out.println("⚠ Evicted: " + evicted);
        }
    }
    
    /**
     * Display cache contents
     */
    public void display() {
        System.out.println("\n┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                    CACHE CONTENTS                       │");
        System.out.println("├─────────────────────────────────────────────────────────┤");
        System.out.printf("│ Capacity: %d | Size: %d | Strategy: %-20s │%n", 
            capacity, size(), evictionStrategy.getStrategyName());
        System.out.println("├─────────────────────────────────────────────────────────┤");
        
        if (cache.isEmpty()) {
            System.out.println("│ Cache is empty                                          │");
        } else {
            CacheNode<K, V> current = list.getHead();
            int count = 1;
            while (current != null) {
                System.out.printf("│ %2d. %-52s │%n", count++, current.toString());
                current = current.next;
            }
        }
        
        System.out.println("└─────────────────────────────────────────────────────────┘");
    }
    
    /**
     * Get all keys in cache
     */
    public Set<K> keySet() {
        return new HashSet<>(cache.keySet());
    }
    
    /**
     * Get all values in cache
     */
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (CacheNode<K, V> node : cache.values()) {
            if (!node.isExpired()) {
                values.add(node.getValue());
            }
        }
        return values;
    }
}

