package state;

import model.TrafficSignal;
import enums.SignalLight;

/**
 * Red State: Stop state
 * This is the default/safe state
 * Duration is variable - depends on when it's this direction's turn
 */
public class RedState implements SignalState {
    private int duration;
    
    public RedState(int duration) {
        this.duration = duration;
    }
    
    public RedState() {
        this(60); // Default red duration
    }
    
    @Override
    public void enter(TrafficSignal signal) {
        signal.setCurrentLight(SignalLight.RED);
        System.out.println("[" + signal.getDirection() + "] Signal: RED (Stop)");
        signal.notifyObservers();
    }
    
    @Override
    public void exit(TrafficSignal signal) {
        // Cleanup if needed
    }
    
    @Override
    public SignalState getNextState() {
        // Red transitions to Green when it's this direction's turn
        return new GreenState();
    }
    
    @Override
    public int getDurationSeconds() {
        return duration;
    }
    
    @Override
    public SignalLight getLight() {
        return SignalLight.RED;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
}


