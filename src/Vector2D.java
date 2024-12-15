// تعريف فئة Vector2D التي تمثل متجهًا ثنائي الأبعاد
public class Vector2D {
    public double x, y;  // تعريف المتغيرات x و y لتمثيل مكونات المتجه (الإحداثيات)

    // منشئ الفئة الذي يهيئ المتجه باستخدام الإحداثيات المعطاة
    public Vector2D(double x, double y) {
        this.x = x;  // تعيين قيمة الإحداثي x
        this.y = y;  // تعيين قيمة الإحداثي y
    }

    // دالة لطرح متجه من آخر
    public Vector2D subtract(Vector2D v) {
        // إعادة متجه جديد يمثل الفرق بين المتجه الحالي والمتجه المعطى
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    // دالة لحساب المتجه العمودي على المتجه الحالي
    public Vector2D perpendicular() {
        // إعادة متجه يمثل المتجه العمودي على المتجه الحالي
        return new Vector2D(-this.y, this.x);
    }

    // دالة لحساب حاصل الضرب النقطي بين متجهين
    public double dot(Vector2D v) {
        // حساب حاصل الضرب النقطي بين المتجه الحالي والمتجه المعطى
        return this.x * v.x + this.y * v.y;
    }
}
