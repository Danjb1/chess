package chess;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Class responsible for creating the window and starting the game.
 *
 * @author Dan Bryce
 */
public abstract class ChessLauncher {

    private static final String TITLE = "Chess";

    /**
     * The entry point of the application.
     *
     * @param args
     */
    public static void main(String[] args) {

        BufferedImage sprite = null;
        try {
            sprite = ImageIO.read(new File("pieces.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Game game = new Game();

        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new GamePanel(game, sprite));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
