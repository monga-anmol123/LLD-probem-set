package service;

import model.*;
import enums.RideType;
import enums.VehicleType;
import factory.RideFactory;
import observer.*;
import strategy.PricingStrategy;
import java.util.*;

public class RideService {
    private static RideService instance;
    
    private final List<Rider> riders;
    private final List<Driver> drivers;
    private final List<Ride> rides;
    private PricingStrategy defaultPricingStrategy;
    
    private RideService() {
        this.riders = new ArrayList<>();
        this.drivers = new ArrayList<>();
        this.rides = new ArrayList<>();
    }
    
    public static synchronized RideService getInstance() {
        if (instance == null) {
            instance = new RideService();
        }
        return instance;
    }
    
    // Register users
    public void registerRider(Rider rider) {
        riders.add(rider);
        System.out.println("✅ Rider registered: " + rider);
    }
    
    public void registerDriver(Driver driver) {
        drivers.add(driver);
        System.out.println("✅ Driver registered: " + driver);
    }
    
    // Request a ride
    public Ride requestRide(Rider rider, Location pickupLocation, 
                           Location dropLocation, RideType rideType) {
        System.out.println("\n🚕 Requesting " + rideType + " ride...");
        System.out.println("   Pickup: " + pickupLocation.getAddress());
        System.out.println("   Drop: " + dropLocation.getAddress());
        
        // Create ride using Factory pattern
        Ride ride = RideFactory.createRide(rider, pickupLocation, dropLocation, rideType);
        
        // Find nearest available driver
        Driver driver = findNearestDriver(pickupLocation, rideType);
        
        if (driver == null) {
            System.out.println("❌ No drivers available for " + rideType + " in your area");
            return null;
        }
        
        // Assign driver
        ride.setDriver(driver);
        driver.setAvailable(false);
        
        // Set pricing strategy
        if (defaultPricingStrategy != null) {
            ride.setPricingStrategy(defaultPricingStrategy);
        }
        
        // Attach observers
        ride.attach(new RiderObserver(rider));
        ride.attach(new DriverObserver(driver));
        
        // Calculate estimated fare
        double distance = pickupLocation.calculateDistance(dropLocation);
        double estimatedTime = distance / 40.0; // Assume 40 km/h average speed
        double estimatedFare = ride.getPricingStrategy().calculateFare(distance, estimatedTime, rideType);
        
        System.out.printf("✅ Ride matched! Driver: %s\n", driver.getName());
        System.out.printf("   Vehicle: %s\n", driver.getVehicle());
        System.out.printf("   Distance: %.2f km\n", distance);
        System.out.printf("   Estimated Fare: $%.2f\n", estimatedFare);
        
        rides.add(ride);
        
        // Notify driver
        ride.notifyObservers(ride, "New ride request from " + rider.getName() + 
                            " at " + pickupLocation.getAddress());
        
        return ride;
    }
    
    // Find nearest available driver
    private Driver findNearestDriver(Location pickupLocation, RideType rideType) {
        return drivers.stream()
            .filter(Driver::isAvailable)
            .filter(d -> d.getVehicle().getType().getRideType() == rideType)
            .min(Comparator.comparingDouble(d -> 
                d.getCurrentLocation().calculateDistance(pickupLocation)))
            .orElse(null);
    }
    
    // Set pricing strategy for all new rides
    public void setDefaultPricingStrategy(PricingStrategy strategy) {
        this.defaultPricingStrategy = strategy;
        System.out.println("✅ Default pricing strategy updated: " + 
                          strategy.getClass().getSimpleName());
    }
    
    // Display statistics
    public void displayStatistics() {
        System.out.println("\n========================================");
        System.out.println("  RIDE SERVICE STATISTICS");
        System.out.println("========================================");
        
        System.out.println("\n📊 Riders: " + riders.size());
        for (Rider rider : riders) {
            System.out.printf("   %s - %d rides, Rating: %.1f⭐\n", 
                rider.getName(), rider.getRideHistory().size(), rider.getRating());
        }
        
        System.out.println("\n🚗 Drivers: " + drivers.size());
        for (Driver driver : drivers) {
            System.out.printf("   %s - %d rides, Earnings: $%.2f, Rating: %.1f⭐, Available: %s\n", 
                driver.getName(), driver.getRideHistory().size(), driver.getEarnings(), 
                driver.getRating(), driver.isAvailable() ? "Yes" : "No");
        }
        
        System.out.println("\n🚕 Total Rides: " + rides.size());
        long completed = rides.stream().filter(r -> r.getStatus().name().equals("COMPLETED")).count();
        long cancelled = rides.stream().filter(r -> r.getStatus().name().equals("CANCELLED")).count();
        System.out.println("   Completed: " + completed);
        System.out.println("   Cancelled: " + cancelled);
        
        double totalRevenue = rides.stream()
            .filter(r -> r.getStatus().name().equals("COMPLETED"))
            .mapToDouble(Ride::getFare)
            .sum();
        System.out.printf("   Total Revenue: $%.2f\n", totalRevenue);
        
        System.out.println("========================================\n");
    }
    
    // Display available drivers
    public void displayAvailableDrivers() {
        System.out.println("\n🚗 Available Drivers:");
        drivers.stream()
            .filter(Driver::isAvailable)
            .forEach(d -> System.out.printf("   %s - %s at %s\n", 
                d.getName(), d.getVehicle().getModel(), d.getCurrentLocation().getAddress()));
    }
    
    public List<Rider> getRiders() {
        return new ArrayList<>(riders);
    }
    
    public List<Driver> getDrivers() {
        return new ArrayList<>(drivers);
    }
    
    public List<Ride> getRides() {
        return new ArrayList<>(rides);
    }
}

