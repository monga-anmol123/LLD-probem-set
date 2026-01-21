package strategy;

import model.Notification;

/**
 * Strategy interface for different notification channels
 */
public interface NotificationSender {
    boolean send(Notification notification);
    String getChannelName();
}
