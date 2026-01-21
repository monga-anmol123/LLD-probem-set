package model;

import enums.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a notification to be sent
 */
public class Notification {
    private String notificationId;
    private User recipient;
    private String subject;
    private String message;
    private NotificationChannel channel;
    private NotificationPriority priority;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private int retryCount;
    private int maxRetries;
    private Map<String, String> metadata;

    public Notification(String notificationId, User recipient, String subject, 
                       String message, NotificationChannel channel, NotificationPriority priority) {
        this.notificationId = notificationId;
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.channel = channel;
        this.priority = priority;
        this.status = NotificationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.sentAt = null;
        this.deliveredAt = null;
        this.retryCount = 0;
        this.maxRetries = 3;
        this.metadata = new HashMap<>();
    }

    public void markQueued() {
        this.status = NotificationStatus.QUEUED;
    }

    public void markSending() {
        this.status = NotificationStatus.SENDING;
    }

    public void markSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    public void markDelivered() {
        this.status = NotificationStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = NotificationStatus.FAILED;
    }

    public void incrementRetry() {
        this.retryCount++;
        this.status = NotificationStatus.RETRYING;
    }

    public boolean canRetry() {
        return retryCount < maxRetries;
    }

    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    public String getNotificationId() {
        return notificationId;
    }

    public User getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }

    @Override
    public String toString() {
        return String.format("Notification %s [%s] [%s] to %s - %s - Status: %s",
                notificationId, channel, priority, recipient.getName(), subject, status);
    }
}
