package model;

import enums.Direction;
import enums.RequestType;

/**
 * Represents a request from a floor button (UP/DOWN).
 */
public class ExternalRequest extends Request {
    
    public ExternalRequest(int floor, Direction direction) {
        super(floor, -1, direction, RequestType.EXTERNAL);
    }
    
    @Override
    public String toString() {
        return String.format("External Request: Floor %d going %s", sourceFloor, direction);
    }
}


