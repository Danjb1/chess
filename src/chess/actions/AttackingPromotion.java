package chess.actions;

import chess.Game;
import chess.GamePiece;

/**
 * A Promotion that also removes the target GamePiece, for example, when a Pawn
 * ends up on the far side of the board as the result of an attack.
 *
 * @author Dan Bryce
 */
public class AttackingPromotion extends Promotion {

    private GamePiece target;

    public AttackingPromotion(GamePiece piece, GamePiece target) {
        super(piece, target.getX(), target.getY());
        this.target = target;
    }

    @Override
    public void execute(Game game) {
        super.execute(game);
        game.removePiece(target);
    }

    @Override
    public void undo(Game game) {
        super.undo(game);
        game.addPiece(target);
    }

}
