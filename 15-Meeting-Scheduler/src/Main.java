import enums.*;
import model.*;
import observer.MeetingObserver;
import service.MeetingSchedulerService;
import strategy.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Demo application for Meeting Scheduler
 * Demonstrates Strategy, Observer, and Singleton patterns
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  MEETING SCHEDULER DEMO");
        System.out.println("========================================\n");
        
        // Initialize Meeting Scheduler Service (Singleton)
        MeetingSchedulerService scheduler = MeetingSchedulerService.getInstance();
        System.out.println("✓ Meeting Scheduler initialized\n");
        
        // Scenario 1: Register Users
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: REGISTERING USERS");
        System.out.println("========================================\n");
        
        User alice = new User("U001", "Alice", "alice@company.com");
        User bob = new User("U002", "Bob", "bob@company.com");
        User charlie = new User("U003", "Charlie", "charlie@company.com");
        User diana = new User("U004", "Diana", "diana@company.com");
        
        scheduler.registerUser(alice);
        scheduler.registerUser(bob);
        scheduler.registerUser(charlie);
        scheduler.registerUser(diana);
        System.out.println();
        
        // Scenario 2: Add Meeting Rooms
        System.out.println("========================================");
        System.out.println("  SCENARIO 2: ADDING MEETING ROOMS");
        System.out.println("========================================\n");
        
        MeetingRoom room1 = new MeetingRoom("R001", "Conference Room A", 10, "Floor 1");
        MeetingRoom room2 = new MeetingRoom("R002", "Conference Room B", 5, "Floor 2");
        MeetingRoom room3 = new MeetingRoom("R003", "Board Room", 20, "Floor 3");
        
        scheduler.addMeetingRoom(room1);
        scheduler.addMeetingRoom(room2);
        scheduler.addMeetingRoom(room3);
        System.out.println();
        
        // Scenario 3: Schedule Simple Meeting
        System.out.println("========================================");
        System.out.println("  SCENARIO 3: SCHEDULE SIMPLE MEETING");
        System.out.println("========================================\n");
        
        LocalDateTime tomorrow10am = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime tomorrow11am = tomorrow10am.plusHours(1);
        
        TimeSlot slot1 = new TimeSlot(tomorrow10am, tomorrow11am);
        
        Meeting meeting1 = scheduler.scheduleMeeting(
            "Project Kickoff",
            "Discuss project requirements",
            slot1,
            "U001", // Alice as organizer
            Arrays.asList("U002", "U003"), // Bob and Charlie
            "R001"
        );
        
        System.out.println("\n" + meeting1);
        
        // Scenario 4: Check Availability
        System.out.println("========================================");
        System.out.println("  SCENARIO 4: CHECK ROOM AVAILABILITY");
        System.out.println("========================================\n");
        
        LocalDateTime tomorrow2pm = tomorrow10am.withHour(14);
        LocalDateTime tomorrow3pm = tomorrow2pm.plusHours(1);
        TimeSlot slot2 = new TimeSlot(tomorrow2pm, tomorrow3pm);
        
        List<MeetingRoom> availableRooms = scheduler.getAvailableRooms(slot2, 3);
        System.out.println("Available rooms for " + slot2 + ":");
        for (MeetingRoom room : availableRooms) {
            System.out.println("  " + room);
        }
        System.out.println();
        
        // Scenario 5: Find Common Free Slots
        System.out.println("========================================");
        System.out.println("  SCENARIO 5: FIND COMMON FREE SLOTS");
        System.out.println("========================================\n");
        
        LocalDateTime searchStart = tomorrow10am.withHour(9);
        LocalDateTime searchEnd = searchStart.plusHours(8);
        
        List<User> participants = Arrays.asList(alice, bob, charlie);
        List<TimeSlot> freeSlots = scheduler.findCommonFreeSlots(
            participants, searchStart, searchEnd, 60
        );
        
        System.out.println("Common free slots for Alice, Bob, Charlie:");
        for (TimeSlot slot : freeSlots) {
            System.out.println("  " + slot);
        }
        System.out.println();
        
        // Scenario 6: Conflict Resolution - REJECT Strategy
        System.out.println("========================================");
        System.out.println("  SCENARIO 6: CONFLICT - REJECT STRATEGY");
        System.out.println("========================================\n");
        
        scheduler.setConflictStrategy(new RejectStrategy());
        
        try {
            // Try to schedule at same time as meeting1
            scheduler.scheduleMeeting(
                "Conflicting Meeting",
                "This should be rejected",
                slot1, // Same time as meeting1
                "U001", // Alice (already busy)
                Arrays.asList("U004"),
                null
            );
        } catch (RuntimeException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
        System.out.println();
        
        // Scenario 7: Conflict Resolution - NOTIFY Strategy
        System.out.println("========================================");
        System.out.println("  SCENARIO 7: CONFLICT - NOTIFY STRATEGY");
        System.out.println("========================================\n");
        
        scheduler.setConflictStrategy(new NotifyStrategy());
        
        Meeting meeting2 = scheduler.scheduleMeeting(
            "Urgent Discussion",
            "Important topic",
            slot1, // Same time as meeting1
            "U002", // Bob (already busy)
            Arrays.asList("U004"),
            null
        );
        
        System.out.println("\n" + meeting2);
        
        // Scenario 8: Conflict Resolution - AUTO_RESCHEDULE Strategy
        System.out.println("========================================");
        System.out.println("  SCENARIO 8: CONFLICT - AUTO_RESCHEDULE");
        System.out.println("========================================\n");
        
        scheduler.setConflictStrategy(new AutoRescheduleStrategy());
        
        Meeting meeting3 = scheduler.scheduleMeeting(
            "Team Sync",
            "Weekly sync meeting",
            slot1, // Same time as meeting1
            "U001", // Alice (already busy)
            Arrays.asList("U002", "U003"),
            "R002"
        );
        
        System.out.println("\n" + meeting3);
        
        // Scenario 9: Recurring Meetings
        System.out.println("========================================");
        System.out.println("  SCENARIO 9: RECURRING MEETINGS");
        System.out.println("========================================\n");
        
        scheduler.setConflictStrategy(new RejectStrategy()); // Reset to reject
        
        LocalDateTime nextWeek9am = tomorrow10am.plusDays(6).withHour(9);
        LocalDateTime nextWeek10am = nextWeek9am.plusHours(1);
        TimeSlot recurringSlot = new TimeSlot(nextWeek9am, nextWeek10am);
        
        List<Meeting> recurringMeetings = scheduler.scheduleRecurringMeeting(
            "Weekly Standup",
            "Team standup meeting",
            recurringSlot,
            "U001",
            Arrays.asList("U002", "U003", "U004"),
            "R001",
            RecurrenceType.WEEKLY,
            4 // 4 weeks
        );
        
        System.out.println("\nScheduled " + recurringMeetings.size() + " recurring meetings:");
        for (Meeting m : recurringMeetings) {
            System.out.println("  " + m.getMeetingId() + ": " + m.getTimeSlot());
        }
        System.out.println();
        
        // Scenario 10: Reschedule Meeting
        System.out.println("========================================");
        System.out.println("  SCENARIO 10: RESCHEDULE MEETING");
        System.out.println("========================================\n");
        
        LocalDateTime newTime = tomorrow10am.withHour(15);
        LocalDateTime newEndTime = newTime.plusHours(1);
        TimeSlot newSlot = new TimeSlot(newTime, newEndTime);
        
        System.out.println("Rescheduling meeting: " + meeting1.getMeetingId());
        System.out.println("Old time: " + meeting1.getTimeSlot());
        System.out.println("New time: " + newSlot);
        
        scheduler.rescheduleMeeting(meeting1.getMeetingId(), newSlot);
        System.out.println();
        
        // Scenario 11: View User's Meetings
        System.out.println("========================================");
        System.out.println("  SCENARIO 11: VIEW USER'S MEETINGS");
        System.out.println("========================================\n");
        
        LocalDateTime weekStart = tomorrow10am.withHour(0).withMinute(0);
        LocalDateTime weekEnd = weekStart.plusDays(7);
        
        System.out.println("Alice's meetings this week:");
        List<Meeting> aliceMeetings = scheduler.getUserMeetings("U001", weekStart, weekEnd);
        for (Meeting m : aliceMeetings) {
            System.out.println("  " + m.getTitle() + " - " + m.getTimeSlot() + 
                             " [" + m.getStatus() + "]");
        }
        System.out.println();
        
        // Scenario 12: Cancel Meeting
        System.out.println("========================================");
        System.out.println("  SCENARIO 12: CANCEL MEETING");
        System.out.println("========================================\n");
        
        System.out.println("Cancelling meeting: " + meeting2.getMeetingId());
        scheduler.cancelMeeting(meeting2.getMeetingId());
        System.out.println();
        
        // Scenario 13: Edge Case - Room Capacity
        System.out.println("========================================");
        System.out.println("  SCENARIO 13: EDGE CASE - ROOM CAPACITY");
        System.out.println("========================================\n");
        
        try {
            LocalDateTime future = tomorrow10am.plusDays(10).withHour(14);
            TimeSlot futureSlot = new TimeSlot(future, future.plusHours(1));
            
            // Try to book 10 people in a 5-person room
            scheduler.scheduleMeeting(
                "Large Team Meeting",
                "Too many people",
                futureSlot,
                "U001",
                Arrays.asList("U002", "U003", "U004", "U001", "U001", "U001", "U001", "U001", "U001"),
                "R002" // Room with capacity 5
            );
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        System.out.println();
        
        // Scenario 14: Edge Case - Invalid User
        System.out.println("========================================");
        System.out.println("  SCENARIO 14: EDGE CASE - INVALID USER");
        System.out.println("========================================\n");
        
        try {
            LocalDateTime future = tomorrow10am.plusDays(10).withHour(16);
            TimeSlot futureSlot = new TimeSlot(future, future.plusHours(1));
            
            scheduler.scheduleMeeting(
                "Invalid Meeting",
                "Should fail",
                futureSlot,
                "U999", // Non-existent user
                Arrays.asList("U001"),
                null
            );
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        System.out.println();
        
        // Statistics
        scheduler.showStatistics();
        
        // Final Summary
        System.out.println("========================================");
        System.out.println("  DEMO COMPLETED SUCCESSFULLY!");
        System.out.println("========================================");
        System.out.println("\n✅ Demonstrated Patterns:");
        System.out.println("  1. Singleton Pattern - MeetingSchedulerService");
        System.out.println("  2. Observer Pattern - Meeting notifications to participants");
        System.out.println("  3. Strategy Pattern - 3 conflict resolution strategies:");
        System.out.println("     • REJECT - Reject conflicting meetings");
        System.out.println("     • NOTIFY - Notify participants about conflicts");
        System.out.println("     • AUTO_RESCHEDULE - Automatically find next slot");
        System.out.println("\n✅ Features Demonstrated:");
        System.out.println("  • Schedule meetings with participants and rooms");
        System.out.println("  • Check room availability");
        System.out.println("  • Find common free slots for multiple participants");
        System.out.println("  • Conflict detection and resolution (3 strategies)");
        System.out.println("  • Recurring meetings (DAILY, WEEKLY, MONTHLY)");
        System.out.println("  • Reschedule meetings");
        System.out.println("  • Cancel meetings");
        System.out.println("  • View user's meeting schedule");
        System.out.println("  • Room capacity validation");
        System.out.println("  • Observer notifications for all events");
        System.out.println("  • Edge case handling");
        System.out.println("\n🎉 All 14 scenarios executed successfully!");
    }
}
