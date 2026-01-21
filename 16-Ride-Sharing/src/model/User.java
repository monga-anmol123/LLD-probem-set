package model;

public abstract class User {
    protected final String userId;
    protected final String name;
    protected final String phone;
    protected double rating;
    protected int totalRatings;
    
    public User(String userId, String name, String phone) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.rating = 5.0; // Default rating
        this.totalRatings = 0;
    }
    
    public void updateRating(double newRating) {
        totalRatings++;
        rating = ((rating * (totalRatings - 1)) + newRating) / totalRatings;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public double getRating() {
        return rating;
    }
    
    public int getTotalRatings() {
        return totalRatings;
    }
}

