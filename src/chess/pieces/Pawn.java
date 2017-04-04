package chess.pieces;

import java.util.Collection;

import chess.Action;
import chess.Game;
import chess.GamePiece;
import chess.actions.Attack;
import chess.actions.AttackingPromotion;
import chess.actions.Move;
import chess.actions.Promotion;

/**
 * GamePiece that can typically just move 1 square forward, but can only attack
 * diagonally.
 *
 * <p>A Pawn can move 2 squares forward if it has not yet moved. A Pawn that
 * reaches the other side of the game board can be promoted to any other
 * GamePiece.
 *
 * @author Dan Bryce
 */
public class Pawn extends GamePiece {

    public Pawn(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    protected void getValidMoves(Game game, Collection<Action> moves,
            boolean onlyAttacks) {

        Action attackEast = getPossibleAttack(game, x - 1, y + getDirection());
        if (attackEast != null){
            moves.add(attackEast);
        }

        Action attackWest = getPossibleAttack(game, x + 1, y + getDirection());
        if (attackWest != null){
            moves.add(attackWest);
        }

        if (onlyAttacks){
            return;
        }

        Action forwardOne = getPossibleMove(game, x, y + getDirection());
        if (forwardOne == null){
            // Pawn is blocked
            return;
        }
        moves.add(forwardOne);

        if (!moved){
            Action forwardTwo =
                    getPossibleMove(game, x, y + 2 * getDirection());
            if (forwardTwo == null){
                // Pawn is blocked
                return;
            }
            moves.add(forwardTwo);
        }
    }

    protected Action getPossibleMove(Game game, int x, int y) {

        if (!Game.isValidPosition(x, y)){
            return null;
        }

        if (game.getPieceAt(x, y) != null){
            // Can't move through another piece
            return null;
        }

        if (isDuePromotion(y)) {
            return new Promotion(this, x, y);
        }

        return new Move(this, x, y);
    }

    /**
     * Determines if a Pawn at the given position has reached the other side.
     * @return
     */
    private boolean isDuePromotion(int y) {
        return y == getOpponentSideY();
    }

    private Action getPossibleAttack(Game game, int x, int y) {

        if (!Game.isValidPosition(x, y)){
            return null;
        }

        GamePiece target = game.getPieceAt(x, y);
        if (target == null || target.getOwner() == owner){
            return null;
        }

        if (isDuePromotion(y)) {
            return new AttackingPromotion(this, target);
        }

        return new Attack(this, target);
    }

    private int getDirection() {
        return owner == Player.BLACK ? 1 : -1;
    }

}
