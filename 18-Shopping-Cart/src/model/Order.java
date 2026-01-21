package model;

import strategy.DiscountStrategy;
import decorator.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class Order {
    private static int orderCounter = 1;
    
    private final String orderId;
    private final LocalDateTime orderDate;
    private final List<CartItem> items;
    private final double subtotal;
    private final List<DiscountStrategy> discounts;
    private final double totalDiscounts;
    private final List<Coupon> coupons;
    private final double totalCouponDiscounts;
    private final double taxAmount;
    private final double finalTotal;
    
    public Order(ShoppingCart cart, double taxAmount, double finalTotal) {
        this.orderId = "ORD-" + String.format("%06d", orderCounter++);
        this.orderDate = LocalDateTime.now();
        this.items = new ArrayList<>(cart.getItems());
        this.subtotal = cart.getSubtotal();
        this.discounts = new ArrayList<>(cart.getDiscounts());
        this.totalDiscounts = cart.getTotalDiscounts();
        this.coupons = new ArrayList<>(cart.getAppliedCoupons());
        this.totalCouponDiscounts = cart.getTotalCouponDiscounts();
        this.taxAmount = taxAmount;
        this.finalTotal = finalTotal;
    }
    
    public void displayOrderSummary() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ORDER CONFIRMATION             ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Order ID: " + orderId);
        System.out.println("Date: " + orderDate.format(formatter));
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("ITEMS:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        for (CartItem item : items) {
            System.out.println("  " + item);
        }
        
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("PRICE BREAKDOWN:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.printf("Subtotal:           $%8.2f\n", subtotal);
        
        if (!discounts.isEmpty()) {
            System.out.println("\nDiscounts Applied:");
            for (DiscountStrategy discount : discounts) {
                System.out.printf("  - %s\n", discount.getDescription());
            }
            System.out.printf("Total Discounts:    -$%8.2f\n", totalDiscounts);
        }
        
        if (!coupons.isEmpty()) {
            System.out.println("\nCoupons Applied:");
            for (Coupon coupon : coupons) {
                System.out.printf("  - %s\n", coupon.getCode());
            }
            System.out.printf("Total Coupon Savings: -$%8.2f\n", totalCouponDiscounts);
        }
        
        double afterDiscounts = subtotal - totalDiscounts - totalCouponDiscounts;
        System.out.printf("\nAmount After Discounts: $%8.2f\n", afterDiscounts);
        
        if (taxAmount > 0) {
            double taxRate = (taxAmount / afterDiscounts) * 100;
            System.out.printf("Tax (%.1f%%):        +$%8.2f\n", taxRate, taxAmount);
        }
        
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.printf("FINAL TOTAL:        $%8.2f\n", finalTotal);
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        double totalSavings = totalDiscounts + totalCouponDiscounts;
        if (totalSavings > 0) {
            System.out.printf("\n💰 You saved $%.2f on this order!\n", totalSavings);
        }
    }
    
    // Getters
    public String getOrderId() {
        return orderId;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public double getFinalTotal() {
        return finalTotal;
    }
}
