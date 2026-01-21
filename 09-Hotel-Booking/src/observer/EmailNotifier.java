package observer;

import model.Booking;

public class EmailNotifier implements Observer {
    @Override
    public void update(Booking booking, String message) {
        System.out.println("📧 [EMAIL to " + booking.getGuest().getEmail() + "] " + message);
    }
}
