package strategy;

import model.CacheNode;
import model.DoublyLinkedList;
import java.util.Map;

/**
 * Most Recently Used (MRU) eviction strategy
 * Evicts the most recently accessed item
 */
public class MRUEviction<K, V> implements EvictionStrategy<K, V> {
    
    @Override
    public CacheNode<K, V> evict(Map<K, CacheNode<K, V>> cache, DoublyLinkedList<K, V> list) {
        // Remove most recently used (head of list)
        CacheNode<K, V> mru = list.removeFirst();
        if (mru != null) {
            cache.remove(mru.getKey());
        }
        return mru;
    }
    
    @Override
    public void onAccess(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // Move to front (most recently used position)
        list.moveToFront(node);
        node.updateTimestamp();
    }
    
    @Override
    public void onInsert(CacheNode<K, V> node, DoublyLinkedList<K, V> list) {
        // Add to front
        list.addFirst(node);
    }
    
    @Override
    public String getStrategyName() {
        return "MRU (Most Recently Used)";
    }
}

