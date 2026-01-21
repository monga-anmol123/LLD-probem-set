package enums;

/**
 * Enum representing the status of a seat
 */
public enum SeatStatus {
    AVAILABLE,    // Seat is available for booking
    LOCKED,       // Seat is temporarily locked during booking process
    BOOKED,       // Seat is booked
    BLOCKED       // Seat is blocked (maintenance, etc.)
}


