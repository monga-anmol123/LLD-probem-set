package model;

import java.time.LocalDateTime;

public class Invoice {
    private String invoiceId;
    private Rental rental;
    private double baseCharge;
    private double insuranceCharge;
    private double addOnsCharge;
    private double lateFee;
    private double oneWayFee;
    private double subtotal;
    private double discount;
    private double taxRate = 0.08; // 8% tax
    private double taxAmount;
    private double totalAmount;
    private LocalDateTime generatedAt;
    
    public Invoice(String invoiceId, Rental rental) {
        this.invoiceId = invoiceId;
        this.rental = rental;
        this.generatedAt = LocalDateTime.now();
        calculateCharges();
    }
    
    private void calculateCharges() {
        // Calculate individual charges
        this.baseCharge = rental.calculateBaseCost();
        this.insuranceCharge = rental.calculateInsuranceCost();
        this.addOnsCharge = rental.calculateAddOnsCost();
        this.lateFee = rental.calculateLateFee();
        this.oneWayFee = rental.calculateOneWayFee();
        
        // Calculate subtotal
        this.subtotal = baseCharge + insuranceCharge + addOnsCharge + lateFee + oneWayFee;
        
        // Apply membership discount (only on base charge)
        this.discount = baseCharge * rental.getCustomer().getMembershipDiscount();
        
        // Calculate tax on (subtotal - discount)
        this.taxAmount = (subtotal - discount) * taxRate;
        
        // Calculate total
        this.totalAmount = subtotal - discount + taxAmount;
    }
    
    public String generateBreakdown() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════════════════════════╗\n");
        sb.append(String.format("║                    RENTAL INVOICE #%s                  ║\n", invoiceId));
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Customer: %-48s ║\n", rental.getCustomer().getName()));
        sb.append(String.format("║ Vehicle:  %-48s ║\n", 
            rental.getVehicle().getMake() + " " + rental.getVehicle().getModel()));
        sb.append(String.format("║ Rental Period: %s to %s                     ║\n", 
            rental.getPickupDate(), rental.getReturnDate()));
        sb.append(String.format("║ Rental Days: %-45d ║\n", rental.getRentalDays()));
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Base Charge (%d days × $%.2f):              $%10.2f ║\n", 
            rental.getRentalDays(), rental.getVehicle().getDailyRate(), baseCharge));
        
        if (insuranceCharge > 0) {
            sb.append(String.format("║ Insurance (%d days × $%.2f):                $%10.2f ║\n", 
                rental.getRentalDays(), 15.0, insuranceCharge));
        }
        
        if (addOnsCharge > 0) {
            sb.append(String.format("║ Add-ons (GPS):                                 $%10.2f ║\n", addOnsCharge));
        }
        
        if (lateFee > 0) {
            sb.append(String.format("║ Late Return Fee (%d days × $30):            $%10.2f ║\n", 
                rental.getLateDays(), lateFee));
        }
        
        if (oneWayFee > 0) {
            sb.append(String.format("║ One-Way Rental Fee:                            $%10.2f ║\n", oneWayFee));
        }
        
        sb.append("║                                                            ║\n");
        sb.append(String.format("║ Subtotal:                                      $%10.2f ║\n", subtotal));
        
        if (discount > 0) {
            sb.append(String.format("║ Membership Discount (%s - %.0f%%):            -$%10.2f ║\n", 
                rental.getCustomer().getMembershipTier(), 
                rental.getCustomer().getMembershipDiscount() * 100, discount));
        }
        
        sb.append(String.format("║ Tax (%.0f%%):                                     $%10.2f ║\n", 
            taxRate * 100, taxAmount));
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ TOTAL AMOUNT:                                  $%10.2f ║\n", totalAmount));
        sb.append("╚════════════════════════════════════════════════════════════╝\n");
        
        return sb.toString();
    }
    
    public void applyAdditionalDiscount(double discountAmount) {
        this.discount += discountAmount;
        this.taxAmount = (subtotal - discount) * taxRate;
        this.totalAmount = subtotal - discount + taxAmount;
    }
    
    // Getters
    public String getInvoiceId() { return invoiceId; }
    public Rental getRental() { return rental; }
    public double getBaseCharge() { return baseCharge; }
    public double getInsuranceCharge() { return insuranceCharge; }
    public double getAddOnsCharge() { return addOnsCharge; }
    public double getLateFee() { return lateFee; }
    public double getOneWayFee() { return oneWayFee; }
    public double getSubtotal() { return subtotal; }
    public double getDiscount() { return discount; }
    public double getTaxAmount() { return taxAmount; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
}


