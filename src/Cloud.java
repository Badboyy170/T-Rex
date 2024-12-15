import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Cloud {
    private int x, y, width, height; // متغيرات لتحديد موقع وأبعاد السحابة
    private BufferedImage cloudImage; // صورة السحابة

    // المُنشئ (Constructor) لتحديد موقع السحابة الأولي
    public Cloud(int startX, int startY) {
        x = startX; // تحديد الموقع الأفقي للسحابة
        y = startY; // تحديد الموقع الرأسي للسحابة
        width = 100; // عرض السحابة
        height = 60; // ارتفاع السحابة

        // تحميل صورة السحابة من الملف
        try {
            BufferedImage originalImage = ImageIO.read(new File("Assets/cloud/cloud.png")); // قراءة الصورة الأصلية
            cloudImage = resizeImage(originalImage, width, height); // إعادة تغيير حجم الصورة لتتناسب مع أبعاد السحابة
        } catch (IOException e) {
            e.printStackTrace(); // طباعة أي خطأ إذا حدث أثناء تحميل الصورة
        }
    }

    // وظيفة لتغيير حجم الصورة
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH); // تغيير حجم الصورة باستخدام خاصية التنعيم
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); // إنشاء صورة جديدة بالحجم الجديد
        Graphics2D g2d = resizedImage.createGraphics(); // الحصول على رسومات الصورة
        g2d.drawImage(tmp, 0, 0, null); // رسم الصورة بعد تغيير حجمها
        g2d.dispose(); // تحرير موارد الرسومات
        return resizedImage; // إرجاع الصورة بالحجم الجديد
    }

    // تحديث موقع السحابة لتحريكها إلى اليسار
    public void update() {
        x -= 2; // تقليل الموقع الأفقي بمقدار 2 بيكسل لتحريك السحابة إلى اليسار
    }

    // رسم السحابة على الشاشة
    public void draw(Graphics g) {
        g.drawImage(cloudImage, x, y, null); // رسم صورة السحابة في موقعها الحالي
    }

    // وظيفة لإرجاع الموقع الأفقي للسحابة
    public int getX() {
        return x; // إرجاع قيمة الموقع الأفقي
    }
}
