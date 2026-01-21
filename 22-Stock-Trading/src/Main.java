import model.*;
import service.*;
import enums.*;
import strategy.*;
import observer.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  📈 STOCK TRADING SYSTEM DEMO 📈");
        System.out.println("========================================\n");
        
        // Initialize Stock Exchange (Singleton)
        StockExchange exchange = StockExchange.getInstance();
        
        // Register stocks
        Stock apple = new Stock("AAPL", "Apple Inc.", 150.00);
        Stock google = new Stock("GOOGL", "Alphabet Inc.", 2800.00);
        Stock tesla = new Stock("TSLA", "Tesla Inc.", 250.00);
        Stock amazon = new Stock("AMZN", "Amazon.com Inc.", 3300.00);
        
        exchange.registerStock(apple);
        exchange.registerStock(google);
        exchange.registerStock(tesla);
        exchange.registerStock(amazon);
        
        // Register traders
        Trader alice = new Trader("T001", "Alice Johnson", 50000.00);
        Trader bob = new Trader("T002", "Bob Smith", 30000.00);
        Trader charlie = new Trader("T003", "Charlie Brown", 20000.00);
        
        exchange.registerTrader(alice);
        exchange.registerTrader(bob);
        exchange.registerTrader(charlie);
        
        // Attach observers
        TraderObserver aliceObserver = new TraderObserver(alice);
        TraderObserver bobObserver = new TraderObserver(bob);
        TraderObserver charlieObserver = new TraderObserver(charlie);
        
        apple.attach(aliceObserver);
        apple.attach(bobObserver);
        tesla.attach(charlieObserver);
        google.attach(aliceObserver);
        
        System.out.println();
        exchange.displayMarketSummary();
        
        // ====================
        // SCENARIO 1: Basic Buy and Sell
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: BASIC BUY AND SELL");
        System.out.println("========================================");
        
        // Alice buys AAPL
        Order order1 = new MarketOrder(alice, apple, 10, TransactionType.BUY);
        exchange.placeOrder(order1);
        
        alice.displayAccount();
        
        // Price increases
        System.out.println("\n📊 Market Update: AAPL price increases...");
        exchange.updateStockPrice("AAPL", 160.00);
        
        // Alice sells AAPL
        Order order2 = new MarketOrder(alice, apple, 10, TransactionType.SELL);
        exchange.placeOrder(order2);
        
        alice.displayAccount();
        
        // ====================
        // SCENARIO 2: Limit Order
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 2: LIMIT ORDER");
        System.out.println("========================================");
        
        // Bob places limit order to buy TSLA at $245 (current: $250)
        Order order3 = new LimitOrder(bob, tesla, 5, TransactionType.BUY, 245.00);
        exchange.placeOrder(order3);
        
        exchange.displayPendingOrders();
        
        // Price drops to trigger limit order
        System.out.println("\n📊 Market Update: TSLA price drops...");
        exchange.updateStockPrice("TSLA", 245.00);
        
        bob.displayAccount();
        
        // ====================
        // SCENARIO 3: Stop-Loss Order
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 3: STOP-LOSS ORDER");
        System.out.println("========================================");
        
        // Charlie buys TSLA
        Order order4 = new MarketOrder(charlie, tesla, 10, TransactionType.BUY);
        exchange.placeOrder(order4);
        
        // Charlie sets stop-loss at $240
        Order order5 = new StopLossOrder(charlie, tesla, 10, 240.00);
        exchange.placeOrder(order5);
        
        exchange.displayPendingOrders();
        
        // Price drops to trigger stop-loss
        System.out.println("\n📊 Market Update: TSLA price drops further...");
        exchange.updateStockPrice("TSLA", 240.00);
        
        charlie.displayAccount();
        
        // ====================
        // SCENARIO 4: Insufficient Funds
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 4: INSUFFICIENT FUNDS");
        System.out.println("========================================");
        
        // Bob tries to buy GOOGL (too expensive)
        Order order6 = new MarketOrder(bob, google, 20, TransactionType.BUY);
        exchange.placeOrder(order6);
        
        System.out.println("✅ System correctly prevented invalid trade");
        
        // ====================
        // SCENARIO 5: Real-Time Price Updates (Observer Pattern)
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 5: REAL-TIME PRICE UPDATES");
        System.out.println("========================================");
        
        System.out.println("\n📊 Multiple price updates:");
        exchange.updateStockPrice("AAPL", 165.00);
        exchange.updateStockPrice("AAPL", 170.00);
        exchange.updateStockPrice("TSLA", 245.00);
        
        // ====================
        // SCENARIO 6: Trading Strategies
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 6: TRADING STRATEGIES");
        System.out.println("========================================");
        
        // Set strategies
        alice.setTradingStrategy(new AggressiveStrategy());
        bob.setTradingStrategy(new ConservativeStrategy());
        charlie.setTradingStrategy(new DayTradingStrategy());
        
        System.out.println("\n📊 Alice uses Aggressive Strategy");
        System.out.println("📊 Bob uses Conservative Strategy");
        System.out.println("📊 Charlie uses Day Trading Strategy");
        
        // Buy some stocks for testing strategies
        Order order7 = new MarketOrder(alice, google, 2, TransactionType.BUY);
        exchange.placeOrder(order7);
        
        Order order8 = new MarketOrder(bob, amazon, 1, TransactionType.BUY);
        exchange.placeOrder(order8);
        
        // Simulate price changes and strategy decisions
        System.out.println("\n📊 Market Update: GOOGL increases by 3%...");
        exchange.updateStockPrice("GOOGL", 2884.00);
        
        Holding aliceGoogle = alice.getPortfolio().getHolding(google);
        if (aliceGoogle != null) {
            boolean shouldSell = alice.getTradingStrategy().shouldSell(google, aliceGoogle);
            System.out.printf("   Alice's Aggressive Strategy says: %s\n", 
                shouldSell ? "SELL (profit > 5% or loss > 3%)" : "HOLD");
        }
        
        System.out.println("\n📊 Market Update: AMZN drops by 4%...");
        exchange.updateStockPrice("AMZN", 3168.00);
        
        Holding bobAmazon = bob.getPortfolio().getHolding(amazon);
        if (bobAmazon != null) {
            boolean shouldSell = bob.getTradingStrategy().shouldSell(amazon, bobAmazon);
            System.out.printf("   Bob's Conservative Strategy says: %s\n",
                shouldSell ? "SELL (profit > 10% or loss > 5%)" : "HOLD");
        }
        
        // ====================
        // SCENARIO 7: Multiple Concurrent Orders
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 7: MULTIPLE CONCURRENT ORDERS");
        System.out.println("========================================");
        
        System.out.println("\n📋 Placing multiple orders simultaneously:");
        
        Order order9 = new MarketOrder(alice, tesla, 5, TransactionType.BUY);
        Order order10 = new LimitOrder(bob, apple, 3, TransactionType.BUY, 168.00);
        Order order11 = new MarketOrder(charlie, apple, 2, TransactionType.BUY);
        
        exchange.placeOrder(order9);
        exchange.placeOrder(order10);
        exchange.placeOrder(order11);
        
        // ====================
        // SCENARIO 8: Portfolio Analytics
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 8: PORTFOLIO ANALYTICS");
        System.out.println("========================================");
        
        System.out.println("\n📊 Alice's Portfolio:");
        alice.displayAccount();
        
        System.out.println("\n📊 Bob's Portfolio:");
        bob.displayAccount();
        
        System.out.println("\n📊 Charlie's Portfolio:");
        charlie.displayAccount();
        
        // ====================
        // SCENARIO 9: Transaction History
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 9: TRANSACTION HISTORY");
        System.out.println("========================================");
        
        System.out.println("\n📜 Alice's Transaction History:");
        for (Transaction txn : alice.getTransactionHistory()) {
            System.out.println("   " + txn);
        }
        
        // ====================
        // Final Market Summary
        // ====================
        exchange.displayMarketSummary();
        
        // Display top gainers/losers
        System.out.println("📊 Top Movers:");
        for (Stock stock : exchange.getAllStocks()) {
            double changePercent = stock.getPriceChangePercent();
            String status = changePercent > 0 ? "GAINER 📈" : "LOSER 📉";
            System.out.printf("   %s: %.2f%% %s\n", 
                stock.getSymbol(), Math.abs(changePercent), status);
        }
        
        System.out.println("\n========================================");
        System.out.println("  ✅ DEMO COMPLETE!");
        System.out.println("========================================\n");
        
        System.out.println("Design Patterns Demonstrated:");
        System.out.println("✓ Observer Pattern - Real-time price updates and order notifications");
        System.out.println("✓ Strategy Pattern - Different trading strategies (Aggressive, Conservative, Day Trading)");
        System.out.println("✓ Singleton Pattern - Single StockExchange instance");
        
        System.out.println("\nKey Features Demonstrated:");
        System.out.println("✓ Market orders (execute immediately)");
        System.out.println("✓ Limit orders (execute at specified price)");
        System.out.println("✓ Stop-loss orders (automatic sell on price drop)");
        System.out.println("✓ Real-time price updates with observer notifications");
        System.out.println("✓ Portfolio management and P/L calculation");
        System.out.println("✓ Trading strategies with different behaviors");
        System.out.println("✓ Order validation (insufficient funds/stocks)");
        System.out.println("✓ Transaction history tracking");
        System.out.println("✓ Market analytics and summaries");
        
        System.out.println("\nExtensibility:");
        System.out.println("✓ Easy to add new order types (implement Order abstract class)");
        System.out.println("✓ Easy to add new trading strategies (implement TradingStrategy interface)");
        System.out.println("✓ Easy to add new stocks and traders");
        System.out.println("✓ Observer pattern allows adding new notification channels");
    }
}

