package state;

import model.Elevator;
import enums.Direction;
import enums.ElevatorStatus;

/**
 * State: Elevator is idle (not moving, no pending requests).
 */
public class IdleState implements ElevatorState {
    
    @Override
    public void moveUp(Elevator elevator) {
        elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
        elevator.setDirection(Direction.UP);
        elevator.setStatus(ElevatorStatus.MOVING_UP);
        elevator.setState(new MovingUpState());
    }
    
    @Override
    public void moveDown(Elevator elevator) {
        elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
        elevator.setDirection(Direction.DOWN);
        elevator.setStatus(ElevatorStatus.MOVING_DOWN);
        elevator.setState(new MovingDownState());
    }
    
    @Override
    public void openDoor(Elevator elevator) {
        elevator.setStatus(ElevatorStatus.DOOR_OPEN);
        elevator.setState(new DoorOpenState());
    }
    
    @Override
    public void closeDoor(Elevator elevator) {
        elevator.setStatus(ElevatorStatus.DOOR_CLOSED);
    }
}


