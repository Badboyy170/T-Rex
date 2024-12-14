import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Road {
    private int x, y, width, height;
    private BufferedImage roadImage;

    public Road(int startX, int startY, int panelWidth) {
        x = startX;
        y = startY;
        width = panelWidth;
        height = 100; // Adjust the height as needed

        // Load the road image once
        try {
            BufferedImage originalImage = ImageIO.read(new File("Assets/road/road.png"));
            roadImage = resizeImage(originalImage, width, height);
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
        x -= 10; // Adjust the speed as needed
        if (x <= -width) {
            x = 0; // Reset x to create a seamless loop
        }
    }

    public void draw(Graphics g) {
        g.drawImage(roadImage, x, y, null);
        g.drawImage(roadImage, x + width, y, null); // Draw a second image to create a seamless loop
    }
}