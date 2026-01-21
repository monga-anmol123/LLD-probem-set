# Solution: Elevator System

## ✅ Complete Implementation

This folder contains a fully working elevator control system demonstrating State, Strategy, and Observer design patterns.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                         Main.java                            │
│                    (Demo/Entry Point)                        │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┬──────────────┐
        │               │               │              │
        ▼               ▼               ▼              ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐ ┌──────────┐
│   State      │ │  Strategy   │ │  Observer  │ │ Service  │
│              │ │             │ │            │ │          │
│ IdleState    │ │ FCFS        │ │ Display    │ │Elevator  │
│ MovingUp     │ │ SCAN        │ │ Panel      │ │Controller│
│ MovingDown   │ │ LOOK        │ │            │ │          │
│ DoorOpen     │ │ SSTF        │ │            │ │          │
└──────────────┘ └─────────────┘ └────────────┘ └──────────┘
        │               │               │              │
        └───────────────┼───────────────┼──────────────┘
                        │               │
                        ▼               ▼
                ┌─────────────┐ ┌─────────────┐
                │    Model    │ │   Enums     │
                │             │ │             │
                │  Elevator   │ │ Direction   │
                │  Request    │ │ Status      │
                │  External   │ │ RequestType │
                │  Internal   │ │             │
                └─────────────┘ └─────────────┘
```

---

## 📦 Package Structure

```
src/
├── enums/                       # Type-safe enumerations
│   ├── Direction.java          # UP, DOWN, IDLE
│   ├── ElevatorStatus.java     # IDLE, MOVING_UP, MOVING_DOWN, DOOR_OPEN, etc.
│   └── RequestType.java        # EXTERNAL, INTERNAL
│
├── model/                       # Domain entities
│   ├── Request.java            # Abstract base class for requests
│   ├── ExternalRequest.java    # Floor button press (UP/DOWN)
│   ├── InternalRequest.java    # Destination selection inside elevator
│   └── Elevator.java           # Elevator with state and request queues
│
├── state/                       # State Pattern
│   ├── ElevatorState.java      # Interface for states
│   ├── IdleState.java          # Not moving, no requests
│   ├── MovingUpState.java      # Moving upward
│   ├── MovingDownState.java    # Moving downward
│   └── DoorOpenState.java      # Stopped with doors open
│
├── strategy/                    # Strategy Pattern
│   ├── SchedulingStrategy.java          # Interface
│   ├── FCFSStrategy.java                # First Come First Serve
│   ├── SCANStrategy.java                # Elevator algorithm
│   ├── LOOKStrategy.java                # SCAN variant
│   └── SSTFStrategy.java                # Shortest Seek Time First
│
├── observer/                    # Observer Pattern
│   ├── ElevatorObserver.java   # Interface for observers
│   └── DisplayPanel.java       # Concrete observer (display)
│
├── service/                     # Business logic
│   └── ElevatorController.java # Central controller (Singleton)
│
└── Main.java                    # Demo application
```

---

## 🎨 Design Patterns Explained

### 1. **State Pattern** (Elevator States)

**Purpose:** Manage elevator state transitions cleanly without complex conditionals.

**Implementation:**

```java
public interface ElevatorState {
    void moveUp(Elevator elevator);
    void moveDown(Elevator elevator);
    void openDoor(Elevator elevator);
    void closeDoor(Elevator elevator);
}

public class MovingUpState implements ElevatorState {
    @Override
    public void moveUp(Elevator elevator) {
        elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
        // State remains MovingUpState
    }
    
    @Override
    public void openDoor(Elevator elevator) {
        elevator.setState(new DoorOpenState());
        // Transition to DoorOpenState
    }
}
```

**State Transitions:**
```
IDLE ──────────> MOVING_UP ──────────> DOOR_OPEN
  ▲                  │                      │
  │                  │                      │
  │                  ▼                      ▼
  └───────────── MOVING_DOWN ◄───────── DOOR_CLOSED
```

**Benefits:**
- ✅ Clean state transitions
- ✅ No complex if-else chains
- ✅ Each state encapsulates its behavior
- ✅ Easy to add new states

---

### 2. **Strategy Pattern** (Scheduling Algorithms)

**Purpose:** Switch between different elevator scheduling algorithms at runtime.

**Implementation:**

```java
public interface SchedulingStrategy {
    Elevator assignElevator(Request request, List<Elevator> elevators);
}

// FCFS: Nearest idle elevator
public class FCFSStrategy implements SchedulingStrategy {
    public Elevator assignElevator(Request request, List<Elevator> elevators) {
        // Find nearest idle elevator
    }
}

// SCAN: Continue in direction, serve all requests
public class SCANStrategy implements SchedulingStrategy {
    public Elevator assignElevator(Request request, List<Elevator> elevators) {
        // Find elevator moving in same direction
    }
}
```

**Strategy Comparison:**

| Strategy | Description | Best For | Avg Wait Time |
|----------|-------------|----------|---------------|
| **FCFS** | Nearest idle elevator | Low traffic | Medium |
| **SCAN** | Continue direction, serve all | High traffic | Low |
| **LOOK** | Reverse when no more requests | Medium traffic | Low-Medium |
| **SSTF** | Serve nearest request first | Variable traffic | Very Low* |

*SSTF can cause starvation for distant requests

**Benefits:**
- ✅ Easy to switch strategies at runtime
- ✅ Easy to add new algorithms
- ✅ Open-Closed Principle (OCP)
- ✅ Testable in isolation

---

### 3. **Observer Pattern** (Display Panels)

**Purpose:** Notify display panels when elevator status changes without tight coupling.

**Implementation:**

```java
public interface ElevatorObserver {
    void update(Elevator elevator);
}

public class DisplayPanel implements ElevatorObserver {
    @Override
    public void update(Elevator elevator) {
        System.out.println("Display: Elevator " + elevator.getId() + 
                          " at Floor " + elevator.getCurrentFloor());
    }
}

public class Elevator {
    private List<ElevatorObserver> observers = new ArrayList<>();
    
    public void notifyObservers() {
        for (ElevatorObserver observer : observers) {
            observer.update(this);
        }
    }
}
```

**Benefits:**
- ✅ Decouples elevator logic from display logic
- ✅ Multiple observers can listen to same elevator
- ✅ Easy to add new observer types (email, SMS, etc.)
- ✅ Publish-subscribe pattern

---

## 🔑 Key Design Decisions

### 1. **Request Queue Management**

```java
private PriorityQueue<Integer> upQueue;      // Min heap (ascending)
private PriorityQueue<Integer> downQueue;    // Max heap (descending)
```

**Why Two Queues?**
- Efficient floor selection in each direction
- Upward requests: Serve lowest floor first (min heap)
- Downward requests: Serve highest floor first (max heap)
- O(log n) insertion, O(1) peek, O(log n) removal

**Alternative:** Single queue with sorting
- Would require O(n log n) sorting on each direction change
- Less efficient for real-time processing

---

### 2. **External vs Internal Requests**

```java
// External: Floor button (UP/DOWN)
ExternalRequest request = new ExternalRequest(floor, direction);

// Internal: Destination selection inside elevator
InternalRequest request = new InternalRequest(currentFloor, destinationFloor);
```

**Why Separate?**
- External requests need elevator assignment
- Internal requests go directly to assigned elevator
- Different validation rules
- Clearer domain modeling

---

### 3. **Singleton Controller**

```java
public class ElevatorController {
    private static ElevatorController instance;
    
    public static synchronized ElevatorController getInstance(int totalFloors) {
        if (instance == null) {
            instance = new ElevatorController(totalFloors);
        }
        return instance;
    }
}
```

**Benefits:**
- ✅ Single point of control
- ✅ Global access
- ✅ Centralized request management

**Trade-offs:**
- ❌ Hard to unit test (mocking difficult)
- ❌ Global state
- ❌ Thread safety overhead

**Production Alternative:** Dependency Injection (Spring, Guice)

---

## ⚖️ Trade-offs

### **1. SCAN vs FCFS Strategy**

**SCAN (Default):**
- ✅ Lower average wait time
- ✅ Better for high traffic
- ✅ Predictable behavior
- ❌ Some requests wait longer

**FCFS:**
- ✅ Simple and fair
- ✅ Good for low traffic
- ❌ Higher average wait time
- ❌ Inefficient movement

**When to Use:**
- **SCAN:** Office buildings during rush hour
- **FCFS:** Residential buildings with low traffic

---

### **2. Priority Queues vs Lists**

**Current (Priority Queues):**
- ✅ O(log n) insertion
- ✅ O(1) peek next floor
- ✅ Automatic sorting
- ❌ O(n) for contains() check

**Alternative (Sorted Lists):**
- ✅ O(1) contains() with Set
- ❌ O(n) insertion
- ❌ Manual sorting

**Decision:** Priority queues are better for real-time request processing.

---

### **3. Synchronous vs Asynchronous Processing**

**Current (Synchronous):**
```java
public void processStep() {
    for (Elevator elevator : elevators) {
        elevator.processStep();
    }
}
```

**Benefits:**
- ✅ Simple to understand
- ✅ Deterministic behavior
- ✅ Easy to debug

**Production (Asynchronous):**
```java
ExecutorService executor = Executors.newFixedThreadPool(elevators.size());
for (Elevator elevator : elevators) {
    executor.submit(() -> elevator.processStep());
}
```

**Benefits:**
- ✅ Parallel processing
- ✅ Better performance
- ❌ Race conditions
- ❌ Complex synchronization

---

## 🧪 Test Coverage

### **Scenarios Tested in Main.java:**

1. ✅ **Basic Movement**
   - Single elevator moves from floor 1 to 5
   - Internal request to floor 8
   - State transitions: IDLE → MOVING_UP → DOOR_OPEN

2. ✅ **Multiple Requests (SCAN)**
   - Multiple simultaneous requests
   - SCAN strategy prioritizes same-direction requests
   - Efficient floor visiting order

3. ✅ **Strategy Comparison**
   - Test all 4 strategies: FCFS, SCAN, LOOK, SSTF
   - Same requests, different assignments
   - Demonstrates strategy switching

4. ✅ **Capacity Management**
   - Board passengers (within capacity)
   - Reject passengers (exceeds capacity)
   - Exit passengers
   - Full elevator handling

5. ✅ **Observer Pattern**
   - Multiple display panels observe one elevator
   - All panels receive updates on movement
   - Demonstrates decoupling

---

## 🚀 How to Compile and Run

### **Option 1: Command Line (No Packages)**
```bash
cd src/
javac enums/*.java model/*.java state/*.java strategy/*.java observer/*.java service/*.java Main.java
java Main
```

### **Option 2: With Package Structure**
```bash
cd 14-Elevator-System/
javac -d bin src/enums/*.java src/model/*.java src/state/*.java src/strategy/*.java src/observer/*.java src/service/*.java src/Main.java
java -cp bin Main
```

### **Expected Output:**
```
========================================
  ELEVATOR SYSTEM DEMO
========================================

============================================================
  SCENARIO 1: Basic Movement
============================================================
✓ Added Elevator E1: Floor 1, IDLE, Load: 0/8
✓ Added Elevator E2: Floor 1, IDLE, Load: 0/8

>>> New Request: Floor 5 going UP
✓ Assigned Elevator E1 (currently at floor 1)

[Lobby Display] Elevator E1: Floor 2 ↑ | Status: MOVING_UP | Load: 0/8
[Lobby Display] Elevator E1: Floor 3 ↑ | Status: MOVING_UP | Load: 0/8
...
```

---

## 📈 Extensions & Improvements

### **1. Add Priority Requests (VIP, Emergency)**

```java
public class PriorityRequest extends Request {
    private int priority;  // 1 = highest
    
    public PriorityRequest(int floor, Direction direction, int priority) {
        super(floor, -1, direction, RequestType.EXTERNAL);
        this.priority = priority;
    }
}

// In ElevatorController
private PriorityQueue<Request> pendingRequests = new PriorityQueue<>(
    Comparator.comparingInt(r -> ((PriorityRequest) r).getPriority())
);
```

---

### **2. Add Express Elevators**

```java
public class ExpressElevator extends Elevator {
    private Set<Integer> expressFloors;  // Only stops at these floors
    
    @Override
    public void addRequest(int floor) {
        if (expressFloors.contains(floor)) {
            super.addRequest(floor);
        } else {
            throw new IllegalArgumentException("Not an express floor");
        }
    }
}
```

---

### **3. Add Energy Optimization**

```java
public class EnergyOptimizedStrategy implements SchedulingStrategy {
    @Override
    public Elevator assignElevator(Request request, List<Elevator> elevators) {
        // Minimize total distance traveled
        // Group nearby requests
        // Consider idle time cost
    }
}
```

---

### **4. Add Analytics & Monitoring**

```java
public class ElevatorAnalytics {
    private Map<String, List<Long>> waitTimes;
    private Map<String, Integer> tripCounts;
    
    public double getAverageWaitTime(String elevatorId) {
        return waitTimes.get(elevatorId).stream()
            .mapToLong(Long::longValue)
            .average()
            .orElse(0.0);
    }
    
    public int getTotalTrips(String elevatorId) {
        return tripCounts.getOrDefault(elevatorId, 0);
    }
    
    public double getSystemEfficiency() {
        // Calculate based on wait times, trips, energy
    }
}
```

---

### **5. Add Emergency Mode**

```java
public class EmergencyState implements ElevatorState {
    @Override
    public void moveDown(Elevator elevator) {
        // Move to ground floor
        elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
        if (elevator.getCurrentFloor() == 1) {
            elevator.setState(new IdleState());
            elevator.openDoor();
        }
    }
    
    // Disable other operations
    @Override
    public void moveUp(Elevator elevator) {
        System.out.println("Emergency mode: Cannot move up");
    }
}

// In ElevatorController
public void activateEmergency() {
    for (Elevator elevator : elevators) {
        elevator.setState(new EmergencyState());
        elevator.setDirection(Direction.DOWN);
    }
}
```

---

## 🎯 Interview Tips

### **What Interviewers Look For:**

1. ✅ **Proper use of design patterns**
   - State pattern for elevator states
   - Strategy pattern for scheduling
   - Observer pattern for notifications

2. ✅ **Efficient data structures**
   - Priority queues for request management
   - Proper queue selection (up/down)

3. ✅ **Clean separation of concerns**
   - Model, State, Strategy, Observer, Service layers
   - Single Responsibility Principle

4. ✅ **Edge case handling**
   - Full elevator
   - Invalid floor
   - No available elevators
   - Capacity limits

5. ✅ **Extensibility**
   - Easy to add new strategies
   - Easy to add new states
   - Easy to add new elevator types

---

### **Common Follow-up Questions:**

**Q: "How would you handle multiple elevators more efficiently?"**

```java
// Zone-based assignment
public class ZoneBasedStrategy implements SchedulingStrategy {
    private Map<Elevator, Set<Integer>> elevatorZones;
    
    @Override
    public Elevator assignElevator(Request request, List<Elevator> elevators) {
        // Assign based on floor zones
        // Elevator 1: Floors 1-5
        // Elevator 2: Floors 6-10
        // Reduces conflicts and wait times
    }
}
```

**Q: "How do you prevent starvation with SSTF?"**

```java
public class AgedSSTFStrategy implements SchedulingStrategy {
    private Map<Request, Long> requestAges;
    
    @Override
    public Elevator assignElevator(Request request, List<Elevator> elevators) {
        // Calculate score = distance + age_penalty
        // Older requests get higher priority
        // Prevents starvation
    }
}
```

**Q: "How would you handle power failure recovery?"**

```java
public class ElevatorController {
    public void saveToPersistence() {
        // Save current state to database
        // Save pending requests
        // Save elevator positions
    }
    
    public void recoverFromFailure() {
        // Load state from database
        // Resume pending requests
        // Verify elevator positions
    }
}
```

**Q: "How do you test this system?"**

```java
@Test
public void testSCANStrategy() {
    ElevatorController controller = ElevatorController.getInstance(10);
    controller.setSchedulingStrategy(new SCANStrategy());
    
    Elevator e1 = new Elevator("E1", 10, 8);
    e1.setCurrentFloor(5);
    e1.setDirection(Direction.UP);
    
    Request request = new ExternalRequest(7, Direction.UP);
    Elevator assigned = controller.assignElevator(request, List.of(e1));
    
    assertEquals(e1, assigned);  // Should assign E1 (same direction, on the way)
}
```

---

## ✅ Checklist Before Interview

- [ ] Can explain all 3 design patterns clearly (State, Strategy, Observer)
- [ ] Can code the solution in <60 minutes
- [ ] Understand trade-offs (SCAN vs FCFS, sync vs async)
- [ ] Can handle follow-up questions (zones, priority, emergency)
- [ ] Code compiles and runs without errors
- [ ] Edge cases handled (capacity, invalid floors, no elevators)
- [ ] Can extend (add new strategies, states, elevator types)
- [ ] Understand time complexity (O(log n) for queue operations)

---

## 📊 Complexity Analysis

### **Time Complexity:**

| Operation | Complexity | Explanation |
|-----------|-----------|-------------|
| Add Request | O(log n) | Priority queue insertion |
| Get Next Floor | O(1) | Priority queue peek |
| Remove Floor | O(log n) | Priority queue removal |
| Assign Elevator | O(m) | m = number of elevators |
| Process Step | O(m) | Process all elevators |

### **Space Complexity:**

| Component | Complexity | Explanation |
|-----------|-----------|-------------|
| Request Queues | O(n) | n = pending requests per elevator |
| Observers | O(k) | k = number of observers per elevator |
| Total | O(m × (n + k)) | m elevators, n requests, k observers |

---

**This solution demonstrates production-quality code with proper design patterns, clean architecture, and extensibility! 🚀**


