package chess;

/**
 * An action that can be taken on a player's turn.
 *
 * <p>Crucially, every Action must be reversable without side-effects, in order
 * to support the "undo" functionality.
 *
 * @author Dan Bryce
 */
public abstract class Action {

    protected GamePiece piece;

    /**
     * The position on the game board representing this action.
     *
     * <p>Note that this is not necessarily the "destination" of the move (in
     * the case of castling, for example); it is just the square that, when
     * clicked, will cause this move to be carried out.
     */
    protected int x, y;

    public Action(GamePiece piece, int x, int y) {
        this.piece = piece;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract void execute(Game game);

    public abstract void undo(Game game);

    public GamePiece getPiece() {
        return piece;
    }

}
