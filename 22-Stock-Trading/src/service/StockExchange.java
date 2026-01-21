package service;

import model.*;
import enums.*;
import observer.*;
import java.util.*;

public class StockExchange extends Subject {
    private static StockExchange instance;
    
    private final Map<String, Stock> stocks; // symbol -> Stock
    private final Map<String, Trader> traders; // traderId -> Trader
    private final List<Order> pendingOrders;
    private final List<Order> executedOrders;
    
    private StockExchange() {
        this.stocks = new HashMap<>();
        this.traders = new HashMap<>();
        this.pendingOrders = new ArrayList<>();
        this.executedOrders = new ArrayList<>();
    }
    
    public static synchronized StockExchange getInstance() {
        if (instance == null) {
            instance = new StockExchange();
        }
        return instance;
    }
    
    // Register stock
    public void registerStock(Stock stock) {
        stocks.put(stock.getSymbol(), stock);
        System.out.println("✅ Stock registered: " + stock);
    }
    
    // Register trader
    public void registerTrader(Trader trader) {
        traders.put(trader.getTraderId(), trader);
        System.out.println("✅ Trader registered: " + trader);
    }
    
    // Update stock price
    public void updateStockPrice(String symbol, double newPrice) {
        Stock stock = stocks.get(symbol);
        if (stock == null) {
            throw new IllegalArgumentException("Stock not found: " + symbol);
        }
        
        stock.updatePrice(newPrice);
        
        // Check and execute pending orders
        matchOrders();
    }
    
    // Place order
    public synchronized Order placeOrder(Order order) {
        System.out.println("\n📋 Placing order: " + order);
        
        // Validate order
        if (!validateOrder(order)) {
            order.reject();
            System.out.println("❌ Order REJECTED: " + order);
            return order;
        }
        
        // If market order, execute immediately
        if (order instanceof MarketOrder) {
            executeOrder(order);
        } else {
            // Add to pending orders
            pendingOrders.add(order);
            System.out.println("⏳ Order added to pending queue");
            
            // Try to match immediately
            if (order.canExecute()) {
                executeOrder(order);
            }
        }
        
        return order;
    }
    
    // Validate order
    private boolean validateOrder(Order order) {
        Trader trader = order.getTrader();
        Stock stock = order.getStock();
        int quantity = order.getQuantity();
        
        if (order.getTransactionType() == TransactionType.BUY) {
            double cost = stock.getCurrentPrice() * quantity;
            if (trader.getBalance() < cost) {
                System.out.println("❌ Insufficient balance: Required $" + 
                    String.format("%.2f", cost) + ", Available $" + 
                    String.format("%.2f", trader.getBalance()));
                return false;
            }
        } else { // SELL
            if (!trader.getPortfolio().hasStock(stock, quantity)) {
                System.out.println("❌ Insufficient shares to sell");
                return false;
            }
        }
        
        return true;
    }
    
    // Execute order
    private void executeOrder(Order order) {
        Trader trader = order.getTrader();
        Stock stock = order.getStock();
        int quantity = order.getQuantity();
        double price = stock.getCurrentPrice();
        
        try {
            if (order.getTransactionType() == TransactionType.BUY) {
                // Buy stocks
                double cost = price * quantity;
                trader.deductBalance(cost);
                trader.getPortfolio().addStock(stock, quantity, price);
                
                System.out.printf("✅ BUY EXECUTED: %d shares of %s at $%.2f (Total: $%.2f)\n",
                    quantity, stock.getSymbol(), price, cost);
            } else {
                // Sell stocks
                trader.getPortfolio().removeStock(stock, quantity);
                double proceeds = price * quantity;
                trader.addBalance(proceeds);
                
                System.out.printf("✅ SELL EXECUTED: %d shares of %s at $%.2f (Total: $%.2f)\n",
                    quantity, stock.getSymbol(), price, proceeds);
            }
            
            // Update order status
            order.execute(price);
            
            // Record transaction
            Transaction transaction = new Transaction(
                order.getTransactionType(), stock, quantity, price);
            trader.addTransaction(transaction);
            
            // Update stock volume
            stock.incrementVolume(quantity);
            
            // Move to executed orders
            pendingOrders.remove(order);
            executedOrders.add(order);
            
            // Notify observers
            notifyOrderExecuted(order);
            
        } catch (Exception e) {
            System.out.println("❌ Order execution failed: " + e.getMessage());
            order.reject();
        }
    }
    
    // Match pending orders
    private void matchOrders() {
        List<Order> toExecute = new ArrayList<>();
        
        for (Order order : pendingOrders) {
            if (order.canExecute() && validateOrder(order)) {
                toExecute.add(order);
            }
        }
        
        for (Order order : toExecute) {
            executeOrder(order);
        }
    }
    
    // Cancel order
    public void cancelOrder(String orderId) {
        for (Order order : pendingOrders) {
            if (order.getOrderId().equals(orderId)) {
                order.cancel();
                pendingOrders.remove(order);
                System.out.println("✅ Order cancelled: " + orderId);
                return;
            }
        }
        System.out.println("❌ Order not found: " + orderId);
    }
    
    // Get stock
    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }
    
    // Display market summary
    public void displayMarketSummary() {
        System.out.println("\n========================================");
        System.out.println("  📊 MARKET SUMMARY");
        System.out.println("========================================");
        
        System.out.println("\n📈 Listed Stocks:");
        for (Stock stock : stocks.values()) {
            double change = stock.getPriceChange();
            double changePercent = stock.getPriceChangePercent();
            String arrow = change >= 0 ? "📈" : "📉";
            System.out.printf("   %s %s: $%.2f (%s%.2f, %.2f%%) Volume: %d\n",
                arrow, stock.getSymbol(), stock.getCurrentPrice(),
                change >= 0 ? "+" : "", change, changePercent, stock.getVolume());
        }
        
        System.out.println("\n👥 Active Traders: " + traders.size());
        System.out.println("📋 Pending Orders: " + pendingOrders.size());
        System.out.println("✅ Executed Orders: " + executedOrders.size());
        
        System.out.println("========================================\n");
    }
    
    // Display pending orders
    public void displayPendingOrders() {
        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders");
            return;
        }
        
        System.out.println("\n⏳ Pending Orders:");
        for (Order order : pendingOrders) {
            System.out.println("   " + order);
        }
    }
    
    public Collection<Stock> getAllStocks() {
        return stocks.values();
    }
    
    public Collection<Trader> getAllTraders() {
        return traders.values();
    }
}

