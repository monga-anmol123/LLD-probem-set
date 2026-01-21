package model;

import enums.AccountType;
import enums.TransactionType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all bank accounts
 */
public abstract class Account {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;
    protected AccountType accountType;
    protected LocalDate createdDate;
    protected List<Transaction> transactionHistory;
    
    public Account(String accountNumber, String accountHolderName, double initialDeposit, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialDeposit;
        this.accountType = accountType;
        this.createdDate = LocalDate.now();
        this.transactionHistory = new ArrayList<>();
        
        // Record initial deposit
        if (initialDeposit > 0) {
            addTransaction(TransactionType.DEPOSIT, initialDeposit);
        }
    }
    
    // Abstract methods to be implemented by subclasses
    public abstract boolean canWithdraw(double amount);
    public abstract double getMinimumBalance();
    public abstract double getWithdrawalLimit();
    
    /**
     * Deposit money into the account
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
        addTransaction(TransactionType.DEPOSIT, amount);
    }
    
    /**
     * Withdraw money from the account
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        if (!canWithdraw(amount)) {
            throw new IllegalStateException("Cannot withdraw: Insufficient balance or exceeds limit");
        }
        
        balance -= amount;
        addTransaction(TransactionType.WITHDRAWAL, amount);
    }
    
    /**
     * Credit interest to the account
     */
    public void creditInterest(double interestAmount) {
        if (interestAmount > 0) {
            balance += interestAmount;
            addTransaction(TransactionType.INTEREST_CREDIT, interestAmount);
        }
    }
    
    /**
     * Add a transaction to history
     */
    public void addTransaction(TransactionType type, double amount) {
        Transaction transaction = new Transaction(accountNumber, type, amount, balance);
        transactionHistory.add(transaction);
    }
    
    /**
     * Get account balance
     */
    public double getBalance() {
        return balance;
    }
    
    /**
     * Get account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }
    
    /**
     * Get account holder name
     */
    public String getAccountHolderName() {
        return accountHolderName;
    }
    
    /**
     * Get account type
     */
    public AccountType getAccountType() {
        return accountType;
    }
    
    /**
     * Get created date
     */
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    /**
     * Get transaction history
     */
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
    
    /**
     * Display account information
     */
    public void displayAccountInfo() {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║              ACCOUNT INFORMATION                       ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf("║ Account Number    : %-33s ║%n", accountNumber);
        System.out.printf("║ Account Holder    : %-33s ║%n", accountHolderName);
        System.out.printf("║ Account Type      : %-33s ║%n", accountType);
        System.out.printf("║ Current Balance   : $%-32.2f ║%n", balance);
        System.out.printf("║ Minimum Balance   : $%-32.2f ║%n", getMinimumBalance());
        System.out.printf("║ Withdrawal Limit  : $%-32.2f ║%n", getWithdrawalLimit());
        System.out.printf("║ Created Date      : %-33s ║%n", createdDate);
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Display transaction history
     */
    public void displayTransactionHistory() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                        TRANSACTION HISTORY                              │");
        System.out.println("├─────────────────────────────────────────────────────────────────────────┤");
        
        if (transactionHistory.isEmpty()) {
            System.out.println("│ No transactions yet                                                     │");
        } else {
            for (Transaction txn : transactionHistory) {
                System.out.printf("│ %-71s │%n", txn.getDetails());
            }
        }
        
        System.out.println("└─────────────────────────────────────────────────────────────────────────┘");
    }
    
    @Override
    public String toString() {
        return String.format("%s Account [%s] - %s: $%.2f", 
            accountType, accountNumber, accountHolderName, balance);
    }
}

