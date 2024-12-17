import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class GamePanel extends JPanel implements ActionListener {
    private Timer gameTimer;

    private T_Rex tRex;
    private List<Obstacle> obstacles;
    private Timer obstacleTimer;
    private List<Cloud> clouds;
    private List<Kanz> kanzs;
    private List<Mak> maks;
    private List<birdObstacle> birdObstacles;
    private Timer birdObstacleTimer;

    private Road road;
    private boolean gameOver;
    private boolean paused;
    private Random random;
    private int lives;
    private JButton continueButton;
    private JButton restartButton;
    private JButton menuButton;
    private JPanel buttonPanel;
    private String difficulty;
    private ImageIcon restartIcon;
    private Rectangle restartIconBounds;
    private JButton closeButton;
    private JButton chooseLevelButton;
    private Image heartImage;
    private boolean scoreSoundPlayed;
    private BufferedImage obstacleImage;
    private BufferedImage cloudImage;
    private BufferedImage kanzImage;
    private BufferedImage makImage;

    private Timer cloudTimer;
    private Timer kanzTimer;
    private Timer makTimer;

    private boolean darkMode = false;
    private Counter counter;

    private boolean speedPotionActive = false;
    private boolean invisibilityPotionActive = false;
    private Timer speedPotionTimer;
    private Timer invisibilityPotionTimer;
    private String namePlayer;
    private int cameraY;




    public GamePanel(String difficulty, String namePlayer) {
        this.difficulty = difficulty;
        this.namePlayer = namePlayer;
        init();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            tRex = new T_Rex(getHeight(), cameraY,1);
            road = new Road(0, getHeight() - 100, getWidth()); // Initialize the road
            counter = new Counter(getWidth(),getHeight());

            int scaledWidth = restartIcon.getIconWidth();
            int scaledHeight = restartIcon.getIconHeight();
            restartIconBounds = new Rectangle((int) (getWidth() / 2.8), getHeight() / 6, scaledWidth, scaledHeight);

            startGame();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !paused) {
            cameraY += 1; // Move the camera
            tRex.update();
            road.update(); // Update the road
            counter.updateScore();
            for (Kanz kanz : kanzs) {
                kanz.update();
                if (CollisionDetection.isColliding(tRex.getPolygon(), kanz.getPolygon())) {
                    lives++;
                    kanzs.clear();
                    break;
                }
            }
            for (Mak mak : maks) {
                mak.update();
                if (CollisionDetection.isColliding(tRex.getPolygon(), mak.getPolygon())) {
                    invisibilityPotionActive=true;
                    maks.clear();
                    activateInvisibilityPotion();
                    break;
                }
            }


            for (Obstacle obstacle : obstacles) {
                obstacle.update();
                if (CollisionDetection.isColliding(tRex.getPolygon(), obstacle.getPolygon()) && !invisibilityPotionActive) {
                    lives--;
                    if (lives >= 1) {
                        SoundPlayer.playSound("Assets/sounds/life_lost.wav"); // Play life lost sound
                    }
                    if (lives <= 0) {
                        gameOver = true;
                        gameTimer.stop();
                        SoundPlayer.playSound("Assets/sounds/death.wav"); // Play death sound
                    } else {
                        obstacles.clear();
                        clouds.clear();
                        tRex = new T_Rex(getHeight(), cameraY,1);
                    }
                    counter.resetScore();
                    break;
                }
            }

            for (birdObstacle birdObstacle : birdObstacles) {
                birdObstacle.update();
                if (CollisionDetection.isColliding(tRex.getPolygon(), birdObstacle.getPolygon()) && !invisibilityPotionActive) {
                    lives--;
                    if (lives >= 1) {
                        SoundPlayer.playSound("Assets/sounds/life_lost.wav");
                    }
                    if (lives <= 0) {
                        gameOver = true;
                        gameTimer.stop();
                        SoundPlayer.playSound("Assets/sounds/death.wav");
                    } else {
                        birdObstacles.clear();
                        clouds.clear();
                        tRex = new T_Rex(getHeight(), cameraY,1);
                    }
                    counter.resetScore();
                    break;
                }
            }

            for (Cloud cloud : clouds) {
                cloud.update();
            }
            obstacles.removeIf(obstacle -> obstacle.getX() < 0);
            clouds.removeIf(cloud -> cloud.getX() < 0);
            birdObstacles.removeIf(birdObstacle -> birdObstacle.getX() < 0);
            kanzs.removeIf(kanz -> kanz.getX() < 0);
            maks.removeIf(mak -> mak.getX() < 0);


            // Play sound every 100 score gained
            if (counter.getScore() % 500 == 0 && counter.getScore() != 0 && !scoreSoundPlayed) {
                SoundPlayer.playSound("Assets/sounds/score.wav");
                scoreSoundPlayed = true;
            } else if (counter.getScore() % 500 != 0) {
                scoreSoundPlayed = false;
            }

            // Switch to dark mode when score reaches 1500
            if (counter.getScore() >= 3000 && !darkMode) {
                darkMode = true;
                switchToDarkMode();
            }

            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        display(g);
    }

    private void init() {
        setBackground(Color.decode("#f8f8f8"));
        setLayout(new BorderLayout());
        obstacles = new ArrayList<>();
        clouds = new ArrayList<>();
        kanzs = new ArrayList<>();
        maks = new ArrayList<>();
        birdObstacles = new ArrayList<>();
        gameOver = false;
        paused = false;
        random = new Random();
        scoreSoundPlayed = false;

        if (difficulty.equals("easy")) {
            lives = 3;
        } else {
            lives = 1;
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    tRex.jump();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    tRex.duck();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    tRex.standUp();
                }
            }
        });
        setFocusable(true);

        // Initialize buttons using ButtonFactory
        continueButton = ButtonFactory.createButton("Continue");
        restartButton = ButtonFactory.createButton("Restart");
        menuButton = ButtonFactory.createButton("Menu");
        closeButton = ButtonFactory.createButton("Close Game");
        //chooseLevelButton = ButtonFactory.createButton("Choose Level");

        continueButton.addActionListener(e -> {
            togglePause();
            hideButtons();
        });
        restartButton.addActionListener(e -> {
            restartGame();
            hideButtons();
            counter.resetScore();
        });

        menuButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame instanceof SinglePlayer) {
                ((SinglePlayer) topFrame).goToMenuScreen();
            }
        });

        closeButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.dispose(); // Close the game window
            }
        });
//        chooseLevelButton.addActionListener(e -> {
//            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
//            if (topFrame instanceof SinglePlayer) {
//                ((SinglePlayer) topFrame).goToMenuScreen(); // Go back to the menu screen
//            }
//        });

        // Create a panel for buttons and add them
        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        buttonPanel.add(continueButton, gbc);
        buttonPanel.add(restartButton, gbc);
        buttonPanel.add(menuButton, gbc);
        buttonPanel.add(closeButton, gbc);
       // buttonPanel.add(chooseLevelButton, gbc);

        add(buttonPanel, BorderLayout.CENTER);

        hideButtons();

        // Load the restart icon
        Image originalImage = new ImageIcon("Assets/Game-Over.png").getImage();
        int scaledWidth = originalImage.getWidth(null);
        int scaledHeight = originalImage.getHeight(null) ;
        Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        restartIcon = new ImageIcon(scaledImage);
        // Add mouse listener for restart icon
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameOver && restartIconBounds.contains(e.getPoint())) {
                    restartGame();
                    counter.resetScore();
                }
            }
        });

        try {
            // Load and resize the heart image
            Image originalHeartImage = new ImageIcon("Assets/heart.png").getImage();
            int scaleHeartdWidth = originalHeartImage.getWidth(null) / 6; // Adjust the scale factor as needed
            int scaleHeartdHeight = originalHeartImage.getHeight(null) / 6; // Adjust the scale factor as needed
            heartImage = originalHeartImage.getScaledInstance(scaleHeartdWidth, scaleHeartdHeight, Image.SCALE_SMOOTH);

            obstacleImage = ImageIO.read(new File("Assets/cactus/cactus.png"));
            kanzImage= ImageIO.read(new File("Assets/heart.png"));
            makImage = ImageIO.read(new File("Assets/potions/resetSpeed.png"));

            BufferedImage originalCloudImage = ImageIO.read(new File("Assets/cloud/cloud.png"));
            int cloudWidth = originalCloudImage.getWidth() / 2; // Adjust the scale factor as needed
            int cloudHeight = originalCloudImage.getHeight() / 2; // Adjust the scale factor as needed
            cloudImage = new BufferedImage(cloudWidth, cloudHeight, BufferedImage.TYPE_INT_ARGB);


            Graphics2D g2d = cloudImage.createGraphics();
            g2d.drawImage(originalCloudImage.getScaledInstance(cloudWidth, cloudHeight, Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void display(Graphics g) {
        if (road != null) {
            road.draw(g); // Draw the road
        }
        for (Cloud cloud : clouds) {
            cloud.draw(g);
        }
        for (birdObstacle birdObstacle : birdObstacles) {
            birdObstacle.draw(g);
        }
        for (Kanz kanz : kanzs) {
            kanz.draw(g);
        }
        for (Mak mak : maks) {
            mak.draw(g);
        }

        if (tRex != null) {
            tRex.draw(g);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }


        // Draw heart images for lives
        int heartX = 10;
        int heartY = 60;
        int heartSpacing = 10;
        for (int i = 0; i < lives; i++) {
            g.drawImage(heartImage, heartX + (i * (heartImage.getWidth(null) + heartSpacing)), 30, null);
        }
        g.setColor(Color.decode("#5271ff"));
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Name Player: " + namePlayer, 10 , 110);

        // In the display method
        if (gameOver) {

            restartIcon.paintIcon(this, g, (int) (getWidth() / 2.8), getHeight() / 6);
        }else{
//            if(counter.getScore() > 500){
//               gameTimer.setDelay(gameTimer.getDelay()-2);
//            }
//            g.setColor(Color.RED);
//            g.setFont(new Font("Arial", Font.BOLD, 36));
//            g.drawString(String.valueOf(gameTimer.getDelay()), getWidth() / 2 - 100, getHeight() / 2);
            counter.draw(g);

        }

    }


    public void startGame() {
        int gameSpeed = difficulty.equals("easy") ? 30 : 20;
        gameTimer = new Timer(gameSpeed, this);
        gameTimer.start();
        scheduleNextObstacle();
        scheduleNextbirdObstacle();

        scheduleNextCloud();
        scheduleNextKanz();
        scheduleNextMak();

        startScoreTimer();
    }

    private void switchToDarkMode() {
        setBackground(Color.decode("#2c2c2c")); // Dark background color
    }

    private void activateSpeedPotion() {
        if (speedPotionTimer != null) {
            speedPotionTimer.stop();
        }
        speedPotionActive = true;
        gameTimer.setDelay(30); // Reset game speed to normal
        speedPotionTimer = new Timer(60000, e -> speedPotionActive = false); // 1 minute
        speedPotionTimer.setRepeats(false);
        speedPotionTimer.start();
    }

    private void activateInvisibilityPotion() {
        if (invisibilityPotionTimer != null) {
            invisibilityPotionTimer.stop();
        }
        invisibilityPotionActive = true;
        invisibilityPotionTimer = new Timer(10000, e -> invisibilityPotionActive = false); // 45 seconds
        invisibilityPotionTimer.setRepeats(false);
        invisibilityPotionTimer.start();
    }


    private void scheduleNextbirdObstacle() {
        if (birdObstacleTimer != null) {
            birdObstacleTimer.stop();
        }//[5-10]
        int delay = difficulty.equals("easy") ? 10000 + random.nextInt(10000) : 5000 + random.nextInt(4000);
        birdObstacleTimer = new Timer(delay, e -> {
            if (!gameOver && !paused) {
                birdObstacles.add(new birdObstacle(getWidth(), getHeight()));
                scheduleNextbirdObstacle(); // Schedule the next bird obstacle
            }
        });
        birdObstacleTimer.setRepeats(false);
        birdObstacleTimer.start();
    }

    private void scheduleNextObstacle() {
        if (obstacleTimer != null) {
            obstacleTimer.stop();
        }//[5-10]
        int delay = difficulty.equals("easy") ? 5000 + random.nextInt(5000) : 2000 + random.nextInt(5000);
        obstacleTimer = new Timer(delay, e -> {
            if (!gameOver && !paused) {
                obstacles.add(new Obstacle(getWidth(), getHeight(), obstacleImage));
                scheduleNextObstacle(); // Schedule the next obstacle
            }
        });
        obstacleTimer.setRepeats(false);
        obstacleTimer.start();
    }


    private void scheduleNextKanz() {
        if (kanzTimer != null) {
            kanzTimer.stop();
        }
        int delay = 60000 + random.nextInt(60000);
        kanzTimer = new Timer(delay, e -> {
            if (!gameOver && !paused) {
                kanzs.add(new Kanz(getWidth() , kanzImage,1));
                scheduleNextKanz();
            }
        });
        kanzTimer.setRepeats(false);
        kanzTimer.start();
    }



    private void scheduleNextCloud() {
        if (cloudTimer != null) {
            cloudTimer.stop();
        }
        int delay = 5000 + random.nextInt(5000); // Increase the delay for cloud respawning
        cloudTimer = new Timer(delay, e -> {
            if (!gameOver && !paused) {
                clouds.add(new Cloud(getWidth(), random.nextInt(getHeight() / 2), cloudImage));
                scheduleNextCloud();
            }
        });
        cloudTimer.setRepeats(false);
        cloudTimer.start();
    }
    private void scheduleNextMak() {
        if (makTimer != null) {
            makTimer.stop();
        }
        int delay = 1000 + random.nextInt(5000);
        makTimer = new Timer(delay, e -> {
            if (!gameOver && !paused) {
                maks.add(new Mak(getWidth(),  makImage,1));
                scheduleNextMak();
            }
        });
        makTimer.setRepeats(false);
        makTimer.start();
    }


    private void startScoreTimer() {
        int scoreIncrement = difficulty.equals("easy") ? 5 : 2;
        new Timer(1000, e -> {
            if (!gameOver && !paused) {
                if (counter.getScore() % 500 == 0) {
                    increaseGameSpeed();
                }
            }
        }).start();
    }

    private void increaseGameSpeed() {
        int delayDecrement = difficulty.equals("hard") ? 2 : 1;
        int newDelay = Math.max(5, gameTimer.getDelay() - delayDecrement);
        gameTimer.setDelay(newDelay);
    }

    private void restartGame() {
        gameOver = false;
        paused = false;
        obstacles.clear();
        birdObstacles.clear();
        clouds.clear();
        kanzs.clear();
        maks.clear();
        counter.resetScore();

        tRex = new T_Rex(getHeight(), cameraY,1);
        road = new Road(0, getHeight() - 100, getWidth()); // Reinitialize the road


        if (difficulty.equals("easy")) {
            lives = 3;
        } else {
            lives = 1;
        }

        // Stop and reset all timers
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (obstacleTimer != null) {
            obstacleTimer.stop();
        }
        if (birdObstacleTimer != null) {
            birdObstacleTimer.stop();
        }
        // Start the game again
        startGame();
    }

    private void togglePause() {
        if (paused) {
            paused = false;
            gameTimer.start();
            scheduleNextObstacle();
            scheduleNextCloud();
            startScoreTimer();
            hideButtons();
        } else {
            paused = true;
            gameTimer.stop();
            if (obstacleTimer != null) {
                obstacleTimer.stop();
            }
            showButtons();
        }
    }

    private void hideButtons() {
        continueButton.setVisible(false);
        restartButton.setVisible(false);
        menuButton.setVisible(false);
        if (closeButton != null) {
            closeButton.setVisible(false);
        }
//        if (chooseLevelButton != null) {
//            chooseLevelButton.setVisible(false);
//        }
    }

    private void showButtons() {
        continueButton.setVisible(true);
        restartButton.setVisible(true);
        menuButton.setVisible(true);
        //chooseLevelButton.setVisible(true);
        closeButton.setVisible(true);
    }
}