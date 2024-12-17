import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
class T_Rex {
    private int x, y, width, height;
    private double yVelocity;
    private boolean jumping;
    private boolean ducking;
    private BufferedImage[] runningImages;
    private BufferedImage jumpingImage;
    private BufferedImage[] duckingImages;
    private boolean debug;
    private int animationFrame;
    private int groundY;
    private int animationCounter;
    private static final int ANIMATION_SPEED = 5;
    private int cameraY;
    private int playerId; // Add player identifier

    public T_Rex(int x, int y, int cameraY, int playerId) {
        this.playerId = playerId; // Initialize player identifier
        this.x = x;
        this.y = y;
        this.width = 640;
        this.height = 340;
        this.yVelocity = 0;
        this.jumping = false;
        this.animationFrame = 0;
        this.animationCounter = 0;
        this.groundY = y;
        this.debug = false;
        this.cameraY = cameraY;

        // Load images
        runningImages = new BufferedImage[6];
        duckingImages = new BufferedImage[2];
        try {
            for (int i = 0; i < 6; i++) {
                BufferedImage originalImage = ImageIO.read(new File("Assets/raptor-run/raptor-run" + (i + 1) + ".png"));
                runningImages[i] = resizeImage(originalImage, width, height);
            }
            BufferedImage originalJumpingImage = ImageIO.read(new File("Assets/raptor-jump/raptor-jump.png"));
            jumpingImage = resizeImage(originalJumpingImage, width, height);

            for (int i = 0; i < 2; i++) {
                BufferedImage originalDuckingImage = ImageIO.read(new File("Assets/raptor-ready-pounce/raptor-ready-pounce" + (i + 1) + ".png"));
                duckingImages[i] = resizeImage(originalDuckingImage, width, height / 2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public T_Rex(int panelHeight, int cameraY, int playerId) {
        this.playerId = playerId; // Initialize player identifier
        x = 50;
        width = 640;
        height = 340;
        yVelocity = 0;
        jumping = false;
        animationFrame = 0;
        animationCounter = 0;
        groundY = panelHeight - height;
        y = groundY;
        debug = false;
        this.cameraY = cameraY;

        // Load images
        runningImages = new BufferedImage[6];
        duckingImages = new BufferedImage[2];
        try {
            for (int i = 0; i < 6; i++) {
                BufferedImage originalImage = ImageIO.read(new File("Assets/raptor-run/raptor-run" + (i + 1) + ".png"));
                runningImages[i] = resizeImage(originalImage, width, height);
            }
            BufferedImage originalJumpingImage = ImageIO.read(new File("Assets/raptor-jump/raptor-jump.png"));
            jumpingImage = resizeImage(originalJumpingImage, width, height);

            for (int i = 0; i < 2; i++) {
                BufferedImage originalDuckingImage = ImageIO.read(new File("Assets/raptor-ready-pounce/raptor-ready-pounce" + (i + 1) + ".png"));
                duckingImages[i] = resizeImage(originalDuckingImage, width, height / 2);
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

    public void jump() {
        if (!jumping) {
            yVelocity = -20;
            jumping = true;
            SoundPlayer.playSound("Assets/sounds/SFX_Jump.wav");
        }
    }

    public void duck() {
        if (!jumping) {
            ducking = true;
            y = groundY + height / 2;
        }
    }

    public void standUp() {
        if (ducking) {
            ducking = false;
            y = groundY;
        }
    }

    public void update() {
        y += yVelocity;
        yVelocity += 0.5;
        if (y >= groundY) {
            y = groundY;
            yVelocity = 0;
            jumping = false;
        }
        if (!jumping && !ducking) {
            animationCounter++;
            if (animationCounter >= ANIMATION_SPEED) {
                animationFrame = (animationFrame + 1) % runningImages.length;
                animationCounter = 0;
            }
        }
        if (ducking) {
            animationCounter++;
            if (animationCounter >= ANIMATION_SPEED) {
                animationFrame = (animationFrame + 1) % duckingImages.length;
                animationCounter = 0;
            }
        }
    }

    public void draw(Graphics g) {
        int distance = Math.max(0, Math.min(y - cameraY, 1000));
        double scale = 1.0 / (1 + distance * 0.001);
        if (debug) {
            Polygon polygon = getPolygon();
            g.setColor(Color.RED);
            g.fillPolygon(polygon.getXPoints(), polygon.getYPoints(), polygon.getNPoints());
        }
        if (jumping) {
            g.drawImage(jumpingImage, x, y, null);
        } else if (ducking) {
            g.drawImage(duckingImages[animationFrame % duckingImages.length], x, y, null);
        } else {
            g.drawImage(runningImages[animationFrame % runningImages.length], x, y, null);
        }
    }

    public Polygon getPolygon() {
        int[] xPoints;
        int[] yPoints;
        if (jumping) {
            xPoints = new int[]{x + 380, x + width - 220, x + width - 300, x + 240};
            yPoints = new int[]{y + 60, y + 60, y + height - 80, y + height - 50};
        } else if (ducking) {
            xPoints = new int[]{x + 130, x + width - 195, x + width - 195, x + 130};
            yPoints = new int[]{y + 280, y + 280, y + height, y + height};
        } else {
            xPoints = new int[]{x + 130, x + width - 195, x + width - 195, x + 130};
            yPoints = new int[]{y + 220, y + 220, y + height, y + height};
        }
        return new Polygon(xPoints, yPoints, xPoints.length);
    }
}