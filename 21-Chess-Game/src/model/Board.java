package model;

import enums.Color;
import enums.PieceType;
import factory.PieceFactory;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] squares;
    private Position enPassantTarget;
    
    public Board() {
        squares = new Piece[8][8];
        enPassantTarget = null;
    }
    
    public void initializeStandardSetup() {
        // White pieces (row 0-1)
        squares[0][0] = PieceFactory.createPiece(PieceType.ROOK, Color.WHITE, new Position(0, 0));
        squares[0][1] = PieceFactory.createPiece(PieceType.KNIGHT, Color.WHITE, new Position(0, 1));
        squares[0][2] = PieceFactory.createPiece(PieceType.BISHOP, Color.WHITE, new Position(0, 2));
        squares[0][3] = PieceFactory.createPiece(PieceType.QUEEN, Color.WHITE, new Position(0, 3));
        squares[0][4] = PieceFactory.createPiece(PieceType.KING, Color.WHITE, new Position(0, 4));
        squares[0][5] = PieceFactory.createPiece(PieceType.BISHOP, Color.WHITE, new Position(0, 5));
        squares[0][6] = PieceFactory.createPiece(PieceType.KNIGHT, Color.WHITE, new Position(0, 6));
        squares[0][7] = PieceFactory.createPiece(PieceType.ROOK, Color.WHITE, new Position(0, 7));
        
        for (int col = 0; col < 8; col++) {
            squares[1][col] = PieceFactory.createPiece(PieceType.PAWN, Color.WHITE, new Position(1, col));
        }
        
        // Black pieces (row 6-7)
        for (int col = 0; col < 8; col++) {
            squares[6][col] = PieceFactory.createPiece(PieceType.PAWN, Color.BLACK, new Position(6, col));
        }
        
        squares[7][0] = PieceFactory.createPiece(PieceType.ROOK, Color.BLACK, new Position(7, 0));
        squares[7][1] = PieceFactory.createPiece(PieceType.KNIGHT, Color.BLACK, new Position(7, 1));
        squares[7][2] = PieceFactory.createPiece(PieceType.BISHOP, Color.BLACK, new Position(7, 2));
        squares[7][3] = PieceFactory.createPiece(PieceType.QUEEN, Color.BLACK, new Position(7, 3));
        squares[7][4] = PieceFactory.createPiece(PieceType.KING, Color.BLACK, new Position(7, 4));
        squares[7][5] = PieceFactory.createPiece(PieceType.BISHOP, Color.BLACK, new Position(7, 5));
        squares[7][6] = PieceFactory.createPiece(PieceType.KNIGHT, Color.BLACK, new Position(7, 6));
        squares[7][7] = PieceFactory.createPiece(PieceType.ROOK, Color.BLACK, new Position(7, 7));
    }
    
    public Piece getPiece(Position position) {
        if (!position.isValid()) return null;
        return squares[position.getRow()][position.getCol()];
    }
    
    public void setPiece(Position position, Piece piece) {
        if (position.isValid()) {
            squares[position.getRow()][position.getCol()] = piece;
        }
    }
    
    public List<Piece> getAllPieces() {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (squares[row][col] != null) {
                    pieces.add(squares[row][col]);
                }
            }
        }
        return pieces;
    }
    
    public List<Piece> getPieces(Color color) {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col];
                if (piece != null && piece.getColor() == color) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }
    
    public Position findKing(Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col];
                if (piece != null && piece.getType() == PieceType.KING && piece.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }
    
    public void display() {
        System.out.println("\n  ┌─────────────────────────────────────────────────┐");
        
        for (int row = 7; row >= 0; row--) {
            System.out.print((row + 1) + " │");
            
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col];
                if (piece == null) {
                    // Checkerboard pattern
                    if ((row + col) % 2 == 0) {
                        System.out.print("     ");
                    } else {
                        System.out.print(" ░░░ ");
                    }
                } else {
                    String symbol = piece.getSymbol();
                    if (piece.getColor() == Color.WHITE) {
                        System.out.print("  " + symbol + "  ");
                    } else {
                        System.out.print("  " + symbol + "  ");
                    }
                }
                System.out.print("│");
            }
            
            System.out.println();
            if (row > 0) {
                System.out.println("  ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤");
            }
        }
        
        System.out.println("  └─────────────────────────────────────────────────┘");
        System.out.println("     a     b     c     d     e     f     g     h");
    }
    
    public Position getEnPassantTarget() {
        return enPassantTarget;
    }
    
    public void setEnPassantTarget(Position position) {
        this.enPassantTarget = position;
    }
    
    public Board copy() {
        Board newBoard = new Board();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col];
                if (piece != null) {
                    Position pos = new Position(row, col);
                    Piece newPiece = PieceFactory.createPiece(piece.getType(), piece.getColor(), pos);
                    newPiece.setHasMoved(piece.hasMoved());
                    newBoard.setPiece(pos, newPiece);
                }
            }
        }
        newBoard.enPassantTarget = this.enPassantTarget;
        return newBoard;
    }
}
