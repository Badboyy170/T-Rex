
import java.awt.*;
import java.awt.image.BufferedImage;

class Kanz{
    private int x, y;
    private BufferedImage kanzImage;

    public Kanz(int startX, int startY, BufferedImage kanzImage) {
        this.x = startX;
        this.y = startY;
        this.kanzImage = kanzImage;
    }

    public void update() {
        x -= 5;
    }

    public void draw(Graphics g) {
        g.drawImage(kanzImage, x, y, null);
    }

    public int getX() {
        return x;
    }
}
