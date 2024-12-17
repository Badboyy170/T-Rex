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
    private int animationCounter;
    private static final int ANIMATION_SPEED = 5;
    private boolean debug;
    private int playerId; // Add player identifier

    public birdObstacle(int startX, int panelHeight, int playerId) {
        this.playerId = playerId; // Initialize player identifier
        this.x = startX;
        this.width = 150;
        this.height = 120;
        this.y = panelHeight - height - 80; // Adjust y to be 50 pixels higher
        flyingImages = new BufferedImage[6];
        this.debug = false; // Initialize the debug variable
        try {
            for (int i = 0; i < 6; i++) {
                BufferedImage originalImage = ImageIO.read(new File("Assets/bird/" + (i + 1) + ".png"));
                flyingImages[i] = resizeImage(originalImage, width, height);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public birdObstacle(int startX, int panelHeight) {
        this.x = startX;
        this.width = 150;
        this.height = 120;
        this.y = panelHeight - height - 80; // Adjust y to be 50 pixels higher
        flyingImages = new BufferedImage[6];
        this.debug = false; // Initialize the debug variable
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
        if (debug) {
            Polygon polygon = getPolygon();
            g.setColor(Color.RED);
            g.fillPolygon(polygon.getXPoints(), polygon.getYPoints(), polygon.getNPoints()); // Draw red background for debugging
        }
        g.drawImage(flyingImages[animationFrame], x, y, null);
    }

    public int getX() {
        return x;
    }

    public Polygon getPolygon() {
        int[] xPoints = {x + 4, x + width - 20, x + width - 20, x + 10};
        int[] yPoints = {y + 4, y + 20, y + height - 20, y + height - 50};
        return new Polygon(xPoints, yPoints, xPoints.length);
    }
}