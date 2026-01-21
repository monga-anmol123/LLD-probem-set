package state;

import model.TrafficSignal;
import enums.SignalLight;

/**
 * State Pattern: Interface for different traffic signal states
 * Each state knows how to transition to the next state
 */
public interface SignalState {
    /**
     * Called when entering this state
     */
    void enter(TrafficSignal signal);
    
    /**
     * Called when exiting this state
     */
    void exit(TrafficSignal signal);
    
    /**
     * Get the next state in the sequence
     */
    SignalState getNextState();
    
    /**
     * Get how long this state should last (in seconds)
     */
    int getDurationSeconds();
    
    /**
     * Get the light color for this state
     */
    SignalLight getLight();
}


