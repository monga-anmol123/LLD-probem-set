# Problem 12: Car Rental System

## 🎯 Difficulty: Medium ⭐⭐⭐

## 📝 Problem Statement

Design a car rental system that manages vehicle inventory, customer rentals, pricing strategies, reservations, and billing. The system should support different vehicle types, flexible pricing, insurance options, and handle multiple rental locations.

---

## 🔍 Functional Requirements (FR)

### FR1: Vehicle Management
- Support multiple vehicle types: Economy, Sedan, SUV, Luxury
- Each vehicle has: VIN, make, model, year, mileage, status
- Vehicle status: Available, Rented, Under Maintenance, Reserved
- Track vehicle location (branch/location)

### FR2: Customer Management
- Register customers with driver's license
- Track rental history
- Support membership tiers: Regular, Silver, Gold (with discounts)
- Verify customer eligibility (valid license, no outstanding dues)

### FR3: Rental Operations
- **Create Rental:** Select vehicle, duration, pickup/return locations
- **Pickup Vehicle:** Verify customer, generate rental agreement
- **Return Vehicle:** Calculate total cost, process payment, inspect vehicle
- Support one-way rentals (different pickup/return locations)

### FR4: Reservation System
- Reserve vehicles in advance
- Hold reservation for 24 hours
- Notify customer when vehicle ready
- Cancel/modify reservations

### FR5: Pricing System
- Base daily rate varies by vehicle type
- Dynamic pricing based on:
  - Season (peak/off-peak)
  - Duration (weekly/monthly discounts)
  - Membership tier
- Additional charges:
  - Insurance (collision, theft)
  - GPS, child seat, additional driver
  - One-way rental fee
  - Late return penalty

### FR6: Billing & Payment
- Calculate total cost breakdown
- Support multiple payment methods
- Generate invoice
- Handle security deposits
- Process refunds

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Scalability
- Support 1000+ vehicles across multiple locations
- Handle 500+ concurrent rentals
- Process 100+ reservations per day

### NFR2: Extensibility
- Easy to add new vehicle types (Electric, Hybrid)
- Easy to add new pricing strategies
- Support for new add-on services
- Add new membership tiers

### NFR3: Reliability
- Handle edge cases:
  - Vehicle not available
  - Overlapping reservations
  - Customer has outstanding dues
  - Vehicle damage on return
  - Late returns

### NFR4: Performance
- Rental creation: <2 seconds
- Vehicle search: <1 second
- Availability check: <500ms

### NFR5: Data Consistency
- Prevent double-booking
- Accurate billing calculations
- Synchronized inventory across locations

---

## 🎨 Design Patterns to Use

### 1. **Factory Pattern**
- **Where:** Vehicle creation, Rental creation
- **Why:** Centralize object creation, support multiple vehicle types

### 2. **Strategy Pattern**
- **Where:** Pricing strategies (daily, weekly, seasonal)
- **Why:** Flexible pricing algorithms, easy to switch at runtime

### 3. **Observer Pattern**
- **Where:** Notify customers about reservation status, vehicle availability
- **Why:** Decouple notification logic from business logic

### 4. **Singleton Pattern** (Optional)
- **Where:** RentalSystem instance
- **Why:** Single system managing all rentals

---

## 📋 Core Entities

### 1. **Vehicle** (Abstract)
- Attributes: `vin`, `make`, `model`, `year`, `type`, `status`, `dailyRate`, `mileage`, `location`
- Subclasses: `EconomyCar`, `Sedan`, `SUV`, `LuxuryCar`
- Methods: `isAvailable()`, `rent()`, `returnVehicle()`

### 2. **Customer**
- Attributes: `customerId`, `name`, `email`, `phone`, `licenseNumber`, `membershipTier`, `rentalHistory`
- Methods: `canRent()`, `addRental()`, `getMembershipDiscount()`
- Implements `Observer` for notifications

### 3. **Rental**
- Attributes: `rentalId`, `vehicle`, `customer`, `pickupDate`, `returnDate`, `actualReturnDate`, `pickupLocation`, `returnLocation`, `totalCost`, `status`
- Methods: `calculateCost()`, `completeRental()`, `extendRental()`

### 4. **Reservation**
- Attributes: `reservationId`, `customer`, `vehicleType`, `pickupDate`, `returnDate`, `status`, `expiryTime`
- Status: Active, Confirmed, Cancelled, Expired, Fulfilled
- Methods: `confirm()`, `cancel()`, `isExpired()`

### 5. **Invoice**
- Attributes: `invoiceId`, `rental`, `baseCharge`, `insuranceCharge`, `addOnsCharge`, `taxAmount`, `discount`, `totalAmount`
- Methods: `generateBreakdown()`, `applyDiscount()`

### 6. **Location**
- Attributes: `locationId`, `name`, `address`, `availableVehicles`
- Methods: `addVehicle()`, `removeVehicle()`, `getAvailableVehicles()`

### 7. **RentalSystem** (Main Service)
- Methods: `searchVehicles()`, `createReservation()`, `createRental()`, `returnVehicle()`, `processPayment()`

---

## 🧪 Test Scenarios

### Scenario 1: Basic Rental Flow
```
1. Register customer (John Doe, Regular member)
2. Search available vehicles (Economy, 3 days)
3. Create rental for Toyota Corolla
4. Pickup vehicle
5. Simulate 3 days passing
6. Return vehicle
7. Calculate cost: 3 days * $50/day = $150
8. Process payment
9. Vehicle status = Available
```

### Scenario 2: Reservation Flow
```
1. Customer searches vehicles for future date
2. Reserve SUV for next week
3. System holds reservation
4. Notify customer 24 hours before pickup
5. Customer picks up reserved vehicle
6. Reservation status = Fulfilled
```

### Scenario 3: Membership Discounts
```
1. Gold member rents luxury car
2. Base rate: $200/day * 5 days = $1000
3. Gold discount: 15% = $150 off
4. Final cost: $850
5. Verify discount applied correctly
```

### Scenario 4: Late Return Penalty
```
1. Rent car for 3 days (due: Jan 10)
2. Return on Jan 12 (2 days late)
3. Base cost: 3 * $50 = $150
4. Late penalty: 2 * $30 = $60
5. Total: $210
```

### Scenario 5: One-Way Rental
```
1. Pickup at Location A (NYC)
2. Return at Location B (Boston)
3. Base cost: 2 days * $60 = $120
4. One-way fee: $75
5. Total: $195
6. Vehicle moved to Location B inventory
```

### Scenario 6: Add-ons and Insurance
```
1. Rent sedan for 4 days
2. Add collision insurance: $15/day
3. Add GPS: $10/day
4. Base: 4 * $70 = $280
5. Insurance: 4 * $15 = $60
6. GPS: 4 * $10 = $40
7. Total: $380
```

### Scenario 7: Vehicle Not Available
```
1. Try to rent specific vehicle
2. Vehicle status = Under Maintenance
3. System suggests similar available vehicles
4. Customer selects alternative
```

---

## ⏱️ Time Allocation (60 minutes)

- **5 mins:** Clarify requirements, ask questions
- **5 mins:** List entities (Vehicle, Customer, Rental, Reservation, Invoice)
- **5 mins:** Identify design patterns (Factory, Strategy, Observer)
- **35 mins:** Write code (enums → model → factory → strategy → observer → service → main)
- **10 mins:** Test with comprehensive demo scenarios

---

## 💡 Hints

<details>
<summary>Hint 1: Vehicle Factory</summary>

```java
public class VehicleFactory {
    public static Vehicle createVehicle(VehicleType type, String vin, String make, String model, int year) {
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
</details>

<details>
<summary>Hint 2: Pricing Strategy</summary>

```java
public interface PricingStrategy {
    double calculatePrice(Rental rental);
}

public class StandardPricingStrategy implements PricingStrategy {
    public double calculatePrice(Rental rental) {
        long days = rental.getRentalDays();
        double baseRate = rental.getVehicle().getDailyRate();
        double basePrice = days * baseRate;
        
        // Apply membership discount
        double discount = rental.getCustomer().getMembershipDiscount();
        return basePrice * (1 - discount);
    }
}

public class SeasonalPricingStrategy implements PricingStrategy {
    private double peakSeasonMultiplier = 1.5;
    
    public double calculatePrice(Rental rental) {
        // Adjust price based on season
    }
}
```
</details>

<details>
<summary>Hint 3: Observer Pattern for Notifications</summary>

```java
public interface Observer {
    void update(String message);
}

public class Customer implements Observer {
    @Override
    public void update(String message) {
        System.out.println("Notification to " + name + ": " + message);
    }
}

public interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(String message);
}
```
</details>

<details>
<summary>Hint 4: Cost Calculation</summary>

```java
public double calculateTotalCost() {
    double baseCost = calculateBaseCost();
    double insuranceCost = calculateInsuranceCost();
    double addOnsCost = calculateAddOnsCost();
    double lateFee = calculateLateFee();
    double oneWayFee = isOneWayRental() ? 75.0 : 0.0;
    
    double subtotal = baseCost + insuranceCost + addOnsCost + lateFee + oneWayFee;
    double discount = customer.getMembershipDiscount() * baseCost;
    
    return subtotal - discount;
}
```
</details>

<details>
<summary>Hint 5: Membership Tiers</summary>

```java
public enum MembershipTier {
    REGULAR(0.0),
    SILVER(0.10),   // 10% discount
    GOLD(0.15);     // 15% discount
    
    private final double discountRate;
    
    MembershipTier(double discountRate) {
        this.discountRate = discountRate;
    }
    
    public double getDiscountRate() {
        return discountRate;
    }
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Add Damage Assessment**
   - Inspect vehicle on return
   - Charge for damages
   - Generate damage report

2. **Add Loyalty Points**
   - Earn points per rental
   - Redeem points for discounts
   - Tier upgrades based on points

3. **Add Vehicle Recommendations**
   - Suggest vehicles based on rental history
   - Show popular vehicles
   - Personalized offers

4. **Add Waiting List**
   - If vehicle type unavailable, join waiting list
   - Notify when vehicle becomes available

5. **Add Multi-Vehicle Rentals**
   - Rent multiple vehicles in one booking
   - Group discounts
   - Corporate accounts

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Factory, Strategy, Observer patterns correctly
- [ ] Handle all test scenarios (7 scenarios)
- [ ] Calculate costs accurately (base + insurance + add-ons + penalties)
- [ ] Support different membership tiers with discounts
- [ ] Handle reservations with notifications
- [ ] Support one-way rentals
- [ ] Track vehicle status correctly
- [ ] Handle edge cases (unavailable vehicles, late returns, etc.)
- [ ] Have clear separation: model, service, strategy, factory, observer layers
- [ ] Generate detailed invoices

---

## 📁 File Structure

```
12-Car-Rental/
├── README.md (this file)
├── SOLUTION.md
├── COMPILATION-GUIDE.md
└── src/
    ├── enums/
    │   ├── VehicleType.java
    │   ├── VehicleStatus.java
    │   ├── RentalStatus.java
    │   ├── ReservationStatus.java
    │   └── MembershipTier.java
    ├── model/
    │   ├── Vehicle.java
    │   ├── EconomyCar.java
    │   ├── Sedan.java
    │   ├── SUV.java
    │   ├── LuxuryCar.java
    │   ├── Customer.java
    │   ├── Rental.java
    │   ├── Reservation.java
    │   ├── Invoice.java
    │   └── Location.java
    ├── factory/
    │   └── VehicleFactory.java
    ├── strategy/
    │   ├── PricingStrategy.java
    │   ├── StandardPricingStrategy.java
    │   └── SeasonalPricingStrategy.java
    ├── observer/
    │   ├── Observer.java
    │   └── Subject.java
    ├── service/
    │   └── RentalSystem.java
    └── Main.java
```

---

**Good luck! This combines multiple patterns and real-world complexity! 🚀**


