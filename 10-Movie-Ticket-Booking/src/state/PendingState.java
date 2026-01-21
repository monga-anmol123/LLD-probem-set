package state;

import model.Booking;
import enums.BookingStatus;

/**
 * Pending state - booking is created but not yet confirmed
 */
public class PendingState implements BookingState {
    
    @Override
    public void confirm(Booking booking) {
        System.out.println("Confirming booking: " + booking.getBookingId());
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setState(new ConfirmedState());
        
        // Book the seats permanently
        booking.getShow().bookSeats(booking.getSeatIds(), booking.getUser().getUserId());
    }
    
    @Override
    public void cancel(Booking booking) {
        System.out.println("Cancelling pending booking: " + booking.getBookingId());
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setState(new CancelledState());
        
        // Release the locked seats
        booking.getShow().releaseSeats(booking.getSeatIds());
    }
}


