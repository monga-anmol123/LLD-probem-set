package model;

import enums.Color;
import enums.PieceType;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    
    public Pawn(Color color, Position position) {
        super(color, position, PieceType.PAWN);
    }
    
    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        int direction = (color == Color.WHITE) ? 1 : -1;
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();
        
        // Forward 1 square
        if (colDiff == 0 && rowDiff == direction) {
            return board.getPiece(to) == null;
        }
        
        // Forward 2 squares from starting position
        if (!hasMoved && colDiff == 0 && rowDiff == 2 * direction) {
            Position middle = new Position(from.getRow() + direction, from.getCol());
            return board.getPiece(middle) == null && board.getPiece(to) == null;
        }
        
        // Diagonal capture
        if (Math.abs(colDiff) == 1 && rowDiff == direction) {
            Piece target = board.getPiece(to);
            if (target != null && target.getColor() != color) {
                return true;
            }
            
            // En passant
            if (board.getEnPassantTarget() != null && board.getEnPassantTarget().equals(to)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int direction = (color == Color.WHITE) ? 1 : -1;
        
        // Forward 1
        Position forward1 = new Position(position.getRow() + direction, position.getCol());
        if (forward1.isValid() && board.getPiece(forward1) == null) {
            moves.add(forward1);
            
            // Forward 2
            if (!hasMoved) {
                Position forward2 = new Position(position.getRow() + 2 * direction, position.getCol());
                if (forward2.isValid() && board.getPiece(forward2) == null) {
                    moves.add(forward2);
                }
            }
        }
        
        // Diagonal captures
        int[] colOffsets = {-1, 1};
        for (int colOffset : colOffsets) {
            Position diagonal = new Position(position.getRow() + direction, position.getCol() + colOffset);
            if (diagonal.isValid()) {
                Piece target = board.getPiece(diagonal);
                if ((target != null && target.getColor() != color) ||
                    (board.getEnPassantTarget() != null && board.getEnPassantTarget().equals(diagonal))) {
                    moves.add(diagonal);
                }
            }
        }
        
        return moves;
    }
}
