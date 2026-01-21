import service.FoodDeliveryPlatform;
import model.*;
import enums.*;
import strategy.*;

/**
 * Demo application for Food Delivery System (Zomato/Swiggy)
 * Demonstrates Factory, Strategy, State, and Observer design patterns
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║           FOOD DELIVERY SYSTEM DEMO (Zomato/Swiggy)               ║");
        System.out.println("║     Patterns: Factory + Strategy + State + Observer + Singleton   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝\n");
        
        // Get platform instance (Singleton)
        FoodDeliveryPlatform platform = FoodDeliveryPlatform.getInstance("FoodExpress");
        System.out.println("🍔 Welcome to " + platform.getPlatformName() + "!\n");
        
        // Scenario 1: Setup Restaurants and Menu
        scenario1_SetupRestaurantsAndMenu(platform);
        
        // Scenario 2: Register Customers and Delivery Partners (Factory Pattern)
        scenario2_RegisterUsersWithFactory(platform);
        
        // Scenario 3: Customer Orders Food
        scenario3_CustomerOrdersFood(platform);
        
        // Scenario 4: Order State Transitions (State Pattern)
        scenario4_OrderStateTransitions(platform);
        
        // Scenario 5: Delivery Pricing Strategies (Strategy Pattern)
        scenario5_DeliveryPricingStrategies(platform);
        
        // Scenario 6: Notifications (Observer Pattern)
        scenario6_NotificationsObserverPattern(platform);
        
        // Scenario 7: Edge Cases
        scenario7_EdgeCases(platform);
        
        // Final Summary
        finalSummary(platform);
    }
    
    /**
     * SCENARIO 1: Setup Restaurants and Menu
     */
    private static void scenario1_SetupRestaurantsAndMenu(FoodDeliveryPlatform platform) {
        System.out.println("\n" + repeat("=", 75));
        System.out.println("  SCENARIO 1: SETUP RESTAURANTS AND MENU");
        System.out.println(repeat("=", 75) + "\n");
        
        // Create addresses
        Address restaurant1Location = new Address("123 Main St", "Mumbai", "MH", "400001", 19.0760, 72.8777);
        Address restaurant2Location = new Address("456 Park Ave", "Mumbai", "MH", "400002", 19.0896, 72.8656);
        Address restaurant3Location = new Address("789 Beach Rd", "Mumbai", "MH", "400003", 19.1136, 72.9083);
        
        // Create restaurants
        Restaurant restaurant1 = new Restaurant("REST001", "Spice Garden", restaurant1Location, CuisineType.INDIAN);
        Restaurant restaurant2 = new Restaurant("REST002", "Dragon Wok", restaurant2Location, CuisineType.CHINESE);
        Restaurant restaurant3 = new Restaurant("REST003", "Pizza Palace", restaurant3Location, CuisineType.ITALIAN);
        
        // Add menu items to Restaurant 1 (Indian)
        restaurant1.addMenuItem(new MenuItem("ITEM001", "Butter Chicken", 12.99, MenuCategory.MAIN_COURSE, "Creamy tomato curry"));
        restaurant1.addMenuItem(new MenuItem("ITEM002", "Paneer Tikka", 10.99, MenuCategory.APPETIZER, "Grilled cottage cheese"));
        restaurant1.addMenuItem(new MenuItem("ITEM003", "Naan", 2.99, MenuCategory.MAIN_COURSE, "Indian bread"));
        restaurant1.addMenuItem(new MenuItem("ITEM004", "Gulab Jamun", 4.99, MenuCategory.DESSERT, "Sweet dumplings"));
        
        // Add menu items to Restaurant 2 (Chinese)
        restaurant2.addMenuItem(new MenuItem("ITEM005", "Kung Pao Chicken", 13.99, MenuCategory.MAIN_COURSE, "Spicy stir-fry"));
        restaurant2.addMenuItem(new MenuItem("ITEM006", "Spring Rolls", 6.99, MenuCategory.APPETIZER, "Crispy rolls"));
        restaurant2.addMenuItem(new MenuItem("ITEM007", "Fried Rice", 8.99, MenuCategory.MAIN_COURSE, "Egg fried rice"));
        
        // Add menu items to Restaurant 3 (Italian)
        restaurant3.addMenuItem(new MenuItem("ITEM008", "Margherita Pizza", 14.99, MenuCategory.MAIN_COURSE, "Classic pizza"));
        restaurant3.addMenuItem(new MenuItem("ITEM009", "Pasta Alfredo", 12.99, MenuCategory.MAIN_COURSE, "Creamy pasta"));
        restaurant3.addMenuItem(new MenuItem("ITEM010", "Tiramisu", 6.99, MenuCategory.DESSERT, "Coffee dessert"));
        
        // Add restaurants to platform
        platform.addRestaurant(restaurant1);
        platform.addRestaurant(restaurant2);
        platform.addRestaurant(restaurant3);
        
        System.out.println("\n✓ All restaurants and menus set up successfully!");
        
        // Display a restaurant menu
        restaurant1.displayMenu();
    }
    
    /**
     * SCENARIO 2: Register Users with Factory Pattern
     */
    private static void scenario2_RegisterUsersWithFactory(FoodDeliveryPlatform platform) {
        System.out.println("\n" + repeat("=", 75));
        System.out.println("  SCENARIO 2: REGISTER USERS (FACTORY PATTERN)");
        System.out.println(repeat("=", 75) + "\n");
        
        // Register customers using Factory
        Customer customer1 = platform.registerCustomer("John Doe", "+91-9876543210", "john@email.com");
        Customer customer2 = platform.registerCustomer("Jane Smith", "+91-9876543211", "jane@email.com");
        
        // Add addresses to customers
        Address customer1Address = new Address("10 Oak St", "Mumbai", "MH", "400004", 19.0825, 72.8750);
        Address customer2Address = new Address("20 Elm St", "Mumbai", "MH", "400005", 19.0950, 72.8800);
        
        customer1.addAddress(customer1Address);
        customer2.addAddress(customer2Address);
        
        // Register delivery partners using Factory
        Address partner1Location = new Address("Central Hub", "Mumbai", "MH", "400001", 19.0760, 72.8777);
        Address partner2Location = new Address("West Hub", "Mumbai", "MH", "400002", 19.0896, 72.8656);
        Address partner3Location = new Address("East Hub", "Mumbai", "MH", "400003", 19.1136, 72.9083);
        
        DeliveryPartner partner1 = platform.registerDeliveryPartner("Raj Kumar", "+91-9876543220", partner1Location);
        DeliveryPartner partner2 = platform.registerDeliveryPartner("Amit Singh", "+91-9876543221", partner2Location);
        DeliveryPartner partner3 = platform.registerDeliveryPartner("Priya Sharma", "+91-9876543222", partner3Location);
        
        System.out.println("\n✓ All users registered using Factory Pattern!");
        
        platform.displayAvailableDeliveryPartners();
    }
    
    /**
     * SCENARIO 3: Customer Orders Food
     */
    private static void scenario3_CustomerOrdersFood(FoodDeliveryPlatform platform) {
        System.out.println("\n" + repeat("=", 75));
        System.out.println("  SCENARIO 3: CUSTOMER ORDERS FOOD");
        System.out.println(repeat("=", 75) + "\n");
        
        Customer customer = platform.getCustomer("CUST0001");
        Restaurant restaurant = platform.getRestaurant("REST001");
        
        // Browse menu
        System.out.println("Customer browsing menu...");
        restaurant.displayMenu();
        
        // Add items to cart
        System.out.println("\nAdding items to cart...");
        Cart cart = customer.getCart();
        cart.addItem(restaurant.getMenuItem("ITEM001"), 2); // Butter Chicken x2
        cart.addItem(restaurant.getMenuItem("ITEM002"), 1); // Paneer Tikka x1
        cart.addItem(restaurant.getMenuItem("ITEM003"), 3); // Naan x3
        
        // Display cart
        cart.displayCart();
        
        // Place order
        System.out.println("\nPlacing order...");
        Order order = platform.placeOrder(customer, restaurant, customer.getPrimaryAddress(), PaymentMethod.UPI);
        
        // Display order summary
        order.displayOrderSummary();
    }
    
    /**
     * SCENARIO 4: Order State Transitions (State Pattern)
     */
    private static void scenario4_OrderStateTransitions(FoodDeliveryPlatform platform) {
        System.out.println("\n" + repeat("=", 75));
        System.out.println("  SCENARIO 4: ORDER STATE TRANSITIONS (STATE PATTERN)");
        System.out.println(repeat("=", 75) + "\n");
        
        Order order = platform.getOrder("ORD000001");
        
        System.out.println("Current Order Status: " + order.getStatus());
        System.out.println("\n--- State Transition Flow ---\n");
        
        // State 1: PLACED → PREPARING
        System.out.println("1. Restaurant starts preparing food...");
        order.prepare();
        
        // State 2: PREPARING → READY_FOR_PICKUP
        System.out.println("\n2. Food is ready for pickup...");
        order.readyForPickup();
        
        // Assign delivery partner
        System.out.println("\n3. Assigning delivery partner...");
        platform.assignDeliveryPartner(order);
        
        // State 3: READY_FOR_PICKUP → OUT_FOR_DELIVERY
        System.out.println("\n4. Delivery partner picked up the order...");
        order.outForDelivery();
        
        // State 4: OUT_FOR_DELIVERY → DELIVERED
        System.out.println("\n5. Delivering to customer...");
        order.deliver();
        
        System.out.println("\n✓ Order completed successfully!");
        System.out.println("Final Status: " + order.getStatus());
    }
    
    /**
     * SCENARIO 5: Delivery Pricing Strategies (Strategy Pattern)
     */
    private static void scenario5_DeliveryPricingStrategies(FoodDeliveryPlatform platform) {
        System.out.println("\n" + repeat("=", 75));
        System.out.println("  SCENARIO 5: DELIVERY PRICING STRATEGIES (STRATEGY PATTERN)");
        System.out.println(repeat("=", 75) + "\n");
        
        Customer customer = platform.getCustomer("CUST0002");
        Restaurant restaurant = platform.getRestaurant("REST002");
        
        // Add items to cart
        Cart cart = customer.getCart();
        cart.addItem(restaurant.getMenuItem("ITEM005"), 1); // Kung Pao Chicken
        cart.addItem(restaurant.getMenuItem("ITEM006"), 2); // Spring Rolls x2
        
        double distance = restaurant.getLocation().distanceTo(customer.getPrimaryAddress());
        System.out.println("Distance: " + String.format("%.2f", distance) + " km\n");
        
        // Strategy 1: Distance-Based Pricing
        System.out.println("--- Strategy 1: Distance-Based Pricing ---");
        DeliveryPricingStrategy distanceStrategy = new DistanceBasedPricing();
        System.out.println(distanceStrategy.getStrategyName());
        double charge1 = distanceStrategy.calculateDeliveryCharge(distance, java.time.LocalTime.now());
        System.out.println("Delivery Charge: $" + charge1);
        
        // Strategy 2: Surge Pricing
        System.out.println("\n--- Strategy 2: Surge Pricing ---");
        DeliveryPricingStrategy surgeStrategy = new SurgePricing();
        System.out.println(surgeStrategy.getStrategyName());
        double charge2 = surgeStrategy.calculateDeliveryCharge(distance, java.time.LocalTime.of(19, 30)); // Peak hour
        System.out.println("Delivery Charge (Peak Hour): $" + charge2);
        
        // Strategy 3: Flat Rate Pricing
        System.out.println("\n--- Strategy 3: Flat Rate Pricing ---");
        DeliveryPricingStrategy flatStrategy = new FlatRatePricing();
        System.out.println(flatStrategy.getStrategyName());
        double charge3 = flatStrategy.calculateDeliveryCharge(distance, java.time.LocalTime.now());
        System.out.println("Delivery Charge: $" + charge3);
        
        System.out.println("\n✓ Different pricing strategies demonstrated!");
        
        // Set surge pricing for next order
        platform.setPricingStrategy(surgeStrategy);
        
        // Clear cart
        cart.clear();
    }
    
    /**
     * SCENARIO 6: Notifications (Observer Pattern)
     */
    private static void scenario6_NotificationsObserverPattern(FoodDeliveryPlatform platform) {
        System.out.println("\n" + repeat("=", 75));
        System.out.println("  SCENARIO 6: NOTIFICATIONS (OBSERVER PATTERN)");
        System.out.println(repeat("=", 75) + "\n");
        
        Customer customer = platform.getCustomer("CUST0002");
        Restaurant restaurant = platform.getRestaurant("REST003");
        
        // Add items to cart
        Cart cart = customer.getCart();
        cart.addItem(restaurant.getMenuItem("ITEM008"), 1); // Pizza
        
        System.out.println("Placing order to demonstrate Observer Pattern...\n");
        
        // Place order (observers will be notified)
        Order order = platform.placeOrder(customer, restaurant, customer.getPrimaryAddress(), PaymentMethod.CARD);
        
        System.out.println("\n--- Demonstrating State Change Notifications ---\n");
        
        // Each state change will notify all observers
        System.out.println("Preparing food...");
        order.prepare();
        
        System.out.println("\nFood ready...");
        order.readyForPickup();
        
        System.out.println("\nAssigning delivery partner...");
        platform.assignDeliveryPartner(order);
        
        System.out.println("\nOut for delivery...");
        order.outForDelivery();
        
        System.out.println("\nDelivered!");
        order.deliver();
        
        System.out.println("\n✓ All observers notified at each state change!");
    }
    
    /**
     * SCENARIO 7: Edge Cases
     */
    private static void scenario7_EdgeCases(FoodDeliveryPlatform platform) {
        System.out.println("\n" + repeat("=", 75));
        System.out.println("  SCENARIO 7: EDGE CASES AND ERROR HANDLING");
        System.out.println(repeat("=", 75) + "\n");
        
        Customer customer = platform.getCustomer("CUST0001");
        Restaurant restaurant = platform.getRestaurant("REST001");
        
        // Test 1: Empty cart
        System.out.println("Test 1: Attempting to place order with empty cart");
        try {
            platform.placeOrder(customer, restaurant, customer.getPrimaryAddress(), PaymentMethod.CASH);
        } catch (Exception e) {
            System.out.println("✓ Correctly rejected: " + e.getMessage() + "\n");
        }
        
        // Test 2: Unavailable menu item
        System.out.println("Test 2: Adding unavailable item to cart");
        MenuItem item = restaurant.getMenuItem("ITEM001");
        item.setAvailability(false);
        try {
            customer.getCart().addItem(item, 1);
        } catch (Exception e) {
            System.out.println("✓ Correctly rejected: " + e.getMessage() + "\n");
        }
        item.setAvailability(true); // Reset
        
        // Test 3: Invalid state transitions
        System.out.println("Test 3: Invalid state transitions");
        customer.getCart().addItem(restaurant.getMenuItem("ITEM002"), 1);
        Order order = platform.placeOrder(customer, restaurant, customer.getPrimaryAddress(), PaymentMethod.UPI);
        
        System.out.println("\nAttempting to deliver without preparing:");
        order.deliver(); // Should fail
        
        System.out.println("\nAttempting to go out for delivery without being ready:");
        order.outForDelivery(); // Should fail
        
        System.out.println("\n✓ All edge cases handled correctly!");
    }
    
    /**
     * Final Summary
     */
    private static void finalSummary(FoodDeliveryPlatform platform) {
        System.out.println("\n" + "╔" + repeat("═", 73) + "╗");
        System.out.println("║" + repeat(" ", 73) + "║");
        System.out.println("║" + centerText("DEMO COMPLETE!", 73) + "║");
        System.out.println("║" + repeat(" ", 73) + "║");
        System.out.println("╚" + repeat("═", 73) + "╝");
        
        System.out.println("\n📊 FINAL STATISTICS:");
        System.out.println("   Platform Name: " + platform.getPlatformName());
        System.out.println("   Total Restaurants: " + platform.getAllRestaurants().size());
        System.out.println("   Total Orders: " + platform.getAllOrders().size());
        System.out.println("   Available Delivery Partners: " + platform.getAvailableDeliveryPartners().size());
        
        System.out.println("\n🎨 DESIGN PATTERNS DEMONSTRATED:");
        System.out.println("   ✓ Factory Pattern    - User creation (UserFactory)");
        System.out.println("   ✓ Strategy Pattern   - Delivery pricing (DeliveryPricingStrategy)");
        System.out.println("   ✓ State Pattern      - Order state management (OrderState)");
        System.out.println("   ✓ Observer Pattern   - Notifications (OrderObserver)");
        System.out.println("   ✓ Singleton Pattern  - Platform instance (FoodDeliveryPlatform)");
        
        System.out.println("\n✅ FEATURES DEMONSTRATED:");
        System.out.println("   ✓ Restaurant and menu management");
        System.out.println("   ✓ Customer registration and cart management");
        System.out.println("   ✓ Order placement and tracking");
        System.out.println("   ✓ Order state machine (PLACED → PREPARING → READY → OUT → DELIVERED)");
        System.out.println("   ✓ Multiple delivery pricing strategies");
        System.out.println("   ✓ Real-time notifications to all stakeholders");
        System.out.println("   ✓ Delivery partner assignment");
        System.out.println("   ✓ Distance calculation and routing");
        System.out.println("   ✓ Comprehensive error handling");
        
        System.out.println("\n💡 KEY LEARNINGS:");
        System.out.println("   • Factory Pattern simplifies user creation");
        System.out.println("   • Strategy Pattern enables flexible pricing");
        System.out.println("   • State Pattern manages complex order lifecycle");
        System.out.println("   • Observer Pattern decouples notification logic");
        System.out.println("   • Singleton Pattern ensures single platform instance");
        
        System.out.println("\n" + repeat("=", 75));
        System.out.println("Thank you for using " + platform.getPlatformName() + "! 🍔🍕🍜");
        System.out.println(repeat("=", 75) + "\n");
    }
    
    /**
     * Helper method to repeat a string
     */
    private static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    /**
     * Helper method to center text
     */
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        sb.append(text);
        while (sb.length() < width) {
            sb.append(" ");
        }
        return sb.toString();
    }
}

