package model;

import enums.RoomType;

public class Room {
    private final String roomNumber;
    private final RoomType roomType;
    private final int floor;
    private boolean isAvailable;
    
    public Room(String roomNumber, RoomType roomType, int floor) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.floor = floor;
        this.isAvailable = true;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public RoomType getRoomType() {
        return roomType;
    }
    
    public int getFloor() {
        return floor;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    @Override
    public String toString() {
        return String.format("Room %s (%s) - Floor %d - $%.2f/night", 
            roomNumber, roomType, floor, roomType.getBasePrice());
    }
}
