# Problem 22: Stock Trading System

## 🎯 Difficulty: Hard ⭐⭐⭐⭐

## 📝 Problem Statement

Design a real-time stock trading system that supports buying and selling stocks, tracking portfolios, real-time price updates, order matching, and notifications. The system should handle market orders, limit orders, stop-loss orders, support multiple trading strategies, and provide real-time updates to traders when stock prices change or orders are executed.

## 🔍 Functional Requirements (FR)

### FR1: Stock Management
- Maintain a list of tradable stocks with real-time prices
- Track stock information (symbol, name, current price, volume)
- Update stock prices in real-time
- Historical price tracking

### FR2: User/Trader Management
- Register traders with accounts
- Track portfolio (stocks owned, quantities, purchase prices)
- Maintain account balance (cash available)
- Track transaction history

### FR3: Order Management
- Support multiple order types:
  - **Market Order**: Execute immediately at current price
  - **Limit Order**: Execute only at specified price or better
  - **Stop-Loss Order**: Sell when price drops to specified level
- Order states: PENDING, EXECUTED, CANCELLED, PARTIALLY_FILLED
- Order matching engine

### FR4: Trading Operations
- Buy stocks (deduct cash, add to portfolio)
- Sell stocks (add cash, remove from portfolio)
- Calculate profit/loss on trades
- Validate sufficient funds/stocks before trading

### FR5: Real-Time Notifications (Observer Pattern)
- Notify traders when their orders are executed
- Notify traders when stock prices change significantly
- Notify traders when stop-loss is triggered
- Alert on portfolio value changes

### FR6: Trading Strategies (Strategy Pattern)
- **Aggressive Strategy**: Quick trades, higher risk
- **Conservative Strategy**: Slow trades, lower risk
- **Day Trading Strategy**: Buy and sell within same day
- **Long-Term Strategy**: Hold for extended periods

### FR7: Market Analytics
- Calculate portfolio value
- Track profit/loss per stock
- Display top gainers/losers
- Market summary statistics

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Real-Time Performance
- Price updates propagate instantly
- Order execution within milliseconds
- Efficient order matching algorithm

### NFR2: Data Consistency
- Prevent double-spending (can't sell more than owned)
- Atomic transactions (buy/sell completes fully or not at all)
- Accurate portfolio calculations

### NFR3: Scalability
- Support thousands of traders
- Handle millions of orders
- Efficient price update mechanism

### NFR4: Extensibility
- Easy to add new order types
- Easy to add new trading strategies
- Support for future features (options, futures, crypto)

## 🎨 Design Patterns to Use

### 1. **Observer Pattern**
- **Where:** Real-time price updates and order notifications
- **Why:** Traders need to be notified when prices change or orders execute. Observer pattern provides loose coupling between stock market and traders.

### 2. **Strategy Pattern**
- **Where:** Trading strategies (Aggressive, Conservative, Day Trading)
- **Why:** Different traders use different strategies. Strategy pattern allows runtime switching and easy addition of new strategies.

### 3. **Singleton Pattern**
- **Where:** StockExchange class (central market)
- **Why:** Only one stock exchange should exist. Singleton ensures single instance and global access point.

## 📋 Core Entities

### 1. **Stock**
- Attributes: symbol, name, currentPrice, previousPrice, volume, priceHistory
- Methods: updatePrice(), getPriceChange(), getPriceChangePercent()

### 2. **Trader**
- Attributes: traderId, name, balance, portfolio, orderHistory
- Methods: buyStock(), sellStock(), getPortfolioValue(), getProfitLoss()

### 3. **Portfolio**
- Attributes: holdings (Map<Stock, Holding>)
- Methods: addStock(), removeStock(), getTotalValue(), getProfitLoss()

### 4. **Holding**
- Attributes: stock, quantity, averagePurchasePrice
- Methods: calculateValue(), calculateProfitLoss()

### 5. **Order** (Abstract)
- Attributes: orderId, trader, stock, quantity, orderType, status, timestamp
- Subclasses: MarketOrder, LimitOrder, StopLossOrder

### 6. **OrderType** (Enum)
- Values: MARKET, LIMIT, STOP_LOSS

### 7. **OrderStatus** (Enum)
- Values: PENDING, EXECUTED, CANCELLED, PARTIALLY_FILLED

### 8. **TradingStrategy** (Interface)
- Methods: shouldBuy(Stock stock), shouldSell(Stock stock, Holding holding)
- Implementations: AggressiveStrategy, ConservativeStrategy, DayTradingStrategy

### 9. **StockExchange** (Singleton)
- Attributes: stocks, traders, orders, observers
- Methods: registerStock(), registerTrader(), placeOrder(), updateStockPrice()

### 10. **Observer** (Interface)
- Methods: onPriceUpdate(Stock stock), onOrderExecuted(Order order)
- Implementations: TraderObserver

## 🧪 Test Scenarios

### Scenario 1: Basic Buy and Sell
```
1. Trader registers with $10,000
2. Trader buys 10 shares of AAPL at $150
3. Price increases to $160
4. Trader sells 10 shares
5. Profit calculated: $100
```

### Scenario 2: Market Order Execution
```
1. Trader places market order to buy 5 shares
2. Order executes immediately at current price
3. Trader notified of execution
4. Portfolio updated
```

### Scenario 3: Limit Order
```
1. Trader places limit order: Buy 10 shares at $145 (current: $150)
2. Order stays pending
3. Price drops to $145
4. Order executes automatically
5. Trader notified
```

### Scenario 4: Stop-Loss Order
```
1. Trader owns 10 shares bought at $150
2. Trader sets stop-loss at $140
3. Price drops to $140
4. Stop-loss triggers, sells automatically
5. Trader notified of loss prevention
```

### Scenario 5: Real-Time Price Updates (Observer Pattern)
```
1. Multiple traders watching AAPL
2. AAPL price changes from $150 to $155
3. All observers notified instantly
4. Portfolios recalculated
```

### Scenario 6: Trading Strategy
```
1. Trader A uses Aggressive Strategy
2. Trader B uses Conservative Strategy
3. Same stock, different decisions
4. Strategy determines buy/sell timing
```

### Scenario 7: Insufficient Funds
```
1. Trader has $1,000
2. Tries to buy 100 shares at $150 ($15,000)
3. Order rejected with error message
4. Balance unchanged
```

### Scenario 8: Portfolio Analytics
```
1. Trader owns multiple stocks
2. Calculate total portfolio value
3. Show profit/loss per stock
4. Display overall P&L
```

## ⏱️ Time Allocation (75-90 minutes)

- **5 mins:** Clarify requirements, identify entities
- **10 mins:** Design class structure (enums, models)
- **15 mins:** Implement Observer pattern for notifications
- **15 mins:** Implement Strategy pattern for trading strategies
- **10 mins:** Implement order types and matching logic
- **15 mins:** Implement StockExchange service (Singleton)
- **10 mins:** Write Main.java with comprehensive scenarios

## 💡 Hints

<details>
<summary>Hint 1: Observer Pattern Structure</summary>

Stock is the Subject, Traders are Observers:
```java
public class Stock extends Subject {
    public void updatePrice(double newPrice) {
        this.currentPrice = newPrice;
        notifyObservers("Price updated to $" + newPrice);
    }
}

public class TraderObserver implements Observer {
    public void onPriceUpdate(Stock stock) {
        System.out.println("Trader notified: " + stock.getSymbol() + 
                          " price changed to $" + stock.getCurrentPrice());
    }
}
```
</details>

<details>
<summary>Hint 2: Strategy Pattern for Trading</summary>

```java
public interface TradingStrategy {
    boolean shouldBuy(Stock stock, double availableBalance);
    boolean shouldSell(Stock stock, Holding holding);
}

public class AggressiveStrategy implements TradingStrategy {
    public boolean shouldBuy(Stock stock, double balance) {
        // Buy if price increased by 2% (momentum)
        return stock.getPriceChangePercent() > 2.0;
    }
    
    public boolean shouldSell(Stock stock, Holding holding) {
        // Sell if profit > 5% or loss > 3%
        double profitPercent = holding.getProfitLossPercent();
        return profitPercent > 5.0 || profitPercent < -3.0;
    }
}
```
</details>

<details>
<summary>Hint 3: Order Matching Logic</summary>

```java
public void matchOrders() {
    for (Order order : pendingOrders) {
        if (order instanceof LimitOrder) {
            LimitOrder limitOrder = (LimitOrder) order;
            if (canExecuteLimitOrder(limitOrder)) {
                executeOrder(limitOrder);
            }
        } else if (order instanceof StopLossOrder) {
            StopLossOrder stopOrder = (StopLossOrder) order;
            if (shouldTriggerStopLoss(stopOrder)) {
                executeOrder(stopOrder);
            }
        }
    }
}
```
</details>

<details>
<summary>Hint 4: Portfolio Calculation</summary>

```java
public double getTotalValue() {
    return holdings.values().stream()
        .mapToDouble(h -> h.getQuantity() * h.getStock().getCurrentPrice())
        .sum();
}

public double getTotalProfitLoss() {
    return holdings.values().stream()
        .mapToDouble(Holding::calculateProfitLoss)
        .sum();
}
```
</details>

<details>
<summary>Hint 5: Atomic Transactions</summary>

```java
public synchronized boolean buyStock(Trader trader, Stock stock, int quantity) {
    double cost = stock.getCurrentPrice() * quantity;
    
    if (trader.getBalance() < cost) {
        return false; // Insufficient funds
    }
    
    // Atomic: both must succeed
    trader.deductBalance(cost);
    trader.getPortfolio().addStock(stock, quantity, stock.getCurrentPrice());
    
    return true;
}
```
</details>

## ✅ Success Criteria

- [ ] Compiles without errors
- [ ] Uses Observer pattern correctly for notifications
- [ ] Uses Strategy pattern correctly for trading strategies
- [ ] Uses Singleton pattern for StockExchange
- [ ] Handles all test scenarios
- [ ] Proper order matching logic
- [ ] Accurate portfolio calculations
- [ ] Prevents invalid trades (insufficient funds/stocks)
- [ ] Real-time price updates propagate to observers
- [ ] Clear error messages for failed operations

## 🎓 Key Learning Points

1. **Observer Pattern:** Best for event-driven systems with real-time updates
2. **Strategy Pattern:** Best for algorithms that vary by context
3. **Singleton Pattern:** Best for centralized resources
4. **Combining Patterns:** Real-world systems use multiple patterns
5. **Atomic Operations:** Ensure data consistency in financial systems
6. **Double Precision:** Use BigDecimal for financial calculations in production
7. **Thread Safety:** Synchronize critical sections for concurrent trading

## 📚 Related Problems

- **Problem 16:** Ride Sharing (similar Observer pattern usage)
- **Problem 20:** Twitter Feed (similar Observer pattern for updates)
- **Problem 23:** LRU Cache (similar Singleton pattern)
- **Problem 24:** Logging Framework (similar Singleton pattern)

---

**Time to implement! This is a complex financial system that showcases multiple patterns! 📈🚀**

