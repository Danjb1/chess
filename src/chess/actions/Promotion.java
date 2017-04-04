package chess.actions;

import chess.Action;
import chess.Game;
import chess.GamePiece;

/**
 * Action that causes the selected Pawn to be promoted.
 *
 * <p>This temporarily alters the state of the game to show a new screen,
 * requiring the current player to choose a new piece to replace the promoted
 * Pawn.
 *
 * @author Dan Bryce
 */
public class Promotion extends Action {

    private GamePiece newPiece;

    public Promotion(GamePiece piece, int x, int y) {
        super(piece, x, y);
    }

    @Override
    public void execute(Game game) {
        // We don't bother moving the pawn, because we can just remove it and
        // place the new piece at the end of the board.
        game.removePiece(piece);
        game.setPromotionInProgress(this);
    }

    @Override
    public void undo(Game game) {
        if (newPiece != null){
            // If this Action is undone before a new piece is chosen, there will
            // be no piece to remove.
            game.removePiece(newPiece);
        }
        game.addPiece(piece);
        game.setPromotionInProgress(null);
    }

    /**
     * Callback for when a new GamePiece has been chosen.
     *
     * @param game
     * @param newPiece
     */
    public void pieceChosen(Game game, GamePiece newPiece){
        this.newPiece = newPiece;
        game.addPiece(newPiece);
        game.setPromotionInProgress(null);
    }

}
