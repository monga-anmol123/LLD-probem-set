package model;

import java.util.*;

/**
 * Represents a shopping cart for a customer
 */
public class Cart {
    private Map<MenuItem, Integer> items;
    
    public Cart() {
        this.items = new HashMap<>();
    }
    
    public void addItem(MenuItem item, int quantity) {
        if (!item.isAvailable()) {
            throw new IllegalStateException("Item is not available: " + item.getName());
        }
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }
    
    public void removeItem(MenuItem item) {
        items.remove(item);
    }
    
    public void updateQuantity(MenuItem item, int quantity) {
        if (quantity <= 0) {
            removeItem(item);
        } else {
            items.put(item, quantity);
        }
    }
    
    public Map<MenuItem, Integer> getItems() {
        return new HashMap<>(items);
    }
    
    public double getSubtotal() {
        return items.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            .sum();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public void clear() {
        items.clear();
    }
    
    public void displayCart() {
        System.out.println("\n┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                      SHOPPING CART                      │");
        System.out.println("├─────────────────────────────────────────────────────────┤");
        
        if (items.isEmpty()) {
            System.out.println("│ Cart is empty                                           │");
        } else {
            for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
                MenuItem item = entry.getKey();
                int quantity = entry.getValue();
                double itemTotal = item.getPrice() * quantity;
                System.out.printf("│ %-30s x%-3d $%-15.2f │%n", 
                    item.getName(), quantity, itemTotal);
            }
            System.out.println("├─────────────────────────────────────────────────────────┤");
            System.out.printf("│ %-40s $%-15.2f │%n", "Subtotal:", getSubtotal());
        }
        
        System.out.println("└─────────────────────────────────────────────────────────┘");
    }
}

