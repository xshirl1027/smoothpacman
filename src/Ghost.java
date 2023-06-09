import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Ghost {
    private int x,y;
    private int init_x, init_y;
    private int direction;
    private int prevDirection;
    private Color color;

    private Pacman pacman;
    private int speed = 3;
    private int frightModeSpeed = 5;

    private int frightModeCounter = 0;
    private int normal_speed = 4;
    private Color normal_color;

    public Ghost(int x, int y, Color color, Pacman pacman) {
        this.pacman = pacman;
        this.x = x;
        this.y = y;
        this.init_x = x;
        this.init_y = y;
        this.color = color;
        this.normal_color = color;
        this.direction = 0;
        this.prevDirection = 0;
    }

    public void turnOnFrightMode(){
        this.frightModeCounter=200;
        this.color = Color.BLUE;
        this.speed=frightModeSpeed;
    }

    public void turnOffFrightMode(){
        this.speed = normal_speed;
        this.color = normal_color;
        this.frightModeCounter = 0;
    }
    public void setEaten(){
        x=init_x;
        y=init_y;
        turnOffFrightMode();
    }
    public boolean isFrightened(){
        return frightModeCounter>0;
    }
    public void draw(Graphics2D g2d){
        int size = 20;
        // Body
        g2d.setColor(this.color);
        g2d.fillArc(x, y+1, size, size, 0, 180);
        g2d.fillRect(x, y+1 + size/2, size, size/2);
        // eyes
        int eyeSize = size / 3;
        int eyeOffset = size / 5;
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x + eyeOffset/2, y + eyeOffset+1, eyeSize, eyeSize);
        g2d.fillOval(x + size - eyeOffset/2 - eyeSize, y + eyeOffset+1, eyeSize, eyeSize);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x + eyeOffset/2 + eyeSize / 4, y + eyeOffset + eyeSize / 4 + 1, eyeSize / 2, eyeSize / 2);
        g2d.fillOval(x + size - eyeOffset/2 - eyeSize + eyeSize / 4, y + eyeOffset + eyeSize / 4 +1, eyeSize / 2, eyeSize / 2);
        frightModeCounter--;
        if(frightModeCounter==0){
            turnOffFrightMode();
        }
    }
    public void move(int[] surrounding_blocks) {
        if(pacman.pacmanCaught()){
           return;
        }
        //if there is no blockage in the block in the direction of movement, move ghost by one block in that direction
        //else choose new viable direction
        this.prevDirection = this.direction;
        if(surrounding_blocks[this.direction] > 1){ // blockage found
            ArrayList<Integer> viableDir = new ArrayList<>();
            for(int i = 0; i<4; i++){ //check for viable directions. there will always be at least one.
                if(surrounding_blocks[i] <= 1){
                    viableDir.add(i);
                }
            }
            //change to a new random direction
            if(viableDir.size() == 1){
                direction = viableDir.get(0);
            }else {
                direction = viableDir.get(ThreadLocalRandom.current().nextInt(0, viableDir.size() ));
                Set UPDOWN = new HashSet<>(Arrays.asList(0,1));
                Set LEFTRIGHT = new HashSet<>(Arrays.asList(2,3));
                if (UPDOWN.contains(direction) && UPDOWN.contains(prevDirection) || //reduce "bouncing" - repeating directions
                        LEFTRIGHT.contains(direction) && LEFTRIGHT.contains(prevDirection)){
                    direction = viableDir.get(ThreadLocalRandom.current().nextInt(0, viableDir.size() ));
                }
            }
        }
        // Update the ghost's position based on the current direction
        switch (direction) {
            case 0: // Up
                y-=speed;
                break;
            case 1: // Down
                y+=speed;
                break;
            case 2: // Left
                x-=speed;
                break;
            case 3: // Right
                x+=speed;
                break;
        }
    }
    public Point getPosition() {
        return new Point(x,y);
    }
}
