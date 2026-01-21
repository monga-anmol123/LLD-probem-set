package strategy;

import model.CacheNode;
import model.DoublyLinkedList;
import java.util.Map;

/**
 * Least Frequently Used (LFU) eviction strategy
 * Evicts the least frequently accessed item
 */
public class LFUEviction<K, V> implements EvictionStrategy<K, V> {
    
    @Override
    public CacheNode<K, V> evict(Map<K, CacheNode<K, V>> cache, DoublyLinkedList<K, V> list) {
        // Find node with minimum frequency
        CacheNode<K, V> current = list.getHead();
        CacheNode<K, V> lfu = current;
        int minFreq = Integer.MAX_VALUE;
        
        while (current != null) {
            if (current.getFrequency() < minFreq) {
                minFreq = current.getFrequency();
                lfu = current;
            }
            current = current.next;
        }
        
        if (lfu != null) {
            list.remove(lfu);
            cache.remove(lfu.getKey());
        }
        
        return lfu;
    }
    
    @Override
    public void onAccess(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // Increment frequency
        node.incrementFrequency();
        node.updateTimestamp();
    }
    
    @Override
    public void onInsert(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // Add to list (position doesn't matter for LFU)
        list.addFirst(node);
    }
    
    @Override
    public String getStrategyName() {
        return "LFU (Least Frequently Used)";
    }
}

