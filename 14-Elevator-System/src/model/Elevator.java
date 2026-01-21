package model;

import enums.Direction;
import enums.ElevatorStatus;
import observer.ElevatorObserver;
import state.*;
import java.util.*;

/**
 * Represents an elevator with state management and request handling.
 */
public class Elevator {
    private String id;
    private int currentFloor;
    private Direction direction;
    private ElevatorStatus status;
    private int capacity;
    private int currentLoad;
    private int totalFloors;
    
    // State Pattern
    private ElevatorState state;
    
    // Request queues (priority queues for efficient floor selection)
    private PriorityQueue<Integer> upQueue;      // Min heap for upward requests
    private PriorityQueue<Integer> downQueue;    // Max heap for downward requests
    
    // Observer Pattern
    private List<ElevatorObserver> observers;
    
    public Elevator(String id, int totalFloors, int capacity) {
        this.id = id;
        this.currentFloor = 1;  // Start at ground floor
        this.direction = Direction.IDLE;
        this.status = ElevatorStatus.IDLE;
        this.capacity = capacity;
        this.currentLoad = 0;
        this.totalFloors = totalFloors;
        
        // Initialize state
        this.state = new IdleState();
        
        // Initialize request queues
        this.upQueue = new PriorityQueue<>();  // Min heap (ascending)
        this.downQueue = new PriorityQueue<>(Collections.reverseOrder());  // Max heap (descending)
        
        // Initialize observers
        this.observers = new ArrayList<>();
    }
    
    // State management
    public void setState(ElevatorState state) {
        this.state = state;
    }
    
    public ElevatorState getState() {
        return state;
    }
    
    // Movement methods (delegated to state)
    public void moveUp() {
        state.moveUp(this);
        notifyObservers();
    }
    
    public void moveDown() {
        state.moveDown(this);
        notifyObservers();
    }
    
    public void openDoor() {
        state.openDoor(this);
        notifyObservers();
    }
    
    public void closeDoor() {
        state.closeDoor(this);
        notifyObservers();
    }
    
    // Request management
    public void addRequest(int floor) {
        if (floor < 1 || floor > totalFloors) {
            throw new IllegalArgumentException("Invalid floor: " + floor);
        }
        
        if (floor == currentFloor) {
            return;  // Already at requested floor
        }
        
        if (floor > currentFloor) {
            if (!upQueue.contains(floor)) {
                upQueue.offer(floor);
            }
        } else {
            if (!downQueue.contains(floor)) {
                downQueue.offer(floor);
            }
        }
    }
    
    public Integer getNextFloor() {
        if (direction == Direction.UP && !upQueue.isEmpty()) {
            return upQueue.peek();
        } else if (direction == Direction.DOWN && !downQueue.isEmpty()) {
            return downQueue.peek();
        }
        return null;
    }
    
    public void removeFloorFromQueue(int floor) {
        if (direction == Direction.UP) {
            upQueue.remove(floor);
        } else if (direction == Direction.DOWN) {
            downQueue.remove(floor);
        }
    }
    
    public boolean hasRequests() {
        return !upQueue.isEmpty() || !downQueue.isEmpty();
    }
    
    public boolean hasPendingRequestsInDirection(Direction dir) {
        if (dir == Direction.UP) {
            return !upQueue.isEmpty();
        } else if (dir == Direction.DOWN) {
            return !downQueue.isEmpty();
        }
        return false;
    }
    
    // Capacity management
    public boolean canAcceptPassengers(int count) {
        return currentLoad + count <= capacity;
    }
    
    public void addPassengers(int count) {
        if (currentLoad + count > capacity) {
            throw new IllegalStateException("Elevator capacity exceeded");
        }
        currentLoad += count;
    }
    
    public void removePassengers(int count) {
        currentLoad = Math.max(0, currentLoad - count);
    }
    
    // Observer pattern
    public void attach(ElevatorObserver observer) {
        observers.add(observer);
    }
    
    public void detach(ElevatorObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers() {
        for (ElevatorObserver observer : observers) {
            observer.update(this);
        }
    }
    
    // Process one step of elevator operation
    public void processStep() {
        if (!hasRequests()) {
            if (direction != Direction.IDLE) {
                direction = Direction.IDLE;
                setState(new IdleState());
                status = ElevatorStatus.IDLE;
                notifyObservers();
            }
            return;
        }
        
        // Determine direction if idle
        if (direction == Direction.IDLE) {
            if (!upQueue.isEmpty()) {
                direction = Direction.UP;
                setState(new MovingUpState());
                status = ElevatorStatus.MOVING_UP;
            } else if (!downQueue.isEmpty()) {
                direction = Direction.DOWN;
                setState(new MovingDownState());
                status = ElevatorStatus.MOVING_DOWN;
            }
        }
        
        // Check if we need to stop at current floor
        Integer nextFloor = getNextFloor();
        if (nextFloor != null && nextFloor == currentFloor) {
            openDoor();
            removeFloorFromQueue(currentFloor);
            closeDoor();
            return;
        }
        
        // Move towards next floor
        if (direction == Direction.UP) {
            moveUp();
        } else if (direction == Direction.DOWN) {
            moveDown();
        }
        
        // Check if we should reverse direction
        if (direction == Direction.UP && upQueue.isEmpty() && !downQueue.isEmpty()) {
            direction = Direction.DOWN;
            setState(new MovingDownState());
            status = ElevatorStatus.MOVING_DOWN;
        } else if (direction == Direction.DOWN && downQueue.isEmpty() && !upQueue.isEmpty()) {
            direction = Direction.UP;
            setState(new MovingUpState());
            status = ElevatorStatus.MOVING_UP;
        }
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public int getCurrentFloor() {
        return currentFloor;
    }
    
    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    public ElevatorStatus getStatus() {
        return status;
    }
    
    public void setStatus(ElevatorStatus status) {
        this.status = status;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public int getCurrentLoad() {
        return currentLoad;
    }
    
    public int getTotalFloors() {
        return totalFloors;
    }
    
    public boolean isFull() {
        return currentLoad >= capacity;
    }
    
    public boolean isIdle() {
        return direction == Direction.IDLE;
    }
    
    @Override
    public String toString() {
        return String.format("Elevator %s: Floor %d, %s, Load: %d/%d", 
            id, currentFloor, direction, currentLoad, capacity);
    }
}


