package model;

import enums.Color;
import enums.PieceType;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    protected Color color;
    protected Position position;
    protected PieceType type;
    protected boolean hasMoved;
    
    public Piece(Color color, Position position, PieceType type) {
        this.color = color;
        this.position = position;
        this.type = type;
        this.hasMoved = false;
    }
    
    public abstract boolean isValidMove(Position from, Position to, Board board);
    
    public abstract List<Position> getPossibleMoves(Board board);
    
    protected boolean isPathClear(Position from, Position to, Board board) {
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();
        
        int rowDir = rowDiff == 0 ? 0 : rowDiff / Math.abs(rowDiff);
        int colDir = colDiff == 0 ? 0 : colDiff / Math.abs(colDiff);
        
        int currentRow = from.getRow() + rowDir;
        int currentCol = from.getCol() + colDir;
        
        while (currentRow != to.getRow() || currentCol != to.getCol()) {
            if (board.getPiece(new Position(currentRow, currentCol)) != null) {
                return false;
            }
            currentRow += rowDir;
            currentCol += colDir;
        }
        
        return true;
    }
    
    public String getSymbol() {
        String symbol = type.getSymbol();
        return color == Color.WHITE ? symbol : symbol.toLowerCase();
    }
    
    // Getters and Setters
    public Color getColor() { return color; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public PieceType getType() { return type; }
    public boolean hasMoved() { return hasMoved; }
    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }
    
    @Override
    public String toString() {
        return color + " " + type + " at " + position;
    }
}
