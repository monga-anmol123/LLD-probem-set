package model;

import observer.MeetingObserver;

/**
 * Represents a user who can attend meetings
 * Implements Observer pattern to receive meeting notifications
 */
public class User implements MeetingObserver {
    private String userId;
    private String name;
    private String email;
    private Calendar calendar;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.calendar = new Calendar(userId, name + "'s Calendar");
    }

    @Override
    public void onMeetingScheduled(Meeting meeting) {
        System.out.println("📧 " + name + ": Meeting scheduled - " + meeting.getTitle());
    }

    @Override
    public void onMeetingCancelled(Meeting meeting) {
        System.out.println("📧 " + name + ": Meeting cancelled - " + meeting.getTitle());
    }

    @Override
    public void onMeetingRescheduled(Meeting meeting, TimeSlot oldSlot) {
        System.out.println("📧 " + name + ": Meeting rescheduled - " + meeting.getTitle());
    }

    @Override
    public void onConflictDetected(Meeting meeting) {
        System.out.println("⚠️  " + name + ": Conflict detected for - " + meeting.getTitle());
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public String toString() {
        return String.format("User %s - %s (%s)", userId, name, email);
    }
}
