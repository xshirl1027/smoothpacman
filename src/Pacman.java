import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;

public class Pacman extends JPanel {

    private int move_mouth_by = 1;
    private int angle_inc = 10;
    public int init_start_angle = 45;
    private int min_start_angle =45;
    private int max_end_angle = 270;
    private int curr_start_angle = 45;
    private int curr_end_angle = 270;
    private int x, y;
    private int prevX=0, prevY=0;
    private boolean isMoving = false;
    private int incX = 0, incY = 0;
    private int radius = 10;

    
    private Clip clip;
    private Clip wakaSound;

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

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("waka.wav"));
            wakaSound = AudioSystem.getClip();
            wakaSound.open(audioInputStream);
            FloatControl gainControl = (FloatControl) wakaSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-15.0f);
            wakaSound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

    public void playChompSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("waka.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        SoundPlayer soundPlayer = new SoundPlayer(clip);
        new Thread(soundPlayer).start();
    }

    private void moveMouth(){
        curr_start_angle = min_start_angle - move_mouth_by /2;
        curr_end_angle = max_end_angle + move_mouth_by;

        if(curr_end_angle >=360 | curr_end_angle <= max_end_angle){
            angle_inc = -angle_inc;
        }
        move_mouth_by = move_mouth_by + angle_inc;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println(this.x+" "+this.y+" " + this.prevX + " " + this.prevY);
        if(this.x != this.prevX || this.y != this.prevY){
            isMoving = true;
        }else{
            isMoving = false;
        }
        if(isMoving){
            moveMouth();
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
