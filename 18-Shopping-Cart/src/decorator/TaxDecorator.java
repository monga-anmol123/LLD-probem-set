package decorator;

public class TaxDecorator implements PriceCalculator {
    private final PriceCalculator calculator;
    private final double taxRate;
    private final String taxName;
    
    public TaxDecorator(PriceCalculator calculator, double taxRate, String taxName) {
        this.calculator = calculator;
        this.taxRate = taxRate;
        this.taxName = taxName;
    }
    
    @Override
    public double calculateTotal() {
        double baseTotal = calculator.calculateTotal();
        return baseTotal + (baseTotal * taxRate);
    }
    
    public double getTaxAmount() {
        return calculator.calculateTotal() * taxRate;
    }
    
    @Override
    public String getDescription() {
        return calculator.getDescription() + String.format(" + %s (%.2f%%): $%.2f",
            taxName, taxRate * 100, getTaxAmount());
    }
    
    public double getTaxRate() {
        return taxRate;
    }
    
    public String getTaxName() {
        return taxName;
    }
}
