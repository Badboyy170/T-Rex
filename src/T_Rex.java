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
    private int animationCounter; // Add a counter for animation speed control
    private static final int ANIMATION_SPEED = 5; // Adjust this value to control the speed

    public T_Rex(int panelHeight) {
        x = 50;
        width = 640;
        height = 340;
        yVelocity = 0;
        jumping = false;
        animationFrame = 0;
        animationCounter = 0; // Initialize the counter
        groundY = panelHeight - height;
        y = groundY;
        debug = true; // Initialize the debug variable

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
                duckingImages[i] = resizeImage(originalDuckingImage, width, height / 2); // Adjust height for ducking
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
            yVelocity = -20; // Reduce the jump velocity
            jumping = true;
            SoundPlayer.playSound("Assets/sounds/SFX_Jump.wav"); // Play jump sound
        }
    }
    public void duck() {
        if (!jumping) {
            ducking = true;
            height /= 2; // Reduce height to half when ducking
            y = groundY + height; // Adjust y position
        }
    }

    public void standUp() {
        if (ducking) {
            ducking = false;
            y = groundY; // Reset y position
            height *= 2; // Reset height
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
                animationCounter = 0; // Reset the counter
            }
        }
        if (ducking) {
            animationCounter++;
            if (animationCounter >= ANIMATION_SPEED) {
                animationFrame = (animationFrame + 1) % duckingImages.length;
                animationCounter = 0; // Reset the counter
            }
        }
    }

    public void draw(Graphics g) {
        if (debug) {
            Polygon polygon = getPolygon();
            g.setColor(Color.RED);
            g.fillPolygon(polygon.getXPoints(), polygon.getYPoints(), polygon.getNPoints()); // Draw red background for debugging
        }
        if (jumping) {
            g.drawImage(jumpingImage, x, y, null);
        } else if (ducking) {
            g.drawImage(duckingImages[animationFrame % duckingImages.length], x, y, null);
        } else {
            g.drawImage(runningImages[animationFrame % runningImages.length], x, y, null);
        }
    }
    /*
    Top left: (x, y)
    Top right: (x + width, y)
    Bottom right: (x + width, y + height)
    Bottom left: (x, y + height)
     */
    public Polygon getPolygon() {
        int[] xPoints;
        int[] yPoints;
        if (jumping) {
            xPoints = new int[]{x + 380, x + width - 220, x + width - 300, x + 240};
            yPoints = new int[]{y + 60, y + 60, y + height - 80, y + height - 50};
        } else {
            xPoints = new int[]{x + 130, x + width - 195, x + width - 195, x + 130};
            yPoints = new int[]{y + 220, y + 220, y + height, y + height};
        }
        return new Polygon(xPoints, yPoints, xPoints.length);
    }
}