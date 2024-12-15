import java.util.List;

public class CollisionDetection {
    // وظيفة للتحقق مما إذا كان هناك تصادم بين مضلعين
    public static boolean isColliding(Polygon p1, Polygon p2) {
        List<Vector2D> axes1 = p1.getAxes(); // الحصول على المحاور الفريدة للمضلع الأول
        List<Vector2D> axes2 = p2.getAxes(); // الحصول على المحاور الفريدة للمضلع الثاني

        // التحقق من وجود تداخل على جميع المحاور للمضلع الأول
        for (Vector2D axis : axes1) {
            if (!overlapOnAxis(p1, p2, axis)) { // إذا لم يكن هناك تداخل على أحد المحاور، فلا يوجد تصادم
                return false;
            }
        }

        // التحقق من وجود تداخل على جميع المحاور للمضلع الثاني
        for (Vector2D axis : axes2) {
            if (!overlapOnAxis(p1, p2, axis)) { // إذا لم يكن هناك تداخل على أحد المحاور، فلا يوجد تصادم
                return false;
            }
        }

        return true; // إذا كان هناك تداخل على جميع المحاور، فإن هناك تصادمًا
    }

    // وظيفة للتحقق مما إذا كان هناك تداخل على محور معين بين مضلعين
    private static boolean overlapOnAxis(Polygon p1, Polygon p2, Vector2D axis) {
        Projection proj1 = p1.project(axis); // إسقاط المضلع الأول على المحور
        Projection proj2 = p2.project(axis); // إسقاط المضلع الثاني على المحور
        return proj1.overlaps(proj2); // التحقق مما إذا كان هناك تداخل بين الإسقاطين
    }
}
