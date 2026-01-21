package service;

import model.*;
import enums.AccountType;
import enums.TransactionType;
import factory.AccountFactory;
import strategy.*;

import java.util.*;

/**
 * Main Bank service class implementing Singleton pattern
 * Manages all accounts and transactions
 */
public class Bank {
    private static Bank instance;
    
    private String bankName;
    private Map<String, Account> accounts;
    private Map<AccountType, InterestStrategy> interestStrategies;
    private int accountCounter;
    
    // Private constructor for Singleton
    private Bank(String bankName) {
        this.bankName = bankName;
        this.accounts = new HashMap<>();
        this.interestStrategies = new HashMap<>();
        this.accountCounter = 1000;
        
        // Initialize interest strategies
        initializeInterestStrategies();
    }
    
    /**
     * Get singleton instance of Bank
     */
    public static synchronized Bank getInstance(String bankName) {
        if (instance == null) {
            instance = new Bank(bankName);
        }
        return instance;
    }
    
    /**
     * Initialize interest calculation strategies for each account type
     */
    private void initializeInterestStrategies() {
        interestStrategies.put(AccountType.SAVINGS, new SavingsInterestStrategy());
        interestStrategies.put(AccountType.CHECKING, new NoInterestStrategy());
        interestStrategies.put(AccountType.FIXED_DEPOSIT, new FixedDepositInterestStrategy());
    }
    
    /**
     * Create a new account
     */
    public Account createAccount(AccountType type, String holderName, double initialDeposit) {
        String accountNumber = generateAccountNumber();
        
        try {
            Account account = AccountFactory.createAccount(type, accountNumber, holderName, initialDeposit);
            accounts.put(accountNumber, account);
            
            System.out.println("✓ Account created successfully!");
            System.out.println("  Account Number: " + accountNumber);
            System.out.println("  Account Type: " + type);
            System.out.println("  Initial Balance: $" + initialDeposit);
            
            return account;
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Failed to create account: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Generate unique account number
     */
    private String generateAccountNumber() {
        return "ACC" + String.format("%06d", accountCounter++);
    }
    
    /**
     * Get account by account number
     */
    public Account getAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account;
    }
    
    /**
     * Deposit money into an account
     */
    public void deposit(String accountNumber, double amount) {
        try {
            Account account = getAccount(accountNumber);
            account.deposit(amount);
            System.out.println("✓ Deposited $" + amount + " to account " + accountNumber);
            System.out.println("  New Balance: $" + account.getBalance());
        } catch (Exception e) {
            System.out.println("❌ Deposit failed: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Withdraw money from an account
     */
    public void withdraw(String accountNumber, double amount) {
        try {
            Account account = getAccount(accountNumber);
            account.withdraw(amount);
            System.out.println("✓ Withdrew $" + amount + " from account " + accountNumber);
            System.out.println("  New Balance: $" + account.getBalance());
        } catch (Exception e) {
            System.out.println("❌ Withdrawal failed: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Transfer money between accounts
     */
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            System.out.println("❌ Cannot transfer to the same account");
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        if (amount <= 0) {
            System.out.println("❌ Transfer amount must be positive");
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        try {
            Account fromAccount = getAccount(fromAccountNumber);
            Account toAccount = getAccount(toAccountNumber);
            
            // Check if withdrawal is possible
            if (!fromAccount.canWithdraw(amount)) {
                throw new IllegalStateException("Cannot transfer: Insufficient balance or exceeds limit");
            }
            
            // Perform transfer
            fromAccount.withdraw(amount);
            fromAccount.addTransaction(TransactionType.TRANSFER_OUT, amount);
            
            toAccount.deposit(amount);
            toAccount.addTransaction(TransactionType.TRANSFER_IN, amount);
            
            System.out.println("✓ Transferred $" + amount);
            System.out.println("  From: " + fromAccountNumber + " (Balance: $" + fromAccount.getBalance() + ")");
            System.out.println("  To: " + toAccountNumber + " (Balance: $" + toAccount.getBalance() + ")");
            
        } catch (Exception e) {
            System.out.println("❌ Transfer failed: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Apply interest to a specific account
     */
    public void applyInterest(String accountNumber) {
        try {
            Account account = getAccount(accountNumber);
            AccountType accountType = account.getAccountType();
            
            InterestStrategy strategy = interestStrategies.get(accountType);
            double interest = strategy.calculateInterest(account);
            
            if (interest > 0) {
                account.creditInterest(interest);
                System.out.println("✓ Interest applied to account " + accountNumber);
                System.out.println("  Interest Amount: $" + interest);
                System.out.println("  New Balance: $" + account.getBalance());
            } else {
                System.out.println("ℹ No interest applicable for account " + accountNumber);
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to apply interest: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Apply interest to all accounts
     */
    public void applyInterestToAllAccounts() {
        System.out.println("\n" + repeat("=", 60));
        System.out.println("  APPLYING INTEREST TO ALL ACCOUNTS");
        System.out.println(repeat("=", 60));
        
        for (Account account : accounts.values()) {
            applyInterest(account.getAccountNumber());
        }
        
        System.out.println(repeat("=", 60));
    }
    
    /**
     * Display all accounts
     */
    public void displayAllAccounts() {
        System.out.println("\n" + repeat("=", 60));
        System.out.println("  ALL ACCOUNTS IN " + bankName.toUpperCase());
        System.out.println(repeat("=", 60));
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (Account account : accounts.values()) {
                System.out.println(account);
            }
        }
        
        System.out.println(repeat("=", 60));
        System.out.println("Total Accounts: " + accounts.size());
        System.out.println(repeat("=", 60));
    }
    
    /**
     * Get bank name
     */
    public String getBankName() {
        return bankName;
    }
    
    /**
     * Get total number of accounts
     */
    public int getTotalAccounts() {
        return accounts.size();
    }
    
    /**
     * Check if account exists
     */
    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }
    
    /**
     * Helper method to repeat a string (for Java 8 compatibility)
     */
    private static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}

