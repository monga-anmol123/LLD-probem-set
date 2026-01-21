package strategy;

import model.Notification;

/**
 * In-app notification sender
 */
public class InAppSender implements NotificationSender {
    
    @Override
    public boolean send(Notification notification) {
        // Simulate in-app notification
        System.out.println("💬 Sending IN-APP notification to: " + notification.getRecipient().getName());
        System.out.println("   Message: " + notification.getMessage());
        
        // In-app notifications are always successful (stored in DB)
        return true;
    }
    
    @Override
    public String getChannelName() {
        return "IN_APP";
    }
}
