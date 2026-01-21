package model;

/**
 * CacheEntry represents a single entry in the cache with metadata.
 */
public class CacheEntry<K, V> {
    private final K key;
    private V value;
    private long timestamp;
    private int accessCount;
    private long expirationTime; // 0 means no expiration

    public CacheEntry(K key, V value) {
        this(key, value, 0);
    }

    public CacheEntry(K key, V value, long ttlMillis) {
        this.key = key;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
        this.accessCount = 0;
        this.expirationTime = ttlMillis > 0 ? System.currentTimeMillis() + ttlMillis : 0;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public int getAccessCount() {
        return accessCount;
    }

    public void incrementAccessCount() {
        this.accessCount++;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long ttlMillis) {
        this.expirationTime = ttlMillis > 0 ? System.currentTimeMillis() + ttlMillis : 0;
    }

    /**
     * Check if this entry has expired.
     */
    public boolean isExpired() {
        return expirationTime > 0 && System.currentTimeMillis() > expirationTime;
    }

    /**
     * Touch this entry (update timestamp and increment access count).
     */
    public void touch() {
        updateTimestamp();
        incrementAccessCount();
    }

    @Override
    public String toString() {
        return "CacheEntry{key=" + key + ", value=" + value + 
               ", accessCount=" + accessCount + ", expired=" + isExpired() + "}";
    }
}
