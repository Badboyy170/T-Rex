// In SinglePlayer.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.jar.JarEntry;

public class SinglePlayer extends JFrame {
    private GamePanel gamePanel;
    private String namePlayer, kindLevels;
    private boolean isEasy, isHard = false;
    private boolean isSelectEasy, isSelectHard = false;


    public SinglePlayer() {
        setTitle("T-Rex Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove title bar



        // Create a panel for difficulty selection
        JPanel menuSingle = new JPanel(new GridBagLayout());
        menuSingle.setBackground(Color.decode("#f8f8f8"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);



        // title button for add name
        JLabel nametitle = new JLabel("Choose Name & levels");
        nametitle.setFont(new Font("Arial", Font.BOLD, 24));
        menuSingle.add(nametitle, gbc);

        // button for enter name
        JTextField nameField = new JTextField(20);
        menuSingle.add(nameField, gbc);





        // Create difficulty buttons
        JButton easyButton = ButtonFactory.createButton("Easy");
        easyButton.setBackground(Color.decode("#edf6f9")); // تعديل على خلفية الزرار
        easyButton.setForeground(Color.decode("#006d77")); // تعديل على لون الخط بداخل الزر


        JButton hardButton = ButtonFactory.createButton("Hard");
        hardButton.setBackground(Color.decode("#edf6f9")); // تعديل على خلفية الزرار
        hardButton.setForeground(Color.decode("#006d77")); // تعديل على لون الخط بداخل الزر


        JButton startGameButton = ButtonFactory.createButton("Start");
        startGameButton.setBackground(Color.decode("#006d77")); // تعديل على خلفية الزرار
        startGameButton.setForeground(Color.decode("#edf6f9")); // تعديل على لون الخط بداخل الزر



        easyButton.addActionListener(e -> {
            if (!isSelectEasy) {
                isEasy = true;
                isHard = false;
                isSelectEasy = true;
                isSelectHard = false;
                easyButton.setBackground(Color.decode("#83c5be")); // لون التحديد
                hardButton.setBackground(Color.decode("#edf6f9")); // لون غير محدد
            }
        });

        hardButton.addActionListener(e -> {
            if (!isSelectHard) {
                isHard = true;
                isEasy = false;
                isSelectHard = true;
                isSelectEasy = false;
                hardButton.setBackground(Color.decode("#83c5be")); // لون التحديد
                easyButton.setBackground(Color.decode("#edf6f9")); // لون غير محدد
            }
        });

        startGameButton.addActionListener(e -> {
            namePlayer = nameField.getText().trim();
            if ((!isEasy || !isHard) && namePlayer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter fields\n- Enter name\n- Choose levels", "Error", JOptionPane.INFORMATION_MESSAGE);
            }else{
                if(isEasy){
                    kindLevels = "easy";
                }else{
                    kindLevels = "hard";
                }
                startGame(kindLevels, namePlayer);
            }
        });


        menuSingle.add(easyButton, gbc);
        menuSingle.add(hardButton, gbc);
        menuSingle.add(startGameButton, gbc);


        add(menuSingle, BorderLayout.CENTER);



        setExtendedState(JFrame.MAXIMIZED_BOTH);


        setVisible(true);
    }

    public void goToMenuScreen() {
        dispose(); // Close the current window
        SwingUtilities.invokeLater(MenuScreen::new); // Run MenuScreen
    }

    private void startGame(String difficulty, String namePlayer) {
        remove(getContentPane().getComponent(0)); // Remove the difficulty panel
        gamePanel = new GamePanel(difficulty, namePlayer);
        add(gamePanel);
        revalidate();
        gamePanel.requestFocusInWindow(); // Request focus on the game panel
        gamePanel.startGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SinglePlayer::new);
    }
}