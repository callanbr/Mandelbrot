import javax.swing.JFrame;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Color;

public class Application extends JFrame implements MouseListener, KeyListener, MouseWheelListener {
    private final int SCREEN_WIDTH = 1280;
    private final int SCREEN_HEIGHT = 720;
    private BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private double zoom = 0.008;
    private double xPos = 0;
    private double yPos = 0;
    private double zx, zy, cX, cY, tmp;
    private int maxIterations = 35;
    private int xMouseStart = 0;
    private int xMouseEnd = 0;
    private int yMouseStart = 0;
    private int yMouseEnd = 0;

    public static void main(String[] args) throws Exception {
        new Application();
    }

    public Application() {
        super("Mandelbrot");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setVisible(true);
        setResizable(true);
        addMouseListener(this);
        addKeyListener(this);
        addMouseWheelListener(this);
        compute();
    }

    private void compute() {
        image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        int modHeight = SCREEN_HEIGHT >> 1;
        int modWidth = SCREEN_WIDTH >> 1;
        for (int y = 0; y < SCREEN_HEIGHT; y++) {
            for (int x = 0; x < SCREEN_WIDTH; x++) {
                zx = zy = 0;
                cX = xPos + (x - modWidth) * zoom;
                cY = yPos + (y - modHeight) * zoom;

                int iteration;
                for (iteration = 0; iteration < maxIterations && zx * zx + zy * zy < 4; iteration++) {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY; // default 2.0 * zx * zy + cY
                    zx = tmp;
                }
                if (iteration == maxIterations) {
                    // Do nothing
                } else {

                    double r = iteration | (iteration << 2); // 2
                    while (r > 255) {
                        r -= 255;
                    }

                    double g = iteration | (iteration << 3); // 3
                    while (g > 255) {
                        g -= 255;
                    }

                    double b = iteration | (iteration << 5); // 5
                    while (b > 255) {
                        b -= 255;
                    }

                    Color color = new Color((int) r, (int) g, (int) b);
                    image.setRGB(x, y, color.getRGB());
                }
            }
        }

        repaint();
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    // MouseListener
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // System.out.println(e);
        if (e.getButton() == 1) {
            xMouseStart = e.getX();
            yMouseStart = e.getY();
        }
        System.out.println(10 * .008);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 1) {
            xMouseEnd = e.getX();
            yMouseEnd = e.getY();
            double xMove = xMouseEnd - xMouseStart;
            double yMove = yMouseEnd - yMouseStart;
            xPos = xPos - (xMove * zoom);
            yPos = yPos - (yMove * zoom);
            compute();
        }
    }

    // KeyListener
    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ADD || e.getKeyCode() == KeyEvent.VK_EQUALS) {
            zoom *= 0.9;
            compute();
        }
        if (e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
            zoom *= 1.1;
            compute();
        }
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            maxIterations += 5;
            compute();
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            maxIterations -= 5;
            compute();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // MouseWheelListener
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            System.out.println("Mouse wheel moved up " + -notches + " notches!");
        } else {
            System.out.println("Mouse wheel moved down " + notches + " notches!");
        }
    }

}
