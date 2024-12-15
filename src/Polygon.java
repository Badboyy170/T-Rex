import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Vector2D> vertices; // قائمة بالنقاط التي تمثل رؤوس المضلع

    // المُنشئ: يقوم بإنشاء مضلع جديد باستخدام قائمة من النقاط
    public Polygon(List<Point> points) {
        vertices = new ArrayList<>(); // تهيئة قائمة الرؤوس
        for (Point p : points) {
            vertices.add(new Vector2D(p.x, p.y)); // تحويل كل نقطة إلى كائن Vector2D وإضافته إلى القائمة
        }
    }

    // وظيفة للحصول على جميع المحاور العمودية على حواف المضلع (تُستخدم في كشف التصادم)
    public List<Vector2D> getAxes() {
        List<Vector2D> axes = new ArrayList<>(); // قائمة لتخزين المحاور
        for (int i = 0; i < vertices.size(); i++) {
            Vector2D p1 = vertices.get(i); // الرأس الحالي
            Vector2D p2 = vertices.get((i + 1) % vertices.size()); // الرأس التالي (يتم استخدام الباقي للتكرار الدائري)
            Vector2D edge = p1.subtract(p2); // حساب الحافة بين الرأسين
            axes.add(edge.perpendicular()); // إضافة المحور العمودي على الحافة إلى القائمة
        }
        return axes; // إعادة قائمة المحاور
    }

    // وظيفة لإسقاط المضلع على محور معين وحساب الإسقاط (لأغراض كشف التصادم)
    public Projection project(Vector2D axis) {
        double min = axis.dot(vertices.get(0)); // أول إسقاط يُعتبر القيمة الدنيا
        double max = min; // أول إسقاط يُعتبر أيضًا القيمة العظمى
        for (int i = 1; i < vertices.size(); i++) {
            double projection = axis.dot(vertices.get(i)); // حساب الإسقاط للنقطة الحالية
            if (projection < min) {
                min = projection; // تحديث القيمة الدنيا إذا كانت الإسقاط أقل
            } else if (projection > max) {
                max = projection; // تحديث القيمة العظمى إذا كانت الإسقاط أكبر
            }
        }
        return new Projection(min, max); // إعادة الكائن الذي يمثل الإسقاط
    }
}
