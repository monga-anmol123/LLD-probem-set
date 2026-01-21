package model;

public class Bill {
    private String billId;
    private Order order;
    private double subtotal;
    private double tax;
    private double serviceCharge;
    private double discount;
    private double total;
    
    private static final double TAX_RATE = 0.08; // 8% tax
    private static final double SERVICE_CHARGE_RATE = 0.10; // 10% service charge
    
    public Bill(String billId, Order order, double subtotal) {
        this.billId = billId;
        this.order = order;
        this.subtotal = subtotal;
        this.discount = 0.0;
        calculateTotal();
    }
    
    public void calculateTotal() {
        this.tax = subtotal * TAX_RATE;
        this.serviceCharge = subtotal * SERVICE_CHARGE_RATE;
        this.total = subtotal + tax + serviceCharge - discount;
    }
    
    public void applyDiscount(double discountAmount) {
        this.discount = discountAmount;
        calculateTotal();
        System.out.println("Discount of $" + String.format("%.2f", discountAmount) + " applied.");
    }
    
    public double[] split(int numberOfPeople) {
        double perPerson = total / numberOfPeople;
        double[] splits = new double[numberOfPeople];
        for (int i = 0; i < numberOfPeople; i++) {
            splits[i] = perPerson;
        }
        return splits;
    }
    
    public void printBill() {
        System.out.println("\n========================================");
        System.out.println("           BILL #" + billId);
        System.out.println("========================================");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Table: " + order.getTable().getTableId());
        System.out.println("----------------------------------------");
        System.out.println("Items:");
        for (MenuItem item : order.getItems()) {
            System.out.println("  " + item.getName() + " - $" + String.format("%.2f", item.getPrice()));
        }
        System.out.println("----------------------------------------");
        System.out.println("Subtotal:        $" + String.format("%.2f", subtotal));
        System.out.println("Tax (8%):        $" + String.format("%.2f", tax));
        System.out.println("Service (10%):   $" + String.format("%.2f", serviceCharge));
        if (discount > 0) {
            System.out.println("Discount:       -$" + String.format("%.2f", discount));
        }
        System.out.println("========================================");
        System.out.println("TOTAL:           $" + String.format("%.2f", total));
        System.out.println("========================================\n");
    }
    
    // Getters
    public String getBillId() {
        return billId;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public double getTax() {
        return tax;
    }
    
    public double getServiceCharge() {
        return serviceCharge;
    }
    
    public double getDiscount() {
        return discount;
    }
    
    public double getTotal() {
        return total;
    }
}


