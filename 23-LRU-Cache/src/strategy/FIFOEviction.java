package strategy;

import model.CacheNode;
import model.DoublyLinkedList;
import java.util.Map;

/**
 * First In First Out (FIFO) eviction strategy
 * Evicts the oldest item (first inserted)
 */
public class FIFOEviction<K, V> implements EvictionStrategy<K, V> {
    
    @Override
    public CacheNode<K, V> evict(Map<K, CacheNode<K, V>> cache, DoublyLinkedList<K, V> list) {
        // Remove oldest (tail of list)
        CacheNode<K, V> oldest = list.removeLast();
        if (oldest != null) {
            cache.remove(oldest.getKey());
        }
        return oldest;
    }
    
    @Override
    public void onAccess(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // FIFO doesn't change order on access
        node.updateTimestamp();
    }
    
    @Override
    public void onInsert(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // Add to front (newest position)
        list.addFirst(node);
    }
    
    @Override
    public String getStrategyName() {
        return "FIFO (First In First Out)";
    }
}

