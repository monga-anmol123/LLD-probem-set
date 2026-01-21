package state;

import model.Booking;
import enums.BookingStatus;
import java.time.LocalDateTime;

/**
 * Confirmed state - booking is confirmed and payment is successful
 */
public class ConfirmedState implements BookingState {
    
    @Override
    public void confirm(Booking booking) {
        System.out.println("Booking " + booking.getBookingId() + " is already confirmed.");
    }
    
    @Override
    public void cancel(Booking booking) {
        // Check if show hasn't started yet
        if (booking.getShow().getStartTime().isAfter(LocalDateTime.now())) {
            System.out.println("Cancelling confirmed booking: " + booking.getBookingId());
            booking.setStatus(BookingStatus.CANCELLED);
            booking.setState(new CancelledState());
            
            // Release the booked seats
            booking.getShow().releaseSeats(booking.getSeatIds());
            
            // Process refund
            if (booking.getPayment() != null) {
                booking.getPayment().refund();
                System.out.println("Refund processed for payment: " + 
                                 booking.getPayment().getPaymentId());
            }
        } else {
            System.out.println("Cannot cancel booking " + booking.getBookingId() + 
                             " - Show has already started or ended.");
        }
    }
}


