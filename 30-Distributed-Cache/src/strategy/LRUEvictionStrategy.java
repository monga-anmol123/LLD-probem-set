package strategy;

import model.Node;
import java.util.HashMap;
import java.util.Map;

/**
 * LRU (Least Recently Used) eviction strategy using doubly linked list.
 * Time Complexity: O(1) for all operations
 */
public class LRUEvictionStrategy<K, V> implements EvictionStrategy<K, V> {
    
    private final Map<K, Node<K, V>> nodeMap;
    private final Node<K, V> head; // Most recently used
    private final Node<K, V> tail; // Least recently used
    
    public LRUEvictionStrategy() {
        this.nodeMap = new HashMap<>();
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }
    
    @Override
    public void onGet(K key) {
        if (nodeMap.containsKey(key)) {
            Node<K, V> node = nodeMap.get(key);
            moveToHead(node);
        }
    }
    
    @Override
    public void onPut(K key, V value) {
        if (nodeMap.containsKey(key)) {
            Node<K, V> node = nodeMap.get(key);
            node.setValue(value);
            moveToHead(node);
        } else {
            Node<K, V> newNode = new Node<>(key, value);
            nodeMap.put(key, newNode);
            addToHead(newNode);
        }
    }
    
    @Override
    public void onDelete(K key) {
        if (nodeMap.containsKey(key)) {
            Node<K, V> node = nodeMap.get(key);
            removeNode(node);
            nodeMap.remove(key);
        }
    }
    
    @Override
    public K evict() {
        if (tail.prev == head) {
            return null; // Empty list
        }
        Node<K, V> lruNode = tail.prev;
        removeNode(lruNode);
        nodeMap.remove(lruNode.getKey());
        return lruNode.getKey();
    }
    
    @Override
    public void clear() {
        nodeMap.clear();
        head.next = tail;
        tail.prev = head;
    }
    
    @Override
    public String getName() {
        return "LRU (Least Recently Used)";
    }
    
    /**
     * Add node right after head (most recently used position).
     */
    private void addToHead(Node<K, V> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    
    /**
     * Remove node from its current position.
     */
    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    /**
     * Move node to head (mark as recently used).
     */
    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }
}
