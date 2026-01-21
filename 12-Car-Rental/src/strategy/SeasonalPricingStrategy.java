package strategy;

import model.Rental;
import java.time.Month;

public class SeasonalPricingStrategy implements PricingStrategy {
    private double peakSeasonMultiplier = 1.5;  // 50% increase during peak season
    private double offSeasonMultiplier = 0.85;  // 15% discount during off-season
    
    @Override
    public double calculatePrice(Rental rental) {
        // Base cost
        double baseCost = rental.calculateBaseCost();
        
        // Apply seasonal multiplier
        double seasonalMultiplier = getSeasonalMultiplier(rental.getPickupDate().getMonth());
        baseCost = baseCost * seasonalMultiplier;
        
        // Insurance cost
        double insuranceCost = rental.calculateInsuranceCost();
        
        // Add-ons cost
        double addOnsCost = rental.calculateAddOnsCost();
        
        // Late fee
        double lateFee = rental.calculateLateFee();
        
        // One-way fee
        double oneWayFee = rental.calculateOneWayFee();
        
        // Subtotal
        double subtotal = baseCost + insuranceCost + addOnsCost + lateFee + oneWayFee;
        
        // Apply membership discount (only on base cost)
        double discount = baseCost * rental.getCustomer().getMembershipDiscount();
        
        return subtotal - discount;
    }
    
    private double getSeasonalMultiplier(Month month) {
        // Peak season: June, July, August, December
        if (month == Month.JUNE || month == Month.JULY || 
            month == Month.AUGUST || month == Month.DECEMBER) {
            return peakSeasonMultiplier;
        }
        // Off-season: January, February
        else if (month == Month.JANUARY || month == Month.FEBRUARY) {
            return offSeasonMultiplier;
        }
        // Regular season
        return 1.0;
    }
    
    @Override
    public String getStrategyName() {
        return "Seasonal Pricing (Peak: +50%, Off: -15%)";
    }
}


