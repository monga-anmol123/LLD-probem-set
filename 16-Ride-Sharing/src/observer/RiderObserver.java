package observer;

import model.Ride;
import model.Rider;

public class RiderObserver implements Observer {
    private final Rider rider;
    
    public RiderObserver(Rider rider) {
        this.rider = rider;
    }
    
    @Override
    public void update(Ride ride, String message) {
        System.out.println("📱 [Rider " + rider.getName() + "] " + message);
    }
    
    public Rider getRider() {
        return rider;
    }
}

