import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

public class FlappyBird extends JPanel implements MouseListener, KeyListener {
    private static int Score = 0;

    // for bird
    private static int x = 150;
    private static int y = 100;
    private int width = 50;
    private int height = 30;
    private static int dx = 5;
    private static int dy = -2;
    private double gravity = 0.8;
    private Timer timer = null;

    // for piller
    private static final int PILLAR_WIDTH = 70;
    private Pillar pillar;
    private boolean pillarGenerated = false;
    private Timer pillarTimer = null;

    // for game screen and timer
    private Image backgroundImage;
    Toolkit t = Toolkit.getDefaultToolkit();
    JFrame mainframe = new JFrame("Flappy Bird By Jainil Prajapati");

    FlappyBird() {
        backgroundImage = new ImageIcon("C:/Users/user/Desktop/Temprory file to uploaded/images (2).jpg").getImage();

        JLabel l = new JLabel("", new ImageIcon("C:/Users/user/Desktop/Temprory file to uploaded/images (2).jpg"),
                JLabel.CENTER);
        l.setSize(600, 500);
        mainframe.add(l);
        mainframe.add(this);
        mainframe.setSize(600, 500);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setVisible(true);

        this.setBackground(Color.WHITE);

        mainframe.addMouseListener(this);
        mainframe.addKeyListener(this);

        this.restart();
    }

    // to making all values to their initial state
    private static void makeAllDefault() {
        x = 150;
        y = 100;
        dx = 5;
        dy = -2;
        Score = 0;
    }

    // paint method where bird, pillar, and background are set
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw the bird
        g.setColor(Color.RED);
        Image bird = t.getImage("C:/Users/user/Desktop/Temprory file to uploaded/images.png");
        g.drawImage(bird, x, y, null);

        // Draw the pillar
        g.setColor(Color.ORANGE);
        pillar.draw(g);
    }

    // restart the game and timers of the game
    private void restart() {
        pillar = new Pillar(getWidth());

        pillarTimer = new Timer(40, new ActionListener() { // Adjust the interval (in milliseconds) for new pillar
            public void actionPerformed(ActionEvent e) {
                updatePillarPosition();
            }
        });
        pillarTimer.start();

        timer = new Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBirdPosition();
                repaint();
            }
        });
        timer.start();
    }

    // showing alert to user when game is over
    private void showAlert() {
        System.out.println("Game Over");
        timer.stop();
        pillarTimer.stop();

        int option = JOptionPane.showOptionDialog(mainframe, "Highest score: " + Score, "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] { "Restart", "Exit" },
                "Restart");

        if (option == JOptionPane.YES_OPTION) {
            makeAllDefault();
            restart();
        } else if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }
    }

    // to update the pillar position and generate the pillar with new y location and
    // height pillar
    private void updatePillarPosition() {
        pillar.updatePosition();

        // Check if the pillar goes off the screen
        if (pillar.getX() + PILLAR_WIDTH <= 150) {
            pillar.reset(getWidth());
            pillarGenerated = true;
        } else {
            pillarGenerated = false;
        }
    }

    // to update bird position and showing alert when bird is touched floor or
    // ceiling or pillar
    private void updateBirdPosition() {
        // Update the bird's position due to gravity
        if ((y >= this.getHeight() - height || y <= 0)) {
            showAlert();
        } else {
            dy += 2;
            y += dy;

            if (y > (getHeight() - height)) {
                dy = -(int) (dy * gravity);
            }

            // Check for collision with the pillar
            int birdRightX = x + width;
            int birdBottomY = y + height;

            int pillarX = pillar.getX();
            int pillar1Height = pillar.getHeight1();
            int pillar2Y = pillar.getY2();

            if (birdRightX >= pillarX && x <= pillarX + PILLAR_WIDTH) {
                if (y <= pillar1Height || birdBottomY >= pillar2Y) {
                    showAlert();
                }
            }

            if (pillarX + PILLAR_WIDTH <= birdRightX) {
                Score++;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        dy = -(int) (15); // Reflect vertically

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 32) {
            dy = -(int) (15); // Reflect vertically
        }

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    public static void main(String args[]) {
        new FlappyBird();
    }

    private class Pillar {
        private int x;
        private int height1;
        private int y2;
        private int height2;

        public Pillar(int x) {
            this.x = x;
            generateRandomPillarPair();
        }

        public int getX() {
            return x;
        }

        public int getHeight1() {
            return height1;
        }

        public int getY2() {
            return y2;
        }

        public int getHeight2() {
            return height2;
        }

        public void reset(int x) {
            this.x = x;
            generateRandomPillarPair();
        }

        public void updatePosition() {
            x -= 5;
        }

        private void generateRandomPillarPair() {
            int DefaultGap = 150;
            int Piller1Height = (int) (Math.random() * (180 - 51) + 51);
            int Piller2Height = 500 - Piller1Height - DefaultGap;

            height1 = Piller1Height;
            y2 = Piller1Height + DefaultGap;
            height2 = Piller2Height;
        }

        public void draw(Graphics g) {
            g.fillRect(x, 0, PILLAR_WIDTH, height1);
            g.fillRect(x, y2, PILLAR_WIDTH, height2);
        }
    }
}
