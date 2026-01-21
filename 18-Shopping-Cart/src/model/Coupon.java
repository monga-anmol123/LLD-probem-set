package model;

import enums.CouponType;
import java.time.LocalDate;

public class Coupon {
    private final String code;
    private final CouponType type;
    private final double value; // percentage or fixed amount
    private final double minOrderAmount;
    private final LocalDate expiryDate;
    private final int usageLimit;
    private int usageCount;
    
    public Coupon(String code, CouponType type, double value, double minOrderAmount,
                  LocalDate expiryDate, int usageLimit) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.minOrderAmount = minOrderAmount;
        this.expiryDate = expiryDate;
        this.usageLimit = usageLimit;
        this.usageCount = 0;
    }
    
    public boolean isValid(double cartSubtotal) {
        if (isExpired()) {
            return false;
        }
        
        if (cartSubtotal < minOrderAmount) {
            return false;
        }
        
        if (usageCount >= usageLimit) {
            return false;
        }
        
        return true;
    }
    
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }
    
    public double calculateDiscount(double amount) {
        if (type == CouponType.PERCENTAGE_OFF) {
            return amount * (value / 100.0);
        } else if (type == CouponType.FIXED_AMOUNT_OFF) {
            return Math.min(value, amount); // Don't exceed cart total
        }
        return 0.0;
    }
    
    public void use() {
        usageCount++;
    }
    
    public String getValidationError(double cartSubtotal) {
        if (isExpired()) {
            return "Coupon expired on " + expiryDate;
        }
        if (cartSubtotal < minOrderAmount) {
            return String.format("Minimum order amount $%.2f required (current: $%.2f)",
                minOrderAmount, cartSubtotal);
        }
        if (usageCount >= usageLimit) {
            return "Coupon usage limit exceeded";
        }
        return null;
    }
    
    // Getters
    public String getCode() {
        return code;
    }
    
    public CouponType getType() {
        return type;
    }
    
    public double getValue() {
        return value;
    }
    
    public double getMinOrderAmount() {
        return minOrderAmount;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public int getUsageLimit() {
        return usageLimit;
    }
    
    public int getUsageCount() {
        return usageCount;
    }
    
    @Override
    public String toString() {
        String valueStr = type == CouponType.PERCENTAGE_OFF ? 
            String.format("%.0f%% off", value) : 
            String.format("$%.2f off", value);
        return String.format("Coupon[%s: %s, Min: $%.2f, Expires: %s]",
            code, valueStr, minOrderAmount, expiryDate);
    }
}
