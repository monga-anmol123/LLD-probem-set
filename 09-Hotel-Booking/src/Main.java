import service.HotelBookingSystem;
import model.*;
import enums.*;
import strategy.*;
import observer.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  HOTEL BOOKING SYSTEM DEMO");
        System.out.println("========================================\n");
        
        HotelBookingSystem system = HotelBookingSystem.getInstance();
        
        // Setup: Add Rooms
        System.out.println("========================================");
        System.out.println("  SETUP: Adding Rooms");
        System.out.println("========================================\n");
        
        system.addRoom(new Room("101", RoomType.SINGLE, 1));
        system.addRoom(new Room("102", RoomType.SINGLE, 1));
        system.addRoom(new Room("201", RoomType.DOUBLE, 2));
        system.addRoom(new Room("202", RoomType.DOUBLE, 2));
        system.addRoom(new Room("301", RoomType.SUITE, 3));
        system.addRoom(new Room("401", RoomType.DELUXE, 4));
        
        System.out.println("✓ 6 rooms added to system\n");
        
        // Setup: Register Guests
        System.out.println("========================================");
        System.out.println("  SETUP: Registering Guests");
        System.out.println("========================================\n");
        
        Guest guest1 = new Guest("G001", "Alice Johnson", "alice@email.com", "+1-555-0101");
        Guest guest2 = new Guest("G002", "Bob Smith", "bob@email.com", "+1-555-0102");
        Guest guest3 = new Guest("G003", "Carol White", "carol@email.com", "+1-555-0103");
        
        system.registerGuest(guest1);
        system.registerGuest(guest2);
        system.registerGuest(guest3);
        
        System.out.println("✓ 3 guests registered\n");
        
        // Scenario 1: Basic Booking with Base Pricing
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: Basic Booking");
        System.out.println("========================================\n");
        
        LocalDate checkIn1 = LocalDate.now().plusDays(5);
        LocalDate checkOut1 = checkIn1.plusDays(3);
        
        Booking booking1 = system.createBooking("G001", "201", checkIn1, checkOut1);
        
        // Scenario 2: Weekend Pricing Strategy
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 2: Weekend Pricing");
        System.out.println("========================================\n");
        
        // Find next Friday
        LocalDate checkIn2 = LocalDate.now().plusDays(1);
        while (checkIn2.getDayOfWeek() != java.time.DayOfWeek.FRIDAY) {
            checkIn2 = checkIn2.plusDays(1);
        }
        LocalDate checkOut2 = checkIn2.plusDays(2); // Friday to Sunday
        
        Booking booking2 = system.createBooking("G002", "301", checkIn2, checkOut2, 
            new WeekendPricingStrategy());
        
        // Scenario 3: Seasonal Pricing Strategy
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 3: Seasonal Pricing");
        System.out.println("========================================\n");
        
        LocalDate checkIn3 = LocalDate.of(2026, 7, 15); // July (peak season)
        LocalDate checkOut3 = checkIn3.plusDays(5);
        
        Booking booking3 = system.createBooking("G003", "401", checkIn3, checkOut3, 
            new SeasonalPricingStrategy());
        
        // Scenario 4: Loyalty Pricing
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 4: Loyalty Pricing");
        System.out.println("========================================\n");
        
        // Give guest1 loyalty points
        guest1.addLoyaltyPoints(100);
        System.out.println("Guest " + guest1.getName() + " now has " + 
                         guest1.getLoyaltyPoints() + " loyalty points");
        System.out.println("Loyalty status: " + guest1.hasLoyaltyStatus() + "\n");
        
        LocalDate checkIn4 = LocalDate.now().plusDays(10);
        LocalDate checkOut4 = checkIn4.plusDays(2);
        
        Booking booking4 = system.createBooking("G001", "102", checkIn4, checkOut4, 
            new LoyaltyPricingStrategy(guest1));
        
        // Scenario 5: Search Available Rooms
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 5: Search Available Rooms");
        System.out.println("========================================\n");
        
        LocalDate searchCheckIn = LocalDate.now().plusDays(5);
        LocalDate searchCheckOut = searchCheckIn.plusDays(3);
        
        system.displayAvailableRooms(searchCheckIn, searchCheckOut);
        
        System.out.println("Searching for DOUBLE rooms only:");
        List<Room> doubleRooms = system.searchAvailableRooms(searchCheckIn, searchCheckOut, RoomType.DOUBLE);
        for (Room room : doubleRooms) {
            System.out.println("  " + room);
        }
        
        // Scenario 6: Observer Pattern - Notifications
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 6: Booking Notifications");
        System.out.println("========================================\n");
        
        LocalDate checkIn6 = LocalDate.now().plusDays(15);
        LocalDate checkOut6 = checkIn6.plusDays(2);
        
        Booking booking6 = system.createBooking("G002", "202", checkIn6, checkOut6);
        
        // Add observers
        booking6.addObserver(new EmailNotifier());
        booking6.addObserver(new SMSNotifier());
        
        System.out.println("\nSimulating booking status changes:");
        booking6.setPaymentStatus(PaymentStatus.PAID);
        
        // Scenario 7: Double Booking Prevention
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 7: Double Booking Prevention");
        System.out.println("========================================\n");
        
        System.out.println("Attempting to book Room 201 for overlapping dates:");
        try {
            system.createBooking("G003", "201", checkIn1, checkOut1);
        } catch (IllegalStateException e) {
            System.out.println("✓ Booking prevented: " + e.getMessage());
        }
        
        System.out.println("\nAttempting to book Room 201 for non-overlapping dates:");
        LocalDate checkIn7 = checkOut1.plusDays(1);
        LocalDate checkOut7 = checkIn7.plusDays(2);
        Booking booking7 = system.createBooking("G003", "201", checkIn7, checkOut7);
        
        // Scenario 8: Booking Cancellation
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 8: Booking Cancellation");
        System.out.println("========================================\n");
        
        System.out.println("Cancelling booking: " + booking7.getBookingId());
        system.cancelBooking(booking7.getBookingId());
        
        // Scenario 9: Check-in and Check-out
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 9: Check-in/Check-out");
        System.out.println("========================================\n");
        
        System.out.println("Check-in for booking: " + booking1.getBookingId());
        system.checkIn(booking1.getBookingId());
        
        System.out.println("\nCheck-out for booking: " + booking1.getBookingId());
        system.checkOut(booking1.getBookingId());
        
        // Scenario 10: Guest Booking History
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 10: Guest Booking History");
        System.out.println("========================================\n");
        
        System.out.println("All bookings for " + guest1.getName() + ":");
        List<Booking> guestBookings = system.getGuestBookings("G001");
        for (Booking booking : guestBookings) {
            System.out.println("  " + booking);
        }
        
        System.out.println("\nTotal loyalty points: " + guest1.getLoyaltyPoints());
        
        // Final Statistics
        System.out.println("\n========================================");
        System.out.println("  FINAL STATISTICS");
        System.out.println("========================================");
        
        system.displaySystemStats();
        
        // Summary
        System.out.println("\n========================================");
        System.out.println("  DEMO COMPLETE!");
        System.out.println("========================================");
        System.out.println("\nDesign Patterns Demonstrated:");
        System.out.println("✓ Strategy Pattern: Multiple pricing strategies");
        System.out.println("  - BasePricingStrategy");
        System.out.println("  - WeekendPricingStrategy (1.5x on Fri/Sat)");
        System.out.println("  - SeasonalPricingStrategy (2x peak, 1.5x high)");
        System.out.println("  - LoyaltyPricingStrategy (15% off)");
        System.out.println("✓ Observer Pattern: Booking notifications");
        System.out.println("  - EmailNotifier");
        System.out.println("  - SMSNotifier");
        System.out.println("✓ Singleton Pattern: HotelBookingSystem");
        System.out.println("\nKey Features Demonstrated:");
        System.out.println("✓ Room booking with date validation");
        System.out.println("✓ Multiple pricing strategies");
        System.out.println("✓ Room availability search");
        System.out.println("✓ Double booking prevention (concurrency safe)");
        System.out.println("✓ Booking cancellation with refund policy");
        System.out.println("✓ Check-in/check-out process");
        System.out.println("✓ Guest loyalty program");
        System.out.println("✓ Real-time notifications");
        System.out.println("✓ Booking history tracking");
        System.out.println("✓ System statistics");
    }
}
