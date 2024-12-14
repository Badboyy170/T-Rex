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
    private BufferedImage[] runningImages;
    private BufferedImage jumpingImage;
    private int animationFrame;
    private int groundY;
    private int animationCounter; // Add a counter for animation speed control
    private static final int ANIMATION_SPEED = 5; // Adjust this value to control the speed

    public T_Rex(int panelHeight) {
        x = 50;
        width = 640;
        height = 320;
        yVelocity = 0;
        jumping = false;
        animationFrame = 0;
        animationCounter = 0; // Initialize the counter
        groundY = panelHeight - height;
        y = groundY;

        // Load images
        runningImages = new BufferedImage[6];
        try {
            for (int i = 0; i < 6; i++) {
                BufferedImage originalImage = ImageIO.read(new File("Assets/raptor-run/raptor-run" + (i + 1) + ".png"));
                runningImages[i] = resizeImage(originalImage, width, height);
            }
            BufferedImage originalJumpingImage = ImageIO.read(new File("Assets/raptor-jump/raptor-jump.png"));
            jumpingImage = resizeImage(originalJumpingImage, width, height);
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

    public void update() {
        y += yVelocity;
        yVelocity += 0.5;
        if (y >= groundY) {
            y = groundY;
            yVelocity = 0;
            jumping = false;
        }
        if (!jumping) {
            animationCounter++;
            if (animationCounter >= ANIMATION_SPEED) {
                animationFrame = (animationFrame + 1) % runningImages.length;
                animationCounter = 0; // Reset the counter
            }
        }
    }

    public void draw(Graphics g) {
        if (jumping) {
            g.drawImage(jumpingImage, x, y, null);
        } else {
            g.drawImage(runningImages[animationFrame], x, y, null);
        }
    }

    public Rectangle getBounds() {
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