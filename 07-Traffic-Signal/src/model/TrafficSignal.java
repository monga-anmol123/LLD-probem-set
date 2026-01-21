package model;

import enums.Direction;
import enums.SignalLight;
import state.SignalState;
import state.RedState;
import observer.TrafficObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a traffic signal for one direction
 * Uses State Pattern for signal state management
 * Uses Observer Pattern to notify interested parties
 */
public class TrafficSignal {
    private Direction direction;
    private SignalLight currentLight;
    private SignalState currentState;
    private List<TrafficObserver> observers;
    private SignalConfig config;
    
    public TrafficSignal(Direction direction, SignalConfig config) {
        this.direction = direction;
        this.config = config;
        this.observers = new ArrayList<>();
        this.currentState = new RedState(); // Start with RED (safe state)
        this.currentLight = SignalLight.RED;
    }
    
    public TrafficSignal(Direction direction) {
        this(direction, new SignalConfig());
    }
    
    /**
     * Transition to the next state in the sequence
     */
    public void transitionToNextState() {
        currentState.exit(this);
        currentState = currentState.getNextState();
        currentState.enter(this);
    }
    
    /**
     * Force signal to RED (for emergency or safety)
     */
    public void forceRed() {
        currentState.exit(this);
        currentState = new RedState();
        currentState.enter(this);
    }
    
    /**
     * Force signal to GREEN (for emergency vehicle)
     */
    public void forceGreen() {
        currentState.exit(this);
        currentState = new state.GreenState();
        currentState.enter(this);
    }
    
    // Observer Pattern methods
    
    /**
     * Register an observer to be notified of state changes
     */
    public void addObserver(TrafficObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Remove an observer
     */
    public void removeObserver(TrafficObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Notify all observers about state change
     */
    public void notifyObservers() {
        for (TrafficObserver observer : observers) {
            observer.update(this, currentLight);
        }
    }
    
    // Getters and Setters
    
    public Direction getDirection() {
        return direction;
    }
    
    public SignalLight getCurrentLight() {
        return currentLight;
    }
    
    public void setCurrentLight(SignalLight light) {
        this.currentLight = light;
    }
    
    public SignalState getCurrentState() {
        return currentState;
    }
    
    public void setCurrentState(SignalState state) {
        this.currentState = state;
    }
    
    public SignalConfig getConfig() {
        return config;
    }
    
    public int getCurrentStateDuration() {
        return currentState.getDurationSeconds();
    }
    
    @Override
    public String toString() {
        return "TrafficSignal{" +
               "direction=" + direction +
               ", currentLight=" + currentLight +
               '}';
    }
}


