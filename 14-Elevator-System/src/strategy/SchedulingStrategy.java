package strategy;

import model.Elevator;
import model.Request;
import java.util.List;

/**
 * Strategy Pattern: Interface for different elevator scheduling algorithms.
 */
public interface SchedulingStrategy {
    /**
     * Assigns the best elevator for the given request.
     * @param request The elevator request
     * @param elevators List of available elevators
     * @return The selected elevator, or null if none available
     */
    Elevator assignElevator(Request request, List<Elevator> elevators);
}


