package state;

import model.Elevator;

/**
 * State Pattern: Interface for different elevator states.
 */
public interface ElevatorState {
    void moveUp(Elevator elevator);
    void moveDown(Elevator elevator);
    void openDoor(Elevator elevator);
    void closeDoor(Elevator elevator);
}


