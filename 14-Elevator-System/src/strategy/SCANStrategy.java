package strategy;

import model.Elevator;
import model.Request;
import enums.Direction;
import java.util.List;

/**
 * SCAN Strategy (Elevator Algorithm): 
 * Continues in current direction serving all requests, then reverses.
 * Most efficient for high-traffic scenarios.
 */
public class SCANStrategy implements SchedulingStrategy {
    
    @Override
    public Elevator assignElevator(Request request, List<Elevator> elevators) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;
        
        // First, try to find an elevator moving in the same direction
        for (Elevator elevator : elevators) {
            if (elevator.getStatus().toString().equals("MAINTENANCE") || elevator.isFull()) {
                continue;
            }
            
            // Check if elevator is moving in same direction and can pick up passenger
            if (elevator.getDirection() == request.getDirection()) {
                boolean canPickup = false;
                int distance = 0;
                
                if (request.getDirection() == Direction.UP) {
                    // Elevator moving up, request is up, and elevator is below request floor
                    if (elevator.getCurrentFloor() <= request.getSourceFloor()) {
                        canPickup = true;
                        distance = request.getSourceFloor() - elevator.getCurrentFloor();
                    }
                } else if (request.getDirection() == Direction.DOWN) {
                    // Elevator moving down, request is down, and elevator is above request floor
                    if (elevator.getCurrentFloor() >= request.getSourceFloor()) {
                        canPickup = true;
                        distance = elevator.getCurrentFloor() - request.getSourceFloor();
                    }
                }
                
                if (canPickup && distance < minDistance) {
                    minDistance = distance;
                    bestElevator = elevator;
                }
            }
        }
        
        // If no suitable moving elevator found, find nearest idle elevator
        if (bestElevator == null) {
            bestElevator = findNearestIdleElevator(request, elevators);
        }
        
        return bestElevator;
    }
    
    private Elevator findNearestIdleElevator(Request request, List<Elevator> elevators) {
        Elevator nearest = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Elevator elevator : elevators) {
            if (elevator.getStatus().toString().equals("MAINTENANCE") || elevator.isFull()) {
                continue;
            }
            
            if (elevator.isIdle()) {
                int distance = Math.abs(elevator.getCurrentFloor() - request.getSourceFloor());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = elevator;
                }
            }
        }
        
        return nearest;
    }
}


