


import java.awt.*;
import java.awt.image.BufferedImage;

class Mak {
    private int x, y;
    private BufferedImage makImage;

    public Mak(int startX, int startY, BufferedImage makImage) {
        this.x = startX;
        this.y = startY;
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
}
