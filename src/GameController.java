import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class GameController extends KeyAdapter {

    Pacman pacman;
    int inc = 1;
    int inc2 = 2;
    public GameController(Pacman pacman){
        this.pacman = pacman;
    }
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode()== KeyEvent.VK_UP && !pacman.isWallThere(pacman.UP))
            this.pacman.changeDir(Pacman.UP);
        if (e.getKeyCode()== KeyEvent.VK_DOWN && !pacman.isWallThere(pacman.DOWN))
            this.pacman.changeDir(Pacman.DOWN);
        if (e.getKeyCode()== KeyEvent.VK_LEFT && !pacman.isWallThere(pacman.LEFT))
            this.pacman.changeDir(Pacman.LEFT );
        if (e.getKeyCode()== KeyEvent.VK_RIGHT && !pacman.isWallThere(pacman.RIGHT))
            this.pacman.changeDir(Pacman.RIGHT);
    }
}