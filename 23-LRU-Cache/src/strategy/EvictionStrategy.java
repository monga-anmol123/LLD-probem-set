package strategy;

import model.CacheNode;
import model.DoublyLinkedList;
import java.util.Map;

/**
 * Strategy interface for cache eviction policies
 * Implements Strategy Design Pattern
 */
public interface EvictionStrategy<K, V> {
    /**
     * Evict one entry from the cache
     * @param cache The cache map
     * @param list The doubly linked list maintaining order
     * @return The evicted node
     */
    CacheNode<K, V> evict(Map<K, CacheNode<K, V>> cache, DoublyLinkedList<K, V> list);
    
    /**
     * Called when a cache entry is accessed
     * @param node The accessed node
     * @param list The doubly linked list
     */
    void onAccess(CacheNode<K, V> node, DoublyLinkedList<K, V> list);
    
    /**
     * Called when a new entry is inserted
     * @param node The inserted node
     * @param list The doubly linked list
     */
    void onInsert(CacheNode<K, V> node, DoublyLinkedList<K, V> list);
    
    /**
     * Get the name of this eviction strategy
     */
    String getStrategyName();
}

