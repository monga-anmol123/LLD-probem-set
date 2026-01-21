# Solution: Ride Sharing System (Uber/Lyft)

## ✅ Complete Implementation

This folder contains a fully working ride-sharing platform demonstrating **State**, **Strategy**, **Observer**, and **Factory** design patterns with comprehensive ride management, dynamic pricing, and real-time notifications.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                         Main.java                            │
│                    (Demo/Entry Point)                        │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┬───────────────┐
        │               │               │               │
        ▼               ▼               ▼               ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐ ┌────────────┐
│   State      │ │  Strategy   │ │  Observer  │ │  Factory   │
│              │ │             │ │            │ │            │
│ Ride         │ │ Pricing     │ │ Rider/     │ │ Ride       │
│ States       │ │ Strategies  │ │ Driver     │ │ Factory    │
│              │ │             │ │ Observers  │ │            │
└──────────────┘ └─────────────┘ └────────────┘ └────────────┘
        │               │               │               │
        └───────────────┼───────────────┼───────────────┘
                        │               │
                        ▼               ▼
                ┌─────────────┐ ┌─────────────┐
                │   Service   │ │    Model    │
                │             │ │             │
                │ Ride        │ │ Ride,       │
                │ Service     │ │ Driver,     │
                │             │ │ Rider,      │
                │             │ │ Location    │
                └─────────────┘ └─────────────┘
```

---

## 📦 Package Structure

```
src/
├── enums/                          # Type-safe enumerations
│   ├── RideType.java              # ECONOMY, PREMIUM, LUXURY
│   ├── RideStatus.java            # REQUESTED, ACCEPTED, STARTED, etc.
│   ├── PaymentMethod.java         # CASH, CARD, WALLET, UPI
│   └── VehicleType.java           # SEDAN, SUV, LUXURY_SEDAN, etc.
│
├── model/                          # Domain entities
│   ├── Location.java              # GPS coordinates with distance calculation
│   ├── Vehicle.java               # Vehicle details
│   ├── User.java                  # Abstract base class
│   ├── Rider.java                 # Rider with payment method
│   ├── Driver.java                # Driver with vehicle and earnings
│   └── Ride.java                  # Ride with State & Observer patterns
│
├── state/                          # State Pattern
│   ├── RideState.java             # Interface
│   ├── RequestedState.java        # Initial state
│   ├── AcceptedState.java         # Driver accepted
│   ├── StartedState.java          # Ride in progress
│   ├── CompletedState.java        # Ride finished
│   └── CancelledState.java        # Ride cancelled
│
├── strategy/                       # Strategy Pattern
│   ├── PricingStrategy.java       # Interface
│   ├── BasePricingStrategy.java   # Standard pricing
│   ├── SurgePricingStrategy.java  # Surge multiplier
│   └── DiscountPricingStrategy.java # Promotional discount
│
├── observer/                       # Observer Pattern
│   ├── Observer.java              # Interface
│   ├── Subject.java               # Abstract subject
│   ├── RiderObserver.java         # Rider notifications
│   └── DriverObserver.java        # Driver notifications
│
├── factory/                        # Factory Pattern
│   └── RideFactory.java           # Ride creation
│
├── service/                        # Business logic
│   └── RideService.java           # Main service (Singleton)
│
└── Main.java                       # Demo with 9 scenarios
```

---

## 🎨 Design Patterns Explained

### 1. **State Pattern** (Ride State Management)

**Purpose:** Manage ride lifecycle with state-specific behavior and valid transitions.

**Problem Solved:**
```java
// ❌ BAD: Giant if-else with all state logic
public void completeRide() {
    if (status == REQUESTED) {
        throw new Exception("Can't complete before accepting");
    } else if (status == ACCEPTED) {
        throw new Exception("Can't complete before starting");
    } else if (status == STARTED) {
        // Complete logic
    } else if (status == COMPLETED) {
        throw new Exception("Already completed");
    }
    // Messy, hard to maintain
}
```

**✅ SOLUTION: State Pattern**
```java
// Each state knows what it can do
public interface RideState {
    void acceptRide(Ride ride);
    void startRide(Ride ride);
    void completeRide(Ride ride);
    void cancelRide(Ride ride);
}

public class StartedState implements RideState {
    public void completeRide(Ride ride) {
        // Valid transition
        ride.setState(new CompletedState());
        // Calculate fare, update earnings, notify observers
    }
    
    public void startRide(Ride ride) {
        // Invalid transition
        throw new IllegalStateException("Ride already started");
    }
}

// In Ride class
public void completeRide() {
    state.completeRide(this); // Delegate to current state
}
```

**State Transitions:**
```
REQUESTED → ACCEPTED → STARTED → COMPLETED
    ↓           ↓          ↓
CANCELLED   CANCELLED  CANCELLED
```

**Benefits:**
- ✅ Each state encapsulates its own behavior
- ✅ Easy to add new states
- ✅ Prevents invalid transitions
- ✅ Clean, maintainable code

---

### 2. **Strategy Pattern** (Pricing Calculation)

**Purpose:** Interchangeable pricing algorithms (base, surge, discount).

**Implementation:**
```java
public interface PricingStrategy {
    double calculateFare(double distance, double time, RideType type);
}

public class BasePricingStrategy implements PricingStrategy {
    public double calculateFare(double distance, double time, RideType type) {
        double fare = BASE_FARE + (distance * PER_KM) + (time * PER_MIN);
        return fare * type.getPriceMultiplier();
    }
}

public class SurgePricingStrategy implements PricingStrategy {
    private double surgeMultiplier; // 1.5x, 2.0x, etc.
    
    public double calculateFare(double distance, double time, RideType type) {
        double baseFare = new BasePricingStrategy().calculateFare(...);
        return baseFare * surgeMultiplier;
    }
}
```

**Usage:**
```java
// Switch pricing at runtime
service.setDefaultPricingStrategy(new BasePricingStrategy());
// High demand detected
service.setDefaultPricingStrategy(new SurgePricingStrategy(2.0)); // 2x surge
// Promotion active
service.setDefaultPricingStrategy(new DiscountPricingStrategy(0.30)); // 30% off
```

**Benefits:**
- ✅ Easy to add new pricing strategies
- ✅ Runtime switching (surge pricing during peak hours)
- ✅ Open-Closed Principle
- ✅ Composable (DiscountStrategy wraps BaseStrategy)

---

### 3. **Observer Pattern** (Notifications)

**Purpose:** Notify riders and drivers of ride status changes.

**Implementation:**
```java
// Subject (Observable)
public abstract class Subject {
    private List<Observer> observers = new ArrayList<>();
    
    public void attach(Observer observer) {
        observers.add(observer);
    }
    
    protected void notifyObservers(Ride ride, String message) {
        for (Observer observer : observers) {
            observer.update(ride, message);
        }
    }
}

// Ride extends Subject
public class Ride extends Subject {
    public void acceptRide() {
        state.acceptRide(this);
        notifyObservers(this, "Driver accepted your ride!");
    }
}

// Observers
public class RiderObserver implements Observer {
    public void update(Ride ride, String message) {
        System.out.println("📱 [Rider] " + message);
    }
}
```

**Benefits:**
- ✅ Loose coupling (Ride doesn't know about specific observers)
- ✅ Easy to add new notification channels (SMS, Push, Email)
- ✅ Multiple observers can listen to same event
- ✅ Real-time updates

---

### 4. **Factory Pattern** (Ride Creation)

**Purpose:** Centralize ride creation with consistent initialization.

**Implementation:**
```java
public class RideFactory {
    public static Ride createRide(Rider rider, Location pickup, 
                                  Location drop, RideType type) {
        String rideId = generateId();
        Ride ride = new Ride(rideId, rider, pickup, drop, type);
        
        // Consistent initialization
        ride.setState(new RequestedState());
        ride.setPricingStrategy(new BasePricingStrategy());
        
        return ride;
    }
}
```

**Benefits:**
- ✅ Single place for ride creation logic
- ✅ Ensures consistent initialization
- ✅ Easy to modify creation logic
- ✅ Hides complexity from clients

---

## 🔑 Key Design Decisions

### 1. **Location Distance Calculation**

Uses Haversine formula for accurate distance:
```java
public double calculateDistance(Location other) {
    // Convert to radians
    double lat1 = Math.toRadians(this.latitude);
    double lat2 = Math.toRadians(other.latitude);
    
    // Haversine formula
    double a = Math.sin(dLat/2) * Math.sin(dLat/2) + ...
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    
    return EARTH_RADIUS_KM * c;
}
```

### 2. **Driver Matching Algorithm**

Proximity-based matching with vehicle type filter:
```java
private Driver findNearestDriver(Location pickup, RideType rideType) {
    return drivers.stream()
        .filter(Driver::isAvailable)
        .filter(d -> d.getVehicle().getType().getRideType() == rideType)
        .min(Comparator.comparingDouble(d -> 
            d.getCurrentLocation().calculateDistance(pickup)))
        .orElse(null);
}
```

### 3. **Commission Model**

80-20 split (driver gets 80%, platform gets 20%):
```java
double fare = ride.calculateFare();
double driverEarnings = fare * 0.80;
driver.addEarnings(driverEarnings);
```

### 4. **Rating System**

Weighted average for continuous ratings:
```java
public void updateRating(double newRating) {
    totalRatings++;
    rating = ((rating * (totalRatings - 1)) + newRating) / totalRatings;
}
```

---

## ⚖️ Trade-offs

### **1. State Pattern**

**✅ Pros:**
- Clean state-specific behavior
- Prevents invalid transitions
- Easy to add new states
- Testable independently

**❌ Cons:**
- More classes (one per state)
- State transitions scattered across state classes
- Can be overkill for simple state machines

**When to Use:**
- Complex state-dependent behavior
- Many state transitions
- Need to prevent invalid transitions

---

### **2. In-Memory Storage**

**Current:** Lists for riders, drivers, rides

**✅ Pros:**
- Fast (O(1) for most operations)
- Simple implementation
- No database setup needed

**❌ Cons:**
- Lost on restart
- Not scalable (memory limit)
- No persistence

**Production Solution:**
- Database (PostgreSQL, MongoDB)
- Redis for real-time data (driver locations)
- Message queue (Kafka) for notifications

---

### **3. Synchronous Processing**

**Current:** All operations are synchronous

**✅ Pros:**
- Simple to implement
- Easy to debug
- Predictable flow

**❌ Cons:**
- Blocks on long operations
- Not scalable for high concurrency

**Production Solution:**
```java
public CompletableFuture<Ride> requestRideAsync(...) {
    return CompletableFuture.supplyAsync(() -> {
        return requestRide(...);
    });
}
```

---

## 🧪 Test Coverage

### **Scenarios Tested in Main.java:**

1. ✅ **Complete Ride Flow** - Happy path (request → accept → start → complete)
2. ✅ **Premium Ride** - SUV with higher pricing
3. ✅ **Luxury Ride** - Luxury vehicle with 2.5x multiplier
4. ✅ **Ride Cancellation** - Cancel after acceptance with penalty
5. ✅ **Surge Pricing** - 2x multiplier during high demand
6. ✅ **Discount Pricing** - 30% promotional discount
7. ✅ **No Drivers Available** - Handle gracefully
8. ✅ **Invalid State Transition** - Prevent starting before accepting
9. ✅ **Multiple Concurrent Rides** - 3 simultaneous rides

---

## 🚀 How to Compile and Run

### **Command Line:**
```bash
cd 16-Ride-Sharing/src/
javac enums/*.java model/*.java observer/*.java state/*.java strategy/*.java factory/*.java service/*.java Main.java
java Main
```

### **Expected Output:**
```
========================================
  🚗 RIDE SHARING SYSTEM DEMO 🚗
========================================

✅ Rider registered: Rider[R001, Alice Johnson, Rating: 5.0⭐]
✅ Driver registered: Driver[D001, David Wilson, Toyota Camry, Rating: 5.0⭐]

🚕 Requesting ECONOMY ride...
✅ Ride matched! Driver: David Wilson
   Distance: 1.42 km
   Estimated Fare: $5.16

✅ Driver accepted the ride!
📱 [Rider Alice Johnson] Driver David Wilson accepted your ride!
📱 [Driver David Wilson] Driver David Wilson accepted your ride!

[... 9 comprehensive scenarios ...]

========================================
  RIDE SERVICE STATISTICS
========================================

📊 Riders: 3
🚗 Drivers: 4
🚕 Total Rides: 10
   Completed: 9
   Cancelled: 1
   Total Revenue: $78.74
```

---

## 📈 Extensions & Improvements

### **1. Real-Time Location Tracking**
```java
public class LocationTracker {
    public void updateDriverLocation(Driver driver, Location newLocation) {
        driver.updateLocation(newLocation);
        notifyNearbyRiders(driver);
    }
}
```

### **2. Ride Pooling (Carpooling)**
```java
public class PooledRide extends Ride {
    private List<Rider> riders;
    private List<Location> stops;
    
    public void addRider(Rider rider, Location pickup, Location drop) {
        riders.add(rider);
        optimizeRoute();
    }
}
```

### **3. Scheduled Rides**
```java
public class ScheduledRide extends Ride {
    private LocalDateTime scheduledTime;
    
    public void scheduleFor(LocalDateTime time) {
        this.scheduledTime = time;
    }
}
```

### **4. Driver Preferences**
```java
public class DriverPreferences {
    private Set<RideType> preferredRideTypes;
    private double minimumFare;
    private int maxDistance;
}
```

---

## 🎯 Interview Tips

### **What Interviewers Look For:**

1. ✅ **Correct use of State pattern** (state-specific behavior, transitions)
2. ✅ **Correct use of Strategy pattern** (interchangeable algorithms)
3. ✅ **Correct use of Observer pattern** (loose coupling, notifications)
4. ✅ **Correct use of Factory pattern** (centralized creation)
5. ✅ **Clean separation** (model, service, state, strategy, observer)
6. ✅ **Edge case handling** (no drivers, invalid states)

### **Common Follow-up Questions:**

**Q: "How would you handle concurrent ride requests?"**
```java
// Option 1: Synchronized methods
public synchronized Ride requestRide(...) { ... }

// Option 2: Lock per driver
private final Map<Driver, ReentrantLock> driverLocks = new ConcurrentHashMap<>();

// Option 3: Optimistic locking with version numbers
public class Driver {
    private AtomicInteger version;
    
    public boolean tryAssign() {
        return version.compareAndSet(expected, updated);
    }
}
```

**Q: "How would you scale to millions of users?"**
```
1. Database sharding (by geographic region)
2. Redis for real-time driver locations
3. Message queue (Kafka) for async processing
4. Microservices architecture:
   - Ride Service
   - Pricing Service
   - Notification Service
   - Payment Service
5. Load balancers for high availability
6. CDN for static assets
```

**Q: "How would you add a new ride state (e.g., DRIVER_ARRIVED)?"**
```java
// Just create a new state class!
public class ArrivedState implements RideState {
    public void startRide(Ride ride) {
        // Valid transition
        ride.setState(new StartedState());
    }
    
    public void cancelRide(Ride ride) {
        // Valid transition
        ride.setState(new CancelledState());
    }
}

// Update AcceptedState to transition to ArrivedState
public class AcceptedState implements RideState {
    public void arriveAtPickup(Ride ride) {
        ride.setState(new ArrivedState());
    }
}
```

---

## ✅ Checklist Before Interview

- [ ] Can explain all 4 design patterns clearly
- [ ] Can code State pattern from scratch (<15 mins)
- [ ] Can code Strategy pattern from scratch (<10 mins)
- [ ] Can code Observer pattern from scratch (<10 mins)
- [ ] Understand trade-offs (in-memory vs database, sync vs async)
- [ ] Can handle follow-up questions (concurrency, scaling)
- [ ] Code compiles and runs without errors
- [ ] Can extend (add new states, pricing strategies, observers)

---

**This solution demonstrates production-quality code with 4 design patterns working together seamlessly! 🚗🚀**

