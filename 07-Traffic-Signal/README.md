# Problem 07: Traffic Signal System

## 🎯 Difficulty: Easy ⭐⭐

## 📝 Problem Statement

Design a traffic signal control system that manages traffic lights at an intersection. The system should handle signal state transitions, timing, emergency vehicle priority, and notify observers about state changes.

---

## 🔍 Functional Requirements (FR)

### FR1: Signal Management
- Support 4 directions: North, South, East, West
- Each direction has 3 lights: Red, Yellow, Green
- Only one direction can have Green at a time
- Yellow transition between Green and Red

### FR2: Signal State Transitions
- **Red → Green:** Direct transition when it's this direction's turn
- **Green → Yellow:** After green duration expires
- **Yellow → Red:** After yellow duration expires (brief warning)
- **Red → Red:** Wait state while other directions have green

### FR3: Timing Management
- Configurable durations for each light:
  - Green: 30-60 seconds (default: 45s)
  - Yellow: 3-5 seconds (default: 3s)
  - Red: Variable (depends on other signals)
- Cycle through all directions in round-robin fashion

### FR4: Emergency Mode
- Emergency vehicle detected → Turn all signals Red except emergency direction
- Emergency direction gets immediate Green
- Resume normal operation after emergency passes

### FR5: Pedestrian Crossing
- Pedestrian button press → Extend green or trigger early transition
- Pedestrian signal synchronized with traffic signal
- Walk/Don't Walk indicators

### FR6: Observer Notifications
- Notify displays when signal changes
- Notify traffic monitoring system
- Notify pedestrian signals
- Log all state changes

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Safety
- Never allow conflicting directions to have Green simultaneously
- Always have Yellow transition before Red
- Emergency vehicles must have priority

### NFR2: Extensibility
- Easy to add new signal states (e.g., Flashing Yellow for caution)
- Easy to add new directions or intersection types
- Easy to add new observer types

### NFR3: Maintainability
- Clear state machine implementation
- Well-documented state transitions
- Easy to modify timing configurations

### NFR4: Real-time Performance
- State transitions should be immediate (<100ms)
- Accurate timing (no drift over long periods)
- Thread-safe for concurrent operations

### NFR5: Reliability
- Handle edge cases:
  - Multiple emergency vehicles
  - System restart (resume from safe state)
  - Sensor failures (default to safe mode)

---

## 🎨 Design Patterns to Use

### 1. **State Pattern**
- **Where:** Traffic signal states (Red, Yellow, Green)
- **Why:** Clean state transitions, each state handles its own behavior

### 2. **Observer Pattern**
- **Where:** Notify displays, monitoring systems, pedestrian signals
- **Why:** Decouple signal controller from observers, easy to add new observers

### 3. **Singleton Pattern**
- **Where:** TrafficController (one controller per intersection)
- **Why:** Central control point, prevent multiple controllers

---

## 📋 Core Entities

### 1. **TrafficSignal**
- Attributes: `direction`, `currentLight`, `observers`
- Methods: `changeLight()`, `getCurrentLight()`, `addObserver()`, `notifyObservers()`

### 2. **SignalState** (Interface)
- Methods: `enter()`, `exit()`, `getNextState()`, `getDuration()`
- Implementations: `RedState`, `YellowState`, `GreenState`

### 3. **TrafficController** (Singleton)
- Attributes: `signals`, `currentActiveDirection`, `isEmergencyMode`, `config`
- Methods: `start()`, `stop()`, `handleEmergency()`, `nextSignal()`

### 4. **Observer** (Interface)
- Method: `update(TrafficSignal signal, SignalLight newLight)`
- Implementations: `DisplayBoard`, `MonitoringSystem`, `PedestrianSignal`

### 5. **SignalConfig**
- Attributes: `greenDuration`, `yellowDuration`, `cycleOrder`
- Methods: `getGreenDuration()`, `getYellowDuration()`

---

## 🧪 Test Scenarios

### Scenario 1: Normal Operation Cycle
```
1. Initialize intersection with 4 directions
2. Start traffic controller
3. North: Green (45s) → Yellow (3s) → Red
4. East: Green (45s) → Yellow (3s) → Red
5. South: Green (45s) → Yellow (3s) → Red
6. West: Green (45s) → Yellow (3s) → Red
7. Cycle repeats
8. Verify only one direction has Green at a time
```

### Scenario 2: Emergency Vehicle
```
1. Normal operation: North is Green
2. Emergency vehicle detected from South
3. North: Green → Yellow → Red (immediate)
4. South: Red → Green (immediate)
5. Emergency passes
6. Resume normal cycle from next direction (East)
```

### Scenario 3: Observer Notifications
```
1. Register 3 observers: Display, Monitor, Pedestrian
2. Change signal from Red → Green
3. Verify all 3 observers receive update
4. Verify correct signal state in notifications
```

### Scenario 4: Pedestrian Crossing
```
1. North signal is Green (30s remaining)
2. Pedestrian presses button
3. Extend North green by 10s OR trigger early transition
4. Pedestrian signal shows "Walk"
5. After crossing time, pedestrian signal shows "Don't Walk"
```

### Scenario 5: Multiple Directions
```
1. 4-way intersection: N, S, E, W
2. Verify cycle order: N → E → S → W → N
3. Verify timing accuracy
4. Verify no conflicting greens
```

---

## ⏱️ Time Allocation (45 minutes)

- **5 mins:** Clarify requirements, understand state transitions
- **5 mins:** List entities, identify patterns
- **5 mins:** Design state machine diagram
- **25 mins:** Write code (enums → model → state → observer → service → main)
- **5 mins:** Test with demo scenarios

---

## 💡 Hints

<details>
<summary>Hint 1: State Pattern Implementation</summary>

```java
public interface SignalState {
    void enter(TrafficSignal signal);
    void exit(TrafficSignal signal);
    SignalState getNextState();
    int getDurationSeconds();
}

public class GreenState implements SignalState {
    private int duration;
    
    public void enter(TrafficSignal signal) {
        System.out.println(signal.getDirection() + " -> GREEN");
        signal.notifyObservers();
    }
    
    public SignalState getNextState() {
        return new YellowState();
    }
}
```
</details>

<details>
<summary>Hint 2: Observer Pattern</summary>

```java
public interface TrafficObserver {
    void update(TrafficSignal signal, SignalLight newLight);
}

public class TrafficSignal {
    private List<TrafficObserver> observers = new ArrayList<>();
    
    public void addObserver(TrafficObserver observer) {
        observers.add(observer);
    }
    
    public void notifyObservers() {
        for (TrafficObserver observer : observers) {
            observer.update(this, currentLight);
        }
    }
}
```
</details>

<details>
<summary>Hint 3: State Transition Logic</summary>

```java
public void transitionToNextState() {
    currentState.exit(this);
    currentState = currentState.getNextState();
    currentState.enter(this);
    
    // Schedule next transition
    scheduleNextTransition(currentState.getDurationSeconds());
}
```
</details>

<details>
<summary>Hint 4: Emergency Mode</summary>

```java
public void handleEmergency(Direction emergencyDirection) {
    isEmergencyMode = true;
    
    // Turn all signals to Red immediately
    for (TrafficSignal signal : signals.values()) {
        if (!signal.getDirection().equals(emergencyDirection)) {
            signal.forceRed();
        }
    }
    
    // Give emergency direction Green
    signals.get(emergencyDirection).forceGreen();
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Add Turn Arrows**
   - Left turn arrow, right turn arrow
   - Protected vs permitted turns
   - Separate timing for turn signals

2. **Add Flashing Modes**
   - Flashing Yellow: Caution (late night)
   - Flashing Red: Treat as stop sign (maintenance)
   - Automatic mode switching based on time

3. **Add Traffic Sensors**
   - Detect traffic volume
   - Adjust green duration dynamically
   - Skip empty directions

4. **Add Multi-Intersection Coordination**
   - Synchronize multiple intersections
   - Green wave for main roads
   - Network-wide optimization

5. **Add Analytics**
   - Track wait times per direction
   - Measure traffic flow
   - Generate optimization reports

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use State and Observer patterns correctly
- [ ] Handle all test scenarios
- [ ] Never allow conflicting green signals
- [ ] Support emergency vehicle priority
- [ ] Notify all observers on state changes
- [ ] Have accurate timing
- [ ] Handle edge cases (emergency during transition, etc.)
- [ ] Be extensible (easy to add new states/observers)

---

## 📁 File Structure

```
07-Traffic-Signal/
├── README.md (this file)
├── src/
│   ├── enums/
│   │   ├── SignalLight.java
│   │   ├── Direction.java
│   │   └── SignalMode.java
│   ├── model/
│   │   ├── TrafficSignal.java
│   │   ├── SignalConfig.java
│   │   └── Road.java
│   ├── state/
│   │   ├── SignalState.java (interface)
│   │   ├── RedState.java
│   │   ├── YellowState.java
│   │   └── GreenState.java
│   ├── observer/
│   │   ├── TrafficObserver.java (interface)
│   │   ├── DisplayBoard.java
│   │   ├── MonitoringSystem.java
│   │   └── PedestrianSignal.java
│   ├── service/
│   │   └── TrafficController.java
│   └── Main.java
├── SOLUTION.md
└── COMPILATION-GUIDE.md
```

---

**Good luck! Start coding! 🚦**


