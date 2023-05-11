import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import static java.lang.Math.floor;
import static java.lang.Math.pow;

public class Pacman extends JPanel {
    int screenWidth, screenHeight;
    int screenWidthMinusRadius, screenHeightMinusRadius;
    int[][]map;
    double speed = 3;
    private int move_mouth_by = 1;
    private int angle_inc = 8;
    private int min_start_angle =45, max_end_angle = 270;
    private int curr_start_angle = 45, arc_angle = 270;
    private int x, y;
    private int prevX=0, prevY=0;
    private int radius = 10;
    private int halfRadius = radius/2;
    private int diameter = radius*2;
    private int smallRadius = 3;
    private Clip wakaSound;

    private Clip sirenSound;
    private Clip intro;
    private Clip deathSound;
    private Clip powerPelletSound;
    private Clip eatGhostSound;


    private boolean playedOnce=false;
    private BufferedImage background;
    private boolean justAte = false;
    private int num_x_block = 0;
    private int num_y_block = 0;
    private ArrayList<Ghost> ghosts;
    private boolean gameOver = false;
    private boolean pacmanCaught = false;
    private ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.RED,Color.PINK,Color.CYAN,Color.ORANGE));;
    private int direction; //0 up 1 down 2 left 3 right

    private int score = 0;

    //macros and static variables
    static final int HORIZONTAL=2, VERTICAL=3, TOP_LEFT=4, TOP_RIGHT=5, BOTTOM_RIGHT=6, BOTTOM_LEFT=7; //diff walls
    static final int SPECIAL_PELLET=-1, EMPTY=0, PELLET=1, GHOST=-2, FRUIT=-3;
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
        this.screenHeight = num_y_block * 20 +65;
        this.screenWidth = num_x_block * 20 + 5;
        this.screenHeightMinusRadius = this.screenHeight - radius;
        this.screenWidthMinusRadius = this.screenWidth - radius;
        this.ghosts = new ArrayList<>();
//        renderBackground();
        start_animation();
    }
    private void renderBackground(){
        background = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = background.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, screenWidth, screenHeight);
        for (int y = 0; y<map.length; y++){
            for (int x = 0; x<map[y].length; x++){
                if(map[y][x] == SPECIAL_PELLET){
                    g2d.setColor(Color.blue);
                    g2d.fillOval(x*diameter + halfRadius, y*diameter + halfRadius,radius,radius);
                }
                if(map[y][x] == PELLET){
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x*diameter + halfRadius, y*diameter + halfRadius,radius,radius);
                }
                if(map[y][x] == HORIZONTAL){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter, y*diameter+5,diameter,radius);
                }
                if(map[y][x] == VERTICAL){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter+5, y*diameter,radius,diameter);
                }
                if(map[y][x] == TOP_LEFT){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter+5, y*diameter+5,diameter-5,radius);
                    g2d.drawRect(x*diameter+5, y*diameter+5,radius,diameter-5);
                }
                if(map[y][x] == TOP_RIGHT){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter, y*diameter+5,diameter-5,radius);
                    g2d.drawRect(x*diameter+5, y*diameter+5,radius,diameter-5);
                }
                if(map[y][x] == BOTTOM_RIGHT){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter, y*diameter+5,diameter-5,radius);
                    g2d.drawRect(x*diameter+5, y*diameter,radius,diameter-5);
                }
                if(map[y][x] == BOTTOM_LEFT){
                    g2d.setColor(Color.blue);
                    g2d.drawRect(x*diameter+5, y*diameter+5,diameter-5,radius);
                    g2d.drawRect(x*diameter+5, y*diameter,radius,diameter-5);
                }
                if(map[y][x] == GHOST){
                    this.ghosts.add(new Ghost(x*diameter, y*diameter, colors.get(0), this));
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
    public boolean pacmanCaught(){
        return pacmanCaught;
    }
    //check if there is a wall in the direction given
    public boolean isWallThere(int direction){
        Point future_pos;
        Point future_pos2;
        if(direction == UP){
            future_pos = new Point(this.x - smallRadius, this.y - this.radius - 1);
            future_pos2 = new Point(this.x + smallRadius, this.y - this.radius - 1);
        }else if(direction == DOWN){
            future_pos = new Point(this.x - smallRadius, this.y + this.radius + 1);
            future_pos2 = new Point(this.x + smallRadius, this.y + this.radius + 1);
        }else if(direction == LEFT){
            future_pos = new Point( this.x - this.radius - 1, this.y + smallRadius);
            future_pos2 = new Point(this.x - this.radius - 1, this.y - smallRadius);
        }else{ //RIGHT
            future_pos = new Point(this.x + this.radius + 1, this.y + smallRadius);
            future_pos2 = new Point(this.x + this.radius + 1, this.y - smallRadius);
        }
        if(direction == 0 || direction == 1){ //vertical check
            Point next_pos_block = find_block(future_pos);
            Point next_pos_block2 = find_block(future_pos2);
            try{
                if(map[next_pos_block.y][next_pos_block.x] >= 2 || map[next_pos_block2.y][next_pos_block2.x] >= 2){
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
            Point next_pos_block2 = find_block(future_pos2);
            try{
                if(map[next_pos_block.y][next_pos_block.x] >= 2 || map[next_pos_block2.y][next_pos_block2.x] >= 2){
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
        if(pacmanCaught){
            return;
        }
        this.prevX = this.x;
        this.prevY = this.y;
        //x is between (0 + radius) and (width - radius)
        if(direction == 0 && !isWallThere(UP)){
            this.y-=speed;
        }
        if(direction == 1 && !isWallThere(DOWN)){
            this.y+=speed;
        }
        if(direction == 2 && !isWallThere(LEFT)){
            this.x-=speed;
        }
        if(direction == 3 && !isWallThere(RIGHT)){
            this.x+=speed;
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
        playIntroSound();
        renderBackground();
        Thread animationThread = new Thread(new Runnable() {
            public void run() {
                while (!gameOver) {
                    try {
                        if(intro!=null && intro.isRunning()){
                        } else{
                            if(intro!=null){
                                intro.close();
                            }
                            if(sirenSound==null || !sirenSound.isRunning()) playSirenSound();
                            repaint();
                            Thread.sleep(25);
                            move();
                            resetIfPacmanReachedEdge();
                            moveMouth();
                            //code for getting surrounding block data from map
                            if((wakaSound==null || !wakaSound.isRunning()) && justAte){
                                if(wakaSound!=null) wakaSound.close();
                                playWakaSound();
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
        animationThread.start();
    }

    public void playIntroSound(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("sounds/intro.wav"));
            intro = AudioSystem.getClip();
            intro.open(audioInputStream);
            FloatControl gainControl = (FloatControl) intro.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-15.0f);
            intro.start();
            audioInputStream.close();
        } catch (Exception ex) {
            System.out.println("Error playing sound: " + ex.getMessage());
        }
    }
    public void playPacmanDeathSound(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("sounds/pacman_death.wav"));
            deathSound = AudioSystem.getClip();
            deathSound.open(audioInputStream);
            FloatControl gainControl = (FloatControl) wakaSound.getControl(FloatControl.Type.MASTER_GAIN);
            deathSound.start();
            audioInputStream.close();
        } catch (Exception ex) {
            System.out.println("Error playing waka sound: " + ex.getMessage());
        }
    }
    public void playWakaSound(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("sounds/waka.wav"));
            wakaSound = AudioSystem.getClip();
            wakaSound.open(audioInputStream);
            FloatControl gainControl = (FloatControl) wakaSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-17.0f);
            wakaSound.start();
            audioInputStream.close();
        } catch (Exception ex) {
            System.out.println("Error playing sound: " + ex.getMessage());
        }
    }

    public void playSirenSound(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("sounds/siren.wav"));
            sirenSound = AudioSystem.getClip();
            sirenSound.open(audioInputStream);
            FloatControl gainControl = (FloatControl) wakaSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-17.0f);
            sirenSound.start();
            audioInputStream.close();
        } catch (Exception ex) {
            System.out.println("Error playing sound: " + ex.getMessage());
        }
    }

    public void playPowerSound(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("sounds/power_pellet.wav"));
            powerPelletSound = AudioSystem.getClip();
            powerPelletSound.open(audioInputStream);
            FloatControl gainControl = (FloatControl) wakaSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-17.0f);
            powerPelletSound.start();
            audioInputStream.close();
        } catch (Exception ex) {
            System.out.println("Error playing sound: " + ex.getMessage());
        }
    }

    public void playEatGhostSound(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("sounds/eat_ghost.wav"));
            eatGhostSound = AudioSystem.getClip();
            eatGhostSound.open(audioInputStream);
            FloatControl gainControl = (FloatControl) wakaSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-17.0f);
            eatGhostSound.start();
            audioInputStream.close();
        } catch (Exception ex) {
            System.out.println("Error playing sound: " + ex.getMessage());
        }
    }
    private void renderGameOver(Graphics2D g2d){
            Font myFont = new Font ("broadway", 1, 18);
            g2d.setFont(myFont);
            g2d.setColor(Color.RED);
            g2d.drawString("GAME OVER", screenWidth/2-3*diameter+radius/2, screenHeight/2-2*diameter);
    }
    private void moveMouth(){
        if(!pacmanCaught) {
            if(isPacmanMoving()){
                curr_start_angle = min_start_angle - move_mouth_by /2;
                arc_angle = max_end_angle + move_mouth_by;
                if (arc_angle >= 360 | arc_angle <= max_end_angle) {
                    angle_inc = -angle_inc;
                }
                move_mouth_by = move_mouth_by + angle_inc;
            }
        }else{
            if(curr_start_angle>0) curr_start_angle+=3;
            arc_angle -=5;
            if(arc_angle <=0){
                gameOver = true;
            }
        }
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

    private void drawBackground(Graphics g){
        g.drawImage(background, 0, 0, null);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Point block = find_block(new Point(this.x, this.y));
        try{
            drawBackground(g);
            if( map[block.y][block.x] == 1 || map[block.y][block.x] == -1){
                if(map[block.y][block.x] == -1){
                    if(powerPelletSound==null || !powerPelletSound.isRunning()) playPowerSound();
                    for(int i=0; i<ghosts.size();i++){
                        ghosts.get(i).turnOnFrightMode();
                    }
                }
                map[block.y][block.x]=0;
                score++;
                justAte = true; //this will trigger the waka sound
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
        if(arc_angle>5){
            g2d.setColor(Color.YELLOW);
            g2d.fillArc(this.x - radius, this.y - radius,  20, 20, curr_start_angle, arc_angle);
        }else{ //pacman has died, don't draw him anymore
            renderGameOver(g2d);
        }
        for(int i=0; i<ghosts.size(); i++){
            Ghost ghost = ghosts.get(i);
            Point pos = ghost.getPosition();
            ghost.draw(g2d);
            int [] surrounding_blocks = get_surrounding_blocks(new Point(pos.x+radius, pos.y+radius));
            ghost.move(surrounding_blocks);
            Rectangle rec1 = new Rectangle(pos.x, pos.y, diameter, diameter); //ghost square outline for intersection
            Rectangle rec2 = new Rectangle(x-radius, y-radius, diameter, diameter); //pacman square outline
            if(rec1.intersects(rec2)){
                if(!ghost.isFrightened()){
                    pacmanCaught = true;
                    if(!playedOnce && (deathSound==null || !deathSound.isRunning())){
                        playPacmanDeathSound();
                        playedOnce = true;
                    }
                }else {
                    score += 50;
                    ghost.setEaten();
                    if(eatGhostSound==null || !eatGhostSound.isRunning()){
                        playEatGhostSound();
                    }
                }
            }
        }
        g2d.setColor(Color.yellow);
        g2d.drawString("Score: " + score, radius, screenHeight-40);
    }
}
