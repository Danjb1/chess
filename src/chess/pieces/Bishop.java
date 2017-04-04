package chess.pieces;

import java.util.Collection;

import chess.Action;
import chess.Game;
import chess.GamePiece;

/**
 * GamePiece that can move any number of squares diagonally.
 *
 * @author Dan Bryce
 */
public class Bishop extends GamePiece {

    public Bishop(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    protected void getValidMoves(Game game, Collection<Action> moves,
            boolean onlyAttacks) {

        // Get the possible moves in each diagonal direction
        addActionsInDirection(game, moves, onlyAttacks, -1, -1);
        addActionsInDirection(game, moves, onlyAttacks, -1, 1);
        addActionsInDirection(game, moves, onlyAttacks, 1, -1);
        addActionsInDirection(game, moves, onlyAttacks, 1, 1);
    }

}
