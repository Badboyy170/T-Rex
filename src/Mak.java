


import java.awt.*;
import java.awt.image.BufferedImage;

class Mak {
    private int x, y, width, height;
    private BufferedImage makImage;
    private boolean debug;
    private int playerId; // Add player identifier

    public Mak(int startX, BufferedImage makImage, int playerId) {
        this.playerId = playerId; // Initialize player identifier
        this.x = startX;
        this.y = 370;
        this.width = 150;
        this.height = 120;
        this.debug = false;
        this.makImage = resizeImage(makImage, width, height);
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
        g.drawImage(makImage, x, y, null);
    }

    public int getX() {
        return x;
    }

    public Polygon getPolygon() {
        int[] xPoints = {x + 47, x + width - 32, x + width - 27, x + 32};
        int[] yPoints = {y + 10, y + 10, y + height - 10, y + height - 10};
        return new Polygon(xPoints, yPoints, xPoints.length);
    }
}