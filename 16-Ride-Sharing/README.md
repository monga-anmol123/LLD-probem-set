# Problem 16: Ride Sharing System (Uber/Lyft)

## 🎯 Difficulty: Medium ⭐⭐⭐

## 📝 Problem Statement

Design a ride-sharing platform like Uber or Lyft that connects riders with drivers. The system should support multiple ride types (Economy, Premium, Luxury), dynamic pricing based on demand and distance, real-time ride matching, driver and rider management, ride state transitions, and notifications for status updates.

## 🔍 Functional Requirements (FR)

### FR1: User Management
- Register riders and drivers with profiles
- Manage driver availability (online/offline)
- Track driver ratings and completed rides
- Support multiple vehicle types per driver

### FR2: Ride Request & Matching
- Riders can request rides with pickup and drop-off locations
- System matches available drivers based on proximity and vehicle type
- Support different ride types: Economy, Premium, Luxury
- Handle ride acceptance/rejection by drivers

### FR3: Ride State Management
- Track ride states: REQUESTED → ACCEPTED → STARTED → COMPLETED → CANCELLED
- Allow cancellation by rider or driver (with penalties)
- Handle ride completion with fare calculation

### FR4: Pricing System
- Calculate fare based on distance, time, and ride type
- Apply surge pricing during high demand
- Support promotional discounts
- Display estimated fare before booking

### FR5: Notifications
- Notify riders when driver accepts ride
- Notify riders of driver arrival
- Notify riders when ride starts
- Notify drivers of new ride requests
- Notify both parties on cancellation

### FR6: Payment Processing
- Calculate final fare after ride completion
- Support multiple payment methods (Cash, Card, Wallet)
- Handle driver earnings and commission

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Scalability
- Support thousands of concurrent rides
- Efficient driver-rider matching algorithm
- Handle peak hour traffic

### NFR2: Real-time Updates
- Instant notifications for state changes
- Live location tracking (simulated)
- Quick driver matching (<5 seconds)

### NFR3: Extensibility
- Easy to add new ride types
- Easy to add new pricing strategies
- Support for future features (carpooling, scheduled rides)

### NFR4: Reliability
- Handle edge cases (no drivers available, cancellations)
- Ensure data consistency during state transitions
- Proper error handling and recovery

## 🎨 Design Patterns to Use

### 1. **State Pattern**
- **Where:** Ride state management (REQUESTED, ACCEPTED, STARTED, COMPLETED, CANCELLED)
- **Why:** Ride behavior changes based on state. State pattern encapsulates state-specific behavior and makes transitions explicit.

### 2. **Strategy Pattern**
- **Where:** Pricing calculation (Base pricing, Surge pricing, Discount pricing)
- **Why:** Multiple pricing algorithms that can be switched at runtime. Easy to add new pricing strategies.

### 3. **Observer Pattern**
- **Where:** Notification system for ride updates
- **Why:** Riders and drivers need to be notified of state changes. Observer pattern provides loose coupling between ride and observers.

### 4. **Factory Pattern**
- **Where:** Creating different ride types (Economy, Premium, Luxury)
- **Why:** Centralize ride creation logic. Easy to add new ride types without modifying existing code.

## 📋 Core Entities

### 1. **User** (Abstract)
- Attributes: userId, name, phone, rating
- Subclasses: Rider, Driver

### 2. **Rider** (extends User)
- Attributes: paymentMethod, rideHistory
- Methods: requestRide(), cancelRide(), rateDriver()

### 3. **Driver** (extends User)
- Attributes: vehicle, isAvailable, location, earnings
- Methods: acceptRide(), rejectRide(), startRide(), completeRide()

### 4. **Vehicle**
- Attributes: vehicleId, type (ECONOMY, PREMIUM, LUXURY), licensePlate, model
- Methods: getType()

### 5. **Location**
- Attributes: latitude, longitude, address
- Methods: calculateDistance(Location other)

### 6. **Ride**
- Attributes: rideId, rider, driver, pickupLocation, dropLocation, rideType, state, fare
- Methods: acceptRide(), startRide(), completeRide(), cancelRide()

### 7. **RideState** (Interface)
- Methods: acceptRide(), startRide(), completeRide(), cancelRide()
- Implementations: RequestedState, AcceptedState, StartedState, CompletedState, CancelledState

### 8. **PricingStrategy** (Interface)
- Methods: calculateFare(Ride ride)
- Implementations: BasePricingStrategy, SurgePricingStrategy, DiscountPricingStrategy

### 9. **RideService**
- Attributes: riders, drivers, rides, pricingStrategy
- Methods: requestRide(), matchDriver(), processRide()

### 10. **Observer** (Interface)
- Methods: update(Ride ride, String message)
- Implementations: RiderObserver, DriverObserver

## 🧪 Test Scenarios

### Scenario 1: Complete Ride Flow (Happy Path)
```
1. Rider requests Economy ride
2. System matches nearest available driver
3. Driver accepts ride
4. Driver arrives and starts ride
5. Driver completes ride
6. Fare calculated and payment processed
7. Both parties rate each other
```

### Scenario 2: Ride Cancellation by Rider
```
1. Rider requests ride
2. Driver accepts
3. Rider cancels before ride starts
4. Cancellation penalty applied
5. Driver becomes available again
```

### Scenario 3: Ride Rejection by Driver
```
1. Rider requests ride
2. First driver rejects
3. System matches second driver
4. Second driver accepts
5. Ride proceeds normally
```

### Scenario 4: Surge Pricing
```
1. High demand period detected
2. Surge pricing activated (2x multiplier)
3. Rider requests ride
4. Estimated fare shows surge pricing
5. Rider accepts and completes ride
6. Higher fare calculated
```

### Scenario 5: Multiple Ride Types
```
1. Request Economy ride (sedan)
2. Request Premium ride (SUV)
3. Request Luxury ride (luxury car)
4. Each matched with appropriate driver
5. Different pricing for each type
```

### Scenario 6: No Drivers Available
```
1. Rider requests ride
2. No drivers available in area
3. System notifies rider
4. Ride request remains pending or cancelled
```

### Scenario 7: Observer Pattern - Notifications
```
1. Ride requested → Notify matched driver
2. Ride accepted → Notify rider
3. Ride started → Notify rider
4. Ride completed → Notify both parties
```

## ⏱️ Time Allocation (60-75 minutes)

- **5 mins:** Clarify requirements, identify entities
- **10 mins:** Design class structure (enums, models)
- **15 mins:** Implement State pattern for ride states
- **15 mins:** Implement Strategy pattern for pricing
- **10 mins:** Implement Observer pattern for notifications
- **5 mins:** Implement Factory pattern for ride creation
- **10 mins:** Write RideService with matching logic
- **10 mins:** Write Main.java with comprehensive scenarios

## 💡 Hints

<details>
<summary>Hint 1: State Pattern Structure</summary>

Each state implements the same interface but behaves differently:
```java
public interface RideState {
    void acceptRide(Ride ride);
    void startRide(Ride ride);
    void completeRide(Ride ride);
    void cancelRide(Ride ride);
}

public class RequestedState implements RideState {
    public void acceptRide(Ride ride) {
        // Valid: transition to ACCEPTED
        ride.setState(new AcceptedState());
    }
    
    public void startRide(Ride ride) {
        // Invalid: can't start before accepting
        throw new IllegalStateException("Cannot start ride before acceptance");
    }
}
```
</details>

<details>
<summary>Hint 2: Observer Pattern for Notifications</summary>

Ride is the Subject, Rider and Driver are Observers:
```java
public class Ride extends Subject {
    public void acceptRide() {
        // Change state
        notifyObservers("Driver accepted your ride!");
    }
}

public class RiderObserver implements Observer {
    public void update(Ride ride, String message) {
        System.out.println("Rider notified: " + message);
    }
}
```
</details>

<details>
<summary>Hint 3: Strategy Pattern for Pricing</summary>

```java
public interface PricingStrategy {
    double calculateFare(double distance, double time, RideType type);
}

public class SurgePricingStrategy implements PricingStrategy {
    private double surgeMultiplier;
    
    public double calculateFare(double distance, double time, RideType type) {
        double baseFare = new BasePricingStrategy().calculateFare(distance, time, type);
        return baseFare * surgeMultiplier;
    }
}
```
</details>

<details>
<summary>Hint 4: Driver Matching Algorithm</summary>

Simple proximity-based matching:
```java
public Driver findNearestDriver(Location pickup, RideType rideType) {
    return drivers.stream()
        .filter(d -> d.isAvailable())
        .filter(d -> d.getVehicle().getType() == rideType)
        .min(Comparator.comparingDouble(d -> 
            d.getLocation().calculateDistance(pickup)))
        .orElse(null);
}
```
</details>

<details>
<summary>Hint 5: Factory Pattern for Rides</summary>

```java
public class RideFactory {
    public static Ride createRide(RideType type, Rider rider, 
                                   Location pickup, Location drop) {
        Ride ride = new Ride(generateId(), rider, pickup, drop, type);
        ride.setState(new RequestedState());
        return ride;
    }
}
```
</details>

## ✅ Success Criteria

- [ ] Compiles without errors
- [ ] Uses State pattern correctly for ride states
- [ ] Uses Strategy pattern correctly for pricing
- [ ] Uses Observer pattern correctly for notifications
- [ ] Uses Factory pattern for ride creation
- [ ] Handles all test scenarios
- [ ] Proper state transition validation
- [ ] Clear error messages for invalid operations
- [ ] Driver-rider matching works correctly
- [ ] Fare calculation accurate for all ride types

## 🎓 Key Learning Points

1. **State Pattern:** Best for objects with complex state-dependent behavior
2. **Strategy Pattern:** Best for interchangeable algorithms
3. **Observer Pattern:** Best for event-driven systems with notifications
4. **Factory Pattern:** Best for object creation with variations
5. **Combining Patterns:** Real-world systems use multiple patterns together
6. **State Validation:** Prevent invalid state transitions
7. **Loose Coupling:** Observer pattern decouples ride from notification logic

## 📚 Related Problems

- **Problem 10:** Movie Ticket Booking (similar state management)
- **Problem 12:** Car Rental System (similar vehicle management)
- **Problem 17:** Food Delivery (similar matching and tracking)
- **Problem 20:** Twitter Feed (similar observer pattern usage)

---

**Time to implement! This is a complex problem that showcases multiple patterns working together! 🚗🚀**

