package strategy;

import model.Notification;

/**
 * SMS notification sender (simulates Twilio)
 */
public class SmsSender implements NotificationSender {
    
    @Override
    public boolean send(Notification notification) {
        // Simulate SMS sending
        System.out.println("📱 Sending SMS to: " + notification.getRecipient().getPhone());
        System.out.println("   Message: " + notification.getMessage());
        
        // Simulate 95% success rate
        return Math.random() > 0.05;
    }
    
    @Override
    public String getChannelName() {
        return "SMS";
    }
}
