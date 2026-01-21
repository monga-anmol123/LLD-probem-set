# Solution: Online Chess Game

## ✅ Complete Implementation

This folder contains a fully working chess game system demonstrating Factory, Command, and State design patterns with complete move validation, check/checkmate detection, and undo/redo functionality.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                         Main.java                            │
│                    (Demo/Entry Point)                        │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┬──────────────┐
        │               │               │              │
        ▼               ▼               ▼              ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐ ┌──────────┐
│   Factory    │ │   Command   │ │   Model    │ │  Enums   │
│              │ │             │ │            │ │          │
│ Piece        │ │ Move        │ │ Game       │ │ Color    │
│ Factory      │ │ Command     │ │ Board      │ │ PieceType│
│              │ │ Castling    │ │ Piece      │ │ GameState│
│              │ │ Command     │ │ Position   │ │          │
└──────────────┘ └─────────────┘ └────────────┘ └──────────┘
```

---

## 🎨 Design Patterns Explained

### 1. **Factory Pattern** (PieceFactory)

**Purpose:** Centralize piece creation

**Implementation:**
```java
public class PieceFactory {
    public static Piece createPiece(PieceType type, Color color, Position position) {
        switch (type) {
            case PAWN: return new Pawn(color, position);
            case ROOK: return new Rook(color, position);
            case KNIGHT: return new Knight(color, position);
            case BISHOP: return new Bishop(color, position);
            case QUEEN: return new Queen(color, position);
            case KING: return new King(color, position);
        }
    }
}
```

**Benefits:**
- ✅ Single place to create pieces
- ✅ Easy to add new piece types
- ✅ Consistent initialization

---

### 2. **Command Pattern** (MoveCommand, CastlingCommand)

**Purpose:** Encapsulate moves as objects to support undo/redo

**Implementation:**
```java
public interface Command {
    void execute();
    void undo();
}

public class MoveCommand implements Command {
    private Board board;
    private Piece piece;
    private Position from, to;
    private Piece capturedPiece;
    private boolean wasFirstMove;
    
    public void execute() {
        capturedPiece = board.getPiece(to);
        board.setPiece(to, piece);
        board.setPiece(from, null);
        piece.setPosition(to);
        piece.setHasMoved(true);
    }
    
    public void undo() {
        board.setPiece(from, piece);
        board.setPiece(to, capturedPiece);
        piece.setPosition(from);
        if (wasFirstMove) {
            piece.setHasMoved(false);
        }
    }
}
```

**Benefits:**
- ✅ Undo/redo functionality
- ✅ Move history tracking
- ✅ Encapsulates move logic
- ✅ Easy to add new move types

---

### 3. **State Pattern** (GameState)

**Purpose:** Manage game states (Active, Check, Checkmate, Stalemate)

**Implementation:**
```java
public enum GameState {
    ACTIVE, CHECK, CHECKMATE, STALEMATE, DRAW
}

private void updateGameState() {
    boolean inCheck = isKingInCheck(currentPlayer);
    boolean hasLegalMoves = hasLegalMoves(currentPlayer);
    
    if (inCheck && !hasLegalMoves) {
        gameState = GameState.CHECKMATE;
    } else if (!inCheck && !hasLegalMoves) {
        gameState = GameState.STALEMATE;
    } else if (inCheck) {
        gameState = GameState.CHECK;
    } else {
        gameState = GameState.ACTIVE;
    }
}
```

**Benefits:**
- ✅ Clear state transitions
- ✅ Easy to add new states
- ✅ State-specific behavior

---

## ♟️ Chess Rules Implementation

### Piece Movement Rules

#### Pawn
```java
// Forward 1 square
if (colDiff == 0 && rowDiff == direction) {
    return board.getPiece(to) == null;
}

// Forward 2 squares from start
if (!hasMoved && colDiff == 0 && rowDiff == 2 * direction) {
    Position middle = new Position(from.getRow() + direction, from.getCol());
    return board.getPiece(middle) == null && board.getPiece(to) == null;
}

// Diagonal capture
if (Math.abs(colDiff) == 1 && rowDiff == direction) {
    Piece target = board.getPiece(to);
    return target != null && target.getColor() != color;
}
```

#### Rook
```java
// Must move horizontally or vertically
if (from.getRow() != to.getRow() && from.getCol() != to.getCol()) {
    return false;
}

// Path must be clear
if (!isPathClear(from, to, board)) {
    return false;
}
```

#### Knight
```java
int rowDiff = Math.abs(to.getRow() - from.getRow());
int colDiff = Math.abs(to.getCol() - from.getCol());

// L-shape: 2+1 or 1+2
return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
```

#### Bishop
```java
int rowDiff = Math.abs(to.getRow() - from.getRow());
int colDiff = Math.abs(to.getCol() - from.getCol());

// Must move diagonally
return rowDiff == colDiff && isPathClear(from, to, board);
```

#### Queen
```java
// Combination of rook and bishop
boolean isHorizontalOrVertical = (from.getRow() == to.getRow() || from.getCol() == to.getCol());
boolean isDiagonal = (rowDiff == colDiff);

return (isHorizontalOrVertical || isDiagonal) && isPathClear(from, to, board);
```

#### King
```java
int rowDiff = Math.abs(to.getRow() - from.getRow());
int colDiff = Math.abs(to.getCol() - from.getCol());

// One square in any direction
return rowDiff <= 1 && colDiff <= 1 && (rowDiff + colDiff > 0);
```

---

## 🔍 Check Detection Algorithm

```java
public boolean isKingInCheck(Color kingColor) {
    Position kingPos = board.findKing(kingColor);
    
    // Check if any opponent piece can attack king
    for (Piece piece : board.getPieces(kingColor.opposite())) {
        if (piece.isValidMove(piece.getPosition(), kingPos, board)) {
            return true;
        }
    }
    
    return false;
}
```

**Time Complexity:** O(n) where n = number of opponent pieces

---

## 🏁 Checkmate Detection Algorithm

```java
public boolean isCheckmate(Color kingColor) {
    // Must be in check
    if (!isKingInCheck(kingColor)) {
        return false;
    }
    
    // Check if any legal move exists
    for (Piece piece : board.getPieces(kingColor)) {
        for (Position to : piece.getPossibleMoves(board)) {
            if (!wouldLeaveKingInCheck(piece, piece.getPosition(), to)) {
                return false; // Found legal move
            }
        }
    }
    
    return true; // No legal moves, checkmate!
}
```

**Algorithm:**
1. Verify king is in check
2. Try all possible moves for all pieces
3. For each move, simulate and check if king still in check
4. If no legal move found, it's checkmate

**Time Complexity:** O(n × m) where n = pieces, m = average moves per piece

---

## 🎯 Key Features

### 1. **Position & Notation**
- Algebraic notation: a1-h8
- Row: 0-7 (1-8 in chess)
- Col: 0-7 (a-h in chess)

### 2. **Move Validation**
- Piece-specific rules
- Path checking (for sliding pieces)
- Capture validation
- Self-check prevention

### 3. **Special Moves**
- **Castling:** King and rook special move
- **En Passant:** Pawn captures pawn
- **Pawn Promotion:** Auto-promote to queen

### 4. **Game States**
- Active: Normal play
- Check: King under attack
- Checkmate: Game over
- Stalemate: Draw

### 5. **Undo/Redo**
- Command pattern implementation
- Full state restoration
- Move history tracking

---

## 🧪 Test Scenarios Covered

### ✅ Scenario 1: Basic Pawn Moves
- Pawn forward 1 square
- Pawn forward 2 squares from start
- Turn alternation

### ✅ Scenario 2: Pawn Capture
- Diagonal capture
- Piece removal

### ✅ Scenario 3: Knight Movement
- L-shape moves
- Jumping over pieces

### ✅ Scenario 4: Bishop and Queen
- Diagonal movement
- Multi-directional queen

### ✅ Scenario 5: Check Detection
- King under attack
- Check notification

### ✅ Scenario 6: Scholar's Mate (Checkmate!)
- 4-move checkmate
- Checkmate detection
- Game over

### ✅ Scenario 7: Undo/Redo
- Move history
- State restoration
- Command pattern

---

## 📊 Design Decisions & Trade-offs

### 1. **Board Representation: 2D Array vs Bitboards**

**Decision:** 2D Array (Piece[][])

**Pros:**
- Simple and intuitive
- Easy to understand
- Direct piece access

**Cons:**
- More memory
- Slower for some operations

**Alternative:** Bitboards (64-bit integers)
- Faster for move generation
- More complex to implement
- Better for AI/engines

---

### 2. **Move Validation: Centralized vs Distributed**

**Decision:** Distributed (each piece validates own moves)

**Pros:**
- Clear separation of concerns
- Easy to add new pieces
- Piece-specific logic encapsulated

**Cons:**
- Need to pass board to each piece
- Slightly more method calls

---

### 3. **Undo/Redo: Command Pattern vs State Snapshots**

**Decision:** Command Pattern

**Pros:**
- Memory efficient
- Only stores move deltas
- Fast undo/redo

**Cons:**
- More complex implementation
- Need to track all state changes

**Alternative:** State Snapshots
- Simple: just copy board
- More memory (64 pieces × history)
- Slower for large histories

---

## 🚀 Extensions & Improvements

### 1. **AI Opponent**
```java
public class ChessAI {
    public Move getBestMove(Board board, Color color) {
        // Minimax with alpha-beta pruning
        return minimax(board, depth, alpha, beta, color);
    }
    
    private int evaluate(Board board) {
        // Material count + position evaluation
    }
}
```

### 2. **Move Notation (PGN)**
```java
public String toAlgebraicNotation(Move move) {
    // e4, Nf3, Qxf7+, O-O, etc.
}
```

### 3. **Time Control**
```java
public class ChessClock {
    private long whiteTime;
    private long blackTime;
    private long increment;
    
    public void startTimer(Color player) {
        // Track time per move
    }
}
```

### 4. **Draw Conditions**
```java
public boolean isDrawByRepetition() {
    // Three-fold repetition
}

public boolean isDrawByFiftyMoveRule() {
    // 50 moves without capture or pawn move
}

public boolean isDrawByInsufficientMaterial() {
    // K vs K, KB vs K, KN vs K
}
```

---

## ✅ Success Metrics

### Code Quality
- ✅ Compiles without errors
- ✅ Runs all 7 scenarios successfully
- ✅ Proper package structure
- ✅ Clear naming conventions

### Design Patterns
- ✅ Factory Pattern for piece creation
- ✅ Command Pattern for undo/redo
- ✅ State Pattern for game states

### Chess Rules
- ✅ All 6 piece types implemented correctly
- ✅ Move validation working
- ✅ Check detection working
- ✅ Checkmate detection working
- ✅ Castling support
- ✅ En passant support
- ✅ Pawn promotion support

### Features
- ✅ Algebraic notation
- ✅ Board display
- ✅ Turn management
- ✅ Undo/redo functionality
- ✅ Scholar's Mate demo

---

## 📚 Learning Outcomes

After studying this solution, you should understand:

1. **Factory Pattern:** Centralized object creation
2. **Command Pattern:** Undo/redo implementation
3. **State Pattern:** Game state management
4. **Chess Rules:** All piece movements
5. **Check Detection:** King safety validation
6. **Checkmate Algorithm:** No legal moves detection
7. **Board Representation:** 2D array approach

---

## 🎓 Interview Tips

### Common Questions:

**Q: How do you detect checkmate?**
A: Check if king is in check AND no legal moves exist for any piece.

**Q: How does undo/redo work?**
A: Command pattern - each move is a command that can execute() and undo().

**Q: How to optimize move generation?**
A: Use bitboards, magic bitboards for sliding pieces, precomputed move tables.

**Q: How to add AI?**
A: Minimax algorithm with alpha-beta pruning and position evaluation.

**Q: How to prevent illegal moves?**
A: Simulate move, check if own king in check, undo simulation.

---

**Total Lines of Code:** ~3,000 lines  
**Files:** 19 Java files  
**Patterns:** 3 design patterns  
**Scenarios:** 7 comprehensive test cases  

**Time to Complete:** 90 minutes (interview setting)

---

*Created: January 2026*  
*Problem: Online Chess Game - Hard*  
*Patterns: Factory, Command, State*
