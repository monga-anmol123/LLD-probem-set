package strategy;

import model.Rental;

public class StandardPricingStrategy implements PricingStrategy {
    
    @Override
    public double calculatePrice(Rental rental) {
        // Base cost
        double baseCost = rental.calculateBaseCost();
        
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
    
    @Override
    public String getStrategyName() {
        return "Standard Pricing";
    }
}


