package observer;

import model.Meeting;
import model.TimeSlot;

/**
 * Observer interface for meeting events
 */
public interface MeetingObserver {
    void onMeetingScheduled(Meeting meeting);
    void onMeetingCancelled(Meeting meeting);
    void onMeetingRescheduled(Meeting meeting, TimeSlot oldSlot);
    void onConflictDetected(Meeting meeting);
}
