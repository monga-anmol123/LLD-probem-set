package state;

import model.Ride;

public class CompletedState implements RideState {
    
    @Override
    public void acceptRide(Ride ride) {
        throw new IllegalStateException("Ride already completed");
    }
    
    @Override
    public void arriveAtPickup(Ride ride) {
        throw new IllegalStateException("Ride already completed");
    }
    
    @Override
    public void startRide(Ride ride) {
        throw new IllegalStateException("Ride already completed");
    }
    
    @Override
    public void completeRide(Ride ride) {
        throw new IllegalStateException("Ride already completed");
    }
    
    @Override
    public void cancelRide(Ride ride) {
        throw new IllegalStateException("Cannot cancel completed ride");
    }
    
    @Override
    public String getStateName() {
        return "COMPLETED";
    }
}

