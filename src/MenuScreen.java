import javax.swing.*;
import java.awt.*;

public class MenuScreen extends JPanel {

    public MenuScreen() {
        // Create the JFrame
        JFrame frame = new JFrame("T-Rex Game Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // Remove title bar
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Enable fullscreen mode

        // Load the GIF as an ImageIcon
        ImageIcon gifBackground = new ImageIcon("Assets/background_menu.gif");

        // Create a custom panel to stretch and display the GIF
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the GIF scaled to the size of the panel
                g.drawImage(gifBackground.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new GridBagLayout()); // Center the buttons
        frame.setContentPane(backgroundPanel);

        // Create buttons using ButtonFactory
        JButton singlePlayerButton = ButtonFactory.createButton("Single Player");
        JButton multiplayerButton = ButtonFactory.createButton("Multiplayer");
        JButton aboutGameButton = ButtonFactory.createButton("About Game");
        JButton closeButton = ButtonFactory.createButton("Close Game");

        // Add buttons to a transparent JPanel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 rows with spacing
        buttonPanel.setOpaque(false); // Transparent panel
        buttonPanel.add(singlePlayerButton);
        buttonPanel.add(multiplayerButton);
        buttonPanel.add(aboutGameButton);
        buttonPanel.add(closeButton);

        // Center the button panel
        backgroundPanel.add(buttonPanel, new GridBagConstraints());

        // Add action listeners for buttons
        singlePlayerButton.addActionListener(e -> {
            frame.dispose(); // Close the menu screen
            SwingUtilities.invokeLater(SinglePlayer::new); // Start the single player game
        });
        multiplayerButton.addActionListener(e -> {
            frame.dispose(); // Close the menu screen
            SwingUtilities.invokeLater(MultiPlayer::new);
        });

        aboutGameButton.addActionListener(e ->
            {
                JOptionPane.showMessageDialog(
                        this,
                        "Welcome to T-Rex Game!\n \n" +
                                "Game Instructions:\n\n" +
                                "- The goal is to survive as long as possible while avoiding obstacles and collecting power-ups.\n\n" +
                                "Gameplay:\n" +
                                "1. Press 'Space' to jump over obstacles (e.g., cacti).\n" +
                                "2. Collect bottles of power (Power Bottles) to become invisible for 10 seconds.\n" +
                                "3. Collect hearts to gain an extra life (adds to your health).\n" +
                                "4. Avoid obstacles while collecting as many points as possible.\n\n" +
                                "Difficulty Levels:\n" +
                                "- Easy Level:\n" +
                                "  * You have 3 lives.\n" +
                                "  * The game speed is moderate.\n" +
                                "- Hard Level:\n" +
                                "  * You have only 1 life.\n" +
                                "  * The game speed is faster.\n" +
                                "  * Obstacles appear more frequently.\n\n" +
                                "Special Features:\n" +
                                "- Power Bottles:\n" +
                                "  * When collected, the T-Rex becomes invisible for 10 seconds and can pass through obstacles without losing lives.\n" +
                                "- Hearts:\n" +
                                "  * Each collected heart increases your lives by 1 (only in Easy Mode).\n\n" +
                                "Good Luck and Have Fun!",
                        "Game Instructions",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        );

        closeButton.addActionListener(e -> frame.dispose());

        // Show the frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuScreen::new);
    }
}