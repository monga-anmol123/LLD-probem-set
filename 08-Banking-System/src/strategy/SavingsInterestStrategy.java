package strategy;

import model.Account;
import model.SavingsAccount;

/**
 * Interest calculation strategy for Savings Accounts
 * 4% annual interest, calculated monthly
 */
public class SavingsInterestStrategy implements InterestStrategy {
    private static final double ANNUAL_RATE = 0.04; // 4%
    private static final int MONTHS_IN_YEAR = 12;
    
    @Override
    public double calculateInterest(Account account) {
        if (!(account instanceof SavingsAccount)) {
            return 0.0;
        }
        
        double balance = account.getBalance();
        double monthlyRate = ANNUAL_RATE / MONTHS_IN_YEAR;
        double interest = balance * monthlyRate;
        
        return Math.round(interest * 100.0) / 100.0; // Round to 2 decimal places
    }
    
    @Override
    public String getStrategyName() {
        return "Savings Interest Strategy (4% annual, monthly calculation)";
    }
}


