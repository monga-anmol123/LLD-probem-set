import model.*;
import enums.*;
import factory.VehicleFactory;
import service.RentalSystem;
import strategy.*;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("          🚗 CAR RENTAL SYSTEM DEMO 🚗");
        System.out.println("════════════════════════════════════════════════════════════\n");
        
        // Initialize system
        RentalSystem system = RentalSystem.getInstance("Premium Car Rentals");
        
        // Setup locations
        setupLocations(system);
        
        // Setup vehicles
        setupVehicles(system);
        
        // Setup customers
        setupCustomers(system);
        
        // Display initial state
        system.displayLocations();
        system.displayAvailableVehicles();
        
        // Run scenarios
        scenario1_BasicRentalFlow(system);
        scenario2_ReservationFlow(system);
        scenario3_MembershipDiscounts(system);
        scenario4_LateReturnPenalty(system);
        scenario5_OneWayRental(system);
        scenario6_AddOnsAndInsurance(system);
        scenario7_VehicleNotAvailable(system);
        
        // Final summary
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("          ✅ DEMO COMPLETE!");
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("\n🎨 Design Patterns Demonstrated:");
        System.out.println("  ✓ Factory Pattern - Vehicle creation");
        System.out.println("  ✓ Strategy Pattern - Pricing strategies (Standard, Seasonal)");
        System.out.println("  ✓ Observer Pattern - Customer notifications");
        System.out.println("  ✓ Singleton Pattern - RentalSystem instance");
        System.out.println("\n📊 Features Demonstrated:");
        System.out.println("  ✓ Basic rental flow (pickup and return)");
        System.out.println("  ✓ Reservation system with notifications");
        System.out.println("  ✓ Membership tiers with discounts");
        System.out.println("  ✓ Late return penalties");
        System.out.println("  ✓ One-way rentals with location transfer");
        System.out.println("  ✓ Insurance and add-ons (GPS)");
        System.out.println("  ✓ Detailed invoice generation");
        System.out.println("  ✓ Vehicle availability management");
        System.out.println("════════════════════════════════════════════════════════════\n");
    }
    
    private static void setupLocations(RentalSystem system) {
        Location loc1 = new Location("LOC-001", "New York Downtown", "123 Broadway, NY");
        Location loc2 = new Location("LOC-002", "Boston Central", "456 Main St, Boston");
        Location loc3 = new Location("LOC-003", "Philadelphia Airport", "789 Airport Rd, Philly");
        
        system.addLocation(loc1);
        system.addLocation(loc2);
        system.addLocation(loc3);
    }
    
    private static void setupVehicles(RentalSystem system) {
        System.out.println("\n--- Setting up vehicle fleet ---");
        
        // Economy cars
        Vehicle v1 = VehicleFactory.createVehicle(VehicleType.ECONOMY, "VIN-001", "Toyota", "Corolla", 2023);
        Vehicle v2 = VehicleFactory.createVehicle(VehicleType.ECONOMY, "VIN-002", "Honda", "Civic", 2023);
        
        // Sedans
        Vehicle v3 = VehicleFactory.createVehicle(VehicleType.SEDAN, "VIN-003", "Honda", "Accord", 2023);
        Vehicle v4 = VehicleFactory.createVehicle(VehicleType.SEDAN, "VIN-004", "Toyota", "Camry", 2023);
        
        // SUVs
        Vehicle v5 = VehicleFactory.createVehicle(VehicleType.SUV, "VIN-005", "Ford", "Explorer", 2023);
        Vehicle v6 = VehicleFactory.createVehicle(VehicleType.SUV, "VIN-006", "Jeep", "Grand Cherokee", 2023);
        
        // Luxury cars
        Vehicle v7 = VehicleFactory.createVehicle(VehicleType.LUXURY, "VIN-007", "BMW", "7 Series", 2024);
        Vehicle v8 = VehicleFactory.createVehicle(VehicleType.LUXURY, "VIN-008", "Mercedes", "S-Class", 2024);
        
        // Add vehicles to locations
        system.addVehicle(v1, "LOC-001");
        system.addVehicle(v2, "LOC-001");
        system.addVehicle(v3, "LOC-001");
        system.addVehicle(v4, "LOC-002");
        system.addVehicle(v5, "LOC-002");
        system.addVehicle(v6, "LOC-002");
        system.addVehicle(v7, "LOC-003");
        system.addVehicle(v8, "LOC-003");
        
        // Set one vehicle under maintenance for testing
        v2.setUnderMaintenance();
        System.out.println("⚠️  Vehicle VIN-002 set to UNDER_MAINTENANCE for testing");
    }
    
    private static void setupCustomers(RentalSystem system) {
        System.out.println("\n--- Registering customers ---");
        
        Customer c1 = new Customer("CUST-001", "John Doe", "john@email.com", "555-0001", "DL-12345", MembershipTier.REGULAR);
        Customer c2 = new Customer("CUST-002", "Jane Smith", "jane@email.com", "555-0002", "DL-67890", MembershipTier.SILVER);
        Customer c3 = new Customer("CUST-003", "Bob Johnson", "bob@email.com", "555-0003", "DL-11111", MembershipTier.GOLD);
        Customer c4 = new Customer("CUST-004", "Alice Brown", "alice@email.com", "555-0004", "DL-22222", MembershipTier.REGULAR);
        
        system.registerCustomer(c1);
        system.registerCustomer(c2);
        system.registerCustomer(c3);
        system.registerCustomer(c4);
    }
    
    private static void scenario1_BasicRentalFlow(RentalSystem system) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 1: Basic Rental Flow");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 John Doe (Regular member) rents Toyota Corolla for 3 days");
        
        LocalDate pickupDate = LocalDate.of(2024, 1, 10);
        LocalDate returnDate = LocalDate.of(2024, 1, 13);
        
        // Create rental
        Rental rental = system.createRental("CUST-001", "VIN-001", pickupDate, returnDate, "LOC-001", "LOC-001");
        
        System.out.println("\n✓ Rental created successfully!");
        System.out.println("  Rental ID: " + rental.getRentalId());
        System.out.println("  Vehicle: " + rental.getVehicle().getMake() + " " + rental.getVehicle().getModel());
        System.out.println("  Duration: " + rental.getRentalDays() + " days");
        System.out.println("  Daily Rate: $" + rental.getVehicle().getDailyRate());
        
        // Return vehicle
        System.out.println("\n🔄 Returning vehicle on time...");
        Invoice invoice = system.returnVehicle(rental.getRentalId(), returnDate);
        
        System.out.println(invoice.generateBreakdown());
        
        System.out.println("✓ Expected: 3 days × $50/day = $150 (no discount for regular member)");
        System.out.println("✓ Total with tax: $" + String.format("%.2f", invoice.getTotalAmount()));
    }
    
    private static void scenario2_ReservationFlow(RentalSystem system) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 2: Reservation Flow with Notifications");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Alice Brown wants to reserve an SUV for next week");
        
        LocalDate futurePickup = LocalDate.of(2024, 1, 20);
        LocalDate futureReturn = LocalDate.of(2024, 1, 25);
        
        // Create reservation
        Reservation reservation = system.createReservation("CUST-004", VehicleType.SUV, futurePickup, futureReturn);
        
        System.out.println("\n✓ Reservation created!");
        System.out.println("  Reservation ID: " + reservation.getReservationId());
        System.out.println("  Vehicle Type: " + reservation.getVehicleType());
        System.out.println("  Pickup Date: " + reservation.getPickupDate());
        System.out.println("  Status: " + reservation.getStatus());
        
        System.out.println("\n🔔 When an SUV becomes available, Alice will be notified!");
        System.out.println("  (Observer pattern in action)");
    }
    
    private static void scenario3_MembershipDiscounts(RentalSystem system) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 3: Membership Tier Discounts");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Bob Johnson (GOLD member - 15% discount) rents BMW 7 Series for 5 days");
        
        LocalDate pickupDate = LocalDate.of(2024, 1, 15);
        LocalDate returnDate = LocalDate.of(2024, 1, 20);
        
        // Create rental
        Rental rental = system.createRental("CUST-003", "VIN-007", pickupDate, returnDate, "LOC-003", "LOC-003");
        
        System.out.println("\n✓ Rental created!");
        System.out.println("  Vehicle: " + rental.getVehicle().getMake() + " " + rental.getVehicle().getModel());
        System.out.println("  Daily Rate: $" + rental.getVehicle().getDailyRate());
        System.out.println("  Duration: " + rental.getRentalDays() + " days");
        System.out.println("  Membership: " + rental.getCustomer().getMembershipTier() + 
                         " (" + (int)(rental.getCustomer().getMembershipDiscount() * 100) + "% discount)");
        
        // Return vehicle
        System.out.println("\n🔄 Returning vehicle...");
        Invoice invoice = system.returnVehicle(rental.getRentalId(), returnDate);
        
        System.out.println(invoice.generateBreakdown());
        
        System.out.println("✓ Base: 5 days × $200/day = $1000");
        System.out.println("✓ Gold discount (15%): -$150");
        System.out.println("✓ Subtotal: $850 + tax");
    }
    
    private static void scenario4_LateReturnPenalty(RentalSystem system) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 4: Late Return Penalty");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Jane Smith rents Honda Accord for 3 days but returns 2 days late");
        
        LocalDate pickupDate = LocalDate.of(2024, 1, 10);
        LocalDate plannedReturn = LocalDate.of(2024, 1, 13);
        LocalDate actualReturn = LocalDate.of(2024, 1, 15); // 2 days late
        
        // Create rental
        Rental rental = system.createRental("CUST-002", "VIN-003", pickupDate, plannedReturn, "LOC-001", "LOC-001");
        
        System.out.println("\n✓ Rental created!");
        System.out.println("  Planned return: " + plannedReturn);
        System.out.println("  Actual return: " + actualReturn);
        System.out.println("  ⚠️  Late by: 2 days");
        
        // Return vehicle late
        System.out.println("\n🔄 Processing late return...");
        Invoice invoice = system.returnVehicle(rental.getRentalId(), actualReturn);
        
        System.out.println(invoice.generateBreakdown());
        
        System.out.println("✓ Base cost: 5 days × $70/day = $350");
        System.out.println("✓ Silver discount (10%): -$35");
        System.out.println("✓ Late penalty: 2 days × $30/day = $60");
        System.out.println("✓ Total: $375 + tax");
    }
    
    private static void scenario5_OneWayRental(RentalSystem system) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 5: One-Way Rental (Different Pickup/Return Locations)");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 John Doe rents Toyota Camry in Boston, returns in Philadelphia");
        
        LocalDate pickupDate = LocalDate.of(2024, 1, 20);
        LocalDate returnDate = LocalDate.of(2024, 1, 22);
        
        // Create one-way rental
        Rental rental = system.createRental("CUST-001", "VIN-004", pickupDate, returnDate, "LOC-002", "LOC-003");
        
        System.out.println("\n✓ One-way rental created!");
        System.out.println("  Pickup: Boston (LOC-002)");
        System.out.println("  Return: Philadelphia (LOC-003)");
        System.out.println("  Duration: " + rental.getRentalDays() + " days");
        System.out.println("  One-way fee: $75.00");
        
        // Return vehicle
        System.out.println("\n🔄 Returning vehicle at different location...");
        Invoice invoice = system.returnVehicle(rental.getRentalId(), returnDate);
        
        System.out.println(invoice.generateBreakdown());
        
        System.out.println("✓ Base: 2 days × $70/day = $140");
        System.out.println("✓ One-way fee: $75");
        System.out.println("✓ Total: $215 + tax");
        System.out.println("✓ Vehicle now at Philadelphia location");
    }
    
    private static void scenario6_AddOnsAndInsurance(RentalSystem system) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 6: Rental with Insurance and Add-ons");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Jane Smith rents Ford Explorer for 4 days with insurance and GPS");
        
        LocalDate pickupDate = LocalDate.of(2024, 1, 25);
        LocalDate returnDate = LocalDate.of(2024, 1, 29);
        
        // Create rental
        Rental rental = system.createRental("CUST-002", "VIN-005", pickupDate, returnDate, "LOC-002", "LOC-002");
        
        // Add insurance and GPS
        rental.addInsurance();
        rental.addGPS();
        
        System.out.println("\n✓ Rental created with add-ons!");
        System.out.println("  Vehicle: " + rental.getVehicle().getMake() + " " + rental.getVehicle().getModel());
        System.out.println("  Duration: " + rental.getRentalDays() + " days");
        System.out.println("  ✓ Collision Insurance: $15/day");
        System.out.println("  ✓ GPS Navigation: $10/day");
        
        // Return vehicle
        System.out.println("\n🔄 Returning vehicle...");
        Invoice invoice = system.returnVehicle(rental.getRentalId(), returnDate);
        
        System.out.println(invoice.generateBreakdown());
        
        System.out.println("✓ Base: 4 days × $100/day = $400");
        System.out.println("✓ Silver discount (10%): -$40");
        System.out.println("✓ Insurance: 4 days × $15/day = $60");
        System.out.println("✓ GPS: 4 days × $10/day = $40");
        System.out.println("✓ Total: $460 + tax");
    }
    
    private static void scenario7_VehicleNotAvailable(RentalSystem system) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 7: Vehicle Not Available (Error Handling)");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Attempting to rent vehicle VIN-002 (Under Maintenance)");
        
        LocalDate pickupDate = LocalDate.of(2024, 2, 1);
        LocalDate returnDate = LocalDate.of(2024, 2, 5);
        
        try {
            Rental rental = system.createRental("CUST-001", "VIN-002", pickupDate, returnDate, "LOC-001", "LOC-001");
            System.out.println("✗ Should have thrown exception!");
        } catch (IllegalStateException e) {
            System.out.println("\n✓ Exception caught correctly: " + e.getMessage());
            System.out.println("\n💡 Suggesting alternative vehicles...");
            
            List<Vehicle> alternatives = system.searchAvailableVehicles(VehicleType.ECONOMY, "LOC-001");
            if (!alternatives.isEmpty()) {
                System.out.println("\n📋 Available ECONOMY vehicles at New York Downtown:");
                for (Vehicle v : alternatives) {
                    System.out.println("  - " + v);
                }
                
                System.out.println("\n✓ Customer can choose alternative: " + alternatives.get(0).getVin());
            }
        }
        
        System.out.println("\n✓ System gracefully handled unavailable vehicle");
    }
}


