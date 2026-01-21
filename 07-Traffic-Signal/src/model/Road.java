package model;

import enums.Direction;

/**
 * Represents a road at the intersection
 * Contains metadata about the road (optional, for extensions)
 */
public class Road {
    private Direction direction;
    private String name;
    private int lanes;
    private boolean hasTurnLane;
    
    public Road(Direction direction, String name, int lanes) {
        this.direction = direction;
        this.name = name;
        this.lanes = lanes;
        this.hasTurnLane = false;
    }
    
    public Road(Direction direction, String name) {
        this(direction, name, 2); // Default 2 lanes
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public String getName() {
        return name;
    }
    
    public int getLanes() {
        return lanes;
    }
    
    public boolean hasTurnLane() {
        return hasTurnLane;
    }
    
    public void setHasTurnLane(boolean hasTurnLane) {
        this.hasTurnLane = hasTurnLane;
    }
    
    @Override
    public String toString() {
        return "Road{" +
               "direction=" + direction +
               ", name='" + name + '\'' +
               ", lanes=" + lanes +
               '}';
    }
}


