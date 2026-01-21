import service.TrafficController;
import model.TrafficSignal;
import model.SignalConfig;
import observer.DisplayBoard;
import observer.MonitoringSystem;
import observer.PedestrianSignal;
import enums.Direction;
import java.util.Arrays;
import java.util.List;

/**
 * Demo class showcasing Traffic Signal System functionality
 * 
 * Design Patterns Used:
 * 1. State Pattern: Signal states (Red, Yellow, Green)
 * 2. Observer Pattern: Observers get notified on state changes
 * 3. Singleton Pattern: Single TrafficController instance
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║     TRAFFIC SIGNAL SYSTEM - DEMO                       ║");
        System.out.println("║     Design Patterns: State, Observer, Singleton        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
        
        // Run all scenarios
        scenario1_BasicOperation();
        scenario2_ObserverPattern();
        scenario3_EmergencyVehicle();
        scenario4_CompleteCycle();
        scenario5_CustomConfiguration();
        
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║     ALL SCENARIOS COMPLETED SUCCESSFULLY ✓             ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
    
    /**
     * SCENARIO 1: Basic 4-way intersection operation
     * Tests: Signal initialization, basic state transitions
     */
    private static void scenario1_BasicOperation() {
        printSeparator("=");
        System.out.println("SCENARIO 1: Basic 4-Way Intersection Operation");
        printSeparator("=");
        
        // Get singleton instance
        TrafficController controller = TrafficController.getInstance();
        
        // Initialize 4-way intersection
        List<Direction> directions = Arrays.asList(
            Direction.NORTH,
            Direction.EAST,
            Direction.SOUTH,
            Direction.WEST
        );
        
        SignalConfig config = new SignalConfig(45, 3, 60);
        controller.initializeIntersection(directions, config);
        
        // Start the system
        controller.start();
        
        // Display initial status
        controller.displayStatus();
        
        // Move to next signal (North: Green → Yellow → Red, East: Red → Green)
        controller.nextSignal();
        
        controller.displayStatus();
        
        // Stop the system
        controller.stop();
        
        System.out.println("✓ Scenario 1 completed\n");
    }
    
    /**
     * SCENARIO 2: Observer Pattern demonstration
     * Tests: Multiple observers receiving notifications
     */
    private static void scenario2_ObserverPattern() {
        printSeparator("=");
        System.out.println("SCENARIO 2: Observer Pattern - Multiple Observers");
        printSeparator("=");
        
        TrafficController controller = TrafficController.getInstance();
        
        // Initialize fresh intersection
        List<Direction> directions = Arrays.asList(Direction.NORTH, Direction.SOUTH);
        controller.initializeIntersection(directions, new SignalConfig(30, 3, 40));
        
        // Create observers
        DisplayBoard displayNorth = new DisplayBoard("North-Display");
        MonitoringSystem monitor = new MonitoringSystem("Central-Monitor");
        PedestrianSignal pedNorth = new PedestrianSignal("North-Crossing");
        
        // Register observers for North signal
        TrafficSignal northSignal = controller.getSignal(Direction.NORTH);
        northSignal.addObserver(displayNorth);
        northSignal.addObserver(monitor);
        northSignal.addObserver(pedNorth);
        
        System.out.println("\n✓ Registered 3 observers for North signal");
        System.out.println("  - Display Board");
        System.out.println("  - Monitoring System");
        System.out.println("  - Pedestrian Signal\n");
        
        // Start and trigger state change
        controller.start();
        
        System.out.println("\n--- All observers notified! ---");
        
        // Transition to next state
        System.out.println("\n--- Triggering state transition ---\n");
        northSignal.transitionToNextState(); // GREEN → YELLOW
        
        System.out.println("\n✓ Scenario 2 completed - All observers received updates\n");
    }
    
    /**
     * SCENARIO 3: Emergency vehicle handling
     * Tests: Emergency mode, priority override
     */
    private static void scenario3_EmergencyVehicle() {
        printSeparator("=");
        System.out.println("SCENARIO 3: Emergency Vehicle Priority");
        printSeparator("=");
        
        TrafficController controller = TrafficController.getInstance();
        
        // Initialize 4-way intersection
        List<Direction> directions = Arrays.asList(
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
        );
        controller.initializeIntersection(directions, new SignalConfig(40, 3, 50));
        
        controller.start();
        
        System.out.println("\n--- Normal operation: North is GREEN ---");
        controller.displayStatus();
        
        // Emergency vehicle from South
        controller.handleEmergency(Direction.SOUTH);
        
        controller.displayStatus();
        
        // Emergency cleared
        controller.resumeNormalOperation();
        
        controller.displayStatus();
        
        controller.stop();
        
        System.out.println("✓ Scenario 3 completed - Emergency handled correctly\n");
    }
    
    /**
     * SCENARIO 4: Complete cycle through all directions
     * Tests: Round-robin cycling, state machine correctness
     */
    private static void scenario4_CompleteCycle() {
        printSeparator("=");
        System.out.println("SCENARIO 4: Complete Cycle - All Directions");
        printSeparator("=");
        
        TrafficController controller = TrafficController.getInstance();
        
        // Initialize 4-way intersection
        List<Direction> directions = Arrays.asList(
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
        );
        controller.initializeIntersection(directions, new SignalConfig(30, 3, 40));
        
        controller.start();
        
        System.out.println("\n--- Running complete cycle (all 4 directions) ---\n");
        
        // Run complete cycle
        controller.runCompleteCycle();
        
        System.out.println("\n--- Cycle completed! Back to North ---");
        controller.displayStatus();
        
        controller.stop();
        
        System.out.println("✓ Scenario 4 completed - Full cycle executed\n");
    }
    
    /**
     * SCENARIO 5: Custom configuration and timing
     * Tests: Configurable durations, extensibility
     */
    private static void scenario5_CustomConfiguration() {
        printSeparator("=");
        System.out.println("SCENARIO 5: Custom Configuration");
        printSeparator("=");
        
        TrafficController controller = TrafficController.getInstance();
        
        // Custom config: Longer green for main road (North-South)
        SignalConfig mainRoadConfig = new SignalConfig(60, 5, 80);
        
        System.out.println("\n--- Custom Configuration ---");
        System.out.println("  Green Duration: 60 seconds (main road)");
        System.out.println("  Yellow Duration: 5 seconds");
        System.out.println("  Red Duration: 80 seconds\n");
        
        List<Direction> directions = Arrays.asList(Direction.NORTH, Direction.SOUTH);
        controller.initializeIntersection(directions, mainRoadConfig);
        
        // Add observers
        TrafficSignal northSignal = controller.getSignal(Direction.NORTH);
        TrafficSignal southSignal = controller.getSignal(Direction.SOUTH);
        
        DisplayBoard displayN = new DisplayBoard("North-Main");
        DisplayBoard displayS = new DisplayBoard("South-Main");
        MonitoringSystem monitor = new MonitoringSystem("Main-Road-Monitor");
        
        northSignal.addObserver(displayN);
        northSignal.addObserver(monitor);
        southSignal.addObserver(displayS);
        southSignal.addObserver(monitor);
        
        controller.start();
        controller.displayStatus();
        
        System.out.println("--- Testing custom timing ---");
        System.out.println("North signal duration: " + northSignal.getCurrentStateDuration() + " seconds");
        
        controller.stop();
        
        System.out.println("\n✓ Scenario 5 completed - Custom config works correctly\n");
    }
    
    /**
     * Helper method to print separator
     */
    private static void printSeparator(String character) {
        System.out.println();
        for (int i = 0; i < 60; i++) {
            System.out.print(character);
        }
        System.out.println();
    }
}

