package service;

import model.*;
import decorator.*;
import enums.ProductCategory;

public class CheckoutService {
    
    public Order checkout(ShoppingCart cart, double stateTaxRate, double localTaxRate) {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cannot checkout empty cart");
        }
        
        // Calculate amount after discounts and coupons
        double amountAfterDiscounts = cart.getAmountAfterDiscounts();
        
        // Calculate tax using Decorator pattern
        // Only apply tax to non-exempt items
        double taxableAmount = calculateTaxableAmount(cart);
        double taxAmount = 0.0;
        
        if (taxableAmount > 0) {
            // Apply state tax
            PriceCalculator calculator = new BasePriceCalculator(taxableAmount);
            
            if (stateTaxRate > 0) {
                calculator = new TaxDecorator(calculator, stateTaxRate, "State Tax");
            }
            
            if (localTaxRate > 0) {
                calculator = new TaxDecorator(calculator, localTaxRate, "Local Tax");
            }
            
            taxAmount = calculator.calculateTotal() - taxableAmount;
        }
        
        double finalTotal = amountAfterDiscounts + taxAmount;
        
        // Create order
        Order order = new Order(cart, taxAmount, finalTotal);
        
        // Reduce stock for all items
        for (CartItem item : cart.getItems()) {
            item.getProduct().reduceStock(item.getQuantity());
        }
        
        return order;
    }
    
    private double calculateTaxableAmount(ShoppingCart cart) {
        double taxableAmount = 0.0;
        
        for (CartItem item : cart.getItems()) {
            if (!item.getProduct().getCategory().isTaxExempt()) {
                taxableAmount += item.getSubtotal();
            }
        }
        
        // Apply discounts proportionally
        double subtotal = cart.getSubtotal();
        if (subtotal > 0) {
            double discountRatio = (cart.getTotalDiscounts() + cart.getTotalCouponDiscounts()) / subtotal;
            taxableAmount *= (1.0 - discountRatio);
        }
        
        return taxableAmount;
    }
    
    public void displayCheckoutSummary(ShoppingCart cart, double stateTaxRate, double localTaxRate) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          CHECKOUT SUMMARY              ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.printf("\nSubtotal:                    $%8.2f\n", cart.getSubtotal());
        
        if (cart.getTotalDiscounts() > 0) {
            System.out.println("\nStore Discounts:");
            for (strategy.DiscountStrategy discount : cart.getDiscounts()) {
                double amount = discount.calculateDiscount(cart);
                System.out.printf("  - %-28s -$%7.2f\n", 
                    discount.getDescription(), amount);
            }
            System.out.printf("Total Store Discounts:       -$%8.2f\n", cart.getTotalDiscounts());
        }
        
        if (!cart.getAppliedCoupons().isEmpty()) {
            System.out.println("\nCoupons:");
            double afterDiscounts = cart.getSubtotal() - cart.getTotalDiscounts();
            for (Coupon coupon : cart.getAppliedCoupons()) {
                double amount = coupon.calculateDiscount(afterDiscounts);
                System.out.printf("  - %-28s -$%7.2f\n",
                    coupon.getCode(), amount);
            }
            System.out.printf("Total Coupon Savings:        -$%8.2f\n", cart.getTotalCouponDiscounts());
        }
        
        double amountAfterDiscounts = cart.getAmountAfterDiscounts();
        System.out.printf("\nAmount After Discounts:      $%8.2f\n", amountAfterDiscounts);
        
        double taxableAmount = calculateTaxableAmount(cart);
        if (taxableAmount > 0) {
            double stateTax = taxableAmount * stateTaxRate;
            double localTax = taxableAmount * localTaxRate;
            double totalTax = stateTax + localTax;
            
            System.out.println("\nTax Breakdown:");
            if (stateTaxRate > 0) {
                System.out.printf("  State Tax (%.1f%%):          +$%7.2f\n", 
                    stateTaxRate * 100, stateTax);
            }
            if (localTaxRate > 0) {
                System.out.printf("  Local Tax (%.1f%%):          +$%7.2f\n",
                    localTaxRate * 100, localTax);
            }
            System.out.printf("Total Tax:                   +$%8.2f\n", totalTax);
            
            double finalTotal = amountAfterDiscounts + totalTax;
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.printf("FINAL TOTAL:                 $%8.2f\n", finalTotal);
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        } else {
            System.out.println("\nNo tax (all items tax-exempt)");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.printf("FINAL TOTAL:                 $%8.2f\n", amountAfterDiscounts);
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        }
    }
}
