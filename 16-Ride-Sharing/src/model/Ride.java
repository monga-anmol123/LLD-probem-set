package model;

import enums.RideType;
import enums.RideStatus;
import state.RideState;
import observer.Subject;
import strategy.PricingStrategy;

public class Ride extends Subject {
    private final String rideId;
    private final Rider rider;
    private Driver driver;
    private final Location pickupLocation;
    private final Location dropLocation;
    private final RideType rideType;
    private RideStatus status;
    private RideState state;
    private PricingStrategy pricingStrategy;
    private double fare;
    private long startTime;
    private long endTime;
    
    public Ride(String rideId, Rider rider, Location pickupLocation, 
                Location dropLocation, RideType rideType) {
        this.rideId = rideId;
        this.rider = rider;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.rideType = rideType;
        this.status = RideStatus.REQUESTED;
        this.fare = 0.0;
    }
    
    // State pattern methods - delegate to current state
    public void acceptRide() {
        state.acceptRide(this);
    }
    
    public void arriveAtPickup() {
        state.arriveAtPickup(this);
    }
    
    public void startRide() {
        state.startRide(this);
    }
    
    public void completeRide() {
        state.completeRide(this);
    }
    
    public void cancelRide() {
        state.cancelRide(this);
    }
    
    // Calculate fare using pricing strategy
    public double calculateFare() {
        if (pricingStrategy == null) {
            throw new IllegalStateException("Pricing strategy not set");
        }
        
        double distance = pickupLocation.calculateDistance(dropLocation);
        long durationMinutes = (endTime - startTime) / (1000 * 60);
        double durationHours = durationMinutes / 60.0;
        
        return pricingStrategy.calculateFare(distance, durationHours, rideType);
    }
    
    // Observer pattern - notify observers
    public void notifyObservers(Ride ride, String message) {
        super.notifyObservers(ride, message);
    }
    
    // Getters and setters
    public String getRideId() {
        return rideId;
    }
    
    public Rider getRider() {
        return rider;
    }
    
    public Driver getDriver() {
        return driver;
    }
    
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
    
    public Location getPickupLocation() {
        return pickupLocation;
    }
    
    public Location getDropLocation() {
        return dropLocation;
    }
    
    public RideType getRideType() {
        return rideType;
    }
    
    public RideStatus getStatus() {
        return status;
    }
    
    public void setStatus(RideStatus status) {
        this.status = status;
    }
    
    public RideState getState() {
        return state;
    }
    
    public void setState(RideState state) {
        this.state = state;
    }
    
    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }
    
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }
    
    public double getFare() {
        return fare;
    }
    
    public void setFare(double fare) {
        this.fare = fare;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public long getEndTime() {
        return endTime;
    }
    
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public String toString() {
        return String.format("Ride[%s, %s, %s → %s, Status: %s]", 
            rideId, rideType, pickupLocation.getAddress(), 
            dropLocation.getAddress(), status);
    }
}

