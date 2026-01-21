# Problem 18: E-Commerce Shopping Cart System

## 🎯 Difficulty: Medium ⭐⭐⭐

## 📝 Problem Statement

Design a comprehensive e-commerce shopping cart system that supports adding/removing items, quantity management, multiple pricing strategies, discount types (percentage, fixed, BOGO, category-based), coupon validation, tax calculation, and checkout with detailed order summaries. The system should handle complex pricing scenarios with multiple discounts, coupons, and taxes applied correctly.

## 🔍 Functional Requirements (FR)

### FR1: Product Management
- Product catalog with categories
- Product attributes (name, price, category, SKU, stock)
- Inventory tracking
- Stock validation before adding to cart

### FR2: Shopping Cart Operations
- Add items to cart with quantity
- Remove items from cart
- Update item quantities
- Clear entire cart
- View cart contents with prices

### FR3: Pricing & Discounts (Strategy Pattern)
- **Percentage Discount**: X% off total or specific items
- **Fixed Amount Discount**: $X off total
- **BOGO (Buy One Get One)**: Buy X get Y free
- **Category Discount**: Discount on specific categories
- **Bulk Discount**: Discount when buying X+ items
- Multiple discounts can be applied with priority

### FR4: Coupon System
- Coupon codes with validation
- Minimum order amount requirement
- Expiry date validation
- Usage limit (single-use, multi-use)
- Coupon types: percentage, fixed amount
- Apply/remove coupons

### FR5: Tax Calculation (Decorator Pattern)
- Sales tax calculation based on location
- Tax-exempt categories (e.g., groceries)
- Multiple tax rates (state, local)
- Tax applied after discounts

### FR6: Checkout & Order Summary
- Calculate subtotal (before discounts)
- Apply discounts and coupons
- Calculate tax
- Calculate final total
- Detailed breakdown showing all calculations
- Generate order confirmation

### FR7: Notifications (Observer Pattern)
- Notify when items added/removed
- Notify when price changes
- Notify when discounts applied
- Notify on low stock warnings

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Accuracy
- Precise decimal calculations (use proper rounding)
- Correct order of operations (discounts → coupons → tax)
- No negative prices

### NFR2: Extensibility
- Easy to add new discount types
- Easy to add new tax rules
- Support for future payment methods

### NFR3: Performance
- Fast cart operations (O(1) for add/remove)
- Efficient price calculations
- Handle large carts (100+ items)

### NFR4: Data Integrity
- Prevent overselling (stock validation)
- Atomic checkout operations
- Consistent cart state

## 🎨 Design Patterns to Use

### 1. **Strategy Pattern**
- **Where:** Discount calculation strategies
- **Why:** Multiple discount algorithms that can be selected at runtime. Easy to add new discount types without modifying existing code.

### 2. **Decorator Pattern**
- **Where:** Tax calculation wrapping the cart total
- **Why:** Dynamically add tax calculations to the price. Can stack multiple tax decorators (state tax, local tax, etc.).

### 3. **Observer Pattern**
- **Where:** Cart notifications for price changes, stock updates
- **Why:** Notify multiple observers (UI, analytics, inventory) when cart state changes. Loose coupling between cart and notification handlers.

## 📋 Core Entities

### 1. **Product**
- Attributes: sku, name, price, category, stock, description
- Methods: updateStock(), isAvailable()

### 2. **CartItem**
- Attributes: product, quantity
- Methods: getSubtotal(), updateQuantity()

### 3. **ShoppingCart**
- Attributes: items, discountStrategy, appliedCoupons
- Methods: addItem(), removeItem(), updateQuantity(), calculateTotal()

### 4. **DiscountStrategy** (Interface)
- Methods: calculateDiscount(cart)
- Implementations: PercentageDiscount, FixedDiscount, BOGODiscount, CategoryDiscount

### 5. **Coupon**
- Attributes: code, discountType, discountValue, minOrderAmount, expiryDate, usageLimit
- Methods: isValid(), apply()

### 6. **TaxDecorator** (Decorator Pattern)
- Wraps cart total and adds tax
- Methods: calculateWithTax()

### 7. **Order**
- Attributes: orderId, items, subtotal, discounts, tax, total
- Methods: generateSummary()

## 🧪 Test Scenarios

### Scenario 1: Basic Cart Operations
```
1. Add 2 items to cart
2. Update quantity of item 1
3. Remove item 2
4. Display cart
```

### Scenario 2: Percentage Discount
```
1. Add items worth $100
2. Apply 20% discount
3. Total should be $80
4. Display breakdown
```

### Scenario 3: BOGO Discount
```
1. Add 3 items of same product
2. Apply "Buy 2 Get 1 Free" discount
3. Should pay for only 2 items
4. Display savings
```

### Scenario 4: Coupon Validation
```
1. Try to apply expired coupon → Rejected
2. Try to apply coupon below min order → Rejected
3. Apply valid coupon → Accepted
4. Try to reuse single-use coupon → Rejected
```

### Scenario 5: Category Discount
```
1. Add electronics and groceries
2. Apply 15% discount on electronics only
3. Groceries at full price
4. Display category-wise breakdown
```

### Scenario 6: Multiple Discounts + Coupon + Tax
```
1. Add items worth $200
2. Apply 10% store-wide discount → $180
3. Apply $20 coupon → $160
4. Apply 8% tax → $172.80
5. Display detailed breakdown
```

### Scenario 7: Stock Validation
```
1. Try to add 100 items (only 10 in stock) → Error
2. Add 5 items → Success
3. Try to add 10 more → Error (insufficient stock)
```

### Scenario 8: Complex Pricing
```
1. Multiple items with different discounts
2. Category-specific discounts
3. BOGO on some items
4. Coupon applied
5. Tax calculation
6. Final order summary
```

## ⏱️ Time Allocation (60 minutes)

- **5 mins:** Clarify requirements, identify entities
- **10 mins:** Design class structure (enums, models)
- **15 mins:** Implement Strategy pattern for discounts
- **10 mins:** Implement Decorator pattern for tax
- **10 mins:** Implement Observer pattern for notifications
- **10 mins:** Write Main.java with comprehensive scenarios

## 💡 Hints

<details>
<summary>Hint 1: Strategy Pattern for Discounts</summary>

```java
public interface DiscountStrategy {
    double calculateDiscount(ShoppingCart cart);
    String getDescription();
}

public class PercentageDiscount implements DiscountStrategy {
    private double percentage;
    
    public double calculateDiscount(ShoppingCart cart) {
        return cart.getSubtotal() * (percentage / 100.0);
    }
}

public class BOGODiscount implements DiscountStrategy {
    private String targetSKU;
    private int buy, getFree;
    
    public double calculateDiscount(ShoppingCart cart) {
        int quantity = cart.getQuantityForProduct(targetSKU);
        int freeItems = (quantity / (buy + getFree)) * getFree;
        return freeItems * cart.getProductPrice(targetSKU);
    }
}
```
</details>

<details>
<summary>Hint 2: Decorator Pattern for Tax</summary>

```java
public interface PriceCalculator {
    double calculateTotal();
}

public class CartPriceCalculator implements PriceCalculator {
    private ShoppingCart cart;
    
    public double calculateTotal() {
        return cart.getSubtotal() - cart.getTotalDiscounts();
    }
}

public class TaxDecorator implements PriceCalculator {
    private PriceCalculator calculator;
    private double taxRate;
    
    public double calculateTotal() {
        double baseTotal = calculator.calculateTotal();
        return baseTotal + (baseTotal * taxRate);
    }
}
```
</details>

<details>
<summary>Hint 3: Observer Pattern for Notifications</summary>

```java
public interface CartObserver {
    void onItemAdded(Product product, int quantity);
    void onItemRemoved(Product product);
    void onPriceChanged(double oldTotal, double newTotal);
}

public class ShoppingCart extends Subject {
    public void addItem(Product product, int quantity) {
        // Add to cart
        notifyItemAdded(product, quantity);
    }
}
```
</details>

<details>
<summary>Hint 4: Coupon Validation</summary>

```java
public class Coupon {
    public boolean isValid(ShoppingCart cart) {
        if (isExpired()) return false;
        if (cart.getSubtotal() < minOrderAmount) return false;
        if (usageCount >= usageLimit) return false;
        return true;
    }
}
```
</details>

<details>
<summary>Hint 5: Order of Operations</summary>

```
1. Calculate Subtotal (sum of all items)
2. Apply Store Discounts (percentage, BOGO, etc.)
3. Apply Coupons
4. Calculate Tax (on discounted amount)
5. Final Total
```
</details>

## ✅ Success Criteria

- [ ] Compiles without errors
- [ ] Uses Strategy pattern correctly for discounts
- [ ] Uses Decorator pattern correctly for tax
- [ ] Uses Observer pattern correctly for notifications
- [ ] Handles all test scenarios
- [ ] Accurate price calculations (proper rounding)
- [ ] Coupon validation works correctly
- [ ] Stock validation prevents overselling
- [ ] Clear order breakdown showing all calculations
- [ ] Edge cases handled (empty cart, invalid coupons, etc.)

## 🎓 Key Learning Points

1. **Strategy Pattern:** Best for interchangeable algorithms (discount types)
2. **Decorator Pattern:** Best for adding responsibilities dynamically (tax layers)
3. **Observer Pattern:** Best for event notifications (cart changes)
4. **Combining Patterns:** Real systems use multiple patterns together
5. **Financial Calculations:** Use proper rounding and order of operations
6. **Validation:** Always validate before operations (stock, coupons)
7. **Immutability:** Consider immutable cart items for thread safety

## 📚 Related Problems

- **Problem 05:** Coffee Machine (similar Builder pattern for customization)
- **Problem 16:** Ride Sharing (similar Strategy pattern for pricing)
- **Problem 22:** Stock Trading (similar Observer pattern for updates)

---

**Time to implement! This showcases e-commerce pricing complexity! 🛒💰**
