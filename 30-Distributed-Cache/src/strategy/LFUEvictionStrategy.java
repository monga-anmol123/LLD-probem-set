package strategy;

import java.util.*;

/**
 * LFU (Least Frequently Used) eviction strategy.
 * Uses frequency map to track access counts.
 * Time Complexity: O(1) for get/put operations
 */
public class LFUEvictionStrategy<K, V> implements EvictionStrategy<K, V> {
    
    private final Map<K, Integer> keyToFrequency;
    private final Map<Integer, LinkedHashSet<K>> frequencyToKeys;
    private int minFrequency;
    
    public LFUEvictionStrategy() {
        this.keyToFrequency = new HashMap<>();
        this.frequencyToKeys = new HashMap<>();
        this.minFrequency = 0;
    }
    
    @Override
    public void onGet(K key) {
        if (!keyToFrequency.containsKey(key)) {
            return;
        }
        updateFrequency(key);
    }
    
    @Override
    public void onPut(K key, V value) {
        if (keyToFrequency.containsKey(key)) {
            updateFrequency(key);
        } else {
            keyToFrequency.put(key, 1);
            frequencyToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
            minFrequency = 1;
        }
    }
    
    @Override
    public void onDelete(K key) {
        if (!keyToFrequency.containsKey(key)) {
            return;
        }
        
        int frequency = keyToFrequency.get(key);
        frequencyToKeys.get(frequency).remove(key);
        
        if (frequencyToKeys.get(frequency).isEmpty()) {
            frequencyToKeys.remove(frequency);
        }
        
        keyToFrequency.remove(key);
    }
    
    @Override
    public K evict() {
        if (minFrequency == 0 || !frequencyToKeys.containsKey(minFrequency)) {
            return null;
        }
        
        LinkedHashSet<K> keys = frequencyToKeys.get(minFrequency);
        K keyToEvict = keys.iterator().next();
        keys.remove(keyToEvict);
        
        if (keys.isEmpty()) {
            frequencyToKeys.remove(minFrequency);
        }
        
        keyToFrequency.remove(keyToEvict);
        return keyToEvict;
    }
    
    @Override
    public void clear() {
        keyToFrequency.clear();
        frequencyToKeys.clear();
        minFrequency = 0;
    }
    
    @Override
    public String getName() {
        return "LFU (Least Frequently Used)";
    }
    
    /**
     * Update frequency of a key.
     */
    private void updateFrequency(K key) {
        int oldFrequency = keyToFrequency.get(key);
        keyToFrequency.put(key, oldFrequency + 1);
        
        frequencyToKeys.get(oldFrequency).remove(key);
        
        if (frequencyToKeys.get(oldFrequency).isEmpty()) {
            frequencyToKeys.remove(oldFrequency);
            if (minFrequency == oldFrequency) {
                minFrequency++;
            }
        }
        
        frequencyToKeys.computeIfAbsent(oldFrequency + 1, k -> new LinkedHashSet<>()).add(key);
    }
}
