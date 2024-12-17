import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Counter {
    private BufferedImage[] numbers;
    private BufferedImage HI_img;
    private double score = 0;
    private int highScore = 0;
    private int xScoreNow, xScoreHeight, xHI, y;


    Counter(int panelWidth){

        xScoreNow = panelWidth - 250;
        xScoreHeight = panelWidth - 500;
        xHI = panelWidth - 600;

        y = 350;



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
    Counter(int panelWidth, int panelHeight){

        xScoreNow = panelWidth - 250;
        xScoreHeight = panelWidth - 500;
        xHI = panelWidth - 600;

        y = 25;



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
        return (int)score;
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
        score+=0.25;

        // تحديث أعلى النقاط إذا كانت النقاط الحالية أعلى
        if (score > highScore) {
            highScore = (int)score;
        }
    }

    public void draw(Graphics g) {
        // رسم النقاط الحالية
        String scoreStr = String.valueOf((int)score);
        for (int i = 0; i < scoreStr.length(); i++) {
            int digit = Character.getNumericValue(scoreStr.charAt(i));
            g.drawImage(numbers[digit], xScoreNow + (i * 50), y, null);
        }

        // رسم صورة "HI" وسجل أعلى النقاط
        g.drawImage(HI_img, xHI , y - 3, null);
        String highScoreStr = String.valueOf(highScore);
        for (int i = 0; i < highScoreStr.length(); i++) {
            int digit = Character.getNumericValue(highScoreStr.charAt(i));
            g.drawImage(numbers[digit], xScoreHeight + (i * 50), y, null);
        }
    }
    public void resetScore() {
        score = 0; // restart score
    }


}
