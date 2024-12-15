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
    private Timer gameTimer; // مؤقت للتحكم في اللعبة
    private Timer obstacleTimer; // مؤقت لإنشاء العقبات بشكل دوري
    private Timer cloudTimer; // مؤقت لإنشاء السحب بشكل دوري
    private T_Rex tRex1; // كائن الديناصور اللاعب 1
    private T_Rex tRex2; // كائن الديناصور اللاعب 2
    private List<Obstacle> obstacles; // قائمة العقبات في اللعبة
    private List<Cloud> clouds; // قائمة السحب في اللعبة
    private Road road; // كائن الطريق
    private boolean gameOver; // حالة اللعبة (انتهت أم لا)
    private boolean paused; // حالة التوقف المؤقت
    private Random random; // مولد أرقام عشوائية

    public MPanel() {
        setBackground(Color.decode("#f8f8f8")); // تعيين لون الخلفية
        obstacles = new ArrayList<>(); // تهيئة قائمة العقبات
        clouds = new ArrayList<>(); // تهيئة قائمة السحب
        gameOver = false; // تعيين حالة اللعبة كبداية
        paused = false; // تعيين حالة التوقف كبداية
        random = new Random(); // إنشاء مولد الأرقام العشوائية

        // إضافة مستمع للأزرار
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // تحكم اللاعب الأول
                if (e.getKeyCode() == KeyEvent.VK_SPACE) { // إذا تم الضغط على مفتاح المسافة
                    tRex1.jump(); // الديناصور اللاعب 1 يقفز
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) { // إذا تم الضغط على مفتاح الأسهم للأسفل
                    tRex1.duck(); // الديناصور اللاعب 1 ينحني
                }

                // تحكم اللاعب الثاني
                if (e.getKeyCode() == KeyEvent.VK_UP) { // إذا تم الضغط على مفتاح الأسهم للأعلى
                    tRex2.jump(); // الديناصور اللاعب 2 يقفز
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) { // إذا تم الضغط على مفتاح الأسهم لليسار
                    tRex2.duck(); // الديناصور اللاعب 2 ينحني
                }

                if (e.getKeyCode() == KeyEvent.VK_R) { // إذا تم الضغط على مفتاح R
                    restartGame(); // إعادة تشغيل اللعبة
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // إذا تم الضغط على مفتاح ESCAPE
                    togglePause(); // تبديل حالة التوقف المؤقت
                }
            }
        });
        setFocusable(true); // تمكين التركيز على لوحة اللعبة
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            tRex1 = new T_Rex(getHeight()); // إنشاء كائن الديناصور اللاعب 1
            tRex2 = new T_Rex(getHeight()); // إنشاء كائن الديناصور اللاعب 2
            road = new Road(0, getHeight() - 100, getWidth()); // إنشاء كائن الطريق
            startGame(); // بدء اللعبة
        });
    }

    public void startGame() {
        gameTimer = new Timer(30, this); // مؤقت لتحديث اللعبة كل 30 مللي ثانية
        gameTimer.start(); // بدء المؤقت
        scheduleNextObstacle(); // جدولة العقبات
        scheduleNextCloud(); // جدولة السحب
    }

    private void scheduleNextObstacle() {
        int delay = 5000 + random.nextInt(5000); // تأخير عشوائي بين 5-10 ثوانٍ
        obstacleTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver && !paused) {
                    obstacles.add(new Obstacle(getWidth(), getHeight())); // إضافة عقبة جديدة
                    scheduleNextObstacle(); // جدولة العقبة التالية
                }
            }
        });
        obstacleTimer.setRepeats(false); // جعل المؤقت ينفذ مرة واحدة فقط
        obstacleTimer.start(); // بدء المؤقت
    }

    private void scheduleNextCloud() {
        int delay = 2000 + random.nextInt(3000); // تأخير عشوائي بين 2-5 ثوانٍ
        cloudTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver && !paused) {
                    clouds.add(new Cloud(getWidth(), random.nextInt(getHeight() / 2))); // إضافة سحابة جديدة
                    scheduleNextCloud(); // جدولة السحابة التالية
                }
            }
        });
        cloudTimer.setRepeats(false); // جعل المؤقت ينفذ مرة واحدة فقط
        cloudTimer.start(); // بدء المؤقت
    }

    private void restartGame() {
        gameOver = false; // إعادة تعيين حالة اللعبة
        paused = false; // إعادة تعيين حالة التوقف
        obstacles.clear(); // تفريغ قائمة العقبات
        clouds.clear(); // تفريغ قائمة السحب
        tRex1 = new T_Rex(getHeight()); // إعادة إنشاء الديناصور اللاعب 1
        tRex2 = new T_Rex(getHeight()); // إعادة إنشاء الديناصور اللاعب 2
        road = new Road(0, getHeight() - 100, getWidth()); // إعادة إنشاء الطريق
        startGame(); // بدء اللعبة من جديد
    }

    private void togglePause() {
        if (paused) {
            paused = false; // استئناف اللعبة
            gameTimer.start();
            scheduleNextObstacle();
            scheduleNextCloud();
        } else {
            paused = true; // إيقاف اللعبة
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
            road.draw(g); // رسم الطريق
        }
        for (Cloud cloud : clouds) {
            cloud.draw(g); // رسم السحب
        }
        if (tRex1 != null) {
            tRex1.draw(g); // رسم الديناصور اللاعب 1
        }
        if (tRex2 != null) {
            tRex2.draw(g); // رسم الديناصور اللاعب 2
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g); // رسم العقبات
        }
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2); // عرض رسالة انتهاء اللعبة
        } else if (paused) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Paused", getWidth() / 2 - 100, getHeight() / 2); // عرض رسالة التوقف المؤقت
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !paused) {
            tRex1.update(); // تحديث حالة الديناصور اللاعب 1
            tRex2.update(); // تحديث حالة الديناصور اللاعب 2
            road.update(); // تحديث حالة الطريق
            for (Obstacle obstacle : obstacles) {
                obstacle.update(); // تحديث حالة العقبات
                if (CollisionDetection.isColliding(tRex1.getPolygon(), obstacle.getPolygon())
                        || CollisionDetection.isColliding(tRex2.getPolygon(), obstacle.getPolygon())) {
                    gameOver = true; // تعيين حالة اللعبة كمنتهية عند التصادم
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
                cloud.update(); // تحديث حالة السحب
            }
            obstacles.removeIf(obstacle -> obstacle.getX() < 0); // إزالة العقبات التي خرجت من الشاشة
            clouds.removeIf(cloud -> cloud.getX() < 0); // إزالة السحب التي خرجت من الشاشة
            repaint(); // إعادة رسم اللعبة
        }
    }
}

