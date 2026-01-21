package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class Portfolio {
    private final Map<String, Holding> holdings; // symbol -> Holding
    
    public Portfolio() {
        this.holdings = new HashMap<>();
    }
    
    public void addStock(Stock stock, int quantity, double purchasePrice) {
        String symbol = stock.getSymbol();
        
        if (holdings.containsKey(symbol)) {
            holdings.get(symbol).addShares(quantity, purchasePrice);
        } else {
            holdings.put(symbol, new Holding(stock, quantity, purchasePrice));
        }
    }
    
    public void removeStock(Stock stock, int quantity) {
        String symbol = stock.getSymbol();
        
        if (!holdings.containsKey(symbol)) {
            throw new IllegalArgumentException("Stock not in portfolio");
        }
        
        Holding holding = holdings.get(symbol);
        
        if (holding.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient shares to sell");
        }
        
        holding.removeShares(quantity);
        
        // Remove holding if no shares left
        if (holding.getQuantity() == 0) {
            holdings.remove(symbol);
        }
    }
    
    public boolean hasStock(Stock stock, int quantity) {
        String symbol = stock.getSymbol();
        if (!holdings.containsKey(symbol)) {
            return false;
        }
        return holdings.get(symbol).getQuantity() >= quantity;
    }
    
    public Holding getHolding(Stock stock) {
        return holdings.get(stock.getSymbol());
    }
    
    public double getTotalValue() {
        return holdings.values().stream()
            .mapToDouble(Holding::getCurrentValue)
            .sum();
    }
    
    public double getTotalInvested() {
        return holdings.values().stream()
            .mapToDouble(Holding::getTotalInvested)
            .sum();
    }
    
    public double getTotalProfitLoss() {
        return holdings.values().stream()
            .mapToDouble(Holding::calculateProfitLoss)
            .sum();
    }
    
    public double getTotalProfitLossPercent() {
        double invested = getTotalInvested();
        if (invested == 0) return 0;
        return (getTotalProfitLoss() / invested) * 100;
    }
    
    public Collection<Holding> getAllHoldings() {
        return holdings.values();
    }
    
    public boolean isEmpty() {
        return holdings.isEmpty();
    }
    
    public void displayPortfolio() {
        if (holdings.isEmpty()) {
            System.out.println("   Portfolio is empty");
            return;
        }
        
        System.out.println("   Holdings:");
        for (Holding holding : holdings.values()) {
            System.out.println("   - " + holding);
        }
        
        System.out.printf("   Total Value: $%.2f\n", getTotalValue());
        System.out.printf("   Total Invested: $%.2f\n", getTotalInvested());
        System.out.printf("   Total P/L: $%.2f (%.2f%%)\n", 
            getTotalProfitLoss(), getTotalProfitLossPercent());
    }
}

