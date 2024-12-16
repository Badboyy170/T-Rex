
import java.awt.*;
import java.awt.image.BufferedImage;

class Kanz{
    private int x, y;
    private BufferedImage kanzImage;
    int width ,height;

    public Kanz(int startX, int startY, BufferedImage kanzImage) {
        this.x = startX;
        this.y = startY;
        this.kanzImage = kanzImage;
        width=5000;
        height=6000;
        BufferedImage originalImage=kanzImage;
        kanzImage=resizeImage(originalImage,width,height);
        this.kanzImage=kanzImage;

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
        g.drawImage(kanzImage, x, y, null);
    }

    public int getX() {
        return x;
    }
}
