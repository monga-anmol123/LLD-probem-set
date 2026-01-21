import enums.*;
import model.*;
import observer.NotificationObserver;
import service.NotificationService;

import java.util.*;

/**
 * Demo application for Notification Service
 * Demonstrates Observer, Strategy, Factory, and Template Method patterns
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  NOTIFICATION SERVICE DEMO");
        System.out.println("========================================\n");
        
        // Initialize Notification Service (Singleton)
        NotificationService service = NotificationService.getInstance();
        System.out.println("✓ Notification Service initialized\n");
        
        // Scenario 1: Register Users
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: REGISTERING USERS");
        System.out.println("========================================\n");
        
        User alice = new User("U001", "Alice", "alice@email.com", "555-0101");
        alice.setDeviceToken("device_token_alice_123");
        
        User bob = new User("U002", "Bob", "bob@email.com", "555-0102");
        bob.setDeviceToken("device_token_bob_456");
        
        User charlie = new User("U003", "Charlie", "charlie@email.com", "555-0103");
        
        service.registerUser(alice);
        service.registerUser(bob);
        service.registerUser(charlie);
        System.out.println();
        
        // Scenario 2: Add Observer for Analytics
        System.out.println("========================================");
        System.out.println("  SCENARIO 2: ADD ANALYTICS OBSERVER (Observer Pattern)");
        System.out.println("========================================\n");
        
        NotificationObserver analyticsObserver = new NotificationObserver() {
            private int sentCount = 0;
            private int deliveredCount = 0;
            private int failedCount = 0;
            
            @Override
            public void onNotificationSent(Notification notification) {
                sentCount++;
                System.out.println("📊 Analytics: Notification sent (Total: " + sentCount + ")");
            }
            
            @Override
            public void onNotificationDelivered(Notification notification) {
                deliveredCount++;
                System.out.println("📊 Analytics: Notification delivered (Total: " + deliveredCount + ")");
            }
            
            @Override
            public void onNotificationFailed(Notification notification) {
                failedCount++;
                System.out.println("📊 Analytics: Notification failed (Total: " + failedCount + ")");
            }
        };
        
        service.attach(analyticsObserver);
        System.out.println("✓ Analytics observer attached\n");
        
        // Scenario 3: Send Email Notification (Strategy Pattern)
        System.out.println("========================================");
        System.out.println("  SCENARIO 3: SEND EMAIL (Strategy Pattern)");
        System.out.println("========================================\n");
        
        Notification email1 = service.createNotification(
            "U001",
            "Welcome to Our Service",
            "Hi Alice, welcome aboard! We're excited to have you.",
            NotificationChannel.EMAIL,
            NotificationPriority.MEDIUM
        );
        
        service.queueNotification(email1);
        service.processQueue();
        
        // Scenario 4: Send SMS Notification
        System.out.println("========================================");
        System.out.println("  SCENARIO 4: SEND SMS (Strategy Pattern)");
        System.out.println("========================================\n");
        
        Notification sms1 = service.createNotification(
            "U002",
            "Verification Code",
            "Your verification code is: 123456",
            NotificationChannel.SMS,
            NotificationPriority.HIGH
        );
        
        service.queueNotification(sms1);
        service.processQueue();
        
        // Scenario 5: Send Push Notification
        System.out.println("========================================");
        System.out.println("  SCENARIO 5: SEND PUSH (Strategy Pattern)");
        System.out.println("========================================\n");
        
        Notification push1 = service.createNotification(
            "U001",
            "New Message",
            "You have a new message from Bob",
            NotificationChannel.PUSH,
            NotificationPriority.URGENT
        );
        
        service.queueNotification(push1);
        service.processQueue();
        
        // Scenario 6: Priority Queue Demonstration
        System.out.println("========================================");
        System.out.println("  SCENARIO 6: PRIORITY QUEUE");
        System.out.println("========================================\n");
        
        System.out.println("Creating notifications with different priorities...\n");
        
        Notification lowPriority = service.createNotification(
            "U001", "Newsletter", "Monthly newsletter", 
            NotificationChannel.EMAIL, NotificationPriority.LOW
        );
        
        Notification mediumPriority = service.createNotification(
            "U002", "Update Available", "New version available", 
            NotificationChannel.IN_APP, NotificationPriority.MEDIUM
        );
        
        Notification highPriority = service.createNotification(
            "U003", "Security Alert", "Unusual login detected", 
            NotificationChannel.EMAIL, NotificationPriority.HIGH
        );
        
        Notification urgentPriority = service.createNotification(
            "U001", "Critical Alert", "Action required immediately", 
            NotificationChannel.SMS, NotificationPriority.URGENT
        );
        
        // Queue in random order
        service.queueNotification(lowPriority);
        service.queueNotification(mediumPriority);
        service.queueNotification(highPriority);
        service.queueNotification(urgentPriority);
        
        System.out.println("\nProcessing queue (should process by priority: URGENT → HIGH → MEDIUM → LOW):\n");
        service.processQueue();
        
        // Scenario 7: Template-based Notifications
        System.out.println("========================================");
        System.out.println("  SCENARIO 7: TEMPLATE-BASED NOTIFICATIONS");
        System.out.println("========================================\n");
        
        NotificationTemplate welcomeTemplate = service.createTemplate(
            "Welcome Email",
            NotificationChannel.EMAIL,
            "Welcome {{name}}!",
            "Hi {{name}}, welcome to {{company}}! Your account {{email}} is now active."
        );
        
        System.out.println("Template variables: " + welcomeTemplate.getRequiredVariables() + "\n");
        
        Map<String, String> variables = new HashMap<>();
        variables.put("name", "Alice");
        variables.put("company", "TechCorp");
        variables.put("email", "alice@email.com");
        
        Notification templateNotif = service.createFromTemplate(
            "U001",
            welcomeTemplate.getTemplateId(),
            variables,
            NotificationPriority.MEDIUM
        );
        
        service.queueNotification(templateNotif);
        service.processQueue();
        
        // Scenario 8: Bulk Notifications
        System.out.println("========================================");
        System.out.println("  SCENARIO 8: BULK NOTIFICATIONS");
        System.out.println("========================================");
        
        List<String> allUsers = Arrays.asList("U001", "U002", "U003");
        service.sendBulkNotifications(
            allUsers,
            "System Maintenance",
            "Scheduled maintenance tonight at 2 AM",
            NotificationChannel.EMAIL,
            NotificationPriority.MEDIUM
        );
        
        service.processQueue();
        
        // Scenario 9: User Preferences
        System.out.println("========================================");
        System.out.println("  SCENARIO 9: USER PREFERENCES");
        System.out.println("========================================\n");
        
        System.out.println("Charlie disables SMS notifications...");
        charlie.setChannelPreference(NotificationChannel.SMS, false);
        
        try {
            service.createNotification(
                "U003",
                "Test SMS",
                "This should fail",
                NotificationChannel.SMS,
                NotificationPriority.MEDIUM
            );
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        System.out.println();
        
        // Scenario 10: View User Notifications
        System.out.println("========================================");
        System.out.println("  SCENARIO 10: USER NOTIFICATION HISTORY");
        System.out.println("========================================\n");
        
        System.out.println("Alice's notifications:");
        List<Notification> aliceNotifs = service.getUserNotifications("U001");
        for (Notification notif : aliceNotifs) {
            System.out.println("  " + notif);
        }
        System.out.println();
        
        // Scenario 11: Statistics
        service.showNotificationStats();
        
        // Scenario 12: Retry Mechanism (Simulated)
        System.out.println("========================================");
        System.out.println("  SCENARIO 12: RETRY MECHANISM");
        System.out.println("========================================\n");
        
        System.out.println("Creating notifications that may fail and retry...\n");
        
        for (int i = 0; i < 3; i++) {
            Notification testNotif = service.createNotification(
                "U002",
                "Test Notification " + (i + 1),
                "Testing retry mechanism",
                NotificationChannel.EMAIL,
                NotificationPriority.LOW
            );
            service.queueNotification(testNotif);
        }
        
        service.processQueue();
        
        // Final Statistics
        service.showNotificationStats();
        
        // Scenario 13: Edge Case - Invalid User
        System.out.println("========================================");
        System.out.println("  SCENARIO 13: EDGE CASE - INVALID USER");
        System.out.println("========================================\n");
        
        try {
            service.createNotification(
                "U999",
                "Test",
                "This should fail",
                NotificationChannel.EMAIL,
                NotificationPriority.MEDIUM
            );
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        System.out.println();
        
        // Scenario 14: Edge Case - Invalid Template Variables
        System.out.println("========================================");
        System.out.println("  SCENARIO 14: EDGE CASE - MISSING TEMPLATE VARIABLES");
        System.out.println("========================================\n");
        
        try {
            Map<String, String> incompleteVars = new HashMap<>();
            incompleteVars.put("name", "Bob");
            // Missing 'company' and 'email' variables
            
            service.createFromTemplate(
                "U002",
                welcomeTemplate.getTemplateId(),
                incompleteVars,
                NotificationPriority.MEDIUM
            );
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        System.out.println();
        
        // Final Summary
        System.out.println("========================================");
        System.out.println("  DEMO COMPLETED SUCCESSFULLY!");
        System.out.println("========================================");
        System.out.println("\n✅ Demonstrated Patterns:");
        System.out.println("  1. Observer Pattern - Analytics tracking");
        System.out.println("  2. Strategy Pattern - 4 notification channels (Email, SMS, Push, In-App)");
        System.out.println("  3. Factory Pattern - NotificationSenderFactory");
        System.out.println("  4. Singleton Pattern - NotificationService");
        System.out.println("  5. Template Method Pattern - NotificationTemplate");
        System.out.println("\n✅ Features Demonstrated:");
        System.out.println("  • Multi-channel delivery (Email, SMS, Push, In-App)");
        System.out.println("  • Priority-based queue (URGENT → HIGH → MEDIUM → LOW)");
        System.out.println("  • Retry mechanism with failure handling");
        System.out.println("  • Template-based notifications with variables");
        System.out.println("  • Bulk notifications");
        System.out.println("  • User preferences and channel control");
        System.out.println("  • Analytics tracking (Observer pattern)");
        System.out.println("  • Notification history");
        System.out.println("  • Statistics and success rate");
        System.out.println("  • Edge case handling");
        System.out.println("\n🎉 All scenarios executed successfully!");
    }
}
