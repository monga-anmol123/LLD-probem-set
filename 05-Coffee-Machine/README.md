# Problem 05: Coffee Machine System

## 🎯 Difficulty: Easy ⭐⭐

## 📝 Problem Statement

Design a coffee machine system that can prepare various types of coffee beverages with different customizations. The system should support multiple coffee types (Espresso, Latte, Cappuccino, Americano), allow customization (milk type, sugar, size), handle ingredient inventory, and calculate prices based on selections.

## 🔍 Functional Requirements (FR)

### FR1: Coffee Preparation
- Support multiple coffee types: Espresso, Latte, Cappuccino, Americano
- Allow customization: milk type (whole, skim, almond, soy), sugar level (0-5), size (small, medium, large)
- Build coffee step-by-step with optional add-ons

### FR2: Ingredient Management
- Track ingredient inventory (coffee beans, milk, water, sugar)
- Check ingredient availability before preparing coffee
- Deduct ingredients after successful preparation
- Refill ingredients when needed

### FR3: Pricing System
- Calculate price based on coffee type, size, and add-ons
- Support different pricing strategies (base pricing, premium pricing)
- Apply discounts or promotions

### FR4: Order Processing
- Create orders with multiple coffee items
- Display order summary with itemized prices
- Calculate total bill

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Extensibility
- Easy to add new coffee types without modifying existing code
- Easy to add new pricing strategies
- Support for future add-ons (whipped cream, flavored syrups)

### NFR2: Maintainability
- Clear separation between coffee building logic and pricing logic
- Type-safe ingredient and coffee type management

### NFR3: Usability
- Clear error messages when ingredients are insufficient
- Easy-to-read order summaries

## 🎨 Design Patterns to Use

### 1. **Builder Pattern**
- **Where:** Coffee creation with multiple optional parameters
- **Why:** Coffee has many optional customizations (milk type, sugar, size, add-ons). Builder pattern provides a fluent interface for step-by-step construction without telescoping constructors.

### 2. **Strategy Pattern**
- **Where:** Pricing calculation
- **Why:** Different pricing strategies (base pricing, premium pricing, promotional pricing) can be switched at runtime. Open-Closed Principle - add new strategies without modifying existing code.

### 3. **Singleton Pattern** (Optional)
- **Where:** CoffeeMachine class
- **Why:** Only one coffee machine instance should exist in the system

## 📋 Core Entities

### 1. **Coffee**
- Attributes: type, size, milkType, sugarLevel, hasWhippedCream, hasExtraShot
- Methods: getDescription(), getIngredients()

### 2. **CoffeeType** (Enum)
- Values: ESPRESSO, LATTE, CAPPUCCINO, AMERICANO
- Attributes: baseCoffeeBeans, baseMilk, baseWater

### 3. **Size** (Enum)
- Values: SMALL, MEDIUM, LARGE
- Attributes: multiplier (for ingredients and price)

### 4. **MilkType** (Enum)
- Values: NONE, WHOLE_MILK, SKIM_MILK, ALMOND_MILK, SOY_MILK
- Attributes: additionalCost

### 5. **Inventory**
- Attributes: coffeeBeans, milk, water, sugar
- Methods: checkAvailability(), deductIngredients(), refill()

### 6. **PricingStrategy** (Interface)
- Methods: calculatePrice(Coffee coffee)
- Implementations: BasePricingStrategy, PremiumPricingStrategy

### 7. **CoffeeMachine**
- Attributes: inventory, pricingStrategy
- Methods: prepareCoffee(), displayMenu(), checkInventory()

## 🧪 Test Scenarios

### Scenario 1: Basic Coffee Preparation
```
1. Create Espresso (small, no milk, 1 sugar)
2. Verify ingredients deducted
3. Display coffee details
```

### Scenario 2: Complex Coffee with Builder
```
1. Build Large Latte with:
   - Almond milk
   - 2 sugars
   - Whipped cream
   - Extra shot
2. Calculate price
3. Prepare coffee
```

### Scenario 3: Multiple Coffee Types
```
1. Prepare Espresso
2. Prepare Cappuccino
3. Prepare Americano
4. Display all orders
```

### Scenario 4: Pricing Strategy Switch
```
1. Use Base Pricing Strategy
2. Prepare coffee and show price
3. Switch to Premium Pricing Strategy
4. Prepare same coffee and show price difference
```

### Scenario 5: Insufficient Ingredients
```
1. Deplete coffee beans inventory
2. Try to prepare coffee
3. Handle error gracefully
4. Refill inventory
5. Retry preparation
```

### Scenario 6: Order with Multiple Items
```
1. Create order with 3 different coffees
2. Calculate itemized prices
3. Display total bill
```

## ⏱️ Time Allocation (45 minutes)

- **5 mins:** Clarify requirements, identify entities
- **10 mins:** Design class structure (enums, models)
- **15 mins:** Implement Builder pattern for Coffee
- **10 mins:** Implement Strategy pattern for Pricing
- **5 mins:** Write Main.java with test scenarios

## 💡 Hints

<details>
<summary>Hint 1: Builder Pattern Structure</summary>

Use a static inner Builder class inside Coffee:
```java
public class Coffee {
    private final CoffeeType type;
    private final Size size;
    // ... other fields
    
    private Coffee(Builder builder) {
        this.type = builder.type;
        this.size = builder.size;
        // ...
    }
    
    public static class Builder {
        private CoffeeType type;
        private Size size = Size.MEDIUM; // default
        
        public Builder(CoffeeType type) {
            this.type = type;
        }
        
        public Builder size(Size size) {
            this.size = size;
            return this;
        }
        
        public Coffee build() {
            return new Coffee(this);
        }
    }
}
```
</details>

<details>
<summary>Hint 2: Enum with Attributes</summary>

Enums can have constructors and methods:
```java
public enum CoffeeType {
    ESPRESSO(18, 0, 30, 3.50),
    LATTE(18, 150, 30, 4.50);
    
    private final int coffeeBeans; // grams
    private final int milk; // ml
    private final int water; // ml
    private final double basePrice;
    
    CoffeeType(int coffeeBeans, int milk, int water, double basePrice) {
        this.coffeeBeans = coffeeBeans;
        // ...
    }
    
    public int getCoffeeBeans() { return coffeeBeans; }
}
```
</details>

<details>
<summary>Hint 3: Strategy Pattern Interface</summary>

Keep the interface simple:
```java
public interface PricingStrategy {
    double calculatePrice(Coffee coffee);
}

public class BasePricingStrategy implements PricingStrategy {
    public double calculatePrice(Coffee coffee) {
        double price = coffee.getType().getBasePrice();
        price *= coffee.getSize().getPriceMultiplier();
        price += coffee.getMilkType().getAdditionalCost();
        // ... add other customizations
        return price;
    }
}
```
</details>

<details>
<summary>Hint 4: Inventory Management</summary>

Calculate total ingredients needed before deducting:
```java
public boolean checkAvailability(Coffee coffee) {
    int beansNeeded = coffee.getType().getCoffeeBeans() * 
                      coffee.getSize().getIngredientMultiplier();
    return coffeeBeans >= beansNeeded && 
           milk >= milkNeeded && 
           water >= waterNeeded;
}
```
</details>

<details>
<summary>Hint 5: Fluent Builder Usage</summary>

Chain method calls for readability:
```java
Coffee latte = new Coffee.Builder(CoffeeType.LATTE)
    .size(Size.LARGE)
    .milkType(MilkType.ALMOND_MILK)
    .sugarLevel(2)
    .withWhippedCream()
    .withExtraShot()
    .build();
```
</details>

## ✅ Success Criteria

- [ ] Compiles without errors
- [ ] Uses Builder pattern correctly for Coffee creation
- [ ] Uses Strategy pattern correctly for pricing
- [ ] Handles all test scenarios
- [ ] Proper inventory management
- [ ] Clear error messages for insufficient ingredients
- [ ] Clean separation of concerns (model, service, strategy, builder)
- [ ] Demonstrates extensibility (easy to add new coffee types or pricing strategies)

## 🎓 Key Learning Points

1. **Builder Pattern:** Best for objects with many optional parameters
2. **Strategy Pattern:** Best for interchangeable algorithms
3. **Enum with Behavior:** Enums can have fields and methods
4. **Fluent Interface:** Method chaining improves readability
5. **Immutability:** Builder creates immutable objects (final fields)

## 📚 Related Problems

- **Problem 04:** Vending Machine (similar inventory management)
- **Problem 18:** Shopping Cart (similar pricing strategies)
- **Problem 11:** Restaurant Management (similar order processing)

---

**Time to implement! Start with enums, then models, then patterns! 🚀**


