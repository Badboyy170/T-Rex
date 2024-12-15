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
    private Road road;
    private boolean gameOver;
    private boolean paused;
    private Random random;
    private int score;
    private int displayedScore;
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
    private Timer cloudTimer;
    private boolean darkMode = false;



    public GamePanel(String difficulty) {
        this.difficulty = difficulty;
        init();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            tRex = new T_Rex(getHeight());
            road = new Road(0, getHeight() - 100, getWidth()); // Initialize the road

            int scaledWidth = restartIcon.getIconWidth();
            int scaledHeight = restartIcon.getIconHeight();
            restartIconBounds = new Rectangle(getWidth() / 2 - scaledWidth / 2, getHeight() / 2 + 50, scaledWidth, scaledHeight);

            startGame();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !paused) {
            tRex.update();
            road.update(); // Update the road
            for (Obstacle obstacle : obstacles) {
                obstacle.update();
                if (CollisionDetection.isColliding(tRex.getPolygon(), obstacle.getPolygon())) {
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
                        tRex = new T_Rex(getHeight());
                    }
                    break;
                }
            }
            for (Cloud cloud : clouds) {
                cloud.update();
            }
            obstacles.removeIf(obstacle -> obstacle.getX() < 0);
            clouds.removeIf(cloud -> cloud.getX() < 0);

            // Smooth score animation
            if (displayedScore < score) {
                displayedScore += Math.min(5, score - displayedScore);
            }

            // Play sound every 100 score gained
            if (score % 100 == 0 && score != 0 && !scoreSoundPlayed) {
                SoundPlayer.playSound("Assets/sounds/score.wav");
                scoreSoundPlayed = true;
            } else if (score % 100 != 0) {
                scoreSoundPlayed = false;
            }

            // Switch to dark mode when score reaches 1500
            if (score >= 1500 && !darkMode) {
                switchToDarkMode();
                darkMode = true;
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
        gameOver = false;
        paused = false;
        random = new Random();
        score = 0;
        displayedScore = 0;
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
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }
        });
        setFocusable(true);

        // Initialize buttons using ButtonFactory
        continueButton = ButtonFactory.createButton("Continue");
        restartButton = ButtonFactory.createButton("Restart");
        menuButton = ButtonFactory.createButton("Menu");
        closeButton = ButtonFactory.createButton("Close Game");
        chooseLevelButton = ButtonFactory.createButton("Choose Level");

        continueButton.addActionListener(e -> {
            togglePause();
            hideButtons();
        });
        restartButton.addActionListener(e -> {
            restartGame();
            hideButtons();
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
        chooseLevelButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame instanceof SinglePlayer) {
                ((SinglePlayer) topFrame).goToMenuScreen(); // Go back to the menu screen
            }
        });

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
        buttonPanel.add(chooseLevelButton, gbc);

        add(buttonPanel, BorderLayout.CENTER);

        hideButtons();

        // Load the restart icon
        Image originalImage = new ImageIcon("Assets/restart.png").getImage();
        int scaledWidth = originalImage.getWidth(null) / 2;
        int scaledHeight = originalImage.getHeight(null) / 2;
        Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        restartIcon = new ImageIcon(scaledImage);
        // Add mouse listener for restart icon
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameOver && restartIconBounds.contains(e.getPoint())) {
                    restartGame();
                }
            }
        });

        // Load and resize the heart image
        Image originalHeartImage = new ImageIcon("Assets/heart.png").getImage();
        int scaleHeartdWidth = originalHeartImage.getWidth(null) / 6; // Adjust the scale factor as needed
        int scaleHeartdHeight = originalHeartImage.getHeight(null) / 6; // Adjust the scale factor as needed
        heartImage = originalHeartImage.getScaledInstance(scaleHeartdWidth, scaleHeartdHeight, Image.SCALE_SMOOTH);

        // Load obstacle and cloud images once
        try {
            obstacleImage = ImageIO.read(new File("Assets/cactus/cactus.png"));
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
        if (tRex != null) {
            tRex.draw(g);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + displayedScore, 10, 30);

        // Draw heart images for lives
        int heartX = 10;
        int heartY = 60;
        int heartSpacing = 10;
        for (int i = 0; i < lives; i++) {
            g.drawImage(heartImage, heartX + (i * (heartImage.getWidth(null) + heartSpacing)), heartY, null);
        }

        // In the display method
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2);
            restartIcon.paintIcon(this, g, restartIconBounds.x, restartIconBounds.y);
        }
    }

    public void startGame() {
        int gameSpeed = difficulty.equals("easy") ? 30 : 15;
        gameTimer = new Timer(gameSpeed, this);
        gameTimer.start();
        scheduleNextObstacle();
        scheduleNextCloud();
        startScoreTimer();
    }

    private void switchToDarkMode() {
        setBackground(Color.decode("#2c2c2c")); // Dark background color
        // Update other UI elements to dark mode if needed
        // For example, change text color, button colors, etc.
        //add dark mode sound effect (later)
    }

    private void scheduleNextObstacle() {
        if (obstacleTimer != null) {
            obstacleTimer.stop();
        }
        int delay = difficulty.equals("easy") ? 5000 + random.nextInt(5000) : 2000 + random.nextInt(5000);
        obstacleTimer = new Timer(delay, e -> {
            if (!gameOver && !paused) {
                obstacles.add(new Obstacle(getWidth(), getHeight())); // Pass the correct height
                scheduleNextObstacle(); // Schedule the next obstacle
            }
        });
        obstacleTimer.setRepeats(false);
        obstacleTimer.start();
    }

    private void scheduleNextCloud() {
        if (cloudTimer != null) {
            cloudTimer.stop();
        }
        int delay = 5000 + random.nextInt(5000); // Increase the delay for cloud respawning
        cloudTimer = new Timer(delay, e -> {
            if (!gameOver && !paused) {
                clouds.add(new Cloud(getWidth(), random.nextInt(getHeight() / 2), cloudImage));
                scheduleNextCloud(); // Schedule the next cloud
            }
        });
        cloudTimer.setRepeats(false);
        cloudTimer.start();
    }

    private void startScoreTimer() {
        int scoreIncrement = difficulty.equals("easy") ? 5 : 2;
        new Timer(1000, e -> {
            if (!gameOver && !paused) {
                score += scoreIncrement;
                if (score % 100 == 0) {
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
        clouds.clear();
        tRex = new T_Rex(getHeight());
        road = new Road(0, getHeight() - 100, getWidth()); // Reinitialize the road
        score = 0;
        displayedScore = 0;

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
        if (chooseLevelButton != null) {
            chooseLevelButton.setVisible(false);
        }
    }

    private void showButtons() {
        continueButton.setVisible(true);
        restartButton.setVisible(true);
        menuButton.setVisible(true);
        chooseLevelButton.setVisible(true);
        closeButton.setVisible(true);
    }
}