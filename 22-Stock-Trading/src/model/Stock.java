package model;

import observer.Subject;
import java.util.ArrayList;
import java.util.List;

public class Stock extends Subject {
    private final String symbol;
    private final String name;
    private double currentPrice;
    private double previousPrice;
    private long volume;
    private final List<Double> priceHistory;
    
    public Stock(String symbol, String name, double initialPrice) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = initialPrice;
        this.previousPrice = initialPrice;
        this.volume = 0;
        this.priceHistory = new ArrayList<>();
        this.priceHistory.add(initialPrice);
    }
    
    public void updatePrice(double newPrice) {
        this.previousPrice = this.currentPrice;
        this.currentPrice = newPrice;
        this.priceHistory.add(newPrice);
        
        // Notify observers of price change
        notifyPriceUpdate(this, previousPrice, newPrice);
    }
    
    public void incrementVolume(long quantity) {
        this.volume += quantity;
    }
    
    public double getPriceChange() {
        return currentPrice - previousPrice;
    }
    
    public double getPriceChangePercent() {
        if (previousPrice == 0) return 0;
        return ((currentPrice - previousPrice) / previousPrice) * 100;
    }
    
    // Getters
    public String getSymbol() {
        return symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public double getCurrentPrice() {
        return currentPrice;
    }
    
    public double getPreviousPrice() {
        return previousPrice;
    }
    
    public long getVolume() {
        return volume;
    }
    
    public List<Double> getPriceHistory() {
        return new ArrayList<>(priceHistory);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - $%.2f", symbol, name, currentPrice);
    }
}

