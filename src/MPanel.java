import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class MPanel extends JPanel implements ActionListener {
    private Timer gameTimer;
    private Timer obstacleTimer1;
    private Timer obstacleTimer2;
    private Timer kanzTimer1;
    private Timer kanzTimer2;
    private Timer cloudTimer1;
    private Timer cloudTimer2;
    private Timer makTimer1;
    private Timer makTimer2;
    private T_Rex tRex1;
    private T_Rex tRex2;
    private List<Obstacle> obstacles1;
    private List<Obstacle> obstacles2;
    private List<Cloud> clouds1;
    private List<Cloud> clouds2;
    private List<Kanz> kanzs1;
    private List<Kanz> kanzs2;
    private List<Mak> maks1;
    private List<Mak> maks2;
    private Road road1;
    private Road road2;
    private boolean gameOver;
    private boolean paused;
    private Random random;
    private int score1;
    private int score2;
    private int displayedScore1;
    private int displayedScore2;
    private int lives1;
    private int lives2;
    private boolean scoreSoundPlayed1;
    private boolean scoreSoundPlayed2;
    private BufferedImage obstacleImage;
    private BufferedImage cloudImage;
    private BufferedImage kanzImage;
    private BufferedImage makImage;
    private boolean darkMode = false;

    public MPanel() {
        init();
    }

    private void init() {
        setBackground(Color.decode("#f8f8f8"));
        setLayout(new BorderLayout());
        obstacles1 = new ArrayList<>();
        obstacles2 = new ArrayList<>();


        clouds1 = new ArrayList<>();
        clouds2 = new ArrayList<>();
        maks1 = new ArrayList<>();
        maks2 = new ArrayList<>();
        kanzs1 = new ArrayList<>();
        kanzs2 = new ArrayList<>();
        gameOver = false;
        paused = false;
        random = new Random();
        score1 = 0;
        score2 = 0;
        displayedScore1 = 0;
        displayedScore2 = 0;
        scoreSoundPlayed1 = false;
        scoreSoundPlayed2 = false;
        lives1 = 3;
        lives2 = 3;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> tRex1.jump();
                    case KeyEvent.VK_S -> tRex1.duck();
                    case KeyEvent.VK_UP -> tRex2.jump();
                    case KeyEvent.VK_DOWN -> tRex2.duck();
                    case KeyEvent.VK_R -> restartGame();
                    case KeyEvent.VK_ESCAPE -> togglePause();
                }
            }
        });
        setFocusable(true);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            tRex1 = new T_Rex(getHeight() / 2);
            tRex2 = new T_Rex(getHeight() / 2);
            road1 = new Road(1, getHeight() / 2 - 100, getWidth());
            road2 = new Road(1, getHeight() - 100, getWidth());

            startGame();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !paused) {
            tRex1.update();
            tRex2.update();
            road1.update();
            road2.update();
            updateObstaclesAndClouds(obstacles1, clouds1, kanzs1,maks1,tRex1, lives1, score1, scoreSoundPlayed1);
            updateObstaclesAndClouds(obstacles2, clouds2,kanzs2, maks2,tRex2, lives2, score2, scoreSoundPlayed2);

            if (displayedScore1 < score1) {
                displayedScore1 += Math.min(5, score1 - displayedScore1);
            }
            if (displayedScore2 < score2) {
                displayedScore2 += Math.min(5, score2 - displayedScore2);
            }

            if (score1 >= 1500 && !darkMode) {
                switchToDarkMode();
                darkMode = true;
            }

            repaint();
        }
    }

    private void updateObstaclesAndClouds(List<Obstacle> obstacles, List<Cloud> clouds, List<Kanz> kanzs,List<Mak> maks,T_Rex tRex, int lives, int score, boolean scoreSoundPlayed) {
        for (Obstacle obstacle : obstacles) {
            obstacle.update();
            if (CollisionDetection.isColliding(tRex.getPolygon(), obstacle.getPolygon())) {
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                    gameTimer.stop();
                }
            }
        }
        for (Cloud cloud : clouds) {
            cloud.update();
        }
        for (Kanz kanz : kanzs) {
            kanz.update();
        }
        for (Mak mak : maks) {
            mak.update();
        }

        obstacles.removeIf(obstacle -> obstacle.getX() < 0);
        clouds.removeIf(cloud -> cloud.getX() < 0);
        clouds.removeIf(kanz -> kanz.getX() < 0);
        clouds.removeIf(mak -> mak.getX() < 0);


        if (score % 100 == 0 && score != 0 && !scoreSoundPlayed) {
            SoundPlayer.playSound("Assets/sounds/score.wav");
            scoreSoundPlayed = true;
        } else if (score % 100 != 0) {
            scoreSoundPlayed = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g, road1, tRex1, obstacles1, clouds1,kanzs1,maks1 ,displayedScore1, 0, getHeight() / 2);
        drawGame(g, road2, tRex2, obstacles2, clouds2, kanzs2,maks2,displayedScore2, getHeight() / 2, getHeight());
    }

    private void drawGame(Graphics g, Road road, T_Rex tRex, List<Obstacle> obstacles, List<Cloud> clouds,List<Kanz> kanzs ,List<Mak> maks,int score, int yStart, int yEnd) {
        g.setClip(0, yStart, getWidth(), yEnd - yStart);
        if (road != null) {
            road.draw(g);
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

        if (tRex != null) {
            tRex.draw(g);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 10, yStart + 30);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over", getWidth() / 2 - 100, (yStart + yEnd) / 2);
        }
    }

    private void startGame() {
        gameTimer = new Timer(30, this);
        gameTimer.start();
        scheduleNextObstacle(obstacles1);
        scheduleNextObstacle(obstacles2);
        scheduleNextCloud(clouds1);
        scheduleNextCloud(clouds2);
        scheduleNextMak(maks1);
        scheduleNextMak(maks2);
        scheduleNextKanz(kanzs1);
        scheduleNextKanz(kanzs2);

        startScoreTimer();
    }

    private void scheduleNextObstacle(List<Obstacle> obstacles) {
        int delay = 5000 + random.nextInt(5000);
        Timer obstacleTimer = new Timer(delay, e -> {
            if (!gameOver && !paused) {
                obstacles.add(new Obstacle(getWidth(), getHeight() / 2, obstacleImage));
                scheduleNextObstacle(obstacles);
            }
        });
        obstacleTimer.setRepeats(false);
        obstacleTimer.start();
    }

    private void scheduleNextCloud(List<Cloud> clouds) {
        int delay = 5000 + random.nextInt(5000);
        Timer cloudTimer = new Timer(delay, e -> {
            if (!gameOver && !paused) {
                clouds.add(new Cloud(getWidth(), random.nextInt(getHeight() / 2), cloudImage));
                scheduleNextCloud(clouds);
            }
        });
        cloudTimer.setRepeats(false);
        cloudTimer.start();
    }
    private void scheduleNextMak(List<Mak> maks) {
        int delay = 5000 + random.nextInt(5000);
        Timer makTimer = new Timer(delay, e -> {
            if (!gameOver && !paused) {
                maks.add(new Mak(getWidth(), random.nextInt(getHeight() / 2), makImage));
                scheduleNextMak(maks);
            }
        });
        makTimer.setRepeats(false);
        makTimer.start();
    }
    private void scheduleNextKanz(List<Kanz> kanzs) {
        int delay = 5000 + random.nextInt(5000);
        Timer kanzTimer = new Timer(delay, e -> {
            if (!gameOver && !paused) {
                kanzs.add(new Kanz(getWidth(), random.nextInt(getHeight() / 2), kanzImage));
                scheduleNextKanz(kanzs);
            }
        });
        kanzTimer.setRepeats(false);
        kanzTimer.start();
    }

    private void switchToDarkMode() {
        setBackground(Color.decode("#2c2c2c"));
    }

    private void restartGame() {
        gameOver = false;
        paused = false;
        obstacles1.clear();
        obstacles2.clear();
        clouds1.clear();
        clouds2.clear();
        maks1.clear();
        maks2.clear();
        kanzs1.clear();
        kanzs2.clear();

        tRex1 = new T_Rex(getHeight() / 2);
        tRex2 = new T_Rex(getHeight() / 2);
        road1 = new Road(0, getHeight() / 2 - 100, getWidth());
        road2 = new Road(0, getHeight() - 100, getWidth());
        score1 = 0;
        score2 = 0;
        displayedScore1 = 0;
        displayedScore2 = 0;
        lives1 = 3;
        lives2 = 3;

        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (obstacleTimer1 != null) {
            obstacleTimer1.stop();
        }
        if (obstacleTimer2 != null) {
            obstacleTimer2.stop();
        }

        startGame();
    }

    private void togglePause() {
        if (paused) {
            paused = false;
            gameTimer.start();
            scheduleNextObstacle(obstacles1);
            scheduleNextObstacle(obstacles2);
            scheduleNextCloud(clouds1);
            scheduleNextCloud(clouds2);
            scheduleNextMak(maks1);
            scheduleNextMak(maks2);
            scheduleNextKanz(kanzs2);
            scheduleNextKanz(kanzs1);

            startScoreTimer();
        } else {
            paused = true;
            gameTimer.stop();
        }
    }

    private void startScoreTimer() {
        new Timer(1000, e -> {
            if (!gameOver && !paused) {
                score1 += 5;
                score2 += 5;
            }
        }).start();
    }
}