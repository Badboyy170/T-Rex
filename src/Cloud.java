import java.awt.*;
import java.awt.image.BufferedImage;

class Cloud {
    private int x, y;
    private BufferedImage cloudImage;

    public Cloud(int startX, int startY, BufferedImage cloudImage) {
        this.x = startX;
        this.y = startY;
        this.cloudImage = cloudImage;
    }


    public void update() {
        x -= 5; // Move the cloud to the left
    }

    public void draw(Graphics g) {
        g.drawImage(cloudImage, x, y, null);
    }

    public int getX() {
        return x;
    }
}