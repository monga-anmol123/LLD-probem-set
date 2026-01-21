package state;

import model.Elevator;
import enums.Direction;
import enums.ElevatorStatus;

/**
 * State: Elevator doors are open (stopped at a floor).
 */
public class DoorOpenState implements ElevatorState {
    
    @Override
    public void moveUp(Elevator elevator) {
        System.out.println("Cannot move with doors open. Close doors first.");
    }
    
    @Override
    public void moveDown(Elevator elevator) {
        System.out.println("Cannot move with doors open. Close doors first.");
    }
    
    @Override
    public void openDoor(Elevator elevator) {
        // Already open
        System.out.println("Doors are already open.");
    }
    
    @Override
    public void closeDoor(Elevator elevator) {
        elevator.setStatus(ElevatorStatus.DOOR_CLOSED);
        
        // Transition back to appropriate state based on direction
        if (elevator.getDirection() == Direction.UP) {
            elevator.setState(new MovingUpState());
        } else if (elevator.getDirection() == Direction.DOWN) {
            elevator.setState(new MovingDownState());
        } else {
            elevator.setState(new IdleState());
        }
    }
}


