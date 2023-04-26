import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Pacman extends JPanel {

    private int mouth_angle = 1;
    private int angle_inc = 1;
    private int x, y;
    private int incX = 0;
    private int incY = 0;
    private int radius = 10;

    private int init_start_angle=45;

    private int init_end_angle=270;
    public void changeDir(int incX, int incY, int init_start_angle){
        this.incX = incX;
        this.incY = incY;
        this.init_start_angle = init_start_angle;
    }

    public void moveX(){
        if(this.x<getWidth()-radius && this.x>radius || this.x <= radius && this.incX>0|| this.x >= getWidth()-radius && this.incX<0) this.x = this.x + this.incX;
    }

    public void moveY(){
        if(this.y<getHeight()-radius && this.y>radius || this.y <= radius && this.incY>0 || this.y >= getHeight()-radius && this.incY<0) this.y = this.y+ this.incY;
    }

    public Pacman(int x, int y) {
        this.x = x;
        this.y = y;
        Thread animationThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    repaint();
                    try {Thread.sleep(7);} catch (Exception ex) {}
                }
            }
        });

        animationThread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        var start_angle = init_start_angle-mouth_angle/2;
        var end_angle = init_end_angle+mouth_angle;

        if(end_angle>=360 | end_angle<=270){
            angle_inc = -angle_inc;
        }
        mouth_angle = mouth_angle + angle_inc;

        moveX();
        moveY();
        var center = new Point(this.x, this.y);

        //g.setColor(Color.YELLOW);
        //g.fillOval(center.x - radius, center.y - radius, diameter, diameter);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.YELLOW);
        g2d.fillArc(center.x - radius, center.y - radius,  20, 20, start_angle, end_angle);

//        g2d.setColor(Color.BLACK);
//        g2d.drawArc(center.x - radius, center.y - radius, 20, 20, start_angle, end_angle);

    }
}
