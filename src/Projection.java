public class Projection {
    public double min, max; // القيم الدنيا والعظمى للإسقاط على محور معين

    // المُنشئ: يقوم بتعيين القيم الدنيا والعظمى للإسقاط
    public Projection(double min, double max) {
        this.min = min; // تعيين القيمة الدنيا
        this.max = max; // تعيين القيمة العظمى
    }

    // وظيفة للتحقق من وجود تداخل بين إسقاطين
    public boolean overlaps(Projection other) {
        // التداخل يحدث إذا لم تكن النهاية العظمى لهذا الإسقاط أقل من البداية الدنيا للإسقاط الآخر
        // أو إذا لم تكن النهاية العظمى للإسقاط الآخر أقل من البداية الدنيا لهذا الإسقاط
        return !(this.max < other.min || other.max < this.min);
    }
}
