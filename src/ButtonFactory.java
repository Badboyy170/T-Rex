import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class ButtonFactory {
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50)); // Typical button size
        button.setBackground(new Color(70, 130, 180)); // Set background color
        button.setForeground(Color.WHITE); // Set text color
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Set font
        button.setFocusPainted(false); // Remove focus border
        return button;
    }
}