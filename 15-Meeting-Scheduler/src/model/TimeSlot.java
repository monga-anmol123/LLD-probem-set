package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a time slot with start and end times
 */
public class TimeSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean overlapsWith(TimeSlot other) {
        return !(this.endTime.isBefore(other.startTime) || 
                 this.endTime.isEqual(other.startTime) ||
                 this.startTime.isAfter(other.endTime) || 
                 this.startTime.isEqual(other.endTime));
    }

    public boolean contains(LocalDateTime time) {
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }

    public long getDurationMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return startTime.format(FORMATTER) + " - " + endTime.format(FORMATTER);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TimeSlot)) return false;
        TimeSlot other = (TimeSlot) obj;
        return startTime.equals(other.startTime) && endTime.equals(other.endTime);
    }

    @Override
    public int hashCode() {
        return startTime.hashCode() + endTime.hashCode();
    }
}
