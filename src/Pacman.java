import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Pacman extends JPanel {
    private int x, y;
    private int incX = 0;
    private int incY = 0;

    public void changeDir(int incX, int incY){
        this.incX = incX;
        this.incY = incY;
    }

    public void moveX(){
        if(this.incX!=0) this.x = this.x + this.incX;
    }

    public void moveY(){
        if(this.incY!=0) this.y = this.y+ this.incY;
    }

    public Pacman(int x, int y) {
        this.x = x;
        this.y = y;
        Thread animationThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    repaint();
                    try {Thread.sleep(10);} catch (Exception ex) {}
                }
            }
        });

        animationThread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        var radius = 10;
        var diameter = radius * 2;
        moveX();
        moveY();
        var center = new Point(this.x, this.y);
        g.setColor(Color.YELLOW);
        g.fillOval(center.x - radius, center.y - radius, diameter, diameter);

    }
}
