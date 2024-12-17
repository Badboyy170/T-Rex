import java.awt.*;
import java.awt.image.BufferedImage;

class Obstacle {
    private int x, y, width, height;
    private BufferedImage obstacleImage;
    private boolean debug;
    private int playerId; // Add player identifier

    public Obstacle(int startX, int panelHeight, BufferedImage obstacleImage, int playerId) {
        this.playerId = playerId; // Initialize player identifier
        this.x = startX;
        this.width = 170;
        this.height = 200;
        this.y = panelHeight - height; // Adjust y based on panel height
        this.obstacleImage = resizeImage(obstacleImage, width, height);
        this.debug = false; // Initialize the debug variable
    }

    public Obstacle(int startX, int panelHeight, BufferedImage obstacleImage) {
        this.x = startX;
        this.width = 170;
        this.height = 200;
        this.y = panelHeight - height; // Adjust y based on panel height
        this.obstacleImage = resizeImage(obstacleImage, width, height);
        this.debug = false; // Initialize the debug variable
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
        if (debug) {
            Polygon polygon = getPolygon();
            g.setColor(Color.RED);
            g.fillPolygon(polygon.getXPoints(), polygon.getYPoints(), polygon.getNPoints()); // Draw red background for debugging
        }
        g.drawImage(obstacleImage, x, y, null);
    }

    public int getX() {
        return x;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width + 2, height + 2);
    }

    public Polygon getPolygon() {
        //(x1,y1)top left, (x2,y2)top right, (x3,y3)bottom right, (x4,y4)bottom left
        int[] xPoints = {x - 10, x + width + 10, x + width-15, x + 25};
        int[] yPoints = {y + 40, y , y + height - 20, y + height - 20};
        return new Polygon(xPoints, yPoints, xPoints.length);
    }
}