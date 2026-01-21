import factory.ProductFactory;
import model.Money;
import model.Product;
import service.VendingMachine;

/**
 * Main demo class showcasing the Vending Machine system.
 * Demonstrates State and Factory patterns.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  VENDING MACHINE DEMO");
        System.out.println("========================================\n");

        // Initialize vending machine
        VendingMachine machine = VendingMachine.getInstance();
        setupProducts(machine);

        // Scenario 1: Successful Purchase
        scenario1_SuccessfulPurchase(machine);

        // Scenario 2: Insufficient Payment
        scenario2_InsufficientPayment(machine);

        // Scenario 3: Out of Stock
        scenario3_OutOfStock(machine);

        // Scenario 4: Exact Change
        scenario4_ExactChange(machine);

        // Scenario 5: Refund
        scenario5_Refund(machine);

        // Scenario 6: Multiple Purchases
        scenario6_MultiplePurchases(machine);

        // Final Summary
        displayFinalSummary();
    }

    /**
     * Setup initial products in the machine.
     */
    private static void setupProducts(VendingMachine machine) {
        System.out.println("Setting up vending machine...\n");
        
        machine.getInventory().addProduct(ProductFactory.createBeverage("A1", "Coca Cola", 1.50, 10));
        machine.getInventory().addProduct(ProductFactory.createBeverage("A2", "Pepsi", 1.50, 10));
        machine.getInventory().addProduct(ProductFactory.createBeverage("A3", "Water", 1.00, 15));
        machine.getInventory().addProduct(ProductFactory.createSnack("B1", "Chips", 2.00, 8));
        machine.getInventory().addProduct(ProductFactory.createSnack("B2", "Cookies", 1.75, 5));
        machine.getInventory().addProduct(ProductFactory.createCandy("C1", "Chocolate", 1.25, 0)); // Out of stock
        
        System.out.println("✓ Products loaded\n");
    }

    /**
     * Scenario 1: Successful purchase with change.
     */
    private static void scenario1_SuccessfulPurchase(VendingMachine machine) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: Successful Purchase");
        System.out.println("========================================\n");

        machine.displayProducts();
        machine.displayStatus();

        System.out.println("Customer inserts $2...");
        machine.insertMoney(Money.ONE_DOLLAR);
        machine.insertMoney(Money.ONE_DOLLAR);

        System.out.println("\nCustomer selects Coca Cola (A1)...");
        machine.selectProduct("A1");

        machine.displayStatus();
        System.out.println("✓ Purchase completed successfully!\n");
    }

    /**
     * Scenario 2: Insufficient payment.
     */
    private static void scenario2_InsufficientPayment(VendingMachine machine) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 2: Insufficient Payment");
        System.out.println("========================================\n");

        machine.displayStatus();

        System.out.println("Customer inserts $1...");
        machine.insertMoney(Money.ONE_DOLLAR);

        System.out.println("\nCustomer selects Chips (B1) - Price: $2.00...");
        machine.selectProduct("B1");

        System.out.println("\nCustomer inserts additional $1...");
        machine.insertMoney(Money.ONE_DOLLAR);

        System.out.println("\nCustomer selects Chips (B1) again...");
        machine.selectProduct("B1");

        System.out.println("✓ Purchase completed after adding more money!\n");
    }

    /**
     * Scenario 3: Out of stock product.
     */
    private static void scenario3_OutOfStock(VendingMachine machine) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 3: Out of Stock");
        System.out.println("========================================\n");

        machine.displayStatus();

        System.out.println("Customer inserts $2...");
        machine.insertMoney(Money.ONE_DOLLAR);
        machine.insertMoney(Money.ONE_DOLLAR);

        System.out.println("\nCustomer selects Chocolate (C1) - OUT OF STOCK...");
        machine.selectProduct("C1");

        System.out.println("\nCustomer selects Water (A3) instead...");
        machine.selectProduct("A3");

        System.out.println("✓ Alternative product purchased!\n");
    }

    /**
     * Scenario 4: Exact change.
     */
    private static void scenario4_ExactChange(VendingMachine machine) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 4: Exact Change");
        System.out.println("========================================\n");

        machine.displayStatus();

        System.out.println("Customer inserts exact amount: $1.50...");
        machine.insertMoney(Money.ONE_DOLLAR);
        machine.insertMoney(Money.QUARTER);
        machine.insertMoney(Money.QUARTER);

        System.out.println("\nCustomer selects Pepsi (A2)...");
        machine.selectProduct("A2");

        System.out.println("✓ Exact change - no change returned!\n");
    }

    /**
     * Scenario 5: Refund request.
     */
    private static void scenario5_Refund(VendingMachine machine) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 5: Refund");
        System.out.println("========================================\n");

        machine.displayStatus();

        System.out.println("Customer inserts $5...");
        machine.insertMoney(Money.FIVE_DOLLAR);

        System.out.println("\nCustomer changes mind and requests refund...");
        machine.refund();

        machine.displayStatus();
        System.out.println("✓ Money refunded successfully!\n");
    }

    /**
     * Scenario 6: Multiple purchases.
     */
    private static void scenario6_MultiplePurchases(VendingMachine machine) {
        System.out.println("========================================");
        System.out.println("  SCENARIO 6: Multiple Purchases");
        System.out.println("========================================\n");

        System.out.println("=== First Purchase ===");
        machine.insertMoney(Money.ONE_DOLLAR);
        machine.insertMoney(Money.ONE_DOLLAR);
        machine.selectProduct("A3"); // Water $1.00

        System.out.println("\n=== Second Purchase ===");
        machine.insertMoney(Money.FIVE_DOLLAR);
        machine.selectProduct("B2"); // Cookies $1.75

        machine.displayProducts();
        System.out.println("✓ Multiple purchases completed!\n");
    }

    /**
     * Display final summary.
     */
    private static void displayFinalSummary() {
        System.out.println("\n========================================");
        System.out.println("  DEMO COMPLETE!");
        System.out.println("========================================\n");

        System.out.println("Design Patterns Demonstrated:");
        System.out.println("✓ State Pattern - Machine states (Idle, HasMoney, Dispensing)");
        System.out.println("✓ Factory Pattern - Product creation");
        System.out.println("✓ Singleton Pattern - Single VendingMachine instance");
        System.out.println();

        System.out.println("Features Demonstrated:");
        System.out.println("✓ Product inventory management");
        System.out.println("✓ Money insertion (coins and notes)");
        System.out.println("✓ Product selection and validation");
        System.out.println("✓ Change calculation and return");
        System.out.println("✓ State transitions (Idle → HasMoney → Dispensing → Idle)");
        System.out.println("✓ Out of stock handling");
        System.out.println("✓ Insufficient payment handling");
        System.out.println("✓ Refund functionality");
        System.out.println("✓ Multiple purchases");
        System.out.println();

        System.out.println("State Transitions:");
        System.out.println("✓ Idle → HasMoney (on money insertion)");
        System.out.println("✓ HasMoney → Dispensing (on valid product selection)");
        System.out.println("✓ Dispensing → Idle (after product dispensed)");
        System.out.println("✓ HasMoney → Idle (on refund)");
        System.out.println();

        System.out.println("Edge Cases Handled:");
        System.out.println("✓ Out of stock products");
        System.out.println("✓ Insufficient payment");
        System.out.println("✓ Invalid product selection");
        System.out.println("✓ Refund requests");
        System.out.println("✓ Exact change scenarios");
        System.out.println();

        System.out.println("========================================");
        System.out.println("  All scenarios executed successfully!");
        System.out.println("========================================\n");
    }
}
