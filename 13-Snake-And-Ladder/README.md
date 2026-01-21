# Problem 13: Snake & Ladder Game

## 🎯 Difficulty: Medium ⭐⭐⭐

## 📝 Problem Statement

Design a Snake and Ladder board game system that supports multiple players, customizable board size, snakes, ladders, and different dice rolling strategies.

---

## 🔍 Functional Requirements (FR)

### FR1: Game Setup
- Support customizable board size (default: 100 cells)
- Support 2-4 players
- Players start at position 0 (off the board)
- First player to reach the final cell wins
- Snakes and ladders can be placed at any position (except start and end)

### FR2: Game Components
- **Board:** N cells (numbered 1 to N)
- **Players:** Each player has a name and current position
- **Snakes:** Move player down from head to tail
- **Ladders:** Move player up from start to end
- **Dice:** Roll to determine movement

### FR3: Game Rules
- Players take turns in order
- Roll dice to get a number (1-6 for standard dice)
- Move forward by the dice value
- If land on snake head → slide down to tail
- If land on ladder start → climb up to end
- Exact number needed to win (if roll exceeds final position, don't move)
- First player to reach the final cell wins

### FR4: Dice Strategies
- **Normal Dice:** Single die (1-6)
- **Double Dice:** Roll two dice, sum the values (2-12)
- **Loaded Dice:** Biased dice with custom probabilities

### FR5: Game Flow
- Initialize game with players, board size, snakes, and ladders
- Start game
- Players take turns rolling dice and moving
- Display game state after each move
- Announce winner when someone reaches the end

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Extensibility
- Easy to add new dice strategies (e.g., weighted dice, triple dice)
- Easy to modify board size
- Easy to add new game rules (e.g., bonus squares, penalty squares)

### NFR2: Maintainability
- Clean separation of concerns
- Well-documented code
- Clear class responsibilities

### NFR3: Flexibility
- Support different board sizes (10, 50, 100, 200 cells)
- Support variable number of snakes and ladders
- Support different dice types per game

### NFR4: Reliability
- Validate snake and ladder positions
- Prevent invalid moves
- Handle edge cases (landing exactly on final cell, overshooting)

---

## 🎨 Design Patterns to Use

### 1. **Strategy Pattern**
- **Where:** Dice rolling strategies
- **Why:** Different dice types with different rolling logic

### 2. **Factory Pattern**
- **Where:** Dice creation
- **Why:** Create different types of dice based on configuration

### 3. **Singleton Pattern** (Optional)
- **Where:** Game instance
- **Why:** Only one game should be running at a time

---

## 📋 Core Entities

### 1. **Player**
- Attributes: `id`, `name`, `currentPosition`
- Methods: `move(steps)`, `setPosition(position)`

### 2. **Snake**
- Attributes: `head`, `tail`
- Methods: `getHead()`, `getTail()`

### 3. **Ladder**
- Attributes: `start`, `end`
- Methods: `getStart()`, `getEnd()`

### 4. **Board**
- Attributes: `size`, `snakes`, `ladders`
- Methods: `getNewPosition(currentPosition)`, `isFinalPosition(position)`

### 5. **Dice** (Interface)
- Methods: `roll()`

### 6. **Game**
- Attributes: `board`, `players`, `dice`, `currentPlayerIndex`
- Methods: `start()`, `playTurn()`, `isGameOver()`, `getWinner()`

---

## 🧪 Test Scenarios

### Scenario 1: Basic Game Flow
```
1. Create board with size 100
2. Add 5 snakes: (17→7), (54→34), (62→19), (64→60), (87→24)
3. Add 5 ladders: (3→38), (8→31), (28→84), (58→77), (75→86)
4. Create 3 players: Alice, Bob, Charlie
5. Play game with normal dice
6. Display moves and winner
```

### Scenario 2: Snake Encounter
```
1. Player at position 10
2. Rolls 7 → moves to 17
3. Snake at 17→7
4. Player slides down to 7
5. Display: "Alice rolled 7, moved to 17, hit snake! Slid down to 7"
```

### Scenario 3: Ladder Encounter
```
1. Player at position 5
2. Rolls 3 → moves to 8
3. Ladder at 8→31
4. Player climbs up to 31
5. Display: "Bob rolled 3, moved to 8, found ladder! Climbed to 31"
```

### Scenario 4: Exact Win
```
1. Player at position 97
2. Rolls 3 → moves to 100
3. Player wins!
4. Display: "Charlie rolled 3, reached 100. Charlie wins!"
```

### Scenario 5: Overshoot (No Move)
```
1. Player at position 97
2. Rolls 6 → would go to 103 (exceeds 100)
3. Player stays at 97
4. Display: "Alice rolled 6, but cannot move (would exceed 100)"
```

### Scenario 6: Double Dice Strategy
```
1. Use double dice (2 dice)
2. Player rolls: Die1=3, Die2=5, Total=8
3. Move 8 steps
4. Display: "Bob rolled [3, 5] = 8, moved to position X"
```

---

## ⏱️ Time Allocation (60 minutes)

- **5 mins:** Clarify requirements, understand game rules
- **5 mins:** List entities and relationships
- **5 mins:** Identify design patterns
- **35 mins:** Write code (enums → model → strategy → factory → service → main)
- **10 mins:** Test with multiple scenarios, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: Strategy Pattern for Dice</summary>

```java
public interface DiceStrategy {
    int roll();
}

public class NormalDice implements DiceStrategy {
    private Random random = new Random();
    
    @Override
    public int roll() {
        return random.nextInt(6) + 1; // 1-6
    }
}

public class DoubleDice implements DiceStrategy {
    private Random random = new Random();
    
    @Override
    public int roll() {
        int die1 = random.nextInt(6) + 1;
        int die2 = random.nextInt(6) + 1;
        return die1 + die2; // 2-12
    }
}
```
</details>

<details>
<summary>Hint 2: Factory Pattern for Dice Creation</summary>

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
</details>

<details>
<summary>Hint 3: Board Position Logic</summary>

```java
public int getNewPosition(int currentPosition, int diceValue) {
    int newPosition = currentPosition + diceValue;
    
    // Check if exceeds board size
    if (newPosition > size) {
        return currentPosition; // Stay at current position
    }
    
    // Check for snake
    for (Snake snake : snakes) {
        if (snake.getHead() == newPosition) {
            return snake.getTail();
        }
    }
    
    // Check for ladder
    for (Ladder ladder : ladders) {
        if (ladder.getStart() == newPosition) {
            return ladder.getEnd();
        }
    }
    
    return newPosition;
}
```
</details>

<details>
<summary>Hint 4: Game Loop</summary>

```java
public void start() {
    while (!isGameOver()) {
        Player currentPlayer = players.get(currentPlayerIndex);
        playTurn(currentPlayer);
        
        if (currentPlayer.getCurrentPosition() == board.getSize()) {
            System.out.println(currentPlayer.getName() + " wins!");
            break;
        }
        
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Add Special Squares**
   - Bonus square: Roll again
   - Penalty square: Skip next turn
   - Teleport square: Jump to random position

2. **Add Game Statistics**
   - Track number of turns per player
   - Track snake encounters
   - Track ladder encounters
   - Display statistics at end

3. **Add Multiplayer Modes**
   - Team mode (2v2)
   - Elimination mode (last to finish is eliminated)

4. **Add Custom Rules**
   - Double roll on 6
   - Multiple snakes/ladders on same cell
   - Minimum dice value to start (e.g., must roll 6 to start)

5. **Add Save/Load Game**
   - Save game state
   - Resume from saved state

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Strategy and Factory patterns correctly
- [ ] Handle all test scenarios
- [ ] Validate snake/ladder positions
- [ ] Handle exact win condition
- [ ] Handle overshoot (no move)
- [ ] Display clear game progress
- [ ] Support multiple dice strategies
- [ ] Be extensible (easy to add new dice types)

---

## 📁 File Structure

```
13-Snake-And-Ladder/
├── README.md (this file)
├── src/
│   ├── enums/
│   │   ├── DiceType.java
│   │   └── GameStatus.java
│   ├── model/
│   │   ├── Player.java
│   │   ├── Snake.java
│   │   ├── Ladder.java
│   │   └── Board.java
│   ├── strategy/
│   │   ├── DiceStrategy.java (interface)
│   │   ├── NormalDice.java
│   │   ├── DoubleDice.java
│   │   └── LoadedDice.java
│   ├── factory/
│   │   └── DiceFactory.java
│   ├── service/
│   │   └── Game.java
│   └── Main.java
├── SOLUTION.md
└── COMPILATION-GUIDE.md
```

---

**Good luck! Start coding! 🚀**


