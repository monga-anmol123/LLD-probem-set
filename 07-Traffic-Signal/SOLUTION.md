# Solution: Traffic Signal System

## 📊 Overview

This solution implements a traffic signal control system using **State Pattern** for signal state management and **Observer Pattern** for notifications. The system safely manages traffic flow at a 4-way intersection with support for emergency vehicles.

---

## 🎨 Design Patterns Used

### 1. **State Pattern** ⭐

**Purpose:** Manage traffic signal state transitions cleanly

**Implementation:**
- `SignalState` interface with `RedState`, `YellowState`, `GreenState` implementations
- Each state knows its next state and duration
- State transitions are encapsulated within state classes

**Benefits:**
- Clean separation of state-specific behavior
- Easy to add new states (e.g., FlashingYellow)
- State transitions are explicit and safe
- Eliminates complex if-else chains

**Example:**
```java
public class GreenState implements SignalState {
    public SignalState getNextState() {
        return new YellowState(); // Green always → Yellow
    }
}

public class YellowState implements SignalState {
    public SignalState getNextState() {
        return new RedState(); // Yellow always → Red
    }
}
```

**Why State Pattern Here?**
- Traffic signals have well-defined states with specific behaviors
- State transitions follow strict rules (Green → Yellow → Red, never Green → Red)
- Each state has different duration and behavior
- Safety-critical: wrong transitions could cause accidents

---

### 2. **Observer Pattern** ⭐

**Purpose:** Notify multiple systems when signal state changes

**Implementation:**
- `TrafficObserver` interface
- Concrete observers: `DisplayBoard`, `MonitoringSystem`, `PedestrianSignal`
- `TrafficSignal` maintains list of observers and notifies them on state change

**Benefits:**
- Loose coupling between signal and observers
- Easy to add new observer types
- Multiple systems can react to same event
- Observers can be added/removed at runtime

**Example:**
```java
public class TrafficSignal {
    private List<TrafficObserver> observers;
    
    public void notifyObservers() {
        for (TrafficObserver observer : observers) {
            observer.update(this, currentLight);
        }
    }
}

public class DisplayBoard implements TrafficObserver {
    public void update(TrafficSignal signal, SignalLight newLight) {
        // Update display
    }
}
```

**Why Observer Pattern Here?**
- Multiple systems need to react to signal changes (displays, monitoring, pedestrian signals)
- Decouples signal logic from notification logic
- Real-world traffic systems have many dependent components
- Extensible: easy to add new notification targets

---

### 3. **Singleton Pattern** ⭐

**Purpose:** Ensure only one traffic controller per intersection

**Implementation:**
- Private constructor
- Static instance with synchronized getter
- Single point of control

**Benefits:**
- Prevents multiple controllers (safety hazard)
- Global access point
- Controlled initialization

**Example:**
```java
public class TrafficController {
    private static TrafficController instance;
    
    private TrafficController() { }
    
    public static synchronized TrafficController getInstance() {
        if (instance == null) {
            instance = new TrafficController();
        }
        return instance;
    }
}
```

**Why Singleton Here?**
- Only one controller should manage an intersection
- Multiple controllers could cause conflicting signals
- Centralized control is safer and easier to manage

---

## 🏗️ Architecture

### **Layer Structure:**

```
┌─────────────────────────────────────────┐
│           Main (Demo)                   │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│     Service Layer (TrafficController)   │  ← Singleton
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│     Model Layer (TrafficSignal)         │  ← Observer Subject
└─────────────────────────────────────────┘
         ↓                        ↓
┌──────────────────┐    ┌──────────────────┐
│  State Pattern   │    │ Observer Pattern │
│  - RedState      │    │ - DisplayBoard   │
│  - YellowState   │    │ - MonitoringSystem│
│  - GreenState    │    │ - PedestrianSignal│
└──────────────────┘    └──────────────────┘
```

---

## 🔄 State Transition Diagram

```
┌─────────┐
│   RED   │ ←─────────────────┐
└─────────┘                    │
     ↓                         │
     │ (When it's turn)        │
     ↓                         │
┌─────────┐                    │
│  GREEN  │                    │
└─────────┘                    │
     ↓                         │
     │ (After duration)        │
     ↓                         │
┌─────────┐                    │
│ YELLOW  │                    │
└─────────┘                    │
     ↓                         │
     │ (After 3s)              │
     └─────────────────────────┘

Emergency Override:
  Any State → RED (all except emergency direction)
  Emergency Direction → GREEN (immediate)
```

---

## 📋 Class Responsibilities

### **TrafficController (Singleton)**
- **Responsibility:** Coordinate all signals, manage cycle
- **Key Methods:**
  - `initializeIntersection()` - Setup signals
  - `start()` / `stop()` - Control system
  - `nextSignal()` - Move to next direction
  - `handleEmergency()` - Override for emergency
  - `displayStatus()` - Show current state

### **TrafficSignal (Subject)**
- **Responsibility:** Manage single direction's signal, notify observers
- **Key Methods:**
  - `transitionToNextState()` - State pattern transition
  - `addObserver()` / `notifyObservers()` - Observer pattern
  - `forceRed()` / `forceGreen()` - Emergency overrides

### **SignalState (Strategy Interface)**
- **Responsibility:** Define state behavior
- **Implementations:** RedState, YellowState, GreenState
- **Key Methods:**
  - `enter()` / `exit()` - State lifecycle
  - `getNextState()` - Transition logic
  - `getDurationSeconds()` - Timing

### **TrafficObserver (Observer Interface)**
- **Responsibility:** React to signal changes
- **Implementations:** DisplayBoard, MonitoringSystem, PedestrianSignal
- **Key Method:** `update(signal, newLight)`

---

## 🎯 Key Design Decisions

### **1. Why State Pattern over Simple Enum?**

❌ **Without State Pattern:**
```java
// Complex switch statements everywhere
switch (currentLight) {
    case RED:
        if (isMyTurn) currentLight = GREEN;
        break;
    case GREEN:
        currentLight = YELLOW;
        break;
    case YELLOW:
        currentLight = RED;
        break;
}
```

✅ **With State Pattern:**
```java
// Clean, encapsulated
currentState.exit(this);
currentState = currentState.getNextState();
currentState.enter(this);
```

**Advantages:**
- Each state is a separate class with its own logic
- Easy to add new states (FlashingYellow, FlashingRed)
- State-specific behavior is encapsulated
- Compile-time safety for transitions

---

### **2. Why Observer Pattern over Direct Calls?**

❌ **Without Observer Pattern:**
```java
// Tight coupling
public void changeLight() {
    currentLight = GREEN;
    displayBoard.update();
    monitoringSystem.log();
    pedestrianSignal.update();
    // Add new system? Modify this method!
}
```

✅ **With Observer Pattern:**
```java
// Loose coupling
public void changeLight() {
    currentLight = GREEN;
    notifyObservers(); // All observers updated automatically
}
```

**Advantages:**
- Add new observers without modifying TrafficSignal
- Observers can be added/removed at runtime
- Signal doesn't know about observer implementations
- Follows Open/Closed Principle

---

### **3. Safety Guarantees**

**Critical Safety Rules Enforced:**

1. **Never skip Yellow:** Green → Yellow → Red (never Green → Red)
   ```java
   public class GreenState {
       public SignalState getNextState() {
           return new YellowState(); // Always go through yellow
       }
   }
   ```

2. **Only one Green at a time:**
   ```java
   private void activateDirection(Direction direction) {
       // Force all others to RED first
       for (TrafficSignal signal : signals.values()) {
           if (!signal.getDirection().equals(direction)) {
               signal.forceRed();
           }
       }
       // Then activate this direction
       signal.transitionToNextState();
   }
   ```

3. **Emergency priority:**
   ```java
   public void handleEmergency(Direction emergencyDirection) {
       // All signals → RED
       // Emergency direction → GREEN
   }
   ```

---

## 🧪 Test Scenarios Covered

### ✅ **Scenario 1: Basic Operation**
- Initialize 4-way intersection
- Cycle through all directions
- Verify state transitions
- **Result:** All signals transition correctly

### ✅ **Scenario 2: Observer Pattern**
- Register multiple observers
- Trigger state change
- Verify all observers notified
- **Result:** All observers receive updates

### ✅ **Scenario 3: Emergency Vehicle**
- Normal operation
- Emergency detected
- All signals → RED except emergency
- Resume normal operation
- **Result:** Emergency handled safely

### ✅ **Scenario 4: Complete Cycle**
- Run full cycle (N → E → S → W → N)
- Verify round-robin order
- Check timing accuracy
- **Result:** Cycle completes correctly

### ✅ **Scenario 5: Custom Configuration**
- Different timing for main road
- Verify custom durations applied
- **Result:** Configurable timing works

---

## 🚀 Extensibility

### **Easy to Add:**

1. **New Signal States:**
   ```java
   public class FlashingYellowState implements SignalState {
       // For late-night caution mode
   }
   ```

2. **New Observers:**
   ```java
   public class TrafficCamera implements TrafficObserver {
       // Record violations
   }
   ```

3. **Turn Signals:**
   ```java
   public class TurnSignal extends TrafficSignal {
       // Left/right turn arrows
   }
   ```

4. **Multi-Intersection Coordination:**
   ```java
   public class NetworkController {
       private List<TrafficController> intersections;
       // Coordinate multiple intersections
   }
   ```

---

## ⚡ Performance Considerations

### **Time Complexity:**
- State transition: O(1)
- Notify observers: O(n) where n = number of observers
- Find next direction: O(1) (index-based)

### **Space Complexity:**
- O(d) where d = number of directions
- O(o) where o = number of observers per signal

### **Thread Safety:**
- Singleton uses synchronized getInstance()
- In production, would need:
  - Locks for state transitions
  - Thread-safe observer list
  - Atomic state changes

---

## 🔧 Production Considerations

### **What's Missing (Intentionally Simplified):**

1. **Actual Timing:**
   - Current: Simulated with print statements
   - Production: Use `ScheduledExecutorService` or Timer

2. **Thread Safety:**
   - Current: Single-threaded demo
   - Production: Concurrent access protection

3. **Persistence:**
   - Current: In-memory only
   - Production: Save state to recover from crashes

4. **Hardware Integration:**
   - Current: Software-only
   - Production: Interface with actual traffic lights

5. **Sensors:**
   - Current: Manual control
   - Production: Vehicle detection sensors

---

## 📊 Trade-offs

### **State Pattern:**
✅ **Pros:**
- Clean, maintainable code
- Easy to add states
- Type-safe transitions

❌ **Cons:**
- More classes (one per state)
- Slight overhead vs simple enum

**Verdict:** Worth it for maintainability and safety

---

### **Observer Pattern:**
✅ **Pros:**
- Loose coupling
- Extensible
- Multiple observers

❌ **Cons:**
- Notification overhead
- Order of notifications not guaranteed

**Verdict:** Essential for real-world traffic systems

---

### **Singleton Pattern:**
✅ **Pros:**
- Single point of control
- Global access
- Prevents conflicts

❌ **Cons:**
- Global state
- Testing challenges
- Thread safety concerns

**Verdict:** Appropriate for this use case (one controller per intersection)

---

## 🎓 Learning Points

### **When to Use State Pattern:**
- Object behavior changes based on internal state
- Many conditional statements based on state
- State transitions are well-defined
- Safety-critical state management

### **When to Use Observer Pattern:**
- One-to-many dependency
- Multiple objects need to react to changes
- Loose coupling desired
- Dynamic subscription needed

### **When to Use Singleton:**
- Exactly one instance needed
- Global access point required
- Controlled initialization important

---

## 🏆 Interview Tips

### **What Interviewers Look For:**

1. **Safety First:**
   - Did you ensure no conflicting green signals?
   - Did you always transition through yellow?

2. **Pattern Application:**
   - Did you use patterns appropriately?
   - Can you explain why you chose each pattern?

3. **Extensibility:**
   - Can you easily add new features?
   - Is the code open for extension?

4. **Edge Cases:**
   - Emergency during transition?
   - Multiple emergencies?
   - System restart?

5. **Real-World Thinking:**
   - Did you consider timing accuracy?
   - Did you think about hardware integration?
   - Did you handle failures gracefully?

---

## 📝 Common Mistakes to Avoid

❌ **Mistake 1:** Using simple if-else instead of State Pattern
- Makes code hard to maintain
- Easy to introduce bugs

❌ **Mistake 2:** Tight coupling between signal and observers
- Hard to add new observers
- Violates Open/Closed Principle

❌ **Mistake 3:** Allowing direct Green → Red transition
- Safety hazard
- Always go through Yellow

❌ **Mistake 4:** Not handling emergency override
- Real-world requirement
- Shows incomplete thinking

❌ **Mistake 5:** Forgetting to ensure only one Green at a time
- Critical safety violation
- Could cause accidents

---

## ✅ Success Criteria Met

- [x] State Pattern correctly implemented
- [x] Observer Pattern correctly implemented
- [x] Singleton Pattern correctly implemented
- [x] Safe state transitions (always through Yellow)
- [x] Only one Green signal at a time
- [x] Emergency vehicle priority
- [x] Multiple observers supported
- [x] Configurable timing
- [x] Complete cycle functionality
- [x] Extensible design
- [x] Clean, readable code
- [x] Comprehensive demo scenarios

---

**Time to Implement:** 45-60 minutes  
**Lines of Code:** ~600 lines  
**Design Patterns:** 3 (State, Observer, Singleton)  
**Test Scenarios:** 5 comprehensive scenarios

---

**This solution demonstrates production-quality design for a safety-critical system!** 🚦✅


