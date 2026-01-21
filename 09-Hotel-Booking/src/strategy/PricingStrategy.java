package strategy;

import model.Room;
import java.time.LocalDate;

public interface PricingStrategy {
    double calculatePrice(Room room, LocalDate checkInDate, LocalDate checkOutDate);
    String getStrategyName();
}
