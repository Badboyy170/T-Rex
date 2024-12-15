import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Counter {
    private BufferedImage[] numbers;
    private BufferedImage HI_img;
    private int score = 0;
    private int highScore = 0;

    int animationFrame = 0;
    int posstion_nums = 0;

    Counter(){

        // Load images
        numbers = new BufferedImage[10];
        try {
            for (int i = 0; i < numbers.length; i++) {
                BufferedImage originalImage = ImageIO.read(new File("Assets/counter/" + i + ".png"));
                numbers[i] = resizeImage(originalImage, 90, 90);
            }
            BufferedImage originalJumpingImage = ImageIO.read(new File("Assets/counter/HI.png"));
            HI_img = resizeImage(originalJumpingImage, 100, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public int getScore() {
        return score;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resizedImage;
    }


    public void updateScore() {
        score++;

        // تحديث أعلى النقاط إذا كانت النقاط الحالية أعلى
        if (score > highScore) {
            highScore = score;
        }
    }

    public void draw(Graphics g) {
        // رسم النقاط الحالية
        String scoreStr = String.valueOf(score);
        for (int i = 0; i < scoreStr.length(); i++) {
            int digit = Character.getNumericValue(scoreStr.charAt(i));
            g.drawImage(numbers[digit], 1250 + (i * 50), 25, null);
        }

        // رسم صورة "HI" وسجل أعلى النقاط
        g.drawImage(HI_img, 920, 20, null);
        String highScoreStr = String.valueOf(highScore);
        for (int i = 0; i < highScoreStr.length(); i++) {
            int digit = Character.getNumericValue(highScoreStr.charAt(i));
            g.drawImage(numbers[digit], 1000 + (i * 50), 25, null);
        }
    }
    public void resetScore() {
        score = 0; // restart score
    }


}
