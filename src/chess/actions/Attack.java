package chess.actions;

import chess.Game;
import chess.GamePiece;

/**
 * A Move that also removes the target GamePiece.
 *
 * @author Dan Bryce
 */
public class Attack extends Move {

    private GamePiece target;

    public Attack(GamePiece attacker, GamePiece target) {
        super(attacker, target.getX(), target.getY());
        this.target = target;
    }

    @Override
    public void execute(Game game) {
        game.removePiece(target);
        super.execute(game);
    }

    @Override
    public void undo(Game game) {
        super.undo(game);
        game.addPiece(target);
    }

    public GamePiece getTarget() {
        return target;
    }

}
