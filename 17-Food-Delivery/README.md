# Problem 17: Food Delivery System (Zomato)

## 🎯 Difficulty: Medium ⭐⭐⭐

## 📝 Problem Statement

Design a food delivery system like Zomato/Swiggy that allows customers to browse restaurants, place orders, track delivery status, and supports multiple delivery partners with different pricing strategies.

---

## 🔍 Functional Requirements (FR)

### FR1: Restaurant Management
- Add restaurants with menu items
- Each restaurant has: ID, name, location, rating, cuisine type
- Menu items have: ID, name, price, category, availability status
- Update menu item availability

### FR2: Customer Management
- Register customers with profile information
- Customers can search restaurants by cuisine, rating, location
- View restaurant menu and ratings
- Add items to cart

### FR3: Order Management
- Place orders from cart
- Order contains: customer, restaurant, items, total amount, delivery address
- Generate unique order ID
- Calculate order total with taxes and delivery charges

### FR4: Order State Management (State Pattern)
- **Order States:**
  - PLACED → Order confirmed
  - PREPARING → Restaurant is preparing food
  - READY_FOR_PICKUP → Food ready, waiting for delivery partner
  - OUT_FOR_DELIVERY → Delivery partner picked up
  - DELIVERED → Order delivered successfully
  - CANCELLED → Order cancelled

### FR5: Delivery Partner Management
- Multiple delivery partners available
- Assign delivery partner to order
- Track delivery partner location
- Delivery partner can accept/reject orders

### FR6: Pricing Strategy (Strategy Pattern)
- **Distance-based pricing:** Base fare + per km charge
- **Surge pricing:** Multiplier during peak hours
- **Flat rate pricing:** Fixed delivery charge
- Calculate delivery charges based on strategy

### FR7: Notifications (Observer Pattern)
- Notify customer on order state changes
- Notify restaurant on new orders
- Notify delivery partner on order assignment
- Multiple notification channels: SMS, Email, Push

### FR8: Payment Processing
- Support multiple payment methods: Cash, Card, UPI, Wallet
- Process payment on order placement
- Handle payment failures

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Scalability
- Handle 10,000+ restaurants
- Support 100,000+ concurrent orders
- Efficient restaurant search

### NFR2: Extensibility
- Easy to add new order states
- Easy to add new pricing strategies
- Easy to add new notification channels
- Easy to add new payment methods

### NFR3: Real-time Updates
- Real-time order tracking
- Live delivery partner location
- Instant notifications

### NFR4: Reliability
- Handle edge cases:
  - Restaurant unavailable
  - Menu item out of stock
  - No delivery partners available
  - Payment failures
  - Order cancellations

### NFR5: Performance
- Order placement: < 2 seconds
- Restaurant search: < 1 second
- Delivery partner assignment: < 5 seconds

---

## 🎨 Design Patterns to Use

### 1. **Factory Pattern**
- **Where:** Creating different types of users (Customer, Restaurant, DeliveryPartner)
- **Why:** Centralize user creation logic

### 2. **Strategy Pattern**
- **Where:** Delivery pricing strategies
- **Why:** Different pricing algorithms based on time, distance, demand

### 3. **State Pattern**
- **Where:** Order state management
- **Why:** Order behavior changes based on current state

### 4. **Observer Pattern**
- **Where:** Notification system
- **Why:** Multiple observers need to be notified of order updates

### 5. **Singleton Pattern** (Optional)
- **Where:** FoodDeliveryPlatform
- **Why:** Single platform instance managing all operations

---

## 📋 Core Entities

### 1. **Restaurant**
- Attributes: `restaurantId`, `name`, `location`, `cuisineType`, `rating`, `menu`
- Methods: `addMenuItem()`, `updateMenuItemAvailability()`, `getMenu()`

### 2. **MenuItem**
- Attributes: `itemId`, `name`, `price`, `category`, `isAvailable`
- Methods: `updatePrice()`, `setAvailability()`

### 3. **Customer**
- Attributes: `customerId`, `name`, `phone`, `email`, `addresses`, `cart`
- Methods: `addToCart()`, `removeFromCart()`, `placeOrder()`

### 4. **Order**
- Attributes: `orderId`, `customer`, `restaurant`, `items`, `totalAmount`, `state`, `deliveryPartner`
- Methods: `updateState()`, `assignDeliveryPartner()`, `calculateTotal()`

### 5. **DeliveryPartner**
- Attributes: `partnerId`, `name`, `phone`, `currentLocation`, `isAvailable`, `rating`
- Methods: `acceptOrder()`, `updateLocation()`, `completeDelivery()`

### 6. **OrderState** (Interface)
- Methods: `prepare()`, `readyForPickup()`, `outForDelivery()`, `deliver()`, `cancel()`
- Implementations: `PlacedState`, `PreparingState`, `ReadyForPickupState`, `OutForDeliveryState`, `DeliveredState`

### 7. **DeliveryPricingStrategy** (Interface)
- Methods: `calculateDeliveryCharge(distance, time)`
- Implementations: `DistanceBasedPricing`, `SurgePricing`, `FlatRatePricing`

### 8. **OrderObserver** (Interface)
- Methods: `onOrderUpdate(order)`
- Implementations: `CustomerNotifier`, `RestaurantNotifier`, `DeliveryPartnerNotifier`

---

## 🧪 Test Scenarios

### Scenario 1: Restaurant and Menu Setup
```
1. Add restaurants with menu items
2. Update menu item prices
3. Mark items as unavailable
4. Display restaurant menu
```

### Scenario 2: Customer Orders Food
```
1. Customer searches restaurants
2. Browses menu
3. Adds items to cart
4. Places order
5. Payment processed
6. Order confirmation
```

### Scenario 3: Order State Transitions (State Pattern)
```
1. Order PLACED → PREPARING
2. Restaurant prepares food → READY_FOR_PICKUP
3. Delivery partner assigned → OUT_FOR_DELIVERY
4. Delivery completed → DELIVERED
5. Track state changes
```

### Scenario 4: Delivery Pricing (Strategy Pattern)
```
1. Calculate with distance-based pricing
2. Calculate with surge pricing (peak hours)
3. Calculate with flat rate pricing
4. Compare different strategies
```

### Scenario 5: Notifications (Observer Pattern)
```
1. Customer notified on order placement
2. Restaurant notified of new order
3. Delivery partner notified of assignment
4. Customer notified on delivery
5. Multiple notification channels
```

### Scenario 6: Delivery Partner Assignment
```
1. Find available delivery partners
2. Assign nearest partner
3. Partner accepts order
4. Track delivery
5. Complete delivery
```

### Scenario 7: Edge Cases
```
1. Order cancellation
2. Restaurant unavailable
3. Menu item out of stock
4. No delivery partners available
5. Payment failure
6. Invalid delivery address
```

---

## ⏱️ Time Allocation (60-75 minutes)

- **5 mins:** Clarify requirements, ask questions
- **10 mins:** List entities and relationships
- **10 mins:** Identify design patterns and their usage
- **40 mins:** Write code (enums → model → state → strategy → observer → factory → service → main)
- **10 mins:** Test with demo, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: Order State Pattern</summary>

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
        order.notifyObservers();
    }
    
    public void cancel(Order order) {
        order.setState(new CancelledState());
        order.notifyObservers();
    }
}
```
</details>

<details>
<summary>Hint 2: Delivery Pricing Strategy</summary>

```java
public interface DeliveryPricingStrategy {
    double calculateDeliveryCharge(double distance, LocalTime orderTime);
}

public class DistanceBasedPricing implements DeliveryPricingStrategy {
    private static final double BASE_FARE = 20.0;
    private static final double PER_KM_CHARGE = 8.0;
    
    public double calculateDeliveryCharge(double distance, LocalTime orderTime) {
        return BASE_FARE + (distance * PER_KM_CHARGE);
    }
}
```
</details>

<details>
<summary>Hint 3: Observer Pattern for Notifications</summary>

```java
public interface OrderObserver {
    void onOrderUpdate(Order order);
}

public class CustomerNotifier implements OrderObserver {
    public void onOrderUpdate(Order order) {
        System.out.println("Customer notified: Order " + order.getOrderId() + 
                          " is now " + order.getCurrentState());
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
</details>

<details>
<summary>Hint 4: Restaurant Search</summary>

```java
public List<Restaurant> searchRestaurants(String cuisine, double minRating) {
    return restaurants.stream()
        .filter(r -> r.getCuisineType().equalsIgnoreCase(cuisine))
        .filter(r -> r.getRating() >= minRating)
        .collect(Collectors.toList());
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Add Rating System**
   - Customers rate restaurants and delivery partners
   - Calculate average ratings

2. **Add Offers and Discounts**
   - Promo codes
   - First-time user discounts
   - Restaurant-specific offers

3. **Add Multiple Addresses**
   - Customer can save multiple delivery addresses
   - Select address during checkout

4. **Add Order History**
   - View past orders
   - Reorder from history

5. **Add Real-time Tracking**
   - GPS-based delivery partner tracking
   - Estimated delivery time

6. **Add Restaurant Analytics**
   - Most ordered items
   - Revenue reports
   - Peak hours analysis

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Factory, Strategy, State, and Observer patterns correctly
- [ ] Handle all test scenarios
- [ ] Have clear separation: model, service, strategy, state, observer layers
- [ ] Implement complete order state machine
- [ ] Support multiple pricing strategies
- [ ] Notify all observers on state changes
- [ ] Handle edge cases (cancellations, unavailability, etc.)
- [ ] Be extensible (easy to add new states, strategies, observers)
- [ ] Have comprehensive demo with 7+ scenarios

---

## 📁 File Structure

```
17-Food-Delivery/
├── README.md (this file)
├── src/
│   ├── enums/
│   │   ├── OrderStatus.java
│   │   ├── CuisineType.java
│   │   ├── MenuCategory.java
│   │   └── PaymentMethod.java
│   ├── model/
│   │   ├── Restaurant.java
│   │   ├── MenuItem.java
│   │   ├── Customer.java
│   │   ├── Order.java
│   │   ├── DeliveryPartner.java
│   │   ├── Cart.java
│   │   └── Address.java
│   ├── state/
│   │   ├── OrderState.java
│   │   ├── PlacedState.java
│   │   ├── PreparingState.java
│   │   ├── ReadyForPickupState.java
│   │   ├── OutForDeliveryState.java
│   │   ├── DeliveredState.java
│   │   └── CancelledState.java
│   ├── strategy/
│   │   ├── DeliveryPricingStrategy.java
│   │   ├── DistanceBasedPricing.java
│   │   ├── SurgePricing.java
│   │   └── FlatRatePricing.java
│   ├── observer/
│   │   ├── OrderObserver.java
│   │   ├── CustomerNotifier.java
│   │   ├── RestaurantNotifier.java
│   │   └── DeliveryPartnerNotifier.java
│   ├── factory/
│   │   └── UserFactory.java
│   ├── service/
│   │   └── FoodDeliveryPlatform.java
│   └── Main.java
└── SOLUTION.md
```

---

**Good luck! Start coding! 🚀**

