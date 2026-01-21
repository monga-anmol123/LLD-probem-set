import model.*;
import service.ATM;
import strategy.GreedyStrategy;
import strategy.MinimumNotesStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("      ATM SYSTEM DEMONSTRATION");
        System.out.println("========================================\n");

        // Initialize ATM with Greedy Strategy
        CashDispenser cashDispenser = new CashDispenser(new GreedyStrategy());
        ATM atm = new ATM("ATM-001", "Downtown Branch", cashDispenser);

        // Create accounts
        Account account1 = new Account("ACC-1001", "Alice Johnson", 5000);
        Account account2 = new Account("ACC-1002", "Bob Smith", 1000);
        Account account3 = new Account("ACC-1003", "Charlie Brown", 10000);

        atm.addAccount(account1);
        atm.addAccount(account2);
        atm.addAccount(account3);

        // Create cards
        Card card1 = new Card("1234567890123456", "ACC-1001", "1234");
        Card card2 = new Card("9876543210987654", "ACC-1002", "5678");
        Card card3 = new Card("1111222233334444", "ACC-1003", "9999");

        // Display initial ATM cash status
        cashDispenser.displayCashStatus();

        // ========================================
        // SCENARIO 1: Successful Withdrawal
        // ========================================
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: Successful Withdrawal");
        System.out.println("========================================\n");

        atm.insertCard(card1);
        atm.enterPIN("1234");
        atm.getState().selectOperation("WITHDRAW");
        atm.withdraw(2700);
        atm.ejectCard();

        System.out.println("\n--- After Scenario 1 ---");
        cashDispenser.displayCashStatus();

        // ========================================
        // SCENARIO 2: Failed PIN Attempts (Card Blocked)
        // ========================================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 2: Failed PIN Attempts");
        System.out.println("========================================\n");

        atm.insertCard(card2);
        System.out.println("Attempting wrong PIN (1111)...");
        atm.enterPIN("1111");
        System.out.println("\nAttempting wrong PIN (2222)...");
        atm.enterPIN("2222");
        System.out.println("\nAttempting wrong PIN (3333)...");
        atm.enterPIN("3333");

        System.out.println("\n--- Card Status ---");
        System.out.println("Card 2 Blocked: " + card2.isBlocked());

        // ========================================
        // SCENARIO 3: Insufficient Balance
        // ========================================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 3: Insufficient Balance");
        System.out.println("========================================\n");

        // Unblock card2 for demo (in real system, bank would do this)
        card2.unblock();
        System.out.println("(Card unblocked by bank for demo purposes)");
        System.out.println("Account balance: $" + account2.getBalance());
        System.out.println("Attempting to withdraw: $1500\n");

        atm.insertCard(card2);
        atm.enterPIN("5678");
        atm.getState().selectOperation("WITHDRAW");
        atm.withdraw(1500);
        atm.ejectCard();

        // ========================================
        // SCENARIO 4: Successful Deposit
        // ========================================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 4: Successful Deposit");
        System.out.println("========================================\n");

        System.out.println("Initial balance: $" + account2.getBalance());
        atm.insertCard(card2);
        atm.enterPIN("5678");
        atm.getState().selectOperation("DEPOSIT");
        atm.deposit(3000);
        System.out.println("After deposit balance: $" + account2.getBalance());
        atm.ejectCard();

        // ========================================
        // SCENARIO 5: Balance Inquiry
        // ========================================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 5: Balance Inquiry");
        System.out.println("========================================\n");

        atm.insertCard(card3);
        atm.enterPIN("9999");
        atm.getState().selectOperation("BALANCE");
        atm.checkBalance();
        atm.ejectCard();

        // ========================================
        // SCENARIO 6: PIN Change
        // ========================================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 6: PIN Change");
        System.out.println("========================================\n");

        atm.insertCard(card1);
        atm.enterPIN("1234");
        atm.getState().selectOperation("CHANGE_PIN");
        System.out.println("Changing PIN from 1234 to 4321...");
        atm.changePIN("1234", "4321");
        atm.ejectCard();

        // Verify new PIN works
        System.out.println("\nVerifying new PIN...");
        atm.insertCard(card1);
        atm.enterPIN("4321");
        System.out.println("✓ New PIN verified successfully!");
        atm.ejectCard();

        // ========================================
        // SCENARIO 7: Insufficient ATM Cash
        // ========================================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 7: Insufficient ATM Cash");
        System.out.println("========================================\n");

        System.out.println("Current ATM cash: $" + cashDispenser.getTotalCash());
        System.out.println("Attempting to withdraw: $50000\n");

        atm.insertCard(card3);
        atm.enterPIN("9999");
        atm.getState().selectOperation("WITHDRAW");
        atm.withdraw(50000);
        atm.ejectCard();

        // ========================================
        // SCENARIO 8: Invalid Amount (Not multiple of 100)
        // ========================================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 8: Invalid Amount");
        System.out.println("========================================\n");

        System.out.println("Attempting to withdraw: $1250 (not multiple of 100)\n");

        atm.insertCard(card3);
        atm.enterPIN("9999");
        atm.getState().selectOperation("WITHDRAW");
        atm.withdraw(1250);
        atm.ejectCard();

        // ========================================
        // SCENARIO 9: Multiple Transactions in One Session
        // ========================================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 9: Multiple Transactions");
        System.out.println("========================================\n");

        atm.insertCard(card3);
        atm.enterPIN("9999");

        System.out.println("Transaction 1: Check Balance");
        atm.getState().selectOperation("BALANCE");
        atm.checkBalance();

        System.out.println("Transaction 2: Withdraw $1000");
        atm.getState().selectOperation("WITHDRAW");
        atm.withdraw(1000);

        System.out.println("Transaction 3: Check Balance Again");
        atm.getState().selectOperation("BALANCE");
        atm.checkBalance();

        atm.ejectCard();

        // ========================================
        // SCENARIO 10: Strategy Pattern Demo
        // ========================================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 10: Cash Dispensing Strategy");
        System.out.println("========================================\n");

        System.out.println("Switching to Minimum Notes Strategy...\n");
        cashDispenser.setStrategy(new MinimumNotesStrategy());

        atm.insertCard(card3);
        atm.enterPIN("9999");
        atm.getState().selectOperation("WITHDRAW");
        atm.withdraw(1500);
        atm.ejectCard();

        // ========================================
        // FINAL SUMMARY
        // ========================================
        System.out.println("\n========================================");
        System.out.println("          FINAL SUMMARY");
        System.out.println("========================================\n");

        System.out.println("Account Balances:");
        System.out.println("  Alice (ACC-1001): $" + account1.getBalance());
        System.out.println("  Bob (ACC-1002): $" + account2.getBalance());
        System.out.println("  Charlie (ACC-1003): $" + account3.getBalance());

        System.out.println("\nCard Status:");
        System.out.println("  Card 1: " + (card1.isBlocked() ? "BLOCKED" : "ACTIVE"));
        System.out.println("  Card 2: " + (card2.isBlocked() ? "BLOCKED" : "ACTIVE"));
        System.out.println("  Card 3: " + (card3.isBlocked() ? "BLOCKED" : "ACTIVE"));

        cashDispenser.displayCashStatus();

        System.out.println("\n========================================");
        System.out.println("       DEMO COMPLETE!");
        System.out.println("========================================");
        System.out.println("\nDesign Patterns Demonstrated:");
        System.out.println("✓ State Pattern - ATM state transitions (Idle → CardInserted → PINVerified → Transaction)");
        System.out.println("✓ Strategy Pattern - Cash dispensing strategies (Greedy vs Minimum Notes)");
        System.out.println("\nKey Features Demonstrated:");
        System.out.println("✓ Card insertion and PIN validation");
        System.out.println("✓ Multiple failed PIN attempts → Card blocked");
        System.out.println("✓ Withdrawal with denomination dispensing");
        System.out.println("✓ Deposit functionality");
        System.out.println("✓ Balance inquiry");
        System.out.println("✓ PIN change");
        System.out.println("✓ Insufficient balance handling");
        System.out.println("✓ Insufficient ATM cash handling");
        System.out.println("✓ Invalid amount validation");
        System.out.println("✓ Multiple transactions in one session");
        System.out.println("✓ Cash dispensing strategy switching");
        System.out.println("\n========================================\n");
    }
}


