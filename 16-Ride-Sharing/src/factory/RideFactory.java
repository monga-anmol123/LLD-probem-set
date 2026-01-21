package factory;

import model.*;
import enums.RideType;
import state.RequestedState;
import strategy.BasePricingStrategy;
import java.util.UUID;

public class RideFactory {
    private static int rideCounter = 1;
    
    public static Ride createRide(Rider rider, Location pickupLocation, 
                                  Location dropLocation, RideType rideType) {
        String rideId = "RIDE-" + String.format("%04d", rideCounter++);
        Ride ride = new Ride(rideId, rider, pickupLocation, dropLocation, rideType);
        
        // Set initial state
        ride.setState(new RequestedState());
        
        // Set default pricing strategy
        ride.setPricingStrategy(new BasePricingStrategy());
        
        return ride;
    }
    
    public static void resetCounter() {
        rideCounter = 1;
    }
}

