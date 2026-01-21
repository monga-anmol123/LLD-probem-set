package strategy;

import model.Notification;

/**
 * Push notification sender (simulates FCM/APNS)
 */
public class PushSender implements NotificationSender {
    
    @Override
    public boolean send(Notification notification) {
        // Simulate push notification
        String deviceToken = notification.getRecipient().getDeviceToken();
        
        if (deviceToken == null) {
            System.out.println("❌ No device token for push notification");
            return false;
        }
        
        System.out.println("🔔 Sending PUSH to device: " + deviceToken);
        System.out.println("   Title: " + notification.getSubject());
        System.out.println("   Body: " + notification.getMessage());
        
        // Simulate 85% success rate
        return Math.random() > 0.15;
    }
    
    @Override
    public String getChannelName() {
        return "PUSH";
    }
}
