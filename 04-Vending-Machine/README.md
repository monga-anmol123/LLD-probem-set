# Problem 4: Vending Machine

## 🎯 Difficulty: Medium ⭐⭐⭐

## 📝 Problem Statement

Design a vending machine system that manages product inventory, handles payments (coins and notes), dispenses products, returns change, and maintains different states throughout the transaction lifecycle.

---

## 🔍 Functional Requirements (FR)

### FR1: Product Management
- Store products with name, price, and quantity
- Track inventory levels
- Support multiple product types
- Check product availability

### FR2: Payment Handling
- Accept coins (1, 5, 10, 25 cents)
- Accept notes (1, 5, 10, 20 dollars)
- Track total amount inserted
- Return change if overpaid
- Refund money if transaction cancelled

### FR3: State Management
- **Idle State:** Waiting for user interaction
- **HasMoney State:** Money inserted, waiting for product selection
- **Dispensing State:** Product being dispensed
- Proper state transitions

### FR4: Product Dispensing
- Validate product selection
- Check sufficient payment
- Dispense product
- Update inventory
- Return change

### FR5: Change Management
- Calculate change amount
- Return change in optimal denominations
- Handle exact change scenarios
- Maintain coin/note inventory for change

### FR6: Error Handling
- Out of stock products
- Insufficient payment
- Invalid product selection
- No change available
- Refund on cancellation

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Performance
- Instant state transitions
- Quick change calculation
- O(1) product lookup

### NFR2: Reliability
- Maintain inventory consistency
- Accurate payment tracking
- Safe state transitions

### NFR3: Maintainability
- Clear state pattern implementation
- Easy to add new products
- Easy to add new payment types

### NFR4: Extensibility
- Support for new product categories
- Support for digital payments
- Support for promotions/discounts

---

## 🎨 Design Patterns to Use

### 1. **State Pattern**
- **Where:** Vending machine states (Idle, HasMoney, Dispensing)
- **Why:** Different behavior based on current state

### 2. **Factory Pattern**
- **Where:** Product creation
- **Why:** Centralized product instantiation

### 3. **Singleton Pattern**
- **Where:** VendingMachine instance
- **Why:** Single machine instance

---

## 📋 Core Entities

### 1. **Product**
- Attributes: `id`, `name`, `price`, `quantity`
- Methods: `isAvailable()`, `decrementQuantity()`

### 2. **Money**
- Attributes: `value`, `type` (COIN/NOTE)
- Denominations: Coins (1, 5, 10, 25¢), Notes ($1, $5, $10, $20)

### 3. **VendingMachineState** (Interface)
- Methods: `insertMoney()`, `selectProduct()`, `dispense()`, `refund()`

### 4. **IdleState, HasMoneyState, DispensingState**
- Concrete state implementations
- Handle state-specific behavior

### 5. **Inventory**
- Attributes: `products`, `changeInventory`
- Methods: `addProduct()`, `getProduct()`, `hasChange()`

### 6. **VendingMachine**
- Attributes: `currentState`, `inventory`, `currentAmount`
- Methods: `insertMoney()`, `selectProduct()`, `dispense()`, `refund()`

---

## 🧪 Test Scenarios

### Scenario 1: Successful Purchase
```
1. Insert $2
2. Select product (price $1.50)
3. Dispense product
4. Return change ($0.50)
5. Return to Idle state
```

### Scenario 2: Insufficient Payment
```
1. Insert $1
2. Select product (price $1.50)
3. Show "Insufficient payment" error
4. Remain in HasMoney state
5. Allow more money or refund
```

### Scenario 3: Out of Stock
```
1. Insert $2
2. Select out-of-stock product
3. Show "Out of stock" error
4. Remain in HasMoney state
5. Allow different selection or refund
```

### Scenario 4: Exact Change
```
1. Insert exact amount ($1.50)
2. Select product (price $1.50)
3. Dispense product
4. No change returned
5. Return to Idle state
```

### Scenario 5: Refund
```
1. Insert $5
2. Press refund button
3. Return all money
4. Return to Idle state
```

### Scenario 6: Multiple Items Purchase
```
1. Complete first purchase
2. Insert money for second item
3. Select and dispense
4. Verify inventory updated correctly
```

---

## ⏱️ Time Allocation (60 minutes)

- **10 mins:** Clarify requirements
- **10 mins:** Design entities and states
- **5 mins:** Identify patterns
- **30 mins:** Write code
- **5 mins:** Test and verify

---

## 💡 Hints

<details>
<summary>Hint 1: State Pattern Structure</summary>

```java
public interface VendingMachineState {
    void insertMoney(Money money);
    void selectProduct(String productId);
    void dispense();
    void refund();
}

public class IdleState implements VendingMachineState {
    public void insertMoney(Money money) {
        // Accept money, transition to HasMoneyState
    }
    public void selectProduct(String productId) {
        // Not allowed in Idle state
    }
}
```
</details>

<details>
<summary>Hint 2: Change Calculation</summary>

```java
public List<Money> calculateChange(double amount) {
    List<Money> change = new ArrayList<>();
    // Start with largest denomination
    // Use greedy algorithm
    // Return list of coins/notes
    return change;
}
```
</details>

<details>
<summary>Hint 3: State Transitions</summary>

```
Idle --[insertMoney]--> HasMoney
HasMoney --[selectProduct + sufficient]--> Dispensing
Dispensing --[dispense complete]--> Idle
HasMoney --[refund]--> Idle
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Digital Payments** - Credit card, mobile payment
2. **Promotions** - Buy 2 get 1 free
3. **Product Categories** - Drinks, Snacks, etc.
4. **Temperature Control** - For cold drinks
5. **Admin Panel** - Restock, collect money

---

## ✅ Success Criteria

- [ ] Compiles without errors
- [ ] State pattern implemented correctly
- [ ] Factory pattern for products
- [ ] All payment types handled
- [ ] Change calculation works
- [ ] Inventory management correct
- [ ] All 6 scenarios work
- [ ] Edge cases handled

---

**Good luck! 🚀**
