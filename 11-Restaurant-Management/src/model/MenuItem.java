package model;

import enums.MenuCategory;

public class MenuItem {
    private String itemId;
    private String name;
    private MenuCategory category;
    private double price;
    private int preparationTimeMinutes;
    private boolean available;
    
    public MenuItem(String itemId, String name, MenuCategory category, 
                   double price, int preparationTimeMinutes) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.preparationTimeMinutes = preparationTimeMinutes;
        this.available = true;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public void updatePrice(double newPrice) {
        this.price = newPrice;
    }
    
    // Getters
    public String getItemId() {
        return itemId;
    }
    
    public String getName() {
        return name;
    }
    
    public MenuCategory getCategory() {
        return category;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getPreparationTimeMinutes() {
        return preparationTimeMinutes;
    }
    
    @Override
    public String toString() {
        return "MenuItem{" +
                "id='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price=$" + String.format("%.2f", price) +
                ", prepTime=" + preparationTimeMinutes + "min" +
                ", available=" + available +
                '}';
    }
}


