package factory;

import model.*;
import enums.*;
import strategy.*;
import java.util.List;

public class ExpenseFactory {
    private static int expenseCounter = 1;
    
    public static Expense createExpense(String description, double amount, User paidBy,
                                       List<Split> splits, SplitType splitType, ExpenseCategory category) {
        
        // Get appropriate split strategy
        SplitStrategy strategy = getSplitStrategy(splitType);
        
        // Calculate splits using strategy
        strategy.calculateSplits(amount, splits);
        
        // Create expense
        String expenseId = "EXP-" + String.format("%04d", expenseCounter++);
        Expense expense = new Expense(expenseId, description, amount, paidBy, splits, splitType, category);
        
        // Validate expense
        if (!expense.validate()) {
            throw new IllegalArgumentException("Invalid expense: splits don't match total amount");
        }
        
        return expense;
    }
    
    private static SplitStrategy getSplitStrategy(SplitType splitType) {
        switch (splitType) {
            case EQUAL:
                return new EqualSplitStrategy();
            case EXACT:
                return new ExactSplitStrategy();
            case PERCENT:
                return new PercentSplitStrategy();
            case SHARE:
                return new ShareSplitStrategy();
            default:
                throw new IllegalArgumentException("Unknown split type: " + splitType);
        }
    }
    
    public static void resetCounter() {
        expenseCounter = 1;
    }
}
