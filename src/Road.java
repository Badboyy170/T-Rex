// استيراد مكتبات Java اللازمة للعمل مع الرسومات والصور والملفات
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// تعريف الفئة "Road" التي تمثل الطريق في اللعبة أو التطبيق
class Road {
    private int x, y, width, height;  // متغيرات لتحديد موقع الطريق وحجمه
    private BufferedImage roadImage;  // متغير لتخزين صورة الطريق

    // منشئ الفئة لتهيئة المتغيرات عند إنشاء كائن جديد
    public Road(int startX, int startY, int panelWidth) {
        x = startX;  // تعيين قيمة المتغير "x" لموقع البداية للطريق على المحور الأفقي
        y = startY;  // تعيين قيمة المتغير "y" لموقع البداية للطريق على المحور الرأسي
        width = panelWidth;  // تعيين عرض الطريق ليطابق عرض اللوحة
        height = 100;  // تعيين ارتفاع الطريق إلى 100 (يمكن تعديله حسب الحاجة)

        // محاولة تحميل صورة الطريق من الملف
        try {
            BufferedImage originalImage = ImageIO.read(new File("Assets/road/road.png"));  // قراءة الصورة الأصلية للطريق من المجلد
            roadImage = resizeImage(originalImage, width, height);  // تغيير حجم الصورة لتتناسب مع الأبعاد المحددة
        } catch (IOException e) {
            e.printStackTrace();  // إذا فشل تحميل الصورة، يتم طباعة رسالة الخطأ
        }
    }

    // دالة لتغيير حجم الصورة لتتناسب مع الأبعاد المطلوبة
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);  // تغيير حجم الصورة بطريقة سلسة
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);  // إنشاء صورة جديدة بحجم الأبعاد الجديدة
        Graphics2D g2d = resizedImage.createGraphics();  // إنشاء كائن رسومي لرسم الصورة على الصورة الجديدة
        g2d.drawImage(tmp, 0, 0, null);  // رسم الصورة المعدلة على الصورة الجديدة
        g2d.dispose();  // تحرير الموارد الرسومية بعد الانتهاء من الرسم
        return resizedImage;  // إرجاع الصورة المعدلة
    }

    // دالة لتحديث موقع الطريق على الشاشة
    public void update() {
        x -= 10;  // تحريك الطريق إلى اليسار بمقدار 10 بيكسلات في كل مرة يتم استدعاء الدالة
        if (x + width < 0) {  // إذا كانت الصورة قد خرجت بالكامل من الشاشة (أي أن الطريق أصبح خارج الحافة اليسرى)
            x = 0;  // إعادة تعيين موقع الطريق إلى بداية الشاشة لإعادة تكرار عرض الطريق (يخلق تأثير التكرار أو التمرير اللانهائي)
        }
    }

    // دالة لرسم الطريق على الشاشة
    public void draw(Graphics g) {
        g.drawImage(roadImage, x, y, null);  // رسم الصورة الأولى للطريق في الموقع المحدد (x, y)
        g.drawImage(roadImage, x + width, y, null);  // رسم الصورة الثانية للطريق بجانب الصورة الأولى لإنشاء تأثير التكرار
    }
}
