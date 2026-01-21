package command;

import model.*;

public class CastlingCommand implements Command {
    private Board board;
    private Piece king;
    private Piece rook;
    private Position kingFrom;
    private Position kingTo;
    private Position rookFrom;
    private Position rookTo;
    private boolean wasKingFirstMove;
    private boolean wasRookFirstMove;
    
    public CastlingCommand(Board board, Piece king, Position kingFrom, Position kingTo) {
        this.board = board;
        this.king = king;
        this.kingFrom = kingFrom;
        this.kingTo = kingTo;
        this.wasKingFirstMove = !king.hasMoved();
        
        // Determine rook position
        boolean isKingside = kingTo.getCol() > kingFrom.getCol();
        int rookCol = isKingside ? 7 : 0;
        int rookToCol = isKingside ? kingTo.getCol() - 1 : kingTo.getCol() + 1;
        
        this.rookFrom = new Position(kingFrom.getRow(), rookCol);
        this.rookTo = new Position(kingFrom.getRow(), rookToCol);
        this.rook = board.getPiece(rookFrom);
        this.wasRookFirstMove = !rook.hasMoved();
    }
    
    @Override
    public void execute() {
        // Move king
        board.setPiece(kingTo, king);
        board.setPiece(kingFrom, null);
        king.setPosition(kingTo);
        king.setHasMoved(true);
        
        // Move rook
        board.setPiece(rookTo, rook);
        board.setPiece(rookFrom, null);
        rook.setPosition(rookTo);
        rook.setHasMoved(true);
    }
    
    @Override
    public void undo() {
        // Move king back
        board.setPiece(kingFrom, king);
        board.setPiece(kingTo, null);
        king.setPosition(kingFrom);
        if (wasKingFirstMove) {
            king.setHasMoved(false);
        }
        
        // Move rook back
        board.setPiece(rookFrom, rook);
        board.setPiece(rookTo, null);
        rook.setPosition(rookFrom);
        if (wasRookFirstMove) {
            rook.setHasMoved(false);
        }
    }
}
