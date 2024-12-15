// استيراد المكتبات اللازمة للعمل مع الرسومات والصور
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

// تعريف فئة T_Rex التي تمثل شخصية الديناصور
class T_Rex {
    private int x, y, width, height, yVelocity;  // تعريف المتغيرات اللازمة لموقع وحجم الديناصور وسرعته الرأسية
    private boolean jumping;  // متغير لتحديد ما إذا كانت الشخصية تقفز أم لا
    private BufferedImage[] runningImages;  // مصفوفة من الصور المتحركة لحركة الجري
    private BufferedImage jumpingImage;  // الصورة الخاصة بالقفز
    private int animationFrame;  // إطار الصورة المتحركة الحالي
    private int groundY;  // موقع الأرض (قاع الشاشة)
    private int animationCounter; // عداد للتحكم في سرعة الحركة المتحركة
    private static final int ANIMATION_SPEED = 5; // سرعة الحركة المتحركة (يمكن تعديلها للتحكم في سرعة التبديل بين الصور)

    // منشئ الفئة الذي يهيئ خصائص الشخصية بناءً على ارتفاع اللوحة
    public T_Rex(int panelHeight) {
        x = 50;  // تحديد الموقع الأفقي للديناصور
        width = 640;  // تحديد عرض الصورة
        height = 320;  // تحديد ارتفاع الصورة
        yVelocity = 0;  // تحديد السرعة الرأسية الأولية (لا تقفز الشخصية في البداية)
        jumping = false;  // الشخصية لا تقفز في البداية
        animationFrame = 0;  // تعيين الإطار المتحرك الحالي إلى 0
        animationCounter = 0; // تهيئة عداد الحركة المتحركة
        groundY = panelHeight - height;  // تحديد موقع الأرض بناءً على ارتفاع اللوحة
        y = groundY;  // تعيين الموقع الرأسي للديناصور إلى الأرض

        // تحميل الصور الخاصة بالجري والقفز
        runningImages = new BufferedImage[6];  // تخصيص مصفوفة لتخزين صور الجري
        try {
            // تحميل صور الجري
            for (int i = 0; i < 6; i++) {
                BufferedImage originalImage = ImageIO.read(new File("Assets/raptor-run/raptor-run" + (i + 1) + ".png"));
                runningImages[i] = resizeImage(originalImage, width, height);  // تغيير حجم الصورة لتناسب الشخصية
            }
            // تحميل صورة القفز
            BufferedImage originalJumpingImage = ImageIO.read(new File("Assets/raptor-jump/raptor-jump.png"));
            jumpingImage = resizeImage(originalJumpingImage, width, height);  // تغيير حجم صورة القفز لتناسب الشخصية
        } catch (IOException e) {
            e.printStackTrace();  // في حالة حدوث خطأ أثناء تحميل الصور، يتم طباعة الخطأ
        }
    }

    // دالة لتغيير حجم الصورة لتناسب الأبعاد المحددة
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        // تغيير حجم الصورة باستخدام الدالة getScaledInstance
        Image tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();  // إنشاء كائن Graphics2D للرسم على الصورة
        g2d.drawImage(tmp, 0, 0, null);  // رسم الصورة المعدلة
        g2d.dispose();  // تحرير الموارد الرسومية بعد الانتهاء
        return resizedImage;  // إعادة الصورة المعدلة
    }

    // دالة لجعل الشخصية تقفز
    public void jump() {
        if (!jumping) {  // إذا لم تكن الشخصية في حالة قفز
            yVelocity = -30;  // تعيين السرعة الرأسية للقفز (أعلى)
            jumping = true;  // تعيين حالة القفز إلى true
        }
    }

    // دالة لتحديث حالة الشخصية (مثل الحركة والقفز)
    public void update() {
        y += yVelocity;  // تعديل الموقع الرأسي بناءً على السرعة الرأسية
        yVelocity += 1;  // إضافة الجاذبية (زيادة السرعة الرأسية)

        // إذا وصلت الشخصية إلى الأرض أو الأساس
        if (y >= groundY) {
            y = groundY;  // تعيين الشخصية على الأرض
            yVelocity = 0;  // إيقاف السرعة الرأسية (وقف القفز)
            jumping = false;  // تعيين حالة القفز إلى false
        }

        // إذا لم تكن الشخصية تقفز، نقوم بتحديث الحركة المتحركة
        if (!jumping) {
            animationCounter++;  // زيادة عداد الحركة المتحركة
            if (animationCounter >= ANIMATION_SPEED) {  // إذا وصل العداد إلى السرعة المحددة
                animationFrame = (animationFrame + 1) % runningImages.length;  // تغيير الإطار المتحرك
                animationCounter = 0;  // إعادة تعيين العداد
            }
        }
    }

    // دالة لرسم الشخصية على الشاشة
    public void draw(Graphics g) {
        if (jumping) {
            g.drawImage(jumpingImage, x, y, null);  // إذا كانت الشخصية تقفز، نرسم صورة القفز
        } else {
            g.drawImage(runningImages[animationFrame], x, y, null);  // إذا كانت الشخصية تجري، نرسم الصورة المناسبة للجري
        }
    }

    // دالة للحصول على حدود الشخصية (مستطيل للمساعدة في الاصطدام)
    public Rectangle getBounds() {
        int margin = -82;  // تعيين هوامش الاصطدام لجعل الكشف أكثر حساسية
        return new Rectangle(x - margin, y - margin, width + 2 * margin, height + 2 * margin);  // إعادة المستطيل مع الهوامش المعدلة
    }

    // دالة للحصول على شكل متعدد الأضلاع (مستطيل) كحدود للشخصية
    public Polygon getPolygon() {
        return new Polygon(Arrays.asList(
                new Point(x, y),  // الزاوية العليا اليسرى
                new Point(x + width, y),  // الزاوية العليا اليمنى
                new Point(x + width, y + height),  // الزاوية السفلى اليمنى
                new Point(x, y + height)  // الزاوية السفلى اليسرى
        ));
    }

    public void duck() {
    }
}
