package observer;

import model.Booking;

public class SMSNotifier implements Observer {
    @Override
    public void update(Booking booking, String message) {
        System.out.println("📱 [SMS to " + booking.getGuest().getPhone() + "] " + message);
    }
}
