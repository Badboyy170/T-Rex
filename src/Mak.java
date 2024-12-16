


import java.awt.*;
import java.awt.image.BufferedImage;

class Mak {
    private int x, y ,width,height;
    private BufferedImage makImage;

    public Mak(int startX,  BufferedImage makImage) {
        this.x = startX;
        this.y = 370;
        this.makImage = makImage;
    }

    public Mak(int width, int startY) {
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
