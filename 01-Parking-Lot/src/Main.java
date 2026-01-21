import model.*;
import factory.VehicleFactory;
import service.*;
import strategy.*;
import enums.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  PARKING LOT SYSTEM DEMO");
        System.out.println("========================================\n");
        
        // Initialize parking lot (Singleton)
        ParkingLot parkingLot = ParkingLot.getInstance("City Center Parking");
        
        // Create Floor 1
        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSpot(new ParkingSpot("F1-C1", SpotType.COMPACT, 1));
        floor1.addSpot(new ParkingSpot("F1-C2", SpotType.COMPACT, 1));
        floor1.addSpot(new ParkingSpot("F1-L1", SpotType.LARGE, 1));
        floor1.addSpot(new ParkingSpot("F1-B1", SpotType.BIKE, 1));
        floor1.addSpot(new ParkingSpot("F1-B2", SpotType.BIKE, 1));
        
        // Create Floor 2
        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addSpot(new ParkingSpot("F2-C1", SpotType.COMPACT, 2));
        floor2.addSpot(new ParkingSpot("F2-L1", SpotType.LARGE, 2));
        floor2.addSpot(new ParkingSpot("F2-H1", SpotType.HANDICAPPED, 2));
        
        parkingLot.addFloor(floor1);
        parkingLot.addFloor(floor2);
        
        // Display initial availability
        System.out.println("INITIAL STATE:");
        parkingLot.displayAvailability();
        
        // Create vehicles using Factory Pattern
        System.out.println("\n========================================");
        System.out.println("  CREATING VEHICLES (Factory Pattern)");
        System.out.println("========================================");
        Vehicle car1 = VehicleFactory.createVehicle(VehicleType.CAR, "ABC-123");
        Vehicle bike1 = VehicleFactory.createVehicle(VehicleType.BIKE, "XYZ-789");
        Vehicle truck1 = VehicleFactory.createVehicle(VehicleType.TRUCK, "TRK-001");
        System.out.println("✓ Created Car: " + car1.getLicensePlate());
        System.out.println("✓ Created Bike: " + bike1.getLicensePlate());
        System.out.println("✓ Created Truck: " + truck1.getLicensePlate());
        
        // Park vehicles
        System.out.println("\n========================================");
        System.out.println("  PARKING VEHICLES");
        System.out.println("========================================");
        ParkingTicket ticket1 = parkingLot.parkVehicle(car1);
        ParkingTicket ticket2 = parkingLot.parkVehicle(bike1);
        ParkingTicket ticket3 = parkingLot.parkVehicle(truck1);
        
        // Display availability after parking
        System.out.println("\nAFTER PARKING:");
        parkingLot.displayAvailability();
        
        // Simulate time passing (for fee calculation)
        System.out.println("\n⏰ Simulating 2 seconds of parking time...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Unpark vehicles with different payment methods
        System.out.println("\n========================================");
        System.out.println("  UNPARKING VEHICLES (Strategy Pattern)");
        System.out.println("========================================");
        Payment cashPayment = new CashPayment();
        Payment cardPayment = new CardPayment("**** **** **** 1234");
        
        System.out.println("\nUsing Card Payment:");
        parkingLot.unparkVehicle(ticket1.getTicketId(), cardPayment);
        
        System.out.println("\nUsing Cash Payment:");
        parkingLot.unparkVehicle(ticket2.getTicketId(), cashPayment);
        
        // Change pricing strategy to Flat Rate
        System.out.println("\n========================================");
        System.out.println("  CHANGING PRICING STRATEGY");
        System.out.println("========================================");
        System.out.println("Switching from Hourly to Flat Rate ($50)");
        parkingLot.setPricingStrategy(new FlatPricingStrategy(50.0));
        
        System.out.println("\nUnparking Truck with new pricing:");
        parkingLot.unparkVehicle(ticket3.getTicketId(), cashPayment);
        
        // Display final availability
        System.out.println("\nFINAL STATE:");
        parkingLot.displayAvailability();
        
        // Test edge case: No available spots
        System.out.println("\n========================================");
        System.out.println("  TESTING EDGE CASE");
        System.out.println("========================================");
        
        // Fill all car spots
        Vehicle car2 = VehicleFactory.createVehicle(VehicleType.CAR, "CAR-002");
        Vehicle car3 = VehicleFactory.createVehicle(VehicleType.CAR, "CAR-003");
        Vehicle car4 = VehicleFactory.createVehicle(VehicleType.CAR, "CAR-004");
        Vehicle car5 = VehicleFactory.createVehicle(VehicleType.CAR, "CAR-005");
        
        parkingLot.parkVehicle(car2);
        parkingLot.parkVehicle(car3);
        parkingLot.parkVehicle(car4);
        parkingLot.parkVehicle(car5);
        
        System.out.println("\nTrying to park one more car (should fail):");
        try {
            Vehicle car6 = VehicleFactory.createVehicle(VehicleType.CAR, "CAR-006");
            parkingLot.parkVehicle(car6);
        } catch (RuntimeException e) {
            System.out.println("❌ Error: " + e.getMessage());
            System.out.println("✓ Edge case handled correctly!");
        }
        
        System.out.println("\n========================================");
        System.out.println("  DEMO COMPLETE!");
        System.out.println("========================================");
        System.out.println("\nDesign Patterns Demonstrated:");
        System.out.println("✓ Factory Pattern - VehicleFactory");
        System.out.println("✓ Singleton Pattern - ParkingLot");
        System.out.println("✓ Strategy Pattern - PricingStrategy");
    }
}


