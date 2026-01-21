package strategy;

import model.CacheNode;
import model.DoublyLinkedList;
import java.util.Map;

/**
 * Least Recently Used (LRU) eviction strategy
 * Evicts the least recently accessed item
 */
public class LRUEviction<K, V> implements EvictionStrategy<K, V> {
    
    @Override
    public CacheNode<K, V> evict(Map<K, CacheNode<K, V>> cache, DoublyLinkedList<K, V> list) {
        // Remove least recently used (tail of list)
        CacheNode<K, V> lru = list.removeLast();
        if (lru != null) {
            cache.remove(lru.getKey());
        }
        return lru;
    }
    
    @Override
    public void onAccess(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // Move to front (most recently used position)
        list.moveToFront(node);
        node.updateTimestamp();
    }
    
    @Override
    public void onInsert(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // Add to front (most recently used position)
        list.addFirst(node);
    }
    
    @Override
    public String getStrategyName() {
        return "LRU (Least Recently Used)";
    }
}

