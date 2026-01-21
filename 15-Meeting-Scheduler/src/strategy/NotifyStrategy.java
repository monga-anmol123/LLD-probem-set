package strategy;

import model.*;
import observer.MeetingObserver;
import java.util.List;

/**
 * Notify strategy - notify all participants about the conflict
 */
public class NotifyStrategy implements ConflictStrategy {
    
    @Override
    public Meeting handleConflict(Meeting newMeeting, List<User> participants, 
                                 MeetingRoom room, Object service) {
        System.out.println("⚠️  Conflict detected - Notifying participants");
        
        // Notify all participants
        for (User participant : participants) {
            if (participant instanceof MeetingObserver) {
                ((MeetingObserver) participant).onConflictDetected(newMeeting);
            }
        }
        
        // Still schedule the meeting but mark it
        System.out.println("📅 Meeting scheduled despite conflicts (participants notified)");
        return newMeeting;
    }
    
    @Override
    public String getStrategyName() {
        return "NOTIFY";
    }
}
