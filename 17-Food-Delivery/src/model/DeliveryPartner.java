package model;

/**
 * Represents a delivery partner in the food delivery system
 */
public class DeliveryPartner {
    private String partnerId;
    private String name;
    private String phone;
    private Address currentLocation;
    private boolean isAvailable;
    private double rating;
    private int totalDeliveries;
    
    public DeliveryPartner(String partnerId, String name, String phone, Address currentLocation) {
        this.partnerId = partnerId;
        this.name = name;
        this.phone = phone;
        this.currentLocation = currentLocation;
        this.isAvailable = true;
        this.rating = 4.5; // Default rating
        this.totalDeliveries = 0;
    }
    
    public String getPartnerId() {
        return partnerId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public Address getCurrentLocation() {
        return currentLocation;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    
    public double getRating() {
        return rating;
    }
    
    public int getTotalDeliveries() {
        return totalDeliveries;
    }
    
    public void updateLocation(Address newLocation) {
        this.currentLocation = newLocation;
    }
    
    public void acceptOrder() {
        this.isAvailable = false;
        System.out.println("✓ Delivery partner " + name + " accepted the order");
    }
    
    public void completeDelivery() {
        this.isAvailable = true;
        this.totalDeliveries++;
        System.out.println("✓ Delivery partner " + name + " completed the delivery");
    }
    
    @Override
    public String toString() {
        return String.format("%s (ID: %s) - Rating: %.1f ⭐ | Deliveries: %d | %s", 
            name, partnerId, rating, totalDeliveries, isAvailable ? "Available" : "Busy");
    }
}

