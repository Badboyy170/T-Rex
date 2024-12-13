import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Vector2D> vertices;

    public Polygon(List<Point> points) {
        vertices = new ArrayList<>();
        for (Point p : points) {
            vertices.add(new Vector2D(p.x, p.y));
        }
    }

    public List<Vector2D> getAxes() {
        List<Vector2D> axes = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Vector2D p1 = vertices.get(i);
            Vector2D p2 = vertices.get((i + 1) % vertices.size());
            Vector2D edge = p1.subtract(p2);
            axes.add(edge.perpendicular());
        }
        return axes;
    }

    public Projection project(Vector2D axis) {
        double min = axis.dot(vertices.get(0));
        double max = min;
        for (int i = 1; i < vertices.size(); i++) {
            double projection = axis.dot(vertices.get(i));
            if (projection < min) {
                min = projection;
            } else if (projection > max) {
                max = projection;
            }
        }
        return new Projection(min, max);
    }
}