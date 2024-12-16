


import java.awt.*;
import java.awt.image.BufferedImage;

class Mak {
    private int x, y ,width,height;
    private BufferedImage makImage;

    public Mak(int startX,  BufferedImage makImage) {
        this.x = startX;
        this.y = 370;
        this.width = 150;
        this.height = 120;
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
        g.drawImage(makImage, x, y, null);
    }

    public int getX() {
        return x;
    }
    public Polygon getPolygon() {
        int[] xPoints = {x  , x + width  , x + width , x };
        int[] yPoints = {y  , y , y + height , y + height };
        return new Polygon(xPoints, yPoints, xPoints.length);
    }
}
