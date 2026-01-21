package model;

import enums.Color;
import enums.GameState;
import enums.PieceType;
import command.*;
import java.util.*;

public class Game {
    private Board board;
    private Color currentPlayer;
    private GameState gameState;
    private Stack<Command> moveHistory;
    private Stack<Command> redoStack;
    private int moveCount;
    
    public Game() {
        board = new Board();
        board.initializeStandardSetup();
        currentPlayer = Color.WHITE;
        gameState = GameState.ACTIVE;
        moveHistory = new Stack<>();
        redoStack = new Stack<>();
        moveCount = 0;
    }
    
    public boolean makeMove(String fromNotation, String toNotation) {
        try {
            Position from = Position.fromNotation(fromNotation);
            Position to = Position.fromNotation(toNotation);
            return makeMove(from, to);
        } catch (Exception e) {
            System.out.println("❌ Invalid move notation: " + e.getMessage());
            return false;
        }
    }
    
    public boolean makeMove(Position from, Position to) {
        Piece piece = board.getPiece(from);
        
        // Validate piece exists and belongs to current player
        if (piece == null) {
            System.out.println("❌ No piece at " + from);
            return false;
        }
        
        if (piece.getColor() != currentPlayer) {
            System.out.println("❌ Not your piece! Current player: " + currentPlayer);
            return false;
        }
        
        // Check if move is valid for the piece
        if (!piece.isValidMove(from, to, board)) {
            System.out.println("❌ Invalid move for " + piece.getType());
            return false;
        }
        
        // Check if move leaves own king in check
        if (wouldLeaveKingInCheck(piece, from, to)) {
            System.out.println("❌ Move would leave king in check!");
            return false;
        }
        
        // Execute move
        Command command;
        
        // Check for castling
        if (piece.getType() == PieceType.KING && Math.abs(to.getCol() - from.getCol()) == 2) {
            command = new CastlingCommand(board, piece, from, to);
        } else {
            command = new MoveCommand(board, piece, from, to);
            
            // Check for pawn promotion
            if (piece.getType() == PieceType.PAWN) {
                int promotionRow = (piece.getColor() == Color.WHITE) ? 7 : 0;
                if (to.getRow() == promotionRow) {
                    ((MoveCommand) command).setPromotion(PieceType.QUEEN); // Auto-promote to queen
                }
            }
        }
        
        command.execute();
        moveHistory.push(command);
        redoStack.clear(); // Clear redo stack after new move
        moveCount++;
        
        // Switch player
        currentPlayer = currentPlayer.opposite();
        
        // Update game state
        updateGameState();
        
        return true;
    }
    
    private boolean wouldLeaveKingInCheck(Piece piece, Position from, Position to) {
        // Simulate move
        Piece captured = board.getPiece(to);
        board.setPiece(to, piece);
        board.setPiece(from, null);
        Position oldPos = piece.getPosition();
        piece.setPosition(to);
        
        boolean inCheck = isKingInCheck(piece.getColor());
        
        // Undo simulation
        board.setPiece(from, piece);
        board.setPiece(to, captured);
        piece.setPosition(oldPos);
        
        return inCheck;
    }
    
    public boolean isKingInCheck(Color kingColor) {
        Position kingPos = board.findKing(kingColor);
        if (kingPos == null) return false;
        
        // Check if any opponent piece can attack king
        for (Piece piece : board.getPieces(kingColor.opposite())) {
            if (piece.isValidMove(piece.getPosition(), kingPos, board)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean hasLegalMoves(Color color) {
        for (Piece piece : board.getPieces(color)) {
            for (Position to : piece.getPossibleMoves(board)) {
                if (!wouldLeaveKingInCheck(piece, piece.getPosition(), to)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void updateGameState() {
        boolean inCheck = isKingInCheck(currentPlayer);
        boolean hasLegalMoves = hasLegalMoves(currentPlayer);
        
        if (inCheck && !hasLegalMoves) {
            gameState = GameState.CHECKMATE;
            System.out.println("\n🎉 CHECKMATE! " + currentPlayer.opposite() + " wins!");
        } else if (!inCheck && !hasLegalMoves) {
            gameState = GameState.STALEMATE;
            System.out.println("\n🤝 STALEMATE! Game is a draw.");
        } else if (inCheck) {
            gameState = GameState.CHECK;
            System.out.println("\n⚠️  CHECK! " + currentPlayer + " king is under attack.");
        } else {
            gameState = GameState.ACTIVE;
        }
    }
    
    public boolean undo() {
        if (moveHistory.isEmpty()) {
            System.out.println("❌ No moves to undo");
            return false;
        }
        
        Command command = moveHistory.pop();
        command.undo();
        redoStack.push(command);
        
        currentPlayer = currentPlayer.opposite();
        moveCount--;
        
        updateGameState();
        
        System.out.println("✓ Move undone");
        return true;
    }
    
    public boolean redo() {
        if (redoStack.isEmpty()) {
            System.out.println("❌ No moves to redo");
            return false;
        }
        
        Command command = redoStack.pop();
        command.execute();
        moveHistory.push(command);
        
        currentPlayer = currentPlayer.opposite();
        moveCount++;
        
        updateGameState();
        
        System.out.println("✓ Move redone");
        return true;
    }
    
    public void displayBoard() {
        board.display();
    }
    
    public void displayStatus() {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("  Current Player: " + currentPlayer);
        System.out.println("  Game State: " + gameState);
        System.out.println("  Move Count: " + moveCount);
        System.out.println("═══════════════════════════════════════════════════════");
    }
    
    public List<Position> getLegalMoves(Position from) {
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != currentPlayer) {
            return new ArrayList<>();
        }
        
        List<Position> legalMoves = new ArrayList<>();
        for (Position to : piece.getPossibleMoves(board)) {
            if (!wouldLeaveKingInCheck(piece, from, to)) {
                legalMoves.add(to);
            }
        }
        
        return legalMoves;
    }
    
    // Getters
    public Board getBoard() { return board; }
    public Color getCurrentPlayer() { return currentPlayer; }
    public GameState getGameState() { return gameState; }
    public int getMoveCount() { return moveCount; }
    public boolean isGameOver() {
        return gameState == GameState.CHECKMATE || 
               gameState == GameState.STALEMATE || 
               gameState == GameState.DRAW;
    }
}
