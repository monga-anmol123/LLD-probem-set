package model;

import enums.AccountType;

/**
 * Checking Account with no interest and minimal restrictions
 */
public class CheckingAccount extends Account {
    private static final double MINIMUM_BALANCE = 100.0;
    private static final double WITHDRAWAL_LIMIT = Double.MAX_VALUE; // No limit
    private boolean overdraftProtection;
    private double overdraftLimit;
    
    public CheckingAccount(String accountNumber, String accountHolderName, double initialDeposit) {
        super(accountNumber, accountHolderName, initialDeposit, AccountType.CHECKING);
        
        if (initialDeposit < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                String.format("Initial deposit must be at least $%.2f for Checking Account", MINIMUM_BALANCE)
            );
        }
        
        this.overdraftProtection = false;
        this.overdraftLimit = 0.0;
    }
    
    public CheckingAccount(String accountNumber, String accountHolderName, double initialDeposit, 
                          boolean overdraftProtection, double overdraftLimit) {
        this(accountNumber, accountHolderName, initialDeposit);
        this.overdraftProtection = overdraftProtection;
        this.overdraftLimit = overdraftLimit;
    }
    
    @Override
    public boolean canWithdraw(double amount) {
        // Check if sufficient balance
        if (balance >= amount) {
            return true;
        }
        
        // Check overdraft protection
        if (overdraftProtection && (amount - balance) <= overdraftLimit) {
            return true;
        }
        
        System.out.println("❌ Insufficient balance for withdrawal");
        return false;
    }
    
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
    
    @Override
    public double getWithdrawalLimit() {
        return WITHDRAWAL_LIMIT;
    }
    
    public boolean hasOverdraftProtection() {
        return overdraftProtection;
    }
    
    public double getOverdraftLimit() {
        return overdraftLimit;
    }
    
    public void enableOverdraftProtection(double limit) {
        this.overdraftProtection = true;
        this.overdraftLimit = limit;
        System.out.println("✓ Overdraft protection enabled with limit: $" + limit);
    }
}


