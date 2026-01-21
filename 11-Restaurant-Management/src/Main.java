import model.*;
import service.*;
import enums.*;
import strategy.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  RESTAURANT MANAGEMENT SYSTEM DEMO");
        System.out.println("========================================\n");
        
        // Initialize Restaurant (Singleton)
        Restaurant restaurant = Restaurant.getInstance("The Gourmet Palace");
        System.out.println("✓ Restaurant initialized: " + restaurant.getName() + "\n");
        
        // Setup Tables
        setupTables(restaurant);
        
        // Setup Menu
        setupMenu(restaurant);
        
        // Setup Staff
        Waiter waiter1 = new Waiter("W001", "Alice", "alice@restaurant.com");
        Waiter waiter2 = new Waiter("W002", "Bob", "bob@restaurant.com");
        Chef chef1 = new Chef("C001", "Gordon", "gordon@restaurant.com", "Italian");
        Chef chef2 = new Chef("C002", "Jamie", "jamie@restaurant.com", "French");
        
        restaurant.getKitchen().addChef(chef1);
        restaurant.getKitchen().addChef(chef2);
        System.out.println();
        
        // Display initial availability
        restaurant.displayAvailability();
        
        // Scenario 1: Basic Reservation & Order Flow
        scenario1_BasicReservationAndOrder(restaurant, waiter1);
        
        // Scenario 2: Walk-in Customer
        scenario2_WalkInCustomer(restaurant, waiter2);
        
        // Scenario 3: No Available Tables
        scenario3_NoAvailableTables(restaurant);
        
        // Scenario 4: Observer Pattern - Order Status Notifications
        scenario4_OrderStatusNotifications(restaurant, waiter1);
        
        // Scenario 5: Strategy Pattern - Dynamic Pricing
        scenario5_DynamicPricing(restaurant, waiter2);
        
        // Scenario 6: Split Bill
        scenario6_SplitBill(restaurant, waiter1);
        
        // Scenario 7: VIP Priority
        scenario7_VIPPriority(restaurant, waiter1, waiter2);
        
        // Final Summary
        printFinalSummary(restaurant, waiter1, waiter2, chef1, chef2);
    }
    
    private static void setupTables(Restaurant restaurant) {
        System.out.println("Setting up tables...");
        // Add various table types
        restaurant.addTable(new Table("T01", TableType.SMALL));
        restaurant.addTable(new Table("T02", TableType.SMALL));
        restaurant.addTable(new Table("T03", TableType.MEDIUM));
        restaurant.addTable(new Table("T04", TableType.MEDIUM));
        restaurant.addTable(new Table("T05", TableType.LARGE));
        restaurant.addTable(new Table("T06", TableType.LARGE));
        restaurant.addTable(new Table("T07", TableType.VIP));
        restaurant.addTable(new Table("T08", TableType.VIP));
        System.out.println("✓ 8 tables added.\n");
    }
    
    private static void setupMenu(Restaurant restaurant) {
        System.out.println("Setting up menu...");
        
        // Appetizers
        restaurant.addMenuItem(new MenuItem("A01", "Caesar Salad", MenuCategory.APPETIZER, 12.99, 10));
        restaurant.addMenuItem(new MenuItem("A02", "Bruschetta", MenuCategory.APPETIZER, 9.99, 8));
        restaurant.addMenuItem(new MenuItem("A03", "Soup of the Day", MenuCategory.APPETIZER, 8.99, 12));
        
        // Main Courses
        restaurant.addMenuItem(new MenuItem("M01", "Grilled Salmon", MenuCategory.MAIN_COURSE, 28.99, 25));
        restaurant.addMenuItem(new MenuItem("M02", "Ribeye Steak", MenuCategory.MAIN_COURSE, 35.99, 30));
        restaurant.addMenuItem(new MenuItem("M03", "Pasta Carbonara", MenuCategory.MAIN_COURSE, 22.99, 20));
        restaurant.addMenuItem(new MenuItem("M04", "Vegetarian Risotto", MenuCategory.MAIN_COURSE, 19.99, 22));
        
        // Desserts
        restaurant.addMenuItem(new MenuItem("D01", "Tiramisu", MenuCategory.DESSERT, 8.99, 5));
        restaurant.addMenuItem(new MenuItem("D02", "Chocolate Lava Cake", MenuCategory.DESSERT, 9.99, 8));
        
        // Beverages
        restaurant.addMenuItem(new MenuItem("B01", "Red Wine", MenuCategory.BEVERAGE, 15.99, 2));
        restaurant.addMenuItem(new MenuItem("B02", "White Wine", MenuCategory.BEVERAGE, 14.99, 2));
        restaurant.addMenuItem(new MenuItem("B03", "Craft Beer", MenuCategory.BEVERAGE, 7.99, 2));
        restaurant.addMenuItem(new MenuItem("B04", "Sparkling Water", MenuCategory.BEVERAGE, 3.99, 1));
        
        System.out.println("✓ Menu items added.\n");
    }
    
    private static void scenario1_BasicReservationAndOrder(Restaurant restaurant, Waiter waiter1) {
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 1: Basic Reservation & Order");
        System.out.println("========================================\n");
        
        // Create reservation
        Reservation res1 = restaurant.createReservation(
            "John Smith", 
            "555-1234", 
            4, 
            LocalDateTime.now()
        );
        
        if (res1 != null) {
            // Seat the reservation
            restaurant.seatReservation(res1);
            Table table = res1.getAssignedTable();
            waiter1.assignTable(table);
            
            // Place order
            List<String> items = Arrays.asList("A01", "A02", "M01", "M02", "M03", "M04", "D01", "D02", "B01", "B02");
            Order order = restaurant.placeOrder(table, waiter1, items);
            
            // Process in kitchen
            System.out.println("\n--- Kitchen Processing ---");
            restaurant.processOrderInKitchen(order);
            
            // Simulate cooking time
            System.out.println("... cooking ...");
            
            // Complete order
            restaurant.completeOrderInKitchen(order);
            
            // Serve order
            restaurant.serveOrder(order);
            
            // Generate bill
            System.out.println("\n--- Billing ---");
            Bill bill = restaurant.generateBill(order);
            bill.printBill();
            
            // Release table
            restaurant.releaseTable(table);
            table.markAvailable();
        }
        
        restaurant.displayAvailability();
    }
    
    private static void scenario2_WalkInCustomer(Restaurant restaurant, Waiter waiter2) {
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 2: Walk-in Customer");
        System.out.println("========================================\n");
        
        System.out.println("Walk-in customer arrives (party of 2)...");
        Table table = restaurant.findAvailableTable(2);
        
        if (table != null) {
            System.out.println("✓ Table found: " + table.getTableId());
            table.setStatus(TableStatus.OCCUPIED);
            waiter2.assignTable(table);
            
            // Quick order
            List<String> items = Arrays.asList("A03", "M03", "D01", "B03");
            Order order = restaurant.placeOrder(table, waiter2, items);
            
            // Fast processing
            restaurant.processOrderInKitchen(order);
            restaurant.completeOrderInKitchen(order);
            restaurant.serveOrder(order);
            
            // Bill and release
            Bill bill = restaurant.generateBill(order);
            bill.printBill();
            restaurant.releaseTable(table);
            table.markAvailable();
        } else {
            System.out.println("✗ No available tables for party of 2.");
        }
    }
    
    private static void scenario3_NoAvailableTables(Restaurant restaurant) {
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 3: No Available Tables");
        System.out.println("========================================\n");
        
        // Occupy all tables
        System.out.println("Occupying all tables...");
        for (Table table : restaurant.getTables()) {
            if (table.isAvailable()) {
                table.setStatus(TableStatus.OCCUPIED);
            }
        }
        
        restaurant.displayAvailability();
        
        // Try to create reservation
        System.out.println("Customer tries to make reservation for 4 people...");
        Reservation res = restaurant.createReservation(
            "Jane Doe", 
            "555-5678", 
            4, 
            LocalDateTime.now()
        );
        
        // Restore tables
        System.out.println("\nRestoring tables for next scenarios...");
        for (Table table : restaurant.getTables()) {
            table.markAvailable();
        }
        restaurant.displayAvailability();
    }
    
    private static void scenario4_OrderStatusNotifications(Restaurant restaurant, Waiter waiter1) {
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 4: Order Status Notifications");
        System.out.println("  (Observer Pattern Demo)");
        System.out.println("========================================\n");
        
        Table table = restaurant.findAvailableTable(2);
        table.setStatus(TableStatus.OCCUPIED);
        
        System.out.println("Placing order with observer notifications...\n");
        List<String> items = Arrays.asList("M01", "B01");
        Order order = restaurant.placeOrder(table, waiter1, items);
        
        System.out.println("\n--- State Transitions with Notifications ---");
        restaurant.processOrderInKitchen(order);
        
        try {
            Thread.sleep(500); // Simulate time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        restaurant.completeOrderInKitchen(order);
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        restaurant.serveOrder(order);
        
        Bill bill = restaurant.generateBill(order);
        restaurant.releaseTable(table);
        table.markAvailable();
    }
    
    private static void scenario5_DynamicPricing(Restaurant restaurant, Waiter waiter2) {
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 5: Dynamic Pricing");
        System.out.println("  (Strategy Pattern Demo)");
        System.out.println("========================================\n");
        
        Table table = restaurant.findAvailableTable(2);
        table.setStatus(TableStatus.OCCUPIED);
        
        List<String> items = Arrays.asList("M02", "B01", "B02", "D01");
        
        // Regular pricing
        System.out.println("--- Regular Pricing ---");
        restaurant.setPricingStrategy(new RegularPricingStrategy());
        Order order1 = restaurant.placeOrder(table, waiter2, items);
        System.out.println("Total: $" + String.format("%.2f", order1.getTotalAmount()));
        
        // Happy Hour pricing
        System.out.println("\n--- Happy Hour Pricing ---");
        restaurant.setPricingStrategy(new HappyHourPricingStrategy());
        Order order2 = restaurant.placeOrder(table, waiter2, items);
        System.out.println("Total: $" + String.format("%.2f", order2.getTotalAmount()));
        System.out.println("Savings: $" + String.format("%.2f", 
            order1.getTotalAmount() - order2.getTotalAmount()));
        
        // Weekend pricing
        System.out.println("\n--- Weekend Pricing ---");
        restaurant.setPricingStrategy(new WeekendPricingStrategy());
        Order order3 = restaurant.placeOrder(table, waiter2, items);
        System.out.println("Total: $" + String.format("%.2f", order3.getTotalAmount()));
        System.out.println("Extra charge: $" + String.format("%.2f", 
            order3.getTotalAmount() - order1.getTotalAmount()));
        
        // Reset to regular
        restaurant.setPricingStrategy(new RegularPricingStrategy());
        table.markAvailable();
    }
    
    private static void scenario6_SplitBill(Restaurant restaurant, Waiter waiter1) {
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 6: Split Bill");
        System.out.println("========================================\n");
        
        Table table = restaurant.findAvailableTable(4);
        table.setStatus(TableStatus.OCCUPIED);
        
        List<String> items = Arrays.asList("M01", "M02", "M03", "M04", "B01", "B02");
        Order order = restaurant.placeOrder(table, waiter1, items);
        
        // Process and serve
        restaurant.processOrderInKitchen(order);
        restaurant.completeOrderInKitchen(order);
        restaurant.serveOrder(order);
        
        // Generate bill
        Bill bill = restaurant.generateBill(order);
        bill.printBill();
        
        // Split bill
        System.out.println("--- Splitting Bill Among 4 People ---");
        double[] splits = bill.split(4);
        for (int i = 0; i < splits.length; i++) {
            System.out.println("Person " + (i + 1) + " pays: $" + String.format("%.2f", splits[i]));
        }
        System.out.println();
        
        restaurant.releaseTable(table);
        table.markAvailable();
    }
    
    private static void scenario7_VIPPriority(Restaurant restaurant, Waiter waiter1, Waiter waiter2) {
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 7: VIP Priority");
        System.out.println("========================================\n");
        
        // Regular table order
        Table regularTable = restaurant.findAvailableTable(2);
        regularTable.setStatus(TableStatus.OCCUPIED);
        System.out.println("Regular table " + regularTable.getTableId() + " places order at 12:00 PM");
        List<String> regularItems = Arrays.asList("M01", "B01");
        Order regularOrder = restaurant.placeOrder(regularTable, waiter1, regularItems);
        
        // VIP table order (placed after regular)
        Table vipTable = null;
        for (Table t : restaurant.getTables()) {
            if (t.getType() == TableType.VIP && t.isAvailable()) {
                vipTable = t;
                break;
            }
        }
        
        if (vipTable != null) {
            vipTable.setStatus(TableStatus.OCCUPIED);
            System.out.println("\nVIP table " + vipTable.getTableId() + " places order at 12:01 PM");
            List<String> vipItems = Arrays.asList("M02", "B02");
            Order vipOrder = restaurant.placeOrder(vipTable, waiter2, vipItems);
            
            System.out.println("\n--- Kitchen Processing (VIP gets priority) ---");
            System.out.println("Processing VIP order first...");
            restaurant.processOrderInKitchen(vipOrder);
            restaurant.completeOrderInKitchen(vipOrder);
            restaurant.serveOrder(vipOrder);
            
            System.out.println("\nProcessing regular order...");
            restaurant.processOrderInKitchen(regularOrder);
            restaurant.completeOrderInKitchen(regularOrder);
            restaurant.serveOrder(regularOrder);
            
            // Cleanup
            restaurant.generateBill(vipOrder);
            restaurant.generateBill(regularOrder);
            restaurant.releaseTable(vipTable);
            restaurant.releaseTable(regularTable);
            vipTable.markAvailable();
            regularTable.markAvailable();
        }
    }
    
    private static void printFinalSummary(Restaurant restaurant, Waiter waiter1, 
                                         Waiter waiter2, Chef chef1, Chef chef2) {
        System.out.println("\n========================================");
        System.out.println("  DEMO COMPLETE!");
        System.out.println("========================================\n");
        
        System.out.println("Design Patterns Demonstrated:");
        System.out.println("✓ Observer Pattern - Order status notifications to waiters and chefs");
        System.out.println("✓ Strategy Pattern - Dynamic pricing (Regular, Happy Hour, Weekend)");
        System.out.println("✓ State Pattern - Order state transitions (Placed → Preparing → Ready → Served)");
        System.out.println("✓ Singleton Pattern - Single Restaurant instance");
        
        System.out.println("\nStaff Performance:");
        System.out.println("- " + waiter1.getName() + ": " + waiter1.getOrdersServed() + " orders served");
        System.out.println("- " + waiter2.getName() + ": " + waiter2.getOrdersServed() + " orders served");
        System.out.println("- " + chef1.getName() + ": " + chef1.getDishesCooked() + " dishes cooked");
        System.out.println("- " + chef2.getName() + ": " + chef2.getDishesCooked() + " dishes cooked");
        
        System.out.println("\nKey Features Demonstrated:");
        System.out.println("✓ Table reservation and management");
        System.out.println("✓ Walk-in customer handling");
        System.out.println("✓ Menu and order management");
        System.out.println("✓ Kitchen operations with priority queue (VIP priority)");
        System.out.println("✓ Real-time order status notifications");
        System.out.println("✓ Dynamic pricing strategies");
        System.out.println("✓ Bill generation and splitting");
        System.out.println("✓ Edge case handling (no available tables)");
        
        System.out.println("\n========================================");
        System.out.println("  Thank you for dining with us!");
        System.out.println("========================================\n");
    }
}


