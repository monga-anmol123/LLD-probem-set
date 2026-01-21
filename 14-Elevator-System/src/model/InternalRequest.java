package model;

import enums.Direction;
import enums.RequestType;

/**
 * Represents a request from inside the elevator (destination floor selection).
 */
public class InternalRequest extends Request {
    
    public InternalRequest(int currentFloor, int destinationFloor) {
        super(currentFloor, destinationFloor, 
              destinationFloor > currentFloor ? Direction.UP : Direction.DOWN,
              RequestType.INTERNAL);
    }
    
    @Override
    public String toString() {
        return String.format("Internal Request: Floor %d → %d", sourceFloor, destinationFloor);
    }
}


