package model;

import enums.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a meeting with participants and room
 */
public class Meeting {
    private String meetingId;
    private String title;
    private String description;
    private TimeSlot timeSlot;
    private User organizer;
    private List<User> participants;
    private MeetingRoom room;
    private MeetingStatus status;
    private RecurrenceType recurrence;
    private LocalDateTime createdAt;

    public Meeting(String meetingId, String title, String description, 
                   TimeSlot timeSlot, User organizer) {
        this.meetingId = meetingId;
        this.title = title;
        this.description = description;
        this.timeSlot = timeSlot;
        this.organizer = organizer;
        this.participants = new ArrayList<>();
        this.participants.add(organizer);
        this.room = null;
        this.status = MeetingStatus.SCHEDULED;
        this.recurrence = RecurrenceType.NONE;
        this.createdAt = LocalDateTime.now();
    }

    public void addParticipant(User user) {
        if (!participants.contains(user)) {
            participants.add(user);
        }
    }

    public void removeParticipant(User user) {
        if (!user.equals(organizer)) {
            participants.remove(user);
        }
    }

    public void setRoom(MeetingRoom room) {
        this.room = room;
    }

    public void setStatus(MeetingStatus status) {
        this.status = status;
    }

    public void setRecurrence(RecurrenceType recurrence) {
        this.recurrence = recurrence;
    }

    public void reschedule(TimeSlot newTimeSlot) {
        this.timeSlot = newTimeSlot;
        this.status = MeetingStatus.RESCHEDULED;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public User getOrganizer() {
        return organizer;
    }

    public List<User> getParticipants() {
        return new ArrayList<>(participants);
    }

    public MeetingRoom getRoom() {
        return room;
    }

    public MeetingStatus getStatus() {
        return status;
    }

    public RecurrenceType getRecurrence() {
        return recurrence;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Meeting %s: %s [%s]\n", meetingId, title, status));
        sb.append(String.format("  Time: %s\n", timeSlot));
        sb.append(String.format("  Organizer: %s\n", organizer.getName()));
        sb.append(String.format("  Participants: %d\n", participants.size()));
        if (room != null) {
            sb.append(String.format("  Room: %s\n", room.getName()));
        }
        if (recurrence != RecurrenceType.NONE) {
            sb.append(String.format("  Recurrence: %s\n", recurrence));
        }
        return sb.toString();
    }
}
