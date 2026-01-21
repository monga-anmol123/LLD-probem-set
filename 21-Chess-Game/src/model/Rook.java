package model;

import enums.Color;
import enums.PieceType;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    
    public Rook(Color color, Position position) {
        super(color, position, PieceType.ROOK);
    }
    
    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        // Must move horizontally or vertically
        if (from.getRow() != to.getRow() && from.getCol() != to.getCol()) {
            return false;
        }
        
        // Path must be clear
        if (!isPathClear(from, to, board)) {
            return false;
        }
        
        // Target square must be empty or contain opponent piece
        Piece target = board.getPiece(to);
        return target == null || target.getColor() != color;
    }
    
    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        
        // Horizontal and vertical directions
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        
        for (int[] dir : directions) {
            int row = position.getRow();
            int col = position.getCol();
            
            while (true) {
                row += dir[0];
                col += dir[1];
                
                Position newPos = new Position(row, col);
                if (!newPos.isValid()) break;
                
                Piece piece = board.getPiece(newPos);
                if (piece == null) {
                    moves.add(newPos);
                } else {
                    if (piece.getColor() != color) {
                        moves.add(newPos);
                    }
                    break;
                }
            }
        }
        
        return moves;
    }
}
