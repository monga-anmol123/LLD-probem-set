# Problem 19: Splitwise (Expense Sharing System)

## 🎯 Difficulty: Hard ⭐⭐⭐⭐

## 📝 Problem Statement

Design a Splitwise-like expense sharing system that allows users to track shared expenses, split bills in multiple ways, calculate balances, simplify debts using graph algorithms, and settle up efficiently.

---

## 🔍 Functional Requirements (FR)

### FR1: User Management
- Register users with unique ID, name, email, phone
- Track user balances (who owes whom)
- View individual user balance sheet
- User can be part of multiple groups

### FR2: Group Management
- Create groups with multiple users
- Add/remove users from groups
- Track group expenses
- Calculate group balances

### FR3: Expense Management
- Add expense with description, amount, paid by user
- Support multiple split types:
  - **Equal Split:** Divide equally among participants
  - **Exact Split:** Specify exact amount for each user
  - **Percentage Split:** Split by percentage (must sum to 100%)
  - **Share Split:** Split by shares/ratios
- Track who paid and who owes
- Add expense notes/category

### FR4: Balance Calculation
- Calculate net balance for each user
- Show who owes whom and how much
- Display balance sheet per user
- Track all transactions

### FR5: Debt Simplification
- **Critical Feature:** Minimize number of transactions needed to settle all debts
- Use graph-based algorithm to simplify debts
- Example: If A owes B $10, B owes C $10 → Simplify to: A owes C $10
- Reduce transaction complexity from O(n²) to O(n)

### FR6: Settlement
- Record settlements between users
- Update balances after settlement
- Track settlement history
- Notify users about settlements

### FR7: Transaction History
- View all expenses
- View all settlements
- Filter by user, group, date range
- Export transaction history

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Scalability
- Support 10,000+ users
- Handle 100,000+ expenses
- Process 1,000+ groups
- Efficient balance calculation

### NFR2: Performance
- Balance calculation: O(n) where n = number of users
- Debt simplification: O(n log n)
- Expense addition: O(1)
- Settlement: O(1)

### NFR3: Accuracy
- Precise decimal calculations (avoid floating point errors)
- Balance sheet must always sum to zero
- No money lost or created in system

### NFR4: Extensibility
- Easy to add new split strategies
- Support for multiple currencies (future)
- Add new expense categories
- Pluggable notification systems

### NFR5: Reliability
- Handle edge cases:
  - Negative amounts
  - Invalid split percentages
  - User not in group
  - Settling more than owed
  - Circular debts

---

## 🎨 Design Patterns to Use

### 1. **Strategy Pattern**
- **Where:** Split strategies (Equal, Exact, Percentage, Share)
- **Why:** Different algorithms for splitting expenses, easy to add new strategies

### 2. **Factory Pattern**
- **Where:** Expense creation, Split strategy creation
- **Why:** Centralize object creation, support multiple expense types

### 3. **Observer Pattern**
- **Where:** Notify users about expenses, settlements, balance changes
- **Why:** Decouple notification logic from business logic

### 4. **Singleton Pattern** (Optional)
- **Where:** SplitwiseSystem instance
- **Why:** Single source of truth for all expenses and balances

---

## 📋 Core Entities

### 1. **User**
- Attributes: `userId`, `name`, `email`, `phone`, `balances` (Map<User, Double>)
- Methods: `getBalance()`, `getBalanceWith(User)`, `getTotalBalance()`
- Implements `Observer` for notifications

### 2. **Group**
- Attributes: `groupId`, `name`, `members`, `expenses`
- Methods: `addMember()`, `removeMember()`, `addExpense()`, `getGroupBalance()`

### 3. **Expense**
- Attributes: `expenseId`, `description`, `amount`, `paidBy`, `splitType`, `participants`, `splits`, `timestamp`
- Methods: `calculateSplits()`, `updateBalances()`

### 4. **Split** (Abstract)
- Attributes: `user`, `amount`
- Subclasses: `EqualSplit`, `ExactSplit`, `PercentSplit`, `ShareSplit`

### 5. **Transaction**
- Attributes: `transactionId`, `from`, `to`, `amount`, `timestamp`, `type` (EXPENSE/SETTLEMENT)
- Represents a single debt/payment

### 6. **Settlement**
- Attributes: `settlementId`, `payer`, `receiver`, `amount`, `timestamp`
- Methods: `settle()`, `updateBalances()`

### 7. **BalanceSheet**
- Attributes: `user`, `balances` (Map<User, Double>)
- Methods: `addBalance()`, `getBalance()`, `getTotalOwed()`, `getTotalOwing()`

### 8. **DebtSimplifier**
- **Critical Component:** Graph-based debt simplification
- Methods: `simplifyDebts()`, `minimizeTransactions()`
- Uses min-heap for efficient debt resolution

### 9. **SplitwiseSystem**
- Main service managing all operations
- Methods: `addExpense()`, `settleUp()`, `getBalanceSheet()`, `simplifyDebts()`

---

## 🧪 Test Scenarios

### Scenario 1: Equal Split
```
Users: Alice, Bob, Charlie
Expense: Dinner $300 paid by Alice
Split: Equal among 3 people
Result:
  - Bob owes Alice $100
  - Charlie owes Alice $100
```

### Scenario 2: Exact Split
```
Users: Alice, Bob, Charlie
Expense: Shopping $500 paid by Alice
Split: Bob $200, Charlie $300
Result:
  - Bob owes Alice $200
  - Charlie owes Alice $300
```

### Scenario 3: Percentage Split
```
Users: Alice, Bob, Charlie
Expense: Rent $3000 paid by Alice
Split: Alice 50%, Bob 30%, Charlie 20%
Result:
  - Alice paid $3000, owes $1500 (net: +$1500)
  - Bob owes Alice $900
  - Charlie owes Alice $600
```

### Scenario 4: Share Split
```
Users: Alice, Bob, Charlie
Expense: Groceries $120 paid by Bob
Split: Alice 2 shares, Bob 1 share, Charlie 1 share
Result:
  - Each share = $30
  - Alice owes Bob $60
  - Charlie owes Bob $30
```

### Scenario 5: Multiple Expenses & Balance Calculation
```
Expense 1: Alice pays $300, split equally (3 people)
Expense 2: Bob pays $150, split equally (3 people)
Expense 3: Charlie pays $90, split equally (3 people)

Net Balances:
  - Alice: +$200 - $50 - $30 = +$120
  - Bob: -$100 + $100 - $30 = -$30
  - Charlie: -$100 - $50 + $60 = -$90
```

### Scenario 6: Debt Simplification (Critical!)
```
Before Simplification:
  - Alice owes Bob $50
  - Bob owes Charlie $50
  - Charlie owes David $50
  - David owes Alice $50

After Simplification:
  - No transactions needed! (Circular debt cancels out)

Example 2:
Before:
  - Alice owes Bob $100
  - Alice owes Charlie $50
  - Bob owes Charlie $30

After:
  - Alice owes Bob $70
  - Alice owes Charlie $20
  - Bob owes Charlie $30
```

### Scenario 7: Settlement
```
Current: Bob owes Alice $100
Action: Bob settles $100 with Alice
Result: Balance between Bob and Alice = $0
```

### Scenario 8: Group Expenses
```
Group: "Roommates" (Alice, Bob, Charlie)
Expense 1: Rent $3000 (equal split)
Expense 2: Utilities $300 (equal split)
Expense 3: Internet $100 (equal split)

Group Balance Sheet shows all members' balances
```

---

## ⏱️ Time Allocation (75-90 minutes)

- **10 mins:** Clarify requirements, understand debt simplification
- **10 mins:** Design entities (User, Expense, Split, Transaction)
- **10 mins:** Design split strategies (Equal, Exact, Percent, Share)
- **15 mins:** Implement debt simplification algorithm (CRITICAL!)
- **30 mins:** Code all components
- **10 mins:** Test with comprehensive scenarios
- **5 mins:** Handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: Split Strategy Pattern</summary>

```java
public interface SplitStrategy {
    Map<User, Double> calculateSplits(double totalAmount, List<User> participants, Map<User, ?> splitData);
}

public class EqualSplitStrategy implements SplitStrategy {
    public Map<User, Double> calculateSplits(double totalAmount, List<User> participants, Map<User, ?> splitData) {
        double amountPerPerson = totalAmount / participants.size();
        Map<User, Double> splits = new HashMap<>();
        for (User user : participants) {
            splits.put(user, amountPerPerson);
        }
        return splits;
    }
}
```
</details>

<details>
<summary>Hint 2: Balance Tracking</summary>

```java
public class User {
    private Map<User, Double> balances = new HashMap<>();
    
    public void addBalance(User other, double amount) {
        balances.put(other, balances.getOrDefault(other, 0.0) + amount);
    }
    
    public double getBalanceWith(User other) {
        return balances.getOrDefault(other, 0.0);
    }
    
    public double getTotalBalance() {
        return balances.values().stream().mapToDouble(Double::doubleValue).sum();
    }
}
```
</details>

<details>
<summary>Hint 3: Debt Simplification Algorithm (CRITICAL!)</summary>

```java
public class DebtSimplifier {
    public List<Transaction> simplifyDebts(Map<User, Double> netBalances) {
        // Step 1: Separate creditors (positive balance) and debtors (negative balance)
        PriorityQueue<UserBalance> creditors = new PriorityQueue<>((a, b) -> Double.compare(b.amount, a.amount));
        PriorityQueue<UserBalance> debtors = new PriorityQueue<>((a, b) -> Double.compare(a.amount, b.amount));
        
        for (Map.Entry<User, Double> entry : netBalances.entrySet()) {
            if (entry.getValue() > 0.01) {
                creditors.add(new UserBalance(entry.getKey(), entry.getValue()));
            } else if (entry.getValue() < -0.01) {
                debtors.add(new UserBalance(entry.getKey(), entry.getValue()));
            }
        }
        
        // Step 2: Match largest creditor with largest debtor
        List<Transaction> simplifiedTransactions = new ArrayList<>();
        
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            UserBalance creditor = creditors.poll();
            UserBalance debtor = debtors.poll();
            
            double settleAmount = Math.min(creditor.amount, Math.abs(debtor.amount));
            
            simplifiedTransactions.add(new Transaction(debtor.user, creditor.user, settleAmount));
            
            creditor.amount -= settleAmount;
            debtor.amount += settleAmount;
            
            if (creditor.amount > 0.01) {
                creditors.add(creditor);
            }
            if (debtor.amount < -0.01) {
                debtors.add(debtor);
            }
        }
        
        return simplifiedTransactions;
    }
}
```

**Why this works:**
- Uses greedy approach with priority queues
- Matches largest creditor with largest debtor
- Minimizes number of transactions
- Time complexity: O(n log n)
- Guarantees minimal transactions
</details>

<details>
<summary>Hint 4: Percentage Split Validation</summary>

```java
public class PercentSplitStrategy implements SplitStrategy {
    public Map<User, Double> calculateSplits(double totalAmount, List<User> participants, Map<User, Double> percentages) {
        // Validate percentages sum to 100
        double totalPercent = percentages.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(totalPercent - 100.0) > 0.01) {
            throw new IllegalArgumentException("Percentages must sum to 100%");
        }
        
        Map<User, Double> splits = new HashMap<>();
        for (User user : participants) {
            double percent = percentages.get(user);
            splits.put(user, totalAmount * percent / 100.0);
        }
        return splits;
    }
}
```
</details>

<details>
<summary>Hint 5: Balance Sheet Calculation</summary>

```java
public class BalanceSheet {
    public static Map<User, Double> calculateNetBalances(List<User> users) {
        Map<User, Double> netBalances = new HashMap<>();
        
        for (User user : users) {
            double netBalance = 0.0;
            
            // Calculate net balance from all pairwise balances
            for (Map.Entry<User, Double> entry : user.getBalances().entrySet()) {
                netBalance += entry.getValue();
            }
            
            netBalances.put(user, netBalance);
        }
        
        return netBalances;
    }
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Multi-Currency Support**
   - Add currency field to expenses
   - Exchange rate conversion
   - Display balances in preferred currency

2. **Recurring Expenses**
   - Monthly rent, utilities
   - Auto-create expenses on schedule
   - Subscription tracking

3. **Expense Categories**
   - Food, Transport, Entertainment, Bills
   - Category-wise spending analysis
   - Budget limits per category

4. **Advanced Debt Simplification**
   - Consider transaction fees
   - Optimize for minimum total amount transferred
   - Support partial settlements

5. **Activity Feed**
   - Timeline of all activities
   - Comments on expenses
   - Like/react to expenses

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Strategy, Factory, Observer patterns correctly
- [ ] Implement all 4 split types (Equal, Exact, Percent, Share)
- [ ] **Implement debt simplification algorithm correctly**
- [ ] Handle all 8+ test scenarios
- [ ] Calculate balances accurately (sum to zero)
- [ ] Support group expenses
- [ ] Track transaction history
- [ ] Handle edge cases (invalid splits, negative amounts, etc.)
- [ ] Have clear separation: model, service, strategy, factory layers
- [ ] Generate detailed balance sheets
- [ ] Demonstrate debt simplification with before/after

---

## 📁 File Structure

```
19-Splitwise/
├── README.md (this file)
├── SOLUTION.md
├── COMPILATION-GUIDE.md
└── src/
    ├── enums/
    │   ├── SplitType.java
    │   ├── TransactionType.java
    │   └── ExpenseCategory.java
    ├── model/
    │   ├── User.java
    │   ├── Group.java
    │   ├── Expense.java
    │   ├── Split.java
    │   ├── EqualSplit.java
    │   ├── ExactSplit.java
    │   ├── PercentSplit.java
    │   ├── ShareSplit.java
    │   ├── Transaction.java
    │   ├── Settlement.java
    │   └── BalanceSheet.java
    ├── strategy/
    │   ├── SplitStrategy.java
    │   ├── EqualSplitStrategy.java
    │   ├── ExactSplitStrategy.java
    │   ├── PercentSplitStrategy.java
    │   └── ShareSplitStrategy.java
    ├── factory/
    │   └── ExpenseFactory.java
    ├── observer/
    │   ├── Observer.java
    │   └── Subject.java
    ├── service/
    │   ├── SplitwiseSystem.java
    │   └── DebtSimplifier.java
    └── Main.java
```

---

## 🎓 Key Learning Points

1. **Graph Algorithms:** Debt simplification is a graph problem
2. **Greedy Algorithms:** Matching creditors/debtors optimally
3. **Strategy Pattern:** Multiple split algorithms
4. **Balance Invariant:** Total balance must always be zero
5. **Precision:** Handling decimal arithmetic correctly

---

**Good luck! Focus on the debt simplification algorithm - it's the most critical and challenging part! 🚀**
