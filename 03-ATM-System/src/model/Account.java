package model;

import enums.TransactionType;
import enums.TransactionStatus;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private List<Transaction> transactions;

    public Account(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount. Amount must be positive.");
            return false;
        }
        
        if (balance >= amount) {
            balance -= amount;
            addTransaction(new Transaction(TransactionType.WITHDRAWAL, amount, TransactionStatus.SUCCESS));
            return true;
        } else {
            System.out.println("Insufficient balance. Available balance: $" + balance);
            addTransaction(new Transaction(TransactionType.WITHDRAWAL, amount, TransactionStatus.FAILED));
            return false;
        }
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount. Amount must be positive.");
            addTransaction(new Transaction(TransactionType.DEPOSIT, amount, TransactionStatus.FAILED));
            return;
        }
        
        balance += amount;
        addTransaction(new Transaction(TransactionType.DEPOSIT, amount, TransactionStatus.SUCCESS));
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getRecentTransactions(int count) {
        int size = transactions.size();
        int fromIndex = Math.max(0, size - count);
        return new ArrayList<>(transactions.subList(fromIndex, size));
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}


