package observer;

/**
 * Subject interface for notification events
 */
public interface NotificationSubject {
    void attach(NotificationObserver observer);
    void detach(NotificationObserver observer);
}
