public class Vector2D {
    public double x, y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D subtract(Vector2D v) {
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    public Vector2D perpendicular() {
        return new Vector2D(-this.y, this.x);
    }

    public double dot(Vector2D v) {
        return this.x * v.x + this.y * v.y;
    }
}