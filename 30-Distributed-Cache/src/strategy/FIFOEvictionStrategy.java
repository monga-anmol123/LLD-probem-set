package strategy;

import java.util.LinkedList;
import java.util.Queue;

/**
 * FIFO (First In First Out) eviction strategy.
 * Evicts the oldest entry.
 * Time Complexity: O(1) for all operations
 */
public class FIFOEvictionStrategy<K, V> implements EvictionStrategy<K, V> {
    
    private final Queue<K> queue;
    
    public FIFOEvictionStrategy() {
        this.queue = new LinkedList<>();
    }
    
    @Override
    public void onGet(K key) {
        // FIFO doesn't care about access patterns
    }
    
    @Override
    public void onPut(K key, V value) {
        if (!queue.contains(key)) {
            queue.offer(key);
        }
    }
    
    @Override
    public void onDelete(K key) {
        queue.remove(key);
    }
    
    @Override
    public K evict() {
        return queue.poll();
    }
    
    @Override
    public void clear() {
        queue.clear();
    }
    
    @Override
    public String getName() {
        return "FIFO (First In First Out)";
    }
}
