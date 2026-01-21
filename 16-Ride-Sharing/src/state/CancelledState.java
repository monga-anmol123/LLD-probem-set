package state;

import model.Ride;

public class CancelledState implements RideState {
    
    @Override
    public void acceptRide(Ride ride) {
        throw new IllegalStateException("Cannot accept cancelled ride");
    }
    
    @Override
    public void arriveAtPickup(Ride ride) {
        throw new IllegalStateException("Cannot arrive for cancelled ride");
    }
    
    @Override
    public void startRide(Ride ride) {
        throw new IllegalStateException("Cannot start cancelled ride");
    }
    
    @Override
    public void completeRide(Ride ride) {
        throw new IllegalStateException("Cannot complete cancelled ride");
    }
    
    @Override
    public void cancelRide(Ride ride) {
        throw new IllegalStateException("Ride already cancelled");
    }
    
    @Override
    public String getStateName() {
        return "CANCELLED";
    }
}

