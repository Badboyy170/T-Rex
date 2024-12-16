import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
class birdObstacle {
    private int x, y, width, height;
    private BufferedImage[] flyingImages;
    private int animationFrame;
    private int animationCounter; // Add a counter for animation speed control
    private static final int ANIMATION_SPEED = 5;

    public birdObstacle(int startX, int panelHeight) {
        this.x = startX;
        this.width = 150;
        this.height = 120;
        this.y = panelHeight - height; // Adjust y based on panel height
        flyingImages = new BufferedImage[6];
        try {
            for (int i = 0; i < 6; i++) {
                BufferedImage originalImage = ImageIO.read(new File("Assets/bird/" + (i + 1) + ".png"));
                flyingImages[i] = resizeImage(originalImage, width, height);
            }
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
        x -= 20;
        animationCounter++;
        if (animationCounter >= ANIMATION_SPEED) {
            animationFrame = (animationFrame + 1) % flyingImages.length;
            animationCounter = 0; // Reset the counter
        }
    }

    public void draw(Graphics g) {
        g.drawImage(flyingImages[animationFrame], x, y, null);
    }

    public int getX() {
        return x;
    }

    public Polygon getPolygon() {
        return new Polygon(Arrays.asList(
                new Point(x, y),
                new Point(x + width, y),
                new Point(x + width, y + height),
                new Point(x, y + height)
        ));
    }
}
