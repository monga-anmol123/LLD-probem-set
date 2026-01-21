package service;

import model.*;
import enums.*;
import factory.ExpenseFactory;
import observer.Subject;
import java.util.*;
import java.util.stream.Collectors;

public class SplitwiseSystem {
    private static SplitwiseSystem instance;
    private String systemName;
    private Map<String, User> users;
    private Map<String, Group> groups;
    private List<Expense> expenses;
    private List<Settlement> settlements;
    private Subject notificationSubject;
    private DebtSimplifier debtSimplifier;
    private int settlementCounter = 1;
    
    private SplitwiseSystem(String systemName) {
        this.systemName = systemName;
        this.users = new HashMap<>();
        this.groups = new HashMap<>();
        this.expenses = new ArrayList<>();
        this.settlements = new ArrayList<>();
        this.notificationSubject = new Subject();
        this.debtSimplifier = new DebtSimplifier();
    }
    
    public static synchronized SplitwiseSystem getInstance(String systemName) {
        if (instance == null) {
            instance = new SplitwiseSystem(systemName);
        }
        return instance;
    }
    
    public static SplitwiseSystem getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SplitwiseSystem not initialized");
        }
        return instance;
    }
    
    // User Management
    public void registerUser(User user) {
        users.put(user.getUserId(), user);
        notificationSubject.attach(user);
        System.out.println("✓ Registered user: " + user);
    }
    
    public User getUser(String userId) {
        return users.get(userId);
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    // Group Management
    public Group createGroup(String groupId, String name, List<User> members) {
        Group group = new Group(groupId, name);
        for (User member : members) {
            group.addMember(member);
        }
        groups.put(groupId, group);
        System.out.println("✓ Created group: " + group);
        return group;
    }
    
    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }
    
    // Expense Management
    public Expense addExpense(String description, double amount, User paidBy,
                             List<Split> splits, SplitType splitType, ExpenseCategory category) {
        
        // Create expense using factory
        Expense expense = ExpenseFactory.createExpense(description, amount, paidBy, splits, splitType, category);
        
        // Update balances
        expense.updateBalances();
        
        // Store expense
        expenses.add(expense);
        
        // Notify participants
        Set<User> participants = splits.stream().map(Split::getUser).collect(Collectors.toSet());
        for (User participant : participants) {
            String message = String.format("New expense: %s ($%.2f) added by %s", 
                description, amount, paidBy.getName());
            notificationSubject.notifySpecificObserver(participant, message);
        }
        
        System.out.println("✓ Added expense: " + expense);
        
        return expense;
    }
    
    public Expense addGroupExpense(String groupId, String description, double amount, 
                                   User paidBy, List<Split> splits, SplitType splitType, 
                                   ExpenseCategory category) {
        Group group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        
        Expense expense = addExpense(description, amount, paidBy, splits, splitType, category);
        group.addExpense(expense);
        
        return expense;
    }
    
    // Settlement
    public Settlement settleUp(User payer, User receiver, double amount) {
        // Validate amount
        double currentBalance = payer.getBalanceWith(receiver);
        if (currentBalance >= 0) {
            throw new IllegalStateException(
                String.format("%s doesn't owe %s anything", payer.getName(), receiver.getName())
            );
        }
        
        double owedAmount = Math.abs(currentBalance);
        if (amount > owedAmount + 0.01) {
            throw new IllegalArgumentException(
                String.format("Cannot settle $%.2f, only owes $%.2f", amount, owedAmount)
            );
        }
        
        // Create settlement
        String settlementId = "SET-" + String.format("%04d", settlementCounter++);
        Settlement settlement = new Settlement(settlementId, payer, receiver, amount);
        
        // Update balances
        settlement.settle();
        
        // Store settlement
        settlements.add(settlement);
        
        // Notify users
        notificationSubject.notifySpecificObserver(payer, 
            String.format("You paid %s $%.2f", receiver.getName(), amount));
        notificationSubject.notifySpecificObserver(receiver, 
            String.format("%s paid you $%.2f", payer.getName(), amount));
        
        System.out.println("✓ Settlement completed: " + settlement);
        
        return settlement;
    }
    
    // Balance Sheet
    public BalanceSheet getBalanceSheet(User user) {
        return new BalanceSheet(user);
    }
    
    public void displayBalanceSheet(User user) {
        BalanceSheet sheet = getBalanceSheet(user);
        System.out.println(sheet.generateReport());
    }
    
    // Debt Simplification
    public List<Transaction> simplifyDebts() {
        List<User> allUsers = new ArrayList<>(users.values());
        return debtSimplifier.simplifyDebts(allUsers);
    }
    
    public List<Transaction> getAllTransactions() {
        List<User> allUsers = new ArrayList<>(users.values());
        return debtSimplifier.getAllTransactions(allUsers);
    }
    
    public void displaySimplifiedDebts() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              DEBT SIMPLIFICATION                           ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        List<Transaction> original = getAllTransactions();
        List<Transaction> simplified = simplifyDebts();
        
        System.out.println("║  BEFORE SIMPLIFICATION:                                    ║");
        if (original.isEmpty()) {
            System.out.println("║    All settled up!                                         ║");
        } else {
            for (Transaction txn : original) {
                System.out.println(String.format("║    %-54s ║", txn.toString()));
            }
        }
        
        System.out.println("║                                                            ║");
        System.out.println("║  AFTER SIMPLIFICATION:                                     ║");
        if (simplified.isEmpty()) {
            System.out.println("║    All settled up!                                         ║");
        } else {
            for (Transaction txn : simplified) {
                System.out.println(String.format("║    %-54s ║", txn.toString()));
            }
        }
        
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println(String.format("║  Transactions reduced from %d to %d                       ║", 
            original.size(), simplified.size()));
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
    
    // Transaction History
    public List<Expense> getExpenseHistory() {
        return new ArrayList<>(expenses);
    }
    
    public List<Settlement> getSettlementHistory() {
        return new ArrayList<>(settlements);
    }
    
    public List<Expense> getUserExpenses(User user) {
        return expenses.stream()
            .filter(e -> e.getPaidBy().equals(user) || 
                        e.getSplits().stream().anyMatch(s -> s.getUser().equals(user)))
            .collect(Collectors.toList());
    }
    
    // Display Methods
    public void displayAllBalances() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              ALL USER BALANCES                             ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        for (User user : users.values()) {
            double netBalance = user.getTotalBalance();
            String status = netBalance > 0.01 ? "gets back" : 
                           (netBalance < -0.01 ? "owes" : "settled");
            System.out.println(String.format("║  %-40s $%10.2f ║", 
                user.getName() + " " + status, Math.abs(netBalance)));
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
    
    public void displayExpenseHistory() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              EXPENSE HISTORY                               ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        if (expenses.isEmpty()) {
            System.out.println("║  No expenses recorded yet.                                 ║");
        } else {
            for (Expense expense : expenses) {
                System.out.println(String.format("║  %-56s ║", 
                    String.format("%s: $%.2f by %s", 
                        expense.getDescription(), expense.getAmount(), expense.getPaidBy().getName())));
            }
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
    
    // Validation
    public boolean validateSystemBalance() {
        return debtSimplifier.validateBalances(new ArrayList<>(users.values()));
    }
    
    // Getters
    public String getSystemName() { return systemName; }
    public Map<String, User> getUsers() { return users; }
    public Map<String, Group> getGroups() { return groups; }
    public List<Expense> getExpenses() { return expenses; }
    public List<Settlement> getSettlements() { return settlements; }
}
