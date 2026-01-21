import model.*;
import service.*;
import enums.*;
import strategy.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  🚗 RIDE SHARING SYSTEM DEMO 🚗");
        System.out.println("========================================\n");
        
        // Initialize service
        RideService service = RideService.getInstance();
        
        // Create locations
        Location loc1 = new Location(37.7749, -122.4194, "Downtown SF");
        Location loc2 = new Location(37.7849, -122.4094, "Mission District");
        Location loc3 = new Location(37.7949, -122.3994, "Castro");
        Location loc4 = new Location(37.8049, -122.3894, "Haight-Ashbury");
        Location loc5 = new Location(37.7649, -122.4294, "SoMa");
        
        // Create riders
        Rider alice = new Rider("R001", "Alice Johnson", "+1-555-0101", PaymentMethod.CREDIT_CARD);
        Rider bob = new Rider("R002", "Bob Smith", "+1-555-0102", PaymentMethod.WALLET);
        Rider charlie = new Rider("R003", "Charlie Brown", "+1-555-0103", PaymentMethod.CASH);
        
        service.registerRider(alice);
        service.registerRider(bob);
        service.registerRider(charlie);
        
        // Create vehicles
        Vehicle sedan1 = new Vehicle("V001", VehicleType.SEDAN, "ABC-123", "Toyota Camry", "Black");
        Vehicle suv1 = new Vehicle("V002", VehicleType.SUV, "XYZ-789", "Honda CR-V", "White");
        Vehicle luxury1 = new Vehicle("V003", VehicleType.LUXURY_SEDAN, "LUX-001", "BMW 7 Series", "Silver");
        Vehicle sedan2 = new Vehicle("V004", VehicleType.SEDAN, "DEF-456", "Honda Accord", "Blue");
        
        // Create drivers
        Driver driver1 = new Driver("D001", "David Wilson", "+1-555-0201", sedan1, loc1);
        Driver driver2 = new Driver("D002", "Emma Davis", "+1-555-0202", suv1, loc2);
        Driver driver3 = new Driver("D003", "Frank Miller", "+1-555-0203", luxury1, loc3);
        Driver driver4 = new Driver("D004", "Grace Lee", "+1-555-0204", sedan2, loc4);
        
        service.registerDriver(driver1);
        service.registerDriver(driver2);
        service.registerDriver(driver3);
        service.registerDriver(driver4);
        
        System.out.println();
        service.displayAvailableDrivers();
        
        // ====================
        // SCENARIO 1: Complete Ride Flow (Happy Path)
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 1: COMPLETE RIDE FLOW");
        System.out.println("========================================");
        
        Ride ride1 = service.requestRide(alice, loc1, loc2, RideType.ECONOMY);
        
        if (ride1 != null) {
            System.out.println("\n--- Driver accepts ride ---");
            ride1.acceptRide();
            
            System.out.println("\n--- Driver arrives at pickup ---");
            ride1.arriveAtPickup();
            
            System.out.println("\n--- Ride starts ---");
            ride1.startRide();
            
            // Simulate ride duration
            try {
                Thread.sleep(100); // Simulate some time passing
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.println("\n--- Ride completes ---");
            ride1.completeRide();
            
            System.out.println("\n--- Ratings ---");
            alice.updateRating(4.5);
            driver1.updateRating(4.8);
            System.out.println("✅ Rider rated driver: 4.8⭐");
            System.out.println("✅ Driver rated rider: 4.5⭐");
        }
        
        // ====================
        // SCENARIO 2: Premium Ride
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 2: PREMIUM RIDE (SUV)");
        System.out.println("========================================");
        
        Ride ride2 = service.requestRide(bob, loc3, loc4, RideType.PREMIUM);
        
        if (ride2 != null) {
            ride2.acceptRide();
            ride2.startRide();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            ride2.completeRide();
            
            bob.updateRating(4.7);
            driver2.updateRating(4.9);
        }
        
        // ====================
        // SCENARIO 3: Luxury Ride
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 3: LUXURY RIDE");
        System.out.println("========================================");
        
        Ride ride3 = service.requestRide(charlie, loc5, loc1, RideType.LUXURY);
        
        if (ride3 != null) {
            ride3.acceptRide();
            ride3.startRide();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            ride3.completeRide();
        }
        
        // ====================
        // SCENARIO 4: Ride Cancellation by Rider
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 4: RIDE CANCELLATION");
        System.out.println("========================================");
        
        Ride ride4 = service.requestRide(alice, loc2, loc3, RideType.ECONOMY);
        
        if (ride4 != null) {
            ride4.acceptRide();
            
            System.out.println("\n--- Rider cancels ride ---");
            ride4.cancelRide();
            
            System.out.println("✅ Driver " + ride4.getDriver().getName() + 
                             " is now available again");
        }
        
        // ====================
        // SCENARIO 5: Surge Pricing
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 5: SURGE PRICING (2x)");
        System.out.println("========================================");
        
        System.out.println("\n⚡ High demand detected! Activating surge pricing...");
        service.setDefaultPricingStrategy(new SurgePricingStrategy(2.0));
        
        Ride ride5 = service.requestRide(bob, loc1, loc4, RideType.ECONOMY);
        
        if (ride5 != null) {
            ride5.acceptRide();
            ride5.startRide();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            ride5.completeRide();
            System.out.println("💡 Note: Fare is 2x due to surge pricing");
        }
        
        // Reset to base pricing
        service.setDefaultPricingStrategy(new BasePricingStrategy());
        
        // ====================
        // SCENARIO 6: Discount Pricing
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 6: PROMOTIONAL DISCOUNT (30% OFF)");
        System.out.println("========================================");
        
        System.out.println("\n🎉 Promotional offer activated!");
        service.setDefaultPricingStrategy(new DiscountPricingStrategy(0.30));
        
        Ride ride6 = service.requestRide(charlie, loc2, loc5, RideType.PREMIUM);
        
        if (ride6 != null) {
            ride6.acceptRide();
            ride6.startRide();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            ride6.completeRide();
            System.out.println("💡 Note: 30% discount applied");
        }
        
        // Reset to base pricing
        service.setDefaultPricingStrategy(new BasePricingStrategy());
        
        // ====================
        // SCENARIO 7: No Drivers Available
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 7: NO DRIVERS AVAILABLE");
        System.out.println("========================================");
        
        // All drivers are currently available, so let's make them unavailable
        System.out.println("\n🔧 Simulating: All drivers busy...");
        for (Driver d : service.getDrivers()) {
            d.setAvailable(false);
        }
        
        Ride ride7 = service.requestRide(alice, loc1, loc2, RideType.ECONOMY);
        
        if (ride7 == null) {
            System.out.println("✅ System correctly handled no available drivers");
        }
        
        // Make drivers available again
        for (Driver d : service.getDrivers()) {
            d.setAvailable(true);
        }
        
        // ====================
        // SCENARIO 8: Invalid State Transition
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 8: INVALID STATE TRANSITION");
        System.out.println("========================================");
        
        Ride ride8 = service.requestRide(bob, loc3, loc1, RideType.ECONOMY);
        
        if (ride8 != null) {
            System.out.println("\n❌ Attempting to start ride without accepting:");
            try {
                ride8.startRide();
            } catch (IllegalStateException e) {
                System.out.println("   Error: " + e.getMessage());
                System.out.println("   ✅ State validation works correctly!");
            }
            
            // Now do it correctly
            System.out.println("\n✅ Correct flow:");
            ride8.acceptRide();
            ride8.startRide();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            ride8.completeRide();
        }
        
        // ====================
        // SCENARIO 9: Multiple Concurrent Rides
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 9: MULTIPLE CONCURRENT RIDES");
        System.out.println("========================================");
        
        System.out.println("\n📊 Requesting 3 rides simultaneously...\n");
        
        Ride ride9a = service.requestRide(alice, loc1, loc3, RideType.ECONOMY);
        Ride ride9b = service.requestRide(bob, loc2, loc4, RideType.PREMIUM);
        Ride ride9c = service.requestRide(charlie, loc5, loc2, RideType.ECONOMY);
        
        System.out.println("\n✅ All rides matched successfully!");
        
        // Complete all rides
        if (ride9a != null) {
            ride9a.acceptRide();
            ride9a.startRide();
            try { Thread.sleep(50); } catch (InterruptedException e) {}
            ride9a.completeRide();
        }
        
        if (ride9b != null) {
            ride9b.acceptRide();
            ride9b.startRide();
            try { Thread.sleep(50); } catch (InterruptedException e) {}
            ride9b.completeRide();
        }
        
        if (ride9c != null) {
            ride9c.acceptRide();
            ride9c.startRide();
            try { Thread.sleep(50); } catch (InterruptedException e) {}
            ride9c.completeRide();
        }
        
        // ====================
        // Final Statistics
        // ====================
        service.displayStatistics();
        
        System.out.println("========================================");
        System.out.println("  ✅ DEMO COMPLETE!");
        System.out.println("========================================\n");
        
        System.out.println("Design Patterns Demonstrated:");
        System.out.println("✓ State Pattern - Ride state management (REQUESTED → ACCEPTED → STARTED → COMPLETED)");
        System.out.println("✓ Strategy Pattern - Pricing strategies (Base, Surge, Discount)");
        System.out.println("✓ Observer Pattern - Real-time notifications to riders and drivers");
        System.out.println("✓ Factory Pattern - Ride creation with consistent initialization");
        
        System.out.println("\nKey Features Demonstrated:");
        System.out.println("✓ Complete ride lifecycle management");
        System.out.println("✓ Driver-rider matching based on proximity and vehicle type");
        System.out.println("✓ Multiple ride types (Economy, Premium, Luxury)");
        System.out.println("✓ Dynamic pricing (Base, Surge, Discount)");
        System.out.println("✓ State transition validation");
        System.out.println("✓ Real-time notifications (Observer pattern)");
        System.out.println("✓ Ride cancellation handling");
        System.out.println("✓ Driver earnings and commission calculation");
        System.out.println("✓ Rating system for riders and drivers");
        System.out.println("✓ Edge case handling (no drivers, invalid states)");
        
        System.out.println("\nExtensibility:");
        System.out.println("✓ Easy to add new ride types (just add to enum)");
        System.out.println("✓ Easy to add new pricing strategies (implement interface)");
        System.out.println("✓ Easy to add new ride states (implement RideState)");
        System.out.println("✓ Easy to add new notification channels (implement Observer)");
    }
}

