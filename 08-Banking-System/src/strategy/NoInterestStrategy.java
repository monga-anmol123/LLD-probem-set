package strategy;

import model.Account;
import model.CheckingAccount;

/**
 * Interest calculation strategy for Checking Accounts
 * No interest paid
 */
public class NoInterestStrategy implements InterestStrategy {
    
    @Override
    public double calculateInterest(Account account) {
        if (!(account instanceof CheckingAccount)) {
            return 0.0;
        }
        
        // Checking accounts don't earn interest
        return 0.0;
    }
    
    @Override
    public String getStrategyName() {
        return "No Interest Strategy (Checking Account)";
    }
}


