# Problem 15: Meeting Scheduler

## 🎯 Problem Statement

Design and implement a **Meeting Scheduler System** similar to Google Calendar, Microsoft Outlook, or Calendly. The system should support scheduling meetings with multiple participants, booking meeting rooms, checking availability, handling conflicts, and supporting recurring meetings.

---

## 📋 Functional Requirements (FR)

### Core Features

1. **User Management**
   - Register users with calendars
   - Each user has their own calendar
   - Track user availability

2. **Meeting Room Management**
   - Add meeting rooms with capacity
   - Track room availability
   - Find available rooms for time slots

3. **Meeting Scheduling**
   - Schedule meetings with title, description, time slot
   - Add participants to meetings
   - Book meeting rooms
   - Organizer and participant roles

4. **Conflict Detection & Resolution**
   - Detect scheduling conflicts (participant or room)
   - Three conflict resolution strategies:
     - **REJECT**: Reject the conflicting meeting
     - **NOTIFY**: Notify participants and schedule anyway
     - **AUTO_RESCHEDULE**: Automatically find next available slot

5. **Availability Management**
   - Check user availability
   - Find common free slots for multiple participants
   - Check room availability

6. **Meeting Operations**
   - Reschedule meetings
   - Cancel meetings
   - View user's meeting schedule

7. **Recurring Meetings**
   - Support DAILY, WEEKLY, MONTHLY recurrence
   - Schedule multiple occurrences automatically

8. **Notifications (Observer Pattern)**
   - Notify participants when meeting is scheduled
   - Notify when meeting is cancelled
   - Notify when meeting is rescheduled
   - Notify when conflicts are detected

---

## 🔧 Non-Functional Requirements (NFR)

1. **Extensibility**
   - Easy to add new conflict resolution strategies
   - Support for different notification channels
   - Pluggable calendar integrations

2. **Scalability**
   - Efficient free slot finding algorithm
   - Handle large number of meetings
   - Support multiple concurrent users

3. **Reliability**
   - Accurate conflict detection
   - Consistent calendar state
   - No double-booking

4. **Usability**
   - Clear error messages
   - Intuitive API
   - Helpful notifications

---

## 🎨 Design Patterns Used

### 1. **Singleton Pattern**
- **Where:** MeetingSchedulerService
- **Why:** Single instance managing all meetings, users, and rooms
- **Benefit:** Global access, centralized state management

### 2. **Observer Pattern**
- **Where:** User implements MeetingObserver
- **Why:** Notify participants of meeting events
- **Benefit:** Loose coupling, automatic notifications

### 3. **Strategy Pattern**
- **Where:** ConflictStrategy with 3 implementations
- **Why:** Different ways to handle scheduling conflicts
- **Benefit:** Runtime strategy selection, easy to add new strategies

---

## 📊 Architecture Overview

```
┌──────────────────────────────────────────────────────────────┐
│                         Main.java                             │
│                    (Demo/Entry Point)                         │
└───────────────────────┬──────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┬───────────────┐
        │               │               │               │
        ▼               ▼               ▼               ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐ ┌────────────┐
│   Service    │ │   Strategy  │ │  Observer  │ │   Model    │
│              │ │             │ │            │ │            │
│  Meeting     │ │  Conflict   │ │  Meeting   │ │   User     │
│  Scheduler   │ │  Strategy   │ │  Observer  │ │  (Observer)│
│  (Singleton) │ │             │ │            │ │            │
│              │ │   Reject    │ │            │ │  Meeting   │
│  - Schedule  │ │   Notify    │ │            │ │  Room      │
│  - Cancel    │ │   Auto      │ │            │ │  Calendar  │
│  - Reschedule│ │   Reschedule│ │            │ │  TimeSlot  │
│  - Find Free │ │             │ │            │ │            │
│    Slots     │ │             │ │            │ │            │
└──────────────┘ └─────────────┘ └────────────┘ └────────────┘
```

---

## 🚀 How to Run

### Compile
```bash
cd 15-Meeting-Scheduler/src/
javac enums/*.java observer/*.java model/*.java strategy/*.java service/*.java Main.java
```

### Run
```bash
java Main
```

---

## ✨ Key Features Demonstrated

### 1. **TimeSlot Management**
```java
TimeSlot slot = new TimeSlot(startTime, endTime);
boolean overlaps = slot1.overlapsWith(slot2);
long duration = slot.getDurationMinutes();
```

### 2. **Conflict Resolution Strategies**

**REJECT Strategy:**
```
Meeting scheduled at 10:00 AM
Try to schedule another at 10:00 AM → REJECTED
```

**NOTIFY Strategy:**
```
Meeting scheduled at 10:00 AM
Try to schedule another at 10:00 AM → NOTIFIED & SCHEDULED
All participants receive conflict notification
```

**AUTO_RESCHEDULE Strategy:**
```
Meeting scheduled at 10:00 AM
Try to schedule another at 10:00 AM → AUTO-RESCHEDULED to 11:00 AM
Participants notified of new time
```

### 3. **Find Common Free Slots**
```java
List<User> participants = Arrays.asList(alice, bob, charlie);
List<TimeSlot> freeSlots = scheduler.findCommonFreeSlots(
    participants, searchStart, searchEnd, durationMinutes
);
// Returns: [09:00-10:00, 11:00-17:00]
```

### 4. **Recurring Meetings**
```java
scheduler.scheduleRecurringMeeting(
    "Weekly Standup",
    "Team sync",
    timeSlot,
    organizerId,
    participantIds,
    roomId,
    RecurrenceType.WEEKLY,
    4 // 4 occurrences
);
```

### 5. **Observer Notifications**
```java
// User implements MeetingObserver
@Override
public void onMeetingScheduled(Meeting meeting) {
    System.out.println("Meeting scheduled: " + meeting.getTitle());
}

@Override
public void onMeetingCancelled(Meeting meeting) {
    System.out.println("Meeting cancelled: " + meeting.getTitle());
}
```

---

## 🧪 Test Scenarios (14 Total)

1. ✅ Register Users
2. ✅ Add Meeting Rooms
3. ✅ Schedule Simple Meeting
4. ✅ Check Room Availability
5. ✅ Find Common Free Slots
6. ✅ Conflict Resolution - REJECT Strategy
7. ✅ Conflict Resolution - NOTIFY Strategy
8. ✅ Conflict Resolution - AUTO_RESCHEDULE Strategy
9. ✅ Recurring Meetings (WEEKLY)
10. ✅ Reschedule Meeting
11. ✅ View User's Meetings
12. ✅ Cancel Meeting
13. ✅ Edge Case - Room Capacity Validation
14. ✅ Edge Case - Invalid User

---

## 📈 Extensions & Future Enhancements

### 1. **Time Zone Support**
```java
public class TimeSlot {
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private ZoneId timeZone;
}
```

### 2. **Meeting Reminders**
```java
public class ReminderService {
    public void scheduleReminder(Meeting meeting, int minutesBefore) {
        // Send reminder X minutes before meeting
    }
}
```

### 3. **Meeting Priorities**
```java
public enum MeetingPriority {
    LOW, MEDIUM, HIGH, URGENT
}
// Higher priority meetings can override lower priority ones
```

### 4. **Resource Booking (Projectors, Whiteboards)**
```java
public class Resource {
    private String resourceId;
    private String name;
    private Calendar calendar;
}
```

### 5. **External Calendar Integration**
```java
public interface CalendarProvider {
    List<Meeting> fetchMeetings(User user, TimeSlot range);
    void syncMeeting(Meeting meeting);
}

public class GoogleCalendarProvider implements CalendarProvider {
    // Integrate with Google Calendar API
}
```

### 6. **Meeting Templates**
```java
public class MeetingTemplate {
    private String name;
    private long defaultDuration;
    private List<String> defaultParticipants;
    private String defaultRoom;
}
```

### 7. **Availability Preferences**
```java
public class AvailabilityPreference {
    private User user;
    private Map<DayOfWeek, List<TimeSlot>> workingHours;
    private List<TimeSlot> blockedSlots;
}
```

---

## 🎓 Learning Objectives

After completing this problem, you should understand:

1. ✅ How to implement **Singleton pattern** for service management
2. ✅ How to use **Observer pattern** for event notifications
3. ✅ How to apply **Strategy pattern** for conflict resolution
4. ✅ How to design **time slot management** and overlap detection
5. ✅ How to implement **free slot finding algorithm**
6. ✅ How to handle **recurring events** with different patterns
7. ✅ How to manage **calendar state** and conflicts
8. ✅ How to design **extensible scheduling systems**

---

## 🏆 Interview Tips

### Common Questions

**Q: How would you handle time zones?**
```java
public class TimeSlot {
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    
    public TimeSlot convertToTimeZone(ZoneId targetZone) {
        return new TimeSlot(
            startTime.withZoneSameInstant(targetZone),
            endTime.withZoneSameInstant(targetZone)
        );
    }
}
```

**Q: How would you optimize finding free slots for many participants?**
```
1. Use interval tree for efficient overlap detection
2. Cache busy slots for frequently checked users
3. Parallel processing for independent calendars
4. Index meetings by time range for quick lookup
```

**Q: How would you scale this to millions of users?**
```
1. Database sharding by user_id
2. Caching layer (Redis) for recent meetings
3. Event-driven architecture (Kafka)
4. Microservices (User Service, Meeting Service, Notification Service)
5. Read replicas for availability queries
```

**Q: How would you handle meeting conflicts across time zones?**
```java
public class ConflictDetector {
    public boolean hasConflict(Meeting m1, Meeting m2) {
        // Convert both meetings to UTC for comparison
        TimeSlot slot1UTC = m1.getTimeSlot().toUTC();
        TimeSlot slot2UTC = m2.getTimeSlot().toUTC();
        return slot1UTC.overlapsWith(slot2UTC);
    }
}
```

**Q: How would you implement meeting series (recurring with exceptions)?**
```java
public class MeetingSeries {
    private String seriesId;
    private Meeting template;
    private RecurrenceRule rule;
    private List<LocalDate> exceptions; // Cancelled dates
    private Map<LocalDate, Meeting> modifications; // Modified instances
}
```

---

## ⚖️ Trade-offs

| Aspect | Current Approach | Alternative | Trade-off |
|--------|-----------------|-------------|-----------|
| **Storage** | In-memory Maps | Database | Speed vs Persistence |
| **Free Slots** | Intersection algorithm | Pre-computed availability | Accuracy vs Performance |
| **Conflicts** | Check at schedule time | Lock time slots | Simple vs Concurrent |
| **Notifications** | Synchronous | Async queue | Simple vs Scalable |

---

## 📚 Related Problems

- **Problem 01:** Parking Lot (Resource allocation)
- **Problem 14:** Elevator System (Scheduling algorithm)
- **Problem 29:** Task Scheduler (Recurring tasks)

---

**Difficulty:** ⭐⭐⭐ Medium-Hard  
**Time to Solve:** 60-75 minutes  
**Design Patterns:** Singleton, Observer, Strategy  
**Key Concepts:** Time management, Conflict resolution, Calendar algorithms

---

**Companies:** Google, Microsoft, Amazon, Calendly, Zoom

*This is a production-grade meeting scheduler demonstrating real-world calendar management!* 📅
