// In SinglePlayer.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SinglePlayer extends JFrame {
    private GamePanel gamePanel;

    public SinglePlayer() {
        setTitle("T-Rex Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove title bar

        // Create a panel for difficulty selection
        JPanel difficultyPanel = new JPanel(new GridBagLayout());
        difficultyPanel.setBackground(Color.decode("#f8f8f8"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel title = new JLabel("Choose Difficulty");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        difficultyPanel.add(title, gbc);
        // Create difficulty buttons
        JButton easyButton = ButtonFactory.createButton("Easy");
        JButton hardButton = ButtonFactory.createButton("Hard");

        easyButton.addActionListener(e -> startGame("easy"));
        hardButton.addActionListener(e -> startGame("hard"));

        difficultyPanel.add(easyButton, gbc);
        difficultyPanel.add(hardButton, gbc);

        add(difficultyPanel, BorderLayout.CENTER);

        // Set the frame to full screen
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);

        // Add window listener to switch to MenuScreen on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goToMenuScreen();
            }
        });

        setVisible(true);
    }

    public void goToMenuScreen() {
        dispose(); // Close the current window
        SwingUtilities.invokeLater(MenuScreen::new); // Run MenuScreen
    }

    private void startGame(String difficulty) {
        remove(getContentPane().getComponent(0)); // Remove the difficulty panel
        gamePanel = new GamePanel(difficulty);
        add(gamePanel);
        revalidate();
        gamePanel.requestFocusInWindow(); // Request focus on the game panel
        gamePanel.startGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SinglePlayer::new);
    }
}