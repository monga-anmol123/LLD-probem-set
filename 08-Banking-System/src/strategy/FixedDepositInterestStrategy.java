package strategy;

import model.Account;
import model.FixedDepositAccount;

/**
 * Interest calculation strategy for Fixed Deposit Accounts
 * 7% annual interest, calculated at maturity
 */
public class FixedDepositInterestStrategy implements InterestStrategy {
    private static final double ANNUAL_RATE = 0.07; // 7%
    
    @Override
    public double calculateInterest(Account account) {
        if (!(account instanceof FixedDepositAccount)) {
            return 0.0;
        }
        
        FixedDepositAccount fdAccount = (FixedDepositAccount) account;
        
        // Only calculate interest if account has matured
        if (!fdAccount.isMatured()) {
            return 0.0;
        }
        
        double balance = account.getBalance();
        double interest = balance * ANNUAL_RATE; // Full year interest
        
        return Math.round(interest * 100.0) / 100.0; // Round to 2 decimal places
    }
    
    @Override
    public String getStrategyName() {
        return "Fixed Deposit Interest Strategy (7% annual, at maturity)";
    }
}


