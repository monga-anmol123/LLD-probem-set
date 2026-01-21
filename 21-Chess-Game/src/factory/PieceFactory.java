package factory;

import model.*;
import enums.Color;
import enums.PieceType;

public class PieceFactory {
    
    public static Piece createPiece(PieceType type, Color color, Position position) {
        switch (type) {
            case PAWN:
                return new Pawn(color, position);
            case ROOK:
                return new Rook(color, position);
            case KNIGHT:
                return new Knight(color, position);
            case BISHOP:
                return new Bishop(color, position);
            case QUEEN:
                return new Queen(color, position);
            case KING:
                return new King(color, position);
            default:
                throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }
}
