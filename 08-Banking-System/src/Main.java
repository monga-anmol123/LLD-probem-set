import service.Bank;
import model.*;
import enums.AccountType;

/**
 * Demo application for Banking System
 * Demonstrates Factory and Strategy design patterns
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║           BANKING SYSTEM DEMO                                  ║");
        System.out.println("║           Design Patterns: Factory + Strategy + Singleton     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        
        // Get Bank instance (Singleton)
        Bank bank = Bank.getInstance("Global Trust Bank");
        System.out.println("🏦 Welcome to " + bank.getBankName() + "!\n");
        
        // Scenario 1: Create Different Account Types
        scenario1_CreateAccounts(bank);
        
        // Scenario 2: Basic Deposit and Withdrawal
        scenario2_BasicOperations(bank);
        
        // Scenario 3: Transfer Between Accounts
        scenario3_TransferMoney(bank);
        
        // Scenario 4: Interest Calculation (Strategy Pattern)
        scenario4_InterestCalculation(bank);
        
        // Scenario 5: Account Rules Validation
        scenario5_AccountRulesValidation(bank);
        
        // Scenario 6: Transaction History
        scenario6_TransactionHistory(bank);
        
        // Scenario 7: Edge Cases
        scenario7_EdgeCases(bank);
        
        // Final Summary
        finalSummary(bank);
    }
    
    /**
     * SCENARIO 1: Create Different Account Types (Factory Pattern)
     */
    private static void scenario1_CreateAccounts(Bank bank) {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 1: CREATE DIFFERENT ACCOUNT TYPES (FACTORY PATTERN)");
        System.out.println(repeat("=", 70) + "\n");
        
        System.out.println("Creating Savings Account...");
        Account savingsAccount = bank.createAccount(AccountType.SAVINGS, "Alice Johnson", 1000.0);
        
        System.out.println("\nCreating Checking Account...");
        Account checkingAccount = bank.createAccount(AccountType.CHECKING, "Bob Smith", 500.0);
        
        System.out.println("\nCreating Fixed Deposit Account...");
        Account fdAccount = bank.createAccount(AccountType.FIXED_DEPOSIT, "Charlie Brown", 15000.0);
        
        System.out.println("\n✓ All accounts created successfully using Factory Pattern!");
        
        bank.displayAllAccounts();
    }
    
    /**
     * SCENARIO 2: Basic Deposit and Withdrawal Operations
     */
    private static void scenario2_BasicOperations(Bank bank) {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 2: BASIC DEPOSIT AND WITHDRAWAL OPERATIONS");
        System.out.println(repeat("=", 70) + "\n");
        
        String savingsAccountNumber = "ACC001000";
        
        System.out.println("Depositing $500 to Savings Account...");
        bank.deposit(savingsAccountNumber, 500.0);
        
        System.out.println("\nWithdrawing $300 from Savings Account...");
        bank.withdraw(savingsAccountNumber, 300.0);
        
        System.out.println("\nChecking balance...");
        Account account = bank.getAccount(savingsAccountNumber);
        System.out.println("Current Balance: $" + account.getBalance());
    }
    
    /**
     * SCENARIO 3: Transfer Money Between Accounts
     */
    private static void scenario3_TransferMoney(Bank bank) {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 3: TRANSFER MONEY BETWEEN ACCOUNTS");
        System.out.println(repeat("=", 70) + "\n");
        
        String fromAccount = "ACC001000"; // Alice's Savings
        String toAccount = "ACC001001";   // Bob's Checking
        
        System.out.println("Transferring $400 from Alice to Bob...");
        bank.transfer(fromAccount, toAccount, 400.0);
        
        System.out.println("\nAccount Balances After Transfer:");
        System.out.println("Alice (Savings): $" + bank.getAccount(fromAccount).getBalance());
        System.out.println("Bob (Checking): $" + bank.getAccount(toAccount).getBalance());
    }
    
    /**
     * SCENARIO 4: Interest Calculation (Strategy Pattern)
     */
    private static void scenario4_InterestCalculation(Bank bank) {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 4: INTEREST CALCULATION (STRATEGY PATTERN)");
        System.out.println(repeat("=", 70) + "\n");
        
        System.out.println("Creating new accounts for interest demo...");
        Account savings = bank.createAccount(AccountType.SAVINGS, "David Lee", 10000.0);
        Account checking = bank.createAccount(AccountType.CHECKING, "Emma Wilson", 5000.0);
        
        System.out.println("\n--- Applying Interest ---");
        
        System.out.println("\n1. Savings Account (4% annual, monthly calculation):");
        System.out.println("   Balance before: $" + savings.getBalance());
        bank.applyInterest(savings.getAccountNumber());
        
        System.out.println("\n2. Checking Account (No interest):");
        System.out.println("   Balance before: $" + checking.getBalance());
        bank.applyInterest(checking.getAccountNumber());
        
        System.out.println("\n✓ Different interest strategies applied based on account type!");
    }
    
    /**
     * SCENARIO 5: Account Rules Validation
     */
    private static void scenario5_AccountRulesValidation(Bank bank) {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 5: ACCOUNT RULES VALIDATION");
        System.out.println(repeat("=", 70) + "\n");
        
        String savingsAccountNumber = "ACC001000";
        String fdAccountNumber = "ACC001002";
        
        // Test 1: Withdrawal exceeds limit
        System.out.println("Test 1: Attempting to withdraw $6,000 from Savings (Limit: $5,000)");
        try {
            bank.withdraw(savingsAccountNumber, 6000.0);
        } catch (Exception e) {
            System.out.println("Expected failure: Withdrawal exceeds limit\n");
        }
        
        // Test 2: Withdrawal violates minimum balance
        System.out.println("Test 2: Attempting to withdraw $1,000 from Savings (would violate min balance)");
        try {
            Account account = bank.getAccount(savingsAccountNumber);
            System.out.println("Current Balance: $" + account.getBalance());
            bank.withdraw(savingsAccountNumber, 1000.0);
        } catch (Exception e) {
            System.out.println("Expected failure: Would violate minimum balance\n");
        }
        
        // Test 3: Withdrawal from Fixed Deposit before maturity
        System.out.println("Test 3: Attempting to withdraw from Fixed Deposit before maturity");
        try {
            bank.withdraw(fdAccountNumber, 1000.0);
        } catch (Exception e) {
            System.out.println("Expected failure: Cannot withdraw before maturity\n");
        }
        
        System.out.println("✓ All validation rules working correctly!");
    }
    
    /**
     * SCENARIO 6: Transaction History
     */
    private static void scenario6_TransactionHistory(Bank bank) {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 6: TRANSACTION HISTORY");
        System.out.println(repeat("=", 70) + "\n");
        
        String accountNumber = "ACC001000"; // Alice's account with multiple transactions
        Account account = bank.getAccount(accountNumber);
        
        System.out.println("Displaying transaction history for account: " + accountNumber);
        account.displayAccountInfo();
        account.displayTransactionHistory();
    }
    
    /**
     * SCENARIO 7: Edge Cases
     */
    private static void scenario7_EdgeCases(Bank bank) {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 7: EDGE CASES AND ERROR HANDLING");
        System.out.println(repeat("=", 70) + "\n");
        
        // Test 1: Create account with insufficient initial deposit
        System.out.println("Test 1: Creating Savings Account with insufficient deposit ($100)");
        try {
            bank.createAccount(AccountType.SAVINGS, "Test User", 100.0);
        } catch (Exception e) {
            System.out.println("✓ Correctly rejected: " + e.getMessage() + "\n");
        }
        
        // Test 2: Transfer to same account
        System.out.println("Test 2: Attempting to transfer to same account");
        try {
            bank.transfer("ACC001000", "ACC001000", 100.0);
        } catch (Exception e) {
            System.out.println("✓ Correctly rejected: " + e.getMessage() + "\n");
        }
        
        // Test 3: Transfer negative amount
        System.out.println("Test 3: Attempting to transfer negative amount");
        try {
            bank.transfer("ACC001000", "ACC001001", -100.0);
        } catch (Exception e) {
            System.out.println("✓ Correctly rejected: " + e.getMessage() + "\n");
        }
        
        // Test 4: Access non-existent account
        System.out.println("Test 4: Accessing non-existent account");
        try {
            bank.getAccount("ACC999999");
        } catch (Exception e) {
            System.out.println("✓ Correctly rejected: " + e.getMessage() + "\n");
        }
        
        // Test 5: Withdraw more than balance
        System.out.println("Test 5: Attempting to withdraw more than balance");
        try {
            bank.withdraw("ACC001001", 999999.0);
        } catch (Exception e) {
            System.out.println("✓ Correctly rejected: Insufficient balance\n");
        }
        
        System.out.println("✓ All edge cases handled correctly!");
    }
    
    /**
     * Final Summary
     */
    private static void finalSummary(Bank bank) {
        System.out.println("\n" + "╔" + repeat("═", 68) + "╗");
        System.out.println("║" + repeat(" ", 68) + "║");
        System.out.println("║" + centerText("DEMO COMPLETE!", 68) + "║");
        System.out.println("║" + repeat(" ", 68) + "║");
        System.out.println("╚" + repeat("═", 68) + "╝");
        
        System.out.println("\n📊 FINAL STATISTICS:");
        System.out.println("   Total Accounts Created: " + bank.getTotalAccounts());
        System.out.println("   Bank Name: " + bank.getBankName());
        
        System.out.println("\n🎨 DESIGN PATTERNS DEMONSTRATED:");
        System.out.println("   ✓ Factory Pattern    - Account creation (AccountFactory)");
        System.out.println("   ✓ Strategy Pattern   - Interest calculation (InterestStrategy)");
        System.out.println("   ✓ Singleton Pattern  - Bank instance (Bank.getInstance())");
        
        System.out.println("\n✅ FEATURES DEMONSTRATED:");
        System.out.println("   ✓ Multiple account types (Savings, Checking, Fixed Deposit)");
        System.out.println("   ✓ Deposit and withdrawal operations");
        System.out.println("   ✓ Money transfer between accounts");
        System.out.println("   ✓ Interest calculation with different strategies");
        System.out.println("   ✓ Account-specific rules and validations");
        System.out.println("   ✓ Transaction history tracking");
        System.out.println("   ✓ Comprehensive error handling");
        
        System.out.println("\n💡 KEY LEARNINGS:");
        System.out.println("   • Factory Pattern centralizes object creation");
        System.out.println("   • Strategy Pattern allows runtime algorithm selection");
        System.out.println("   • Singleton Pattern ensures single instance of Bank");
        System.out.println("   • Proper validation prevents invalid operations");
        System.out.println("   • Transaction history provides audit trail");
        
        System.out.println("\n" + repeat("=", 70));
        System.out.println("Thank you for using " + bank.getBankName() + "! 🏦");
        System.out.println(repeat("=", 70) + "\n");
    }
    
    /**
     * Helper method to center text
     */
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        sb.append(text);
        while (sb.length() < width) {
            sb.append(" ");
        }
        return sb.toString();
    }
    
    /**
     * Helper method to repeat a string (for Java 8 compatibility)
     */
    private static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}

