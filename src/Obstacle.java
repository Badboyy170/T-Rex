import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
class Obstacle {
    private int x, y, width, height;
    private BufferedImage obstacleImage;

    public Obstacle(int startX, int panelHeight, BufferedImage obstacleImage) {
        this.x = startX;
        this.width = 170;
        this.height = 200;
        this.y = panelHeight - height; // Adjust y based on panel height
        this.obstacleImage = resizeImage(obstacleImage, width, height);
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
        x -= 10; // Move the obstacle to the left
    }

    public void draw(Graphics g) {
        g.drawImage(obstacleImage, x, y, null);
    }

    public int getX() {
        return x;
    }

    public Rectangle getBounds() {
//        int margin = -82; // Adjust this value to make the collision detection more sensitive
        return new Rectangle(x , y , width + 2 , height + 2 );
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