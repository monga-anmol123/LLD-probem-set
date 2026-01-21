import model.*;
import service.*;
import enums.*;
import strategy.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  ☕ COFFEE MACHINE SYSTEM DEMO ☕");
        System.out.println("========================================\n");
        
        // Initialize coffee machine with inventory (Singleton)
        Inventory inventory = new Inventory(
            1000,  // 1000g coffee beans
            2000,  // 2000ml milk
            3000,  // 3000ml water
            500    // 500g sugar
        );
        
        CoffeeMachine machine = CoffeeMachine.getInstance("StarBrew Coffee Machine", inventory);
        
        System.out.println("✅ Coffee Machine Initialized!");
        machine.checkInventory();
        
        // Display menu
        machine.displayMenu();
        
        // ====================
        // SCENARIO 1: Simple Coffee (Builder Pattern)
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: SIMPLE ESPRESSO");
        System.out.println("========================================");
        
        Coffee espresso = new Coffee.Builder(CoffeeType.ESPRESSO)
            .size(Size.SMALL)
            .sugarLevel(1)
            .build();
        
        double price1 = machine.prepareCoffee(espresso);
        
        // ====================
        // SCENARIO 2: Complex Coffee with All Options (Builder Pattern)
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 2: COMPLEX LATTE (Builder Pattern)");
        System.out.println("========================================");
        
        Coffee complexLatte = new Coffee.Builder(CoffeeType.LATTE)
            .size(Size.LARGE)
            .milkType(MilkType.ALMOND_MILK)
            .sugarLevel(2)
            .withWhippedCream()
            .withExtraShot()
            .build();
        
        double price2 = machine.prepareCoffee(complexLatte);
        
        // ====================
        // SCENARIO 3: Multiple Coffee Types
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 3: MULTIPLE COFFEE TYPES");
        System.out.println("========================================");
        
        Coffee cappuccino = new Coffee.Builder(CoffeeType.CAPPUCCINO)
            .size(Size.MEDIUM)
            .milkType(MilkType.WHOLE_MILK)
            .sugarLevel(3)
            .withWhippedCream()
            .build();
        
        Coffee americano = new Coffee.Builder(CoffeeType.AMERICANO)
            .size(Size.LARGE)
            .sugarLevel(0)
            .build();
        
        double price3 = machine.prepareCoffee(cappuccino);
        double price4 = machine.prepareCoffee(americano);
        
        // ====================
        // SCENARIO 4: Pricing Strategy Switch (Strategy Pattern)
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 4: PRICING STRATEGIES");
        System.out.println("========================================\n");
        
        // Create same coffee
        Coffee testCoffee = new Coffee.Builder(CoffeeType.LATTE)
            .size(Size.MEDIUM)
            .milkType(MilkType.SOY_MILK)
            .sugarLevel(2)
            .withWhippedCream()
            .build();
        
        System.out.println("Testing same coffee with different pricing strategies:");
        System.out.println("Coffee: " + testCoffee.getDescription() + "\n");
        
        // Base Pricing
        machine.setPricingStrategy(new BasePricingStrategy());
        BasePricingStrategy baseStrategy = new BasePricingStrategy();
        double basePrice = baseStrategy.calculatePrice(testCoffee);
        System.out.printf("Base Pricing: $%.2f\n", basePrice);
        
        // Premium Pricing
        machine.setPricingStrategy(new PremiumPricingStrategy());
        PremiumPricingStrategy premiumStrategy = new PremiumPricingStrategy();
        double premiumPrice = premiumStrategy.calculatePrice(testCoffee);
        System.out.printf("Premium Pricing: $%.2f (%.0f%% more)\n", 
            premiumPrice, ((premiumPrice - basePrice) / basePrice * 100));
        
        // Discount Pricing
        machine.setPricingStrategy(new DiscountPricingStrategy(0.20)); // 20% off
        DiscountPricingStrategy discountStrategy = new DiscountPricingStrategy(0.20);
        double discountPrice = discountStrategy.calculatePrice(testCoffee);
        System.out.printf("Discount Pricing (20%% off): $%.2f\n\n", discountPrice);
        
        // Reset to base pricing
        machine.setPricingStrategy(new BasePricingStrategy());
        
        // ====================
        // SCENARIO 5: Order with Multiple Items
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 5: COMPLETE ORDER");
        System.out.println("========================================");
        
        Order order = new Order();
        
        Coffee orderCoffee1 = new Coffee.Builder(CoffeeType.ESPRESSO)
            .size(Size.SMALL)
            .build();
        double orderPrice1 = machine.prepareCoffee(orderCoffee1);
        order.addItem(orderCoffee1, orderPrice1);
        
        Coffee orderCoffee2 = new Coffee.Builder(CoffeeType.CAPPUCCINO)
            .size(Size.MEDIUM)
            .milkType(MilkType.OAT_MILK)
            .sugarLevel(1)
            .build();
        double orderPrice2 = machine.prepareCoffee(orderCoffee2);
        order.addItem(orderCoffee2, orderPrice2);
        
        Coffee orderCoffee3 = new Coffee.Builder(CoffeeType.LATTE)
            .size(Size.LARGE)
            .milkType(MilkType.ALMOND_MILK)
            .sugarLevel(2)
            .withWhippedCream()
            .build();
        double orderPrice3 = machine.prepareCoffee(orderCoffee3);
        order.addItem(orderCoffee3, orderPrice3);
        
        order.displayOrderSummary();
        
        // ====================
        // SCENARIO 6: Inventory Check
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 6: INVENTORY STATUS");
        System.out.println("========================================\n");
        
        System.out.println("After preparing " + (order.getItems().size() + 4) + " coffees:");
        machine.checkInventory();
        
        // ====================
        // SCENARIO 7: Insufficient Ingredients (Error Handling)
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 7: INSUFFICIENT INGREDIENTS");
        System.out.println("========================================");
        
        // Manually deplete coffee beans for demonstration
        System.out.println("\n🔧 Simulating low inventory...");
        int currentBeans = inventory.getCoffeeBeans();
        inventory.deductIngredients(new Coffee.Builder(CoffeeType.ESPRESSO).build());
        
        // Keep deducting until almost empty
        while (inventory.getCoffeeBeans() > 50) {
            try {
                Coffee depleter = new Coffee.Builder(CoffeeType.ESPRESSO)
                    .size(Size.LARGE)
                    .withExtraShot()
                    .build();
                inventory.deductIngredients(depleter);
            } catch (IllegalStateException e) {
                break;
            }
        }
        
        System.out.println("\n⚠️  Low inventory detected!");
        machine.checkInventory();
        
        System.out.println("\n❌ Attempting to prepare coffee with insufficient ingredients:");
        try {
            Coffee failCoffee = new Coffee.Builder(CoffeeType.LATTE)
                .size(Size.LARGE)
                .withExtraShot()
                .build();
            machine.prepareCoffee(failCoffee);
        } catch (IllegalStateException e) {
            System.out.println("\n✓ Error handled gracefully!");
        }
        
        // Refill inventory
        machine.refillInventory(1000, 2000, 3000, 500);
        
        System.out.println("\n✅ Now we can prepare coffee again!");
        Coffee afterRefill = new Coffee.Builder(CoffeeType.LATTE)
            .size(Size.MEDIUM)
            .milkType(MilkType.WHOLE_MILK)
            .build();
        machine.prepareCoffee(afterRefill);
        
        // ====================
        // SCENARIO 8: Builder Validation
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 8: BUILDER VALIDATION");
        System.out.println("========================================\n");
        
        System.out.println("❌ Attempting invalid coffee (whipped cream on espresso without milk):");
        try {
            Coffee invalidCoffee = new Coffee.Builder(CoffeeType.ESPRESSO)
                .withWhippedCream() // Invalid: espresso has no milk
                .build();
        } catch (IllegalStateException e) {
            System.out.println("   Error: " + e.getMessage());
            System.out.println("   ✓ Validation works correctly!\n");
        }
        
        System.out.println("❌ Attempting invalid sugar level:");
        try {
            Coffee invalidSugar = new Coffee.Builder(CoffeeType.LATTE)
                .sugarLevel(10) // Invalid: max is 5
                .build();
        } catch (IllegalArgumentException e) {
            System.out.println("   Error: " + e.getMessage());
            System.out.println("   ✓ Validation works correctly!\n");
        }
        
        // ====================
        // SCENARIO 9: All Coffee Types Showcase
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 9: ALL COFFEE TYPES");
        System.out.println("========================================\n");
        
        System.out.println("Preparing one of each type:\n");
        
        for (CoffeeType type : CoffeeType.values()) {
            Coffee showcaseCoffee = new Coffee.Builder(type)
                .size(Size.MEDIUM)
                .build();
            
            System.out.println("• " + type + ": " + type.getDescription());
            double showcasePrice = machine.prepareCoffee(showcaseCoffee);
        }
        
        // ====================
        // Final Summary
        // ====================
        System.out.println("\n========================================");
        System.out.println("  FINAL INVENTORY STATUS");
        System.out.println("========================================\n");
        
        machine.checkInventory();
        
        System.out.println("\n========================================");
        System.out.println("  ✅ DEMO COMPLETE!");
        System.out.println("========================================\n");
        
        System.out.println("Design Patterns Demonstrated:");
        System.out.println("✓ Builder Pattern - Coffee creation with fluent interface");
        System.out.println("✓ Strategy Pattern - Interchangeable pricing strategies");
        System.out.println("✓ Singleton Pattern - Single CoffeeMachine instance");
        
        System.out.println("\nKey Features Demonstrated:");
        System.out.println("✓ Fluent builder interface for complex object creation");
        System.out.println("✓ Multiple coffee types with customizations");
        System.out.println("✓ Ingredient inventory management");
        System.out.println("✓ Dynamic pricing strategy switching");
        System.out.println("✓ Order management with multiple items");
        System.out.println("✓ Error handling (insufficient ingredients, invalid builds)");
        System.out.println("✓ Input validation (sugar level, whipped cream rules)");
        
        System.out.println("\nExtensibility:");
        System.out.println("✓ Easy to add new coffee types (just add to enum)");
        System.out.println("✓ Easy to add new pricing strategies (implement interface)");
        System.out.println("✓ Easy to add new milk types or add-ons");
        System.out.println("✓ Clean separation of concerns (model, service, strategy)");
    }
}


