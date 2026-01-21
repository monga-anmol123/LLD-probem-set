package model;

import enums.Direction;
import enums.RequestType;
import java.time.LocalDateTime;

/**
 * Represents an elevator request (external or internal).
 */
public abstract class Request {
    private static int requestCounter = 0;
    
    protected String requestId;
    protected int sourceFloor;
    protected int destinationFloor;
    protected Direction direction;
    protected RequestType type;
    protected LocalDateTime timestamp;
    
    public Request(int sourceFloor, int destinationFloor, Direction direction, RequestType type) {
        this.requestId = "REQ-" + (++requestCounter);
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.direction = direction;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public int getSourceFloor() {
        return sourceFloor;
    }
    
    public int getDestinationFloor() {
        return destinationFloor;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public RequestType getType() {
        return type;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("%s: Floor %d → %d (%s)", 
            requestId, sourceFloor, destinationFloor, direction);
    }
}


