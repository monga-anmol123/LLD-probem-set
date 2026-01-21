package factory;

import model.Seat;
import enums.SeatType;

/**
 * Factory for creating different types of seats
 * Demonstrates Factory Pattern
 */
public class SeatFactory {
    
    // Base prices for different seat types
    private static final double REGULAR_BASE_PRICE = 100.0;
    private static final double PREMIUM_BASE_PRICE = 150.0;
    private static final double VIP_BASE_PRICE = 200.0;
    private static final double WHEELCHAIR_BASE_PRICE = 100.0;

    /**
     * Create a seat of specified type
     * @param seatId Unique seat identifier
     * @param row Row identifier
     * @param column Column number
     * @param type Type of seat
     * @return Seat object with appropriate base price
     */
    public static Seat createSeat(String seatId, String row, int column, SeatType type) {
        double basePrice;
        
        switch (type) {
            case REGULAR:
                basePrice = REGULAR_BASE_PRICE;
                break;
            case PREMIUM:
                basePrice = PREMIUM_BASE_PRICE;
                break;
            case VIP:
                basePrice = VIP_BASE_PRICE;
                break;
            case WHEELCHAIR:
                basePrice = WHEELCHAIR_BASE_PRICE;
                break;
            default:
                basePrice = REGULAR_BASE_PRICE;
        }
        
        return new Seat(seatId, row, column, type, basePrice);
    }

    /**
     * Create a regular seat
     */
    public static Seat createRegularSeat(String seatId, String row, int column) {
        return createSeat(seatId, row, column, SeatType.REGULAR);
    }

    /**
     * Create a premium seat
     */
    public static Seat createPremiumSeat(String seatId, String row, int column) {
        return createSeat(seatId, row, column, SeatType.PREMIUM);
    }

    /**
     * Create a VIP seat
     */
    public static Seat createVIPSeat(String seatId, String row, int column) {
        return createSeat(seatId, row, column, SeatType.VIP);
    }

    /**
     * Create a wheelchair accessible seat
     */
    public static Seat createWheelchairSeat(String seatId, String row, int column) {
        return createSeat(seatId, row, column, SeatType.WHEELCHAIR);
    }
}


