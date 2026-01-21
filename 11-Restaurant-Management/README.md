# Problem 11: Restaurant Management System

## 🎯 Difficulty: Medium ⭐⭐⭐

## 📝 Problem Statement

Design a restaurant management system that handles table reservations, order management, billing, kitchen operations, and staff management. The system should support real-time order tracking, dynamic pricing strategies, and notifications for order status updates.

---

## 🔍 Functional Requirements (FR)

### FR1: Table Management
- Support multiple table types: Small (2 seats), Medium (4 seats), Large (6 seats), VIP (8 seats)
- Track table status: Available, Reserved, Occupied, Cleaning
- Table assignment based on party size
- Support table merging for large groups

### FR2: Reservation System
- Create reservations with date, time, party size
- Reservation status: Pending, Confirmed, Seated, Completed, Cancelled
- Automatic table assignment based on availability
- Notify customers when table is ready
- Handle walk-in customers

### FR3: Menu & Order Management
- Menu with categories: Appetizer, Main Course, Dessert, Beverage
- Each item has: name, price, preparation time, availability
- Create orders with multiple items
- Track order status: Placed, Preparing, Ready, Served, Paid
- Support order modifications (add/remove items)

### FR4: Kitchen Operations
- Orders sent to kitchen automatically
- Kitchen staff can update order status
- Track preparation time for each item
- Notify waiters when order is ready
- Priority queue for orders (VIP tables get priority)

### FR5: Billing System
- Calculate bill: items + tax + service charge
- Support different pricing strategies:
  - Regular pricing
  - Happy hour discounts (20% off beverages)
  - Weekend surcharge (10% extra)
- Split bill functionality
- Multiple payment methods: Cash, Card, Digital Wallet

### FR6: Staff Management
- Different roles: Manager, Waiter, Chef, Cashier
- Waiters assigned to specific tables
- Track waiter performance (orders served, tips earned)

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Scalability
- Support 50+ tables
- Handle 200+ orders per day
- Support 20+ staff members

### NFR2: Extensibility
- Easy to add new menu items
- Easy to add new table types
- Easy to add new pricing strategies
- Support for multiple restaurant branches

### NFR3: Real-time Updates
- Order status updates in real-time
- Table availability updates immediately
- Kitchen gets orders instantly

### NFR4: Reliability
- Handle edge cases:
  - No available tables
  - Out of stock menu items
  - Payment failures
  - Reservation conflicts

### NFR5: Performance
- Order placement: <2 seconds
- Bill generation: <1 second
- Table search: O(n) where n = number of tables

---

## 🎨 Design Patterns to Use

### 1. **Observer Pattern**
- **Where:** Order status notifications (Kitchen → Waiter → Customer)
- **Why:** Decouple order tracking from notification logic, multiple observers can watch order status

### 2. **Strategy Pattern**
- **Where:** Pricing strategies (Regular, Happy Hour, Weekend)
- **Why:** Switch pricing algorithms dynamically based on time/day

### 3. **State Pattern**
- **Where:** Order state transitions (Placed → Preparing → Ready → Served)
- **Why:** Clean state management with clear transitions

### 4. **Factory Pattern** (Optional)
- **Where:** Creating different types of staff members
- **Why:** Centralize staff creation logic

### 5. **Singleton Pattern**
- **Where:** Restaurant instance
- **Why:** Single restaurant system, global access

---

## 📋 Core Entities

### 1. **Table**
- Attributes: `tableId`, `tableType`, `capacity`, `status`, `currentReservation`
- Methods: `canAccommodate()`, `occupy()`, `release()`

### 2. **Reservation**
- Attributes: `reservationId`, `customerName`, `phone`, `partySize`, `dateTime`, `status`, `assignedTable`
- Methods: `confirm()`, `cancel()`, `seat()`

### 3. **MenuItem**
- Attributes: `itemId`, `name`, `category`, `price`, `preparationTime`, `available`
- Methods: `isAvailable()`, `updatePrice()`

### 4. **Order**
- Attributes: `orderId`, `table`, `items`, `status`, `waiter`, `orderTime`, `totalAmount`
- Methods: `addItem()`, `removeItem()`, `updateStatus()`, `calculateTotal()`
- Implements `Subject` for notifications

### 5. **Bill**
- Attributes: `billId`, `order`, `subtotal`, `tax`, `serviceCharge`, `discount`, `total`
- Methods: `calculateTotal()`, `applyDiscount()`, `split()`

### 6. **Staff** (Abstract)
- Attributes: `staffId`, `name`, `role`, `contactInfo`
- Subclasses: `Waiter`, `Chef`, `Manager`, `Cashier`
- Waiter implements `Observer` for order notifications

### 7. **Restaurant** (Singleton)
- Attributes: `name`, `tables`, `menu`, `activeOrders`, `reservations`, `pricingStrategy`
- Methods: `createReservation()`, `placeOrder()`, `generateBill()`

---

## 🧪 Test Scenarios

### Scenario 1: Basic Reservation & Order Flow
```
1. Create restaurant with 10 tables (mix of sizes)
2. Customer makes reservation for 4 people
3. Assign medium table
4. Customer arrives, table marked as OCCUPIED
5. Waiter takes order (2 appetizers, 4 main courses, 2 desserts)
6. Order sent to kitchen
7. Kitchen updates status: PREPARING → READY
8. Waiter serves food, marks order as SERVED
9. Generate bill with tax and service charge
10. Process payment
11. Table released, marked as CLEANING
```

### Scenario 2: Walk-in Customer (No Reservation)
```
1. Walk-in customer, party of 2
2. Find available small table
3. Assign table immediately
4. Place order and complete flow
```

### Scenario 3: No Available Tables
```
1. All tables occupied
2. Customer requests table for 4
3. System: "No tables available. Estimated wait: 30 mins"
4. Add to waiting queue
5. When table available, notify customer
```

### Scenario 4: Order Status Notifications (Observer Pattern)
```
1. Waiter places order
2. Kitchen receives notification: "New order #123"
3. Chef starts preparing, updates status to PREPARING
4. Waiter receives notification: "Order #123 is being prepared"
5. Chef completes, updates status to READY
6. Waiter receives notification: "Order #123 is ready for pickup"
7. Waiter serves, updates status to SERVED
```

### Scenario 5: Dynamic Pricing (Strategy Pattern)
```
1. Regular hours: Order total = $100
2. Switch to Happy Hour strategy (20% off beverages)
3. Same order total = $85 (beverages discounted)
4. Switch to Weekend strategy (10% surcharge)
5. Same order total = $110
```

### Scenario 6: Split Bill
```
1. Order total: $120
2. 3 customers want to split equally
3. Each pays: $40
4. Support partial splits (one pays $60, others split $60)
```

### Scenario 7: VIP Priority
```
1. Regular table orders at 12:00 PM
2. VIP table orders at 12:01 PM
3. Kitchen processes VIP order first
4. VIP order ready before regular order
```

---

## ⏱️ Time Allocation (60 minutes)

- **5 mins:** Clarify requirements, ask questions
- **5 mins:** Identify entities (Table, Reservation, Order, MenuItem, Bill)
- **5 mins:** Identify patterns (Observer, Strategy, State)
- **35 mins:** Code (enums → model → observer → strategy → state → service → main)
- **10 mins:** Test with demo, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: Observer Pattern for Order Tracking</summary>

```java
public interface Observer {
    void update(Order order, String message);
}

public class Waiter extends Staff implements Observer {
    @Override
    public void update(Order order, String message) {
        System.out.println("Waiter " + name + " notified: " + message);
    }
}

public class Order implements Subject {
    private List<Observer> observers = new ArrayList<>();
    
    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        notifyObservers("Order #" + orderId + " status: " + newStatus);
    }
}
```

</details>

<details>
<summary>Hint 2: Strategy Pattern for Pricing</summary>

```java
public interface PricingStrategy {
    double calculateTotal(List<MenuItem> items);
}

public class HappyHourStrategy implements PricingStrategy {
    public double calculateTotal(List<MenuItem> items) {
        double total = 0;
        for (MenuItem item : items) {
            if (item.getCategory() == MenuCategory.BEVERAGE) {
                total += item.getPrice() * 0.8; // 20% off
            } else {
                total += item.getPrice();
            }
        }
        return total;
    }
}
```

</details>

<details>
<summary>Hint 3: State Pattern for Order States</summary>

```java
public interface OrderState {
    void next(Order order);
    void prev(Order order);
    void printStatus();
}

public class PlacedState implements OrderState {
    public void next(Order order) {
        order.setState(new PreparingState());
    }
}

public class Order {
    private OrderState state;
    
    public void nextState() {
        state.next(this);
    }
}
```

</details>

<details>
<summary>Hint 4: Table Assignment Logic</summary>

```java
public Table findSuitableTable(int partySize) {
    // Try exact match first
    for (Table table : tables) {
        if (table.getCapacity() == partySize && table.isAvailable()) {
            return table;
        }
    }
    
    // Find smallest table that fits
    Table bestFit = null;
    for (Table table : tables) {
        if (table.getCapacity() >= partySize && table.isAvailable()) {
            if (bestFit == null || table.getCapacity() < bestFit.getCapacity()) {
                bestFit = table;
            }
        }
    }
    return bestFit;
}
```

</details>

---

## 🚀 Extensions (If Time Permits)

1. **Add Loyalty Program**
   - Track customer visits
   - Offer discounts based on frequency
   - Points system

2. **Add Delivery System**
   - Online orders
   - Delivery tracking
   - Delivery fee calculation

3. **Add Inventory Management**
   - Track ingredient stock
   - Alert when items running low
   - Auto-disable menu items when out of stock

4. **Add Analytics Dashboard**
   - Most popular dishes
   - Peak hours
   - Revenue reports
   - Waiter performance metrics

5. **Add Multi-branch Support**
   - Multiple restaurant locations
   - Centralized menu management
   - Branch-specific pricing

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Observer, Strategy, State patterns correctly
- [ ] Handle all test scenarios
- [ ] Support reservations and walk-ins
- [ ] Track order status with notifications
- [ ] Calculate bills with different pricing strategies
- [ ] Handle edge cases (no tables, out of stock, etc.)
- [ ] Have clear separation: model, service, strategy, observer layers
- [ ] Be extensible (easy to add menu items, table types, pricing strategies)

---

## 📁 File Structure

```
11-Restaurant-Management/
├── README.md (this file)
├── src/
│   ├── enums/
│   │   ├── TableType.java
│   │   ├── TableStatus.java
│   │   ├── ReservationStatus.java
│   │   ├── OrderStatus.java
│   │   ├── MenuCategory.java
│   │   └── StaffRole.java
│   ├── model/
│   │   ├── Table.java
│   │   ├── Reservation.java
│   │   ├── MenuItem.java
│   │   ├── Order.java
│   │   ├── Bill.java
│   │   ├── Staff.java
│   │   ├── Waiter.java
│   │   ├── Chef.java
│   │   └── Customer.java
│   ├── observer/
│   │   ├── Observer.java
│   │   └── Subject.java
│   ├── strategy/
│   │   ├── PricingStrategy.java
│   │   ├── RegularPricingStrategy.java
│   │   ├── HappyHourPricingStrategy.java
│   │   └── WeekendPricingStrategy.java
│   ├── state/
│   │   ├── OrderState.java
│   │   ├── PlacedState.java
│   │   ├── PreparingState.java
│   │   ├── ReadyState.java
│   │   └── ServedState.java
│   ├── service/
│   │   ├── Restaurant.java
│   │   └── Kitchen.java
│   └── Main.java
└── SOLUTION.md
```

---

**Good luck! This combines Observer, Strategy, and State patterns - focus on clean separation of concerns! 🚀**


