package model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a calendar that holds meetings
 */
public class Calendar {
    private String calendarId;
    private String name;
    private List<Meeting> meetings;

    public Calendar(String calendarId, String name) {
        this.calendarId = calendarId;
        this.name = name;
        this.meetings = new ArrayList<>();
    }

    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    public void removeMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    public boolean hasConflict(TimeSlot timeSlot) {
        return meetings.stream()
                .filter(m -> m.getStatus() == enums.MeetingStatus.SCHEDULED || 
                            m.getStatus() == enums.MeetingStatus.CONFIRMED)
                .anyMatch(m -> m.getTimeSlot().overlapsWith(timeSlot));
    }

    public List<Meeting> getMeetingsInRange(LocalDateTime start, LocalDateTime end) {
        return meetings.stream()
                .filter(m -> {
                    LocalDateTime meetingStart = m.getTimeSlot().getStartTime();
                    return !meetingStart.isBefore(start) && meetingStart.isBefore(end);
                })
                .sorted(Comparator.comparing(m -> m.getTimeSlot().getStartTime()))
                .collect(Collectors.toList());
    }

    public List<TimeSlot> getFreeSlotsInRange(LocalDateTime start, LocalDateTime end, 
                                              long durationMinutes) {
        List<TimeSlot> freeSlots = new ArrayList<>();
        
        // Get all busy slots
        List<TimeSlot> busySlots = meetings.stream()
                .filter(m -> m.getStatus() == enums.MeetingStatus.SCHEDULED || 
                            m.getStatus() == enums.MeetingStatus.CONFIRMED)
                .map(Meeting::getTimeSlot)
                .sorted(Comparator.comparing(TimeSlot::getStartTime))
                .collect(Collectors.toList());
        
        LocalDateTime currentTime = start;
        
        for (TimeSlot busy : busySlots) {
            if (busy.getStartTime().isAfter(end)) break;
            
            if (currentTime.isBefore(busy.getStartTime())) {
                long availableMinutes = java.time.Duration.between(
                    currentTime, busy.getStartTime()).toMinutes();
                
                if (availableMinutes >= durationMinutes) {
                    freeSlots.add(new TimeSlot(currentTime, busy.getStartTime()));
                }
            }
            
            if (busy.getEndTime().isAfter(currentTime)) {
                currentTime = busy.getEndTime();
            }
        }
        
        // Check remaining time after last meeting
        if (currentTime.isBefore(end)) {
            long availableMinutes = java.time.Duration.between(currentTime, end).toMinutes();
            if (availableMinutes >= durationMinutes) {
                freeSlots.add(new TimeSlot(currentTime, end));
            }
        }
        
        return freeSlots;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public String getName() {
        return name;
    }

    public List<Meeting> getMeetings() {
        return new ArrayList<>(meetings);
    }

    @Override
    public String toString() {
        return String.format("Calendar %s - %s (%d meetings)", 
                calendarId, name, meetings.size());
    }
}
