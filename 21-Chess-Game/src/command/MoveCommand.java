package command;

import model.*;
import enums.PieceType;

public class MoveCommand implements Command {
    private Board board;
    private Piece piece;
    private Position from;
    private Position to;
    private Piece capturedPiece;
    private boolean wasFirstMove;
    private Position oldEnPassantTarget;
    private Position enPassantCapturePos;
    private boolean isPromotion;
    private PieceType promotionType;
    
    public MoveCommand(Board board, Piece piece, Position from, Position to) {
        this.board = board;
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.wasFirstMove = !piece.hasMoved();
        this.oldEnPassantTarget = board.getEnPassantTarget();
        this.isPromotion = false;
    }
    
    public void setPromotion(PieceType promotionType) {
        this.isPromotion = true;
        this.promotionType = promotionType;
    }
    
    @Override
    public void execute() {
        // Save captured piece
        capturedPiece = board.getPiece(to);
        
        // Handle en passant capture
        if (piece.getType() == PieceType.PAWN && to.equals(board.getEnPassantTarget())) {
            int captureRow = (piece.getColor() == enums.Color.WHITE) ? to.getRow() - 1 : to.getRow() + 1;
            enPassantCapturePos = new Position(captureRow, to.getCol());
            capturedPiece = board.getPiece(enPassantCapturePos);
            board.setPiece(enPassantCapturePos, null);
        }
        
        // Move piece
        board.setPiece(to, piece);
        board.setPiece(from, null);
        piece.setPosition(to);
        piece.setHasMoved(true);
        
        // Set en passant target if pawn moved 2 squares
        if (piece.getType() == PieceType.PAWN && Math.abs(to.getRow() - from.getRow()) == 2) {
            int enPassantRow = (from.getRow() + to.getRow()) / 2;
            board.setEnPassantTarget(new Position(enPassantRow, from.getCol()));
        } else {
            board.setEnPassantTarget(null);
        }
        
        // Handle pawn promotion
        if (isPromotion && piece.getType() == PieceType.PAWN) {
            int promotionRow = (piece.getColor() == enums.Color.WHITE) ? 7 : 0;
            if (to.getRow() == promotionRow) {
                Piece promotedPiece = factory.PieceFactory.createPiece(promotionType, piece.getColor(), to);
                promotedPiece.setHasMoved(true);
                board.setPiece(to, promotedPiece);
            }
        }
    }
    
    @Override
    public void undo() {
        // Move piece back
        board.setPiece(from, piece);
        board.setPiece(to, capturedPiece);
        piece.setPosition(from);
        
        // Restore hasMoved flag
        if (wasFirstMove) {
            piece.setHasMoved(false);
        }
        
        // Restore en passant capture
        if (enPassantCapturePos != null) {
            board.setPiece(enPassantCapturePos, capturedPiece);
            board.setPiece(to, null);
        }
        
        // Restore en passant target
        board.setEnPassantTarget(oldEnPassantTarget);
    }
    
    public Piece getPiece() { return piece; }
    public Position getFrom() { return from; }
    public Position getTo() { return to; }
    public Piece getCapturedPiece() { return capturedPiece; }
}
