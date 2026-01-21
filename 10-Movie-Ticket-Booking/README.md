# Problem 10: Movie Ticket Booking System

## 🎯 Difficulty: Medium ⭐⭐⭐

## 📝 Problem Statement

Design a movie ticket booking system like BookMyShow that allows users to search movies, view showtimes, book seats, and process payments. The system should support multiple theaters, screens, movies, and seat types with dynamic pricing.

---

## 🔍 Functional Requirements (FR)

### FR1: Theater & Screen Management
- Support multiple theaters in different cities
- Each theater has multiple screens
- Each screen has a unique seating layout (rows and columns)
- Seat types: REGULAR, PREMIUM, VIP, WHEELCHAIR

### FR2: Movie Management
- Add/remove movies with details (title, genre, duration, rating)
- Multiple languages and formats (2D, 3D, IMAX)
- Movie status: NOW_SHOWING, COMING_SOON, ENDED

### FR3: Show Management
- Create shows with movie, screen, date, and time
- Show status: SCHEDULED, ONGOING, COMPLETED, CANCELLED
- No overlapping shows on the same screen

### FR4: Seat Booking
- View available seats for a show
- Select multiple seats (max 10 per booking)
- Lock seats for 10 minutes during booking process
- Seat status: AVAILABLE, BOOKED, LOCKED, BLOCKED

### FR5: Booking Management
- Create booking with user details
- Generate unique booking ID
- Booking status: PENDING, CONFIRMED, CANCELLED
- Support cancellation before show starts

### FR6: Pricing & Payment
- Dynamic pricing based on:
  - Seat type (Regular < Premium < VIP)
  - Show timing (Matinee < Evening < Night)
  - Day of week (Weekday < Weekend)
- Apply discount codes
- Process payment and generate ticket

### FR7: Search & Filter
- Search movies by title, genre, language
- Filter shows by theater, date, time
- View theater locations

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Scalability
- Support 100+ theaters across multiple cities
- Handle 1000+ concurrent bookings
- Each theater can have 10+ screens
- Each screen can have 200+ seats

### NFR2: Concurrency
- Handle simultaneous seat selection by multiple users
- Prevent double booking
- Seat locking mechanism with timeout

### NFR3: Extensibility
- Easy to add new seat types (e.g., RECLINER, COUPLE_SEAT)
- Easy to add new pricing strategies (e.g., Dynamic surge pricing)
- Easy to add new payment methods
- Support for food & beverage add-ons

### NFR4: Performance
- Search results in <500ms
- Seat availability check in <200ms
- Booking confirmation in <2 seconds

### NFR5: Reliability
- Handle edge cases:
  - Seats already booked
  - Show fully booked
  - Booking timeout
  - Payment failure
  - Invalid discount codes

---

## 🎨 Design Patterns to Use

### 1. **Factory Pattern**
- **Where:** Creating different seat types, payment methods
- **Why:** Centralize object creation, easy to add new types

### 2. **Observer Pattern**
- **Where:** Notify users about booking confirmation, seat availability
- **Why:** Decouple notification logic from booking logic

### 3. **State Pattern**
- **Where:** Booking state transitions (PENDING → CONFIRMED → CANCELLED)
- **Why:** Clean state management with clear transitions

### 4. **Strategy Pattern**
- **Where:** Pricing strategies (time-based, seat-based, day-based)
- **Why:** Switch between different pricing algorithms dynamically

### 5. **Singleton Pattern**
- **Where:** BookingSystem (central coordinator)
- **Why:** Single point of access, manage global state

---

## 📋 Core Entities

### 1. **Theater**
- Attributes: `theaterId`, `name`, `city`, `address`, `screens`
- Methods: `addScreen()`, `getScreen()`, `getAllScreens()`

### 2. **Screen**
- Attributes: `screenId`, `name`, `theater`, `seats`, `shows`
- Methods: `addShow()`, `getAvailableSeats()`, `getSeatLayout()`

### 3. **Seat**
- Attributes: `seatId`, `row`, `column`, `type`, `status`, `basePrice`
- Methods: `lock()`, `unlock()`, `book()`, `release()`

### 4. **Movie**
- Attributes: `movieId`, `title`, `genre`, `duration`, `language`, `rating`, `status`
- Methods: `getDetails()`, `updateStatus()`

### 5. **Show**
- Attributes: `showId`, `movie`, `screen`, `startTime`, `endTime`, `status`, `seatAvailability`
- Methods: `getAvailableSeats()`, `lockSeats()`, `bookSeats()`, `releaseSeats()`

### 6. **Booking**
- Attributes: `bookingId`, `user`, `show`, `seats`, `totalAmount`, `status`, `bookingTime`
- Methods: `confirm()`, `cancel()`, `applyDiscount()`, `processPayment()`

### 7. **User**
- Attributes: `userId`, `name`, `email`, `phone`, `bookingHistory`
- Methods: `createBooking()`, `cancelBooking()`, `viewBookings()`

### 8. **Payment**
- Attributes: `paymentId`, `booking`, `amount`, `method`, `status`
- Methods: `process()`, `refund()`

---

## 🧪 Test Scenarios

### Scenario 1: Complete Booking Flow
```
1. Create theater with 2 screens
2. Add movie "Inception"
3. Create show for tomorrow 7 PM
4. User searches for "Inception"
5. User selects show and views seats
6. User selects 3 seats (2 Regular, 1 Premium)
7. System locks seats for 10 minutes
8. User applies discount code "FIRST50"
9. Calculate final price
10. Process payment
11. Confirm booking and generate ticket
12. Send confirmation notification
```

### Scenario 2: Concurrent Booking Conflict
```
1. User A and User B both select Seat A5
2. User A locks seat first
3. User B's request should fail (seat locked)
4. User A completes payment
5. Seat A5 marked as BOOKED
6. User B tries again - seat unavailable
```

### Scenario 3: Booking Timeout
```
1. User selects 5 seats
2. Seats locked for 10 minutes
3. User doesn't complete payment
4. After 10 minutes, seats automatically released
5. Seats become available again
```

### Scenario 4: Booking Cancellation
```
1. User books 4 seats for tomorrow's show
2. User cancels booking 2 hours before show
3. System processes refund (80% refund)
4. Seats marked as AVAILABLE
5. Other users can now book these seats
```

### Scenario 5: Dynamic Pricing
```
1. Show at 2 PM (Matinee): Regular seat = $100
2. Show at 7 PM (Evening): Regular seat = $150
3. Show at 10 PM (Night): Regular seat = $120
4. Weekend shows: +20% surcharge
5. Premium seats: +50% on base price
6. VIP seats: +100% on base price
```

### Scenario 6: Full Show Handling
```
1. Show has 100 seats
2. 100 users book all seats
3. 101st user tries to book
4. System shows "Show Full" message
5. Offer waitlist option
```

---

## ⏱️ Time Allocation (60 minutes)

- **5 mins:** Clarify requirements, ask questions
- **5 mins:** List entities and relationships
- **5 mins:** Identify design patterns
- **35 mins:** Write code (enums → model → factory → observer → state → strategy → service → main)
- **10 mins:** Test with demo scenarios, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: Seat Locking Mechanism</summary>

Use a timer to automatically release locked seats:

```java
public class Seat {
    private SeatStatus status;
    private long lockExpiryTime;
    
    public boolean lock() {
        if (status == SeatStatus.AVAILABLE) {
            status = SeatStatus.LOCKED;
            lockExpiryTime = System.currentTimeMillis() + (10 * 60 * 1000); // 10 mins
            return true;
        }
        return false;
    }
    
    public void checkAndReleaseLock() {
        if (status == SeatStatus.LOCKED && System.currentTimeMillis() > lockExpiryTime) {
            status = SeatStatus.AVAILABLE;
        }
    }
}
```
</details>

<details>
<summary>Hint 2: Dynamic Pricing Strategy</summary>

```java
public interface PricingStrategy {
    double calculatePrice(Seat seat, Show show);
}

public class TimingBasedPricing implements PricingStrategy {
    public double calculatePrice(Seat seat, Show show) {
        double basePrice = seat.getBasePrice();
        int hour = show.getStartTime().getHour();
        
        if (hour >= 12 && hour < 15) {
            return basePrice * 0.8; // Matinee discount
        } else if (hour >= 18 && hour < 21) {
            return basePrice * 1.2; // Evening premium
        }
        return basePrice;
    }
}
```
</details>

<details>
<summary>Hint 3: Observer Pattern for Notifications</summary>

```java
public interface BookingObserver {
    void onBookingConfirmed(Booking booking);
    void onBookingCancelled(Booking booking);
}

public class EmailNotification implements BookingObserver {
    public void onBookingConfirmed(Booking booking) {
        System.out.println("Email sent to: " + booking.getUser().getEmail());
    }
}

public class SMSNotification implements BookingObserver {
    public void onBookingConfirmed(Booking booking) {
        System.out.println("SMS sent to: " + booking.getUser().getPhone());
    }
}
```
</details>

<details>
<summary>Hint 4: State Pattern for Booking</summary>

```java
public interface BookingState {
    void confirm(Booking booking);
    void cancel(Booking booking);
}

public class PendingState implements BookingState {
    public void confirm(Booking booking) {
        booking.setState(new ConfirmedState());
    }
    
    public void cancel(Booking booking) {
        booking.setState(new CancelledState());
        booking.releaseSeats();
    }
}
```
</details>

<details>
<summary>Hint 5: Preventing Double Booking</summary>

```java
public synchronized boolean bookSeats(List<Seat> seats, User user) {
    // Check all seats are available/locked by this user
    for (Seat seat : seats) {
        if (seat.getStatus() != SeatStatus.LOCKED) {
            return false;
        }
    }
    
    // Book all seats atomically
    for (Seat seat : seats) {
        seat.book();
    }
    
    return true;
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Food & Beverage Add-ons**
   - Add popcorn, drinks to booking
   - Combo offers

2. **Loyalty Program**
   - Reward points for bookings
   - Tier-based benefits (Silver, Gold, Platinum)

3. **Recommendation Engine**
   - Suggest movies based on history
   - Popular movies in user's city

4. **Seat Selection Preferences**
   - Auto-suggest best available seats
   - Group seating optimization

5. **Multi-language Support**
   - Same movie in different languages
   - Different showtimes for each language

6. **Admin Dashboard**
   - View all bookings
   - Revenue reports
   - Occupancy analytics

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Factory, Observer, State, Strategy patterns correctly
- [ ] Handle all test scenarios
- [ ] Prevent double booking
- [ ] Implement seat locking with timeout
- [ ] Calculate dynamic pricing correctly
- [ ] Handle concurrent bookings
- [ ] Support booking cancellation with refund
- [ ] Send notifications on booking events
- [ ] Be extensible (easy to add new features)

---

## 📁 File Structure

```
10-Movie-Ticket-Booking/
├── README.md (this file)
├── SOLUTION.md
├── COMPILATION-GUIDE.md
└── src/
    ├── enums/
    │   ├── SeatType.java
    │   ├── SeatStatus.java
    │   ├── MovieStatus.java
    │   ├── ShowStatus.java
    │   ├── BookingStatus.java
    │   └── PaymentStatus.java
    ├── model/
    │   ├── Theater.java
    │   ├── Screen.java
    │   ├── Seat.java
    │   ├── Movie.java
    │   ├── Show.java
    │   ├── Booking.java
    │   ├── User.java
    │   └── Payment.java
    ├── factory/
    │   ├── SeatFactory.java
    │   └── PaymentFactory.java
    ├── observer/
    │   ├── BookingObserver.java
    │   ├── EmailNotification.java
    │   └── SMSNotification.java
    ├── state/
    │   ├── BookingState.java
    │   ├── PendingState.java
    │   ├── ConfirmedState.java
    │   └── CancelledState.java
    ├── strategy/
    │   ├── PricingStrategy.java
    │   ├── TimingBasedPricing.java
    │   ├── SeatTypePricing.java
    │   └── WeekendPricing.java
    ├── service/
    │   └── BookingSystem.java
    └── Main.java
```

---

**Good luck! Start coding! 🎬🍿**


