import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A panel maintaining a picture of a Do Not Enter sign.
 */
public class Main extends JPanel {
    private static final long serialVersionUID = 7148504528835036003L;

    /**
     * Called by the runtime system whenever the panel needs painting.
     */


    /**
     * A little driver in case you want a stand-alone application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var pacman = new Pacman(50,50);
            pacman.setBackground(Color.DARK_GRAY.darker());
            var frame = new JFrame("A simple graphics program");
            frame.setSize(600, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(pacman, BorderLayout.CENTER);
            frame.addKeyListener(new MovePacman(pacman));
            frame.setVisible(true);
        });
    }
}