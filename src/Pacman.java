import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;

import static java.lang.Math.floor;

public class Pacman extends JPanel {

    int screenWidth, screenHeight;
    private ArrayList<ArrayList<Integer>>map;
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

    private boolean justAte = false;

    private void init_pellets(){
        if(map == null){
            map = new ArrayList<ArrayList<Integer>>();
            var x_count = screenWidth / (radius * 2);
            var y_count = screenHeight / (radius * 2);
            for (int y = 0; y < y_count; y++){
                map.add(y,new ArrayList());
                for (int x = 0; x < x_count; x++){
                    map.get(y).add(1);
                }
            }
//            System.out.println(pellets);
        }
    }

    private void render_pellets(){

    }
    public void changeDir(int incX, int incY, int init_start_angle){
        this.incX = incX;
        this.incY = incY;
        this.min_start_angle = init_start_angle;
    }

    public void moveX(){
        this.prevX = this.x;
        if(this.x<screenWidth-radius && this.x>radius || this.x <= radius && this.incX>0|| this.x >= screenWidth-radius && this.incX<0){
            this.x = this.x + this.incX;
        }
    }

    public void moveY(){
        this.prevY = this.y;
        if(this.y<screenHeight-radius && this.y>radius || this.y <= radius && this.incY>0 || this.y >= screenHeight-radius && this.incY<0){
            this.y = this.y + this.incY;
        }
    }
    public Pacman(int screenWidth, int screenHeight) {
        this.x = 50;
        this.y = 50;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        init_waka_sound();
        init_pellets();
        start_animation();
    }

    public void start_animation(){
        Thread animationThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    repaint();
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
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("waka.wav"));
            wakaSound = AudioSystem.getClip();
            wakaSound.open(audioInputStream);
            FloatControl gainControl = (FloatControl) wakaSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-15.0f);
            wakaSound.start();
        } catch (Exception ex) {
            System.out.println("Error playing waka sound: " + ex.getMessage());
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

    private int find_block(int p){
       return (int) floor((double) p / (double) (radius*2));
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        moveX();
        moveY();
        if(isPacmanMoving()){
            moveMouth();

        }
        if(!wakaSound.isRunning() && justAte){
            playWakaSound();
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.YELLOW);
        g2d.fillArc(this.x - radius, this.y - radius,  20, 20, curr_start_angle, curr_end_angle);

        int x_block = find_block(this.x);

        int y_block = find_block(this.y);

        if( map.get(y_block).get(x_block) == 1){
            map.get(y_block).set(x_block, 0);
            justAte = true;
        } else {
            justAte = false;
        }

        for (int y = 0; y<map.size(); y++){
            for (int x = 0; x<map.get(y).size(); x++){
                if(map.get(y).get(x) == 1){
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x*radius*2 + radius, y*radius*2 + radius,10,10);
                }
            }
        }

    }
}
