import model.*;
import enums.*;
import service.SplitwiseSystem;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("          💰 SPLITWISE EXPENSE SHARING SYSTEM 💰");
        System.out.println("════════════════════════════════════════════════════════════\n");
        
        // Initialize system
        SplitwiseSystem system = SplitwiseSystem.getInstance("Splitwise");
        
        // Setup users
        User alice = new User("U001", "Alice", "alice@email.com", "555-0001");
        User bob = new User("U002", "Bob", "bob@email.com", "555-0002");
        User charlie = new User("U003", "Charlie", "charlie@email.com", "555-0003");
        User david = new User("U004", "David", "david@email.com", "555-0004");
        
        system.registerUser(alice);
        system.registerUser(bob);
        system.registerUser(charlie);
        system.registerUser(david);
        
        System.out.println();
        
        // Run scenarios
        scenario1_EqualSplit(system, alice, bob, charlie);
        scenario2_ExactSplit(system, alice, bob, charlie);
        scenario3_PercentageSplit(system, alice, bob, charlie);
        scenario4_ShareSplit(system, alice, bob, charlie);
        scenario5_MultipleExpensesAndBalances(system, alice, bob, charlie);
        scenario6_DebtSimplification(system, alice, bob, charlie, david);
        scenario7_Settlement(system, alice, bob);
        scenario8_GroupExpenses(system, alice, bob, charlie);
        
        // Final summary
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("          ✅ DEMO COMPLETE!");
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("\n🎨 Design Patterns Demonstrated:");
        System.out.println("  ✓ Strategy Pattern - 4 split strategies (Equal, Exact, Percent, Share)");
        System.out.println("  ✓ Factory Pattern - Expense creation with split calculation");
        System.out.println("  ✓ Observer Pattern - User notifications for expenses and settlements");
        System.out.println("  ✓ Singleton Pattern - SplitwiseSystem instance management");
        System.out.println("\n📊 Features Demonstrated:");
        System.out.println("  ✓ Equal split - divide equally among participants");
        System.out.println("  ✓ Exact split - specify exact amounts");
        System.out.println("  ✓ Percentage split - split by percentages");
        System.out.println("  ✓ Share split - split by shares/ratios");
        System.out.println("  ✓ Balance calculation - track who owes whom");
        System.out.println("  ✓ Debt simplification - minimize transactions (CRITICAL!)");
        System.out.println("  ✓ Settlement - record payments between users");
        System.out.println("  ✓ Group expenses - manage expenses within groups");
        System.out.println("  ✓ Transaction history - view all expenses and settlements");
        System.out.println("  ✓ Balance sheets - detailed balance reports per user");
        System.out.println("\n🔬 Algorithm Highlights:");
        System.out.println("  ✓ Debt simplification using greedy algorithm with priority queues");
        System.out.println("  ✓ Time complexity: O(n log n)");
        System.out.println("  ✓ Reduces transactions from O(n²) to O(n)");
        System.out.println("════════════════════════════════════════════════════════════\n");
    }
    
    private static void scenario1_EqualSplit(SplitwiseSystem system, User alice, User bob, User charlie) {
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 1: Equal Split");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Dinner expense: $300 paid by Alice, split equally among 3 people");
        
        List<Split> splits = Arrays.asList(
            new EqualSplit(alice),
            new EqualSplit(bob),
            new EqualSplit(charlie)
        );
        
        Expense expense = system.addExpense("Dinner at Restaurant", 300.0, alice, 
            splits, SplitType.EQUAL, ExpenseCategory.FOOD);
        
        System.out.println("\n💡 Split breakdown:");
        for (Split split : expense.getSplits()) {
            System.out.println("  - " + split);
        }
        
        System.out.println("\n📊 Balances after expense:");
        System.out.println("  Bob owes Alice: $" + String.format("%.2f", Math.abs(bob.getBalanceWith(alice))));
        System.out.println("  Charlie owes Alice: $" + String.format("%.2f", Math.abs(charlie.getBalanceWith(alice))));
        
        system.displayBalanceSheet(alice);
    }
    
    private static void scenario2_ExactSplit(SplitwiseSystem system, User alice, User bob, User charlie) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 2: Exact Split");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Shopping expense: $500 paid by Alice");
        System.out.println("   Bob's share: $200, Charlie's share: $300");
        
        List<Split> splits = Arrays.asList(
            new ExactSplit(bob, 200.0),
            new ExactSplit(charlie, 300.0)
        );
        
        Expense expense = system.addExpense("Shopping", 500.0, alice, 
            splits, SplitType.EXACT, ExpenseCategory.SHOPPING);
        
        System.out.println("\n💡 Split breakdown:");
        for (Split split : expense.getSplits()) {
            System.out.println("  - " + split);
        }
        
        System.out.println("\n📊 Updated balances:");
        System.out.println("  Bob owes Alice: $" + String.format("%.2f", Math.abs(bob.getBalanceWith(alice))));
        System.out.println("  Charlie owes Alice: $" + String.format("%.2f", Math.abs(charlie.getBalanceWith(alice))));
    }
    
    private static void scenario3_PercentageSplit(SplitwiseSystem system, User alice, User bob, User charlie) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 3: Percentage Split");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Rent expense: $3000 paid by Alice");
        System.out.println("   Alice: 50%, Bob: 30%, Charlie: 20%");
        
        List<Split> splits = Arrays.asList(
            new PercentSplit(alice, 50.0),
            new PercentSplit(bob, 30.0),
            new PercentSplit(charlie, 20.0)
        );
        
        Expense expense = system.addExpense("Monthly Rent", 3000.0, alice, 
            splits, SplitType.PERCENT, ExpenseCategory.RENT);
        
        System.out.println("\n💡 Split breakdown:");
        for (Split split : expense.getSplits()) {
            System.out.println("  - " + split);
        }
        
        System.out.println("\n📊 Net effect:");
        System.out.println("  Alice paid $3000, owes $1500 (50%) → Net: +$1500");
        System.out.println("  Bob owes Alice: $" + String.format("%.2f", Math.abs(bob.getBalanceWith(alice))));
        System.out.println("  Charlie owes Alice: $" + String.format("%.2f", Math.abs(charlie.getBalanceWith(alice))));
    }
    
    private static void scenario4_ShareSplit(SplitwiseSystem system, User alice, User bob, User charlie) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 4: Share Split");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Groceries expense: $120 paid by Bob");
        System.out.println("   Alice: 2 shares, Bob: 1 share, Charlie: 1 share");
        System.out.println("   Total: 4 shares, Each share = $30");
        
        List<Split> splits = Arrays.asList(
            new ShareSplit(alice, 2),
            new ShareSplit(bob, 1),
            new ShareSplit(charlie, 1)
        );
        
        Expense expense = system.addExpense("Groceries", 120.0, bob, 
            splits, SplitType.SHARE, ExpenseCategory.FOOD);
        
        System.out.println("\n💡 Split breakdown:");
        for (Split split : expense.getSplits()) {
            System.out.println("  - " + split);
        }
        
        System.out.println("\n📊 Net effect:");
        System.out.println("  Alice owes Bob: $60 (2 shares)");
        System.out.println("  Charlie owes Bob: $30 (1 share)");
        System.out.println("  Bob paid $120, owes $30 (1 share) → Net: +$90");
    }
    
    private static void scenario5_MultipleExpensesAndBalances(SplitwiseSystem system, User alice, User bob, User charlie) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 5: Multiple Expenses & Balance Calculation");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Adding multiple expenses to see cumulative balances:");
        
        // Expense 1: Alice pays $300, split equally
        System.out.println("\n  Expense 1: Alice pays $300 (equal split)");
        List<Split> splits1 = Arrays.asList(
            new EqualSplit(alice),
            new EqualSplit(bob),
            new EqualSplit(charlie)
        );
        system.addExpense("Dinner", 300.0, alice, splits1, SplitType.EQUAL, ExpenseCategory.FOOD);
        
        // Expense 2: Bob pays $150, split equally
        System.out.println("  Expense 2: Bob pays $150 (equal split)");
        List<Split> splits2 = Arrays.asList(
            new EqualSplit(alice),
            new EqualSplit(bob),
            new EqualSplit(charlie)
        );
        system.addExpense("Movie", 150.0, bob, splits2, SplitType.EQUAL, ExpenseCategory.ENTERTAINMENT);
        
        // Expense 3: Charlie pays $90, split equally
        System.out.println("  Expense 3: Charlie pays $90 (equal split)");
        List<Split> splits3 = Arrays.asList(
            new EqualSplit(alice),
            new EqualSplit(bob),
            new EqualSplit(charlie)
        );
        system.addExpense("Snacks", 90.0, charlie, splits3, SplitType.EQUAL, ExpenseCategory.FOOD);
        
        System.out.println("\n📊 Net Balance Calculation:");
        System.out.println("  Alice: +$200 - $50 - $30 = +$120 (gets back)");
        System.out.println("  Bob: -$100 + $100 - $30 = -$30 (owes)");
        System.out.println("  Charlie: -$100 - $50 + $60 = -$90 (owes)");
        
        system.displayAllBalances();
        
        System.out.println("✓ Balance validation: " + 
            (system.validateSystemBalance() ? "PASS (sum = 0)" : "FAIL"));
    }
    
    private static void scenario6_DebtSimplification(SplitwiseSystem system, User alice, User bob, User charlie, User david) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 6: Debt Simplification (CRITICAL!)");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Creating complex debt scenario for simplification:");
        
        // Create a fresh scenario for simplification demo
        // Reset by creating new users
        User u1 = new User("U101", "User1", "u1@email.com", "555-0101");
        User u2 = new User("U102", "User2", "u2@email.com", "555-0102");
        User u3 = new User("U103", "User3", "u3@email.com", "555-0103");
        User u4 = new User("U104", "User4", "u4@email.com", "555-0104");
        
        system.registerUser(u1);
        system.registerUser(u2);
        system.registerUser(u3);
        system.registerUser(u4);
        
        // Create expenses that result in complex debts
        // U1 pays $400 for all 4 people
        List<Split> s1 = Arrays.asList(
            new EqualSplit(u1), new EqualSplit(u2), new EqualSplit(u3), new EqualSplit(u4)
        );
        system.addExpense("Trip", 400.0, u1, s1, SplitType.EQUAL, ExpenseCategory.TRAVEL);
        
        // U2 pays $200 for U2, U3, U4
        List<Split> s2 = Arrays.asList(
            new EqualSplit(u2), new EqualSplit(u3), new EqualSplit(u4)
        );
        system.addExpense("Hotel", 300.0, u2, s2, SplitType.EQUAL, ExpenseCategory.TRAVEL);
        
        // U3 pays $150 for U3, U4
        List<Split> s3 = Arrays.asList(
            new EqualSplit(u3), new EqualSplit(u4)
        );
        system.addExpense("Food", 150.0, u3, s3, SplitType.EQUAL, ExpenseCategory.FOOD);
        
        System.out.println("\n🔍 Current balances:");
        System.out.println("  User1 net: $" + String.format("%.2f", u1.getTotalBalance()));
        System.out.println("  User2 net: $" + String.format("%.2f", u2.getTotalBalance()));
        System.out.println("  User3 net: $" + String.format("%.2f", u3.getTotalBalance()));
        System.out.println("  User4 net: $" + String.format("%.2f", u4.getTotalBalance()));
        
        System.out.println("\n🔄 Applying debt simplification algorithm...");
        system.displaySimplifiedDebts();
        
        System.out.println("💡 Algorithm explanation:");
        System.out.println("  1. Calculate net balance for each user");
        System.out.println("  2. Separate creditors (positive) and debtors (negative)");
        System.out.println("  3. Use priority queues to match largest amounts");
        System.out.println("  4. Minimize number of transactions needed");
        System.out.println("  5. Time complexity: O(n log n)");
    }
    
    private static void scenario7_Settlement(SplitwiseSystem system, User alice, User bob) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 7: Settlement");
        System.out.println("════════════════════════════════════════════════════════════");
        
        double owedAmount = Math.abs(bob.getBalanceWith(alice));
        System.out.println("\n📝 Current: Bob owes Alice $" + String.format("%.2f", owedAmount));
        System.out.println("   Action: Bob settles $100 with Alice");
        
        Settlement settlement = system.settleUp(bob, alice, 100.0);
        
        System.out.println("\n✓ Settlement recorded: " + settlement);
        
        double remainingAmount = Math.abs(bob.getBalanceWith(alice));
        System.out.println("📊 After settlement:");
        System.out.println("  Bob still owes Alice: $" + String.format("%.2f", remainingAmount));
        
        system.displayBalanceSheet(bob);
    }
    
    private static void scenario8_GroupExpenses(SplitwiseSystem system, User alice, User bob, User charlie) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("  SCENARIO 8: Group Expenses");
        System.out.println("════════════════════════════════════════════════════════════");
        
        System.out.println("\n📝 Creating group 'Roommates' with Alice, Bob, Charlie");
        
        Group roommates = system.createGroup("G001", "Roommates", 
            Arrays.asList(alice, bob, charlie));
        
        System.out.println("\n  Adding group expenses:");
        
        // Rent
        List<Split> rentSplits = Arrays.asList(
            new EqualSplit(alice), new EqualSplit(bob), new EqualSplit(charlie)
        );
        system.addGroupExpense("G001", "Monthly Rent", 3000.0, alice, 
            rentSplits, SplitType.EQUAL, ExpenseCategory.RENT);
        System.out.println("  ✓ Rent: $3000 (equal split)");
        
        // Utilities
        List<Split> utilitySplits = Arrays.asList(
            new EqualSplit(alice), new EqualSplit(bob), new EqualSplit(charlie)
        );
        system.addGroupExpense("G001", "Utilities", 300.0, bob, 
            utilitySplits, SplitType.EQUAL, ExpenseCategory.UTILITIES);
        System.out.println("  ✓ Utilities: $300 (equal split)");
        
        // Internet
        List<Split> internetSplits = Arrays.asList(
            new EqualSplit(alice), new EqualSplit(bob), new EqualSplit(charlie)
        );
        system.addGroupExpense("G001", "Internet", 100.0, charlie, 
            internetSplits, SplitType.EQUAL, ExpenseCategory.BILLS);
        System.out.println("  ✓ Internet: $100 (equal split)");
        
        System.out.println("\n📊 Group summary:");
        System.out.println("  " + roommates);
        
        System.out.println("\n💰 Individual balance sheets:");
        system.displayBalanceSheet(alice);
        system.displayBalanceSheet(bob);
        system.displayBalanceSheet(charlie);
        
        system.displayExpenseHistory();
    }
}
