
import java.awt.*;
import java.awt.image.BufferedImage;

class Kanz {
    private boolean debug;
    private int x, y, width, height;
    private BufferedImage kanzImage;
    private int playerId; // Add player identifier

    public Kanz(int startX,int y1, BufferedImage kanzImage, int playerId) {
        this.playerId = playerId; // Initialize player identifier
        this.x = startX;
        this.y = y1;
        this.width = 150;
        this.height = 120;
        this.kanzImage = resizeImage(kanzImage, width, height);
        this.debug = false;
    }
    public Kanz(int startX, BufferedImage kanzImage, int playerId) {
        this.playerId = playerId; // Initialize player identifier
        this.x = startX;
        this.y = 370;
        this.width = 150;
        this.height = 120;
        this.kanzImage = resizeImage(kanzImage, width, height);
        this.debug = true;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resizedImage;
    }

    public void update() {
        x -= 5;
    }

    public void draw(Graphics g) {
        if (debug) {
            Polygon polygon = getPolygon();
            g.setColor(Color.RED);
            g.fillPolygon(polygon.getXPoints(), polygon.getYPoints(), polygon.getNPoints());
        }
        g.drawImage(kanzImage, x, y, null);
    }

    public int getX() {
        return x;
    }

    public Polygon getPolygon() {
        int[] xPoints = {x + 20, x + width - 20, x + width - 70, x + 50};
        int[] yPoints = {y + 30, y + 30, y + height - 30, y + height - 30};
        return new Polygon(xPoints, yPoints, xPoints.length);
    }
}