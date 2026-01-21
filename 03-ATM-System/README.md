# Problem 03: ATM System

## 🎯 Difficulty: Easy ⭐⭐

## 📝 Problem Statement

Design an ATM (Automated Teller Machine) system that supports multiple operations like cash withdrawal, deposit, balance inquiry, and PIN change. The ATM should handle different states (Idle, Card Inserted, PIN Verified, etc.) and support multiple transaction types with proper validation.

---

## 🔍 Functional Requirements (FR)

### FR1: Card Management
- Insert card (validate card number)
- Eject card
- Read card details (card number, account number)

### FR2: Authentication
- Enter PIN (4-digit)
- Validate PIN against account
- Allow 3 attempts before blocking card
- Support PIN change

### FR3: Account Operations
- **Check Balance:** Display current account balance
- **Withdraw Cash:** Withdraw amount (validate sufficient balance and ATM cash)
- **Deposit Cash:** Deposit amount into account
- **Transfer Money:** Transfer to another account (basic support)

### FR4: Cash Management
- Track available cash in ATM
- Support different denominations (100, 500, 1000, 2000)
- Dispense cash using optimal denomination strategy
- Alert when cash is low

### FR5: Transaction Management
- Generate transaction receipt
- Record transaction history
- Transaction types: Withdrawal, Deposit, Balance Inquiry, PIN Change

### FR6: State Management
- **Idle State:** Waiting for card
- **Card Inserted State:** Card inserted, waiting for PIN
- **PIN Verified State:** PIN correct, ready for transaction
- **Transaction State:** Processing transaction
- **Blocked State:** Card blocked after 3 failed PIN attempts

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Security
- Encrypt PIN storage
- Mask PIN input (show ****)
- Auto-eject card after 2 minutes of inactivity
- Block card after 3 failed PIN attempts

### NFR2: Reliability
- Handle edge cases:
  - Insufficient balance
  - Insufficient ATM cash
  - Invalid card
  - Network failure (simulate)
  - Power failure (transaction rollback)

### NFR3: Extensibility
- Easy to add new transaction types
- Support different cash dispensing strategies
- Add new ATM states

### NFR4: Performance
- Transaction processing < 5 seconds
- Balance inquiry < 2 seconds

### NFR5: Usability
- Clear menu options
- Informative error messages
- Transaction receipts with all details

---

## 🎨 Design Patterns to Use

### 1. **State Pattern**
- **Where:** ATM state management (Idle, CardInserted, PINVerified, Transaction, Blocked)
- **Why:** Clean state transitions, behavior changes based on state

### 2. **Strategy Pattern**
- **Where:** Cash dispensing strategies (Greedy, Minimum Notes, Specific Denominations)
- **Why:** Different algorithms for dispensing cash

### 3. **Singleton Pattern** (Optional)
- **Where:** ATM instance
- **Why:** Single ATM machine, global access

---

## 📋 Core Entities

### 1. **Card**
- Attributes: `cardNumber`, `accountNumber`, `pin`, `isBlocked`
- Methods: `validatePIN()`, `block()`, `unblock()`

### 2. **Account**
- Attributes: `accountNumber`, `accountHolderName`, `balance`, `transactions`
- Methods: `withdraw()`, `deposit()`, `getBalance()`, `addTransaction()`

### 3. **ATMState** (Interface)
- Methods: `insertCard()`, `enterPIN()`, `selectOperation()`, `ejectCard()`
- Implementations: `IdleState`, `CardInsertedState`, `PINVerifiedState`, `TransactionState`, `BlockedState`

### 4. **Transaction**
- Attributes: `transactionId`, `type`, `amount`, `timestamp`, `status`
- Types: Withdrawal, Deposit, BalanceInquiry, PINChange, Transfer

### 5. **CashDispenser**
- Attributes: `availableCash`, `denominations` (Map<Integer, Integer>)
- Methods: `dispenseCash()`, `canDispense()`, `addCash()`

### 6. **CashDispensingStrategy** (Interface)
- Methods: `dispenseCash(amount, availableDenominations)`
- Implementations: `GreedyStrategy`, `MinimumNotesStrategy`

### 7. **ATM**
- Attributes: `atmId`, `location`, `currentState`, `cashDispenser`, `currentCard`, `pinAttempts`
- Methods: `insertCard()`, `enterPIN()`, `withdraw()`, `deposit()`, `checkBalance()`, `changePIN()`

---

## 🧪 Test Scenarios

### Scenario 1: Successful Withdrawal
```
1. Insert card
2. Enter correct PIN
3. Select "Withdraw"
4. Enter amount: $500
5. Verify sufficient balance
6. Dispense cash
7. Print receipt
8. Eject card
```

### Scenario 2: Failed PIN Attempts
```
1. Insert card
2. Enter wrong PIN (Attempt 1) → "Incorrect PIN, 2 attempts remaining"
3. Enter wrong PIN (Attempt 2) → "Incorrect PIN, 1 attempt remaining"
4. Enter wrong PIN (Attempt 3) → "Card blocked. Contact bank."
5. Card ejected and blocked
```

### Scenario 3: Insufficient Balance
```
1. Insert card (Balance: $1000)
2. Enter correct PIN
3. Select "Withdraw"
4. Enter amount: $1500
5. Error: "Insufficient balance"
6. Return to menu
```

### Scenario 4: Insufficient ATM Cash
```
1. ATM has only $2000 remaining
2. User tries to withdraw $3000
3. Error: "ATM has insufficient cash. Please try a lower amount."
```

### Scenario 5: Deposit Cash
```
1. Insert card
2. Enter correct PIN
3. Select "Deposit"
4. Enter amount: $2000
5. Confirm deposit
6. Update balance
7. Print receipt
```

### Scenario 6: Cash Dispensing Strategy
```
1. Withdraw $2700
2. ATM has: 100x10, 500x5, 1000x3, 2000x2
3. Greedy Strategy: 2000x1 + 500x1 + 100x2 = 2700
4. Dispense: "Please collect: 1x2000, 1x500, 2x100"
```

### Scenario 7: Balance Inquiry
```
1. Insert card
2. Enter correct PIN
3. Select "Balance Inquiry"
4. Display: "Your current balance is: $5000"
5. Return to menu or eject card
```

---

## ⏱️ Time Allocation (45 minutes)

- **5 mins:** Clarify requirements, identify states and transitions
- **5 mins:** List entities (Card, Account, ATM, Transaction, CashDispenser)
- **5 mins:** Identify design patterns (State, Strategy)
- **25 mins:** Code (enums → model → state → strategy → service → main)
- **5 mins:** Test with demo scenarios

---

## 💡 Hints

<details>
<summary>Hint 1: State Pattern Structure</summary>

```java
public interface ATMState {
    void insertCard(ATM atm, Card card);
    void enterPIN(ATM atm, String pin);
    void selectOperation(ATM atm, String operation);
    void ejectCard(ATM atm);
}

public class IdleState implements ATMState {
    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("Card inserted. Please enter PIN.");
        atm.setCurrentCard(card);
        atm.setState(new CardInsertedState());
    }
    
    @Override
    public void enterPIN(ATM atm, String pin) {
        System.out.println("Please insert card first.");
    }
}
```

</details>

<details>
<summary>Hint 2: State Transitions</summary>

```
Idle State
   ↓ (insertCard)
Card Inserted State
   ↓ (enterPIN - correct)
PIN Verified State
   ↓ (selectOperation)
Transaction State
   ↓ (transaction complete)
PIN Verified State (can do more transactions)
   ↓ (ejectCard)
Idle State

Alternative flow:
Card Inserted State
   ↓ (enterPIN - wrong 3 times)
Blocked State
   ↓ (auto eject)
Idle State
```

</details>

<details>
<summary>Hint 3: Cash Dispensing Strategy</summary>

```java
public interface CashDispensingStrategy {
    Map<Integer, Integer> dispenseCash(int amount, Map<Integer, Integer> availableDenominations);
}

public class GreedyStrategy implements CashDispensingStrategy {
    @Override
    public Map<Integer, Integer> dispenseCash(int amount, Map<Integer, Integer> available) {
        Map<Integer, Integer> dispensed = new HashMap<>();
        int[] denominations = {2000, 1000, 500, 100};
        
        for (int denom : denominations) {
            if (amount >= denom && available.get(denom) > 0) {
                int count = Math.min(amount / denom, available.get(denom));
                dispensed.put(denom, count);
                amount -= count * denom;
            }
        }
        
        return amount == 0 ? dispensed : null; // null if can't dispense exact amount
    }
}
```

</details>

<details>
<summary>Hint 4: PIN Validation with Attempts</summary>

```java
public class ATM {
    private int pinAttempts = 0;
    private static final int MAX_PIN_ATTEMPTS = 3;
    
    public void enterPIN(String pin) {
        if (currentCard.validatePIN(pin)) {
            pinAttempts = 0;
            setState(new PINVerifiedState());
        } else {
            pinAttempts++;
            if (pinAttempts >= MAX_PIN_ATTEMPTS) {
                currentCard.block();
                System.out.println("Card blocked. Contact bank.");
                setState(new BlockedState());
            } else {
                System.out.println("Incorrect PIN. " + (MAX_PIN_ATTEMPTS - pinAttempts) + " attempts remaining.");
            }
        }
    }
}
```

</details>

---

## 🚀 Extensions (If Time Permits)

1. **Add Mini Statement**
   - Show last 5 transactions
   - Transaction details: date, type, amount, balance

2. **Add Multi-Language Support**
   - English, Hindi, Spanish
   - Strategy pattern for language selection

3. **Add Receipt Printing**
   ```java
   public class Receipt {
       private String transactionId;
       private String atmId;
       private LocalDateTime timestamp;
       private String transactionType;
       private double amount;
       private double balance;
   }
   ```

4. **Add Network Failure Handling**
   - Simulate network down
   - Queue transactions
   - Retry mechanism

5. **Add Admin Operations**
   - Refill cash
   - View transaction logs
   - Unblock cards

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use State and Strategy patterns correctly
- [ ] Handle all test scenarios
- [ ] Proper state transitions (Idle → CardInserted → PINVerified → Transaction)
- [ ] Block card after 3 failed PIN attempts
- [ ] Validate sufficient balance and ATM cash
- [ ] Dispense cash using optimal denominations
- [ ] Generate transaction receipts
- [ ] Handle edge cases gracefully

---

## 📁 File Structure

```
03-ATM-System/
├── README.md (this file)
├── SOLUTION.md
├── COMPILATION-GUIDE.md
└── src/
    ├── enums/
    │   ├── ATMStateType.java
    │   ├── TransactionType.java
    │   └── TransactionStatus.java
    ├── model/
    │   ├── Card.java
    │   ├── Account.java
    │   ├── Transaction.java
    │   └── CashDispenser.java
    ├── state/
    │   ├── ATMState.java
    │   ├── IdleState.java
    │   ├── CardInsertedState.java
    │   ├── PINVerifiedState.java
    │   ├── TransactionState.java
    │   └── BlockedState.java
    ├── strategy/
    │   ├── CashDispensingStrategy.java
    │   ├── GreedyStrategy.java
    │   └── MinimumNotesStrategy.java
    ├── service/
    │   └── ATM.java
    └── Main.java
```

---

**Good luck! Focus on State Pattern for ATM states and Strategy Pattern for cash dispensing! 🚀**


