package state;

import model.Ride;
import enums.RideStatus;

public class AcceptedState implements RideState {
    
    @Override
    public void acceptRide(Ride ride) {
        throw new IllegalStateException("Ride already accepted");
    }
    
    @Override
    public void arriveAtPickup(Ride ride) {
        System.out.println("🚗 Driver arrived at pickup location!");
        ride.setStatus(RideStatus.ARRIVED);
        ride.notifyObservers(ride, "Your driver has arrived at the pickup location!");
    }
    
    @Override
    public void startRide(Ride ride) {
        System.out.println("🚀 Ride started!");
        ride.setStatus(RideStatus.STARTED);
        ride.setState(new StartedState());
        ride.setStartTime(System.currentTimeMillis());
        ride.notifyObservers(ride, "Your ride has started. Enjoy your trip!");
    }
    
    @Override
    public void completeRide(Ride ride) {
        throw new IllegalStateException("Cannot complete ride before starting");
    }
    
    @Override
    public void cancelRide(Ride ride) {
        System.out.println("❌ Ride cancelled in ACCEPTED state");
        System.out.println("⚠️  Cancellation penalty may apply");
        ride.setStatus(RideStatus.CANCELLED);
        ride.setState(new CancelledState());
        ride.getDriver().setAvailable(true);
        ride.notifyObservers(ride, "Ride has been cancelled. Cancellation fee may apply.");
    }
    
    @Override
    public String getStateName() {
        return "ACCEPTED";
    }
}

