import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

class Obstacle {
    private int x, y, width, height; // إحداثيات وحجم العقبة
    private BufferedImage obstacleImage; // صورة العقبة

    // المُنشئ: يُستخدم لإنشاء كائن عقبة جديد
    public Obstacle(int startX, int panelHeight) {
        x = startX; // تعيين موضع العقبة على المحور الأفقي
        width = 170; // تعيين عرض العقبة
        height = 200; // تعيين ارتفاع العقبة
        y = panelHeight - height; // تعيين موضع العقبة على المحور العمودي بناءً على ارتفاع اللوحة

        // تحميل صورة العقبة
        try {
            BufferedImage originalImage = ImageIO.read(new File("Assets/cactus/cactus.png")); // قراءة الصورة الأصلية
            obstacleImage = resizeImage(originalImage, width, height); // تغيير حجم الصورة بما يتناسب مع العقبة
        } catch (IOException e) {
            e.printStackTrace(); // طباعة الخطأ إذا فشل تحميل الصورة
        }
    }

    // وظيفة لتغيير حجم الصورة
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH); // تصغير أو تكبير الصورة
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); // إنشاء صورة جديدة بالحجم المطلوب
        Graphics2D g2d = resizedImage.createGraphics(); // إنشاء كائن للرسم
        g2d.drawImage(tmp, 0, 0, null); // رسم الصورة في الإطار الجديد
        g2d.dispose(); // تحرير الموارد
        return resizedImage; // إعادة الصورة بالحجم الجديد
    }

    // وظيفة لتحديث موضع العقبة (تحريكها إلى اليسار)
    public void update() {
        x -= 10; // تقليل الإحداثي الأفقي للعقبة بمقدار 10 وحدات
    }

    // وظيفة لرسم العقبة على الشاشة
    public void draw(Graphics g) {
        g.drawImage(obstacleImage, x, y, null); // رسم صورة العقبة في موقعها الحالي
    }

    // وظيفة للحصول على الإحداثي الأفقي للعقبة
    public int getX() {
        return x; // إعادة الإحداثي الأفقي الحالي
    }

    // وظيفة للحصول على حدود العقبة كمستطيل (لأغراض الكشف عن التصادم)
    public Rectangle getBounds() {
        int margin = -82; // تعديل حدود المستطيل لجعل الكشف عن التصادم أكثر حساسية
        return new Rectangle(x - margin, y - margin, width + 2 * margin, height + 2 * margin); // إعادة المستطيل المعدل
    }

    // وظيفة للحصول على حدود العقبة كمتعدد أضلاع (لأغراض الكشف عن التصادم)
    public Polygon getPolygon() {
        return new Polygon(Arrays.asList(
                new Point(x, y), // النقطة العلوية اليسرى
                new Point(x + width, y), // النقطة العلوية اليمنى
                new Point(x + width, y + height), // النقطة السفلية اليمنى
                new Point(x, y + height) // النقطة السفلية اليسرى
        ));
    }
}
