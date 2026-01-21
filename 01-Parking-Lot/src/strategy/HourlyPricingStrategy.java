package strategy;

import model.ParkingTicket;
import enums.VehicleType;
import java.util.Map;

public class HourlyPricingStrategy implements PricingStrategy {
    private static final Map<VehicleType, Double> HOURLY_RATES = Map.of(
        VehicleType.BIKE, 10.0,
        VehicleType.CAR, 20.0,
        VehicleType.TRUCK, 50.0
    );
    
    @Override
    public double calculateFee(ParkingTicket ticket) {
        long minutes = ticket.getParkingDurationInMinutes();
        double hours = Math.ceil(minutes / 60.0);
        VehicleType type = ticket.getVehicle().getType();
        return hours * HOURLY_RATES.get(type);
    }
}


