package service;

import enums.*;
import model.*;
import observer.*;
import strategy.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main Meeting Scheduler Service (Singleton pattern)
 * Manages meetings, users, rooms, and conflict resolution
 */
public class MeetingSchedulerService implements MeetingSubject {
    private static MeetingSchedulerService instance;
    
    private Map<String, User> users;
    private Map<String, MeetingRoom> rooms;
    private Map<String, Meeting> meetings;
    private List<MeetingObserver> observers;
    private ConflictStrategy conflictStrategy;
    private int meetingCounter;

    private MeetingSchedulerService() {
        this.users = new HashMap<>();
        this.rooms = new HashMap<>();
        this.meetings = new HashMap<>();
        this.observers = new ArrayList<>();
        this.conflictStrategy = new RejectStrategy(); // Default
        this.meetingCounter = 1;
    }

    public static synchronized MeetingSchedulerService getInstance() {
        if (instance == null) {
            instance = new MeetingSchedulerService();
        }
        return instance;
    }

    // Observer pattern methods
    @Override
    public void attach(MeetingObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(MeetingObserver observer) {
        observers.remove(observer);
    }

    private void notifyMeetingScheduled(Meeting meeting) {
        for (User participant : meeting.getParticipants()) {
            participant.onMeetingScheduled(meeting);
        }
        for (MeetingObserver observer : observers) {
            observer.onMeetingScheduled(meeting);
        }
    }

    private void notifyMeetingCancelled(Meeting meeting) {
        for (User participant : meeting.getParticipants()) {
            participant.onMeetingCancelled(meeting);
        }
        for (MeetingObserver observer : observers) {
            observer.onMeetingCancelled(meeting);
        }
    }

    private void notifyMeetingRescheduled(Meeting meeting, TimeSlot oldSlot) {
        for (User participant : meeting.getParticipants()) {
            participant.onMeetingRescheduled(meeting, oldSlot);
        }
        for (MeetingObserver observer : observers) {
            observer.onMeetingRescheduled(meeting, oldSlot);
        }
    }

    // Strategy pattern - set conflict resolution strategy
    public void setConflictStrategy(ConflictStrategy strategy) {
        this.conflictStrategy = strategy;
        System.out.println("✓ Conflict strategy set to: " + strategy.getStrategyName());
    }

    // User management
    public void registerUser(User user) {
        users.put(user.getUserId(), user);
        System.out.println("✓ User registered: " + user.getName());
    }

    public User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return user;
    }

    // Room management
    public void addMeetingRoom(MeetingRoom room) {
        rooms.put(room.getRoomId(), room);
        System.out.println("✓ Meeting room added: " + room.getName());
    }

    public MeetingRoom getRoom(String roomId) {
        MeetingRoom room = rooms.get(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Room not found: " + roomId);
        }
        return room;
    }

    public List<MeetingRoom> getAvailableRooms(TimeSlot timeSlot, int requiredCapacity) {
        return rooms.values().stream()
                .filter(room -> room.canAccommodate(requiredCapacity))
                .filter(room -> !room.getCalendar().hasConflict(timeSlot))
                .collect(Collectors.toList());
    }

    // Meeting scheduling
    public Meeting scheduleMeeting(String title, String description, TimeSlot timeSlot,
                                  String organizerId, List<String> participantIds,
                                  String roomId) {
        User organizer = getUser(organizerId);
        List<User> participants = new ArrayList<>();
        participants.add(organizer);
        
        for (String participantId : participantIds) {
            if (!participantId.equals(organizerId)) {
                participants.add(getUser(participantId));
            }
        }

        MeetingRoom room = null;
        if (roomId != null) {
            room = getRoom(roomId);
            
            // Check room capacity
            if (!room.canAccommodate(participants.size())) {
                throw new IllegalArgumentException(
                    "Room capacity insufficient: " + room.getCapacity() + 
                    " < " + participants.size()
                );
            }
        }

        // Check for conflicts
        boolean hasConflict = false;
        
        // Check participant conflicts
        for (User participant : participants) {
            if (participant.getCalendar().hasConflict(timeSlot)) {
                hasConflict = true;
                break;
            }
        }
        
        // Check room conflict
        if (room != null && room.getCalendar().hasConflict(timeSlot)) {
            hasConflict = true;
        }

        String meetingId = "MTG" + String.format("%04d", meetingCounter++);
        Meeting meeting = new Meeting(meetingId, title, description, timeSlot, organizer);
        
        for (User participant : participants) {
            if (!participant.equals(organizer)) {
                meeting.addParticipant(participant);
            }
        }
        
        if (room != null) {
            meeting.setRoom(room);
        }

        // Handle conflicts using strategy pattern
        if (hasConflict) {
            meeting = conflictStrategy.handleConflict(meeting, participants, room, this);
        }

        // Add to calendars
        for (User participant : participants) {
            participant.getCalendar().addMeeting(meeting);
        }
        
        if (room != null) {
            room.getCalendar().addMeeting(meeting);
        }

        meetings.put(meetingId, meeting);
        
        System.out.println("✓ Meeting scheduled: " + meetingId + " - " + title);
        notifyMeetingScheduled(meeting);
        
        return meeting;
    }

    public void cancelMeeting(String meetingId) {
        Meeting meeting = meetings.get(meetingId);
        if (meeting == null) {
            throw new IllegalArgumentException("Meeting not found: " + meetingId);
        }

        meeting.setStatus(MeetingStatus.CANCELLED);
        
        // Remove from calendars
        for (User participant : meeting.getParticipants()) {
            participant.getCalendar().removeMeeting(meeting);
        }
        
        if (meeting.getRoom() != null) {
            meeting.getRoom().getCalendar().removeMeeting(meeting);
        }

        System.out.println("✓ Meeting cancelled: " + meetingId);
        notifyMeetingCancelled(meeting);
    }

    public void rescheduleMeeting(String meetingId, TimeSlot newTimeSlot) {
        Meeting meeting = meetings.get(meetingId);
        if (meeting == null) {
            throw new IllegalArgumentException("Meeting not found: " + meetingId);
        }

        TimeSlot oldSlot = meeting.getTimeSlot();
        
        // Check for conflicts at new time
        boolean hasConflict = false;
        
        for (User participant : meeting.getParticipants()) {
            if (participant.getCalendar().hasConflict(newTimeSlot)) {
                // Exclude current meeting from conflict check
                List<Meeting> conflicts = participant.getCalendar().getMeetings().stream()
                        .filter(m -> m.getTimeSlot().overlapsWith(newTimeSlot))
                        .filter(m -> !m.equals(meeting))
                        .collect(Collectors.toList());
                if (!conflicts.isEmpty()) {
                    hasConflict = true;
                    break;
                }
            }
        }
        
        if (meeting.getRoom() != null) {
            List<Meeting> roomConflicts = meeting.getRoom().getCalendar().getMeetings().stream()
                    .filter(m -> m.getTimeSlot().overlapsWith(newTimeSlot))
                    .filter(m -> !m.equals(meeting))
                    .collect(Collectors.toList());
            if (!roomConflicts.isEmpty()) {
                hasConflict = true;
            }
        }

        if (hasConflict) {
            throw new RuntimeException("Cannot reschedule - conflicts detected at new time");
        }

        meeting.reschedule(newTimeSlot);
        
        System.out.println("✓ Meeting rescheduled: " + meetingId);
        notifyMeetingRescheduled(meeting, oldSlot);
    }

    // Find common free slots for multiple participants
    public List<TimeSlot> findCommonFreeSlots(List<User> participants, 
                                             LocalDateTime start, LocalDateTime end,
                                             long durationMinutes) {
        if (participants.isEmpty()) {
            return new ArrayList<>();
        }

        // Get free slots for first participant
        List<TimeSlot> commonSlots = participants.get(0).getCalendar()
                .getFreeSlotsInRange(start, end, durationMinutes);

        // Intersect with other participants' free slots
        for (int i = 1; i < participants.size(); i++) {
            List<TimeSlot> participantSlots = participants.get(i).getCalendar()
                    .getFreeSlotsInRange(start, end, durationMinutes);
            
            commonSlots = intersectTimeSlots(commonSlots, participantSlots, durationMinutes);
        }

        return commonSlots;
    }

    private List<TimeSlot> intersectTimeSlots(List<TimeSlot> slots1, List<TimeSlot> slots2,
                                             long durationMinutes) {
        List<TimeSlot> result = new ArrayList<>();
        
        for (TimeSlot slot1 : slots1) {
            for (TimeSlot slot2 : slots2) {
                if (slot1.overlapsWith(slot2)) {
                    LocalDateTime start = slot1.getStartTime().isAfter(slot2.getStartTime()) ?
                            slot1.getStartTime() : slot2.getStartTime();
                    LocalDateTime end = slot1.getEndTime().isBefore(slot2.getEndTime()) ?
                            slot1.getEndTime() : slot2.getEndTime();
                    
                    long availableMinutes = java.time.Duration.between(start, end).toMinutes();
                    if (availableMinutes >= durationMinutes) {
                        result.add(new TimeSlot(start, end));
                    }
                }
            }
        }
        
        return result;
    }

    // Recurring meetings
    public List<Meeting> scheduleRecurringMeeting(String title, String description,
                                                  TimeSlot firstSlot, String organizerId,
                                                  List<String> participantIds, String roomId,
                                                  RecurrenceType recurrence, int occurrences) {
        List<Meeting> recurringMeetings = new ArrayList<>();
        
        TimeSlot currentSlot = firstSlot;
        
        for (int i = 0; i < occurrences; i++) {
            try {
                Meeting meeting = scheduleMeeting(title, description, currentSlot,
                        organizerId, participantIds, roomId);
                meeting.setRecurrence(recurrence);
                recurringMeetings.add(meeting);
                
                // Calculate next occurrence
                currentSlot = getNextOccurrence(currentSlot, recurrence);
            } catch (Exception e) {
                System.out.println("⚠️  Could not schedule occurrence " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        System.out.println("✓ Scheduled " + recurringMeetings.size() + " out of " + 
                          occurrences + " recurring meetings");
        
        return recurringMeetings;
    }

    private TimeSlot getNextOccurrence(TimeSlot slot, RecurrenceType recurrence) {
        LocalDateTime start = slot.getStartTime();
        LocalDateTime end = slot.getEndTime();
        
        switch (recurrence) {
            case DAILY:
                return new TimeSlot(start.plusDays(1), end.plusDays(1));
            case WEEKLY:
                return new TimeSlot(start.plusWeeks(1), end.plusWeeks(1));
            case MONTHLY:
                return new TimeSlot(start.plusMonths(1), end.plusMonths(1));
            default:
                return slot;
        }
    }

    // Query methods
    public List<Meeting> getUserMeetings(String userId, LocalDateTime start, LocalDateTime end) {
        User user = getUser(userId);
        return user.getCalendar().getMeetingsInRange(start, end);
    }

    public Meeting getMeeting(String meetingId) {
        Meeting meeting = meetings.get(meetingId);
        if (meeting == null) {
            throw new IllegalArgumentException("Meeting not found: " + meetingId);
        }
        return meeting;
    }

    public void showStatistics() {
        System.out.println("\n========================================");
        System.out.println("  MEETING SCHEDULER STATISTICS");
        System.out.println("========================================");
        System.out.println("Total Users: " + users.size());
        System.out.println("Total Rooms: " + rooms.size());
        System.out.println("Total Meetings: " + meetings.size());
        
        Map<MeetingStatus, Long> statusCount = meetings.values().stream()
                .collect(Collectors.groupingBy(Meeting::getStatus, Collectors.counting()));
        
        System.out.println("\nMeetings by status:");
        statusCount.forEach((status, count) -> 
                System.out.println("  " + status + ": " + count));
        
        System.out.println("========================================\n");
    }
}
