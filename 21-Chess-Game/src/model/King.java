package model;

import enums.Color;
import enums.PieceType;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    
    public King(Color color, Position position) {
        super(color, position, PieceType.KING);
    }
    
    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        // Normal king move: one square in any direction
        if (rowDiff <= 1 && colDiff <= 1 && (rowDiff + colDiff > 0)) {
            Piece target = board.getPiece(to);
            return target == null || target.getColor() != color;
        }
        
        // Castling: king moves 2 squares horizontally
        if (!hasMoved && rowDiff == 0 && colDiff == 2) {
            return canCastle(from, to, board);
        }
        
        return false;
    }
    
    private boolean canCastle(Position from, Position to, Board board) {
        // Determine if kingside or queenside
        boolean isKingside = to.getCol() > from.getCol();
        int rookCol = isKingside ? 7 : 0;
        Position rookPos = new Position(from.getRow(), rookCol);
        
        Piece rook = board.getPiece(rookPos);
        if (rook == null || rook.getType() != PieceType.ROOK || rook.hasMoved()) {
            return false;
        }
        
        // Path must be clear between king and rook
        int startCol = Math.min(from.getCol(), rookCol);
        int endCol = Math.max(from.getCol(), rookCol);
        
        for (int col = startCol + 1; col < endCol; col++) {
            if (board.getPiece(new Position(from.getRow(), col)) != null) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        
        // All 8 adjacent squares
        int[][] offsets = {
            {0, 1}, {0, -1}, {1, 0}, {-1, 0},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
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
        
        // Castling moves
        if (!hasMoved) {
            // Kingside
            Position kingsideCastle = new Position(position.getRow(), position.getCol() + 2);
            if (kingsideCastle.isValid() && canCastle(position, kingsideCastle, board)) {
                moves.add(kingsideCastle);
            }
            
            // Queenside
            Position queensideCastle = new Position(position.getRow(), position.getCol() - 2);
            if (queensideCastle.isValid() && canCastle(position, queensideCastle, board)) {
                moves.add(queensideCastle);
            }
        }
        
        return moves;
    }
}
