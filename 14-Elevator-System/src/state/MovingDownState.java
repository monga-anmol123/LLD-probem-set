package state;

import model.Elevator;
import enums.ElevatorStatus;

/**
 * State: Elevator is moving downward.
 */
public class MovingDownState implements ElevatorState {
    
    @Override
    public void moveUp(Elevator elevator) {
        // Cannot move up while moving down - need to stop first
        System.out.println("Cannot move up while moving down. Stop first.");
    }
    
    @Override
    public void moveDown(Elevator elevator) {
        if (elevator.getCurrentFloor() > 1) {
            elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
            elevator.setStatus(ElevatorStatus.MOVING_DOWN);
        }
    }
    
    @Override
    public void openDoor(Elevator elevator) {
        elevator.setStatus(ElevatorStatus.DOOR_OPEN);
        elevator.setState(new DoorOpenState());
    }
    
    @Override
    public void closeDoor(Elevator elevator) {
        elevator.setStatus(ElevatorStatus.DOOR_CLOSED);
        elevator.setState(new MovingDownState());
    }
}


