package service;

import model.CacheStatistics;

/**
 * Cache interface defining core cache operations.
 */
public interface Cache<K, V> {
    
    /**
     * Get value by key.
     * Returns null if key not found or expired.
     */
    V get(K key);
    
    /**
     * Put key-value pair into cache.
     */
    void put(K key, V value);
    
    /**
     * Put key-value pair with TTL (time to live in milliseconds).
     */
    void put(K key, V value, long ttlMillis);
    
    /**
     * Delete key from cache.
     */
    void delete(K key);
    
    /**
     * Clear entire cache.
     */
    void clear();
    
    /**
     * Get current cache size.
     */
    int size();
    
    /**
     * Get maximum capacity.
     */
    int capacity();
    
    /**
     * Check if key exists in cache.
     */
    boolean containsKey(K key);
    
    /**
     * Get cache statistics.
     */
    CacheStatistics getStatistics();
}
