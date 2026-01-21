package observer;

import model.Booking;

/**
 * Email notification observer
 * Sends email notifications for booking events
 */
public class EmailNotification implements BookingObserver {
    
    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("\n📧 EMAIL NOTIFICATION");
        System.out.println("To: " + booking.getUser().getEmail());
        System.out.println("Subject: Booking Confirmed - " + booking.getBookingId());
        System.out.println("Dear " + booking.getUser().getName() + ",");
        System.out.println("Your booking for " + booking.getShow().getMovie().getTitle() + 
                         " has been confirmed.");
        System.out.println("Show Time: " + booking.getShow().getStartTime());
        System.out.println("Seats: " + booking.getSeats().size());
        System.out.println("Total Amount: $" + String.format("%.2f", booking.getTotalAmount()));
        System.out.println("Booking ID: " + booking.getBookingId());
    }
    
    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("\n📧 EMAIL NOTIFICATION");
        System.out.println("To: " + booking.getUser().getEmail());
        System.out.println("Subject: Booking Cancelled - " + booking.getBookingId());
        System.out.println("Dear " + booking.getUser().getName() + ",");
        System.out.println("Your booking for " + booking.getShow().getMovie().getTitle() + 
                         " has been cancelled.");
        System.out.println("Refund will be processed within 5-7 business days.");
    }
}


