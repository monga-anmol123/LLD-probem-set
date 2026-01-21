package model;

import enums.PaymentMethod;
import java.util.ArrayList;
import java.util.List;

public class Rider extends User {
    private PaymentMethod paymentMethod;
    private final List<Ride> rideHistory;
    
    public Rider(String userId, String name, String phone, PaymentMethod paymentMethod) {
        super(userId, name, phone);
        this.paymentMethod = paymentMethod;
        this.rideHistory = new ArrayList<>();
    }
    
    public void addRideToHistory(Ride ride) {
        rideHistory.add(ride);
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public List<Ride> getRideHistory() {
        return new ArrayList<>(rideHistory);
    }
    
    @Override
    public String toString() {
        return String.format("Rider[%s, %s, Rating: %.1f⭐]", userId, name, rating);
    }
}

