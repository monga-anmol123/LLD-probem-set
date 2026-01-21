package model;

import enums.Color;
import enums.PieceType;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    
    public Knight(Color color, Position position) {
        super(color, position, PieceType.KNIGHT);
    }
    
    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        // L-shape: 2+1 or 1+2
        if (!((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2))) {
            return false;
        }
        
        // Target square must be empty or contain opponent piece
        Piece target = board.getPiece(to);
        return target == null || target.getColor() != color;
    }
    
    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        
        // All 8 possible knight moves
        int[][] offsets = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
        
        for (int[] offset : offsets) {
            Position newPos = new Position(
                position.getRow() + offset[0],
                position.getCol() + offset[1]
            );
            
            if (newPos.isValid()) {
                Piece piece = board.getPiece(newPos);
                if (piece == null || piece.getColor() != color) {
                    moves.add(newPos);
                }
            }
        }
        
        return moves;
    }
}
