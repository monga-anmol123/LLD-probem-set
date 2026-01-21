# Problem 08: Banking System

## 🎯 Difficulty: Easy ⭐⭐

## 📝 Problem Statement

Design a banking system that supports multiple account types (Savings, Checking, Fixed Deposit) with different interest calculation strategies and transaction processing capabilities.

---

## 🔍 Functional Requirements (FR)

### FR1: Account Management
- Support multiple account types: Savings, Checking, Fixed Deposit
- Each account has: account number, account holder name, balance, account type
- Account creation with initial deposit (minimum balance requirements)

### FR2: Transaction Operations
- **Deposit:** Add money to account
- **Withdraw:** Remove money from account (with balance validation)
- **Transfer:** Move money between accounts
- **Check Balance:** View current balance

### FR3: Interest Calculation
- Different interest calculation strategies:
  - Savings Account: 4% annual interest, calculated monthly
  - Checking Account: No interest
  - Fixed Deposit: 7% annual interest, calculated at maturity
- Apply interest based on account type

### FR4: Account Rules
- **Savings Account:**
  - Minimum balance: $500
  - Withdrawal limit: $5,000 per transaction
  - Interest: 4% per annum
- **Checking Account:**
  - Minimum balance: $100
  - No withdrawal limit
  - No interest
  - Overdraft protection (optional)
- **Fixed Deposit Account:**
  - Minimum balance: $10,000
  - No withdrawals allowed before maturity
  - Interest: 7% per annum
  - Lock-in period: 1 year

### FR5: Transaction History
- Maintain transaction history for each account
- Track: transaction ID, type, amount, timestamp, balance after transaction

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Scalability
- System should handle 10,000+ accounts
- Support 1,000+ concurrent transactions
- Efficient balance lookups

### NFR2: Extensibility
- Easy to add new account types (e.g., Business Account, Student Account)
- Easy to add new interest calculation strategies
- Easy to add new transaction types (e.g., Bill Payment, Loan)

### NFR3: Security
- Validate all transactions
- Prevent negative balances (except overdraft)
- Ensure thread-safe operations

### NFR4: Maintainability
- Clean code with clear separation of concerns
- Well-documented classes and methods
- Easy to understand and modify

### NFR5: Reliability
- Handle edge cases:
  - Insufficient balance
  - Invalid account numbers
  - Minimum balance violations
  - Transfer to same account
  - Withdrawal from fixed deposit before maturity

---

## 🎨 Design Patterns to Use

### 1. **Factory Pattern**
- **Where:** Account creation
- **Why:** Centralize object creation, easy to add new account types

### 2. **Strategy Pattern**
- **Where:** Interest calculation strategies
- **Why:** Different algorithms for different account types, switch at runtime

### 3. **Singleton Pattern** (Optional)
- **Where:** Bank instance
- **Why:** Only one bank should exist, global access point

---

## 📋 Core Entities

### 1. **Account** (Abstract)
- Attributes: `accountNumber`, `accountHolderName`, `balance`, `accountType`, `createdDate`
- Methods: `deposit()`, `withdraw()`, `getBalance()`, `canWithdraw()`
- Subclasses: `SavingsAccount`, `CheckingAccount`, `FixedDepositAccount`

### 2. **Transaction**
- Attributes: `transactionId`, `accountNumber`, `type`, `amount`, `timestamp`, `balanceAfter`
- Methods: `getDetails()`

### 3. **Bank** (Singleton)
- Attributes: `name`, `accounts`, `transactions`, `interestStrategy`
- Methods: `createAccount()`, `deposit()`, `withdraw()`, `transfer()`, `applyInterest()`

### 4. **InterestStrategy** (Interface)
- Methods: `calculateInterest(Account account)`
- Implementations: `SavingsInterestStrategy`, `NoInterestStrategy`, `FixedDepositInterestStrategy`

---

## 🧪 Test Scenarios

### Scenario 1: Basic Account Operations
```
1. Create Savings Account with $1,000
2. Deposit $500 → Balance: $1,500
3. Withdraw $300 → Balance: $1,200
4. Check balance
5. View transaction history
```

### Scenario 2: Transfer Between Accounts
```
1. Create two accounts: Account A ($2,000), Account B ($1,000)
2. Transfer $500 from A to B
3. Verify: A = $1,500, B = $1,500
4. Check transaction history for both accounts
```

### Scenario 3: Interest Calculation
```
1. Create Savings Account with $10,000
2. Apply monthly interest (4% annual = 0.33% monthly)
3. Verify balance increased by $33.33
4. Create Checking Account with $5,000
5. Apply interest → No change (checking has no interest)
```

### Scenario 4: Account Rules Validation
```
1. Create Savings Account with $1,000
2. Try to withdraw $6,000 → Should fail (exceeds limit)
3. Try to withdraw $900 → Should fail (violates minimum balance)
4. Create Fixed Deposit with $15,000
5. Try to withdraw before maturity → Should fail
```

### Scenario 5: Edge Cases
```
1. Try to create account with insufficient initial deposit
2. Try to withdraw from account with insufficient balance
3. Try to transfer to same account
4. Try to transfer negative amount
5. Try to access non-existent account
```

---

## ⏱️ Time Allocation (45 minutes)

- **5 mins:** Clarify requirements, ask questions
- **5 mins:** List entities and relationships
- **5 mins:** Identify design patterns
- **25 mins:** Write code (enums → model → strategy → factory → service → main)
- **5 mins:** Test with demo, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: Account Creation with Factory</summary>

Use Factory Pattern:
```java
public class AccountFactory {
    public static Account createAccount(AccountType type, String accountNumber, 
                                       String holderName, double initialDeposit) {
        switch (type) {
            case SAVINGS:
                return new SavingsAccount(accountNumber, holderName, initialDeposit);
            case CHECKING:
                return new CheckingAccount(accountNumber, holderName, initialDeposit);
            case FIXED_DEPOSIT:
                return new FixedDepositAccount(accountNumber, holderName, initialDeposit);
        }
    }
}
```
</details>

<details>
<summary>Hint 2: Interest Calculation Strategy</summary>

```java
public interface InterestStrategy {
    double calculateInterest(Account account);
}

public class SavingsInterestStrategy implements InterestStrategy {
    private static final double ANNUAL_RATE = 0.04; // 4%
    
    public double calculateInterest(Account account) {
        return account.getBalance() * (ANNUAL_RATE / 12); // Monthly
    }
}
```
</details>

<details>
<summary>Hint 3: Withdrawal Validation</summary>

In each account type:
```java
@Override
public boolean canWithdraw(double amount) {
    // Savings: Check minimum balance and withdrawal limit
    if (amount > 5000) return false;
    if (balance - amount < 500) return false;
    return true;
}
```
</details>

<details>
<summary>Hint 4: Transfer Operation</summary>

```java
public void transfer(String fromAccount, String toAccount, double amount) {
    Account from = accounts.get(fromAccount);
    Account to = accounts.get(toAccount);
    
    if (from.canWithdraw(amount)) {
        from.withdraw(amount);
        to.deposit(amount);
        // Record transactions
    }
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Add Overdraft Protection**
   - Allow checking accounts to go negative up to a limit
   - Charge overdraft fees

2. **Add Loan Accounts**
   - Track principal, interest, EMI
   - Payment schedule

3. **Add Credit Card Accounts**
   - Credit limit
   - Billing cycle
   - Minimum payment

4. **Add Account Statements**
   - Generate monthly statements
   - PDF export

5. **Add Multi-Currency Support**
   - Different currencies per account
   - Exchange rate handling

6. **Add Beneficiary Management**
   - Add/remove beneficiaries
   - Quick transfer to beneficiaries

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Factory and Strategy patterns correctly
- [ ] Handle all test scenarios
- [ ] Have clear separation: model, service, strategy layers
- [ ] Validate all transactions properly
- [ ] Calculate interest correctly for each account type
- [ ] Maintain transaction history
- [ ] Handle edge cases (insufficient balance, invalid accounts, etc.)
- [ ] Be extensible (easy to add new account types)

---

## 📁 File Structure

```
08-Banking-System/
├── README.md (this file)
├── src/
│   ├── enums/
│   │   ├── AccountType.java
│   │   └── TransactionType.java
│   ├── model/
│   │   ├── Account.java
│   │   ├── SavingsAccount.java
│   │   ├── CheckingAccount.java
│   │   ├── FixedDepositAccount.java
│   │   └── Transaction.java
│   ├── factory/
│   │   └── AccountFactory.java
│   ├── strategy/
│   │   ├── InterestStrategy.java
│   │   ├── SavingsInterestStrategy.java
│   │   ├── NoInterestStrategy.java
│   │   └── FixedDepositInterestStrategy.java
│   ├── service/
│   │   └── Bank.java
│   └── Main.java
└── SOLUTION.md
```

---

**Good luck! Start coding! 🚀**


