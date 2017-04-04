package chess;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import chess.GamePiece.Player;
import chess.actions.Promotion;
import chess.pieces.Bishop;
import chess.pieces.Knight;
import chess.pieces.Queen;
import chess.pieces.Rook;

/**
 * Class responsible for handling mouse input within the GamePanel.
 *
 * @author Dan Bryce
 */
public class MouseHandler extends MouseAdapter {

    private Game game;
    private GamePanel gamePanel;

    public MouseHandler(Game game, GamePanel gamePanel) {
        this.game = game;
        this.gamePanel = gamePanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (game.isGameOver()){
            return;
        }

        // Right-click (undo / deselect)
        if (e.getButton() == MouseEvent.BUTTON3){
            GamePiece existingSelection = game.getSelectedPiece();
            if (existingSelection == null){
                // Undo can only be performed if no piece is selected
                game.undo();
                gamePanel.repaint();
            } else {
                // Deselect
                game.setSelectedPiece(null);
                gamePanel.repaint();
            }
            return;
        }

        int x = e.getX() / GamePanel.PIXELS_PER_SQUARE;
        int y = e.getY() / GamePanel.PIXELS_PER_SQUARE;

        Promotion promotion = game.getPromotionInProgress();
        if (promotion != null){
            // Player is due to select a new piece as a promotion
            if (y > 0 || x > 3){
                // No piece chosen
                return;
            }
            int pieceX = promotion.getX();
            int pieceY = promotion.getY();
            Player owner = promotion.getPiece().getOwner();
            // The order of the pieces here has to match what's rendered in the
            // GamePanel.
            if (x == 0){
                promotion.pieceChosen(game, new Rook(pieceX, pieceY, owner));
            } else if (x == 1){
                promotion.pieceChosen(game, new Knight(pieceX, pieceY, owner));
            } else if (x == 2){
                promotion.pieceChosen(game, new Bishop(pieceX, pieceY, owner));
            } else if (x == 3){
                promotion.pieceChosen(game, new Queen(pieceX, pieceY, owner));
            }
            gamePanel.repaint();
            return;
        }

        GamePiece existingSelection = game.getSelectedPiece();
        GamePiece newSelection = game.getPieceAt(x, y);
        if (existingSelection == null){
            // Try to select a piece
            if (newSelection == null ||
                    newSelection.getOwner() != game.getCurrentPlayer()){
                return;
            }
            game.setSelectedPiece(newSelection);
            gamePanel.repaint();
            return;
        }

        if (newSelection != null && newSelection.equals(existingSelection)){
            // Currently-selected piece was clicked again (deselect)
            game.setSelectedPiece(null);
            gamePanel.repaint();
            return;
        }

        Action move = game.getActionAt(x, y);
        if (move == null){
            // No such move
            return;
        }
        move.execute(game);
        game.addHistory(move);
        game.setSelectedPiece(null);
        game.flipTurn();
        game.determineGameOver();
        gamePanel.repaint();
    }

}
