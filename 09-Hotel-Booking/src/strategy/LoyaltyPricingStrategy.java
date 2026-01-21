package strategy;

import model.Room;
import model.Guest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LoyaltyPricingStrategy implements PricingStrategy {
    private final Guest guest;
    private static final double LOYALTY_DISCOUNT = 0.15; // 15% off
    
    public LoyaltyPricingStrategy(Guest guest) {
        this.guest = guest;
    }
    
    @Override
    public double calculatePrice(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double basePrice = room.getRoomType().getBasePrice() * nights;
        
        if (guest.hasLoyaltyStatus()) {
            return basePrice * (1 - LOYALTY_DISCOUNT);
        }
        
        return basePrice;
    }
    
    @Override
    public String getStrategyName() {
        return "Loyalty Pricing (15% off for loyalty members)";
    }
}
