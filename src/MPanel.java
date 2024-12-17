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
    private List<Obstacle> obstacles1, obstacles2;
    private Timer obstacleTimer1, obstacleTimer2;
    private List<Cloud> clouds1, clouds2;
    private List<Kanz> kanzs1, kanzs2;
    private List<Mak> maks1, maks2;
    private List<birdObstacle> birdObstacles1, birdObstacles2;
    private Timer birdObstacleTimer1, birdObstacleTimer2;

    private Road road1, road2;
    private boolean gameOver;
    private boolean paused;
    private Random random;
    private int lives1, lives2;
    private JButton continueButton;
    private JButton restartButton;
    private JButton menuButton;
    private JPanel buttonPanel;
    private String difficulty;
    private ImageIcon restartIcon;
    private Rectangle restartIconBounds;
    private JButton closeButton;
    //private JButton chooseLevelButton;
    private Image heartImage;
    private boolean scoreSoundPlayed;
    private BufferedImage obstacleImage;
    private BufferedImage cloudImage;
    private BufferedImage kanzImage;
    private BufferedImage makImage;

    private Timer cloudTimer1, cloudTimer2;
    private Timer kanzTimer1, kanzTimer2;
    private Timer makTimer1, makTimer2;

    private boolean darkMode = false;
    private Counter counter1, counter2;

    private boolean speedPotionActive1 = false;
    private boolean speedPotionActive2 = false;
    private boolean invisibilityPotionActive1 = false;
    private boolean invisibilityPotionActive2 = false;
    private Timer speedPotionTimer1, speedPotionTimer2;
    private Timer invisibilityPotionTimer1, invisibilityPotionTimer2;
    private String namePlayer1, namePlayer2;
    private int cameraY1, cameraY2;

    public MPanel(String difficulty, String namePlayer1, String namePlayer2) {
        this.difficulty = difficulty;
        this.namePlayer1 = namePlayer1;
        this.namePlayer2 = namePlayer2;
        init();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            tRex1 = new T_Rex(50, getHeight() / 2, cameraY1, 1); // Adjusted position for tRex1
            tRex2 = new T_Rex( getHeight() -500, cameraY2, 2); // Adjusted position for tRex2
            road1 = new Road(0, getHeight() / 2 - 100, getWidth());
            road2 = new Road(0, getHeight() - 100, getWidth());
            counter1 = new Counter(getWidth(), getHeight() / 2);
            counter2 = new Counter(getWidth());

            int scaledWidth = restartIcon.getIconWidth();
            int scaledHeight = restartIcon.getIconHeight();
            restartIconBounds = new Rectangle((int) (getWidth() / 2.8), getHeight() / 6, scaledWidth, scaledHeight);

            startGame();
        });
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !paused) {
            cameraY1 += 1;
            cameraY2 += 1;
            tRex1.update();
            tRex2.update();
            road1.update();
            road2.update();
            counter1.updateScore();
            counter2.updateScore();
            updateGameObjects(obstacles1, tRex1, kanzs1, maks1, birdObstacles1, clouds1, counter1, cameraY1, 1);
            updateGameObjects(obstacles2, tRex2, kanzs2, maks2, birdObstacles2, clouds2, counter2, cameraY2, 2);
            repaint();
        }
    }

    private void updateGameObjects(List<Obstacle> obstacles, T_Rex tRex, List<Kanz> kanzs, List<Mak> maks, List<birdObstacle> birdObstacles, List<Cloud> clouds, Counter counter, int cameraY, int playerId) {
        for (Kanz kanz : kanzs) {
            kanz.update();
            if (CollisionDetection.isColliding(tRex.getPolygon(), kanz.getPolygon())) {
                if (playerId == 1) {
                    lives1++;
                } else {
                    lives2++;
                }
                kanzs.clear();
                break;
            }
        }
        for (Mak mak : maks) {
            mak.update();
            if (CollisionDetection.isColliding(tRex.getPolygon(), mak.getPolygon())) {
                if (playerId == 1) {
                    invisibilityPotionActive1 = true;
                    activateInvisibilityPotion(1);
                } else {
                    invisibilityPotionActive2 = true;
                    activateInvisibilityPotion(2);
                }
                maks.clear();
                break;
            }
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.update();
            if (CollisionDetection.isColliding(tRex.getPolygon(), obstacle.getPolygon()) && !isInvisibilityPotionActive(playerId)) {
                if (playerId == 1) {
                    lives1--;
                } else {
                    lives2--;
                }
                if ((playerId == 1 && lives1 <= 0) || (playerId == 2 && lives2 <= 0)) {
                    gameOver = true;
                    gameTimer.stop();
                    SoundPlayer.playSound("Assets/sounds/death.wav");
                } else {
                    obstacles.clear();
                    clouds.clear();
                    if (playerId == 1) {
                        tRex1 = new T_Rex(getHeight() / 2, cameraY1, 1);
                    } else {
                        tRex2 = new T_Rex(getHeight() / 2, cameraY2, 2);
                    }
                }
                counter.resetScore();
                break;
            }
        }
        for (birdObstacle birdObstacle : birdObstacles) {
            birdObstacle.update();
            if (CollisionDetection.isColliding(tRex.getPolygon(), birdObstacle.getPolygon()) && !isInvisibilityPotionActive(playerId)) {
                if (playerId == 1) {
                    lives1--;
                } else {
                    lives2--;
                }
                if ((playerId == 1 && lives1 <= 0) || (playerId == 2 && lives2 <= 0)) {
                    gameOver = true;
                    gameTimer.stop();
                    SoundPlayer.playSound("Assets/sounds/death.wav");
                } else {
                    birdObstacles.clear();
                    clouds.clear();
                    if (playerId == 1) {
                        tRex1 = new T_Rex(getHeight() / 2, cameraY1, 1);
                    } else {
                        tRex2 = new T_Rex(getHeight() / 2, cameraY2, 2);
                    }
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        display(g);
    }

    private void init() {
        setBackground(Color.decode("#f8f8f8"));
        setLayout(new BorderLayout());
        obstacles1 = new ArrayList<>();
        obstacles2 = new ArrayList<>();
        clouds1 = new ArrayList<>();
        clouds2 = new ArrayList<>();
        kanzs1 = new ArrayList<>();
        kanzs2 = new ArrayList<>();
        maks1 = new ArrayList<>();
        maks2 = new ArrayList<>();
        birdObstacles1 = new ArrayList<>();
        birdObstacles2 = new ArrayList<>();
        gameOver = false;
        paused = false;
        random = new Random();
        scoreSoundPlayed = false;

        if (difficulty.equals("easy")) {
            lives1 = 3;
            lives2 = 3;
        } else {
            lives1 = 1;
            lives2 = 1;
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    tRex1.jump();
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    tRex1.duck();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    tRex2.jump();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    tRex2.duck();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    tRex1.standUp();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    tRex2.standUp();
                }
            }
        });
        setFocusable(true);

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
            counter1.resetScore();
            counter2.resetScore();
        });

        menuButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame instanceof MultiPlayer) {
                ((MultiPlayer) topFrame).goToMenuScreen();
            }
        });

        closeButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.dispose();
            }
        });
//        chooseLevelButton.addActionListener(e -> {
//            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
//            if (topFrame instanceof MultiPlayer) {
//                ((MultiPlayer) topFrame).goToMenuScreen();
//            }
//        });

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
        //buttonPanel.add(chooseLevelButton, gbc);

        add(buttonPanel, BorderLayout.CENTER);

        hideButtons();

        Image originalImage = new ImageIcon("Assets/Game-Over.png").getImage();
        int scaledWidth = originalImage.getWidth(null);
        int scaledHeight = originalImage.getHeight(null);
        Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        restartIcon = new ImageIcon(scaledImage);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameOver && restartIconBounds.contains(e.getPoint())) {
                    restartGame();
                    counter1.resetScore();
                    counter2.resetScore();
                }
            }
        });

        try {
            Image originalHeartImage = new ImageIcon("Assets/heart.png").getImage();
            int scaleHeartdWidth = originalHeartImage.getWidth(null) / 6;
            int scaleHeartdHeight = originalHeartImage.getHeight(null) / 6;
            heartImage = originalHeartImage.getScaledInstance(scaleHeartdWidth, scaleHeartdHeight, Image.SCALE_SMOOTH);

            obstacleImage = ImageIO.read(new File("Assets/cactus/cactus.png"));
            kanzImage = ImageIO.read(new File("Assets/heart.png"));
            makImage = ImageIO.read(new File("Assets/potions/resetSpeed.png"));

            BufferedImage originalCloudImage = ImageIO.read(new File("Assets/cloud/cloud.png"));
            int cloudWidth = originalCloudImage.getWidth() / 2;
            int cloudHeight = originalCloudImage.getHeight() / 2;
            cloudImage = new BufferedImage(cloudWidth, cloudHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = cloudImage.createGraphics();
            g2d.drawImage(originalCloudImage.getScaledInstance(cloudWidth, cloudHeight, Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void display(Graphics g) {
        if (road1 != null) {
            road1.draw(g);
        }
        if (road2 != null) {
            road2.draw(g);
        }
        for (Cloud cloud : clouds1) {
            cloud.draw(g);
        }
        for (Cloud cloud : clouds2) {
            cloud.draw(g);
        }
        for (birdObstacle birdObstacle : birdObstacles1) {
            birdObstacle.draw(g);
        }
        for (birdObstacle birdObstacle : birdObstacles2) {
            birdObstacle.draw(g);
        }
        for (Kanz kanz : kanzs1) {
            kanz.draw(g);
        }
        for (Kanz kanz : kanzs2) {
            kanz.draw(g);
        }
        for (Mak mak : maks1) {
            mak.draw(g);
        }
        for (Mak mak : maks2) {
            mak.draw(g);
        }
        for (Obstacle obstacle : obstacles1) {
            obstacle.draw(g);
        }
        for (Obstacle obstacle : obstacles2) {
            obstacle.draw(g);
        }
        tRex1.draw(g);
        tRex2.draw(g);
        counter1.draw(g);
        counter2.draw(g);
        drawLives(g,10, lives1, 1);
        drawLives(g,350, lives2, 2);
        g.setColor(Color.decode("#5271ff"));
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Name Player: " + namePlayer1, 10 , 110);
        g.drawString("Name Player: " + namePlayer2, 10 , 450);
        if (gameOver) {
            g.drawImage(restartIcon.getImage(), restartIconBounds.x, restartIconBounds.y, null);
        }
    }

    private void drawLives(Graphics g,int y1, int lives, int playerId) {
        int x = 10;
        int y = y1;
        int spacing = 10;
        Image heart = heartImage;
        for (int i = 0; i < lives; i++) {
            if (playerId == 1) {
                g.drawImage(heart, x, y, null);
            } else {
                g.drawImage(heart, x, y + 20, null);
            }
            x += heart.getWidth(null) + spacing;
        }
    }

    public void startGame() {
        gameTimer = new Timer(1000 / 60, this);
        gameTimer.start();
        obstacleTimer1 = new Timer(2000, e -> {
            int randomInt = random.nextInt(3);
            if (randomInt == 0) {
                obstacles1.add(new Obstacle(getWidth(), getHeight() / 2 - 100, obstacleImage, 1));
            } else if (randomInt == 1) {
                birdObstacles1.add(new birdObstacle(getWidth(), getHeight() / 2 - 100, 1));
            } else if (randomInt == 2) {
                kanzs1.add(new Kanz(getWidth(), kanzImage, 1));
            }
        });
        obstacleTimer1.start();
        obstacleTimer2 = new Timer(2000, e -> {
            int randomInt = random.nextInt(3);
            if (randomInt == 0) {
                obstacles2.add(new Obstacle(getWidth(), getHeight() - 100,obstacleImage, 2));
            } else if (randomInt == 1) {
                birdObstacles2.add(new birdObstacle(getWidth(), getHeight() - 100, 2));
            } else if (randomInt == 2) {
                kanzs2.add(new Kanz(getWidth(),100, kanzImage, 2));
            }
        });
        obstacleTimer2.start();
        cloudTimer1 = new Timer(5000, e -> clouds1.add(new Cloud(getWidth(), random.nextInt(200), cloudImage)));
        cloudTimer1.start();
        cloudTimer2 = new Timer(5000, e -> clouds2.add(new Cloud(getWidth(), random.nextInt(200), cloudImage)));
        cloudTimer2.start();
        makTimer1 = new Timer(10000, e -> maks1.add(new Mak(getWidth(), makImage, 1)));
        makTimer1.start();
        makTimer2 = new Timer(10000, e -> maks2.add(new Mak(getWidth(), makImage, 2)));
        makTimer2.start();
    }

    private void restartGame() {
        gameOver = false;
        lives1 = 3;
        lives2 = 3;
        obstacles1.clear();
        obstacles2.clear();
        clouds1.clear();
        clouds2.clear();
        kanzs1.clear();
        kanzs2.clear();
        maks1.clear();
        maks2.clear();
        birdObstacles1.clear();
        birdObstacles2.clear();
        cameraY1 = 0;
        cameraY2 = 0;
        tRex1 = new T_Rex(getHeight() / 2, cameraY1, 1);
        tRex2 = new T_Rex(getHeight() / 2, cameraY2, 2);
        counter1.resetScore();
        counter2.resetScore();
        gameTimer.start();
        obstacleTimer1.start();
        obstacleTimer2.start();
        cloudTimer1.start();
        cloudTimer2.start();
        makTimer1.start();
        makTimer2.start();
    }

    private void togglePause() {
        if (paused) {
            paused = false;
            gameTimer.start();
            startTimers();
            hideButtons();
        } else {
            paused = true;
            gameTimer.stop();
            stopTimers();
            showButtons();
        }
    }


    private void hideButtons() {
        continueButton.setVisible(false);
        restartButton.setVisible(false);
        menuButton.setVisible(false);
        closeButton.setVisible(false);
        //chooseLevelButton.setVisible(false);
    }

    private void showButtons() {
        continueButton.setVisible(true);
        restartButton.setVisible(true);
        menuButton.setVisible(true);
        closeButton.setVisible(true);
//        chooseLevelButton.setVisible(true);
    }

    private void activateSpeedPotion(int playerId) {
        if (playerId == 1) {
            speedPotionActive1 = true;
            speedPotionTimer1 = new Timer(5000, e -> {
                speedPotionActive1 = false;
                speedPotionTimer1.stop();
            });
            speedPotionTimer1.start();
        } else {
            speedPotionActive2 = true;
            speedPotionTimer2 = new Timer(5000, e -> {
                speedPotionActive2 = false;
                speedPotionTimer2.stop();
            });
            speedPotionTimer2.start();
        }
    }

    private void activateInvisibilityPotion(int playerId) {
        if (playerId == 1) {
            invisibilityPotionActive1 = true;
            invisibilityPotionTimer1 = new Timer(5000, e -> {
                invisibilityPotionActive1 = false;
                invisibilityPotionTimer1.stop();
            });
            invisibilityPotionTimer1.start();
        } else {
            invisibilityPotionActive2 = true;
            invisibilityPotionTimer2 = new Timer(5000, e -> {
                invisibilityPotionActive2 = false;
                invisibilityPotionTimer2.stop();
            });
            invisibilityPotionTimer2.start();
        }
    }

    private boolean isInvisibilityPotionActive(int playerId) {
        if (playerId == 1) {
            return invisibilityPotionActive1;
        } else {
            return invisibilityPotionActive2;
        }
    }

    private boolean isSpeedPotionActive(int playerId) {
        if (playerId == 1) {
            return speedPotionActive1;
        } else {
            return speedPotionActive2;
        }
    }

    private void stopTimers() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (obstacleTimer1 != null) {
            obstacleTimer1.stop();
        }
        if (obstacleTimer2 != null) {
            obstacleTimer2.stop();
        }
        if (cloudTimer1 != null) {
            cloudTimer1.stop();
        }
        if (cloudTimer2 != null) {
            cloudTimer2.stop();
        }
        if (makTimer1 != null) {
            makTimer1.stop();
        }
        if (makTimer2 != null) {
            makTimer2.stop();
        }
        if (birdObstacleTimer1 != null) {
            birdObstacleTimer1.stop();
        }
        if (birdObstacleTimer2 != null) {
            birdObstacleTimer2.stop();
        }
    }

    private void startTimers() {
        gameTimer.start();
        obstacleTimer1.start();
        obstacleTimer2.start();
        cloudTimer1.start();
        cloudTimer2.start();
        makTimer1.start();
        makTimer2.start();
        birdObstacleTimer1.start();
        birdObstacleTimer2.start();
    }

    private void stopPotionTimers(int playerId) {
        if (playerId == 1) {
            if (speedPotionTimer1 != null) {
                speedPotionTimer1.stop();
            }
            if (invisibilityPotionTimer1 != null) {
                invisibilityPotionTimer1.stop();
            }
        } else {
            if (speedPotionTimer2 != null) {
                speedPotionTimer2.stop();
            }
            if (invisibilityPotionTimer2 != null) {
                invisibilityPotionTimer2.stop();
            }
        }
    }

    private void startPotionTimers(int playerId) {
        if (playerId == 1) {
            if (speedPotionTimer1 != null) {
                speedPotionTimer1.start();
            }
            if (invisibilityPotionTimer1 != null) {
                invisibilityPotionTimer1.start();
            }
        } else {
            if (speedPotionTimer2 != null) {
                speedPotionTimer2.start();
            }
            if (invisibilityPotionTimer2 != null) {
                invisibilityPotionTimer2.start();
            }
        }
    }
}