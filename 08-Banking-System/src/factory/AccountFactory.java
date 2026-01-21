package factory;

import model.*;
import enums.AccountType;

/**
 * Factory class for creating different types of bank accounts
 * Implements Factory Design Pattern
 */
public class AccountFactory {
    
    /**
     * Create an account based on the account type
     * 
     * @param type The type of account to create
     * @param accountNumber Unique account number
     * @param holderName Name of the account holder
     * @param initialDeposit Initial deposit amount
     * @return A new Account instance
     * @throws IllegalArgumentException if account type is invalid
     */
    public static Account createAccount(AccountType type, String accountNumber, 
                                       String holderName, double initialDeposit) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be empty");
        }
        
        if (holderName == null || holderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty");
        }
        
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }
        
        switch (type) {
            case SAVINGS:
                return new SavingsAccount(accountNumber, holderName, initialDeposit);
                
            case CHECKING:
                return new CheckingAccount(accountNumber, holderName, initialDeposit);
                
            case FIXED_DEPOSIT:
                return new FixedDepositAccount(accountNumber, holderName, initialDeposit);
                
            default:
                throw new IllegalArgumentException("Unknown account type: " + type);
        }
    }
    
    /**
     * Create a Checking Account with overdraft protection
     * 
     * @param accountNumber Unique account number
     * @param holderName Name of the account holder
     * @param initialDeposit Initial deposit amount
     * @param overdraftLimit Overdraft limit
     * @return A new CheckingAccount with overdraft protection
     */
    public static CheckingAccount createCheckingAccountWithOverdraft(
            String accountNumber, String holderName, double initialDeposit, double overdraftLimit) {
        return new CheckingAccount(accountNumber, holderName, initialDeposit, true, overdraftLimit);
    }
}


