// استيراد المكتبات اللازمة للعمل مع الأزرار والرسومات
import javax.swing.*;
import java.awt.*;

// تعريف فئة RoundedButton التي تمثل زرًا مخصصًا
class RoundedButton extends JButton {

    // منشئ الفئة الذي يقوم بتمرير النص إلى الزر
    public RoundedButton(String text) {
        super(text);  // استدعاء مُنشئ الفئة الأم JButton وتمرير النص إلى الزر.
        setContentAreaFilled(false);  // إيقاف ملء منطقة المحتوى داخل الزر بحيث يظل الزر شفافًا من الداخل.
    }

    // دالة الرسم الخاصة بمحتوى الزر
    @Override
    protected void paintComponent(Graphics g) {
        // إنشاء كائن جديد من Graphics2D للعمل على الرسومات
        Graphics2D g2 = (Graphics2D) g.create();

        // تفعيل تحسين الحواف لجعل الأشكال ناعمة
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // تعيين اللون الذي يمثل خلفية الزر
        g2.setColor(getBackground());

        // رسم مستطيل ذو زوايا دائرية باستخدام دالة fillRoundRect
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // استدعاء دالة paintComponent الأصلية من JButton لرسم النص داخل الزر
        super.paintComponent(g);

        // تحرير الموارد الرسومية بعد الرسم
        g2.dispose();
    }

    // دالة الرسم الخاصة بالحدود الخارجية للزر
    @Override
    protected void paintBorder(Graphics g) {
        // إنشاء كائن جديد من Graphics2D لرسم الحدود
        Graphics2D g2 = (Graphics2D) g.create();

        // تفعيل تحسين الحواف لجعل الحدود ناعمة
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // تعيين اللون الذي يمثل الحد الخارجي للزر
        g2.setColor(getForeground());

        // رسم الحدود حول الزر باستخدام مستطيل ذو زوايا دائرية باستخدام دالة drawRoundRect
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

        // تحرير الموارد الرسومية بعد رسم الحدود
        g2.dispose();
    }

    // دالة للتحقق مما إذا كانت النقطة (x, y) داخل الزر أم لا
    @Override
    public boolean contains(int x, int y) {
        // تحديد نصف القطر للزوايا الدائرية
        int radius = 30;

        // استخدام كائن RoundRectangle2D للتحقق مما إذا كانت النقطة داخل الزر الذي له زوايا دائرية
        return new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius).contains(x, y);
    }
}
