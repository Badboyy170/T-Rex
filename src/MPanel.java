import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class MPanel extends JPanel implements ActionListener {
    private Timer gameTimer;
    private Timer obstacleTimer;
    private Timer cloudTimer;
    private T_Rex tRex1;
    private T_Rex tRex2;
    private List<Obstacle> obstacles;
    private List<Cloud> clouds;
    private Road road;
    private boolean gameOver;
    private boolean paused;
    private Random random;

    public MPanel() {
        setBackground(Color.decode("#f8f8f8"));
        obstacles = new ArrayList<>();
        clouds = new ArrayList<>();
        gameOver = false;
        paused = false;
        random = new Random();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    tRex1.jump();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    tRex1.duck();
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    tRex2.jump();
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    tRex2.duck();
                }

                if (e.getKeyCode() == KeyEvent.VK_R) {
                    restartGame();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }
        });
        setFocusable(true);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            tRex1 = new T_Rex(getHeight());
            tRex2 = new T_Rex(getHeight());
            road = new Road(0, getHeight() - 100, getWidth());
            startGame();
        });
    }

    public void startGame() {
        gameTimer = new Timer(30, this);
        gameTimer.start();
        scheduleNextObstacle();
        scheduleNextCloud();
    }

    private void scheduleNextObstacle() {
        int delay = 5000 + random.nextInt(5000);
        obstacleTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver && !paused) {
                    obstacles.add(new Obstacle(getWidth(), getHeight()));
                    scheduleNextObstacle();
                }
            }
        });
        obstacleTimer.setRepeats(false);
        obstacleTimer.start();
    }

    private void scheduleNextCloud() {
        int delay = 2000 + random.nextInt(3000);
        cloudTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver && !paused) {
                    clouds.add(new Cloud(getWidth(), random.nextInt(getHeight() / 2)));
                    scheduleNextCloud();
                }
            }
        });
        cloudTimer.setRepeats(false);
        cloudTimer.start();
    }

    private void restartGame() {
        gameOver = false;
        paused = false;
        obstacles.clear();
        clouds.clear();
        tRex1 = new T_Rex(getHeight());
        tRex2 = new T_Rex(getHeight());
        road = new Road(0, getHeight() - 100, getWidth());
        startGame();
    }

    private void togglePause() {
        if (paused) {
            paused = false;
            gameTimer.start();
            scheduleNextObstacle();
            scheduleNextCloud();
        } else {
            paused = true;
            gameTimer.stop();
            if (obstacleTimer != null) {
                obstacleTimer.stop();
            }
            if (cloudTimer != null) {
                cloudTimer.stop();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (road != null) {
            road.draw(g);
        }
        for (Cloud cloud : clouds) {
            cloud.draw(g);
        }
        if (tRex1 != null) {
            tRex1.draw(g);
        }
        if (tRex2 != null) {
            tRex2.draw(g);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2);
        } else if (paused) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Paused", getWidth() / 2 - 100, getHeight() / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !paused) {
            tRex1.update();
            tRex2.update();
            road.update();
            for (Obstacle obstacle : obstacles) {
                obstacle.update();
                if (CollisionDetection.isColliding(tRex1.getPolygon(), obstacle.getPolygon())
                        || CollisionDetection.isColliding(tRex2.getPolygon(), obstacle.getPolygon())) {
                    gameOver = true;
                    gameTimer.stop();
                    if (obstacleTimer != null) {
                        obstacleTimer.stop();
                    }
                    if (cloudTimer != null) {
                        cloudTimer.stop();
                    }
                    break;
                }
            }
            for (Cloud cloud : clouds) {
                cloud.update();
            }
            obstacles.removeIf(obstacle -> obstacle.getX() < 0);
            clouds.removeIf(cloud -> cloud.getX() < 0);
            repaint();
        }
    }
}
