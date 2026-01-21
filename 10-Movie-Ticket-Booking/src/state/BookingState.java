package state;

import model.Booking;

/**
 * State interface for booking state management
 * Demonstrates State Pattern
 */
public interface BookingState {
    
    /**
     * Confirm the booking
     * @param booking The booking to confirm
     */
    void confirm(Booking booking);
    
    /**
     * Cancel the booking
     * @param booking The booking to cancel
     */
    void cancel(Booking booking);
}


