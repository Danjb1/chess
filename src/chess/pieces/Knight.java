package chess.pieces;

import java.util.Collection;

import chess.Action;
import chess.Game;
import chess.GamePiece;

/**
 * GamePiece that can move 2 squares in any orthogonal direction, then 1 square
 * in a direction perpendicular to the first.
 *
 * <p>The Knight is the only GamePiece that can "jump over" other pieces.
 *
 * @author Dan Bryce
 */
public class Knight extends GamePiece {

    public Knight(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    protected void getValidMoves(Game game, Collection<Action> moves,
            boolean onlyAttacks) {

        Action move = getPossibleMove(game, x + 1, y + 2, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x + 1, y - 2, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x - 1, y + 2, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x - 1, y - 2, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x + 2, y - 1, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x + 2, y + 1, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x - 2, y - 1, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x - 2, y + 1, onlyAttacks);
        if (move != null){
            moves.add(move);
        }
    }

}
