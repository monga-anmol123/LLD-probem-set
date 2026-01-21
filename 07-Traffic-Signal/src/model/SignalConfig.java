package model;

/**
 * Configuration for signal timing
 * Stores duration for each light color
 */
public class SignalConfig {
    private int greenDuration;    // seconds
    private int yellowDuration;   // seconds
    private int redDuration;      // seconds (variable, depends on cycle)
    
    public SignalConfig(int greenDuration, int yellowDuration, int redDuration) {
        this.greenDuration = greenDuration;
        this.yellowDuration = yellowDuration;
        this.redDuration = redDuration;
    }
    
    // Default configuration
    public SignalConfig() {
        this(45, 3, 60); // 45s green, 3s yellow, 60s red
    }
    
    public int getGreenDuration() {
        return greenDuration;
    }
    
    public void setGreenDuration(int greenDuration) {
        this.greenDuration = greenDuration;
    }
    
    public int getYellowDuration() {
        return yellowDuration;
    }
    
    public void setYellowDuration(int yellowDuration) {
        this.yellowDuration = yellowDuration;
    }
    
    public int getRedDuration() {
        return redDuration;
    }
    
    public void setRedDuration(int redDuration) {
        this.redDuration = redDuration;
    }
    
    public int getTotalCycleDuration() {
        return greenDuration + yellowDuration + redDuration;
    }
    
    @Override
    public String toString() {
        return "SignalConfig{" +
               "green=" + greenDuration + "s, " +
               "yellow=" + yellowDuration + "s, " +
               "red=" + redDuration + "s}";
    }
}


