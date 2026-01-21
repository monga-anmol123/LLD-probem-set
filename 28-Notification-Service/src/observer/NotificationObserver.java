package observer;

import model.Notification;

/**
 * Observer interface for notification events
 */
public interface NotificationObserver {
    void onNotificationSent(Notification notification);
    void onNotificationDelivered(Notification notification);
    void onNotificationFailed(Notification notification);
}
