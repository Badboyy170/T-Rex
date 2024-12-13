import java.util.List;

public class CollisionDetection {
    public static boolean isColliding(Polygon p1, Polygon p2) {
        List<Vector2D> axes1 = p1.getAxes();
        List<Vector2D> axes2 = p2.getAxes();

        for (Vector2D axis : axes1) {
            if (!overlapOnAxis(p1, p2, axis)) {
                return false;
            }
        }

        for (Vector2D axis : axes2) {
            if (!overlapOnAxis(p1, p2, axis)) {
                return false;
            }
        }

        return true;
    }

    private static boolean overlapOnAxis(Polygon p1, Polygon p2, Vector2D axis) {
        Projection proj1 = p1.project(axis);
        Projection proj2 = p2.project(axis);
        return proj1.overlaps(proj2);
    }
}