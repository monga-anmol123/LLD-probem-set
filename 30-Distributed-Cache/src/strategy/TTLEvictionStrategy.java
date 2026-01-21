package strategy;

import java.util.*;

/**
 * TTL (Time To Live) eviction strategy.
 * Evicts expired entries based on timestamp.
 */
public class TTLEvictionStrategy<K, V> implements EvictionStrategy<K, V> {
    
    private final Map<K, Long> keyToExpiration;
    private final long defaultTTLMillis;
    
    public TTLEvictionStrategy(long defaultTTLMillis) {
        this.keyToExpiration = new HashMap<>();
        this.defaultTTLMillis = defaultTTLMillis;
    }
    
    @Override
    public void onGet(K key) {
        // Check if expired
        if (isExpired(key)) {
            keyToExpiration.remove(key);
        }
    }
    
    @Override
    public void onPut(K key, V value) {
        long expirationTime = System.currentTimeMillis() + defaultTTLMillis;
        keyToExpiration.put(key, expirationTime);
    }
    
    @Override
    public void onDelete(K key) {
        keyToExpiration.remove(key);
    }
    
    @Override
    public K evict() {
        // Find first expired key
        long now = System.currentTimeMillis();
        for (Map.Entry<K, Long> entry : keyToExpiration.entrySet()) {
            if (entry.getValue() <= now) {
                K key = entry.getKey();
                keyToExpiration.remove(key);
                return key;
            }
        }
        
        // If no expired keys, evict oldest
        if (!keyToExpiration.isEmpty()) {
            K oldestKey = keyToExpiration.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
            keyToExpiration.remove(oldestKey);
            return oldestKey;
        }
        
        return null;
    }
    
    @Override
    public void clear() {
        keyToExpiration.clear();
    }
    
    @Override
    public String getName() {
        return "TTL (Time To Live)";
    }
    
    /**
     * Check if a key has expired.
     */
    public boolean isExpired(K key) {
        if (!keyToExpiration.containsKey(key)) {
            return false;
        }
        return System.currentTimeMillis() > keyToExpiration.get(key);
    }
    
    /**
     * Clean up all expired keys.
     */
    public List<K> cleanupExpired() {
        List<K> expiredKeys = new ArrayList<>();
        long now = System.currentTimeMillis();
        
        Iterator<Map.Entry<K, Long>> iterator = keyToExpiration.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, Long> entry = iterator.next();
            if (entry.getValue() <= now) {
                expiredKeys.add(entry.getKey());
                iterator.remove();
            }
        }
        
        return expiredKeys;
    }
}
