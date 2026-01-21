package model;

import enums.AccountType;

/**
 * Savings Account with 4% annual interest and withdrawal limits
 */
public class SavingsAccount extends Account {
    private static final double MINIMUM_BALANCE = 500.0;
    private static final double WITHDRAWAL_LIMIT = 5000.0;
    private static final double ANNUAL_INTEREST_RATE = 0.04; // 4%
    
    public SavingsAccount(String accountNumber, String accountHolderName, double initialDeposit) {
        super(accountNumber, accountHolderName, initialDeposit, AccountType.SAVINGS);
        
        if (initialDeposit < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                String.format("Initial deposit must be at least $%.2f for Savings Account", MINIMUM_BALANCE)
            );
        }
    }
    
    @Override
    public boolean canWithdraw(double amount) {
        // Check withdrawal limit
        if (amount > WITHDRAWAL_LIMIT) {
            System.out.println("❌ Withdrawal exceeds limit of $" + WITHDRAWAL_LIMIT);
            return false;
        }
        
        // Check if balance after withdrawal meets minimum balance requirement
        if (balance - amount < MINIMUM_BALANCE) {
            System.out.println("❌ Withdrawal would violate minimum balance requirement of $" + MINIMUM_BALANCE);
            return false;
        }
        
        return true;
    }
    
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
    
    @Override
    public double getWithdrawalLimit() {
        return WITHDRAWAL_LIMIT;
    }
    
    public double getAnnualInterestRate() {
        return ANNUAL_INTEREST_RATE;
    }
}


