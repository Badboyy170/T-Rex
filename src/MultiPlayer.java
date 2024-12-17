import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MultiPlayer extends JFrame {
    private MPanel gamePanel;
    private String namePlayer1, namePlayer2, kindLevels;
    private boolean isEasy, isHard = false;
    private boolean isSelectEasy, isSelectHard = false;

    public MultiPlayer() {
        setTitle("T-Rex Game - Multiplayer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove title bar

        // Create a panel for difficulty selection
        JPanel menuMulti = new JPanel(new GridBagLayout());
        menuMulti.setBackground(Color.decode("#f8f8f8"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Title for adding names
        JLabel nametitle = new JLabel("Choose Names & Levels");
        nametitle.setFont(new Font("Arial", Font.BOLD, 24));
        menuMulti.add(nametitle, gbc);

        // Fields for entering names
        JTextField nameField1 = new JTextField(20);
        JTextField nameField2 = new JTextField(20);
        menuMulti.add(new JLabel("Player 1 Name:"), gbc);
        menuMulti.add(nameField1, gbc);
        menuMulti.add(new JLabel("Player 2 Name:"), gbc);
        menuMulti.add(nameField2, gbc);

        // Create difficulty buttons
        JButton easyButton = ButtonFactory.createButton("Easy");
        easyButton.setBackground(Color.decode("#edf6f9"));
        easyButton.setForeground(Color.decode("#006d77"));

        JButton hardButton = ButtonFactory.createButton("Hard");
        hardButton.setBackground(Color.decode("#edf6f9"));
        hardButton.setForeground(Color.decode("#006d77"));

        JButton startGameButton = ButtonFactory.createButton("Start");
        startGameButton.setBackground(Color.decode("#006d77"));
        startGameButton.setForeground(Color.decode("#edf6f9"));

        easyButton.addActionListener(e -> {
            if (!isSelectEasy) {
                isEasy = true;
                isHard = false;
                isSelectEasy = true;
                isSelectHard = false;
                easyButton.setBackground(Color.decode("#83c5be"));
                hardButton.setBackground(Color.decode("#edf6f9"));
            }
        });

        hardButton.addActionListener(e -> {
            if (!isSelectHard) {
                isHard = true;
                isEasy = false;
                isSelectHard = true;
                isSelectEasy = false;
                hardButton.setBackground(Color.decode("#83c5be"));
                easyButton.setBackground(Color.decode("#edf6f9"));
            }
        });

        startGameButton.addActionListener(e -> {
            namePlayer1 = nameField1.getText().trim();
            namePlayer2 = nameField2.getText().trim();
            if ((!isEasy && !isHard) || namePlayer1.isEmpty() || namePlayer2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter fields\n- Enter names\n- Choose levels", "Error", JOptionPane.INFORMATION_MESSAGE);
            } else {
                kindLevels = isEasy ? "easy" : "hard";
                startGame(kindLevels, namePlayer1, namePlayer2);
            }
        });

        menuMulti.add(easyButton, gbc);
        menuMulti.add(hardButton, gbc);
        menuMulti.add(startGameButton, gbc);

        add(menuMulti, BorderLayout.CENTER);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public void goToMenuScreen() {
        dispose(); // Close the current window
        SwingUtilities.invokeLater(MenuScreen::new); // Run MenuScreen
    }

    private void startGame(String difficulty, String namePlayer1, String namePlayer2) {
        remove(getContentPane().getComponent(0)); // Remove the difficulty panel
        gamePanel = new MPanel(difficulty, namePlayer1, namePlayer2);
        add(gamePanel);
        revalidate();
        gamePanel.requestFocusInWindow(); // Request focus on the game panel
        gamePanel.startGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MultiPlayer::new);
    }
}