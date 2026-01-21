package model;

import enums.TransactionType;
import strategy.TradingStrategy;
import java.util.ArrayList;
import java.util.List;

public class Trader {
    private final String traderId;
    private final String name;
    private double balance;
    private final Portfolio portfolio;
    private final List<Transaction> transactionHistory;
    private TradingStrategy tradingStrategy;
    
    public Trader(String traderId, String name, double initialBalance) {
        this.traderId = traderId;
        this.name = name;
        this.balance = initialBalance;
        this.portfolio = new Portfolio();
        this.transactionHistory = new ArrayList<>();
    }
    
    public void addBalance(double amount) {
        this.balance += amount;
    }
    
    public void deductBalance(double amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.balance -= amount;
    }
    
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
    
    public double getTotalAssets() {
        return balance + portfolio.getTotalValue();
    }
    
    public void setTradingStrategy(TradingStrategy strategy) {
        this.tradingStrategy = strategy;
    }
    
    // Getters
    public String getTraderId() {
        return traderId;
    }
    
    public String getName() {
        return name;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public Portfolio getPortfolio() {
        return portfolio;
    }
    
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
    
    public TradingStrategy getTradingStrategy() {
        return tradingStrategy;
    }
    
    public void displayAccount() {
        System.out.println("\n=== Trader Account: " + name + " ===");
        System.out.printf("Cash Balance: $%.2f\n", balance);
        System.out.printf("Portfolio Value: $%.2f\n", portfolio.getTotalValue());
        System.out.printf("Total Assets: $%.2f\n", getTotalAssets());
        portfolio.displayPortfolio();
    }
    
    @Override
    public String toString() {
        return String.format("Trader[%s, %s, Balance: $%.2f, Portfolio: $%.2f]",
            traderId, name, balance, portfolio.getTotalValue());
    }
}

