package observer;

/**
 * Subject interface for meeting events
 */
public interface MeetingSubject {
    void attach(MeetingObserver observer);
    void detach(MeetingObserver observer);
}
