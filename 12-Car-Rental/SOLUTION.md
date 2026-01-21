# Solution: Car Rental System

## ✅ Complete Implementation

This folder contains a fully working car rental system demonstrating Factory, Strategy, Observer, and Singleton design patterns.

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
│   Factory    │ │   Service   │ │  Strategy  │ │ Observer │
│              │ │             │ │            │ │          │
│ Vehicle      │ │ Rental      │ │ Pricing    │ │ Customer │
│ Factory      │ │ System      │ │ Strategy   │ │ Notif.   │
│              │ │ (Singleton) │ │            │ │          │
└──────────────┘ └─────────────┘ └────────────┘ └──────────┘
        │               │               │              │
        └───────────────┼───────────────┴──────────────┘
                        │
                        ▼
                ┌─────────────┐
                │    Model    │
                │             │
                │  Vehicle    │
                │  Customer   │
                │  Rental     │
                │  Reservation│
                │  Invoice    │
                │  Location   │
                └─────────────┘
```

---

## 📦 Package Structure

```
src/
├── enums/                          # Type-safe enumerations
│   ├── VehicleType.java           # ECONOMY, SEDAN, SUV, LUXURY
│   ├── VehicleStatus.java         # AVAILABLE, RENTED, UNDER_MAINTENANCE, RESERVED
│   ├── RentalStatus.java          # ACTIVE, COMPLETED, CANCELLED
│   ├── ReservationStatus.java     # ACTIVE, CONFIRMED, CANCELLED, EXPIRED, FULFILLED
│   └── MembershipTier.java        # REGULAR, SILVER, GOLD (with discount rates)
│
├── model/                          # Domain entities
│   ├── Vehicle.java               # Abstract base class
│   ├── EconomyCar.java            # $50/day
│   ├── Sedan.java                 # $70/day
│   ├── SUV.java                   # $100/day
│   ├── LuxuryCar.java             # $200/day
│   ├── Customer.java              # Implements Observer
│   ├── Rental.java                # Rental agreement with cost calculation
│   ├── Reservation.java           # Future booking
│   ├── Invoice.java               # Detailed billing
│   └── Location.java              # Rental branch
│
├── factory/                        # Factory Pattern
│   └── VehicleFactory.java        # Centralized vehicle creation
│
├── strategy/                       # Strategy Pattern
│   ├── PricingStrategy.java       # Interface
│   ├── StandardPricingStrategy.java    # Standard pricing
│   └── SeasonalPricingStrategy.java    # Peak/off-season pricing
│
├── observer/                       # Observer Pattern
│   ├── Observer.java              # Observer interface
│   └── Subject.java               # Subject for notifications
│
├── service/                        # Business logic
│   └── RentalSystem.java          # Main service (Singleton)
│
└── Main.java                       # Demo application (7 scenarios)
```

---

## 🎨 Design Patterns Explained

### 1. **Factory Pattern** (VehicleFactory)

**Purpose:** Centralize vehicle creation logic and support multiple vehicle types

**Implementation:**
```java
public class VehicleFactory {
    public static Vehicle createVehicle(VehicleType type, String vin, 
                                       String make, String model, int year) {
        switch (type) {
            case ECONOMY:
                return new EconomyCar(vin, make, model, year);
            case SEDAN:
                return new Sedan(vin, make, model, year);
            case SUV:
                return new SUV(vin, make, model, year);
            case LUXURY:
                return new LuxuryCar(vin, make, model, year);
        }
    }
}
```

**Benefits:**
- ✅ Single place to create vehicles
- ✅ Easy to add new vehicle types (just add new case)
- ✅ Encapsulates object creation complexity
- ✅ Consistent initialization (daily rates, default status)

**Usage in Demo:**
```java
Vehicle v1 = VehicleFactory.createVehicle(VehicleType.ECONOMY, "VIN-001", "Toyota", "Corolla", 2023);
```

---

### 2. **Strategy Pattern** (PricingStrategy)

**Purpose:** Flexible pricing algorithms that can be switched at runtime

**Implementation:**
```java
public interface PricingStrategy {
    double calculatePrice(Rental rental);
    String getStrategyName();
}

public class StandardPricingStrategy implements PricingStrategy {
    public double calculatePrice(Rental rental) {
        double baseCost = rental.calculateBaseCost();
        double insuranceCost = rental.calculateInsuranceCost();
        double addOnsCost = rental.calculateAddOnsCost();
        double lateFee = rental.calculateLateFee();
        double oneWayFee = rental.calculateOneWayFee();
        
        double subtotal = baseCost + insuranceCost + addOnsCost + lateFee + oneWayFee;
        double discount = baseCost * rental.getCustomer().getMembershipDiscount();
        
        return subtotal - discount;
    }
}

public class SeasonalPricingStrategy implements PricingStrategy {
    public double calculatePrice(Rental rental) {
        // Apply seasonal multipliers (peak: 1.5x, off-season: 0.85x)
        // Then calculate as standard
    }
}
```

**Benefits:**
- ✅ Easy to switch pricing strategies
- ✅ Open/Closed Principle: Add new strategies without modifying existing code
- ✅ Testable: Each strategy can be tested independently
- ✅ Real-world flexibility: Weekend rates, corporate rates, promotional pricing

**Usage in Demo:**
```java
system.setPricingStrategy(new StandardPricingStrategy());
// or
system.setPricingStrategy(new SeasonalPricingStrategy());
```

---

### 3. **Observer Pattern** (Customer Notifications)

**Purpose:** Decouple notification logic from business logic

**Implementation:**
```java
public interface Observer {
    void update(String message);
    String getNotificationId();
}

public class Customer implements Observer {
    @Override
    public void update(String message) {
        System.out.println("📧 Notification to " + name + ": " + message);
    }
}

public class Subject {
    private List<Observer> observers = new ArrayList<>();
    
    public void attach(Observer observer) {
        observers.add(observer);
    }
    
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
```

**Benefits:**
- ✅ Loose coupling: RentalSystem doesn't need to know how notifications are sent
- ✅ Easy to add new notification types (Email, SMS, Push)
- ✅ Multiple observers can listen to same events
- ✅ Real-time updates to customers

**Usage in Demo:**
- Reservation created → Notify customer
- Reserved vehicle available → Notify waiting customer
- Rental completed → Notify customer

---

### 4. **Singleton Pattern** (RentalSystem)

**Purpose:** Ensure only one instance of RentalSystem exists

**Implementation:**
```java
public class RentalSystem {
    private static RentalSystem instance;
    
    private RentalSystem(String systemName) {
        // Private constructor
    }
    
    public static synchronized RentalSystem getInstance(String systemName) {
        if (instance == null) {
            instance = new RentalSystem(systemName);
        }
        return instance;
    }
}
```

**Benefits:**
- ✅ Global access point
- ✅ Single source of truth for all rentals
- ✅ Prevents multiple systems managing same vehicles
- ✅ Thread-safe with synchronized

---

## 💰 Cost Calculation Logic

### Base Cost Calculation
```
Base Cost = Rental Days × Vehicle Daily Rate
```

### Membership Discounts
- **REGULAR:** 0% discount
- **SILVER:** 10% discount on base cost
- **GOLD:** 15% discount on base cost

### Additional Charges
1. **Insurance:** $15/day (optional)
2. **GPS:** $10/day (optional)
3. **Late Fee:** $30/day for each day late
4. **One-Way Fee:** $75 flat fee if pickup ≠ return location

### Final Calculation
```
Subtotal = Base Cost + Insurance + Add-ons + Late Fee + One-Way Fee
Discount = Base Cost × Membership Discount Rate
Tax = (Subtotal - Discount) × 8%
Total = Subtotal - Discount + Tax
```

### Example (Scenario 3):
```
Customer: Bob Johnson (GOLD - 15% discount)
Vehicle: BMW 7 Series ($200/day)
Duration: 5 days

Base Cost = 5 × $200 = $1000
Discount = $1000 × 0.15 = $150
Subtotal = $1000
After Discount = $1000 - $150 = $850
Tax = $850 × 0.08 = $68
Total = $850 + $68 = $918
```

---

## 🔄 Rental Flow

### 1. Create Rental
```
Customer → Search Vehicles → Select Vehicle → Create Rental
                                                    ↓
                                            Vehicle Status = RENTED
                                            Generate Rental Agreement
```

### 2. Return Vehicle
```
Return Vehicle → Calculate Costs → Generate Invoice → Process Payment
                                                           ↓
                                                   Vehicle Status = AVAILABLE
                                                   Check Waiting Reservations
                                                   Notify Customers
```

### 3. Reservation Flow
```
Customer → Create Reservation → Vehicle Type Reserved
                                        ↓
                                When Vehicle Available
                                        ↓
                                Notify Customer
                                        ↓
                                Customer Picks Up
                                        ↓
                                Reservation = FULFILLED
```

---

## 📊 Key Features

### 1. **Multi-Location Support**
- Vehicles tracked per location
- One-way rentals supported
- Vehicle automatically transferred to return location

### 2. **Flexible Pricing**
- Strategy pattern allows runtime pricing changes
- Seasonal pricing (peak/off-season multipliers)
- Membership tier discounts
- Add-on pricing (insurance, GPS)

### 3. **Comprehensive Billing**
- Detailed invoice breakdown
- Line-by-line cost explanation
- Tax calculation
- Discount application

### 4. **Reservation System**
- Reserve vehicles in advance
- 24-hour expiry on reservations
- Notify customers when vehicle available
- Prevent double-booking

### 5. **Vehicle Status Management**
- AVAILABLE → RENTED → AVAILABLE
- UNDER_MAINTENANCE support
- RESERVED status for future bookings

### 6. **Customer Management**
- Membership tiers (Regular, Silver, Gold)
- Rental history tracking
- Outstanding dues tracking
- Eligibility checks before rental

---

## 🧪 Test Scenarios Covered

### ✅ Scenario 1: Basic Rental Flow
- Create rental
- Return on time
- Calculate cost with no extras
- **Result:** $162 (3 days × $50 + tax)

### ✅ Scenario 2: Reservation Flow
- Create reservation
- Notify customer
- Observer pattern demonstration
- **Result:** Reservation created, notifications sent

### ✅ Scenario 3: Membership Discounts
- Gold member (15% discount)
- Luxury vehicle rental
- **Result:** $918 (5 days × $200 - 15% + tax)

### ✅ Scenario 4: Late Return Penalty
- Return 2 days late
- Calculate late fees
- **Result:** $405 (base + late fee - discount + tax)

### ✅ Scenario 5: One-Way Rental
- Different pickup/return locations
- One-way fee applied
- Vehicle location updated
- **Result:** $232.20 (base + $75 one-way fee + tax)

### ✅ Scenario 6: Add-ons and Insurance
- Insurance ($15/day)
- GPS ($10/day)
- **Result:** $496.80 (base + insurance + GPS - discount + tax)

### ✅ Scenario 7: Vehicle Not Available
- Attempt to rent vehicle under maintenance
- Exception handling
- Suggest alternatives
- **Result:** Graceful error handling

---

## 🎯 Design Decisions & Trade-offs

### 1. **Vehicle Hierarchy**
**Decision:** Abstract `Vehicle` class with concrete subclasses

**Pros:**
- Easy to add new vehicle types
- Each type has its own daily rate
- Polymorphism for vehicle operations

**Cons:**
- More classes to maintain
- Could use composition instead (Vehicle + VehicleType)

**Alternative:** Single Vehicle class with VehicleType enum
- Simpler structure
- Less flexible for type-specific behavior

---

### 2. **Pricing Strategy**
**Decision:** Strategy pattern for pricing

**Pros:**
- Runtime flexibility
- Easy to add new pricing algorithms
- Testable independently

**Cons:**
- More classes
- Slight overhead for simple pricing

**Alternative:** Single pricing method in Rental class
- Simpler but less flexible
- Hard to change pricing logic

---

### 3. **Invoice Generation**
**Decision:** Separate Invoice class

**Pros:**
- Single Responsibility Principle
- Detailed breakdown generation
- Can be stored/printed independently

**Cons:**
- Additional class
- Duplicates some Rental calculations

**Alternative:** Invoice generation in Rental class
- Simpler but violates SRP
- Harder to test invoice formatting

---

### 4. **Observer Pattern for Notifications**
**Decision:** Customer implements Observer

**Pros:**
- Loose coupling
- Easy to add notification channels
- Real-time updates

**Cons:**
- More complexity
- Overhead for simple notifications

**Alternative:** Direct method calls
- Simpler but tightly coupled
- Hard to extend notification types

---

### 5. **Singleton for RentalSystem**
**Decision:** Singleton pattern

**Pros:**
- Global access point
- Single source of truth
- Prevents duplicate systems

**Cons:**
- Global state (testing challenges)
- Thread-safety considerations

**Alternative:** Dependency injection
- More testable
- More flexible
- Requires DI framework

---

## 🚀 Extensions & Improvements

### 1. **Add Damage Assessment**
```java
public class DamageReport {
    private String reportId;
    private Rental rental;
    private List<Damage> damages;
    private double totalDamageCost;
    
    public double calculateDamageCost() {
        return damages.stream()
            .mapToDouble(Damage::getCost)
            .sum();
    }
}
```

### 2. **Add Loyalty Points System**
```java
public class LoyaltyProgram {
    private Map<Customer, Integer> points;
    
    public void earnPoints(Customer customer, double rentalCost) {
        int points = (int) (rentalCost / 10); // 1 point per $10
        addPoints(customer, points);
    }
    
    public double redeemPoints(Customer customer, int points) {
        return points * 0.1; // $0.10 per point
    }
}
```

### 3. **Add Vehicle Recommendations**
```java
public class RecommendationEngine {
    public List<Vehicle> recommendVehicles(Customer customer) {
        // Based on rental history
        // Popular vehicles
        // Similar to previously rented
    }
}
```

### 4. **Add Multi-Vehicle Rentals**
```java
public class GroupRental {
    private String groupRentalId;
    private List<Rental> rentals;
    private double groupDiscount = 0.10; // 10% for 3+ vehicles
    
    public double calculateTotalCost() {
        // Apply group discount
    }
}
```

### 5. **Add Payment Processing**
```java
public interface PaymentProcessor {
    boolean processPayment(double amount, PaymentMethod method);
}

public class CreditCardProcessor implements PaymentProcessor {
    public boolean processPayment(double amount, PaymentMethod method) {
        // Process credit card payment
    }
}
```

---

## 📈 Scalability Considerations

### 1. **Database Integration**
- Currently in-memory (HashMap)
- Production: Use database (PostgreSQL, MySQL)
- Add repository layer for data access

### 2. **Concurrency**
- Add locks for vehicle booking
- Prevent race conditions
- Use optimistic locking

### 3. **Caching**
- Cache available vehicles
- Cache pricing calculations
- Invalidate on status changes

### 4. **Microservices**
- Split into services:
  - Vehicle Service
  - Rental Service
  - Billing Service
  - Notification Service

### 5. **Event-Driven Architecture**
- Publish events (VehicleRented, VehicleReturned)
- Async processing
- Better scalability

---

## ✅ Success Metrics

### Code Quality
- ✅ Compiles without errors
- ✅ Runs all 7 scenarios successfully
- ✅ Proper package structure
- ✅ Clear naming conventions
- ✅ Comprehensive comments

### Design Patterns
- ✅ Factory Pattern correctly implemented
- ✅ Strategy Pattern with multiple strategies
- ✅ Observer Pattern for notifications
- ✅ Singleton Pattern for system

### Features
- ✅ Multi-vehicle types supported
- ✅ Membership tiers with discounts
- ✅ Late return penalties
- ✅ One-way rentals
- ✅ Insurance and add-ons
- ✅ Detailed invoices
- ✅ Reservation system
- ✅ Error handling

### Edge Cases
- ✅ Vehicle not available
- ✅ Customer has outstanding dues
- ✅ Late returns
- ✅ One-way rentals
- ✅ Vehicle under maintenance

---

## 📚 Learning Outcomes

After studying this solution, you should understand:

1. **Factory Pattern:** When and how to centralize object creation
2. **Strategy Pattern:** How to make algorithms interchangeable
3. **Observer Pattern:** How to implement event-driven notifications
4. **Singleton Pattern:** When to use and potential pitfalls
5. **Domain Modeling:** Separating entities (Vehicle, Customer, Rental)
6. **Cost Calculation:** Complex pricing with multiple factors
7. **State Management:** Vehicle status transitions
8. **Error Handling:** Graceful handling of edge cases

---

## 🎓 Interview Tips

### Common Questions:

**Q: Why use Factory Pattern for vehicles?**
A: Centralized creation, easy to add new types, consistent initialization

**Q: How would you handle concurrent bookings?**
A: Add synchronization, optimistic locking, or use database transactions

**Q: How to scale to millions of vehicles?**
A: Database sharding, caching, microservices, event-driven architecture

**Q: Why separate Invoice from Rental?**
A: Single Responsibility Principle, better testability, independent storage

**Q: How to add new pricing strategies?**
A: Implement PricingStrategy interface, no changes to existing code

---

**Total Lines of Code:** ~1,500 lines  
**Files:** 24 Java files  
**Patterns:** 4 design patterns  
**Scenarios:** 7 comprehensive test cases  

**Time to Complete:** 60-75 minutes (interview setting)

---

*Created: January 2026*  
*Problem: Car Rental System (Medium)*  
*Patterns: Factory, Strategy, Observer, Singleton*


