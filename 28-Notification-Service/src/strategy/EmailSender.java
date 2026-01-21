package strategy;

import model.Notification;

/**
 * Email notification sender (simulates SMTP)
 */
public class EmailSender implements NotificationSender {
    
    @Override
    public boolean send(Notification notification) {
        // Simulate email sending
        System.out.println("📧 Sending EMAIL to: " + notification.getRecipient().getEmail());
        System.out.println("   Subject: " + notification.getSubject());
        System.out.println("   Message: " + notification.getMessage());
        
        // Simulate 90% success rate
        return Math.random() > 0.1;
    }
    
    @Override
    public String getChannelName() {
        return "EMAIL";
    }
}
