# Problem 14: Elevator System

## 🎯 Difficulty: Medium ⭐⭐⭐

## 📝 Problem Statement

Design an elevator control system for a multi-story building that efficiently handles passenger requests, manages multiple elevators, and optimizes travel time using different scheduling strategies.

---

## 🔍 Functional Requirements (FR)

### FR1: Elevator Management
- Support multiple elevators in a building
- Each elevator has: ID, current floor, direction (UP/DOWN/IDLE), capacity
- Track current load (number of passengers)
- Support buildings with N floors (e.g., 10 floors)

### FR2: Request Handling
- **External Requests:** Passengers press UP/DOWN button on a floor
- **Internal Requests:** Passengers select destination floor inside elevator
- Queue and process requests efficiently
- Handle multiple simultaneous requests

### FR3: Elevator States
- **IDLE:** Not moving, no pending requests
- **MOVING_UP:** Moving upward
- **MOVING_DOWN:** Moving downward
- **DOOR_OPEN:** Stopped at floor with doors open
- **DOOR_CLOSED:** Stopped at floor with doors closed
- **MAINTENANCE:** Out of service

### FR4: Movement Logic
- Move one floor at a time
- Stop at floors with pending requests
- Pick up passengers going in the same direction
- Drop off passengers at their destination floors
- Return to IDLE when no pending requests

### FR5: Scheduling Strategies
- **FCFS (First Come First Serve):** Process requests in order received
- **SCAN (Elevator Algorithm):** Continue in current direction, serve all requests, then reverse
- **LOOK:** Like SCAN but reverse when no more requests in current direction
- **Shortest Seek Time First (SSTF):** Serve nearest request first

### FR6: Display & Monitoring
- Display current floor for each elevator
- Show direction indicator (↑/↓)
- Display wait time estimates
- Show elevator status (available/full/maintenance)

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Performance
- Request assignment should complete in O(n) where n = number of elevators
- Minimize average wait time for passengers
- Optimize total travel distance

### NFR2: Scalability
- Support 1-10 elevators per building
- Handle 100+ floors
- Process 1000+ requests per day

### NFR3: Reliability
- Handle edge cases:
  - All elevators full
  - Elevator out of service
  - Emergency stop
  - Power failure recovery
- Prevent deadlocks

### NFR4: Extensibility
- Easy to add new scheduling algorithms
- Support different elevator types (express, freight)
- Add priority requests (VIP, emergency)

### NFR5: Safety
- Don't exceed capacity limits
- Emergency stop functionality
- Proper door open/close timing

---

## 🎨 Design Patterns to Use

### 1. **State Pattern**
- **Where:** Elevator state transitions
- **Why:** Clean state management (IDLE → MOVING_UP → DOOR_OPEN → MOVING_UP)

### 2. **Strategy Pattern**
- **Where:** Request scheduling algorithms
- **Why:** Switch between different scheduling strategies at runtime

### 3. **Observer Pattern**
- **Where:** Notify display panels when elevator status changes
- **Why:** Decouple elevator logic from display/monitoring

### 4. **Singleton Pattern** (Optional)
- **Where:** ElevatorController
- **Why:** Single controller managing all elevators

---

## 📋 Core Entities

### 1. **Elevator**
- Attributes: `id`, `currentFloor`, `direction`, `capacity`, `currentLoad`, `state`
- Methods: `moveUp()`, `moveDown()`, `openDoor()`, `closeDoor()`, `addRequest()`

### 2. **Request**
- Attributes: `requestId`, `sourceFloor`, `destinationFloor`, `direction`, `timestamp`
- Types: `ExternalRequest` (floor button), `InternalRequest` (inside elevator)

### 3. **ElevatorState** (Interface)
- Methods: `moveUp()`, `moveDown()`, `openDoor()`, `closeDoor()`, `handleRequest()`
- Implementations: `IdleState`, `MovingUpState`, `MovingDownState`, `DoorOpenState`

### 4. **SchedulingStrategy** (Interface)
- Methods: `assignElevator(Request, List<Elevator>)`, `getNextFloor(Elevator)`
- Implementations: `FCFSStrategy`, `SCANStrategy`, `LOOKStrategy`, `SSTFStrategy`

### 5. **ElevatorController**
- Attributes: `elevators`, `pendingRequests`, `schedulingStrategy`
- Methods: `requestElevator()`, `processRequests()`, `setStrategy()`

### 6. **Display** (Observer)
- Attributes: `elevatorId`, `currentFloor`, `direction`, `status`
- Methods: `update()`, `showStatus()`

---

## 🧪 Test Scenarios

### Scenario 1: Basic Movement
```
1. Building with 10 floors, 2 elevators
2. Elevator 1 at floor 1, Elevator 2 at floor 5
3. Request from floor 7 going UP
4. System assigns nearest elevator (Elevator 2)
5. Elevator 2 moves from 5 → 6 → 7
6. Picks up passenger
7. Passenger selects floor 9
8. Elevator moves to floor 9
9. Drops off passenger
10. Returns to IDLE
```

### Scenario 2: Multiple Requests (SCAN Strategy)
```
1. Elevator at floor 1, moving UP
2. Requests: Floor 3 (UP), Floor 5 (DOWN), Floor 7 (UP)
3. SCAN: Continues UP, stops at 3, 7 (same direction)
4. Reaches top, reverses to DOWN
5. Stops at floor 5 (DOWN request)
```

### Scenario 3: Capacity Limit
```
1. Elevator capacity: 8 passengers
2. Current load: 7 passengers
3. Request from floor 4 with 3 passengers
4. Elevator stops at floor 4
5. Only 1 passenger can board
6. Remaining 2 wait for next elevator
```

### Scenario 4: Strategy Comparison
```
Test same requests with different strategies:
- FCFS: Average wait time = 45 seconds
- SCAN: Average wait time = 30 seconds
- SSTF: Average wait time = 25 seconds
```

### Scenario 5: Emergency/Maintenance
```
1. 3 elevators operating
2. Elevator 2 goes into MAINTENANCE
3. Redistribute pending requests to Elevators 1 and 3
4. System continues operating with 2 elevators
```

---

## ⏱️ Time Allocation (60 minutes)

- **5 mins:** Clarify requirements, ask questions
- **5 mins:** List entities (Elevator, Request, Controller)
- **5 mins:** Identify patterns (State, Strategy, Observer)
- **35 mins:** Code (enums → model → state → strategy → observer → service → main)
- **10 mins:** Test with demo, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: State Pattern for Elevator</summary>

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
        System.out.println("Elevator " + elevator.getId() + " moved to floor " + elevator.getCurrentFloor());
    }
    
    @Override
    public void openDoor(Elevator elevator) {
        elevator.setState(new DoorOpenState());
        System.out.println("Doors opening at floor " + elevator.getCurrentFloor());
    }
}
```
</details>

<details>
<summary>Hint 2: SCAN Scheduling Strategy</summary>

```java
public class SCANStrategy implements SchedulingStrategy {
    @Override
    public Elevator assignElevator(Request request, List<Elevator> elevators) {
        // Find elevator moving in same direction and closest to request floor
        Elevator best = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Elevator elevator : elevators) {
            if (elevator.getDirection() == request.getDirection() &&
                elevator.getCurrentFloor() <= request.getSourceFloor()) {
                int distance = request.getSourceFloor() - elevator.getCurrentFloor();
                if (distance < minDistance) {
                    minDistance = distance;
                    best = elevator;
                }
            }
        }
        
        return (best != null) ? best : findNearestIdleElevator(request, elevators);
    }
}
```
</details>

<details>
<summary>Hint 3: Request Queue Management</summary>

```java
public class Elevator {
    private Queue<Integer> upQueue = new PriorityQueue<>();  // Min heap
    private Queue<Integer> downQueue = new PriorityQueue<>(Collections.reverseOrder()); // Max heap
    
    public void addRequest(int floor) {
        if (floor > currentFloor) {
            upQueue.offer(floor);
        } else if (floor < currentFloor) {
            downQueue.offer(floor);
        }
    }
    
    public Integer getNextFloor() {
        if (direction == Direction.UP && !upQueue.isEmpty()) {
            return upQueue.poll();
        } else if (direction == Direction.DOWN && !downQueue.isEmpty()) {
            return downQueue.poll();
        }
        return null;
    }
}
```
</details>

<details>
<summary>Hint 4: Observer Pattern for Display</summary>

```java
public interface ElevatorObserver {
    void update(Elevator elevator);
}

public class DisplayPanel implements ElevatorObserver {
    @Override
    public void update(Elevator elevator) {
        System.out.println("Display: Elevator " + elevator.getId() + 
                          " at Floor " + elevator.getCurrentFloor() + 
                          " " + elevator.getDirection());
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
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Add Express Elevators**
   - Skip certain floors
   - Serve only high floors (e.g., 20+)

2. **Add Priority Requests**
   ```java
   public class PriorityRequest extends Request {
       private int priority; // 1 = highest
   }
   ```

3. **Add Energy Optimization**
   - Minimize total distance traveled
   - Group nearby requests

4. **Add Analytics**
   ```java
   public class ElevatorAnalytics {
       public double getAverageWaitTime();
       public int getTotalTrips();
       public double getEnergyConsumption();
   }
   ```

5. **Add Emergency Mode**
   - All elevators go to ground floor
   - Disable new requests

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use State, Strategy, Observer patterns correctly
- [ ] Handle all test scenarios
- [ ] Implement at least 2 scheduling strategies
- [ ] Manage elevator states properly
- [ ] Handle capacity limits
- [ ] Process requests efficiently
- [ ] Display elevator status updates
- [ ] Be extensible (easy to add new strategies/states)

---

## 📁 File Structure

```
14-Elevator-System/
├── README.md (this file)
├── src/
│   ├── enums/
│   │   ├── Direction.java
│   │   ├── ElevatorStatus.java
│   │   └── RequestType.java
│   ├── model/
│   │   ├── Elevator.java
│   │   ├── Request.java
│   │   ├── ExternalRequest.java
│   │   └── InternalRequest.java
│   ├── state/
│   │   ├── ElevatorState.java
│   │   ├── IdleState.java
│   │   ├── MovingUpState.java
│   │   ├── MovingDownState.java
│   │   └── DoorOpenState.java
│   ├── strategy/
│   │   ├── SchedulingStrategy.java
│   │   ├── FCFSStrategy.java
│   │   ├── SCANStrategy.java
│   │   └── LOOKStrategy.java
│   ├── observer/
│   │   ├── ElevatorObserver.java
│   │   └── DisplayPanel.java
│   ├── service/
│   │   └── ElevatorController.java
│   └── Main.java
└── SOLUTION.md
```

---

**Good luck! Focus on State transitions and Strategy selection! 🚀**


