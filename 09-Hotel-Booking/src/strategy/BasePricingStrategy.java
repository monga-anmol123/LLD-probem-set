package strategy;

import model.Room;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BasePricingStrategy implements PricingStrategy {
    
    @Override
    public double calculatePrice(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return room.getRoomType().getBasePrice() * nights;
    }
    
    @Override
    public String getStrategyName() {
        return "Base Pricing";
    }
}
