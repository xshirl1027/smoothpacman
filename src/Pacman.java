import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;

import static java.lang.Math.floor;

public class Pacman extends JPanel {

    int screenWidth, screenHeight;

    int screenWidthMinusRadius, screenHeightMinusRadius;
    int[][]map;
    private int move_mouth_by = 1;
    private int angle_inc = 5;
    public int init_start_angle = 45;
    private int min_start_angle =45, max_end_angle = 270;
    private int curr_start_angle = 45, curr_end_angle = 270;
    private int x, y;
    private int prevX=0, prevY=0;
    private int incX = 0, incY = 0;
    private int radius = 10;
    private int halfRadius = radius/2;
    private int diameter = radius*2;
    private int smallRadius = 3;
    private Clip wakaSound;

    private BufferedImage background;
    private boolean justAte = false;

    public Pacman(int[][]map) {
        this.map = map;
        this.x = 31;
        this.y = 171;
        this.screenHeight = map.length*20 + 20;
        this.screenWidth = map[9].length*20 + 20;
        this.screenHeightMinusRadius = this.screenHeight - radius;
        this.screenWidthMinusRadius = this.screenWidth - radius;
        renderBackground();
        start_animation();
    }

    private void renderBackground(){
        background = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = background.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, screenWidth, screenHeight);
        for (int y = 0; y<map.length; y++){
            for (int x = 0; x<map[y].length; x++){
                if(map[y][x] == 2){
                    g2d.setColor(Color.RED);
                    g2d.drawRect(x*diameter, y*diameter,diameter,diameter);
                }
            }
        }
        g2d.dispose();
//
//        // save the image to a file
//        try {
//            ImageIO.write(backgroundImage, "png", new File("background.png"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        // load the image and set it as the background
//        try {
//            backgroundImage = ImageIO.read(new File("background.png"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    public void changeDir(int incX, int incY, int init_start_angle){
        this.incX = incX;
        this.incY = incY;
        this.min_start_angle = init_start_angle;
    }

    //this should just be one function that takes in a direction that checks: is there blockage in the block in that direction?
    public boolean reachedWall_x(int incX){
        int next_pos;
        if(incX > 0){
            next_pos = this.x + incX + this.radius;
        }else if (incX < 0){
            next_pos = this.x + incX - this.radius;
        }else {
            next_pos = this.x;
        }
        int next_pos_block_x = find_block(next_pos);
        int pos_block_y = find_block(this.y+ smallRadius);
        int pos_block_y2 = find_block(this.y - smallRadius);
        if(map[pos_block_y][next_pos_block_x] == 2 || map[pos_block_y2][next_pos_block_x] == 2){
            return true;
        }else{
            return false;
        }
    }

    public boolean reachedWall_y(int incY){
        int next_pos;
        if(incY>0){
            next_pos = this.y + incY + this.radius;
        }else if (incY<0){
            next_pos = this.y + incY - this.radius;
        }else {
            next_pos = this.y;
        }
        int next_pos_block_y = find_block(next_pos);
        int pos_block_x = find_block(this.x + smallRadius);
        int pos_block_x2 = find_block(this.x - smallRadius);
        if(map[next_pos_block_y][pos_block_x] == 2 || map[next_pos_block_y][pos_block_x2] == 2){
            return true;
        }else{
            return false;
        }
    }
    public void moveX(){
        this.prevX = this.x;
        //x is between (0 + radius) and (width - radius)
        if(!reachedWall_x(this.incX) && (this.x<screenWidthMinusRadius-incX && this.x>radius ||
                this.x>=screenWidthMinusRadius && incX < 0 ||
                this.x<= radius && incX > 0)){
            this.x = this.x + this.incX;
        }
    }

    public void moveY(){
        this.prevY = this.y;
        //if y is between (0 + radius) and (height - radius)
        if(!reachedWall_y(this.incY)&&(this.y<screenHeightMinusRadius-incX && this.y>radius ||
                this.y>=screenHeightMinusRadius && incY < 0 || this.y<=radius && incY > 0)){
            this.y = this.y + this.incY;
        }
    }
    public void start_animation(){
        Thread animationThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    repaint();
//                    try {Thread.sleep(2);} catch (Exception ex) {}
                }
            }
        });
        animationThread.start();
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
       return (int) floor((double) p / (double) (diameter));
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        moveX();
        moveY();
        if(isPacmanMoving()){
            moveMouth();

        }
        if(wakaSound==null || (!wakaSound.isRunning() && justAte)){
            if(wakaSound!=null) wakaSound.close();
            playWakaSound();
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x_block = find_block(this.x);

        int y_block = find_block(this.y);

        try{
            if( map[y_block][x_block] == 1 || map[y_block][x_block] == 7){
                map[y_block][x_block]=0;
                justAte = true;
            } else {
                justAte = false;
            }
        }catch (Exception e){
            System.out.println(y_block+" "+x_block);
        }
        for (int y = 0; y<map.length; y++){
            for (int x = 0; x<map[y].length; x++){
                if(map[y][x] == 1){
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x*diameter + halfRadius, y*diameter + halfRadius,radius,radius);
                }
                if(map[y][x] == 0){             //i need to do this to maintain constant speed. otherwise pacman speeds
                    g2d.setColor(Color.BLACK); //up as pellets disappear
                    g2d.fillOval(x*diameter + halfRadius, y*diameter + halfRadius,radius,radius);
                }
                if(map[y][x] == 7){
                    g2d.setColor(Color.blue);
                    g2d.fillOval(x*diameter + halfRadius, y*diameter + halfRadius,radius,radius);
                }
            }
        }
        g2d.setColor(Color.YELLOW);
        g2d.fillArc(this.x - radius, this.y - radius,  20, 20, curr_start_angle, curr_end_angle);

    }
}
