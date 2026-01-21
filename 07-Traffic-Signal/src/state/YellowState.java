package state;

import model.TrafficSignal;
import enums.SignalLight;

/**
 * Yellow State: Caution/Warning state
 * Brief transition between Green and Red
 * Warns drivers that signal is about to turn red
 */
public class YellowState implements SignalState {
    private static final int DEFAULT_YELLOW_DURATION = 3; // 3 seconds
    private int duration;
    
    public YellowState(int duration) {
        this.duration = duration;
    }
    
    public YellowState() {
        this(DEFAULT_YELLOW_DURATION);
    }
    
    @Override
    public void enter(TrafficSignal signal) {
        signal.setCurrentLight(SignalLight.YELLOW);
        System.out.println("[" + signal.getDirection() + "] Signal: YELLOW (Caution - Prepare to Stop)");
        signal.notifyObservers();
    }
    
    @Override
    public void exit(TrafficSignal signal) {
        // Cleanup if needed
    }
    
    @Override
    public SignalState getNextState() {
        // Yellow always transitions to Red
        return new RedState();
    }
    
    @Override
    public int getDurationSeconds() {
        return duration;
    }
    
    @Override
    public SignalLight getLight() {
        return SignalLight.YELLOW;
    }
}


