package decorator;

public class BasePriceCalculator implements PriceCalculator {
    private final double amount;
    
    public BasePriceCalculator(double amount) {
        this.amount = amount;
    }
    
    @Override
    public double calculateTotal() {
        return amount;
    }
    
    @Override
    public String getDescription() {
        return String.format("Base Amount: $%.2f", amount);
    }
}
