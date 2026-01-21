package model;

import enums.SeatStatus;
import enums.SeatType;

/**
 * Represents a seat in a screen
 */
public class Seat {
    private String seatId;
    private String row;
    private int column;
    private SeatType type;
    private SeatStatus status;
    private double basePrice;
    private long lockExpiryTime;
    private String lockedByUserId;

    public Seat(String seatId, String row, int column, SeatType type, double basePrice) {
        this.seatId = seatId;
        this.row = row;
        this.column = column;
        this.type = type;
        this.basePrice = basePrice;
        this.status = SeatStatus.AVAILABLE;
        this.lockExpiryTime = 0;
        this.lockedByUserId = null;
    }

    /**
     * Lock the seat for a specific user
     * @param userId User who is locking the seat
     * @param lockDurationMinutes Duration for which seat should be locked
     * @return true if successfully locked, false otherwise
     */
    public synchronized boolean lock(String userId, int lockDurationMinutes) {
        checkAndReleaseLock();
        
        if (status == SeatStatus.AVAILABLE) {
            status = SeatStatus.LOCKED;
            lockedByUserId = userId;
            lockExpiryTime = System.currentTimeMillis() + (lockDurationMinutes * 60 * 1000L);
            return true;
        }
        return false;
    }

    /**
     * Check if lock has expired and release if needed
     */
    public synchronized void checkAndReleaseLock() {
        if (status == SeatStatus.LOCKED && System.currentTimeMillis() > lockExpiryTime) {
            status = SeatStatus.AVAILABLE;
            lockedByUserId = null;
            lockExpiryTime = 0;
        }
    }

    /**
     * Book the seat (convert from LOCKED to BOOKED)
     * @param userId User who is booking
     * @return true if successfully booked, false otherwise
     */
    public synchronized boolean book(String userId) {
        if (status == SeatStatus.LOCKED && userId.equals(lockedByUserId)) {
            status = SeatStatus.BOOKED;
            lockedByUserId = null;
            lockExpiryTime = 0;
            return true;
        }
        return false;
    }

    /**
     * Release the seat (make it available again)
     */
    public synchronized void release() {
        status = SeatStatus.AVAILABLE;
        lockedByUserId = null;
        lockExpiryTime = 0;
    }

    /**
     * Block the seat (for maintenance, etc.)
     */
    public void block() {
        status = SeatStatus.BLOCKED;
    }

    public String getSeatId() {
        return seatId;
    }

    public String getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public SeatType getType() {
        return type;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getLockedByUserId() {
        return lockedByUserId;
    }

    @Override
    public String toString() {
        return row + column + " (" + type + "-" + status + ")";
    }
}


