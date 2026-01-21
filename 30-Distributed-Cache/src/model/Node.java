package model;

/**
 * Node for doubly linked list used in LRU cache.
 */
public class Node<K, V> {
    public K key;
    public V value;
    public Node<K, V> prev;
    public Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
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
}
