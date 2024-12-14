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

        multiplayerButton.addActionListener(e ->
                JOptionPane.showMessageDialog(frame, "Multiplayer selected!")
        );

        aboutGameButton.addActionListener(e ->
                JOptionPane.showMessageDialog(frame, "This is the T-Rex game! A fun endless runner game.")
        );

        closeButton.addActionListener(e -> frame.dispose());

        // Show the frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuScreen::new);
    }
}