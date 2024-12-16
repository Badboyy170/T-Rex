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

class MPanel extends JPanel implements ActionListener {
    private Timer gameTimer;
    private T_Rex tRex1, tRex2;
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

    public MPanel(String difficulty) {
        this.difficulty = difficulty;
        init();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            tRex1 = new T_Rex(getHeight()); // First T_Rex at the bottom
            tRex2 = new T_Rex(getHeight()/2); // Second T_Rex at the top
            road = new Road(0, getHeight() - 100, getWidth()); // Initialize the road
            counter = new Counter(getWidth(), getHeight());

            int scaledWidth = restartIcon.getIconWidth();
            int scaledHeight = restartIcon.getIconHeight();
            restartIconBounds = new Rectangle((int) (getWidth() / 2.8), getHeight() / 6, scaledWidth, scaledHeight);

            startGame();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !paused) {
            tRex1.update();
            tRex2.update();
            road.update(); // Update the road
            counter.updateScore();

            for (Obstacle obstacle : obstacles) {
                obstacle.update();
                if (CollisionDetection.isColliding(tRex1.getPolygon(), obstacle.getPolygon())) {
                    lives--;
                    if (lives >= 1) {
                        SoundPlayer.playSound("Assets/sounds/life_lost.wav");
                    }
                    if (lives <= 0) {
                        gameOver = true;
                        gameTimer.stop();
                        SoundPlayer.playSound("Assets/sounds/death.wav");
                    } else {
                        obstacles.clear();
                        clouds.clear();
                        tRex1 = new T_Rex(getHeight());
                    }
                    counter.resetScore();
                    break;
                }
                if (CollisionDetection.isColliding(tRex2.getPolygon(), obstacle.getPolygon())) {
                    lives--;
                    if (lives >= 1) {
                        SoundPlayer.playSound("Assets/sounds/life_lost.wav");
                    }
                    if (lives <= 0) {
                        gameOver = true;
                        gameTimer.stop();
                        SoundPlayer.playSound("Assets/sounds/death.wav");
                    } else {
                        obstacles.clear();
                        clouds.clear();
                        tRex2 = new T_Rex(getHeight()/2);
                    }
                    counter.resetScore();
                    break;
                }
            }

            for (birdObstacle birdObstacle : birdObstacles) {
                birdObstacle.update();
                if (CollisionDetection.isColliding(tRex1.getPolygon(), birdObstacle.getPolygon())) {
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
                        tRex1 = new T_Rex(getHeight());
                    }
                    counter.resetScore();
                    break;
                }
                if (CollisionDetection.isColliding(tRex2.getPolygon(), birdObstacle.getPolygon())) {
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
                        tRex2 = new T_Rex( getHeight() / 2);
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

            if (counter.getScore() % 500 == 0 && counter.getScore() != 0 && !scoreSoundPlayed) {
                SoundPlayer.playSound("Assets/sounds/score.wav");
                scoreSoundPlayed = true;
            } else if (counter.getScore() % 500 != 0) {
                scoreSoundPlayed = false;
            }

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
                    tRex1.jump();
                    tRex2.jump();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    tRex1.duck();
                    tRex2.duck();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    tRex1.standUp();
                    tRex2.standUp();
                }
            }
        });
        setFocusable(true);

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
                topFrame.dispose();
            }
        });

        chooseLevelButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.dispose();
            }
        });

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(continueButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(menuButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(chooseLevelButton);
        buttonPanel.setVisible(false);
        add(buttonPanel, BorderLayout.CENTER);

        gameTimer = new Timer(1000 / 60, this);
        gameTimer.start();
    }

    public void startGame() {
        gameOver = false;
        paused = false;
        scoreSoundPlayed = false;
        gameTimer.start();
    }

    public void togglePause() {
        if (paused) {
            paused = false;
            gameTimer.start();
        } else {
            paused = true;
            gameTimer.stop();
        }
    }

    public void restartGame() {
        gameOver = false;
        paused = false;
        lives = 3;
        counter.resetScore();
        obstacles.clear();
        clouds.clear();
        kanzs.clear();
        maks.clear();
        birdObstacles.clear();
        tRex1 = new T_Rex(getHeight());
        tRex2 = new T_Rex( getHeight() / 2);
        gameTimer.start();
    }

    private void hideButtons() {
        buttonPanel.setVisible(false);
    }

    private void display(Graphics g) {
        road.draw(g);
        tRex1.draw(g);
        tRex2.draw(g);
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }

        for (birdObstacle birdObstacle : birdObstacles) {
            birdObstacle.draw(g);
        }

        for (Cloud cloud : clouds) {
            cloud.draw(g);
        }
        for (Kanz kanz : kanzs) {
            kanz.draw(g);
        }
        for (Mak mak : maks) {
            mak.draw(g);
        }

        if (gameOver) {
            drawGameOverScreen(g);
        } else if (paused) {
            drawPauseScreen(g);
        }
    }

    private void drawGameOverScreen(Graphics g) {
        String gameOverMessage = "Game Over!";
        Font font = new Font("Arial", Font.BOLD, 40);
        g.setFont(font);
        g.setColor(Color.RED);
        FontMetrics metrics = g.getFontMetrics(font);
        g.drawString(gameOverMessage, getWidth() / 2 - metrics.stringWidth(gameOverMessage) / 2, getHeight() / 3);

        restartIcon = new ImageIcon("Assets/restart_icon.png");
        g.drawImage(restartIcon.getImage(), restartIconBounds.x, restartIconBounds.y, restartIconBounds.width, restartIconBounds.height, null);

        buttonPanel.setVisible(true);
    }

    private void drawPauseScreen(Graphics g) {
        String pauseMessage = "Game Paused";
        Font font = new Font("Arial", Font.BOLD, 40);
        g.setFont(font);
        g.setColor(Color.YELLOW);
        FontMetrics metrics = g.getFontMetrics(font);
        g.drawString(pauseMessage, getWidth() / 2 - metrics.stringWidth(pauseMessage) / 2, getHeight() / 3);
    }

    private void switchToDarkMode() {
        // Change the colors of all game elements to dark mode
        setBackground(Color.BLACK);

    }
}
