package strategy;

import model.*;
import java.util.List;

/**
 * Strategy interface for handling meeting conflicts
 */
public interface ConflictStrategy {
    Meeting handleConflict(Meeting newMeeting, List<User> participants, 
                          MeetingRoom room, Object service);
    String getStrategyName();
}
