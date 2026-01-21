package model;

import enums.ProductCategory;

public class Product {
    private final String sku;
    private final String name;
    private double price;
    private final ProductCategory category;
    private int stock;
    private final String description;
    
    public Product(String sku, String name, double price, ProductCategory category, 
                   int stock, String description) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.description = description;
    }
    
    public boolean isAvailable(int quantity) {
        return stock >= quantity;
    }
    
    public void reduceStock(int quantity) {
        if (quantity > stock) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.stock -= quantity;
    }
    
    public void increaseStock(int quantity) {
        this.stock += quantity;
    }
    
    public void updatePrice(double newPrice) {
        this.price = newPrice;
    }
    
    // Getters
    public String getSku() {
        return sku;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public ProductCategory getCategory() {
        return category;
    }
    
    public int getStock() {
        return stock;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s ($%.2f) [Stock: %d]", 
            sku, name, price, stock);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return sku.equals(product.sku);
    }
    
    @Override
    public int hashCode() {
        return sku.hashCode();
    }
}
