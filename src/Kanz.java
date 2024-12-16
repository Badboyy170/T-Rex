
import java.awt.*;
import java.awt.image.BufferedImage;

class Kanz{
    private boolean debug;
    private int x, y,width,height;
    private BufferedImage kanzImage;


    public Kanz(int startX,  BufferedImage kanzImage) {
        this.x = startX;
        this.y = 370;
        this.kanzImage = kanzImage;
        this.debug=true;

    }


    public void update() {
        x -= 5;
    }

    public void draw(Graphics g) {
        if (debug) {
            Polygon polygon = getPolygon();
            g.setColor(Color.RED);
            g.fillPolygon(polygon.getXPoints(), polygon.getYPoints(), polygon.getNPoints()); // Draw red background for debugging
        }
        g.drawImage(kanzImage, x, y, null);
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
