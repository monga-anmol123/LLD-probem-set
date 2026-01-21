package model;

import java.util.ArrayList;
import java.util.List;

public class Driver extends User {
    private final Vehicle vehicle;
    private Location currentLocation;
    private boolean isAvailable;
    private double earnings;
    private final List<Ride> rideHistory;
    
    public Driver(String userId, String name, String phone, Vehicle vehicle, Location location) {
        super(userId, name, phone);
        this.vehicle = vehicle;
        this.currentLocation = location;
        this.isAvailable = true;
        this.earnings = 0.0;
        this.rideHistory = new ArrayList<>();
    }
    
    public void addEarnings(double amount) {
        this.earnings += amount;
    }
    
    public void addRideToHistory(Ride ride) {
        rideHistory.add(ride);
    }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    
    public void updateLocation(Location location) {
        this.currentLocation = location;
    }
    
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public Location getCurrentLocation() {
        return currentLocation;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public double getEarnings() {
        return earnings;
    }
    
    public List<Ride> getRideHistory() {
        return new ArrayList<>(rideHistory);
    }
    
    @Override
    public String toString() {
        return String.format("Driver[%s, %s, %s, Rating: %.1f⭐, Available: %s]", 
            userId, name, vehicle.getModel(), rating, isAvailable ? "Yes" : "No");
    }
}

