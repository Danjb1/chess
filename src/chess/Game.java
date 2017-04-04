package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import chess.GamePiece.Player;
import chess.actions.Promotion;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

/**
 * Class representing the current state of the game.
 *
 * @author Dan Bryce
 */
public class Game {

    public static final int SQUARES_PER_SIDE = 8;

    /**
     * 2D array representing the board.
     *
     * We keep track of this in addition to a list of pieces, so that we can
     * quickly look up a piece at a particular position, rather than having to
     * search through all the pieces.
     *
     * It is imperative that this is kept in-sync with the list of pieces! That
     * is, if a piece's position is changed, it must also be moved in this
     * array. The methods in this class should be used for all movement to
     * ensure that this happens.
     */
    private GamePiece[][] squares =
            new GamePiece[SQUARES_PER_SIDE][SQUARES_PER_SIDE];

    /**
     * List of pieces currently in play.
     */
    private List<GamePiece> pieces = new ArrayList<>();

    private List<Action> history = new ArrayList<>();

    private GamePiece selectedPiece;

    private Collection<Action> validActions = new ArrayList<>();

    private Player currentPlayer = Player.WHITE;

    private boolean gameOver;

    private Promotion promotionInProgress;

    public Game() {

        // Initialise pieces
        addPiece(new Rook(0, 0, Player.BLACK));
        addPiece(new Knight(1, 0, Player.BLACK));
        addPiece(new Bishop(2, 0, Player.BLACK));
        addPiece(new Queen(3, 0, Player.BLACK));
        addPiece(new King(4, 0, Player.BLACK));
        addPiece(new Bishop(5, 0, Player.BLACK));
        addPiece(new Knight(6, 0, Player.BLACK));
        addPiece(new Rook(7, 0, Player.BLACK));
        addPiece(new Rook(0, 7, Player.WHITE));
        addPiece(new Knight(1, 7, Player.WHITE));
        addPiece(new Bishop(2, 7, Player.WHITE));
        addPiece(new Queen(3, 7, Player.WHITE));
        addPiece(new King(4, 7, Player.WHITE));
        addPiece(new Bishop(5, 7, Player.WHITE));
        addPiece(new Knight(6, 7, Player.WHITE));
        addPiece(new Rook(7, 7, Player.WHITE));
        for (int x = 0; x < SQUARES_PER_SIDE; x++){
            addPiece(new Pawn(x, 1, Player.BLACK));
            addPiece(new Pawn(x, 6, Player.WHITE));
        }
    }

    public void addPiece(GamePiece piece) {
        pieces.add(piece);
        squares[piece.x][piece.y] = piece;
    }

    public void removePiece(GamePiece piece) {
        pieces.remove(piece);
        squares[piece.x][piece.y] = null;
    }

    public void movePiece(GamePiece piece, int x, int y){
        int oldX = piece.x;
        int oldY = piece.y;
        squares[oldX][oldY] = null;
        piece.x = x;
        piece.y = y;
        squares[x][y] = piece;
        piece.setMoved(true);
    }

    public GamePiece getPieceAt(int x, int y) {
        return squares[x][y];
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void flipTurn(){
        currentPlayer = (currentPlayer == Player.BLACK) ?
                Player.WHITE : Player.BLACK;
    }

    public void setSelectedPiece(GamePiece selectedPiece) {
        this.selectedPiece = selectedPiece;
        if (selectedPiece == null){
            // Deselect
            validActions.clear();
            return;
        }

        validActions = generateValidActions(selectedPiece);
    }

    private Collection<Action> generateValidActions(GamePiece piece) {
        Collection<Action> actions = piece.getValidActions(this, false);

        // Remove any actions that result in the owner being in check
        Collection<Action> removeList = new ArrayList<>();
        for (Action action : actions){
            action.execute(this);
            if (isPlayerInCheck(piece.getOwner())){
                removeList.add(action);
            }
            action.undo(this);
        }
        actions.removeAll(removeList);

        return actions;
    }

    /**
     * Determines if the given player is check.
     *
     * @param player
     * @return
     */
    private boolean isPlayerInCheck(Player player) {
        // See comment in doesValidActionExist().
        for (int i = 0; i < pieces.size(); i++){
            GamePiece piece = pieces.get(i);
            if (piece.getOwner() == player && piece instanceof King){
                return isSquareAttackedByPlayer(piece.x, piece.y,
                        piece.getOpponent());
            }
        }
        return false;
    }

    /**
     * Determines if the given square is under threat from the given Player.
     *
     * <p>This works by generating actions for all of the player's pieces, and
     * seeing if any of them are attacks against the given square.
     *
     * @param x
     * @param y
     * @param player
     * @return
     */
    public boolean isSquareAttackedByPlayer(int x, int y, Player player) {
        // See comment in doesValidActionExist().
        for (int i = 0; i < pieces.size(); i++){

            GamePiece piece = pieces.get(i);
            if (piece.getOwner() != player){
                // We are only interested in the given player's pieces
                continue;
            }

            Collection<Action> actions = piece.getValidActions(this, true);
            for (Action action : actions){
                if (action.x == x && action.y == y){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if a valid action exists for the current player.
     *
     * @return
     */
    private boolean doesValidActionExist() {
        /*
         * We use a regular for-loop instead of a for-each loop because
         * executing an action inside such a loop could cause a
         * ConcurrentModificationException.
         */
        for (int i = 0; i < pieces.size(); i++){
            GamePiece piece = pieces.get(i);
            if (piece.getOwner() != currentPlayer){
                // We are only interested in the current player's pieces
                continue;
            }
            if (!generateValidActions(piece).isEmpty()){
                return true;
            }
        }
        return false;
    }

    public GamePiece getSelectedPiece() {
        return selectedPiece;
    }

    public static boolean isValidPosition(int x, int y) {
        return x >= 0 && x < SQUARES_PER_SIDE &&
                y >= 0 && y < SQUARES_PER_SIDE;
    }

    public Collection<Action> getValidActions() {
        return validActions;
    }

    public Action getActionAt(int x, int y) {
        for (Action action : validActions){
            if (action.getX() == x && action.getY() == y){
                return action;
            }
        }
        return null;
    }

    public void addHistory(Action action) {
        history.add(0, action);
    }

    public void undo(){
        if (history.isEmpty()){
            return;
        }
        Action action = history.get(0);
        action.undo(this);
        history.remove(0);
        flipTurn();
        gameOver = false;
    }

    public void determineGameOver() {
        gameOver = !doesValidActionExist();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Collection<GamePiece> getPieces() {
        return pieces;
    }

    public void setPromotionInProgress(Promotion promotionInProgress){
        this.promotionInProgress = promotionInProgress;
    }

    public boolean isPromotionInProgress() {
        return promotionInProgress != null;
    }

    public Promotion getPromotionInProgress() {
        return promotionInProgress;
    }

}
