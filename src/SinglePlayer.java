import javax.swing.*;
import java.awt.*;

public class SinglePlayer extends JFrame {
    private GamePanel gamePanel;

    public SinglePlayer() {
        setTitle("T-Rex Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove title bar

        gamePanel = new GamePanel();
        add(gamePanel);

        // Set the frame to full screen
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);

        setVisible(true);
        gamePanel.startGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SinglePlayer::new);
    }
}