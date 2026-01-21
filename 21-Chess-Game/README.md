# Problem 21: Online Chess Game

## 🎯 Difficulty: Hard ⭐⭐⭐⭐

## 📝 Problem Statement

Design a complete chess game system that supports all standard chess rules including move validation for all pieces, check/checkmate/stalemate detection, special moves (castling, en passant, pawn promotion), and undo/redo functionality using the Command pattern.

---

## 🔍 Functional Requirements (FR)

### FR1: Board & Position Management
- 8x8 chess board representation
- Position notation (algebraic: a1-h8)
- Display board with pieces
- Track piece positions

### FR2: Piece Management
- Support all 6 piece types: Pawn, Rook, Knight, Bishop, Queen, King
- Each piece has color (White/Black)
- Each piece has unique movement rules
- Factory pattern for piece creation

### FR3: Move Validation
- **Pawn:** Forward 1 (or 2 from start), diagonal capture
- **Rook:** Horizontal/vertical any distance
- **Knight:** L-shape (2+1 or 1+2)
- **Bishop:** Diagonal any distance
- **Queen:** Horizontal/vertical/diagonal any distance
- **King:** One square in any direction
- Validate moves don't go off board
- Validate path is clear (except knight)
- Validate not capturing own pieces

### FR4: Special Moves
- **Castling:** King and rook special move
  - King hasn't moved
  - Rook hasn't moved
  - No pieces between them
  - King not in check
  - King doesn't pass through check
- **En Passant:** Pawn captures pawn that just moved 2 squares
- **Pawn Promotion:** Pawn reaching opposite end becomes Queen/Rook/Bishop/Knight

### FR5: Check Detection
- Detect when king is under attack
- Validate moves don't leave own king in check
- Display check status

### FR6: Game End Conditions
- **Checkmate:** King in check, no legal moves
- **Stalemate:** Not in check, no legal moves
- **Draw:** Insufficient material, repetition, 50-move rule

### FR7: Turn Management
- Alternate turns (White, Black)
- Validate correct player moves
- Track move history

### FR8: Undo/Redo (Command Pattern)
- Undo last move
- Redo undone move
- Maintain move history stack
- Restore board state

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Performance
- Move validation: O(1) for most pieces, O(n) for sliding pieces
- Check detection: O(n) where n = number of pieces
- Board display: O(64) = O(1)

### NFR2: Extensibility
- Easy to add new piece types
- Easy to add new game variants (Chess960, etc.)
- Pluggable move validators

### NFR3: Correctness
- All chess rules correctly implemented
- No illegal moves allowed
- Accurate check/checkmate detection

### NFR4: Usability
- Clear board display
- Algebraic notation support
- Move history tracking
- Error messages for invalid moves

---

## 🎨 Design Patterns to Use

### 1. **Factory Pattern**
- **Where:** Piece creation
- **Why:** Centralize piece instantiation, support different piece types

### 2. **State Pattern**
- **Where:** Game states (Active, Check, Checkmate, Stalemate)
- **Why:** Different behavior based on game state

### 3. **Command Pattern**
- **Where:** Move execution, undo/redo
- **Why:** Encapsulate moves as objects, support undo/redo

### 4. **Strategy Pattern** (Optional)
- **Where:** Move validation strategies per piece
- **Why:** Each piece has different movement rules

---

## 📋 Core Entities

### 1. **Position**
- Attributes: `row` (0-7), `col` (0-7)
- Methods: `fromNotation("e4")`, `toNotation()`, `isValid()`
- Algebraic notation: a1-h8

### 2. **Piece** (Abstract)
- Attributes: `color`, `position`, `hasMoved`
- Methods: `isValidMove(from, to, board)`, `getPossibleMoves(board)`
- Subclasses: Pawn, Rook, Knight, Bishop, Queen, King

### 3. **Board**
- Attributes: `squares` (8x8 array of Piece)
- Methods: `getPiece(position)`, `setPiece(position, piece)`, `display()`, `isPathClear(from, to)`

### 4. **Move** (Command)
- Attributes: `piece`, `from`, `to`, `capturedPiece`, `specialMove`
- Methods: `execute()`, `undo()`
- Implements Command pattern

### 5. **Game**
- Attributes: `board`, `currentPlayer`, `gameState`, `moveHistory`, `undoStack`, `redoStack`
- Methods: `makeMove(from, to)`, `undo()`, `redo()`, `isCheck()`, `isCheckmate()`, `isStalemate()`

### 6. **GameState** (Enum/State)
- Values: ACTIVE, CHECK, CHECKMATE, STALEMATE, DRAW

---

## 🧪 Test Scenarios

### Scenario 1: Basic Moves
```
1. e2 to e4 (pawn 2 squares)
2. e7 to e5 (pawn 2 squares)
3. Nf3 (knight move)
4. Nc6 (knight move)
```

### Scenario 2: Capture
```
1. e4
2. d5
3. exd5 (pawn captures pawn)
```

### Scenario 3: Castling
```
1. e4, e5
2. Nf3, Nc6
3. Bc4, Bc5
4. O-O (kingside castling)
```

### Scenario 4: En Passant
```
1. e4, a6
2. e5, d5
3. exd6 (en passant capture)
```

### Scenario 5: Pawn Promotion
```
Move pawn to 8th rank
Promote to Queen
```

### Scenario 6: Check Detection
```
Move piece to attack king
Verify check is detected
Verify only legal moves shown
```

### Scenario 7: Scholar's Mate (Checkmate in 4 moves)
```
1. e4, e5
2. Bc4, Nc6
3. Qh5, Nf6
4. Qxf7# (checkmate!)
```

### Scenario 8: Stalemate
```
Create position where king not in check
But has no legal moves
Verify stalemate detected
```

### Scenario 9: Undo/Redo
```
Make several moves
Undo moves
Redo moves
Verify board state restored
```

---

## ⏱️ Time Allocation (90 minutes)

- **10 mins:** Clarify requirements, understand chess rules
- **10 mins:** Design entities (Piece, Board, Position, Move)
- **10 mins:** Design move validation logic
- **20 mins:** Implement pieces and move validation
- **15 mins:** Implement check/checkmate detection
- **10 mins:** Implement special moves (castling, en passant)
- **10 mins:** Implement Command pattern (undo/redo)
- **5 mins:** Test with scenarios

---

## 💡 Hints

<details>
<summary>Hint 1: Position Class</summary>

```java
public class Position {
    private int row; // 0-7
    private int col; // 0-7
    
    public static Position fromNotation(String notation) {
        // "e4" -> col=4, row=3
        char file = notation.charAt(0); // a-h
        char rank = notation.charAt(1); // 1-8
        
        int col = file - 'a'; // 0-7
        int row = rank - '1'; // 0-7
        
        return new Position(row, col);
    }
    
    public String toNotation() {
        char file = (char)('a' + col);
        char rank = (char)('1' + row);
        return "" + file + rank;
    }
}
```
</details>

<details>
<summary>Hint 2: Move Validation Structure</summary>

```java
public abstract class Piece {
    protected Color color;
    protected Position position;
    protected boolean hasMoved;
    
    public abstract boolean isValidMove(Position from, Position to, Board board);
    
    public abstract List<Position> getPossibleMoves(Board board);
}

public class Pawn extends Piece {
    public boolean isValidMove(Position from, Position to, Board board) {
        int direction = (color == Color.WHITE) ? 1 : -1;
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        // Forward 1
        if (colDiff == 0 && rowDiff == direction) {
            return board.getPiece(to) == null;
        }
        
        // Forward 2 from start
        if (!hasMoved && colDiff == 0 && rowDiff == 2 * direction) {
            Position middle = new Position(from.getRow() + direction, from.getCol());
            return board.getPiece(middle) == null && board.getPiece(to) == null;
        }
        
        // Diagonal capture
        if (colDiff == 1 && rowDiff == direction) {
            Piece target = board.getPiece(to);
            return target != null && target.getColor() != color;
        }
        
        return false;
    }
}
```
</details>

<details>
<summary>Hint 3: Check Detection</summary>

```java
public boolean isKingInCheck(Color kingColor, Board board) {
    // Find king position
    Position kingPos = findKing(kingColor, board);
    
    // Check if any opponent piece can attack king
    for (Piece piece : board.getAllPieces()) {
        if (piece.getColor() != kingColor) {
            if (piece.isValidMove(piece.getPosition(), kingPos, board)) {
                return true;
            }
        }
    }
    
    return false;
}
```
</details>

<details>
<summary>Hint 4: Command Pattern for Moves</summary>

```java
public class MoveCommand implements Command {
    private Piece piece;
    private Position from;
    private Position to;
    private Piece capturedPiece;
    private Board board;
    
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
        // Restore hasMoved flag if needed
    }
}
```
</details>

<details>
<summary>Hint 5: Checkmate Detection</summary>

```java
public boolean isCheckmate(Color kingColor, Board board) {
    // Must be in check
    if (!isKingInCheck(kingColor, board)) {
        return false;
    }
    
    // Check if any legal move exists
    for (Piece piece : board.getPieces(kingColor)) {
        for (Position to : piece.getPossibleMoves(board)) {
            // Try move
            if (isLegalMove(piece, piece.getPosition(), to, board)) {
                return false; // Found legal move
            }
        }
    }
    
    return true; // No legal moves, checkmate!
}

public boolean isLegalMove(Piece piece, Position from, Position to, Board board) {
    // Move must be valid for piece
    if (!piece.isValidMove(from, to, board)) {
        return false;
    }
    
    // Simulate move
    Piece captured = board.getPiece(to);
    board.setPiece(to, piece);
    board.setPiece(from, null);
    
    // Check if own king in check
    boolean inCheck = isKingInCheck(piece.getColor(), board);
    
    // Undo simulation
    board.setPiece(from, piece);
    board.setPiece(to, captured);
    
    return !inCheck;
}
```
</details>

<details>
<summary>Hint 6: Castling Validation</summary>

```java
public boolean canCastle(King king, Rook rook, Board board) {
    // King and rook haven't moved
    if (king.hasMoved() || rook.hasMoved()) {
        return false;
    }
    
    // King not in check
    if (isKingInCheck(king.getColor(), board)) {
        return false;
    }
    
    // Path is clear
    Position kingPos = king.getPosition();
    Position rookPos = rook.getPosition();
    
    if (!board.isPathClear(kingPos, rookPos)) {
        return false;
    }
    
    // King doesn't pass through check
    int direction = (rookPos.getCol() > kingPos.getCol()) ? 1 : -1;
    Position through = new Position(kingPos.getRow(), kingPos.getCol() + direction);
    
    // Simulate king on through square
    board.setPiece(through, king);
    board.setPiece(kingPos, null);
    boolean throughCheck = isKingInCheck(king.getColor(), board);
    board.setPiece(kingPos, king);
    board.setPiece(through, null);
    
    return !throughCheck;
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Chess Clock**
   - Time control per player
   - Increment per move
   - Time forfeit

2. **Move Notation**
   - Standard algebraic notation (SAN)
   - PGN export/import
   - Move annotations

3. **AI Opponent**
   - Minimax algorithm
   - Alpha-beta pruning
   - Position evaluation

4. **Online Multiplayer**
   - Network play
   - Move synchronization
   - Spectator mode

5. **Game Analysis**
   - Move quality evaluation
   - Blunder detection
   - Opening database

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Factory, State, Command patterns correctly
- [ ] Implement all 6 piece types with correct movement
- [ ] Validate all moves (including path checking)
- [ ] Detect check correctly
- [ ] Detect checkmate correctly
- [ ] Detect stalemate correctly
- [ ] Support castling
- [ ] Support en passant
- [ ] Support pawn promotion
- [ ] Implement undo/redo with Command pattern
- [ ] Handle all 9+ test scenarios
- [ ] Display board clearly
- [ ] Use algebraic notation
- [ ] Handle edge cases (off-board, invalid moves, etc.)

---

## 📁 File Structure

```
21-Chess-Game/
├── README.md (this file)
├── SOLUTION.md
├── COMPILATION-GUIDE.md
└── src/
    ├── enums/
    │   ├── Color.java
    │   ├── PieceType.java
    │   └── GameState.java
    ├── model/
    │   ├── Position.java
    │   ├── Piece.java
    │   ├── Pawn.java
    │   ├── Rook.java
    │   ├── Knight.java
    │   ├── Bishop.java
    │   ├── Queen.java
    │   ├── King.java
    │   └── Board.java
    ├── command/
    │   ├── Command.java
    │   ├── MoveCommand.java
    │   └── CastlingCommand.java
    ├── factory/
    │   └── PieceFactory.java
    ├── state/
    │   ├── GameStateManager.java
    │   └── (state classes if needed)
    └── Main.java
```

---

## 🎓 Key Learning Points

1. **Chess Rules:** Understanding all movement and special rules
2. **Move Validation:** Path checking, capture rules
3. **Check Detection:** King safety validation
4. **Command Pattern:** Undo/redo implementation
5. **State Management:** Game state transitions
6. **Algorithm Design:** Checkmate detection algorithm

---

**Good luck! This is one of the most complex LLD problems - focus on correctness of move validation and check detection! ♟️**
