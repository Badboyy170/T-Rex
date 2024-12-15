import javax.swing.*;
import java.awt.*;

public class MenuScreen {

    public MenuScreen() {
        // إنشاء نافذة (JFrame) لواجهة القائمة الرئيسية
        JFrame frame = new JFrame("T-Rex Game Menu");
        // تحديد الإجراء عند إغلاق النافذة لإنهاء البرنامج
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // إزالة شريط العنوان من النافذة لجعلها بدون إطار
        frame.setUndecorated(true);
        // جعل النافذة تعمل في وضع ملء الشاشة
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // تحميل صورة متحركة (GIF) كخلفية من المسار المحدد
        ImageIcon gifBackground = new ImageIcon("Assets/background_menu.gif");

        // إنشاء لوحة مخصصة لعرض صورة الخلفية بحجم ممتد لتناسب النافذة
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // رسم صورة الخلفية مع تمديدها لتناسب حجم اللوحة
                g.drawImage(gifBackground.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        // ضبط تخطيط اللوحة ليكون في المنتصف
        backgroundPanel.setLayout(new GridBagLayout());
        // تعيين اللوحة كالمحتوى الأساسي للنافذة
        frame.setContentPane(backgroundPanel);

        // إنشاء أزرار القائمة الرئيسية
        JButton singlePlayerButton = createButton("Single Player"); // زر لبدء اللعبة بوضع لاعب واحد
        JButton multiplayerButton = createButton("Multiplayer"); // زر لبدء اللعبة بوضع متعدد اللاعبين
        JButton aboutGameButton = createButton("About Game"); // زر لعرض معلومات عن اللعبة
        JButton closeButton = createButton("Close Game"); // زر لإغلاق اللعبة

        // إنشاء لوحة شفافة لإضافة الأزرار بداخلها
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // ترتيب الأزرار في شبكة من 4 صفوف مع مسافات بينهما
        buttonPanel.setOpaque(false); // جعل اللوحة شفافة
        buttonPanel.add(singlePlayerButton); // إضافة زر لاعب واحد
        buttonPanel.add(multiplayerButton); // إضافة زر متعدد اللاعبين
        buttonPanel.add(aboutGameButton); // إضافة زر معلومات اللعبة
        buttonPanel.add(closeButton); // إضافة زر إغلاق اللعبة

        // إضافة لوحة الأزرار إلى اللوحة الخلفية وتوسيطها
        backgroundPanel.add(buttonPanel, new GridBagConstraints());

        // إضافة مستمعي الأحداث للأزرار لتنفيذ وظائف عند الضغط عليها

        // عند الضغط على زر "Single Player" يتم إغلاق القائمة وبدء اللعبة في وضع لاعب واحد
        singlePlayerButton.addActionListener(e -> {
            frame.dispose(); // إغلاق نافذة القائمة
            SwingUtilities.invokeLater(SinglePlayer::new); // بدء وضع اللاعب الواحد (يفترض وجود فئة SinglePlayer)
        });

        // عند الضغط على زر "Multiplayer" يتم إغلاق القائمة وبدء اللعبة في وضع اللاعبين المتعددين
        multiplayerButton.addActionListener(e -> {
            frame.dispose(); // إغلاق نافذة القائمة
            SwingUtilities.invokeLater(MultiPlayer::new); // بدء وضع اللاعبين المتعددين (يفترض وجود فئة MultiPlayer)
        });

        // عند الضغط على زر "About Game" يتم عرض رسالة تحتوي على معلومات عن اللعبة
        aboutGameButton.addActionListener(e -> JOptionPane.showMessageDialog(
                frame,
                "This is the T-Rex game!\nA fun endless runner game for single and multiplayer modes."
        ));

        // عند الضغط على زر "Close Game" يتم إغلاق نافذة القائمة
        closeButton.addActionListener(e -> frame.dispose());

        // إظهار النافذة
        frame.setVisible(true);
    }

    // وظيفة لإنشاء زر مخصص
    private JButton createButton(String text) {
        JButton button = new JButton(text); // إنشاء زر عادي
        button.setPreferredSize(new Dimension(200, 50)); // تحديد الحجم الافتراضي للزر
        button.setBackground(new Color(70, 130, 180)); // تحديد لون خلفية الزر (لون أزرق)
        button.setForeground(Color.WHITE); // تحديد لون النص داخل الزر (لون أبيض)
        button.setFont(new Font("Arial", Font.BOLD, 16)); // تحديد نوع الخط وحجمه
        button.setFocusPainted(false); // إزالة الإطار الذي يظهر عند التركيز على الزر
        return button; // إرجاع الزر المخصص
    }

    // نقطة الدخول الرئيسية لتشغيل البرنامج
    public static void main(String[] args) {
        // تشغيل واجهة المستخدم الرسومية في خيط مخصص باستخدام SwingUtilities
        SwingUtilities.invokeLater(MenuScreen::new);
    }
}