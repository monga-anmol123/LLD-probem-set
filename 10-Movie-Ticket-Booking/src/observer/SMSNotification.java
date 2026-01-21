package observer;

import model.Booking;

/**
 * SMS notification observer
 * Sends SMS notifications for booking events
 */
public class SMSNotification implements BookingObserver {
    
    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("\n📱 SMS NOTIFICATION");
        System.out.println("To: " + booking.getUser().getPhone());
        System.out.println("Message: Your booking " + booking.getBookingId() + 
                         " for " + booking.getShow().getMovie().getTitle() + 
                         " is confirmed. Show at " + booking.getShow().getStartTime() + 
                         ". Seats: " + booking.getSeats().size() + 
                         ". Amount: $" + String.format("%.2f", booking.getTotalAmount()));
    }
    
    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("\n📱 SMS NOTIFICATION");
        System.out.println("To: " + booking.getUser().getPhone());
        System.out.println("Message: Your booking " + booking.getBookingId() + 
                         " for " + booking.getShow().getMovie().getTitle() + 
                         " has been cancelled. Refund will be processed soon.");
    }
}


