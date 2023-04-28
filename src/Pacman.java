import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Pacman extends JPanel {

    private Hashtable<Integer, java.util.List> pellets;
    private int move_mouth_by = 1;
    private int angle_inc = 5;
    public int init_start_angle = 45;
    private int min_start_angle =45, max_end_angle = 270;
    private int curr_start_angle = 45, curr_end_angle = 270;
    private int x, y;
    private int prevX=0, prevY=0;
    private int incX = 0, incY = 0;
    private int radius = 10;
    private Clip wakaSound;

    private void init_pellets(){
        if(pellets == null){
            pellets = new Hashtable<>();
            var x_count = getWidth() / (radius * 2);
            var y_count = getHeight() / (radius * 2);
            for (int y = 0; y < y_count; y++){
                pellets.put(y,new ArrayList());
                for (int x = 0; x < x_count; x++){
                    pellets.get(y).add(1);
                }
            }
            System.out.println(pellets);
        }
    }
    public void changeDir(int incX, int incY, int init_start_angle){
        this.incX = incX;
        this.incY = incY;
        this.min_start_angle = init_start_angle;
    }

    public void moveX(){
        this.prevX = this.x;
        if(this.x<getWidth()-radius && this.x>radius || this.x <= radius && this.incX>0|| this.x >= getWidth()-radius && this.incX<0){
            this.x = this.x + this.incX;
        }
    }

    public void moveY(){
        this.prevY = this.y;
        if(this.y<getHeight()-radius && this.y>radius || this.y <= radius && this.incY>0 || this.y >= getHeight()-radius && this.incY<0){
            this.y = this.y + this.incY;
        }
    }

    public Pacman(int x, int y) {
        this.x = x;
        this.y = y;

        init_waka_sound();
        Thread animationThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    repaint();
                    try {Thread.sleep(5
                    );} catch (Exception ex) {}
                }
            }
        });
        animationThread.start();
    }

    public void init_waka_sound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("waka.wav"));
            wakaSound = AudioSystem.getClip();
            wakaSound.open(audioInputStream);
            FloatControl gainControl = (FloatControl) wakaSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-15.0f);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playWakaSound(){
        try {
            wakaSound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void stopWakaSound() {
        if (wakaSound != null && wakaSound.isRunning()) {
            wakaSound.stop();
        }
    }
    private void moveMouth(){
        curr_start_angle = min_start_angle - move_mouth_by /2;
        curr_end_angle = max_end_angle + move_mouth_by;

        if(curr_end_angle >=360 | curr_end_angle <= max_end_angle){
            angle_inc = -angle_inc;
        }
        move_mouth_by = move_mouth_by + angle_inc;
    }

    private boolean isPacmanMoving(){
        if(this.x != this.prevX || this.y != this.prevY){
            return true;
        }else{
            return false;
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        init_pellets();
        if(isPacmanMoving()){
            moveMouth();
            if(!wakaSound.isRunning()){
                playWakaSound();
            }
        } else{
                stopWakaSound();
        }
        moveX();
        moveY();

        var center = new Point(this.x, this.y);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.YELLOW);
        g2d.fillArc(center.x - radius, center.y - radius,  20, 20, curr_start_angle, curr_end_angle);

    }
}
