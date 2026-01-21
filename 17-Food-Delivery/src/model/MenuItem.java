package model;

import enums.MenuCategory;

/**
 * Represents a menu item in a restaurant
 */
public class MenuItem {
    private String itemId;
    private String name;
    private double price;
    private MenuCategory category;
    private boolean isAvailable;
    private String description;
    
    public MenuItem(String itemId, String name, double price, MenuCategory category, String description) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.isAvailable = true;
    }
    
    public String getItemId() {
        return itemId;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public MenuCategory getCategory() {
        return category;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailability(boolean available) {
        this.isAvailable = available;
    }
    
    public void updatePrice(double newPrice) {
        this.price = newPrice;
    }
    
    @Override
    public String toString() {
        String status = isAvailable ? "✓" : "✗";
        return String.format("[%s] %s - $%.2f (%s) %s", 
            status, name, price, category, description);
    }
}

