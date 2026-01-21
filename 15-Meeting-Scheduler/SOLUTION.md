# Solution: Meeting Scheduler

## 📐 Architecture Design

### Component Breakdown

```
15-Meeting-Scheduler/
├── enums/
│   ├── MeetingStatus.java        # SCHEDULED, CONFIRMED, CANCELLED, etc.
│   ├── RecurrenceType.java       # NONE, DAILY, WEEKLY, MONTHLY
│   └── ConflictResolution.java   # REJECT, NOTIFY, AUTO_RESCHEDULE
├── model/
│   ├── TimeSlot.java             # Time range with overlap detection
│   ├── User.java                 # User with calendar (Observer)
│   ├── MeetingRoom.java          # Room with capacity and calendar
│   ├── Calendar.java             # Holds meetings, finds free slots
│   └── Meeting.java              # Meeting with participants and room
├── observer/
│   ├── MeetingObserver.java      # Observer interface
│   └── MeetingSubject.java       # Subject interface
├── strategy/
│   ├── ConflictStrategy.java     # Strategy interface
│   ├── RejectStrategy.java       # Reject conflicting meetings
│   ├── NotifyStrategy.java       # Notify and schedule anyway
│   └── AutoRescheduleStrategy.java # Find next available slot
├── service/
│   └── MeetingSchedulerService.java # Main service (Singleton)
└── Main.java                     # Demo with 14 scenarios
```

---

## 🎨 Design Patterns Explained

### 1. Singleton Pattern

**Implementation:**
```java
public class MeetingSchedulerService {
    private static MeetingSchedulerService instance;
    
    private MeetingSchedulerService() {
        // Private constructor
    }
    
    public static synchronized MeetingSchedulerService getInstance() {
        if (instance == null) {
            instance = new MeetingSchedulerService();
        }
        return instance;
    }
}
```

**Why Singleton?**
- Single source of truth for all meetings
- Centralized conflict detection
- Global access from anywhere in application
- Prevents multiple scheduler instances

**Benefits:**
- Consistent state management
- Easy to test and mock
- Thread-safe with synchronized

---

### 2. Observer Pattern

**Implementation:**
```java
// Observer interface
public interface MeetingObserver {
    void onMeetingScheduled(Meeting meeting);
    void onMeetingCancelled(Meeting meeting);
    void onMeetingRescheduled(Meeting meeting, TimeSlot oldSlot);
    void onConflictDetected(Meeting meeting);
}

// User implements Observer
public class User implements MeetingObserver {
    @Override
    public void onMeetingScheduled(Meeting meeting) {
        System.out.println("Meeting scheduled: " + meeting.getTitle());
    }
    // ... other methods
}

// Service notifies observers
private void notifyMeetingScheduled(Meeting meeting) {
    for (User participant : meeting.getParticipants()) {
        participant.onMeetingScheduled(meeting);
    }
}
```

**Why Observer?**
- Decouple meeting events from notifications
- Participants automatically notified
- Easy to add new notification types
- Follows open-closed principle

**Benefits:**
- Loose coupling between scheduler and users
- Extensible notification system
- Real-time updates
- Can add email, SMS, push notifications easily

---

### 3. Strategy Pattern

**Implementation:**
```java
// Strategy interface
public interface ConflictStrategy {
    Meeting handleConflict(Meeting newMeeting, List<User> participants, 
                          MeetingRoom room, Object service);
    String getStrategyName();
}

// Concrete strategies
public class RejectStrategy implements ConflictStrategy {
    @Override
    public Meeting handleConflict(...) {
        throw new RuntimeException("Cannot schedule - conflicts detected");
    }
}

public class NotifyStrategy implements ConflictStrategy {
    @Override
    public Meeting handleConflict(...) {
        // Notify participants and schedule anyway
        return newMeeting;
    }
}

public class AutoRescheduleStrategy implements ConflictStrategy {
    @Override
    public Meeting handleConflict(...) {
        // Find next available slot and reschedule
        return rescheduledMeeting;
    }
}

// Service uses strategy
public void setConflictStrategy(ConflictStrategy strategy) {
    this.conflictStrategy = strategy;
}
```

**Why Strategy?**
- Different organizations have different conflict policies
- Runtime strategy selection
- Easy to add new strategies
- Testable in isolation

**Benefits:**
- Flexible conflict handling
- No if-else chains
- Each strategy is independent
- Easy to extend

---

## 🧠 Key Algorithms

### 1. Time Slot Overlap Detection

```java
public boolean overlapsWith(TimeSlot other) {
    // No overlap if:
    // - This ends before other starts
    // - This starts after other ends
    return !(this.endTime.isBefore(other.startTime) || 
             this.endTime.isEqual(other.startTime) ||
             this.startTime.isAfter(other.endTime) || 
             this.startTime.isEqual(other.endTime));
}
```

**Time Complexity:** O(1)

**Explanation:**
- Two time slots overlap if they share any time
- Easier to check when they DON'T overlap
- Edge case: Adjacent slots (10:00-11:00 and 11:00-12:00) don't overlap

---

### 2. Find Free Slots Algorithm

```java
public List<TimeSlot> getFreeSlotsInRange(LocalDateTime start, 
                                         LocalDateTime end,
                                         long durationMinutes) {
    List<TimeSlot> freeSlots = new ArrayList<>();
    
    // Get all busy slots, sorted by start time
    List<TimeSlot> busySlots = meetings.stream()
        .filter(m -> m.getStatus() == SCHEDULED || m.getStatus() == CONFIRMED)
        .map(Meeting::getTimeSlot)
        .sorted(Comparator.comparing(TimeSlot::getStartTime))
        .collect(Collectors.toList());
    
    LocalDateTime currentTime = start;
    
    // Find gaps between busy slots
    for (TimeSlot busy : busySlots) {
        if (busy.getStartTime().isAfter(end)) break;
        
        if (currentTime.isBefore(busy.getStartTime())) {
            long availableMinutes = Duration.between(
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
        long availableMinutes = Duration.between(currentTime, end).toMinutes();
        if (availableMinutes >= durationMinutes) {
            freeSlots.add(new TimeSlot(currentTime, end));
        }
    }
    
    return freeSlots;
}
```

**Time Complexity:** O(n log n) where n = number of meetings
- Sorting: O(n log n)
- Iteration: O(n)

**Space Complexity:** O(n) for storing busy slots

---

### 3. Find Common Free Slots (Multiple Participants)

```java
public List<TimeSlot> findCommonFreeSlots(List<User> participants, 
                                         LocalDateTime start,
                                         LocalDateTime end,
                                         long durationMinutes) {
    if (participants.isEmpty()) return new ArrayList<>();
    
    // Get free slots for first participant
    List<TimeSlot> commonSlots = participants.get(0)
        .getCalendar()
        .getFreeSlotsInRange(start, end, durationMinutes);
    
    // Intersect with other participants' free slots
    for (int i = 1; i < participants.size(); i++) {
        List<TimeSlot> participantSlots = participants.get(i)
            .getCalendar()
            .getFreeSlotsInRange(start, end, durationMinutes);
        
        commonSlots = intersectTimeSlots(commonSlots, participantSlots, 
                                        durationMinutes);
    }
    
    return commonSlots;
}

private List<TimeSlot> intersectTimeSlots(List<TimeSlot> slots1, 
                                         List<TimeSlot> slots2,
                                         long durationMinutes) {
    List<TimeSlot> result = new ArrayList<>();
    
    for (TimeSlot slot1 : slots1) {
        for (TimeSlot slot2 : slots2) {
            if (slot1.overlapsWith(slot2)) {
                LocalDateTime start = slot1.getStartTime().isAfter(slot2.getStartTime()) ?
                        slot1.getStartTime() : slot2.getStartTime();
                LocalDateTime end = slot1.getEndTime().isBefore(slot2.getEndTime()) ?
                        slot1.getEndTime() : slot2.getEndTime();
                
                long availableMinutes = Duration.between(start, end).toMinutes();
                if (availableMinutes >= durationMinutes) {
                    result.add(new TimeSlot(start, end));
                }
            }
        }
    }
    
    return result;
}
```

**Time Complexity:** O(p × n log n + p × m²)
- p = number of participants
- n = average meetings per participant
- m = average free slots per participant

**Optimization Ideas:**
1. Use interval trees for faster overlap detection
2. Merge overlapping free slots before intersection
3. Early termination if no common slots found

---

## 🔄 Recurring Meetings Implementation

```java
public List<Meeting> scheduleRecurringMeeting(String title, 
                                             String description,
                                             TimeSlot firstSlot,
                                             String organizerId,
                                             List<String> participantIds,
                                             String roomId,
                                             RecurrenceType recurrence,
                                             int occurrences) {
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
            // Log and continue with next occurrence
        }
    }
    
    return recurringMeetings;
}

private TimeSlot getNextOccurrence(TimeSlot slot, RecurrenceType recurrence) {
    LocalDateTime start = slot.getStartTime();
    LocalDateTime end = slot.getEndTime();
    
    switch (recurrence) {
        case DAILY:   return new TimeSlot(start.plusDays(1), end.plusDays(1));
        case WEEKLY:  return new TimeSlot(start.plusWeeks(1), end.plusWeeks(1));
        case MONTHLY: return new TimeSlot(start.plusMonths(1), end.plusMonths(1));
        default:      return slot;
    }
}
```

**Key Design Decisions:**
1. Each occurrence is a separate `Meeting` object
2. All occurrences share the same recurrence type
3. Conflicts are handled per occurrence
4. Failed occurrences don't stop the series

---

## ⚖️ Trade-offs & Design Decisions

### 1. In-Memory Storage vs Database

**Current:** In-memory Maps
```java
private Map<String, User> users;
private Map<String, Meeting> meetings;
private Map<String, MeetingRoom> rooms;
```

**Pros:**
- Fast access (O(1))
- Simple implementation
- No database setup needed
- Perfect for interviews

**Cons:**
- Data lost on restart
- No persistence
- Limited scalability

**Production Alternative:**
```sql
CREATE TABLE meetings (
    meeting_id VARCHAR PRIMARY KEY,
    title VARCHAR,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    organizer_id VARCHAR,
    room_id VARCHAR,
    status VARCHAR,
    recurrence VARCHAR
);

CREATE INDEX idx_meetings_time ON meetings(start_time, end_time);
CREATE INDEX idx_meetings_organizer ON meetings(organizer_id);
```

---

### 2. Conflict Detection Timing

**Current:** Check at schedule time
```java
if (hasConflict) {
    meeting = conflictStrategy.handleConflict(...);
}
```

**Pros:**
- Simple logic
- Immediate feedback
- No locking needed

**Cons:**
- Race conditions possible
- No pessimistic locking

**Production Alternative:**
```java
public Meeting scheduleMeeting(...) {
    // Acquire lock on time slot
    Lock lock = lockManager.acquireLock(timeSlot);
    try {
        // Check conflicts and schedule
        return doSchedule(...);
    } finally {
        lock.release();
    }
}
```

---

### 3. Free Slot Algorithm

**Current:** Linear scan with sorting
- Time: O(n log n)
- Space: O(n)

**Alternative 1: Interval Tree**
```java
public class IntervalTree {
    // O(log n) insertion
    // O(log n + k) overlap query (k = overlaps)
}
```

**Alternative 2: Pre-computed Availability**
```java
public class AvailabilityCache {
    // Pre-compute free slots for common time ranges
    // Trade memory for speed
    private Map<String, List<TimeSlot>> cachedSlots;
}
```

---

## 🚀 Scalability Considerations

### 1. Database Sharding

```
Shard by user_id:
- User 1-1000 → DB1
- User 1001-2000 → DB2
- User 2001-3000 → DB3

Challenges:
- Cross-shard meetings
- Distributed transactions
```

### 2. Caching Strategy

```java
public class MeetingCache {
    private Cache<String, Meeting> meetingCache;
    private Cache<String, List<TimeSlot>> availabilityCache;
    
    public Meeting getMeeting(String id) {
        return meetingCache.get(id, 
            () -> database.loadMeeting(id));
    }
}
```

### 3. Event-Driven Architecture

```
Meeting Scheduled Event
    ↓
Kafka Topic
    ↓
├── Notification Service → Send emails
├── Calendar Service → Update calendars
└── Analytics Service → Track metrics
```

---

## 🎯 Interview Discussion Points

### 1. Time Zone Handling

**Question:** "How would you handle meetings across time zones?"

**Answer:**
```java
public class TimeSlot {
    private ZonedDateTime startTime;  // Store with timezone
    private ZonedDateTime endTime;
    
    public TimeSlot convertToUserTimeZone(ZoneId userZone) {
        return new TimeSlot(
            startTime.withZoneSameInstant(userZone),
            endTime.withZoneSameInstant(userZone)
        );
    }
    
    public boolean overlapsWith(TimeSlot other) {
        // Convert both to UTC for comparison
        ZonedDateTime thisStartUTC = startTime.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime otherStartUTC = other.startTime.withZoneSameInstant(ZoneOffset.UTC);
        // ... compare in UTC
    }
}
```

---

### 2. Optimizing Free Slot Finding

**Question:** "How would you optimize finding free slots for 100 participants?"

**Answer:**
1. **Parallel Processing:**
```java
List<CompletableFuture<List<TimeSlot>>> futures = participants.stream()
    .map(user -> CompletableFuture.supplyAsync(
        () -> user.getCalendar().getFreeSlotsInRange(start, end, duration)
    ))
    .collect(Collectors.toList());

List<List<TimeSlot>> allSlots = futures.stream()
    .map(CompletableFuture::join)
    .collect(Collectors.toList());
```

2. **Early Termination:**
```java
// Stop if no common slots remain
if (commonSlots.isEmpty()) {
    return Collections.emptyList();
}
```

3. **Caching:**
```java
// Cache busy slots for frequently queried users
private Cache<String, List<TimeSlot>> busySlotsCache;
```

---

### 3. Handling Meeting Series

**Question:** "How would you handle recurring meetings with exceptions?"

**Answer:**
```java
public class MeetingSeries {
    private String seriesId;
    private Meeting template;
    private RecurrenceRule rule;
    private Set<LocalDate> cancelledDates;
    private Map<LocalDate, Meeting> modifiedInstances;
    
    public List<Meeting> getOccurrences(LocalDate start, LocalDate end) {
        List<Meeting> occurrences = new ArrayList<>();
        
        LocalDate current = start;
        while (!current.isAfter(end)) {
            if (cancelledDates.contains(current)) {
                // Skip cancelled
            } else if (modifiedInstances.containsKey(current)) {
                // Use modified instance
                occurrences.add(modifiedInstances.get(current));
            } else {
                // Generate from template
                occurrences.add(generateOccurrence(current));
            }
            
            current = rule.getNextOccurrence(current);
        }
        
        return occurrences;
    }
}
```

---

## 📊 Complexity Analysis

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| Schedule Meeting | O(n + m) | O(1) |
| Cancel Meeting | O(p) | O(1) |
| Find Free Slots | O(n log n) | O(n) |
| Common Free Slots | O(p × n log n) | O(p × n) |
| Check Conflict | O(n) | O(1) |
| Reschedule | O(n + m) | O(1) |

Where:
- n = number of meetings
- m = number of rooms
- p = number of participants

---

## 🎓 Key Takeaways

1. **Singleton** for centralized state management
2. **Observer** for decoupled notifications
3. **Strategy** for flexible conflict resolution
4. **Time slot overlap** is a fundamental algorithm
5. **Free slot finding** requires efficient interval processing
6. **Recurring meetings** need careful state management
7. **Scalability** requires caching, sharding, and async processing

---

**This solution demonstrates production-ready meeting scheduling with clean architecture and extensible design!** 📅
