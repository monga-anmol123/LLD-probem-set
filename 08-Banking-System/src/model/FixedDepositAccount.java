package model;

import enums.AccountType;
import java.time.LocalDate;

/**
 * Fixed Deposit Account with high interest but no withdrawals before maturity
 */
public class FixedDepositAccount extends Account {
    private static final double MINIMUM_BALANCE = 10000.0;
    private static final double ANNUAL_INTEREST_RATE = 0.07; // 7%
    private static final int LOCK_IN_PERIOD_MONTHS = 12; // 1 year
    
    private LocalDate maturityDate;
    
    public FixedDepositAccount(String accountNumber, String accountHolderName, double initialDeposit) {
        super(accountNumber, accountHolderName, initialDeposit, AccountType.FIXED_DEPOSIT);
        
        if (initialDeposit < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                String.format("Initial deposit must be at least $%.2f for Fixed Deposit Account", MINIMUM_BALANCE)
            );
        }
        
        this.maturityDate = createdDate.plusMonths(LOCK_IN_PERIOD_MONTHS);
    }
    
    @Override
    public boolean canWithdraw(double amount) {
        LocalDate today = LocalDate.now();
        
        if (today.isBefore(maturityDate)) {
            System.out.println("❌ Cannot withdraw from Fixed Deposit before maturity date: " + maturityDate);
            return false;
        }
        
        if (amount > balance) {
            System.out.println("❌ Insufficient balance");
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
        return balance; // Can withdraw full amount at maturity
    }
    
    public double getAnnualInterestRate() {
        return ANNUAL_INTEREST_RATE;
    }
    
    public LocalDate getMaturityDate() {
        return maturityDate;
    }
    
    public boolean isMatured() {
        return !LocalDate.now().isBefore(maturityDate);
    }
    
    @Override
    public void displayAccountInfo() {
        super.displayAccountInfo();
        System.out.println("\n┌─────────────────────────────────────────────────────────┐");
        System.out.printf("│ Maturity Date     : %-35s │%n", maturityDate);
        System.out.printf("│ Status            : %-35s │%n", isMatured() ? "MATURED" : "LOCKED");
        System.out.printf("│ Interest Rate     : %.2f%% per annum%-23s │%n", ANNUAL_INTEREST_RATE * 100, "");
        System.out.println("└─────────────────────────────────────────────────────────┘");
    }
}


