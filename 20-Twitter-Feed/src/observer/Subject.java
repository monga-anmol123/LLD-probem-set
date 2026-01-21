package observer;

import model.Notification;

/**
 * Subject interface for objects that can be observed.
 */
public interface Subject {
    /**
     * Add an observer to be notified of changes.
     * 
     * @param observer Observer to add
     */
    void addObserver(Observer observer);
    
    /**
     * Remove an observer.
     * 
     * @param observer Observer to remove
     */
    void removeObserver(Observer observer);
    
    /**
     * Notify all observers of a change.
     * 
     * @param notification Notification to send
     */
    void notifyObservers(Notification notification);
}

