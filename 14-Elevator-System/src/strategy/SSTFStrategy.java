package strategy;

import model.Elevator;
import model.Request;
import java.util.List;

/**
 * Shortest Seek Time First Strategy:
 * Always serves the nearest request first.
 * Minimizes individual wait times but can cause starvation.
 */
public class SSTFStrategy implements SchedulingStrategy {
    
    @Override
    public Elevator assignElevator(Request request, List<Elevator> elevators) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Elevator elevator : elevators) {
            if (elevator.getStatus().toString().equals("MAINTENANCE") || elevator.isFull()) {
                continue;
            }
            
            // Calculate distance from elevator to request
            int distance = Math.abs(elevator.getCurrentFloor() - request.getSourceFloor());
            
            // Add penalty for busy elevators
            if (!elevator.isIdle()) {
                distance += 5;  // Penalty for busy elevator
            }
            
            if (distance < minDistance) {
                minDistance = distance;
                bestElevator = elevator;
            }
        }
        
        return bestElevator;
    }
}


