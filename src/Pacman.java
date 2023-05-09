import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

import static java.lang.Math.floor;

public class Pacman extends JPanel {
    int screenWidth, screenHeight;
    int screenWidthMinusRadius, screenHeightMinusRadius;
    int[][]map;
    private int move_mouth_by = 1;
    private int angle_inc = 3;
    public int init_start_angle = 45;
    private int min_start_angle =45, max_end_angle = 270;
    private int curr_start_angle = 45, curr_end_angle = 270;
    private int x, y;
    private int prevX=0, prevY=0;
    private int radius = 10;
    private int halfRadius = radius/2;
    private int diameter = radius*2;
    private int smallRadius = 5;
    private Clip wakaSound;
    private BufferedImage background;
    private boolean justAte = false;
    private int num_x_block = 0;
    private int num_y_block = 0;
    private ArrayList<Ghost> ghosts;

    private ArrayList<Color> colors;
    private int direction; //0 up 1 down 2 left 3 right
    static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
    static final int pi = 180;
    static final Hashtable<Integer, Integer> angles = new Hashtable<Integer, Integer>() {{ put(UP, 45 + pi/2); put(DOWN, 45+pi*3/2); put(LEFT, 45+pi); put(RIGHT, 45); }};
    public Pacman(int[][]map) {
        this.map = map;
        this.direction = -1; //stationary
        this.x = 31;
        this.y = 170;
        num_x_block = map[9].length;
        num_y_block = map.length;
        this.screenHeight = num_y_block*20 + 5;
        this.screenWidth = num_x_block*20 + 65;
        this.screenHeightMinusRadius = this.screenHeight - radius;
        this.screenWidthMinusRadius = this.screenWidth - radius;
        this.ghosts = new ArrayList<>();
        this.colors = new ArrayList<>(Arrays.asList(Color.RED,Color.GREEN,Color.WHITE,Color.BLUE));;
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
                if(map[y][x] == -1){
                    g2d.setColor(Color.blue);
                    g2d.fillOval(x*diameter + halfRadius, y*diameter + halfRadius,radius,radius);
                }
                if(map[y][x] == 1){
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x*diameter + halfRadius, y*diameter + halfRadius,radius,radius);
                }
                if(map[y][x] == 2){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter, y*diameter+5,diameter,radius);
                }
                if(map[y][x] == 3){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter+5, y*diameter,radius,diameter);
                }
                if(map[y][x] == 4){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter+5, y*diameter+5,diameter-5,radius);
                    g2d.drawRect(x*diameter+5, y*diameter+5,radius,diameter-5);
                }
                if(map[y][x] == 5){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter, y*diameter+5,diameter-5,radius);
                    g2d.drawRect(x*diameter+5, y*diameter+5,radius,diameter-5);
                }
                if(map[y][x] == 6){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter, y*diameter+5,diameter-5,radius);
                    g2d.drawRect(x*diameter+5, y*diameter,radius,diameter-5);
                }
                if(map[y][x] == 7){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter+5, y*diameter+5,diameter-5,radius);
                    g2d.drawRect(x*diameter+5, y*diameter,radius,diameter-5);
                }
                if(map[y][x] == -2){
                    this.ghosts.add(new Ghost(x*diameter, y*diameter, colors.get(0), null));
                    colors.remove(0);
                }
            }
        }
        g2d.dispose();
    }
    public void changeDir(int direction){
        this.direction = direction;
        this.min_start_angle = angles.get(direction);
    }

    //check if there is a wall in the direction given
    public boolean isWallThere(int direction){
        Point future_pos;
        if(direction == UP){
            future_pos = new Point(this.x, this.y - this.radius - 1);
        }else if(direction == DOWN){
            future_pos = new Point(this.x, this.y + this.radius + 1);
        }else if(direction == LEFT){
            future_pos = new Point( this.x - this.radius - 1, this.y);
        }else{
            future_pos = new Point(this.x + this.radius + 1, this.y);
        }
        if(direction == 0 || direction == 1){ //vertical check
            Point next_pos_block = find_block(future_pos);
//            int pos_block_x = find_block(this.x + smallRadius +1);
//            int pos_block_x2 = find_block(this.x - smallRadius + 1);
            try{
                if(map[next_pos_block.y][next_pos_block.x] >= 2){
                    return true;
                }else{
                    return false;
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            return false;
        }else if (direction ==  2 || direction == 3){ //horizontal check
            Point next_pos_block = find_block(future_pos);
//            int pos_block_y = find_block(this.y + smallRadius);
//            int pos_block_y2 = find_block(this.y - smallRadius);
            try{
                if(map[next_pos_block.y][next_pos_block.x] >= 2){
                    return true;
                }else{
                    return false;
                }
            }catch(Exception E){
                System.out.println(E.getMessage());
            }
            return false;
        }
        return false;
    }
    public void move(){
        this.prevX = this.x;
        this.prevY = this.y;
        //x is between (0 + radius) and (width - radius)
        if(direction == 0 && !isWallThere(UP)){
            this.y--;
        }
        if(direction == 1 && !isWallThere(DOWN)){
            this.y++;
        }
        if(direction == 2 && !isWallThere(LEFT)){
            this.x--;
        }
        if(direction == 3 && !isWallThere(RIGHT)){
            this.x++;
        }
    }

    public int[] get_surrounding_blocks(Point pos){
        var blockUP = find_block(new Point(pos.x, pos.y-radius-1));
        var blockDOWN = find_block(new Point(pos.x, pos.y+radius+1));
        var blockLEFT = find_block(new Point(pos.x-radius-1, pos.y));
        var blockRIGHT = find_block(new Point(pos.x+radius+1, pos.y));

        return new int[]{map[blockUP.y][blockUP.x],map[blockDOWN.y][blockDOWN.x],map[blockLEFT.y][blockLEFT.x],map[blockRIGHT.y][blockRIGHT.x]};
    }
    public void start_animation(){
        Thread animationThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    repaint();
                    try {
                        Thread.sleep(10);
                        move();
                        resetIfPacmanReachedEdge();
                        if(isPacmanMoving()){
                            moveMouth();
                        }
                        //code for getting surrounding block data from map
                        for (int i=0;i<ghosts.size();i++){
                            int [] surrounding_blocks = get_surrounding_blocks(ghosts.get(i).getPosition());
                            ghosts.get(i).move(surrounding_blocks);
                        }
                        if(wakaSound==null || (!wakaSound.isRunning() && justAte)){
                            if(wakaSound!=null) wakaSound.close();
                            playWakaSound();
                        }} catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
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

    private void resetIfPacmanReachedEdge(){
        if(this.x >= num_x_block*diameter-10){
            this.x=radius+1;
        }
        if(this.x < radius+1){
            this.x=(num_x_block-1)*diameter;
        }
    }
    private Point find_block(Point pos){
       return new Point((int) floor((double) pos.x / (double) (diameter)), (int) floor((double) pos.y / (double) (diameter)));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, null);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Point block = find_block(new Point(this.x, this.y));
        try{
            if( map[block.y][block.x] == 1 || map[block.y][block.x] == -1){
                map[block.y][block.x]=0;
                justAte = true;
                Graphics2D bg_g2d = background.createGraphics();
                bg_g2d.setColor(Color.BLACK);
                bg_g2d.fillOval(block.x*diameter + halfRadius-1, block.y*diameter + halfRadius-1,radius+1,radius+1);
                bg_g2d.dispose();
            } else {
                justAte = false;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        g2d.setColor(Color.YELLOW);
        g2d.fillArc(this.x - radius, this.y - radius,  20, 20, curr_start_angle, curr_end_angle);
        for(int i=0; i<ghosts.size(); i++){
            ghosts.get(i).draw(g2d);
        }
    }
}
