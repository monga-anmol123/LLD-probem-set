# Solution: Vending Machine

## ✅ Complete Implementation

Production-quality vending machine with State and Factory patterns, handling payments, inventory, and change calculation.

---

## 🎨 Design Patterns

### 1. **State Pattern** (Machine States)

**Implementation:**
```java
public interface VendingMachineState {
    void insertMoney(Money money);
    void selectProduct(String productId);
    void dispense();
    void refund();
}

// Concrete states:
- IdleState: Waiting for money
- HasMoneyState: Money inserted, waiting for selection
- DispensingState: Dispensing product
```

**State Transitions:**
- Idle → HasMoney (insert money)
- HasMoney → Dispensing (select valid product)
- Dispensing → Idle (after dispensing)
- HasMoney → Idle (refund)

**Benefits:**
- ✅ Clean separation of state-specific behavior
- ✅ Easy to add new states
- ✅ Prevents invalid operations in wrong state

---

### 2. **Factory Pattern** (Product Creation)

**Implementation:**
```java
public class ProductFactory {
    public static Product createBeverage(String id, String name, double price, int quantity);
    public static Product createSnack(String id, String name, double price, int quantity);
    public static Product createCandy(String id, String name, double price, int quantity);
}
```

**Benefits:**
- ✅ Centralized product creation
- ✅ Easy to add new product types
- ✅ Consistent product initialization

---

### 3. **Singleton Pattern** (VendingMachine)

**Implementation:**
```java
public class VendingMachine {
    private static VendingMachine instance;
    
    private VendingMachine() {}
    
    public static synchronized VendingMachine getInstance() {
        if (instance == null) {
            instance = new VendingMachine();
        }
        return instance;
    }
}
```

**Benefits:**
- ✅ Single machine instance
- ✅ Global access point
- ✅ Consistent state management

---

## 🔑 Key Algorithms

### 1. **Change Calculation (Greedy)**

```java
private List<Money> calculateChange(double amount) {
    List<Money> change = new ArrayList<>();
    Money[] denominations = {$20, $10, $5, $1, 25¢, 10¢, 5¢, 1¢};
    
    for (Money denom : denominations) {
        while (remaining >= denom.getValue() && hasChange(denom)) {
            change.add(denom);
            remaining -= denom.getValue();
        }
    }
    return change;
}
```

**Time Complexity:** O(n) where n = number of denominations  
**Space Complexity:** O(m) where m = number of coins/notes returned

---

### 2. **Product Selection Validation**

```java
public void selectProduct(String productId) {
    Product product = inventory.getProduct(productId);
    
    // Validation checks:
    if (product == null) return "Invalid product";
    if (!product.isAvailable()) return "Out of stock";
    if (currentAmount < product.getPrice()) return "Insufficient payment";
    
    // Proceed to dispensing
    setState(dispensingState);
    dispense();
}
```

---

## 📊 Complexity Analysis

| Operation | Time | Space |
|-----------|------|-------|
| insertMoney() | O(1) | O(1) |
| selectProduct() | O(1) | O(1) |
| dispense() | O(1) | O(1) |
| calculateChange() | O(n) | O(m) |
| refund() | O(1) | O(1) |

---

## 🧪 Test Scenarios

### ✅ Scenario 1: Successful Purchase
- Insert $2, select $1.50 item
- **Result:** Product dispensed, $0.50 change returned

### ✅ Scenario 2: Insufficient Payment
- Insert $1, try to buy $2 item
- **Result:** Error message, can add more money

### ✅ Scenario 3: Out of Stock
- Try to buy item with 0 quantity
- **Result:** Error message, can select different item

### ✅ Scenario 4: Exact Change
- Insert exact amount
- **Result:** Product dispensed, no change

### ✅ Scenario 5: Refund
- Insert money, request refund
- **Result:** All money returned, back to Idle

### ✅ Scenario 6: Multiple Purchases
- Complete multiple transactions
- **Result:** Inventory updated correctly

---

## 🎯 Interview Discussion Points

### 1. **Why State Pattern?**
"State pattern allows the vending machine to behave differently based on its current state. For example, in Idle state, we can't select products. In HasMoney state, we can select products or refund. This prevents invalid operations and makes the code more maintainable."

### 2. **How to handle concurrent access?**
```java
public synchronized void insertMoney(Money money) {
    // Thread-safe money insertion
}
```

### 3. **What if change not available?**
"Check change inventory before accepting payment. If insufficient change, reject large bills or display 'Exact change only' message."

### 4. **How to add digital payments?**
"Create new Money subclasses (CreditCard, MobilePay) or use Strategy pattern for payment processing."

---

## ✅ Success Criteria Met

- ✅ Compiles without errors
- ✅ State pattern implemented
- ✅ Factory pattern for products
- ✅ Singleton for machine
- ✅ Change calculation works
- ✅ All 6 scenarios pass
- ✅ Edge cases handled

---

**Time to implement:** 60 minutes  
**Difficulty:** Medium ⭐⭐⭐  
**Patterns:** State, Factory, Singleton
