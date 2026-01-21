package strategy;

import model.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Auto-reschedule strategy - automatically find next available slot
 */
public class AutoRescheduleStrategy implements ConflictStrategy {
    
    @Override
    public Meeting handleConflict(Meeting newMeeting, List<User> participants, 
                                 MeetingRoom room, Object serviceObj) {
        System.out.println("🔄 Conflict detected - Auto-rescheduling to next available slot");
        
        // Cast service object
        service.MeetingSchedulerService service = (service.MeetingSchedulerService) serviceObj;
        
        // Find next available slot
        LocalDateTime searchStart = newMeeting.getTimeSlot().getStartTime();
        LocalDateTime searchEnd = searchStart.plusDays(7); // Search within next week
        long duration = newMeeting.getTimeSlot().getDurationMinutes();
        
        List<TimeSlot> commonSlots = service.findCommonFreeSlots(
            participants, searchStart, searchEnd, duration
        );
        
        if (commonSlots.isEmpty()) {
            System.out.println("❌ No available slots found in next 7 days");
            throw new RuntimeException("Cannot auto-reschedule - no available slots");
        }
        
        // Use first available slot
        TimeSlot newSlot = commonSlots.get(0);
        TimeSlot oldSlot = newMeeting.getTimeSlot();
        newMeeting.reschedule(newSlot);
        
        System.out.println("✓ Rescheduled to: " + newSlot);
        
        // Notify participants
        for (User participant : participants) {
            participant.onMeetingRescheduled(newMeeting, oldSlot);
        }
        
        return newMeeting;
    }
    
    @Override
    public String getStrategyName() {
        return "AUTO_RESCHEDULE";
    }
}
