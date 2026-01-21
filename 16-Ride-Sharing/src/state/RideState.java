package state;

import model.Ride;

public interface RideState {
    void acceptRide(Ride ride);
    void arriveAtPickup(Ride ride);
    void startRide(Ride ride);
    void completeRide(Ride ride);
    void cancelRide(Ride ride);
    String getStateName();
}

