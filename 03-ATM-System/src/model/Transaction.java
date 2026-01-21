package model;

import enums.TransactionType;
import enums.TransactionStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
    private TransactionStatus status;

    public Transaction(TransactionType type, double amount, TransactionStatus status) {
        this.transactionId = UUID.randomUUID().toString().substring(0, 8);
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - $%.2f - %s", 
            transactionId, type, amount, status);
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
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

    public TransactionStatus getStatus() {
        return status;
    }
}


