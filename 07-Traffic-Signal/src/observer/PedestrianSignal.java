package observer;

import model.TrafficSignal;
import enums.SignalLight;

/**
 * Concrete Observer: Pedestrian crossing signal
 * Shows Walk/Don't Walk based on traffic signal state
 * Pedestrians can cross when traffic signal is RED
 */
public class PedestrianSignal implements TrafficObserver {
    private String crossingId;
    private boolean walkSignOn;
    
    public PedestrianSignal(String crossingId) {
        this.crossingId = crossingId;
        this.walkSignOn = false;
    }
    
    @Override
    public void update(TrafficSignal signal, SignalLight newLight) {
        // Pedestrians can walk when traffic signal is RED
        // (perpendicular traffic has green)
        if (newLight == SignalLight.RED) {
            walkSignOn = true;
            System.out.println("  [Pedestrian Signal " + crossingId + "] " +
                             "🚶 WALK - Safe to cross " + signal.getDirection());
        } else {
            walkSignOn = false;
            System.out.println("  [Pedestrian Signal " + crossingId + "] " +
                             "🛑 DON'T WALK - " + signal.getDirection() + " traffic moving");
        }
    }
    
    public boolean isWalkSignOn() {
        return walkSignOn;
    }
    
    public String getCrossingId() {
        return crossingId;
    }
}


