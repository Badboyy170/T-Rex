import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

class Obstacle {
    private int x, y, width, height;
    private BufferedImage obstacleImage;

    public Obstacle(int startX, int panelHeight) {
        x = startX;
        width = 170;
        height = 200;
        y = panelHeight - height; // Adjust y based on panel height

        // Load the obstacle image
        try {
            BufferedImage originalImage = ImageIO.read(new File("Assets/cactus/cactus.png"));
            obstacleImage = resizeImage(originalImage, width, height);
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

    public void update() {// Move the obstacle to the left
        x -= 10; // Reduce the speed of the obstacles
    }

    public void draw(Graphics g) {
        g.drawImage(obstacleImage, x, y, null);
    }

    public int getX() {
        return x;
    }

    public Rectangle getBounds() {
        int margin = -82; // Adjust this value to make the collision detection more sensitive
        return new Rectangle(x - margin, y - margin, width + 2 * margin, height + 2 * margin);
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