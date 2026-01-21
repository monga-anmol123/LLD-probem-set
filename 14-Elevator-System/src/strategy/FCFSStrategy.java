package strategy;

import model.Elevator;
import model.Request;
import enums.Direction;
import java.util.List;

/**
 * First Come First Serve Strategy: Assigns nearest idle elevator.
 * Simple strategy that assigns the closest available elevator.
 */
public class FCFSStrategy implements SchedulingStrategy {
    
    @Override
    public Elevator assignElevator(Request request, List<Elevator> elevators) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Elevator elevator : elevators) {
            // Skip elevators in maintenance
            if (elevator.getStatus().toString().equals("MAINTENANCE")) {
                continue;
            }
            
            // Skip full elevators
            if (elevator.isFull()) {
                continue;
            }
            
            // Calculate distance
            int distance = Math.abs(elevator.getCurrentFloor() - request.getSourceFloor());
            
            // Prefer idle elevators
            if (elevator.isIdle()) {
                if (distance < minDistance) {
                    minDistance = distance;
                    bestElevator = elevator;
                }
            } else if (bestElevator == null || (bestElevator.isIdle() == false && distance < minDistance)) {
                // If no idle elevator found, pick closest moving elevator
                minDistance = distance;
                bestElevator = elevator;
            }
        }
        
        return bestElevator;
    }
}


