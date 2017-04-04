package chess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;

import javax.swing.JPanel;

import chess.GamePiece.Player;
import chess.actions.Promotion;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

/**
 * JPanel responsible for rendering the game.
 *
 * @author Dan Bryce
 */
public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public static final int PIXELS_PER_SQUARE = 96;

    private static final int DISPLAY_WIDTH =
            Game.SQUARES_PER_SIDE * PIXELS_PER_SQUARE;
    private static final int DISPLAY_HEIGHT = DISPLAY_WIDTH;

    private static final int NUM_IMAGES_X = 6;
    private static final int NUM_IMAGES_Y = 2;

    private static final Color GAME_OVER_COLOUR = new Color(0, 0, 0, 0.5f);
    private static final Color HIGHLIGHT_COLOUR = new Color(1, 0, 0, 0.5f);

    private Game game;
    private BufferedImage sprite;
    private int pieceImageWidth, pieceImageHeight;

    public GamePanel(Game game, BufferedImage sprite) {
        this.game = game;
        this.sprite = sprite;

        pieceImageWidth = sprite.getWidth() / NUM_IMAGES_X;
        pieceImageHeight = sprite.getHeight() / NUM_IMAGES_Y;

        setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        addMouseListener(new MouseHandler(game, this));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        boolean black = false;
        int drawX = 0;
        int drawY = 0;

        // Draw board
        for (int y = 0; y < Game.SQUARES_PER_SIDE; y++){
            for (int x = 0; x < Game.SQUARES_PER_SIDE; x++){
                Color col = black ? Color.BLACK : Color.WHITE;
                drawSquare(g, drawX, drawY, col);
                drawX += PIXELS_PER_SQUARE;
                black = !black;
            }
            drawX = 0;
            drawY += PIXELS_PER_SQUARE;
            black = !black;
        }

        // Highlight selected piece
        GamePiece selectedPiece = game.getSelectedPiece();
        if (selectedPiece != null){
            drawSquare(g,
                    selectedPiece.x * PIXELS_PER_SQUARE,
                    selectedPiece.y * PIXELS_PER_SQUARE,
                    HIGHLIGHT_COLOUR);
        }

        // Draw available moves
        Collection<Action> validMoves = game.getValidActions();
        for (Action move : validMoves){
            drawSquare(g,
                    move.getX() * PIXELS_PER_SQUARE,
                    move.getY() * PIXELS_PER_SQUARE,
                    HIGHLIGHT_COLOUR);
        }

        // If a promotion is ready, we draw all possible pieces to choose from
        Promotion promotion = game.getPromotionInProgress();
        if (promotion != null){
            Player player = promotion.getPiece().getOwner();
            drawGamePiece(g, new Rook(0, 0, player));
            drawGamePiece(g, new Knight(1, 0, player));
            drawGamePiece(g, new Bishop(2, 0, player));
            drawGamePiece(g, new Queen(3, 0, player));
            return;
        }

        // Draw pieces
        for (GamePiece piece : game.getPieces()){
            drawGamePiece(g, piece);
        }

        // Grey-out the game if it's over
        if (game.isGameOver()){
            g.setColor(GAME_OVER_COLOUR);
            g.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
        }
    }

    private void drawSquare(Graphics g, int drawX, int drawY, Color col) {
        g.setColor(col);
        g.fillRect(drawX, drawY, PIXELS_PER_SQUARE, PIXELS_PER_SQUARE);
    }

    private void drawGamePiece(Graphics g, GamePiece piece) {

        if (piece == null){
            return;
        }

        int x = piece.getX();
        int y = piece.getY();

        int dx1 = x * PIXELS_PER_SQUARE;
        int dy1 = y * PIXELS_PER_SQUARE;
        int dx2 = dx1 + PIXELS_PER_SQUARE;
        int dy2 = dy1 + PIXELS_PER_SQUARE;

        int sx1 = getImageIndex(piece) * pieceImageWidth;
        int sy1 = piece.isBlack() ? 0 : pieceImageHeight;
        int sx2 = sx1 + pieceImageWidth;
        int sy2 = sy1 + pieceImageWidth;

        g.drawImage(sprite, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }

    private static int getImageIndex(GamePiece piece) {
        if (piece instanceof King){
            return 0;
        }
        if (piece instanceof Queen){
            return 1;
        }
        if (piece instanceof Bishop){
            return 2;
        }
        if (piece instanceof Knight){
            return 3;
        }
        if (piece instanceof Rook){
            return 4;
        }
        if (piece instanceof Pawn){
            return 5;
        }
        throw new IllegalArgumentException("Invalid piece: " + piece);
    }

}
