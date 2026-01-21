package state;

import model.TrafficSignal;
import enums.SignalLight;

/**
 * Green State: Go state
 * Allows traffic to proceed in this direction
 * After duration expires, transitions to Yellow
 */
public class GreenState implements SignalState {
    private static final int DEFAULT_GREEN_DURATION = 45; // 45 seconds
    private int duration;
    
    public GreenState(int duration) {
        this.duration = duration;
    }
    
    public GreenState() {
        this(DEFAULT_GREEN_DURATION);
    }
    
    @Override
    public void enter(TrafficSignal signal) {
        signal.setCurrentLight(SignalLight.GREEN);
        System.out.println("[" + signal.getDirection() + "] Signal: GREEN (Go)");
        signal.notifyObservers();
    }
    
    @Override
    public void exit(TrafficSignal signal) {
        // Cleanup if needed
    }
    
    @Override
    public SignalState getNextState() {
        // Green always transitions to Yellow (never directly to Red for safety)
        return new YellowState();
    }
    
    @Override
    public int getDurationSeconds() {
        return duration;
    }
    
    @Override
    public SignalLight getLight() {
        return SignalLight.GREEN;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
}


