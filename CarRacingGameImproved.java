import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class CarRacingGameImproved extends JPanel implements ActionListener, KeyListener {
    private int playerX = 180, playerY = 500;
    private int speed = 10;
    private Timer timer;
    private ArrayList<Rectangle> obstacles;
    private Random rand;
    private boolean gameOver = false;
    private int score = 0;
    private int roadSpeed = 8;
    private int lineOffset = 0;

    public CarRacingGameImproved() {
        setFocusable(true);
        setBackground(new Color(220, 230, 255));
        setPreferredSize(new Dimension(400, 600));
        addKeyListener(this);

        rand = new Random();
        obstacles = new ArrayList<>();

        timer = new Timer(25, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint roadGradient = new GradientPaint(0, 0, new Color(220, 220, 255), 0, getHeight(), new Color(180, 190, 255));
        g2.setPaint(roadGradient);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(150, 150, 255));
        g2.fillRect(120, 0, 160, getHeight());

        g2.setColor(Color.WHITE);
        for (int i = 0; i < getHeight(); i += 80)
            g2.fillRect(195, i + lineOffset, 10, 40);

        g2.setColor(new Color(100, 160, 255));
        g2.fillRoundRect(playerX, playerY, 40, 70, 10, 10);

        g2.setColor(new Color(255, 140, 140));
        for (Rectangle obs : obstacles) {
            g2.fillRoundRect(obs.x, obs.y, obs.width, obs.height, 10, 10);
        }

        g2.setColor(new Color(60, 40, 100));
        g2.setFont(new Font("Times New Roman", Font.BOLD, 22));
        g2.drawString("Score: " + score, 20, 30);

        if (gameOver) {
            g2.setColor(new Color(80, 0, 80));
            g2.setFont(new Font("Times New Roman", Font.BOLD, 36));
            g2.drawString("GAME OVER", 100, 280);
            g2.setFont(new Font("Times New Roman", Font.PLAIN, 22));
            g2.drawString("Press R to Restart", 125, 320);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            lineOffset += roadSpeed;
            if (lineOffset > 80) lineOffset = 0;

            for (int i = 0; i < obstacles.size(); i++) {
                Rectangle obs = obstacles.get(i);
                obs.y += roadSpeed;

                if (obs.y > getHeight()) {
                    obstacles.remove(i);
                    score++;
                }

                if (obs.intersects(new Rectangle(playerX, playerY, 40, 70))) {
                    gameOver = true;
                    timer.stop();
                }
            }

            if (rand.nextInt(25) == 0) {
                int xPos = 120 + rand.nextInt(120);
                obstacles.add(new Rectangle(xPos, -100, 40, 70));
            }

            roadSpeed = Math.min(20, 8 + score / 10);
            repaint();
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && playerX > 130) playerX -= speed;
        if (key == KeyEvent.VK_RIGHT && playerX < 240) playerX += speed;

        if (key == KeyEvent.VK_R && gameOver) {
            resetGame();
        }

        repaint();
    }

    private void resetGame() {
        gameOver = false;
        score = 0;
        playerX = 180;
        obstacles.clear();
        roadSpeed = 8;
        timer.start();
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Car Racing Game");
        CarRacingGameImproved game = new CarRacingGameImproved();

        frame.setIconImage(new ImageIcon("🚗").getImage());
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JOptionPane.showMessageDialog(frame,
                "Use ← and → arrows to move.\nAvoid red cars!\nPress R to restart when you crash.",
                "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}
