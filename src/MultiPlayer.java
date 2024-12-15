import javax.swing.*;

public class MultiPlayer extends JFrame {
    private MPanel mPanel; // لوحة اللعبة المتعددة اللاعبين

    public MultiPlayer() {
        setTitle("T-Rex Multiplayer Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false); // إزالة إطار النافذة
        setExtendedState(JFrame.MAXIMIZED_BOTH); // تكبير النافذة لتملأ الشاشة

        // إنشاء لوحة اللعبة الخاصة باللعب المتعدد
        mPanel = new MPanel();
        add(mPanel);

        // إعداد التحكم في لوحة اللعبة
        mPanel.setFocusable(true);

        // جعل اللوحة مرئية
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MultiPlayer()); // تشغيل اللعبة في خيط منفصل
    }
}

