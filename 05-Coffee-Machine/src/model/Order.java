package model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private static int orderCounter = 1;
    
    private final String orderId;
    private final List<OrderItem> items;
    
    public Order() {
        this.orderId = "ORD-" + String.format("%04d", orderCounter++);
        this.items = new ArrayList<>();
    }
    
    public void addItem(Coffee coffee, double price) {
        items.add(new OrderItem(coffee, price));
    }
    
    public double getTotalPrice() {
        return items.stream()
                   .mapToDouble(OrderItem::getPrice)
                   .sum();
    }
    
    public void displayOrderSummary() {
        System.out.println("\n========================================");
        System.out.println("  ORDER SUMMARY - " + orderId);
        System.out.println("========================================");
        
        int itemNum = 1;
        for (OrderItem item : items) {
            System.out.printf("%d. %s\n", itemNum++, item.getCoffee().getDescription());
            System.out.printf("   Price: $%.2f\n\n", item.getPrice());
        }
        
        System.out.println("----------------------------------------");
        System.out.printf("TOTAL: $%.2f\n", getTotalPrice());
        System.out.println("========================================\n");
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }
    
    // Inner class for order items
    public static class OrderItem {
        private final Coffee coffee;
        private final double price;
        
        public OrderItem(Coffee coffee, double price) {
            this.coffee = coffee;
            this.price = price;
        }
        
        public Coffee getCoffee() {
            return coffee;
        }
        
        public double getPrice() {
            return price;
        }
    }
}


