package chess;

import java.util.ArrayList;
import java.util.Collection;

import chess.actions.Attack;
import chess.actions.Move;

/**
 * Class representing a piece present on the game board.
 *
 * @author Dan Bryce
 */
public abstract class GamePiece {

    /**
     * The maximum number of squares a piece can ever move.
     */
    protected static final int MAX_MOVES = Game.SQUARES_PER_SIDE - 1;

    public static enum Player {
        WHITE,
        BLACK
    }

    protected int x, y;
    protected Player owner;
    protected boolean moved;

    public GamePiece(int x, int y, Player owner) {
        this.x = x;
        this.y = y;
        this.owner = owner;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isBlack(){
        return owner == Player.BLACK;
    }

    public boolean isWhite(){
        return owner == Player.WHITE;
    }

    public Player getOwner() {
        return owner;
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Produces a Collection containing all of the valid moves for this piece.
     *
     * <p>Note that this doesn't consider whether or not these moves might put
     * the owner's king in check; this is handled by Game.generateValidMoves().
     *
     * @param game
     * @param onlyAttacks
     *      Whether non-attacking Actions should be omitted.
     *
     *      <p>This not only allows us to be more efficient at finding out if a
     *      square is under attack (we don't need to consider any non-attacking
     *      moves), but it is also important in preventing some cyclic logic
     *      where the King's getValidActions() method calls
     *      game.isSquareAttackedByPlayer(), which in turn will call the
     *      opposing King's getValidActions() method again.
     * @return
     */
    public final Collection<Action> getValidActions(Game game,
            boolean onlyAttacks){
        Collection<Action> moves = new ArrayList<>();
        getValidMoves(game, moves, onlyAttacks);
        return moves;
    }

    protected abstract void getValidMoves(Game game,
            Collection<Action> moves, boolean onlyAttacks);

    /**
     * Attempts to create a valid move to the given destination.
     *
     * @param game
     * @param x
     * @param y
     * @param onlyAttacks
     * @return
     */
    protected Action getPossibleMove(Game game, int x, int y,
            boolean onlyAttacks) {

        if (!Game.isValidPosition(x, y)){
            return null;
        }

        GamePiece piece = game.getPieceAt(x, y);
        if (piece == null){
            // Destination is empty
            return onlyAttacks ? null : new Move(this, x, y);
        }

        if (piece.getOwner() == owner){
            // Can't take own piece!
            return null;
        }

        return new Attack(this, piece);
    }

    /**
     * Scans a line of the board and adds all available Move / Attack actions.
     *
     * <p>This line could be orthogonal or diagonal.
     *
     * @param game
     * @param moves
     * @param onlyAttacks
     * @param xDir
     * @param yDir
     */
    protected void addActionsInDirection(Game game, Collection<Action> moves,
            boolean onlyAttacks, int xDir, int yDir) {
        for (int i = 1; i < MAX_MOVES; i++){
            Action move = getPossibleMove(game,
                    x + (xDir * i),
                    y - (yDir * i),
                    false);
            if (move == null){
                // No more valid moves in this direction
                break;
            }
            if (move instanceof Attack){
                moves.add(move);
                // A piece is blocking the way, so stop with this direction
                break;
            }
            if (!onlyAttacks){
                moves.add(move);
            }
        }
    }

    /**
     * Gets the y-position of this piece's owner's side of the board.
     *
     * @return
     */
    protected int getOwnerSideY() {
        return owner == Player.BLACK ? 0 : Game.SQUARES_PER_SIDE - 1;
    }

    /**
     * Gets the y-position of the opponent's side of the board.
     *
     * @return
     */
    protected int getOpponentSideY() {
        return owner == Player.BLACK ? Game.SQUARES_PER_SIDE - 1 : 0;
    }

    protected Player getOpponent(){
        return owner == Player.BLACK ? Player.WHITE : Player.BLACK;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean hasMoved() {
        return moved;
    }

}
