package strategy;

import model.Elevator;
import model.Request;
import enums.Direction;
import java.util.List;

/**
 * LOOK Strategy: Like SCAN but reverses immediately when no more requests 
 * in current direction (doesn't go to the end).
 * More efficient than SCAN for medium traffic.
 */
public class LOOKStrategy implements SchedulingStrategy {
    
    @Override
    public Elevator assignElevator(Request request, List<Elevator> elevators) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;
        int bestScore = Integer.MAX_VALUE;
        
        for (Elevator elevator : elevators) {
            if (elevator.getStatus().toString().equals("MAINTENANCE") || elevator.isFull()) {
                continue;
            }
            
            int distance = Math.abs(elevator.getCurrentFloor() - request.getSourceFloor());
            int score = calculateScore(elevator, request);
            
            // Prefer elevators with lower score (better match)
            if (score < bestScore || (score == bestScore && distance < minDistance)) {
                bestScore = score;
                minDistance = distance;
                bestElevator = elevator;
            }
        }
        
        return bestElevator;
    }
    
    private int calculateScore(Elevator elevator, Request request) {
        // Score based on:
        // 1. Same direction and on the way: score = 1
        // 2. Idle: score = 2
        // 3. Opposite direction: score = 3
        // 4. Same direction but passed: score = 4
        
        if (elevator.isIdle()) {
            return 2;
        }
        
        Direction elevatorDir = elevator.getDirection();
        Direction requestDir = request.getDirection();
        int elevatorFloor = elevator.getCurrentFloor();
        int requestFloor = request.getSourceFloor();
        
        // Same direction
        if (elevatorDir == requestDir) {
            if (requestDir == Direction.UP && elevatorFloor <= requestFloor) {
                return 1;  // On the way up
            } else if (requestDir == Direction.DOWN && elevatorFloor >= requestFloor) {
                return 1;  // On the way down
            } else {
                return 4;  // Already passed
            }
        } else {
            return 3;  // Opposite direction
        }
    }
}


