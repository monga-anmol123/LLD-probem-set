package model;

import enums.CuisineType;
import java.util.*;

/**
 * Represents a restaurant in the food delivery system
 */
public class Restaurant {
    private String restaurantId;
    private String name;
    private Address location;
    private CuisineType cuisineType;
    private double rating;
    private Map<String, MenuItem> menu;
    private boolean isOpen;
    
    public Restaurant(String restaurantId, String name, Address location, CuisineType cuisineType) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.location = location;
        this.cuisineType = cuisineType;
        this.rating = 4.0; // Default rating
        this.menu = new HashMap<>();
        this.isOpen = true;
    }
    
    public String getRestaurantId() {
        return restaurantId;
    }
    
    public String getName() {
        return name;
    }
    
    public Address getLocation() {
        return location;
    }
    
    public CuisineType getCuisineType() {
        return cuisineType;
    }
    
    public double getRating() {
        return rating;
    }
    
    public boolean isOpen() {
        return isOpen;
    }
    
    public void setOpen(boolean open) {
        this.isOpen = open;
    }
    
    public void addMenuItem(MenuItem item) {
        menu.put(item.getItemId(), item);
    }
    
    public MenuItem getMenuItem(String itemId) {
        return menu.get(itemId);
    }
    
    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu.values());
    }
    
    public void updateMenuItemAvailability(String itemId, boolean available) {
        MenuItem item = menu.get(itemId);
        if (item != null) {
            item.setAvailability(available);
        }
    }
    
    public void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.printf("║ %-58s ║%n", name + " - " + cuisineType);
        System.out.printf("║ Rating: %.1f ⭐ | Status: %-33s ║%n", rating, isOpen ? "OPEN" : "CLOSED");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        if (menu.isEmpty()) {
            System.out.println("║ No items in menu                                           ║");
        } else {
            for (MenuItem item : menu.values()) {
                System.out.printf("║ %-58s ║%n", item.toString());
            }
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - Rating: %.1f ⭐", name, cuisineType, rating);
    }
}

