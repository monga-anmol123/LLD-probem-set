package model;

public class Guest {
    private final String guestId;
    private final String name;
    private final String email;
    private final String phone;
    private int loyaltyPoints;
    
    public Guest(String guestId, String name, String email, String phone) {
        this.guestId = guestId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.loyaltyPoints = 0;
    }
    
    public String getGuestId() {
        return guestId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
    
    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }
    
    public boolean hasLoyaltyStatus() {
        return loyaltyPoints >= 100;
    }
    
    @Override
    public String toString() {
        return String.format("Guest[%s]: %s (%s) - Loyalty: %d pts", 
            guestId, name, email, loyaltyPoints);
    }
}
