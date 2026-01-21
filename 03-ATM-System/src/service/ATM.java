package service;

import model.*;
import state.*;
import enums.TransactionType;
import enums.TransactionStatus;
import java.util.HashMap;
import java.util.Map;

public class ATM {
    private String atmId;
    private String location;
    private ATMState currentState;
    private CashDispenser cashDispenser;
    private Card currentCard;
    private Map<String, Account> accounts; // accountNumber -> Account
    private int pinAttempts;
    private static final int MAX_PIN_ATTEMPTS = 3;

    public ATM(String atmId, String location, CashDispenser cashDispenser) {
        this.atmId = atmId;
        this.location = location;
        this.cashDispenser = cashDispenser;
        this.currentState = new IdleState(this);
        this.accounts = new HashMap<>();
        this.pinAttempts = 0;
    }

    // State management
    public void setState(ATMState state) {
        this.currentState = state;
    }

    public ATMState getState() {
        return currentState;
    }

    // Card operations
    public void insertCard(Card card) {
        currentState.insertCard(card);
    }

    public void enterPIN(String pin) {
        currentState.enterPIN(pin);
    }

    public void ejectCard() {
        currentState.ejectCard();
    }

    // Account management
    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    private Account getCurrentAccount() {
        if (currentCard == null) {
            return null;
        }
        return accounts.get(currentCard.getAccountNumber());
    }

    // PIN attempts management
    public void incrementPinAttempts() {
        pinAttempts++;
    }

    public void resetPinAttempts() {
        pinAttempts = 0;
    }

    public int getRemainingPinAttempts() {
        return MAX_PIN_ATTEMPTS - pinAttempts;
    }

    // Transaction operations
    public void withdraw(double amount) {
        if (!(currentState instanceof TransactionState)) {
            System.out.println("❌ Invalid state for withdrawal.");
            return;
        }

        Account account = getCurrentAccount();
        if (account == null) {
            System.out.println("❌ Account not found.");
            ((TransactionState) currentState).completeTransaction();
            return;
        }

        // Validate amount
        if (amount <= 0) {
            System.out.println("❌ Invalid amount. Amount must be positive.");
            ((TransactionState) currentState).completeTransaction();
            return;
        }

        if (amount % 100 != 0) {
            System.out.println("❌ Amount must be in multiples of 100.");
            ((TransactionState) currentState).completeTransaction();
            return;
        }

        // Check account balance
        if (account.getBalance() < amount) {
            System.out.println("❌ Insufficient balance. Available: $" + account.getBalance());
            account.addTransaction(new Transaction(TransactionType.WITHDRAWAL, amount, TransactionStatus.FAILED));
            ((TransactionState) currentState).completeTransaction();
            return;
        }

        // Check ATM cash availability
        if (!cashDispenser.canDispense(amount)) {
            System.out.println("❌ ATM has insufficient cash or cannot dispense exact amount.");
            System.out.println("   Please try a different amount.");
            account.addTransaction(new Transaction(TransactionType.WITHDRAWAL, amount, TransactionStatus.FAILED));
            ((TransactionState) currentState).completeTransaction();
            return;
        }

        // Dispense cash
        Map<Integer, Integer> dispensed = cashDispenser.dispenseCash(amount);
        if (dispensed == null) {
            System.out.println("❌ Unable to dispense cash. Please try again.");
            account.addTransaction(new Transaction(TransactionType.WITHDRAWAL, amount, TransactionStatus.FAILED));
            ((TransactionState) currentState).completeTransaction();
            return;
        }

        // Deduct from account
        account.withdraw(amount);

        // Print receipt
        System.out.println("\n========================================");
        System.out.println("         WITHDRAWAL RECEIPT");
        System.out.println("========================================");
        System.out.println("ATM ID: " + atmId);
        System.out.println("Location: " + location);
        System.out.println("Card: " + currentCard.maskCardNumber());
        System.out.println("Account: " + account.getAccountNumber());
        System.out.println("----------------------------------------");
        System.out.println("Amount Withdrawn: $" + amount);
        System.out.println("\nCash Dispensed:");
        for (Map.Entry<Integer, Integer> entry : dispensed.entrySet()) {
            System.out.println("  $" + entry.getKey() + " x " + entry.getValue() + " = $" + (entry.getKey() * entry.getValue()));
        }
        System.out.println("----------------------------------------");
        System.out.println("Remaining Balance: $" + account.getBalance());
        System.out.println("========================================\n");

        ((TransactionState) currentState).completeTransaction();
    }

    public void deposit(double amount) {
        if (!(currentState instanceof TransactionState)) {
            System.out.println("❌ Invalid state for deposit.");
            return;
        }

        Account account = getCurrentAccount();
        if (account == null) {
            System.out.println("❌ Account not found.");
            ((TransactionState) currentState).completeTransaction();
            return;
        }

        if (amount <= 0) {
            System.out.println("❌ Invalid amount. Amount must be positive.");
            ((TransactionState) currentState).completeTransaction();
            return;
        }

        account.deposit(amount);

        // Print receipt
        System.out.println("\n========================================");
        System.out.println("          DEPOSIT RECEIPT");
        System.out.println("========================================");
        System.out.println("ATM ID: " + atmId);
        System.out.println("Location: " + location);
        System.out.println("Card: " + currentCard.maskCardNumber());
        System.out.println("Account: " + account.getAccountNumber());
        System.out.println("----------------------------------------");
        System.out.println("Amount Deposited: $" + amount);
        System.out.println("New Balance: $" + account.getBalance());
        System.out.println("========================================\n");

        ((TransactionState) currentState).completeTransaction();
    }

    public void checkBalance() {
        if (!(currentState instanceof TransactionState)) {
            System.out.println("❌ Invalid state for balance inquiry.");
            return;
        }

        Account account = getCurrentAccount();
        if (account == null) {
            System.out.println("❌ Account not found.");
            ((TransactionState) currentState).completeTransaction();
            return;
        }

        account.addTransaction(new Transaction(TransactionType.BALANCE_INQUIRY, 0, TransactionStatus.SUCCESS));

        System.out.println("\n========================================");
        System.out.println("        BALANCE INQUIRY");
        System.out.println("========================================");
        System.out.println("Account: " + account.getAccountNumber());
        System.out.println("Account Holder: " + account.getAccountHolderName());
        System.out.println("----------------------------------------");
        System.out.println("Current Balance: $" + account.getBalance());
        System.out.println("========================================\n");

        ((TransactionState) currentState).completeTransaction();
    }

    public void changePIN(String oldPin, String newPin) {
        if (!(currentState instanceof TransactionState)) {
            System.out.println("❌ Invalid state for PIN change.");
            return;
        }

        if (currentCard == null) {
            System.out.println("❌ No card inserted.");
            ((TransactionState) currentState).completeTransaction();
            return;
        }

        try {
            currentCard.changePIN(oldPin, newPin);
            Account account = getCurrentAccount();
            if (account != null) {
                account.addTransaction(new Transaction(TransactionType.PIN_CHANGE, 0, TransactionStatus.SUCCESS));
            }
            System.out.println("✓ PIN changed successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
            Account account = getCurrentAccount();
            if (account != null) {
                account.addTransaction(new Transaction(TransactionType.PIN_CHANGE, 0, TransactionStatus.FAILED));
            }
        }

        ((TransactionState) currentState).completeTransaction();
    }

    public void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("           ATM MAIN MENU");
        System.out.println("========================================");
        System.out.println("1. Withdraw Cash");
        System.out.println("2. Deposit Cash");
        System.out.println("3. Check Balance");
        System.out.println("4. Change PIN");
        System.out.println("5. Eject Card");
        System.out.println("========================================\n");
    }

    // Getters and setters
    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card card) {
        this.currentCard = card;
    }

    public CashDispenser getCashDispenser() {
        return cashDispenser;
    }

    public String getAtmId() {
        return atmId;
    }

    public String getLocation() {
        return location;
    }
}


