package strategy;

import model.Room;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class WeekendPricingStrategy implements PricingStrategy {
    private static final double WEEKEND_MULTIPLIER = 1.5;
    
    @Override
    public double calculatePrice(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double basePrice = room.getRoomType().getBasePrice();
        double totalPrice = 0;
        
        LocalDate currentDate = checkInDate;
        for (int i = 0; i < nights; i++) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY) {
                totalPrice += basePrice * WEEKEND_MULTIPLIER;
            } else {
                totalPrice += basePrice;
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return totalPrice;
    }
    
    @Override
    public String getStrategyName() {
        return "Weekend Pricing (1.5x on Fri/Sat)";
    }
}
