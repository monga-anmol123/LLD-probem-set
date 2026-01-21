# ATM System - Complete Solution

## 📋 Table of Contents
1. [Overview](#overview)
2. [Design Patterns Used](#design-patterns-used)
3. [Class Diagram](#class-diagram)
4. [Component Details](#component-details)
5. [State Transitions](#state-transitions)
6. [Key Design Decisions](#key-design-decisions)
7. [Trade-offs](#trade-offs)
8. [Extensions](#extensions)

---

## Overview

This ATM System implements a complete automated teller machine with support for:
- Card insertion and PIN validation
- Cash withdrawal with optimal denomination dispensing
- Cash deposit
- Balance inquiry
- PIN change
- Security features (card blocking after 3 failed attempts)
- Multiple cash dispensing strategies

**Total Lines of Code:** ~1,000 lines  
**Files:** 17 Java files  
**Design Patterns:** State Pattern, Strategy Pattern  

---

## Design Patterns Used

### 1. **State Pattern** ⭐⭐⭐

**Purpose:** Manage ATM state transitions and behavior changes based on current state.

**Implementation:**

```java
public interface ATMState {
    void insertCard(Card card);
    void enterPIN(String pin);
    void selectOperation(String operation);
    void ejectCard();
    String getStateName();
}
```

**States:**
1. **IdleState** - Waiting for card insertion
2. **CardInsertedState** - Card inserted, waiting for PIN
3. **PINVerifiedState** - PIN correct, ready for operations
4. **TransactionState** - Processing a transaction
5. **BlockedState** - Card blocked after failed PIN attempts

**Why State Pattern?**
- ✅ Clean separation of state-specific behavior
- ✅ Easy to add new states
- ✅ Prevents invalid operations in wrong states
- ✅ Clear state transition logic

**Example Flow:**
```
Idle → (insertCard) → CardInserted → (enterPIN) → PINVerified 
     → (selectOperation) → Transaction → (complete) → PINVerified
     → (ejectCard) → Idle
```

### 2. **Strategy Pattern** ⭐⭐⭐

**Purpose:** Different algorithms for cash dispensing.

**Implementation:**

```java
public interface CashDispensingStrategy {
    Map<Integer, Integer> dispenseCash(int amount, 
                                       Map<Integer, Integer> availableDenominations);
}
```

**Strategies:**
1. **GreedyStrategy** - Uses largest denominations first
2. **MinimumNotesStrategy** - Minimizes total number of notes

**Why Strategy Pattern?**
- ✅ Switch dispensing algorithms at runtime
- ✅ Easy to add new strategies (e.g., PreferSpecificDenomination)
- ✅ Encapsulates complex dispensing logic
- ✅ Testable in isolation

**Example:**
```
Amount: $2700
Available: 100x10, 500x5, 1000x3, 2000x2

Greedy Strategy:
  2000x1 + 500x1 + 100x2 = $2700

Minimum Notes Strategy:
  2000x1 + 500x1 + 100x2 = $2700 (same in this case)
```

---

## Class Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                         ATM System                          │
└─────────────────────────────────────────────────────────────┘

┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│     ATM      │◆────────│   ATMState   │         │     Card     │
│              │         │  (Interface) │         │              │
│ - atmId      │         │              │         │ - cardNumber │
│ - location   │         │ + insertCard()│        │ - pin        │
│ - state      │         │ + enterPIN() │         │ - isBlocked  │
│ - accounts   │         │ + ejectCard()│         │              │
│              │         └──────────────┘         │ + validatePIN│
│ + withdraw() │                △                 │ + changePIN()│
│ + deposit()  │                │                 │ + block()    │
│ + checkBal() │         ┌──────┴──────┐          └──────────────┘
└──────────────┘         │             │
       │                 │             │
       │          ┌──────┴─────┐ ┌────┴─────┐
       │          │ IdleState  │ │CardInsert│
       │          └────────────┘ └──────────┘
       │          ┌────────────┐ ┌──────────┐
       │          │PINVerified │ │Transaction│
       │          └────────────┘ └──────────┘
       │          ┌────────────┐
       │          │BlockedState│
       │          └────────────┘
       │
       ├────────────┐
       │            │
┌──────▼──────┐ ┌──▼──────────────┐
│   Account   │ │ CashDispenser   │
│             │ │                 │
│ - accNumber │ │ - denominations │◇────┐
│ - balance   │ │ - strategy      │     │
│ - transactions│ │                │     │
│             │ │ + dispenseCash()│     │
│ + withdraw()│ │ + canDispense() │     │
│ + deposit() │ └─────────────────┘     │
└─────────────┘                         │
       │                                │
       │                         ┌──────▼──────────────┐
       │                         │CashDispensingStrategy│
       │                         │    (Interface)       │
       │                         │                      │
       │                         │ + dispenseCash()     │
       │                         └──────────────────────┘
       │                                  △
┌──────▼──────┐                          │
│Transaction  │                   ┌──────┴──────┐
│             │                   │             │
│ - txnId     │            ┌──────┴─────┐ ┌────┴────────┐
│ - type      │            │   Greedy   │ │MinimumNotes │
│ - amount    │            │  Strategy  │ │  Strategy   │
│ - timestamp │            └────────────┘ └─────────────┘
│ - status    │
└─────────────┘
```

---

## Component Details

### 1. **Model Layer**

#### Card
```java
public class Card {
    private String cardNumber;
    private String accountNumber;
    private String pin;
    private boolean isBlocked;
    
    public boolean validatePIN(String inputPin);
    public void changePIN(String oldPin, String newPin);
    public void block();
    public String maskCardNumber(); // Returns ****-****-****-1234
}
```

**Responsibilities:**
- Store card details
- Validate PIN
- Track blocked status
- Provide masked card number for security

#### Account
```java
public class Account {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private List<Transaction> transactions;
    
    public boolean withdraw(double amount);
    public void deposit(double amount);
    public List<Transaction> getRecentTransactions(int count);
}
```

**Responsibilities:**
- Manage account balance
- Process withdrawals and deposits
- Track transaction history
- Validate sufficient balance

#### Transaction
```java
public class Transaction {
    private String transactionId;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
    private TransactionStatus status;
}
```

**Responsibilities:**
- Record transaction details
- Generate unique transaction ID
- Format timestamp for receipts

#### CashDispenser
```java
public class CashDispenser {
    private Map<Integer, Integer> denominations; // denom -> count
    private CashDispensingStrategy strategy;
    
    public boolean canDispense(double amount);
    public Map<Integer, Integer> dispenseCash(double amount);
    public void addCash(int denomination, int count);
    public double getTotalCash();
}
```

**Responsibilities:**
- Track available cash by denomination
- Validate if amount can be dispensed
- Use strategy to determine optimal dispensing
- Update denomination counts after dispensing

### 2. **State Layer**

All states implement `ATMState` interface and handle operations differently:

| State | insertCard | enterPIN | selectOperation | ejectCard |
|-------|-----------|----------|-----------------|-----------|
| **Idle** | ✅ Transition to CardInserted | ❌ Error | ❌ Error | ❌ Error |
| **CardInserted** | ❌ Error | ✅ Validate & transition | ❌ Error | ✅ Back to Idle |
| **PINVerified** | ❌ Error | ❌ Error | ✅ Start transaction | ✅ Back to Idle |
| **Transaction** | ❌ Error | ❌ Error | ❌ Error | ❌ Error |
| **Blocked** | ❌ Error | ❌ Error | ❌ Error | ✅ Back to Idle |

### 3. **Strategy Layer**

#### GreedyStrategy
```java
public class GreedyStrategy implements CashDispensingStrategy {
    public Map<Integer, Integer> dispenseCash(int amount, 
                                              Map<Integer, Integer> available) {
        // Use largest denominations first
        // Time: O(n) where n = number of denominations
    }
}
```

**Algorithm:**
1. Sort denominations in descending order (2000, 1000, 500, 100)
2. For each denomination:
   - Calculate how many notes needed
   - Take minimum of (needed, available)
   - Reduce remaining amount
3. Return null if exact amount cannot be dispensed

**Pros:**
- ✅ Fast (O(n))
- ✅ Simple to implement
- ✅ Preserves smaller denominations

**Cons:**
- ❌ May not always find solution when one exists
- ❌ May not minimize note count

#### MinimumNotesStrategy
```java
public class MinimumNotesStrategy implements CashDispensingStrategy {
    // Uses backtracking to find minimum notes
    // Time: O(n * amount) with memoization
}
```

**Algorithm:**
1. Try all possible combinations
2. Use backtracking to find valid solutions
3. Return solution with minimum notes

**Pros:**
- ✅ Finds optimal solution (minimum notes)
- ✅ More likely to find solution when available

**Cons:**
- ❌ Slower (exponential without optimization)
- ❌ More complex implementation

### 4. **Service Layer**

#### ATM
```java
public class ATM {
    private String atmId;
    private ATMState currentState;
    private CashDispenser cashDispenser;
    private Card currentCard;
    private Map<String, Account> accounts;
    private int pinAttempts;
    
    // Delegated to current state
    public void insertCard(Card card);
    public void enterPIN(String pin);
    public void ejectCard();
    
    // Transaction operations
    public void withdraw(double amount);
    public void deposit(double amount);
    public void checkBalance();
    public void changePIN(String oldPin, String newPin);
}
```

**Responsibilities:**
- Manage current state
- Delegate operations to current state
- Process transactions
- Track PIN attempts
- Generate receipts
- Manage accounts

---

## State Transitions

### Normal Flow
```
┌──────────┐
│   IDLE   │ ◄──────────────────────────────┐
└────┬─────┘                                 │
     │ insertCard()                          │
     ▼                                       │
┌──────────────┐                             │
│CARD_INSERTED │                             │
└──────┬───────┘                             │
       │ enterPIN(correct)                   │
       ▼                                     │
┌──────────────┐                             │
│PIN_VERIFIED  │ ◄──────────┐                │
└──────┬───────┘            │                │
       │ selectOperation()  │                │
       ▼                    │                │
┌──────────────┐            │                │
│ TRANSACTION  │            │                │
└──────┬───────┘            │                │
       │ complete()         │                │
       └────────────────────┘                │
                                             │
       ejectCard() ──────────────────────────┘
```

### Failed PIN Flow
```
┌──────────────┐
│CARD_INSERTED │
└──────┬───────┘
       │ enterPIN(wrong) - Attempt 1
       │ ↓ (stay in same state)
       │ enterPIN(wrong) - Attempt 2
       │ ↓ (stay in same state)
       │ enterPIN(wrong) - Attempt 3
       ▼
┌──────────────┐
│   BLOCKED    │
└──────┬───────┘
       │ auto ejectCard()
       ▼
┌──────────────┐
│     IDLE     │
└──────────────┘
```

---

## Key Design Decisions

### 1. **Separation of Card and Account**
**Decision:** Card and Account are separate entities.

**Rationale:**
- One account can have multiple cards
- Card contains authentication info (PIN)
- Account contains financial info (balance)
- Better security (card can be blocked without affecting account)

### 2. **State Pattern for ATM States**
**Decision:** Use State Pattern instead of if-else or switch statements.

**Rationale:**
- ✅ Each state encapsulates its own behavior
- ✅ Easy to add new states
- ✅ Prevents invalid operations (compile-time safety)
- ✅ Clear state transitions
- ❌ More classes (5 state classes)

**Alternative:** Enum with switch statements
```java
// NOT USED - Less maintainable
public void insertCard(Card card) {
    switch (currentState) {
        case IDLE:
            // handle
            break;
        case CARD_INSERTED:
            // handle
            break;
        // ... many cases
    }
}
```

### 3. **Strategy Pattern for Cash Dispensing**
**Decision:** Use Strategy Pattern for dispensing algorithms.

**Rationale:**
- ✅ Switch algorithms at runtime
- ✅ Easy to test each strategy independently
- ✅ Can add new strategies without modifying CashDispenser
- ✅ Different ATMs can use different strategies

**Alternative:** Hardcoded greedy algorithm
- ❌ Not extensible
- ❌ Cannot switch at runtime
- ❌ Harder to test

### 4. **PIN Attempts Tracking**
**Decision:** Track PIN attempts in ATM, not Card.

**Rationale:**
- PIN attempts are session-specific
- Different ATMs should have independent attempt counters
- Card blocking is permanent (stored in Card)
- Attempts reset when card is ejected

### 5. **Transaction Receipt Generation**
**Decision:** Generate receipts immediately after transaction.

**Rationale:**
- User gets immediate feedback
- Includes all relevant details (ATM ID, timestamp, balance)
- Can be extended to print physical receipts

### 6. **Denomination Storage**
**Decision:** Use `Map<Integer, Integer>` for denominations.

**Rationale:**
- ✅ Easy to add new denominations
- ✅ Fast lookup O(1)
- ✅ Easy to update counts
- ❌ No ordering (need to sort for greedy)

**Alternative:** Array or List
- ❌ Fixed size or requires searching

---

## Trade-offs

### 1. **State Pattern Complexity**
**Trade-off:** More classes vs cleaner code

| Aspect | State Pattern | If-Else |
|--------|--------------|---------|
| Classes | 5 state classes | 1 class |
| Maintainability | ✅ High | ❌ Low |
| Extensibility | ✅ Easy | ❌ Hard |
| Complexity | Medium | Low |
| Type Safety | ✅ Compile-time | ❌ Runtime |

**Verdict:** State Pattern is worth the extra classes for maintainability.

### 2. **Cash Dispensing Strategy**
**Trade-off:** Speed vs Optimality

| Strategy | Time Complexity | Optimality | Use Case |
|----------|----------------|------------|----------|
| Greedy | O(n) | Not guaranteed | Fast ATMs |
| Minimum Notes | O(n * amount) | ✅ Optimal | When accuracy matters |
| Dynamic Programming | O(n * amount) | ✅ Optimal | Best balance |

**Verdict:** Greedy for most cases, Minimum Notes for special scenarios.

### 3. **PIN Storage**
**Trade-off:** Security vs Simplicity

**Current:** Plain text PIN (for demo)
```java
private String pin = "1234";
```

**Production:** Hashed PIN
```java
private String pinHash = BCrypt.hashpw("1234", BCrypt.gensalt());
public boolean validatePIN(String input) {
    return BCrypt.checkpw(input, pinHash);
}
```

**Verdict:** Use hashing in production, plain text acceptable for demo.

### 4. **Account Storage**
**Trade-off:** In-memory vs Database

**Current:** `Map<String, Account>` in memory
- ✅ Fast
- ✅ Simple for demo
- ❌ Not persistent
- ❌ Not scalable

**Production:** Database with connection pool
- ✅ Persistent
- ✅ Scalable
- ✅ ACID transactions
- ❌ Slower
- ❌ More complex

**Verdict:** In-memory for demo, database for production.

---

## Extensions

### 1. **Add Multi-Currency Support**
```java
public enum Currency {
    USD, EUR, GBP, INR
}

public class CashDispenser {
    private Map<Currency, Map<Integer, Integer>> denominations;
    
    public Map<Integer, Integer> dispenseCash(double amount, Currency currency) {
        // Use currency-specific denominations
    }
}
```

### 2. **Add Transaction Limits**
```java
public class Account {
    private double dailyWithdrawalLimit = 5000;
    private double dailyWithdrawn = 0;
    private LocalDate lastWithdrawalDate;
    
    public boolean withdraw(double amount) {
        if (dailyWithdrawn + amount > dailyWithdrawalLimit) {
            throw new LimitExceededException();
        }
        // ... proceed
    }
}
```

### 3. **Add Network Failure Handling**
```java
public class ATM {
    private boolean isOnline = true;
    
    public void withdraw(double amount) {
        if (!isOnline) {
            // Queue transaction for later
            pendingTransactions.add(new Transaction(...));
            System.out.println("Network down. Transaction queued.");
            return;
        }
        // ... proceed
    }
}
```

### 4. **Add Admin Operations**
```java
public class AdminOperations {
    public void refillCash(ATM atm, Map<Integer, Integer> cash);
    public void unblockCard(Card card);
    public List<Transaction> getTransactionLog(LocalDate date);
    public void markATMOutOfService(ATM atm);
}
```

### 5. **Add Receipt Printing**
```java
public class Receipt {
    private String transactionId;
    private LocalDateTime timestamp;
    private String atmId;
    private String cardNumber;
    private TransactionType type;
    private double amount;
    private double balance;
    
    public void print() {
        // Format and print receipt
    }
    
    public String toJSON() {
        // For digital receipts
    }
}
```

### 6. **Add Fraud Detection**
```java
public class FraudDetector {
    public boolean isSuspicious(Transaction txn, Account account) {
        // Check for unusual patterns
        // - Large withdrawal after small deposits
        // - Multiple failed PINs from different locations
        // - Withdrawal amount >> usual pattern
        return false;
    }
}
```

---

## Performance Analysis

### Time Complexity

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| Insert Card | O(1) | State transition |
| Validate PIN | O(1) | String comparison |
| Withdraw | O(n) | n = denominations |
| Deposit | O(1) | Balance update |
| Check Balance | O(1) | Direct access |
| Change PIN | O(1) | String update |

### Space Complexity

| Component | Space | Notes |
|-----------|-------|-------|
| ATM | O(1) | Fixed attributes |
| Account | O(t) | t = transactions |
| CashDispenser | O(d) | d = denominations |
| Total | O(a * t + d) | a = accounts |

---

## Testing Strategy

### Unit Tests
```java
@Test
public void testPINValidation() {
    Card card = new Card("1234", "ACC-001", "1234");
    assertTrue(card.validatePIN("1234"));
    assertFalse(card.validatePIN("5678"));
}

@Test
public void testCardBlockingAfterThreeAttempts() {
    // Test PIN blocking logic
}

@Test
public void testGreedyStrategy() {
    // Test cash dispensing
}
```

### Integration Tests
```java
@Test
public void testCompleteWithdrawalFlow() {
    // Insert card → Enter PIN → Withdraw → Eject
}

@Test
public void testStateTransitions() {
    // Verify correct state changes
}
```

### Edge Cases
- Withdraw amount > balance
- Withdraw amount > ATM cash
- Amount not multiple of 100
- 3 failed PIN attempts
- Multiple transactions in one session
- Concurrent ATM access (if multi-threaded)

---

## Conclusion

This ATM System demonstrates:
- ✅ **State Pattern** for clean state management
- ✅ **Strategy Pattern** for flexible algorithms
- ✅ **Separation of Concerns** (model, state, strategy, service)
- ✅ **Security** (PIN validation, card blocking)
- ✅ **Extensibility** (easy to add states, strategies, operations)
- ✅ **Real-world Features** (receipts, denominations, limits)

**Interview Performance:**
- ⏱️ Can be completed in 45-60 minutes
- 💡 Shows strong OOP and design pattern knowledge
- 🎯 Covers common interview topics (state machines, strategy)
- 🚀 Easy to extend with additional features

---

**Total Implementation:**
- 17 Java files
- ~1,000 lines of code
- 2 design patterns
- 10 comprehensive scenarios
- Production-ready structure



