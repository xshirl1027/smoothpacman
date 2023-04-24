import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Pacman extends JPanel {
    private int x, y;
    private int inc = 3;

    public void changeX(int inc){
        this.x = this.x + inc;
    }

    public void changeY(int inc){
        this.y = this.y + inc;
    }

    public Pacman(int x, int y) {
        this.x = x;
        this.y = y;
        Thread animationThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    repaint();
                    try {Thread.sleep(1);} catch (Exception ex) {}
                }
            }
        });

        animationThread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        var radius = 10;
        var diameter = radius * 2;
        var center = new Point(this.x, this.y);
        g.setColor(Color.YELLOW);
        g.fillOval(center.x - radius, center.y - radius, diameter, diameter);

    }
}
