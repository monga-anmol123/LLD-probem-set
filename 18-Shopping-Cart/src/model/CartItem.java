package model;

public class CartItem {
    private final Product product;
    private int quantity;
    
    public CartItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.product = product;
        this.quantity = quantity;
    }
    
    public void updateQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = newQuantity;
    }
    
    public void incrementQuantity(int amount) {
        this.quantity += amount;
    }
    
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
    
    // Getters
    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    @Override
    public String toString() {
        return String.format("%s x%d @ $%.2f each = $%.2f",
            product.getName(), quantity, product.getPrice(), getSubtotal());
    }
}
