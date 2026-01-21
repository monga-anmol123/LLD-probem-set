package model;

/**
 * Represents a node in the cache's doubly linked list
 * @param <K> Key type
 * @param <V> Value type
 */
public class CacheNode<K, V> {
    private K key;
    private V value;
    private int frequency;
    private long timestamp;
    private Long expiryTime;  // null means no expiry
    
    public CacheNode<K, V> prev;
    public CacheNode<K, V> next;
    
    public CacheNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.frequency = 1;
        this.timestamp = System.currentTimeMillis();
        this.expiryTime = null;
    }
    
    public CacheNode(K key, V value, long ttlMillis) {
        this(key, value);
        if (ttlMillis > 0) {
            this.expiryTime = System.currentTimeMillis() + ttlMillis;
        }
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
    
    public int getFrequency() {
        return frequency;
    }
    
    public void incrementFrequency() {
        this.frequency++;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public boolean isExpired() {
        if (expiryTime == null) {
            return false;
        }
        return System.currentTimeMillis() > expiryTime;
    }
    
    public Long getExpiryTime() {
        return expiryTime;
    }
    
    @Override
    public String toString() {
        return String.format("[%s=%s, freq=%d]", key, value, frequency);
    }
}

