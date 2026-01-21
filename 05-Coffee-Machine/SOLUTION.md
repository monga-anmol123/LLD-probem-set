# Solution: Coffee Machine System

## ✅ Complete Implementation

This folder contains a fully working coffee machine system demonstrating **Builder** and **Strategy** design patterns with proper inventory management and pricing strategies.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                         Main.java                            │
│                    (Demo/Entry Point)                        │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
        ▼               ▼               ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐
│   Builder    │ │   Service   │ │  Strategy  │
│              │ │             │ │            │
│ Coffee       │ │ Coffee      │ │ Pricing    │
│ Builder      │ │ Machine     │ │ Strategy   │
│              │ │ (Singleton) │ │            │
└──────────────┘ └─────────────┘ └────────────┘
        │               │               │
        └───────────────┼───────────────┘
                        │
                        ▼
                ┌─────────────┐
                │    Model    │
                │             │
                │  Coffee     │
                │  Inventory  │
                │  Order      │
                └─────────────┘
```

---

## 📦 Package Structure

```
src/
├── enums/                      # Type-safe enumerations
│   ├── CoffeeType.java        # ESPRESSO, LATTE, CAPPUCCINO, AMERICANO
│   ├── Size.java              # SMALL, MEDIUM, LARGE
│   └── MilkType.java          # NONE, WHOLE_MILK, ALMOND_MILK, etc.
│
├── model/                      # Domain entities
│   ├── Coffee.java            # Coffee with Builder pattern
│   ├── Inventory.java         # Ingredient management
│   └── Order.java             # Order with multiple items
│
├── strategy/                   # Strategy Pattern
│   ├── PricingStrategy.java           # Interface
│   ├── BasePricingStrategy.java       # Standard pricing
│   ├── PremiumPricingStrategy.java    # 25% markup
│   └── DiscountPricingStrategy.java   # Discount pricing
│
├── service/                    # Business logic
│   └── CoffeeMachine.java     # Main service (Singleton)
│
└── Main.java                   # Demo application
```

---

## 🎨 Design Patterns Explained

### 1. **Builder Pattern** (Coffee.Builder)

**Purpose:** Create complex Coffee objects with many optional parameters without telescoping constructors.

**Problem Solved:**
```java
// ❌ BAD: Telescoping constructors
public Coffee(CoffeeType type) { ... }
public Coffee(CoffeeType type, Size size) { ... }
public Coffee(CoffeeType type, Size size, MilkType milk) { ... }
public Coffee(CoffeeType type, Size size, MilkType milk, int sugar) { ... }
// ... 20+ constructors!

// ❌ BAD: Too many parameters
Coffee coffee = new Coffee(CoffeeType.LATTE, Size.LARGE, MilkType.ALMOND_MILK, 
                          2, true, true); // What do true/true mean?
```

**✅ SOLUTION: Builder Pattern**
```java
Coffee latte = new Coffee.Builder(CoffeeType.LATTE)
    .size(Size.LARGE)
    .milkType(MilkType.ALMOND_MILK)
    .sugarLevel(2)
    .withWhippedCream()
    .withExtraShot()
    .build();
```

**Implementation Details:**

```java
public class Coffee {
    // Immutable fields (final)
    private final CoffeeType type;
    private final Size size;
    private final MilkType milkType;
    // ...
    
    // Private constructor - only accessible via Builder
    private Coffee(Builder builder) {
        this.type = builder.type;
        this.size = builder.size;
        // ...
    }
    
    // Static inner Builder class
    public static class Builder {
        // Required parameter
        private final CoffeeType type;
        
        // Optional parameters with defaults
        private Size size = Size.MEDIUM;
        private MilkType milkType = MilkType.NONE;
        private int sugarLevel = 0;
        private boolean hasWhippedCream = false;
        private boolean hasExtraShot = false;
        
        public Builder(CoffeeType type) {
            this.type = type; // Required
        }
        
        // Fluent setters return 'this'
        public Builder size(Size size) {
            this.size = size;
            return this; // Enable chaining
        }
        
        public Builder milkType(MilkType milkType) {
            this.milkType = milkType;
            return this;
        }
        
        public Builder sugarLevel(int sugarLevel) {
            if (sugarLevel < 0 || sugarLevel > 5) {
                throw new IllegalArgumentException("Sugar level must be 0-5");
            }
            this.sugarLevel = sugarLevel;
            return this;
        }
        
        public Builder withWhippedCream() {
            this.hasWhippedCream = true;
            return this;
        }
        
        public Builder withExtraShot() {
            this.hasExtraShot = true;
            return this;
        }
        
        // Build method with validation
        public Coffee build() {
            // Validate: Can't have whipped cream without milk
            if (hasWhippedCream && type.getMilk() == 0 && milkType == MilkType.NONE) {
                throw new IllegalStateException(
                    "Cannot add whipped cream to coffee without milk");
            }
            
            return new Coffee(this);
        }
    }
}
```

**Benefits:**
- ✅ **Readable:** Self-documenting code (`.withWhippedCream()` vs `true`)
- ✅ **Flexible:** Optional parameters with sensible defaults
- ✅ **Immutable:** Coffee object is final and thread-safe
- ✅ **Validation:** Validate in `build()` before creating object
- ✅ **Fluent:** Method chaining improves readability

---

### 2. **Strategy Pattern** (PricingStrategy)

**Purpose:** Define a family of pricing algorithms and make them interchangeable at runtime.

**Problem Solved:**
```java
// ❌ BAD: Hard-coded pricing logic
public double calculatePrice(Coffee coffee) {
    if (isPremiumHour()) {
        return coffee.getBasePrice() * 1.25;
    } else if (isHappyHour()) {
        return coffee.getBasePrice() * 0.80;
    } else {
        return coffee.getBasePrice();
    }
    // Adding new pricing requires modifying this method!
}
```

**✅ SOLUTION: Strategy Pattern**
```java
// Interface
public interface PricingStrategy {
    double calculatePrice(Coffee coffee);
}

// Concrete strategies
public class BasePricingStrategy implements PricingStrategy {
    public double calculatePrice(Coffee coffee) {
        double price = coffee.getType().getBasePrice();
        price *= coffee.getSize().getPriceMultiplier();
        price += coffee.getMilkType().getAdditionalCost();
        if (coffee.hasWhippedCream()) price += 0.75;
        if (coffee.hasExtraShot()) price += 1.00;
        return price;
    }
}

public class PremiumPricingStrategy implements PricingStrategy {
    public double calculatePrice(Coffee coffee) {
        double price = coffee.getType().getBasePrice();
        price *= coffee.getSize().getPriceMultiplier();
        price += coffee.getMilkType().getAdditionalCost() * 1.5; // Higher milk cost
        if (coffee.hasWhippedCream()) price += 1.00; // More expensive
        if (coffee.hasExtraShot()) price += 1.50;
        return price * 1.25; // 25% markup
    }
}

public class DiscountPricingStrategy implements PricingStrategy {
    private final double discountPercentage;
    
    public double calculatePrice(Coffee coffee) {
        PricingStrategy base = new BasePricingStrategy();
        return base.calculatePrice(coffee) * (1.0 - discountPercentage);
    }
}
```

**Usage:**
```java
CoffeeMachine machine = CoffeeMachine.getInstance(...);

// Switch strategies at runtime
machine.setPricingStrategy(new BasePricingStrategy());
double price1 = machine.prepareCoffee(coffee); // $4.50

machine.setPricingStrategy(new PremiumPricingStrategy());
double price2 = machine.prepareCoffee(coffee); // $5.63 (25% more)

machine.setPricingStrategy(new DiscountPricingStrategy(0.20));
double price3 = machine.prepareCoffee(coffee); // $3.60 (20% off)
```

**Benefits:**
- ✅ **Open-Closed Principle:** Add new strategies without modifying existing code
- ✅ **Runtime Switching:** Change pricing dynamically (happy hour, promotions)
- ✅ **Testable:** Test each strategy independently
- ✅ **Composable:** DiscountPricingStrategy wraps BasePricingStrategy

---

### 3. **Singleton Pattern** (CoffeeMachine)

**Purpose:** Ensure only one coffee machine instance exists.

**Implementation:**
```java
public class CoffeeMachine {
    private static CoffeeMachine instance;
    
    private CoffeeMachine(String name, Inventory inventory) {
        // Private constructor
    }
    
    public static synchronized CoffeeMachine getInstance(
            String name, Inventory inventory) {
        if (instance == null) {
            instance = new CoffeeMachine(name, inventory);
        }
        return instance;
    }
}
```

**Benefits:**
- ✅ Global access point
- ✅ Controlled instantiation
- ✅ Thread-safe (synchronized)

---

## 🔑 Key Design Decisions

### 1. **Enums with Behavior**

Instead of plain enums, we give them attributes and methods:

```java
public enum CoffeeType {
    ESPRESSO(18, 0, 30, 3.50, "Strong and concentrated"),
    LATTE(18, 150, 30, 4.50, "Espresso with steamed milk");
    
    private final int coffeeBeans;
    private final int milk;
    private final int water;
    private final double basePrice;
    private final String description;
    
    CoffeeType(int coffeeBeans, int milk, int water, 
               double basePrice, String description) {
        this.coffeeBeans = coffeeBeans;
        this.milk = milk;
        this.water = water;
        this.basePrice = basePrice;
        this.description = description;
    }
    
    public int getCoffeeBeans() { return coffeeBeans; }
    public double getBasePrice() { return basePrice; }
}
```

**Benefits:**
- Type-safe
- Each coffee type knows its own recipe
- Easy to add new types (just add enum value)

---

### 2. **Immutable Coffee Objects**

Coffee fields are `final` and set only via Builder:

```java
public class Coffee {
    private final CoffeeType type;
    private final Size size;
    // No setters!
}
```

**Benefits:**
- Thread-safe
- Can't be modified after creation
- Prevents bugs from accidental changes

---

### 3. **Inventory Management**

```java
public class Inventory {
    public boolean checkAvailability(Coffee coffee) {
        return coffeeBeans >= coffee.getCoffeeBeansNeeded() &&
               milk >= coffee.getMilkNeeded() &&
               water >= coffee.getWaterNeeded() &&
               sugar >= coffee.getSugarNeeded();
    }
    
    public void deductIngredients(Coffee coffee) {
        if (!checkAvailability(coffee)) {
            throw new IllegalStateException("Insufficient ingredients");
        }
        coffeeBeans -= coffee.getCoffeeBeansNeeded();
        milk -= coffee.getMilkNeeded();
        water -= coffee.getWaterNeeded();
        sugar -= coffee.getSugarNeeded();
    }
}
```

**Benefits:**
- Check before deduct (atomic operation)
- Clear error messages
- Prevents negative inventory

---

## ⚖️ Trade-offs

### **1. Builder Pattern**

**✅ Pros:**
- Readable, fluent interface
- Immutable objects
- Validation before creation
- No telescoping constructors

**❌ Cons:**
- More code (Builder class)
- Slightly more verbose for simple objects
- Extra object allocation (Builder instance)

**When to Use:**
- Objects with 4+ optional parameters
- Complex validation rules
- Need immutability

**When NOT to Use:**
- Simple objects with 1-2 fields
- All parameters are required

---

### **2. Strategy Pattern**

**✅ Pros:**
- Easy to add new strategies
- Runtime switching
- Testable independently
- Open-Closed Principle

**❌ Cons:**
- More classes (one per strategy)
- Client must know about strategies
- Slight performance overhead (interface call)

**When to Use:**
- Multiple algorithms for same task
- Need to switch at runtime
- Algorithms change frequently

**When NOT to Use:**
- Only one algorithm
- Algorithm never changes

---

### **3. Singleton Pattern**

**✅ Pros:**
- Global access
- One instance guaranteed
- Lazy initialization

**❌ Cons:**
- Hard to unit test (global state)
- Thread safety overhead (synchronized)
- Tight coupling (clients depend on Singleton)

**Better Alternative:**
Dependency Injection (Spring, Guice)

```java
// Instead of Singleton
@Service
public class CoffeeMachine {
    // Spring manages lifecycle
}

// Inject where needed
@Autowired
private CoffeeMachine machine;
```

---

## 🧪 Test Coverage

### **Scenarios Tested in Main.java:**

1. ✅ **Simple Coffee** - Basic espresso with builder
2. ✅ **Complex Coffee** - All customizations (milk, sugar, whipped cream, extra shot)
3. ✅ **Multiple Coffee Types** - All 4 types (Espresso, Latte, Cappuccino, Americano)
4. ✅ **Pricing Strategies** - Base, Premium, Discount
5. ✅ **Order Management** - Multiple items with total
6. ✅ **Inventory Tracking** - Deduction after each coffee
7. ✅ **Insufficient Ingredients** - Error handling
8. ✅ **Builder Validation** - Invalid combinations (whipped cream on espresso)
9. ✅ **All Coffee Types Showcase** - One of each type

---

## 🚀 How to Compile and Run

### **Option 1: Command Line**
```bash
cd 05-Coffee-Machine/src/
javac enums/*.java model/*.java strategy/*.java service/*.java Main.java
java Main
```

### **Option 2: With Packages**
```bash
cd 05-Coffee-Machine/
javac -d bin src/enums/*.java src/model/*.java src/strategy/*.java src/service/*.java src/Main.java
java -cp bin Main
```

### **Expected Output:**
```
========================================
  ☕ COFFEE MACHINE SYSTEM DEMO ☕
========================================

✅ Coffee Machine Initialized!
=== Current Inventory ===
Coffee Beans: 1000g
Milk: 2000ml
Water: 3000ml
Sugar: 500g

========================================
  STARBREW COFFEE MACHINE - MENU
========================================

☕ COFFEE TYPES:
   ESPRESSO        $3.50 - Strong and concentrated coffee
   LATTE           $4.50 - Espresso with steamed milk
   CAPPUCCINO      $4.25 - Espresso with foamed milk
   AMERICANO       $3.75 - Espresso diluted with hot water

...
```

---

## 📈 Extensions & Improvements

### **1. Add Loyalty Program**

```java
public class LoyaltyPricingStrategy implements PricingStrategy {
    private final int loyaltyPoints;
    
    public double calculatePrice(Coffee coffee) {
        double price = new BasePricingStrategy().calculatePrice(coffee);
        double discount = Math.min(loyaltyPoints * 0.01, price * 0.50); // Max 50% off
        return price - discount;
    }
}
```

### **2. Add Seasonal Flavors**

```java
public enum FlavorShot {
    VANILLA(0.50),
    CARAMEL(0.50),
    HAZELNUT(0.50),
    PUMPKIN_SPICE(0.75), // Seasonal
    PEPPERMINT(0.75);    // Holiday
    
    private final double cost;
}

// In Coffee.Builder
public Builder addFlavor(FlavorShot flavor) {
    this.flavorShots.add(flavor);
    return this;
}
```

### **3. Add Temperature Control**

```java
public enum Temperature {
    EXTRA_HOT(190),
    HOT(180),
    WARM(160),
    ICED(40);
    
    private final int fahrenheit;
}
```

### **4. Add Database Persistence**

```java
public interface OrderRepository {
    void saveOrder(Order order);
    List<Order> getOrderHistory(String customerId);
    double getTotalRevenue();
}
```

### **5. Add Async Order Processing**

```java
public CompletableFuture<Coffee> prepareCoffeeAsync(Coffee coffee) {
    return CompletableFuture.supplyAsync(() -> {
        // Simulate preparation time
        Thread.sleep(5000);
        return prepareCoffee(coffee);
    });
}
```

---

## 🎯 Interview Tips

### **What Interviewers Look For:**

1. ✅ **Correct use of Builder pattern** (static inner class, fluent interface)
2. ✅ **Correct use of Strategy pattern** (interface + implementations)
3. ✅ **Immutability** (final fields in Coffee)
4. ✅ **Validation** (in Builder.build() method)
5. ✅ **Clean separation** (model, service, strategy)
6. ✅ **Extensibility** (easy to add new coffee types, strategies)

### **Common Follow-up Questions:**

**Q: "Why use Builder instead of constructor with many parameters?"**
```
A: Builder pattern solves several problems:
1. Readability - .withWhippedCream() vs true
2. Optional parameters - no need for 20+ constructors
3. Immutability - set all fields before creating object
4. Validation - validate in build() before creation
5. Fluent interface - method chaining improves code clarity
```

**Q: "Why use Strategy pattern for pricing?"**
```
A: Strategy pattern provides:
1. Open-Closed Principle - add new strategies without modifying existing code
2. Runtime flexibility - switch pricing during happy hour, promotions
3. Testability - test each strategy independently
4. Single Responsibility - each strategy handles one pricing algorithm
```

**Q: "How would you handle concurrent orders?"**
```java
// Option 1: Synchronized methods
public synchronized Coffee prepareCoffee(Coffee coffee) { ... }

// Option 2: Lock per resource
private final ReentrantLock inventoryLock = new ReentrantLock();

public Coffee prepareCoffee(Coffee coffee) {
    inventoryLock.lock();
    try {
        // Check and deduct inventory
    } finally {
        inventoryLock.unlock();
    }
}

// Option 3: Atomic operations
private final AtomicInteger coffeeBeans = new AtomicInteger(1000);
```

**Q: "How would you add a new coffee type?"**
```java
// Just add to enum - no other code changes needed!
public enum CoffeeType {
    ESPRESSO(18, 0, 30, 3.50, "Strong and concentrated"),
    LATTE(18, 150, 30, 4.50, "Espresso with steamed milk"),
    CAPPUCCINO(18, 100, 30, 4.25, "Espresso with foamed milk"),
    AMERICANO(18, 0, 150, 3.75, "Espresso diluted with hot water"),
    MOCHA(18, 120, 30, 5.00, "Espresso with chocolate and steamed milk"); // NEW!
}
```

---

## ✅ Checklist Before Interview

- [ ] Can explain Builder pattern clearly (why, when, how)
- [ ] Can explain Strategy pattern clearly (why, when, how)
- [ ] Can code Builder from scratch in <10 minutes
- [ ] Can code Strategy from scratch in <5 minutes
- [ ] Understand immutability benefits
- [ ] Can handle follow-up questions (concurrency, extensions)
- [ ] Code compiles and runs without errors
- [ ] Can extend (add new coffee types, pricing strategies)

---

**This solution demonstrates production-quality code with proper design patterns, clean architecture, and extensibility! ☕🚀**


