package model;

import enums.ProductCategory;

/**
 * Product represents an item in the vending machine.
 */
public class Product {
    private final String id;
    private final String name;
    private final double price;
    private int quantity;
    private final ProductCategory category;

    public Product(String id, String name, double price, int quantity, ProductCategory category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public void decrementQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    @Override
    public String toString() {
        return String.format("%s - %s ($%.2f) [Stock: %d]", id, name, price, quantity);
    }
}
