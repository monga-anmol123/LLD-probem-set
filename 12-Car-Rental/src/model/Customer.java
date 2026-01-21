package model;

import enums.MembershipTier;
import observer.Observer;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Observer {
    private String customerId;
    private String name;
    private String email;
    private String phone;
    private String licenseNumber;
    private MembershipTier membershipTier;
    private List<Rental> rentalHistory;
    private double outstandingDues;
    
    public Customer(String customerId, String name, String email, String phone, String licenseNumber, MembershipTier tier) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.membershipTier = tier;
        this.rentalHistory = new ArrayList<>();
        this.outstandingDues = 0.0;
    }
    
    public boolean canRent() {
        // Customer can rent if they have no outstanding dues
        return outstandingDues == 0.0;
    }
    
    public void addRental(Rental rental) {
        rentalHistory.add(rental);
    }
    
    public double getMembershipDiscount() {
        return membershipTier.getDiscountRate();
    }
    
    public void addOutstandingDues(double amount) {
        outstandingDues += amount;
    }
    
    public void clearDues() {
        outstandingDues = 0.0;
    }
    
    // Observer pattern implementation
    @Override
    public void update(String message) {
        System.out.println("📧 Notification to " + name + " (" + email + "): " + message);
    }
    
    @Override
    public String getNotificationId() {
        return customerId;
    }
    
    // Getters
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getLicenseNumber() { return licenseNumber; }
    public MembershipTier getMembershipTier() { return membershipTier; }
    public void setMembershipTier(MembershipTier tier) { this.membershipTier = tier; }
    public List<Rental> getRentalHistory() { return rentalHistory; }
    public double getOutstandingDues() { return outstandingDues; }
    
    @Override
    public String toString() {
        return String.format("Customer: %s (ID: %s, Tier: %s, License: %s)", 
            name, customerId, membershipTier, licenseNumber);
    }
}


