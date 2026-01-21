# Problem 01: Parking Lot System

## 🎯 Difficulty: Easy ⭐⭐

## 📝 Problem Statement

Design a parking lot system that can accommodate different types of vehicles (Car, Bike, Truck) across multiple floors with different pricing strategies.

---

## 🔍 Functional Requirements (FR)

### FR1: Vehicle Management
- Support multiple vehicle types: Car, Bike, Truck
- Each vehicle has a license plate (unique identifier)

### FR2: Parking Spot Management
- Support different spot types: Compact, Large, Handicapped, Bike
- Each spot has an ID, type, floor number, and status (Available/Occupied/Out of Service)
- Spot assignment rules:
  - Bike → Bike spot only
  - Car → Compact OR Large OR Handicapped spot
  - Truck → Large spot only

### FR3: Parking Operations
- **Park Vehicle:** Assign an available spot, generate parking ticket
- **Unpark Vehicle:** Calculate fee, process payment, free the spot
- Display availability (spots available per type per floor)

### FR4: Fee Calculation
- Different pricing strategies:
  - Hourly pricing (different rates for different vehicle types)
  - Flat rate pricing
- Fees calculated based on parking duration

### FR5: Payment Processing
- Support multiple payment methods: Cash, Card
- Process payment on exit

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Scalability
- System should handle multiple floors (10+ floors)
- Each floor can have 100+ spots
- Support 1000+ simultaneous parkings

### NFR2: Extensibility
- Easy to add new vehicle types (e.g., Electric Car, Motorbike)
- Easy to add new spot types (e.g., EV Charging spots)
- Easy to add new pricing strategies (e.g., Weekend rates, Peak hour rates)

### NFR3: Maintainability
- Clean code with clear separation of concerns
- Well-documented classes and methods
- Easy to understand and modify

### NFR4: Performance
- Parking/Unparking operation should complete in <1 second
- Finding available spot should be O(n) where n = spots per floor

### NFR5: Reliability
- System should handle edge cases:
  - No available spots
  - Invalid ticket ID
  - Payment failure
  - Spot already occupied

---

## 🎨 Design Patterns to Use

### 1. **Factory Pattern**
- **Where:** Vehicle creation
- **Why:** Centralize object creation, easy to add new vehicle types

### 2. **Singleton Pattern**
- **Where:** ParkingLot instance
- **Why:** Only one parking lot should exist, global access point

### 3. **Strategy Pattern**
- **Where:** Pricing strategies
- **Why:** Switch between different pricing algorithms at runtime

---

## 📋 Core Entities

### 1. **Vehicle** (Abstract)
- Attributes: `licensePlate`, `type`
- Subclasses: `Car`, `Bike`, `Truck`

### 2. **ParkingSpot**
- Attributes: `spotId`, `type`, `status`, `floor`, `parkedVehicle`
- Methods: `canFitVehicle()`, `parkVehicle()`, `removeVehicle()`

### 3. **ParkingTicket**
- Attributes: `ticketId`, `vehicle`, `spot`, `entryTime`, `exitTime`, `fee`
- Methods: `getParkingDurationInMinutes()`, `markExit()`

### 4. **ParkingFloor**
- Attributes: `floorNumber`, `spots`
- Methods: `findAvailableSpot()`, `getAvailableSpotCount()`

### 5. **ParkingLot** (Singleton)
- Attributes: `name`, `floors`, `activeTickets`, `pricingStrategy`
- Methods: `parkVehicle()`, `unparkVehicle()`, `displayAvailability()`

---

## 🧪 Test Scenarios

### Scenario 1: Basic Parking Flow
```
1. Create parking lot with 2 floors
2. Add spots: 5 compact, 3 large, 2 bike spots per floor
3. Create vehicles: Car, Bike, Truck
4. Park all vehicles successfully
5. Display availability (should show reduced available spots)
6. Unpark vehicles with payment
7. Display availability (spots restored)
```

### Scenario 2: No Available Spot
```
1. Park 5 cars (fill all compact spots)
2. Try to park 6th car
3. Should throw exception: "No available spots"
```

### Scenario 3: Different Pricing Strategies
```
1. Use hourly pricing: Car=$20/hr, Bike=$10/hr, Truck=$50/hr
2. Park vehicles, wait, unpark
3. Verify fee calculation
4. Switch to flat rate: $50 for all
5. Park new vehicle, unpark
6. Verify flat rate applied
```

### Scenario 4: Multiple Floors
```
1. Park 5 cars on floor 1 (fill floor 1)
2. Park 6th car (should go to floor 2)
3. Verify correct floor assignment
```

---

## ⏱️ Time Allocation (45 minutes)

- **5 mins:** Clarify requirements, ask questions
- **5 mins:** List entities and relationships
- **5 mins:** Identify design patterns
- **25 mins:** Write code (enums → model → strategy → factory → service → main)
- **5 mins:** Test with demo, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: Vehicle Creation</summary>

Use Factory Pattern:
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
</details>

<details>
<summary>Hint 2: Spot Assignment Logic</summary>

In `ParkingSpot.canFitVehicle()`:
```java
switch (vehicle.getType()) {
    case BIKE:
        return type == SpotType.BIKE;
    case CAR:
        return type == SpotType.COMPACT || type == SpotType.LARGE || type == SpotType.HANDICAPPED;
    case TRUCK:
        return type == SpotType.LARGE;
}
```
</details>

<details>
<summary>Hint 3: Singleton Pattern</summary>

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
</details>

<details>
<summary>Hint 4: Fee Calculation</summary>

```java
public interface PricingStrategy {
    double calculateFee(ParkingTicket ticket);
}

public class HourlyPricingStrategy implements PricingStrategy {
    private Map<VehicleType, Double> hourlyRates;
    
    public double calculateFee(ParkingTicket ticket) {
        long minutes = ticket.getParkingDurationInMinutes();
        double hours = Math.ceil(minutes / 60.0);
        return hours * hourlyRates.get(ticket.getVehicle().getType());
    }
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Add Electric Vehicle Charging Spots**
   - New spot type: `EV_CHARGING`
   - Additional charging fee

2. **Add Reservation System**
   - Reserve a spot in advance
   - Hold spot for 15 minutes

3. **Add Admin Panel**
   - Mark spots as "Out of Service"
   - View all active parkings
   - Generate revenue report

4. **Add Multi-tenancy**
   - Different pricing for members vs visitors
   - Monthly pass holders

5. **Add Waiting Queue**
   - If no spots available, add to queue
   - Notify when spot becomes available

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Factory, Singleton, Strategy patterns correctly
- [ ] Handle all test scenarios
- [ ] Have clear separation: model, service, strategy layers
- [ ] Calculate fees correctly
- [ ] Display availability accurately
- [ ] Handle edge cases (no spots, invalid ticket, etc.)
- [ ] Be extensible (easy to add new vehicle/spot types)

---

## 📁 File Structure

```
01-Parking-Lot/
├── README.md (this file)
├── src/
│   ├── enums/
│   │   ├── VehicleType.java
│   │   ├── SpotType.java
│   │   └── ParkingSpotStatus.java
│   ├── model/
│   │   ├── Vehicle.java
│   │   ├── Car.java
│   │   ├── Bike.java
│   │   ├── Truck.java
│   │   ├── ParkingSpot.java
│   │   ├── ParkingTicket.java
│   │   └── ParkingFloor.java
│   ├── factory/
│   │   └── VehicleFactory.java
│   ├── strategy/
│   │   ├── PricingStrategy.java
│   │   ├── HourlyPricingStrategy.java
│   │   └── FlatPricingStrategy.java
│   ├── service/
│   │   ├── ParkingLot.java
│   │   └── Payment.java
│   └── Main.java
└── SOLUTION.md
```

---

**Good luck! Start coding! 🚀**


