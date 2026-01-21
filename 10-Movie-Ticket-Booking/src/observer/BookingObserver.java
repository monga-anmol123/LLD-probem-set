package observer;

import model.Booking;

/**
 * Observer interface for booking notifications
 * Demonstrates Observer Pattern
 */
public interface BookingObserver {
    
    /**
     * Called when a booking is confirmed
     * @param booking The confirmed booking
     */
    void onBookingConfirmed(Booking booking);
    
    /**
     * Called when a booking is cancelled
     * @param booking The cancelled booking
     */
    void onBookingCancelled(Booking booking);
}


