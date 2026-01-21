# Problem 9: Hotel Booking System

## Difficulty: Medium

## Problem Statement

Design and implement a hotel booking system that manages room reservations, handles multiple pricing strategies, prevents double bookings, and provides real-time notifications.

## Functional Requirements

### Core Features
1. **Room Management**
   - Support multiple room types (Single, Double, Suite, Deluxe)
   - Track room availability in real-time
   - Each room has unique number, type, floor, and base price

2. **Guest Management**
   - Register guests with contact information
   - Track loyalty points
   - Maintain booking history

3. **Booking Operations**
   - Create bookings with date validation
   - Check room availability for date ranges
   - Prevent double bookings (thread-safe)
   - Support check-in and check-out
   - Cancel bookings with refund policy

4. **Pricing Strategies**
   - **Base Pricing**: Standard room rate × nights
   - **Weekend Pricing**: 1.5x rate on Friday/Saturday
   - **Seasonal Pricing**: 2x peak season, 1.5x high season, 1x low season
   - **Loyalty Pricing**: 15% discount for loyalty members (100+ points)

5. **Search and Availability**
   - Search available rooms by date range
   - Filter by room type
   - Display room details and pricing

6. **Notifications**
   - Email notifications for booking updates
   - SMS notifications for status changes
   - Real-time observer pattern implementation

7. **Loyalty Program**
   - Earn 1 point per $10 spent
   - Loyalty status at 100+ points
   - Automatic discount application

## Non-Functional Requirements

1. **Concurrency Safety**
   - Thread-safe booking operations
   - Prevent race conditions in availability checks
   - Use ConcurrentHashMap for shared data

2. **Data Integrity**
   - Validate all dates (no past dates, check-out after check-in)
   - Prevent overlapping bookings for same room
   - Maintain consistent booking states

3. **Extensibility**
   - Easy to add new pricing strategies
   - Simple to add new notification channels
   - Support for new room types

4. **Performance**
   - O(n) availability search where n = number of bookings
   - Efficient date overlap detection
   - Fast booking creation and cancellation

5. **Usability**
   - Clear error messages
   - Comprehensive booking details
   - Easy-to-understand pricing breakdown

## Design Patterns Used

### 1. Strategy Pattern (Pricing)
- **Interface**: `PricingStrategy`
- **Implementations**:
  - `BasePricingStrategy`: Standard pricing
  - `WeekendPricingStrategy`: Weekend premium
  - `SeasonalPricingStrategy`: Season-based pricing
  - `LoyaltyPricingStrategy`: Loyalty discount
- **Benefit**: Easy to add new pricing models without modifying existing code

### 2. Observer Pattern (Notifications)
- **Interface**: `Observer`
- **Implementations**:
  - `EmailNotifier`: Email notifications
  - `SMSNotifier`: SMS notifications
- **Benefit**: Loose coupling between booking events and notification systems

### 3. Singleton Pattern (System)
- **Class**: `HotelBookingSystem`
- **Benefit**: Single point of control for all bookings and rooms

## Key Classes

### Enums
- `RoomType`: SINGLE, DOUBLE, SUITE, DELUXE (with base prices)
- `BookingStatus`: CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED
- `PaymentStatus`: PENDING, PAID, REFUNDED, FAILED

### Models
- `Room`: Room details and availability
- `Guest`: Guest information and loyalty points
- `Booking`: Booking details with date range and pricing

### Services
- `HotelBookingSystem`: Main system managing all operations

### Strategies
- `PricingStrategy`: Interface for pricing calculations
- Multiple concrete pricing implementations

### Observers
- `Observer`: Interface for notifications
- `EmailNotifier`, `SMSNotifier`: Concrete notification channels

## Scenarios Covered

1. **Basic Booking**: Standard booking with base pricing
2. **Weekend Pricing**: Booking with weekend premium rates
3. **Seasonal Pricing**: Peak season booking with 2x multiplier
4. **Loyalty Discount**: Booking with 15% loyalty member discount
5. **Room Search**: Search available rooms by date and type
6. **Notifications**: Real-time email and SMS notifications
7. **Double Booking Prevention**: Concurrent booking conflict detection
8. **Cancellation**: Booking cancellation with refund calculation
9. **Check-in/Check-out**: Complete booking lifecycle
10. **Booking History**: Guest booking history and loyalty tracking

## Edge Cases Handled

1. **Date Validation**
   - No bookings in the past
   - Check-out must be after check-in
   - Minimum 1 night stay

2. **Availability Conflicts**
   - Detect overlapping bookings
   - Prevent double booking
   - Thread-safe availability checks

3. **Cancellation Rules**
   - 7+ days before: 100% refund
   - 3-6 days before: 50% refund
   - Less than 3 days: No refund
   - Cannot cancel checked-out bookings

4. **Status Transitions**
   - CONFIRMED → CHECKED_IN → CHECKED_OUT
   - CONFIRMED → CANCELLED
   - Cannot check-in cancelled bookings

5. **Loyalty Points**
   - Automatic calculation based on total price
   - Persistent across bookings
   - Threshold-based benefits

## Hints for Implementation

1. **Date Overlap Detection**
   ```java
   boolean overlaps = !booking.checkOut.isBefore(newCheckIn) && 
                      !booking.checkIn.isAfter(newCheckOut);
   ```

2. **Thread Safety**
   - Use `synchronized` on booking creation
   - Use `ConcurrentHashMap` for shared collections
   - Atomic availability checks

3. **Pricing Calculation**
   - Iterate through each night for day-specific pricing
   - Apply multipliers based on day of week or season
   - Calculate loyalty discount on final total

4. **Observer Pattern**
   - Maintain list of observers in Booking
   - Notify on status changes
   - Decouple notification logic from booking logic

5. **Refund Calculation**
   - Use `ChronoUnit.DAYS.between()` for date difference
   - Apply refund percentage based on cancellation policy
   - Update payment status to REFUNDED

## Testing Checklist

- [ ] Create bookings with all pricing strategies
- [ ] Search available rooms by date and type
- [ ] Attempt double booking (should fail)
- [ ] Cancel booking and verify refund
- [ ] Check-in and check-out flow
- [ ] Verify loyalty points accumulation
- [ ] Test observer notifications
- [ ] Validate date edge cases
- [ ] Test concurrent bookings
- [ ] Verify booking history

## Expected Output

The system should demonstrate:
- Successful bookings with different pricing strategies
- Clear pricing breakdowns
- Availability search results
- Double booking prevention
- Cancellation with refund amounts
- Check-in/check-out confirmations
- Loyalty points tracking
- Real-time notifications
- System statistics

## Complexity Analysis

- **Booking Creation**: O(n) where n = existing bookings for room
- **Availability Search**: O(r × b) where r = rooms, b = bookings per room
- **Cancellation**: O(1)
- **Check-in/Check-out**: O(1)
- **Space Complexity**: O(r + g + b) where r = rooms, g = guests, b = bookings
