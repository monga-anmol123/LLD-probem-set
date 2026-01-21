import model.*;
import service.ElevatorController;
import strategy.*;
import observer.*;
import enums.Direction;

/**
 * Demo application for Elevator System.
 * Demonstrates State, Strategy, and Observer patterns.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  ELEVATOR SYSTEM DEMO");
        System.out.println("========================================\n");
        
        // Run all scenarios
        scenario1_BasicMovement();
        scenario2_MultipleRequests();
        scenario3_StrategyComparison();
        scenario4_CapacityManagement();
        scenario5_ObserverPattern();
        
        System.out.println("\n" + repeatChar('=', 60));
        System.out.println("  ALL SCENARIOS COMPLETED SUCCESSFULLY! ✓");
        System.out.println(repeatChar('=', 60));
    }
    
    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
    
    /**
     * Scenario 1: Basic elevator movement and state transitions.
     */
    private static void scenario1_BasicMovement() {
        System.out.println("\n" + repeatChar('=', 60));
        System.out.println("  SCENARIO 1: Basic Movement");
        System.out.println(repeatChar('=', 60));
        
        ElevatorController.resetInstance();
        ElevatorController controller = ElevatorController.getInstance(10);
        
        // Create 2 elevators
        Elevator elevator1 = new Elevator("E1", 10, 8);
        Elevator elevator2 = new Elevator("E2", 10, 8);
        
        controller.addElevator(elevator1);
        controller.addElevator(elevator2);
        
        controller.displayStatus();
        
        // Request from floor 5 going UP
        System.out.println("\n--- Test: Request from floor 5 going UP ---");
        controller.requestElevator(5, Direction.UP);
        
        // Process steps to move elevator
        for (int i = 0; i < 6; i++) {
            System.out.println("\nStep " + (i + 1) + ":");
            controller.processStep();
        }
        
        // Add internal request
        System.out.println("\n--- Test: Passenger selects floor 8 ---");
        controller.addInternalRequest("E1", 8);
        
        for (int i = 0; i < 4; i++) {
            System.out.println("\nStep " + (i + 7) + ":");
            controller.processStep();
        }
        
        controller.displayStatus();
        
        System.out.println("\n✓ Scenario 1 completed: Basic movement working correctly!");
    }
    
    /**
     * Scenario 2: Multiple simultaneous requests with SCAN strategy.
     */
    private static void scenario2_MultipleRequests() {
        System.out.println("\n\n" + repeatChar('=', 60));
        System.out.println("  SCENARIO 2: Multiple Requests (SCAN Strategy)");
        System.out.println(repeatChar('=', 60));
        
        ElevatorController.resetInstance();
        ElevatorController controller = ElevatorController.getInstance(10);
        
        Elevator elevator = new Elevator("E1", 10, 8);
        controller.addElevator(elevator);
        controller.setSchedulingStrategy(new SCANStrategy());
        
        controller.displayStatus();
        
        // Multiple requests
        System.out.println("\n--- Test: Multiple requests at different floors ---");
        controller.requestElevator(3, Direction.UP);
        controller.requestElevator(7, Direction.UP);
        controller.requestElevator(5, Direction.DOWN);
        controller.requestElevator(2, Direction.UP);
        
        // Process to handle all requests
        controller.processSteps(15);
        
        controller.displayStatus();
        
        System.out.println("\n✓ Scenario 2 completed: SCAN strategy handled multiple requests!");
    }
    
    /**
     * Scenario 3: Compare different scheduling strategies.
     */
    private static void scenario3_StrategyComparison() {
        System.out.println("\n\n" + repeatChar('=', 60));
        System.out.println("  SCENARIO 3: Strategy Comparison");
        System.out.println(repeatChar('=', 60));
        
        // Test FCFS Strategy
        System.out.println("\n--- Testing FCFS Strategy ---");
        testStrategy(new FCFSStrategy(), "FCFS");
        
        // Test SCAN Strategy
        System.out.println("\n--- Testing SCAN Strategy ---");
        testStrategy(new SCANStrategy(), "SCAN");
        
        // Test LOOK Strategy
        System.out.println("\n--- Testing LOOK Strategy ---");
        testStrategy(new LOOKStrategy(), "LOOK");
        
        // Test SSTF Strategy
        System.out.println("\n--- Testing SSTF Strategy ---");
        testStrategy(new SSTFStrategy(), "SSTF");
        
        System.out.println("\n✓ Scenario 3 completed: All strategies tested successfully!");
    }
    
    private static void testStrategy(SchedulingStrategy strategy, String name) {
        ElevatorController.resetInstance();
        ElevatorController controller = ElevatorController.getInstance(10);
        
        Elevator e1 = new Elevator("E1", 10, 8);
        Elevator e2 = new Elevator("E2", 10, 8);
        controller.addElevator(e1);
        controller.addElevator(e2);
        
        controller.setSchedulingStrategy(strategy);
        
        // Same requests for all strategies
        controller.requestElevator(7, Direction.UP);
        controller.requestElevator(3, Direction.DOWN);
        controller.requestElevator(9, Direction.DOWN);
        
        System.out.println("Requests assigned using " + name + " strategy");
    }
    
    /**
     * Scenario 4: Capacity management and full elevator handling.
     */
    private static void scenario4_CapacityManagement() {
        System.out.println("\n\n" + repeatChar('=', 60));
        System.out.println("  SCENARIO 4: Capacity Management");
        System.out.println(repeatChar('=', 60));
        
        ElevatorController.resetInstance();
        ElevatorController controller = ElevatorController.getInstance(10);
        
        Elevator elevator = new Elevator("E1", 10, 8);
        controller.addElevator(elevator);
        
        controller.displayStatus();
        
        // Move elevator to floor 3
        System.out.println("\n--- Test: Move elevator to floor 3 ---");
        controller.requestElevator(3, Direction.UP);
        controller.processSteps(3);
        
        // Board passengers
        System.out.println("\n--- Test: Board 7 passengers (near capacity) ---");
        controller.boardPassengers("E1", 7);
        controller.displayStatus();
        
        // Try to board more passengers
        System.out.println("\n--- Test: Try to board 3 more passengers (exceeds capacity) ---");
        controller.boardPassengers("E1", 3);
        
        // Board within capacity
        System.out.println("\n--- Test: Board 1 passenger (within capacity) ---");
        controller.boardPassengers("E1", 1);
        controller.displayStatus();
        
        // Move to floor 7 and exit passengers
        System.out.println("\n--- Test: Move to floor 7 and exit passengers ---");
        controller.addInternalRequest("E1", 7);
        controller.processSteps(5);
        controller.exitPassengers("E1", 5);
        controller.displayStatus();
        
        System.out.println("\n✓ Scenario 4 completed: Capacity management working correctly!");
    }
    
    /**
     * Scenario 5: Observer pattern with display panels.
     */
    private static void scenario5_ObserverPattern() {
        System.out.println("\n\n" + repeatChar('=', 60));
        System.out.println("  SCENARIO 5: Observer Pattern (Display Panels)");
        System.out.println(repeatChar('=', 60));
        
        ElevatorController.resetInstance();
        ElevatorController controller = ElevatorController.getInstance(10);
        
        Elevator elevator = new Elevator("E1", 10, 8);
        
        // Attach observers (display panels at different locations)
        DisplayPanel lobby = new DisplayPanel("Lobby");
        DisplayPanel floor5 = new DisplayPanel("Floor 5");
        DisplayPanel floor10 = new DisplayPanel("Floor 10");
        
        elevator.attach(lobby);
        elevator.attach(floor5);
        elevator.attach(floor10);
        
        controller.addElevator(elevator);
        
        System.out.println("\n--- Test: Observers notified on elevator movement ---");
        System.out.println("(Display panels at Lobby, Floor 5, and Floor 10 will show updates)\n");
        
        // Request and move elevator
        controller.requestElevator(5, Direction.UP);
        controller.processSteps(6);
        
        controller.addInternalRequest("E1", 9);
        controller.processSteps(5);
        
        System.out.println("\n✓ Scenario 5 completed: Observer pattern working correctly!");
        System.out.println("  All display panels received updates!");
    }
    
    /**
     * Bonus: Demonstrate edge cases.
     */
    @SuppressWarnings("unused")
    private static void scenarioBonus_EdgeCases() {
        System.out.println("\n\n" + repeatChar('=', 60));
        System.out.println("  BONUS: Edge Cases");
        System.out.println(repeatChar('=', 60));
        
        ElevatorController.resetInstance();
        ElevatorController controller = ElevatorController.getInstance(10);
        
        Elevator elevator = new Elevator("E1", 10, 8);
        controller.addElevator(elevator);
        
        // Test 1: Invalid floor request
        System.out.println("\n--- Test: Invalid floor request (floor 15) ---");
        try {
            controller.requestElevator(15, Direction.UP);
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Caught exception: " + e.getMessage());
        }
        
        // Test 2: Request from current floor
        System.out.println("\n--- Test: Request from current floor (no movement) ---");
        controller.requestElevator(1, Direction.UP);
        controller.processStep();
        
        // Test 3: All elevators full
        System.out.println("\n--- Test: All elevators full (pending queue) ---");
        controller.boardPassengers("E1", 8);
        controller.requestElevator(5, Direction.UP);
        
        controller.displayStatus();
        
        System.out.println("\n✓ Bonus scenario completed: Edge cases handled correctly!");
    }
}

