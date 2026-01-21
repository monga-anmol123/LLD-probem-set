package strategy;

import model.*;
import java.util.List;

/**
 * Reject strategy - simply reject the meeting if there's a conflict
 */
public class RejectStrategy implements ConflictStrategy {
    
    @Override
    public Meeting handleConflict(Meeting newMeeting, List<User> participants, 
                                 MeetingRoom room, Object service) {
        System.out.println("❌ Conflict detected - Meeting rejected");
        throw new RuntimeException("Cannot schedule meeting - conflicts detected");
    }
    
    @Override
    public String getStrategyName() {
        return "REJECT";
    }
}
