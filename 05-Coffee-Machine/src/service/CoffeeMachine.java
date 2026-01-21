package service;

import model.*;
import enums.*;
import strategy.PricingStrategy;
import strategy.BasePricingStrategy;

public class CoffeeMachine {
    private static CoffeeMachine instance;
    
    private final String machineName;
    private final Inventory inventory;
    private PricingStrategy pricingStrategy;
    
    // Private constructor for Singleton
    private CoffeeMachine(String machineName, Inventory inventory) {
        this.machineName = machineName;
        this.inventory = inventory;
        this.pricingStrategy = new BasePricingStrategy(); // Default pricing
    }
    
    // Singleton getInstance
    public static synchronized CoffeeMachine getInstance(String machineName, Inventory inventory) {
        if (instance == null) {
            instance = new CoffeeMachine(machineName, inventory);
        }
        return instance;
    }
    
    // Set pricing strategy (Strategy Pattern)
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
        System.out.println("✓ Pricing strategy updated to: " + pricingStrategy.getClass().getSimpleName());
    }
    
    // Prepare a coffee
    public double prepareCoffee(Coffee coffee) {
        System.out.println("\n☕ Preparing: " + coffee.getDescription());
        
        // Check ingredient availability
        if (!inventory.checkAvailability(coffee)) {
            System.out.println("❌ ERROR: Insufficient ingredients!");
            System.out.println("   Required:");
            System.out.println("   - Coffee Beans: " + coffee.getCoffeeBeansNeeded() + "g (Available: " + inventory.getCoffeeBeans() + "g)");
            System.out.println("   - Milk: " + coffee.getMilkNeeded() + "ml (Available: " + inventory.getMilk() + "ml)");
            System.out.println("   - Water: " + coffee.getWaterNeeded() + "ml (Available: " + inventory.getWater() + "ml)");
            System.out.println("   - Sugar: " + coffee.getSugarNeeded() + "g (Available: " + inventory.getSugar() + "g)");
            throw new IllegalStateException("Insufficient ingredients to prepare coffee");
        }
        
        // Calculate price
        double price = pricingStrategy.calculatePrice(coffee);
        
        // Deduct ingredients
        inventory.deductIngredients(coffee);
        
        // Display preparation steps
        System.out.println("   1. Grinding " + coffee.getCoffeeBeansNeeded() + "g of coffee beans...");
        System.out.println("   2. Heating " + coffee.getWaterNeeded() + "ml of water...");
        
        if (coffee.getMilkNeeded() > 0) {
            System.out.println("   3. Steaming " + coffee.getMilkNeeded() + "ml of " + coffee.getMilkType().getDisplayName() + "...");
        }
        
        if (coffee.getSugarLevel() > 0) {
            System.out.println("   4. Adding " + coffee.getSugarNeeded() + "g of sugar...");
        }
        
        if (coffee.hasWhippedCream()) {
            System.out.println("   5. Adding whipped cream...");
        }
        
        if (coffee.hasExtraShot()) {
            System.out.println("   6. Adding extra espresso shot...");
        }
        
        System.out.println("✅ Coffee ready!");
        System.out.printf("💰 Price: $%.2f\n", price);
        
        return price;
    }
    
    // Display menu
    public void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("  " + machineName.toUpperCase() + " - MENU");
        System.out.println("========================================\n");
        
        System.out.println("☕ COFFEE TYPES:");
        for (CoffeeType type : CoffeeType.values()) {
            System.out.printf("   %-15s $%.2f - %s\n", 
                type, type.getBasePrice(), type.getDescription());
        }
        
        System.out.println("\n📏 SIZES:");
        for (Size size : Size.values()) {
            System.out.printf("   %-10s %s (%.0f%% price)\n", 
                size, size.getVolume(), size.getPriceMultiplier() * 100);
        }
        
        System.out.println("\n🥛 MILK OPTIONS:");
        for (MilkType milk : MilkType.values()) {
            if (milk.getAdditionalCost() > 0) {
                System.out.printf("   %-15s +$%.2f\n", milk.getDisplayName(), milk.getAdditionalCost());
            } else {
                System.out.printf("   %-15s Free\n", milk.getDisplayName());
            }
        }
        
        System.out.println("\n🎨 ADD-ONS:");
        System.out.println("   Whipped Cream   +$0.75");
        System.out.println("   Extra Shot      +$1.00");
        System.out.println("   Sugar (1-5)     Free");
        
        System.out.println("\n========================================\n");
    }
    
    // Check inventory status
    public void checkInventory() {
        inventory.displayInventory();
    }
    
    // Refill inventory
    public void refillInventory(int coffeeBeans, int milk, int water, int sugar) {
        System.out.println("\n🔄 Refilling inventory...");
        inventory.refill(coffeeBeans, milk, water, sugar);
        System.out.println("✅ Inventory refilled!");
        inventory.displayInventory();
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public String getMachineName() {
        return machineName;
    }
}


