import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

import java.awt.BorderLayout;
import java.io.InputStream;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * A panel maintaining a picture of a Do Not Enter sign.
 */
public class Main extends JPanel {
    private static final long serialVersionUID = 7148504528835036003L;
    public static void main(String[] args) {
        var resourceName = "map.json";
        InputStream is = Main.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }
        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        JSONArray map = object.getJSONArray("map");
        int height = map.length()*20 + 65;
        int width = ((JSONArray) map.get(0)).length()*20 + 5;
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame("A simple graphics program");
            frame.setSize(width, height);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            var pacman = new Pacman(convertMapToArray(map));
            frame.getContentPane().add(pacman, BorderLayout.CENTER);
            frame.addKeyListener(new GameController(pacman));
            frame.setVisible(true);
        });
    }

   public static int[][] convertMapToArray(JSONArray jsonMap){
        int[][] map = new int[jsonMap.length()][((JSONArray)jsonMap.get(0)).length()];
        for(int i=0;i<jsonMap.length();i++){
            for(int j = 0; j<((JSONArray)jsonMap.get(0)).length() ; j++)
            map[i][j] =(Integer)((JSONArray)jsonMap.get(i)).get(j);
        }
        return map;
    }
}