package model;

import observer.Observer;
import java.util.HashMap;
import java.util.Map;

public class User implements Observer {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private Map<User, Double> balances; // Positive = they owe me, Negative = I owe them
    
    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.balances = new HashMap<>();
    }
    
    public void addBalance(User other, double amount) {
        if (other == this) return;
        
        balances.put(other, balances.getOrDefault(other, 0.0) + amount);
        
        // Maintain symmetry: if I owe them +X, they owe me -X
        other.balances.put(this, other.balances.getOrDefault(this, 0.0) - amount);
    }
    
    public double getBalanceWith(User other) {
        return balances.getOrDefault(other, 0.0);
    }
    
    public double getTotalBalance() {
        return balances.values().stream().mapToDouble(Double::doubleValue).sum();
    }
    
    public Map<User, Double> getBalances() {
        return new HashMap<>(balances);
    }
    
    public Map<User, Double> getPositiveBalances() {
        Map<User, Double> positive = new HashMap<>();
        for (Map.Entry<User, Double> entry : balances.entrySet()) {
            if (entry.getValue() > 0.01) {
                positive.put(entry.getKey(), entry.getValue());
            }
        }
        return positive;
    }
    
    public Map<User, Double> getNegativeBalances() {
        Map<User, Double> negative = new HashMap<>();
        for (Map.Entry<User, Double> entry : balances.entrySet()) {
            if (entry.getValue() < -0.01) {
                negative.put(entry.getKey(), entry.getValue());
            }
        }
        return negative;
    }
    
    // Observer pattern implementation
    @Override
    public void update(String message) {
        System.out.println("📧 Notification to " + name + ": " + message);
    }
    
    @Override
    public String getObserverId() {
        return userId;
    }
    
    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    
    @Override
    public String toString() {
        return name + " (" + userId + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId.equals(user.userId);
    }
    
    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
