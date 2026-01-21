package state;

import model.Elevator;
import enums.ElevatorStatus;

/**
 * State: Elevator is moving upward.
 */
public class MovingUpState implements ElevatorState {
    
    @Override
    public void moveUp(Elevator elevator) {
        if (elevator.getCurrentFloor() < elevator.getTotalFloors()) {
            elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
            elevator.setStatus(ElevatorStatus.MOVING_UP);
        }
    }
    
    @Override
    public void moveDown(Elevator elevator) {
        // Cannot move down while moving up - need to stop first
        System.out.println("Cannot move down while moving up. Stop first.");
    }
    
    @Override
    public void openDoor(Elevator elevator) {
        elevator.setStatus(ElevatorStatus.DOOR_OPEN);
        elevator.setState(new DoorOpenState());
    }
    
    @Override
    public void closeDoor(Elevator elevator) {
        elevator.setStatus(ElevatorStatus.DOOR_CLOSED);
        elevator.setState(new MovingUpState());
    }
}


