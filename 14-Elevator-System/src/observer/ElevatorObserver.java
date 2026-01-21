package observer;

import model.Elevator;

/**
 * Observer Pattern: Interface for objects that need to be notified 
 * of elevator status changes.
 */
public interface ElevatorObserver {
    void update(Elevator elevator);
}


