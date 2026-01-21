package strategy;

import model.Account;

/**
 * Strategy interface for calculating interest on different account types
 */
public interface InterestStrategy {
    /**
     * Calculate interest for the given account
     * @param account The account to calculate interest for
     * @return The interest amount
     */
    double calculateInterest(Account account);
    
    /**
     * Get the name of this interest strategy
     * @return Strategy name
     */
    String getStrategyName();
}


