package chess.actions;

import chess.Action;
import chess.Game;
import chess.GamePiece;

/**
 * An Action that simply moves the selected GamePiece to a new position.
 *
 * @author Dan Bryce
 */
public class Move extends Action {

    private boolean firstMove;
    private int startX, startY;

    public Move(GamePiece piece, int x, int y) {
        super(piece, x, y);
    }

    @Override
    public void execute(Game game) {
        startX = piece.getX();
        startY = piece.getY();
        firstMove = !piece.hasMoved();
        game.movePiece(piece, x, y);
    }

    @Override
    public void undo(Game game) {
        game.movePiece(piece, startX, startY);
        if (firstMove){
            piece.setMoved(false);
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

}
