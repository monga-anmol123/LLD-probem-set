package service;

import enums.EvictionPolicy;

/**
 * CacheManager - Singleton for managing cache instances.
 */
public class CacheManager {
    
    private static CacheManager instance;
    private Cache<String, String> defaultCache;
    
    private CacheManager() {
        // Private constructor for singleton
    }
    
    /**
     * Get singleton instance.
     */
    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }
    
    /**
     * Get or create default cache.
     */
    public Cache<String, String> getDefaultCache() {
        if (defaultCache == null) {
            defaultCache = new CacheImpl<>(100, EvictionPolicy.LRU);
        }
        return defaultCache;
    }
    
    /**
     * Create a new cache with specified capacity and policy.
     */
    public <K, V> Cache<K, V> createCache(int capacity, EvictionPolicy policy) {
        return new CacheImpl<>(capacity, policy);
    }
}
