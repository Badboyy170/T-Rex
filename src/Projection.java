public class Projection {
    public double min, max;

    public Projection(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public boolean overlaps(Projection other) {
        return !((this.max/10000) < (other.min/10000) || (other.max/10000) < (this.min/10000));
    }
}