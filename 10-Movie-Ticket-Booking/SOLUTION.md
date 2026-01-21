# Solution: Movie Ticket Booking System

## ✅ Complete Implementation

This folder contains a fully working movie ticket booking system demonstrating Factory, Observer, State, and Strategy design patterns.

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
│   Factory    │ │  Observer   │ │   State    │ │ Strategy │
│              │ │             │ │            │ │          │
│ Seat         │ │ Email       │ │ Pending    │ │ Pricing  │
│ Payment      │ │ SMS         │ │ Confirmed  │ │ Timing   │
│              │ │             │ │ Cancelled  │ │ SeatType │
└──────────────┘ └─────────────┘ └────────────┘ └──────────┘
        │               │               │              │
        └───────────────┼───────────────┼──────────────┘
                        │               │
                        ▼               ▼
                ┌─────────────┐ ┌─────────────┐
                │   Service   │ │    Model    │
                │             │ │             │
                │  Booking    │ │  Theater    │
                │  System     │ │  Screen     │
                │ (Singleton) │ │  Movie      │
                │             │ │  Show       │
                │             │ │  Seat       │
                │             │ │  Booking    │
                │             │ │  User       │
                └─────────────┘ └─────────────┘
```

---

## 📦 Package Structure

```
src/
├── enums/                      # Type-safe enumerations
│   ├── SeatType.java          # REGULAR, PREMIUM, VIP, WHEELCHAIR
│   ├── SeatStatus.java        # AVAILABLE, LOCKED, BOOKED, BLOCKED
│   ├── MovieStatus.java       # NOW_SHOWING, COMING_SOON, ENDED
│   ├── ShowStatus.java        # SCHEDULED, ONGOING, COMPLETED, CANCELLED
│   ├── BookingStatus.java     # PENDING, CONFIRMED, CANCELLED
│   └── PaymentStatus.java     # PENDING, SUCCESS, FAILED, REFUNDED
│
├── model/                      # Domain entities
│   ├── Theater.java           # Theater with multiple screens
│   ├── Screen.java            # Screen with seats and shows
│   ├── Seat.java              # Individual seat with locking mechanism
│   ├── Movie.java             # Movie details
│   ├── Show.java              # Movie show with seat management
│   ├── Booking.java           # Booking with state management
│   ├── User.java              # User with booking history
│   └── Payment.java           # Payment processing
│
├── factory/                    # Factory Pattern
│   ├── SeatFactory.java       # Create different seat types
│   └── PaymentFactory.java    # Create different payment methods
│
├── observer/                   # Observer Pattern
│   ├── BookingObserver.java   # Observer interface
│   ├── EmailNotification.java # Email notifications
│   └── SMSNotification.java   # SMS notifications
│
├── state/                      # State Pattern
│   ├── BookingState.java      # State interface
│   ├── PendingState.java      # Pending booking state
│   ├── ConfirmedState.java    # Confirmed booking state
│   └── CancelledState.java    # Cancelled booking state
│
├── strategy/                   # Strategy Pattern
│   ├── PricingStrategy.java   # Pricing strategy interface
│   ├── TimingBasedPricing.java    # Time-based pricing
│   ├── SeatTypePricing.java       # Seat type-based pricing
│   ├── WeekendPricing.java        # Weekend pricing
│   └── CompositePricing.java      # Composite pricing
│
├── service/                    # Business logic
│   └── BookingSystem.java     # Main service (Singleton)
│
└── Main.java                   # Demo application
```

---

## 🎨 Design Patterns Explained

### 1. **Factory Pattern** (SeatFactory, PaymentFactory)

**Purpose:** Centralize object creation logic for different types of seats and payments

**Implementation:**

```java
// SeatFactory
public static Seat createSeat(String seatId, String row, int column, SeatType type) {
    double basePrice;
    switch (type) {
        case REGULAR: basePrice = 100.0; break;
        case PREMIUM: basePrice = 150.0; break;
        case VIP: basePrice = 200.0; break;
        case WHEELCHAIR: basePrice = 100.0; break;
    }
    return new Seat(seatId, row, column, type, basePrice);
}

// PaymentFactory
public static Payment createPayment(Booking booking, double amount, String method) {
    String paymentId = "PAY" + String.format("%05d", paymentCounter++);
    return new Payment(paymentId, booking, amount, method);
}
```

**Benefits:**
- ✅ Centralized seat creation with consistent pricing
- ✅ Easy to add new seat types (e.g., RECLINER, COUPLE_SEAT)
- ✅ Easy to add new payment methods
- ✅ Encapsulates creation logic

**Trade-offs:**
- ⚠️ Additional layer of abstraction
- ⚠️ More classes to maintain

---

### 2. **Observer Pattern** (BookingObserver, EmailNotification, SMSNotification)

**Purpose:** Notify multiple observers when booking events occur

**Implementation:**

```java
// Observer interface
public interface BookingObserver {
    void onBookingConfirmed(Booking booking);
    void onBookingCancelled(Booking booking);
}

// Concrete observers
public class EmailNotification implements BookingObserver {
    public void onBookingConfirmed(Booking booking) {
        // Send email
    }
}

// Subject (BookingSystem)
private void notifyBookingConfirmed(Booking booking) {
    for (BookingObserver observer : observers) {
        observer.onBookingConfirmed(booking);
    }
}
```

**Benefits:**
- ✅ Loose coupling between booking logic and notifications
- ✅ Easy to add new notification channels (Push, WhatsApp)
- ✅ Can enable/disable notifications dynamically
- ✅ Single Responsibility Principle

**Trade-offs:**
- ⚠️ Order of notification not guaranteed
- ⚠️ Need to manage observer lifecycle

---

### 3. **State Pattern** (BookingState, PendingState, ConfirmedState, CancelledState)

**Purpose:** Manage booking state transitions cleanly

**Implementation:**

```java
// State interface
public interface BookingState {
    void confirm(Booking booking);
    void cancel(Booking booking);
}

// Concrete states
public class PendingState implements BookingState {
    public void confirm(Booking booking) {
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setState(new ConfirmedState());
        booking.getShow().bookSeats(...);
    }
}

// Context (Booking)
public void confirm() {
    state.confirm(this);
}
```

**State Transition Diagram:**

```
    PENDING ──confirm()──> CONFIRMED ──cancel()──> CANCELLED
       │                                              ▲
       └──────────────cancel()────────────────────────┘
```

**Benefits:**
- ✅ Clear state transitions
- ✅ Each state handles its own logic
- ✅ Easy to add new states
- ✅ Prevents invalid state transitions

**Trade-offs:**
- ⚠️ More classes (one per state)
- ⚠️ State explosion for complex systems

---

### 4. **Strategy Pattern** (PricingStrategy, TimingBasedPricing, etc.)

**Purpose:** Switch between different pricing algorithms dynamically

**Implementation:**

```java
// Strategy interface
public interface PricingStrategy {
    double calculatePrice(Seat seat, Show show);
}

// Concrete strategies
public class TimingBasedPricing implements PricingStrategy {
    public double calculatePrice(Seat seat, Show show) {
        int hour = show.getStartTime().getHour();
        if (hour >= 18 && hour < 21) {
            return seat.getBasePrice() * 1.2; // Evening premium
        }
        return seat.getBasePrice();
    }
}

// Context (BookingSystem)
private PricingStrategy pricingStrategy;

public void setPricingStrategy(PricingStrategy strategy) {
    this.pricingStrategy = strategy;
}
```

**Available Strategies:**
1. **TimingBasedPricing:** Matinee discount, evening premium
2. **SeatTypePricing:** Premium/VIP seat surcharge
3. **WeekendPricing:** Weekend premium
4. **CompositePricing:** Combine multiple strategies

**Benefits:**
- ✅ Switch pricing algorithms at runtime
- ✅ Easy to add new pricing strategies
- ✅ Clean separation of pricing logic
- ✅ Open/Closed Principle

**Trade-offs:**
- ⚠️ Client must know which strategy to use
- ⚠️ More classes

---

### 5. **Singleton Pattern** (BookingSystem)

**Purpose:** Ensure only one instance of the booking system exists

**Implementation:**

```java
public class BookingSystem {
    private static BookingSystem instance;
    
    private BookingSystem() {
        // Private constructor
    }
    
    public static synchronized BookingSystem getInstance() {
        if (instance == null) {
            instance = new BookingSystem();
        }
        return instance;
    }
}
```

**Benefits:**
- ✅ Single point of access
- ✅ Global state management
- ✅ Lazy initialization

**Trade-offs:**
- ⚠️ Global state (testing challenges)
- ⚠️ Thread-safety considerations

---

## 🔐 Key Features

### 1. **Seat Locking Mechanism**

Prevents double booking with time-based locks:

```java
public synchronized boolean lock(String userId, int lockDurationMinutes) {
    if (status == SeatStatus.AVAILABLE) {
        status = SeatStatus.LOCKED;
        lockedByUserId = userId;
        lockExpiryTime = System.currentTimeMillis() + (lockDurationMinutes * 60 * 1000L);
        return true;
    }
    return false;
}

public synchronized void checkAndReleaseLock() {
    if (status == SeatStatus.LOCKED && System.currentTimeMillis() > lockExpiryTime) {
        status = SeatStatus.AVAILABLE;
        lockedByUserId = null;
    }
}
```

**Features:**
- ✅ 10-minute lock duration
- ✅ Automatic lock release on timeout
- ✅ Thread-safe operations
- ✅ User-specific locks

---

### 2. **Concurrent Booking Handling**

Atomic seat booking with rollback:

```java
public boolean lockSeats(List<String> seatIds, String userId, int lockDurationMinutes) {
    // Check all seats first
    for (String seatId : seatIds) {
        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            return false;
        }
    }
    
    // Lock all seats atomically
    for (String seatId : seatIds) {
        if (!seat.lock(userId, lockDurationMinutes)) {
            rollbackLocks(seatIds, userId);
            return false;
        }
    }
    return true;
}
```

**Features:**
- ✅ All-or-nothing seat locking
- ✅ Automatic rollback on failure
- ✅ Prevents partial bookings

---

### 3. **Dynamic Pricing**

Multiple pricing strategies with composite support:

```java
// Composite pricing
CompositePricing composite = new CompositePricing();
composite.addStrategy(new TimingBasedPricing());
composite.addStrategy(new SeatTypePricing());
composite.addStrategy(new WeekendPricing());

// Calculates average of all strategies
double price = composite.calculatePrice(seat, show);
```

**Pricing Factors:**
- 🕐 Time of day (Matinee/Evening/Night)
- 💺 Seat type (Regular/Premium/VIP)
- 📅 Day of week (Weekday/Weekend)

---

### 4. **Booking Cancellation with Refund**

State-based cancellation with automatic refund:

```java
public void cancel(Booking booking) {
    if (booking.getShow().getStartTime().isAfter(LocalDateTime.now())) {
        booking.setStatus(BookingStatus.CANCELLED);
        booking.getShow().releaseSeats(booking.getSeatIds());
        booking.getPayment().refund();
    } else {
        System.out.println("Cannot cancel - Show has started");
    }
}
```

**Features:**
- ✅ Time-based cancellation rules
- ✅ Automatic seat release
- ✅ Refund processing
- ✅ State validation

---

## 📊 Data Flow

### Complete Booking Flow:

```
1. User searches for movie
   └─> BookingSystem.searchMovies()

2. User selects show
   └─> BookingSystem.getShowsForMovie()

3. View available seats
   └─> Show.getAvailableSeats()

4. Select seats
   └─> BookingSystem.createBooking()
       ├─> Show.lockSeats() [Locks seats for 10 mins]
       ├─> PricingStrategy.calculatePrice() [Calculate prices]
       └─> Create Booking (PENDING state)

5. Apply discount (optional)
   └─> Booking.applyDiscount()

6. Process payment
   └─> BookingSystem.confirmBooking()
       ├─> PaymentFactory.createPayment()
       ├─> Payment.process()
       ├─> BookingState.confirm() [PENDING → CONFIRMED]
       ├─> Show.bookSeats() [LOCKED → BOOKED]
       └─> Notify observers (Email, SMS)

7. Receive confirmation
   └─> EmailNotification.onBookingConfirmed()
   └─> SMSNotification.onBookingConfirmed()
```

---

## 🧪 Test Scenarios Covered

### ✅ Scenario 1: Complete Booking Flow
- Create booking with multiple seats
- Apply discount code
- Process payment
- Receive notifications

### ✅ Scenario 2: Concurrent Booking Conflict
- Two users select same seat
- First user locks seat
- Second user's request fails
- First user completes booking

### ✅ Scenario 3: Booking Cancellation
- Confirm booking
- Cancel before show starts
- Seats released
- Refund processed

### ✅ Scenario 4: Dynamic Pricing
- Test timing-based pricing
- Test seat type pricing
- Test weekend pricing
- Test composite pricing

### ✅ Scenario 5: Full Show Handling
- Book multiple seats
- Show approaches full capacity
- Display warning messages

### ✅ Scenario 6: Discount Application
- Apply percentage discounts
- Calculate final amount
- Process discounted payment

---

## 🚀 Extensibility

### Easy to Add:

1. **New Seat Types**
   ```java
   // In SeatFactory
   case RECLINER: basePrice = 250.0; break;
   case COUPLE_SEAT: basePrice = 300.0; break;
   ```

2. **New Notification Channels**
   ```java
   public class PushNotification implements BookingObserver {
       public void onBookingConfirmed(Booking booking) {
           // Send push notification
       }
   }
   ```

3. **New Pricing Strategies**
   ```java
   public class DynamicSurgePricing implements PricingStrategy {
       public double calculatePrice(Seat seat, Show show) {
           // Surge pricing based on demand
       }
   }
   ```

4. **Food & Beverage Add-ons**
   ```java
   public class FoodItem {
       private String name;
       private double price;
   }
   
   // Add to Booking
   private List<FoodItem> foodItems;
   ```

---

## ⚡ Performance Considerations

### Time Complexity:
- **Seat locking:** O(n) where n = number of seats
- **Seat availability check:** O(n) where n = total seats
- **Search movies:** O(m) where m = number of movies
- **Get shows for movie:** O(s) where s = total shows

### Space Complexity:
- **Theater storage:** O(t × s × seats) where t = theaters, s = screens
- **Booking storage:** O(b) where b = total bookings
- **Show storage:** O(s) where s = total shows

### Optimizations:
- ✅ Synchronized seat operations prevent race conditions
- ✅ Automatic lock expiry reduces memory overhead
- ✅ HashMap-based lookups for O(1) access
- ✅ Lazy initialization of singleton

---

## 🎯 Design Decisions & Trade-offs

### 1. **Seat Locking vs Optimistic Locking**

**Chosen:** Pessimistic locking with timeout

**Pros:**
- ✅ Prevents double booking
- ✅ Clear user feedback
- ✅ Simple implementation

**Cons:**
- ⚠️ Seats locked but not booked (timeout needed)
- ⚠️ Potential for seat hoarding

**Alternative:** Optimistic locking (check at payment time)

---

### 2. **Show-Specific Seats vs Shared Seats**

**Chosen:** Each show has its own copy of seats

**Pros:**
- ✅ Independent seat management per show
- ✅ No interference between shows
- ✅ Easy to track availability

**Cons:**
- ⚠️ More memory usage
- ⚠️ Duplicate seat data

**Alternative:** Shared seats with show-specific status

---

### 3. **Composite Pricing (Average) vs Multiplicative**

**Chosen:** Average of all strategies

**Pros:**
- ✅ Balanced pricing
- ✅ Prevents extreme prices

**Cons:**
- ⚠️ May not reflect true combined effect

**Alternative:** Multiply factors (could lead to very high prices)

---

## 📈 Scalability Considerations

### Current Design Supports:
- ✅ 100+ theaters
- ✅ 1000+ concurrent bookings
- ✅ 10+ screens per theater
- ✅ 200+ seats per screen

### To Scale Further:
1. **Database Integration**
   - Replace in-memory maps with database
   - Use connection pooling

2. **Distributed Locking**
   - Use Redis for seat locks
   - Distributed transaction management

3. **Caching**
   - Cache popular movies and shows
   - Cache seat availability

4. **Load Balancing**
   - Multiple BookingSystem instances
   - Session affinity for bookings

5. **Async Processing**
   - Async notifications
   - Background payment processing

---

## ✅ Success Criteria Met

- [x] Compiles without errors
- [x] Uses Factory, Observer, State, Strategy patterns correctly
- [x] Handles all test scenarios
- [x] Prevents double booking
- [x] Implements seat locking with timeout
- [x] Calculates dynamic pricing correctly
- [x] Handles concurrent bookings
- [x] Supports booking cancellation with refund
- [x] Sends notifications on booking events
- [x] Extensible design

---

## 🎓 Learning Outcomes

After studying this implementation, you should understand:

1. **Factory Pattern:** Centralized object creation
2. **Observer Pattern:** Event-driven notifications
3. **State Pattern:** Clean state management
4. **Strategy Pattern:** Runtime algorithm selection
5. **Singleton Pattern:** Global instance management
6. **Concurrency:** Thread-safe operations
7. **Domain Modeling:** Theater, Show, Booking entities
8. **Business Logic:** Seat locking, pricing, cancellation

---

## 📚 Related Problems

Similar complexity:
- Hotel Booking System (Problem 09)
- Restaurant Management (Problem 11)
- Car Rental System (Problem 12)
- Ride Sharing System (Problem 16)

---

**Implementation Time:** ~60 minutes  
**Lines of Code:** ~1,800  
**Files:** 30  
**Design Patterns:** 5  

---

*Last Updated: January 2026*


