package service;

import model.TrafficSignal;
import model.SignalConfig;
import enums.Direction;
import enums.SignalMode;
import enums.SignalLight;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Singleton: Central traffic controller for the intersection
 * Manages all traffic signals and coordinates their state transitions
 */
public class TrafficController {
    private static TrafficController instance;
    
    private Map<Direction, TrafficSignal> signals;
    private Direction currentActiveDirection;
    private List<Direction> cycleOrder;
    private int currentCycleIndex;
    private SignalMode mode;
    private SignalConfig config;
    private boolean isRunning;
    
    // Private constructor for Singleton
    private TrafficController() {
        this.signals = new HashMap<>();
        this.cycleOrder = new ArrayList<>();
        this.currentCycleIndex = 0;
        this.mode = SignalMode.NORMAL;
        this.config = new SignalConfig();
        this.isRunning = false;
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized TrafficController getInstance() {
        if (instance == null) {
            instance = new TrafficController();
        }
        return instance;
    }
    
    /**
     * Initialize the intersection with signals for specified directions
     */
    public void initializeIntersection(List<Direction> directions, SignalConfig config) {
        this.config = config;
        this.cycleOrder = new ArrayList<>(directions);
        
        for (Direction direction : directions) {
            TrafficSignal signal = new TrafficSignal(direction, config);
            signals.put(direction, signal);
        }
        
        System.out.println("✓ Traffic Controller initialized with " + directions.size() + " directions");
        System.out.println("✓ Cycle order: " + cycleOrder);
        System.out.println("✓ Config: " + config);
    }
    
    /**
     * Start the traffic signal system
     */
    public void start() {
        if (cycleOrder.isEmpty()) {
            throw new IllegalStateException("Cannot start: No signals initialized");
        }
        
        isRunning = true;
        currentCycleIndex = 0;
        currentActiveDirection = cycleOrder.get(currentCycleIndex);
        
        System.out.println("\n🚦 Traffic Controller STARTED");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        
        // Set first direction to GREEN, others to RED
        activateDirection(currentActiveDirection);
    }
    
    /**
     * Stop the traffic signal system
     */
    public void stop() {
        isRunning = false;
        // Set all signals to RED for safety
        for (TrafficSignal signal : signals.values()) {
            signal.forceRed();
        }
        System.out.println("\n🛑 Traffic Controller STOPPED - All signals RED");
    }
    
    /**
     * Move to the next signal in the cycle
     */
    public void nextSignal() {
        if (!isRunning) {
            return;
        }
        
        // Current direction: GREEN → YELLOW
        TrafficSignal currentSignal = signals.get(currentActiveDirection);
        if (currentSignal.getCurrentLight() == SignalLight.GREEN) {
            System.out.println("\n--- Transitioning " + currentActiveDirection + " ---");
            currentSignal.transitionToNextState(); // GREEN → YELLOW
            
            // Simulate waiting for yellow duration
            simulateWait(currentSignal.getCurrentStateDuration());
            
            // YELLOW → RED
            System.out.println("\n--- Transitioning " + currentActiveDirection + " ---");
            currentSignal.transitionToNextState(); // YELLOW → RED
            
            // Move to next direction
            currentCycleIndex = (currentCycleIndex + 1) % cycleOrder.size();
            currentActiveDirection = cycleOrder.get(currentCycleIndex);
            
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Next direction: " + currentActiveDirection);
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            
            // Activate next direction
            activateDirection(currentActiveDirection);
        }
    }
    
    /**
     * Activate a specific direction (set to GREEN)
     */
    private void activateDirection(Direction direction) {
        TrafficSignal signal = signals.get(direction);
        
        // Ensure all other signals are RED
        for (Map.Entry<Direction, TrafficSignal> entry : signals.entrySet()) {
            if (!entry.getKey().equals(direction)) {
                if (entry.getValue().getCurrentLight() != SignalLight.RED) {
                    entry.getValue().forceRed();
                }
            }
        }
        
        // Activate this direction: RED → GREEN
        System.out.println("--- Activating " + direction + " ---");
        signal.transitionToNextState(); // RED → GREEN
        
        // Simulate waiting for green duration
        simulateWait(signal.getCurrentStateDuration());
    }
    
    /**
     * Handle emergency vehicle from a specific direction
     */
    public void handleEmergency(Direction emergencyDirection) {
        System.out.println("\n🚨 EMERGENCY VEHICLE DETECTED from " + emergencyDirection + " 🚨");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        mode = SignalMode.EMERGENCY;
        
        // Turn all signals to RED immediately
        for (Map.Entry<Direction, TrafficSignal> entry : signals.entrySet()) {
            if (!entry.getKey().equals(emergencyDirection)) {
                entry.getValue().forceRed();
                System.out.println("  → " + entry.getKey() + " forced to RED");
            }
        }
        
        // Give emergency direction GREEN immediately
        TrafficSignal emergencySignal = signals.get(emergencyDirection);
        emergencySignal.forceGreen();
        System.out.println("  → " + emergencyDirection + " forced to GREEN (Emergency Priority)");
        
        currentActiveDirection = emergencyDirection;
        
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
    }
    
    /**
     * Resume normal operation after emergency
     */
    public void resumeNormalOperation() {
        System.out.println("\n✓ Emergency cleared - Resuming normal operation");
        mode = SignalMode.NORMAL;
        
        // Continue from next direction in cycle
        currentCycleIndex = (cycleOrder.indexOf(currentActiveDirection) + 1) % cycleOrder.size();
        currentActiveDirection = cycleOrder.get(currentCycleIndex);
        
        activateDirection(currentActiveDirection);
    }
    
    /**
     * Run a complete cycle (all directions get green once)
     */
    public void runCompleteCycle() {
        int directionsCount = cycleOrder.size();
        for (int i = 0; i < directionsCount; i++) {
            nextSignal();
        }
    }
    
    /**
     * Display current status of all signals
     */
    public void displayStatus() {
        System.out.println("\n📊 Current Signal Status:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        for (Direction direction : cycleOrder) {
            TrafficSignal signal = signals.get(direction);
            String status = signal.getCurrentLight().toString();
            String indicator = signal.getCurrentLight() == SignalLight.GREEN ? "🟢" :
                             signal.getCurrentLight() == SignalLight.YELLOW ? "🟡" : "🔴";
            System.out.println("  " + direction + ": " + indicator + " " + status);
        }
        System.out.println("  Mode: " + mode);
        System.out.println("  Active Direction: " + currentActiveDirection);
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
    }
    
    /**
     * Simulate waiting (in real system, would use actual timers)
     */
    private void simulateWait(int seconds) {
        System.out.println("  ⏱️  Duration: " + seconds + " seconds");
        // In real system: Thread.sleep(seconds * 1000);
        // For demo, we just print the duration
    }
    
    // Getters
    
    public TrafficSignal getSignal(Direction direction) {
        return signals.get(direction);
    }
    
    public Direction getCurrentActiveDirection() {
        return currentActiveDirection;
    }
    
    public SignalMode getMode() {
        return mode;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public List<Direction> getCycleOrder() {
        return new ArrayList<>(cycleOrder);
    }
}


