package chess.actions;

import chess.Action;
import chess.Game;
import chess.GamePiece;
import chess.pieces.Rook;

/**
 * Action whereby a King moves 2 squares towards a Rook, and the Rook moves to
 * the square over which the King passed.
 *
 * @author Dan Bryce
 */
public class Castle extends Action {

    private static final int KING_INITIAL_X = 4;
    private static final int KING_DEST_X_EAST = KING_INITIAL_X - 2;
    private static final int KING_DEST_X_WEST = KING_INITIAL_X + 2;

    private static final int ROOK_INITIAL_X_EAST = 0;
    private static final int ROOK_INITIAL_X_WEST = Game.SQUARES_PER_SIDE - 1;
    private static final int ROOK_DEST_X_EAST = KING_DEST_X_EAST + 1;
    private static final int ROOK_DEST_X_WEST = KING_DEST_X_WEST - 1;

    private GamePiece rook;

    public Castle(GamePiece piece, Rook rook) {
        super(piece, rook.getX(), rook.getY());
        this.rook = rook;
    }

    @Override
    public void execute(Game game) {
        if (rook.getX() == 0){
            // Castling to the east
            game.movePiece(piece, KING_DEST_X_EAST, piece.getY());
            game.movePiece(rook, ROOK_DEST_X_EAST, piece.getY());
        } else {
            // Castling to the west
            game.movePiece(piece, KING_DEST_X_WEST, piece.getY());
            game.movePiece(rook, ROOK_DEST_X_WEST, piece.getY());
        }
        piece.setMoved(true);
        rook.setMoved(true);
    }

    @Override
    public void undo(Game game) {
        game.movePiece(piece, KING_INITIAL_X, piece.getY());
        if (rook.getX() == ROOK_DEST_X_EAST){
            // Castled to the east
            game.movePiece(rook, ROOK_INITIAL_X_EAST, piece.getY());
        } else {
            // Castled to the west
            game.movePiece(rook, ROOK_INITIAL_X_WEST, piece.getY());
        }
        piece.setMoved(false);
        rook.setMoved(false);
    }

}
