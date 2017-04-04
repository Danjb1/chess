package chess.pieces;

import java.util.Collection;

import chess.Action;
import chess.Game;
import chess.GamePiece;
import chess.actions.Castle;

/**
 * GamePiece that can move 1 square in any direction.
 *
 * @author Dan Bryce
 */
public class King extends GamePiece {

    public King(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    protected void getValidMoves(Game game, Collection<Action> moves,
            boolean onlyAttacks) {

        Action move = getPossibleMove(game, x - 1, y - 1, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x, y - 1, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x + 1, y - 1, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x - 1, y, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x + 1, y, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x - 1, y + 1, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x, y + 1, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        move = getPossibleMove(game, x + 1, y + 1, onlyAttacks);
        if (move != null){
            moves.add(move);
        }

        if (onlyAttacks){
            return;
        }

        if (!moved && !game.isSquareAttackedByPlayer(x, y, getOpponent())){
            // We have not moved and we are not in check, so let's see if we can
            // castle...

            // ... to the east
            move = getPossibleCastle(game, 0, getOwnerSideY());
            if (move != null){
                moves.add(move);
            }

            // ... to the west
            move = getPossibleCastle(game, Game.SQUARES_PER_SIDE - 1,
                    getOwnerSideY());
            if (move != null){
                moves.add(move);
            }
        }
    }

    /**
     * Determines if castling is possible at the given location.
     *
     * <p>Castling is only possible if all of the following conditions are true:
     *
     * <ul>
     *  <li>Neither the King nor the Rook has previously moved.</li>
     *  <li>There are no pieces in between them.</li>
     *  <li>The King is not in check, and does not pass through any squares that
     * are "under attack".</li>
     * </ul>
     *
     * @param game
     * @param rookX
     * @param rookY
     * @return
     */
    private Action getPossibleCastle(Game game, int rookX, int rookY) {
        GamePiece piece = game.getPieceAt(rookX, rookY);
        if (piece instanceof Rook && !piece.hasMoved()){

            // Search every space between the King and the Rook
            int searchDirection = (rookX == 0) ? 1 : -1;
            int startX = rookX + searchDirection;

            for (int x = startX; x != this.x; x += searchDirection){
                if (game.getPieceAt(x, rookY) != null ||
                        game.isSquareAttackedByPlayer(x, rookY, getOpponent())){
                    // Another piece is in the way, or the square is under
                    // attack.
                    return null;
                }
            }

            return new Castle(this, (Rook) piece);
        }
        return null;
    }

}
