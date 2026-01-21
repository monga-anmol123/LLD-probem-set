package state;

import model.Ride;
import enums.RideStatus;

public class StartedState implements RideState {
    
    @Override
    public void acceptRide(Ride ride) {
        throw new IllegalStateException("Ride already in progress");
    }
    
    @Override
    public void arriveAtPickup(Ride ride) {
        throw new IllegalStateException("Ride already started");
    }
    
    @Override
    public void startRide(Ride ride) {
        throw new IllegalStateException("Ride already started");
    }
    
    @Override
    public void completeRide(Ride ride) {
        System.out.println("🏁 Ride completed!");
        ride.setStatus(RideStatus.COMPLETED);
        ride.setState(new CompletedState());
        ride.setEndTime(System.currentTimeMillis());
        
        // Calculate fare
        double fare = ride.calculateFare();
        ride.setFare(fare);
        
        // Update driver earnings (80% to driver, 20% commission)
        double driverEarnings = fare * 0.80;
        ride.getDriver().addEarnings(driverEarnings);
        ride.getDriver().setAvailable(true);
        
        // Add to history
        ride.getRider().addRideToHistory(ride);
        ride.getDriver().addRideToHistory(ride);
        
        System.out.printf("💰 Total Fare: $%.2f (Driver earns: $%.2f)\n", fare, driverEarnings);
        ride.notifyObservers(ride, String.format("Ride completed! Total fare: $%.2f", fare));
    }
    
    @Override
    public void cancelRide(Ride ride) {
        System.out.println("❌ Ride cancelled in STARTED state");
        System.out.println("⚠️  Full cancellation penalty applies");
        ride.setStatus(RideStatus.CANCELLED);
        ride.setState(new CancelledState());
        ride.getDriver().setAvailable(true);
        ride.notifyObservers(ride, "Ride cancelled during trip. Full penalty applies.");
    }
    
    @Override
    public String getStateName() {
        return "STARTED";
    }
}

