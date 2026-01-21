package state;

import model.Ride;
import enums.RideStatus;

public class RequestedState implements RideState {
    
    @Override
    public void acceptRide(Ride ride) {
        System.out.println("✅ Driver accepted the ride!");
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setState(new AcceptedState());
        ride.notifyObservers(ride, "Driver " + ride.getDriver().getName() + 
                            " accepted your ride! Vehicle: " + ride.getDriver().getVehicle());
    }
    
    @Override
    public void arriveAtPickup(Ride ride) {
        throw new IllegalStateException("Cannot arrive at pickup before accepting ride");
    }
    
    @Override
    public void startRide(Ride ride) {
        throw new IllegalStateException("Cannot start ride before accepting");
    }
    
    @Override
    public void completeRide(Ride ride) {
        throw new IllegalStateException("Cannot complete ride before starting");
    }
    
    @Override
    public void cancelRide(Ride ride) {
        System.out.println("❌ Ride cancelled in REQUESTED state");
        ride.setStatus(RideStatus.CANCELLED);
        ride.setState(new CancelledState());
        ride.getDriver().setAvailable(true);
        ride.notifyObservers(ride, "Ride has been cancelled");
    }
    
    @Override
    public String getStateName() {
        return "REQUESTED";
    }
}

