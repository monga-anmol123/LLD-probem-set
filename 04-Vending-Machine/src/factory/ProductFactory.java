package factory;

import enums.ProductCategory;
import model.Product;

/**
 * Factory for creating products.
 */
public class ProductFactory {
    
    /**
     * Create a product with given parameters.
     */
    public static Product createProduct(String id, String name, double price, int quantity, ProductCategory category) {
        return new Product(id, name, price, quantity, category);
    }
    
    /**
     * Create a beverage product.
     */
    public static Product createBeverage(String id, String name, double price, int quantity) {
        return new Product(id, name, price, quantity, ProductCategory.BEVERAGE);
    }
    
    /**
     * Create a snack product.
     */
    public static Product createSnack(String id, String name, double price, int quantity) {
        return new Product(id, name, price, quantity, ProductCategory.SNACK);
    }
    
    /**
     * Create a candy product.
     */
    public static Product createCandy(String id, String name, double price, int quantity) {
        return new Product(id, name, price, quantity, ProductCategory.CANDY);
    }
}
