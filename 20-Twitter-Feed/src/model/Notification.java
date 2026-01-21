package model;

import enums.NotificationType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Represents a notification sent to a user.
 */
public class Notification {
    private final String notificationId;
    private final User recipient;
    private final NotificationType type;
    private final LocalDateTime timestamp;
    private boolean isRead;
    private final User relatedUser;  // User who triggered the notification
    private final Post relatedPost;  // Post related to the notification (can be null)
    
    public Notification(NotificationType type, User recipient, User relatedUser, Post relatedPost) {
        this.notificationId = UUID.randomUUID().toString().substring(0, 8);
        this.type = type;
        this.recipient = recipient;
        this.relatedUser = relatedUser;
        this.relatedPost = relatedPost;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }
    
    public void markAsRead() {
        this.isRead = true;
    }
    
    public String getMessage() {
        switch (type) {
            case FOLLOW:
                return relatedUser.getUsername() + " started following you";
            case LIKE:
                return relatedUser.getUsername() + " liked your post";
            case COMMENT:
                return relatedUser.getUsername() + " commented on your post";
            case RETWEET:
                return relatedUser.getUsername() + " retweeted your post";
            default:
                return "New notification";
        }
    }
    
    // Getters
    public String getNotificationId() {
        return notificationId;
    }
    
    public User getRecipient() {
        return recipient;
    }
    
    public NotificationType getType() {
        return type;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public User getRelatedUser() {
        return relatedUser;
    }
    
    public Post getRelatedPost() {
        return relatedPost;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm");
        String readStatus = isRead ? "✓" : "●";
        return readStatus + " " + getMessage() + " (" + timestamp.format(formatter) + ")";
    }
}

