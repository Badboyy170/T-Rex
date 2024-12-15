// استيراد المكتبات اللازمة للعمل مع النوافذ والألعاب
import javax.swing.*;
import java.awt.*;

// تعريف فئة SinglePlayer التي تمثل لعبة من لاعب واحد، وهي امتداد لـ JFrame لإنشاء نافذة اللعبة
public class SinglePlayer extends JFrame {
    private GamePanel gamePanel;  // تعريف كائن من الفئة GamePanel لعرض اللعبة داخل الإطار

    // منشئ الفئة الذي يقوم بتهيئة الإطار وتخصيص الإعدادات
    public SinglePlayer() {
        setTitle("T-Rex Game");  // تعيين عنوان نافذة اللعبة (سيتم تجاهله عند استخدام full screen)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // تحديد سلوك التطبيق عند إغلاق النافذة
        setUndecorated(true);  // إزالة شريط العنوان وجعل النافذة بدون إطار

        gamePanel = new GamePanel();  // إنشاء كائن من الفئة GamePanel لعرض اللعبة
        add(gamePanel);  // إضافة لوحة اللعبة إلى نافذة الإطار

        // جعل نافذة اللعبة تعمل في وضع الشاشة الكاملة
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);  // تعيين النافذة لتكون في وضع الشاشة الكاملة

        setVisible(true);  // جعل النافذة مرئية
        gamePanel.startGame();  // بدء اللعبة من خلال استدعاء دالة startGame في لوحة اللعبة
    }

    // الدالة الرئيسية التي تبدأ التطبيق
    public static void main(String[] args) {
        // استخدام SwingUtilities لتشغيل إنشاء النافذة في thread آمن
        SwingUtilities.invokeLater(SinglePlayer::new);  // استدعاء المنشئ لإنشاء نافذة اللعبة
    }
}