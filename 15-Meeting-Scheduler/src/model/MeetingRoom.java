package model;

/**
 * Represents a meeting room with capacity
 */
public class MeetingRoom {
    private String roomId;
    private String name;
    private int capacity;
    private String location;
    private Calendar calendar;

    public MeetingRoom(String roomId, String name, int capacity, String location) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.calendar = new Calendar(roomId, name + " Calendar");
    }

    public boolean canAccommodate(int attendeeCount) {
        return attendeeCount <= capacity;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public String toString() {
        return String.format("Room %s - %s (Capacity: %d) at %s", 
                roomId, name, capacity, location);
    }
}
