import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class MovePacman extends KeyAdapter {

    Pacman pacman;
    int inc = 4;
    public MovePacman(Pacman pacman){
        this.pacman = pacman;
    }
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode()== KeyEvent.VK_UP)
            this.pacman.changeY(-inc);
        if (e.getKeyCode()== KeyEvent.VK_DOWN)
            this.pacman.changeY(inc);
        if (e.getKeyCode()== KeyEvent.VK_LEFT)
            this.pacman.changeX(-inc);
        if (e.getKeyCode()== KeyEvent.VK_RIGHT)
            this.pacman.changeX(inc);
    }
}