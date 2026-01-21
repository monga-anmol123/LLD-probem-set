# Solution: Food Delivery System (Zomato/Swiggy)

## ✅ Complete Implementation

This folder contains a fully working food delivery system demonstrating **Factory, Strategy, State, Observer, and Singleton** design patterns.

---

## 🏗️ Architecture Overview

```
┌──────────────────────────────────────────────────────────────┐
│                        Main.java                              │
│                   (Demo/Entry Point)                          │
└───────────────────────┬──────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┬───────────────┐
        │               │               │               │
        ▼               ▼               ▼               ▼
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│ Factory  │    │ Strategy │    │  State   │    │ Observer │
│          │    │          │    │          │    │          │
│ User     │    │ Delivery │    │  Order   │    │  Order   │
│ Factory  │    │ Pricing  │    │  State   │    │ Observer │
└──────────┘    └──────────┘    └──────────┘    └──────────┘
        │               │               │               │
        └───────────────┼───────────────┼───────────────┘
                        │               │
                        ▼               ▼
                ┌──────────────┐ ┌──────────────┐
                │   Service    │ │    Model     │
                │              │ │              │
                │ FoodDelivery │ │  Order       │
                │  Platform    │ │  Customer    │
                │ (Singleton)  │ │  Restaurant  │
                └──────────────┘ └──────────────┘
```

---

## 📦 Package Structure

```
src/
├── enums/                          # Type-safe enumerations
│   ├── OrderStatus.java           # PLACED, PREPARING, etc.
│   ├── CuisineType.java           # INDIAN, CHINESE, etc.
│   ├── MenuCategory.java          # APPETIZER, MAIN_COURSE, etc.
│   └── PaymentMethod.java         # CASH, CARD, UPI, WALLET
│
├── model/                          # Domain entities
│   ├── Address.java               # Delivery address with distance calculation
│   ├── MenuItem.java              # Menu item with price and availability
│   ├── Restaurant.java            # Restaurant with menu
│   ├── Cart.java                  # Shopping cart
│   ├── Customer.java              # Customer with cart and addresses
│   ├── DeliveryPartner.java      # Delivery partner
│   └── Order.java                 # Order with state and observers
│
├── state/                          # State Pattern
│   ├── OrderState.java            # State interface
│   ├── PlacedState.java           # Initial state
│   ├── PreparingState.java        # Restaurant preparing
│   ├── ReadyForPickupState.java   # Ready for delivery
│   ├── OutForDeliveryState.java   # Out for delivery
│   ├── DeliveredState.java        # Final state (success)
│   └── CancelledState.java        # Final state (cancelled)
│
├── strategy/                       # Strategy Pattern
│   ├── DeliveryPricingStrategy.java    # Strategy interface
│   ├── DistanceBasedPricing.java       # Base + per km
│   ├── SurgePricing.java               # Peak hour multiplier
│   └── FlatRatePricing.java            # Fixed rate
│
├── observer/                       # Observer Pattern
│   ├── OrderObserver.java         # Observer interface
│   ├── CustomerNotifier.java      # Notify customers
│   ├── RestaurantNotifier.java    # Notify restaurants
│   └── DeliveryPartnerNotifier.java # Notify delivery partners
│
├── factory/                        # Factory Pattern
│   └── UserFactory.java           # Create customers and partners
│
├── service/                        # Business logic
│   └── FoodDeliveryPlatform.java  # Main service (Singleton)
│
└── Main.java                       # Demo application
```

---

## 🎨 Design Patterns Explained

### 1. **Factory Pattern** (UserFactory)

**Purpose:** Centralize creation of users (customers and delivery partners)

**Implementation:**
```java
public class UserFactory {
    private static int customerCounter = 1;
    private static int partnerCounter = 1;
    
    public static Customer createCustomer(String name, String phone, String email) {
        String customerId = "CUST" + String.format("%04d", customerCounter++);
        return new Customer(customerId, name, phone, email);
    }
    
    public static DeliveryPartner createDeliveryPartner(String name, String phone, Address location) {
        String partnerId = "DP" + String.format("%04d", partnerCounter++);
        return new DeliveryPartner(partnerId, name, phone, location);
    }
}
```

**Benefits:**
- ✅ Automatic ID generation
- ✅ Centralized user creation logic
- ✅ Easy to add new user types
- ✅ Consistent ID format

**Usage:**
```java
Customer customer = UserFactory.createCustomer("John Doe", "+91-9876543210", "john@email.com");
DeliveryPartner partner = UserFactory.createDeliveryPartner("Raj", "+91-9876543220", location);
```

---

### 2. **Strategy Pattern** (DeliveryPricingStrategy)

**Purpose:** Different algorithms for calculating delivery charges

**Implementation:**
```java
public interface DeliveryPricingStrategy {
    double calculateDeliveryCharge(double distance, LocalTime orderTime);
    String getStrategyName();
}

// Strategy 1: Distance-based
public class DistanceBasedPricing implements DeliveryPricingStrategy {
    private static final double BASE_FARE = 20.0;
    private static final double PER_KM_CHARGE = 8.0;
    
    public double calculateDeliveryCharge(double distance, LocalTime orderTime) {
        return BASE_FARE + (distance * PER_KM_CHARGE);
    }
}

// Strategy 2: Surge pricing
public class SurgePricing implements DeliveryPricingStrategy {
    private static final double SURGE_MULTIPLIER = 1.5;
    
    public double calculateDeliveryCharge(double distance, LocalTime orderTime) {
        double baseCharge = BASE_FARE + (distance * PER_KM_CHARGE);
        if (isPeakHour(orderTime)) {
            baseCharge *= SURGE_MULTIPLIER;
        }
        return baseCharge;
    }
}

// Strategy 3: Flat rate
public class FlatRatePricing implements DeliveryPricingStrategy {
    private static final double FLAT_RATE = 40.0;
    
    public double calculateDeliveryCharge(double distance, LocalTime orderTime) {
        return FLAT_RATE;
    }
}
```

**Benefits:**
- ✅ Easy to switch pricing strategies at runtime
- ✅ Each strategy is independent and testable
- ✅ Easy to add new pricing strategies
- ✅ Follows Open/Closed Principle

**Usage:**
```java
// Set strategy
platform.setPricingStrategy(new SurgePricing());

// Calculate charge
double charge = pricingStrategy.calculateDeliveryCharge(distance, LocalTime.now());
```

---

### 3. **State Pattern** (OrderState)

**Purpose:** Order behavior changes based on current state

**Implementation:**
```java
public interface OrderState {
    void prepare(Order order);
    void readyForPickup(Order order);
    void outForDelivery(Order order);
    void deliver(Order order);
    void cancel(Order order);
}

public class PlacedState implements OrderState {
    public void prepare(Order order) {
        order.setState(new PreparingState());
        order.setStatus(OrderStatus.PREPARING);
        order.notifyObservers();
    }
    
    public void cancel(Order order) {
        order.setState(new CancelledState());
        order.setStatus(OrderStatus.CANCELLED);
        order.notifyObservers();
    }
    
    // Other transitions invalid from this state
    public void readyForPickup(Order order) {
        System.out.println("❌ Cannot mark as ready - order is not being prepared yet");
    }
}
```

**State Transition Diagram:**
```
PLACED ──prepare()──> PREPARING ──readyForPickup()──> READY_FOR_PICKUP
                                                              │
                                                              │ outForDelivery()
                                                              ▼
                                                       OUT_FOR_DELIVERY
                                                              │
                                                              │ deliver()
                                                              ▼
                                                          DELIVERED

Any state (except OUT_FOR_DELIVERY, DELIVERED) ──cancel()──> CANCELLED
```

**Benefits:**
- ✅ Clear state transitions
- ✅ Invalid transitions prevented
- ✅ Easy to add new states
- ✅ State-specific behavior encapsulated

**Usage:**
```java
Order order = new Order(...);  // Starts in PlacedState
order.prepare();               // Transitions to PreparingState
order.readyForPickup();        // Transitions to ReadyForPickupState
order.outForDelivery();        // Transitions to OutForDeliveryState
order.deliver();               // Transitions to DeliveredState
```

---

### 4. **Observer Pattern** (OrderObserver)

**Purpose:** Notify multiple parties when order state changes

**Implementation:**
```java
public interface OrderObserver {
    void onOrderUpdate(Order order);
}

public class CustomerNotifier implements OrderObserver {
    public void onOrderUpdate(Order order) {
        System.out.println("📱 [SMS to " + order.getCustomer().getPhone() + "] " +
            "Your order " + order.getOrderId() + " is now " + order.getStatus());
    }
}

public class RestaurantNotifier implements OrderObserver {
    public void onOrderUpdate(Order order) {
        System.out.println("🏪 [Restaurant " + order.getRestaurant().getName() + "] " +
            "Order " + order.getOrderId() + " status: " + order.getStatus());
    }
}

// In Order class
private List<OrderObserver> observers = new ArrayList<>();

public void notifyObservers() {
    for (OrderObserver observer : observers) {
        observer.onOrderUpdate(this);
    }
}
```

**Benefits:**
- ✅ Decouples notification logic from order
- ✅ Easy to add new notification channels
- ✅ Multiple observers can be notified simultaneously
- ✅ Follows Open/Closed Principle

**Usage:**
```java
// Register observers
order.addObserver(new CustomerNotifier());
order.addObserver(new RestaurantNotifier());
order.addObserver(new DeliveryPartnerNotifier());

// Any state change notifies all observers
order.prepare();  // All observers notified
```

---

### 5. **Singleton Pattern** (FoodDeliveryPlatform)

**Purpose:** Ensure only one instance of the platform exists

**Implementation:**
```java
public class FoodDeliveryPlatform {
    private static FoodDeliveryPlatform instance;
    
    private FoodDeliveryPlatform(String platformName) {
        // Private constructor
    }
    
    public static synchronized FoodDeliveryPlatform getInstance(String platformName) {
        if (instance == null) {
            instance = new FoodDeliveryPlatform(platformName);
        }
        return instance;
    }
}
```

**Benefits:**
- ✅ Single source of truth
- ✅ Global access point
- ✅ Controlled instantiation
- ✅ Thread-safe

---

## 💡 Key Design Decisions

### 1. **Distance Calculation**

Used simplified Haversine formula for calculating distance between addresses:

```java
public double distanceTo(Address other) {
    // Convert to radians
    double lat1 = Math.toRadians(this.latitude);
    double lon1 = Math.toRadians(this.longitude);
    double lat2 = Math.toRadians(other.latitude);
    double lon2 = Math.toRadians(other.longitude);
    
    // Haversine formula
    double dlon = lon2 - lon1;
    double dlat = lat2 - lat1;
    double a = Math.pow(Math.sin(dlat / 2), 2) + 
               Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
    double c = 2 * Math.asin(Math.sqrt(a));
    double r = 6371; // Radius of earth in kilometers
    
    return c * r;
}
```

---

### 2. **Order State Management**

Each state knows which transitions are valid:

```java
// From PlacedState
prepare() → PreparingState ✓
cancel() → CancelledState ✓
readyForPickup() → Error ❌
outForDelivery() → Error ❌
deliver() → Error ❌
```

---

### 3. **Automatic Notification**

Every state transition automatically notifies all observers:

```java
public void prepare() {
    order.setState(new PreparingState());
    order.setStatus(OrderStatus.PREPARING);
    order.notifyObservers();  // Automatic notification
}
```

---

### 4. **Delivery Partner Assignment**

Finds nearest available delivery partner:

```java
private DeliveryPartner findNearestDeliveryPartner(Address restaurantLocation) {
    return deliveryPartners.values().stream()
        .filter(DeliveryPartner::isAvailable)
        .min(Comparator.comparingDouble(p -> 
            p.getCurrentLocation().distanceTo(restaurantLocation)))
        .orElse(null);
}
```

---

## 🔍 How It All Works Together

### Complete Order Flow

```
1. Customer browses restaurants
   └─> FoodDeliveryPlatform.searchRestaurants()

2. Customer adds items to cart
   └─> Cart.addItem()

3. Customer places order
   └─> FoodDeliveryPlatform.placeOrder()
       ├─> Calculate distance
       ├─> Apply pricing strategy (Strategy Pattern)
       ├─> Create order in PlacedState (State Pattern)
       ├─> Register observers (Observer Pattern)
       └─> Notify all observers

4. Restaurant prepares food
   └─> order.prepare()
       ├─> PlacedState → PreparingState (State Pattern)
       └─> Notify observers (Observer Pattern)

5. Food ready
   └─> order.readyForPickup()
       ├─> PreparingState → ReadyForPickupState
       └─> Notify observers

6. Assign delivery partner
   └─> platform.assignDeliveryPartner()
       ├─> Find nearest available partner
       └─> Partner accepts order

7. Out for delivery
   └─> order.outForDelivery()
       ├─> ReadyForPickupState → OutForDeliveryState
       └─> Notify observers

8. Delivered
   └─> order.deliver()
       ├─> OutForDeliveryState → DeliveredState
       ├─> Partner marked available
       └─> Notify observers
```

---

## 📊 Class Relationships

```
┌─────────────────┐
│ FoodDelivery    │
│ Platform        │◄────────────┐
│ (Singleton)     │             │
└─────────────────┘             │
         │                      │
         │ manages              │ uses
         ▼                      │
┌─────────────────┐    ┌────────────────┐
│     Order       │───>│ OrderState     │
│                 │    │ (Interface)    │
└─────────────────┘    └────────────────┘
         │                      △
         │ has                  │ implements
         ▼             ┌────────┼────────┐
┌─────────────────┐   │        │        │
│  OrderObserver  │   │   ┌────────┐   │
│  (Interface)    │   │   │Placed  │   │
└─────────────────┘   │   │State   │   │
         △            │   └────────┘   │
         │            │                │
    ┌────┼────┐       │   ┌────────┐  │
    │    │    │       └───│Preparing│  │
┌───────┐│┌───────┐       │State   │  │
│Customer││Restaurant│     └────────┘  │
│Notifier│││Notifier│                  │
└───────┘│└───────┘       ┌────────┐  │
         │                │Ready   │──┘
    ┌────────┐            │State   │
    │Delivery│            └────────┘
    │Partner │
    │Notifier│            ┌────────┐
    └────────┘            │OutFor  │
                          │Delivery│
┌─────────────────┐       │State   │
│ DeliveryPricing │       └────────┘
│ Strategy        │
│ (Interface)     │       ┌────────┐
└─────────────────┘       │Delivered│
         △                │State   │
         │                └────────┘
    ┌────┼────┐
    │    │    │           ┌────────┐
┌───────┐│┌───────┐       │Cancelled│
│Distance││Surge  │       │State   │
│Based  │││Pricing│       └────────┘
└───────┘│└───────┘
         │
    ┌────────┐
    │FlatRate│
    │Pricing │
    └────────┘
```

---

## ⚖️ Trade-offs and Alternatives

### 1. **State Pattern vs. Enum with Switch**

**Current (State Pattern):**
```java
order.prepare();  // Delegates to current state
```

**Alternative (Enum with Switch):**
```java
switch (order.getStatus()) {
    case PLACED:
        order.setStatus(OrderStatus.PREPARING);
        break;
    case PREPARING:
        // Error
        break;
}
```

**Trade-offs:**
- ✅ State Pattern: Cleaner, extensible, follows OOP
- ❌ State Pattern: More classes
- ✅ Enum: Simpler, fewer classes
- ❌ Enum: Violates Open/Closed Principle, harder to extend

---

### 2. **Observer Pattern vs. Direct Notification**

**Current (Observer):**
```java
order.notifyObservers();  // All observers notified
```

**Alternative (Direct):**
```java
notifyCustomer(order);
notifyRestaurant(order);
notifyDeliveryPartner(order);
```

**Trade-offs:**
- ✅ Observer: Decoupled, easy to add new observers
- ❌ Observer: More complexity
- ✅ Direct: Simpler
- ❌ Direct: Tight coupling, hard to extend

---

## 🚀 Extensibility

### Adding a New Order State

1. Create new state class:
```java
public class PackagingState implements OrderState {
    public void package(Order order) {
        order.setState(new ReadyForPickupState());
        order.notifyObservers();
    }
}
```

2. Update transition in previous state:
```java
public class PreparingState implements OrderState {
    public void readyForPickup(Order order) {
        order.setState(new PackagingState());  // New intermediate state
        order.notifyObservers();
    }
}
```

---

### Adding a New Pricing Strategy

```java
public class SubscriptionPricing implements DeliveryPricingStrategy {
    public double calculateDeliveryCharge(double distance, LocalTime orderTime) {
        return 0.0;  // Free delivery for subscribers
    }
    
    public String getStrategyName() {
        return "Subscription Pricing (Free delivery)";
    }
}

// Use it
platform.setPricingStrategy(new SubscriptionPricing());
```

---

### Adding a New Notification Channel

```java
public class PushNotifier implements OrderObserver {
    public void onOrderUpdate(Order order) {
        System.out.println("🔔 [Push Notification] Order " + order.getOrderId() + 
                          " is now " + order.getStatus());
    }
}

// Register it
order.addObserver(new PushNotifier());
```

---

## 📈 Performance Considerations

### Time Complexity

| Operation | Complexity | Notes |
|-----------|-----------|-------|
| Place Order | O(1) | Direct operations |
| State Transition | O(n) | n = number of observers |
| Find Nearest Partner | O(m) | m = number of partners |
| Search Restaurants | O(r) | r = number of restaurants |
| Calculate Distance | O(1) | Haversine formula |

### Space Complexity

- **Orders:** O(n) where n = number of orders
- **Restaurants:** O(r) where r = number of restaurants
- **Delivery Partners:** O(m) where m = number of partners
- **Overall:** O(n + r + m)

---

## ✅ What This Solution Demonstrates

### Design Patterns
1. ✅ **Factory Pattern** - User creation
2. ✅ **Strategy Pattern** - Delivery pricing
3. ✅ **State Pattern** - Order lifecycle
4. ✅ **Observer Pattern** - Notifications
5. ✅ **Singleton Pattern** - Platform instance

### OOP Principles
1. ✅ **Encapsulation** - Private fields, public methods
2. ✅ **Abstraction** - Interfaces for patterns
3. ✅ **Polymorphism** - Different implementations
4. ✅ **Inheritance** - State and strategy hierarchies

### Best Practices
1. ✅ **Separation of Concerns** - Clear package structure
2. ✅ **Single Responsibility** - Each class has one job
3. ✅ **Open/Closed Principle** - Easy to extend
4. ✅ **Dependency Inversion** - Depend on abstractions
5. ✅ **Interface Segregation** - Focused interfaces

---

## 🎓 Learning Outcomes

After studying this solution, you should understand:

1. **When to use State Pattern**
   - Complex state machines
   - State-dependent behavior
   - Clear state transitions

2. **When to use Strategy Pattern**
   - Multiple algorithms for same task
   - Runtime algorithm selection
   - Avoid conditional logic

3. **When to use Observer Pattern**
   - One-to-many dependencies
   - Event-driven systems
   - Decoupled notifications

4. **How to combine multiple patterns**
   - Patterns work together
   - Each solves different problem
   - Synergy between patterns

---

## 🔧 Testing Scenarios Covered

1. ✅ Restaurant and menu setup
2. ✅ User registration (Factory Pattern)
3. ✅ Order placement
4. ✅ Complete order lifecycle (State Pattern)
5. ✅ Multiple pricing strategies (Strategy Pattern)
6. ✅ Notifications to all parties (Observer Pattern)
7. ✅ Delivery partner assignment
8. ✅ Edge cases (empty cart, invalid transitions, etc.)

---

**This is a complete, production-quality implementation suitable for interview discussions and real-world adaptation!** 🎉

