import model.*;
import service.*;
import enums.*;
import strategy.*;
import observer.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  🛒 E-COMMERCE SHOPPING CART DEMO 🛒");
        System.out.println("========================================\n");
        
        // Create product catalog
        Product laptop = new Product("ELEC001", "Gaming Laptop", 1200.00, 
            ProductCategory.ELECTRONICS, 10, "High-performance gaming laptop");
        Product phone = new Product("ELEC002", "Smartphone", 800.00,
            ProductCategory.ELECTRONICS, 15, "Latest smartphone");
        Product shirt = new Product("CLO001", "T-Shirt", 25.00,
            ProductCategory.CLOTHING, 50, "Cotton t-shirt");
        Product jeans = new Product("CLO002", "Jeans", 60.00,
            ProductCategory.CLOTHING, 30, "Denim jeans");
        Product milk = new Product("GRO001", "Milk", 4.50,
            ProductCategory.GROCERIES, 100, "Fresh milk");
        Product bread = new Product("GRO002", "Bread", 3.00,
            ProductCategory.GROCERIES, 80, "Whole wheat bread");
        Product book = new Product("BOOK001", "Design Patterns", 45.00,
            ProductCategory.BOOKS, 20, "Gang of Four book");
        
        System.out.println("📦 Product Catalog:");
        System.out.println("  " + laptop);
        System.out.println("  " + phone);
        System.out.println("  " + shirt);
        System.out.println("  " + jeans);
        System.out.println("  " + milk);
        System.out.println("  " + bread);
        System.out.println("  " + book);
        
        // Create shopping cart
        ShoppingCart cart = new ShoppingCart();
        
        // Attach observer
        CartNotificationObserver observer = new CartNotificationObserver("Customer");
        cart.attach(observer);
        
        // ====================
        // SCENARIO 1: Basic Cart Operations
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 1: BASIC CART OPERATIONS");
        System.out.println("========================================\n");
        
        cart.addItem(laptop, 1);
        cart.addItem(shirt, 2);
        cart.addItem(milk, 3);
        
        cart.displayCart();
        
        System.out.println("\n--- Update quantity ---");
        cart.updateQuantity("CLO001", 3);
        
        System.out.println("\n--- Remove item ---");
        cart.removeItem("GRO001");
        
        cart.displayCart();
        
        // ====================
        // SCENARIO 2: Percentage Discount
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 2: PERCENTAGE DISCOUNT");
        System.out.println("========================================\n");
        
        cart.clearCart();
        cart.addItem(laptop, 1);
        cart.addItem(phone, 1);
        
        System.out.println("Before discount:");
        cart.displayCart();
        
        System.out.println("\n💰 Applying 15% store-wide discount...");
        cart.addDiscount(new PercentageDiscount(15.0));
        
        System.out.printf("Subtotal: $%.2f\n", cart.getSubtotal());
        System.out.printf("Discount: -$%.2f\n", cart.getTotalDiscounts());
        System.out.printf("After Discount: $%.2f\n", cart.getAmountAfterDiscounts());
        
        // ====================
        // SCENARIO 3: BOGO Discount
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 3: BOGO DISCOUNT");
        System.out.println("========================================\n");
        
        cart.clearCart();
        cart.addItem(shirt, 4);
        
        System.out.println("Cart: 4 T-Shirts @ $25 each");
        System.out.printf("Subtotal: $%.2f\n", cart.getSubtotal());
        
        System.out.println("\n💰 Applying Buy 2 Get 1 Free...");
        cart.addDiscount(new BOGODiscount("CLO001", 2, 1));
        
        System.out.printf("Discount (1 free shirt): -$%.2f\n", cart.getTotalDiscounts());
        System.out.printf("You pay for: 3 shirts\n");
        System.out.printf("After Discount: $%.2f\n", cart.getAmountAfterDiscounts());
        
        // ====================
        // SCENARIO 4: Coupon Validation
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 4: COUPON VALIDATION");
        System.out.println("========================================\n");
        
        cart.clearCart();
        cart.addItem(laptop, 1);
        
        // Create coupons
        Coupon expiredCoupon = new Coupon("EXPIRED20", CouponType.PERCENTAGE_OFF, 
            20.0, 0.0, LocalDate.now().minusDays(1), 1);
        Coupon minOrderCoupon = new Coupon("MIN500", CouponType.FIXED_AMOUNT_OFF,
            50.0, 500.0, LocalDate.now().plusDays(30), 10);
        Coupon validCoupon = new Coupon("SAVE100", CouponType.FIXED_AMOUNT_OFF,
            100.0, 1000.0, LocalDate.now().plusDays(30), 1);
        
        System.out.printf("Cart subtotal: $%.2f\n\n", cart.getSubtotal());
        
        System.out.println("❌ Trying expired coupon:");
        if (!cart.applyCoupon(expiredCoupon)) {
            System.out.println("   " + expiredCoupon.getValidationError(cart.getSubtotal()));
        }
        
        System.out.println("\n❌ Trying coupon with min order requirement:");
        if (!cart.applyCoupon(minOrderCoupon)) {
            System.out.println("   " + minOrderCoupon.getValidationError(cart.getSubtotal()));
        }
        
        System.out.println("\n✅ Applying valid coupon:");
        if (cart.applyCoupon(validCoupon)) {
            System.out.println("   Coupon SAVE100 applied successfully!");
            System.out.printf("   Discount: -$%.2f\n", cart.getTotalCouponDiscounts());
        }
        
        // ====================
        // SCENARIO 5: Category Discount
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 5: CATEGORY DISCOUNT");
        System.out.println("========================================\n");
        
        cart.clearCart();
        cart.addItem(laptop, 1);   // Electronics
        cart.addItem(phone, 1);    // Electronics
        cart.addItem(shirt, 2);    // Clothing
        cart.addItem(milk, 3);     // Groceries
        
        cart.displayCart();
        
        System.out.println("\n💰 Applying 20% discount on Electronics only...");
        cart.addDiscount(new CategoryDiscount(ProductCategory.ELECTRONICS, 20.0));
        
        double electronicsSubtotal = laptop.getPrice() + phone.getPrice();
        double electronicsDiscount = electronicsSubtotal * 0.20;
        
        System.out.printf("Electronics subtotal: $%.2f\n", electronicsSubtotal);
        System.out.printf("Electronics discount (20%%): -$%.2f\n", electronicsDiscount);
        System.out.printf("Other items: No discount\n");
        System.out.printf("Total discount: -$%.2f\n", cart.getTotalDiscounts());
        System.out.printf("After discount: $%.2f\n", cart.getAmountAfterDiscounts());
        
        // ====================
        // SCENARIO 6: Complex Pricing (Multiple Discounts + Coupon + Tax)
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 6: COMPLEX PRICING");
        System.out.println("========================================\n");
        
        cart.clearCart();
        cart.addItem(laptop, 1);   // $1200
        cart.addItem(phone, 1);    // $800
        cart.addItem(book, 2);     // $90
        
        System.out.println("Step 1: Initial cart");
        cart.displayCart();
        
        System.out.println("\nStep 2: Apply 10% store-wide discount");
        cart.addDiscount(new PercentageDiscount(10.0));
        System.out.printf("After 10%% discount: $%.2f\n", cart.getAmountAfterDiscounts());
        
        System.out.println("\nStep 3: Apply $150 coupon");
        Coupon coupon150 = new Coupon("MEGA150", CouponType.FIXED_AMOUNT_OFF,
            150.0, 2000.0, LocalDate.now().plusDays(30), 1);
        cart.applyCoupon(coupon150);
        System.out.printf("After coupon: $%.2f\n", cart.getAmountAfterDiscounts());
        
        System.out.println("\nStep 4: Calculate tax (8% on taxable items)");
        CheckoutService checkout = new CheckoutService();
        checkout.displayCheckoutSummary(cart, 0.08, 0.0);
        
        // ====================
        // SCENARIO 7: Stock Validation
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 7: STOCK VALIDATION");
        System.out.println("========================================\n");
        
        cart.clearCart();
        
        System.out.println("Laptop stock: " + laptop.getStock());
        System.out.println("\n❌ Trying to add 100 laptops (only " + laptop.getStock() + " available):");
        try {
            cart.addItem(laptop, 100);
        } catch (IllegalStateException e) {
            System.out.println("   Error: " + e.getMessage());
            System.out.println("   ✅ Stock validation works!");
        }
        
        System.out.println("\n✅ Adding 2 laptops:");
        cart.addItem(laptop, 2);
        cart.displayCart();
        
        // ====================
        // SCENARIO 8: Bulk Discount
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 8: BULK DISCOUNT");
        System.out.println("========================================\n");
        
        cart.clearCart();
        cart.addItem(shirt, 5);
        cart.addItem(jeans, 3);
        cart.addItem(book, 2);
        
        System.out.printf("Total items: %d\n", cart.getTotalItemCount());
        cart.displayCart();
        
        System.out.println("\n💰 Applying 15% bulk discount (10+ items)...");
        cart.addDiscount(new BulkDiscount(10, 15.0));
        
        System.out.printf("Bulk discount: -$%.2f\n", cart.getTotalDiscounts());
        System.out.printf("After discount: $%.2f\n", cart.getAmountAfterDiscounts());
        
        // ====================
        // SCENARIO 9: Tax-Exempt Items
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 9: TAX-EXEMPT ITEMS");
        System.out.println("========================================\n");
        
        cart.clearCart();
        cart.addItem(laptop, 1);   // Taxable
        cart.addItem(milk, 2);     // Tax-exempt (groceries)
        cart.addItem(book, 1);     // Tax-exempt (books)
        
        cart.displayCart();
        
        System.out.println("\n📋 Tax Information:");
        System.out.println("  Electronics: Taxable");
        System.out.println("  Groceries: Tax-exempt");
        System.out.println("  Books: Tax-exempt");
        
        checkout.displayCheckoutSummary(cart, 0.08, 0.02);
        
        // ====================
        // SCENARIO 10: Complete Checkout
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 10: COMPLETE CHECKOUT");
        System.out.println("========================================\n");
        
        cart.clearCart();
        cart.addItem(laptop, 1);
        cart.addItem(shirt, 3);
        cart.addItem(book, 2);
        
        cart.addDiscount(new PercentageDiscount(10.0));
        
        Coupon finalCoupon = new Coupon("FINAL50", CouponType.FIXED_AMOUNT_OFF,
            50.0, 1000.0, LocalDate.now().plusDays(30), 1);
        cart.applyCoupon(finalCoupon);
        
        Order order = checkout.checkout(cart, 0.08, 0.02);
        order.displayOrderSummary();
        
        System.out.println("\n✅ Order placed successfully!");
        System.out.println("Order ID: " + order.getOrderId());
        
        // ====================
        // Final Summary
        // ====================
        System.out.println("\n========================================");
        System.out.println("  ✅ DEMO COMPLETE!");
        System.out.println("========================================\n");
        
        System.out.println("Design Patterns Demonstrated:");
        System.out.println("✓ Strategy Pattern - Multiple discount strategies");
        System.out.println("✓ Decorator Pattern - Tax calculation layers");
        System.out.println("✓ Observer Pattern - Cart change notifications");
        
        System.out.println("\nKey Features Demonstrated:");
        System.out.println("✓ Add/remove/update cart items");
        System.out.println("✓ Percentage discounts (10%, 15%, 20%)");
        System.out.println("✓ Fixed amount discounts");
        System.out.println("✓ BOGO discounts (Buy 2 Get 1 Free)");
        System.out.println("✓ Category-specific discounts");
        System.out.println("✓ Bulk purchase discounts");
        System.out.println("✓ Coupon validation (expiry, min order, usage limit)");
        System.out.println("✓ Tax calculation with tax-exempt categories");
        System.out.println("✓ Multiple tax layers (state + local)");
        System.out.println("✓ Stock validation");
        System.out.println("✓ Complex pricing scenarios");
        System.out.println("✓ Detailed order summaries");
        
        System.out.println("\nExtensibility:");
        System.out.println("✓ Easy to add new discount types (implement DiscountStrategy)");
        System.out.println("✓ Easy to add new tax rules (stack TaxDecorators)");
        System.out.println("✓ Easy to add new observers (implement CartObserver)");
        System.out.println("✓ Clean separation of concerns");
    }
}
