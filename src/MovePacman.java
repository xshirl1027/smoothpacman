import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class MovePacman extends KeyAdapter {

    Pacman pacman;
    int inc = 2;
    int inc2 = 2;
    public MovePacman(Pacman pacman){
        this.pacman = pacman;
    }
    public void keyPressed(KeyEvent e)
    {
        var pi = 180;
        if (e.getKeyCode()== KeyEvent.VK_UP && !this.pacman.reachedWall_y(-inc2))
            this.pacman.changeDir(0,-inc, this.pacman.init_start_angle +pi/2);
        if (e.getKeyCode()== KeyEvent.VK_DOWN && !this.pacman.reachedWall_y(inc2))
            this.pacman.changeDir(0, inc, this.pacman.init_start_angle +pi+pi/2);
        if (e.getKeyCode()== KeyEvent.VK_LEFT && !this.pacman.reachedWall_x(-inc2))
            this.pacman.changeDir(-inc,0, this.pacman.init_start_angle +pi);
        if (e.getKeyCode()== KeyEvent.VK_RIGHT && !this.pacman.reachedWall_x(inc2))
            this.pacman.changeDir(inc, 0, this.pacman.init_start_angle);
    }
}