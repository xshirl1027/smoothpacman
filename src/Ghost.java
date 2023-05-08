import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Ghost {
    private int x;
    private int y;
    private int direction;
    private Color color;

    private int[] surrounding_blocks; //ex. in the order of up down left right [0,0,0,0] or [1,1,0,0]
    public Ghost(int x, int y, Color color, int[] surrounding_blocks) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.direction = 0;
        this.surrounding_blocks = surrounding_blocks;
    }

    public void draw(Graphics2D g2d){
        int size = 20;
        // Body
        g2d.setColor(this.color);
        g2d.fillArc(x, y, size, size, 0, 180);
        g2d.fillRect(x, y + size/2, size, size/2);
        // eyes
        int eyeSize = size / 3;
        int eyeOffset = size / 5;
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x + eyeOffset/2, y + eyeOffset, eyeSize, eyeSize);
        g2d.fillOval(x + size - eyeOffset/2 - eyeSize, y + eyeOffset, eyeSize, eyeSize);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x + eyeOffset/2 + eyeSize / 4, y + eyeOffset + eyeSize / 4, eyeSize / 2, eyeSize / 2);
        g2d.fillOval(x + size - eyeOffset/2 - eyeSize + eyeSize / 4, y + eyeOffset + eyeSize / 4, eyeSize / 2, eyeSize / 2);
    }
    public void move(int[] surrounding_blocks) {
        this.surrounding_blocks = surrounding_blocks;
        //if there is no blockage in the block in the direction of movement, move ghost by one block in that direction
        //else choose new viable direction
        if(surrounding_blocks[direction] > 1){ // blockage found
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
                direction = viableDir.get(ThreadLocalRandom.current().nextInt(0, viableDir.size() + 1));
            }
        }
        // Update the ghost's position based on the current direction
        switch (direction) {
            case 0: // Up
                y--;
                break;
            case 1: // Down
                y++;
                break;
            case 2: // Left
                x--;
                break;
            case 3: // Right
                x++;
                break;
        }
    }

}
