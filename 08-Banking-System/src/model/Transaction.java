package model;

import enums.TransactionType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a bank transaction
 */
public class Transaction {
    private static int transactionCounter = 1;
    
    private String transactionId;
    private String accountNumber;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
    private double balanceAfter;
    
    public Transaction(String accountNumber, TransactionType type, double amount, double balanceAfter) {
        this.transactionId = "TXN" + String.format("%06d", transactionCounter++);
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.balanceAfter = balanceAfter;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    public String getDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s | Type: %s | Amount: $%.2f | Balance: $%.2f",
            transactionId,
            timestamp.format(formatter),
            type,
            amount,
            balanceAfter
        );
    }
    
    @Override
    public String toString() {
        return getDetails();
    }
}


