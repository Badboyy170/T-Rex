import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Cloud {
    private int x, y, width, height;
    private BufferedImage cloudImage;

    public Cloud(int startX, int startY) {
        x = startX;
        y = startY;
        width = 100;
        height = 60;

        // Load the cloud image
        try {
            BufferedImage originalImage = ImageIO.read(new File("Assets/cloud/cloud.png"));
            cloudImage = resizeImage(originalImage, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        x -= 2; // Move the cloud to the left
    }

    public void draw(Graphics g) {
        g.drawImage(cloudImage, x, y, null);
    }

    public int getX() {
        return x;
    }
}