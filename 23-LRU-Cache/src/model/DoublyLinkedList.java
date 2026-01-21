package model;

/**
 * Doubly linked list for maintaining cache order
 * Provides O(1) operations for add, remove, and move operations
 * @param <K> Key type
 * @param <V> Value type
 */
public class DoublyLinkedList<K, V> {
    private CacheNode<K, V> head;
    private CacheNode<K, V> tail;
    private int size;
    
    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    
    /**
     * Add node to the front of the list (most recently used position)
     */
    public void addFirst(CacheNode<K, V> node) {
        if (node == null) return;
        
        node.next = head;
        node.prev = null;
        
        if (head != null) {
            head.prev = node;
        }
        
        head = node;
        
        if (tail == null) {
            tail = node;
        }
        
        size++;
    }
    
    /**
     * Add node to the end of the list (least recently used position)
     */
    public void addLast(CacheNode<K, V> node) {
        if (node == null) return;
        
        node.prev = tail;
        node.next = null;
        
        if (tail != null) {
            tail.next = node;
        }
        
        tail = node;
        
        if (head == null) {
            head = node;
        }
        
        size++;
    }
    
    /**
     * Remove node from the list
     */
    public void remove(CacheNode<K, V> node) {
        if (node == null) return;
        
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        
        node.prev = null;
        node.next = null;
        size--;
    }
    
    /**
     * Move node to the front of the list (mark as most recently used)
     */
    public void moveToFront(CacheNode<K, V> node) {
        if (node == null || node == head) return;
        
        remove(node);
        addFirst(node);
    }
    
    /**
     * Move node to the end of the list (mark as least recently used)
     */
    public void moveToEnd(CacheNode<K, V> node) {
        if (node == null || node == tail) return;
        
        remove(node);
        addLast(node);
    }
    
    /**
     * Remove and return the last node (least recently used)
     */
    public CacheNode<K, V> removeLast() {
        if (tail == null) return null;
        
        CacheNode<K, V> node = tail;
        remove(node);
        return node;
    }
    
    /**
     * Remove and return the first node (most recently used)
     */
    public CacheNode<K, V> removeFirst() {
        if (head == null) return null;
        
        CacheNode<K, V> node = head;
        remove(node);
        return node;
    }
    
    public CacheNode<K, V> getHead() {
        return head;
    }
    
    public CacheNode<K, V> getTail() {
        return tail;
    }
    
    public int getSize() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Display the list from head to tail
     */
    public void display() {
        CacheNode<K, V> current = head;
        System.out.print("HEAD → ");
        while (current != null) {
            System.out.print(current + " → ");
            current = current.next;
        }
        System.out.println("TAIL");
    }
}

