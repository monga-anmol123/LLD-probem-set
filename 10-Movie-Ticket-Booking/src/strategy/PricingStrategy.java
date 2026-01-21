package strategy;

import model.Seat;
import model.Show;

/**
 * Strategy interface for different pricing strategies
 * Demonstrates Strategy Pattern
 */
public interface PricingStrategy {
    
    /**
     * Calculate price for a seat in a show
     * @param seat The seat to price
     * @param show The show
     * @return Final price after applying strategy
     */
    double calculatePrice(Seat seat, Show show);
}


