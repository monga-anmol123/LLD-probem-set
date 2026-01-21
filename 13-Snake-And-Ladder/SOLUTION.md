# Solution: Snake & Ladder Game

## ✅ Complete Implementation

This folder contains a fully working Snake and Ladder game system demonstrating Strategy and Factory design patterns.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                         Main.java                            │
│                    (Demo/Entry Point)                        │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
        ▼               ▼               ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐
│   Factory    │ │   Service   │ │  Strategy  │
│              │ │             │ │            │
│ Dice         │ │   Game      │ │   Dice     │
│ Factory      │ │             │ │ Strategy   │
│              │ │             │ │            │
└──────────────┘ └─────────────┘ └────────────┘
        │               │               │
        └───────────────┼───────────────┘
                        │
                        ▼
                ┌─────────────┐
                │    Model    │
                │             │
                │   Player    │
                │   Snake     │
                │   Ladder    │
                │   Board     │
                └─────────────┘
```

---

## 📦 Package Structure

```
src/
├── enums/                  # Type-safe enumerations
│   ├── DiceType.java      # NORMAL, DOUBLE, LOADED
│   └── GameStatus.java    # NOT_STARTED, IN_PROGRESS, COMPLETED
│
├── model/                  # Domain entities
│   ├── Player.java        # Player with position tracking
│   ├── Snake.java         # Snake with head and tail
│   ├── Ladder.java        # Ladder with start and end
│   └── Board.java         # Board with snakes, ladders, and logic
│
├── strategy/               # Strategy Pattern
│   ├── DiceStrategy.java       # Interface
│   ├── NormalDice.java         # Single die (1-6)
│   ├── DoubleDice.java         # Two dice (2-12)
│   └── LoadedDice.java         # Biased dice
│
├── factory/                # Factory Pattern
│   └── DiceFactory.java   # Centralized dice creation
│
├── service/                # Business logic
│   └── Game.java          # Main game orchestration
│
└── Main.java               # Demo application with 5 scenarios
```

---

## 🎨 Design Patterns Explained

### 1. **Strategy Pattern** (DiceStrategy)

**Purpose:** Allow different dice rolling algorithms to be used interchangeably

**Implementation:**
```java
public interface DiceStrategy {
    int roll();
    String getName();
}

public class NormalDice implements DiceStrategy {
    public int roll() {
        return random.nextInt(6) + 1; // 1-6
    }
}

public class DoubleDice implements DiceStrategy {
    public int roll() {
        int die1 = random.nextInt(6) + 1;
        int die2 = random.nextInt(6) + 1;
        return die1 + die2; // 2-12
    }
}
```

**Benefits:**
- ✅ Easy to add new dice types (e.g., triple dice, weighted dice)
- ✅ Dice behavior can be changed at runtime
- ✅ Each dice type is isolated and testable
- ✅ Game logic doesn't need to know dice implementation details

**Real-world Usage:**
- Different game modes (easy/hard)
- Special events (double dice power-up)
- Testing with predictable dice

---

### 2. **Factory Pattern** (DiceFactory)

**Purpose:** Centralize dice object creation

**Implementation:**
```java
public class DiceFactory {
    public static DiceStrategy createDice(DiceType type) {
        switch (type) {
            case NORMAL:
                return new NormalDice();
            case DOUBLE:
                return new DoubleDice();
            case LOADED:
                return new LoadedDice();
            default:
                return new NormalDice();
        }
    }
}
```

**Benefits:**
- ✅ Single place to manage dice creation
- ✅ Easy to add new dice types
- ✅ Client code doesn't need to know concrete classes
- ✅ Can add caching or pooling logic if needed

**Usage in Code:**
```java
DiceStrategy dice = DiceFactory.createDice(DiceType.NORMAL);
Game game = new Game(board, players, dice);
```

---

## 🔑 Key Design Decisions

### 1. **Board Position Management**

**Decision:** Use HashMap for O(1) snake/ladder lookups

```java
private final Map<Integer, Integer> snakeMap;  // head -> tail
private final Map<Integer, Integer> ladderMap; // start -> end
```

**Why:**
- Fast lookups when player lands on a position
- Better than iterating through lists every move
- Prevents duplicate snakes/ladders at same position

**Alternative Considered:**
- Array-based (wasteful for large boards with few snakes/ladders)
- List iteration (O(n) lookup time)

---

### 2. **Exact Win Condition**

**Decision:** Player must roll exact number to reach final cell

```java
if (targetPosition > board.getSize()) {
    // Stay at current position
    return currentPosition;
}
```

**Why:**
- Standard Snake & Ladder rule
- Adds strategy near the end
- Prevents anticlimactic wins

---

### 3. **Player Position Tracking**

**Decision:** Store position in Player object, not in Board

```java
public class Player {
    private int currentPosition;
}
```

**Why:**
- Player owns their position (Single Responsibility)
- Board doesn't need to track all players
- Easier to add player-specific features (history, stats)

**Alternative Considered:**
- Board tracks all player positions (tight coupling)
- Separate PlayerPosition class (over-engineering for this problem)

---

### 4. **Snake and Ladder Validation**

**Decision:** Validate at construction time

```java
public Snake(int head, int tail) {
    if (head <= tail) {
        throw new IllegalArgumentException("Snake head must be greater than tail");
    }
}
```

**Why:**
- Fail fast - catch errors early
- Invalid snakes/ladders never exist in the system
- Clear error messages for developers

---

### 5. **Game Flow Control**

**Decision:** Game class orchestrates turns, not Board or Player

```java
public class Game {
    public void start() {
        while (!isGameOver()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            playTurn(currentPlayer);
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }
}
```

**Why:**
- Clear separation of concerns
- Game logic in one place
- Easy to add features (undo, save/load, replay)

---

## 🧩 Class Responsibilities

### **Player**
- ✅ Track current position
- ✅ Move to new position
- ✅ Store player identity (id, name)
- ❌ Does NOT know about board, snakes, ladders

### **Board**
- ✅ Store snakes and ladders
- ✅ Calculate new position after move
- ✅ Validate snake/ladder positions
- ❌ Does NOT track players or game state

### **Snake / Ladder**
- ✅ Store positions (head/tail or start/end)
- ✅ Validate positions at construction
- ❌ Does NOT have any behavior logic

### **DiceStrategy**
- ✅ Generate random dice values
- ✅ Encapsulate rolling logic
- ❌ Does NOT know about game or players

### **Game**
- ✅ Orchestrate game flow
- ✅ Manage turns
- ✅ Determine winner
- ✅ Display game state
- ❌ Does NOT implement dice or board logic

---

## 🎯 Design Principles Applied

### **1. Single Responsibility Principle (SRP)**
- Each class has one reason to change
- Player: Player data changes
- Board: Board configuration changes
- Game: Game rules change
- DiceStrategy: Dice behavior changes

### **2. Open/Closed Principle (OCP)**
- Open for extension: Add new dice types without modifying existing code
- Closed for modification: DiceStrategy interface is stable

### **3. Liskov Substitution Principle (LSP)**
- Any DiceStrategy implementation can be used in Game
- All dice types behave consistently (return valid roll values)

### **4. Interface Segregation Principle (ISP)**
- DiceStrategy has minimal interface (roll, getName)
- Clients only depend on what they need

### **5. Dependency Inversion Principle (DIP)**
- Game depends on DiceStrategy interface, not concrete implementations
- High-level game logic doesn't depend on low-level dice details

---

## 🚀 Extensibility Examples

### **Adding a New Dice Type**

```java
// 1. Create new strategy
public class TripleDice implements DiceStrategy {
    public int roll() {
        return random.nextInt(6) + random.nextInt(6) + random.nextInt(6) + 3;
    }
    public String getName() {
        return "Triple Dice (3-18)";
    }
}

// 2. Add to enum
public enum DiceType {
    NORMAL, DOUBLE, LOADED, TRIPLE
}

// 3. Add to factory
public class DiceFactory {
    public static DiceStrategy createDice(DiceType type) {
        // ... existing cases ...
        case TRIPLE:
            return new TripleDice();
    }
}
```

**No changes needed in:**
- Game.java
- Board.java
- Player.java
- Main.java (unless you want to demo it)

---

### **Adding Special Squares**

```java
// 1. Create new model
public class BonusSquare {
    private final int position;
    private final String effect; // "ROLL_AGAIN", "SKIP_TURN", etc.
}

// 2. Add to Board
public class Board {
    private final Map<Integer, BonusSquare> bonusSquares;
    
    public int getNewPosition(int currentPosition, int diceValue) {
        // ... existing logic ...
        
        // Check for bonus square
        if (bonusSquares.containsKey(newPosition)) {
            applyBonus(bonusSquares.get(newPosition));
        }
    }
}
```

---

### **Adding Game Statistics**

```java
public class GameStatistics {
    private int totalTurns;
    private Map<Player, Integer> snakeHits;
    private Map<Player, Integer> ladderHits;
    
    public void recordSnakeHit(Player player) {
        snakeHits.merge(player, 1, Integer::sum);
    }
    
    public void displayStats() {
        // Print statistics
    }
}

// Add to Game
public class Game {
    private GameStatistics stats;
    
    private void playTurn(Player player) {
        // ... existing logic ...
        if (board.hasSnake(targetPosition)) {
            stats.recordSnakeHit(player);
        }
    }
}
```

---

## 🧪 Test Scenarios Covered

### **Scenario 1: Basic Game Flow** ✅
- 3 players, normal dice, 100-cell board
- Multiple snakes and ladders
- Complete game until winner

### **Scenario 2: Double Dice** ✅
- 2 players, double dice (2-12 range)
- Faster gameplay due to higher rolls
- Tests strategy pattern with different dice

### **Scenario 3: Small Board** ✅
- 4 players, 50-cell board
- Quick game for testing
- Validates board size flexibility

### **Scenario 4: Loaded Dice** ✅
- 2 players, biased dice (favors high values)
- Tests weighted probability dice
- Demonstrates strategy pattern extensibility

### **Scenario 5: Complex Board** ✅
- 3 players, many snakes and ladders
- Tests collision detection
- Validates no duplicate positions

---

## 🎮 Sample Game Output

```
══════════════════════════════════════════════════════════════
🎲 SNAKE AND LADDER GAME STARTED! 🎲
══════════════════════════════════════════════════════════════
Board Size: 100
Dice Type: Normal Dice (1-6)
Players: 3
  - Alice
  - Bob
  - Charlie
Snakes: 8
  Snake[17 → 7]
  Snake[54 → 34]
  ...
Ladders: 8
  Ladder[3 → 38]
  Ladder[8 → 31]
  ...
══════════════════════════════════════════════════════════════

Turn 1: Alice's turn
  Alice rolled: 5
  Alice moved from 0 to 5
  📍 Alice is now at position 5

Turn 2: Bob's turn
  Bob rolled: 3
  Bob moved from 0 to 3
  🪜 Yay! Found a ladder at 3!
  Bob climbs up to 38
  📍 Bob is now at position 38

Turn 3: Charlie's turn
  Charlie rolled: 6
  Charlie moved from 0 to 6
  📍 Charlie is now at position 6

...

Turn 47: Bob's turn
  Bob rolled: 3
  Bob moved from 97 to 100
  📍 Bob is now at position 100

🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉
🏆 Bob WINS THE GAME! 🏆
🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉
```

---

## ⚖️ Trade-offs and Alternatives

### **1. Random vs Deterministic Dice**

**Current:** Uses Random for dice rolls
```java
private final Random random = new Random();
```

**Alternative:** Seed-based or deterministic for testing
```java
public NormalDice(long seed) {
    this.random = new Random(seed);
}
```

**Trade-off:**
- ✅ Current: True randomness, better gameplay
- ✅ Alternative: Reproducible games, easier testing
- 💡 Could add both via constructor overloading

---

### **2. Turn Management**

**Current:** Round-robin using modulo
```java
currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
```

**Alternative:** Queue-based
```java
Queue<Player> playerQueue = new LinkedList<>(players);
Player current = playerQueue.poll();
playerQueue.offer(current);
```

**Trade-off:**
- ✅ Current: Simple, efficient, clear
- ✅ Alternative: Easier to skip turns, reorder players
- 💡 Current approach is sufficient for standard rules

---

### **3. Position Validation**

**Current:** Validate at move time
```java
if (targetPosition > board.getSize()) {
    return currentPosition;
}
```

**Alternative:** Validate before move
```java
if (!board.isValidMove(currentPosition, diceValue)) {
    return;
}
```

**Trade-off:**
- ✅ Current: Simpler, fewer method calls
- ✅ Alternative: Better separation of concerns
- 💡 Current approach works well for this problem size

---

## 🏆 Strengths of This Solution

1. **Clean Separation of Concerns**
   - Each class has a clear, single responsibility
   - Easy to understand and maintain

2. **Extensible Design**
   - Add new dice types without changing existing code
   - Add new features (bonus squares, statistics) easily

3. **Type Safety**
   - Enums for DiceType and GameStatus
   - Compile-time checks prevent errors

4. **Comprehensive Validation**
   - Snake/ladder positions validated at construction
   - Invalid moves prevented
   - Clear error messages

5. **Rich Demo**
   - 5 different scenarios
   - Tests all major features
   - Clear, formatted output

6. **Production-Ready Code**
   - Proper JavaDoc comments
   - Consistent naming conventions
   - No magic numbers
   - Defensive programming

---

## 📈 Complexity Analysis

### **Time Complexity**

| Operation | Complexity | Explanation |
|-----------|-----------|-------------|
| Roll Dice | O(1) | Simple random generation |
| Check Snake/Ladder | O(1) | HashMap lookup |
| Move Player | O(1) | Direct position update |
| Play Turn | O(1) | Fixed operations |
| Complete Game | O(n) | n = number of turns (varies) |

### **Space Complexity**

| Component | Complexity | Explanation |
|-----------|-----------|-------------|
| Board | O(s + l) | s = snakes, l = ladders |
| Players | O(p) | p = number of players |
| Game State | O(p) | Tracks player positions |
| Total | O(p + s + l) | Linear in input size |

---

## 🎓 Interview Discussion Points

### **What would you discuss in an interview?**

1. **Design Patterns**
   - Why Strategy pattern for dice?
   - Why Factory pattern for dice creation?
   - Could we use other patterns?

2. **Scalability**
   - How to handle 1000-cell board? (No problem, HashMap scales well)
   - How to handle 100 players? (No problem, List scales well)
   - How to handle networked multiplayer? (Need to add serialization, state sync)

3. **Extensibility**
   - How to add new game modes?
   - How to add undo/redo?
   - How to add AI players?

4. **Testing**
   - How to test random dice? (Dependency injection, mock dice)
   - How to test game logic? (Unit tests for each class)
   - How to test edge cases? (Exact win, overshoot, etc.)

5. **Performance**
   - Is HashMap necessary? (Yes, for O(1) lookups)
   - Could we optimize turn management? (Current approach is optimal)
   - Any memory concerns? (No, space complexity is linear)

---

## ✅ Checklist

- [x] Compiles without errors
- [x] Runs successfully
- [x] Uses Strategy pattern correctly
- [x] Uses Factory pattern correctly
- [x] Handles exact win condition
- [x] Handles overshoot (no move)
- [x] Validates snake/ladder positions
- [x] Prevents duplicate positions
- [x] Supports multiple dice types
- [x] Supports multiple players (2-4)
- [x] Supports variable board sizes
- [x] Clear, formatted output
- [x] Comprehensive demo (5 scenarios)
- [x] Well-documented code
- [x] Follows naming conventions
- [x] No magic numbers
- [x] Proper error handling

---

## 🎯 Learning Outcomes

After studying this solution, you should understand:

1. ✅ How to implement Strategy pattern for interchangeable algorithms
2. ✅ How to implement Factory pattern for object creation
3. ✅ How to design a turn-based game system
4. ✅ How to validate game rules and positions
5. ✅ How to structure a multi-class Java project
6. ✅ How to separate concerns (model, service, strategy, factory)
7. ✅ How to write extensible, maintainable code
8. ✅ How to handle game state and flow control

---

**This solution is interview-ready and production-quality!** 🚀


