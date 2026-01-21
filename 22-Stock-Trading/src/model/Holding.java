package model;

public class Holding {
    private final Stock stock;
    private int quantity;
    private double averagePurchasePrice;
    private double totalInvested;
    
    public Holding(Stock stock, int quantity, double purchasePrice) {
        this.stock = stock;
        this.quantity = quantity;
        this.averagePurchasePrice = purchasePrice;
        this.totalInvested = quantity * purchasePrice;
    }
    
    public void addShares(int quantity, double purchasePrice) {
        double newInvestment = quantity * purchasePrice;
        this.totalInvested += newInvestment;
        this.quantity += quantity;
        this.averagePurchasePrice = this.totalInvested / this.quantity;
    }
    
    public void removeShares(int quantity) {
        if (quantity > this.quantity) {
            throw new IllegalArgumentException("Cannot remove more shares than owned");
        }
        
        double investmentReduced = (this.totalInvested / this.quantity) * quantity;
        this.totalInvested -= investmentReduced;
        this.quantity -= quantity;
        
        if (this.quantity == 0) {
            this.averagePurchasePrice = 0;
            this.totalInvested = 0;
        }
    }
    
    public double getCurrentValue() {
        return quantity * stock.getCurrentPrice();
    }
    
    public double calculateProfitLoss() {
        return getCurrentValue() - totalInvested;
    }
    
    public double getProfitLossPercent() {
        if (totalInvested == 0) return 0;
        return (calculateProfitLoss() / totalInvested) * 100;
    }
    
    // Getters
    public Stock getStock() {
        return stock;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public double getAveragePurchasePrice() {
        return averagePurchasePrice;
    }
    
    public double getTotalInvested() {
        return totalInvested;
    }
    
    @Override
    public String toString() {
        return String.format("%s: %d shares @ avg $%.2f (Current: $%.2f, P/L: $%.2f [%.2f%%])",
            stock.getSymbol(), quantity, averagePurchasePrice, stock.getCurrentPrice(),
            calculateProfitLoss(), getProfitLossPercent());
    }
}

