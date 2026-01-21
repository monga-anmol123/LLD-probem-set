import model.*;
import service.BookingSystem;
import factory.SeatFactory;
import observer.EmailNotification;
import observer.SMSNotification;
import strategy.*;
import enums.SeatType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Demo application for Movie Ticket Booking System
 * Demonstrates all design patterns and features
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  MOVIE TICKET BOOKING SYSTEM DEMO");
        System.out.println("========================================\n");

        // Get singleton instance
        BookingSystem bookingSystem = BookingSystem.getInstance();

        // Add observers for notifications
        bookingSystem.addObserver(new EmailNotification());
        bookingSystem.addObserver(new SMSNotification());

        // Setup: Create theaters, screens, movies, and shows
        setupSystem(bookingSystem);

        // Scenario 1: Complete Booking Flow
        scenario1CompleteBookingFlow(bookingSystem);

        // Scenario 2: Concurrent Booking Conflict
        scenario2ConcurrentBooking(bookingSystem);

        // Scenario 3: Booking Cancellation
        scenario3BookingCancellation(bookingSystem);

        // Scenario 4: Dynamic Pricing Strategies
        scenario4DynamicPricing(bookingSystem);

        // Scenario 5: Full Show Handling
        scenario5FullShow(bookingSystem);

        // Scenario 6: Discount Application
        scenario6DiscountApplication(bookingSystem);

        // Final Summary
        System.out.println("\n========================================");
        System.out.println("  DEMO COMPLETE!");
        System.out.println("========================================");
        System.out.println("\nDesign Patterns Demonstrated:");
        System.out.println("✓ Factory Pattern - SeatFactory, PaymentFactory");
        System.out.println("✓ Observer Pattern - Email & SMS Notifications");
        System.out.println("✓ State Pattern - Booking state transitions");
        System.out.println("✓ Strategy Pattern - Multiple pricing strategies");
        System.out.println("✓ Singleton Pattern - BookingSystem");
        System.out.println("\nKey Features:");
        System.out.println("✓ Seat locking mechanism");
        System.out.println("✓ Concurrent booking handling");
        System.out.println("✓ Dynamic pricing");
        System.out.println("✓ Booking cancellation with refund");
        System.out.println("✓ Multi-theater support");
        System.out.println("✓ Real-time seat availability");
    }

    private static void setupSystem(BookingSystem bookingSystem) {
        System.out.println("========================================");
        System.out.println("  SYSTEM SETUP");
        System.out.println("========================================\n");

        // Create theater
        Theater pvr = new Theater("TH001", "PVR Cinemas", "Mumbai", "Phoenix Mall");
        bookingSystem.addTheater(pvr);
        System.out.println("✓ Created theater: " + pvr.getName());

        // Create screen
        Screen screen1 = new Screen("SCR001", "Screen 1", pvr);
        pvr.addScreen(screen1);
        System.out.println("✓ Created screen: " + screen1.getName());

        // Add seats using Factory Pattern
        System.out.println("✓ Adding seats...");
        String[] rows = {"A", "B", "C", "D", "E"};
        for (String row : rows) {
            for (int col = 1; col <= 10; col++) {
                String seatId = row + col;
                Seat seat;
                
                if (row.equals("A")) {
                    // First row - VIP seats
                    seat = SeatFactory.createVIPSeat(seatId, row, col);
                } else if (row.equals("B") || row.equals("C")) {
                    // Middle rows - Premium seats
                    seat = SeatFactory.createPremiumSeat(seatId, row, col);
                } else if (col == 1) {
                    // First column - Wheelchair accessible
                    seat = SeatFactory.createWheelchairSeat(seatId, row, col);
                } else {
                    // Rest - Regular seats
                    seat = SeatFactory.createRegularSeat(seatId, row, col);
                }
                
                screen1.addSeat(seat);
            }
        }
        System.out.println("  Total seats: " + screen1.getTotalSeats());

        // Create movies
        Movie inception = new Movie("MOV001", "Inception", "Sci-Fi", 148, "English", "PG-13");
        Movie avengers = new Movie("MOV002", "Avengers: Endgame", "Action", 181, "English", "PG-13");
        bookingSystem.addMovie(inception);
        bookingSystem.addMovie(avengers);
        System.out.println("✓ Added movies: " + inception.getTitle() + ", " + avengers.getTitle());

        // Create shows
        LocalDateTime tomorrow7PM = LocalDateTime.now().plusDays(1).withHour(19).withMinute(0);
        LocalDateTime tomorrow2PM = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
        LocalDateTime tomorrow10PM = LocalDateTime.now().plusDays(1).withHour(22).withMinute(0);
        
        String show1 = bookingSystem.createShow(inception, screen1, tomorrow7PM);
        String show2 = bookingSystem.createShow(avengers, screen1, tomorrow2PM);
        String show3 = bookingSystem.createShow(inception, screen1, tomorrow10PM);
        
        System.out.println("✓ Created shows:");
        System.out.println("  - " + show1 + " (Inception 7 PM)");
        System.out.println("  - " + show2 + " (Avengers 2 PM)");
        System.out.println("  - " + show3 + " (Inception 10 PM)");

        // Create users
        User user1 = new User("U001", "John Doe", "john@example.com", "+1234567890");
        User user2 = new User("U002", "Jane Smith", "jane@example.com", "+0987654321");
        User user3 = new User("U003", "Bob Wilson", "bob@example.com", "+1122334455");
        bookingSystem.addUser(user1);
        bookingSystem.addUser(user2);
        bookingSystem.addUser(user3);
        System.out.println("✓ Created users: " + user1.getName() + ", " + user2.getName() + 
                         ", " + user3.getName());

        System.out.println("\n✅ System setup complete!\n");
    }

    private static void scenario1CompleteBookingFlow(BookingSystem bookingSystem) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: Complete Booking Flow");
        System.out.println("========================================\n");

        User user = bookingSystem.getUser("U001");
        Show show = bookingSystem.getShow("SHOW00001");

        System.out.println("User: " + user.getName());
        System.out.println("Movie: " + show.getMovie().getTitle());
        System.out.println("Show Time: " + show.getStartTime());

        // Display initial availability
        bookingSystem.displayShowAvailability("SHOW00001");

        // Select seats
        List<String> selectedSeats = Arrays.asList("B5", "B6", "B7");
        System.out.println("\n📍 User selects seats: " + selectedSeats);

        // Create booking
        Booking booking = bookingSystem.createBooking(user, show, selectedSeats);
        if (booking != null) {
            System.out.println("✓ Booking created: " + booking.getBookingId());
            System.out.println("  Status: " + booking.getStatus());
            System.out.println("  Seats locked for 10 minutes");
            System.out.println("  Total Amount: $" + String.format("%.2f", booking.getTotalAmount()));

            // Apply discount
            System.out.println("\n💰 Applying discount code 'FIRST50' (10% off)");
            booking.applyDiscount(10);
            System.out.println("  New Amount: $" + String.format("%.2f", booking.getTotalAmount()));

            // Process payment
            System.out.println("\n💳 Processing payment...");
            boolean paymentSuccess = bookingSystem.confirmBooking(booking.getBookingId(), "CREDIT_CARD");
            
            if (paymentSuccess) {
                System.out.println("✅ Payment successful!");
                bookingSystem.displayBookingSummary(booking.getBookingId());
            }
        }

        System.out.println("\n");
    }

    private static void scenario2ConcurrentBooking(BookingSystem bookingSystem) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 2: Concurrent Booking Conflict");
        System.out.println("========================================\n");

        User user1 = bookingSystem.getUser("U001");
        User user2 = bookingSystem.getUser("U002");
        Show show = bookingSystem.getShow("SHOW00001");

        System.out.println("Two users trying to book the same seat...\n");

        // User 1 tries to book seat A5
        List<String> seats1 = Arrays.asList("A5");
        System.out.println("👤 User 1 (" + user1.getName() + ") selects seat: A5");
        Booking booking1 = bookingSystem.createBooking(user1, show, seats1);
        
        if (booking1 != null) {
            System.out.println("✓ User 1 successfully locked seat A5");
            System.out.println("  Booking ID: " + booking1.getBookingId());
        }

        // User 2 tries to book the same seat
        System.out.println("\n👤 User 2 (" + user2.getName() + ") tries to book seat: A5");
        Booking booking2 = bookingSystem.createBooking(user2, show, seats1);
        
        if (booking2 == null) {
            System.out.println("❌ User 2 failed - Seat already locked by User 1");
        }

        // User 1 completes payment
        System.out.println("\n💳 User 1 completes payment...");
        bookingSystem.confirmBooking(booking1.getBookingId(), "UPI");
        System.out.println("✅ Seat A5 now permanently booked by User 1");

        // User 2 tries again
        System.out.println("\n👤 User 2 tries again...");
        Booking booking3 = bookingSystem.createBooking(user2, show, seats1);
        if (booking3 == null) {
            System.out.println("❌ User 2 failed - Seat already booked");
            System.out.println("💡 Suggesting alternative seats: A6, A7, A8");
        }

        System.out.println("\n");
    }

    private static void scenario3BookingCancellation(BookingSystem bookingSystem) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 3: Booking Cancellation");
        System.out.println("========================================\n");

        User user = bookingSystem.getUser("U003");
        Show show = bookingSystem.getShow("SHOW00002");

        // Create and confirm booking
        List<String> seats = Arrays.asList("C3", "C4", "C5", "C6");
        System.out.println("👤 User: " + user.getName());
        System.out.println("📍 Booking seats: " + seats);
        
        Booking booking = bookingSystem.createBooking(user, show, seats);
        if (booking != null) {
            bookingSystem.confirmBooking(booking.getBookingId(), "DEBIT_CARD");
            System.out.println("✅ Booking confirmed: " + booking.getBookingId());

            // User decides to cancel
            System.out.println("\n❌ User decides to cancel the booking...");
            boolean cancelled = bookingSystem.cancelBooking(booking.getBookingId());
            
            if (cancelled) {
                System.out.println("✓ Booking cancelled successfully");
                System.out.println("✓ Seats released and available for others");
                System.out.println("✓ Refund initiated");
                
                // Check seat availability
                System.out.println("\n📊 Seats " + seats + " are now available again");
            }
        }

        System.out.println("\n");
    }

    private static void scenario4DynamicPricing(BookingSystem bookingSystem) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 4: Dynamic Pricing Strategies");
        System.out.println("========================================\n");

        Show show1 = bookingSystem.getShow("SHOW00001"); // 7 PM show
        Show show2 = bookingSystem.getShow("SHOW00002"); // 2 PM show
        Show show3 = bookingSystem.getShow("SHOW00003"); // 10 PM show
        
        Seat regularSeat = show1.getSeat("D5");
        Seat premiumSeat = show1.getSeat("B5");
        Seat vipSeat = show1.getSeat("A5");

        System.out.println("Base Prices:");
        System.out.println("  Regular Seat: $" + regularSeat.getBasePrice());
        System.out.println("  Premium Seat: $" + premiumSeat.getBasePrice());
        System.out.println("  VIP Seat: $" + vipSeat.getBasePrice());

        // Test different pricing strategies
        System.out.println("\n--- Timing-Based Pricing ---");
        PricingStrategy timingPricing = new TimingBasedPricing();
        bookingSystem.setPricingStrategy(timingPricing);
        
        System.out.println("Matinee Show (2 PM) - Regular Seat: $" + 
                         timingPricing.calculatePrice(regularSeat, show2));
        System.out.println("Evening Show (7 PM) - Regular Seat: $" + 
                         timingPricing.calculatePrice(regularSeat, show1));
        System.out.println("Night Show (10 PM) - Regular Seat: $" + 
                         timingPricing.calculatePrice(regularSeat, show3));

        System.out.println("\n--- Seat Type Pricing ---");
        PricingStrategy seatTypePricing = new SeatTypePricing();
        bookingSystem.setPricingStrategy(seatTypePricing);
        
        System.out.println("Regular Seat: $" + seatTypePricing.calculatePrice(regularSeat, show1));
        System.out.println("Premium Seat: $" + seatTypePricing.calculatePrice(premiumSeat, show1));
        System.out.println("VIP Seat: $" + seatTypePricing.calculatePrice(vipSeat, show1));

        System.out.println("\n--- Weekend Pricing ---");
        PricingStrategy weekendPricing = new WeekendPricing();
        bookingSystem.setPricingStrategy(weekendPricing);
        
        System.out.println("Weekend Show - Regular Seat: $" + 
                         weekendPricing.calculatePrice(regularSeat, show1));

        System.out.println("\n--- Composite Pricing (All Strategies Combined) ---");
        CompositePricing compositePricing = new CompositePricing();
        compositePricing.addStrategy(new TimingBasedPricing());
        compositePricing.addStrategy(new SeatTypePricing());
        compositePricing.addStrategy(new WeekendPricing());
        bookingSystem.setPricingStrategy(compositePricing);
        
        System.out.println("VIP Seat with all strategies: $" + 
                         compositePricing.calculatePrice(vipSeat, show1));

        // Reset to default
        bookingSystem.setPricingStrategy(new TimingBasedPricing());
        
        System.out.println("\n");
    }

    private static void scenario5FullShow(BookingSystem bookingSystem) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 5: Full Show Handling");
        System.out.println("========================================\n");

        Show show = bookingSystem.getShow("SHOW00003");
        User user = bookingSystem.getUser("U001");

        System.out.println("Show: " + show.getMovie().getTitle() + " at " + show.getStartTime());
        System.out.println("Initial Available Seats: " + show.getAvailableSeatsCount());

        // Book many seats to fill the show
        System.out.println("\n📊 Booking multiple seats to fill the show...");
        int bookedCount = 0;
        
        for (int i = 1; i <= 8; i++) {
            List<String> seats = Arrays.asList("E" + i);
            Booking booking = bookingSystem.createBooking(user, show, seats);
            if (booking != null) {
                bookingSystem.confirmBooking(booking.getBookingId(), "WALLET");
                bookedCount++;
            }
        }

        System.out.println("✓ Booked " + bookedCount + " seats");
        System.out.println("📊 Remaining Available Seats: " + show.getAvailableSeatsCount());

        if (show.getAvailableSeatsCount() < 10) {
            System.out.println("⚠️  Limited seats available - Hurry up!");
        }

        System.out.println("\n");
    }

    private static void scenario6DiscountApplication(BookingSystem bookingSystem) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 6: Discount Application");
        System.out.println("========================================\n");

        User user = bookingSystem.getUser("U002");
        Show show = bookingSystem.getShow("SHOW00002");

        List<String> seats = Arrays.asList("A1", "A2", "A3");
        System.out.println("👤 User: " + user.getName());
        System.out.println("📍 Selected Seats: " + seats);

        Booking booking = bookingSystem.createBooking(user, show, seats);
        if (booking != null) {
            System.out.println("\n💰 Original Amount: $" + 
                             String.format("%.2f", booking.getTotalAmount()));

            // Apply different discounts
            System.out.println("\n--- Testing Discount Codes ---");
            
            double original = booking.getTotalAmount();
            
            System.out.println("Applying 'STUDENT20' (20% off)");
            booking.applyDiscount(20);
            System.out.println("  New Amount: $" + String.format("%.2f", booking.getTotalAmount()));
            System.out.println("  Saved: $" + String.format("%.2f", original - booking.getTotalAmount()));

            // Complete booking
            System.out.println("\n💳 Processing payment with discount...");
            bookingSystem.confirmBooking(booking.getBookingId(), "UPI");
        }

        System.out.println("\n");
    }
}


