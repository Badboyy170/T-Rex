import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MultiPlayer extends JFrame {
    private MPanel gamePanel1, gamePanel2;

    public MultiPlayer() {
        setTitle("T-Rex Game - Multiplayer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        JPanel difficultyPanel = new JPanel(new GridBagLayout());
        difficultyPanel.setBackground(Color.decode("#f8f8f8"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel title = new JLabel("Choose Difficulty");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        difficultyPanel.add(title, gbc);

        JButton easyButton = ButtonFactory.createButton("Easy");
        JButton hardButton = ButtonFactory.createButton("Hard");

        easyButton.addActionListener(e -> startGame("easy"));
        hardButton.addActionListener(e -> startGame("hard"));

        difficultyPanel.add(easyButton, gbc);
        difficultyPanel.add(hardButton, gbc);

        add(difficultyPanel, BorderLayout.CENTER);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goToMenuScreen();
            }
        });

        setVisible(true);
    }

    public void goToMenuScreen() {
        dispose();
        SwingUtilities.invokeLater(MenuScreen::new);
    }

    private void startGame(String difficulty) {
        remove(getContentPane().getComponent(0));

        gamePanel1 = new MPanel(difficulty);
        gamePanel2 = new MPanel(difficulty);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gamePanel1, gamePanel2);
        splitPane.setDividerLocation(getWidth() / 2);
        add(splitPane, BorderLayout.CENTER);
        revalidate();

        gamePanel1.startGame();
        gamePanel2.startGame();
        gamePanel1.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MultiPlayer::new);
    }
}
