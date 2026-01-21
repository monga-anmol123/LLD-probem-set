package observer;

import model.TrafficSignal;
import enums.SignalLight;

/**
 * Observer Pattern: Interface for objects that want to be notified
 * when traffic signal state changes
 */
public interface TrafficObserver {
    /**
     * Called when the traffic signal changes state
     * @param signal The signal that changed
     * @param newLight The new light color
     */
    void update(TrafficSignal signal, SignalLight newLight);
}


