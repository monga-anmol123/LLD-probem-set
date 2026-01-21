package model;

import observer.Subject;
import strategy.DiscountStrategy;
import java.util.*;

public class ShoppingCart extends Subject {
    private final Map<String, CartItem> items; // SKU -> CartItem
    private final List<DiscountStrategy> discounts;
    private final List<Coupon> appliedCoupons;
    
    public ShoppingCart() {
        this.items = new HashMap<>();
        this.discounts = new ArrayList<>();
        this.appliedCoupons = new ArrayList<>();
    }
    
    public void addItem(Product product, int quantity) {
        if (!product.isAvailable(quantity)) {
            throw new IllegalStateException(
                String.format("Insufficient stock for %s. Available: %d, Requested: %d",
                    product.getName(), product.getStock(), quantity));
        }
        
        String sku = product.getSku();
        
        if (items.containsKey(sku)) {
            CartItem item = items.get(sku);
            int oldQuantity = item.getQuantity();
            item.incrementQuantity(quantity);
            notifyQuantityUpdated(product, oldQuantity, item.getQuantity());
        } else {
            items.put(sku, new CartItem(product, quantity));
            notifyItemAdded(product, quantity);
        }
    }
    
    public void removeItem(String sku) {
        CartItem item = items.remove(sku);
        if (item != null) {
            notifyItemRemoved(item.getProduct());
        }
    }
    
    public void updateQuantity(String sku, int newQuantity) {
        CartItem item = items.get(sku);
        if (item == null) {
            throw new IllegalArgumentException("Item not in cart");
        }
        
        if (!item.getProduct().isAvailable(newQuantity)) {
            throw new IllegalStateException("Insufficient stock");
        }
        
        int oldQuantity = item.getQuantity();
        item.updateQuantity(newQuantity);
        notifyQuantityUpdated(item.getProduct(), oldQuantity, newQuantity);
    }
    
    public void clearCart() {
        items.clear();
        discounts.clear();
        appliedCoupons.clear();
        notifyCartCleared();
    }
    
    public void addDiscount(DiscountStrategy discount) {
        discounts.add(discount);
    }
    
    public boolean applyCoupon(Coupon coupon) {
        if (!coupon.isValid(getSubtotal())) {
            return false;
        }
        
        appliedCoupons.add(coupon);
        coupon.use();
        return true;
    }
    
    public void removeCoupon(String code) {
        appliedCoupons.removeIf(c -> c.getCode().equals(code));
    }
    
    public double getSubtotal() {
        return items.values().stream()
            .mapToDouble(CartItem::getSubtotal)
            .sum();
    }
    
    public double getTotalDiscounts() {
        return discounts.stream()
            .mapToDouble(d -> d.calculateDiscount(this))
            .sum();
    }
    
    public double getTotalCouponDiscounts() {
        double afterDiscounts = getSubtotal() - getTotalDiscounts();
        return appliedCoupons.stream()
            .mapToDouble(c -> c.calculateDiscount(afterDiscounts))
            .sum();
    }
    
    public double getAmountAfterDiscounts() {
        return getSubtotal() - getTotalDiscounts() - getTotalCouponDiscounts();
    }
    
    public int getTotalItemCount() {
        return items.values().stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }
    
    public CartItem getItemBySKU(String sku) {
        return items.get(sku);
    }
    
    public Collection<CartItem> getItems() {
        return items.values();
    }
    
    public List<DiscountStrategy> getDiscounts() {
        return new ArrayList<>(discounts);
    }
    
    public List<Coupon> getAppliedCoupons() {
        return new ArrayList<>(appliedCoupons);
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public void displayCart() {
        if (isEmpty()) {
            System.out.println("🛒 Cart is empty");
            return;
        }
        
        System.out.println("\n🛒 Shopping Cart:");
        System.out.println("================");
        for (CartItem item : items.values()) {
            System.out.println("  " + item);
        }
        System.out.println("================");
        System.out.printf("Subtotal: $%.2f (%d items)\n", getSubtotal(), getTotalItemCount());
    }
}
