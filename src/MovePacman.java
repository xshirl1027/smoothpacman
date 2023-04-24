import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class MovePacman extends KeyAdapter {

    Pacman pacman;
    int inc = 1;
    public MovePacman(Pacman pacman){
        this.pacman = pacman;
    }
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode()== KeyEvent.VK_UP)
            this.pacman.changeDir(0,-inc);
        if (e.getKeyCode()== KeyEvent.VK_DOWN)
            this.pacman.changeDir(0, inc);
        if (e.getKeyCode()== KeyEvent.VK_LEFT)
            this.pacman.changeDir(-inc,0);
        if (e.getKeyCode()== KeyEvent.VK_RIGHT)
            this.pacman.changeDir(inc, 0);
    }
}