package observer;

import model.Notification;

/**
 * Observer interface for receiving notifications.
 */
public interface Observer {
    /**
     * Update the observer with a new notification.
     * 
     * @param notification The notification to process
     */
    void update(Notification notification);
    
    /**
     * Get the observer's unique identifier.
     * 
     * @return Observer ID
     */
    String getObserverId();
}

