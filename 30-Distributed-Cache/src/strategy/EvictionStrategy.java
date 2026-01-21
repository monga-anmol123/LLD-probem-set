package strategy;

import model.CacheEntry;
import java.util.Map;

/**
 * Strategy interface for cache eviction policies.
 */
public interface EvictionStrategy<K, V> {
    
    /**
     * Called when a key is accessed (get operation).
     */
    void onGet(K key);
    
    /**
     * Called when a key-value pair is added (put operation).
     */
    void onPut(K key, V value);
    
    /**
     * Called when a key is deleted.
     */
    void onDelete(K key);
    
    /**
     * Evict one entry and return the key to be removed.
     * Returns null if no eviction is possible.
     */
    K evict();
    
    /**
     * Clear all tracking data.
     */
    void clear();
    
    /**
     * Get the name of this eviction strategy.
     */
    String getName();
}
