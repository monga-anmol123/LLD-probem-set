# Solution: Parking Lot System

## ✅ Complete Implementation

This folder contains a fully working parking lot system demonstrating Factory, Singleton, and Strategy design patterns.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                         Main.java                            │
│                    (Demo/Entry Point)                        │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
        ▼               ▼               ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐
│   Factory    │ │   Service   │ │  Strategy  │
│              │ │             │ │            │
│ Vehicle      │ │ Parking     │ │ Pricing    │
│ Factory      │ │ Lot         │ │ Strategy   │
│              │ │ (Singleton) │ │            │
└──────────────┘ └─────────────┘ └────────────┘
        │               │               │
        └───────────────┼───────────────┘
                        │
                        ▼
                ┌─────────────┐
                │    Model    │
                │             │
                │  Vehicle    │
                │  Parking    │
                │  Spot       │
                │  Ticket     │
                │  Floor      │
                └─────────────┘
```

---

## 📦 Package Structure

```
src/
├── enums/                  # Type-safe enumerations
│   ├── VehicleType.java   # CAR, BIKE, TRUCK
│   ├── SpotType.java      # COMPACT, LARGE, HANDICAPPED, BIKE
│   └── ParkingSpotStatus.java  # AVAILABLE, OCCUPIED, OUT_OF_SERVICE
│
├── model/                  # Domain entities
│   ├── Vehicle.java       # Abstract base class
│   ├── Car.java           # Concrete vehicle type
│   ├── Bike.java          # Concrete vehicle type
│   ├── Truck.java         # Concrete vehicle type
│   ├── ParkingSpot.java   # Spot with status and rules
│   ├── ParkingTicket.java # Ticket with fee calculation
│   └── ParkingFloor.java  # Floor with multiple spots
│
├── factory/                # Factory Pattern
│   └── VehicleFactory.java # Centralized vehicle creation
│
├── strategy/               # Strategy Pattern
│   ├── PricingStrategy.java        # Interface
│   ├── HourlyPricingStrategy.java  # Hourly rates
│   └── FlatPricingStrategy.java    # Flat rate
│
├── service/                # Business logic
│   ├── Payment.java        # Payment interface
│   ├── CashPayment.java    # Cash payment impl
│   ├── CardPayment.java    # Card payment impl
│   └── ParkingLot.java     # Main service (Singleton)
│
└── Main.java               # Demo application
```

---

## 🎨 Design Patterns Explained

### 1. **Factory Pattern** (VehicleFactory)

**Purpose:** Centralize object creation logic

**Implementation:**
```java
public class VehicleFactory {
    public static Vehicle createVehicle(VehicleType type, String licensePlate) {
        switch (type) {
            case CAR: return new Car(licensePlate);
            case BIKE: return new Bike(licensePlate);
            case TRUCK: return new Truck(licensePlate);
        }
    }
}
```

**Benefits:**
- ✅ Client doesn't need to know concrete classes
- ✅ Easy to add new vehicle types (just add case)
- ✅ Single place to modify creation logic

**Usage:**
```java
Vehicle car = VehicleFactory.createVehicle(VehicleType.CAR, "ABC-123");
```

---

### 2. **Singleton Pattern** (ParkingLot)

**Purpose:** Ensure only one parking lot instance exists

**Implementation:**
```java
public class ParkingLot {
    private static ParkingLot instance;
    
    private ParkingLot(String name) {
        // Private constructor
    }
    
    public static synchronized ParkingLot getInstance(String name) {
        if (instance == null) {
            instance = new ParkingLot(name);
        }
        return instance;
    }
}
```

**Benefits:**
- ✅ Global access point
- ✅ Controlled instantiation
- ✅ Thread-safe (synchronized)

**Usage:**
```java
ParkingLot parkingLot = ParkingLot.getInstance("City Center");
```

---

### 3. **Strategy Pattern** (PricingStrategy)

**Purpose:** Switch pricing algorithms at runtime

**Implementation:**
```java
public interface PricingStrategy {
    double calculateFee(ParkingTicket ticket);
}

public class HourlyPricingStrategy implements PricingStrategy {
    public double calculateFee(ParkingTicket ticket) {
        // Hourly calculation
    }
}

public class FlatPricingStrategy implements PricingStrategy {
    public double calculateFee(ParkingTicket ticket) {
        // Flat rate
    }
}
```

**Benefits:**
- ✅ Easy to add new pricing strategies
- ✅ Change pricing at runtime
- ✅ Open-Closed Principle (OCP)

**Usage:**
```java
parkingLot.setPricingStrategy(new HourlyPricingStrategy());
// Later...
parkingLot.setPricingStrategy(new FlatPricingStrategy(50.0));
```

---

## 🔑 Key Design Decisions

### 1. **Spot Assignment Logic**

```java
public boolean canFitVehicle(Vehicle vehicle) {
    switch (vehicle.getType()) {
        case BIKE:
            return type == SpotType.BIKE;
        case CAR:
            return type == SpotType.COMPACT || 
                   type == SpotType.LARGE || 
                   type == SpotType.HANDICAPPED;
        case TRUCK:
            return type == SpotType.LARGE;
    }
}
```

**Rules:**
- Bike → Bike spot ONLY
- Car → Compact OR Large OR Handicapped
- Truck → Large ONLY

**Why?**  
Trucks need large spots, cars can fit in smaller spots (flexibility), bikes need dedicated spots (safety).

---

### 2. **Fee Calculation**

```java
public long getParkingDurationInMinutes() {
    LocalDateTime end = (exitTime != null) ? exitTime : LocalDateTime.now();
    return Duration.between(entryTime, end).toMinutes();
}
```

**Uses Java 8 Time API:**
- `LocalDateTime` for timestamps
- `Duration.between()` for time calculation
- Rounds up to nearest hour for hourly pricing

---

### 3. **Multi-Floor Search**

```java
public ParkingTicket parkVehicle(Vehicle vehicle) {
    for (ParkingFloor floor : floors) {
        ParkingSpot spot = floor.findAvailableSpot(vehicle);
        if (spot != null) {
            // Park here
        }
    }
    throw new RuntimeException("No available spots");
}
```

**Strategy:** First-fit algorithm (first available spot found)

**Alternative:** Best-fit (smallest suitable spot)

---

## ⚖️ Trade-offs

### **1. Singleton Pattern**

**✅ Pros:**
- Global access
- Controlled instance
- Memory efficient (one instance)

**❌ Cons:**
- Hard to unit test (mocking difficult)
- Global state (can cause issues in large apps)
- Thread safety overhead (synchronized)

**Better Alternative for Production:**
Dependency Injection (Spring, Guice)

---

### **2. In-Memory Storage**

**Current:** `Map<String, ParkingTicket> activeTickets`

**✅ Pros:**
- Fast (O(1) lookup)
- Simple implementation

**❌ Cons:**
- Lost on restart
- Not scalable (memory limit)

**Production Solution:**
Database (SQL or NoSQL) with proper indexing

---

### **3. First-Fit vs Best-Fit**

**Current:** First-Fit (first available spot)

**Alternative:** Best-Fit (smallest suitable spot)

**Example:**
- Car needs spot
- Floor 1: Large spot available
- Floor 2: Compact spot available

**First-Fit:** Parks in Large spot (Floor 1)  
**Best-Fit:** Parks in Compact spot (Floor 2, better utilization)

**Trade-off:** Best-fit requires scanning all floors (slower, but better utilization)

---

## 🧪 Test Coverage

### **Scenarios Tested in Main.java:**

1. ✅ **Basic Parking Flow**
   - Create parking lot with 2 floors
   - Park Car, Bike, Truck
   - Verify spot assignment

2. ✅ **Fee Calculation**
   - Hourly pricing (different rates per vehicle type)
   - Flat pricing ($50 for all)

3. ✅ **Payment Methods**
   - Cash payment
   - Card payment

4. ✅ **Strategy Pattern Switch**
   - Change from Hourly to Flat rate mid-operation

5. ✅ **Edge Case: No Available Spots**
   - Fill all car spots
   - Try parking another car
   - Should throw RuntimeException

### **Additional Test Cases (Not in Demo):**

```java
// Test Case 1: Invalid Ticket
try {
    parkingLot.unparkVehicle("INVALID-TICKET", payment);
} catch (IllegalArgumentException e) {
    // Expected
}

// Test Case 2: Spot Already Occupied
ParkingSpot spot = new ParkingSpot("F1-C1", SpotType.COMPACT, 1);
spot.parkVehicle(car1);
try {
    spot.parkVehicle(car2); // Should fail
} catch (IllegalStateException e) {
    // Expected
}

// Test Case 3: Wrong Spot Type
ParkingSpot bikeSpot = new ParkingSpot("F1-B1", SpotType.BIKE, 1);
assertFalse(bikeSpot.canFitVehicle(car)); // Car can't fit in bike spot
```

---

## 🚀 How to Compile and Run

### **Option 1: Command Line (No Packages)**
```bash
cd src/
javac enums/*.java model/*.java factory/*.java strategy/*.java service/*.java Main.java
java Main
```

### **Option 2: With Package Structure (Recommended)**
```bash
# From Problem-Questions/01-Parking-Lot/
javac -d bin src/enums/*.java src/model/*.java src/factory/*.java src/strategy/*.java src/service/*.java src/Main.java
java -cp bin Main
```

### **Expected Output:**
```
========================================
  PARKING LOT SYSTEM DEMO
========================================

INITIAL STATE:
=== Parking Lot Availability ===
Floor 1:
  COMPACT: 2 spots
  LARGE: 1 spots
  HANDICAPPED: 0 spots
  BIKE: 2 spots
Floor 2:
  COMPACT: 1 spots
  LARGE: 1 spots
  HANDICAPPED: 1 spots
  BIKE: 0 spots

========================================
  CREATING VEHICLES (Factory Pattern)
========================================
✓ Created Car: ABC-123
✓ Created Bike: XYZ-789
✓ Created Truck: TRK-001

========================================
  PARKING VEHICLES
========================================
Vehicle ABC-123 parked at floor 1, spot F1-C1
Vehicle XYZ-789 parked at floor 1, spot F1-B1
Vehicle TRK-001 parked at floor 1, spot F1-L1

...
```

---

## 📈 Extensions & Improvements

### **1. Add Database Persistence**

```java
public interface ParkingLotRepository {
    void saveTicket(ParkingTicket ticket);
    ParkingTicket getTicket(String ticketId);
    void updateSpot(ParkingSpot spot);
    List<ParkingTicket> getActiveTickets();
}

public class ParkingLotRepositoryImpl implements ParkingLotRepository {
    // Use JDBC or Hibernate
}
```

### **2. Add Concurrency Control**

```java
public synchronized ParkingTicket parkVehicle(Vehicle vehicle) {
    // Already synchronized, but can use ReentrantLock for fine-grained control
}

// Or use concurrent collections
private ConcurrentHashMap<String, ParkingTicket> activeTickets;
```

### **3. Add Reservation System**

```java
public class ParkingReservation {
    private String reservationId;
    private Vehicle vehicle;
    private LocalDateTime reservationTime;
    private LocalDateTime expiryTime; // Hold for 15 mins
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }
}
```

### **4. Add Electric Vehicle Charging**

```java
public enum SpotType {
    COMPACT, LARGE, HANDICAPPED, BIKE, EV_CHARGING
}

public class EVChargingSpot extends ParkingSpot {
    private double chargingRatePerMinute;
    
    public double calculateChargingFee(long minutes) {
        return minutes * chargingRatePerMinute;
    }
}
```

### **5. Add Analytics**

```java
public class ParkingAnalytics {
    public double getTotalRevenue();
    public int getAverageOccupancy();
    public Map<VehicleType, Integer> getVehicleDistribution();
    public double getPeakHourUtilization();
}
```

---

## 🎯 Interview Tips

### **What Interviewers Look For:**

1. ✅ **Clear class hierarchy** (Vehicle abstract class)
2. ✅ **Proper use of enums** (type safety)
3. ✅ **Design patterns correctly applied**
4. ✅ **Clean separation** (model, service, strategy)
5. ✅ **Edge case handling** (no spots, invalid ticket)
6. ✅ **Extensibility** (easy to add new types)

### **Common Follow-up Questions:**

**Q: "How would you handle peak hours with dynamic pricing?"**
```java
public class DynamicPricingStrategy implements PricingStrategy {
    public double calculateFee(ParkingTicket ticket) {
        double baseFee = hourlyRate * hours;
        
        // Peak hours: 8-10 AM, 5-7 PM
        if (isPeakHour(ticket.getEntryTime())) {
            baseFee *= 1.5; // 50% surge
        }
        
        return baseFee;
    }
}
```

**Q: "How would you handle multiple payment methods (split payment)?"**
```java
public class CompositePayment implements Payment {
    private List<Payment> payments;
    
    public boolean processPayment(double amount) {
        double remaining = amount;
        for (Payment payment : payments) {
            remaining = payment.processPartial(remaining);
            if (remaining <= 0) return true;
        }
        return false;
    }
}
```

**Q: "How do you prevent race conditions when two cars try to take the same spot?"**
```java
// Option 1: Synchronized method
public synchronized ParkingTicket parkVehicle(Vehicle vehicle) { ... }

// Option 2: Lock per spot
public class ParkingSpot {
    private final ReentrantLock lock = new ReentrantLock();
    
    public boolean tryParkVehicle(Vehicle vehicle) {
        if (lock.tryLock()) {
            try {
                parkVehicle(vehicle);
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
}
```

---

## ✅ Checklist Before Interview

- [ ] Can explain all 3 design patterns clearly
- [ ] Can code the solution in <45 minutes
- [ ] Understand trade-offs (Singleton, in-memory storage, etc.)
- [ ] Can handle follow-up questions (concurrency, database, etc.)
- [ ] Code compiles and runs without errors
- [ ] Edge cases handled (no spots, invalid ticket)
- [ ] Can extend (add new vehicle types, pricing strategies)

---

**This solution demonstrates production-quality code with proper design patterns, clean architecture, and extensibility! 🚀**


