package _3D;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ParallaxLayer {
    private BufferedImage image;
    private int x, y, speed;

    public ParallaxLayer(BufferedImage image, int speed) {
        this.image = image;
        this.speed = speed;
        this.x = 0;
        this.y = 0;
    }

    public void update() {
        x -= speed;
        if (x <= -image.getWidth()) {
            x = 0;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
        g.drawImage(image, x + image.getWidth(), y, null);
    }
}