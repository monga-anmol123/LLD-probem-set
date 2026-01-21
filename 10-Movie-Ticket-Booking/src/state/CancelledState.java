package state;

import model.Booking;

/**
 * Cancelled state - booking has been cancelled
 */
public class CancelledState implements BookingState {
    
    @Override
    public void confirm(Booking booking) {
        System.out.println("Cannot confirm cancelled booking: " + booking.getBookingId());
    }
    
    @Override
    public void cancel(Booking booking) {
        System.out.println("Booking " + booking.getBookingId() + " is already cancelled.");
    }
}


