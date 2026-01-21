package service;

import model.*;
import enums.Direction;
import strategy.SchedulingStrategy;
import strategy.SCANStrategy;
import java.util.*;

/**
 * Central controller for managing multiple elevators.
 * Uses Strategy pattern for scheduling and coordinates all elevator operations.
 */
public class ElevatorController {
    private static ElevatorController instance;
    
    private List<Elevator> elevators;
    private Queue<Request> pendingRequests;
    private SchedulingStrategy schedulingStrategy;
    private int totalFloors;
    
    private ElevatorController(int totalFloors) {
        this.elevators = new ArrayList<>();
        this.pendingRequests = new LinkedList<>();
        this.schedulingStrategy = new SCANStrategy();  // Default strategy
        this.totalFloors = totalFloors;
    }
    
    public static synchronized ElevatorController getInstance(int totalFloors) {
        if (instance == null) {
            instance = new ElevatorController(totalFloors);
        }
        return instance;
    }
    
    // Add elevator to the system
    public void addElevator(Elevator elevator) {
        elevators.add(elevator);
        System.out.println("✓ Added " + elevator);
    }
    
    // Set scheduling strategy
    public void setSchedulingStrategy(SchedulingStrategy strategy) {
        this.schedulingStrategy = strategy;
        System.out.println("✓ Scheduling strategy changed to: " + strategy.getClass().getSimpleName());
    }
    
    // Request elevator from a floor
    public void requestElevator(int floor, Direction direction) {
        if (floor < 1 || floor > totalFloors) {
            throw new IllegalArgumentException("Invalid floor: " + floor);
        }
        
        if (direction == Direction.IDLE) {
            throw new IllegalArgumentException("Direction cannot be IDLE for external request");
        }
        
        ExternalRequest request = new ExternalRequest(floor, direction);
        System.out.println("\n>>> New Request: Floor " + floor + " going " + direction);
        
        // Assign elevator using current strategy
        Elevator assignedElevator = schedulingStrategy.assignElevator(request, elevators);
        
        if (assignedElevator == null) {
            System.out.println("⚠ No available elevator. Adding to pending queue.");
            pendingRequests.offer(request);
        } else {
            System.out.println("✓ Assigned Elevator " + assignedElevator.getId() + 
                             " (currently at floor " + assignedElevator.getCurrentFloor() + ")");
            assignedElevator.addRequest(floor);
        }
    }
    
    // Internal request (from inside elevator)
    public void addInternalRequest(String elevatorId, int destinationFloor) {
        Elevator elevator = findElevatorById(elevatorId);
        if (elevator == null) {
            System.out.println("⚠ Elevator " + elevatorId + " not found.");
            return;
        }
        
        if (destinationFloor < 1 || destinationFloor > totalFloors) {
            throw new IllegalArgumentException("Invalid floor: " + destinationFloor);
        }
        
        System.out.println(">>> Internal Request in Elevator " + elevatorId + 
                         ": Going to floor " + destinationFloor);
        elevator.addRequest(destinationFloor);
    }
    
    // Simulate passengers boarding
    public void boardPassengers(String elevatorId, int count) {
        Elevator elevator = findElevatorById(elevatorId);
        if (elevator == null) {
            System.out.println("⚠ Elevator " + elevatorId + " not found.");
            return;
        }
        
        if (elevator.canAcceptPassengers(count)) {
            elevator.addPassengers(count);
            System.out.println("✓ " + count + " passengers boarded Elevator " + elevatorId);
        } else {
            System.out.println("⚠ Elevator " + elevatorId + " cannot accept " + count + 
                             " passengers (capacity: " + elevator.getCapacity() + 
                             ", current: " + elevator.getCurrentLoad() + ")");
        }
    }
    
    // Simulate passengers exiting
    public void exitPassengers(String elevatorId, int count) {
        Elevator elevator = findElevatorById(elevatorId);
        if (elevator == null) {
            System.out.println("⚠ Elevator " + elevatorId + " not found.");
            return;
        }
        
        elevator.removePassengers(count);
        System.out.println("✓ " + count + " passengers exited Elevator " + elevatorId);
    }
    
    // Process all elevators (simulate one time step)
    public void processStep() {
        for (Elevator elevator : elevators) {
            elevator.processStep();
        }
        
        // Try to assign pending requests
        processPendingRequests();
    }
    
    // Process multiple steps
    public void processSteps(int steps) {
        System.out.println("\n" + repeatChar('=', 60));
        System.out.println("  PROCESSING " + steps + " TIME STEPS");
        System.out.println(repeatChar('=', 60));
        
        for (int i = 1; i <= steps; i++) {
            System.out.println("\n--- Step " + i + " ---");
            processStep();
            
            // Small delay for readability
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void processPendingRequests() {
        if (pendingRequests.isEmpty()) {
            return;
        }
        
        List<Request> stillPending = new ArrayList<>();
        
        while (!pendingRequests.isEmpty()) {
            Request request = pendingRequests.poll();
            Elevator elevator = schedulingStrategy.assignElevator(request, elevators);
            
            if (elevator != null) {
                System.out.println("✓ Assigned pending request to Elevator " + elevator.getId());
                elevator.addRequest(request.getSourceFloor());
            } else {
                stillPending.add(request);
            }
        }
        
        // Re-add requests that couldn't be assigned
        pendingRequests.addAll(stillPending);
    }
    
    // Display current status of all elevators
    public void displayStatus() {
        System.out.println("\n" + repeatChar('=', 60));
        System.out.println("  ELEVATOR SYSTEM STATUS");
        System.out.println(repeatChar('=', 60));
        System.out.println("Strategy: " + schedulingStrategy.getClass().getSimpleName());
        System.out.println("Total Floors: " + totalFloors);
        System.out.println("Elevators: " + elevators.size());
        System.out.println("Pending Requests: " + pendingRequests.size());
        System.out.println();
        
        for (Elevator elevator : elevators) {
            String dirSymbol = getDirectionSymbol(elevator.getDirection());
            System.out.printf("  %s | Floor: %2d %s | Status: %-12s | Load: %d/%d | Requests: %s\n",
                elevator.getId(),
                elevator.getCurrentFloor(),
                dirSymbol,
                elevator.getStatus(),
                elevator.getCurrentLoad(),
                elevator.getCapacity(),
                elevator.hasRequests() ? "Yes" : "No"
            );
        }
        System.out.println(repeatChar('=', 60));
    }
    
    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
    
    private String getDirectionSymbol(Direction direction) {
        switch (direction) {
            case UP: return "↑";
            case DOWN: return "↓";
            case IDLE: return "•";
            default: return "-";
        }
    }
    
    private Elevator findElevatorById(String id) {
        for (Elevator elevator : elevators) {
            if (elevator.getId().equals(id)) {
                return elevator;
            }
        }
        return null;
    }
    
    public List<Elevator> getElevators() {
        return new ArrayList<>(elevators);
    }
    
    public int getTotalFloors() {
        return totalFloors;
    }
    
    // Reset singleton (useful for testing)
    public static void resetInstance() {
        instance = null;
    }
}

