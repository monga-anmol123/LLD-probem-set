package service;

import enums.*;
import factory.NotificationSenderFactory;
import model.*;
import observer.*;
import strategy.NotificationSender;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Main Notification Service (Singleton pattern)
 * Manages notifications with priority queue, retry logic, and multi-channel delivery
 */
public class NotificationService implements NotificationSubject {
    private static NotificationService instance;
    
    private Map<String, User> users;
    private Map<String, Notification> notifications;
    private Map<String, NotificationTemplate> templates;
    private PriorityQueue<Notification> notificationQueue;
    private List<NotificationObserver> observers;
    private int notificationCounter;
    private int templateCounter;
    
    private NotificationService() {
        this.users = new HashMap<>();
        this.notifications = new HashMap<>();
        this.templates = new HashMap<>();
        this.notificationQueue = new PriorityQueue<>(
            Comparator.comparing(Notification::getPriority, 
                               Comparator.comparingInt(NotificationPriority::getValue))
                     .thenComparing(Notification::getCreatedAt)
        );
        this.observers = new ArrayList<>();
        this.notificationCounter = 1;
        this.templateCounter = 1;
    }
    
    public static synchronized NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }
    
    // Observer pattern methods
    @Override
    public void attach(NotificationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    @Override
    public void detach(NotificationObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObserversSent(Notification notification) {
        for (NotificationObserver observer : observers) {
            observer.onNotificationSent(notification);
        }
    }
    
    private void notifyObserversDelivered(Notification notification) {
        for (NotificationObserver observer : observers) {
            observer.onNotificationDelivered(notification);
        }
    }
    
    private void notifyObserversFailed(Notification notification) {
        for (NotificationObserver observer : observers) {
            observer.onNotificationFailed(notification);
        }
    }
    
    // User management
    public void registerUser(User user) {
        users.put(user.getUserId(), user);
        System.out.println("✓ User registered: " + user.getName());
    }
    
    public User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return user;
    }
    
    // Template management
    public NotificationTemplate createTemplate(String name, NotificationChannel channel,
                                              String subjectTemplate, String messageTemplate) {
        String templateId = "TPL" + String.format("%04d", templateCounter++);
        NotificationTemplate template = new NotificationTemplate(
            templateId, name, channel, subjectTemplate, messageTemplate
        );
        templates.put(templateId, template);
        System.out.println("✓ Template created: " + templateId + " - " + name);
        return template;
    }
    
    public NotificationTemplate getTemplate(String templateId) {
        NotificationTemplate template = templates.get(templateId);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateId);
        }
        return template;
    }
    
    // Notification management
    public Notification createNotification(String userId, String subject, String message,
                                          NotificationChannel channel, NotificationPriority priority) {
        User user = getUser(userId);
        
        // Check user preferences
        if (!user.isChannelEnabled(channel)) {
            throw new IllegalArgumentException(
                "User has disabled " + channel + " notifications"
            );
        }
        
        String notificationId = "NOTIF" + String.format("%04d", notificationCounter++);
        Notification notification = new Notification(
            notificationId, user, subject, message, channel, priority
        );
        
        notifications.put(notificationId, notification);
        System.out.println("✓ Notification created: " + notificationId);
        return notification;
    }
    
    public Notification createFromTemplate(String userId, String templateId, 
                                          Map<String, String> variables, 
                                          NotificationPriority priority) {
        User user = getUser(userId);
        NotificationTemplate template = getTemplate(templateId);
        
        // Validate variables
        if (!template.validateVariables(variables)) {
            throw new IllegalArgumentException(
                "Missing required variables: " + template.getRequiredVariables()
            );
        }
        
        // Render template
        String subject = template.renderSubject(variables);
        String message = template.renderMessage(variables);
        
        return createNotification(userId, subject, message, template.getChannel(), priority);
    }
    
    public void queueNotification(Notification notification) {
        notification.markQueued();
        notificationQueue.offer(notification);
        System.out.println("✓ Notification queued: " + notification.getNotificationId() + 
                          " [Priority: " + notification.getPriority() + "]");
    }
    
    public void sendNotification(Notification notification) {
        notification.markSending();
        
        // Get appropriate sender using Factory pattern
        NotificationSender sender = NotificationSenderFactory.getSender(notification.getChannel());
        
        System.out.println("\n→ Sending " + notification.getNotificationId() + "...");
        boolean success = sender.send(notification);
        
        if (success) {
            notification.markSent();
            notification.markDelivered(); // Simulate immediate delivery
            notifyObserversSent(notification);
            notifyObserversDelivered(notification);
            System.out.println("✓ Notification sent successfully");
        } else {
            handleFailure(notification);
        }
    }
    
    private void handleFailure(Notification notification) {
        if (notification.canRetry()) {
            notification.incrementRetry();
            System.out.println("⚠ Notification failed, will retry (" + 
                             notification.getRetryCount() + "/3)");
            
            // Re-queue with exponential backoff (simulated)
            notificationQueue.offer(notification);
        } else {
            notification.markFailed();
            notifyObserversFailed(notification);
            System.out.println("❌ Notification failed after max retries");
        }
    }
    
    public void processQueue() {
        System.out.println("\n========================================");
        System.out.println("  PROCESSING NOTIFICATION QUEUE");
        System.out.println("========================================");
        System.out.println("Queue size: " + notificationQueue.size() + "\n");
        
        while (!notificationQueue.isEmpty()) {
            Notification notification = notificationQueue.poll();
            sendNotification(notification);
        }
        
        System.out.println("\n✓ Queue processing complete");
        System.out.println("========================================\n");
    }
    
    public void sendBulkNotifications(List<String> userIds, String subject, String message,
                                     NotificationChannel channel, NotificationPriority priority) {
        System.out.println("\n→ Creating bulk notifications for " + userIds.size() + " users...");
        
        for (String userId : userIds) {
            try {
                Notification notification = createNotification(
                    userId, subject, message, channel, priority
                );
                queueNotification(notification);
            } catch (Exception e) {
                System.out.println("⚠ Failed to create notification for user " + userId + ": " + e.getMessage());
            }
        }
        
        System.out.println("✓ Bulk notifications queued\n");
    }
    
    // Analytics and queries
    public void showNotificationStats() {
        System.out.println("\n========================================");
        System.out.println("  NOTIFICATION STATISTICS");
        System.out.println("========================================");
        System.out.println("Total Users: " + users.size());
        System.out.println("Total Notifications: " + notifications.size());
        System.out.println("Total Templates: " + templates.size());
        System.out.println("Queue Size: " + notificationQueue.size());
        
        // Status breakdown
        Map<NotificationStatus, Long> statusCount = notifications.values().stream()
            .collect(Collectors.groupingBy(Notification::getStatus, Collectors.counting()));
        
        System.out.println("\nNotifications by status:");
        statusCount.forEach((status, count) -> 
            System.out.println("  " + status + ": " + count));
        
        // Channel breakdown
        Map<NotificationChannel, Long> channelCount = notifications.values().stream()
            .collect(Collectors.groupingBy(Notification::getChannel, Collectors.counting()));
        
        System.out.println("\nNotifications by channel:");
        channelCount.forEach((channel, count) -> 
            System.out.println("  " + channel + ": " + count));
        
        // Success rate
        long delivered = notifications.values().stream()
            .filter(n -> n.getStatus() == NotificationStatus.DELIVERED)
            .count();
        long failed = notifications.values().stream()
            .filter(n -> n.getStatus() == NotificationStatus.FAILED)
            .count();
        long total = delivered + failed;
        
        if (total > 0) {
            double successRate = (delivered * 100.0) / total;
            System.out.println(String.format("\nSuccess Rate: %.1f%% (%d/%d)", 
                                            successRate, delivered, total));
        }
        
        System.out.println("========================================\n");
    }
    
    public List<Notification> getUserNotifications(String userId) {
        return notifications.values().stream()
            .filter(n -> n.getRecipient().getUserId().equals(userId))
            .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
            .collect(Collectors.toList());
    }
    
    public List<Notification> getNotificationsByStatus(NotificationStatus status) {
        return notifications.values().stream()
            .filter(n -> n.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    public int getQueueSize() {
        return notificationQueue.size();
    }
}
